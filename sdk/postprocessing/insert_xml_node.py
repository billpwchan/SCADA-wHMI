#!/usr/bin/python

################################################################################
def write_file(output, content):
	output.seek(0)
	output.truncate(0)
	output.writelines(content)
	return True

################################################################################
def read_file(input):
	return input.readlines()

################################################################################
def insert_text_to_xml(xml, text, node):
	import re
	pattern = re.compile(r'^(.*)(</%s[\s>].*)$' % node)

	lines = []
	for l in xml:
		matched = pattern.match(l)
		if matched:
			prefix = matched.group(1)
			postfix = matched.group(2)
			if prefix:
				if 0 < len(prefix.strip()):
					lines.append(prefix + '\n')
				else:
					postfix = prefix + postfix
			for t in text:
				lines.append(t)
				if t[-1] != '\n': lines.append('\n')
			lines.append(postfix + '\n')
		else:
			lines.append(l)

#	print lines
	return write_file(xml, lines)

################################################################################
def main():
	import argparse
	
	ap = argparse.ArgumentParser(
		description='Insert a blob of text as an XML node, under the specified node, as the last child',
		formatter_class=argparse.ArgumentDefaultsHelpFormatter
	)
	ap.add_argument(
		'-i', '--input',
		nargs='+',
		type=argparse.FileType('r+'),
		required=True,
		help='Input XML file'
	)
	ap.add_argument(
		'-n', '--node',
		required=True,
		help='Insert under this node, as the last child'
	)
	ap.add_argument(
		'-t', '--text',
		type=argparse.FileType('r'),
		required=True,
		help='File containing the text to be inserted'
	)

	args = ap.parse_args()

	text = None
	with args.text:
		text = read_file(args.text)
	if not text:
		print '[ERROR] Failed in reading text file: %s' % args.text.name
		return -1
#	print text

	for x in args.input:
		with x:
			ret = insert_text_to_xml(x, text, args.node)
			if not ret:
				print '[ERROR] Failed in inserting text to: %s' % x.name
				return -1
#			print '[INFO] %s: %s' % (x.name, ret)
	return 0
	
################################################################################
if __name__ == '__main__':
	import sys
	retCode = main()
	sys.exit(retCode)
