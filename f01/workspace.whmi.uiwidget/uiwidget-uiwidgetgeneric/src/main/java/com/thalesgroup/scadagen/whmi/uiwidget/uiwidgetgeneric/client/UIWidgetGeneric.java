package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
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
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.RootAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.RootWidgetType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetMedia;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetType;

public class UIWidgetGeneric extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetGeneric.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	
	private static final String basePath = GWT.getModuleBaseURL();
	
	private int rows	= 0;
	private int cols	= 0;
	private int totals	= 0;
	
    private String strRootContainerCss;
    private String strRootPanel;

    private String strRootCss;
    
    private HashMap<String, WidgetStatus> widgetStatus = new HashMap<String, WidgetStatus>();
    
    private HashMap<Integer, HashMap<String, String>> values = new HashMap<Integer, HashMap<String, String>>();
    
    private HashMap<Integer, Widget> widgets = new HashMap<Integer, Widget>();
    
    protected String getDefaultDebugId(String element) {
    	String debugId = null;
		if ( null != uiNameCard && null != element ) {
			String uiPath = uiNameCard.getUiPath();
			if ( null != uiPath ) {
				debugId = uiPath + ":" + element;
			}
		}
    	return debugId;
	}

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
			logger.info(className, "getElementValues", "name[{}][{}]", i, elements[i]);
		}
    	return elements;
    }

    private Dictionary dictionaryHeader = null;
    private Dictionary dictionaryOption = null;

	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
	};
    
	@Override
    public void init() {
		final String function = "init";
    	
    	logger.info(className, function, "this.viewXMLFile[{}]", this.viewXMLFile);
    	
    	DictionariesCache uiPanelSettingCache = DictionariesCache.getInstance("UIWidgetGeneric");
		
		this.dictionaryHeader = uiPanelSettingCache.getDictionary( this.viewXMLFile, DictionaryCacheInterface.Header );
		this.dictionaryOption = uiPanelSettingCache.getDictionary( this.viewXMLFile, DictionaryCacheInterface.Option );
		
		ready(this.dictionaryHeader);
		ready(this.dictionaryOption);
    	
		//UIGeneric
		
		logger.begin(className, function);
		
		if ( null == dictionaryHeader || null == dictionaryOption ) {
			
			logger.warn(className, function, "dictionaryHeader OR dictionaryOption IS NULL");
						
			rootPanel = new VerticalPanel();
			
			if ( null == dictionaryHeader ) rootPanel.add( new InlineLabel( "Faild to load viewXMLFile["+this.viewXMLFile+"] strHeader["+DictionaryCacheInterface.Header+"]" ));
			
			if ( null == dictionaryOption ) rootPanel.add( new InlineLabel( "Faild to load viewXMLFile["+this.viewXMLFile+"] strOption["+DictionaryCacheInterface.Option+"]" ));
			
		} else {

			logger.info(className, function, "strRootPanel[{}]", strRootPanel);
			logger.info(className, function, "strRootContainerCss[{}]", strRootContainerCss);
	    	logger.info(className, function, "strRootCss[{}]", strRootCss);
	    	
	    	if ( RootWidgetType.HorizontalPanel.equalsName(strRootPanel) ) {

	    		rootPanel = new HorizontalPanel();
	    		
	    	} else if ( RootWidgetType.FlexTable.equalsName(strRootPanel) ) {

	    		rootPanel = new FlexTable();
	    		
	    	} else if ( RootWidgetType.VerticalPanel.equalsName(strRootPanel) ) {

	    		rootPanel = new VerticalPanel();
	    		
	    	} else if ( RootWidgetType.AbsolutePanel.equalsName(strRootPanel) ) {

	    		rootPanel = new AbsolutePanel();
	    	}
	    	
	    	if ( null != strRootCss )	rootPanel.addStyleName(strRootCss);
			
		    for ( int i = 0 ; i < rows ; ++i ) {
		    	
				logger.info(className, function, "Build Filter Table Loop i[{}] Begin", i);
				
				for ( int j = 0 ; j < cols ; ++j ) {
					
					int index = (i*cols)+j;
					
					logger.info(className, function, "Build Filter Table Loop i[{}] j[{}] => index[{}]", new Object[]{i, j, index});
					
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
							
							String cssContainer		= valueMap.get(WidgetAttribute.cssContainer.toString());
							
							String left				= valueMap.get(WidgetAttribute.left.toString());
							String top				= valueMap.get(WidgetAttribute.top.toString());
							
//							String menuType			= valueMap.get(WidgetAttribute.menuType.toString());
//							String menuLevel		= valueMap.get(WidgetAttribute.menuLevel.toString());
							
							String groupName		= valueMap.get(WidgetAttribute.groupName.toString());
							
							String element			= valueMap.get(WidgetAttribute.element.toString());
							String debugId			= valueMap.get(WidgetAttribute.debugId.toString());
							
							logger.info(className, function, "Build Filter Table Loop i[{}] j[{}] widget[{}]", new Object[]{i, j, widget});
							logger.info(className, function, "Build Filter Table Loop i[{}] j[{}] label[{}]", new Object[]{i, j, label});
							logger.info(className, function, "Build Filter Table Loop i[{}] j[{}] css[{}]", new Object[]{i, j, css});
							
							TranslationMgr translationMgr = TranslationMgr.getInstance();
							if ( null != translationMgr ) {
								label = translationMgr.getTranslation(label);
							} else {
								logger.warn(className, function, "getTranslation IS NULL");
							}
							
							if ( WidgetType.TextBox.equalsName(widget)
									|| null == widget || 0 == widget.length() ) {
								w = new TextBox();
								
								if ( null != label )	((TextBox)w).setText(label);
								if ( null != tooltip )	((TextBox)w).setTitle(tooltip);
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

								if ( WidgetAttribute.maxlength.equalsName(maxlength) )
									((PasswordTextBox)w).setMaxLength(Integer.parseInt(maxlength));
								
								if ( WidgetAttribute.readonly.equalsName(readonly) )
									((PasswordTextBox)w).setReadOnly(true);

								this.widgets.put(index, w);
							} else if ( WidgetType.InlineLabel.equalsName(widget) ) {
								
								w = new InlineLabel();
								
								if ( null != label )	((InlineLabel)w).setText(label);
								if ( null != tooltip )	((InlineLabel)w).setTitle(tooltip);

								this.widgets.put(index, w);
							} else if ( WidgetType.Button.equalsName(widget) ) {
								
								w = new Button();
								
								if ( null != label )	((Button)w).setText(label);
								if ( null != tooltip )	((Button)w).setTitle(tooltip);
								
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
									
									logger.info(className, function, "getMainPanel html["+html+"]");
									
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

								this.widgets.put(index, w);
							} else if ( WidgetType.ListBox.equalsName(widget) ) {
								
								w = new ListBox();
								if ( null != label )	((ListBox)w).addItem(label);
								
								if ( WidgetAttribute.visibleitemcount.equalsName(visibleitemcount) ) {
									((ListBox)w).setVisibleItemCount(Integer.parseInt(visibleitemcount));
								}

								this.widgets.put(index, w);
							} else if ( WidgetType.HTML.equalsName(widget) ) {
								
								if ( null != label ) 	w = new HTML(label);

								this.widgets.put(index, w);
							} else if ( WidgetType.RadioButton.equalsName(widget) ) {
								
								if ( null == groupName )
									logger.warn(className, function, "getMainPanel created widget["+widget+"] groupName IS NULL");
								
								if ( null != groupName ) 	w = new RadioButton(groupName);
								
								if ( null != label )		((RadioButton)w).setText(label);
								if ( null != tooltip )		((RadioButton)w).setTitle(tooltip);
								
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
								
								String view = null;
								String opt = null;
								HashMap<String, Object> options = new HashMap<String, Object>();
								
								UIWidget_i uiWIdget = uiPredefinePanelMgr.getUIWidget(widget, view, uiNameCard, opt, options);
	
								if ( null != uiWIdget ) {
									uiWIdget.setUINameCard(this.uiNameCard);
									w = uiWIdget.getMainPanel();
								} else {
									logger.warn(className, function, "getMainPanel created UIPredefinePanelMgr widget["+widget+"] IS NULL");
								}
	
								this.widgets.put(index, w);
							} else {
								logger.warn(className, function, "getMainPanel widget["+widget+"] IS INVALID");
							}
							
							if ( null != w ) {
								
								if ( null != css )		w.addStyleName(css);
								
								String strDebugId = getDefaultDebugId(element);
								if ( strDebugId != null ) {
									w.ensureDebugId(strDebugId);
								}
								if ( null != debugId ) w.ensureDebugId(debugId);									
								
								if ( RootWidgetType.HorizontalPanel.equalsName(strRootPanel) ) {
									rootPanel.add(w);
								} else if ( RootWidgetType.VerticalPanel.equalsName(strRootPanel) ) {
									rootPanel.add(w);
								} else if ( RootWidgetType.FlexTable.equalsName(strRootPanel) ) {	
									((FlexTable)rootPanel).setWidget(i, j, w);
								} else if ( RootWidgetType.AbsolutePanel.equalsName(strRootPanel) ) {
									int x = -1;
									int y = -1;
									try {
										if ( null != left )	x = Integer.parseInt(left);
										if ( null != top )	y = Integer.parseInt(top);
									} catch ( NumberFormatException e ) {
										logger.warn(className, function, "getMainPanel left or top IS INVALID");
									}
									((AbsolutePanel)rootPanel).add(w, x, y);
								}
								
								if ( null != cssContainer ) {
									Element container = DOM.getParent(w.getElement());
									container.setClassName(cssContainer);
								}
							} else {
								logger.warn(className, function, "can't createt widget index[{}], w IS NULL", index);
							}
						} else {
							logger.warn(className, function, "index[{}], check index please, valueMap IS NULL", index);
						}
					} else {
						logger.warn(className, function, "Build Filter INVALID index[{}] > values.length[{}]", index, values.size());
					}
				}
				logger.info(className, function, "Build Filter Table Loop i[{}] End", i);
		    }

	    }
		
		logger.end(className, function);
		
    }
    
    public String getWidgetElement(Widget widget) {
    	final String function = "getWidgetElement";
    	
    	logger.begin(className, function);
    	
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
    						logger.info(className, function, "key[{}] element[{}]", key, element);
    						break;
    					} else {
    						logger.warn(className, function, "hashMap at key[{}] IS NULL", key);
    					}
    				}
    			}
    		}
    	}
    	
    	logger.end(className, function);
    	
		return element;
    	
    }
    
    @Override
    public Widget getWidget(String element) {
    	final String function = "getWidget";
    	
    	logger.begin(className, function);
    	
    	logger.info(className, function, "element[{}]", element);
    	
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
    		logger.warn(className, function, "elementValue[{}] widget IS NULL", element);
    	}
    	
    	logger.end(className, function);
    	
		return widget;
    }
    
    @Override
    public WidgetStatus getWidgetStatus ( String element ) {
    	final String function = "getWidgetStatus";
    	logger.begin(className, function);
    	
    	logger.info(className, function, "element[{}]", element);
    	
    	WidgetStatus status = getWidgetStatus(getWidget(element));
    	
    	logger.info(className, function, "status[{}]", status);
    	
    	logger.end(className, function);
    	
    	return status;
    }
    
    public WidgetStatus getWidgetStatus ( Widget widget ) {
    	final String function = "getWidgetStatus";
    	
    	logger.begin(className, function);
    	
    	logger.info(className, function, "widget[{}]", widget);
    	
    	WidgetStatus status = null;
    	if ( null != widget ) {
    		HashMap<String, String> valueMap 	= getWidgetValues(widget);
    		String element						= valueMap.get(WidgetAttribute.element.toString());
    		status = widgetStatus.get(element);
        	if ( null == status ) {
        		logger.warn(className, function, "element[{}] status IS NULL, set as WidgetStatus.Up", element);
        		
        		status = WidgetStatus.Up;
        		widgetStatus.put(element, status);
        	}
    	}
    	
    	return status;
    }
    
	@Override
	public void setWidgetStatus(String element, WidgetStatus status) {
		final String function = "setWidgetStatus";
		
    	logger.begin(className, function);
    	logger.info(className, function, "element[{}] status[{}]", element, status.toString());
    	
    	setWidgetStatus(getWidget(element), status);
    	
    	logger.end(className, function);
	}
    
    public void setWidgetStatus ( Widget widget, WidgetStatus status ) {
    	final String function = "setWidgetStatus";
    	
    	logger.begin(className, function);
    	logger.info(className, function, "widget[{}] status[{}]", widget, status);
    	
    	if ( null != widget ) {
    		 
    		if ( null != status ) {
    			
    			HashMap<String, String> valueMap 	= getWidgetValues(widget);
    			
    			if ( null != valueMap ) {
	 				String strWidget					= valueMap.get(WidgetAttribute.widget.toString());
					String iconDivWidth					= valueMap.get(WidgetAttribute.iconDivWidth.toString());
					String iconDivHeight				= valueMap.get(WidgetAttribute.iconDivHeight.toString());
					String iconImgWidth					= valueMap.get(WidgetAttribute.iconImgWidth.toString());
					String iconImgHeight				= valueMap.get(WidgetAttribute.iconImgHeight.toString());
					
					String element						= valueMap.get(WidgetAttribute.element.toString());
					widgetStatus.put(element, status);
					
					String label	= null;
					String icon		= null;
					String toolTip	= null;
	    			
					if ( WidgetType.RadioButton.toString().equals(strWidget) ) {
						
						((RadioButton)widget).setValue(WidgetStatus.Down == status);
						
					} else if ( WidgetType.ImageButton.toString().equals(strWidget) || WidgetType.ImageToggleButton.toString().equals(strWidget) 
							|| WidgetType.Button.toString().equals(strWidget) || WidgetType.InlineLabel.toString().equals(strWidget)) {
						
						String cssUp		= valueMap.get(WidgetAttribute.cssUp.toString());
						String cssDown		= valueMap.get(WidgetAttribute.cssDown.toString());
						String cssDisable	= valueMap.get(WidgetAttribute.cssDisable.toString());
						
						if ( WidgetStatus.Up == status ) {
							label		= valueMap.get(WidgetAttribute.label.toString());
							toolTip		= valueMap.get(WidgetAttribute.tooltip.toString());
							icon		= valueMap.get(WidgetAttribute.icon.toString());
						} else if ( WidgetStatus.Down == status ) {
							label		= valueMap.get(WidgetAttribute.labelDown.toString());
							toolTip		= valueMap.get(WidgetAttribute.tooltipDown.toString());
							icon		= valueMap.get(WidgetAttribute.iconDown.toString());
						} else if ( WidgetStatus.Disable == status ) {
							label		= valueMap.get(WidgetAttribute.labelDisable.toString());
							toolTip		= valueMap.get(WidgetAttribute.tooltipDisable.toString());
							icon		= valueMap.get(WidgetAttribute.iconDisable.toString());
						}
						
						TranslationMgr translationMgr = TranslationMgr.getInstance();
						if ( null !=  translationMgr.getTranslationEngine() ) {
							label = translationMgr.getTranslation(label);
						}

						if ( WidgetType.InlineLabel.toString().equals(strWidget) ) {
							if ( null != label )	((InlineLabel)widget).setText(label);
						} else {
							if ( null != label )	((Button)widget).setText(label);
						}

						if ( WidgetType.ImageButton.toString().equals(strWidget) || WidgetType.ImageToggleButton.toString().equals(strWidget) 
							|| WidgetType.Button.toString().equals(strWidget)  ) {
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
								
								logger.info(className, function, "html["+html+"]");
								
								((Button)widget).setHTML(html);
							}							
						}
						
						if ( null != toolTip )	widget.setTitle(toolTip);
	
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
							widget.removeStyleName(cssRemove1);
							logger.info(className, function, "status[{}] removeStyleName[{}]", status, cssRemove1);
						}
						if ( null != cssRemove2 ) {
							widget.removeStyleName(cssRemove2);
							logger.info(className, function, "status[{}] removeStyleName[{}]", status, cssRemove2);
						}
						if ( null != cssAdd ) {
							widget.addStyleName(cssAdd);
							logger.info(className, function, "status[{}] addStyleName[{}]", status, cssAdd);
						}
						
//						if ( null != enable )	((Button)widget).setEnabled(0==enable.compareToIgnoreCase("true"));
						

						
						if ( WidgetType.Button.toString().equals(strWidget) )	
							((Button)widget).setEnabled(!(WidgetStatus.Disable == status));
		    			
					} else {
						logger.warn(className, function, "widget IS INVALID");
					}
    			} else {
    				logger.warn(className, function, "valueMap IS INVALID");
    			}
    		} else {
    			logger.warn(className, function, "status IS NULL");
    		}
    	} else {
    		logger.warn(className, function, "widget IS NULL");
    	}
    	
    	logger.end(className, function);
    }
    
	@Override
	public void setWidgetValue (String element, String value) {
		final String function = "setWidgetValue";
		
		if ( null != value ) logger.begin(className, function);
		if ( null != value ) logger.debug(className, function, "element[{}] value[{}]", element, value);

		int index = getElementIndex(WidgetAttribute.element, element);
		
		Widget w = this.widgets.get(index);
		
		if ( index >= 0 && index < this.widgets.size() ) {
			
			if ( null != w ) {
				
				HashMap<String, String> valueMap = this.values.get(index);
				String widget		= valueMap.get(WidgetAttribute.widget.toString());
				String media		= valueMap.get(WidgetAttribute.media.toString());
				String label		= valueMap.get(WidgetAttribute.label.toString());
				String format		= valueMap.get(WidgetAttribute.format.toString());
				
				if ( null != value ) logger.debug(className, function, "index[{}] widget[{}] media[{}]", new Object[]{index, widget, media});

				if ( null != value ) label = value;
				
				if ( WidgetMedia.DateTimeFormat.equalsName(media) && (null != format && 0 != format.length()) ) {
					label = DateTimeFormat.getFormat(format).format(new Date());
				}
				if ( WidgetType.RadioButton.equalsName(widget) ) {
					((RadioButton)w).setValue((label.equals("true")?true:false));
				} else if ( WidgetType.TextBox.equalsName(widget) ) {
					((TextBox)w).setText(label);
				} else if ( WidgetType.InlineLabel.equalsName(widget) ) {
					((InlineLabel)w).setText(label);
				} else if ( WidgetType.Image.equalsName(widget) ) {
					((Image)w).setUrl(label);
				} else {
					logger.warn(className, function, "WidgetType IS NULL");
				}
				
				
			} else {
				logger.debug(className, function, "widget IS NULL");
			}
		} else {
			logger.warn(className, function, "index IS INVALID index[{}] this.widgets.size()[{}]", index, this.widgets.size());
		}
		
		if ( null != value ) logger.end(className, function);
	}
	
	public void ready(Dictionary dictionary) {
		final String function = "ready";
		logger.begin(className, function);
		logger.info(className, function, "this.viewXMLFile[{}]", this.viewXMLFile);
		
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
								} else if ( RootAttribute.rootCss.equalsName(k) ) {
									strRootCss = v;
								} else if ( RootAttribute.rootContainerCss.equalsName(k)  ) {
									strRootContainerCss = v;
								} else if ( RootAttribute.rootPanel.equalsName(k) ) {
									strRootPanel = v;
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
				
				logger.info(className, function, "dictionary ");

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
									logger.info(className, function, "dictionary row[{}] col[{}]", row, col);
									
									index = (row * cols) + col;
									
									logger.info(className, function, "dictionary row[{}] col[{}] => index[{}]", new Object[]{row, col, index});
									
									HashMap<String, String> hashMap = this.values.get(Integer.valueOf(index));
									if ( null != hashMap ) {
										for ( Object o2 : d2.getValueKeys() ) {
											if ( null != o2 ) {
												String k = (String)o2;
												String v = (String)d2.getValue(o2);
												
												logger.info(className, function, "dictionary k[{}] v[{}]", k, v);
				
												hashMap.put(k, v);
											}
										}
									} else {
										logger.warn(className, function, "row[{}] col[{}] => index[{}] Index NOT EXISTS", new Object[]{row, col, index});
									}
								} else {
									logger.warn(className, function, "keys[0][{}] OR keys[1][{}] is not a number", keys[0], keys[1]);
								}
							}
						} else {
							logger.warn(className, function, "key IS NULL");
						}
					}
				}
			}

		} else {
			logger.warn(className, function, "this.viewXMLFile[{}] dictionary IS NULL", this.viewXMLFile);
		}
		
		logger.end(className, function);
		
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
