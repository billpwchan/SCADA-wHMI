#!/usr/bin/python

################################################################################
def isSVGExisted(svgName):
	import os
	svDir = os.environ['SITUATIONVIEW_DIR']
	svgPath = os.path.join(svDir, 'bricks', svgName)
	return os.path.isfile(svgPath)

################################################################################
def genericMapping(symbol, postfix):
	transforms = {
		'_Up_': '_U_',
		'_Down_': '_D_',
		'_Left_': '_L_',
		'_Right_': '_R_',
		'_Horizontal_': '_H_',
		'_Vertical_': '_V_'
	}
	revisedSymbol = symbol
	for k in transforms.keys():
		revisedSymbol = revisedSymbol.replace(k, transforms[k])

	removePrefix=[7,11]
	for rf in removePrefix:
		newSymbol = revisedSymbol 
		newSymbol = newSymbol[rf:-len('_symbol.xml')]
		newSymbol = newSymbol + postfix + '.svg'
		if isSVGExisted(newSymbol):
			return newSymbol
	return symbol

################################################################################
def getMOSVG(x, y):
	xOffset = 40
	x = x - xOffset
	svgId = 'Decor_MO_40x40_%(x)f_%(y)f' % locals()
	svgContent = '''
<svg contentScriptType="text/ecmascript" xmlns:xlink="http://www.w3.org/1999/xlink" zoomAndPan="magnify" contentStyleType="text/css" preserveAspectRatio="xMidYMid meet" xmlns="http://www.w3.org/2000/svg" version="1.0">
  <g id="%(svgId)s" transform="translate(%(x)f %(y)f)">
    <rect fill="#989898" width="40" height="40" class="AnRectangle"/>
    <text x="7" y="32" fill="#FFFF00" font-family="sans-serif" font-size="32" class="AnText">M</text>
  </g>
</svg>
''' % locals()
	return (svgId, svgContent)

def getMOSVGReversed(x, y):
	xOffset = 40
	x = x - xOffset
	svgId = 'Decor_MO_40x40_%(x)f_%(y)f_R270' % locals()

	temp = -y
	y = -(x + xOffset)
	x = temp - xOffset
	svgContent = '''
<svg contentScriptType="text/ecmascript" xmlns:xlink="http://www.w3.org/1999/xlink" zoomAndPan="magnify" contentStyleType="text/css" preserveAspectRatio="xMidYMid meet" xmlns="http://www.w3.org/2000/svg" version="1.0">
  <g id="%(svgId)s" transform="translate(%(x)f %(y)f)">
    <rect fill="#989898" width="40" height="40" class="AnRectangle"/>
    <text x="7" y="32" fill="#FFFF00" font-family="sans-serif" font-size="32" class="AnText">M</text>
  </g>
</svg>
''' % locals()
	return (svgId, svgContent)

################################################################################
def saveMOSVG(svgName, svgContent):
	import os
	svDir = os.environ['SITUATIONVIEW_DIR']
	svgPath = os.path.join(svDir, 'bricks', svgName)

	with open(svgPath, 'wb') as f:
		f.write(svgContent)
		return True
	return False 

def updateBricksDefinition(svgId, svgName):
	import os
	from insert_xml_node import insert_text_to_xml

	svDir = os.environ['SITUATIONVIEW_DIR']
	bricksDefinition = os.path.join(svDir, 'brick_definitions.xml')
	textToAdd = ['    <brick id="%(svgId)s" filename="bricks/%(svgName)s"></brick>' % locals()]

	with open(bricksDefinition, 'r+') as f:
		insert_text_to_xml(f, textToAdd, 'bricks')
		return True
	return False

################################################################################
def openSymbol(symbol):
	import os
	svDir = os.environ['SITUATIONVIEW_DIR']
	symbolPath = os.path.join(svDir, 'bricks', symbol)
	return open(symbolPath, 'rb')

def saveSVG(id, name, svgContent):
	# check if svg is already created
	if isSVGExisted(name): return True

	# save the svg
	if saveMOSVG(name, svgContent):
		# register the svg in brick definition
		if updateBricksDefinition(id, name):
			return True

			return False

def manualOverrideMapping(symbol, postfix):
	from get_mwt_svg_rect import parse_svg_file_rect_size  

	mapped = genericMapping(symbol, postfix)
	try:
		rect = None
		with openSymbol(mapped) as f:
			rect = parse_svg_file_rect_size(f)

		# top right corner
		svgId, svgContent = getMOSVG(rect[0] / 2, rect[1] / -2)
		svgName = svgId + '.svg'

		if saveSVG(svgId, svgName, svgContent):
			svgRId, svgRContent = getMOSVGReversed(rect[0] / 2, rect[1] / -2)
			svgRName = svgRId + '.svg'
			saveSVG(svgRId, svgRName, svgRContent)
			return svgName
	except: pass

	return mapped

################################################################################
def main():
	import argparse
	ap = argparse.ArgumentParser(
		description='Specific logic for generating point management representation for 1166B',
		formatter_class=argparse.ArgumentDefaultsHelpFormatter
	)
	ap.add_argument(
		'-s', '--symbol',
		required=True,
		help='Input MWT symbol filename'
	)
	ap.add_argument(
		'-p', '--postfix',
		required=True,
		help='Input postfix'
	)
	args = ap.parse_args()

	mappedFile = None
	if args.postfix == '_State_MO':
		mappedFile = manualOverrideMapping(args.symbol, args.postfix)
	else:
		mappedFile = genericMapping(args.symbol, args.postfix)
	print mappedFile
	return 0

################################################################################
if __name__ == '__main__':
	import sys
	retCode = main()
	sys.exit(retCode)
