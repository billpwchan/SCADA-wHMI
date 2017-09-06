#!/usr/bin/python

class BrickDefinition:
    NS = 'http://www.thalesgroup.com/hv/mwt/conf/situationview/brick'
    def __init__(self):
        self.bricks = {}
    def __init__(self, fileName):
        import xml.etree.ElementTree as ET
        tree = ET.parse(fileName)
        root = tree.getroot()
        self.bricks = {}
        for brick in root.findall('{%s}brick' % BrickDefinition.NS):
            id = brick.attrib['id']
            filename = brick.attrib['filename']
            self.bricks[id] = filename
#        print self.bricks

    def __len__(self): return len(self.bricks)
    def __str__(self): return str(self.bricks)

    def has(self, id):
        return self.bricks.has_key(id)
    def get(self, id):
        return self.bricks.get(id)
    def set(self, id, filename):
        self.bricks[id] = filename
    def pop(self, id):
        return self.bricks.pop(id)

    def write(self, fileName):
        import xml.etree.ElementTree as ET
        with open(fileName, 'w') as o:
            ET.register_namespace('', BrickDefinition.NS)
            root = ET.Element('{%s}bricks' % BrickDefinition.NS)
            for key in sorted(self.bricks.keys()):
                brick = ET.SubElement(root, '{%s}brick' % BrickDefinition.NS)
                brick.attrib = {'id': key, 'filename': self.bricks[key]}
            tree = ET.ElementTree(root)
            tree.write(o, encoding='UTF-8')

################################################################################
def main():
    import argparse

    ap = argparse.ArgumentParser(
        description='Parse HV brick_definitions.xml',
        formatter_class=argparse.ArgumentDefaultsHelpFormatter
    )
    ap.add_argument(
        '-i', '--input',
        type=argparse.FileType('r'),
        required=True,
        help='Input brick_defintions.xml file'
    )
    ap.add_argument(
        '-o', '--output',
        type=argparse.FileType('w'),
        required=True,
        help='Ouput brick_defintions.xml file'
    )

    args = ap.parse_args()

    bd = BrickDefinition(args.input.name)
    bd.write(args.output.name)
    return 0

################################################################################
if __name__ == '__main__':
    import sys
    retCode = main()
    sys.exit(retCode)
