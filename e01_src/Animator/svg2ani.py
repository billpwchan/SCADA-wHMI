# standard library import
import sys, os
import csv
import argparse

# extra library
import svg
# add support for ESRI shape format
import shapefile

import inspect
from __builtin__ import isinstance

def buildStyle(elt):
    style = ''
    if elt is not None and elt.get('style') is not None:
        style = elt.get('style')
        
    if elt is not None and elt.get('fill') is not None:
        style = style + ';fill:' + elt.get('fill')
    
    if elt is not None and elt.get('stroke') is not None:
        style = style + ';stroke:' + elt.get('stroke')
    if elt is not None and elt.get('stroke-width') is not None:
        style = style + ';stroke-width:' + elt.get('stroke-width')
     
    
    return style

class SvgPath(svg.Path):
    '''SVG <path>'''
    # class Path handles the <path> tag
    tag = 'path'

    def __init__(self, elt=None):
        svg.Path.__init__(self, elt)
        self.style = buildStyle(elt)
        if elt is not None and elt.get('class') is not None:
            self.cssClass = elt.get('class').split(' ')

class SvgGroup(svg.Group):
    # class hiding official tag to add class
    tag = 'g'

    def __init__(self, elt=None):
        svg.Group.__init__(self, elt)
        if elt is not None and elt.get('class') is not None:
            self.cssClass = elt.get('class').split(' ')
            
    def bbox(self):
        '''Bounding box'''
        bboxes = [x.bbox() for x in self.items]
        if len(bboxes) == 0:
            return (svg.Point(1000000,1000000), svg.Point(-1000000,-1000000))
        else:
            xmin = min([b[0].x for b in bboxes])
            xmax = max([b[1].x for b in bboxes])
            ymin = min([b[0].y for b in bboxes])
            ymax = max([b[1].y for b in bboxes])
            
            return (svg.Point(xmin,ymin), svg.Point(xmax,ymax))
        

class SvgLine(svg.Line):
    # class hiding official tag to add class
    tag = 'line'

    def __init__(self, elt=None):
        svg.Transformable.__init__(self, elt)
        self.style = ''
        if elt is not None and elt.get('class') is not None:
            self.cssClass = elt.get('class').split(' ')
        self.style = buildStyle(elt)   
        if elt is not None:
            self.P1 = svg.Point(self.xlength(elt.get('x1')),
                            self.ylength(elt.get('y1')))
            self.P2 = svg.Point(self.xlength(elt.get('x2')),
                            self.ylength(elt.get('y2')))
            self.segment = svg.Segment(self.P1, self.P2)
            
    def __repr__(self):
        return '<Line ' + self.id + '>'

    def bbox(self):
        '''Bounding box'''
        xmin = min([p.x for p in (self.P1, self.P2)])
        xmax = max([p.x for p in (self.P1, self.P2)])
        ymin = min([p.y for p in (self.P1, self.P2)])
        ymax = max([p.y for p in (self.P1, self.P2)])

        return (Point(xmin,ymin), Point(xmax,ymax))

    def transform(self, matrix):
        print self.matrix
        self.P1 = self.matrix * self.P1
        self.P2 = self.matrix * self.P2
        self.segment = svg.Segment(self.P1, self.P2)
        print self.segment.segments()

    def segments(self, precision=0):
        print self.segment.segments()
        return [self.segment.segments()]

    def simplify(self, precision):
        return self.segments(precision)
            
class SvgRect(svg.Rect):
    # class hiding official tag to add class
    tag = 'rect'

    def __init__(self, elt=None):
        svg.Rect.__init__(self, elt)
        if elt is not None and elt.get('class') is not None:
            self.cssClass = elt.get('class').split(' ')
        self.style = buildStyle(elt)
             
class SvgPolyline(svg.Transformable):
    '''SVG <polyline class="fil7" points="10969,10903 10969,10949 10966,10949 10966,10903 "/>'''
    # class SvgPolyline handles the <polyline> tag
    tag = 'polyline'

    def __init__(self, elt=None):
        svg.Transformable.__init__(self, elt)
        if elt is not None:
            self.style = buildStyle(elt)
            if elt.get('class') != None:
                self.cssClass = elt.get('class').split(' ')
            pts = elt.get('points').split(' ')
            self.ptList = []
            for pt in pts:
                coord = pt.split(',')
                if len(coord) == 2:
                    self.ptList.append( svg.Point(float(coord[0]), float(coord[1]) ))

    def __repr__(self):
        return '<Polyline ' + self.id + '>'
        
    def bbox(self):
        '''Bounding box'''
        xmin = min([p.x for p in self.ptList])
        xmax = max([p.x for p in self.ptList])
        ymin = min([p.y for p in self.ptList])
        ymax = max([p.y for p in self.ptList])

        return (svg.Point(xmin,ymin), svg.Point(xmax,ymax))

    def transform(self, matrix):
        self.ptList = [self.matrix * p for p in self.ptList]

    def segments(self, precision=0):
        return [self.ptList]

    def simplify(self, precision):
        return self.segments(precision)
    
class SvgPolygon(SvgPolyline):
    '''SVG <polygon class="fil7" points="10969,10903 10969,10949 10966,10949 10966,10903 "/>'''
    # class SvgPolygon handles the <polygon> tag
    tag = 'polygon'

    def __init__(self, elt=None):
        SvgPolyline.__init__(self, elt)
        pt = self.ptList[0]
        self.ptList.append(pt)
        
    def __repr__(self):
        return '<Polygon ' + self.id + '>'

class SvgEllipse(svg.Ellipse):
    '''SVG <ellipse>'''
    # class Ellipse handles the <ellipse> tag
    tag = 'ellipse'

    def __init__(self, elt=None):
        svg.Ellipse.__init__(self, elt)
        if elt is not None and elt.get('class') is not None:
            self.cssClass = elt.get('class').split(' ')
        self.style = buildStyle(elt)

# A circle is a special type of ellipse where rx = ry = radius
class SvgCircle(svg.Circle):
    '''SVG <circle>'''
    # class Circle handles the <circle> tag
    tag = 'circle'

    def __init__(self, elt=None):
        svg.Circle.__init__(self, elt)
        if elt is not None and elt.get('class') is not None:
            self.cssClass = elt.get('class').split(' ')
        self.style = buildStyle(elt)

    
        
class SvgText(svg.Transformable):
    '''SVG <text>'''
    # class Line handles the <line> tag
    tag = 'text'

    def __init__(self, elt=None):
        svg.Transformable.__init__(self, elt)
        if elt is not None:
            self.style = buildStyle(elt)
            if elt.get('class') != None:
                self.cssClass = elt.get('class').split(' ')
            try:
                if elt.text != None:
                    self.text = elt.text.decode('utf-8', errors='ignore')
                else:
                    self.text = u''
            except:
                self.text = u''
            
            l = len(self.text) + 1
            
            P1 = svg.Point(self.xlength(elt.get('x')),
                            self.ylength(elt.get('y')))
            P2 = svg.Point(P1.x + l*12, P1.y)
            
            self.ptList = [P1, P2]
   
            #print "svg text",self.text

    def __repr__(self):
        return '<Text ' + self.id + '>'

    def bbox(self):
        '''Bounding box'''
        xmin = min([p.x for p in self.ptList])
        xmax = max([p.x for p in self.ptList])
        ymin = min([p.y for p in self.ptList])
        ymax = max([p.y for p in self.ptList])

        return (svg.Point(xmin,ymin), svg.Point(xmax,ymax))

    def transform(self, matrix):
        self.ptList = [self.matrix * p for p in self.ptList]

    def segments(self, precision=0):
        return [self.ptList]

    def simplify(self, precision):
        return self.segments(precision)
    
    
class SvgPalette(object):
    
    STROKE_TYPE = 0
    FILL_TYPE = 1
    
    def __init__(self, ptype, color):
        self.m_type = ptype
        self.m_color = color
        self.m_width = 0
    
    def __repr__(self):
        if self.m_type == SvgPalette.STROKE_TYPE:
            return 'stroke:' + self.m_color + ';' + 'stroke-width:' + str(self.m_width)
        elif self.m_type == SvgPalette.FILL_TYPE:
            return 'fill:' + self.m_color

def dumpHeader(fout, name):
    print >>fout, '// Animator Version: ANIMATOR2007 4.10.0'
    print >>fout, '// File generated: svg2ani'
    print >>fout, '// Model: %s.ans' % (name)
    print >>fout, ''
    print >>fout, 'Version = "41000"'
    print >>fout, ''
    print >>fout, 'Symbol'
    print >>fout, '{'
    print >>fout, '  PrimitiveSet'
    print >>fout, '  {'
    
def dumpFooter(fout):
    print >>fout, '  }'
    print >>fout, '}'

def getStyleInfo(st):
    # style="fill:#FFF;stroke:#000;stroke-width:12.5;stroke-linejoin:round
    ret = {}
    for entry in st.split(';'):
        v = entry.strip().split(':')
        if len(v) == 2:
            ret[v[0].strip()] = v[1].strip()
    
    return ret
# Polygon {
#   Points = { (0, -11), (-12, -11), (-12, 9), (0, 9), (0, -11)      }
#   Filled = True  # for filled polygon 
#   Palette = { "background","foreground","default","solid","solid",1 }
# }

def dumpSvg(node, indent, styleMap, fout, colmgr):
    if isinstance(node, svg.Transformable):
        # print indent, node.__class__.__name__, node.__class__.__dict__['tag']
        if not isinstance(node, svg.Group):
            col = 'black'
            width = 1
            text = ''
            isFilled = False
            isLine = False
            fillColor = ''

            palette = getStyleInfo(node.style)
            
            if 'fill' in palette and palette['fill'] != 'none':
                isFilled = True
                fillColor = colmgr.getColorCode(palette['fill'])
                
            if 'stroke' in palette and palette['stroke'] != 'none':
                isLine = True
                col = colmgr.getColorCode(palette['stroke'])
                
            if 'stroke-width' in palette:
                width = int(node.length(palette['stroke-width'], 'x'))

            if isinstance(node, SvgText):
                style = 'TEXT'
                text = node.text
                if len(text) == 0:
                    text = "#"
                col = fillColor
                width = 8
            else:
                for seg in node.segments(1):
                  if isFilled:
                      print >>fout, '    Polygon {'
                      print >>fout, '      Points = { ',
                      first = True
                      
                      for pt in seg:
                          if not first:
                              print >>fout, ', ',
                          else:
                              first = False
                          print >>fout, '(%d, %d)' % (pt.x,pt.y) ,
                          
                      print >>fout, '}'
                      print >>fout, '      Filled = True'
                      print >>fout, '      Palette = { "background","%s","default","solid","solid",%d }' % (fillColor, width)
                      print >>fout, '    }'
                    
                  if isLine:
                      print >>fout, '    Polyline {'
                      print >>fout, '      Points = {',
                      first = True 
                      for pt in seg:
                          if not first:
                              print >>fout, ', ',
                          else:
                              first = False
                          print >>fout, '(%d, %d)' % (pt.x,pt.y) ,
                      print >>fout, '}'
                      print >>fout, '      Palette = { "background","%s","default","solid","solid",%d }' % (col, width)
                      print >>fout, '    }'

        for n in node.items:
            dumpSvg(n, indent+'  ', styleMap, fout, colmgr)


    
def line2polygon(node, width):
    shp = []
    for seg in node.segments(0.1):
        
        count = len(seg)
        for i in range(count-1):
            plsh = []
            p1 = seg[i]
            p2 = seg[i+1]
            
            # calculate normal vector
            sv = p2 - p1
            length = sv.length()
            if length > 0.0:
                sv.x = sv.x / length
                sv.y = sv.y / length
                svn = svg.Point(-sv.y, sv.x) * (float(width)/20.0)
                svnr = svg.Point(-svn.x, -svn.y)
                # translate
                p11 = p1 + svn
                p21 = p2 + svn
                p12 = p1 + svnr
                p22 = p2 + svnr
                
                plsh.append([p12.x,p12.y])
                plsh.append([p11.x,p11.y])
                plsh.append([p21.x,p21.y])
                plsh.append([p22.x,p22.y])
                plsh.append([p12.x,p12.y])
                shp.append(plsh)
            else:
                pass
                       
    return shp
    
def changeMatrix(node, matrix):
    if isinstance(node, svg.Transformable):
        node.matrix =  matrix        
        for n in node.items:
            changeMatrix(n, matrix)

class ColorMgr(object):
    def __init__(self):
        self.m_colorTab = {}
        self.m_colorNameTab = {}
        self.m_curId = 1
        
    def getColorCode(self, col):
        
        if col in self.m_colorTab:
            return self.m_colorTab[col]
            
        name = "SVG_COL%03d" % (self.m_curId)
        self.m_curId = self.m_curId + 1
        self.m_colorTab[col] = name
        self.m_colorNameTab[name] = col
        return name
    
    def loadColor(self, fname):
        try:
            with open(fname, 'rb') as csvfile:
                classReader = csv.DictReader(csvfile, delimiter=',', skipinitialspace='True')
                for row in classReader:
                    self.m_curId = self.m_curId + 1
                    self.m_colorTab[row['COLOR_DEF']] = row['ANI_COLOR_NAME']
                    self.m_colorNameTab[row['ANI_COLOR_NAME']] = row['COLOR_DEF']
        except:
            pass
   
    def dumpColor(self, fname):
        
        with open(fname, 'wb') as csvfile:
            csvwriter = csv.writer(csvfile, delimiter=',', quotechar="'", quoting=csv.QUOTE_MINIMAL)
            csvwriter.writerow(['ANI_COLOR_NAME','COLOR_DEF', 'AN_DEF'])
            ks = self.m_colorNameTab.keys()
            ks.sort()
            
            for k in ks:
                andef = self.m_colorNameTab[k]
               
                if andef[0] == '#':
                    if len(andef) == 4:
                        r = int(andef[1], base=16) * 65535.0 / 15.0
                        g = int(andef[2], base=16) * 65535.0 / 15.0
                        b = int(andef[3], base=16) * 65535.0 / 15.0
                    else:
                        r = int(andef[1:3], base=16) * 65535.0 / 255.0
                        g = int(andef[3:5], base=16) * 65535.0 / 255.0
                        b = int(andef[5:], base=16) * 65535.0 / 255.0
                    
                    andef = 'Define Color "%s" { Value = RGB(%d, %d, %d);}' % (k, r, g, b)
                
                csvwriter.writerow([k, self.m_colorNameTab[k], andef])

if __name__ == "__main__":
    # create option parser
    parser = argparse.ArgumentParser()
    parser.add_argument("-s", "--svgfile", dest="svgfile",
                      help="svg file name (default .)", default="symbol_frame.svg")
    
    options = parser.parse_args()
    
    # Register all classes with attribute 'tag' in svgClass dict
    for name, cls in inspect.getmembers(sys.modules[__name__], inspect.isclass):
        tag = getattr(cls, 'tag', None)
        if tag:
            svg.svgClass[svg.svg_ns + tag] = cls
        
        
    f = svg.parse(options.svgfile)
    
    # collect style information
    styleMap = {}
    for n in f.root.iter(svg.svg_ns + 'style'):
        for l in n.text.split('}'):
            st = l.split('{')
            if len(st) == 2:
                cssname = st[0].strip()
                params = [elt.strip() for elt in st[1].split(';')]
                if params[0].startswith('stroke'):
                    pal = SvgPalette(SvgPalette.STROKE_TYPE, params[0].split(':')[1])
                    styleMap[str(st[0].strip())] = pal
                    for p in params[1:]:
                        if p.startswith('stroke-width'):
                            pal.m_width = float(p.split(':')[1])
                            
                if params[0].startswith('fill'):
                    col = params[0].split(':')[1]
                    if col != 'none':
                        styleMap[str(st[0].strip())] = SvgPalette(SvgPalette.FILL_TYPE, col)

    # f.transform()
    
    # open symbol file
    name = options.svgfile[:-4]
    pth = os.path.split(options.svgfile[:-4]+'.svg')[0]
    if pth and not os.path.exists(pth):
        os.makedirs(pth)
    fout = open(name +'.ans', 'w') 
    dumpHeader(fout, name)
    
    colmgr = ColorMgr()
    colmgr.loadColor('colmap.csv')
    
    dumpSvg(f.items[0], '', styleMap, fout, colmgr)
    dumpFooter(fout)
    colmgr.dumpColor('colmap.csv')
    
    print "done"
