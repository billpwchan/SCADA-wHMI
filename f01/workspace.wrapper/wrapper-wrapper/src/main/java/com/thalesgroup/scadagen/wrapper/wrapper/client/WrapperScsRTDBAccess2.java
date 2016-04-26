package com.thalesgroup.scadagen.wrapper.wrapper.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

public class WrapperScsRTDBAccess2 {
	
	private static Logger logger = Logger.getLogger(WrapperScsRTDBAccess.class.getName());
	
	private ScsRTDBComponentAccess rtdb = null;
	
	private WrapperScsRTDBAccessEvent2 wrapperScsRTDBAccessEvent2 = null;
	
	public WrapperScsRTDBAccess2 (WrapperScsRTDBAccessEvent2 wrapperScsRTDBAccessEvent2) {
		
		this.wrapperScsRTDBAccessEvent2 = wrapperScsRTDBAccessEvent2;
		
		rtdb = new ScsRTDBComponentAccess(new IRTDBComponentClient() {

			@Override
			public void setPresenter(HypervisorPresenterClientAbstract<? extends HypervisorView> presenter) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Widget asWidget() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void destroy() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setReadResult(String key, String[] value, int errorCode, String errorMessage) {
				
				setReadResult2(key, value, errorCode, errorMessage);
				
			}

			@Override
			public void setWriteValueRequestResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetInstancesByClassIdResult(String key, String[] values, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetClassesResult(String key, String[] values, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetClassNameResult(String key, String className, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetClassIdResult(String key, int cid, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetUserClassIdResult(String key, int cuid, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetClassInfoResult(String clientKey, DbmClassInfo cinfo, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetInstancesByClassNameResult(String clientKey, String[] instances, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetInstancesByUserClassIdResult(String clientKey, String[] instances, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
				
				setGetChildrenResult2(clientKey, instances, errorCode, errorMessage);
				
			}

			@Override
			public void setGetFullPathResult(String clientKey, String fullPath, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetAliasResult(String clientKey, String alias, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setQueryByNameResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetAttributesResult(String clientKey, String[] attributes, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetAttributesDescriptionResult(String clientKey, ScsClassAttInfo[] attributesInfo,
					int errorCode, String errorMessage) {
				
				setGetAttributesDescriptionResult2(clientKey, attributesInfo, errorCode, errorMessage);
				
			}

			@Override
			public void setGetAttributeStructureResult(String clientKey, String attType, int fieldCount,
					int recordCount, int recordSize, String[] fieldNames, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetUserFormulasNamesResult(String clientKey, String[] formulas, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetDatabaseInfoResult(String clientKey, long dbsize, int nbClasses, int nbFormula,
					int nbInstances, String scsPath, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setForceSnapshotResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetAttributeFormulasResult(String clientKey, String[] formulas, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setSetAttributeFormulaResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetCEOperResult(String clientKey, int[] ceModes, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setControlCEResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setGetChildrenAliasesResult(String clientKey, String[] values, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
    public void multiReadValueRequest(String key, String scsEnvId, String[] dbaddresses) {
    	
    	logger.log(Level.SEVERE, "multiReadValueRequest Begin");
    	
    	logger.log(Level.SEVERE, "multiReadValueRequest key["+key+"]");
    	logger.log(Level.SEVERE, "multiReadValueRequest scsEnvId["+scsEnvId+"]");
    	
    	for(int i = 0; i < dbaddresses.length; ++i ) {
    		logger.log(Level.SEVERE, "multiReadValueRequest dbaddresses("+i+")["+dbaddresses[i]+"]");
    	}
        
        JSONObject jsparam = new JSONObject();

        // build dbaddress param with a list of address
        JSONArray addr = new JSONArray();
        int i;
        for(i = 0; i < dbaddresses.length; i++) {
             addr.set(i, new JSONString(dbaddresses[i]));
        }
        
        jsparam.put("dbaddress", addr);

        JSONObject jsdata = this.rtdb.buildJSONRequest("multiReadValue", jsparam);

        this.rtdb.sendJSONRequest(key, scsEnvId, jsdata.toString());
        
        logger.log(Level.SEVERE, "multiReadValueRequest End");
    }
    
    public void multiReadValueRequestCaches(String key, String scsEnvId, String[] dbaddresses) {
    	
    	logger.log(Level.SEVERE, "multiReadValueRequestCaches Begin");
    	
    	logger.log(Level.SEVERE, "multiReadValueRequestCaches key["+key+"]");
    	logger.log(Level.SEVERE, "multiReadValueRequestCaches scsEnvId["+scsEnvId+"]");
    	
    	for(int i = 0; i < dbaddresses.length; ++i ) {
    		logger.log(Level.SEVERE, "multiReadValueRequestCaches dbaddresses("+i+")["+dbaddresses[i]+"]");
    	}
        
        JSONObject jsparam = new JSONObject();

        // build dbaddress param with a list of address
        JSONArray addr = new JSONArray();
        int i;
        for(i = 0; i < dbaddresses.length; i++) {
             addr.set(i, new JSONString(dbaddresses[i]));
        }
        
        jsparam.put("dbaddress", addr);

        JSONObject jsdata = this.rtdb.buildJSONRequest("multiReadValue", jsparam);

        this.rtdb.sendJSONRequest(key, scsEnvId, jsdata.toString());
        
        logger.log(Level.SEVERE, "multiReadValueRequestCaches End");
    }
    
    public void setReadResult2(String key, String[] values, int errorCode, String errorMessage) {
    	
    	logger.log(Level.SEVERE, "setReadResult2 Begin");
    	
		if ( values != null ) {
			
			this.wrapperScsRTDBAccessEvent2.setReadResult(key, values, errorCode, errorMessage);
			
			for(String value : values) {
				logger.log(Level.SEVERE, "setReadResult2 "
						+ " key["+key+"] value["+value+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
			}
		} else {
			logger.log(Level.SEVERE, "setReadResult2 values IS NULL");

		}
		
		logger.log(Level.SEVERE, "setReadResult2 End");
    	
    }
    
    public void getChildren(String key, String scsEnvId, String dbaddress) {
    	
    	logger.log(Level.SEVERE, "getChildren End");
    	logger.log(Level.SEVERE, "getChildren key["+key+"]");
    	logger.log(Level.SEVERE, "getChildren scsEnvId["+scsEnvId+"]");
    	logger.log(Level.SEVERE, "getChildren dbaddress["+dbaddress+"]");
    	
    	
        JSONObject jsparam = new JSONObject();

        // build param list
        jsparam.put("dbaddress", new JSONString(dbaddress));

        JSONObject jsdata = this.rtdb.buildJSONRequest("GetChildren", jsparam);

        this.rtdb.sendJSONRequest(key, scsEnvId, jsdata.toString());
        
        logger.log(Level.SEVERE, "setReadResult2 End");
    }
    
    public void setGetChildrenResult2(String clientKey, String[] instances, int errorCode, String errorMessage) {
    	logger.log(Level.SEVERE, "setGetChildrenResult2 Begin");
    	logger.log(Level.SEVERE, "setGetChildrenResult2 key["+clientKey+"]");
    	logger.log(Level.SEVERE, "setGetChildrenResult2 errorCode["+errorCode+"]");
    	logger.log(Level.SEVERE, "setGetChildrenResult2 errorMessage["+errorMessage+"]");
    	
    	if ( null != instances ) {

    		this.wrapperScsRTDBAccessEvent2.setGetChildrenResult(clientKey, instances, errorCode, errorMessage);
    	
			for(int i = 0 ; i < instances.length ; ++i ) {
				logger.log(Level.SEVERE, "setReadResult2 attributesInfo("+i+") m_attributeName["+instances[i]+"]");
			}
		
    	} else {
    		logger.log(Level.SEVERE, "setGetChildrenResult2 attributesInfo IS NULL");

    	}
    
		
		logger.log(Level.SEVERE, "setGetChildrenResult2 End");
    }
    
    public void getAttributesDescription(String key, String scsEnvId, String className) {
    	
    	logger.log(Level.SEVERE, "getAttributesDescription Begin");
    	logger.log(Level.SEVERE, "getAttributesDescription key["+key+"]");
    	logger.log(Level.SEVERE, "getAttributesDescription scsEnvId["+scsEnvId+"]");
    	logger.log(Level.SEVERE, "getAttributesDescription className["+className+"]");
    	
        JSONObject jsparam = new JSONObject();

        // build param list
        jsparam.put("className", new JSONString(className));

        JSONObject jsdata = rtdb.buildJSONRequest("GetAttributesDescription", jsparam);

        rtdb.sendJSONRequest(key, scsEnvId, jsdata.toString());
        
        logger.log(Level.SEVERE, "getAttributesDescription End");
    }
    
    public void setGetAttributesDescriptionResult2(String clientKey, ScsClassAttInfo[] attributesInfo, int errorCode, String errorMessage) {
    	
    	logger.log(Level.SEVERE, "setGetAttributesDescriptionResult2 Begin");
    	logger.log(Level.SEVERE, "setGetAttributesDescriptionResult2 key["+clientKey+"]");
    	logger.log(Level.SEVERE, "setGetAttributesDescriptionResult2 errorCode["+errorCode+"]");
    	logger.log(Level.SEVERE, "setGetAttributesDescriptionResult2 errorMessage["+errorMessage+"]");
    	
    	if ( null != attributesInfo ) {

    		this.wrapperScsRTDBAccessEvent2.setGetAttributesDescriptionResult(clientKey, attributesInfo, errorCode, errorMessage);
    	
			for(int i = 0 ; i < attributesInfo.length ; ++i ) {
				logger.log(Level.SEVERE, "setReadResult2 attributesInfo("+i+")"
						+ " m_attributeName["+attributesInfo[i].m_attributeName+"]"
						+ " m_valueScsType["+attributesInfo[i].m_valueScsType+"] "
						+ " m_defaultValue["+attributesInfo[i].m_defaultValue+"] ");
			}
		
    	} else {
    		logger.log(Level.SEVERE, "setGetAttributesDescriptionResult2 attributesInfo IS NULL");

    	}
    
		
		logger.log(Level.SEVERE, "setGetAttributesDescriptionResult2 End");
    	
    }
}
