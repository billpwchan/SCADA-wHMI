package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class HVID2SCS {
	private final String className = UIWidgetUtil.getClassSimpleName(HVID2SCS.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String hvid = null;
	public void setHVID(String hvid) {
		this.hvid = hvid;
	}
	
	private String dbaddress = null;
	public String getDBAddress() {
		final String function = "getDBAddress";
		logger.beginEnd(className, function, "this.dbaddress[{}]", this.dbaddress);
		return this.dbaddress; 
		
	}
	
	private String scsEnvId = null;
	public String getScsEnvID() { 
		final String function = "getScsEnvID";
		logger.beginEnd(className, function, "this.scsEnvId[{}]", this.scsEnvId);
		return this.scsEnvId; 
	}
	
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		logger.debug(className, function, "this.hvid[{}]", hvid);
		
		String dbaddresses	= null;
		String hvids[] = hvid.split("_");
		if ( null != hvids ) {
			if ( hvids.length >= 1 && null != hvids[0] ) {
				this.scsEnvId = hvids[0];
			}
			dbaddresses = "";
			for ( int i = 1 ; i < hvids.length ; i++ ) {
				if ( dbaddresses.length() > 0 ) {
					dbaddresses += "_";
				}
				dbaddresses += hvids[i];
			}
			if ( dbaddresses.length() > 0 ) {
				
				int s = 0, n = 0;
				
				s = n;
				n = s+3;
				String dbaddress0	= dbaddresses.substring(s, n);
				
				s = n;
				n = s+3;
				String dbaddress1	= dbaddresses.substring(s, n);
				
				s = n;
				n = s+3;
				String dbaddress2	= dbaddresses.substring(s);
				
				this.dbaddress	= ":" + dbaddress0 + ":" + dbaddress1 + ":" + dbaddress2;
			} else {
				logger.warn(className, function, "dbaddresses.length() == 0");
			}
			
		} else {
			logger.warn(className, function, "hvids IS NULL");
		}
		
		logger.end(className, function);
	}

}
