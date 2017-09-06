#!/usr/bin/python

################################################################################
import os
import re
import glob

################################################################################
def getSituationViewDirectory(arg):
    svDir = arg
    if not svDir and os.environ.has_key('SITUATIONVIEW_DIR'):
        svDir = os.environ['SITUATIONVIEW_DIR']

    if svDir:
        if os.path.isdir(svDir):
            return svDir
    return None

################################################################################
def getSymbolList(svDir):
    symbolPath = os.path.join(svDir, 'symbols')
    return glob.glob(os.path.join(symbolPath, '*_symbol.xml'))

################################################################################
def getSymbolOverrideList(symbolList):
    replaces = [
        (r'([^_]+)_([^_]+)_(.*)_symbol.xml', r'\3_State_MO.svg'),
        (r'_Up', r'_U'),
        (r'_Down', r'_D'),
        (r'_Left', r'_L'),
        (r'_Right', r'_R'),
        (r'_Horizontal', r'_H'),
        (r'_Vertical', r'_V')
    ]

    matches = symbolList
    for r in replaces:
        matches = [re.sub(r[0], r[1], i) for i in matches]

    print matches

################################################################################
def main():
    import argparse
    ap = argparse.ArgumentParser(
        description='Insert override indications to symbols',
        formatter_class=argparse.ArgumentDefaultsHelpFormatter
    )
    ap.add_argument(
        '-d', '--directory',
        default=None,
        help='Input directory (default obtained from environment variable "SITUATIONVIEW_DIR")'
    )
    args = ap.parse_args()

    svDir = getSituationViewDirectory(args.directory)
    if not svDir:
        print '[ERROR] Cannot find Situation View Directory'
        return -1
    print '[INFO] SituationView Directory: %s' % svDir

    symbolList = getSymbolList(svDir)
#    print symbolList

    symbolOverrideList = getSymbolOverrideList(symbolList)
    print symbolOverrideList

################################################################################
if __name__ == '__main__':
    import sys
    retCode = main()
    sys.exit(retCode)
