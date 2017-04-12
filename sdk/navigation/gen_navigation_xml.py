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
    # def getItemGeoCatColumn(self):
    #     return self._settings['NAVIGATION_ITEM_GEOCAT_COLUMN']
    # def getItemFunCatColumn(self):
    #     return self._settings['NAVIGATION_ITEM_FUNCAT_COLUMN']
    # def getItemModeCatColumn(self):
    #     return self._settings['NAVIGATION_ITEM_MODECAT_COLUMN']
    # def getItemActionCatColumn(self):
    #     return self._settings['NAVIGATION_ITEM_ACTIONCAT_COLUMN']
    def getItemOPMColumn(self):
        return self._settings['NAVIGATION_ITEM_OPM_COLUMN']
    def getItemOPMOperationColumn(self):
        return self._settings['NAVIGATION_ITEM_OPM_OPERATION_COLUMN']
    def getItemTypeColumn(self):
        return self._settings['NAVIGATION_ITEM_TYPE_COLUMN']
    def getItemSituationViewNameColumn(self):
        return self._settings['NAVIGATION_ITEM_SITUATIONVIEW_NAME_COLUMN']
    def getItemUiViewNameColumn(self):
        return self._settings['NAVIGATION_ITEM_UIVIEW_NAME_COLUMN']
    def getItemCSSColumn(self):
        return self._settings['NAVIGATION_ITEM_CSS_COLUMN']
    def getItemSkipColumn(self):
        return self._settings['NAVIGATION_ITEM_SKIP_COLUMN']

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
    # def getItemGeoCatIndex(self):
    #     if None != self.getItemGeoCatColumn():
    #         return Settings.__ColumnToIndex(self.getItemGeoCatColumn())
    #     else:
    #         return None
    # def getItemFunCatIndex(self):
    #     if None != self.getItemFunCatColumn():
    #         return Settings.__ColumnToIndex(self.getItemFunCatColumn())
    #     else:
    #         return None
    # def getItemModeCatIndex(self):
    #     if None != self.getItemModeCatColumn():
    #         return Settings.__ColumnToIndex(self.getItemModeCatColumn())
    #     else:
    #         return None
    # def getItemActionCatIndex(self):
    #     if None != self.getItemActionCatColumn():
    #         return Settings.__ColumnToIndex(self.getItemActionCatColumn())
    #     else:
    #         return None
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
    def getItemTypeIndex(self):
        return Settings.__ColumnToIndex(self.getItemTypeColumn())
    def getItemSituationViewNameIndex(self):
        return Settings.__ColumnToIndex(self.getItemSituationViewNameColumn())
    def getItemUiViewNameIndex(self):
        return Settings.__ColumnToIndex(self.getItemUiViewNameColumn())
    def getItemCSSIndex(self):
        if None != self.getItemCSSColumn():
            return Settings.__ColumnToIndex(self.getItemCSSColumn())
        else:
            return None
    def getItemSkipIndex(self):
        if None != self.getItemSkipColumn():
            return Settings.__ColumnToIndex(self.getItemSkipColumn())
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
        # self._data['GEOCAT'] = NodeData.__GetRowContent(row, settings.getItemGeoCatIndex())
        # self._data['FUNCAT'] = NodeData.__GetRowContent(row, settings.getItemFunCatIndex())
        # self._data['MODECAT'] = NodeData.__GetRowContent(row, settings.getItemModeCatIndex())
        # self._data['ACTIONCAT'] = NodeData.__GetRowContent(row, settings.getItemActionCatIndex())
        self._data['OPM'] = NodeData.__GetRowContent(row, settings.getItemOPMIndex())
        self._data['OPMOPERATION'] = NodeData.__GetRowContent(row, settings.getItemOPMOperationIndex())
        self._data['TYPE'] = NodeData.__GetRowContent(row, settings.getItemTypeIndex())
        self._data['SITUATIONVIEWNAME'] = NodeData.__GetRowContent(row, settings.getItemSituationViewNameIndex())
        self._data['UIVIEWNAME'] = NodeData.__GetRowContent(row, settings.getItemUiViewNameIndex())
        self._data['CSS'] = NodeData.__GetRowContent(row, settings.getItemCSSIndex())
        self._data['SKIP'] = NodeData.__GetRowContent(row, settings.getItemSkipIndex())

        valid = self._sanityCheck()
        if not valid:
            print '[ERROR] Failed in parsing row %d: %s' % (rowId, str(self))
        assert valid
    def __str__(self):
        return str(self._data)
    def __len__(self):
        return len(self._data)

    def _sanityCheck(self):
        if not ((None == self.getSkip()) or (0 < len(self.getSkip()))): return False
        if self.getSkip(): return True # this row is skipped, no need to check further

        if not ((type(self.getKey()) is unicode) and (0 < len(self.getKey()))): return False
        if not ((type(self.getParent()) is unicode) and (0 < len(self.getParent()))): return False
        if not ((type(self.getText()) is unicode) and (0 < len(self.getText()))): return False
        if not (0 > self.getText().find('|')): return False
        if not ((None == self.getTitle()) or (0 < len(self.getTitle()))): return False
        if not ((None == self.getTooltip()) or (0 < len(self.getTooltip()))): return False
        # if not ((None == self.getGeoCat()) or (0 < len(self.getGeoCat()))): return False
        # if not ((None == self.getFunCat()) or (0 < len(self.getFunCat()))): return False
        # if not ((None == self.getModeCat()) or (0 < len(self.getModeCat()))): return False
        # if not ((None == self.getActionCat()) or (0 < len(self.getActionCat()))): return False
        if not ((None == self.getOPM()) or (0 < len(self.getOPM()))): return False
        if not ((None == self.getOPMOperation()) or (0 < len(self.getOPMOperation()))): return False
        if not ((type(self.getType()) is unicode) and (0 < len(self.getType()))): return False
        if not (('M' == self.getType()) or ('S' == self.getType()) or ('P' == self.getType())): return False
        if 'M' == self.getType():
            if not ((None == self.getSituationViewName()) or (0 >= len(self.getSituationViewName()))): return False
            if not ((None == self.getUiViewName()) or (0 >= len(self.getUiViewName()))): return False
        elif 'S' == self.getType():
            if not ((None == self.getUiViewName()) or (0 >= len(self.getUiViewName()))): return False
        if not ((None == self.getCSS()) or (0 < len(self.getCSS()))): return False
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
    # def getGeoCat(self):
    #     return self._data['GEOCAT']
    # def getFunCat(self):
    #     return self._data['FUNCAT']
    # def getModeCat(self):
    #     return self._data['MODECAT']
    # def getActionCat(self):
    #     return self._data['ACTIONCAT']
    def getOPM(self):
        return self._data['OPM']
    def getOPMOperation(self):
        return self._data['OPMOPERATION']
    def getType(self):
        return self._data['TYPE']
    def getSituationViewName(self):
        return self._data['SITUATIONVIEWNAME']
    def getUiViewName(self):
        return self._data['UIVIEWNAME']
    def getCSS(self):
        return self._data['CSS']
    def getSkip(self):
        return self._data['SKIP']

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
            f.write('\t<option key="%d">\n\t\t<type>%s</type>\n\t\t<name>%s</name>\n' % (key, itemData.getType(), escapeText(itemData.getText())))
            if itemData.getTitle():
                f.write('\t\t<title>%s</title>\n' % escapeText(itemData.getTitle()))
            if itemData.getSituationViewName():
                f.write('\t\t<uiPanel>%s</uiPanel>\n' % escapeText(itemData.getSituationViewName()))
            f.write('\t\t<uiScreen>0</uiScreen>\n')
            f.write('\t\t<uiPath>%s</uiPath>\n' % UiPath)
            if itemData.getCSS():
                f.write('\t\t<css>%s</css>\n' % escapeText(itemData.getCSS()))
            # if itemData.getGeoCat():
            #     f.write('\t\t<locCat>%s</locCat>\n' % escapeText(itemData.getGeoCat()))
            # if itemData.getFunCat():
            #     f.write('\t\t<funCat>%s</funCat>\n' % escapeText(itemData.getFunCat()))
            # if itemData.getActionCat():
            #     f.write('\t\t<actionCat>%s</actionCat>\n' % escapeText(itemData.getActionCat()))
            # if itemData.getModeCat():
            #     f.write('\t\t<modeCat>%s</modeCat>\n' % escapeText(itemData.getModeCat()))
            if itemData.getOPM():
                f.write('\t\t<opm>%s</opm>\n' % escapeText(itemData.getOPM()))
            if itemData.getOPMOperation():
                f.write('\t\t<opmOperation>%s</opmOperation>\n' % escapeText(itemData.getOPMOperation()))
            if itemData.getUiViewName():
                f.write('\t\t<uiView>%s</uiView>\n' % escapeText(itemData.getUiViewName()))
            if itemData.getTooltip():
                f.write('\t\t<tooltips>%s</tooltips>\n' % escapeText(itemData.getTooltip()))
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
        formatter_class=argparse.RawDescriptionHelpFormatter,
        description='Convert Navigation XLS to navigationMapping.xml & navigationSetting.xml',
    )
    ap.add_argument('-i', '--input', type=argparse.FileType('rb'), required=True, help='Input Excel file (.xls), must contain a sheet called "Navigation"')
    ap.add_argument('-g', '--graph', action='store_true', help='Generate png output')
    ns = ap.parse_args()

    settings = None
    rows = []
    xls = ns.input
    with xls:
        xls.seek(0)
        settings = readSetting(xls, 'settings')
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
    navTree.show()
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
