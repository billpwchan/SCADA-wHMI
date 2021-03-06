package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.DirectionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.PanelAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.RootAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.TypeAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.WidgetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;

public class UILayoutGeneric extends UIGeneric {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Map<String, UIWidget_i> uiGeneric = new HashMap<String, UIWidget_i>();
	
	private Map<Integer, Map<String, String>> values = new HashMap<Integer, Map<String, String>>();
	
	private int rows;
	private int cols;
	private int totals;
	
	private String strPanel;
	private String strCSS;

	private Dictionary dictionaryHeader = null;
	private Dictionary dictionaryOption = null;
	
	@Override
	public void terminate() {
		super.terminate();
		for (String key: uiGeneric.keySet() ) {
			UIWidget_i uiwidget = uiGeneric.get(key);
			if ( null != uiwidget ) {
				uiwidget.terminate();
			}
		}
	}
	
	public UIWidget_i getUIWidget(String element) {
		return uiGeneric.get(element);
	}
	
	public String [] getUIWidgetElements() {
		final String function = "getUIWidgetElements";
		logger.begin(function);
		String [] elements = null;
		Set<String> elementSet = uiGeneric.keySet();
		if ( null != elementSet ) {
			elements = elementSet.toArray(new String[0]);
		} else {	
			logger.warn(function, "elementSet IS NULL");
		}
		logger.end(function);
		return elements;
	}
	
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
	};
	
	@Override
	public void init() {
		final String function = "init";
		logger.begin(function);
		logger.debug(function, "viewXMLFile[{}]", this.viewXMLFile);

		DictionariesCache dictionariesCache = DictionariesCache.getInstance("UIWidgetGeneric");
			
		this.dictionaryHeader = dictionariesCache.getDictionary( this.viewXMLFile, DictionaryCacheInterface.Header );
		this.dictionaryOption = dictionariesCache.getDictionary( this.viewXMLFile, DictionaryCacheInterface.Option );
		
		ready(this.dictionaryHeader);
		ready(this.dictionaryOption);
		
		logger.debug(function, "strPanel[{}] strCSS[{}]", strPanel, strCSS);
		
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
			} else if ( PanelAttribute.FlowPanel.equalsName(strPanel) ) {
				rootPanel = new FlowPanel();
			}

		} else {
			logger.warn(function, "strPanel IS NULL");
		}

		if ( null != rootPanel ) {
			
			if ( null != strCSS ) {
				rootPanel.addStyleName(strCSS);
			} else {
				logger.warn(function, "strCSS IS NULL");
			}
			
		    for ( int i = 0 ; i < rows ; ++i ) {
		    	
		    	logger.trace(function, "Build Filter Table Loop i[{}] Begin", i);
				
				for ( int j = 0 ; j < cols ; ++j ) {
					
					int index = (i*cols)+j;
					
					logger.trace(function, "Build Filter Table Loop i[{}] j[{}] => index[{}]", new Object[]{i, j, index});
					
					Map<String, String> valueMap = this.values.get(index);
					
					if ( null != valueMap ) {
						
						String type					= valueMap.get(WidgetAttribute.type.toString());
						String uiCtrl 				= valueMap.get(WidgetAttribute.uiCtrl.toString());
						String uiview				= valueMap.get(WidgetAttribute.uiView.toString());
						String uiopts				= valueMap.get(WidgetAttribute.uiOpts.toString());
						String uidict				= valueMap.get(WidgetAttribute.uiDict.toString());
						String element				= valueMap.get(WidgetAttribute.element.toString());
						
						String direction			= valueMap.get(WidgetAttribute.direction.toString());
						String size					= valueMap.get(WidgetAttribute.width.toString());
						String cellwidth			= valueMap.get(WidgetAttribute.cellwidth.toString());
						String cellheight			= valueMap.get(WidgetAttribute.cellheight.toString());
						String left					= valueMap.get(WidgetAttribute.left.toString());
						String top					= valueMap.get(WidgetAttribute.top.toString());
						String csscontainer			= valueMap.get(WidgetAttribute.csscontainer.toString());
						
						String debugId				= valueMap.get(WidgetAttribute.debugId.toString());

						Map<String, Object> options = new HashMap<String, Object>();
						
						for ( String key : valueMap.keySet() ) {
							Object value = valueMap.get(key);
							if ( null != value ) options.put(key, value);
						}
						
						if ( logger.isTraceEnabled() ) {
							for ( String key : valueMap.keySet() ) {
								String value = valueMap.get(key);
								logger.trace(function, "valueMap key[{}] value[{}]", key, value);
							}
						}
						
						if ( null != uiCtrl ) {

							Panel panel = null;
							
							UIWidget_i uiWidget = null;
							
							// Not recommend to use, prefer using UILayoutConfiguration
							if ( TypeAttribute.layoutconfiguration.equalsName(type) ) {
								
								uiWidget = new UILayoutGeneric();
								uiWidget.setElement(element);
								uiWidget.setUINameCard(this.uiNameCard);
								uiWidget.setViewXMLFile(uiview);
								uiWidget.setOptsXMLFile(uiopts);
								uiWidget.init();
								panel = uiWidget.getMainPanel();
									
								// Not recommend to use, prefer using UIWidgetConfiguration
							} else if ( TypeAttribute.widgetconfiguration.equalsName(type) ) {
									
								uiWidget = new UIWidgetGeneric();
								uiWidget.setElement(element);
								uiWidget.setUINameCard(this.uiNameCard);
								uiWidget.setViewXMLFile(uiview);
								uiWidget.setOptsXMLFile(uiopts);
								uiWidget.init();
								panel = uiWidget.getMainPanel();
									
							} else {
								
								UIWidgetMgr uiPredefinePanelMgr = UIWidgetMgr.getInstance();
								uiWidget = uiPredefinePanelMgr.getUIWidget(uiCtrl, uiview, uiNameCard, uiopts, element, uidict, options);
								if ( null != uiWidget ) {
									uiWidget.setElement(element);
									uiWidget.setUINameCard(this.uiNameCard);
									panel = uiWidget.getMainPanel();
								} else {
									logger.warn(function, "created UIPredefinePanelMgr uiCtrl[{}] IS NULL", uiCtrl);
								}
								
							} 

							if ( null != uiWidget ) {
								if ( null != element ) {
									if ( null != element && element.trim().length() > 0 ) {
										uiGeneric.put(element, uiWidget);
										logger.trace(function, "uiGeneric element[{}] ADDED", element);
									}
								}
								if ( ! uiGeneric.containsKey(element) ) {
									uiGeneric.put(uiCtrl, uiWidget);
									logger.trace(function, "uiGeneric uiCtrl[{}] ADDED", uiCtrl);									
								}
							} else {
								logger.warn(function, "created UIWidgetGeneric type[{}] uiCtrl[{}] IS NULL", type, uiCtrl);
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
												logger.warn(function, "size IS INVALID");
												logger.warn(function, "e[{}]", e.toString());
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
											logger.warn(function, "direction IS INVALID");
										}
									} else {
										logger.debug(function, "direction IS null");
									}
								} else if ( rootPanel instanceof AbsolutePanel ) {
								
									//AbsolutePanel
									int x = -1;
									int y = -1;
									try {
										if ( null != left )	x = Integer.parseInt(left);
										if ( null != top )	y = Integer.parseInt(top);
									} catch ( NumberFormatException e ) {
										logger.warn(function, "left or top IS INVALID");
									}
									((AbsolutePanel)rootPanel).add(panel, x, y);
								
								} else {
									rootPanel.add(panel);
									
									if ( null != cellwidth ) 	((CellPanel) rootPanel).setCellWidth(panel, cellwidth);
									if ( null != cellheight )	((CellPanel) rootPanel).setCellHeight(panel, cellwidth);
								}
								
								logger.trace(function, "csscontainer["+csscontainer+"]");
								if ( null != panel ) {
									if ( null != csscontainer ) {
										DOM.getParent(panel.getElement()).setClassName(csscontainer);
									}
								}
								
								logger.trace(function, "debugId["+debugId+"]");
								if ( null != panel ) {
									if ( null == debugId ) {
										panel.ensureDebugId(this.uiNameCard.getUiPath()+this.uiNameCard.getUiScreen());
									} else {
										panel.ensureDebugId(debugId);
									}
								}
															
							} else {
								logger.warn(function, "complexPanel IS NULL");
							}
						} else {
							logger.warn(function, "config IS NULL");
						}					
					}
				}
		    }
		} else {
			logger.warn(function, "Panel IS NULL");
		}
		
		logger.end(function);
	}

	public void ready(Dictionary dictionary) {
		final String function = "ready";
		
		logger.begin(function);
		logger.debug(function, "this.xmlFile[{}]", this.viewXMLFile);
		
		if ( null != dictionary ) {
			String xmlFile				= (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.FileName.toString());
			String XmlTag				= (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.Tag.toString());
			String CreateDateTimeLabel	= (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.DateTime.toString());
			
			logger.debug(function, "dictionary XmlFile[{}] XmlTag[{}] CreateDateTimeLabel[{}]", new Object[]{xmlFile, XmlTag, CreateDateTimeLabel});			
			
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
				
				logger.debug(function, "dictionary cols[{}] rows[{}] => totals[{}]", new Object[]{cols, rows, totals});
				
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
										
										logger.trace(function, "dictionary key[{}]", key);
										
										break;
									}
									
								}
								// Get Header End
							}
						}
						
						logger.trace(function, "dictionary key[{}]", key);
						
						if ( null != keys ) {
							if ( 2 == keys.length ) {
								boolean isvalid = false;
								try {
									row = Integer.parseInt(keys[0]);
									col = Integer.parseInt(keys[1]);
									
									isvalid=true;
								} catch ( NumberFormatException e ) {
									logger.warn(function, "NumberFormatException e[{}]", e);
								}
								
								if ( isvalid ) {
									logger.trace(function, "dictionary row[{}] col[{}]", new Object[]{row, col});
									
									index = (row * cols) + col;
									
									logger.debug(function, "dictionary row[{}] col[{}] => index[{}]", new Object[]{row, col, index});
									
									Map<String, String> hashMap = this.values.get(Integer.valueOf(index));
									if ( null != hashMap ) {
										for ( Object o2 : d2.getValueKeys() ) {
											if ( null != o2 ) {
												String k = (String)o2;
												String v = (String)d2.getValue(o2);
												
												logger.trace(function, "dictionary k[{}] v[{}]", new Object[]{k, v});
				
												hashMap.put(k, v);
											}
										}
									} else {
										logger.warn(function, "row[{}] col[{}] => index[{}] Index NOT EXISTS", new Object[]{row, col, index});
									}
								} else {
									logger.warn(function, "keys[0][{}] OR keys[1][{}] is not a number", new Object[]{keys[0], keys[1]});
								}
							}
						} else {
							logger.warn(function, "key IS NULL");
						}
					}
				}
			}

		} else {
			logger.warn(function, "this.xmlFile[{}] dictionary IS NULL", this.viewXMLFile);
		}
		logger.end(function);
	}

	public UIWidget_i getPredefineWidget(String uiCtrl) {
		logger.debug("getPredefineWidget", "uiCtrl[{}]", uiCtrl);
		return uiGeneric.get(uiCtrl);
	}

}
