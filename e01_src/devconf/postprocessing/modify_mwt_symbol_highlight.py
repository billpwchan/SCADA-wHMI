#!/usr/bin/python

################################################################################
def write_file(output, content):
	output.seek(0)
	output.truncate(0)
	output.write(content)
	return True

################################################################################
def read_file(input):
	return input.read()

################################################################################
def modify_mwt_symbol_highlight(input, width, height):
	import re

	content = read_file(input)
	if not content:
		print '[ERROR] Failed in reading file: %s' % input.name
		return False

	replaced = re.sub(r'\s+width="\d+\.?\d*"', ' width="%f"' % width, content)
	replaced = re.sub(r'\s+height="\d+\.?\d*"', ' height="%f"' % height, replaced)

	ret = write_file(input, replaced)
	if not content:
		print '[ERROR] Failed in writing file: %s' % input.name
		return False

	return True

################################################################################
def main():
	import argparse

	ap = argparse.ArgumentParser(
		description='Modify MWT symbol highlight width and height',
		formatter_class=argparse.ArgumentDefaultsHelpFormatter
	)
	ap.add_argument(
		'-i', '--input',
		type=argparse.FileType('r+b'),
		required=True,
		help='Input MWT symbol file'
	)
	ap.add_argument(
		'-W', '--width',
		type=float,
		required=True,
		help='Highlight width'
	)
	ap.add_argument(
		'-H', '--height',
		type=float,
		required=True,
		help='Hightlight height'
	)

	args = ap.parse_args()

	with args.input:
		ret = modify_mwt_symbol_highlight(args.input, args.width, args.height)
		if not ret:
			print '[ERROR] Failed in modifying mwt symbol highlight: input: %s' % args.input.name
			return -1
	return 0

################################################################################
if __name__ == '__main__':
	import sys
	retCode = main()
	sys.exit(retCode)
