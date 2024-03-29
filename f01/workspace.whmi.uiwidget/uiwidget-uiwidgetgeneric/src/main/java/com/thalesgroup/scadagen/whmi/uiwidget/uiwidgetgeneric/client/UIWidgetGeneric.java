package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.RootAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.RootWidgetType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetMedia;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetType;

public class UIWidgetGeneric extends UIGeneric {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private static final String basePath = GWT.getModuleBaseURL();
	
	private int rows	= 0;
	private int cols	= 0;
	private int totals	= 0;
	
    private String strRootContainerCss;
    private String strRootPanel;

    private String strRootCss;
    
    private Map<String, WidgetStatus> widgetStatus = new HashMap<String, WidgetStatus>();
    
    private Map<Integer, HashMap<String, String>> values = new HashMap<Integer, HashMap<String, String>>();
    
    private Map<Integer, Widget> widgets = new HashMap<Integer, Widget>();
    
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
			Map<String, String> valueMap = this.values.get(i);
			if ( null != valueMap ) {
				elements[i] = valueMap.get(element);
			} else {
				elements[i] = "";
			}
		}
		if ( logger.isTraceEnabled() ) {
			for( int i=0 ; i < values.size(); ++i ) {
				logger.trace("getElementValues", "name[{}][{}]", i, elements[i]);
			}
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
    	logger.begin(function);
    	logger.debug(function, "this.viewXMLFile[{}]", this.viewXMLFile);
    	
    	DictionariesCache uiPanelSettingCache = DictionariesCache.getInstance("UIWidgetGeneric");
		
		this.dictionaryHeader = uiPanelSettingCache.getDictionary( this.viewXMLFile, DictionaryCacheInterface.Header );
		this.dictionaryOption = uiPanelSettingCache.getDictionary( this.viewXMLFile, DictionaryCacheInterface.Option );
		
		ready(this.dictionaryHeader);
		ready(this.dictionaryOption);
    	
		//UIGeneric
		
		logger.begin(function);
		
		if ( null == dictionaryHeader || null == dictionaryOption ) {
			
			logger.warn(function, "dictionaryHeader OR dictionaryOption IS NULL");
						
			rootPanel = new VerticalPanel();
			
			if ( null == dictionaryHeader ) rootPanel.add( new Label( "Faild to load viewXMLFile["+this.viewXMLFile+"] strHeader["+DictionaryCacheInterface.Header+"]" ));
			
			if ( null == dictionaryOption ) rootPanel.add( new Label( "Faild to load viewXMLFile["+this.viewXMLFile+"] strOption["+DictionaryCacheInterface.Option+"]" ));
			
		} else {

			logger.debug(function, "strRootPanel[{}]", strRootPanel);
			logger.debug(function, "strRootContainerCss[{}]", strRootContainerCss);
	    	logger.debug(function, "strRootCss[{}]", strRootCss);
	    	
	    	if ( RootWidgetType.HorizontalPanel.equalsName(strRootPanel) ) {

	    		rootPanel = new HorizontalPanel();
	    		
	    	} else if ( RootWidgetType.FlexTable.equalsName(strRootPanel) ) {

	    		rootPanel = new FlexTable();
	    		
	    	} else if ( RootWidgetType.VerticalPanel.equalsName(strRootPanel) ) {

	    		rootPanel = new VerticalPanel();
	    		
	    	} else if ( RootWidgetType.AbsolutePanel.equalsName(strRootPanel) ) {

	    		rootPanel = new AbsolutePanel();
	    		
	    	} else if ( RootWidgetType.FlowPanel.equalsName(strRootPanel) ) {

	    		rootPanel = new FlowPanel();
	    	}
	    	
	    	if ( null != strRootCss )	rootPanel.addStyleName(strRootCss);
			
		    for ( int i = 0 ; i < rows ; ++i ) {
		    	
				logger.trace(function, "Build Filter Table Loop i[{}] Begin", i);
				
				for ( int j = 0 ; j < cols ; ++j ) {
					
					int index = (i*cols)+j;
					
					logger.trace(function, "Build Filter Table Loop i[{}] j[{}] => index[{}]", new Object[]{i, j, index});
					
					if ( index < values.size() ) {
						
						Map<String, String> valueMap = this.values.get(index);
						
						if ( null != valueMap ) {
							
							Widget w = null;
							
							String widget			= valueMap.get(WidgetAttribute.widget.toString());
							String label			= valueMap.get(WidgetAttribute.label.toString());
							String tooltip			= valueMap.get(WidgetAttribute.tooltip.toString());
							String css				= valueMap.get(WidgetAttribute.css.toString());
							String readonly			= valueMap.get(WidgetAttribute.readonly.toString());
							String maxlength		= valueMap.get(WidgetAttribute.maxlength.toString());
							String placeholder	    = valueMap.get(WidgetAttribute.placeholder.toString());
							String visibleitemcount	= valueMap.get(WidgetAttribute.visibleitemcount.toString());
							
							String characterwidth	= valueMap.get(WidgetAttribute.characterwidth.toString());
							String visiblelines		= valueMap.get(WidgetAttribute.visiblelines.toString());
							
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
							
							String disableTranslation			= valueMap.get(WidgetAttribute.disableTranslation.toString());
							
							String uiCtrl			= valueMap.get(WidgetAttribute.uiCtrl.toString());
							String uiView			= valueMap.get(WidgetAttribute.uiView.toString());
							String uiOpts			= valueMap.get(WidgetAttribute.uiOpts.toString());
							
							logger.trace(function, "Build Filter Table Loop i[{}] j[{}] widget[{}]", new Object[]{i, j, widget});
							logger.trace(function, "Build Filter Table Loop i[{}] j[{}] label[{}]", new Object[]{i, j, label});
							logger.trace(function, "Build Filter Table Loop i[{}] j[{}] css[{}]", new Object[]{i, j, css});
							
							if ( null != label ) {
								if ( null == disableTranslation || ( null != disableTranslation && ! disableTranslation.equalsIgnoreCase("true") )) {
									TranslationMgr translationMgr = TranslationMgr.getInstance();
									if ( null != translationMgr ) {
										label = translationMgr.getTranslation(label);
									} else {
										logger.warn(function, "getTranslation IS NULL");
									}
								}
							}

							if ( null != tooltip ) {
								if ( null == disableTranslation || ( null != disableTranslation && ! disableTranslation.equalsIgnoreCase("true") )) {
									TranslationMgr translationMgr = TranslationMgr.getInstance();
									if ( null != translationMgr ) {
										tooltip = translationMgr.getTranslation(tooltip);
									} else {
										logger.warn(function, "getTranslation IS NULL");
									}
								}
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
								if ( null != placeholder && placeholder.length()>0)	{
									if (placeholder.startsWith("&")){
										TranslationMgr translationMgr = TranslationMgr.getInstance();
										placeholder = translationMgr.getTranslation(placeholder);
									}
									((TextBox)w).getElement().setPropertyString("placeholder", placeholder);
								}
								if ( null != readonly )
									((TextBox)w).setReadOnly(true);

								this.widgets.put(index, w);
							} else if ( WidgetType.TextArea.equalsName(widget) ) { 
								
								w = new TextArea();
								
								if ( null != label )	((TextArea)w).setText(label);
								
								if ( WidgetAttribute.characterwidth.equalsName(characterwidth) )
									((TextArea)w).setCharacterWidth(Integer.parseInt(characterwidth));
								
								if ( WidgetAttribute.visiblelines.equalsName(visiblelines) )
									((TextArea)w).setVisibleLines(Integer.parseInt(visiblelines));
								
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
							} else if ( WidgetType.Label.equalsName(widget) ) {
								
								w = new Label();
								
								if ( null != label )	((Label)w).setText(label);
								if ( null != tooltip )	((Label)w).setTitle(tooltip);

								this.widgets.put(index, w);
							}  else if ( WidgetType.Button.equalsName(widget) ) {
								
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
									
									logger.trace(function, "getMainPanel html["+html+"]");
									
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
									logger.warn(function, "getMainPanel created widget["+widget+"] groupName IS NULL");
								
								logger.trace(function, "groupName BF[{}]", groupName);
								groupName = groupName + "_" + uiNameCard.getUiScreen();
								logger.trace(function, "groupName AF[{}]", groupName);
								
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
							}
							else if ( WidgetType.WidgetFactory.toString().equals(widget) ) {
								
								UIWidgetMgr uiPredefinePanelMgr = UIWidgetMgr.getInstance();
								
								String ctrl	= uiCtrl;
								String view	= uiView;
								String opt	= uiOpts;
								String dict = null;
								String elem = null;
								HashMap<String, Object> options = new HashMap<String, Object>();
								
								logger.trace(function, "getMainPanel ctrl[{}] view[{}] opt[{}]", new Object[]{ctrl, view, opt});
								
								UIWidget_i uiWidget = uiPredefinePanelMgr.getUIWidget(ctrl, view, uiNameCard, opt, elem, dict, options);
	
								if ( null != uiWidget ) {
									uiWidget.setUINameCard(this.uiNameCard);
									w = uiWidget.getMainPanel();
								} else {
									logger.warn(function, "getMainPanel created UIPredefinePanelMgr widget["+ctrl+"] IS NULL");
								}
	
								this.widgets.put(index, w);
							} else {
								logger.warn(function, "getMainPanel widget["+widget+"] IS INVALID");
							}
							
							if ( null != w ) {
								
								if ( null != css )		w.addStyleName(css);
								
								String strDebugId = getDefaultDebugId(element);
								if ( strDebugId != null ) {
									strDebugId = strDebugId + "_" + uiNameCard.getUiScreen();
									w.ensureDebugId(strDebugId);
								}
								if ( null != debugId ) w.ensureDebugId(debugId);									
								
								if ( RootWidgetType.HorizontalPanel.equalsName(strRootPanel) ) {
									rootPanel.add(w);
								} else if ( RootWidgetType.VerticalPanel.equalsName(strRootPanel) ) {
									rootPanel.add(w);
								} else if ( RootWidgetType.FlowPanel.equalsName(strRootPanel) ) {	
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
										logger.warn(function, "getMainPanel left or top IS INVALID");
									}
									((AbsolutePanel)rootPanel).add(w, x, y);
								}
								
								if ( null != cssContainer ) {
									Element container = DOM.getParent(w.getElement());
									container.setClassName(cssContainer);
								}
							} else {
								logger.warn(function, "can't createt widget index[{}], w IS NULL", index);
							}
						} else {
							logger.warn(function, "index[{}], check index please, valueMap IS NULL", index);
						}
					} else {
						logger.warn(function, "Build Filter INVALID index[{}] > values.length[{}]", index, values.size());
					}
				}
				logger.debug(function, "Build Filter Table Loop i[{}] End", i);
		    }

	    }
		
		logger.end(function);
		
    }
    
    public String getWidgetElement(Widget widget) {
    	final String function = "getWidgetElement";
    	
    	logger.begin(function);
    	
    	String element = null;
    	Set<Integer> keys = this.widgets.keySet();
    	Iterator<Integer> iter = keys.iterator();
    	while ( iter.hasNext() ) {
    		Integer key = iter.next();
    		if ( null != key ) {
    			Widget w = this.widgets.get(key);
    			if ( null != w ) {
    				if ( w == widget ) {
    					Map<String, String> hashMap = this.values.get(key);
    					if ( null != hashMap) {
    						element = hashMap.get(WidgetAttribute.element.toString());
    						logger.trace(function, "key[{}] element[{}]", key, element);
    						break;
    					} else {
    						logger.warn(function, "hashMap at key[{}] IS NULL", key);
    					}
    				}
    			}
    		}
    	}
    	
    	logger.end(function);
    	
		return element;
    	
    }
    
    @Override
    public Widget getWidget(String element) {
    	final String function = "getWidget";
    	logger.begin(function);
    	logger.trace(function, "element[{}]", element);
    	
    	Widget widget = null;
    	Set<Integer> keys = this.values.keySet();
    	Iterator<Integer> iter = keys.iterator();
    	while ( iter.hasNext() ) {
    		Integer integer = iter.next();
    		if ( null != integer ) {
	    		Map<String, String> valueMap = this.values.get(integer);
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
    		logger.warn(function, "elementValue[{}] widget IS NULL", element);
    	}
    	logger.end(function);
		return widget;
    }
    
    @Override
    public WidgetStatus getWidgetStatus ( String element ) {
    	final String function = "getWidgetStatus";
    	logger.begin(function);
    	logger.trace(function, "element[{}]", element);
    	
    	WidgetStatus status = getWidgetStatus(getWidget(element));
    	
    	logger.trace(function, "status[{}]", status);
    	logger.end(function);
    	return status;
    }
    
    public WidgetStatus getWidgetStatus ( Widget widget ) {
    	final String function = "getWidgetStatus";
    	logger.begin(function);
    	logger.trace(function, "widget[{}]", widget);
    	
    	WidgetStatus status = null;
    	if ( null != widget ) {
    		Map<String, String> valueMap 	= getWidgetValues(widget);
    		String element						= valueMap.get(WidgetAttribute.element.toString());
    		status = widgetStatus.get(element);
        	if ( null == status ) {
        		logger.warn(function, "element[{}] status IS NULL, set as WidgetStatus.Up", element);
        		
        		status = WidgetStatus.Up;
        		widgetStatus.put(element, status);
        	}
    	}
    	return status;
    }
		
	@Override
	public void setWidgetStatus(String element, WidgetStatus status) {
		final String function = "setWidgetStatus";
    	logger.begin(function);
    	logger.trace(function, "element[{}] status[{}]", element, status.toString());
    	
    	setWidgetStatus(getWidget(element), status);
    	
    	logger.end(function);
	}
    
    public void setWidgetStatus ( Widget widget, WidgetStatus status ) {
    	final String function = "setWidgetStatus";
    	logger.begin(function);
    	logger.trace(function, "widget[{}] status[{}]", widget, status);
    	
    	if ( null != widget ) {
    		 
    		if ( null != status ) {
    			
    			Map<String, String> valueMap 	= getWidgetValues(widget);
    			
    			if ( null != valueMap ) {
    			
	    			if ( WidgetStatus.Visible == status || WidgetStatus.Invisible == status ) {
	        			boolean visible = (WidgetStatus.Visible == status);
	        			logger.trace(function, "visible[{}]", status);
	        			widget.setVisible(visible);
	
	        			String element						= valueMap.get(WidgetAttribute.element.toString());
						widgetStatus.put(element, status);
	    			} else {

    	 				String strWidget					= valueMap.get(WidgetAttribute.widget.toString());
    					String iconDivWidth					= valueMap.get(WidgetAttribute.iconDivWidth.toString());
    					String iconDivHeight				= valueMap.get(WidgetAttribute.iconDivHeight.toString());
    					String iconImgWidth					= valueMap.get(WidgetAttribute.iconImgWidth.toString());
    					String iconImgHeight				= valueMap.get(WidgetAttribute.iconImgHeight.toString());
    					
    					String disableTranslation			= valueMap.get(WidgetAttribute.disableTranslation.toString());
    					
    					String element						= valueMap.get(WidgetAttribute.element.toString());
    					widgetStatus.put(element, status);
    					
    					String label	= null;
    					String icon		= null;
    					String toolTip	= null;
    	    			
    					if ( WidgetType.RadioButton.toString().equals(strWidget) ) {
    						
    						((RadioButton)widget).setValue(WidgetStatus.Down == status);
    						
    					} else if ( 
    							   WidgetType.ImageButton.toString().equals(strWidget) 
    							|| WidgetType.ImageToggleButton.toString().equals(strWidget) 
    							|| WidgetType.Button.toString().equals(strWidget) 
    							|| WidgetType.InlineLabel.toString().equals(strWidget)
    							|| WidgetType.Label.toString().equals(strWidget)
    							|| WidgetType.ListBox.toString().equals(strWidget)
    							) {
    						
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
    						
    						if ( null != label ) {
    							if ( null == disableTranslation || ( null != disableTranslation && ! disableTranslation.equalsIgnoreCase("true") )) {
	        						TranslationMgr translationMgr = TranslationMgr.getInstance();
	        						if ( null !=  translationMgr.getTranslationEngine() ) {
	        							label = translationMgr.getTranslation(label);
	        						}
    							}
    						}

    						/*
    						if ( WidgetType.InlineLabel.toString().equals(strWidget) ) {
    							if ( null != label )	((InlineLabel)widget).setText(label);
    						} else if ( WidgetType.Label.toString().equals(strWidget) ) {
    							if ( null != label )	((Label)widget).setText(label);
    						} else {
    							if ( null != label )	((Button)widget).setText(label);
    						}*/

    						if ( WidgetType.ImageButton.toString().equals(strWidget) || WidgetType.ImageToggleButton.toString().equals(strWidget) 
    							/*|| WidgetType.Button.toString().equals(strWidget)*/  ) {
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
    								
    								logger.trace(function, "html["+html+"]");
    								
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
    							logger.trace(function, "status[{}] removeStyleName[{}]", status, cssRemove1);
    						}
    						if ( null != cssRemove2 ) {
    							widget.removeStyleName(cssRemove2);
    							logger.trace(function, "status[{}] removeStyleName[{}]", status, cssRemove2);
    						}
    						if ( null != cssAdd ) {
    							widget.addStyleName(cssAdd);
    							logger.trace(function, "status[{}] addStyleName[{}]", status, cssAdd);
    						}

    						if ( widget instanceof FocusWidget ) {
    							logger.debug(function, "widget[{}] instanceof FocusWidget", widget);
    							boolean bStatus = !(WidgetStatus.Disable == status);
    							logger.trace(function, "widget[{}] setEnabled bStatus[{}]", widget, bStatus);
    							((FocusWidget)widget).setEnabled(bStatus);
    						}
    					} else {
    						logger.warn(function, "widget IS INVALID");
    					}
        			}
    			} else {
    				logger.warn(function, "valueMap IS INVALID");
    			}
    		} else {
    			logger.warn(function, "status IS NULL");
    		}
    	} else {
    		logger.warn(function, "widget IS NULL");
    	}
    	
    	logger.end(function);
    }
    
    private final String [] getWidgetValueSupported = new String[]{
  		  WidgetType.Button.toString(), WidgetType.RadioButton.toString()
  		, WidgetType.TextBox.toString(), WidgetType.TextArea.toString(), WidgetType.PasswordTextBox.toString()
  		, WidgetType.InlineLabel.toString(), WidgetType.Label.toString()
  		, WidgetType.Image.toString()
  		, WidgetType.ListBox.toString()};
  public boolean isGetWidgetValueSupported(String widgetType) {
		final String function = "isGetWidgetValueSupported";
		logger.begin(function);
		boolean result = false;
		logger.trace(function, "widgetType[{}]", widgetType);
		if ( null != widgetType ) {
			for ( int i = 0 ; i < getWidgetValueSupported.length ; ++i ) {
				if ( 0 == widgetType.compareTo(getWidgetValueSupported[i]) ) {
					result = true;
					break;
				}
			}
		} else {
			logger.warn(function, "widgetType IS NULL");
		}
		return result;
  }
    
    @Override
    public String getWidgetValue(String element) {
    	final String function = "getWidgetValue";
    	logger.begin(function);
    	
    	String value = null;
    	
		int index = getElementIndex(WidgetAttribute.element, element);
		
		Widget w = this.widgets.get(index);
		
		if ( index >= 0 && index < this.widgets.size() ) {
			
			if ( null != w ) {
				
				Map<String, String> valueMap = this.values.get(index);
				String widget		= valueMap.get(WidgetAttribute.widget.toString());

				if ( WidgetType.Button.equalsName(widget) ) {
					value = ((Button)w).getText().toString();
				} else if ( WidgetType.RadioButton.equalsName(widget) ) {
					value = ((RadioButton)w).getValue().toString();
				} else if ( WidgetType.TextBox.equalsName(widget) ) {
					value = ((TextBox)w).getText();
				} else if ( WidgetType.TextArea.equalsName(widget) ) {
					value = ((TextArea)w).getText();
				} else if ( WidgetType.PasswordTextBox.equalsName(widget) ) {
					value = ((PasswordTextBox)w).getText();
				} else if ( WidgetType.InlineLabel.equalsName(widget) ) {
					value = ((InlineLabel)w).getText();
				} else if ( WidgetType.Label.equalsName(widget) ) {
					value = ((Label)w).getText();
				} else if ( WidgetType.Image.equalsName(widget) ) {
					value = ((Image)w).getUrl();
				} else if ( WidgetType.ListBox.equalsName(widget) ) {
					int selectedIndex = ((ListBox)w).getSelectedIndex();
					value = ((ListBox)w).getValue(selectedIndex);
					
					// Following function not support in GWT 1.6
//					value = ((ListBox)w).getSelectedValue();
//					value = ((ListBox)w).getSelectedItemText();
				} else {
					logger.warn(function, "WidgetType IS NULL");
				}
				
			} else {
				logger.warn(function, "widget IS NULL");
			}
		} else {
			logger.warn(function, "index IS INVALID index[{}] this.widgets.size()[{}]", index, this.widgets.size());
		}
    	
    	logger.end(function);
    	
    	return value;
    }
    
    private final String [] setWidgetValueSupported = new String[]{
    		  WidgetType.Button.toString(), WidgetType.RadioButton.toString()
    	  		, WidgetType.TextBox.toString(), WidgetType.TextArea.toString(), WidgetType.PasswordTextBox.toString()
    	  		, WidgetType.InlineLabel.toString(), WidgetType.Label.toString()
    	  		, WidgetType.Image.toString()
    	  		, WidgetType.ListBox.toString()};
    public boolean isSetWidgetValueSupported(String widgetType) {
		final String function = "setWidgetValueSupported";
		logger.begin(function);
		boolean result = false;
		logger.trace(function, "widgetType[{}]", widgetType);
		if ( null != widgetType ) {
			for ( int i = 0 ; i < setWidgetValueSupported.length ; ++i ) {
				if ( 0 == widgetType.compareTo(setWidgetValueSupported[i]) ) {
					result = true;
					break;
				}
			}
		} else {
			logger.warn(function, "widgetType IS NULL");
		}
		return result;
    }
    
	@Override
	public void setWidgetValue (String element, String value) {
		setWidgetValue (element, value, null);
	}
	
	@Override
	public void setWidgetValue (String element, String value, Object[] msgParam) {
		final String function = "setWidgetValue";
		if ( null != value ) logger.begin(function);
		if ( null != value ) logger.debug(function, "element[{}] value[{}]", element, value);

		int index = getElementIndex(WidgetAttribute.element, element);
		
		Widget w = this.widgets.get(index);
		
		if ( index >= 0 && index < this.widgets.size() ) {
			
			if ( null != w ) {
				
				Map<String, String> valueMap = this.values.get(index);
				String widget		= valueMap.get(WidgetAttribute.widget.toString());
				String media		= valueMap.get(WidgetAttribute.media.toString());
				String label		= valueMap.get(WidgetAttribute.label.toString());
				String format		= valueMap.get(WidgetAttribute.format.toString());
				
				String disableTranslation		= valueMap.get(WidgetAttribute.disableTranslation.toString());
				
				if ( null != value ) logger.trace(function, "index[{}] widget[{}] media[{}]", new Object[]{index, widget, media});

				if ( null != value ) label = value;
				
				if ( null != label ) {
					
					if ( null == disableTranslation || ( null != disableTranslation && ! disableTranslation.equalsIgnoreCase("true") )) {
						TranslationMgr translationMgr = TranslationMgr.getInstance();
						if ( null != translationMgr ) {
							if(null != msgParam) {
								label = translationMgr.getTranslation(label, msgParam);
							} else {
								label = translationMgr.getTranslation(label);
							}
						} else {
							logger.warn(function, "getTranslation IS NULL");
						}
					}
				}
				
				if ( WidgetMedia.DateTimeFormat.equalsName(media) && (null != format && 0 != format.length()) ) {
					label = DateTimeFormat.getFormat(format).format(new Date());
				}
				if ( WidgetType.Button.equalsName(widget) ) {
					((Button)w).setText(label);
				} else if ( WidgetType.RadioButton.equalsName(widget) ) {
					((RadioButton)w).setValue((label.equals("true")?true:false));
				} else if ( WidgetType.TextBox.equalsName(widget) ) {
					((TextBox)w).setText(label);
				} else if ( WidgetType.TextArea.equalsName(widget) ) {
					((TextArea)w).setText(label);
				} else if ( WidgetType.PasswordTextBox.equalsName(widget) ) {
					((PasswordTextBox)w).setText(label);
				} else if ( WidgetType.InlineLabel.equalsName(widget) ) {
					((InlineLabel)w).setText(label);
				} else if ( WidgetType.Label.equalsName(widget) ) {
					((Label)w).setText(label);
				} else if ( WidgetType.Image.equalsName(widget) ) {
					((Image)w).setUrl(label);
				} else if ( WidgetType.ListBox.equalsName(widget) ) {
					
					if ( value.equals("null") ) {
						// Clear all item in List Box
						((ListBox)w).clear();
					} else if ( value.equals("SetSelectedIndex_0") ) {
						((ListBox)w).setSelectedIndex(0);
					} else if ( value.equals("SetSelectedIndex_1") ) {
						((ListBox)w).setSelectedIndex(1);
					} else if ( value.equals("SetSelectedIndex_Last") ) {
						int iLastIndex = ((ListBox)w).getItemCount();
						iLastIndex = (iLastIndex>0?iLastIndex--:0);
						((ListBox)w).setSelectedIndex(iLastIndex);
					} else {
						// Insert into List Box
						((ListBox)w).addItem(label);
					}
				} else {
					logger.warn(function, "WidgetType IS NULL");
				}
			} else {
				logger.warn(function, "widget IS NULL");
			}
		} else {
			logger.warn(function, "index IS INVALID index[{}] this.widgets.size()[{}]", index, this.widgets.size());
		}
		
		if ( null != value ) logger.end(function);
	}
	
	public void ready(Dictionary dictionary) {
		final String function = "ready";
		logger.begin(function);
		logger.debug(function, "this.viewXMLFile[{}]", this.viewXMLFile);
		
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
				
				logger.debug(function, "dictionary cols[{}] rows[{}] => totals[{}]", new Object[]{cols, rows, totals});
				
				for ( int i = 0 ; i < totals ; ++i ) {
					values.put(i, new HashMap<String, String>());
				}				
				
				logger.debug(function, "dictionary ");

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
						
						logger.debug(function, "dictionary key[{}]", key);
						
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
									logger.trace(function, "dictionary row[{}] col[{}]", row, col);
									
									index = (row * cols) + col;
									
									logger.trace(function, "dictionary row[{}] col[{}] => index[{}]", new Object[]{row, col, index});
									
									Map<String, String> hashMap = this.values.get(Integer.valueOf(index));
									if ( null != hashMap ) {
										for ( Object o2 : d2.getValueKeys() ) {
											if ( null != o2 ) {
												String k = (String)o2;
												String v = (String)d2.getValue(o2);
												
												logger.trace(function, "dictionary k[{}] v[{}]", k, v);
				
												hashMap.put(k, v);
											}
										}
									} else {
										logger.warn(function, "row[{}] col[{}] => index[{}] Index NOT EXISTS", new Object[]{row, col, index});
									}
								} else {
									logger.warn(function, "keys[0][{}] OR keys[1][{}] is not a number", keys[0], keys[1]);
								}
							}
						} else {
							logger.warn(function, "key IS NULL");
						}
					}
				}
			}

		} else {
			logger.warn(function, "this.viewXMLFile[{}] dictionary IS NULL", this.viewXMLFile);
		}
		
		logger.end(function);
		
	}
	
	private Map<String, String> getWidgetValues(Widget widget) {
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
			Map<String, String> valueMap = this.values.get(i);
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
