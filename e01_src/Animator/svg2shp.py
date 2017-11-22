# standard library import
import sys

import argparse

# extra library
import svg
# add support for ESRI shape format
import shapefile

import inspect
from __builtin__ import isinstance


class SvgPath(svg.Path):
    '''SVG <path>'''
    # class Path handles the <path> tag
    tag = 'path'

    def __init__(self, elt=None):
        svg.Path.__init__(self, elt)
        self.cssClass = []
        if elt is not None and elt.get('class') is not None:
            self.cssClass = elt.get('class').split(' ')
        if elt is not None and elt.get('style') is not None:
            self.style = elt.get('style')
        else:
            self.style = ''

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
        self.cssClass = []
        svg.Line.__init__(self, elt)
        if elt is not None and elt.get('class') is not None:
            self.cssClass = elt.get('class').split(' ')
        if elt is not None and elt.get('style') is not None:
            self.style = elt.get('style')
            
class SvgRect(svg.Rect):
    # class hiding official tag to add class
    tag = 'rect'

    def __init__(self, elt=None):
        self.cssClass = []
        svg.Rect.__init__(self, elt)
        if elt is not None and elt.get('class') is not None:
            self.cssClass = elt.get('class').split(' ')
        if elt is not None and elt.get('style') is not None:
            self.style = elt.get('style')
             
class SvgPolyline(svg.Transformable):
    '''SVG <polyline class="fil7" points="10969,10903 10969,10949 10966,10949 10966,10903 "/>'''
    # class SvgPolyline handles the <polyline> tag
    tag = 'polyline'

    def __init__(self, elt=None):
        self.cssClass = []
        svg.Transformable.__init__(self, elt)
        if elt is not None:
            self.style = elt.get('style')
            if elt.get('class') != None:
                print elt.get('class')
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
    
class SvgText(svg.Transformable):
    '''SVG <text>'''
    # class Line handles the <line> tag
    tag = 'text'

    def __init__(self, elt=None):
        self.cssClass = []
        svg.Transformable.__init__(self, elt)
        if elt is not None:
            self.style = elt.get('style')
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

def getStyleInfo(st):
    # style="fill:#FFF;stroke:#000;stroke-width:12.5;stroke-linejoin:round
    ret = {}
    for entry in st.split(';'):
        v = entry.strip().split(':')
        if len(v) == 2:
            ret[v[0].strip()] = v[1].strip()
    
    return ret

def dumpSvg(node, indent, m_shape, m_shapePlg, styleMap, sindex):
    if isinstance(node, svg.Transformable):
        print indent, node.__class__.__name__, node.__class__.__dict__['tag']
        if not isinstance(node, svg.Group):
            col = 'black'
            style = 'LINE'
            width = 1
            text = ''
            isFilled = False
            isLine = False
            fillColor = ''
            cssClass = ''
            
            palette = getStyleInfo(node.style)
         
            if 'fill' in palette and palette['fill'] != 'none':
                isFilled = True
                fillColor = palette['fill']
                
            if 'stroke' in palette and palette['stroke'] != 'none':
                isLine = True
                col = palette['stroke']
                
            if 'stroke-width' in palette:
                width = int(node.length(palette['stroke-width'], 'x'))
                
            
            if isinstance(node, SvgText):
                style = 'TEXT'
                text = node.text
                if len(text) == 0:
                    text = "#"
                col = fillColor
                width = 8
                
            shp = []
            if isFilled and not isinstance(node, SvgText):
                for seg in node.segments(5):
                    plsh = []
                    for pt in seg:
                        plsh.append([pt.x,pt.y])
                        
                    shp.append(plsh)
            else:
                shp = line2polygon(node, width)
                    
            if isFilled and not isinstance(node, SvgText):
                m_shape.poly(shapeType=shapefile.POLYGON, parts=shp)
                m_shape.record(node.__class__.__dict__['tag'], 'level ' + str(sindex), fillColor, 'FILLED', 0, 'solid', '', '', '', '', cssClass, str(node.cssClass), '')
                
            if isLine and not isinstance(node, SvgText):
                m_shape.poly(shapeType=shapefile.POLYGON, parts=shp)
                m_shape.record(node.__class__.__dict__['tag'], 'level ' + str(sindex), col, style, width, 'solid', text, 'SimSun', 'Normal', 'Normal', cssClass, str(node.cssClass), '')
            elif isinstance(node, SvgText):
                m_shape.poly(shapeType=shapefile.POLYLINE, parts=shp)
                m_shape.record(node.__class__.__dict__['tag'], 'level ' + str(sindex), col, style, width, 'solid', text, 'SimSun', 'Normal', 'Normal', cssClass, str(node.cssClass), '')
 
            
            sindex = sindex + 1
        
        for n in node.items:
            sindex = dumpSvg(n, indent+'  ', m_shape, m_shapePlg, styleMap, sindex)
            
    return sindex

def line2polygon(node, width):
    shp = []
    for seg in node.segments(5):
        
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
            
if __name__ == "__main__":
    # create option parser
    parser = argparse.ArgumentParser()
    parser.add_argument("-s", "--svgfile", dest="svgfile",
                      help="svg file name (default .)", default="Layout_MAT-1M.svg")
    
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
        
    print styleMap
    # create shapefile schema for polyline
    m_shape = shapefile.Writer(shapeType=shapefile.POLYLINE)
    m_shape.autoBalance = 1
    # field type C = Character, L = boolean, D = Date, N = Numbers decimal is number of digit after . (0 is int)
    m_shape.field('SCS_LAYER')
    m_shape.field('SCS_LEVEL')
    m_shape.field('SCS_COLOR')
    m_shape.field('SCS_STYLE')
    m_shape.field('SCS_WIDTH', fieldType="N", size="9", decimal=0)
    m_shape.field('SCS_DASH')
    m_shape.field('SCS_TEXT')
    m_shape.field('SCS_FONT')
    m_shape.field('SCS_FTSTYL')
    m_shape.field('SCS_FTWGT')
    m_shape.field('SCS_PATH')
    m_shape.field('CMN_ACLA')
    m_shape.field('CMN_EID')
       
    # create shapefile schema for polygon
    m_shapePlg = shapefile.Writer(shapeType=shapefile.POLYGON)
    m_shapePlg.autoBalance = 1
    # field type C = Character, L = boolean, D = Date, N = Numbers decimal is number of digit after . (0 is int)
    m_shapePlg.field('SCS_LAYER')
    m_shapePlg.field('SCS_LEVEL')
    m_shapePlg.field('SCS_COLOR')
    m_shapePlg.field('SCS_STYLE')
    m_shapePlg.field('SCS_WIDTH', fieldType="N", size="9", decimal=0)
    m_shapePlg.field('SCS_DASH')
    m_shapePlg.field('SCS_TEXT')
    m_shapePlg.field('SCS_FONT')
    m_shapePlg.field('SCS_FTSTYL')
    m_shapePlg.field('SCS_FTWGT')
    m_shapePlg.field('SCS_PATH')
    m_shapePlg.field('CMN_ACLA')
    m_shapePlg.field('CMN_EID')
    
    (p1, p2) = f.bbox()
    f.matrix = svg.Matrix([1, 0, 0, -1, 0, p2.y-p1.y])
    changeMatrix(f.items[0], f.matrix)
    f.transform()
    
    dumpSvg(f.items[0], '', m_shape, m_shapePlg, styleMap, 1)
    
    if len(m_shape._shapes) > 0:
        m_shape.save(options.svgfile[:-4]+"pll")
        
    if len(m_shapePlg._shapes) > 0:
        m_shapePlg.save(options.svgfile[:-4]+"plg")    
        
    
        
    print "done"
    
            