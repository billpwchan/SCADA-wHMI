package com.thalesgroup.scadagen.wrapper.wrapper.client.db.util;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.HVID2SCS_i.Pattern;

public class HVID2SCS {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
	public final static String STR_SPLITER = "_";
	public final static String STR_ALIAS = "<alias>";
	
	private String scsEnvId = null;
	public String getScsEnvID() { return this.scsEnvId; }
	public void setScsEnvID(String scsEnvId) { this.scsEnvId = scsEnvId; }
	
	private String dbaddress = null;
	public String getDBAddress() { return this.dbaddress; }
	public void setDBAddress(String dbaddress) { this.dbaddress = dbaddress; }
	
	public interface Handler {
		void convert(String hvid);
	}
	private Map<String, Handler> map = new HashMap<String, Handler>();
	public void addHandler(String key, Handler handler) { this.map.put(key, handler); }
	
	public HVID2SCS(String hvid, String pattern) {
		final String function = "HVID2SCS";
		logger.begin(className, function);
		logger.debug(className, function, "hvid[{}] pattern[{}]", hvid, pattern);
		
		init();
		
		scsEnvId = hvid;
		dbaddress = hvid;
		
		Handler handler = map.get(pattern);
		if ( null != handler ) {
			handler.convert(hvid);
		}
		else {
			logger.warn(className, function, "pattern[{}] NOT FOUND!", pattern);
		}
		
		logger.debug(className, function, "scsEnvId[{}] dbaddress[{}]", scsEnvId, dbaddress);
		
		logger.end(className, function);
	}

	public void init() {
		
		addHandler(Pattern.HVID_ALIAS.toString(), new Handler() {
			
			@Override
			public void convert(String hvid) {
				final String function = "conver "+Pattern.HVID_ALIAS.toString();
				logger.begin(className, function);
				logger.debug(className, function, "hvid[{}]", hvid);
				if ( null != hvid ) {
					int charAt = hvid.indexOf(String.valueOf(STR_SPLITER));
					if ( charAt > -1 ) {
						dbaddress = STR_ALIAS+hvid.substring(charAt+1);
					}
				} else {
					logger.warn(className, function, "hvids IS NULL");
				}
				logger.end(className, function);
			}
		});
		
		addHandler(Pattern.HVID_PATH.toString(), new Handler() {
			
			@Override
			public void convert(String hvid) {
				final String function = "conver "+Pattern.HVID_ALIAS.toString();
				logger.begin(className, function);
				logger.debug(className, function, "hvid[{}]", hvid);
				if ( null != hvid ) {
					int charAt = hvid.indexOf(String.valueOf(STR_SPLITER));
					if ( charAt > -1 ) {
						dbaddress = hvid.substring(charAt+1);
					}
				} else {
					logger.warn(className, function, "hvids IS NULL");
				}
				logger.end(className, function);
			}
		});
		
		addHandler(Pattern.ENV_ALIAS.toString(), new Handler() {
			
			@Override
			public void convert(String hvid) {
				final String function = "convert "+Pattern.ENV_ALIAS.toString();
				logger.begin(className, function);
				logger.debug(className, function, "hvid[{}]", hvid);
				if ( null != hvid ) {
					int charAt = hvid.indexOf(String.valueOf(STR_SPLITER));
					if ( charAt > -1 ) {
						scsEnvId = hvid.substring(0, charAt);
						dbaddress = STR_ALIAS+hvid.substring(charAt+1);
					}
				} else {
					logger.warn(className, function, "hvids IS NULL");
				}
				logger.end(className, function);
			}
		});
		
		addHandler(Pattern.ENV_PATH.toString(), new Handler() {
			
			@Override
			public void convert(String hvid) {
				final String function = "convert "+Pattern.ENV_ALIAS.toString();
				logger.begin(className, function);
				logger.debug(className, function, "hvid[{}]", hvid);
				if ( null != hvid ) {
					int charAt = hvid.indexOf(String.valueOf(STR_SPLITER));
					if ( charAt > -1 ) {
						scsEnvId = hvid.substring(0, charAt);
						dbaddress = hvid.substring(charAt+1);
					}
				} else {
					logger.warn(className, function, "hvids IS NULL");
				}
				logger.end(className, function);
			}
		});
	}

}
