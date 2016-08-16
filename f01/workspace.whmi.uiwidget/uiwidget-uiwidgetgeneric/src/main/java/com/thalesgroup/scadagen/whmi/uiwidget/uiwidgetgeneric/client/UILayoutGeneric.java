package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryCache;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIPanelGeneric_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;

public class UILayoutGeneric extends UIWidget_i {
	
	private Logger logger = Logger.getLogger(UILayoutGeneric.class.getName());

	private HashMap<String, UIWidget_i> uiWidgetGeneric = new HashMap<String, UIWidget_i>();
	
	private HashMap<Integer, HashMap<String, String>> values = new HashMap<Integer, HashMap<String, String>>();
	
	private int rows;
	private int cols;
	private int totals;
	
	private String strPanel;
	private String strCSS;

	private Dictionary dictionaryHeader = null;
	private Dictionary dictionaryOption = null;
	
	public UIWidget_i getUIWidget(String xmlFile) {
		return uiWidgetGeneric.get(xmlFile);
	}
	
	@Override
	public void init() {
		
		logger.log(Level.SEVERE, "init xmlFile["+this.xmlFile+"]");
		
		DictionaryCache uiPanelSettingCache = DictionaryCache.getInstance("UIWidgetGeneric");
		
		this.dictionaryHeader = uiPanelSettingCache.getDictionary( this.xmlFile, DictionaryCacheInterface.Header );
		this.dictionaryOption = uiPanelSettingCache.getDictionary( this.xmlFile, DictionaryCacheInterface.Option );
		
		ready(this.dictionaryHeader);
		ready(this.dictionaryOption);
		
		// Start the UIGeneric
		logger.log(Level.FINE, "getMainPanel Begin");
		logger.log(Level.SEVERE, "getMainPanel xmlFile["+this.xmlFile+"]");

		logger.log(Level.SEVERE, "getMainPanel strPanel["+strPanel+"] strCSS["+strCSS+"]");
		
		rootPanel = null;
		if ( null != strPanel ) {
			if ( UIPanelGeneric_i.PanelAttribute.HorizontalPanel.equalsName(strPanel) ) {
				rootPanel = new HorizontalPanel();
			} else if ( UIPanelGeneric_i.PanelAttribute.VerticalPanel.equalsName(strPanel) ) {
				rootPanel = new VerticalPanel();
			} else if ( UIPanelGeneric_i.PanelAttribute.DockLayoutPanel.equalsName(strPanel) ) {
				rootPanel = new DockLayoutPanel(Unit.PX);
			} else if ( UIPanelGeneric_i.PanelAttribute.AbsolutePanel.equalsName(strPanel) ) {
				rootPanel = new AbsolutePanel();
				Element e = rootPanel.getElement();
				DOM.setStyleAttribute(e, "position", "absolute");
			} else {
			}

		} else {
			logger.log(Level.SEVERE, "getMainPanel strPanel IS NULL");
		}

		if ( null != rootPanel ) {
			
			if ( null != strCSS ) {
				rootPanel.addStyleName(strCSS);
			} else {
				logger.log(Level.SEVERE, "getMainPanel strCSS IS NULL");
			}
			
		    for ( int i = 0 ; i < rows ; ++i ) {
		    	
				logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] Begin");
				
				for ( int j = 0 ; j < cols ; ++j ) {
					
					int index = (i*cols)+j;
					
					logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] => index["+index+"]");
					
					HashMap<String, String> valueMap = this.values.get(index);
					
					if ( null != valueMap ) {
						String type					= valueMap.get(UIPanelGeneric_i.WidgetAttribute.type.toString());
						String widget 				= valueMap.get(UIPanelGeneric_i.WidgetAttribute.widget.toString());
						String direction			= valueMap.get(UIPanelGeneric_i.WidgetAttribute.direction.toString());
						String size					= valueMap.get(UIPanelGeneric_i.WidgetAttribute.width.toString());
						String cellwidth			= valueMap.get(UIPanelGeneric_i.WidgetAttribute.cellwidth.toString());
						String cellheight			= valueMap.get(UIPanelGeneric_i.WidgetAttribute.cellheight.toString());
						String left					= valueMap.get(UIPanelGeneric_i.WidgetAttribute.left.toString());
						String top					= valueMap.get(UIPanelGeneric_i.WidgetAttribute.top.toString());
						String csscontainer			= valueMap.get(UIPanelGeneric_i.WidgetAttribute.csscontainer.toString());
						
						logger.log(Level.SEVERE, "getMainPanel type["+type+"] widget["+widget+"]");
						logger.log(Level.SEVERE, "getMainPanel direction["+direction+"] size["+size+"]");
						
						if ( null != widget ) {

							Panel panel = null;
							
							if ( UIPanelGeneric_i.TypeAttribute.predefine.equalsName(type) ) {
								
								// predefine
								UIWidgetMgr uiPredefinePanelMgr = UIWidgetMgr.getInstance();
								uiWidgetGeneric.put(widget, uiPredefinePanelMgr.getUIWidget(widget));
								UIWidget_i uiWidget = uiWidgetGeneric.get(widget);
								if ( null != uiWidget ) {
									uiWidget.setUINameCard(this.uiNameCard);
									panel = uiWidget.getMainPanel();
								} else {
									logger.log(Level.SEVERE, "getMainPanel created UIPredefinePanelMgr widget["+widget+"] IS NULL");
								}
							} else if ( UIPanelGeneric_i.TypeAttribute.layoutconfiguration.equalsName(type) ) {
								
								// layoutconfiguration
								uiWidgetGeneric.put(widget, new UILayoutGeneric());
								UIWidget_i uiWidget = uiWidgetGeneric.get(widget);
								if ( null != uiWidget ) {
									uiWidget.setUINameCard(this.uiNameCard);
									uiWidget.setXMLFile(widget);
									uiWidget.init();
									panel = uiWidget.getMainPanel();
								} else {
									logger.log(Level.SEVERE, "getMainPanel created UIPanelGeneric widget["+widget+"] IS NULL");
								}
							} else if ( UIPanelGeneric_i.TypeAttribute.configuration.equalsName(type) ) {
								
								//configuration
								uiWidgetGeneric.put(widget, new UIWidgetGeneric());
								UIWidget_i uiWidget = uiWidgetGeneric.get(widget);
								if ( null != uiWidget ) {
									uiWidget.setUINameCard(this.uiNameCard);
									uiWidget.setXMLFile(widget);
									uiWidget.init();
									panel = uiWidget.getMainPanel();
								} else {
									logger.log(Level.SEVERE, "getMainPanel created UIWidgetGeneric widget["+widget+"] IS NULL");
								}
							} else {
								logger.log(Level.SEVERE, "getMainPanel type IS INVALID");
							}
							
							if ( null != panel ) {
								if ( UIPanelGeneric_i.PanelAttribute.DockLayoutPanel.equalsName(strPanel) ) {
									
									//DockLayoutPanel
									if ( null != direction ) {
										int width = 0;
										if ( null != size ) {
											try {
												width = Integer.parseInt(size);
											} catch ( NumberFormatException e) {
												logger.log(Level.SEVERE, "getMainPanel size IS INVALID");
												logger.log(Level.SEVERE, "getMainPanel e["+e.toString()+"]");
											}
										}
										
										if ( UIPanelGeneric_i.DirectionAttribute.North.equalsName(direction) ) {
											((DockLayoutPanel)rootPanel).addNorth(panel, width);
										} else if ( UIPanelGeneric_i.DirectionAttribute.East.equalsName(direction) ) {
											((DockLayoutPanel)rootPanel).addEast(panel, width);
										} else if ( UIPanelGeneric_i.DirectionAttribute.South.equalsName(direction) ) {
											((DockLayoutPanel)rootPanel).addSouth(panel, width);
										} else if ( UIPanelGeneric_i.DirectionAttribute.West.equalsName(direction) ) {
											((DockLayoutPanel)rootPanel).addWest(panel, width);
										} else if ( UIPanelGeneric_i.DirectionAttribute.Center.equalsName(direction) ) {
											((DockLayoutPanel)rootPanel).add(panel);
										} else {
											logger.log(Level.SEVERE, "getMainPanel direction IS INVALID");
										}
									} else {
										logger.log(Level.SEVERE, "getMainPanel direction IS null");
									}
								} else if ( UIPanelGeneric_i.PanelAttribute.AbsolutePanel.equalsName(strPanel) ) {
								
									//AbsolutePanel
									int x = -1;
									int y = -1;
									try {
										if ( null != left )	x = Integer.parseInt(left);
										if ( null != top )	y = Integer.parseInt(top);
									} catch ( NumberFormatException e ) {
										logger.log(Level.SEVERE, "getMainPanel left or top IS INVALID");
									}
									((AbsolutePanel)rootPanel).add(panel, x, y);
								
								} else {
									rootPanel.add(panel);
									
									if ( null != cellwidth ) 	((CellPanel) rootPanel).setCellWidth(panel, cellwidth);
									if ( null != cellheight )	((CellPanel) rootPanel).setCellHeight(panel, cellwidth);
								}
								
								logger.log(Level.SEVERE, "getMainPanel csscontainer["+csscontainer+"]");
								
								if ( null != panel ) {
									if ( null != csscontainer ) {
										Element container = DOM.getParent(panel.getElement());
										container.setClassName(csscontainer);
									}
								}
								
							} else {
								logger.log(Level.SEVERE, "getMainPanel complexPanel IS NULL");
							}
						} else {
							logger.log(Level.SEVERE, "getMainPanel config IS NULL");
						}					
					}
				}
		    }
		} else {
			logger.log(Level.SEVERE, "getMainPanel Panel IS NULL");
		}
		
		logger.log(Level.FINE, "getMainPanel End");
	}

	public void ready(Dictionary dictionary) {
		logger.log(Level.FINE, "ready Begin");
		logger.log(Level.SEVERE, "ready this.xmlFile["+this.xmlFile+"]");
		
		if ( null != dictionary ) {
			String xmlFile				= (String)dictionary.getAttribute(DictionaryCacheInterface.XmlFile);
			String XmlTag				= (String)dictionary.getAttribute(DictionaryCacheInterface.XmlTag);
			String CreateDateTimeLabel	= (String)dictionary.getAttribute(DictionaryCacheInterface.CreateDateTimeLabel);
			
			logger.log(Level.SEVERE, "ready dictionary XmlFile["+xmlFile+"] XmlTag["+XmlTag+"] CreateDateTimeLabel["+CreateDateTimeLabel+"]");			
			
			if ( 0 == DictionaryCacheInterface.Header.compareTo(XmlTag)) {

				for ( Object o : dictionary.getValueKeys() ) {
					if ( null != o ) {
						Dictionary d2 = (Dictionary) dictionary.getValue(o);
						for ( Object o2 : d2.getAttributeKeys() ) {
							if ( null != o2 ) {
							}
						}
						for ( Object o2 : d2.getValueKeys() ) {
							if ( null != o2 ) {
								// Get Header Begin
								String k = (String)o2;
								String v = (String)d2.getValue(o2);
								if ( UIPanelGeneric_i.RootAttribute.rows.equalsName(k) ) {
									rows = Integer.parseInt(v);
								} else if ( UIPanelGeneric_i.RootAttribute.cols.equalsName(k) ) {
									cols = Integer.parseInt(v);
								} else if ( UIPanelGeneric_i.RootAttribute.rootPanel.equalsName(k) ) {
									strPanel = v;
								} else if ( UIPanelGeneric_i.RootAttribute.rootCSS.equalsName(k) ) {
									strCSS = v;
								}
								// Get Header End								
							}
						}
					}
				}
				
				totals = rows * cols;
				
				logger.log(Level.FINE, "ready dictionary cols["+cols+"] rows["+rows+"] => totals["+totals+"]");
				
				for ( int i = 0 ; i < totals ; ++i ) {
					values.put(i, new HashMap<String, String>());
				}				

			} else if ( 0 == DictionaryCacheInterface.Option.compareTo(XmlTag) ) {
				
				for ( Object o : dictionary.getValueKeys() ) {
					if ( null != o ) {
						
						String key=null;
						String keys[]=null;
						int index=0;
						int row=0;
						int col=0;
						
						Dictionary d2 = (Dictionary) dictionary.getValue(o);
						for ( Object o2 : d2.getAttributeKeys() ) {
							if ( null != o2 ) {
								
								// Get Header Begin
								String k = (String)o2;
								String v = (String)d2.getAttribute(o2);
								if ( 0 == DictionaryCacheInterface.Key.compareTo(k) ) {
									
									key = v;
									
									if ( null != key) {
										keys = v.split("\\|");
										
										logger.log(Level.FINE, "ready dictionary key["+key+"]");
										
										break;
									}
									
								}
								// Get Header End
							}
						}
						
						logger.log(Level.FINE, "ready dictionary key["+key+"]");
						
						if ( null != keys ) {
							if ( 2 == keys.length ) {
								boolean isvalid = false;
								try {
									row = Integer.parseInt(keys[0]);
									col = Integer.parseInt(keys[1]);
									
									isvalid=true;
								} catch ( NumberFormatException e ) {
									logger.log(Level.SEVERE, "ready NumberFormatException e["+e+"]");
								}
								
								if ( isvalid ) {
									logger.log(Level.FINE, "ready dictionary row["+row+"] col["+col+"]");
									
									index = (row * cols) + col;
									
									logger.log(Level.FINE, "ready dictionary row["+row+"] col["+col+"] => index["+index+"]");
									
									HashMap<String, String> hashMap = this.values.get(Integer.valueOf(index));
									if ( null != hashMap ) {
										for ( Object o2 : d2.getValueKeys() ) {
											if ( null != o2 ) {
												String k = (String)o2;
												String v = (String)d2.getValue(o2);
												
												logger.log(Level.FINE, "ready dictionary k["+k+"] v["+v+"]");
				
												hashMap.put(k, v);
											}
										}
									} else {
										logger.log(Level.SEVERE, "ready row["+row+"] col["+col+"] => index["+index+"] Index NOT EXISTS");
									}
								} else {
									logger.log(Level.SEVERE, "ready keys[0]["+keys[0]+"] OR keys[1]["+keys[1]+"] is not a number");
								}
							}
						} else {
							logger.log(Level.SEVERE, "ready key IS NULL");
						}
					}
				}
			}

		} else {
			logger.log(Level.SEVERE, "ready this.xmlFile["+this.xmlFile+"] dictionary IS NULL");
		}
		
		logger.log(Level.FINE, "ready End");
		
	}

	public UIWidget_i getPredefineWidget(String widget) {
		return uiWidgetGeneric.get(widget);
	}

}
