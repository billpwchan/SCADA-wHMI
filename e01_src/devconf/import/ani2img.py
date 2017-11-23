# ani2img.py
# Read animator image file and convert to ESRI shape format
# this converter had to be used with a corresponding SLD (see SLD_FILE constant)
# for proper geoserver rendering use the gridset defined in ANI_GRID_SET constant
# with a metatile of 14x14 and a gutter of 50

# standard library import
import sys, math, datetime
import os, re, ast
import csv
import traceback
from itertools import izip
import xml.dom.minidom
import xml.sax.saxutils
import xml.etree.ElementTree as et
#import pprint
import logging
import argparse
import subprocess
import shutil

# extra library
# add support for ESRI shape format V1.2.1
import shapefile
# add library to build animator property file parser V2.0.3
import pyparsing

ShapeTypeName = { shapefile.NULL: "Null",
shapefile.POINT: "POINT",
shapefile.POLYLINE: "POLYLINE",
shapefile.POLYGON: "POLYGON",
shapefile.MULTIPOINT: "MULTIPOINT",
shapefile.POINTZ: "POINTZ",
shapefile.POLYLINEZ: "POLYLINEZ",
shapefile.POLYGONZ: "POLYGONZ",
shapefile.MULTIPOINTZ: "MULTIPOINTZ",
shapefile.POINTM: "POINTM",
shapefile.POLYLINEM: "POLYLINEM",
shapefile.POLYGONM: "POLYGONM",
shapefile.MULTIPOINTM: "MULTIPOINTM",
shapefile.MULTIPATCH: "MULTIPATCH"}

# Define mapping between Animator font name and geoserver font name
# If you need to use non ASCII character you may need to map animator font to another one that support your locale
FONT_MAPPING = { 'courier' : 'Courier New' , 'Courier' : 'Courier New' , 'lucidatypewriter': 'Lucida Sans Typewriter','helv': 'Arial', 'helvetica': 'Arial', 'arial' : 'Arial', 'courier new' : 'Courier New', 'times' : 'Arial', 'new century schoolbook' : 'Arial',
                 'symbol': 'Symbol', 'Arial': 'Arial', 'Courier New' : 'Courier New', 'BatangChe' : 'Arial', 'GulimChe' : 'Arial', 
                 'MS PGothic' : 'Courier New', 'MS PMincho' : 'SimSun',  'SimSun' : 'SimSun', 'SimHei' : 'SimSun', 
                 'NSimSun' : 'SimSun', 'KaiTi_GB2312' : 'SimSun', 'FangSong_GB2312' : 'SimSun', 'PMingLiU': 'SimSun' }

RGB_TO_COLOR_NAMES = {
    (0, 0, 0): ['Black'],
    (0, 0, 128): ['Navy', 'NavyBlue'],
    (0, 0, 139): ['DarkBlue'],
    (0, 0, 205): ['MediumBlue'],
    (0, 0, 255): ['Blue'],
    (0, 100, 0): ['DarkGreen'],
    (0, 128, 0): ['Green'],
    (0, 139, 139): ['DarkCyan'],
    (0, 191, 255): ['DeepSkyBlue'],
    (0, 206, 209): ['DarkTurquoise'],
    (0, 250, 154): ['MediumSpringGreen'],
    (0, 255, 0): ['Lime'],
    (0, 255, 127): ['SpringGreen'],
    (0, 255, 255): ['Cyan', 'Aqua'],
    (25, 25, 112): ['MidnightBlue'],
    (30, 144, 255): ['DodgerBlue'],
    (32, 178, 170): ['LightSeaGreen'],
    (34, 139, 34): ['ForestGreen'],
    (46, 139, 87): ['SeaGreen'],
    (47, 79, 79): ['DarkSlateGray', 'DarkSlateGrey'],
    (50, 205, 50): ['LimeGreen'],
    (60, 179, 113): ['MediumSeaGreen'],
    (64, 224, 208): ['Turquoise'],
    (65, 105, 225): ['RoyalBlue'],
    (70, 130, 180): ['SteelBlue'],
    (72, 61, 139): ['DarkSlateBlue'],
    (72, 209, 204): ['MediumTurquoise'],
    (75, 0, 130): ['Indigo'],
    (85, 107, 47): ['DarkOliveGreen'],
    (95, 158, 160): ['CadetBlue'],
    (100, 149, 237): ['CornflowerBlue'],
    (102, 205, 170): ['MediumAquamarine'],
    (105, 105, 105): ['DimGray', 'DimGrey'],
    (106, 90, 205): ['SlateBlue'],
    (107, 142, 35): ['OliveDrab'],
    (112, 128, 144): ['SlateGray', 'SlateGrey'],
    (119, 136, 153): ['LightSlateGray', 'LightSlateGrey'],
    (123, 104, 238): ['MediumSlateBlue'],
    (124, 252, 0): ['LawnGreen'],
    (127, 255, 0): ['Chartreuse'],
    (127, 255, 212): ['Aquamarine'],
    (128, 0, 0): ['Maroon'],
    (128, 0, 128): ['Purple'],
    (128, 128, 0): ['Olive'],
    (128, 128, 128): ['Gray', 'Grey'],
    (50, 50, 50): ['gray50'],
    (132, 112, 255): ['LightSlateBlue'],
    (135, 206, 235): ['SkyBlue'],
    (135, 206, 250): ['LightSkyBlue'],
    (138, 43, 226): ['BlueViolet'],
    (139, 0, 0): ['DarkRed'],
    (139, 0, 139): ['DarkMagenta'],
    (139, 69, 19): ['SaddleBrown'],
    (143, 188, 143): ['DarkSeaGreen'],
    (144, 238, 144): ['LightGreen'],
    (147, 112, 219): ['MediumPurple'],
    (148, 0, 211): ['DarkViolet'],
    (152, 251, 152): ['PaleGreen'],
    (153, 50, 204): ['DarkOrchid'],
    (154, 205, 50): ['YellowGreen'],
    (160, 82, 45): ['Sienna'],
    (165, 42, 42): ['Brown'],
    (168, 46, 46): ['Brown1'],
    (175, 52, 52): ['Brown2'],
    (169, 169, 169): ['DarkGray', 'DarkGrey'],
    (173, 216, 230): ['LightBlue'],
    (173, 255, 47): ['GreenYellow'],
    (175, 238, 238): ['PaleTurquoise'],
    (176, 196, 222): ['LightSteelBlue'],
    (176, 224, 230): ['PowderBlue'],
    (178, 34, 34): ['Firebrick'],
    (184, 134, 11): ['DarkGoldenrod'],
    (186, 85, 211): ['MediumOrchid'],
    (188, 143, 143): ['RosyBrown'],
    (189, 183, 107): ['DarkKhaki'],
    (192, 192, 192): ['Silver'],
    (199, 21, 133): ['MediumVioletRed'],
    (205, 92, 92): ['IndianRed'],
    (205, 133, 63): ['Peru'],
    (208, 32, 144): ['VioletRed'],
    (210, 105, 30): ['Chocolate'],
    (210, 180, 140): ['Tan'],
    (211, 211, 211): ['LightGray', 'LightGrey'],
    (216, 191, 216): ['Thistle'],
    (218, 112, 214): ['Orchid'],
    (218, 165, 32): ['Goldenrod'],
    (219, 112, 147): ['PaleVioletRed'],
    (220, 20, 60): ['Crimson'],
    (220, 220, 220): ['Gainsboro'],
    (221, 160, 221): ['Plum'],
    (222, 184, 135): ['Burlywood'],
    (224, 255, 255): ['LightCyan'],
    (230, 230, 250): ['Lavender'],
    (233, 150, 122): ['DarkSalmon'],
    (238, 130, 238): ['Violet'],
    (238, 221, 130): ['LightGoldenrod'],
    (238, 232, 170): ['PaleGoldenrod'],
    (240, 128, 128): ['LightCoral'],
    (240, 230, 140): ['Khaki'],
    (240, 248, 255): ['AliceBlue'],
    (240, 255, 240): ['Honeydew'],
    (240, 255, 255): ['Azure'],
    (244, 164, 96): ['SandyBrown'],
    (245, 222, 179): ['Wheat'],
    (245, 245, 220): ['Beige'],
    (245, 245, 245): ['WhiteSmoke'],
    (245, 255, 250): ['MintCream'],
    (248, 248, 255): ['GhostWhite'],
    (250, 128, 114): ['Salmon'],
    (250, 235, 215): ['AntiqueWhite'],
    (250, 240, 230): ['Linen'],
    (250, 250, 210): ['LightGoldenrodYellow'],
    (253, 245, 230): ['OldLace'],
    (255, 0, 0): ['Red'],
    (255, 0, 255): ['Magenta', 'Fuchsia'],
    (255, 20, 147): ['DeepPink'],
    (255, 69, 0): ['OrangeRed'],
    (255, 99, 71): ['Tomato'],
    (255, 105, 180): ['HotPink'],
    (255, 127, 80): ['Coral'],
    (255, 140, 0): ['DarkOrange'],
    (255, 160, 122): ['LightSalmon'],
    (255, 165, 0): ['Orange'],
    (255, 182, 193): ['LightPink'],
    (255, 192, 203): ['Pink'],
    (255, 215, 0): ['Gold'],
    (255, 218, 185): ['PeachPuff'],
    (255, 222, 173): ['NavajoWhite'],
    (255, 228, 181): ['Moccasin'],
    (255, 228, 196): ['Bisque'],
    (255, 228, 225): ['MistyRose'],
    (255, 235, 205): ['BlanchedAlmond'],
    (255, 239, 213): ['PapayaWhip'],
    (255, 240, 245): ['LavenderBlush'],
    (255, 245, 238): ['Seashell'],
    (255, 248, 220): ['Cornsilk'],
    (255, 250, 205): ['LemonChiffon'],
    (255, 250, 240): ['FloralWhite'],
    (255, 250, 250): ['Snow'],
    (255, 255, 0): ['Yellow'],
    (255, 255, 224): ['LightYellow'],
    (255, 255, 240): ['Ivory'],
    (255, 255, 255): ['White']
}

## Building inverse relation
COLOR_NAME_TO_RGB = dict(
    (name.lower(), rgb)
    for rgb, names in RGB_TO_COLOR_NAMES.items()
    for name in names)
    
SLD_FILE = '''
<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Default Styler</sld:Name>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:Title>Animator Line</sld:Title>
      <sld:Abstract>line coming from animator2shape transformation</sld:Abstract>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>SCS_STYLE</ogc:PropertyName>
              <ogc:Literal>FILLED</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">
                <ogc:PropertyName>SCS_COLOR</ogc:PropertyName>
              </sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_STYLE</ogc:PropertyName>
                <ogc:Literal>LINE</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_DASH</ogc:PropertyName>
                <ogc:Literal>solid</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:LineSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Stroke>
              <sld:CssParameter name="stroke">
                <ogc:PropertyName>SCS_COLOR</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:PropertyName>SCS_WIDTH</ogc:PropertyName>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_STYLE</ogc:PropertyName>
                <ogc:Literal>LINE</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_DASH</ogc:PropertyName>
                <ogc:Literal>dot</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:LineSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Stroke>
              <sld:CssParameter name="stroke">
                <ogc:PropertyName>SCS_COLOR</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:PropertyName>SCS_WIDTH</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">1 2</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_STYLE</ogc:PropertyName>
                <ogc:Literal>LINE</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_DASH</ogc:PropertyName>
                <ogc:Literal>doubledot</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:LineSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Stroke>
              <sld:CssParameter name="stroke">
                <ogc:PropertyName>SCS_COLOR</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:PropertyName>SCS_WIDTH</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">1 1 1 3</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_STYLE</ogc:PropertyName>
                <ogc:Literal>LINE</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_DASH</ogc:PropertyName>
                <ogc:Literal>dashdot</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:LineSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Stroke>
              <sld:CssParameter name="stroke">
                <ogc:PropertyName>SCS_COLOR</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:PropertyName>SCS_WIDTH</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">5 2 1 2</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_STYLE</ogc:PropertyName>
                <ogc:Literal>LINE</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_DASH</ogc:PropertyName>
                <ogc:Literal>dash</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:LineSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Stroke>
              <sld:CssParameter name="stroke">
                <ogc:PropertyName>SCS_COLOR</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:PropertyName>SCS_WIDTH</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">5 2</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_STYLE</ogc:PropertyName>
                <ogc:Literal>LINE</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>SCS_DASH</ogc:PropertyName>
                <ogc:Literal>longdash</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:LineSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Stroke>
              <sld:CssParameter name="stroke">
                <ogc:PropertyName>SCS_COLOR</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:PropertyName>SCS_WIDTH</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">10 5</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>

      </sld:FeatureTypeStyle>
      
      <sld:FeatureTypeStyle>
        <sld:Name>text0</sld:Name>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>SCS_STYLE</ogc:PropertyName>
              <ogc:Literal>TEXT</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:TextSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Label>
              <ogc:PropertyName>SCS_TEXT</ogc:PropertyName>
            </sld:Label>
            <sld:Font>
              <sld:CssParameter name="font-family">
                <ogc:PropertyName>SCS_FONT</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="font-size">
                  <ogc:PropertyName>SCS_WIDTH</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="font-style">
                <ogc:PropertyName>SCS_FTSTYL</ogc:PropertyName>
              </sld:CssParameter>
              <sld:CssParameter name="font-weight">
                <ogc:PropertyName>SCS_FTWGT</ogc:PropertyName>
              </sld:CssParameter>
            </sld:Font>
            <sld:LabelPlacement>
              <sld:PointPlacement>
                <sld:AnchorPoint>
                  <sld:AnchorPointX>0</sld:AnchorPointX>
                  <sld:AnchorPointY>0</sld:AnchorPointY>
                </sld:AnchorPoint>
              </sld:PointPlacement>
            </sld:LabelPlacement>
            <sld:Halo>
              <sld:Radius>0</sld:Radius>
              <sld:Fill>
                <sld:CssParameter name="fill">#DDDDDD</sld:CssParameter>
              </sld:Fill>
            </sld:Halo>
            <sld:Fill>
              <sld:CssParameter name="fill">
                <ogc:PropertyName>SCS_COLOR</ogc:PropertyName>
              </sld:CssParameter>
            </sld:Fill>
            <sld:VendorOption name="conflictResolution">false</sld:VendorOption>
            <sld:VendorOption name="goodnessOfFit">0.1</sld:VendorOption>
            <sld:VendorOption name="spaceAround">-1</sld:VendorOption>
          </sld:TextSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
      
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>
'''

ANI_GRID_SET = """   
    <gridSet>
      <name>EPSG:23030</name>
      <srs>
        <number>23030</number>
      </srs>
      <extent>
        <coords>
          <double>-10.0</double>
          <double>-10.0</double>
          <double>10010.0</double>
          <double>10010.0</double>
        </coords>
      </extent>
      <alignTopLeft>false</alignTopLeft>
      <resolutions>
        <double>4.0</double>
        <double>2.0</double>
        <double>1.5</double>
        <double>1.0</double>
        <double>0.8</double>
        <double>0.6</double>
        <double>0.4</double>
        <double>0.2</double>
        <double>0.1</double>
        <double>0.05</double>
      </resolutions>
      <metersPerUnit>1.0</metersPerUnit>
      <pixelSize>2.8E-4</pixelSize>
      <scaleNames>
        <string>EPSG:23030:0</string>
        <string>EPSG:23030:1</string>
        <string>EPSG:23030:2</string>
        <string>EPSG:23030:3</string>
        <string>EPSG:23030:4</string>
        <string>EPSG:23030:5</string>
        <string>EPSG:23030:6</string>
        <string>EPSG:23030:7</string>
        <string>EPSG:23030:8</string>
        <string>EPSG:23030:9</string>
      </scaleNames>
      <tileHeight>256</tileHeight>
      <tileWidth>256</tileWidth>
      <yCoordinateFirst>false</yCoordinateFirst>
    </gridSet>
"""

class AniObject(object):
    
    def __init__(self, fname, objtype):
        g_logger.debug("Create object %s %s", objtype, fname)
        self.m_keywords = []
        self.m_name = fname
        self.m_refCount = 0
        self.m_refObject = None
        self.m_type = objtype
        self.m_version = 41000
        self.m_filename = fname
        self.m_children = []
        self.m_properties = {}
    
    def refObject(self, obj):
        if obj == None:
            return
        
        if self.m_refObject != None:
            g_logger.debug("Replace RefObject of %s with %s", self.m_name, obj.m_name)
            self.m_refCount = self.m_refCount - 1
        else:
            g_logger.debug("Set RefObject of %s to %s", self.m_name, obj.m_name)
            
        obj.m_refCount = obj.m_refCount + 1
        self.m_refObject = obj
        
    def __setattr__(self, name, value):
        if name == "Version":
            self.m_version = value.strip().strip('"')
        else:
            if self.__dict__.has_key('m_keywords') and name[0:2] != 'm_' and name != 'Point':
                self.m_keywords.append(name)
                
            if name == 'Transformer':
                value = Matrix2D(value)
                
            if name == 'Palette':
                value = AnPalette(value)
                
            if name == 'Point':
                value = Point2D(value)
                if 'Points' in self.__dict__:
                    self.Points.append(value)
                    return
                else:
                    self.m_keywords.append('Points')
                    super(AniObject, self).__setattr__('Points', [value])
                    return
                
            if name == 'Points' and len(value) == 1:
                try:
                    ptTab = ast.literal_eval(value[0])
                    value = []
                    for pt in ptTab:
                        value.append(Point2D(str(pt)))
                    if self.m_name == 'Polygon':
                        value.append(value[0])
                        
                except:
                    g_logger.error("Error while setting Points from'%s' (%s)", str(value), sys.exc_type)
                
            if name == 'Default Symbol':
                v = int(value)
                g_logger.debug("set default symbol of %s to num(%d):%s", self.m_name, v, str(self.Symbols))
                if v < len(self.Symbols):
                    self.refObject(self.Symbols[v])
                
            super(AniObject, self).__setattr__(name, value)
    
    def __getattr__(self, attr):
        if attr in self.m_properties:
            return self.m_properties[attr]
        
        if attr == 'Transformer':
            super(AniObject, self).__setattr__('Transformer', None)
            return None
        elif attr == 'Points':
            super(AniObject, self).__setattr__('Points', None)
            return None
        elif attr == 'Point':
            super(AniObject, self).__setattr__('Point', None)
            return None
        elif attr == 'Label':
            super(AniObject, self).__setattr__('Label', None)
            return None
        elif attr == 'Palette':
            super(AniObject, self).__setattr__('Palette', None)
            return None
        elif attr == 'Environment':
            super(AniObject, self).__setattr__('Environment', None)
            return None
        elif attr == 'ObjectPath':
            super(AniObject, self).__setattr__('ObjectPath', None)
            return None
        elif attr == 'Layer':
            super(AniObject, self).__setattr__('Layer', None)
            return None
        elif attr == 'Level':
            super(AniObject, self).__setattr__('Level', None)
            return None
        elif attr == 'Filled':
            super(AniObject, self).__setattr__('Filled', False)
            return False
            
        raise AttributeError("%r object has no attribute %r" % (self.__class__, attr))
                         
    def __str__(self):
        rep = "AniObject file:" + self.m_filename
        if len(self.m_children) > 0:
            rep = rep + " type:" + self.m_children[0].m_type
        rep = rep + " Version:" + str(self.m_version)
        return rep
    
    def __repr__(self):
        rep = self.m_type + "(" + str(self.m_name) + ")"
        # for o in self.m_children:
            # rep = rep + '\n[' + repr(o) + ']'
        return rep
    
    def setProperty(self, p, v):
        if len(v) == 0:
            return
            
        # first elt contains type and value for scalar
        tab = v[0].split()
        if tab[0] == 'AnStringProperty' or tab[0] == 'AnV8CommentProperty':
            if len(tab) >= 2:
                self.m_properties[p] = tab[1].strip('"')
            else:
                self.m_properties[p] = ""
        elif tab[0] == 'AnV8SelectionProperty':
            if len(tab) >= 2 and tab[1] == '1':
                self.m_properties[p] = True
            else:
                self.m_properties[p] = False
        elif tab[0] == 'AnListProperty':
            self.setPropertyList(p, v[1:])
        else:
            g_logger.error("unknown Property type: %s %s", p, v)
            self.m_properties[p] = v
          
    def setPropertyList(self, p, v):
        propMap = {}
        for elt in v:
            tab = elt.split()
            if tab[0] == "AnNamedProperty":
                key = tab[1].strip('"')
                val = ""
                if tab[2] != "AnStringProperty":
                    g_logger.error("Error unknown property subtype: %s", elt)
                else:
                    pos = elt.find("AnStringProperty")
                    val = elt[pos+17:].strip().strip('"')
                propMap[key] = val
            elif tab[0] == "ScsAccessor":
                key = tab[2]
                val = tab[3].strip('"')
                mode = 5
                if len(tab) >= 5:
                    mode = tab[4]
                if mode == '2':
                    propMap[key] = ('read', val)
                elif mode == '4':
                    propMap[key] = ('poll', val)
                elif mode == '5':
                    propMap[key] = ('compute', val)
                else:
                    g_logger.error("Error unknown property subscribe mode: %s", elt)
            else:
                g_logger.error("Error unknown property type: %s", elt)
        
        self.m_properties[p] = propMap
    
    def getProperty(self, p):
        return self.m_properties[p]
        
    def addChild(self, o):
        if o != None:
            o.m_refCount = o.m_refCount + 1
            self.m_children.append(o)
    
class AniRectangle(AniObject):
    # Rectangle {
    #   Size = ((217, 214), 335, 297)
    #   Palette = { "background","foreground","default","solid","solid",1 }
    #   Level = "AnAlwaysVisibleLevel"
    # }
    def __init__(self, fname, objtype):
        super(AniRectangle, self).__init__(fname, objtype)

    def __setattr__(self, name, value):
        if name == "Size":
            rectDef = ast.literal_eval(value)
            p,w,h = rectDef
            x,y = p
            name = 'Points'
            p1 = Point2D()
            p1.x = x
            p1.y = y
            p2 = Point2D()
            p2.x = x + w
            p2.y = y
            p3 = Point2D()
            p3.x = x + w
            p3.y = y + h
            p4 = Point2D()
            p4.x = x
            p4.y = y + h
            value = [p1,p2,p3,p4,p1]
        
        super(AniRectangle, self).__setattr__(name, value)

class AniCircle(AniObject):
    # Circle {
    #   Size = ((1696, 706), 28, 28)
    #   Palette = { "Background","C11","Arial/12B","solid","solid",0 }
    #   Level = "AnAlwaysVisibleLevel"
    # }
    def __init__(self, fname, objtype):
        super(AniCircle, self).__init__(fname, objtype)

    def __setattr__(self, name, value):
        if name == "Size":
            circDef = ast.literal_eval(value)
            p,rx,ry = circDef
            x,y = p
            g_logger.debug("Circle: %s  C(%f, %f) R(%f, %f)", value, x, y, rx, ry)
            name = 'Points'
            cx = x + rx / 2.0
            cy = y + ry / 2.0
            nbpt = 50.0
            step = 2 * math.pi / nbpt
            value = []
            for i in range(int(nbpt)):
                pt = Point2D()
                pt.x = cx + rx / 2.0 * math.cos(i * step)
                pt.y = cy + ry / 2.0 * math.sin(i * step)
                value.append(pt)
            value.append(value[0])
        
        super(AniCircle, self).__setattr__(name, value)

class AniSpline(AniObject):
    # Spline {
    #   Points = { (1844, 777), (1847, 777), (1849, 777), (1851, 776) }
    #   Palette = { "White","Black","Arial/12B","solid","solid",1 }
    #   Level = "AnAlwaysVisibleLevel"
    # }
    #
    # Cubic  Bezier  
    #   CP1 o ------o CP2
    #      /         \
    #     /           \ 
    # SP o            o EP
    #
    # P(u) = u*u*u * (EP + 3*(CP1 - CP2) - SP) + 3*u*u*(SP - 2* CP1 + CP2) + 3*u*(CP1 - SP) + SP
    # P(t) = P0*u*u + P1*2*u*(1-u) + P2*(1-t)^2
    def __init__(self, fname, objtype):
        super(AniSpline, self).__init__(fname, objtype)

    def __setattr__(self, name, value):
        if name == "Points":
            ptTab = ast.literal_eval(value[0])
            newvalue = []
            for pt in ptTab:
                newvalue.append(Point2D(str(pt)))
            
            if len(newvalue) < 3:
                g_logger.warn("1,2 points spline: %s", value)
                pass
            elif len(newvalue) == 3:
                g_logger.debug("3 points spline")
                newvalue = self.toPolyline3pts(newvalue[0], newvalue[1], newvalue[2], 20)
            else:
                newvalue = self.toPolyline(newvalue, 20)
            super(AniSpline, self).__setattr__(name, newvalue)
        else:        
            super(AniSpline, self).__setattr__(name, value)
    
    def toPolyline3pts(self, p0, p1, p2, nbpts):
        result = []
        step = 1.0 / nbpts
        for i in range(nbpts-1):
            u = i * step
            pt = Point2D()
            pt.x = p0.x * u*u + p1.x * 2*u*(1-u) + p2.x *(1-u)*(1-u)
            pt.y = p0.y * u*u + p1.y * 2*u*(1-u) + p2.y *(1-u)*(1-u)
            result.append(pt)
            
        result.append(p0)
        return result
        
    def toPolyline(self, ctlpts, nbpts):
        result = []
        nbpieces = (len(ctlpts) - 1) / 3
        index = 0
        for _ in range(nbpieces):
            self.bezierToPoly(ctlpts[index], ctlpts[index + 1], ctlpts[index + 2], ctlpts[index + 3], nbpts, result)
            index = index + 3
        result.append(ctlpts[len(ctlpts)-1])
        return result
        
    def bezierToPoly(self, sp, cp1, cp2, ep, nbpts, poly):
        step = 1.0 / nbpts
        for i in range(nbpts-1):
            u = i * step
            pt = Point2D()
            pt.x = u*u*u * (ep.x + 3*(cp1.x - cp2.x) - sp.x) + 3*u*u*(sp.x - 2* cp1.x + cp2.x) + 3*u*(cp1.x - sp.x) + sp.x
            pt.y = u*u*u * (ep.y + 3*(cp1.y - cp2.y) - sp.y) + 3*u*u*(sp.y - 2* cp1.y + cp2.y) + 3*u*(cp1.y - sp.y) + sp.y
            poly.append(pt)

class AniBitmap(AniObject):
    # Icon {
    #   Point = (-46, -38)
    #   Name = "lock.png"
    # }
    def __init__(self, fname, objtype):
        super(AniBitmap, self).__init__(fname, objtype)
        

def createAniObject(fname, objtype):
    if objtype == 'Rectangle':
        return AniRectangle(fname, objtype)
    if objtype == 'Circle' or objtype == 'Ellipse':
        return AniCircle(fname, objtype)
    if objtype == 'Spline' or objtype == 'ClosedSpline':
        return AniSpline(fname, objtype)
    if objtype == 'Icon':
        return AniBitmap(fname, objtype)
    else:
        return AniObject(fname, objtype)
    
class AnPalette(object):

    def __init__(self, tab = None):
        self.m_background = "Black"
        self.m_foreground = "White"
        self.m_font = "default"
        self.m_pattern = ""
        self.m_lineStyle = ""
        self.m_lineWidth = 1
        self.m_fillStyle = -1
        self.m_arcMode = -1
        self.m_fillRule = -1
        if tab != None:
            if len(tab) == 1:
                tab = tab[0].strip(']').strip('[').split(',')
            if len(tab) >= 6:
                self.m_background = tab[0].strip('"')
                self.m_foreground = tab[1].strip('"')
                self.m_font = tab[2].strip('"')
                
                self.m_pattern = tab[3].strip('"')
                self.m_lineStyle = tab[4].strip('"')
                    
                self.m_lineWidth = int(tab[5].strip('"'))
            if len(tab) >= 7:
                self.m_fillStyle = tab[6].strip('"')
            if len(tab) >= 8:
                self.m_arcMode = tab[7].strip('"')
            if len(tab) >= 9:
                self.m_fillRule = tab[8].strip('"')

    def getFgColor(self):
        return self.m_foreground
        
    def getWidth(self):
        return self.m_lineWidth
        
    def __str__(self):
        return "bg:%s fg:%s font:%s lwidth:%d lstyle:%s" % (self.m_background, self.m_foreground, self.m_font, self.m_lineWidth, self.m_lineStyle)

class AnContext(object):
               
    def __init__(self):
        self.m_envStack = []
        self.m_objectPathStack = []
        self.m_layerStack = []
        self.m_levelStack = []
        self.m_matrixStack = []
        self.m_paletteStack = []
        self.m_anClassStack = []
        self.m_subImageStack = []
        self.m_currentMatrix = Matrix2D()
        
    def pushObject(self, obj):
        if obj != None:
            if obj.Transformer != None:
                self.pushMatrix(obj.Transformer)
        
            if obj.Palette != None:
                self.pushPalette(obj.Palette)
            
            if obj.Level != None:
                self.pushLevel(obj.Level)
           
            if obj.Environment != None:
                self.pushEnv(obj.Environment)
                
            if obj.m_type == 'Layer':
                self.pushLayer(obj.m_name)
                
            if obj.m_type == 'SubImage':
                self.pushSubImage(obj.Name)
                
            if obj.m_type in ['ActiveBackdropInstance', 'ActiveTextInstance', 'ActiveSymbolInstance', 'ActiveBackdropInstance', 'ActiveNumberInstance', 'MobileSymbolInstance', 'FillingSymbolInstance']:
                self.pushAnClass(obj.m_type + ':' + str(obj.m_name))
            
            if not obj.m_type in ['ActiveBackdrop', 'ActiveText', 'ActiveSymbol', 'ActiveBackdrop', 'ActiveNumber', 'FillingSymbol', 'MobileSymbol']:
                if obj.ObjectPath != None:
                    self.pushObjectPath(obj.ObjectPath)
                    
            if obj.m_type in ['ActiveBackdrop', 'MobileSymbol', 'FillingSymbol']:
                pal = AnPalette()
                pal.m_foreground = obj.Colors[int(obj.__getattribute__('Default Color'))]
                self.pushPalette(pal)
            
            if obj.m_type in ['ActiveText', 'ActiveNumber']:
                pal = AnPalette()
                pal.m_font = obj.Font
                pal.m_foreground = obj.Colors[int(obj.__getattribute__('Default Color'))]
                self.pushPalette(pal)
        
    def popObject(self, obj):
        if obj != None:
            if obj.Transformer != None:
                self.popMatrix()
        
            if obj.Palette != None:
                self.popPalette()
                
            if obj.Level != None:
                self.popLevel()
          
            if obj.Environment != None:
                self.popEnv()
                
            if obj.m_type == 'Layer':
                self.popLayer()
            
            if obj.m_type == 'SubImage':
                self.popSubImage()
                
            if obj.m_type in ['ActiveBackdropInstance', 'ActiveTextInstance', 'ActiveSymbolInstance', 'ActiveBackdropInstance', 'ActiveNumberInstance', 'MobileSymbolInstance', 'FillingSymbolInstance']:
                self.popAnClass()
                
            if not obj.m_type in ['ActiveBackdrop', 'ActiveText', 'ActiveSymbol', 'ActiveBackdrop', 'ActiveNumber', 'FillingSymbol', 'MobileSymbol']:
                if obj.ObjectPath != None:
                    self.popObjectPath()
                    
            if obj.m_type in ['ActiveBackdrop', 'MobileSymbol', 'FillingSymbol']:
                self.popPalette()
                    
            if obj.m_type  in ['ActiveText', 'ActiveNumber']:
                self.popPalette()
                    
    def pushMatrix(self, m):
        if m != None:
            self.m_currentMatrix = self.m_currentMatrix * m
            self.m_matrixStack.append(m)
        
    def popMatrix(self):
        if len(self.m_matrixStack) == 0:
            g_logger.warn("AnContext: pb during matrix pop")
        else:
            self.m_matrixStack.pop()
            self.m_currentMatrix = Matrix2D()
            for m in self.m_matrixStack:
                self.m_currentMatrix = self.m_currentMatrix * m
    
    def getMatrix(self):
        return self.m_currentMatrix
    
    def pushSubImage(self, name):
        self.m_subImageStack.append(name)
        
    def popSubImage(self):
        if len(self.m_subImageStack) == 0:
            g_logger.warn("AnContext: pb during subimage pop")
        else:
            self.m_subImageStack.pop()

    def getSubImageName(self):
        return '_'.join(self.m_subImageStack)
        
    def pushLayer(self, l):
        if l != None:
            self.m_layerStack.append(l)
        
    def popLayer(self):
        if len(self.m_layerStack) == 0:
            g_logger.warn("AnContext: pb during layer pop")
        
        else:
            self.m_layerStack.pop()
    
    def getLayer(self):
        if len(self.m_layerStack) == 0:
            return None
        else:
            return self.m_layerStack[len(self.m_layerStack)-1]
    
    def pushLevel(self, l):
        if l != None:
            self.m_levelStack.append(l)
        
    def popLevel(self):
        if len(self.m_levelStack) == 0:
            g_logger.warn("AnContext: pb during level pop")
        
        else:
            self.m_levelStack.pop()
    
    def getLevel(self):
        if len(self.m_levelStack) == 0:
            return None
        else:
            return self.m_levelStack[len(self.m_levelStack)-1]

    def pushEnv(self, l):
        if l != None:
            self.m_envStack.append(l)
        
    def popEnv(self):
        if len(self.m_envStack) == 0:
            g_logger.warn("AnContext: pb during env pop")
        else:
            self.m_envStack.pop()
    
    def getEnv(self):
        if len(self.m_envStack) == 0:
            return None
        else:
            # for env we keep the first found
            return self.m_envStack[0]
            
    def pushPalette(self, p):
        if p != None:
            self.m_paletteStack.append(p)
        
    def popPalette(self):
        if len(self.m_paletteStack) == 0:
            g_logger.warn("AnContext: pb during palette pop")
        else:
            self.m_paletteStack.pop()
    
    def getPalette(self):
        if len(self.m_paletteStack) == 0:
            g_logger.warn("AnContext: pb during palette get")
            return AnPalette()
        else:
            return self.m_paletteStack[len(self.m_paletteStack)-1]
            
    def pushObjectPath(self, p):
        if p != None:
            self.m_objectPathStack.append(p)
        
    def popObjectPath(self):
        if len(self.m_objectPathStack) == 0:
            g_logger.warn("AnContext: pb during object pop")
        else:
            self.m_objectPathStack.pop()
    
    def getObjectPath(self):
        opath = ''
        if len(self.m_objectPathStack) == 0:
            opath = ''
        else:
            opath = ':'.join([e for e in self.m_objectPathStack if len(e)>0])
        
        if len(opath) > 0 and (opath[0] != ':' and opath[0] != '<'):
            opath = ':' + opath
         
        if len(opath) > 0 and (opath[len(opath)-1] == ':'):
            opath = opath[:-1]
         
        if len(opath) > 0 and (opath[0] == ':' and opath[1] == ':'):
            pass
        
        return opath
    
    def pushAnClass(self, c):
        if c != None:
            self.m_anClassStack.append(c)
        
    def popAnClass(self):
        if len(self.m_anClassStack) == 0:
            g_logger.warn("AnContext: pb during AnClass pop")
        else:
            self.m_anClassStack.pop()
            
    def getAnClass(self):
        if len(self.m_anClassStack) == 0:
            return ''
        else:
            return self.m_anClassStack[len(self.m_anClassStack)-1]

class AnParser(object):
    # file type
    IMAGE_TYPE = 0
    CLASS_TYPE = 1
    SYMBOL_TYPE = 2
    # parse state
    INIT_STATE = 0
    PARSE_VAR_STATE = 1
    START_BLOCK_STATE = 2
    END_BLOCK_STATE = 3
    PARSE_PROPERTY_STATE = 4
    
    def __init__(self, rootdir, filename, wks, defaultName, ftype):
        self.m_filename = filename
        self.m_workspace = wks
        self.m_type = ftype
        self.m_rootdir = rootdir
        self.m_object = AniObject(self.m_filename, "AniFile")
        self.m_object.m_name = defaultName
        self.m_objstack = []
        self.m_objstack.append(self.m_object)
        self.m_keyword = None
        self.m_readname = None
        self.m_readvalue = None
        self.m_state = AnParser.INIT_STATE
        
    def curObj(self):
        return self.m_objstack[len(self.m_objstack)-1]
        
    def continueVariable(self, line):
        if line == '{':
            self.m_state = AnParser.PARSE_VAR_STATE
            self.m_readvalue = []
            # print "continueVariable open:", self.m_keyword
        elif line == '}':
            # close prop list
            # print "continueVariable close:", self.m_keyword
            self.m_state = AnParser.END_BLOCK_STATE
            
            if self.m_keyword == 'Symbols':
                symblist = []
                for s in self.m_readvalue:
                    if s[-4:] == ".ans":
                        s = s[:-4]
                    if s == '<empty>':
                        symblist.append(None)
                    else:
                        sobject = self.m_workspace.getSymbol(s)
                        if sobject != None:
                            self.curObj().addChild(sobject)
                            symblist.append(sobject)
                        else:
                            print "Error: missing symbol:", s
                setattr(self.curObj(), self.m_keyword, symblist)
            
            else:
                setattr(self.curObj(), self.m_keyword, self.m_readvalue)
        else:
            v = line.strip().strip(',').strip().strip('"').strip()
            # print "continueVariable append:", v
            self.m_readvalue.append(v)
            
    def continueProperty(self, line):
        if line == '{':
            self.m_state = AnParser.PARSE_PROPERTY_STATE
            self.m_readvalue = []
            # print "continueProperty open:", self.m_readname
        elif line == '}':
            # close prop list
            # print "continueProperty close:", self.m_readname, self.m_readvalue
            self.m_state = AnParser.END_BLOCK_STATE
            self.curObj().setProperty(self.m_readname, self.m_readvalue)
        else:
            v = line.strip(',').strip('"')
            # print "continueVariable append:", v
            self.m_readvalue.append(v)

    def parseLine(self, line):

        if self.m_state == AnParser.PARSE_VAR_STATE:
            self.continueVariable(line)
            return
        
        if self.m_state == AnParser.PARSE_PROPERTY_STATE:
            self.continueProperty(line)
            return
            
        if line.find('=') != -1:
            # property
            self.m_keyword = line[0:line.find('=')].strip()
            right = line[line.find('=')+1:].strip()
            # check for name
            if self.m_keyword.find('"') != -1:
                k = self.m_keyword.split('"')
                self.m_keyword = k[0].strip()
                if len(k) >= 2:
                    self.m_readname = k[1].strip()
                if len(k) >= 3:
                    print "Error during parse of keyword type:", line
            
            if len(right) == 0:
                self.m_state = AnParser.PARSE_VAR_STATE
            else:
                value = right.strip('"')
                if self.m_keyword == 'Mobile':
                    self.curObj().refObject(self.m_workspace.getSymbol(value))
                
                setattr(self.curObj(), self.m_keyword, value)
                # print "Property:", self.m_keyword, value
                self.m_keyword = None
                self.m_readname = None

        elif line.find('{') != -1:
            # start new object
            self.m_state = AnParser.START_BLOCK_STATE
        elif line.find('}') != -1:
            # close object
            self.m_state = AnParser.END_BLOCK_STATE
            o = self.m_objstack.pop()
            if self.m_type == AnParser.IMAGE_TYPE:
                if o.m_type == "SubImage":
                    o.refObject(self.m_workspace.getSubImage(o.m_name))
                elif o.m_type == 'FillingSymbol':
                    fs = self.m_workspace.getActiveClass(o.m_name)
                    fs.refObject(self.m_workspace.getSymbol(fs.m_children[0].Symbol))
                    o.refObject(fs)
                    o.m_type = o.m_type + 'Instance'
                elif o.m_type == 'Symbol':    
                    symb = self.m_workspace.getSymbol(o.m_name)
                    o.refObject(symb)
                    o.m_type = o.m_type + 'Instance'
                elif o.m_type in ['ActiveSymbol', 'ActiveText', 'ActiveBackdrop', 'ActiveNumber', 'MobileSymbol']:
                    o.refObject(self.m_workspace.getActiveClass(o.m_name))
                    o.m_type = o.m_type + 'Instance'
                    
                    
        elif line.find('"') != -1:
            k = line.split('"')
            self.m_keyword = k[0].strip()
            if len(k) >= 2:
                self.m_readname = k[1].strip().strip('"')
            if len(k) >= 4:
                print "Error during parse of:", line
                
            if self.m_keyword == "Include":
                pass
            else:
                # print "Add Object:", self.m_keyword, self.m_readname
                if self.m_keyword == "Property":
                    self.m_state = AnParser.PARSE_PROPERTY_STATE
                else:
                    obj = createAniObject(self.m_filename, self.m_keyword)
                    obj.m_name = self.m_readname
                    self.curObj().addChild(obj)
                    self.m_objstack.append(obj)
        else:
            self.m_keyword = line
            obj = createAniObject(self.m_filename, self.m_keyword)
            self.curObj().addChild(obj)
            self.m_objstack.append(obj)
            #print "Add anonymous Object:", self.m_keyword, self.m_filename

    def splitAndParse(self, line):
        if line.find('{') != -1:
            p = line.find('{')
            l1 = line[0:p].strip()
            l2 = '{'
            l3 = line[p+1:].strip()
            if len(l1) > 0:
                self.splitAndParse(l1)
            self.parseLine(l2)
            if len(l3) > 0:
                self.splitAndParse(l3)
        elif line.find('}') != -1:
            p = line.find('}')
            l1 = line[0:p].strip()
            l2 = '}'
            l3 = line[p+1:].strip()
            if len(l1) > 0:
                self.splitAndParse(l1)
            self.parseLine(l2)
            if len(l3) > 0:
                self.splitAndParse(l3)
        else:
            self.parseLine(line)
    
    def readAniFile(self):
        try:
            with open(self.m_filename) as f:
                for l in f.readlines():
                    line = l.strip()
                    # skip comment and empty line
                    if len(line) == 1 or (len(line) > 2 and line[0:2] != "//"):
                        self.splitAndParse(line)
        
        except IOError, e:
            g_logger.error("Error while reading '%s' (%s)", self.m_filename, e)
            self.m_object = None
                    
class AnDump(object):
    INDENT_TEXT = '  '
    
    def __init__(self, obj, filename):
        self.m_filename = filename
        self.m_object = obj
    
    def dumpObject(self, fout, indent, obj):
        if obj == None:
            print >>fout, indent, "None object"
            return
    
        if obj.m_name != None:
            print >>fout, indent, "%s(%s)" % (obj.m_type, obj.m_name)
        else:
            print >>fout, indent, obj.m_type
        indent = indent + AnDump.INDENT_TEXT
        if obj.m_refObject != None:
            print >>fout, indent, "point to: ", repr(obj.m_refObject)
            self.dumpObject(fout, indent + AnDump.INDENT_TEXT, obj.m_refObject)
        
        if len(obj.m_properties) > 0:
            for k, v in obj.m_properties.iteritems():
                print >>fout, indent, k, "=", v
        
        if len(obj.m_keywords) > 0:
            for k in obj.m_keywords:
                print >>fout, indent, k, "=", str(getattr(obj, k))
                if k == 'Default Symbol':
                    p = int(getattr(obj, k))
                    self.dumpObject(fout, indent + AnDump.INDENT_TEXT, obj.Symbols[p])
                
        indent = indent + AnDump.INDENT_TEXT
        for o in obj.m_children:
            if o.m_type != "AniFile":
                self.dumpObject(fout, indent, o)
    
    def dumpFile(self):
        try:
            pth = os.path.split(self.m_filename)[0]
            if pth and not os.path.exists(pth):
                os.makedirs(pth)
            with open(self.m_filename, 'w') as f:
                self.dumpObject(f, '', self.m_object)
        except:
            g_logger.error("Error while writing '%s' (%s)", self.m_filename, sys.exc_type)

class AnGeomDumper(object):
    INDENT_TEXT = '  '
    CLASS_TO_SKIP = ['ActiveSymbolInstance', 'ActiveTextInstance', 'ActiveNumberInstance', 'MobileSymbolInstance', 'FillingSymbolInstance', 'Chart']
    ACTIVE_CLASS = ['ActiveSymbolInstance', 'ActiveTextInstance', 'ActiveNumberInstance', 'MobileSymbolInstance', 'FillingSymbolInstance']
    
    def __init__(self, obj, filename, wks, fullExport, width, height, hvMapper, viewid, exportList):
        self.m_filename = filename
        self.m_object = obj
        self.m_workspace = wks
        self.m_context = AnContext()
        self.m_fullExport = fullExport
        self.m_width = width
        self.m_height = height
        self.m_viewid = viewid
        self.m_hvMapper = hvMapper
        self.m_activeSymbolList = []
        self.m_instancesList = []
        self.m_exportList = exportList
        self.m_subImageNames = []
        
        # create shapefile schema for polyline
        self.m_shape = shapefile.Writer(shapeType=shapefile.POLYLINE)
        self.m_shape.autoBalance = 1
        # field type C = Character, L = boolean, D = Date, N = Numbers decimal is number of digit after . (0 is int)
        self.m_shape.field('SCS_LAYER')
        self.m_shape.field('SCS_LEVEL')
        self.m_shape.field('SCS_COLOR')
        self.m_shape.field('SCS_STYLE')
        self.m_shape.field('SCS_WIDTH', fieldType="N", size="9", decimal=0)
        self.m_shape.field('SCS_DASH')
        self.m_shape.field('SCS_TEXT', size=128)
        self.m_shape.field('SCS_FONT')
        self.m_shape.field('SCS_FTSTYL')
        self.m_shape.field('SCS_FTWGT')
        self.m_shape.field('SCS_PATH')
        self.m_shape.field('CMN_ACLA')
        self.m_shape.field('HV_ID')
        # create shapefile schema for polygon
        self.m_shapeGone = shapefile.Writer(shapeType=shapefile.POLYGON)
        self.m_shapeGone.autoBalance = 1
        self.m_shapeGone.field('SCS_LAYER')
        self.m_shapeGone.field('SCS_LEVEL')
        self.m_shapeGone.field('SCS_COLOR')
        self.m_shapeGone.field('SCS_STYLE')
        self.m_shapeGone.field('SCS_WIDTH', fieldType="N", size="9", decimal=0)
        self.m_shapeGone.field('SCS_DASH')
        self.m_shapeGone.field('SCS_TEXT', size=128)
        self.m_shapeGone.field('SCS_FONT')
        self.m_shapeGone.field('SCS_FTSTYL')
        self.m_shapeGone.field('SCS_FTWGT')
        self.m_shapeGone.field('SCS_PATH')
        self.m_shapeGone.field('CMN_ACLA')
        self.m_shapeGone.field('HV_ID')

        # create shapefile schema for animated object
        self.m_shapeObject = shapefile.Writer(shapeType=shapefile.POLYLINE)
        self.m_shapeObject.autoBalance = 1
        self.m_shapeObject.field('SCS_LAYER')
        self.m_shapeObject.field('SCS_LEVEL')
        self.m_shapeObject.field('SCS_COLOR')
        self.m_shapeObject.field('SCS_STYLE')
        self.m_shapeObject.field('SCS_WIDTH', fieldType="N", size="9", decimal=0)
        self.m_shapeObject.field('SCS_DASH')
        self.m_shapeObject.field('SCS_TEXT', size=128)
        self.m_shapeObject.field('SCS_FONT')
        self.m_shapeObject.field('SCS_FTSTYL')
        self.m_shapeObject.field('SCS_FTWGT')
        self.m_shapeObject.field('SCS_PATH', size=128)
        self.m_shapeObject.field('CMN_ACLA', size=128)
        self.m_shapeObject.field('HV_ID', size=128)

        self.m_shapeObjectGone = shapefile.Writer(shapeType=shapefile.POLYGON)
        self.m_shapeObjectGone.autoBalance = 1
        self.m_shapeObjectGone.field('SCS_LAYER')
        self.m_shapeObjectGone.field('SCS_LEVEL')
        self.m_shapeObjectGone.field('SCS_COLOR')
        self.m_shapeObjectGone.field('SCS_STYLE')
        self.m_shapeObjectGone.field('SCS_WIDTH', fieldType="N", size="9", decimal=0)
        self.m_shapeObjectGone.field('SCS_DASH')
        self.m_shapeObjectGone.field('SCS_TEXT', size=128)
        self.m_shapeObjectGone.field('SCS_FONT')
        self.m_shapeObjectGone.field('SCS_FTSTYL')
        self.m_shapeObjectGone.field('SCS_FTWGT')
        self.m_shapeObjectGone.field('SCS_PATH', size=128)
        self.m_shapeObjectGone.field('CMN_ACLA', size=128)
        self.m_shapeObjectGone.field('HV_ID', size=128)

    def boundingBox(self, obj, xmin, xmax, ymin, ymax):
        self.m_context.pushObject(obj)
        g_logger.debug("bbox for %s" % (obj.m_name))
        if obj.Points != None:
            for pt in obj.Points:
                pt = self.m_context.getMatrix() * pt
                if xmin == None or pt.x < xmin:
                    xmin = pt.x
                if xmax == None or pt.x > xmax:
                    xmax = pt.x
                if ymin == None or pt.y < ymin:
                    ymin = pt.y
                if ymax == None or pt.y > ymax:
                    ymax = pt.y
                    
        if obj.m_refObject != None:
            (xmin, xmax, ymin, ymax) = self.boundingBox(obj.m_refObject, xmin, xmax, ymin, ymax)

        for o in obj.m_children:
            (xmin, xmax, ymin, ymax) = self.boundingBox(o, xmin, xmax, ymin, ymax)
                
        self.m_context.popObject(obj)
        return (xmin, xmax, ymin, ymax)
        
    def localBoundingBox(self, obj, xmin, xmax, ymin, ymax):
        g_logger.debug("local bbox for %s" % (obj.m_name))
        if obj.Points != None:
            for pt in obj.Points:
                pt = self.m_context.getMatrix() * pt
                if xmin == None or pt.x < xmin:
                    xmin = pt.x
                if xmax == None or pt.x > xmax:
                    xmax = pt.x
                if ymin == None or pt.y < ymin:
                    ymin = pt.y
                if ymax == None or pt.y > ymax:
                    ymax = pt.y
                    
        if obj.m_refObject != None:
            (xmin, xmax, ymin, ymax) = self.boundingBox(obj.m_refObject, xmin, xmax, ymin, ymax)

        for o in obj.m_children:
            (xmin, xmax, ymin, ymax) = self.boundingBox(o, xmin, xmax, ymin, ymax)
                
        return (xmin, xmax, ymin, ymax)
        
    def dumpLabel(self, fout, indent, obj):
        plsh = []
        pt1 = self.m_context.getMatrix() * obj.Points[0]
        plsh.append([pt1.x, pt1.y])
        pt2 = self.m_context.getMatrix() * obj.Points[0]
        pt2.x = pt2.x + 0.01
        plsh.append([pt2.x, pt2.y])
        
        self.m_shape.poly(shapeType=shapefile.POLYLINE, parts=[plsh])
        col = self.m_workspace.getColor(self.m_context.getPalette().getFgColor())
        
        fdef = self.m_context.getPalette().m_font
        if not fdef in self.m_workspace.m_fontMap:
            g_logger.error('Missing font: %s', fdef)
            fdef = 'default'
        (font, fsize, style, weight) = self.m_workspace.m_fontMap[fdef]
        self.m_shape.record(self.m_context.getLayer(), self.m_context.getLevel(), col, 'TEXT', fsize, '', obj.Label, font, style, weight, '', '', '')
        
        if fout != None:
            print >>fout, indent, "Text Label:", obj.Label, ":", str(self.m_context.getPalette()), "points:", pt1
    
    def dumpActiveText(self, fout, indent, obj, scspath, anclass, cmn_eid):
        plsh = []
        # create a dummy point at 0,0
        pt = Point2D()
        pt.x = 0
        pt.y = 0
        pt1 = self.m_context.getMatrix() * pt
        plsh.append([pt1.x, pt1.y])
        pt2 = pt1
        pt2.x = pt2.x + 0.01
        plsh.append([pt2.x, pt2.y])
        
        self.m_shapeObject.poly(shapeType=shapefile.POLYLINE, parts=[plsh])
        col = self.m_workspace.getColor(self.m_context.getPalette().getFgColor())
        
        fdef = self.m_context.getPalette().m_font
        if not fdef in self.m_workspace.m_fontMap:
            g_logger.error('Missing font: %s', fdef)
            fdef = 'default'
        (font, fsize, style, weight) = self.m_workspace.m_fontMap[fdef]
        self.m_shapeObject.record(self.m_context.getLayer(), self.m_context.getLevel(), col, 'TEXT', fsize, '', obj.Text, font, style, weight, scspath, anclass, cmn_eid)
        
        if fout != None:
            print >>fout, indent, "ActiveText Label:", obj.Text, ":", str(self.m_context.getPalette()), "points:", pt1
    
    def dumpActiveNumber(self, fout, indent, obj, scspath, anclass, cmn_eid):
        plsh = []
        # create a dummy point at 0,0
        pt = Point2D()
        pt.x = 0
        pt.y = 0
        pt1 = self.m_context.getMatrix() * pt
        plsh.append([pt1.x, pt1.y])
        pt2 = pt1
        pt2.x = pt2.x + 0.01
        plsh.append([pt2.x, pt2.y])
        
        self.m_shapeObject.poly(shapeType=shapefile.POLYLINE, parts=[plsh])
        col = self.m_workspace.getColor(self.m_context.getPalette().getFgColor())
        
        fdef = self.m_context.getPalette().m_font
        if not fdef in self.m_workspace.m_fontMap:
            g_logger.error('Missing font: %s', fdef)
            fdef = 'default'
        (font, fsize, style, weight) = self.m_workspace.m_fontMap[fdef]
        
        textValue = obj.Prefix + ' ' + obj.Value  + ' ' + obj.Postfix
        self.m_shapeObject.record(self.m_context.getLayer(), self.m_context.getLevel(), col, 'TEXT', fsize, '', textValue, font, style, weight, scspath, anclass, cmn_eid)
        
        if fout != None:
            print >>fout, indent, "ActiveNumber Label:", textValue, ":", str(self.m_context.getPalette()), "points:", pt1


    def dumpIcon(self, fout, indent, obj):
        plsh = []
        pt1 = self.m_context.getMatrix() * obj.Points[0]
        pt1.x = pt1.x -1
        plsh.append([pt1.x, pt1.y])
        pt2 = self.m_context.getMatrix() * obj.Points[0]
        pt2.x = pt2.x + 1
        plsh.append([pt2.x, pt2.y])
        
        self.m_shape.poly(shapeType=shapefile.POLYLINE, parts=[plsh])
        col = "#0000FF"
        
        fdef = "default"
        if not fdef in self.m_workspace.m_fontMap:
            g_logger.error('Missing font: %s', fdef)
            fdef = 'default'
        (font, fsize, style, weight) = self.m_workspace.m_fontMap[fdef]
        self.m_shape.record(self.m_context.getLayer(), self.m_context.getLevel(), col, 'ICON', fsize, '', obj.m_name, font, style, weight, '', '', '')
        
        if fout != None:
            print >>fout, indent, "Icon:", obj.m_name, "points:", pt1
            
    def dumpSymbol(self, fout, indent, obj):
        symb = self.m_workspace.getSymbol(obj.m_name)
        if symb != None:
            self.dumpObject(fout, indent+'   ', symb)
        else:
            g_logger.error("Cannot find symbol: %s" % (obj.m_name))
            
    def getHVClass(self, context):
        anclass = context.getAnClass()
        if anclass == '':
            return ''
        
        return self.m_hvMapper.getHVClass(anclass)
    
    def getHVID(self, context, obj):
        scspath = self.m_context.getObjectPath()
        if scspath == '':
            g_logger.debug("Missing DB Path for current obj (%s, %s)" % (str(obj.m_name), obj.m_type))
            return ''

        return self.m_hvMapper.getHVInstance(scspath)
    
    def addActiveObject(self, obj, hvclass, hvid, scspath):
        if not hvclass in self.m_activeSymbolList:
            g_logger.debug('addActiveObject add class:%s' % (hvclass))
            self.m_activeSymbolList.append(hvclass)
        g_logger.debug('addActiveObject add instance:%s %s' % (hvid, scspath))
        # update list of tuple  (oid, asymb, x, y, name, hflip, vflip, rotation)
        pt = Point2D()
        pt.x = 0
        pt.y = 0
        m = self.m_context.getMatrix()
        pt1 = m * pt
        
        # we need to remove the Y inversion to calculate on the real matrix 
        inv = Matrix2D()
        inv.rs22 = -1
        m = m * inv
        hf = m.isHFlip()
        vf = m.isVFlip()
        rotation = m.getAngle()
        
        # get width height
        (xmin, xmax, ymin, ymax) = self.localBoundingBox(obj, None, None, None, None)
        
        
        w = 50
        if xmin != None and xmax != None:
            w = (xmax-xmin)
        h = 50
        if ymin != None and ymax != None:
            h = (ymax-ymin)
        
        # get scale, we assume homogeneous x and y scale
        scale = m.getScale()
        
        g_logger.debug("obj %s bbox %s %s %s %s %d %d %f %s" % (hvid, str(xmin), str(xmax), str(ymin), str(ymax), w, h, scale, self.m_context.getLayer()))
        self.m_instancesList.append((hvid, hvclass, pt1.x, pt1.y, scspath, hf, vf, rotation, w, h, scale, self.m_context.getLayer()))
        
    def dumpObject(self, fout, indent, obj):
        if fout != None:
            print >>fout, indent, "Enter:", str(obj.m_name), "type:", obj.m_type, "{"
            if obj.ObjectPath != None:
                print >>fout, indent, "DBPath:", obj.ObjectPath
                
        # check for matrix
        self.m_context.pushObject(obj)
        
        # check if it is a subimage already seen
        # subimage may appear several times one for each layer
        if obj.m_type == 'SubImage':
            siName = self.m_context.getSubImageName()
            if siName in self.m_subImageNames:
                self.m_context.popObject(obj)
                return
            self.m_subImageNames.append(siName)
            
        anclass = self.m_context.getAnClass()
        hvclass = self.getHVClass(self.m_context)
        scspath = ''
        hvid = ''
        if hvclass != '':
            scspath = self.m_context.getObjectPath()
            hvid = self.getHVID(self.m_context, obj)
            
        if obj.m_type in AnGeomDumper.ACTIVE_CLASS:
            self.addActiveObject(obj, hvclass, hvid, scspath)
            
        if obj.m_type == 'Label':
            self.dumpLabel(fout, indent, obj)
        elif obj.m_type == 'Icon':
            self.dumpIcon(fout, indent, obj)
        elif self.m_fullExport and obj.m_type == 'ActiveText':
            self.dumpActiveText(fout, indent, obj, scspath, hvclass, hvid)
        elif self.m_fullExport and obj.m_type == 'ActiveNumber':
            self.dumpActiveNumber(fout, indent, obj, scspath, hvclass, hvid)
        else:
            if obj.Points != None:
                plsh = []
                ptlist = ""
                
                for pt in obj.Points:
                    pt = self.m_context.getMatrix() * pt
                    ptlist = ptlist + str(pt)
                    plsh.append([pt.x, pt.y])
                    
                if len(obj.Points) > 1:
                    col = self.m_workspace.getColor(self.m_context.getPalette().getFgColor())
                    style = 'LINE'
                    if obj.Filled:
                        style = 'FILLED'
                    dash = ""
                    if self.m_context.getPalette() != None:
                        dash = self.m_context.getPalette().m_lineStyle
                        
                    if anclass != '':
                        if hvclass != '':
                            if obj.Filled:
                                self.m_shapeObjectGone.poly(shapeType=shapefile.POLYGON, parts=[plsh])
                                self.m_shapeObjectGone.record(self.m_context.getLayer(), self.m_context.getLevel(), col, style, self.m_context.getPalette().getWidth(), dash, '', '', 'none', '', scspath, hvclass, hvid)
                            else:
                                self.m_shapeObject.poly(shapeType=shapefile.POLYLINE, parts=[plsh])
                                self.m_shapeObject.record(self.m_context.getLayer(), self.m_context.getLevel(), col, style, self.m_context.getPalette().getWidth(), dash, '', '', 'none', '', scspath, hvclass, hvid)
   
                    elif obj.Filled:
                        self.m_shapeGone.poly(shapeType=shapefile.POLYGON, parts=[plsh])
                        self.m_shapeGone.record(self.m_context.getLayer(), self.m_context.getLevel(), col, style, self.m_context.getPalette().getWidth(), dash, '', '', 'none', '', scspath, anclass, hvid)
                        self.m_shape.poly(shapeType=shapefile.POLYGON, parts=[plsh])
                        self.m_shape.record(self.m_context.getLayer(), self.m_context.getLevel(), col, style, self.m_context.getPalette().getWidth(), dash, '', '', 'none', '', scspath, anclass, hvid)
                    else:
                        self.m_shape.poly(shapeType=shapefile.POLYLINE, parts=[plsh])
                        self.m_shape.record(self.m_context.getLayer(), self.m_context.getLevel(), col, style, self.m_context.getPalette().getWidth(), dash, '', '', 'none', '', scspath, anclass, hvid)
                    
                if fout != None:
                    if obj.Label != None:
                        print >>fout, indent, obj.m_type, obj.Label, ":", str(self.m_context.getPalette()), "points:", ptlist
                    else:
                        if obj.m_type == "Pin":
                            print >>fout, indent, obj.m_type, "points:", ptlist
                        else:
                            print >>fout, indent, obj.m_type, ":", str(self.m_context.getPalette()), "points:", ptlist
                        
        oldindent = indent
        indent = indent + AnDump.INDENT_TEXT
        
        if self.m_fullExport == False and (obj.m_type in AnGeomDumper.CLASS_TO_SKIP):
            # replace with point with class info
            (xmin, xmax, ymin, ymax) = self.boundingBox(obj, None, None, None, None)
            pt = Point2D()
            if xmin != None and xmax != None:
                pt.x = (xmin+xmax)/2.0
            if ymin != None and ymax != None:
                pt.y = (ymin+ymax)/2.0
            
            self.m_context.pushObject(obj.m_refObject)
            pt = self.m_context.getMatrix() * pt
            if fout != None:
                print >>fout, oldindent, "Skip:", str(obj.m_name), "type:", obj.m_type, "{", pt.x, ",", pt.y, "}"
                
            self.m_context.popObject(obj.m_refObject)
            
        else:
            if obj.m_refObject != None:
                self.dumpObject(fout, indent, obj.m_refObject)

        for o in obj.m_children:
            self.dumpObject(fout, indent, o)
                
        self.m_context.popObject(obj)
        if fout != None:
            print >>fout, oldindent, "Leave:", str(obj.m_name), "type:", obj.m_type, "}"
            
    def dumpFile(self, debugInfo = False):
        fout = None
        
        (xmin, xmax, ymin, ymax) = self.boundingBox(self.m_object, None, None, None, None)
        if xmin == None:
            xmin = 0
        if xmax == None:
            xmax = 0
        if ymin == None:
            ymin = 0
        if ymax == None:
            ymax = 0
        
        w = xmax - xmin
        h = ymax - ymin
        
        g_logger.debug("Initial Bounding box(%s)= xmin%d ymin%d xmax%d ymax%d w%d h%d",self.m_filename[:-8], xmin, ymin, xmax, ymax, w, h)
        
        if self.m_width != -1 and self.m_height != -1:
            # fit image in m_width x m_height square
            # translate image to start at 0,0
            # invert Y axis because shape files are geographic normal axis and not computer graphics
            s = 1
            xs = 1
            ys = 1
            if w > 0:
                xs = self.m_width / w
                if xs < s:
                    s = xs
            if h > 0:
                ys = self.m_height / h
                if ys < s:
                    s = ys
            
            ms = Matrix2D("("+ str(s) +",0,0,"+ str(s) +",0,0)")
            self.m_context.pushMatrix(ms)
             
        m = Matrix2D("(1,0,0,-1," + str(-xmin) + "," + str(ymax) + ")")
        self.m_context.pushMatrix(m)

        (xmin, xmax, ymin, ymax) = self.boundingBox(self.m_object, None, None, None, None)
        if xmin == None:
            xmin = 0
        if xmax == None:
            xmax = 0
        if ymin == None:
            ymin = 0
        if ymax == None:
            ymax = 0
        
        w = xmax - xmin
        h = ymax - ymin

        # uncomment to write debug file
        if debugInfo and self.m_filename != None:
            pth = os.path.split(self.m_filename)[0]
            if pth and not os.path.exists(pth):
                os.makedirs(pth)
            fout = open(self.m_filename[:-4] +'.dbg', 'w') 

        # parse representation and build shape object
        self.dumpObject(fout, ' ', self.m_object)
        
        # write shape file
        fn = self.m_filename[:-8]
        g_logger.info("Bounding box(%s)= xmin%d ymin%d xmax%d ymax%d w%d h%d", fn, xmin, ymin, xmax, ymax, w, h)
        
        # add in every shape file a dummy rectangle corresponding to the bounding box
        bboxPolyline = [[xmin, ymin], [xmax, ymin], [xmax, ymax],  [xmin, ymax], [xmin, ymin]]
       
        if 'shape' in self.m_exportList:
            # write polylines
            if len(self.m_shape._shapes) > 0:
                self.m_shape.poly(shapeType=shapefile.POLYLINE, parts=[bboxPolyline])
                self.m_shape.record('','','','HIDDEN',0,'','','','','', '', '', '')
                self.m_shape.save(fn +"pll")
     
            # write polygon
            if len(self.m_shapeGone._shapes) > 0:
                self.m_shapeGone.poly(shapeType=shapefile.POLYGON, parts=[bboxPolyline])
                self.m_shapeGone.record('','','','HIDDEN',0,'','','','','', '', '', '')
                self.m_shapeGone.save(fn +"plg")
                
            # write object
            if len(self.m_shapeObject._shapes) > 0:
                self.m_shapeObject.poly(shapeType=shapefile.POLYLINE, parts=[bboxPolyline])
                self.m_shapeObject.record('','','','HIDDEN',0,'','','','','', '', '', '')
                self.m_shapeObject.save(fn +"pllobj")
                
            if len(self.m_shapeObjectGone._shapes) > 0:
                self.m_shapeObjectGone.poly(shapeType=shapefile.POLYGON, parts=[bboxPolyline])
                self.m_shapeObjectGone.record('','','','HIDDEN',0,'','','','','', '', '', '')
                self.m_shapeObjectGone.save(fn +"plgobj")
            
        # dump instances infos
        if 'instance' in self.m_exportList:
            self.dumpEquipFileForMetaconf(fn+"_inst.svg", xmin, ymin, xmax, ymax, w, h)
        
        # dump MWt view
        # self.dumpView(fn+".xml", self.m_viewid, ymax)
        
        # create CSv file for debug
        if debugInfo and self.m_filename != None:
            self.dumpCSVFile()
        
    def dumpCSVFile(self):
        # create csv dumper for debug
        try:
            with open(self.m_filename[:-8] + '.csv', 'wb') as csvfile:
                csvwriter = csv.writer(csvfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
                csvwriter.writerow(['SCS_LAYER','SCS_LEVEL','SCS_COLOR','SCS_STYLE','SCS_WIDTH','SCS_DASH','SCS_TEXT','SCS_FONT','SCS_FTSTYL','SCS_FTWGT', 'SCS_PATH', 'CMN_ACLA', 'HV_ID', 'SCS_GEOMTYPE', 'SCS_GEOM'])
                # create row from shape
                for rec, shp in izip(self.m_shape.records, self.m_shape.shapes()):
                    csvline = rec
                    csvline.append(ShapeTypeName[shp.shapeType])
                    csvline.append(str(shp.points))
                    csvwriter.writerow(csvline)
                    
                for rec, shp in izip(self.m_shapeObject.records, self.m_shapeObject.shapes()):
                    csvline = rec
                    csvline.append(ShapeTypeName[shp.shapeType])
                    csvline.append(str(shp.points))
                    csvwriter.writerow(csvline)    
        except:
            g_logger.error("Error while writing CSV  file '%s' (%s)", self.m_filename[:-8] + '.csv', sys.exc_value)
            
    def dumpEquipFileForMetaconf(self, filename, xmin, ymin, xmax, ymax, w, h):
        if len(self.m_instancesList) == 0:
            return
        
        pth = os.path.split(filename)[0]
        if pth and not os.path.exists(pth):
            os.makedirs(pth)
        fout = open(filename, 'w')
        
        # header
        print >>fout, '<?xml version="1.0" encoding="utf-8"?>'
        print >>fout, '<svg'
        print >>fout, '  xmlns="http://www.w3.org/2000/svg"'
        print >>fout, '  xmlns:xlink="http://www.w3.org/1999/xlink"'
        print >>fout, '  xmlns:mconf="http://www.thalesgroup.com/metaconf"'
        print >>fout, '  version="1.0"'
        print >>fout, '  viewBox="%d %d %d %d" width="%d" height="%d"' % (xmin, ymin, xmax, ymax, w, h)
        print >>fout, '>'
        
        # activesymbol/graphicrep list
        print >>fout, '  <!-- EQUIPMENT TYPES -->'
        print >>fout, '  <defs id="equipmentTypeDefs">'
        # loop on all used activesymbol
        
        for asym in self.m_activeSymbolList:
           
            if asym != None and asym != '':
                asym = xml.sax.saxutils.escape(asym)
                # <g id="ZoneIntrusion" />
                print >>fout, '  <g id="%s">' % (asym)
                print >>fout, '    <rect x="-2" y="-2" width="4" height="4px" style="fill:red;"/>'
                print >>fout, '    <text x="-10" y="20" fill="black">%s</text>' % (asym)
                print >>fout, '  </g>'
        print >>fout, '  </defs>'
        
        # instances
        print >>fout, '  <!-- IMAGE INSTANCES -->'
        # loop on all instances
        for (oid, asymb, x, y, name, hflip, vflip, rotation, w, h, scale, layer) in self.m_instancesList:
            if asymb != None and asymb != '' and name != '':
                asymb = xml.sax.saxutils.escape(asymb)
                oid = xml.sax.saxutils.escape(oid)
                name = xml.sax.saxutils.escape(name)
            
                # <use id="zi002" xlink:href="#ZoneIntrusion" transform="translate(850 830)" uri="zi002"/>
                hftext = ''
                vftext = ''
                rotext = ''
                
                if hflip:
                    hftext = 'hflip="true"'
                if vflip:
                    vftext = 'vflip="true"'
                if rotation != 0.0:
 #                   rotext = 'rotation="%.1f"' % (rotation)
                    # Modified by TLO, fix symbols rotation problem
                    if hflip or vflip:
                        rotext = 'rotation="%.1f"' % (rotation)
                    else:
                        if rotation > 0:
                            rotext = 'rotation="%.1f"' % (360 - rotation)
                        else:
                            rotext = 'rotation="%.1f"' % (rotation * (-1))
                    
                print >>fout, '  <use id="%s" xlink:href="#%s" transform="translate(%f %f)" uri="%s" layer="%s" scale="%f" %s %s %s/>' % (oid, asymb, x, (-1*y)+ymax, name, layer, scale, hftext, vftext, rotext)
            
            
        # loop on all instances to put their name
        print >>fout, '  <g>'
        for (oid, asym, x, y, name, hflip, vflip, rotation, w, h, scale, layer) in self.m_instancesList:
            if asym != None and asym != '' and name != '':
                name = xml.sax.saxutils.escape(name)
                print >>fout, '    <g><title>%s</title>' % (name)
                print >>fout, '      <rect x="%d" y="%d" width="%dpx" height="%dpx" style="stroke:red;fill:none;"/>' % (x-(w/2), (-1*y)+ymax-(h/2), w, h)
                print >>fout, '    </g>'
        print >>fout, '  </g>'
        
        # footer
        print >>fout, '</svg>'

    def dumpView(self, filename, viewname, ymax):
        
        pth = os.path.split(filename)[0]
        if pth and not os.path.exists(pth):
            os.makedirs(pth)
        fout = open(filename, 'w')
        
        # header
        print >>fout, '<?xml version="1.0" encoding="UTF-8"?>'
        print >>fout, '<viewConfiguration xmlns:ns2="http://www.thalesgroup.com/hv/mwt/conf/common"'
        print >>fout, '                   xmlns="http://www.thalesgroup.com/hv/mwt/conf/situationview/view">'
        print >>fout, ''
        print >>fout, '    <view id="%s"' % (viewname)
        print >>fout, '          maxExtent="-10.0 -10.0 1600.0 1300.0"'
        print >>fout, '          restrictedExtent="-10.0 -10.0 1600.0 1300.0"'
        print >>fout, '          initialExtent="0.0 0.0 1600.0 1300.0"'
        print >>fout, '          resolution="0.2 0.4 0.6 0.8 1 1.5 2 2.25 2.5 2.75 3 3.25 3.5"'
        print >>fout, '          resolutionReference="1.0"'
        print >>fout, '          srs="EPSG:23030" units="m">'
        print >>fout, ''
        print >>fout, '        <controls displayPanZoomBar="true" displayMousePosition="true" displayScale="true" displayScaleLine="true" displayLayerSwitcher="true"/>'
        print >>fout, ''
        
        # raster background
        print >>fout, '        <rasterLayer serverLayers="ani:%s" format="image/png" gisConfId="defaultwms" type="BASE_LAYER" name="base_layer"/>' % (viewname)
        print >>fout, ''
        
        # active backdrop
        if len(self.m_shapeObject._shapes) > 0:
            print >>fout, '        <vectorLayer featureNS="ani" featureType="%spllobj" gisConfId="defaultwfs" type="OVERLAY" name="ani_%spllobj_VectorLayer">' % (viewname, viewname)
            for rec in self.m_shapeObject.records:
                abclass = rec[11]
                hvid = rec[12]
                if abclass != '' and hvid != '':
                    print >>fout, '            <vectorEntry animationId="%s_animation" entityId="%s"/>' % (abclass, hvid)
                
            print >>fout, '        </vectorLayer>'
            print >>fout, ''
            
        if len(self.m_shapeObjectGone._shapes) > 0:
            print >>fout, '        <vectorLayer featureNS="ani" featureType="%splgobj" gisConfId="defaultwfs" type="OVERLAY" name="ani_%splgobj_VectorLayer">' % (viewname, viewname)
            for rec in self.m_shapeObjectGone.records:
                abclass = rec[11]
                hvid = rec[12]
                if abclass != '' and hvid != '':
                    print >>fout, '            <vectorEntry animationId="%s_animation" entityId="%s"/>' % (abclass, hvid)
          
            print >>fout, '        </vectorLayer>'
            print >>fout, ''
            
        # active symbol
        if len(self.m_instancesList) > 0:
            instMap = {}
            for (oid, asymb, x, y, name, _, _, _, _, _, _, _) in self.m_instancesList:
                if asymb != None and asymb != '' and name != '':
                    asymb = xml.sax.saxutils.escape(asymb)
                    oid = xml.sax.saxutils.escape(oid)
                    if not asymb in instMap:
                        instMap[asymb] = []
                        
                    instMap[asymb].append((oid, x, (-1*y)+ymax, asymb))
            
            for actSymb in instMap.keys():
                print >>fout, '        <symbolLayer type="OVERLAY" name="%s_layer">' % (actSymb)
                
                for tpl in instMap[actSymb]:
                    print >>fout, '            <entityEntry entityId="%s" x="%f" y="%f" symbolId="%s"/>' % tpl
            
                print >>fout, '        </symbolLayer>'
                print >>fout, ''
            
        # footer
        print >>fout, '    </view>'
        print >>fout, '</viewConfiguration>'

class AnSVGDumper(object):
    def __init__(self, srcfile, dstfile, hvMapper):
        self.m_srcfile = srcfile
        self.m_dstfile = dstfile
        self.m_HVMapper = hvMapper
        self.m_matrixStack = []
        self.m_currentMatrix = Matrix2D()
        
        et.register_namespace('', 'http://www.w3.org/2000/svg')
        # create folder if necessary
        pth = os.path.split(self.m_dstfile)[0]
        if pth and not os.path.exists(pth):
            os.makedirs(pth)
        
    def moveFile(self, src, dst):
        try:
            pth = os.path.split(dst)[0]
            if pth and not os.path.exists(pth):
                os.makedirs(pth)
            shutil.move(src, dst)
        except:
            g_logger.error("Error while moving '%s' to '%s': ('%s')", src, dst, sys.exc_type)
            
    def pushMatrix(self, mrepr):
        m = Matrix2D(mrepr)
        if m != None:
            self.m_currentMatrix = self.m_currentMatrix * m
            self.m_matrixStack.append(m)
        
    def popMatrix(self):
        if len(self.m_matrixStack) == 0:
            g_logger.warn("AnContext: pb during matrix pop")
        else:
            self.m_matrixStack.pop()
            self.m_currentMatrix = Matrix2D()
            for m in self.m_matrixStack:
                self.m_currentMatrix = self.m_currentMatrix * m
    
    def getMatrix(self):
        return str(self.m_currentMatrix)

    def cleanupStyle(self, node, currentStyle):
        attribToRemove = ['fill', 'fill-opacity', 'stroke-opacity', 'stroke', 'stroke-width', 'opacity']
        
        # if 'style' in node.attrib:
            # node.attrib.pop('style')
            
        for att in attribToRemove:
            if att in node.attrib:
                currentStyle = currentStyle + att + ':' + node.attrib[att] + '; '
                node.attrib.pop(att)
            
        for child in node:
            currentStyle = currentStyle + self.cleanupStyle(child, currentStyle)
            
        return currentStyle
    
    # search for block like:        
    # <g anclass="smokeAB" anpath="SITE1:B001:F001:FIRE:FD001" class="smokeAB ActiveBackdrop" id="SITE1:B001:F001:FIRE:FD001_19">      
    # <polyline class="AnPolyline" fill="none" points="78,234 256,234 256,752 78,752 78,234 " stroke="#CFFFCF" stroke-width="1px" />
    # </g>
    def parseActiveBackdrop(self, parentNode, dbpathStack, curnode):
        
        if 'transform' in curnode.attrib:
            self.pushMatrix(curnode.attrib['transform'])
            
        if 'anpath' in curnode.attrib:
            dbpathStack.append(curnode.attrib['anpath'])
        if 'anclass' in curnode.attrib:
            ani_class = curnode.attrib['anclass']
            if 'class' in curnode.attrib:
                if 'ActiveBackdrop' in curnode.attrib['class'].split():
                    # add attribute cmn_acla="FireZoneActiveBackdrop" hv_id="B01FD001"
#                    mc_graphicrep = self.m_HVMapper.getHVClass(ani_class)
                    # Modified by TLO, fix searching active backdrop in classmapping file 
                    mc_graphicrep = self.m_HVMapper.getHVClass("ActiveBackdropInstance:" + ani_class)
                    dbpath = self.getObjectPath(dbpathStack)
                    hvid = self.m_HVMapper.getHVInstance(dbpath)
                    if len(mc_graphicrep) > 0 and len(hvid) > 0:
                        curnode.attrib['cmn_acla'] = mc_graphicrep
                        curnode.attrib['hv_id'] = hvid
                        childList = []
                        for child in curnode:
                            childList.append(child)
                        # create embedded node to store transform
                        gNode = et.SubElement(curnode, '{http://www.w3.org/2000/svg}g')
                        gNode.attrib['transform'] = self.getMatrix()
                        for child in childList:
                            curnode.remove(child)
                            gNode.append(child)

                        cstyle = self.cleanupStyle(gNode, '')

                        if len(cstyle) > 0:
                            curnode.attrib['style'] = cstyle
                        if 'transform' in curnode.attrib:
                            curnode.attrib.pop('transform')
                        if 'anpath' in curnode.attrib:
                            dbpathStack.pop()
                        return 1
    
        localABackNumber = 0
        childToRemove = []
        for child in curnode:
            if self.parseActiveBackdrop(curnode, dbpathStack, child) > 0:
                localABackNumber = localABackNumber + 1
            else:
                childToRemove.append(child)
                 
        for child in childToRemove:
            curnode.remove(child)
            
        if 'anpath' in curnode.attrib:
            dbpathStack.pop()
        if 'transform' in curnode.attrib:
            self.popMatrix()
            curnode.attrib.pop('transform')
            
        return localABackNumber
                    
    def getObjectPath(self, dbpathStack):
        opath = ''
        if len(dbpathStack) == 0:
            opath = ''
        else:
            opath = ':'.join([e for e in dbpathStack if len(e)>0])
        
        if len(opath) > 0 and (opath[0] != ':' and opath[0] != '<'):
            opath = ':' + opath
         
        if len(opath) > 0 and (opath[len(opath)-1] == ':'):
            opath = opath[:-1]
         
        if len(opath) > 0 and (opath[0] == ':' and opath[1] == ':'):
            pass
        
        return opath
    
    def convertActiveBackdrop(self):
        # get encoding from first line
        fencoding = self.getEncoding(self.m_srcfile)
        
        # parse xml
        abtree = None
        et.register_namespace('', 'http://www.w3.org/2000/svg')
        try:
            abtree = et.parse(self.m_srcfile)
        except:
            g_logger.error("Error while parsing '%s': ('%s')", self.m_srcfile, traceback.format_exc())
            self.moveFile(self.m_srcfile, self.m_dstfile)
            return

        dbpathStack = []
    
        rootNode = abtree.getroot()
        x0, y0, w, h = self.getViewbox(rootNode)
        rootNode.attrib['viewBox'] = '0 0 %d %d' % (w, h)
        if x0 != 0 or y0 != 0:
            childList = []
            for child in rootNode:
                childList.append(child)
                
            gNode = et.SubElement(rootNode, '{http://www.w3.org/2000/svg}g')
            gNode.attrib['transform'] = 'matrix(1, 0, 0, 1, %d, %d)' % (-x0, -y0)
            for child in childList:  
                rootNode.remove(child)
                gNode.append(child)
                
        nbabackdrop = self.parseActiveBackdrop(None, dbpathStack, rootNode)
        
        if nbabackdrop > 0:
            abtree.write(self.m_dstfile, encoding=fencoding, xml_declaration=True, method='xml')
    
    def getEncoding(self, xmlfile):
        with open(xmlfile, 'r') as f:
            first_line = f.readline()
            file_content = f.read()
            f.close()
            
        # line looks like <?xml version='1.0' encoding='ISO-8859-1' standalone='no'?>
        m = re.search('.*encoding *= *.([^\s]+)', first_line)
        if m != None:
            fencoding = m.group(1)[:-1]
            utf8text = file_content.decode(fencoding).encode('utf8')
            with open(xmlfile, 'wb') as f:
                f.write(utf8text)
            return fencoding
        
        return 'UTF-8'
        
    def getViewbox(self, svgnode):
        x0 = 0
        y0 = 0
        w = 1000
        h = 1000
        
        if 'viewBox' in svgnode.attrib:
            vb = svgnode.attrib['viewBox'].strip().split()
            if len(vb) == 4:
                x0 = float(vb[0])
                y0 = float(vb[1])
                h = float(vb[3])
                w = float(vb[2])
    
        return (x0, y0, w, h)
    
    def centerCoord(self, origin, size, center):
        newsize = size
        
        dw1 = 0 - origin
        dw2 = size - dw1
        
        if dw1 > dw2:
            newsize = 2 * dw1
        else:
            newsize = 2 * dw2
            origin = origin - (dw2 -dw1)
        
        neworigin = origin + center
        return (neworigin, newsize)
        
    def convertSymbol(self, name, anrootdir):
        
        # use animator tool to generate SVG
        subprocess.call(["an2svg.exe", name, anrootdir])
        
        fencoding = self.getEncoding(self.m_srcfile)
        
        
        # parse xml
        # animator symbol are always made of a unique g entity withh 0,0 at the centre of the bounding box
        # for metaconf we want a symbol with a center at 100,100 in 200x200 box
        abtree = None
        et.register_namespace('', 'http://www.w3.org/2000/svg')
        try:
            abtree = et.parse(self.m_srcfile)
           
        except:
            g_logger.error("Error while parsing '%s': ('%s')", self.m_srcfile, traceback.format_exc())
            self.moveFile(self.m_srcfile, self.m_dstfile)
            return
        
        svgRoot = abtree.getroot()
       
        if svgRoot.tag != '{http://www.w3.org/2000/svg}svg':
            g_logger.error("Error while searching svg node in '%s': ('%s')", self.m_srcfile, traceback.format_exc())
            self.moveFile(self.m_srcfile, self.m_dstfile)
            return
        
        # get box value
        scale = 1
        tx = 100
        ty = 100
        x0, y0, w, h = self.getViewbox(svgRoot)

        # modify viewbox
        x0, w = self.centerCoord(x0, w, 100)
        y0, h = self.centerCoord(y0, h, 100)
        svgRoot.attrib['viewBox'] = "%d %d %d %d" % (x0, y0, w, h)
        svgRoot.attrib['height'] = str(h)
        svgRoot.attrib['width'] = str(w)
        
        # add transform in first g node
        symbolNode = svgRoot.find('{http://www.w3.org/2000/svg}g')
        if symbolNode == None:
            g_logger.error("Error while searching g node under svg in '%s': ('%s')", self.m_srcfile, traceback.format_exc())
            self.moveFile(self.m_srcfile, self.m_dstfile)
            return
        
        symbolNode.attrib['transform'] = "matrix(%f, 0, 0, %f, %f, %f)" % (scale, scale, tx, ty)
        
        # add a 200x200 rectangle to have something well centerd in MC
        rect = et.Element('{http://www.w3.org/2000/svg}rect')
        rect.attrib['height'] = str(h)
        rect.attrib['width'] = str(w)
        rect.attrib['x'] = str(x0)
        rect.attrib['y'] = str(y0)
        rect.attrib['fill'] = 'red'
        rect.attrib['opacity'] = '0'
        svgRoot.insert(0, rect)

        abtree.write(self.m_dstfile, encoding=fencoding, xml_declaration=True, method='xml')
        
    def convertBackground(self):
        
        # parse xml
        bgtree = None
        
        try:
            # get encoding from first line
            fencoding = self.getEncoding(self.m_srcfile)
            
            bgtree = et.parse(self.m_srcfile)
        except:
            g_logger.error("Error while parsing '%s': ('%s')", self.m_srcfile, traceback.format_exc())
            self.moveFile(self.m_srcfile, self.m_dstfile)
            return

        rootNode = bgtree.getroot()
        x0, y0, w, h = self.getViewbox(rootNode)
        
        if x0 != 0 or y0 != 0:
            rootNode.attrib['viewBox'] = '0 0 %d %d' % (w, h)
            childList = []
            for child in rootNode:
                childList.append(child)
                 
            gNode = et.SubElement(rootNode, '{http://www.w3.org/2000/svg}g')
            gNode.attrib['transform'] = 'translate(%d, %d)' % (-x0, -y0)
            for child in childList:
                rootNode.remove(child)
                gNode.append(child)
                 
        bgtree.write(self.m_dstfile, encoding='UTF-8', xml_declaration=True, method='xml')

# This class will collect all the files in an Animator workspace
# and call the AnParser to build a memory representation of all images        
class AnWorkspace(object):

    ACTIVE_CLASS_FOLDERS = ['ActiveText', 'ActiveSymbol', 'ActiveBackdrop', 'ActiveNumber', 'MobileSymbol', 'FillingSymbol', 'Chart']
    
    def __init__(self, rootdir):
        self.m_rootdir = rootdir
        self.m_symbolList = {}
        self.m_classList = {}
        self.m_imagelist = {}
        self.m_layerList = []
        self.m_levelList = []
        self.m_colorMap = {}
        self.m_fontMap = {}
        self.m_propParser = None
        self.m_imgRoot = ''
        self.m_imgBackground = 'White'
        
        self.m_symbolFileList = {}
        self.m_classFileList = {}
        self.m_imageFileList = {}
    
    def moveFile(self, src, dst):
        try:
            pth = os.path.split(dst)[0]
            if pth and not os.path.exists(pth):
                os.makedirs(pth)
            shutil.move(src, dst)
        except:
            g_logger.error("Error while moving '%s' to '%s': ('%s')", src, dst, traceback.format_exc())


    def getSymbol(self, name):
        if name == None:
            return None
            
        fname = name + ".ans"
        fname = fname.replace('\\', '/')
            
        if name in self.m_symbolList:
            self.m_symbolFileList[fname] = self.m_symbolFileList[fname] + 1
            return self.m_symbolList[name]
        
        g_logger.debug("Parse symbol: %s", name)
        symbparser = AnParser(self.m_rootdir, os.path.join(rootdir, "symbol/" + name +".ans"), self, name, AnParser.SYMBOL_TYPE)
        symbparser.readAniFile()
        if symbparser.m_object != None:
            symbparser.m_object.m_name = name
            self.m_symbolList[name] = symbparser.m_object
            if fname in self.m_symbolFileList:
                self.m_symbolFileList[fname] = self.m_symbolFileList[fname] + 1
            else:
                self.m_symbolFileList[fname] = 0

            return symbparser.m_object
        else:
            g_logger.error("Error while parsing symbol: %s", name)
            return None
            
    def getActiveClass(self, name):
        if name == None:
            return None
            
        fname = name + ".anc"
        fname = fname.replace('\\', '/')
        
        if name in self.m_classList:
            self.m_classFileList[fname] = self.m_classFileList[fname] + 1
            return self.m_classList[name]
        
        for d in AnWorkspace.ACTIVE_CLASS_FOLDERS:
            filename = self.m_rootdir + "/class/" + d + "/" + name + ".anc"
            if os.path.exists(filename):
                g_logger.debug("Parse class: %s/%s", d, name)
                parser = AnParser(rootdir, filename, self, name, AnParser.CLASS_TYPE)
                parser.readAniFile()
                if parser.m_object != None:
                    parser.m_object.m_name = name
                    self.m_classList[name] = parser.m_object
                    if fname in self.m_classFileList:
                        self.m_classFileList[fname] = self.m_classFileList[fname] + 1
                    else:
                        self.m_classFileList[fname] = 0
                        
                    return parser.m_object
                else:
                    g_logger.error("Error while parsing class:%s/%s", d, name)
        
        g_logger.error("Error cannot find class: %s", name)
        return None
            
    def getSubImage(self, name):
        if name == None:
            return None
        
        fname = name + ".ani"
        fname = fname.replace('\\', '/')
        
        if name in self.m_imagelist:
            self.m_imageFileList[fname] = self.m_imageFileList[fname] + 1
            return self.m_imagelist[name]
        
        g_logger.debug("Parse subimage: %s", name)
        incparser = AnParser(self.m_rootdir, os.path.join(rootdir, "image/" + name +".ani"), self, name, AnParser.IMAGE_TYPE)
        incparser.readAniFile()
        if incparser.m_object != None:
            incparser.m_object.m_name = name
            self.m_imagelist[name] = incparser.m_object
            if fname in self.m_imageFileList:
                self.m_imageFileList[fname] = self.m_imageFileList[fname] + 1
            else:
                self.m_imageFileList[fname] = 0
            
            return incparser.m_object
        else:
            g_logger.error("Error while parsing subimage: %s" % (name))
            return None
    
    def getColor(self, name):
        if name in self.m_colorMap:
            return self.m_colorMap[name]
            
        if name.lower() in COLOR_NAME_TO_RGB:
            g_logger.info("WKS color found in COLOR_NAME_TO_RGB: %s", name)
            return COLOR_NAME_TO_RGB[name.lower()]
            
        g_logger.error("WKS cannot find color: %s", name)
        return "#AAAAAA"
    
    def dumAniObject(self):
        # for obj in self.m_symbolList.itervalues():
            # print "Dump symbol:", obj.m_name
            # dump = AnDump(obj, obj.m_filename+".dmp")
            # dump.dumpFile()
        
        # for obj in self.m_classList.itervalues():
            # print "Dump class:", obj.m_name
            # dump = AnDump(obj, obj.m_filename+".dmp")
            # dump.dumpFile()
            
        for obj in self.m_imagelist.itervalues():
            if obj.m_name[0:len(self.m_imgRoot)] == self.m_imgRoot:
                logStart("Dump image: " + obj.m_name)
                dumpGeom = AnGeomDumper(obj, obj.m_filename+".geo", self)
                dumpGeom.dumpFile()
                logEnd()
    
    def buildGrammar(self):
        # define comments
        comment = pyparsing.Literal("//") + pyparsing.Optional(pyparsing.restOfLine)
        
        # define keywords 
        defineKwd = pyparsing.Keyword('Define')
        orderedKwd = pyparsing.Keyword('Ordered')
        includeKwd = pyparsing.Keyword('Include')
        EQ,LBRACE,RBRACE, SEMICOLON, LPAREN, RPAREN, COMMA = map(pyparsing.Suppress, "={};(),")
         
        #define data types that might be in the values
        real = pyparsing.Regex(r"[+-]?\d+\.\d*%?").setParseAction(lambda x: float(x[0].replace('%','')))
        integer = pyparsing.Regex(r"[+-]?\d+%?").setParseAction(lambda x: int(x[0].replace('%','')))
        pyparsing.dblQuotedString.setParseAction(pyparsing.removeQuotes)
        trueBoolean = pyparsing.CaselessKeyword("true").setParseAction(pyparsing.replaceWith(True))
        falseBoolean = pyparsing.CaselessKeyword("false").setParseAction(pyparsing.replaceWith(False))
        colorName = pyparsing.Keyword("COLOR").suppress() + LPAREN + pyparsing.dblQuotedString  + RPAREN
        
        colorRGB = pyparsing.Keyword("RGB").suppress() + LPAREN + integer + COMMA + integer + COMMA + integer + RPAREN
        colorRGB.setParseAction(lambda x: "#%0.2X%0.2X%0.2X" % (int(x[0]*255.0/65535.0), int(x[1]*255.0/65535.0), int(x[2]*255.0/65535.0)))
        colorrgb = pyparsing.Keyword("rgb").suppress() + LPAREN + integer + COMMA + integer + COMMA + integer + RPAREN
        colorrgb.setParseAction(lambda x: "#%0.2X%0.2X%0.2X" % (x[0], x[1], x[2]))
      
        nameListItem = pyparsing.dblQuotedString + pyparsing.Optional(COMMA)
        nameList = pyparsing.Group(LBRACE + pyparsing.OneOrMore(nameListItem) + RBRACE)
     
        # define value token
        value = pyparsing.Forward() 
        value << (pyparsing.dblQuotedString  | real | integer | trueBoolean | falseBoolean | colorName | colorRGB | colorrgb | nameList)
        
        # define name token
        name = pyparsing.Word(pyparsing.alphanums+"_")
        
        # propertyDef definition a property is name=value
        propertyDef = pyparsing.Group(pyparsing.Combine(pyparsing.OneOrMore(name), adjacent=False, joinString=" ") + EQ + value + SEMICOLON)
        
        # define file structure
        entry = pyparsing.Forward() 

        #declare the overall structure of a nested data element
        key = name + pyparsing.Optional(pyparsing.dblQuotedString)
        key.setParseAction(lambda x: x[len(x)-1])
        
        block = pyparsing.Group(defineKwd.suppress() + pyparsing.Optional(orderedKwd).suppress() + key + LBRACE + pyparsing.ZeroOrMore(entry) + RBRACE)
        
        #declare the types that might be contained in our data value - string, real, int, or the struct we declared
        entry << (block | propertyDef)
        
        include = pyparsing.Group(includeKwd + pyparsing.dblQuotedString)
        
        # a file is a list of Define block and Include directive
        fileitem = pyparsing.Forward() 
        fileitem << (block | include)
        self.m_propParser = pyparsing.OneOrMore(fileitem)
        
        self.m_propParser.ignore(comment)
    
    def loadProperty(self, fname):
        if self.m_propParser == None:
            self.buildGrammar()
            
        g_logger.info("Parsing property file: %s/%s", self.m_rootdir, fname)
        tokens = self.m_propParser.parseFile(self.m_rootdir + '/' + fname)
        # pp = pprint.PrettyPrinter(2)
        # with open(fname+".dmp", 'w') as f:
            # print >>f, pp.pformat( tokens.asList() )
    
        # interpret token
        for t in tokens:
            if t[0] == 'Include':
                self.loadProperty(t[1])
            elif t[0] == 'Colors':
                self.addColors(t[1:])
            elif t[0] == 'Layers':
                self.addLayers(t[1:])
            elif t[0] == 'Decluttering':
                self.addLevel(t[1:])
            elif t[0] == 'Fonts':
                self.addFont(t[1:])
            elif t[0] == 'WorkSpace':
                for e in t[1:]:
                    if e[0] == 'Default Background':
                        self.m_imgBackground = e[1]
                
        self.m_fontMap['default'] = ('Arial', 14, 'normal', 'normal')
        
            
    # ['helvetica1/10', ['Type', 'System'], ['Font', '%helvetica-10-BI']],
    # {"fonts":["MV Boli","Monotype Koufi","Arial Italic","Lucida Bright Demibold Italic","Serif.plain","Pristina","Dialog.bolditalic","Monospaced.bold","Algerian","Palatino Linotype Gras","Lucida Fax Demi-gras Italique","Old Antic Bold","Arial monospaced Bold for SAP","Calibri Italic","Poor Richard","Niagara Engraved","Meiryo","Wingdings","Courier New Bold","Garamond","Cambria Math","Baskerville Old Face","Dialog.bold","Meiryo Gras Italique","Bernard MT Condensed","Garamond Gras","Sylfaen","MS Reference Specialty","Colonna MT","Magneto","Courier New Italic","Corbel","Wide Latin","Bookman Old Style","Lucida Handwriting","Old Antic Outline","Bold Italic Art","Farsi Simple Bold","Lucida Fax","Lucida Console","MT Extra","Corbel Bold","Batang","AGA Arabesque","Microsoft Sans Serif","Marlett","Serif.bolditalic","Book Antiqua Gras Italique","Old Antic Decorative","Cambria Bold Italic","Bell MT Gras","Chiller","Tahoma","DialogInput.italic","Verdana Italique","Juice ITC","MS Outlook","Trebuchet MS Gras","Harrington","Magneto Gras","Kartika","PT Simple Bold Ruled","DecoType Naskh Special","Diwani Letter","Lucida Sans Demibold","Calibri Bold Italic","Arial Narrow","Meiryo UI","DialogInput.bolditalic","Modern No. 20","DialogInput","Consolas Bold","Footlight MT Light","Garamond Italique","Lucida Sans Unicode","Led Italic Font","Arial Unicode MS","Mangal","Cooper Black","Georgia Italique","DecoType Thuluth","Times New Roman Italic","Corbel Bold Italic","Consolas Bold Italic","Comic Sans MS","Ravie","Comic Sans MS Gras","SansSerif.bold","Snap ITC","Brush Script MT","Segoe UI Italique","Kunstler Script","PT Bold Dusky","Arial Bold","Vladimir Script","Mudir MT","Arial Narrow Gras","Broadway","Constantia Bold Italic","Bell MT","Arial monospaced for SAP","Lucida Sans","Calibri","Raavi","Lucida Sans Typewriter Regular","DialogInput.plain","Lucida Bright Regular","Monospaced.plain","Verdana Gras Italique","Times New Roman","Akhbar MT Bold","Segoe UI","SAPDings","Book Antiqua Italique","Arial Black Italique","Arial Bold Italic","Bodoni MT Poster Compressed","Century Gothic","Britannic Bold","Lucida Calligraphy Italic","Monospaced.bolditalic","Vrinda","Wingdings 3","Wingdings 2","Kristen ITC","Dialog","Berlin Sans FB Demi","PT Bold Arch","Bookman Old Style Italique","Consolas Italic","Kufi Outline Shaded","Century","Tahoma Gras","Palatino Linotype Italique","Trebuchet MS Gras Italique","Meiryo UI Italique","Dialog.italic","Bookshelf Symbol 7","Berlin Sans FB Gras","Tunga","SAPDings Normal","Showcard Gothic","Monotype Corsiva","Bookman Old Style Gras Italique","Matura MT Script Capitals","DialogInput.bold","SAPIcons","Bauhaus 93","Segoe UI Gras","DecoType Naskh Variants","Constantia Bold","DecoType Naskh Extensions","Berlin Sans FB Demi Gras","SAPIcons Normal","Serif.bold","Gautami","SWGamekeys MT","Mistral","Georgia Gras","Meiryo UI Gras","Freestyle Script","Century Gothic Gras","Candara Bold","AGA Arabesque Desktop","Monospaced.italic","Centaur","Monospaced","PT Bold Broken","Bell MT Italique","Trebuchet MS","Diwani Outline Shaded","French Script MT","Courier New","Lucida Fax Italique","Palatino Linotype","PT Bold Mirror","Californian FB Italique","Candara Italic","Calibri Bold","Onyx","Playbill","Californian FB Gras","Century Gothic Italique","Lucida Sans Regular","Lucida Bright Italic","Lucida Bright","PT Separated Baloon","Webdings","Diwani Simple Outline","Latha","High Tower Text Italique","Brush Script MT Italique","Lucida Bright Demibold","Simple Indust Shaded","Cambria Bold","Book Antiqua Gras","Lucida Sans Typewriter Bold","Estrangelo Edessa","Niagara Solid","Georgia","Meiryo Italique","Courier New Bold Italic","High Tower Text","MS Reference Sans Serif","Monotype Koufi Bold","Franklin Gothic Medium","Trebuchet MS Italique","Arial Narrow Gras Italique","Diwani Simple Outline 2","Simple Bold Jut Out","Bradley Hand ITC","Verdana","DecoType Naskh","SansSerif.bolditalic","Cambria","Vivaldi","Papyrus","Akhbar MT","Arial Black","MS Mincho","Corbel Italic","Segoe UI Gras Italique","PMingLiU","SansSerif.plain","Candara Bold Italic","Old English Text MT","Meiryo UI Gras Italique","PT Bold Stars","Harlow Solid Italic","Lucida Handwriting Italique","Symbol","Meiryo Gras","Farsi Simple Outline","Lucida Calligraphy","Arial","Jokerman","Diwani Bent","SansSerif","Parchment","Vivaldi Italique","Berlin Sans FB","Arial Narrow Italique","Diwani Simple Striped","SansSerif.italic","PT Bold Heading","Candara","Georgia Gras Italique","SimSun","Serif","Dialog.plain","Palatino Linotype Gras Italique","Franklin Gothic Medium Italique","Informal Roman","Kufi Extended Outline","Verdana Gras","Serif.italic","Times New Roman Bold Italic","Times New Roman Bold","Constantia","Impact","Book Antiqua","Lucida Fax Demi-gras","Shruti","Cambria Italic","Italic Outline Art","Simple Outline Pat","Lucida Sans Typewriter","Californian FB","Bookman Old Style Gras","DecoType Naskh Swashes","Stencil","Old Antic Outline Shaded","Consolas","Viner Hand ITC","Tempus Sans ITC","Constantia Italic","Century Gothic Gras Italique","Simple Indust Outline"]}
    
    def addFont(self, tparam):
        for fdef in tparam:
            fkey = fdef[0]
            for p in fdef[1:]:
                if p[0] == 'Font':
                    style = 'Normal'
                    weight = 'Normal'
                    ft = p[1].split('-')
                    if len(ft) == 2 or len(ft) == 3:
                        if len(ft) == 3:
                            if 'B' in ft[2]:
                                weight = 'Bold'
                            if 'I' in ft[2]:
                                style = 'Italic'
                        
                        fname = ft[0].strip('%')
                        if fname in FONT_MAPPING:
                            fname = FONT_MAPPING[fname]
                        else:
                            g_logger.error("Error no match for font:%s" % (fname))
                            
                        self.m_fontMap[fkey] = (fname, int(ft[1]), style, weight)
        
    def addColors(self, tcols):
        for c in tcols:
            col = c[1][1]
            if col[0] != '#':
                r,g,b = COLOR_NAME_TO_RGB[col.lower()]
                col = "#%0.2X%0.2X%0.2X" % (r,g,b)
            self.m_colorMap[c[0]] = col
        # print pprint.PrettyPrinter(2).pformat(self.m_colorMap)
        
    def addLayers(self, tlayers):
        for l in tlayers:
            self.m_layerList.append(l[0])
        g_logger.debug("Layers: %s", str(self.m_layerList))
        
    def addLevel(self, tlevel):
        for l in tlevel:
            self.m_levelList.append(l[0])
        g_logger.debug("Levels: %s", str(self.m_levelList))
        
    def addSymbol(self, filename, doExport):
        symMatch = re.compile(r".*/symbol/")
        m = symMatch.match(filename)
        name = filename[m.end():]
        self.m_symbolFileList[name] = 0
        if doExport:
            logStart("Parse Symbol: " + name)
            svgDumper = AnSVGDumper(self.m_rootdir + "/svgSymbol/" + name[:-4] + ".svg", "./svgSymbol/" + name[:-4] + ".svg", None)
            svgDumper.convertSymbol(name, self.m_rootdir)
            logEnd()
        
    def buildList(self, imgRoot, fullExport, width, height, hvMapper, exportList):
        logStart("Parse image folder: " + imgRoot)
        # root dir for animator files
        self.m_imgRoot = imgRoot
        
        # collect all animator files
        self.m_symbolFileList = {}
        
        for path, _, files in os.walk(self.m_rootdir + "/symbol"):
            for fn in files:
                filename = os.path.join(path,fn).replace('\\','/')
                extension = fn[-4:]
                if extension == ".ans":
                    self.addSymbol(filename, 'symbol' in exportList)
                    
        self.m_classFileList = {}
        classMatch = re.compile(r".*/class/(ActiveText|ActiveSymbol|ActiveBackdrop|ActiveNumber|MobileSymbol|FillingSymbol|Chart)/")
        for path,_,files in os.walk(self.m_rootdir + "/class"):
            for fn in files:
                filename = os.path.join(path,fn).replace('\\','/')
                extension = fn[-4:]
                if extension == ".anc":
                    m = classMatch.match(filename)
                    if m != None:
                        name = filename[m.end():]
                        self.m_classFileList[name] = 0
                        g_logger.debug("Class: %s", name)
                    else:
                        g_logger.warn("Skipping class name: %s" % (filename))
        
        self.m_imageFileList = {}
        imgMatch = re.compile(r".*/image/")
        for path,_,files in os.walk(self.m_rootdir + "/image"):
            for fn in files:
                filename = os.path.join(path,fn).replace('\\','/')
                extension = fn[-4:]
                if extension == ".ani":
                    m = imgMatch.match(filename)
                    name = filename[m.end():]
                    self.m_imageFileList[name] = 0
                    g_logger.debug("Image: %s", name)
        
        # open global file
        if not os.path.exists('images'):
            os.makedirs('images')
        fdico_out = open('images/dico.properties', 'a')
        fcss_out = open('images/image.css', 'a')
        fnavtree_out = open('images/navtree.xml', 'a')
        print >>fnavtree_out, '  <navigationNodeDef navId="navigation_Node_SCADA1" labelName="navigation_Node_SCADA1" isOpen="true" navigable="false">'
        
        # Parse images
        imageNumber = 0
        for path,_,files in os.walk(self.m_rootdir + "/image/" + imgRoot):
            for fn in files:
                extension = fn[-4:]
                if extension == ".ani":
                    filename = os.path.join(path,fn)
                    imageNumber = imageNumber + 1
                    
                    pos = filename.rfind('/image')
                    name = filename[:-4].replace('\\','/')
                    if pos != -1:
                        name = name[pos + 7:]
                    
                    if not name in self.m_imagelist:
                        logStart("Parse image: " + name)
                        parser = AnParser(rootdir, filename, self, name, AnParser.IMAGE_TYPE)
                        parser.readAniFile()
                        self.m_imagelist[name] = parser.m_object
                        parser.m_object.m_name = name
                        logEnd()
                        
                        if 'svg' in exportList:
                            logStart("Dump svg image: " + name)
                            subprocess.call(["an2svg.exe", name, self.m_rootdir])
                            
                            svgDumper = AnSVGDumper(self.m_rootdir + "/svgback/" + name + ".svg", "./images/" + name + ".svg", None)
                            svgDumper.convertBackground()
                            svgDumper = AnSVGDumper(self.m_rootdir + "/svgab/" + name + ".svg", "./images/" + name + "_shapes.svg", hvMapper)
                            svgDumper.convertActiveBackdrop()
                            logEnd()
                        
                        viewid = name.replace('/', '_')
                        if 'shape' in exportList or 'instance' in exportList:
                            logStart("Convert to shape : " + name)
                            dumpGeom = AnGeomDumper(parser.m_object, "images/" + name + ".ani.geo", self, fullExport, width, height, hvMapper, viewid, exportList)
                            # if fullExport true with dump extra file
                            dumpGeom.dumpFile(fullExport)
                            logEnd()
                            
                        # navigation tree
                        print >>fnavtree_out, '    <navigationNodeDef labelName="navigation_Node_%s" navId="%s" isOpen="true" navigable="true" />' % (viewid, viewid)
                       
                        # dico
                        print >>fdico_out, 'tabLayout_%s: %s'  % (viewid, viewid)
                        print >>fdico_out, 'navigation_Node_%s: %s' % (viewid, viewid)
                        
                        # CSS block
                        print >>fcss_out, '.%s .mwt-sv-container {' % (viewid)
                        print >>fcss_out, '    background-color: %s;' % (self.m_imgBackground)
                        print >>fcss_out, '}'

                        
                        
                    if len(self.m_imagelist) > 1000:
                        g_logger.warn("========= Cleaning image cache ===========")
                        self.m_imagelist = {}
        
        print >>fnavtree_out, '  </navigationNodeDef>'
        logEnd()
        
        g_logger.info("Nb used symbol: %d/%d  Nb used class: %d/%d", len(self.m_symbolList), len(self.m_symbolFileList), len(self.m_classList), len(self.m_classFileList))
        g_logger.info("Nb images : %d/%d",imageNumber, len(self.m_imageFileList))
    
    
class An2HVMapper(object):
    def __init__(self):
        
        self.m_classMap = {}
        self.m_instanceMap = {}
        self.m_unkownClasses = []
        self.m_unkownInstances = []
        
    def loadCsvMapping(self, classFileList, instanceFileList):
        # load mapping
        for classFile in classFileList:
            try:
                with open(classFile, 'rb') as csvfile:
                    classReader = csv.DictReader(csvfile, delimiter=',', skipinitialspace='True')
                    for row in classReader:
                        if 'MC_GraphicRep' in row and 'AnClassType' in row:
                            self.m_classMap[row['AnClassType'].strip()] = row['MC_GraphicRep'].strip()
                        else:
                            g_logger.error("Cannot interpret class mapping (%s)", str(row))
            except:
                g_logger.error("Error while reading class mapping file '%s' (%s)", classFile, sys.exc_value)
        
        g_logger.info("Ani to HV class mapping %s" % (str(self.m_classMap)))
        
        
        for instanceFile in instanceFileList:
            try:
                with open(instanceFile, 'rb') as instfile:
                    # instance may be CSV or XML
                    if instanceFile[-4:] == '.csv':
                        instReader = csv.DictReader(instfile, delimiter=',', skipinitialspace='True')
                        for row in instReader:
                            if 'HV_ID' in row and 'AnDBPath' in row:
                                self.m_instanceMap[row['AnDBPath'].strip()] = row['HV_ID'].strip()
                            else:
                                g_logger.error("Cannot interpret instance mapping (%s)", str(row))
                            
                    else:
                        xmldoc = xml.dom.minidom.parse(instfile)
                        for inst in xmldoc.getElementsByTagName('instance'):
                            # <instance hv='B01SE001' scs=':SITE1:B001:F001:POWER:SE001'/>
                            self.m_instanceMap[inst.attributes['scs'].value] = inst.attributes['hv'].value
            except:
                g_logger.error("Error while reading instance mapping file '%s' (%s)", instanceFile, sys.exc_value)   
                 
        g_logger.debug("Ani to HV instance mapping %s" % (str(self.m_instanceMap)))
        
    
    def getHVClass(self, anclass):
        
        if anclass in self.m_classMap:
            g_logger.debug("Replace ani class (%s) with HV class (%s)" % (anclass, self.m_classMap[anclass]))
            return self.m_classMap[anclass]
        
        g_logger.debug("Cannot map ani class (%s) to HV class" % (anclass))
        
        if not anclass in self.m_unkownClasses:
            self.m_unkownClasses.append(anclass)
            
        return anclass
    
    def getHVInstance(self, andbpath):
        
        if andbpath in self.m_instanceMap:
            g_logger.debug("Replace ani dbpath (%s) with HV ID (%s)" % (andbpath, self.m_instanceMap[andbpath]))
            return self.m_instanceMap[andbpath]
        
        g_logger.debug("Cannot map ani dbpath (%s) to HV ID" % (andbpath))
        
        if not andbpath in self.m_unkownInstances:
            self.m_unkownInstances.append(andbpath)
            
        return andbpath
    
    def dumpMissingInfo(self):
        if len(self.m_unkownClasses) > 0:
            g_logger.warn("AN2HV unknown an class %s" %(str(self.m_unkownClasses)))
            
        if len(self.m_unkownInstances) > 0:    
            g_logger.warn("AN2HV unknown an dbpath %s" %(str(self.m_unkownInstances)))
    
# Homogeneous transformation matrix

# x y 1
class Point2D(object):
    def __init__(self, stringrep=None):
        self.x = 0
        self.y = 0
        if stringrep != None:
            vtab = stringrep.strip('(').strip(')').split(',')
            if len(vtab) == 2:
                self.x = float(vtab[0].strip())
                self.y = float(vtab[1].strip())
            
    def __repr__(self):
        return ('Point[%8.2f %8.2f]') % (self.x, self.y)
        
    def __str__(self):
        return ('(%.2f,%.2f)') % (self.x, self.y)

# rs11 rs12 tx 
# rs21 rs22 ty
# 0    0    1         
class Matrix2D(object):
    def __init__(self, stringrep=None):
        self.rs11 = 1
        self.rs12 = 0
        self.rs21 = 0
        self.rs22 = 1
        self.tx = 0
        self.ty = 0
        self.hflip = None
        self.vflip = None
        self.angle = None
        
        if stringrep != None:
            mpart = stringrep.strip().split('(')
            
            vtab = mpart[1].strip('(').strip(')').split(',')
            if len(vtab) == 6:
                self.rs11 = float(vtab[0].strip())
                self.rs12 = float(vtab[1].strip())
                self.rs21 = float(vtab[2].strip())
                self.rs22 = float(vtab[3].strip())
                self.tx = float(vtab[4].strip())
                self.ty = float(vtab[5].strip())
                
    def simplify(self):
        # due to floating conversion some value may be close to zero but not zero
        # change that, this simplify flip guessing
        if self.rs11 * self.rs11 < 0.00000001:
            self.rs11 = 0.0
        if self.rs12 * self.rs12 < 0.00000001:
            self.rs12 = 0.0
        if self.rs21 * self.rs21 < 0.00000001:
            self.rs21 = 0.0
        if self.rs22 * self.rs22 < 0.00000001:
            self.rs22 = 0.0
            
    def __str__(self):
        return 'matrix(%f, %f, %f, %f, %f, %f)' % (self.rs11, self.rs12, self.rs21, self.rs22, self.tx, self.ty)
            
    def __repr__(self):
        return ('Matrix[%8.2f %8.2f %8.2f'\
                ' %8.2f % 8.2f %8.2f'  \
                ' %8.2f % 8.2f %8.2f]') \
                % (self.rs11, self.rs12, self.tx,
                   self.rs21, self.rs22, self.ty,
                   0, 0, 1)

    def __mul__(self, other):
        if isinstance(other, Matrix2D):
            Ars11 = self.rs11
            Ars12 = self.rs12
            Atx = self.tx
            Ars21 = self.rs21
            Ars22 = self.rs22
            Aty = self.ty
            Brs11 = other.rs11
            Brs12 = other.rs12
            Btx = other.tx
            Brs21 = other.rs21
            Brs22 = other.rs22
            Bty = other.ty

            res = Matrix2D()
            res.rs11 = Ars11 * Brs11 + Ars12 * Brs21
            res.rs12 = Ars11 * Brs12 + Ars12 * Brs22
            res.tx = Ars11 * Btx + Ars12 * Bty + Atx
            res.rs21 = Ars21 * Brs11 + Ars22 * Brs21
            res.rs22 = Ars21 * Brs12 + Ars22 * Brs22
            res.ty = Ars21 * Btx + Ars22 * Bty + Aty
            return res
            
        elif isinstance(other, Point2D):
            A = self
            B = other
            res = Point2D()
            res.x = A.rs11 * B.x + A.rs12 * B.y + A.tx
            res.y = A.rs21 * B.x + A.rs22 * B.y + A.ty
            return res
            
    def getAngle(self):
        if self.angle != None:
            return self.angle
        
        self.simplify()
        # return transformed angle in degree
        a = math.atan2(self.rs21, self.rs11) * 180.0 / math.pi
        if a * a < 0.00000001:
            a = 0.0
             
        self.hflip = False
        self.vflip = False
        self.angle = a
        
        if self.rs11 * self.rs22 < 0:
            # calculate angle for HFlip
            hangle = -self.angle
            if hangle < 0:
                hangle = 360 + hangle
                    
            # calculate angle for VFlip
            vangle = 180 - self.angle
            if vangle < 0:
                vangle = 360 + vangle
                
            # choose smallest
            if hangle < vangle:
                self.hflip = True
                self.angle = hangle
            else:
                self.vflip = True
                self.angle = vangle
                
        if self.angle < 0:
            self.angle = self.angle + 360
        return self.angle
            
    def getScale(self):
        self.simplify()
        # we transform a size 1 vector to get its length
        return math.sqrt(self.rs11 * self.rs11 + self.rs12 * self.rs12)
        
    def isHFlip(self):
        if self.hflip == None:
            _ = self.getAngle()

        return self.hflip
        
    def isVFlip(self):
        if self.vflip == None:
            _ = self.getAngle()

        return self.vflip
    
       
#--------------------------------------------------------------------------------------
# create logger
g_logger = logging.getLogger('parseani')
g_logger.setLevel(logging.DEBUG)
# create console handler
ch = logging.StreamHandler(sys.stdout)
ch.setLevel(logging.INFO)
# create formatter and add it to the handlers
formatter = logging.Formatter('%(asctime)s - %(name)s[%(levelname)s] %(message)s')
ch.setFormatter(formatter)
# add the handlers to the logger
g_logger.addHandler(ch)

g_logLevelMap = { 'error' : logging.ERROR, 'warn' : logging.WARN, 'info' : logging.INFO, 'debug' : logging.DEBUG}
g_logStack = []
def logStart(msg):
    sd = datetime.datetime.today()
    prefix = '    ' * len(g_logStack)
        
    g_logStack.append((sd, msg))
    
    g_logger.info(">%s%s", prefix, msg)

def logEnd():
    ed = datetime.datetime.today()
    sd, msg = g_logStack.pop()
    prefix = '    ' * len(g_logStack)
    g_logger.info("<%s%s FOR: %s\n", prefix, str(ed - sd), msg)
#--------------------------------------------------------------------------------------

if __name__ == "__main__":
    # create option parser
    parser = argparse.ArgumentParser()
    parser.add_argument("-d", "--datadir", dest="rootdir",
                      help="root folder of Animator workspace (default .)", default=".")
    parser.add_argument("-i", "--imagedir", dest="imgdir",
                      help="Animator subimage folder to convert (default '')", default="")
    parser.add_argument("-c", "--classMappingFile", dest="classMappingFile", action='append',
                      help="Mapping file between animator class and Hypervisor generic symbol (default no mapping)", default=[])
    parser.add_argument("-m", "--instanceMappingFile", dest="instanceMappingFile", action='append',
                      help="Mapping file between animator dbpath and Hypervisor equipment ID (default no mapping)", default=[])
    parser.add_argument("-e", "--export", dest="exportList", action='append',
                      help="Type of file to export: svg, shape, symbol, instance (default svg)", default=[])
                      
    parser.add_argument("-W", "--width", dest="width", type=int,
                      help="Width of exported image (default source width)", default=-1)
    parser.add_argument("-H", "--height", dest="height", type=int,
                      help="Height of exported image (default source height)", default=-1)
    
    parser.add_argument("-f", "--full", action="store_true", dest="fullexport",
                      help="Export ActiveClass geometry too", default=False)
    parser.add_argument("-l", "--logLevel", dest="logLevel",
                      help="Application log level [error,warn,info,debug] (default 'info') ", default="info")
    
    parser.add_argument("-F", "--font", dest="font",
                      help="Font to use for text (default use animator specified font)", default="")
          
    options = parser.parse_args()
    
    # set log level
    if options.logLevel in g_logLevelMap:
        g_logger.setLevel(g_logLevelMap[options.logLevel])
        for h in g_logger.handlers:
            h.setLevel(g_logLevelMap[options.logLevel])
    else:
        g_logger.error("Unknown loglevel (%s), info will be used" % (options.logLevel))
    
    # get parameter
    rootdir = options.rootdir
    imgdir = options.imgdir
    
    if len(options.exportList) == 0:
        options.exportList.add('svg')
      
    # modify font mapping
    if options.font != '':
        for k in FONT_MAPPING.keys():
            FONT_MAPPING[k] = options.font
            
    # create worksapce object
    wks = AnWorkspace(rootdir)
    wks.loadProperty('animator.ini')
    wks.m_imgBackground = wks.getColor(wks.m_imgBackground)
    
    # load mapping
    hvMapper = An2HVMapper()
    hvMapper.loadCsvMapping(options.classMappingFile, options.instanceMappingFile)

    try:
        # load and convert animator images
        wks.buildList(imgdir, options.fullexport, options.width , options.height, hvMapper, options.exportList)
    except:
        g_logger.error("Exception during image convertion: %s" % (traceback.format_exc()))
    
    g_logger.debug("Unused symbols:")
    for (k,v) in wks.m_symbolFileList.items():
        if v == 0:
            g_logger.debug(" %s", k)
    g_logger.debug("Unused classes:")
    for (k,v) in wks.m_classFileList.items():
        if v == 0:
            g_logger.debug(" %s", k)
 
    hvMapper.dumpMissingInfo()
    # print str(wks.m_symbolFileList)
    # print str(wks.m_classFileList)
    # print str(wks.m_imageFileList)
    # wks.dumAniObject()
    
    
