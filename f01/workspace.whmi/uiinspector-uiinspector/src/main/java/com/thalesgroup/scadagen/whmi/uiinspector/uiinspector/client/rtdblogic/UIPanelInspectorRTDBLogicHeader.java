package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ChildrenResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;

public class UIPanelInspectorRTDBLogicHeader {
	
	private static Logger logger = Logger.getLogger(UIPanelInspectorRTDBLogic.class.getName());
	
	private WrapperScsRTDBAccess wrapperScsRTDBAccess = null;
	
	private String scsEnvId		= null;
	private String dbaddress	= null;
	
	public void setScsEnvId(String scsEnvId) {
		this.scsEnvId = scsEnvId;
	}
	public void setDBaddress(String dbaddress) {
		this.dbaddress = dbaddress;
	}

	private LinkedList<String> dbaddresses					= new LinkedList<String>();
	private UINameCard uiNameCard = null;
	public UIPanelInspectorRTDBLogicHeader(UINameCard uiNameCard) {

		logger.log(Level.SEVERE, "UIPanelInspectorLogic Begin");
			
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendMgr(this);
		
		logger.log(Level.SEVERE, "UIPanelInspectorLogic End");
	}
	
	public void disconnect() {
		logger.log(Level.SEVERE, "disconnect Begin");
		
		wrapperScsRTDBAccess.remove(uiNameCard.getUiPath());
		
		logger.log(Level.SEVERE, "disconnect End");
	}
	
	public void connect() {
		
		logger.log(Level.SEVERE, "connect Begin");

		// Get Equipment Points
		{
			wrapperScsRTDBAccess = new WrapperScsRTDBAccess();
			
			logger.log(Level.SEVERE, "connect getChildren Begin");
			
			String clientkey = this.uiNameCard.getUiPath() + "_" + "getChildren" + "_" + this.dbaddress;
			wrapperScsRTDBAccess.getChildren(clientkey, this.scsEnvId, this.dbaddress, new ChildrenResult() {
				@Override
				public void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
					logger.log(Level.SEVERE, "setGetChildrenResult Begin");
					if ( null != instances ) {
						
						logger.log(Level.SEVERE, "setGetChildrenResult dbaddress["+dbaddress+"]");
						for ( int i = 0 ; i < instances.length ; ++i ) {
							logger.log(Level.SEVERE, "setGetChildrenResult instances("+i+")instances["+instances[i]+"]");
						}
						
						logger.log(Level.SEVERE, "setGetChildrenResult Begin");
					} else {
						logger.log(Level.SEVERE, "setGetChildrenResult instances IS NULL");
					}
					
					for(String instance: instances) {
						dbaddresses.add(instance);
					}
					
					notifyUIPanelInspectorRTDBLogicChildEvent(instances);
					
					logger.log(Level.SEVERE, "setGetChildrenResult Begin");
				}
			});
			
			logger.log(Level.SEVERE, "connect getChildren End");
		}
		
		logger.log(Level.SEVERE, "connect End");
	}
	
	private UIPanelInspectorRTDBLogicChildEvent uiPanelInspectorRTDBLogicChildEvent = null;
	public void setUIPanelInspectorRTDBLogicChildEvent(UIPanelInspectorRTDBLogicChildEvent uiPanelInspectorRTDBLogicChildEvent) { this.uiPanelInspectorRTDBLogicChildEvent = uiPanelInspectorRTDBLogicChildEvent; }
	private void notifyUIPanelInspectorRTDBLogicChildEvent(String[] instances) {
		logger.log(Level.SEVERE, "notifyUIPanelLogicDynamicDataEvent Begin");
		
		for ( int i = 0 ; i < instances.length ; ++i ) {
			logger.log(Level.SEVERE, "notifyUIPanelLogicDynamicDataEvent instances("+i+")["+instances[i]+"]");
		}

		if ( null != uiPanelInspectorRTDBLogicChildEvent ) uiPanelInspectorRTDBLogicChildEvent.ready(instances);
		
		logger.log(Level.SEVERE, "notifyUIPanelLogicDynamicDataEvent instances.length["+instances.length+"]");
		
		logger.log(Level.SEVERE, "notifyUIPanelLogicDynamicDataEvent End");
	}
}
