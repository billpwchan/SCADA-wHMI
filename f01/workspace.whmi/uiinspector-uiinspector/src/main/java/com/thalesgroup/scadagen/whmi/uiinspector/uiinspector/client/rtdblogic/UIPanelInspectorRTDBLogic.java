package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.storage.Point;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ReadResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.SubscriptionResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;

public class UIPanelInspectorRTDBLogic {
	
	private static Logger logger = Logger.getLogger(UIPanelInspectorRTDBLogic.class.getName());
	
	private WrapperScsRTDBAccess wrapperScsRTDBAccess = null;
	private WrapperScsRTDBAccess wrapperScsRTDBAccessSubsecription = null;
	
	private String scsEnvId			= null;
	private String parent			= null;
	private String[] dbaddresses	= null;
	
	public void setScsEnvId(String scsEnvId) {
		this.scsEnvId = scsEnvId;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public void setDBaddresses(String[] dbaddresses) {
		this.dbaddresses = dbaddresses;
	}
	
	// Static Attribute List
	private String staticAttibutes[]	= null;
	public void setStaticAttributes(String[] attributes)	{ staticAttibutes = attributes; }

	// Dynamic Attribute List
	private String dynamicAttibutes[]	= null;
	public void setDynamicAttributes(String[] attributes)	{ dynamicAttibutes = attributes; }
	
	// Static List to Read DB
	private LinkedList<String> dbstatics			= new LinkedList<String>();
	
	// Subscription List to Scribe
	private LinkedList<String> dbsubscriptions		= new LinkedList<String>();
	
	private LinkedHashMap<String, String> dbstaticvalues	= new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> dbdynamicvalues	= new LinkedHashMap<String, String>();
	
	private UINameCard uiNameCard = null;
	public UIPanelInspectorRTDBLogic(UINameCard uiNameCard) {

		logger.log(Level.SEVERE, "UIPanelInspectorLogic Begin");
			
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendMgr(this);
		
		logger.log(Level.SEVERE, "UIPanelInspectorLogic End");
	}
	
	public void disconnect() {
		logger.log(Level.SEVERE, "disconnect Begin");
		
		wrapperScsRTDBAccess.remove(uiNameCard.getUiPath());
		wrapperScsRTDBAccessSubsecription.remove(uiNameCard.getUiPath());
		
		logger.log(Level.SEVERE, "disconnect End");
	}
	
	public void connect() {
		
		logger.log(Level.SEVERE, "connect Begin");
		
		{
			logger.log(Level.SEVERE, "building static points Begin");
			
			for ( int i = 0 ; i < staticAttibutes.length ; ++i ) {
				
				for ( String dbaddress : this.dbaddresses ) {
					
					String dbattribute = dbaddress + staticAttibutes[i];
				
					dbstatics.add(dbattribute);	
				
					logger.log(Level.SEVERE, "building static points: i["+i+"] dbaddress["+dbaddress+"]["+staticAttibutes[i]+"]");
					logger.log(Level.SEVERE, "building static points: i["+i+"] dbattribute["+dbattribute+"]");
				}
				

			}
			logger.log(Level.SEVERE, "building static points End");
		}
		
		{
//			WrapperScsRTDBAccess wrapperScsRTDBAccess = WrapperScsRTDBAccess.getInstance();
			wrapperScsRTDBAccess = new WrapperScsRTDBAccess();
			
			logger.log(Level.SEVERE, "requestStaticData Begin");
			String clientkey = this.uiNameCard.getUiPath() + "_" + "multiReadValueRequestCaches" + "_" + this.parent;
			wrapperScsRTDBAccess.multiReadValueRequestCaches(clientkey, this.scsEnvId, dbstatics.toArray(new String[0]), ReadResult.class.getName(), new ReadResult() {
				@Override
				public void setReadResult(String clientKey, String[][] values, int errorCode, String errorMessage) {

					if ( null != values ) {
						for ( int x = 0 ; x < values.length ; ++x ) {
							if ( 2 == values[x].length ) {
								dbstaticvalues.put(values[x][0], values[x][1]);
								logger.log(Level.SEVERE, "processStaticData values["+x+"][0]["+values[x][0]+"] values["+x+"][1]["+values[x][1]+"]");
							} else {
								for ( int y = 0 ; y < values[x].length ; ++y ) {
									logger.log(Level.SEVERE, "processStaticData values["+x+"]["+y+"]["+values[x][y]+"]");
								}
							}

						}
					}
					
					notifyUIPanelInspectorRTDBLogicStaticEvent(staticAttibutes, dbstaticvalues);
				}
			});
			logger.log(Level.SEVERE, "requestStaticData End");
		}
		
		{
			logger.log(Level.SEVERE, "building dynamic points Begin");
			
			for ( String dbaddress : dbaddresses ) {
				for ( int i = 0 ; i < dynamicAttibutes.length ; ++i ) {
					
					String dbattribute = dbaddress + dynamicAttibutes[i];
					
					dbsubscriptions.add(dbattribute);
	
					logger.log(Level.SEVERE, "building dynamic points: i["+i+"] dbaddress["+dbaddress+"]["+dynamicAttibutes[i]+"]");
					logger.log(Level.SEVERE, "building dynamic points: i["+i+"] dbattribute["+dbattribute+"]");
				}				
			}

			logger.log(Level.SEVERE, "building dynamic points End");
		}
		
		{
//			WrapperScsRTDBAccess wrapperScsRTDBAccess = WrapperScsRTDBAccess.getInstance();
			wrapperScsRTDBAccessSubsecription = new WrapperScsRTDBAccess();
			
			logger.log(Level.SEVERE, "requestDynmaicData Begin");
			String clientkey = this.uiNameCard.getUiPath() + "_" + "subscriptionRequest" + "_" + this.parent;
			wrapperScsRTDBAccessSubsecription.subscriptionRequest(clientkey, this.scsEnvId, dbsubscriptions.toArray(new String[0]), new SubscriptionResult() {
				@Override
				public void setReadResultSubscription(String clientKey, String[][] values, int errorCode, String errorMessage) {
					if ( null != values ) {
						for ( int x = 0 ; x < values.length ; ++x ) {
							if ( 2 == values[x].length ) {
								dbdynamicvalues.put(values[x][0], values[x][1]);
								logger.log(Level.SEVERE, "processStaticData values["+x+"][0]["+values[x][0]+"] values["+x+"][1]["+values[x][1]+"]");
							} else {
								for ( int y = 0 ; y < values[x].length ; ++y ) {
									logger.log(Level.SEVERE, "processStaticData values["+x+"]["+y+"]["+values[x][y]+"]");
								}
							}

						}
					}
					
					notifyUIPanelInspectorRTDBLogicDynamicEvent(dynamicAttibutes, dbdynamicvalues);
				}
			});
			logger.log(Level.SEVERE, "requestDynmaicData End");
		}
		
		logger.log(Level.SEVERE, "connect End");
	}
	
	private Point[] filledPointList(String[] addresses, String[] attibutes, LinkedHashMap<String, String> dbvalues) {
		
		logger.log(Level.SEVERE, "filledPointList Begin");
		
		LinkedList<Point> points = new LinkedList<Point>();
		
		for ( String address : addresses ) {
			Point point = new Point(address, attibutes);
			logger.log(Level.SEVERE, "filledPointList address["+address+"]");
			for ( String attribute : attibutes ) {
				String dbaddress = address+attribute;
				String value = dbvalues.get(dbaddress);
				point.setValue(attribute, value);
				
				logger.log(Level.SEVERE, "filledPointList address["+address+"] + attribute["+attribute+"] => dbaddress["+dbaddress+"] : value["+value+"]");
			}
			
			points.add(point);
		}
	
		logger.log(Level.SEVERE, "filledPointList points.size()["+points.size()+"]");
		
		logger.log(Level.SEVERE, "filledPointList End");
		
		return points.toArray(new Point[0]);
	}
	
	private UIPanelInspectorRTDBLogicStaticEvent uiPanelInspectorRTDBLogicStaticEvent = null;
	public void setUIPanelInspectorRTDBLogicStaticEvent(UIPanelInspectorRTDBLogicStaticEvent uiPanelInspectorRTDBLogicStaticEvent) { this.uiPanelInspectorRTDBLogicStaticEvent = uiPanelInspectorRTDBLogicStaticEvent; }
	private void notifyUIPanelInspectorRTDBLogicStaticEvent(String [] staticAttibutes, LinkedHashMap<String, String> dbstaticvalues) {
		logger.log(Level.SEVERE, "notifyUIPanelLogicStaticDataEvent Begin");
		
		Point[] points = filledPointList(this.dbaddresses, staticAttibutes, dbstaticvalues);

		logger.log(Level.SEVERE, "notifyUIPanelLogicStaticDataEvent points.length()["+points.length+"]");
		
		if ( null != uiPanelInspectorRTDBLogicStaticEvent ) uiPanelInspectorRTDBLogicStaticEvent.ready(points); 
		
		logger.log(Level.SEVERE, "notifyUIPanelLogicStaticDataEvent End");
	}
	
	private UIPanelInspectorRTDBLogicDynamicEvent uiPanelInspectorRTDBLogicDynamicEvent = null;
	public void setUIPanelInspectorRTDBLogicDynamicEventt(UIPanelInspectorRTDBLogicDynamicEvent uiPanelInspectorRTDBLogicDynamicEvent) { this.uiPanelInspectorRTDBLogicDynamicEvent = uiPanelInspectorRTDBLogicDynamicEvent; }
	private void notifyUIPanelInspectorRTDBLogicDynamicEvent(String[] dynamicAttibutes, LinkedHashMap<String, String> dbdynamicvalues) {
		logger.log(Level.SEVERE, "notifyUIPanelLogicDynamicDataEvent Begin");
		
		Point[] points = filledPointList(this.dbaddresses, dynamicAttibutes, dbdynamicvalues);
		
		logger.log(Level.SEVERE, "notifyUIPanelLogicDynamicDataEvent points.length()["+points.length+"]");

		if ( null != uiPanelInspectorRTDBLogicDynamicEvent ) uiPanelInspectorRTDBLogicDynamicEvent.update(points);
		
		logger.log(Level.SEVERE, "notifyUIPanelLogicDynamicDataEvent End");
	}

}
