package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.Collections;
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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.Point;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.PointSort;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogic;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogic_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelLogicChildrenDataEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelLogicDynamicDataEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelLogicStaticDataEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
public class UIInspectorInfo implements UIPanelInspector_i {
	
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

	private int numOfPoints = 12;
	
	// Static Attribute List
	private String attributesStatic [] = new String[] {strLabel, strValueTable, strHmiOrder};

	// Dynamic Attribute List
	private String attributesDynamic [] = new String[] {strValue, strValidity, strValueAlarmVector, strForcedStatus};
	
	private UIPanelInspectorRTDBLogic uiPanelRTDBLogin = null;
	
	private String scsEnvId = null;
	private String dbaddress = null;

	@Override
	public void setConnection(String scsEnvId, String dbaddress) {
		logger.log(Level.SEVERE, "setConnection Begin");
		this.scsEnvId = scsEnvId;
		this.dbaddress = dbaddress;

		logger.log(Level.SEVERE, "setConnection this.scsEnvId["+this.scsEnvId+"] this.dbaddress["+this.dbaddress+"]");
		
		this.uiPanelRTDBLogin = new UIPanelInspectorRTDBLogic();
		
		this.uiPanelRTDBLogin.setUINameCard(this.uiNameCard);
		this.uiPanelRTDBLogin.setConnection(this.scsEnvId, this.dbaddress);
		this.uiPanelRTDBLogin.setAttibute(UIPanelInspectorRTDBLogic_i.strStatic, attributesStatic);
		this.uiPanelRTDBLogin.setAttibute(UIPanelInspectorRTDBLogic_i.strDynamic, attributesDynamic);
		
		this.uiPanelRTDBLogin.setUIPanelLogicChildrenDataEvent(new UIPanelLogicChildrenDataEvent() {
			@Override
			public void ready(String[] instances) {
				updateChildrenList(instances);
				
				readyToReadStaticData();
			}
		});
		
		this.uiPanelRTDBLogin.setUIPanelLogicStaticDataEvent(new UIPanelLogicStaticDataEvent() {
			@Override
			public void ready(LinkedList<Point> points) {
				updateStaticDisplay(points);
				
				readyToSubscribeDynamicData();
			}
		});
		this.uiPanelRTDBLogin.setUIPanelLogicDynamicDataEvent(new UIPanelLogicDynamicDataEvent() {
			@Override
			public void update(LinkedList<Point> points) {
				updateDynamicDisplay(points);
			}
		});
		logger.log(Level.SEVERE, "setConnection End");
	}
	
	@Override
	public void readyToReadChildrenData() {
		logger.log(Level.SEVERE, "readyToReadChildrenData Begin");
		
		this.uiPanelRTDBLogin.readyToReadChildrenData();
		
		logger.log(Level.SEVERE, "readyToReadChildrenData End");
	}
	
	@Override
	public void readyToReadStaticData() {
		logger.log(Level.SEVERE, "readyToReadStaticData Begin");
		
		this.uiPanelRTDBLogin.readyToReadStaticData();
		
		logger.log(Level.SEVERE, "readyToReadStaticData End");
	}
	
	@Override
	public void readyToSubscribeDynamicData() {
		logger.log(Level.SEVERE, "readyToSubscribeDynamicData Begin");
		
		this.uiPanelRTDBLogin.readyToSubscribeDynamicData();
		
		logger.log(Level.SEVERE, "readyToSubscribeDynamicData End");
	}
	
	@Override
	public void removeConnection() {
		logger.log(Level.SEVERE, "removeConnection Begin");
		
		this.uiPanelRTDBLogin.removeDynamicSubscription();
		
		logger.log(Level.SEVERE, "removeConnection End");
	}
	
	private String[] childrenList = null;
	private void updateChildrenList(String[] instances) {
		logger.log(Level.SEVERE, "updateChildrenList Begin");
		
		this.childrenList = instances;
		
		logger.log(Level.SEVERE, "updateChildrenList End");
		
	}
	
	private LinkedList<Point> pointStatics = null;
	private LinkedList<Point> pointDisplays = null;
	private void updateStaticDisplay(LinkedList<Point> points) {
		
		logger.log(Level.SEVERE, "updateStaticDisplay Begin");

		this.pointStatics = points;
		
		if ( null != points ) {
			
			logger.log(Level.SEVERE, "updateStaticDisplay points.size()["+ points.size() +"]");
			
			pointDisplays = new LinkedList<Point>();
			Iterator<Point> iterator = points.iterator();
			while ( iterator.hasNext() ) {
				Point pointStatic = iterator.next();

				if ( null != pointStatic ) {
					
					logger.log(Level.SEVERE, "updateStaticDisplay  pointStatic.getValue(strHmiOrder)["+ pointStatic.getValue(strHmiOrder)+"]");
					
					String address = pointStatic.getAddress();
					
					logger.log(Level.SEVERE, "updateStaticDisplay address["+ address +"]");
					
					String[] ignores = new String[] {"dio", "aio", "sio"};
					
					boolean ignored = false;
					
					if ( null != address ) {
						String addresses[] = address.split(":");
						String pointName = addresses[addresses.length-1];
						
						for(int i = 0 ; i < ignores.length ; ++i ) {
							
							logger.log(Level.SEVERE, "updateStaticDisplay pointName["+ pointName+"] ignores[i]["+ ignores[i]+"]");
							
							if ( pointName.startsWith(ignores[i]) ) {
								
								ignored = true;
								
								logger.log(Level.SEVERE, "updateStaticDisplay pointName["+ pointName+"] ignores[i]["+ ignores[i]+"] IGNORED");
								
								break;
							}
						}
					} else {
						
						ignored = true;
						
						logger.log(Level.SEVERE, "updateStaticDisplay address IS NULL");
					}
					
					if ( ignored ) continue;
					
					if ( null != pointStatic.getValue(strHmiOrder) && 0 == pointStatic.getValue(strHmiOrder).compareTo("-1") ) {
						// Invisiable Point
						continue;
					}
					
					pointDisplays.add(pointStatic);					
				} else {
					logger.log(Level.SEVERE, "updateStaticDisplay pointStatic IS NULL");
				}
			}
			
			logger.log(Level.SEVERE, "updateStaticDisplay pointDisplays.size()["+ pointDisplays.size() +"]");
			
//			LinkedList<PointSort> pointDisplaysSorted = new LinkedList<PointSort>();
//			
			// sort in ascending order
//			Collections.sort(pointDisplaysSorted);
//	 
//			// sort in descending order
//			Collections.sort(pointDisplaysSorted, Collections.reverseOrder());

			int i = 0;
			Iterator<Point> iteratorDisplay = pointDisplays.iterator();
			while ( iteratorDisplay.hasNext() ) {
				Point pointDisplay = iteratorDisplay.next();
				
				if ( null != pointDisplay ) {
					
					logger.log(Level.SEVERE, "updateStaticDisplay pointDisplay.label("+i+")["+i+"] pointDisplay.label["+pointDisplay.getValue(strLabel)+"]");
					
					lblAttibuteLabel[i].setVisible(true);
					lblAttibuteLabel[i].setText(pointDisplay.getValue(strLabel));

					txtAttribute[i].setVisible(true);
					txtAttribute[i].setText("-");

					txtAttibuteColor[i].setVisible(true);
					txtAttibuteColor[i].setStyleName(strCSSStatusGrey);
							
					++i;
				} else {
					logger.log(Level.SEVERE, "updateStaticDisplay IS NULL");
				}
			}
				
			for ( ; i < numOfPoints ; ++i ) {
				lblAttibuteLabel[i].setVisible(false);
				txtAttribute[i].setVisible(false);
				txtAttibuteColor[i].setVisible(false);
			}
		} else {
			logger.log(Level.SEVERE, "updateStaticDisplay points IS NULL");
		}
		
		logger.log(Level.SEVERE, "updateStaticDisplay End");
	}
	
	private void updateDynamicDisplay(LinkedList<Point> pointDynamics) {
		
		logger.log(Level.SEVERE, "updateDynamicDisplay Begin");
		
		if ( null != pointDynamics ) {
			
			logger.log(Level.SEVERE, "updateDynamicDisplay pointDynamics.size()["+ pointDynamics.size() +"] pointDisplays.size()["+ pointDisplays.size() +"]");
			
			Iterator<Point> iteratorDynamic = pointDynamics.iterator();
			while ( iteratorDynamic.hasNext() ) {
				Point pointDynamic = iteratorDynamic.next();
				
				if ( null != pointDynamic ) {

					{
						String [] keys = pointDynamic.getAttributeKeys();
						logger.log(Level.SEVERE, "updateDynamicDisplay pointDynamic.getAddress()["+pointDynamic.getAddress()+"] keys.length["+keys.length+"]");
						for ( String k : keys ) {
							logger.log(Level.SEVERE, "updateDynamicDisplay pointDynamic.getValue("+k+")["+pointDynamic.getValue(k)+"]");
						}
					}
					
					String address = pointDynamic.getAddress();
					
					if ( null != address ) {
						
						String value = pointDynamic.getValue(strValue);
						
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
											String v = UIPanelInspectorRTDBLogic.getArrayValues(valueTable, 4, r );
											logger.log(Level.SEVERE, "updateDynamicDisplay getArrayValues r["+r+"] v["+v+"] == valueTable[i]["+valueTable+"]");
											if ( 0 == v.compareTo(value) ) {
												name = UIPanelInspectorRTDBLogic.getArrayValues(valueTable, 1, r );
												break;
											}
										}
										
										if ( null != name ) {
											txtAttribute[i].setText(name);	
										} else {
											txtAttribute[i].setText(value);	
										}
										
										logger.log(Level.SEVERE, "getArrayValues value["+value+"] or name["+name+"]");
										
										String strColorCSS = getColorCSS(pointDynamic.getValue(strValueAlarmVector), pointDynamic.getValue(strValidity), pointDynamic.getValue(strForcedStatus));
										txtAttibuteColor[i].setStyleName(strColorCSS);
										
										logger.log(Level.SEVERE, "getArrayValues strColorCSS["+strColorCSS+"]");
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

	private static final int intMO = 2, intAI = 8, intSS = 512;
	private String getColorCSS(String alarmVector, String validity, String forcedStatus) {
		
		logger.log(Level.FINE, "getColorCSS Begin");
		
		String colorCSS	= strCSSStatusGrey;
		int intAlarmVector	= 0;
		int intValidity		= 0;
		int intForcedStatus	= 0;
		try {
			intAlarmVector	= Integer.parseInt(alarmVector.split(",")[1]);
			intValidity		= Integer.parseInt(validity);
			intForcedStatus	= Integer.parseInt(forcedStatus);
		} catch ( NumberFormatException e ) {
			logger.log(Level.FINE, "getColorCSS NumberFormatException["+e.toString()+"]");
		}
		
		// 2=MO, AI=8, 512=SS
		if ( (intForcedStatus & intMO) == intMO || (intForcedStatus & intAI) == intAI || (intForcedStatus & intSS) == intSS ) {
			colorCSS = strCSSStatusBlue;
			
		// 0=invalid, 1=valid	
		} else if ( intValidity == 0 ) {
			colorCSS = strCSSStatusGrey;
			
		// 0=normal, 1=alarm
		} else if ( intAlarmVector == 1 ) {
			colorCSS = strCSSStatusRed;
			
		// Grey
		} else {
			colorCSS = strCSSStatusGreen;
		}
		
		logger.log(Level.FINE, "getColorCSS colorCode["+colorCSS+"]");
		
		logger.log(Level.FINE, "getColorCSS End");

		return colorCSS;
	}
	
	FlexTable flexTableAttibutes = null;

	private TextBox txtAttribute[];
	private InlineLabel lblAttibuteLabel[];
	private InlineLabel txtAttibuteColor[];
	
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.SEVERE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		flexTableAttibutes = new FlexTable();
		
		txtAttribute = new TextBox[numOfPoints];
		lblAttibuteLabel = new InlineLabel[numOfPoints];
		txtAttibuteColor = new InlineLabel[numOfPoints];
		
		flexTableAttibutes = new FlexTable();
		
		FlexTable flexTableAttibutes = new FlexTable();
		flexTableAttibutes.setWidth("100%");
		for ( int i = 0; i < numOfPoints ; i ++ ) {
			
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
		basePanel.add(flexTableAttibutes);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.SEVERE, "getMainPanel End");
		
		return vp;
	}


}
