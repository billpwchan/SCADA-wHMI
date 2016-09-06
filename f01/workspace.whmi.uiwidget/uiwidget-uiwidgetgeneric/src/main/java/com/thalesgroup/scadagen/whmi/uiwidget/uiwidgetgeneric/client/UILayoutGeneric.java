package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.DirectionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.PanelAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.RootAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.TypeAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.WidgetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;

public class UILayoutGeneric extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutGeneric.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	
	private HashMap<String, UIWidget_i> uiGeneric = new HashMap<String, UIWidget_i>();
	
	private HashMap<Integer, HashMap<String, String>> values = new HashMap<Integer, HashMap<String, String>>();
	
	private int rows;
	private int cols;
	private int totals;
	
	private String strPanel;
	private String strCSS;

	private Dictionary dictionaryHeader = null;
	private Dictionary dictionaryOption = null;
	
	public UIWidget_i getUIWidget(String xmlFile) {
		return uiGeneric.get(xmlFile);
	}
	
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
	};
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.info(className, function, "xmlFile[{}]", this.xmlFile);

		DictionariesCache dictionariesCache = DictionariesCache.getInstance("UIWidgetGeneric");
			
		this.dictionaryHeader = dictionariesCache.getDictionary( this.xmlFile, DictionaryCacheInterface.Header );
		this.dictionaryOption = dictionariesCache.getDictionary( this.xmlFile, DictionaryCacheInterface.Option );
		
		ready(this.dictionaryHeader);
		ready(this.dictionaryOption);
		
		// Start the UIGeneric

		logger.begin(className, function);
		logger.info(className, function, "strPanel[{}] strCSS[{}]", strPanel, strCSS);
		
		rootPanel = null;
		if ( null != strPanel ) {
			if ( PanelAttribute.HorizontalPanel.equalsName(strPanel) ) {
				rootPanel = new HorizontalPanel();
			} else if ( PanelAttribute.VerticalPanel.equalsName(strPanel) ) {
				rootPanel = new VerticalPanel();
			} else if ( PanelAttribute.DockLayoutPanel.equalsName(strPanel) ) {
				rootPanel = new DockLayoutPanel(Unit.PX);
			} else if ( PanelAttribute.AbsolutePanel.equalsName(strPanel) ) {
				rootPanel = new AbsolutePanel();
//				Element e = rootPanel.getElement();
//				DOM.setStyleAttribute(e, "position", "absolute");
			} else {
			}

		} else {
			logger.warn(className, function, "strPanel IS NULL");
		}

		if ( null != rootPanel ) {
			
			if ( null != strCSS ) {
				rootPanel.addStyleName(strCSS);
			} else {
				logger.warn(className, function, "strCSS IS NULL");
			}
			
		    for ( int i = 0 ; i < rows ; ++i ) {
		    	
		    	logger.info(className, function, "Build Filter Table Loop i[{}] Begin", i);
				
				for ( int j = 0 ; j < cols ; ++j ) {
					
					int index = (i*cols)+j;
					
					logger.info(className, function, "Build Filter Table Loop i[{}] j[{}] => index[{}]", new Object[]{i, j, index});
					
					HashMap<String, String> valueMap = this.values.get(index);
					
					if ( null != valueMap ) {
						String type					= valueMap.get(WidgetAttribute.type.toString());
						String widget 				= valueMap.get(WidgetAttribute.widget.toString());
						String direction			= valueMap.get(WidgetAttribute.direction.toString());
						String size					= valueMap.get(WidgetAttribute.width.toString());
						String cellwidth			= valueMap.get(WidgetAttribute.cellwidth.toString());
						String cellheight			= valueMap.get(WidgetAttribute.cellheight.toString());
						String left					= valueMap.get(WidgetAttribute.left.toString());
						String top					= valueMap.get(WidgetAttribute.top.toString());
						String csscontainer			= valueMap.get(WidgetAttribute.csscontainer.toString());
						String uiview				= valueMap.get(WidgetAttribute.uiView.toString());
						String element				= valueMap.get(WidgetAttribute.element.toString());
						
						HashMap<String, Object> options = new HashMap<String, Object>();
						
						for ( String key : valueMap.keySet() ) {
							Object value = valueMap.get(key);
							if ( null != value ) options.put(key, value);
						}
						
						logger.info(className, function, "xmlFile[{}]", xmlFile);
						for ( String key : valueMap.keySet() ) {
							String value = valueMap.get(key);
							logger.info(className, function, "valueMap key[{}] value[{}]", key, value);
						}
						
//						for ( OptionAttribute optionAttibute : OptionAttribute.values() ) {
//							String strOptionAttibute = optionAttibute.toString();
//							String option = valueMap.get(strOptionAttibute);
//							logger.info(className, function, "strOptionAttibute[{}] option[{}]", strOptionAttibute, option);
//							if ( null != option ) options.put(strOptionAttibute, option);
//						}
//						
//						for ( ActionAttribute actionAttibute : ActionAttribute.values() ) {
//							String strActionAttibute = actionAttibute.toString();
//							String option = valueMap.get(strActionAttibute);
//							logger.info(className, function, "strActionAttibute[{}] option[{}]", strActionAttibute, option);
//							if ( null != option ) options.put(strActionAttibute, option);
//						}
//						
//						logger.info(className, function, "xmlFile[{}]", xmlFile);
//						for ( String key : valueMap.keySet() ) {
//							String value = valueMap.get(key);
//							logger.info(className, function, "valueMap key[{}] value[{}]", key, value);
//						}
//					
//						logger.info(className, function, "xmlFile[{}]", xmlFile);
//						for ( String key : options.keySet() ) {
//							String value = (String) options.get(key);
//							logger.info(className, function, "options key[{}] value[{}]", key, value);
//						}
						
						if ( null != widget ) {

							Panel panel = null;
							
							UIWidget_i uiWidget = null;
							
							if ( TypeAttribute.predefine.equalsName(type) ) {
								
								// predefine
								UIWidgetMgr uiPredefinePanelMgr = UIWidgetMgr.getInstance();
								uiGeneric.put(widget, uiPredefinePanelMgr.getUIWidget(widget, uiview, uiNameCard, options));
								uiWidget = uiGeneric.get(widget);
								if ( null != uiWidget ) {
									uiWidget.setUINameCard(this.uiNameCard);
									panel = uiWidget.getMainPanel();
								} else {
									logger.warn(className, function, "created UIPredefinePanelMgr widget[{}] IS NULL", widget);
								}
							} else if ( TypeAttribute.layoutconfiguration.equalsName(type) ) {
								
								String viewSel = widget;
								if ( null != uiview ) viewSel = uiview;
								
								// layoutconfiguration
								uiGeneric.put(widget, new UILayoutGeneric());
								uiWidget = uiGeneric.get(widget);
								if ( null != uiWidget ) {
									uiWidget.setUINameCard(this.uiNameCard);
									uiWidget.setXMLFile(viewSel);
									uiWidget.init();
									panel = uiWidget.getMainPanel();
								} else {
									logger.warn(className, function, "created UILayoutGeneric widget[{}] IS NULL", widget);
								}
							} else if ( TypeAttribute.configuration.equalsName(type) ) {
								
								String viewSel = widget;
								if ( null != uiview ) viewSel = uiview;
								
								//configuration
								uiGeneric.put(widget, new UIWidgetGeneric());
								uiWidget = uiGeneric.get(widget);
								if ( null != uiWidget ) {
									uiWidget.setUINameCard(this.uiNameCard);
									uiWidget.setXMLFile(viewSel);
									uiWidget.init();
									panel = uiWidget.getMainPanel();
								} else {
									logger.warn(className, function, "created UIWidgetGeneric widget[{}] IS NULL", widget);
								}
							} else {
								logger.warn(className, function, "type IS INVALID");
							}
							
							if ( null != uiWidget ) {
								if ( ! uiGeneric.containsKey(element) ) {
									if ( null != element && element.trim().length() > 0 ) {
										logger.info(className, function, "uiGeneric element[{}] ADDED", element);
										uiGeneric.put(element, uiWidget);
									} else {
										logger.warn(className, function, "uiGeneric element[{}] IS NULL OR EMPTY, SKIP ADD", element);
									}
								} else {
									logger.warn(className, function, "uiGeneric contain the element[{}], SKIP ADD", element);
								}
							} else {
								logger.warn(className, function, "uiGeneric uiWidget IS NULL, SKIP ADD");
							}
							
							if ( null != panel ) {
								if ( rootPanel instanceof DockLayoutPanel ) {
									
									//DockLayoutPanel
									if ( null != direction ) {
										int width = 0;
										if ( null != size ) {
											try {
												width = Integer.parseInt(size);
											} catch ( NumberFormatException e) {
												logger.warn(className, function, "size IS INVALID");
												logger.warn(className, function, "e[{}]", e.toString());
											}
										}
										
										if ( DirectionAttribute.North.equalsName(direction) ) {
											((DockLayoutPanel)rootPanel).addNorth(panel, width);
										} else if ( DirectionAttribute.East.equalsName(direction) ) {
											((DockLayoutPanel)rootPanel).addEast(panel, width);
										} else if ( DirectionAttribute.South.equalsName(direction) ) {
											((DockLayoutPanel)rootPanel).addSouth(panel, width);
										} else if ( DirectionAttribute.West.equalsName(direction) ) {
											((DockLayoutPanel)rootPanel).addWest(panel, width);
										} else if ( DirectionAttribute.Center.equalsName(direction) ) {
											((DockLayoutPanel)rootPanel).add(panel);
										} else {
											logger.warn(className, function, "direction IS INVALID");
										}
									} else {
										logger.info(className, function, "direction IS null");
									}
								} else if ( rootPanel instanceof AbsolutePanel ) {
								
									//AbsolutePanel
									int x = -1;
									int y = -1;
									try {
										if ( null != left )	x = Integer.parseInt(left);
										if ( null != top )	y = Integer.parseInt(top);
									} catch ( NumberFormatException e ) {
										logger.info(className, function, "left or top IS INVALID");
									}
									((AbsolutePanel)rootPanel).add(panel, x, y);
								
								} else {
									rootPanel.add(panel);
									
									if ( null != cellwidth ) 	((CellPanel) rootPanel).setCellWidth(panel, cellwidth);
									if ( null != cellheight )	((CellPanel) rootPanel).setCellHeight(panel, cellwidth);
								}
								
								logger.info(className, function, "csscontainer["+csscontainer+"]");
								
								if ( null != panel ) {
									if ( null != csscontainer ) {
										DOM.getParent(panel.getElement()).setClassName(csscontainer);
									}
								}
								
							} else {
								logger.warn(className, function, "complexPanel IS NULL");
							}
						} else {
							logger.warn(className, function, "config IS NULL");
						}					
					}
				}
		    }
		} else {
			logger.warn(className, function, "Panel IS NULL");
		}
		
		logger.end(className, function);
	}

	public void ready(Dictionary dictionary) {
		final String function = "ready";
		
		logger.begin(className, function);
		logger.info(className, function, "this.xmlFile[{}]", this.xmlFile);
		
		if ( null != dictionary ) {
			String xmlFile				= (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.FileName.toString());
			String XmlTag				= (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.Tag.toString());
			String CreateDateTimeLabel	= (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.DateTime.toString());
			
			logger.info(className, function, "dictionary XmlFile[{}] XmlTag[{}] CreateDateTimeLabel[{}]", new Object[]{xmlFile, XmlTag, CreateDateTimeLabel});			
			
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
								if ( RootAttribute.rows.equalsName(k) ) {
									rows = Integer.parseInt(v);
								} else if ( RootAttribute.cols.equalsName(k) ) {
									cols = Integer.parseInt(v);
								} else if ( RootAttribute.rootPanel.equalsName(k) ) {
									strPanel = v;
								} else if ( RootAttribute.rootCSS.equalsName(k) ) {
									strCSS = v;
								}
								// Get Header End								
							}
						}
					}
				}
				
				totals = rows * cols;
				
				logger.info(className, function, "dictionary cols[{}] rows[{}] => totals[{}]", new Object[]{cols, rows, totals});
				
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
										
										logger.info(className, function, "dictionary key[{}]", key);
										
										break;
									}
									
								}
								// Get Header End
							}
						}
						
						logger.info(className, function, "dictionary key[{}]", key);
						
						if ( null != keys ) {
							if ( 2 == keys.length ) {
								boolean isvalid = false;
								try {
									row = Integer.parseInt(keys[0]);
									col = Integer.parseInt(keys[1]);
									
									isvalid=true;
								} catch ( NumberFormatException e ) {
									logger.warn(className, function, "NumberFormatException e[{}]", e);
								}
								
								if ( isvalid ) {
									logger.info(className, function, "dictionary row[{}] col[{}]", new Object[]{row, col});
									
									index = (row * cols) + col;
									
									logger.info(className, function, "dictionary row[{}] col[{}] => index[{}]", new Object[]{row, col, index});
									
									HashMap<String, String> hashMap = this.values.get(Integer.valueOf(index));
									if ( null != hashMap ) {
										for ( Object o2 : d2.getValueKeys() ) {
											if ( null != o2 ) {
												String k = (String)o2;
												String v = (String)d2.getValue(o2);
												
												logger.info(className, function, "dictionary k[{}] v[{}]", new Object[]{k, v});
				
												hashMap.put(k, v);
											}
										}
									} else {
										logger.warn(className, function, "row[{}] col[{}] => index[{}] Index NOT EXISTS", new Object[]{row, col, index});
									}
								} else {
									logger.warn(className, function, "keys[0][{}] OR keys[1][{}] is not a number", new Object[]{keys[0], keys[1]});
								}
							}
						} else {
							logger.warn(className, function, "key IS NULL");
						}
					}
				}
			}

		} else {
			logger.warn(className, function, "this.xmlFile[{}] dictionary IS NULL", this.xmlFile);
		}
		logger.end(className, function);
	}

	public UIWidget_i getPredefineWidget(String widget) {
		logger.info(className, "getPredefineWidget", "widget[{}]", widget);
		return uiGeneric.get(widget);
	}

}
