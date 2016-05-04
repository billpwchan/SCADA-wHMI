package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogic;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogicDynamicEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogicStaticEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.storage.Point;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIInspectorInfo implements UIInspectorTab_i {
	
	private static Logger logger = Logger.getLogger(UIInspectorInfo.class.getName());
	
	// 1 Get DBAddress Children
	// 2 Get DBAddress Static Data
	// 3 Get DBAddress Subscription Data
	// 4 Update Scscription Data
	// 5 Disconnect
	
	public final String strCSSStatusGreen		= "project-gwt-inlinelabel-inspector-info-status-green";
	public final String strCSSStatusRed			= "project-gwt-inlinelabel-inspector-info-status-red";
	public final String strCSSStatusBlue		= "project-gwt-inlinelabel-inspector-info-status-blue";
	public final String strCSSStatusGrey		= "project-gwt-inlinelabel-inspector-info-status-grey";

	// Static Attribute
	private final String strLabel				= ".label";
	private final String strValueTable			= ":dal.valueTable";
	private final String strHmiOrder			= ".hmiOrder";
	
	// Dynamic Attribute
	private final String strValue				= ".value";
	private final String strValidity			= ".validity"; // 0=invalid, 1=valid
	private final String strValueAlarmVector	= ":dal.valueAlarmVector"; // (0,1)==0 = normal, (0,1)==1 = alarm 
	private final String strForcedStatus		= ":dfo.forcedStatus"; // 2=MO, AI=8, 512=SS //dfo.forcedStatus

	// Static Attribute List
	private String staticAttibutes [] = new String[] {strLabel, strValueTable, strHmiOrder};

	// Dynamic Attribute List
	private String dynamicAttibutes [] = new String[] {strValue, strValidity, strValueAlarmVector, strForcedStatus};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String parent) {
		this.parent=parent;
	}
	
	@Override
	public void setAddresses(String scsEnvId, String[] addresses) {
		logger.log(Level.SEVERE, "setAddresses Begin");
		
		this.scsEnvId = scsEnvId;
		
		logger.log(Level.SEVERE, "setConnection this.scsEnvId["+this.scsEnvId+"]");
		
		this.addresses = addresses;
		
		logger.log(Level.SEVERE, "setAddresses End");
	}
	
	private UIPanelInspectorRTDBLogic logic = null;
	@Override
	public void connect() {
		logger.log(Level.SEVERE, "connect Begin");
		
		if ( RTDB_Helper.addressesIsValid(this.addresses) ) {
			
			{
				logger.log(Level.SEVERE, "connect setUIPanelInspectorRTDBLogicStaticEvent Begin");
				
				if ( null != logic ) logic.disconnect();
				
				logic = new UIPanelInspectorRTDBLogic(this.uiNameCard);
				
				logic.setScsEnvId(scsEnvId);
				logic.setParent(parent);
				logic.setDBaddresses(this.addresses);
				logic.setStaticAttributes(staticAttibutes);
//				logic.setDynamicAttributes(dynamicAttibutes);
				logic.setUIPanelInspectorRTDBLogicStaticEvent(new UIPanelInspectorRTDBLogicStaticEvent() {
					@Override
					public void ready(Point[] points) {
						buildPointList(points);
						buildWidgets(points.length);
						updateStaticDisplay(points);
					}
				});
				logic.setUIPanelInspectorRTDBLogicDynamicEventt(new UIPanelInspectorRTDBLogicDynamicEvent() {
					@Override
					public void update(Point[] points) {
						updateDynamicDisplay(points);
					}
				});
				
				logic.connect();
				
				logger.log(Level.SEVERE, "connect setUIPanelInspectorRTDBLogicStaticEvent End");
			}
			
		} else {
			logger.log(Level.SEVERE, "connect addressIsValid IS FALSE");
		}

		logger.log(Level.SEVERE, "connect End");
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.SEVERE, "disconnect Begin");
		
		if ( null != logic ) logic.disconnect();
		
		logger.log(Level.SEVERE, "disconnect End");
	}
	
	@Override
	public void buildWidgets() {
		logger.log(Level.SEVERE, "buildWidgets Begin");
		
//		buildPointList(this.addresses.length);
//		buildWidgets(this.addresses.length);
	
		logger.log(Level.SEVERE, "buildWidgets End");
	}
	
	private void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.SEVERE, "buildWidgets Begin");
		
		logger.log(Level.SEVERE, "buildWidgets numOfWidgets["+numOfWidgets+"]");
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			if ( RTDB_Helper.addressesIsValid(this.addresses) ) {
				
				txtAttribute = new TextBox[numOfWidgets];
				lblAttibuteLabel = new InlineLabel[numOfWidgets];
				txtAttibuteColor = new InlineLabel[numOfWidgets];

				flexTableAttibutes = new FlexTable();
				flexTableAttibutes.setWidth("100%");
				for( int i = 0 ; i < numOfWidgets ; ++i ) {
					
					logger.log(Level.SEVERE, "buildWidgets i["+i+"]");
						
					lblAttibuteLabel[i] = new InlineLabel();
					lblAttibuteLabel[i].setWidth("100%");
					lblAttibuteLabel[i].addStyleName("project-gwt-inlinelabel-inspector-info-label");
					lblAttibuteLabel[i].setText("ATTRIBUTE_LABEL_"+(i+1)+":");
					flexTableAttibutes.setWidget(i+1, 0, lblAttibuteLabel[i]);
					txtAttribute[i] = new TextBox();
					txtAttribute[i].setWidth("95%");
					txtAttribute[i].setText("ATTRIBUTE_STATUS_"+(i+1));
					txtAttribute[i].addStyleName("project-gwt-textbox-inspector-info-value");
					txtAttribute[i].setReadOnly(true);
					txtAttribute[i].setMaxLength(16);
					flexTableAttibutes.setWidget(i+1, 1, txtAttribute[i]);
					txtAttibuteColor[i] = new InlineLabel();
					txtAttibuteColor[i].setText("R");
					txtAttibuteColor[i].setStyleName(strCSSStatusGrey);
					flexTableAttibutes.setWidget(i+1, 2, txtAttibuteColor[i]);
				}

				flexTableAttibutes.getColumnFormatter().addStyleName(0, "project-gwt-flextable-inspector-info-label-col");
				flexTableAttibutes.getColumnFormatter().addStyleName(1, "project-gwt-flextable-inspector-info-value-col");
				flexTableAttibutes.getColumnFormatter().addStyleName(2, "project-gwt-flextable-inspector-info-status-col");

			} else {
				logger.log(Level.SEVERE, "buildWidgets this.pointStatics IS NULL");
			}
			
			vpCtrls.add(flexTableAttibutes);
			
		} else {
			logger.log(Level.SEVERE, "buildWidgets points IS NULL");
		}
		
		logger.log(Level.SEVERE, "buildWidgets End");
	}
	
	private LinkedList<Point> pointDisplays = null;
	private void buildPointList(Point[] points) {
		logger.log(Level.SEVERE, "buildVisibleList Begin");

		if ( null != points ) {
			
			logger.log(Level.SEVERE, "buildVisibleList points.length["+ points.length +"]");
			
			pointDisplays = new LinkedList<Point>();
			for ( Point point : points ) {

				if ( null != point ) {
					
					logger.log(Level.SEVERE, "buildVisibleList point.getValue(strHmiOrder)["+ point.getValue(strHmiOrder)+"]");
					
					String address = point.getAddress();
					
					logger.log(Level.SEVERE, "buildVisibleList address["+ address +"]");
					
					if ( null != point.getValue(strHmiOrder) && 0 == point.getValue(strHmiOrder).compareTo("-1") ) {
						// Invisiable Point
						continue;
					}
					
					pointDisplays.add(point);
					
				} else {
					logger.log(Level.SEVERE, "buildVisibleList point IS NULL");
				}
			}
			
			logger.log(Level.SEVERE, "buildVisibleList pointDisplays.size()["+ pointDisplays.size() +"]");
		}
	}
	
	
	private void updateStaticDisplay(Point[] points) {
		
		logger.log(Level.SEVERE, "updateStaticDisplay Begin");
		
		if ( null != points ) {

			logger.log(Level.SEVERE, "updateStaticDisplay points.length["+points.length+"]");
			
			int i=0;
			for ( Point point: points ) {
				
				if ( null != point ) {
					String address = point.getAddress();
					
					logger.log(Level.SEVERE, "updateStaticDisplay address["+address+"]");
					
//					Integer integer = widgetIndex.get(address);
//					if ( null != integer ) {
						
						int index = ++i;
						
						logger.log(Level.SEVERE, "updateStaticDisplay index["+index+"]");
						
						logger.log(Level.SEVERE, "updateStaticDisplay pointDisplays("+index+") point.label["+point.getValue(strLabel)+"]");
						
						lblAttibuteLabel[index].setText(point.getValue(strLabel));
		
						txtAttribute[index].setText("-");
		
						txtAttibuteColor[index].setStyleName(strCSSStatusGrey);								
//					} else {
//						logger.log(Level.SEVERE, "updateStaticDisplay integer IS NULL");
//					}
				} else {
					logger.log(Level.SEVERE, "updateStaticDisplay point IS NULL");
				}
			}

		} else {
			logger.log(Level.SEVERE, "updateStaticDisplay points IS NULL");
		}
		
		logger.log(Level.SEVERE, "updateStaticDisplay End");
	}
	
	private void updateDynamicDisplay(Point[] points) {
		
		logger.log(Level.SEVERE, "updateDynamicDisplay Begin");
		
		if ( null != points ) {
			
			logger.log(Level.SEVERE, "updateDynamicDisplay points.length["+ points.length +"] points.length["+ pointDisplays.size() +"]");
			
			for ( Point point : points ) {
				
				if ( null != point ) {

					{
						String [] keys = point.getAttributeKeys();
						logger.log(Level.SEVERE, "updateDynamicDisplay point.getAddress()["+point.getAddress()+"] keys.length["+keys.length+"]");
						for ( String k : keys ) {
							logger.log(Level.SEVERE, "updateDynamicDisplay point.getValue("+k+")["+point.getValue(k)+"]");
						}
					}
					
					String address = point.getAddress();
					
					if ( null != address ) {
						
						String value = point.getValue(strValue);
						
						logger.log(Level.SEVERE, "updateDynamicDisplay address["+address+"] == value["+value+"]");
						
						int i = 0;
						Iterator<Point> iteratorDisplay = pointDisplays.iterator();
						while ( iteratorDisplay.hasNext() ) {
							Point pointDisplay = iteratorDisplay.next();
							
							if ( null != pointDisplay ) {
								
								if ( null != pointDisplay.getAddress() ) {
									
									if ( 0 == address.compareTo(pointDisplay.getAddress()) ) {
										
										String name = null;
								
										String valueTable = pointDisplay.getValue(strValueTable);
										for( int r = 0 ; r < 12 ; ++r ) {
											String v = RTDB_Helper.getArrayValues(valueTable, 4, r );
											logger.log(Level.SEVERE, "updateDynamicDisplay getArrayValues r["+r+"] v["+v+"] == valueTable[i]["+valueTable+"]");
											if ( 0 == v.compareTo(value) ) {
												name = RTDB_Helper.getArrayValues(valueTable, 1, r );
												break;
											}
										}
										
										if ( null != name ) {
											txtAttribute[i].setText(name);	
										} else {
											txtAttribute[i].setText(value);	
										}
										
										logger.log(Level.SEVERE, "updateDynamicDisplay value["+value+"] or name["+name+"]");
										
										String strColorCSS = RTDB_Helper.getColorCSS(point.getValue(strValueAlarmVector), point.getValue(strValidity), point.getValue(strForcedStatus));
										txtAttibuteColor[i].setStyleName(strColorCSS);
										
										logger.log(Level.SEVERE, "updateDynamicDisplay strColorCSS["+strColorCSS+"]");
									}
								} else {
									logger.log(Level.SEVERE, "updateDynamicDisplay pointDisplay.getAddress() IS NULL");
								}
							} else {
								logger.log(Level.SEVERE, "updateDynamicDisplay pointDisplay IS NULL");
							}
							++i;
						}
					} else {
						logger.log(Level.SEVERE, "updateDynamicDisplay pointDynamic.getAddress() IS NULL");
					}
					
				} else {
					logger.log(Level.SEVERE, "updateDynamicDisplay pointDynamic IS NULL");
				}
			} // pointDynamics iterator
			
		} else {
			logger.log(Level.SEVERE, "updateDynamicDisplay pointDynamics IS NULL");
		}

		logger.log(Level.SEVERE, "updateDynamicDisplay End");
	}
	
	FlexTable flexTableAttibutes = null;

	private TextBox txtAttribute[];
	private InlineLabel lblAttibuteLabel[];
	private InlineLabel txtAttibuteColor[];
	
	private VerticalPanel vpCtrls = null;
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.SEVERE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		vpCtrls = new VerticalPanel();
		vpCtrls.setWidth("100%");
		
		Button btnUp = new Button();
		btnUp.addStyleName("project-gwt-button-inspector-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		
		InlineLabel lblPageNum = new InlineLabel();
		lblPageNum.addStyleName("project-gwt-inlinelabel-pagenum");
		lblPageNum.setText("1 / 1");
		
		Button btnDown = new Button();
		btnDown.addStyleName("project-gwt-button-inspector-down");
		btnDown.setText("▼");
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});	
		
		Button btnAckCurPage = new Button();
		btnAckCurPage.addStyleName("project-gwt-button-inspector-info-ackpage");
		btnAckCurPage.setText("Ack. Page");
		btnAckCurPage.addClickHandler(new ClickHandler() {
			
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
		bottomBar.addStyleName("project-gwt-panel-inspector-info-bottom");
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(pageBar);
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(btnAckCurPage);

		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-inspector");
		basePanel.setHeight("400px");
		basePanel.setWidth("400px");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.SEVERE, "getMainPanel End");
		
		return vp;
	}
}
