package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control;

import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSimultaneousLoginControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSimultaneousLoginControl_i.GwsIdendifyType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSimultaneousLoginControl_i.UserIdendifyType;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class SimultaneousLogin {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetSimultaneousLoginControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String dictionariesCacheName = "UIJson";
	private final String fileName = "simultaneousLogin.json";
	
	private final String strDbAttrResrvReserveReqID = "DbAttrResrvReserveReqID";
	private final String strDbAttrResrvUnreserveReqID = "DbAttrResrvUnreserveReqID";
	
	private final String arrayKey = "GWS";
	private final String arrayIndexKeyValue = "Key";
	
	private final String strAlias = "Alias";
	private final String strScsEnvId = "ScsEnvId";
	
	private final String strOpmApi = "OpmApi";
	
	private final String strUsrIdentityType = "UsrIdentityType";
	private final String strGwsIdentityType = "GwsIdentityType";

	private String opmApi = null;
	public String getOpmApi() { return this.opmApi; }
	
	private String alias = null;
	public String getAlias() { return this.alias; }
	private String scsEnvId = null;
	public String getScsEnvId() { return this.scsEnvId; }
	
	private String usrIdentityType = null;
	public String getUsrIdentityType() { return usrIdentityType; }
	
	private String gwsIdentityType = null;
	public String getGwsIdentityType() { return gwsIdentityType; }
	
	private String usrIdentity = null;
	public String getUsrIdentity() { return this.usrIdentity; }
	
	private String dbAttrResrvReserveReqID = null;
	public String getDbAttriuteReserveReqID() { return this.dbAttrResrvReserveReqID; }
	
	private String dbAttrResrvUnreserveReqID = null;
	public String getDbAttriuteUnreserveReqID() { return this.dbAttrResrvUnreserveReqID; }
	
	public SimultaneousLogin() {
		final String function = "SimultaneousLogin";
		logger.begin(className, function);
		
		logger.debug(className, function, "dictionariesCacheName[{}] fileName[{}]", dictionariesCacheName, fileName);
		
		opmApi = ReadJson.getStringFromJson(dictionariesCacheName, fileName, strOpmApi);
		logger.debug(className, function, "strOpmApi[{}] opmApi[{}]", strOpmApi, opmApi);
		
		dbAttrResrvReserveReqID = ReadJson.getStringFromJson(dictionariesCacheName, fileName, strDbAttrResrvReserveReqID);
		logger.debug(className, function, "strDbAttrResrvReserveReqID[{}]  dbAttrResrvReserveReqID[{}]", strDbAttrResrvReserveReqID, dbAttrResrvReserveReqID);
		
		dbAttrResrvUnreserveReqID = ReadJson.getStringFromJson(dictionariesCacheName, fileName, strDbAttrResrvUnreserveReqID);
		logger.debug(className, function, "strDbAttrResrvUnreserveReqID[{}]  dbAttrResrvUnreserveReqID[{}]", strDbAttrResrvUnreserveReqID, dbAttrResrvUnreserveReqID);
		
		usrIdentityType = ReadJson.getStringFromJson(dictionariesCacheName, fileName, strUsrIdentityType);
		logger.debug(className, function, "strUsrIdentityType[{}]  usrIdentityType[{}]", strUsrIdentityType, usrIdentityType);
		
		gwsIdentityType = ReadJson.getStringFromJson(dictionariesCacheName, fileName, strGwsIdentityType);
		logger.debug(className, function, "strGwsIdentityType[{}]  gwsIdentityType[{}]", strGwsIdentityType, gwsIdentityType);
		
		String gwsIdendifyType = getGwsIdentityType();
		logger.debug(className, function, "opmApi[{}] gwsIdendifyType[{}]", opmApi, gwsIdendifyType);
		
		String gwsIdentity = getGwsIdentity(opmApi, gwsIdendifyType);
		logger.debug(className, function, "gwsIdentity[{}]", gwsIdentity);
		
		scsEnvId = getScsEnvId(gwsIdentity);
		alias = getAlias(gwsIdentity);
		logger.debug(className, function, "scsEnvId[{}] alias[{}]", scsEnvId, alias);

		String usrIdentityType = getUsrIdentityType();
		logger.debug(className, function, "opmApi[{}] usrIdentityType[{}]", opmApi, usrIdentityType);

		usrIdentity = getUsrIdentity(opmApi, usrIdentityType);
		logger.debug(className, function, "usrIdentity[{}]", usrIdentity);

		logger.end(className, function);
	}
	
	public String getAlias(String key) {
		final String function = "getAlias";
		logger.begin(className, function);
		
		String alias = ReadJson.getStringFromJsonArray(dictionariesCacheName, fileName, arrayKey, arrayIndexKeyValue, key, strAlias);
		
		logger.debug(className, function, "alias[{}]", alias);
		logger.end(className, function);
		return alias;
	}
	
	public String getScsEnvId(String key) {
		final String function = "getScsEnvId";
		logger.begin(className, function);
		
		String scsEnvId = ReadJson.getStringFromJsonArray(dictionariesCacheName, fileName, arrayKey, arrayIndexKeyValue, key, strScsEnvId);
		
		logger.debug(className, function, "scsEnvId[{}]", scsEnvId);
		logger.end(className, function);
		return scsEnvId;
	}
	
	private String getUsrIdentity(String opmApi, String usrIdentityType) {
		final String function = "getUsrIdentity";
		logger.begin(className, function);
		
		logger.debug(className, function, "opmApi[{}] usrIdentityType[{}]", opmApi, usrIdentityType);

		String usrIdentity = null;
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(opmApi);
		if ( null != uiOpm_i ) {
			if ( null != usrIdentityType ) {
				if ( UserIdendifyType.Profile.toString().equals(usrIdentityType) ) {
					usrIdentity = uiOpm_i.getCurrentProfile();
				} else if ( UserIdendifyType.Operator.toString().equals(usrIdentityType) ) {
					usrIdentity = uiOpm_i.getCurrentOperator();
				}
			} else {
				logger.warn(className, function, "usrIdentityType IS NULL");
			}
		} else {
			logger.warn(className, function, "uiOpm_i IS NULL");
		}
		
		logger.debug(className, function, "usrIdentity[{}]", usrIdentity);
		
		// Default value of the UsrIdentity is Profile
		if ( null == usrIdentity ) uiOpm_i.getCurrentProfile();
		
		logger.debug(className, function, "usrIdentity[{}]", usrIdentity);
		logger.end(className, function);
		return usrIdentity;
	}
	
	private String getGwsIdentity(String opmApi, String gwsIdentityType) {
		final String function = "getGwsIdentity";
		logger.begin(className, function);
		
		logger.debug(className, function, "opmApi[{}] gwsIdentityType[{}]", opmApi, gwsIdentityType);

		String selfIdentity = null;
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(opmApi);
		if ( null != uiOpm_i ) {
			if ( null != gwsIdentityType ) {
				if ( GwsIdendifyType.HostName.toString().equals(gwsIdentityType) ) {
					selfIdentity = uiOpm_i.getCurrentHostName();
				}
				else if ( GwsIdendifyType.IpAddress.toString().equals(gwsIdentityType) ) {
					selfIdentity = uiOpm_i.getCurrentIPAddress();
				}
			} else {
				logger.warn(className, function, "gwsIdentityType IS NULL");
			}
		} else {
			logger.warn(className, function, "uiOpm_i IS NULL");
		}
		
		logger.debug(className, function, "gwsIdentityType[{}]", gwsIdentityType);
		
		// Default value of the SelfIdentity is IP Address
		if ( null == selfIdentity ) uiOpm_i.getCurrentIPAddress();
		
		logger.debug(className, function, "gwsIdentityType[{}]", gwsIdentityType);
		logger.end(className, function);
		return selfIdentity;
	}
}
