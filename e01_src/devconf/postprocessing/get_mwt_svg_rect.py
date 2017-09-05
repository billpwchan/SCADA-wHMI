#!/usr/bin/python

################################################################################
def parse_svg_element_scale_transform(element):
	import re
	# translate(-100.0 -100.0)scale(1 0.2)
	# translate(-100.0 -100.0)scale(1,0.2)
	# translate(-100.0 -100.0)scale(1 , 0.2)
	scalePattern = re.compile(r'.*scale\s*\(\s*(\d+\.?\d*)\s*,?\s*(\d+\.?\d*)\s*\).*')
	if not element.attrib.has_key('transform'): return (1.0,1.0)

	transform = element.attrib['transform']
	matched = scalePattern.match(transform)
	if not matched:
#		print '[DEBUG] No "scale" transform found in "%s"' % transform
		return (1.0,1.0)

	try:
		scaleX = float(matched.group(1))
		scaleY = float(matched.group(2))
#		print '[INFO] Parsed (scaleX = %f, scaleY = %f) from transform "%s"' % (scaleX, scaleY, transform)
		return (scaleX, scaleY)
	except:
		print '[ERROR] Error in parsing "scale" transfrom in "%s"' % transform
		return None

################################################################################
def parse_svg_element_rotate_transform(element):
	import re
	# rotate(-90 100 100)
	# rotate(0, 100,100)
	# rotate(90, 100,100)
	# rotate(180, 100,100)
	rotatePattern = re.compile(r'.*rotate\s*\(\s*(-?\d+\.?\d*).*\)')
	if not element.attrib.has_key('transform'): return 0

	transform = element.attrib['transform']
	matched = rotatePattern.match(transform)
	if not matched:
#		print '[DEBUG] No "rotate" transform found in "%s"' % transform
		return 0

	try:
		rotate = float(matched.group(1))
#		print '[INFO] Parsed (rotate: %f) from transform "%s"' % (rotate, transform)
		return rotate
	except:
		print '[ERROR] Error in parsing "rotate" transfrom in "%s"' % transform
		return None

################################################################################
def apply_svg_element_rotate_transform(rotate, width, height):
	if abs(rotate) == 0 or abs(rotate) == 180:
		return width, height
	elif abs(rotate) == 90 or abs(rotate) == 270:
		return height, width
	else:
		print '[ERROR] Unsupported rotation angle: %f' % rotate
		return None

################################################################################
def parse_svg_element_rect_size(element):
	import re

	# supported tags
	svgTag = re.compile(r'^({.+})?svg$')
	gTag = re.compile(r'^({.+})?g$')
	rectTag = re.compile(r'^({.+})?rect$')
	textTag = re.compile(r'^({.+})?text$')
	lineTag = re.compile(r'^({.+})?line$')
	circleTag = re.compile(r'^({.+})?circle$')
	ellipseTag = re.compile(r'^({.+})?ellipse$')
	pathTag = re.compile(r'^({.+})?path$')
	polygonTag = re.compile(r'^({.+})?polygon$')
	animateTag = re.compile(r'^({.+})?animate$')

#	print '[DEBUG] parse_svg_element_rect_size: tag: %s, attrib: %s' % (element.tag, element.attrib)
	if svgTag.match(element.tag) or gTag.match(element.tag):
		# it is a <g> tag
		scale = parse_svg_element_scale_transform(element)
		if not scale:
			print '[ERROR] Error in parsing scale transform: tag: %s, attrib: %s' % (element.tag, element.attrib)
			return None
		rotate = parse_svg_element_rotate_transform(element)
		if None == rotate:
			print '[ERROR] Error in parsing rotate transform: tag: %s, attrib: %s' % (element.tag, element.attrib)
			return None
		maxWidth, maxHeight = 0, 0
		for child in element:
			rect = parse_svg_element_rect_size(child)
			assert not rect or 2 == len(rect)
			if not rect:
				print '[ERROR] Error in parsing: tag: %s, attrib: %s' % (child.tag, child.attrib)
				return None
			# TODO: note that this is very wrong, as we should properly handle
			#       other kinds of transformations
			w, h = rect[0] * scale[0], rect[1] * scale[1]
			maxWidth, maxHeight = max(w, maxWidth), max(h, maxHeight)
#		print '[INFO] <%s> (width = %f, height = %f)' % (element.tag, maxWidth, maxHeight)
		maxWidth, maxHeight = apply_svg_element_rotate_transform(rotate, maxWidth, maxHeight)
#		print '[INFO] <%s> (width = %f, height = %f, rotated)' % (element.tag, maxWidth, maxHeight)
		return maxWidth, maxHeight
	elif rectTag.match(element.tag):
		attrib = element.attrib
		if (not attrib.has_key('width')) or (not attrib.has_key('height')):
			print '[ERROR] No "width" or "height" attribute(s) found in <rect> element: %s' % attrib
			return None
		try:
			w, h = float(attrib['width']), float(attrib['height'])
#			print '[INFO] <rect> (width = %f, height = %f)' % (w, h)
			return (w,h)
		except:
			print '[ERROR] Error in parsing "width" or "height" value(s): %s' % attrib
			return None
	elif textTag.match(element.tag):
		# TODO: to properly handle this kind of element
		return (0,0)
	elif lineTag.match(element.tag):
		# TODO: to properly handle this kind of element
		return (0,0)
	elif circleTag.match(element.tag):
		# TODO: to properly handle this kind of element
		return (0,0)
	elif ellipseTag.match(element.tag):
		# TODO: to properly handle this kind of element
		return (0,0)
	elif pathTag.match(element.tag):
		# TODO: to properly handle this kind of element
		return (0,0)
	elif polygonTag.match(element.tag):
		# TODO: to properly handle this kind of element
		return (0,0)
	elif animateTag.match(element.tag):
		# TODO: to properly handle this kind of element
		return (0,0)
	else:
		print '[ERROR] Unexpected element type: %s' % element.tag
		return None

################################################################################
def parse_svg_file_rect_size(svg):
	import re
	import xml.etree.ElementTree as ET

	tree = None
	try:
		tree = ET.parse(svg.name)
	except:
		print '[ERROR] Error in parsing SVG: "%s"' % svg.name
		return None
	root = tree.getroot()

	rect = parse_svg_element_rect_size(root)
	assert not rect or 2 == len(rect)
	if not rect:
		print '[ERROR] Error in parsing encompassing rectangle for "%s"' % svg.name
		return None
	return rect

################################################################################
def main():
	import argparse
	
	ap = argparse.ArgumentParser(
		description='Get the encompassing rectangle of the SVG',
		formatter_class=argparse.ArgumentDefaultsHelpFormatter
	)
	ap.add_argument(
		'-i', '--input',
		nargs='+',
		type=argparse.FileType('rb'),
		required=True,
		help='Input SVG file'
	)
	ap.add_argument(
		'-b', '--bare',
		action='store_true',
		default=False,
		help='Bare output, return width and height separated by space'
	)

	args = ap.parse_args()

	for svg in args.input:
		rect = parse_svg_file_rect_size(svg)
		assert not rect or 2 == len(rect) 
		if not rect:
			print '[ERROR] Failed in parsing SVG file: %s' % svg.name
			return -1

		if args.bare:
			print '%f %f' % (rect[0], rect[1])
		else:
			print '%s: (%f, %f)' % (svg.name, rect[0], rect[1])
	return 0
	
################################################################################
if __name__ == '__main__':
	import sys
	retCode = main()
	sys.exit(retCode)
