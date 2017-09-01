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