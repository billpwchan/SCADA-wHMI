#!/usr/bin/python

################################################################################
def getDocHash():
	import os
	import hashlib
	import inspect

	filePath = os.path.abspath(inspect.getsourcefile(lambda :0))

	return hashlib.md5(open(filePath, 'rb').read()).hexdigest()
	
################################################################################
class HvId:
    def __init__(self, id):
        self._id = id
        self._area = None
        self._connector = None

    def getId(self): return self._id

    def setArea(self, area): self._area = area
    def getArea(self): return self._area

    def setConnector(self, connector): self._connector = connector
    def getConnector(self): return self._connector

    def __str__(self): return self._id
    def __len__(self): return len(self._id)

class EntityPerSubsystemAllocation:
    def __init__(self, sysConfPath):
        import os

        entitiesFileName = os.path.join(sysConfPath, 'mapping', 'entitiesPerSubsystemAllocation.xml')
        result = self._parseEntitiesPerSubsystemAllocation(entitiesFileName)
        if not result:
            raise Exception('Failed in extracting connector to hvid map from the file: %s' % entitiesFileName)

        entitiesFileName = os.path.join(sysConfPath, 'mapping', 'entitiesPerAreaAllocation.xml')
        result = self._parseEntitiesPerAreaAllocation(entitiesFileName)
        if not result:
            raise Exception('Failed in extracting area to hvid map from the file: %s' % entitiesFileName)

    def __len__(self): return len(self._connectorToHVIDMap)

    def _parseEntitiesPerSubsystemAllocation(self, entitiesFile):
        import re

        originalContent = None
        with open(entitiesFile, 'r') as f: originalContent = f.read()
#        print originalContent
        if not originalContent:
            print '[ERROR] No content in file: %s' % entitiesFile
            return False

        # extract connector blocks
        connectersPattern = r'.*?(^\s*<p:entry.*</p:entry>)'
        connectors = re.match(connectersPattern, originalContent, re.MULTILINE | re.DOTALL)
        if (not connectors) or len(connectors.groups()) <= 0:
            print '[ERROR] Failed in extracting connectors from the file'
            return False
        connectorsContent = (connectors.group(1)).rstrip()
#        print connectorsContent

        connectorPattern = r'(^\s*<p:entry.*?key\s*=\s*"(.*?)".*?</p:entry>)'
        self._connectorToHVIDMap = {}
        while len(connectorsContent) > 0:
            connector = re.match(connectorPattern, connectorsContent, re.MULTILINE | re.DOTALL)
            if (not connector) or len(connector.groups()) <= 1:
                print '[ERROR] Failed in extracting individual connector from the file'
                return False
            connectorName = connector.group(2)
            connectorContent = connector.group(1)
            keyValues = self._extractKeyValue(connectorContent)
            hvids = {}
            for hvid in keyValues:
                hvids[hvid] = HvId(hvid)
                hvids[hvid].setConnector(connectorName)
            self._connectorToHVIDMap[connectorName] = hvids
            connectorsContent = connectorsContent[len(connectorContent):]
        return True

    def writeEntitiesPerSubsystemAllocation(self, out):
        import os

        header = (
            '<?xml version="1.0" encoding="UTF-8"?>\n',
            '<p:configuration xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.thalesgroup.com/hv/data-v1/system/configuration" xsi:schemaLocation="http://www.thalesgroup.com/hv/data-v1/system/configuration hypervisor_system_configuration.xsd">\n',
            '  <p:mapping runtimeMode="OPERATIONAL" xsi:type="p:IdToIdMapType">\n'
        )
        connectorHeader = '    <p:entry key="%s" role="DEFAULT" xsi:type="p:IdToIdEntryType">\n'
        connectorFooter = ('    </p:entry>\n')
        footer = (
            '  </p:mapping>\n',
            '</p:configuration>\n'
        )

        dirPath = os.path.join(out, 'mapping')
        if not self.mkdir_p(dirPath):
            print '[ERROR] Failed to created directory: %s' % dirPath
            return False

        print 'To export %d entitiesPerSubsystemAllocation.xml:' % len(self._connectorToHVIDMap.keys())
        for i in xrange(len(self._connectorToHVIDMap.keys())):
            key = self._connectorToHVIDMap.keys()[i]
            path = os.path.join(dirPath, 'entitiesPerSubsystemAllocation.%s.xml' % key)
            values = sorted(self._connectorToHVIDMap[key].keys())
#            print values
            print '[%d/%d] [%s] Extracting to: %s' % (i + 1, len(self._connectorToHVIDMap.keys()), key, path)
            try:
                with open(path, 'w') as f:
                    f.writelines(header)
                    f.write(connectorHeader % self.escape(key))
                    for value in values:
                        f.write('      <p:value>%(value)s</p:value>\n' % locals())
                    f.writelines(connectorFooter)
                    f.writelines(footer)
            except Exception as e:
                print '[ERROR] Error in writing file: %s' % path
                return False
        return True

    def writeEquipments(self, out):
        import os

        header = (
            '<?xml version="1.0" encoding="UTF-8"?>\n',
            '<hv-conf:entitiesConfiguration xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:hv-conf="http://www.thalesgroup.com/hv/data-v1/entity/configuration" xmlns:myproject="http://www.thalesgroup.com/myproject/data/config/equipment/common/configuration">\n'
        )
        content = '  <hv-conf:entity xsi:type="myproject:SGEQPT8Type" id="%s"></hv-conf:entity>\n'
        footer = ('</hv-conf:entitiesConfiguration>\n')

        dirPath = os.path.join(out, 'instances')
        if not self.mkdir_p(dirPath):
            print '[ERROR] Failed to created directory: %s' % dirPath
            return False

        print 'To export %d equipments.xml:' % len(self._connectorToHVIDMap.keys())
        for i in xrange(len(self._connectorToHVIDMap.keys())):
            key = self._connectorToHVIDMap.keys()[i]
            path = os.path.join(dirPath, 'equipments.%s.xml' % key)
            values = sorted(self._connectorToHVIDMap[key].keys())
            print '[%d/%d] [%s] Extracting to: %s' % (i + 1, len(self._connectorToHVIDMap.keys()), key, path)
            try:
                with open(path, 'w') as f:
                    f.writelines(header)
                    for value in values:
                        if value == key: continue # skip the connector name
                        f.write(content % self.escape(value))
                    f.writelines(footer)
            except Exception as e:
                print '[ERROR] Error in writing file: %s' % path
                return False
        return True

    def _parseEntitiesPerAreaAllocation(self, entitiesFile):
        import re

        originalContent = None
        with open(entitiesFile, 'r') as f: originalContent = f.read()
#        print originalContent
        if not originalContent:
            print '[ERROR] No content in file: %s' % entitiesFile
            return False

        # extract connector blocks
        areasPattern = r'.*?(^\s*<p:entry.*</p:entry>)'
        areas = re.match(areasPattern, originalContent, re.MULTILINE | re.DOTALL)
        if (not areas) or len(areas.groups()) <= 0:
            print '[ERROR] Failed in extracting areas from the file: %s' % entitiesFile
            return False
        areasContent = (areas.group(1)).rstrip()
#        print areasContent

        areaPattern = r'(^\s*<p:entry.*?key\s*=\s*"(.*?)".*?</p:entry>)'
        self._connectorToAreaToHVIDMap = {}
        self._areaToHVIDMap = {}
        while len(areasContent) > 0:
            area = re.match(areaPattern, areasContent, re.MULTILINE | re.DOTALL)
            if (not area) or len(area.groups()) <= 1:
                print '[ERROR] Failed in extracting individual area from the file: %s' % entitiesFile
                return False
            areaName = area.group(2)
            areaContent = area.group(1)
            keyValues = self._extractKeyValue(areaContent)
#            print '%s: %s' % (areaName, str(keyValues))
            for value in keyValues:
                hvid = self._findHvId(value)
                if not hvid:
                    print '[WARNING] Unknown HVID (not defined in entitiesPerSubsystemAllocation?): %s [IGNORED]' % value
                    continue
                hvid.setArea(areaName)
                if self._connectorToAreaToHVIDMap.has_key(hvid.getConnector()):
                    if self._connectorToAreaToHVIDMap[hvid.getConnector()].has_key(areaName):
                        self._connectorToAreaToHVIDMap[hvid.getConnector()][areaName].append(hvid)
                    else:
                        self._connectorToAreaToHVIDMap[hvid.getConnector()][areaName] = [hvid]
                else:
                    self._connectorToAreaToHVIDMap[hvid.getConnector()] = {areaName:[hvid]}
                if self._areaToHVIDMap.has_key(areaName):
                    self._areaToHVIDMap[areaName].append(hvid)
                else:
                    self._areaToHVIDMap[areaName] = [hvid]
            areasContent = areasContent[len(areaContent):]
        return True

    def writeEntitiesPerAreaAllocation(self, out):
        import os

        header = (
            '<?xml version="1.0" encoding="UTF-8"?>\n',
            '<p:configuration xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.thalesgroup.com/hv/data-v1/system/configuration" xsi:schemaLocation="http://www.thalesgroup.com/hv/data-v1/system/configuration hypervisor_system_configuration.xsd">\n',
            '  <p:mapping runtimeMode="OPERATIONAL" xsi:type="p:IdToIdMapType">\n'
        )
        areaHeader = '    <p:entry key="%s" role="DEFAULT" xsi:type="p:IdToIdEntryType">\n'
        content = '      <p:value>%(value)s</p:value>\n'
        areaFooter = '    </p:entry>\n'
        footer = (
            '  </p:mapping>\n',
            '</p:configuration>\n'
        )

        dirPath = os.path.join(out, 'mapping')
        if not self.mkdir_p(dirPath):
            print '[ERROR] Failed to created directory: %s' % dirPath
            return False

        print 'To export %d entitiesPerAreaAllocation.xml:' % len(self._connectorToAreaToHVIDMap.keys())
        for i in xrange(len(self._connectorToAreaToHVIDMap.keys())):
            connectorName = self._connectorToAreaToHVIDMap.keys()[i]
            connector = self._connectorToAreaToHVIDMap[connectorName]
            path = os.path.join(dirPath, 'entitiesPerAreaAllocation.%s.xml' % connectorName)
            areas = sorted(connector.keys())
#            print areas
            print '[%d/%d] [%s] Extracting to: %s' % (i + 1, len(self._connectorToAreaToHVIDMap.keys()), connectorName, path)
            try:
                with open(path, 'w') as f:
                    f.writelines(header)
                    #print areas
                    for area in areas:
                        #print area
                        f.write(areaHeader % self.escape(area))
                        for value in sorted(x.getId() for x in connector[area]):
                            f.write(content % locals())
                        f.write(areaFooter)
                    f.writelines(footer)
            except Exception as e:
                print e
                print '[ERROR] Error in writing file: %s' % path
                return False
        return True

    def writeAreas(self, out, split = True):
        import os

        header = (
            '<?xml version="1.0" encoding="UTF-8"?>\n',
            '<hv-conf:entitiesConfiguration xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:hv-conf="http://www.thalesgroup.com/hv/data-v1/entity/configuration" xmlns:area="http://www.thalesgroup.com/hv/data-v1/area/configuration">\n'
        )
        content = '  <hv-conf:entity xsi:type="area:AreaType" id="%s"></hv-conf:entity>\n'
        footer = ('</hv-conf:entitiesConfiguration>\n')

        dirPath = os.path.join(out, 'instances')
        if not self.mkdir_p(dirPath):
            print '[ERROR] Failed to created directory: %s' % dirPath
            return False

        if split:
            print 'To export %d areas.xml:' % len(self._connectorToAreaToHVIDMap.keys())
            for i in xrange(len(self._connectorToAreaToHVIDMap.keys())):
                connectorName = self._connectorToAreaToHVIDMap.keys()[i]
                connector = self._connectorToAreaToHVIDMap[connectorName]
                path = os.path.join(dirPath, 'areas.%s.xml' % connectorName)
                areas = sorted(connector.keys())
#                print areas
                print '[%d/%d] [%s] Extracting to: %s' % (i + 1, len(self._connectorToAreaToHVIDMap.keys()), connectorName, path)
                try:
                    with open(path, 'w') as f:
                        f.writelines(header)
                        for area in areas:
                            f.write(content % self.escape(area))
                        f.writelines(footer)
                except Exception as e:
                    print e
                    print '[ERROR] Error in writing file: %s' % path
                    return False
        else:
            print 'To export 1 areas.xml:'
            path = os.path.join(dirPath, 'areas.xml')
            areas = sorted(self._areaToHVIDMap.keys())
            print '[1/1] [ALL] Extracting to: %s' % path
            try:
                with open(path, 'w') as f:
                    f.writelines(header)
                    for area in areas:
                        f.write(content % self.escape(area))
                    f.writelines(footer)
            except Exception as e:
                print e
                print '[ERROR] Error in writing file: %s' % path
                return False

        return True

    def _findHvId(self, hvid):
        for connector in self._connectorToHVIDMap:
            obj = self._connectorToHVIDMap[connector].get(hvid)
            if obj: return obj
        return None

    def _extractKeyValue(self, content):
        import re
        valuesPattern = r'(<p:value>.*</p:value>)'
        value = re.search(valuesPattern, content, re.MULTILINE | re.DOTALL)
        if (not value) or len(value.groups()) < 1: return None
        values = value.group(0)
        values = values.replace('<p:value>', '')
        values = values.replace('</p:value>', '')
        return values.split()

    def mkdir_p(self, path):
        import os
        import errno
        try:
            os.makedirs(path)
        except OSError as e:
            if  e.errno != errno.EEXIST and os.path.isdir(path):
                return False
        return True
    
    def escape(self, text):
        import cgi
        escaped = cgi.escape(text)
        escaped = escaped.replace('"', r'&quot;')
        return escaped

################################################################################
def writeFile(filename, func, key):
    try:
        with open(filename, 'w') as f:
            func(f, key)
    except Exception as e:
        print '[ERROR] %s' % str(e)
        return False
    return True

################################################################################
def main():
    import os
    import argparse

    ap = argparse.ArgumentParser(
        description='Expand HV System Configuration into one per connector',
        formatter_class=argparse.ArgumentDefaultsHelpFormatter
    )
    ap.add_argument(
        '-i', '--input',
        required=True,
        help='HV System Configuration Directory Path'
    )
    ap.add_argument(
        '-o', '--output',
        required=True,
        help='Output directory path'
    )

    args = ap.parse_args()

    try:
        entity = EntityPerSubsystemAllocation(args.input)
    except Exception as e:
        print '[ERROR] %s' % str(e)
        return 1

    if len(entity) <= 0:
        print '[ERROR] No connectors defined in the file'
        return 2

    result = entity.writeEntitiesPerSubsystemAllocation(args.output)
    if not result:
        print '[ERROR] Failed in exporting EntitiesPerSubsystemAllocation'
        return 3

    result = entity.writeEquipments(args.output)
    if not result:
        print '[ERROR] Failed in exporting Equipments'
        return 4

    result = entity.writeEntitiesPerAreaAllocation(args.output)
    if not result:
        print '[ERROR] Failed in exporting EntitiesPerAreaAllocation'
        return 5

    result = entity.writeAreas(args.output, False)
    if not result:
        print '[ERROR] Failed in exporting Areas'
        return 6

    print 'Completed successfully'
    return 0

################################################################################
if __name__ == '__main__':
    import sys

    print 'Script Version: %s' % getDocHash()
    retCode = main()
    sys.exit(retCode)
