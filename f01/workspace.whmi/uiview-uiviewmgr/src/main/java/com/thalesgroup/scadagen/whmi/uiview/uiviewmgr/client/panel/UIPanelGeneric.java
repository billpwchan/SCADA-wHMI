package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryCache;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIPanelGeneric {
	
	private static Logger logger = Logger.getLogger(UIPanelGeneric.class.getName());
	
	private static final String basePath				= GWT.getModuleBaseURL();
	
	public static final String strHeader 				= "header";
	public static final String strOption 				= "option";    
	public static final String strKey					= "key";
	
	public static final String strHorizontalPanel		= "HorizontalPanel";
	public static final String strVerticalPanel			= "VerticalPanel";
	public static final String strFlexTable				= "FlexTable";
	
	public static final String strInlineLabel			= "InlineLabel";
	public static final String strTextBox				= "TextBox";
	public static final String strPasswordTextBox		= "PasswordTextBox";
	public static final String strListBox				= "ListBox";
	public static final String strButton				= "Button";
	public static final String strImage					= "Image";
	public static final String strImageButton			= "ImageButton";
	public static final String strImageToggleButton		= "ImageToggleButton";
	
	//media
	public static final String strDateTimeFormat		= "DateTimeFormat";
	
	public static final String strWidget				= "widget";
    public static final String strLabel					= "label";
    public static final String strIcon					= "icon";
    public static final String strTooltip				= "tooltip";
    public static final String strCss					= "css";
    public static final String strElement				= "element";
    public static final String strFormat				= "format";
    public static final String strMedia					= "media";
    public static final String strReadOnly				= "readonly";
    public static final String strMaxLength				= "maxlength";
    public static final String strVisibleItemCount		= "visibleitemcount";
    public static final String strEnable				= "enable";
    
    public static final String strLabelDown				= "labelDown";
    public static final String strLabelDisable			= "labelDisable";
    
    public static final String strTooltipDown			= "tooltipDown";
    public static final String strTooltipDisable		= "tooltipDisable";
    
    public static final String strIconDown				= "iconDown";
    public static final String strIconDisable			= "iconDisable";
    
    public static final String strIconDivWidth			= "iconDivWidth";
	public static final String strIconDivHeight			= "iconDivHeight";
	public static final String strIconImgWidth			= "iconImgWidth";
	public static final String strIconImgHeight			= "iconImgHeight";
    
    public static final String strCssDown				= "cssDown";
    public static final String strCssDisable			= "cssDisable";
    
    public static final String strEnableDown			= "enableDown";
    public static final String strEnableDisable			= "enableDisable";
    
    public static final String strUp					= "up";
    public static final String strDown					= "down";
    public static final String strDisable				= "disable";
	
	private int rows	= 0;
	private int cols	= 0;
	private int totals	= 0;
	
    private VerticalPanel rootPanel;
    private String strCSSStatPanel;
    private String strRootWidget;

    private String strCSSFlexTable;
    
    private HashMap<Integer, HashMap<String, String>> values = new HashMap<Integer, HashMap<String, String>>();
    
    private HashMap<Integer, Widget> widgets = new HashMap<Integer, Widget>();
    
    private UIPanelGenericEvent uiPanelGenericEvent = null;
    public void setUIPanelGenericEvent(UIPanelGenericEvent uiPanelGenericEvent) { this.uiPanelGenericEvent = uiPanelGenericEvent; }
    
    public String [] getElementValues(String name) {
    	String elements [] = new String[this.values.size()];
		for( int i=0 ; i < this.values.size(); ++i ) {
			HashMap<String, String> valueMap = this.values.get(i);
			if ( null != valueMap ) {
				elements[i] = valueMap.get(name);
			} else {
				elements[i] = "";
			}
		}
		for( int i=0 ; i < values.size(); ++i ) {
			logger.log(Level.SEVERE, " **** getValues name["+i+"]["+elements[i]+"]");
		}
    	return elements;
    }
    
    private String xmlFile;

    private Dictionary dictionaryHeader = null;
    private Dictionary dictionaryOption = null;
    
    public void init(String xmlFile) {
    	
    	this.xmlFile = xmlFile;
    	
		DictionaryCache uiPanelSettingCache = DictionaryCache.getInstance();
		
		this.dictionaryHeader = uiPanelSettingCache.getDictionary( this.xmlFile, strHeader );
		ready(this.dictionaryHeader);
		
		this.dictionaryOption = uiPanelSettingCache.getDictionary( this.xmlFile, strOption );
		ready(this.dictionaryOption);
    	
    }
    
    public String getWidgetName(Widget widget) {
    	
    	logger.log(Level.SEVERE, "getWidgetName Begin");
    	
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
    						element = hashMap.get(strElement);
    						logger.log(Level.SEVERE, "getWidgetName key["+key+"] element["+element+"]");
    						break;
    					} else {
    						logger.log(Level.SEVERE, "getWidgetName hashMap at key["+key+"] IS NULL");
    					}
    				}
    			}
    		}
    	}
    	
    	logger.log(Level.SEVERE, "getWidgetName End");
    	
		return element;
    	
    }

    public Widget getWidget(String element) {
    	
    	logger.log(Level.SEVERE, "getWidget Begin");
    	
    	logger.log(Level.SEVERE, "getWidget element["+element+"]");
    	
    	Widget widget = null;
    	Set<Integer> keys = this.values.keySet();
    	Iterator<Integer> iter = keys.iterator();
    	while ( iter.hasNext() ) {
    		Integer integer = iter.next();
    		if ( null != integer ) {
	    		HashMap<String, String> valueMap = this.values.get(integer);
	    		if ( null != valueMap ) {
	 				String elementValue = valueMap.get(strElement);
					if ( elementValue != null && 0 == elementValue.compareTo(element) ) {
						widget = widgets.get(integer);
						break;
					}
	    		}
    		}
    	}
    	
    	if ( null == widget ) {
    		logger.log(Level.SEVERE, "getWidget widget IS NULL");
    	}
    	
    	logger.log(Level.SEVERE, "getWidget End");
    	
		return widget;
    }
    
    public String getWidgetStatus ( String element ) {
    	logger.log(Level.SEVERE, " **** setWidgetStatus Begin");
    	
    	logger.log(Level.SEVERE, " **** setWidgetStatus element["+element+"]");
    	
    	String status = null;
    	
    	status = getWidgetStatus(getWidget(element));
    	
    	logger.log(Level.SEVERE, " **** setWidgetStatus status["+status+"]");
    	
    	logger.log(Level.SEVERE, " **** setWidgetStatus End");
    	
    	return status;
    }
    
    public String getWidgetStatus ( Widget widget ) {
    	
    	logger.log(Level.SEVERE, " **** getWidgetStatus Begin");
    	
    	logger.log(Level.SEVERE, " **** getWidgetStatus widget["+widget+"]");
    	
    	String status = null;
    	if ( null != widget ) {
			HashMap<String, String> valueMap 	= getWidgetValues(widget);
			
			String strWidget					= valueMap.get(UIPanelGeneric.strWidget);
			
			String label		= valueMap.get(UIPanelGeneric.strLabel);
			String icon			= valueMap.get(UIPanelGeneric.strIcon);
			String labelDown	= valueMap.get(UIPanelGeneric.strLabelDown);
			String iconDown		= valueMap.get(UIPanelGeneric.strIconDown);
			String labelDisable	= valueMap.get(UIPanelGeneric.strLabelDisable);
			String iconDisable	= valueMap.get(UIPanelGeneric.strIconDisable);
			
			if ( 0 == strWidget.compareTo(strImageButton) || 0 == strWidget.compareTo(strImageToggleButton) ) {
				
				String html = ((Button)widget).getHTML();
				
				logger.log(Level.SEVERE, " **** getWidgetStatus html["+html+"]");
				
				if ( null != label ) {
					if ( null != labelDisable && html.indexOf(labelDisable) != -1 ) {
						status = strDisable;
					} else if ( null != labelDown && html.indexOf(labelDown) != -1 ) {
						status = strDown;
					} else if ( null != label && html.indexOf(label) != -1 ) {
						status = strUp;
					}  
				} else if ( null != icon ) {
					if ( null != iconDisable && html.indexOf(iconDisable) != -1 ) {
						status = strDisable;
					} else if ( null != iconDown && html.indexOf(iconDown) != -1 ) {
						status = strDown;
					} else  if ( null != icon && html.indexOf(icon) != -1 ) {
						status = strUp;
					} 
				}
			}
			
    	} else {
    		
    		logger.log(Level.SEVERE, " **** setWidgetStatus widget IS NULL");
    		
    	}
    	
    	logger.log(Level.SEVERE, " **** getWidgetStatus status["+status+"]");
    	
    	logger.log(Level.SEVERE, " **** getWidgetStatus End");
    	
    	return status;
    }
    
    public void setWidgetStatus ( String element, String status ) {
    	
    	logger.log(Level.SEVERE, " **** setWidgetStatus Begin");
    	
    	logger.log(Level.SEVERE, " **** setWidgetStatus element["+element+"] status["+status+"]");
    	
    	setWidgetStatus(getWidget(element), status);
    	
    	logger.log(Level.SEVERE, " **** setWidgetStatus End");
    }
    
    public void setWidgetStatus ( Widget widget, String status ) {
    	
    	logger.log(Level.SEVERE, " **** setWidgetStatus Begin");
    	
    	logger.log(Level.SEVERE, " **** setWidgetStatus widget["+widget+"] status["+status+"]");
    	
    	if ( null != widget ) {
    		 
    		if ( null != status ) {
    			
    			HashMap<String, String> valueMap 	= getWidgetValues(widget);
    					
				String strWidget					= valueMap.get(UIPanelGeneric.strWidget);
				String iconDivWidth					= valueMap.get(UIPanelGeneric.strIconDivWidth);
				String iconDivHeight				= valueMap.get(UIPanelGeneric.strIconDivHeight);
				String iconImgWidth					= valueMap.get(UIPanelGeneric.strIconImgWidth);
				String iconImgHeight				= valueMap.get(UIPanelGeneric.strIconImgHeight);
				
				String label	= null;
				String icon		= null;
				String toolTip	= null;
				String css		= null;
				String enable	= null;
    			
				if ( 0 == strWidget.compareTo(strImageButton) || 0 == strWidget.compareTo(strImageToggleButton) ) {
					if ( 0 == status.compareTo(strUp) ) {
						label		= valueMap.get(UIPanelGeneric.strLabel);
						toolTip		= valueMap.get(UIPanelGeneric.strTooltip);
						icon		= valueMap.get(UIPanelGeneric.strIcon);
						css			= valueMap.get(UIPanelGeneric.strCss);
						enable		= valueMap.get(UIPanelGeneric.strEnable);
					} else if ( 0 == status.compareTo(strDown) ) {
						label		= valueMap.get(UIPanelGeneric.strLabelDown);
						toolTip		= valueMap.get(UIPanelGeneric.strTooltipDown);
						icon		= valueMap.get(UIPanelGeneric.strIconDown);
						css			= valueMap.get(UIPanelGeneric.strCssDown);
						enable		= valueMap.get(UIPanelGeneric.strEnableDown);
					} else if ( 0 == status.compareTo(strDisable) ) {
						label		= valueMap.get(UIPanelGeneric.strLabelDisable);
						toolTip		= valueMap.get(UIPanelGeneric.strTooltipDisable);
						icon		= valueMap.get(UIPanelGeneric.strIconDisable);
						css			= valueMap.get(UIPanelGeneric.strCssDisable);
						enable		= valueMap.get(UIPanelGeneric.strEnableDisable);
					}
					
					if ( null != label )	((Button)widget).setText(label);
//					if ( null != icon ) 	((Button)widget).setHTML("<div width=\"90px\" height=\"32px\"><center><img src=\""+basePath+icon+"\" width=\"32px\" height=\"32px\"></center></br></div>");
					
					if ( null != iconDivWidth && null != iconDivHeight ) {
						String img = null;
						String lbl = null;
						if ( null != icon && null != iconImgHeight && null != iconImgHeight ) {
							img = "<img src=\""+basePath+icon+"\" width=\""+iconImgHeight+"\" height=\""+iconImgHeight+"\">";
						}
						if ( null != label ) {
							lbl = "<label>"+label+"</lable>";
						}
						String html = "<div width=\""+iconDivWidth+"\" height=\""+iconDivHeight+"\"><center>"+(null==img?"":img)+(null==lbl?"":lbl)+"</center></br></div>";
						
						logger.log(Level.FINE, "setWidgetStatus html["+html+"]");
						
						((Button)widget).setHTML(html);
					}
					
					if ( null != toolTip )	((Button)widget).setTitle(toolTip);
					
					if ( null != css ) {
						((Button)widget).removeStyleName(css);
						((Button)widget).addStyleName(css);
						
						logger.log(Level.FINE, "setSplitButton addStyleName["+css+"] removeStyleName["+css+"]");
					}
					
					if ( null != enable )	((Button)widget).setEnabled(0==enable.compareToIgnoreCase("true"));
	    			
				}
    		} else {
    			logger.log(Level.SEVERE, " **** setWidgetStatus status IS NULL");
    		}
    		
    		
    	} else {
    		
    		logger.log(Level.SEVERE, " **** setWidgetStatus widget IS NULL");
    		
    	}
    	
    	logger.log(Level.SEVERE, " **** setWidgetStatus End");
    }
    
	public void setValue (String element) {
    	
    	logger.log(Level.SEVERE, " **** updateValue Begin");
    	
    	logger.log(Level.SEVERE, " **** updateValue name["+element+"]");
    	
    	setValue(element, null);
    	
    	logger.log(Level.SEVERE, " **** updateValue End");
    	
    }
	
	public void setValue (String element, String value) {
		
		logger.log(Level.SEVERE, " **** updateValue Begin");
		
		logger.log(Level.SEVERE, " **** updateValue name["+element+"] value["+value+"]");

		int index = getElementIndex(UIPanelGeneric.strElement, element);
		
		Widget w = this.widgets.get(index);
		
		if ( index >= 0 && index < this.widgets.size() ) {
			
			if ( null != w ) {
				
				HashMap<String, String> valueMap = this.values.get(index);
				String widget		= valueMap.get(UIPanelGeneric.strWidget);
				String media		= valueMap.get(UIPanelGeneric.strMedia);
				String label		= valueMap.get(UIPanelGeneric.strLabel);
				String format		= valueMap.get(UIPanelGeneric.strFormat);
				
				logger.log(Level.SEVERE, "updateValue index["+index+"] widget["+widget+"] media["+media+"]");

				if ( null != value ) label = value;
				
				if ( 0 == strDateTimeFormat.compareTo(media) && (null != format && 0 != format.length()) ) {
					label = DateTimeFormat.getFormat(format).format(new Date());
				}
				
				if ( 0 == widget.compareTo(strTextBox) ) {
					((TextBox)w).setText(label);
				} else if ( 0 == widget.compareTo(strInlineLabel) ) {
					((InlineLabel)w).setText(label);
				} else if ( 0 == widget.compareTo(strImage) ) {
					((Image)w).setUrl(label);
				}
				
				
			} else {
				logger.log(Level.SEVERE, " **** updateValue widget IS NULL");
			}
		} else {
			logger.log(Level.SEVERE, " **** updateValue index IS INVALID index["+index+"] this.widgets.size()["+this.widgets.size()+"]");
		}
		
		logger.log(Level.SEVERE, " **** updateValue End");
	}
	
	public void ready(Dictionary dictionary) {
		logger.log(Level.SEVERE, "ready Begin");
		
		if ( null != dictionary ) {
			String xmlFile				= (String)dictionary.getAttribute(DictionaryCache.strXmlFile);
			String XmlTag				= (String)dictionary.getAttribute(DictionaryCache.strXmlTag);
			String CreateDateTimeLabel	= (String)dictionary.getAttribute(DictionaryCache.strCreateDateTimeLabel);
			
			logger.log(Level.SEVERE, "ready dictionary XmlFile["+xmlFile+"]");
			logger.log(Level.SEVERE, "ready dictionary XmlTag["+XmlTag+"]");
			logger.log(Level.SEVERE, "ready dictionary CreateDateTimeLabel["+CreateDateTimeLabel+"]");			
			
			if (0 == XmlTag.compareTo(strHeader) ) {

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
								if ( 0 == k.compareTo("rows") ) {
									rows = Integer.parseInt(v);
								} else if ( 0 == k.compareTo("cols") ) {
									cols = Integer.parseInt(v);
								} else if ( 0 == k.compareTo("cssVerticalPanel") ) {
									strCSSStatPanel = v;
								} else if ( 0 == k.compareTo("cssFlexTable") ) {
									strCSSFlexTable = v;
								} else if ( 0 == k.compareTo("widget") ) {
									strRootWidget = v;
								}
								// Get Header End								
							}
						}
					}
				}
				
				totals = rows * cols;
				
				logger.log(Level.SEVERE, "ready dictionary cols["+cols+"] rows["+rows+"] => totals["+totals+"]");
				
				for ( int i = 0 ; i < totals ; ++i ) {
					values.put(i, new HashMap<String, String>());
				}				
				
				logger.log(Level.SEVERE, "ready dictionary ");

			} else if (0 == XmlTag.compareTo(strOption) ) {
				
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
								if ( 0 == k.compareTo(strKey) ) {
									
									key = v;
									
									if ( null != key) {
										keys = v.split("\\|");
										
										logger.log(Level.SEVERE, "ready dictionary key["+key+"]");
										
										break;
									}
									
								}
								// Get Header End
							}
						}
						
						logger.log(Level.SEVERE, "ready dictionary key["+key+"]");
						
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
									logger.log(Level.SEVERE, "ready dictionary row["+row+"] col["+col+"]");
									
									index = (row * cols) + col;
									
									logger.log(Level.SEVERE, "ready dictionary row["+row+"] col["+col+"] => index["+index+"]");
									
									HashMap<String, String> hashMap = this.values.get(Integer.valueOf(index));
									if ( null != hashMap ) {
										for ( Object o2 : d2.getValueKeys() ) {
											if ( null != o2 ) {
												String k = (String)o2;
												String v = (String)d2.getValue(o2);
												
												logger.log(Level.SEVERE, "ready dictionary k["+k+"] v["+v+"]");
				
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
			logger.log(Level.SEVERE, "ready dictionary IS NULL");
		}
		
		logger.log(Level.SEVERE, "ready End");
		
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
	
	private int getWidgetIndex(Widget widget) {
		int index = -1;
		Set<Integer> keys = this.widgets.keySet();
		Iterator<Integer> iter = keys.iterator();
		while ( iter.hasNext() ) {
			Integer key = iter.next();
			if ( key != null ) {
				if ( widget == this.widgets.get(key) ) {
					index = key.intValue();
					break;
				}
			}
		}
		return index;
	}
    
    private int getElementIndex(String strElement, String strElementValue) {
		int index = -1;
		for( int i=0 ; i < values.size(); i++ ) {
			HashMap<String, String> valueMap = this.values.get(i);
			if ( null != valueMap ) {
				String element = valueMap.get(strElement);
				if ( element != null && 0 == strElementValue.compareTo(element) ) {
					index = i;
					break;
				}
			}
		}
		return index;
    }
	
    private UINameCard uiNameCard;
	public VerticalPanel getMainPanel (UINameCard uiNameCard) {
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		logger.log(Level.SEVERE, "getMainPanel Begin");
		
		rootPanel = new VerticalPanel();
		
		if ( null == dictionaryHeader || null == dictionaryOption ) {
			
			if ( null == dictionaryHeader ) rootPanel.add( new InlineLabel( "Faild to load xmlFile["+this.xmlFile+"] strHeader["+strHeader+"]" ));
			
			if ( null == dictionaryOption ) rootPanel.add( new InlineLabel( "Faild to load xmlFile["+this.xmlFile+"] strOption["+strOption+"]" ));
			
		} else {
			
			logger.log(Level.SEVERE, "getMainPanel appling root panel css strCSSStatPanel["+strCSSStatPanel+"]");
			
		    rootPanel.addStyleName(strCSSStatPanel);
		    rootPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		    rootPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

	    	FlexTable flexTable = null;
	    	
	    	HorizontalPanel horizontalPanel = null;
	    	
	    	VerticalPanel verticalPanel = null;
	    	
	    	logger.log(Level.SEVERE, "getMainPanel strRootWidget["+strRootWidget+"]");
	    	
	    	if ( 0 == strRootWidget.compareTo(strHorizontalPanel) ) {
	    		
	    		logger.log(Level.SEVERE, "getMainPanel appling sub panel is ["+strHorizontalPanel+"]");
	    		
	    		horizontalPanel = new HorizontalPanel();
	    		if ( null != strCSSFlexTable )	horizontalPanel.addStyleName(strCSSFlexTable);
	    		
	    	} else if ( 0 == strRootWidget.compareTo(strFlexTable) ) {
	    		
	    		logger.log(Level.SEVERE, "getMainPanel appling sub panel is ["+strFlexTable+"]");
	    	
	    		flexTable = new FlexTable();
	    		if ( null != strCSSFlexTable )	flexTable.addStyleName(strCSSFlexTable);
	    		
	    	} else if ( 0 == strRootWidget.compareTo(strVerticalPanel) ) {
	    		
	    		logger.log(Level.SEVERE, "getMainPanel appling sub panel is ["+strVerticalPanel+"]");
	    	
	    		verticalPanel = new VerticalPanel();
	    		if ( null != strCSSFlexTable )	verticalPanel.addStyleName(strCSSFlexTable);
	    	}
			
		    for ( int i = 0 ; i < rows ; ++i ) {
		    	
				logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] Begin");
				
				for ( int j = 0 ; j < cols ; ++j ) {
					
					int index = (i*cols)+j;
					
					logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] => index["+index+"]");
					
					if ( index < values.size() ) {
						
						Widget w = null;
						
						HashMap<String, String> valueMap = this.values.get(index);
						String widget			= valueMap.get(UIPanelGeneric.strWidget);
						String label			= valueMap.get(UIPanelGeneric.strLabel);
						String tooltip			= valueMap.get(UIPanelGeneric.strTooltip);
						String css				= valueMap.get(UIPanelGeneric.strCss);
						String readonly			= valueMap.get(UIPanelGeneric.strReadOnly);
						String maxlength		= valueMap.get(UIPanelGeneric.strMaxLength);
						String visibleitemcount	= valueMap.get(UIPanelGeneric.strVisibleItemCount);
						
						String icon				= valueMap.get(UIPanelGeneric.strIcon);
						String iconDown			= valueMap.get(UIPanelGeneric.strIconDown);
						String iconDisable		= valueMap.get(UIPanelGeneric.strIconDisable);
						
						String iconDivWidth		= valueMap.get(UIPanelGeneric.strIconDivWidth);
						String iconDivHeight	= valueMap.get(UIPanelGeneric.strIconDivHeight);
						String iconImgWidth		= valueMap.get(UIPanelGeneric.strIconImgWidth);
						String iconImgHeight	= valueMap.get(UIPanelGeneric.strIconImgHeight);
						
						String cssDown			= valueMap.get(UIPanelGeneric.strCssDown);
						String cssDisable		= valueMap.get(UIPanelGeneric.strCssDisable);
						
						logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] widget["+widget+"]");
						logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] label["+label+"]");
						logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] css["+css+"]");
						
						if ( null == widget || 0 == widget.length() || 0 == widget.compareTo(strTextBox) ) {
							w = new TextBox();
							
							if ( null != label )	((TextBox)w).setText(label);
							if ( null != tooltip )	((TextBox)w).setTitle(tooltip);
							if ( null != css )		((TextBox)w).addStyleName(css);
							((TextBox)w).addKeyPressHandler(new KeyPressHandler() {
								@Override
								public void onKeyPress(KeyPressEvent event) {
									if ( null != uiPanelGenericEvent ) {
										uiPanelGenericEvent.onKeyPressHandler(event);
									}
								}
							});
							
							if ( null != maxlength && 0 == maxlength.compareTo(strMaxLength) && 0 < maxlength.length() )
								((TextBox)w).setMaxLength(Integer.parseInt(maxlength));
							
							if ( null != readonly && 0 == readonly.compareTo(strReadOnly) && 0 < readonly.length())
								((TextBox)w).setReadOnly(true);
							
							this.widgets.put(index, w);
						} else if ( 0 == widget.compareTo(strPasswordTextBox) ) {
							
							w = new PasswordTextBox();
							
							if ( null != label )	((PasswordTextBox)w).setText(label);
							if ( null != css )		((PasswordTextBox)w).addStyleName(css);
							
							if ( null != maxlength && 0 == maxlength.compareTo(strMaxLength) && 0 < maxlength.length() )
								((PasswordTextBox)w).setMaxLength(Integer.parseInt(maxlength));
							
							if ( null != readonly && 0 == readonly.compareTo(strReadOnly) && 0 < readonly.length() )
								((PasswordTextBox)w).setReadOnly(true);
							
							this.widgets.put(index, w);
						} else if ( 0 == widget.compareTo(strInlineLabel) ) {
							
							w = new InlineLabel();
							
							if ( null != label )	((InlineLabel)w).setText(label);
							if ( null != tooltip )	((InlineLabel)w).setTitle(tooltip);
							if ( null != css )		((InlineLabel)w).addStyleName(css);

							this.widgets.put(index, w);
						} else if ( 0 == widget.compareTo(strButton) ) {
							
							w = new Button();
							
							if ( null != label )	((Button)w).setText(label);
							if ( null != tooltip )	((Button)w).setTitle(tooltip);
							if ( null != css )		((Button)w).addStyleName(css);
							
							((Button)w).addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									if ( null != uiPanelGenericEvent ) {
										uiPanelGenericEvent.onClickHandler(event);
									}
								}
							});

							this.widgets.put(index, w);
						} else if ( 0 == widget.compareTo(strImageButton) || 0 == widget.compareTo(strImageToggleButton) ) {
							
							w = new Button();

							if ( null != tooltip )	((Button)w).setTitle(tooltip);
							
							if ( null != iconDivWidth && null != iconDivHeight ) {
								String img = null;
								String lbl = null;
								if ( null != icon && null != iconImgHeight && null != iconImgHeight ) {
									img = "<img src=\""+basePath+icon+"\" width=\""+iconImgHeight+"\" height=\""+iconImgHeight+"\">";
								}
								if ( null != label ) {
									lbl = "<label>"+label+"</label>";
								}
								String html = "<div width=\""+iconDivWidth+"\" height=\""+iconDivHeight+"\"><center>"+(null==img?"":img)+(null==lbl?"":lbl)+"</center></br></div>";
								
								logger.log(Level.FINE, "getMainPanel html["+html+"]");
								
								((Button)w).setHTML(html);
							}
							
							this.widgets.put(index, w);
						} else if ( 0 == widget.compareTo(strImage) ) {
							w = new Image();
							if ( null != tooltip )	((Image)w).setTitle(tooltip);
							if ( null != label )	((Image)w).setUrl(basePath+label);
							if ( null != css )		((Image)w).addStyleName(css);
							
							this.widgets.put(index, w);
						} else if ( 0 == widget.compareTo(strListBox) ) {
							
							w = new ListBox();
							if ( null != label )	((ListBox)w).addItem(label);
							if ( null != css )		((ListBox)w).addStyleName(css);
							
							if ( null != visibleitemcount && 0 == visibleitemcount.compareTo(strVisibleItemCount) && 0 < visibleitemcount.length() ) {
								((ListBox)w).setVisibleItemCount(Integer.parseInt(visibleitemcount));
							}
							
							this.widgets.put(index, w);
						} 
						if ( null != w ) {
							if ( 0 == strRootWidget.compareTo(strHorizontalPanel) ) {
								horizontalPanel.add(w);
							} else if ( 0 == strRootWidget.compareTo(strVerticalPanel) ) {
								verticalPanel.add(w);
							} else if ( 0 == strRootWidget.compareTo(strFlexTable) ) {	
								flexTable.setWidget(i, j, w);
							}
						}
					} else {
						logger.log(Level.SEVERE, "getMainPanel Build Filter INVALID index["+index +"] > values.length["+values.size()+"]");
					}
				}
				logger.log(Level.SEVERE, "getMainPanel Build Filter Table Loop i["+i+"] End");
		    }
		    
		    if ( 0 == strRootWidget.compareTo(strFlexTable) ) {
		    	for ( int j = 0 ; j < cols ; ++j ) {
		    		flexTable.getColumnFormatter().setWidth(j, "40px");
		    	}
		    }
		    
		    if ( 0 == strRootWidget.compareTo(strHorizontalPanel) ) {
		    	rootPanel.add(horizontalPanel);
		    } else if ( 0 == strRootWidget.compareTo(strFlexTable) ) {
		    	rootPanel.add(flexTable);
			}
		
	    }
		
		logger.log(Level.SEVERE, "getMainPanel End");
		
		return rootPanel;
		
	}

}
