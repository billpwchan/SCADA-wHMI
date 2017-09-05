#!/usr/bin/python

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
def writeHVIDsToSchMap(output, map):
	import csv
	writer = csv.writer(output)
	for key in map:
#		print key
#		print map[key]
		writer.writerow([key] + map[key])
	return True

################################################################################
def main():
	import argparse
	
	ap = argparse.ArgumentParser(
		description='Generate the mapping of hvid to schematics',
		formatter_class=argparse.ArgumentDefaultsHelpFormatter
	)
	ap.add_argument(
		'-s', '--schematic',
		required=True,
		help='Directory containing the schematics (typically xxx/widgets/situationView/view/)'
	)
	ap.add_argument(
		'-o', '--output',
		type=argparse.FileType('wb'),
		required=True,
		help='Output file containing the mapping'
	)

	args = ap.parse_args()
	schToHVIDsMap = getHVIDFromDirectory(args.schematic)
#	print schToHVIDsMap
	if not schToHVIDsMap:
		print '[ERROR] Failed in getting schematics to HVID map'
		return -1

	hvidsToSchMap = getHVIDToSchematicMap(schToHVIDsMap)
#	print hvidsToSchMap
	if not hvidsToSchMap:
		print '[ERROR] Failed in getting HVID to schematics map'
		return -1

	with args.output:
		ret = writeHVIDsToSchMap(args.output, hvidsToSchMap)
		if not ret:
			print '[ERROR] Failed in writing HVID to schematics map to output file: %s' % args.output.name
			return -1

	return 0

################################################################################
if __name__ == '__main__':
	import sys
	retCode = main()
	sys.exit(retCode)
