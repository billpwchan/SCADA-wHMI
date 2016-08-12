package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryCache;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.RootAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.RootWidgetType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetMedia;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;

public class UIWidgetGeneric extends UIWidget_i {
	
	private static Logger logger = Logger.getLogger(UIWidgetGeneric.class.getName());
	
	private static final String basePath = GWT.getModuleBaseURL();
	
	private int rows	= 0;
	private int cols	= 0;
	private int totals	= 0;
	
    private String strCSSStatPanel;
    private String strRootWidget;

    private String strCSSFlexTable;
    
    private HashMap<Integer, HashMap<String, String>> values = new HashMap<Integer, HashMap<String, String>>();
    
    private HashMap<Integer, Widget> widgets = new HashMap<Integer, Widget>();

    public String [] getElementValues(WidgetAttribute element) {
    	String elements [] = new String[this.values.size()];
		for( int i=0 ; i < this.values.size(); ++i ) {
			HashMap<String, String> valueMap = this.values.get(i);
			if ( null != valueMap ) {
				elements[i] = valueMap.get(element);
			} else {
				elements[i] = "";
			}
		}
		for( int i=0 ; i < values.size(); ++i ) {
			logger.log(Level.SEVERE, " **** getValues name["+i+"]["+elements[i]+"]");
		}
    	return elements;
    }

    private Dictionary dictionaryHeader = null;
    private Dictionary dictionaryOption = null;

	@Override
    public void init() {
    	
    	logger.log(Level.SEVERE, "init this.xmlFile["+this.xmlFile+"]");
    	
		DictionaryCache uiPanelSettingCache = DictionaryCache.getInstance("UIWidgetGeneric");
		
		this.dictionaryHeader = uiPanelSettingCache.getDictionary( this.xmlFile, DictionaryCacheInterface.Header );
		ready(this.dictionaryHeader);
		
		this.dictionaryOption = uiPanelSettingCache.getDictionary( this.xmlFile, DictionaryCacheInterface.Option );
		ready(this.dictionaryOption);
    	
		
		//UIGeneric
		
		
		logger.log(Level.SEVERE, "getMainPanel Begin");
		logger.log(Level.SEVERE, "getMainPanel this.xmlFile["+this.xmlFile+"]");
		
		rootPanel = new VerticalPanel();
		
		if ( null == dictionaryHeader || null == dictionaryOption ) {
			
			if ( null == dictionaryHeader ) rootPanel.add( new InlineLabel( "Faild to load xmlFile["+this.xmlFile+"] strHeader["+DictionaryCacheInterface.Header+"]" ));
			
			if ( null == dictionaryOption ) rootPanel.add( new InlineLabel( "Faild to load xmlFile["+this.xmlFile+"] strOption["+DictionaryCacheInterface.Option+"]" ));
			
		} else {
			
			logger.log(Level.SEVERE, "getMainPanel appling root panel css strCSSStatPanel["+strCSSStatPanel+"]");
			
		    rootPanel.addStyleName(strCSSStatPanel);
		    ((VerticalPanel)rootPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		    ((VerticalPanel)rootPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

	    	FlexTable flexTable = null;
	    	
	    	HorizontalPanel horizontalPanel = null;
	    	
	    	VerticalPanel verticalPanel = null;
	    	
	    	AbsolutePanel absolutePanel = null;
	    	
	    	logger.log(Level.SEVERE, "getMainPanel strRootWidget["+strRootWidget+"]");
	    	
	    	if ( RootWidgetType.HorizontalPanel.equalsName(strRootWidget) ) {
	    		
	    		logger.log(Level.SEVERE, "getMainPanel appling sub panel is ["+RootWidgetType.HorizontalPanel+"]");
	    		
	    		horizontalPanel = new HorizontalPanel();
	    		if ( null != strCSSFlexTable )	horizontalPanel.addStyleName(strCSSFlexTable);
	    		
	    	} else if ( RootWidgetType.FlexTable.equalsName(strRootWidget) ) {
	    		
	    		logger.log(Level.SEVERE, "getMainPanel appling sub panel is ["+RootWidgetType.FlexTable+"]");
	    	
	    		flexTable = new FlexTable();
	    		if ( null != strCSSFlexTable )	flexTable.addStyleName(strCSSFlexTable);
	    		
	    	} else if ( RootWidgetType.VerticalPanel.equalsName(strRootWidget) ) {
	    		
	    		logger.log(Level.SEVERE, "getMainPanel appling sub panel is ["+RootWidgetType.VerticalPanel+"]");
	    	
	    		verticalPanel = new VerticalPanel();
	    		if ( null != strCSSFlexTable )	verticalPanel.addStyleName(strCSSFlexTable);
	    		
	    	} else if ( RootWidgetType.AbsolutePanel.equalsName(strRootWidget) ) {
	    		
	    		logger.log(Level.SEVERE, "getMainPanel appling sub panel is ["+RootWidgetType.VerticalPanel+"]");
	    	
	    		absolutePanel = new AbsolutePanel();
	    		if ( null != strCSSFlexTable )	absolutePanel.addStyleName(strCSSFlexTable);
	    	}
			
		    for ( int i = 0 ; i < rows ; ++i ) {
		    	
				logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] Begin");
				
				for ( int j = 0 ; j < cols ; ++j ) {
					
					int index = (i*cols)+j;
					
					logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] => index["+index+"]");
					
					if ( index < values.size() ) {
						
						HashMap<String, String> valueMap = this.values.get(index);
						
						if ( null != valueMap ) {
							
							Widget w = null;
							
							String widget			= valueMap.get(WidgetAttribute.widget.toString());
							String label			= valueMap.get(WidgetAttribute.label.toString());
							String tooltip			= valueMap.get(WidgetAttribute.tooltip.toString());
							String css				= valueMap.get(WidgetAttribute.css.toString());
							String readonly			= valueMap.get(WidgetAttribute.readonly.toString());
							String maxlength		= valueMap.get(WidgetAttribute.maxlength.toString());
							String visibleitemcount	= valueMap.get(WidgetAttribute.visibleitemcount.toString());
							
//							String labelDown		= valueMap.get(WidgetAttribute.labelDown.toString());
//							String labelDisable		= valueMap.get(WidgetAttribute.labelDisable.toString());
							
							String icon				= valueMap.get(WidgetAttribute.icon.toString());
//							String iconDown			= valueMap.get(WidgetAttribute.iconDown.toString());
//							String iconDisable		= valueMap.get(WidgetAttribute.iconDisable.toString());
							
							String iconDivWidth		= valueMap.get(WidgetAttribute.iconDivWidth.toString());
							String iconDivHeight	= valueMap.get(WidgetAttribute.iconDivHeight.toString());
							String iconImgWidth		= valueMap.get(WidgetAttribute.iconImgWidth.toString());
							String iconImgHeight	= valueMap.get(WidgetAttribute.iconImgHeight.toString());
							
//							String cssUp			= valueMap.get(WidgetAttribute.cssUp.toString());
//							String cssDown			= valueMap.get(WidgetAttribute.cssDown.toString());
//							String cssDisable		= valueMap.get(WidgetAttribute.cssDisable.toString());
							
							String left				= valueMap.get(WidgetAttribute.left.toString());
							String top				= valueMap.get(WidgetAttribute.top.toString());
							
//							String menuType			= valueMap.get(WidgetAttribute.menuType.toString());
//							String menuLevel		= valueMap.get(WidgetAttribute.menuLevel.toString());
							
							String groupName		= valueMap.get(WidgetAttribute.groupName.toString());
							
							logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] widget["+widget+"]");
							logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] label["+label+"]");
							logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] css["+css+"]");
							
							TranslationMgr translationMgr = TranslationMgr.getInstance();
							if ( null != translationMgr ) {
								label = translationMgr.getTranslation(label);
							} else {
								logger.log(Level.SEVERE, "getMainPanel getTranslation IS NULL");
							}
							
							if ( WidgetType.TextBox.equalsName(widget)
									|| null == widget || 0 == widget.length() ) {
								w = new TextBox();
								
								if ( null != label )	((TextBox)w).setText(label);
								if ( null != tooltip )	((TextBox)w).setTitle(tooltip);
								if ( null != css )		((TextBox)w).addStyleName(css);
								((TextBox)w).addKeyPressHandler(new KeyPressHandler() {
									@Override
									public void onKeyPress(KeyPressEvent event) {
										if ( null != uiWidgetEventOnKeyPressHandler ) {
											uiWidgetEventOnKeyPressHandler.onKeyPressHandler(event);
										}
									}
								});
								((TextBox)w).addValueChangeHandler(new ValueChangeHandler<String>() {
									
									@Override
									public void onValueChange(ValueChangeEvent<String> event) {
										if ( null != uiWidgetEventOnValueChangeHandler ) {
											uiWidgetEventOnValueChangeHandler.setUIWidgetEventOnValueChangeHandler(event);
										}
									}
								});
								
								if ( null != maxlength && maxlength.length() > 0 )
									((TextBox)w).setMaxLength(Integer.parseInt(maxlength));
								
								if ( null != readonly )
									((TextBox)w).setReadOnly(true);
								
								this.widgets.put(index, w);
							} else if ( WidgetType.PasswordTextBox.equalsName(widget) ) {
								
								w = new PasswordTextBox();
								
								if ( null != label )	((PasswordTextBox)w).setText(label);
								if ( null != css )		((PasswordTextBox)w).addStyleName(css);
								
								if ( WidgetAttribute.maxlength.equalsName(maxlength) )
									((PasswordTextBox)w).setMaxLength(Integer.parseInt(maxlength));
								
								if ( WidgetAttribute.readonly.equalsName(readonly) )
									((PasswordTextBox)w).setReadOnly(true);
								
								this.widgets.put(index, w);
							} else if ( WidgetType.InlineLabel.equalsName(widget) ) {
								
								w = new InlineLabel();
								
								if ( null != label )	((InlineLabel)w).setText(label);
								if ( null != tooltip )	((InlineLabel)w).setTitle(tooltip);
								if ( null != css )		((InlineLabel)w).addStyleName(css);

								this.widgets.put(index, w);
							} else if ( WidgetType.Button.equalsName(widget) ) {
								
								w = new Button();
								
								if ( null != label )	((Button)w).setText(label);
								if ( null != tooltip )	((Button)w).setTitle(tooltip);
								if ( null != css )		((Button)w).addStyleName(css);
								
								((Button)w).addClickHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										if ( null != uiWidgetEventOnClickHandler ) {
											uiWidgetEventOnClickHandler.onClickHandler(event);
										}
									}
								});

								this.widgets.put(index, w);
							} else if ( WidgetType.ImageButton.equalsName(widget) || WidgetType.ImageToggleButton.equalsName(widget) ) {
								
								w = new Button();
								
								if ( null != css )		((Button)w).addStyleName(css);

								if ( null != tooltip )	((Button)w).setTitle(tooltip);
								
								if ( null != iconDivWidth && null != iconDivHeight ) {
									String img = null;
									String lbl = null;
									if ( null != icon && null != iconImgWidth && null != iconImgHeight ) {
										img = "<img src=\""+basePath+icon+"\" width=\""+iconImgWidth+"\" height=\""+iconImgHeight+"\">";
									}
									if ( null != label ) {
										lbl = "<label>"+label+"</label>";
									}
									String html = "<div width=\""+iconDivWidth+"\" height=\""+iconDivHeight+"\"><center>"+(null==img?"":img)+(null==lbl?"":lbl)+"</center></div>";
									
									logger.log(Level.FINE, "getMainPanel html["+html+"]");
									
									((Button)w).setHTML(html);
								}
								
								((Button)w).addClickHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										if ( null != uiWidgetEventOnClickHandler ) {
											uiWidgetEventOnClickHandler.onClickHandler(event);
										}
									}
								});
								
								this.widgets.put(index, w);
							} else if ( WidgetType.Image.equalsName(widget) ) {
								w = new Image();
								if ( null != tooltip )	((Image)w).setTitle(tooltip);
								if ( null != label )	((Image)w).setUrl(basePath+label);
								if ( null != css )		((Image)w).addStyleName(css);
								
								this.widgets.put(index, w);
							} else if ( WidgetType.ListBox.equalsName(widget) ) {
								
								w = new ListBox();
								if ( null != label )	((ListBox)w).addItem(label);
								if ( null != css )		((ListBox)w).addStyleName(css);
								
								if ( WidgetAttribute.visibleitemcount.equalsName(visibleitemcount) ) {
									((ListBox)w).setVisibleItemCount(Integer.parseInt(visibleitemcount));
								}
								
								this.widgets.put(index, w);
							} else if ( WidgetType.HTML.equalsName(widget) ) {
								
								if ( null != label ) 	w = new HTML(label);
								
								if ( null != css )		w.addStyleName(css);									
						
								this.widgets.put(index, w);
							} else if ( WidgetType.RadioButton.equalsName(widget) ) {
								
								if ( null == groupName )
									logger.log(Level.SEVERE, "getMainPanel created widget["+widget+"] groupName IS NULL");
								
								if ( null != groupName ) 	w = new RadioButton(groupName);
								
								if ( null != label )		((RadioButton)w).setText(label);
								if ( null != tooltip )		((RadioButton)w).setTitle(tooltip);
								if ( null != css )			((RadioButton)w).addStyleName(css);
								
								((RadioButton)w).addClickHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										if ( null != uiWidgetEventOnClickHandler ) {
											uiWidgetEventOnClickHandler.onClickHandler(event);
										}
									}
								});
								
								this.widgets.put(index, w);
							} else if ( widget.startsWith(WidgetType.WidgetFactory.toString()) ) {
									
								UIWidgetMgr uiPredefinePanelMgr = UIWidgetMgr.getInstance();
								UIWidget_i uiWIdget = uiPredefinePanelMgr.getUIWidget(widget);

								if ( null != uiWIdget ) {
									uiWIdget.setUINameCard(this.uiNameCard);
									w = uiWIdget.getMainPanel();
								} else {
									logger.log(Level.SEVERE, "getMainPanel created UIPredefinePanelMgr widget["+widget+"] IS NULL");
								}

								if ( null != css )		w.addStyleName(css);
								
								this.widgets.put(index, w);
							} else {
								logger.log(Level.SEVERE, "getMainPanel widget["+widget+"] IS INVALID");
							}
							
							if ( null != w ) {
								if ( RootWidgetType.HorizontalPanel.equalsName(strRootWidget) ) {
									horizontalPanel.add(w);
								} else if ( RootWidgetType.VerticalPanel.equalsName(strRootWidget) ) {
									verticalPanel.add(w);
								} else if ( RootWidgetType.FlexTable.equalsName(strRootWidget) ) {	
									flexTable.setWidget(i, j, w);
								} else if ( RootWidgetType.AbsolutePanel.equalsName(strRootWidget) ) {
									int x = -1;
									int y = -1;
									try {
										if ( null != left )	x = Integer.parseInt(left);
										if ( null != top )	y = Integer.parseInt(top);
									} catch ( NumberFormatException e ) {
										logger.log(Level.SEVERE, "getMainPanel left or top IS INVALID");
									}
									absolutePanel.add(w, x, y);
								}
							} else {
								logger.log(Level.SEVERE, "getMainPanel w IS NULL");
							}
						} else {
							logger.log(Level.SEVERE, "getMainPanel index["+index+"], check index please, valueMap IS NULL");
						}

								

					} else {
						logger.log(Level.SEVERE, "getMainPanel Build Filter INVALID index["+index +"] > values.length["+values.size()+"]");
					}
				}
				logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] End");
		    }
		    
		    if ( RootWidgetType.FlexTable.equalsName(strRootWidget) ) {
		    	for ( int j = 0 ; j < cols ; ++j ) {
		    		flexTable.getColumnFormatter().setWidth(j, "40px");
		    	}
		    }
		    
		    if ( RootWidgetType.HorizontalPanel.equalsName(strRootWidget) ) {
		    	rootPanel.add(horizontalPanel);
		    } else if ( RootWidgetType.VerticalPanel.equalsName(strRootWidget) ) {
		    	rootPanel.add(verticalPanel);
		    } else if ( RootWidgetType.FlexTable.equalsName(strRootWidget) ) {
		    	rootPanel.add(flexTable);
			} else if ( RootWidgetType.AbsolutePanel.equalsName(strRootWidget) ) {
		    	rootPanel.add(absolutePanel);
			}
		
	    }
		
		logger.log(Level.FINE, "getMainPanel End");
		
    }
    
    public String getWidgetElement(Widget widget) {
    	
    	logger.log(Level.FINE, "getWidgetName Begin");
    	
    	String element = null;
    	Set<Integer> keys = this.widgets.keySet();
    	Iterator<Integer> iter = keys.iterator();
    	while ( iter.hasNext() ) {
    		Integer key = iter.next();
    		if ( null != key ) {
    			Widget w = this.widgets.get(key);
    			if ( null != w ) {
    				if ( w == widget ) {
    					HashMap<String, String> hashMap = this.values.get(key);
    					if ( null != hashMap) {
    						element = hashMap.get(WidgetAttribute.element.toString());
    						logger.log(Level.SEVERE, "getWidgetName key["+key+"] element["+element+"]");
    						break;
    					} else {
    						logger.log(Level.SEVERE, "getWidgetName hashMap at key["+key+"] IS NULL");
    					}
    				}
    			}
    		}
    	}
    	
    	logger.log(Level.FINE, "getWidgetName End");
    	
		return element;
    	
    }
    
    @Override
    public Widget getWidget(String element) {
    	
    	logger.log(Level.FINE, "getWidget Begin");
    	
    	logger.log(Level.SEVERE, "getWidget element["+element+"]");
    	
    	Widget widget = null;
    	Set<Integer> keys = this.values.keySet();
    	Iterator<Integer> iter = keys.iterator();
    	while ( iter.hasNext() ) {
    		Integer integer = iter.next();
    		if ( null != integer ) {
	    		HashMap<String, String> valueMap = this.values.get(integer);
	    		if ( null != valueMap ) {
	 				String e = valueMap.get(WidgetAttribute.element.toString());
					if ( e != null && 0 == element.compareTo(e) ) {
						widget = widgets.get(integer);
						break;
					}
	    		}
    		}
    	}
    	
    	if ( null == widget ) {
    		logger.log(Level.SEVERE, "getWidget elementValue["+element+"] widget IS NULL");
    	}
    	
    	logger.log(Level.FINE, "getWidget End");
    	
		return widget;
    }
    
    @Override
    public WidgetStatus getWidgetStatus ( String element ) {
    	logger.log(Level.FINE, "setWidgetStatus Begin");
    	
    	logger.log(Level.SEVERE, "setWidgetStatus element["+element+"]");
    	
    	WidgetStatus status = getWidgetStatus(getWidget(element));
    	
    	logger.log(Level.SEVERE, "setWidgetStatus status["+status+"]");
    	
    	logger.log(Level.FINE, "setWidgetStatus End");
    	
    	return status;
    }
    
    public WidgetStatus getWidgetStatus ( Widget widget ) {
    	
    	logger.log(Level.FINE, "getWidgetStatus Begin");
    	
    	logger.log(Level.SEVERE, "getWidgetStatus widget["+widget+"]");
    	
    	WidgetStatus status = null;
    	if ( null != widget ) {
			HashMap<String, String> valueMap 	= getWidgetValues(widget);
			
			String strWidget					= valueMap.get(WidgetAttribute.widget.toString());
			
			String label						= valueMap.get(WidgetAttribute.label.toString());
			String icon							= valueMap.get(WidgetAttribute.icon.toString());
			String css							= valueMap.get(WidgetAttribute.css.toString());
			String labelDown					= valueMap.get(WidgetAttribute.labelDown.toString());
			String iconDown						= valueMap.get(WidgetAttribute.iconDown.toString());
			String cssDown						= valueMap.get(WidgetAttribute.cssDown.toString());
			String tooltipDown					= valueMap.get(WidgetAttribute.tooltipDown.toString());
			String labelDisable					= valueMap.get(WidgetAttribute.labelDisable.toString());
			String iconDisable					= valueMap.get(WidgetAttribute.iconDisable.toString());
			String cssDisable					= valueMap.get(WidgetAttribute.cssDisable.toString());
			String tooltipDisable				= valueMap.get(WidgetAttribute.tooltipDisable.toString());
			
			TranslationMgr translationMgr = TranslationMgr.getInstance();
			if ( null != translationMgr.getTranslationEngine() ) {
				label = translationMgr.getTranslation(label);
			}
			
			if ( WidgetType.ImageButton.equalsName(strWidget) || WidgetType.ImageToggleButton.equalsName(strWidget) ) {
				
				String html = ((Button)widget).getHTML();
				
				logger.log(Level.SEVERE, "getWidgetStatus html["+html+"]");
				
				if ( null != label ) {
					if ( null != labelDisable && html.indexOf(labelDisable) != -1 ) {
						status = WidgetStatus.Disable;
					} else if ( null != labelDown && html.indexOf(labelDown) != -1 ) {
						status = WidgetStatus.Down;
					} else if ( null != label && html.indexOf(label) != -1 ) {
						status = WidgetStatus.Up;
					}
				} else if ( null != icon ) {
					if ( null != iconDisable && html.indexOf(iconDisable) != -1 ) {
						status = WidgetStatus.Disable;
					} else if ( null != iconDown && html.indexOf(iconDown) != -1 ) {
						status = WidgetStatus.Down;
					} else  if ( null != icon && html.indexOf(icon) != -1 ) {
						status = WidgetStatus.Up;
					}
				}
			} else if ( WidgetType.InlineLabel.equalsName(strWidget) || WidgetType.Button.equalsName(strWidget) ) {
				
				String tooltip = null;
				String stylename = null;
				
				if ( WidgetType.InlineLabel.equalsName(strWidget) ) {
					tooltip = ((InlineLabel)widget).getTitle();
					stylename = ((InlineLabel)widget).getStyleName();
				} else if ( WidgetType.Button.equalsName(strWidget) ) {
					tooltip = ((Button)widget).getTitle();
					stylename = ((Button)widget).getStyleName();
				} else {
					logger.log(Level.SEVERE, "getWidgetStatus widget type IS VALID");
				}

				if ( null != tooltipDown || null != tooltipDisable ) {
					
					logger.log(Level.SEVERE, "getWidgetStatus stylename["+stylename+"]");
					
					if ( null != tooltip ) {
						if ( null != tooltipDisable && 0 == tooltip.compareTo(tooltipDisable) ) {
							status = WidgetStatus.Disable;
						} else if ( null != tooltipDown && 0 == tooltip.compareTo(tooltipDown) ) {
							status = WidgetStatus.Down;
						} else  if ( null != tooltip && 0 == tooltip.compareTo(tooltip) ) {
							status = WidgetStatus.Up;
						}
					}
				} else if ( null != cssDown || null != cssDisable ) {
					
					logger.log(Level.SEVERE, "getWidgetStatus stylename["+stylename+"]");
					
					if ( null != stylename ) {
		//				String stylenames [] = stylename.split("\\s");
						if ( null != cssDisable && stylename.indexOf(cssDisable) != -1 ) {
							status = WidgetStatus.Disable;
						} else if ( null != cssDown && stylename.indexOf(cssDown) != -1 ) {
							status = WidgetStatus.Down;
						} else  if ( null != css && stylename.indexOf(css) != -1 ) {
							status = WidgetStatus.Up;
						}					
					}					
				} else {
					logger.log(Level.SEVERE, "getWidgetStatus status checking IS INALID");
				}
			} else {
				logger.log(Level.SEVERE, "getWidgetStatus widget type IS VALID");
			}
    	} else {
    		logger.log(Level.SEVERE, "getWidgetStatus widget IS NULL");
    	}
    	
    	logger.log(Level.SEVERE, "getWidgetStatus status["+status+"]");
    	
    	logger.log(Level.FINE, "getWidgetStatus End");
    	
    	return status;
    }
    
	@Override
	public void setWidgetStatus(String element, WidgetStatus status) {
    	logger.log(Level.FINE, "setWidgetStatus Begin");
    	
    	logger.log(Level.SEVERE, "setWidgetStatus element["+element+"] status["+status.toString()+"]");
    	
    	setWidgetStatus(getWidget(element), status);
    	
    	logger.log(Level.FINE, "setWidgetStatus End");
	}
    
    public void setWidgetStatus ( Widget widget, WidgetStatus status ) {
    	
    	logger.log(Level.FINE, "setWidgetStatus Begin");
    	
    	logger.log(Level.SEVERE, "setWidgetStatus widget["+widget+"] status["+status+"]");
    	
    	if ( null != widget ) {
    		 
    		if ( null != status ) {
    			
    			HashMap<String, String> valueMap 	= getWidgetValues(widget);
    			
    			if ( null != valueMap ) {
	 				String strWidget					= valueMap.get(WidgetAttribute.widget.toString());
					String iconDivWidth					= valueMap.get(WidgetAttribute.iconDivWidth.toString());
					String iconDivHeight				= valueMap.get(WidgetAttribute.iconDivHeight.toString());
					String iconImgWidth					= valueMap.get(WidgetAttribute.iconImgWidth.toString());
					String iconImgHeight				= valueMap.get(WidgetAttribute.iconImgHeight.toString());
					
					String label	= null;
					String icon		= null;
					String toolTip	= null;
//					String css		= null;
//					String enable	= null;
	    			
					if ( WidgetType.ImageButton.toString().equals(strWidget) || WidgetType.ImageToggleButton.toString().equals(strWidget) 
							|| WidgetType.Button.toString().equals(strWidget) || WidgetType.InlineLabel.toString().equals(strWidget)) {
						
						String cssUp		= valueMap.get(WidgetAttribute.cssUp.toString());
						String cssDown		= valueMap.get(WidgetAttribute.cssDown.toString());
						String cssDisable	= valueMap.get(WidgetAttribute.cssDisable.toString());
						
						if ( WidgetStatus.Up == status ) {
							label		= valueMap.get(WidgetAttribute.label.toString());
							toolTip		= valueMap.get(WidgetAttribute.tooltip.toString());
							icon		= valueMap.get(WidgetAttribute.icon.toString());
//							css			= valueMap.get(WidgetAttribute.css.toString());
//							enable		= valueMap.get(WidgetAttribute.enable.toString());
						} else if ( WidgetStatus.Down == status ) {
							label		= valueMap.get(WidgetAttribute.labelDown.toString());
							toolTip		= valueMap.get(WidgetAttribute.tooltipDown.toString());
							icon		= valueMap.get(WidgetAttribute.iconDown.toString());
//							css			= valueMap.get(WidgetAttribute.cssDown.toString());
//							enable		= valueMap.get(WidgetAttribute.enableDown.toString());
						} else if ( WidgetStatus.Disable == status ) {
							label		= valueMap.get(WidgetAttribute.labelDisable.toString());
							toolTip		= valueMap.get(WidgetAttribute.tooltipDisable.toString());
							icon		= valueMap.get(WidgetAttribute.iconDisable.toString());
//							css			= valueMap.get(WidgetAttribute.cssDisable.toString());
//							enable		= valueMap.get(WidgetAttribute.enableDisable.toString());
						}
						
						TranslationMgr translationMgr = TranslationMgr.getInstance();
						if ( null !=  translationMgr.getTranslationEngine() ) {
							label = translationMgr.getTranslation(label);
						}
						
						if ( null != label )	((Button)widget).setText(label);
	
						if ( null != iconDivWidth && null != iconDivHeight ) {
							String img = null;
							String lbl = null;
							if ( null != icon && null != iconImgWidth && null != iconImgHeight ) {
								img = "<img src=\""+basePath+icon+"\" width=\""+iconImgWidth+"\" height=\""+iconImgHeight+"\">";
							}
							if ( null != label ) {
								lbl = "<label>"+label+"</lable>";
							}
							String html = "<div width=\""+iconDivWidth+"\" height=\""+iconDivHeight+"\"><center>"+(null==img?"":img)+(null==lbl?"":lbl)+"</center></div>";
							
							logger.log(Level.FINE, "setWidgetStatus html["+html+"]");
							
							((Button)widget).setHTML(html);
						}
						
						if ( null != toolTip )	((Button)widget).setTitle(toolTip);
	
						String cssAdd = null, cssRemove1 = null, cssRemove2 = null;
						if ( WidgetStatus.Disable == status ) {
							cssRemove1	= cssUp;
							cssRemove2	= cssDown;
							cssAdd		= cssDisable;
						} else if ( WidgetStatus.Down == status ) {
							cssRemove1	= cssUp;
							cssRemove2	= cssDisable;
							cssAdd		= cssDown;
						} else if ( WidgetStatus.Up == status ) {
							cssRemove1	= cssDown;
							cssRemove2	= cssDisable;
							cssAdd		= cssUp;
						}
						if ( null != cssRemove1 ) {
							((Button)widget).removeStyleName(cssRemove1);
							logger.log(Level.SEVERE, "setWidgetStatus status["+status+"] removeStyleName["+cssRemove1+"]");
						}
						if ( null != cssRemove2 ) {
							((Button)widget).removeStyleName(cssRemove2);
							logger.log(Level.SEVERE, "setWidgetStatus status["+status+"] removeStyleName["+cssRemove2+"]");
						}
						if ( null != cssAdd ) {
							((Button)widget).addStyleName(cssAdd);
							logger.log(Level.SEVERE, "setWidgetStatus status["+status+"] addStyleName["+cssAdd+"]");
						}
						
//						if ( null != enable )	((Button)widget).setEnabled(0==enable.compareToIgnoreCase("true"));
						
						if ( WidgetType.RadioButton.toString().equals(strWidget) ) {
							((RadioButton)widget).setValue(WidgetStatus.Down == status);
						}
						
						if ( WidgetType.Button.toString().equals(strWidget) )	
							((Button)widget).setEnabled(!(WidgetStatus.Disable == status));
		    			
					} else {
						logger.log(Level.SEVERE, "setWidgetStatus widget IS INVALID");
					}
    			} else {
    				logger.log(Level.SEVERE, "setWidgetStatus valueMap IS INVALID");
    			}
    		} else {
    			logger.log(Level.SEVERE, "setWidgetStatus status IS NULL");
    		}
    	} else {
    		logger.log(Level.SEVERE, "setWidgetStatus widget IS NULL");
    	}
    	
    	logger.log(Level.FINE, "setWidgetStatus End");
    }
    
    @Override
	public void setValue (String elementValue) {
    	
    	logger.log(Level.FINE, " **** updateValue Begin");
    	
    	logger.log(Level.FINE, " **** updateValue name["+elementValue+"]");
    	
    	setValue(elementValue, null);
    	
    	logger.log(Level.FINE, " **** updateValue End");
    	
    }
	
	@Override
	public void setValue (String elementValue, String value) {
		
		logger.log(Level.FINE, " **** updateValue Begin");
		
		logger.log(Level.FINE, " **** updateValue name["+elementValue+"] value["+value+"]");

		int index = getElementIndex(WidgetAttribute.element, elementValue);
		
		Widget w = this.widgets.get(index);
		
		if ( index >= 0 && index < this.widgets.size() ) {
			
			if ( null != w ) {
				
				HashMap<String, String> valueMap = this.values.get(index);
				String widget		= valueMap.get(WidgetAttribute.widget.toString());
				String media		= valueMap.get(WidgetAttribute.media.toString());
				String label		= valueMap.get(WidgetAttribute.label.toString());
				String format		= valueMap.get(WidgetAttribute.format.toString());
				
				logger.log(Level.FINE, "updateValue index["+index+"] widget["+widget+"] media["+media+"]");

				if ( null != value ) label = value;
				
				if ( WidgetMedia.DateTimeFormat.equalsName(media) && (null != format && 0 != format.length()) ) {
					label = DateTimeFormat.getFormat(format).format(new Date());
				}
				
				if ( WidgetType.TextBox.equalsName(widget) ) {
					((TextBox)w).setText(label);
				} else if ( WidgetType.InlineLabel.equalsName(widget) ) {
					((InlineLabel)w).setText(label);
				} else if ( WidgetType.Image.equalsName(widget) ) {
					((Image)w).setUrl(label);
				} else {
					logger.log(Level.SEVERE, "updateValue WidgetType IS NULL");
				}
				
				
			} else {
				logger.log(Level.FINE, " **** updateValue widget IS NULL");
			}
		} else {
			logger.log(Level.FINE, " **** updateValue index IS INVALID index["+index+"] this.widgets.size()["+this.widgets.size()+"]");
		}
		
		logger.log(Level.FINE, " **** updateValue End");
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
								if ( RootAttribute.rows.equalsName(k) ) {
									rows = Integer.parseInt(v);
								} else if ( RootAttribute.cols.equalsName(k) ) {
									cols = Integer.parseInt(v);
								} else if ( RootAttribute.cssVerticalPanel.equalsName(k) ) {
									strCSSStatPanel = v;
								} else if ( RootAttribute.cssFlexTable.equalsName(k)  ) {
									strCSSFlexTable = v;
								} else if ( RootAttribute.widget.equalsName(k) ) {
									strRootWidget = v;
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
				
				logger.log(Level.FINE, "FINE dictionary ");

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
										
										logger.log(Level.FINE, "FINE dictionary key["+key+"]");
										
										break;
									}
									
								}
								// Get Header End
							}
						}
						
						logger.log(Level.FINE, "dictionary key["+key+"]");
						
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
									
									logger.log(Level.FINE, "dictionary row["+row+"] col["+col+"] => index["+index+"]");
									
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
	
	private HashMap<String, String> getWidgetValues(Widget widget) {
		HashMap<String, String> values = null;
		Set<Integer> keys = this.widgets.keySet();
		Iterator<Integer> iter = keys.iterator();
		while ( iter.hasNext() ) {
			Integer key = iter.next();
			if ( key != null ) {
				if ( widget == this.widgets.get(key) ) {
					values = this.values.get(key);
					break;
				}
			}
		}
		return values;
	}
    
    private int getElementIndex(WidgetAttribute widgetAttribute, String elementValue) {
		int index = -1;
		for( int i=0 ; i < values.size(); i++ ) {
			HashMap<String, String> valueMap = this.values.get(i);
			if ( null != valueMap ) {
				String element = valueMap.get(widgetAttribute.toString());
				if ( element != null && 0 == elementValue.compareTo(element) ) {
					index = i;
					break;
				}
			}
		}
		return index;
    }
    
}
