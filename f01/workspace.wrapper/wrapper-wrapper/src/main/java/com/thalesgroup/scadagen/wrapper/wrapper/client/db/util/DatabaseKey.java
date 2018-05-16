package com.thalesgroup.scadagen.wrapper.wrapper.client.db.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class DatabaseKey {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private String strDateFormat = "yyyyMMddHHmmssSSS";
	public void setDateTimeFormat(String strDateFormat) { this.strDateFormat = strDateFormat; }
	
	private String strSpliter = "_";
	public void setSpliter(String strSpliter) { this.strSpliter = strSpliter; }
	
	public String getDateTimeString() {
		String function = "getDateTimeString";
		logger.begin(function);
		Date date = new Date();
		DateTimeFormat dtf = DateTimeFormat.getFormat(strDateFormat);
		String ret = dtf.format(date, TimeZone.createTimeZone(0));
		logger.debug(function, "ret[{}]", ret);
		logger.end(function);
		return ret;
	}
	
	public String getRandomString() {
		String function = "getRandomString";
		logger.begin(function);
		String ret = String.valueOf(Math.floor(100000 + Math.random() * 900000));
		logger.debug(function, "ret[{}]", ret);
		logger.end(function);
		return ret;
	}
	
	/**
	 * Return the key with current datatime string as prefix, append and concat parameters with "_"
	 * 
	 * @param className Class calling from
	 * @param function Function calling from
	 * @param scsEnvId ScsEnvId to request for database operation
	 * @param dbAddress DbAddress to request for database operation
	 * @return Return the key in String
	 */
	public String getKey(final String className, final String function, final String scsEnvId, final String dbAddress) {
		return getDateTimeString()+strSpliter+getRandomString()+strSpliter+className+strSpliter+function+strSpliter+scsEnvId+strSpliter+dbAddress;
	}
	
	/**
	 * Same as API getKey(final String function, final String scsEnvId, final String dbAddress);
	 * Append and concat "value" parameter with "_"
	 * 
	 * @param className Class calling from
	 * @param function Function calling from
	 * @param scsEnvId ScsEnvId to request for database operation
	 * @param dbAddress DbAddress to request for database operation
	 * @param value Value to request for database operation
	 * @return Return the key in String
	 */
	public String getKey(final String className, final String function, final String scsEnvId, final String dbAddress, final String value) {
		return getKey(className, function, scsEnvId, dbAddress)+strSpliter+value;
	}
}
