package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.SimultaneousLogin_i.ParameterAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.SimultaneousLogin_i.StorageAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSimultaneousLoginControl_i.GwsIdendifyType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSimultaneousLoginControl_i.UserIdendifyType;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class SimultaneousLogin {
	
	private final String className = UIWidgetUtil.getClassSimpleName(SimultaneousLogin.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static SimultaneousLogin instance = null;
	private SimultaneousLogin() {}
	public static SimultaneousLogin getInstance() {
		if ( null == instance ) instance = new SimultaneousLogin();
		return instance;
	}
	
	private final String dictionariesCacheName = "UIJson";
	private final String fileName = "simultaneousLogin.json";
	
	private final String defaultDbAttrResrvReserveReqID = null;
	private final String defaultDbAttrResrvUnreserveReqID = null;

	private final String defaultOpmApi = null;
	
	private final String defaultUsrIdentityType = null;
	private final String defaultGwsIdentityType = null;

	private String opmApi = null;
	public String getOpmApi() { return this.opmApi; }
	
	private String gwsIdentity = null;
	public String getGwsIdentity() { return this.gwsIdentity; }
	private String area = null;
	public String getArea() { return this.area; }
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
	
	private int recordThreshold = 1;
	public void setRecordThreshold(int recordThreshold) { this.recordThreshold = recordThreshold; }

	private Map<String, Map<String, String>> storage = new HashMap<String, Map<String, String>>();
	public Map<String, Map<String, String>> getStorage(String key) { return storage; }
	public void setStorage(String key, Map<String, String> entities) {
		storage.put(key, entities); 
		if ( logger.isDebugEnabled() ) dumpStorage();
	}
	
	public void dumpStorage() {
		final String function = "dumpStorage";
		logger.begin(className, function);
		for ( Entry<String, Map<String, String>> entities : storage.entrySet() ) {
			String entitiesKey = entities.getKey();
			logger.debug(className, function, "entitiesKey[{}]", entitiesKey);
			Map<String, String> entity = entities.getValue();
			if ( null != entity ) {
				for ( Entry<String, String> values : entity.entrySet() ) {
					if ( null != values ) {
						logger.debug(className, function, "values key[{}] value[{}]", values.getKey(), values.getValue());
					} else {
						logger.warn(className, function, "values IS NULL");
					}
				}
			} else {
				logger.warn(className, function, "entity IS NULL");
			}
		}
		logger.end(className, function);
	}
	
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		
		logger.debug(className, function, "dictionariesCacheName[{}] fileName[{}]", dictionariesCacheName, fileName);
		
		recordThreshold = ReadJson.readInt(dictionariesCacheName, fileName, SimultaneousLogin_i.ParameterAttribute.RecordThreshold.toString(), 1);
		logger.debug(className, function, "RecordThreshold[{}]  usrIdentityType[{}]", ParameterAttribute.RecordThreshold.toString(), usrIdentityType);
		
		opmApi = ReadJson.readString(dictionariesCacheName, fileName, ParameterAttribute.OpmApi.toString(), defaultOpmApi);
		logger.debug(className, function, "OpmApi[{}] opmApi[{}]", ParameterAttribute.OpmApi.toString(), opmApi);
		
		dbAttrResrvReserveReqID = ReadJson.readString(dictionariesCacheName, fileName, ParameterAttribute.DbAttrResrvReserveReqID.toString(), defaultDbAttrResrvReserveReqID);
		logger.debug(className, function, "DbAttrResrvReserveReqID[{}]  dbAttrResrvReserveReqID[{}]", ParameterAttribute.DbAttrResrvReserveReqID.toString(), dbAttrResrvReserveReqID);
		
		dbAttrResrvUnreserveReqID = ReadJson.readString(dictionariesCacheName, fileName, ParameterAttribute.DbAttrResrvUnreserveReqID.toString(), defaultDbAttrResrvUnreserveReqID);
		logger.debug(className, function, "DbAttrResrvUnreserveReqID[{}]  dbAttrResrvUnreserveReqID[{}]", ParameterAttribute.DbAttrResrvUnreserveReqID.toString(), dbAttrResrvUnreserveReqID);
		
		usrIdentityType = ReadJson.readString(dictionariesCacheName, fileName, ParameterAttribute.UsrIdentityType.toString(), defaultUsrIdentityType);
		logger.debug(className, function, "UsrIdentityType[{}]  usrIdentityType[{}]", ParameterAttribute.UsrIdentityType.toString(), usrIdentityType);
		
		gwsIdentityType = ReadJson.readString(dictionariesCacheName, fileName, ParameterAttribute.GwsIdentityType.toString(), defaultGwsIdentityType);
		logger.debug(className, function, "GwsIdentityType[{}]  gwsIdentityType[{}]", ParameterAttribute.GwsIdentityType.toString(), gwsIdentityType);
		
		String gwsIdendifyType = getGwsIdentityType();
		logger.debug(className, function, "opmApi[{}] gwsIdendifyType[{}]", opmApi, gwsIdendifyType);
		
		gwsIdentity = getGwsIdentity(opmApi, gwsIdendifyType);
		logger.debug(className, function, "gwsIdentity[{}]", gwsIdentity);
		
		area = getArea(gwsIdentity);
		scsEnvId = getScsEnvId(gwsIdentity);
		alias = getAlias(gwsIdentity);
		logger.debug(className, function, "gwsIdentity[{}] area[{}] scsEnvId[{}] alias[{}]", new Object[]{gwsIdentity, area, scsEnvId, alias});

		String usrIdentityType = getUsrIdentityType();
		logger.debug(className, function, "opmApi[{}] usrIdentityType[{}]", opmApi, usrIdentityType);

		usrIdentity = getUsrIdentity(opmApi, usrIdentityType);
		logger.debug(className, function, "usrIdentity[{}]", usrIdentity);

		logger.end(className, function);
	}
	
	public String getArea(String key) {
		final String function = "getArea";
		logger.begin(className, function);
		
		String area = null;
		if ( null != storage.get(key) ) area = storage.get(key).get(StorageAttribute.Area.toString());
		
		logger.debug(className, function, "area[{}]", area);
		logger.end(className, function);
		return area;
	}
	
	public String getAlias(String key) {
		final String function = "getAlias";
		logger.begin(className, function);
		
		String alias = null;
		if ( null != storage.get(key) ) alias = storage.get(key).get(StorageAttribute.Alias.toString());
		
		logger.debug(className, function, "alias[{}]", alias);
		logger.end(className, function);
		return alias;
	}
	
	public String getScsEnvId(String key) {
		final String function = "getScsEnvId";
		logger.begin(className, function);
		
		String scsEnvId = null;
		if ( null != storage.get(key) ) scsEnvId = storage.get(key).get(StorageAttribute.ScsEnvId.toString());
		
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
	
	public int validiteLogin() {
		final String function = "validiteLogin";
		logger.begin(className, function);
		
		SimultaneousLogin simultaneousLogin = SimultaneousLogin.getInstance();
		String area = simultaneousLogin.getArea();

		String usrIdentity = simultaneousLogin.getUsrIdentity();
	
		logger.debug(className, function, "gwsIdentity, usrIdentity[{}] area[{}] ", new Object[]{gwsIdentity, usrIdentity, area});

		Map<String, Integer> records = new HashMap<String, Integer>();
		
		if ( null != storage ) {
			
			logger.debug(className, function, "storage size[{}]", storage.size());
			
			for ( Entry<String, Map<String, String>> entities : storage.entrySet() ) {
				
				Map<String, String> entite = entities.getValue();
				
				String gwsUsrIdentity = entite.get(StorageAttribute.ResrReservedID.toString());
				logger.debug(className, function, "gwsUsrIdentity[{}]", gwsUsrIdentity);
				
				if ( null != gwsUsrIdentity ) {
					
					if ( gwsUsrIdentity.equals(usrIdentity) ) {

						String gwsArea = simultaneousLogin.getArea(gwsIdentity);
						
						logger.debug(className, function, "gwsIdentity[{}] gwsArea[{}]", gwsIdentity, gwsArea);
						
						if ( null == records.get(gwsArea) ) records.put(gwsArea, 0);

						records.put(gwsArea, records.get(gwsArea) + 1);
						
						logger.debug(className, function, "records.get({})[{}]", gwsArea, records.get(gwsArea));
					}
				} else {
					logger.warn(className, function, "gwsUsrIdentity IS NULL");
				}
			}
		} else {
			logger.warn(className, function, "rowStorage IS NULL");
		}
		
		int iValid = -1;
		if ( null != area && null != records.get(area) ) {
			iValid = 0;
			
			int record = records.get(area);
			logger.debug(className, function, "area[{}] record[{}] <= recordThreshold[{}]", new Object[]{area, record, recordThreshold});

			if ( record <= recordThreshold ) iValid = 1;
		}

		logger.debug(className, function, "iValid[{}]", iValid);
		logger.end(className, function);
		
		return iValid;
	}
}
