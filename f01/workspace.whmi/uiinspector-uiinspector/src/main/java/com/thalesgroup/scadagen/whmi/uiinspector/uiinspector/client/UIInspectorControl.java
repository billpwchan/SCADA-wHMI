package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Observer;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Subject;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIInspectorControl implements UIInspectorTab_i, IClientLifeCycle {
	
	private static Logger logger = Logger.getLogger(UIInspectorControl.class.getName());
	
	private String tagname			= "control";
	
	private String strLabel			= ".label";
	private String strValueTable	= ".valueTable";
	
	// Static Attribute List
	private String staticAttibutes[]	= new String[] {strLabel};
	
	// Static Attribute List
	private String dioStaticAttibutes[]	= new String[] {strLabel, strValueTable};

	// Dynamic Attribute List
	private String dynamicAttibutes[]	= new String[] {};
	
	private String dio = "dio";
	private String aio = "aio";
	private String sio = "sio";
		
	private LinkedList<String> dios = new LinkedList<String>();
	private LinkedList<String> aios = new LinkedList<String>();
	private LinkedList<String> sios = new LinkedList<String>();	

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String parent) {
		this.parent = parent;
		logger.log(Level.FINE, "setParent this.parent["+this.parent+"]");
	}
	
	@Override
	public void setAddresses(String scsEnvId, String[] addresses, String period) {
		logger.log(Level.FINE, "setAddresses Begin");
		
		this.scsEnvId = scsEnvId;
		
		logger.log(Level.FINE, "setAddresses this.scsEnvId["+this.scsEnvId+"]");
		
		this.addresses = addresses;
		
		logger.log(Level.FINE, "setAddresses End");
	}
	
	private void updatePager() {
		
		logger.log(Level.FINE, "updatePager Begin");
		
		pageCounter.calc(pageIndex);
				
		if ( pageCounter.hasPreview || pageCounter.hasNext ) {
			btnUp.setEnabled(pageCounter.hasPreview);
			lblPageNum.setText(String.valueOf(pageIndex+1) + " / " + String.valueOf(pageCounter.pageCount));
			btnDown.setEnabled(pageCounter.hasNext);
		} else {
			btnUp.setVisible(false);
			lblPageNum.setVisible(false);
			btnDown.setVisible(false);
		}
		
		logger.log(Level.FINE, "updatePager End");
	}

	@Override
	public void buildWidgets() {
		buildWidgets(this.addresses.length);
	}
	
	private int pageIndex = 0;
	private PageCounter pageCounter = null;
	
	private VerticalPanel[] widgetBoxes = null;
	private InlineLabel[] inlineLabels = null;
	private HorizontalPanel[] controlboxes = null;
	private void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.FINE, "buildWidgets Begin");
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, 10);
			pageCounter.calc(pageIndex);
			
			updatePager();
			
			int numOfWidgetShow = pageCounter.pageRowCount;
			
			if ( null != this.addresses ) {
				
				String btnWidth = "90px";
				String btnHeight = "26px";
				
				widgetBoxes		= new VerticalPanel[numOfWidgetShow];
				inlineLabels	= new InlineLabel[numOfWidgetShow];
				controlboxes	= new HorizontalPanel[numOfWidgetShow];
				
				int numOfCtrlBtnRow = 4;
				for(int x=0;x<numOfWidgetShow;++x){
					widgetBoxes[x] = new VerticalPanel();
					widgetBoxes[x].setWidth("100%");
					widgetBoxes[x].getElement().getStyle().setPadding(5, Unit.PX);
					inlineLabels[x] = new InlineLabel();
					inlineLabels[x].addStyleName("project-gwt-label-inspector-"+tagname+"-control");
					inlineLabels[x].setText("Control: "+(x+1));
					widgetBoxes[x].add(inlineLabels[x]);
					
					controlboxes[x] = new HorizontalPanel();
					for(int y=1;y<=numOfCtrlBtnRow;++y){
						Button btnCtrl = new Button();
						btnCtrl.setText("Control "+y);
						btnCtrl.setWidth(btnWidth);
						btnCtrl.setHeight(btnHeight);
						btnCtrl.addStyleName("project-gwt-button-inspector-"+tagname+"-button");
						controlboxes[x].add(btnCtrl);
					}
					widgetBoxes[x].add(controlboxes[x]);
					
					vpCtrls.add(widgetBoxes[x]);
				}
			} else {
				logger.log(Level.SEVERE, "buildWidgets this.pointStatics IS NULL");
			}
		} else {
			logger.log(Level.SEVERE, "buildWidgets points IS NULL");
		}
		
		logger.log(Level.FINE, "buildWidgets End");
	}
	
	public void buildWidgetList() {
		
		logger.log(Level.FINE, "buildWidgetList Begin");
		
		for ( int i = 0 ; i < addresses.length ; ++i ) {
			String dbaddress = addresses[i];
			if ( null != dbaddress ) {
				String point = RTDB_Helper.getPoint(dbaddress);
				if ( null != point ) {
					if ( point.startsWith(dio) ) {
						dios.add(dbaddress);
						continue;
					}
					if ( point.endsWith(aio) ) {
						aios.add(dbaddress);
						continue;
					}
					if ( point.startsWith(sio) ) {
						sios.add(dbaddress);
						continue;
					}
				}
			} else {
				logger.log(Level.FINE, "buildWidgetList dbaddress IS NULL");
			}
		}
		
		logger.log(Level.FINE, "buildWidgetList End");
		
	}
	
	class ControlPoint {
		private HashMap<String, String> hashmap = new HashMap<String, String>();
		public ControlPoint(String point, String scsenvid, String dbaddress) {
			hashmap.put("point", point);
			hashmap.put("scsenvid", scsenvid);
			hashmap.put("dbaddress", dbaddress);
		}
		public ControlPoint(String point, String scsenvid, String dbaddress, String value) {
			this( point, scsenvid, dbaddress);
			hashmap.put("value", value);
		}
		public String getValue(String key) {
			return hashmap.get(key);
		}
	}
	
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesStatic	= new LinkedHashMap<String, HashMap<String, String>>();
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, HashMap<String, String>>();
	private HashMap<Widget, ControlPoint> widgetPoints = new HashMap<Widget, ControlPoint>();
	private HashMap<String, Widget[]> widgetGroups = new HashMap<String, Widget[]>();
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.FINE, "updateValue Begin");
		logger.log(Level.FINE, "updateValue clientkey["+clientKey+"]");
		
		if ( 0 == "static".compareTo(clientKey.split("_")[2]) ) {
			
			keyAndValuesStatic.put(clientKey, keyAndValue);
			
			updateValue(true);
			
		} else if ( 0 == "dynamic".compareTo(clientKey.split("_")[2]) ) {
			
			keyAndValuesDynamic.put(clientKey, keyAndValue);
			
			updateValue(false);
		}
		
		logger.log(Level.FINE, "updateValue End");
	}
	
	private void updateValue(boolean withStatic) {
		
		logger.log(Level.FINE, "updateValue Begin");
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;
		
		if ( withStatic ) {
			for ( String clientKey : keyAndValuesStatic.keySet() ) {
					
				HashMap<String, String> keyAndValue = keyAndValuesStatic.get(clientKey);
				
				String clientKey_multiReadValue_inspectorcontrol_static = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;
				if ( 0 == clientKey_multiReadValue_inspectorcontrol_static.compareTo(clientKey) ) {
					
					widgetPoints.clear();
					widgetGroups.clear();
					
					for ( int z = 0 ; z < dios.size() ; ++z ) {
						
						String dioaddress = dios.get(z);
						
						String diolabeldbaddress = dioaddress + strLabel;
						
						logger.log(Level.FINE, "updateValue diolabeldbaddress["+diolabeldbaddress+"]");
						
						if ( keyAndValue.containsKey(diolabeldbaddress) ) {
							for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
								String dbaddress = addresses[x];
								
								logger.log(Level.FINE, "updateValue diolabeldbaddress dbaddress["+diolabeldbaddress+"]");
								
								if ( diolabeldbaddress.startsWith(dbaddress) ) {
									String value = keyAndValue.get(diolabeldbaddress);
									value = RTDB_Helper.removeDBStringWrapper(value);
									
									logger.log(Level.FINE, "updateValue value["+value+"]");
									
									inlineLabels[y].setText(value);
								}
							}
						}
						
						
						String diovaluetabledbaddress = dioaddress + strValueTable;
						
						logger.log(Level.FINE, "updateValue siodbaddress["+diovaluetabledbaddress+"]");
						
						int dovnameCol = 0, labelCol = 1, valueCol = 2;
						if ( keyAndValue.containsKey(diovaluetabledbaddress) ) {
							for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
								String dbaddress = addresses[x];
								
								logger.log(Level.FINE, "updateValue siodbaddress dbaddress["+diovaluetabledbaddress+"]");
								
								if ( diovaluetabledbaddress.startsWith(dbaddress) ) {
									
									controlboxes[y].clear();
									
									String value = keyAndValue.get(diovaluetabledbaddress);

									{
										int numOfRow = 12;
										String points[] = new String[numOfRow];
										String labels[] = new String[numOfRow];
										String values[] = new String[numOfRow];
										for( int r = 0 ; r < numOfRow ; ++r ) {
											
											points[r] = RTDB_Helper.getArrayValues(value, dovnameCol, r );
											points[r] = RTDB_Helper.removeDBStringWrapper(points[r]);
											
											labels[r] = RTDB_Helper.getArrayValues(value, labelCol, r );
											labels[r] = RTDB_Helper.removeDBStringWrapper(labels[r]);
											
											values[r] = RTDB_Helper.getArrayValues(value, valueCol, r );
											values[r] = RTDB_Helper.removeDBStringWrapper(values[r]);					
										}
										
										LinkedList<UIButtonToggle> btnOptions = new LinkedList<UIButtonToggle>();
										for ( int i = 0 ; i < points.length ; ++i ) {
											
											if ( labels[i].length() == 0  ) break;
											
											UIButtonToggle btnOption = new UIButtonToggle(labels[i]);
											btnOption.addStyleName("project-gwt-button-inspector-"+tagname+"-ctrl");
											btnOption.addClickHandler(new ClickHandler() {
												
												@Override
												public void onClick(ClickEvent event) {
													onButton(event);
												}
											});
											
											btnOptions.add(btnOption);
											
											controlboxes[y].add(btnOption);
											
											widgetGroups.put(dioaddress, btnOptions.toArray(new UIButtonToggle[0]));
											
											widgetPoints.put(btnOption, new ControlPoint("dio", scsEnvId, dbaddress, values[i]));

										}
									}
								}
							}
						}
						
					}
					
					for ( int z = 0 ; z < aios.size() ; ++z ) {
						String aioaddress = aios.get(z);
						String aiodbaddresslabel = aioaddress + strLabel;
						
						logger.log(Level.FINE, "updateValue aiodbaddress["+aiodbaddresslabel+"]");
						
						if ( keyAndValue.containsKey(aiodbaddresslabel) ) {
							for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
								String dbaddress = addresses[x];
								
								logger.log(Level.FINE, "updateValue aiodbaddress dbaddress["+dbaddress+"]");
								
								if ( aiodbaddresslabel.startsWith(dbaddress) ) {
									String label = keyAndValue.get(aiodbaddresslabel);
									label = RTDB_Helper.removeDBStringWrapper(label);
									
									logger.log(Level.FINE, "updateValue label["+label+"]");
									
									inlineLabels[y].setText(label);
									
									// AI and SI Value Box
									TextBox textBox = new TextBox();
									textBox.setWidth("250px");							
									
									controlboxes[y].clear();
									
									controlboxes[y].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
									
									controlboxes[y].add(textBox);
									
									widgetGroups.put(aioaddress, new TextBox[]{textBox});
									
									widgetPoints.put(textBox, new ControlPoint("aio", scsEnvId, dbaddress));
									
								}
							}
						}
					}

					for ( int z = 0 ; z < sios.size() ; ++z ) {
						String sioaddress = sios.get(z);
						String siodbaddress = sioaddress + strLabel;
						
						logger.log(Level.FINE, "updateValue siodbaddress["+siodbaddress+"]");
						
						if ( keyAndValue.containsKey(siodbaddress) ) {
							for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
								String dbaddress = addresses[x];
								
								logger.log(Level.FINE, "updateValue siodbaddress dbaddress["+dbaddress+"]");
								
								if ( siodbaddress.startsWith(dbaddress) ) {
									String label = keyAndValue.get(siodbaddress);
									label = RTDB_Helper.removeDBStringWrapper(label);
									
									logger.log(Level.FINE, "updateValue label["+label+"]");
									
									inlineLabels[y].setText(label);
									
									// AI and SI Value Box
									TextBox textBox = new TextBox();
									textBox.setWidth("250px");							
									
									controlboxes[y].clear();
									
									controlboxes[y].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
									
									controlboxes[y].add(textBox);
									
									widgetGroups.put(sioaddress, new TextBox[]{textBox});
									
									widgetPoints.put(textBox, new ControlPoint("sio", scsEnvId, dbaddress));
									
								}
							}
						}
					}
				}

			}//End of for keyAndValuesStatic
		}
	
		for ( String clientKey : keyAndValuesDynamic.keySet() ) {
		
		}
		
		logger.log(Level.FINE, "updateValue End");
	}
	
	private CtlMgr ctlMgr = null;
	private Observer observer = null;
	private Subject controlMgrSubject = null;
	@Override
	public void connect() {
		logger.log(Level.FINE, "connect Begin");
		
		ctlMgr = CtlMgr.getInstance(tagname);
		controlMgrSubject = ctlMgr.getSubject();
		
		observer = new Observer() {
			@Override
			public void setSubject(Subject subject){
				this.subject = subject;
				this.subject.attach(this);
			}
			
			@Override
			public void update() {
				if ( null != messageBoxEvent ) {
					
					String message = subject.getState();
					
					if ( 0 == "sendFloatCommand sent".compareTo(message) ) {
						message = "Command Sent.";
					} else if ( 0 == "sendIntCommand sent".compareTo(message) ) {
						message = "Command Sent.";
					} else if ( 0 == "sendStringCommand sent".compareTo(message) ) {
						message = "Command Sent.";
					} else if ( 0 == "command executed successfully".compareTo(message) ) {
						message = "Command Executed Successfully.";
					}
					
					messageBoxEvent.setMessage(message);
				}
			}
		};
		
		observer.setSubject(controlMgrSubject);
		
		logger.log(Level.FINE, "connect End");
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.FINE, "disconnect Begin");
		
		logger.log(Level.FINE, "disconnect End");
	}
	
	private Button btnExecute = null;
	
	private Button btnUp			= null;
	private InlineLabel lblPageNum	= null;
	private Button btnDown			= null;
	
	private VerticalPanel vpCtrls = null;
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		vpCtrls  = new VerticalPanel();
		vpCtrls.setWidth("100%");
		
		btnExecute = new Button();
		btnExecute.getElement().getStyle().setPadding(10, Unit.PX);
		btnExecute.setText("Execute");
		btnExecute.addStyleName("project-gwt-button-inspector-"+tagname+"-execute");
		btnExecute.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				logger.log(Level.FINE, "getMainPanel onClick Begin");
				
				onButton(event);
				
				logger.log(Level.FINE, "getMainPanel onClick End");
			}
		});
		
		btnUp = new Button();
		btnUp.addStyleName("project-gwt-button-inspector-"+tagname+"-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				onButton(event);
			}
		});
		
		lblPageNum = new InlineLabel();
		lblPageNum.addStyleName("project-gwt-inlinelabel-inspector-"+tagname+"-pagenum");
		lblPageNum.setText("1 / 1");
		
		btnDown = new Button();
		btnDown.addStyleName("project-gwt-button-inspector-"+tagname+"-down");
		btnDown.setText("▼");
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				onButton(event);
			}
		});	

		HorizontalPanel pageBar = new HorizontalPanel();

		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(btnUp);
		
		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(lblPageNum);
		
		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(btnDown);
		
		HorizontalPanel bottomBar = new HorizontalPanel();
		bottomBar.setWidth("100%");
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(pageBar);
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(btnExecute);
				
		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-inspector");
		basePanel.setHeight("400px");
		basePanel.setWidth("400px");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.FINE, "getMainPanel End");
		
		return vp;
	}
	

	private void toggleButtonGroup ( UIButtonToggle btnSource, UIButtonToggle[] btnGroup ) {
		boolean toggled = btnSource.isHightLight();
		if ( !toggled ) {
			for ( UIButtonToggle btn : btnGroup ) {
				if ( btn != btnSource )
					btn.setHightLight(false);
			}
			btnSource.setHightLight(true);
		} else {
			btnSource.setHightLight(false);
		}
	}
	private void toggleButtonGroup ( Widget btnSource, Widget[] btnGroup ) {
		toggleButtonGroup((UIButtonToggle)btnSource, (UIButtonToggle[])btnGroup);
	}
	
	private Widget[] getSelectedGroup(Widget btn) {
		Widget[] targetGroups = null;
		for ( String address : widgetGroups.keySet() ) {
			if ( targetGroups != null ) break;
			Widget[] widgets = widgetGroups.get(address);
			for ( Widget widget : widgets ) {
				if ( targetGroups != null ) break;
				if ( widget == btn ) {
					targetGroups = widgets;
				}
			}
		}
		return targetGroups;
	}
	
	private Widget getActivateWidget() {
		logger.log(Level.FINE, "getActivateWidget Begin");
		Widget target = null;
		for ( String address : widgetGroups.keySet() ) {
			if ( target != null ) break;
			Widget[] widgets = widgetGroups.get(address);
			for ( Widget widget : widgets ) {
				if ( target != null ) break;
				if ( null != widget ) {
					if ( widget instanceof TextBox ) {
						TextBox txt = (TextBox)widget;
						String s = txt.getText();
						if ( s.length() > 0 ) {
							target = widget;
						}
					} else if ( widget instanceof UIButtonToggle ) {
						UIButtonToggle btn = (UIButtonToggle)widget;
						boolean b = btn.isHightLight();
						if ( b ) {
							target = widget;
						}
					} else {
						logger.log(Level.FINE, "getActivateWidget widget IS NOT SUPPORTED");
					}
				} else {
					logger.log(Level.FINE, "getActivateWidget btn IS NULL");
				}
			}
		}
		logger.log(Level.FINE, "getActivateWidget End");
		return target;
	}
	
	private void onButton(ClickEvent event) {
		Button btn = (Button)event.getSource();
		if ( btn instanceof UIButtonToggle ) {
			
			logger.log(Level.FINE, "onButton Button IS UIButtonToggle");
			
			Widget[] targetGroups = getSelectedGroup(btn);
			if ( null != targetGroups ) {
				toggleButtonGroup(btn, targetGroups);
			}
			
		} else if ( btn instanceof Button ) {
			logger.log(Level.FINE, "onButton Button IS Button");
			
			if ( btnExecute == btn ) {
				
				logger.log(Level.FINE, "onButton btn IS btnExecute");
				
				Widget widget = getActivateWidget();
				
				if ( null != widget ) {
					
					// Reset the Widget
					if ( widget instanceof UIButtonToggle ) {
						((UIButtonToggle) widget).setHightLight(false);
					} else if ( widget instanceof TextBox ) {
						((TextBox) widget).setText("");
					}
					
					// Send Control
					if ( widgetPoints.containsKey(widget) ) {
						
						ControlPoint controlPoint = widgetPoints.get(widget);
						
						if ( null != controlPoint ) {
							
							String sPoint		= controlPoint.getValue("point");
							String sScsEnvId	= controlPoint.getValue("scsenvid");
							String sDbAddress	= controlPoint.getValue("dbaddress");
							String sValue		= controlPoint.getValue("value");
							
							logger.log(Level.FINE, "onButton sPoint["+sPoint+"]");
							logger.log(Level.FINE, "onButton sScsEnvId["+sScsEnvId+"]");
							logger.log(Level.FINE, "onButton sDbAddress["+sDbAddress+"]");
							logger.log(Level.FINE, "onButton sValue["+sValue+"]");
							
							if ( 0 == sPoint.compareTo("dio") ) {
								
								logger.log(Level.FINE, "onButton controlPoint.dbaddress["+sDbAddress+"] Integer ["+Integer.parseInt(sValue)+"]");
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, Integer.valueOf(sValue), 1, 1, 1);
								
							} else if ( 0 == sPoint.compareTo("aio") ) {
								
								logger.log(Level.FINE, "onButton controlPoint.dbaddress["+sDbAddress+"] Float ["+Float.parseFloat(sValue)+"]");
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, Float.parseFloat(sValue), 1, 1, 1);
								
							} else if ( 0 == sPoint.compareTo("sio") ) {
								
								logger.log(Level.FINE, "onButton controlPoint.dbaddress["+sDbAddress+"] String ["+sValue+"]");
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, sValue, 1, 1, 1);
								
							} else {
								
								logger.log(Level.SEVERE, "onButton INVALID controlPoint.point");
								
							}
						} else {
							
							logger.log(Level.SEVERE, "onButton controlPoint IS NULL");
							
						}

					} else {
						
						logger.log(Level.SEVERE, "onButton controlPoint NOT CONTAIN the Key");
						
					}
				} else {
					
					logger.log(Level.SEVERE, "onButton controlPoint widget IS NULL");
					
				}
			} else if ( btn == btnUp || btn == btnDown ) {
				if ( btn == btnUp) {
					--pageIndex;
				} else if ( btn == btnDown ) {
					++pageIndex;
				}
				updatePager();
				updateValue(true);
			}
		}
	}

	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
		this.messageBoxEvent = messageBoxEvent;
	}
	
	@Override
	public void terminate() {
		logger.log(Level.FINE, "terminate Begin");

		logger.log(Level.FINE, "terminate End");
	}

}
