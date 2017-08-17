package com.thalesgroup.scadagen.wrapper.wrapper.client.db.util;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.HVID2SCS_i.Pattern;

public class HVID2SCS {
	
	private final String className = UIWidgetUtil.getClassSimpleName(HVID2SCS.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public final static String STR_SPLITER = "_";
	
	private String scsEnvId = null;
	public String getScsEnvID() { return this.scsEnvId; }
	
	private String dbaddress = null;
	public String getDBAddress() {  return this.dbaddress; }
	
	public interface Handler {
		void conver(String hvid);
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
			handler.conver(hvid);
		}
		else {
			logger.warn(className, function, "pattern[{}] NOT FOUND!", pattern);
		}
		
		logger.end(className, function);
	}

	public void init() {
		
		addHandler(Pattern.SCADAgen.toString(), new Handler() {
			
			@Override
			public void conver(String hvid) {
				final String function = "HVID2SCS";
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
	}

}
