#!/usr/bin/python

################################################################################
def getDocHash():
	import os
	import hashlib
	import inspect

	filePath = os.path.abspath(inspect.getsourcefile(lambda :0))

	return hashlib.md5(open(filePath, 'rb').read()).hexdigest()
	
################################################################################
def genCallImageAction(hvid, navKeys, firstOnly):
	template =            '\t<actionset key="%s">\n'
	template = template + '\t\t<OperationType>actionsetkey</OperationType>\n'
	template = template + '\t\t<OperationString1>UITaskLaunch_%s</OperationString1>\n'
	template = template + '\t</actionset>\n'
	template = template + '\t<action key="UITaskLaunch_%s">\n'
	template = template + '\t\t<OperationType>action</OperationType>\n'
	template = template + '\t\t<OperationAction>uitask</OperationAction>\n'
	template = template + '\t\t<OperationString1>UITaskLaunch</OperationString1>\n'
	template = template + '\t\t<OperationString2>:UIGws:UIPanelScreen:UIScreenMMI:NavigationMgr</OperationString2>\n'
	template = template + '\t\t<OperationString3>-1</OperationString3>\n'
	template = template + '\t\t<OperationString4>%s</OperationString4>\n'
	template = template + '\t\t<OperationString5>true</OperationString5>\n'
	template = template + '\t</action>\n\n'
	return template % (hvid, hvid, hvid, navKeys[0])

def writeCallImageconfig(output, map, firstOnly):
	header =          '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>\n<!DOCTYPE xml>\n'
	header = header + '<data-set xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">\n'
	header = header + '\t<header key="header"></header>\n'
	header = header + '\t<actionset key="init"></actionset>\n'
	footer = '</data-set>\n'

	output.writelines(header)
	for key in sorted(map.keys()):
		if map[key]:
#			print 'key: %s' % key
#			print 'map: %s' % str(map[key])
			action = genCallImageAction(key, map[key], firstOnly)
			output.writelines(action)
	output.writelines(footer)
	return True

################################################################################
def readHVIDsToNavKeyMap(input):
	import csv
	hvidsToNavKeyMap = {}
	reader = csv.reader(input)
	for row in reader:
		hvidsToNavKeyMap[row[0]] =  row[1:]
#	print hvidsToNavKeyMap
	return hvidsToNavKeyMap

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
		help='CSV file (HVID -> Navigation Key)'
	)
	ap.add_argument(
		'-o', '--output',
		type=argparse.FileType('wb'),
		required=True,
		help='XML file output (typically UIEventActionProcessor_CallImage.opts.xml)'
	)
	ap.add_argument(
		'-f', '--first',
		action='store_true',
		default=False,
		help='Generate only for the first navigation key'
	)
	args = ap.parse_args()
	with args.input:
		hvidsToNavKeyMap = readHVIDsToNavKeyMap(args.input)
#		print hvidsToNavKeyMap
		if not hvidsToNavKeyMap:
			print '[ERROR] Failed to read HVID to Navigation Key map: %s' % (args.input.name)
			return -1

	with args.output:
		ret = writeCallImageconfig(args.output, hvidsToNavKeyMap, args.first)
		if not ret:
			print '[ERROR] Failed to write call image config' % (args.output.name)
			return -1

	return 0

################################################################################
if __name__ == '__main__':
	print 'Script Version: %s' % getDocHash()

	import sys
	retCode = main()
	sys.exit(retCode)
