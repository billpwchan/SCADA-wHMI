package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ChildrenResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ReadResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.SubscriptionResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;

public class UIPanelInspectorRTDBLogic {
	
	private static Logger logger = Logger.getLogger(UIPanelInspectorRTDBLogic.class.getName());

	private WrapperScsRTDBAccess wrapperScsRTDBAccess = null;

	/*
	 * 1. setUINameCard
	 * 2. setConnection
	 * 3. setAttibuteStatic
	 * 4. setAttibuteDynamic
	 * setChildren
	 * 5. setUIPanelLogicStaticDataEvent
	 * 6. setUIPanelLogicDynamicDataEvent
	 * 7. readyToReadStaticData
			UIPanelLogicStaticDataEvent.ready
	 * 8. readyToSubscribeDynamicData
			UIPanelLogicDynamicDataEvent.update
	 * 9. removeDynamicSubscription
	 */
	
	private UINameCard uiNameCard = null;

	public void setUINameCard(UINameCard uiNameCard) {
		logger.log(Level.SEVERE, "setUINameCard Begin");
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendMgr(this);
		logger.log(Level.SEVERE, "setUINameCard End");
	}
	
	private String scsEnvId = null;
	private String dbaddress = null;
	
	public void setConnection(String scsEnvId, String dbaddress) {
		logger.log(Level.SEVERE, "setConnection Begin");
		this.scsEnvId = scsEnvId;
		this.dbaddress = dbaddress;
		
		logger.log(Level.SEVERE, "setConnection this.scsEnvId["+this.scsEnvId+"] this.dbaddress["+this.dbaddress+"]");
		
		this.wrapperScsRTDBAccess = WrapperScsRTDBAccess.getInstance();
		
		logger.log(Level.SEVERE, "setConnection End");
	}
	
	private HashMap<String, String[]> attributes = new HashMap<String, String[]>();
	public String[] getAttibute( String type ) { return this.attributes.get(type); }
	public void setAttibute ( String type, String[] attributes ) { this.attributes.put(type, attributes); }
	
	public void removeDynamicSubscription() {
		logger.log(Level.SEVERE, "removeDynamicSubscription Begin");
		
		this.wrapperScsRTDBAccess.remove(uiNameCard.getUiPath());
		
		logger.log(Level.SEVERE, "removeDynamicSubscription End");
	}
	
	private UIPanelLogicChildrenDataEvent uiPanelLogicChildrenDataEvent = null;
	public void setUIPanelLogicChildrenDataEvent(UIPanelLogicChildrenDataEvent uiPanelLogicChildrenDataEvent) { this.uiPanelLogicChildrenDataEvent = uiPanelLogicChildrenDataEvent; }
	private void notifyUIPanelLogicChildrenDataEvent() {
		logger.log(Level.SEVERE, "notifyUIPanelLogicStaticDataEvent Begin");
		
		String[] instances = getEqupmentDBAddressesValue(this.dbaddress);
		
		if ( null != uiPanelLogicChildrenDataEvent ) uiPanelLogicChildrenDataEvent.ready(instances);
		
		logger.log(Level.SEVERE, "notifyUIPanelLogicStaticDataEvent instances.size()["+instances.length+"]");
	}
	
	private UIPanelLogicStaticDataEvent uiPanelLogicStaticDataEvent = null;
	public void setUIPanelLogicStaticDataEvent(UIPanelLogicStaticDataEvent uiPanelLogicStaticDataEvent) { this.uiPanelLogicStaticDataEvent = uiPanelLogicStaticDataEvent; }
	private void notifyUIPanelLogicStaticDataEvent() {
		logger.log(Level.SEVERE, "notifyUIPanelLogicStaticDataEvent Begin");
		
		String[] instances = getEqupmentDBAddressesValue(this.dbaddress);
		LinkedList<Point> pointStatics = new LinkedList<Point>();
		for ( int x = 0 ; x < instances.length ; ++x ) {
			Point point = new Point(instances[x], this.getAttibute(UIPanelInspectorRTDBLogic_i.strStatic));
			for(String string: point.getAttributeKeys() ) {
				String key = point.getAddress()+string;
				String value = getAttributesStaticValue(key);
				point.setValue(string, value);
			}
			pointStatics.add(point);
		}
		
		if ( null != uiPanelLogicStaticDataEvent ) uiPanelLogicStaticDataEvent.ready(pointStatics); 
		
		logger.log(Level.SEVERE, "notifyUIPanelLogicStaticDataEvent pointStatics.size()["+pointStatics.size()+"]");
	}
	
	private UIPanelLogicDynamicDataEvent uiPanelLogicDynamicDataEvent = null;
	public void setUIPanelLogicDynamicDataEvent(UIPanelLogicDynamicDataEvent uiPanelLogicDynamicDataEvent) { this.uiPanelLogicDynamicDataEvent = uiPanelLogicDynamicDataEvent; }
	private void notifyUIPanelLogicDynamicDataEvent() {
		logger.log(Level.SEVERE, "notifyUIPanelLogicDynamicDataEvent Begin");
		
		String[] pointDbAddresses = this.getEqupmentDBAddressesValue(this.dbaddress);
		LinkedList<Point> pointDynamics = new LinkedList<Point>();
		for ( int x = 0 ; x < pointDbAddresses.length ; ++x ) {
			Point pointDynamic = new Point(pointDbAddresses[x], this.getAttibute(UIPanelInspectorRTDBLogic_i.strDynamic));
			for(String string: pointDynamic.getAttributeKeys() ) {
				String key = pointDynamic.getAddress()+string;
				String value = getAttributesDynamicValue(key);
				pointDynamic.setValue(string, value);
			}
			pointDynamics.add(pointDynamic);
		}
		if ( null != uiPanelLogicDynamicDataEvent ) uiPanelLogicDynamicDataEvent.update(pointDynamics);
		
		logger.log(Level.SEVERE, "notifyUIPanelLogicDynamicDataEvent End");
	}
	
	public void readyToReadChildrenData() {
		logger.log(Level.SEVERE, "readyToReadStaticData Begin");

		requestChildrenData();

		logger.log(Level.SEVERE, "readyToReadStaticData End");
	}
	
	public void readyToReadStaticData() {
		logger.log(Level.SEVERE, "readyToReadStaticData Begin");

		prepareStaticRequest();
		requestStaticData();

		logger.log(Level.SEVERE, "readyToReadStaticData End");
	}

	public void readyToSubscribeDynamicData() {
		logger.log(Level.SEVERE, "readyToSubscribeDynamicData Begin");
		
		prepareDynmaicRequest();
		requestDynmaicData();
		
		logger.log(Level.SEVERE, "readyToSubscribeDynamicData End");
	}

	// Equipment List
	private LinkedHashMap<String, String[]> equpmentDBAddresses = null;
	public Set<String> getEqupmentDBAddressesValueKeys() { return equpmentDBAddresses.keySet(); }
	public String[] getEqupmentDBAddressesValue(String key) { return equpmentDBAddresses.get(key); }
	
	// Attribute (Static)
	private LinkedHashMap<String, String[]>	attributeStaticDBAddresses = null;
	private LinkedList<String> attributeStaticDBAddressesToRead = null;
	
	private HashMap<String, String> attributesStaticValues = null;
	public Set<String> getAttributesStaticValueKeys() { return attributesStaticValues.keySet(); }
	public String getAttributesStaticValue(String key) { return attributesStaticValues.get(key); }

	// Attribute (Dynamic)	
	private LinkedHashMap<String, String[]> attributesDynamicDBAddress = null;
	private LinkedList<String> pointsDBAddressesDynamicToRead = null;
	
	private HashMap<String, String> attributesDynamicValues = null;
	public Set<String> getAttributesDynamicValueKeys() { return attributesDynamicValues.keySet(); }
	public String getAttributesDynamicValue(String key) { return attributesDynamicValues.get(key); }
	
	private void requestChildrenData() {
		logger.log(Level.SEVERE, "requestChildrenData Begin");
		String clientkey = this.uiNameCard.getUiPath() + UIPanelInspectorRTDBLogic_i.strUnderscore + "getChildren" + UIPanelInspectorRTDBLogic_i.strUnderscore + this.dbaddress;
		wrapperScsRTDBAccess.getChildren(clientkey, this.scsEnvId, this.dbaddress, new ChildrenResult() {
			@Override
			public void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
				processChildrenResult(clientKey, instances, errorCode, errorMessage);
				notifyUIPanelLogicChildrenDataEvent();
			}
		});
		logger.log(Level.SEVERE, "requestChildrenData End");
	}
	
	private void prepareChildrenResult(String[] instances) {
		logger.log(Level.SEVERE, "fillChildrenResult Begin");
		this.equpmentDBAddresses = new LinkedHashMap<String, String[]>();
		this.equpmentDBAddresses.put(this.dbaddress, instances);
		for ( int i = 0 ; i < instances.length ; ++i ) {
			logger.log(Level.SEVERE, "fillChildrenResult this.dbaddress["+this.dbaddress+"], instances["+instances[i]+"]");
		}
		logger.log(Level.SEVERE, "fillChildrenResult End");
	}
	
	private void processChildrenResult(String clientkey, String[] instances, int errorCode, String errorMessage) {
		logger.log(Level.SEVERE, "processChildrenResult Begin");
		if ( null != instances ) {
			this.equpmentDBAddresses = new LinkedHashMap<String, String[]>();
			this.equpmentDBAddresses.put(this.dbaddress, instances);
			for ( int i = 0 ; i < instances.length ; ++i ) {
				logger.log(Level.SEVERE, "processChildrenResult this.dbaddress["+this.dbaddress+"], instances["+instances[i]+"]");
			}
		} else {
			logger.log(Level.SEVERE, "processChildrenResult instances IS NULL");
		}
		logger.log(Level.SEVERE, "processChildrenResult End");
	}
	
	private void prepareStaticRequest() {
		logger.log(Level.SEVERE, "prepareStaticRequest Begin");
		this.attributeStaticDBAddressesToRead = new LinkedList<String>();
		this.attributeStaticDBAddresses = new LinkedHashMap<String, String[]>();
		if ( null != equpmentDBAddresses ) {
			Iterator<String> iterator = this.equpmentDBAddresses.keySet().iterator();
			while(iterator.hasNext()) {
				String dbaddress = iterator.next();
				String[] points = equpmentDBAddresses.get(dbaddress);
				for ( int x = 0 ; x < points.length ; ++x ) {
					String [] attributes = this.attributes.get(UIPanelInspectorRTDBLogic_i.strStatic);
					for ( int y = 0 ; y < attributes.length ; ++y ) {
						attributeStaticDBAddressesToRead.add(points[x]+attributes[y]);	
						
						logger.log(Level.SEVERE, "prepareStaticRequest points[x]["+points[x]+"]+attributes[y]["+attributes[y]+"] => ["+points[x]+attributes[y]+"]");
					}
					this.attributeStaticDBAddresses.put(points[x], attributeStaticDBAddressesToRead.toArray(new String[0]));
				}
			}
		} else {
			logger.log(Level.SEVERE, "prepareStaticRequest equpmentDBAddresses IS NULL");
		}
		logger.log(Level.SEVERE, "prepareStaticRequest End");
	}
	
	private void requestStaticData() {
		logger.log(Level.SEVERE, "requestStaticData Begin");
		String clientkey = this.uiNameCard.getUiPath() + UIPanelInspectorRTDBLogic_i.strUnderscore + "multiReadValueRequestCaches" + UIPanelInspectorRTDBLogic_i.strUnderscore + this.dbaddress;
		wrapperScsRTDBAccess.multiReadValueRequestCaches(clientkey, this.scsEnvId, attributeStaticDBAddressesToRead.toArray(new String[0]), ReadResult.class.getName(), new ReadResult() {
			@Override
			public void setReadResult(String clientKey, String[][] values, int errorCode, String errorMessage) {
				processStaticData(clientKey, values, errorCode, errorMessage);
				notifyUIPanelLogicStaticDataEvent();
			}
		});
		logger.log(Level.SEVERE, "requestStaticData End");
	}
	
	private void processStaticData(String clientKey, String[][] values, int errorCode, String errorMessage) {
		logger.log(Level.SEVERE, "processStaticData Begin");
		attributesStaticValues = new HashMap<String, String>();
		if ( null != values && values.length > 0 ) {
			for ( int x = 0 ; x < values.length ; ++x ) {
				if ( 2 == values[x].length ) {
					attributesStaticValues.put(values[x][0], values[x][1]);
					logger.log(Level.SEVERE, "processStaticData values["+x+"][0]["+values[x][0]+"] values["+x+"][1]["+values[x][1]+"]");
				} else {
					for ( int y = 0 ; y < values[x].length ; ++y ) {
						logger.log(Level.SEVERE, "processStaticData values["+x+"]["+y+"]["+values[x][y]+"]");
					}
				}

			}
		}
		logger.log(Level.SEVERE, "processStaticData End");
	}
	
	private void prepareDynmaicRequest() {
		logger.log(Level.SEVERE, "prepareDynmaicRequest End");
		this.pointsDBAddressesDynamicToRead = new LinkedList<String>();
		this.attributesDynamicDBAddress = new LinkedHashMap<String, String[]>();
		if ( null != equpmentDBAddresses ) {
			Iterator<String> iterator = this.equpmentDBAddresses.keySet().iterator();
			while(iterator.hasNext()) {
				String dbaddress = iterator.next();
				String[] points = equpmentDBAddresses.get(dbaddress);
				for ( int x = 0 ; x < points.length ; ++x ) {
					String [] attributes = this.attributes.get(UIPanelInspectorRTDBLogic_i.strDynamic);
					for ( int y = 0 ; y < attributes.length ; ++y ) {
						pointsDBAddressesDynamicToRead.add(points[x]+attributes[y]);
						
						logger.log(Level.SEVERE, "prepareDynmaicRequest points[x]["+points[x]+"]+attributes[y]["+attributes[y]+"] => ["+points[x]+attributes[y]+"]");
					}
					this.attributesDynamicDBAddress.put(points[x], pointsDBAddressesDynamicToRead.toArray(new String[0]));
				}
			}
		} else {
			logger.log(Level.SEVERE, "prepareDynmaicRequest equpmentDBAddresses IS NULL");
		}
		logger.log(Level.SEVERE, "prepareDynmaicRequest End");
	}
	
	private void requestDynmaicData() {
		logger.log(Level.SEVERE, "requestDynmaicData Begin");
		String clientkey = this.uiNameCard.getUiPath() + UIPanelInspectorRTDBLogic_i.strUnderscore + "subscriptionRequest" + UIPanelInspectorRTDBLogic_i.strUnderscore + this.dbaddress;
		wrapperScsRTDBAccess.subscriptionRequest(clientkey, this.scsEnvId, pointsDBAddressesDynamicToRead.toArray(new String[0]), new SubscriptionResult() {
			@Override
			public void setReadResultSubscription(String clientKey, String[][] values, int errorCode, String errorMessage) {
				processDynamicData(clientKey, values, errorCode, errorMessage);
				notifyUIPanelLogicDynamicDataEvent();
			}
		});
		logger.log(Level.SEVERE, "requestDynmaicData End");
	}
	
	private void processDynamicData(String clientKey, String[][] values, int errorCode, String errorMessage) {
		logger.log(Level.SEVERE, "processDynamicData Begin");
		attributesDynamicValues = new HashMap<String, String>();
		if ( null != values ) {
			for ( int x = 0 ; x < values.length ; ++x ) {
				if ( 2 == values[x].length ) {
					attributesDynamicValues.put(values[x][0], values[x][1]);
					logger.log(Level.SEVERE, "processDynamicData values["+x+"][0]["+values[x][0]+"] values["+x+"][1]["+values[x][1]+"]");
				} else {				
					for ( int y = 0 ; y < values[x].length ; ++y ) {
						logger.log(Level.SEVERE, "processDynamicData values["+x+"]["+y+"]["+values[x][y]+"]");
					}
				}
			}
		}
		logger.log(Level.SEVERE, "processDynamicData End");		
	}

	public static String getArrayValues(String string, int row, int col) {
		String str = null;
		
		logger.log(Level.FINE, "getArrayValues Begin");
		logger.log(Level.FINE, "getArrayValues string["+string+"] x["+row+"] y["+row+"]");
				
		if (null != string && string.length() > 0) {
			if (string.charAt(0) == '[')
				string = string.substring(1);
			if (string.charAt(string.length() - 1) == ']')
				string = string.substring(0, string.length() - 1);
			String[] strs = string.split("\\],\\[");
			if (strs.length > 0 && row < strs.length) {
				String s = strs[row];
				logger.log(Level.FINE, "getArrayValues s["+s+"]");
				//for (String s : strs) {
					//System.out.println("s [" + s + "]");
					String str2s[] = s.split(",");
					if ( str2s.length > 0 && col < str2s.length ) {
						str = str2s[col];
						logger.log(Level.FINE, "getArrayValues str["+str+"]");
						//for (String s2 : str2s) {
						//	System.out.println("s2 [" + s2 + "]");
						//}						
					}
				//}
			} else {
				// Invalid str length or index
				logger.log(Level.SEVERE, "getArrayValues Invalid str length or index");
			}
		}
		
		logger.log(Level.FINE, "getArrayValues End");

		return str;
	}

}
