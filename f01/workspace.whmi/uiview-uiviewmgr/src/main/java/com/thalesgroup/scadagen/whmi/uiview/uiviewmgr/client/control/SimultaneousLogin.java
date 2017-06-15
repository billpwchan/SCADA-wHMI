package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSimultaneousLoginControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSimultaneousLoginControl_i.OpmIdendifyType;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class SimultaneousLogin {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetSimultaneousLoginControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String dictionariesCacheNameValue = "UIJson";
	private String fileNameValue = "simultaneousLogin.json";
	private String arrayKey = "GWS";
	private String arrayIndexKeyValue = "Key";
	
	private String strAlias = "Alias";
	private String strScsEnvId = "ScsEnvId";
	
	private final String strOpmApi = "OpmApi";
	private final String strOpmIdentityType = "OpmIdentityType";
	
	public String getStringFromJsonArray(String dictionariesCacheName, String fileName, String arrayKey, String arrayIndexKeyValue, String arrayIndexKey, String valueKey) {
		final String function = "getStringFromJsonArray";
		logger.begin(className, function);
		
		logger.debug(className, function, "dictionariesCacheNameValue[{}] fileNameValue[{}] arrayKey[{}] arrayIndexKeyValue[{}] arrayIndexKey[{}] arrayIndexKey[{}] valueKey"
				, new Object[]{dictionariesCacheNameValue, fileNameValue, arrayKey, arrayIndexKeyValue, arrayIndexKey, arrayIndexKey, valueKey});
		
		JSONArray jsonArray = ReadJson.readArray(dictionariesCacheName, fileName, arrayKey);
		
		JSONObject jsonObject = ReadJson.readObject(jsonArray, arrayIndexKeyValue, arrayIndexKey);
		
		String value = ReadJson.readString(jsonObject, valueKey);
		logger.end(className, function);
		return value;
	}
	
	public String getStringFromJson(String dictionariesCacheNameValue, String fileNameValue, String key) {
		final String function = "getStringFromJson";
		logger.begin(className, function);
		logger.debug(className, function, "dictionariesCacheNameValue[{}] fileNameValue[{}] key[{}]", new Object[]{dictionariesCacheNameValue, fileNameValue, key});
		
		String value = ReadJson.readString(dictionariesCacheNameValue, fileNameValue, key, null);
		
		logger.debug(className, function, "dictionariesCacheNameValue[{}] fileNameValue[{}] key[{}] value[{}]", new Object[]{dictionariesCacheNameValue, fileNameValue, key, value});
		logger.end(className, function);
		return value;
	}
	
	public String getAlias(String key) {
		final String function = "getAlias";
		logger.begin(className, function);
		
		String alias = getStringFromJsonArray(dictionariesCacheNameValue, fileNameValue, arrayKey, arrayIndexKeyValue, key, strAlias);
		
		logger.debug(className, function, "alias[{}]", alias);
		logger.end(className, function);
		return alias;
	}
	
	public String getScsEnvId(String key) {
		final String function = "getScsEnvId";
		logger.begin(className, function);
		
		String scsEnvId = getStringFromJsonArray(dictionariesCacheNameValue, fileNameValue, arrayKey, arrayIndexKeyValue, key, strScsEnvId);
		
		logger.debug(className, function, "scsEnvId[{}]", scsEnvId);
		logger.end(className, function);
		return scsEnvId;
	}
	
	public String getOpmApi() {
		final String function = "getOpmApi";
		logger.begin(className, function);
		
		String opmApi = getStringFromJson(dictionariesCacheNameValue, fileNameValue, strOpmApi);
		
		logger.debug(className, function, "opmApi[{}]", opmApi);
		logger.end(className, function);
		return opmApi;
	}
	
	public String getOpmIdentityType() {
		final String function = "getOpmIdentityType";
		logger.begin(className, function);
		
		String opmIdentityType = getStringFromJson(dictionariesCacheNameValue, fileNameValue, strOpmIdentityType);
		
		logger.debug(className, function, "opmIdentityType[{}]", opmIdentityType);
		logger.end(className, function);
		return opmIdentityType;
	}
	
	public String getSelfIdentity() {
		final String function = "getSelfIdentity";
		logger.begin(className, function);
		String opmApi = getOpmApi();
		String opmIdentityType = getOpmIdentityType();
		logger.debug(className, function, "opmApi[{}] opmIdentityType[{}]", opmApi, opmIdentityType);
		logger.end(className, function);
		return getSelfIdentity(opmApi, opmIdentityType);
	}
	
	public String getSelfIdentity(String opmApi, String opmIdentityType) {
		final String function = "getSelfIdentity";
		logger.begin(className, function);
		
		logger.debug(className, function, "opmApi[{}] opmIdentityType[{}]", opmApi, opmIdentityType);

		String selfIdentity = null;
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(opmApi);
		if ( null != uiOpm_i ) {
			if ( null != opmIdentityType ) {
				if ( OpmIdendifyType.HostName.toString().equals(opmIdentityType) ) {
					selfIdentity = uiOpm_i.getCurrentHostName();
				}
				else if ( OpmIdendifyType.IpAddress.toString().equals(opmIdentityType) ) {
					selfIdentity = uiOpm_i.getCurrentIPAddress();
				}
				else if ( OpmIdendifyType.Profile.toString().equals(opmIdentityType) ) {
					selfIdentity = uiOpm_i.getCurrentProfile();
				}
			} else {
				logger.warn(className, function, "opmIdentityType IS NULL");
			}
		} else {
			logger.warn(className, function, "uiOpm_i IS NULL");
		}
		
		logger.debug(className, function, "selfIdentity[{}]", selfIdentity);
		
		// Default value of the SelfIdentity is IP Address
		if ( null == selfIdentity ) uiOpm_i.getCurrentIPAddress();
		
		logger.debug(className, function, "selfIdentity[{}]", selfIdentity);
		logger.end(className, function);
		return selfIdentity;
	}
}
