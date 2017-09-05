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
def parse_mwt_view_zoom_bar_status(content):
	import re
	zoomBarPattern = re.compile(r'.*\s+displayPanZoomBar\s*=\s*"([^"]+)"', re.DOTALL)

	matched = zoomBarPattern.match(content)
	if not matched:
#		print '[INFO] displayPanZoomBar not defined'
		return False
	else:
		status = matched.group(1).strip().lower()
		if 'true' == status:
#			print '[INFO] displayPanZoomBar enabled'
			return True
		else:
#			print '[INFO] displayPanZoomBar disabled'
			return False

################################################################################
def parse_mwt_view_max_extent(content):
	import re
	maxExtentPattern = re.compile(r'.*\s+maxExtent\s*=\s*"\s*(\d+\.?\d*)\s+(\d+\.?\d*)\s+(\d+\.?\d*)\s+(\d+\.?\d*)\s*"', re.DOTALL)

	matched = maxExtentPattern.match(content)
	if not matched:
		print '[ERROR] Error in extracting maxExtent from content'
		return None

	try:
		return (
			float(matched.group(3)),
			float(matched.group(4))
		)
	except:
		print '[ERROR] Error in parsing maxExtent'
		return None

################################################################################
def generate_mwt_view_resolution(extentWidth, extentHeight, viewWidth, viewHeight, levels):
	ratio = max(extentWidth / viewWidth, extentHeight / viewHeight)
	resolutions = []
	for l in levels: resolutions.append(ratio / l)
	return resolutions

################################################################################
def modify_mwt_view_resolution(view, viewWidth, viewHeight, levels):
	import re

	content = read_file(view)
	if not content:
		print '[ERROR] Failed to read file: "%s"' % view.name 
		return None

	maxExtent = parse_mwt_view_max_extent(content)
	if not maxExtent:
		print '[ERROR] Failed to parse maxExtent from file: "%s"' % view.name 
		return None

	zoomEnabled = parse_mwt_view_zoom_bar_status(content)
	zoomLevels = [1]
	if zoomEnabled: zoomLevels = levels

	resolutions = generate_mwt_view_resolution(maxExtent[0], maxExtent[1], viewWidth, viewHeight, zoomLevels)
	resolution = reduce(lambda x,y: x+' '+y, map(lambda x: str(x), resolutions))

	content = re.sub(r'resolution\s*=\s*"([^"]+)"', r'resolution="%s"' % resolution, content, re.DOTALL)

	ret = write_file(view, content)
	if not ret:
		print '[ERROR] Failed to write file: "%s"' % view.name 
		return None
	return resolution

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
		type=argparse.FileType('r+b'),
		required=True,
		help='Input view file(s)'
	)
	ap.add_argument(
		'-z', '--zoom',
		nargs='+',
		type=float,
		required=True,
		help='Zoom level(s) to be created'
	)
	ap.add_argument(
		'-W', '--width',
		type=float,
		required=True,
		help='Display view width'
	)
	ap.add_argument(
		'-H', '--height',
		type=float,
		required=True,
		help='Display view height'
	)

	args = ap.parse_args()

	for view in args.input:
		with view:
			ret = modify_mwt_view_resolution(view, args.width, args.height, args.zoom)
			if not ret:
				print '[ERROR] Failed in modifying resolution of view file: %s' % view.name
				return -1
#			print '[INFO] %s: %s' % (view.name, ret)
	return 0
	
################################################################################
if __name__ == '__main__':
	import sys
	retCode = main()
	sys.exit(retCode)
