package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;
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
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
	};
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.info(className, function, "xmlFile[{}]", this.xmlFile);
		
		DictionaryCache uiPanelSettingCache = DictionaryCache.getInstance("UIWidgetGeneric");
		
		this.dictionaryHeader = uiPanelSettingCache.getDictionary( this.xmlFile, DictionaryCacheInterface.Header );
		this.dictionaryOption = uiPanelSettingCache.getDictionary( this.xmlFile, DictionaryCacheInterface.Option );
		
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
				Element e = rootPanel.getElement();
				DOM.setStyleAttribute(e, "position", "absolute");
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
						String view					= valueMap.get(WidgetAttribute.view.toString());
						
						String option1				= valueMap.get(WidgetAttribute.option1.toString());
						String option2				= valueMap.get(WidgetAttribute.option2.toString());
						String option3				= valueMap.get(WidgetAttribute.option3.toString());
						String option4				= valueMap.get(WidgetAttribute.option4.toString());
						String option5				= valueMap.get(WidgetAttribute.option5.toString());
						
						HashMap<String, Object> options = new HashMap<String, Object>();
						if ( null != option1 ) options.put(WidgetAttribute.option1.toString(), option1);
						if ( null != option2 ) options.put(WidgetAttribute.option2.toString(), option2);
						if ( null != option3 ) options.put(WidgetAttribute.option3.toString(), option3);
						if ( null != option4 ) options.put(WidgetAttribute.option4.toString(), option4);
						if ( null != option5 ) options.put(WidgetAttribute.option5.toString(), option5);
						
						logger.info(className, function, "type[{}] widget[{}]", new Object[]{type, widget});
						logger.info(className, function, "direction[{}] size[{}]", new Object[]{direction, size});
						
						if ( null != widget ) {

							Panel panel = null;
							
							if ( TypeAttribute.predefine.equalsName(type) ) {
								
								// predefine
								UIWidgetMgr uiPredefinePanelMgr = UIWidgetMgr.getInstance();
								uiWidgetGeneric.put(widget, uiPredefinePanelMgr.getUIWidget(widget, view, uiNameCard, options));
								UIWidget_i uiWidget = uiWidgetGeneric.get(widget);
								if ( null != uiWidget ) {
									uiWidget.setUINameCard(this.uiNameCard);
									panel = uiWidget.getMainPanel();
								} else {
									logger.warn(className, function, "created UIPredefinePanelMgr widget[{}] IS NULL", widget);
								}
							} else if ( TypeAttribute.layoutconfiguration.equalsName(type) ) {
								
								// layoutconfiguration
								uiWidgetGeneric.put(widget, new UILayoutGeneric());
								UIWidget_i uiWidget = uiWidgetGeneric.get(widget);
								if ( null != uiWidget ) {
									uiWidget.setUINameCard(this.uiNameCard);
									uiWidget.setXMLFile(widget);
									uiWidget.init();
									panel = uiWidget.getMainPanel();
								} else {
									logger.warn(className, function, "created UIPanelGeneric widget[{}] IS NULL", widget);
								}
							} else if ( TypeAttribute.configuration.equalsName(type) ) {
								
								//configuration
								uiWidgetGeneric.put(widget, new UIWidgetGeneric());
								UIWidget_i uiWidget = uiWidgetGeneric.get(widget);
								if ( null != uiWidget ) {
									uiWidget.setUINameCard(this.uiNameCard);
									uiWidget.setXMLFile(widget);
									uiWidget.init();
									panel = uiWidget.getMainPanel();
								} else {
									logger.warn(className, function, "created UIWidgetGeneric widget[{}] IS NULL", widget);
								}
							} else {
								logger.info(className, function, "type IS INVALID");
							}
							
							if ( null != panel ) {
								if ( PanelAttribute.DockLayoutPanel.equalsName(strPanel) ) {
									
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
								} else if ( PanelAttribute.AbsolutePanel.equalsName(strPanel) ) {
								
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
										Element container = DOM.getParent(panel.getElement());
										container.setClassName(csscontainer);
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
			String xmlFile				= (String)dictionary.getAttribute(DictionaryCacheInterface.XmlFile);
			String XmlTag				= (String)dictionary.getAttribute(DictionaryCacheInterface.XmlTag);
			String CreateDateTimeLabel	= (String)dictionary.getAttribute(DictionaryCacheInterface.CreateDateTimeLabel);
			
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
		return uiWidgetGeneric.get(widget);
	}

}
