#!/usr/bin/python

################################################################################
def writeHVIDsToNavSettingMap(output, map):
	import csv
	writer = csv.writer(output)
	for key in map:
#		print key
#		print map[key]
		writer.writerow([key] + map[key])
	return True

################################################################################
def getFileList(dir, pattern):
	import os
	import glob
	return glob.glob(os.path.join(dir, pattern))

################################################################################
def getHVIDFromSchematic(schematic):
	import re
	pattern = re.compile(r'^\s*<entityEntry\s+.*entityId="([^"]+)"')
	hvids = []
	with open(schematic, 'r') as f:
		for l in f:
			matched = pattern.match(l)
			if matched:
				hvids.append(matched.group(1))
#	print '%s: %s' % (schematic, str(hvids))
	return hvids

################################################################################
def getHVIDFromDirectory(dir):
	import os
	schList = getFileList(dir, r'*.xml')
	hvidMap = []
	for sch in schList:
		hvids = getHVIDFromSchematic(sch)
		hvidMap.append([os.path.basename(sch)[:-4], hvids])
#	print 'All HVID: %s' % str(hvidMap)
	return hvidMap

################################################################################
def getHVIDToSchematicMap(schToHVIDsMap):
	hvidsMap = {}
	for map in schToHVIDsMap:
		sch = map[0]
		hvids = map[1]
		for hvid in hvids:
			if not hvidsMap.has_key(hvid): hvidsMap[hvid] = []
			schMap = hvidsMap[hvid]
			if not sch in schMap: schMap.append(sch)
#	print hvidsMap
	return hvidsMap

################################################################################
def readHVIDsToSchMap(input):
	import csv
	hvidsToSchMap = {}
	reader = csv.reader(input)
	for row in reader:
		hvidsToSchMap[row[0]] =  row[1:]
#	print hvidsToSchMap
	return hvidsToSchMap

################################################################################
def readSchToNavKeyMap(input):
	import re
	pattern = r'.*<option\s+key\s*=\s*"([^"]+)"\s*>.*<uiPanel>([^<]+)</uiPanel>'
	content = input.read()
	contentSplit = content.split(r'</option>')
	schToNavKeyMap = {}
	for i in contentSplit:
		matched = re.match(pattern, i, re.DOTALL)
		if matched:
			key = matched.group(1)
			sch = matched.group(2)
#			print '%s: %s' % (sch, key)
			if not schToNavKeyMap.has_key(sch): schToNavKeyMap[sch] = []
			schToNavKeyMap[sch].append(key)
#	print schToNavKeyMap
	return schToNavKeyMap

################################################################################
def readSchToNavSettingMap(input):
	import re
	pattern = r'.*<option\s+key\s*=\s*"([^"]+)"\s*>.*<setting>([^<]+)</setting>'
	content = input.read()
	contentSplit = content.split('\n')
	schToNavSettingMap = {}
	for i in contentSplit:
		matched = re.match(pattern, i, re.DOTALL)
		if matched:
			key = matched.group(1)
			setting = matched.group(2)
#			print '%s: %s' % (setting, key)
			if not schToNavSettingMap.has_key(setting): schToNavSettingMap[setting] = []
			schToNavSettingMap[setting].append(key)
#	print schToNavSettingMap
	return schToNavSettingMap

################################################################################
def genHVIDToNavKeyMap(hvidsToSchMap, schToNavKeyMap):
	hvidsToNavKeyMap = {}
	for hvidKey in hvidsToSchMap:
		hvidItems = hvidsToSchMap[hvidKey]
		navKey = []
		for hvidItem in hvidItems:
			if schToNavKeyMap.has_key(hvidItem):
				navKey.extend(schToNavKeyMap[hvidItem])
		if hvidsToNavKeyMap.has_key(hvidKey):
			print '[ERROR] Duplicated hvid: %s [IGNORED]' % hvidKey
		hvidsToNavKeyMap[hvidKey] = navKey
#	print hvidsToNavKeyMap
	return hvidsToNavKeyMap

################################################################################
def genHVIDToNavSettingMap(hvidsToNavKeyMap, schToNavSettingMap):
	hvidsToNavSettingMap = {}
	for hvidKey in hvidsToNavKeyMap:
		hvidItems = hvidsToNavKeyMap[hvidKey]
		navKey = []
		for hvidItem in hvidItems:
			if schToNavSettingMap.has_key(hvidItem):
				navKey.extend(schToNavSettingMap[hvidItem])
		if hvidsToNavSettingMap.has_key(hvidKey):
			print '[ERROR] Duplicated hvid: %s [IGNORED]' % hvidKey
		hvidsToNavSettingMap[hvidKey] = navKey
#	print hvidsToNavSettingMap
	return hvidsToNavSettingMap

################################################################################
def main():
	import argparse
	
	ap = argparse.ArgumentParser(
		description='Generate the mapping of hvid to navigation key',
		formatter_class=argparse.ArgumentDefaultsHelpFormatter
	)
	ap.add_argument(
		'-i', '--input',
		type=argparse.FileType('rb'),
		required=True,
		help='CSV file output by gen_hvid_to_sch_map.py'
	)
	ap.add_argument(
		'-n', '--navigation',
		type=argparse.FileType('rb'),
		required=True,
		help='navigationSetting.xml'
	)
	ap.add_argument(
		'-m', '--mapping',
		type=argparse.FileType('rb'),
		required=True,
		help='navigationMapping.xml'
	)
	ap.add_argument(
		'-o', '--output',
		type=argparse.FileType('wb'),
		required=True,
		help='CSV file output'
	)

	args = ap.parse_args()
	with args.input:
		hvidsToSchMap = readHVIDsToSchMap(args.input)
#		print hvidsToSchMap
		if not hvidsToSchMap:
			print '[ERROR] Failed to read HVID to schematics map: %s' % (args.input.name)
			return -1

	with args.navigation:
		schToNavKeyMap = readSchToNavKeyMap(args.navigation)
#		print schToNavKeyMap
		if not schToNavKeyMap:
			print '[ERROR] Failed to read schematics to navigation key map: %s' % (args.navigation.name)
			return -1

	with args.mapping:
		schToNavSettingMap = readSchToNavSettingMap(args.mapping)
#		print schToNavSettingMap
		if not schToNavSettingMap:
			print '[ERROR] Failed to read schematics to navigation setting map: %s' % (args.mapping.name)
			return -1

	hvidsToNavKeyMap = genHVIDToNavKeyMap(hvidsToSchMap, schToNavKeyMap)
#	print hvidsToNavKeyMap
	if not hvidsToNavKeyMap:
		print '[ERROR] Failed to generate HVIDs to navigation key map'
		return -1

	hvidsToNavSettingMap = genHVIDToNavSettingMap(hvidsToNavKeyMap, schToNavSettingMap)
#	print hvidsToNavSettingMap
	if not hvidsToNavSettingMap:
		print '[ERROR] Failed to generate HVIDs to navigation setting map'
		return -1

	with args.output:
		ret = writeHVIDsToNavSettingMap(args.output, hvidsToNavSettingMap)
		if not ret:
			print '[ERROR] Failed in writing HVID to schematics map to output file: %s' % args.output.name
			return -1

	return 0

################################################################################
if __name__ == '__main__':
	import sys
	retCode = main()
	sys.exit(retCode)
