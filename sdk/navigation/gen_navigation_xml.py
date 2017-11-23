#!/usr/bin/env python

################################################################################
class Settings:
    @staticmethod
    def __ColumnToIndex(columnName):
        if None == columnName: return None
        columnIndex = 0
        for i in columnName.strip().upper():
            if 'A' > i or 'Z' < i:
                print 'Invalid column name: %s' % columnName
                return None
            columnIndex = (ord(i) - ord('A') + 1) + 26 * columnIndex
        assert columnIndex >= 1
        return columnIndex - 1

    def __init__(self, settings):
        self._settings = {}
        for setting in settings:
            assert len(setting) > 0
            assert len(setting) <= 2
            if 2 <= len(setting):
                self._settings[setting[0]] = setting[1]
            else:
                self._settings[setting[0]] = None
        valid = self._sanityCheck()
        if not valid:
            print '[ERROR] Failed in parsing setting: %s' % str(self)
        assert valid
    def __str__(self):
        return str(self._settings)
    def __len__(self):
        return len(self._settings)

    def _sanityCheck(self):
        try:
            if not (type(self.getSheetName()) is unicode): return False
            if not (type(self.getTreeDepth()) is int): return False
            if not (type(self.getItemTypeIndex()) is int): return False
            if not (type(self.getItemSituationViewNameIndex()) is int): return False
            if not (type(self.getItemUiViewNameIndex()) is int): return False
            if not (type(self.getItemUiScreenNameIndex()) is int): return False
            if not (type(self.getItemUiCtrlNameIndex()) is int): return False
            if not (type(self.getItemUiOptsNameIndex()) is int): return False
            for level in xrange(self.getTreeDepth()):
                if not (type(self.getItemTextIndex(level)) is int): return False
        except:
            return False
        return True

    def getSheetName(self):
        return self._settings['NAVIGATION_SHEET']
    def getTreeDepth(self):
        return int(self._settings['NAVIGATION_TREE_DEPTH_MAX'])
    def getItemTextColumn(self, level = 0):
        return self._settings['NAVIGATION_ITEM_TEXT_COLUMN_LEVEL_%d' % level]
    def getItemTitleColumn(self):
        return self._settings['NAVIGATION_ITEM_TITLE_COLUMN']
    def getItemTooltipColumn(self):
        return self._settings['NAVIGATION_ITEM_TOOLTIP_COLUMN']
    def getItemCSSColumn(self):
        return self._settings['NAVIGATION_ITEM_CSS_COLUMN']
    def getItemTypeColumn(self):
        return self._settings['NAVIGATION_ITEM_TYPE_COLUMN']
    def getItemSituationViewNameColumn(self):
        return self._settings['NAVIGATION_ITEM_UISVID_NAME_COLUMN']
    def getItemUiViewNameColumn(self):
        return self._settings['NAVIGATION_ITEM_UIVIEW_NAME_COLUMN']
    def getItemUiScreenNameColumn(self):
        return self._settings['NAVIGATION_ITEM_UISCREEN_COLUMN']
    def getItemUiCtrlNameColumn(self):
        return self._settings['NAVIGATION_ITEM_UICTRL_NAME_COLUMN']
    def getItemUiOptsColumn(self):
        return self._settings['NAVIGATION_ITEM_UIOPTS_NAME_COLUMN']
    def getItemSkipColumn(self):
        return self._settings['NAVIGATION_ITEM_SKIP_COLUMN']
    def getItemOPMColumn(self):
        return self._settings['NAVIGATION_ITEM_OPM_COLUMN']
    def getItemOPMOperationColumn(self):
        return self._settings['NAVIGATION_ITEM_OPM_OPERATION_COLUMN']

    def getItemTextIndex(self, level = 0):
        return Settings.__ColumnToIndex(self.getItemTextColumn(level))
    def getItemTitleIndex(self):
        if None != self.getItemTitleColumn():
            return Settings.__ColumnToIndex(self.getItemTitleColumn())
        else:
            return None
    def getItemTooltipIndex(self):
        if None != self.getItemTooltipColumn():
            return Settings.__ColumnToIndex(self.getItemTooltipColumn())
        else:
            return None
    def getItemCSSIndex(self):
        if None != self.getItemCSSColumn():
            return Settings.__ColumnToIndex(self.getItemCSSColumn())
        else:
            return None
    def getItemTypeIndex(self):
        return Settings.__ColumnToIndex(self.getItemTypeColumn())
    def getItemSituationViewNameIndex(self):
        return Settings.__ColumnToIndex(self.getItemSituationViewNameColumn())
    def getItemUiViewNameIndex(self):
        return Settings.__ColumnToIndex(self.getItemUiViewNameColumn())
    def getItemUiScreenNameIndex(self):
        return Settings.__ColumnToIndex(self.getItemUiScreenNameColumn())
    def getItemUiCtrlNameIndex(self):
        return Settings.__ColumnToIndex(self.getItemUiCtrlNameColumn())
    def getItemUiOptsNameIndex(self):
        return Settings.__ColumnToIndex(self.getItemUiOptsColumn())
    def getItemSkipIndex(self):
        if None != self.getItemSkipColumn():
            return Settings.__ColumnToIndex(self.getItemSkipColumn())
        else:
            return None
    def getItemOPMIndex(self):
        if None != self.getItemOPMColumn():
            return Settings.__ColumnToIndex(self.getItemOPMColumn())
        else:
            return None
    def getItemOPMOperationIndex(self):
        if None != self.getItemOPMOperationColumn():
            return Settings.__ColumnToIndex(self.getItemOPMOperationColumn())
        else:
            return None

################################################################################
class NodeData:
    @staticmethod
    def __GetRowContent(row, index):
        if None == index: return None
        if len(row) <= index: return None
        content = row[index]
        if type(content) is float:
            content = u'%.0f' % content
        elif not type(content) is unicode:
            return None
        else:
            content = content.strip()
            if 0 >= len(content): return None
        return content

    @staticmethod
    def __GetRowText(row, settings):
        depth = settings.getTreeDepth()
        for level in xrange(depth - 1, -1, -1):
            index = settings.getItemTextIndex(level)
            text = NodeData.__GetRowContent(row, index)
            if text: return text
        return None

    @staticmethod
    def __GetRowParent(row, settings):
        key = NodeData.__GetRowKey(row, settings)
        if not key: return None
        index = key.rfind(u'|')
        assert 0 != index
        if 0 >= index:
            return u'root'
        else:
            return key[0:index]

    @staticmethod
    def __GetRowKey(row, settings):
        depth = settings.getTreeDepth()
        upperNodes = []
        for level in xrange(depth):
            index = settings.getItemTextIndex(level)
            text = NodeData.__GetRowContent(row, index)
            if text:
                upperNodes.append(text)
            else:
                break
        if 0 < len(upperNodes):
            return reduce(lambda x, y: x + u'|' + y, upperNodes)
        else:
            return None

    def __init__(self, rowId, row, settings):
        self._data = {}
        self._data['ROWID'] = rowId
        self._data['KEY'] = NodeData.__GetRowKey(row, settings)
        self._data['PARENT'] = NodeData.__GetRowParent(row, settings)
        self._data['TEXT'] = NodeData.__GetRowText(row, settings)
        self._data['TITLE'] = NodeData.__GetRowContent(row, settings.getItemTitleIndex())
        self._data['TOOLTIP'] = NodeData.__GetRowContent(row, settings.getItemTooltipIndex())
        self._data['TYPE'] = NodeData.__GetRowContent(row, settings.getItemTypeIndex())
        self._data['CSS'] = NodeData.__GetRowContent(row, settings.getItemCSSIndex())
        self._data['SITUATIONVIEWNAME'] = NodeData.__GetRowContent(row, settings.getItemSituationViewNameIndex())
        self._data['UIVIEWNAME'] = NodeData.__GetRowContent(row, settings.getItemUiViewNameIndex())
        self._data['UISCREEN'] = NodeData.__GetRowContent(row, settings.getItemUiScreenNameIndex())
        self._data['UICTRL'] = NodeData.__GetRowContent(row, settings.getItemUiCtrlNameIndex())
        self._data['UIOPTS'] = NodeData.__GetRowContent(row, settings.getItemUiOptsNameIndex())
        self._data['SKIP'] = NodeData.__GetRowContent(row, settings.getItemSkipIndex())
        self._data['OPM'] = NodeData.__GetRowContent(row, settings.getItemOPMIndex())
        self._data['OPMOPERATION'] = NodeData.__GetRowContent(row, settings.getItemOPMOperationIndex())

        valid = self._sanityCheck()
        if not valid:
            print '[ERROR] Failed in parsing row %d: %s' % (rowId, str(self))
        assert valid
    def __str__(self):
        return str(self._data)
    def __len__(self):
        return len(self._data)

    def _sanityCheck(self):
        if not ((None == self.getSkip()) or (0 < len(self.getSkip()))):
            print '[ERROR] Invalid Skip Column'
            return False
        if self.getSkip(): return True # this row is skipped, no need to check further

        if not ((type(self.getKey()) is unicode) and (0 < len(self.getKey()))):
            print '[ERROR] Invalid Key'
            return False
        if not ((type(self.getParent()) is unicode) and (0 < len(self.getParent()))):
            print '[ERROR] Invalid Parent'
            return False
        if not ((type(self.getText()) is unicode) and (0 < len(self.getText()))):
            print '[ERROR] Invalid Text'
            return False
        if not (0 > self.getText().find('|')):
            print '[ERROR] Invalid Text (Invalid Character?)'
            return False
        if not ((None == self.getTitle()) or (0 < len(self.getTitle()))):
            print '[ERROR] Invalid Title'
            return False
        if not ((None == self.getTooltip()) or (0 < len(self.getTooltip()))):
            print '[ERROR] Invalid Tooltips'
            return False
        if not ((type(self.getType()) is unicode) and (0 < len(self.getType()))):
            print '[ERROR] Invalid Type'
            return False
        if not (('M' == self.getType()) or ('S' == self.getType()) or ('P' == self.getType())):
            print '[ERROR] Invalid Type (not M/S/P)'
            return False
        if 'M' == self.getType():
            if not ((None == self.getSituationViewName()) or (0 >= len(self.getSituationViewName()))):
                print '[ERROR][M] Non empty Situation View'
                return False
            if not ((None == self.getUiViewName()) or (0 >= len(self.getUiViewName()))):
                print '[ERROR][M] Non empty View Name'
                return False
            if not ((None == self.getUiCtrlName()) or (0 >= len(self.getUiCtrlName()))):
                print '[ERROR][M] Non empty Ctrl Name'
                return False
            if not ((None == self.getUiOptsName()) or (0 >= len(self.getUiOptsName()))):
                print '[ERROR][M] Non empty Opts Name'
                return False
        elif 'S' == self.getType():
            if ((None == self.getSituationViewName()) or (0 >= len(self.getSituationViewName()))):
                print '[ERROR][S] Empty Situation View'
                return False
            if not ((None == self.getUiViewName()) or (0 >= len(self.getUiViewName()))):
                print '[ERROR][S] Non empty View Name'
                return False
            if ((None == self.getUiCtrlName()) or (0 >= len(self.getUiCtrlName()))):
                print '[ERROR][S] Empty Ctrl Name'
                return False
            if not ((None == self.getUiOptsName()) or (0 >= len(self.getUiOptsName()))):
                print '[ERROR][S] Non empty Opts Name'
                return False
        elif 'P' == self.getType():
            if not ((None == self.getSituationViewName()) or (0 >= len(self.getSituationViewName()))):
                print '[ERROR][P] Non empty Situation View'
                return False
            if ((None == self.getUiViewName()) or (0 >= len(self.getUiViewName()))):
                print '[ERROR][P] Empty View Name'
                return False
            if ((None == self.getUiCtrlName()) or (0 >= len(self.getUiCtrlName()))):
                print '[ERROR][P] Empty Ctrl Name'
                return False
        if ((None == self.getUiScreenName()) or (0 >= len(self.getUiScreenName()))):
            print '[ERROR] Empty Screen Name'
            return False
        if not ((None == self.getCSS()) or (0 < len(self.getCSS()))):
            print '[ERROR] Empty CSS'
            return False
        if not ((None == self.getOPM()) or (0 < len(self.getOPM()))):
            print '[ERROR] Empty OPM'
            return False
        if not ((None == self.getOPMOperation()) or (0 < len(self.getOPMOperation()))):
            print '[ERROR] Empty OPM Operation'
            return False
        return True

    def getRowId(self):
        return self._data['ROWID']
    def getKey(self):
        return self._data['KEY']
    def getParent(self):
        return self._data['PARENT']
    def getText(self):
        return self._data['TEXT']
    def getTitle(self):
        return self._data['TITLE']
    def getTooltip(self):
        return self._data['TOOLTIP']
    def getType(self):
        return self._data['TYPE']
    def getCSS(self):
        return self._data['CSS']
    def getSituationViewName(self):
        return self._data['SITUATIONVIEWNAME']
    def getUiViewName(self):
        return self._data['UIVIEWNAME']
    def getUiScreenName(self):
        return self._data['UISCREEN']
    def getUiCtrlName(self):
        return self._data['UICTRL']
    def getUiOptsName(self):
        return self._data['UIOPTS']
    def getSkip(self):
        return self._data['SKIP']
    def getOPM(self):
        return self._data['OPM']
    def getOPMOperation(self):
        return self._data['OPMOPERATION']

    def setSettingId(self, id):
        self._data['SETTINGID'] = id
    def getSettingId(self):
        return self._data['SETTINGID']

################################################################################
def escapeText(text):
    import cgi
    return cgi.escape(text)

################################################################################
def readXLS(xls, sheetName):
    import mmap
    import xlrd
    fileContent = mmap.mmap(xls.fileno(), 0, access = mmap.ACCESS_READ)
    wb = xlrd.open_workbook(file_contents = fileContent)
    if not wb: return None

    ws = wb.sheet_by_name(sheetName)
    if not ws: return None

    rows = [
        [item.value for item in row]
        for row in ws.get_rows()
    ]
    return rows

################################################################################
def readSetting(xls, sheetName):
    # raw settings
    rows = readXLS(xls, sheetName)

    # remove comments and empty column
    rows = [
        [item for item in row if not ((type(item) is unicode or type(item) is str) and (len(item) <= 0 or item.startswith('#')))]
        for row in rows
    ]

    # remove empty rows
    rows = [row for row in rows if len(row) > 0]
    #print rows
    return Settings(rows)

################################################################################
def constructNavigationtree(rows, settings):
    import treelib
    navTree = treelib.Tree()
    rootNode = navTree.create_node(tag = 'root', identifier = 'root')
    for i in xrange(len(rows)):
        row = rows[i]
        rowData = NodeData(i + 1, row, settings)
        if rowData.getSkip(): continue
        #print rowData.getText()
        #print rowData.getKey()
        navNode = navTree.create_node(tag = rowData.getText(), identifier = rowData.getKey(), parent = rowData.getParent(), data = rowData)
    return navTree

################################################################################
def generateConfigurationMapping(navTree, startNode = 'root', settingIdStart = 0):
    mapping = []
    settingId = settingIdStart
    children = navTree.children(startNode)
    for child in children:
        child.data.setSettingId(settingId)
        mapping.append((child, settingId))
        settingId = settingId + 1

        childMapping = generateConfigurationMapping(navTree, child.identifier, settingId)
        mapping.extend(childMapping)
        settingId = settingId + len(childMapping)
    return mapping

def generateConfigurationMappingFile(mapping, filename):
    with open(filename, 'wb') as f:
        f.write('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>\n<data-set xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">\n')
        for item in mapping:
            itemData = item[0].data
            key = item[1]
            f.write('\t<option key="%s"><setting>%d</setting></option>\n' % (escapeText(itemData.getKey()), key))
        f.write('</data-set>\n')
    return True

def generateConfigurationSettingFile(mapping, filename):
    UiPath = ':UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout'
    with open(filename, 'wb') as f:
        f.write('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>\n<data-set xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">\n\t<option key="M"><type>M</type></option>\n')
        for item in mapping:
            itemData = item[0].data
            key = item[1]
            f.write('\t<option key="%d">\n' % key)
            f.write('\t\t<type>%s</type>\n' % escapeText(itemData.getType()))
            f.write('\t\t<name>%s</name>\n' % escapeText(itemData.getText()))
            f.write('\t\t<uiPath>%s</uiPath>\n' % escapeText(UiPath))
            if itemData.getTitle():
                f.write('\t\t<title>%s</title>\n' % escapeText(itemData.getTitle()))
            if itemData.getTooltip():
                f.write('\t\t<tooltips>%s</tooltips>\n' % escapeText(itemData.getTooltip()))
            if itemData.getUiScreenName():
                f.write('\t\t<uiScreen>%s</uiScreen>\n' % escapeText(itemData.getUiScreenName()))
            if itemData.getUiCtrlName():
                if itemData.getSituationViewName():
                    f.write('\t\t<uiConf>uiCtrl=%s&amp;uiSvId=%s</uiConf>\n' % (escapeText(itemData.getUiCtrlName()), escapeText(itemData.getSituationViewName())))
                elif itemData.getUiViewName() and itemData.getUiOptsName():
                    f.write('\t\t<uiConf>uiCtrl=%s&amp;uiView=%s&amp;uiOpts=%s</uiConf>\n' % (escapeText(itemData.getUiCtrlName()), escapeText(itemData.getUiViewName()), escapeText(itemData.getUiOptsName())))
                elif itemData.getUiViewName():
                    f.write('\t\t<uiConf>uiCtrl=%s&amp;uiView=%s</uiConf>\n' % (escapeText(itemData.getUiCtrlName()), escapeText(itemData.getUiViewName())))
            if itemData.getCSS():
                f.write('\t\t<css>%s</css>\n' % escapeText(itemData.getCSS()))
            if itemData.getOPM():
                f.write('\t\t<opm>%s</opm>\n' % escapeText(itemData.getOPM()))
            if itemData.getOPMOperation():
                f.write('\t\t<opmOperation>%s</opmOperation>\n' % escapeText(itemData.getOPMOperation()))
            f.write('\t</option>\n')

        f.write('</data-set>\n')
    return True

def generateConfiguration(navTree):
    settingMapping = generateConfigurationMapping(navTree)
    #print settingMapping
    if not settingMapping:
        print '[ERROR] Failed in generating mapping'
        return False
    result = generateConfigurationMappingFile(settingMapping, 'navigationMapping.xml')
    if not result:
        print '[ERROR] Failed in generating mapping file'
        return False

    result = generateConfigurationSettingFile(settingMapping, 'navigationSetting.xml')
    if not result:
        print '[ERROR] Failed in generating setting file'
        return False

    return True

################################################################################
def main():
    import argparse
    ap = argparse.ArgumentParser(
        formatter_class=argparse.ArgumentDefaultsHelpFormatter,
        description='Convert Navigation XLS to navigationMapping.xml & navigationSetting.xml',
    )
    ap.add_argument('-i', '--input', type=argparse.FileType('rb'), required=True, help='Input Excel file (.xls), must contain a sheet called "Navigation"')
    ap.add_argument('-t', '--tree', action='store_true', help='Display the generated navigation tree')
    ap.add_argument('-s', '--setting', default='settings', help='Name of sheet containing the setting parameters')
    ns = ap.parse_args()

    settings = None
    rows = []
    xls = ns.input
    with xls:
        xls.seek(0)
        settings = readSetting(xls, ns.setting)
        #print settings
        if not settings:
            print '[ERROR] Cannot parse the input XLS for setting: %s' % ns.input.name
            return -1
        xls.seek(0)
        rows = readXLS(xls, settings.getSheetName())
        #print rows
        if not rows:
            print '[ERROR] Cannot parse the input XLS: %s' % ns.input.name
            return -1
    assert settings
    assert rows

    navTree = constructNavigationtree(rows, settings)
    if ns.tree: navTree.show()
    if not navTree:
        print '[ERORR] Cannot construct navigation tree from the input XLS: %s' % ns.input.name
        return -1

    result = generateConfiguration(navTree)
    if not result:
        print '[ERORR] Failed in generating navigation configuration files: %s' % ns.input.name
        return -1

    return 0

################################################################################k
if __name__ == '__main__':
    import sys
    retCode = main()
    sys.exit(retCode)
