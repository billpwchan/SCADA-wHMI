package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Observer;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Subject;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ICTLComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ScsCTLComponentAccess;

public class UIInspectorTag implements UIInspectorTab_i, IClientLifeCycle {
	
	private static Logger logger = Logger.getLogger(UIInspectorTag.class.getName());
	
	private String strLabel			= ".label";
	private String strValueTable	= ".valueTable";
	
	// Static Attribute List
	private String staticAttibutes[]	= new String[] {strLabel};
	
	// Static Attribute List
	private String dioStaticAttibutes[]	= new String[] {strLabel, strValueTable};

	// Dynamic Attribute List
	private String dynamicAttibutes[]	= new String[] {};
	
	private String dio = "dio";
		
	private LinkedList<String> dios = new LinkedList<String>();

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String parent) {
		this.parent = parent;
		logger.log(Level.SEVERE, "setParent this.parent["+this.parent+"]");
	}
	
	@Override
	public void setAddresses(String scsEnvId, String[] addresses) {
		logger.log(Level.SEVERE, "setAddresses Begin");
		
		this.scsEnvId = scsEnvId;
		
		logger.log(Level.SEVERE, "setAddresses this.scsEnvId["+this.scsEnvId+"]");
		
		this.addresses = addresses;
		
		logger.log(Level.SEVERE, "setAddresses End");
	}

	@Override
	public void buildWidgets() {
		buildWidgets(this.addresses.length);
	}
	
	private VerticalPanel[] widgetBoxes = null;
	private InlineLabel[] inlineLabels = null;
	private HorizontalPanel[] controlboxes = null;
	private void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.SEVERE, "buildWidgets Begin");
		
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
					inlineLabels[x].addStyleName("project-gwt-label-inspector-tag-control");
					inlineLabels[x].setText("Control: "+(x+1));
					widgetBoxes[x].add(inlineLabels[x]);
					
					controlboxes[x] = new HorizontalPanel();
					for(int y=1;y<=numOfCtrlBtnRow;++y){
						UIButtonToggle btnCtrl = new UIButtonToggle("Control "+y);
						btnCtrl.setWidth(btnWidth);
						btnCtrl.setHeight(btnHeight);
						btnCtrl.addStyleName("project-gwt-button-inspector-tag-option");
						controlboxes[x].add(btnCtrl);
					}
					widgetBoxes[x].add(controlboxes[x]);
					
					HorizontalPanel hp2 = new HorizontalPanel();
					hp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
					Button btnCtrl = new Button();
					btnCtrl.setText("Execute");
					btnCtrl.setWidth(btnWidth);
					btnCtrl.setHeight(btnHeight);
					btnCtrl.addStyleName("project-gwt-button-inspector-tag-execute");
					btnCtrl.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							onButton(event);
						}
					});
					hp2.add(btnCtrl);

					widgetBoxes[x].add(hp2);
					
					vpCtrls.add(widgetBoxes[x]);
				}
			} else {
				logger.log(Level.SEVERE, "buildWidgets this.pointStatics IS NULL");
			}
		} else {
			logger.log(Level.SEVERE, "buildWidgets points IS NULL");
		}
		
		logger.log(Level.SEVERE, "buildWidgets End");
	}
	
	public void buildWidgetList() {
		
		logger.log(Level.SEVERE, "buildWidgetList Begin");
		
		for ( int i = 0 ; i < addresses.length ; ++i ) {
			String dbaddress = addresses[i];
			if ( null != dbaddress ) {
				String point = RTDB_Helper.getPoint(dbaddress);
				if ( null != point ) {
					if ( point.startsWith(dio) ) {
						dios.add(dbaddress);
						continue;
					}
				}
			} else {
				logger.log(Level.FINE, "buildWidgetList dbaddress IS NULL");
			}
		}
		
		logger.log(Level.SEVERE, "buildWidgetList End");
		
	}
	
	private HashMap<Widget, Control> widgetControls = new HashMap<Widget, Control>();
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.SEVERE, "updateValue Begin");
		
		String clientKey_multiReadValue_inspectortag_static = "multiReadValue" + "inspectortag" + "static" + parent;
		if ( 0 == clientKey_multiReadValue_inspectortag_static.compareTo(clientKey) ) {
			
			for ( int y = 0 ; y < dios.size() ; ++y ) {
				
				String dioaddress = dios.get(y);
				
				String diolabeldbaddress = dioaddress + strLabel;
				
				logger.log(Level.SEVERE, "updateValue diolabeldbaddress["+diolabeldbaddress+"]");
				
				if ( keyAndValue.containsKey(diolabeldbaddress) ) {
					for ( int x = 0 ; x < addresses.length ; ++x ) {
						String dbaddress = addresses[x];
						
						logger.log(Level.SEVERE, "updateValue diolabeldbaddress dbaddress["+diolabeldbaddress+"]");
						
						if ( diolabeldbaddress.startsWith(dbaddress) ) {
							String value = keyAndValue.get(diolabeldbaddress);
							value = RTDB_Helper.removeDBStringWrapper(value);
							
							logger.log(Level.SEVERE, "updateValue value["+value+"]");
							
							inlineLabels[x].setText(value);
						}
					}
				}
				
				
				String diovaluetabledbaddress = dioaddress + strValueTable;
				
				logger.log(Level.SEVERE, "updateValue siodbaddress["+diovaluetabledbaddress+"]");
				
				if ( keyAndValue.containsKey(diovaluetabledbaddress) ) {
					for ( int x = 0 ; x < addresses.length ; ++x ) {
						String dbaddress = addresses[x];
						
						logger.log(Level.SEVERE, "updateValue siodbaddress dbaddress["+diovaluetabledbaddress+"]");
						
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
									
									logger.log(Level.SEVERE, "updateValue points[r]["+points[r]+"] BF");
									logger.log(Level.SEVERE, "updateValue labels[r]["+labels[r]+"] BF");
									logger.log(Level.SEVERE, "updateValue values[r]["+values[r]+"] BF");
									
									points[r] = RTDB_Helper.removeDBStringWrapper(points[r]);
									labels[r] = RTDB_Helper.removeDBStringWrapper(labels[r]);
									values[r] = RTDB_Helper.removeDBStringWrapper(values[r]);
									
									logger.log(Level.SEVERE, "updateValue points[r]["+points[r]+"] AF");
									logger.log(Level.SEVERE, "updateValue labels[r]["+labels[r]+"] AF");
									logger.log(Level.SEVERE, "updateValue values[r]["+values[r]+"] AF");
															
								}
								
								for ( int i = 0 ; i < points.length ; ++i ) {
									if ( null != labels[i] ) {
//										labels[i] = RTDB_Helper.removeDBStringWrapper(labels[i]);
										if ( labels[i].length() > 0 ) {
											UIButtonToggle btnCtrl = new UIButtonToggle(labels[i]);
											btnCtrl.setWidth(btnWidth);
											btnCtrl.setHeight(btnHeight);
											btnCtrl.addStyleName("project-gwt-button-inspector-tag-control");
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
											
											logger.log(Level.SEVERE, "updateValue points[r]["+points[i]+"] => RTDB_Helper.removeDBStringWrapper["+RTDB_Helper.removeDBStringWrapper(points[i])+"]");
											logger.log(Level.SEVERE, "updateValue labels[r]["+labels[i]+"] => RTDB_Helper.removeDBStringWrapper["+RTDB_Helper.removeDBStringWrapper(labels[i])+"]");
											logger.log(Level.SEVERE, "updateValue values[r]["+values[i]+"] => RTDB_Helper.removeDBStringWrapper["+RTDB_Helper.removeDBStringWrapper(values[i])+"]");
											
											
											widgetControls.put(btnCtrl, new Control("dio", scsEnvId, dioaddress, points[i], labels[i], values[i]));
										}
									}
								}
							}
						}
					}
				}
				
			}

		}
		
		logger.log(Level.SEVERE, "updateValue End");
	}
	
	private CtlMgr controlMgr = null;
//	private Observer observer = null;
//	private Subject controlMgrSubject = null;
	@Override
	public void connect() {
		logger.log(Level.SEVERE, "connect Begin");
		
		controlMgr = CtlMgr.getInstance();
//		controlMgrSubject = controlMgr.getSubject();
//		
//		observer = new Observer() {
//			@Override
//			public void setSubject(Subject subject){
//				this.subject = subject;
//				this.subject.attach(this);
//			}
//			
//			@Override
//			public void update() {
//
//			}
//		};
//		
//		observer.setSubject(controlMgrSubject);
		
		logger.log(Level.SEVERE, "connect End");
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.SEVERE, "disconnect Begin");
		
		logger.log(Level.SEVERE, "disconnect End");
	}
	

	private VerticalPanel vpCtrls = null;
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.SEVERE, "getMainPanel Begin");

		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		vpCtrls = new VerticalPanel();
		vpCtrls.setWidth("100%");
		
		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-inspector");
		basePanel.setHeight("400px");
		basePanel.setWidth("400px");
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.SEVERE, "getMainPanel End");
		
		return vp;
	}
	
	private void onButton(ClickEvent event) {
		logger.log(Level.SEVERE, "onButton Begin");
		
		for ( Widget widget : widgetControls.keySet() ) {
			if ( null != widget ) {
				if ( widget instanceof UIButtonToggle ) {
					logger.log(Level.SEVERE, "getMainPanel onClick widget IS UIButtonControl");
					UIButtonToggle uiButtonControl = (UIButtonToggle)widget;
					if ( uiButtonControl.isHightLight() ) {
						
						Control control = widgetControls.get(widget);
						logger.log(Level.SEVERE, "getMainPanel onClick TextBox control.io["+control.io+"]");
						controlMgr.sendControl(control.io, control.scsEnvId, new String[]{control.dbaddress}, control.value, 1, 1, 1);
						
						break;
					}
				}
			}
		}
		
		logger.log(Level.SEVERE, "onButton End");
	}
	
	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
		this.messageBoxEvent = messageBoxEvent;
	}

	@Override
	public void terminate() {
		logger.log(Level.SEVERE, "terminate Begin");

		logger.log(Level.SEVERE, "terminate End");
	}

}
