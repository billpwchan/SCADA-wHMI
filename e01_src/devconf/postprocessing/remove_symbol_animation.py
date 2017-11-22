#!/usr/bin/python

################################################################################
def write_file(output, content):
    output.seek(0)
    output.truncate(0)
    output.writelines(content)
    return True

################################################################################
def read_file(input):
    return reduce(lambda x,y:x+y,input.readlines())

################################################################################
def remove_animation_node(text, name):
    import re
    pattern = r'^\s*<animationRule>\s*?<source[^>]*?name\s*=\s*"%s".*?</animationRule>\s*^' % name
    text = re.sub(pattern, '', text, flags = re.DOTALL | re.MULTILINE)
    return text

################################################################################
def main():
    import argparse

    ap = argparse.ArgumentParser(
        description='Remove animation node by name from HV symbol',
        formatter_class=argparse.ArgumentDefaultsHelpFormatter
    )
    ap.add_argument(
        '-i', '--input',
        type=argparse.FileType('r+'),
        required=True,
        help='Input and output XML file'
    )
    ap.add_argument(
        '-n', '--name',
        required=True,
        help='Name of the animation to be removed'
    )

    args = ap.parse_args()

    text = None
    with args.input:
        text = read_file(args.input)
        if not text:
            print '[ERROR] Failed in reading xml file: %s' % args.input.name
            return -1
#        print text

        text = remove_animation_node(text, args.name)
#        print text

        ret = write_file(args.input, text)
        if not ret:
            print '[ERROR] Failed in writing xml file: %s' % args.input.name
            return -3
    return 0

################################################################################
if __name__ == '__main__':
    import sys
    retCode = main()
    sys.exit(retCode)
