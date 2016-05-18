package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.HashMap;
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

	@Override
	public void buildWidgets() {
		buildWidgets(this.addresses.length);
	}
	
	private VerticalPanel[] widgetBoxes = null;
	private InlineLabel[] inlineLabels = null;
	private HorizontalPanel[] controlboxes = null;
	private void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.FINE, "buildWidgets Begin");
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			if ( null != this.addresses ) {
				
				String btnWidth = "90px";
				String btnHeight = "26px";
				
				widgetBoxes = new VerticalPanel[addresses.length];
				inlineLabels = new InlineLabel[addresses.length];
				controlboxes = new HorizontalPanel[addresses.length];
				
				int numOfCtrlBtnRow = 4;
				for(int x=0;x<addresses.length;++x){
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

	private HashMap<Widget, Control> widgetControls = new HashMap<Widget, Control>();
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.FINE, "updateValue Begin");
		
		String clientKey_multiReadValue_inspectorcontrol_static = "multiReadValue" + "inspectorcontrol" + "static" + parent;
		if ( 0 == clientKey_multiReadValue_inspectorcontrol_static.compareTo(clientKey) ) {
			
			
			for ( int y = 0 ; y < dios.size() ; ++y ) {
				
				String dioaddress = dios.get(y);
				
				String diolabeldbaddress = dioaddress + strLabel;
				
				logger.log(Level.FINE, "updateValue diolabeldbaddress["+diolabeldbaddress+"]");
				
				if ( keyAndValue.containsKey(diolabeldbaddress) ) {
					for ( int x = 0 ; x < addresses.length ; ++x ) {
						String dbaddress = addresses[x];
						
						logger.log(Level.FINE, "updateValue diolabeldbaddress dbaddress["+diolabeldbaddress+"]");
						
						if ( diolabeldbaddress.startsWith(dbaddress) ) {
							String value = keyAndValue.get(diolabeldbaddress);
							value = RTDB_Helper.removeDBStringWrapper(value);
							
							logger.log(Level.FINE, "updateValue value["+value+"]");
							
							inlineLabels[x].setText(value);
						}
					}
				}
				
				
				String diovaluetabledbaddress = dioaddress + strValueTable;
				
				logger.log(Level.FINE, "updateValue siodbaddress["+diovaluetabledbaddress+"]");
				
				if ( keyAndValue.containsKey(diovaluetabledbaddress) ) {
					for ( int x = 0 ; x < addresses.length ; ++x ) {
						String dbaddress = addresses[x];
						
						logger.log(Level.FINE, "updateValue siodbaddress dbaddress["+diovaluetabledbaddress+"]");
						
						if ( diovaluetabledbaddress.startsWith(dbaddress) ) {
							
							controlboxes[x].clear();
							
							String value = keyAndValue.get(diovaluetabledbaddress);
										
							String btnWidth = "90px";
							String btnHeight = "26px";
							{
								int numOfRow = 12;
								String points[] = new String[numOfRow];
								String labels[] = new String[numOfRow];
								String values[] = new String[numOfRow];
								for( int r = 0 ; r < numOfRow ; ++r ) {
									points[r] = RTDB_Helper.getArrayValues(value, 0, r );
									labels[r] = RTDB_Helper.getArrayValues(value, 1, r );
									values[r] = RTDB_Helper.getArrayValues(value, 2, r );
									
									logger.log(Level.FINE, "updateValue points[r]["+points[r]+"] BF");
									logger.log(Level.FINE, "updateValue labels[r]["+labels[r]+"] BF");
									logger.log(Level.FINE, "updateValue values[r]["+values[r]+"] BF");
									
									points[r] = RTDB_Helper.removeDBStringWrapper(points[r]);
									labels[r] = RTDB_Helper.removeDBStringWrapper(labels[r]);
									values[r] = RTDB_Helper.removeDBStringWrapper(values[r]);
									
									logger.log(Level.FINE, "updateValue points[r]["+points[r]+"] AF");
									logger.log(Level.FINE, "updateValue labels[r]["+labels[r]+"] AF");
									logger.log(Level.FINE, "updateValue values[r]["+values[r]+"] AF");
															
								}
								
								for ( int i = 0 ; i < points.length ; ++i ) {
									if ( null != labels[i] ) {
//										labels[i] = RTDB_Helper.removeDBStringWrapper(labels[i]);
										if ( labels[i].length() > 0 ) {
											UIButtonToggle btnCtrl = new UIButtonToggle(labels[i]);
											btnCtrl.addStyleName("project-gwt-button-inspector-"+tagname+"-ctrl");
											btnCtrl.addClickHandler(new ClickHandler() {
												
												@Override
												public void onClick(ClickEvent event) {
													UIButtonToggle button = (UIButtonToggle) event.getSource();
													if ( ! button.isHightLight() ) {
														button.setHightLight(true);
													} else {
														button.setHightLight(false);
													}
												}
											});
											controlboxes[x].add(btnCtrl);
											
											logger.log(Level.FINE, "updateValue points[r]["+points[i]+"] => RTDB_Helper.removeDBStringWrapper["+RTDB_Helper.removeDBStringWrapper(points[i])+"]");
											logger.log(Level.FINE, "updateValue labels[r]["+labels[i]+"] => RTDB_Helper.removeDBStringWrapper["+RTDB_Helper.removeDBStringWrapper(labels[i])+"]");
											logger.log(Level.FINE, "updateValue values[r]["+values[i]+"] => RTDB_Helper.removeDBStringWrapper["+RTDB_Helper.removeDBStringWrapper(values[i])+"]");
											
											
											widgetControls.put(btnCtrl, new Control("dio", scsEnvId, dioaddress, points[i], labels[i], values[i]));
										}
									}
								}
							}
						}
					}
				}
				
			}
			
			for ( int y = 0 ; y < aios.size() ; ++y ) {
				String aioaddress = aios.get(y);
				String aiodbaddresslabel = aioaddress + strLabel;
				
				logger.log(Level.FINE, "updateValue aiodbaddress["+aiodbaddresslabel+"]");
				
				if ( keyAndValue.containsKey(aiodbaddresslabel) ) {
					for ( int x = 0 ; x < addresses.length ; ++x ) {
						String dbaddress = addresses[x];
						
						logger.log(Level.FINE, "updateValue aiodbaddress dbaddress["+dbaddress+"]");
						
						if ( aiodbaddresslabel.startsWith(dbaddress) ) {
							String label = keyAndValue.get(aiodbaddresslabel);
							label = RTDB_Helper.removeDBStringWrapper(label);
							
							logger.log(Level.FINE, "updateValue label["+label+"]");
							
							inlineLabels[x].setText(label);
							
							controlboxes[x].clear();
							
							controlboxes[x].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
							
							TextBox textBox = new TextBox();
							textBox.setWidth("250px");
							controlboxes[x].add(textBox);
							
							widgetControls.put(textBox, new Control("aio", scsEnvId, aioaddress, label));
						}
					}
				}
			}

			for ( int y = 0 ; y < sios.size() ; ++y ) {
				String sioaddress = sios.get(y);
				String siodbaddress = sioaddress + strLabel;
				
				logger.log(Level.FINE, "updateValue siodbaddress["+siodbaddress+"]");
				
				if ( keyAndValue.containsKey(siodbaddress) ) {
					for ( int x = 0 ; x < addresses.length ; ++x ) {
						String dbaddress = addresses[x];
						
						logger.log(Level.FINE, "updateValue siodbaddress dbaddress["+dbaddress+"]");
						
						if ( siodbaddress.startsWith(dbaddress) ) {
							String label = keyAndValue.get(siodbaddress);
							label = RTDB_Helper.removeDBStringWrapper(label);
							
							logger.log(Level.FINE, "updateValue label["+label+"]");
							
							inlineLabels[x].setText(label);
							
							controlboxes[x].clear();
							
							controlboxes[x].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
							
							TextBox textBox = new TextBox();
							textBox.addStyleName("project-gwt-textbox-inspector-"+tagname+"-control");
							controlboxes[x].add(textBox);
							
							widgetControls.put(textBox, new Control("sio", scsEnvId, sioaddress, label));
						}
					}
				}
			}
		}
		
		logger.log(Level.FINE, "updateValue End");
	}
	
	private CtlMgr ctlMgr = null;
	private Observer observer = null;
	private Subject controlMgrSubject = null;
	@Override
	public void connect() {
		logger.log(Level.FINE, "connect Begin");
		
		ctlMgr = CtlMgr.getInstance("control");
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
					messageBoxEvent.setMessage(subject.getState());
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
	
	private VerticalPanel vpCtrls = null;
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		vpCtrls  = new VerticalPanel();
		vpCtrls.setWidth("100%");
		
		Button btnExecute = new Button();
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
		
		Button btnUp = new Button();
		btnUp.addStyleName("project-gwt-button-inspector-"+tagname+"-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		
		InlineLabel lblPageNum = new InlineLabel();
		lblPageNum.addStyleName("project-gwt-inlinelabel-inspector-"+tagname+"-pagenum");
		lblPageNum.setText("1 / 1");
		
		Button btnDown = new Button();
		btnDown.addStyleName("project-gwt-button-inspector-"+tagname+"-down");
		btnDown.setText("▼");
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
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
	
	private void onButton(ClickEvent event) {
		logger.log(Level.FINE, "onButton Begin");
		
		for ( Widget widget : widgetControls.keySet() ) {
			if ( null != widget ) {
				
				if ( widget instanceof TextBox ) {
					
					// AIO, SIO
					logger.log(Level.FINE, "getMainPanel onClick widget IS TextBox");
					TextBox textBox = (TextBox)widget;
					String value = textBox.getText().trim();
					logger.log(Level.FINE, "getMainPanel onClick TextBox value["+value+"]");
					if ( value.length() > 0 && 0 != value.compareTo("") ) {
						Control control = widgetControls.get(widget);
						if ( 0 == control.io.compareTo("sio") ) {
							
							logger.log(Level.FINE, "getMainPanel onClick TextBox control.io["+control.io+"]");
							
							ctlMgr.sendControl(control.scsEnvId, new String[]{control.dbaddress}, value, 1, 1, 1);
							
						} else if ( 0 == control.io.compareTo("aio") ) {
							
							logger.log(Level.FINE, "getMainPanel onClick TextBox control.io["+control.io+"]");
							
							ctlMgr.sendControl(control.scsEnvId, new String[]{control.dbaddress}, Float.parseFloat(value), 1, 1, 1);
							
						}
						break;
					}
				} else if ( widget instanceof UIButtonToggle ) {
					
					// DIO
					logger.log(Level.FINE, "getMainPanel onClick widget IS UIButtonControl");
					UIButtonToggle uiButtonControl = (UIButtonToggle)widget;
					if ( uiButtonControl.isHightLight() ) {
						
						Control control = widgetControls.get(widget);
						
						logger.log(Level.FINE, "getMainPanel onClick TextBox control.io["+control.io+"]");
						
						ctlMgr.sendControl(control.scsEnvId, new String[]{control.dbaddress}, Integer.parseInt(control.value), 1, 1, 1);
						
						break;
					}
				}
			}
		}
		
		logger.log(Level.FINE, "onButton End");
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
