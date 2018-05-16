package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.json.client.JSONObject;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJsonFile;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.SimultaneousLogin_i.BitPosDescription;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.SimultaneousLogin_i.ParameterAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.SimultaneousLogin_i.StorageAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSimultaneousLoginControl_i.GwsIdendifyType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSimultaneousLoginControl_i.UserIdendifyType;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class SimultaneousLogin {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static SimultaneousLogin instance = null;
	private SimultaneousLogin() {}
	public static SimultaneousLogin getInstance() {
		if ( null == instance ) instance = new SimultaneousLogin();
		return instance;
	}
	
	private final String dictionariesCacheName = "UIJson";
	private final String fileName = "simultaneousLogin.json";
	
	private final String defaultDbAttrResrvReserveReqID = ".resrvReserveReqID";
	private final String defaultDbAttrResrvUnreserveReqID = ".resrvUnreserveReqID";

	private final String defaultOpmApi = "UIOpmSCADAgen";
	
	private final String defaultUsrIdentityType = "IpAddress";
	private final String defaultGwsIdentityType = "Profile";
	
	private final int defaultGroupThreshold = 0;

	private String opmApi = null;
	public String getOpmApi() { return this.opmApi; }
	
	private String selfGwsIdentity = null;
	public String getGwsIdentity() { return this.selfGwsIdentity; }
	private String selfGwsArea = null;
	public String getSelfArea() { return this.selfGwsArea; }
	private String selfGwsAlias = null;
	public String getSelfAlias() { return this.selfGwsAlias; }
	private String selfGwsScsEnvId = null;
	public String getSelfScsEnvId() { return this.selfGwsScsEnvId; }
	
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
	
	private int groupThreshold = 1;
	public int getGroupThreshold() { return this.groupThreshold; }
	public void setGroupThreshold(int groupThreshold) { this.groupThreshold = groupThreshold; }
	
	private String [] byPassUsrIdentity = null;
	public String [] getByPassUsrIdentities() { return this.byPassUsrIdentity; }
	
	private boolean byPassUsrIdentityIgnoreCase = true;
	public boolean getByPassUsrIdentityIgnoreCase() { return this.byPassUsrIdentityIgnoreCase; }

	private Map<String, Map<String, String>> storage = new HashMap<String, Map<String, String>>();
	public Map<String, Map<String, String>> getStorage() { return storage; }
	public void setStorage(String key, Map<String, String> entities) {
		storage.put(key, entities); 
		if ( logger.isDebugEnabled() ) dumpStorage();
	}
	
	public void dumpStorage() {
		final String function = "dumpStorage";
		logger.begin(function);
		for ( Entry<String, Map<String, String>> entities : storage.entrySet() ) {
			String entitiesKey = entities.getKey();
			logger.debug(function, "entitiesKey[{}]", entitiesKey);
			Map<String, String> entity = entities.getValue();
			if ( null != entity ) {
				String msg = "";
				for ( Entry<String, String> values : entity.entrySet() ) {
					if ( msg.length() > 0 ) msg += " ";
					msg += "["+values.getKey()+"]:["+values.getValue()+"]";
				}
				logger.debug(function, "msg[{}]", msg);
			} else {
				logger.warn(function, "entity IS NULL");
			}
		}
		logger.end(function);
	}
	
	public void loadConfig() {
		final String function = "loadConfig";
		logger.begin(function);
		
		logger.debug(function, "dictionariesCacheName[{}] fileName[{}]", dictionariesCacheName, fileName);
		
		JSONObject jsonObject = ReadJsonFile.readJson(dictionariesCacheName, fileName);

		opmApi = ReadJson.readString(jsonObject, ParameterAttribute.OpmApi.toString(), defaultOpmApi);
		logger.debug(function, "OpmApi[{}] opmApi[{}]", ParameterAttribute.OpmApi.toString(), opmApi);
		
		usrIdentityType = ReadJson.readString(jsonObject, ParameterAttribute.UsrIdentityType.toString(), defaultUsrIdentityType);
		logger.debug(function, "UsrIdentityType[{}]  usrIdentityType[{}]", ParameterAttribute.UsrIdentityType.toString(), usrIdentityType);
		
		gwsIdentityType = ReadJson.readString(jsonObject, ParameterAttribute.GwsIdentityType.toString(), defaultGwsIdentityType);
		logger.debug(function, "GwsIdentityType[{}]  gwsIdentityType[{}]", ParameterAttribute.GwsIdentityType.toString(), gwsIdentityType);
		
		dbAttrResrvReserveReqID = ReadJson.readString(jsonObject, ParameterAttribute.DbAttrResrvReserveReqID.toString(), defaultDbAttrResrvReserveReqID);
		logger.debug(function, "DbAttrResrvReserveReqID[{}]  dbAttrResrvReserveReqID[{}]", ParameterAttribute.DbAttrResrvReserveReqID.toString(), dbAttrResrvReserveReqID);
		
		dbAttrResrvUnreserveReqID = ReadJson.readString(jsonObject, ParameterAttribute.DbAttrResrvUnreserveReqID.toString(), defaultDbAttrResrvUnreserveReqID);
		logger.debug(function, "DbAttrResrvUnreserveReqID[{}]  dbAttrResrvUnreserveReqID[{}]", ParameterAttribute.DbAttrResrvUnreserveReqID.toString(), dbAttrResrvUnreserveReqID);

		groupThreshold = defaultGroupThreshold;
//		groupThreshold = ReadJson.readInt(jsonObject, ParameterAttribute.GroupThreshold.toString(), defaultGroupThreshold);
//		logger.debug(function, "GroupThreshold[{}]  groupThreshold[{}]", ParameterAttribute.GroupThreshold.toString(), groupThreshold);
		
		byPassUsrIdentity = ReadJson.readStringArray(jsonObject, ParameterAttribute.ByPassUsrIdentity.toString());
		logger.debug(function, "ByPassUsrIdentity[{}]  byPassUsrIdentity[{}]", ParameterAttribute.ByPassUsrIdentity.toString(), byPassUsrIdentity);

		logger.end(function);
	}
	
	public boolean isSelfIdentityReady() {
		final String function = "isSelfIdentityReady";
		logger.begin(function);
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(opmApi);
		if ( null == uiOpm_i ) logger.warn(function, "opmApi[{}] IS NULL", opmApi);
		
		selfGwsIdentity = getSelfGwsIdentity(uiOpm_i, getGwsIdentityType());
		logger.debug(function, "selfGwsIdentity[{}]", selfGwsIdentity);

		logger.end(function);
		
		return (null!=selfGwsIdentity);
	}
	
	public boolean isUsrIdentityReady() {
		final String function = "isUsrIdentityReady";
		logger.begin(function);
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(opmApi);
		if ( null == uiOpm_i ) logger.warn(function, "opmApi[{}] IS NULL", opmApi);

		usrIdentity = getUsrIdentity(uiOpm_i, getUsrIdentityType());
		logger.end(function);

		return (null!=usrIdentity);
	}
	
	public boolean getWkstInfo() {
		final String function = "getWkstInfo";
		logger.begin(function);
		
		selfGwsArea = getArea(selfGwsIdentity);
		selfGwsScsEnvId = getScsEnvId(selfGwsIdentity);
		selfGwsAlias = getAlias(selfGwsIdentity);
		logger.debug(function, "selfGwsIdentity[{}] selfGwsArea[{}] selfGwsScsEnvId[{}] selfGwsAlias[{}]"
				, new Object[]{selfGwsIdentity, selfGwsArea, selfGwsScsEnvId, selfGwsAlias});

		logger.end(function);
		
		return (null!=selfGwsAlias);
	}
	
	public void setCurrentWS() {
		final String function = "setCurrentWS";
		logger.begin(function);
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(opmApi);
		if ( null != uiOpm_i ) {

			logger.debug(function, "selfGwsScsEnvId[{}] selfGwsAlias[{}]", selfGwsScsEnvId, selfGwsAlias);
			
			uiOpm_i.setCurrentEnv(selfGwsScsEnvId);
			uiOpm_i.setCurrentAlias(selfGwsAlias);
			
			logger.debug(function, "uiOpm_i.getCurrentEnv[{}] uiOpm_i.getCurrentAlias[{}]", uiOpm_i.getCurrentEnv(), uiOpm_i.getCurrentAlias());
		} else {
			logger.warn(function, "opmApi[{}] IS NULL", opmApi);
		}
		
		logger.end(function);
	}

	public String getArea(String key) {
		final String function = "getArea";
		logger.begin(function);
		
		String area = null;
		if ( null != storage.get(key) ) area = storage.get(key).get(StorageAttribute.Area.toString());
		
		logger.debug(function, "area[{}]", area);
		logger.end(function);
		return area;
	}
	
	public String getAlias(String key) {
		final String function = "getAlias";
		logger.begin(function);
		
		String alias = null;
		if ( null != storage.get(key) ) alias = storage.get(key).get(StorageAttribute.Alias.toString());
		
		logger.debug(function, "alias[{}]", alias);
		logger.end(function);
		return alias;
	}
	
	public String getScsEnvId(String key) {
		final String function = "getScsEnvId";
		logger.begin(function);
		
		String scsEnvId = null;
		if ( null != storage.get(key) ) scsEnvId = storage.get(key).get(StorageAttribute.ScsEnvId.toString());
		
		logger.debug(function, "scsEnvId[{}]", scsEnvId);
		logger.end(function);
		return scsEnvId;
	}

	private String getUsrIdentity(UIOpm_i uiOpm_i, String usrIdentityType) {
		final String function = "getUsrIdentity";
		logger.begin(function);
		
		logger.debug(function, "usrIdentityType[{}]", usrIdentityType);

		String usrIdentity = null;
		if ( null == uiOpm_i ) {
			logger.warn(function, "uiOpm_i IS NULL");
		} 
		else if ( null == usrIdentityType ) {
			logger.warn(function, "usrIdentityType IS NULL");
		} 
		else if ( 0 == UserIdendifyType.Profile.toString().compareTo(usrIdentityType) ) {
			usrIdentity = uiOpm_i.getCurrentProfile();
		} 
		else if ( 0 == UserIdendifyType.Operator.toString().compareTo(usrIdentityType) ) {
			usrIdentity = uiOpm_i.getCurrentOperator();
		}
		else {
			logger.warn(function, "usrIdentityType[{}] IS UNKNOW", usrIdentityType);
		}
		
		logger.debug(function, "usrIdentity[{}]", usrIdentity);
		
		// Default value of the UsrIdentity is Profile
		if ( null == usrIdentity ) uiOpm_i.getCurrentProfile();
		
		logger.debug(function, "usrIdentity[{}]", usrIdentity);
		logger.end(function);
		return usrIdentity;
	}
	
	private String getSelfGwsIdentity(UIOpm_i uiOpm_i, String gwsIdentityType) {
		final String function = "getSelfGwsIdentity";
		logger.begin(function);
		
		logger.debug(function, "gwsIdentityType[{}]", gwsIdentityType);

		String selfIdentity = null;
		
		if ( null == uiOpm_i ) {
			logger.warn(function, "uiOpm_i IS NULL");
		} 
		else if ( null == gwsIdentityType ) {
			logger.warn(function, "gwsIdentityType IS NULL");
		} 
		else if ( 0 == GwsIdendifyType.HostName.toString().compareTo(gwsIdentityType) ) {
			selfIdentity = uiOpm_i.getCurrentHostName();
		}
		else if ( 0 == GwsIdendifyType.IpAddress.toString().compareTo(gwsIdentityType) ) {
			selfIdentity = uiOpm_i.getCurrentIPAddress();
		}
		else {
			logger.warn(function, "gwsIdentityType[{}] IS UNKNOW", gwsIdentityType);
		}

		logger.debug(function, "selfIdentity[{}]", selfIdentity);
		
		// Default value of the SelfIdentity is IP Address
		if ( null == selfIdentity ) uiOpm_i.getCurrentIPAddress();
		
		logger.debug(function, "selfIdentity[{}]", selfIdentity);
		logger.end(function);
		return selfIdentity;
	}
	
	public boolean isByPassUsrIdentity() {
		final String function = "isByPassUsrIdentity";
		logger.begin(function);
		boolean valid = false; 
		String usrIdentity = getUsrIdentity();
		String [] byPassUsrIdentities = getByPassUsrIdentities();
		boolean byPassUsrIdentityIgnoreCase = getByPassUsrIdentityIgnoreCase();
		
		logger.debug(function, "byPassUsrIdentityIgnoreCase[{}] usrIdentity[{}]"
				, byPassUsrIdentityIgnoreCase, usrIdentity);
		
		if ( byPassUsrIdentities != null ) {
			for ( int i = 0 ; i < byPassUsrIdentities.length ; ++i ) {
				String byPassUsrIdentity = byPassUsrIdentities[i];
				logger.debug(function, "byPassUsrIdentity[{}]", byPassUsrIdentity);
				if ( 0 == usrIdentity.compareTo(byPassUsrIdentity) ) {
					valid = true;
					break;
				} 
			}
		}

		logger.debug(function, "valid[{}]", valid);
		logger.end(function);
		return valid;
	}

	public boolean reservedBySelf(String selfGwsIdentity, String selfUsrIdentity) {
		final String function = "reservedBySelf";
		logger.begin(function);
		boolean result = false;
		
		logger.debug(function, "selfGwsIdentity[{}] selfUsrIdentity[{}]", selfGwsIdentity, selfUsrIdentity);
		 Map<String, String> entity = storage.get(selfGwsIdentity);
		 if ( null != entity ) {
			 String gwsUsrIdentity = entity.get(StorageAttribute.ResrReservedID.toString());
			 logger.debug(function, "gwsUsrIdentity[{}]", gwsUsrIdentity);
			 if ( null != gwsUsrIdentity ) {
				 if ( 0 == gwsUsrIdentity.compareTo(selfUsrIdentity) ) {
					 
					result = true;

					logger.debug(function, "gwsUsrIdentity[{}] AND selfUsrIdentity[{}] IS EQUAL, Gws Reserved", gwsUsrIdentity, selfUsrIdentity);
				 } else {
					 logger.warn(function, "gwsUsrIdentity[{}] AND selfUsrIdentity[{}] IS NOT EQUAL", gwsUsrIdentity, selfUsrIdentity);
				 }
			 } else {
				 logger.warn(function, "gwsUsrIdentity IS NULL");
			 }
		 } else {
			 logger.warn(function, "entity IS NULL");
		 }
		 logger.end(function);
		 return result;
	}
	
	public boolean reservedByOther(String selfGwsIdentity, String selfUsrIdentity) {
		final String function = "reservedByOther";
		logger.begin(function);
		boolean result = false;
		
		logger.debug(function, "selfGwsIdentity[{}] selfUsrIdentity[{}]", selfGwsIdentity, selfUsrIdentity);
		 Map<String, String> entity = storage.get(selfGwsIdentity);
		 if ( null != entity ) {
			 String gwsUsrIdentity = entity.get(StorageAttribute.ResrReservedID.toString());
			 logger.debug(function, "gwsUsrIdentity[{}]", gwsUsrIdentity);
			 if ( null != gwsUsrIdentity ) {
				 if ( gwsUsrIdentity.trim().length() > 0 && 0 != gwsUsrIdentity.compareTo(selfUsrIdentity) ) {
					 
					 result = true;

					 logger.warn(function, "gwsUsrIdentity[{}] AND selfUsrIdentity[{}] IS EQUAL, Gws Reserved by other", gwsUsrIdentity, selfUsrIdentity);
				 } else {
					 logger.warn(function, "gwsUsrIdentity[{}] IS Empty OR gwsUsrIdentity selfUsrIdentity[{}] IS EQUAL, Gws Reserved by Self", gwsUsrIdentity, selfUsrIdentity);
				 }
			 } else {
				 logger.warn(function, "gwsUsrIdentity IS NULL");
			 }
		 } else {
			 logger.warn(function, "entity IS NULL");
		 }
		 logger.end(function);
		 return result;
	}
	
	public boolean reservedInOtherArea(String selfGwsArea, String selfUsrIdentity, int recordThreshold) {
		final String function = "reservedInOtherArea";
		logger.begin(function);
		boolean result = false;
		
		// Verify in other area 
		Map<String, Integer> records = new HashMap<String, Integer>();
		for ( Entry<String, Map<String, String>> entities : storage.entrySet() ) {
			
			if ( null != entities ) {
				String gwsIdentity = entities.getKey();
				Map<String, String> entity = entities.getValue();
				
				String gwsUsrIdentity = entity.get(StorageAttribute.ResrReservedID.toString());
				logger.debug(function, "gwsUsrIdentity[{}]", gwsUsrIdentity);
				
				if ( null == gwsUsrIdentity ) {
					logger.warn(function, "gwsUsrIdentity IS NULL");
				}
				else if ( 0 == gwsUsrIdentity.compareTo(selfUsrIdentity) ) {

					String gwsArea = getArea(gwsIdentity);
					logger.debug(function, "gwsIdentity[{}] selfGwsArea[{}]", gwsIdentity, selfGwsArea);
					
					if ( null == records.get(gwsArea) ) records.put(gwsArea, 0);

					records.put(gwsArea, records.get(gwsArea) + 1);
					logger.debug(function, "records.get({})[{}]", gwsArea, records.get(gwsArea));
				}
			} else {
				logger.warn(function, "entities IS NULL");
			}
		}
		
		for ( Entry<String, Integer> entity : records.entrySet() ) {
			String a = entity.getKey();
			if ( null != a && 0 != a.compareTo(selfGwsArea) ) {
				Integer record = entity.getValue();
				if ( null != record ) {
					if ( record > recordThreshold ) {
						result = true;
						logger.debug(function, "selfGwsArea[{}] record[{}] > recordThreshold[{}]", new Object[]{selfGwsArea, record, recordThreshold});
						break;
					}						
				}
			}
		}
		
		logger.end(function);
		return result;
	}
	
//	private int setBit(int b, int p) { return b = b | ( 1 << p ); }
//	private int unSetBit(int b, int p) { return b = b & ~ ( 1 << p ); }
//	private boolean isBitSet(int b, int p) { return ( (b & (1 << p)) != 0 ); }
	
	public String getBitPosString(final int input) {
		String result= null;
		if ( input == SimultaneousLogin_i.Bit_Pos_SelfGwsIdentity_IsInvalid ) {
			result = BitPosDescription.SelfGwsIdentity_IsInvalid.toString();
		}
		else if ( input == SimultaneousLogin_i.Bit_Pos_SelfGwsArea_IsInvalid ) {
			result = BitPosDescription.SelfGwsArea_IsInvalid.toString();
		}
		else if ( input == SimultaneousLogin_i.Bit_Pos_SelfUsrIdentity_IsInvalid ) {
			result = BitPosDescription.SelfUsrIdentity_IsInvalid.toString();
		}
		else if ( input == SimultaneousLogin_i.Bit_Pos_Storage_IsEmpty ) {
			result = BitPosDescription.Storage_IsEmpty.toString();
		}
		else if ( input == SimultaneousLogin_i.Bit_Pos_ReservedInOtherArea ) {
			result = BitPosDescription.ReservedInOtherArea.toString();
		}
		else if ( input == SimultaneousLogin_i.Bit_Pos_IsByPassUsrIdentity ) {
			result = BitPosDescription.IsByPassUsrIdentity.toString();
		}
		else if ( input == SimultaneousLogin_i.Bit_Pos_ReservedSelfGws ) {
			result = BitPosDescription.ReservedSelfGws.toString();
		}
		else if ( input == SimultaneousLogin_i.Bit_Pos_ReservedByOther ) {
			result = BitPosDescription.ReservedByOther.toString();
		}
		return result;
	}
	
	public int validateCondition() {
		final String function = "validateCondition";
		logger.begin(function);
		
		String selfGwsIdentity	= getGwsIdentity();
		String selfGwsArea		= getSelfArea();
		String selfUsrIdentity	= getUsrIdentity();
		
		int groupThreshold 	= getGroupThreshold();
		
		logger.debug(function, "selfGwsIdentity[{}] selfGwsArea[{}] selfUsrIdentity[{}] groupThreshold[{}]"
				, new Object[]{selfGwsIdentity, selfGwsArea, selfUsrIdentity, groupThreshold});
		
		int result = 0;

		if ( isByPassUsrIdentity() ) {
			
			result = result | SimultaneousLogin_i.Bit_Pos_IsByPassUsrIdentity;

			logger.debug(function, "isByPassUsrIdentity IS TRUE result[{}]", result);
		} 
		else if ( null == selfGwsIdentity ) {
			
			result = result | SimultaneousLogin_i.Bit_Pos_SelfGwsIdentity_IsInvalid;

			logger.warn(function, "selfGwsIdentity IS NULL");
		} 
		else if ( null == selfGwsArea ) {
			
			result = result | SimultaneousLogin_i.Bit_Pos_SelfGwsArea_IsInvalid;

			logger.warn(function, "selfGwsArea IS NULL");
		}
		else if ( null == selfUsrIdentity ) {
			
			result = result | SimultaneousLogin_i.Bit_Pos_SelfUsrIdentity_IsInvalid;

			logger.warn(function, "usrIdentity IS NULL");
		}
		else {
			
			if ( null != storage ) {
				
				logger.debug(function, "storage size[{}]", storage.size());
				
				if ( reservedByOther(selfGwsIdentity, selfUsrIdentity) ) {
					
					result = result | SimultaneousLogin_i.Bit_Pos_ReservedByOther;
					
					logger.debug(function, "reservedByOther IS TRUE result[{}]", result);
				}
				
				if ( reservedBySelf(selfGwsIdentity, selfUsrIdentity) ) {
					
					result = result | SimultaneousLogin_i.Bit_Pos_ReservedSelfGws;
					
					logger.debug(function, "reservedBySelf IS TRUE result[{}]", result);
				}

				if ( reservedInOtherArea(selfGwsArea, selfUsrIdentity, groupThreshold)) {
					
					result = result | SimultaneousLogin_i.Bit_Pos_ReservedInOtherArea;
					
					logger.debug(function, "reservedInOtherArea IS TRUE result[{}]", result);
				}
			} else {
				
				result = result | SimultaneousLogin_i.Bit_Pos_Storage_IsEmpty;
				
				logger.warn(function, "rowStorage IS NULL");
			}
		}

		logger.debug(function, "result[{}]", result);
		logger.end(function);
		
		return result;
	}
}
