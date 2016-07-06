package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryCache;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIPanelGeneric_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;

public class UILayoutGeneric implements UIWidget_i {
	
	private static Logger logger = Logger.getLogger(UILayoutGeneric.class.getName());
	
	private String xmlFile = null;

	private HashMap<String, UIWidget_i> uiWidgetGeneric = new HashMap<String, UIWidget_i>();
	
	private HashMap<Integer, HashMap<String, String>> values = new HashMap<Integer, HashMap<String, String>>();
	
	private int rows;
	private int cols;
	private int totals;
	
	private String strOuterPanel;
	private String strOuterCSS;
	
	private String strInnerPanel;
	private String strInnerCSS;
	
	private String outerHorizontalAlignment;
	private String outerVerticalAlignment;
		
	private String innerHorizontalAlignment;
	private String innerVerticalAlignment;
	
	private Dictionary dictionaryHeader = null;
	private Dictionary dictionaryOption = null;
	
	public UIWidget_i getUIWidget(String xmlFile) {
		return uiWidgetGeneric.get(xmlFile);
	}
	
    private HashMap<String, String> paramaters = new HashMap<String, String>();
    @Override
    public void setParameter(String key, String value) {
    	paramaters.put(key, value);
    }
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		if ( null != uiNameCard ) {
			this.uiNameCard = new UINameCard(uiNameCard);
			this.uiNameCard.appendUIPanel(this);
		}
	}
	
	private ComplexPanel outerPanel = null;
	public void init(String xmlFile) {
		this.xmlFile = xmlFile;
		
		logger.log(Level.SEVERE, "init xmlFile["+this.xmlFile+"]");
		
		DictionaryCache uiPanelSettingCache = DictionaryCache.getInstance("UIWidgetGeneric");
		
		this.dictionaryHeader = uiPanelSettingCache.getDictionary( this.xmlFile, DictionaryCacheInterface.Header );
		this.dictionaryOption = uiPanelSettingCache.getDictionary( this.xmlFile, DictionaryCacheInterface.Option );
		
		ready(this.dictionaryHeader);
		ready(this.dictionaryOption);
		
		
		// Start the UIGeneric
		logger.log(Level.FINE, "getMainPanel Begin");
		logger.log(Level.SEVERE, "getMainPanel xmlFile["+this.xmlFile+"]");
		
		
		
		logger.log(Level.SEVERE, "getMainPanel strOuterPanel["+strOuterPanel+"] strOuterCSS["+strOuterCSS+"]");
		logger.log(Level.SEVERE, "getMainPanel strInnerPanel["+strInnerPanel+"] strInnerCSS["+strInnerCSS+"]");
		logger.log(Level.SEVERE, "getMainPanel innerHorizontalAlignment["+innerHorizontalAlignment+"] innerVerticalAlignment["+innerVerticalAlignment+"]");
		logger.log(Level.SEVERE, "getMainPanel outerHorizontalAlignment["+outerHorizontalAlignment+"] outerVerticalAlignment["+outerVerticalAlignment+"]");
		
		outerPanel = null;
		if ( null != strOuterPanel ) {
			if ( UIPanelGeneric_i.PanelAttribute.VerticalPanel.equalsName(strOuterPanel) ) {
				outerPanel = new VerticalPanel();
			} else if ( UIPanelGeneric_i.PanelAttribute.HorizontalPanel.equalsName(strOuterPanel) ) {
				outerPanel = new HorizontalPanel();
			} else if ( UIPanelGeneric_i.PanelAttribute.DockLayoutPanel.equalsName(strOuterPanel) ) {
				outerPanel = new DockLayoutPanel(Unit.PX);
				Element e = outerPanel.getElement();
				DOM.setStyleAttribute(e, "position", "absolute");
			} else if ( UIPanelGeneric_i.PanelAttribute.AbsolutePanel.equalsName(strOuterPanel) ) {
				outerPanel = new AbsolutePanel();
				Element e = outerPanel.getElement();
				DOM.setStyleAttribute(e, "position", "absolute");
			} else {
				logger.log(Level.SEVERE, "getMainPanel strOuterPanel["+strOuterPanel+"] IS INVALID");
			}

			if ( null != outerPanel ) {
				if ( null != strOuterCSS ) {
					outerPanel.addStyleName(strOuterCSS);
				} else {
					logger.log(Level.SEVERE, "getMainPanel outerPanel IS NULL");
				}
				
				if ( UIPanelGeneric_i.PanelAttribute.DockLayoutPanel.equalsName(strOuterPanel) ) {
					
				} else if ( UIPanelGeneric_i.PanelAttribute.AbsolutePanel.equalsName(strOuterPanel) ) {
					
				} else {
				
					if ( null != outerHorizontalAlignment && null != outerVerticalAlignment ) {
					
						if ( UIPanelGeneric_i.PanelAttribute.HorizontalPanel.equalsName(strOuterPanel) ) {
							
							if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_LEFT.equalsName(outerHorizontalAlignment) ) {
								((HorizontalPanel)outerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
							} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_CENTER.equalsName(outerHorizontalAlignment) ) {
								((HorizontalPanel)outerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
							} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_RIGHT.equalsName(outerHorizontalAlignment) ) {
								((HorizontalPanel)outerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
							}
							
							if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_TOP.equalsName(outerVerticalAlignment) ) {
								((HorizontalPanel)outerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
							} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_MIDDLE.equalsName(outerVerticalAlignment) ) {
								((HorizontalPanel)outerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
							} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_BOTTOM.equalsName(outerVerticalAlignment) ) {	
								((HorizontalPanel)outerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
							}
						} else if ( UIPanelGeneric_i.PanelAttribute.VerticalPanel.equalsName(strOuterPanel) ) {
							
							if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_LEFT.equalsName(outerHorizontalAlignment) ) {
								((VerticalPanel)outerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
							} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_CENTER.equalsName(outerHorizontalAlignment) ) {
								((VerticalPanel)outerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
							} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_RIGHT.equalsName(outerHorizontalAlignment) ) {
								((VerticalPanel)outerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
							}
							
							if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_TOP.equalsName(outerVerticalAlignment) ) {
								((VerticalPanel)outerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
							} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_MIDDLE.equalsName(outerVerticalAlignment) ) {
								((VerticalPanel)outerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
							} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_BOTTOM.equalsName(outerVerticalAlignment) ) {	
								((VerticalPanel)outerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
							}
						} else {
							
						}
					
					} else {
						logger.log(Level.SEVERE, "getMainPanel outerHorizontalAlignment IS NULL or outerVerticalAlignment IS NULL or");
					}
				}
				
			}
		} else {
			logger.log(Level.SEVERE, "getMainPanel strOuterPanel IS NULL");
		}
		
		ComplexPanel innerPanel = null;
		if ( null != strInnerPanel ) {
			if ( UIPanelGeneric_i.PanelAttribute.HorizontalPanel.equalsName(strInnerPanel) ) {
				innerPanel = new HorizontalPanel();
			} else if ( UIPanelGeneric_i.PanelAttribute.VerticalPanel.equalsName(strInnerPanel) ) {
				innerPanel = new VerticalPanel();
			} else if ( UIPanelGeneric_i.PanelAttribute.DockLayoutPanel.equalsName(strInnerPanel) ) {
				innerPanel = new DockLayoutPanel(Unit.PX);
			} else if ( UIPanelGeneric_i.PanelAttribute.AbsolutePanel.equalsName(strInnerPanel) ) {
				innerPanel = new AbsolutePanel();
				Element e = innerPanel.getElement();
				DOM.setStyleAttribute(e, "position", "absolute");
			} else {
			}
			
			if ( null != innerPanel ) {
				
				if ( null != strInnerCSS ) {
					innerPanel.addStyleName(strInnerCSS);
				} else {
					logger.log(Level.SEVERE, "getMainPanel strInnerCSS IS NULL");
				}
				
				if ( UIPanelGeneric_i.PanelAttribute.DockLayoutPanel.equalsName(strInnerPanel) ) {
					
				} else if ( UIPanelGeneric_i.PanelAttribute.AbsolutePanel.equalsName(strInnerPanel) ) {
					
				} else {
				
					if ( null != innerHorizontalAlignment && null != innerVerticalAlignment ) {
						
						if ( UIPanelGeneric_i.PanelAttribute.HorizontalPanel.equalsName(strInnerPanel) ) {
							
							if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_LEFT.equalsName(innerHorizontalAlignment) ) {
								((HorizontalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
							} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_CENTER.equalsName(innerHorizontalAlignment) ) {
								((HorizontalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
							} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_RIGHT.equalsName(innerHorizontalAlignment) ) {
								((HorizontalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
							}
							
							if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_TOP.equalsName(innerVerticalAlignment) ) {
								((HorizontalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
							} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_MIDDLE.equalsName(innerVerticalAlignment) ) {
								((HorizontalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
							} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_BOTTOM.equalsName(innerVerticalAlignment) ) {	
								((HorizontalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
							}
							
						} else if ( UIPanelGeneric_i.PanelAttribute.VerticalPanel.equalsName(strInnerPanel) ) {
							
							if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_LEFT.equalsName(innerHorizontalAlignment) ) {
								((VerticalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
							} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_CENTER.equalsName(innerHorizontalAlignment) ) {
								((VerticalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
							} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_RIGHT.equalsName(innerHorizontalAlignment) ) {
								((VerticalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
							}
							
							if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_TOP.equalsName(innerVerticalAlignment) ) {
								((VerticalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
							} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_MIDDLE.equalsName(innerVerticalAlignment) ) {
								((VerticalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
							} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_BOTTOM.equalsName(innerVerticalAlignment) ) {	
								((VerticalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
							}
						}
					} else {
						logger.log(Level.SEVERE, "getMainPanel outerHorizontalAlignment IS NULL or outerVerticalAlignment IS NULL or");
					}
				}
			} else {
				logger.log(Level.SEVERE, "getMainPanel innerPanel IS NULL");
			}
		} else {
			logger.log(Level.SEVERE, "getMainPanel strInnerPanel IS NULL");
		}

		if ( null != innerPanel ) {
			
			outerPanel.add(innerPanel);
			
		    for ( int i = 0 ; i < rows ; ++i ) {
		    	
				logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] Begin");
				
				for ( int j = 0 ; j < cols ; ++j ) {
					
					int index = (i*cols)+j;
					
					logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] => index["+index+"]");
					
					HashMap<String, String> valueMap = this.values.get(index);
					
					if ( null != valueMap ) {
						String type					= valueMap.get(UIPanelGeneric_i.WidgetAttribute.type.toString());
						String widget 				= valueMap.get(UIPanelGeneric_i.WidgetAttribute.widget.toString());
						String horizontalalignment 	= valueMap.get(UIPanelGeneric_i.WidgetAttribute.horizontalalignment.toString());
						String verticalalignment	= valueMap.get(UIPanelGeneric_i.WidgetAttribute.verticalalignment.toString());
						String direction			= valueMap.get(UIPanelGeneric_i.WidgetAttribute.direction.toString());
						String size					= valueMap.get(UIPanelGeneric_i.WidgetAttribute.width.toString());
						String cellwidth			= valueMap.get(UIPanelGeneric_i.WidgetAttribute.cellwidth.toString());
						String cellheight			= valueMap.get(UIPanelGeneric_i.WidgetAttribute.cellheight.toString());
						String left					= valueMap.get(UIPanelGeneric_i.WidgetAttribute.left.toString());
						String top					= valueMap.get(UIPanelGeneric_i.WidgetAttribute.top.toString());
						
						logger.log(Level.SEVERE, "getMainPanel type["+type+"] widget["+widget+"]");
						logger.log(Level.SEVERE, "getMainPanel horizontalalignment["+horizontalalignment+"] verticalalignment["+verticalalignment+"]");
						logger.log(Level.SEVERE, "getMainPanel direction["+direction+"] size["+size+"]");
						
						if ( null != widget ) {
							
							if ( UIPanelGeneric_i.PanelAttribute.DockLayoutPanel.equalsName(strInnerPanel) ) {
								
							} else if ( UIPanelGeneric_i.PanelAttribute.AbsolutePanel.equalsName(strInnerPanel) ) {
								
							} else {
								if ( null != horizontalalignment && null != verticalalignment ) {
									
									if ( UIPanelGeneric_i.PanelAttribute.HorizontalPanel.equalsName(strInnerPanel) ) {
									
										if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_LEFT.equalsName(horizontalalignment) ) {
											((HorizontalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
										} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_CENTER.equalsName(horizontalalignment) ) {
											((HorizontalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
										} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_RIGHT.equalsName(horizontalalignment) ) {
											((HorizontalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
										}
										
										if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_TOP.equalsName(verticalalignment) ) {
											((HorizontalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
										} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_MIDDLE.equalsName(verticalalignment) ) {
											((HorizontalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
										} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_BOTTOM.equalsName(verticalalignment) ) {
											((HorizontalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
										}
										
									} else if ( UIPanelGeneric_i.PanelAttribute.VerticalPanel.equalsName(strInnerPanel) ) {
										
										if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_LEFT.equalsName(horizontalalignment) ) {
											((VerticalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
										} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_CENTER.equalsName(horizontalalignment) ) {
											((VerticalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
										} else if ( UIPanelGeneric_i.HorizontalAlignmentAttribute.ALIGN_RIGHT.equalsName(horizontalalignment) ) {
											((VerticalPanel)innerPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
										}
										
										if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_TOP.equalsName(verticalalignment) ) {
											((VerticalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
										} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_MIDDLE.equalsName(verticalalignment) ) {
											((VerticalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
										} else if ( UIPanelGeneric_i.VerticalAlignmentAttribute.ALIGN_BOTTOM.equalsName(verticalalignment) ) {
											((VerticalPanel)innerPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
										}
									}
									
								} else {
									logger.log(Level.SEVERE, "getMainPanel horizontalalignment IS NULL or verticalalignment IS NULL");
								}								
							}
							
							ComplexPanel complexPanel = null;
							
							if ( UIPanelGeneric_i.TypeAttribute.predefine.equalsName(type) ) {
								
								// predefine
								UIWidgetMgr uiPredefinePanelMgr = UIWidgetMgr.getInstance();
								uiWidgetGeneric.put(widget, uiPredefinePanelMgr.getUIWidget(widget));
								UIWidget_i uiWIdget = uiWidgetGeneric.get(widget);
								if ( null != uiWIdget ) {
									uiWIdget.setUINameCard(this.uiNameCard);
									complexPanel = uiWIdget.getMainPanel();
								} else {
									logger.log(Level.SEVERE, "getMainPanel created UIPredefinePanelMgr widget["+widget+"] IS NULL");
								}
							} else if ( UIPanelGeneric_i.TypeAttribute.layoutconfiguration.equalsName(type) ) {
								
								// layoutconfiguration
								uiWidgetGeneric.put(widget, new UILayoutGeneric());
								UIWidget_i uiWIdget = uiWidgetGeneric.get(widget);
								if ( null != uiWIdget ) {
									uiWIdget.setUINameCard(this.uiNameCard);
									uiWIdget.init(widget);
									complexPanel = uiWIdget.getMainPanel();
								} else {
									logger.log(Level.SEVERE, "getMainPanel created UIPanelGeneric widget["+widget+"] IS NULL");
								}
							} else if ( UIPanelGeneric_i.TypeAttribute.configuration.equalsName(type) ) {
								
								//configuration
								uiWidgetGeneric.put(widget, new UIWidgetGeneric());
								UIWidget_i uiWIdget = uiWidgetGeneric.get(widget);
								if ( null != uiWIdget ) {
									uiWIdget.setUINameCard(this.uiNameCard);
									uiWIdget.init(widget);
									complexPanel = uiWIdget.getMainPanel();
								} else {
									logger.log(Level.SEVERE, "getMainPanel created UIWidgetGeneric widget["+widget+"] IS NULL");
								}
							} else {
								logger.log(Level.SEVERE, "getMainPanel type IS INVALID");
							}
							
							if ( null != complexPanel ) {
								if ( UIPanelGeneric_i.PanelAttribute.DockLayoutPanel.equalsName(strInnerPanel) ) {
									
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
											((DockLayoutPanel)innerPanel).addNorth(complexPanel, width);
										} else if ( UIPanelGeneric_i.DirectionAttribute.East.equalsName(direction) ) {
											((DockLayoutPanel)innerPanel).addEast(complexPanel, width);
										} else if ( UIPanelGeneric_i.DirectionAttribute.South.equalsName(direction) ) {
											((DockLayoutPanel)innerPanel).addSouth(complexPanel, width);
										} else if ( UIPanelGeneric_i.DirectionAttribute.West.equalsName(direction) ) {
											((DockLayoutPanel)innerPanel).addWest(complexPanel, width);
										} else if ( UIPanelGeneric_i.DirectionAttribute.Center.equalsName(direction) ) {
											((DockLayoutPanel)innerPanel).add(complexPanel);
										} else {
											logger.log(Level.SEVERE, "getMainPanel direction IS INVALID");
										}
									} else {
										logger.log(Level.SEVERE, "getMainPanel direction IS null");
									}
								} else if ( UIPanelGeneric_i.PanelAttribute.AbsolutePanel.equalsName(strInnerPanel) ) {
								
									//AbsolutePanel
									int x = -1;
									int y = -1;
									try {
										if ( null != left )	x = Integer.parseInt(left);
										if ( null != top )	y = Integer.parseInt(top);
									} catch ( NumberFormatException e ) {
										logger.log(Level.SEVERE, "getMainPanel left or top IS INVALID");
									}
									((AbsolutePanel)innerPanel).add(complexPanel, x, y);
								
								} else {
									innerPanel.add(complexPanel);
									
									if ( null != cellwidth ) 	((CellPanel) innerPanel).setCellWidth(complexPanel, cellwidth);
									if ( null != cellheight )	((CellPanel) innerPanel).setCellHeight(complexPanel, cellwidth);
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
			logger.log(Level.SEVERE, "getMainPanel innerPanel IS NULL");
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
								} else if ( UIPanelGeneric_i.RootAttribute.innerPanel.equalsName(k) ) {
									strInnerPanel = v;
								} else if ( UIPanelGeneric_i.RootAttribute.innerCSS.equalsName(k) ) {
									strInnerCSS = v;
								} else if ( UIPanelGeneric_i.RootAttribute.outerPanel.equalsName(k)  ) {
									strOuterPanel = v;
								} else if ( UIPanelGeneric_i.RootAttribute.outerCSS.equalsName(k)  ) {
									strOuterCSS = v;
								} else if ( UIPanelGeneric_i.RootAttribute.outerhorizontalalignment.equalsName(k)  ) {
									outerHorizontalAlignment = v;
								} else if ( UIPanelGeneric_i.RootAttribute.outerverticalalignment.equalsName(k)  ) {
									outerVerticalAlignment = v;
								} else if ( UIPanelGeneric_i.RootAttribute.innerhorizontalalignment.equalsName(k)  ) {
									innerHorizontalAlignment = v;
								} else if ( UIPanelGeneric_i.RootAttribute.innerverticalalignment.equalsName(k)  ) {
									innerVerticalAlignment = v;
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
	
	public ComplexPanel getMainPanel(){
		return outerPanel;
	}
	
	public UIWidget_i getPredefineWidget(String widget) {
		return uiWidgetGeneric.get(widget);
	}

	@Override
	public Widget getWidget(String widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWidgetElement(Widget widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getWidgetStatus(String element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWidgetStatus(String element,
			String up) {
		// TODO Auto-generated method stub
		
	}



}
