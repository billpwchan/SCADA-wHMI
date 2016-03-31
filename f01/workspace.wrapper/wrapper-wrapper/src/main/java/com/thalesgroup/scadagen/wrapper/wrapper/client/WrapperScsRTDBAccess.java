/**
 * 
 */
package com.thalesgroup.scadagen.wrapper.wrapper.client;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author T0009042
 *
 */
/**
 * @author syau
 *
 */
public class WrapperScsRTDBAccess extends Composite implements IRTDBComponentClient , IClientLifeCycle {
	
	private static Logger logger = Logger.getLogger(WrapperScsRTDBAccess.class.getName());

    private ScsRTDBComponentAccess m_RTDBAccess = null;
    
    private WrapperScsRTDBAccessEvent wrapperScsRTDBAccessEvent = null;

    public WrapperScsRTDBAccess(WrapperScsRTDBAccessEvent wrapperScsRTDBAccessEvent) {
    	
    	logger.log(Level.FINE, "WrapperScsRTDBAccess Begin");

		this.wrapperScsRTDBAccessEvent = wrapperScsRTDBAccessEvent;
        m_RTDBAccess = new ScsRTDBComponentAccess(this);
        init();
        
        logger.log(Level.FINE, "WrapperScsRTDBAccess End");
    }


    private void init() {
    	
    	logger.log(Level.FINE, "init Begin");
    	
        if (m_RTDBAccess != null) {
        	
            m_RTDBAccess.getClasses("classes", "B001");
            m_RTDBAccess.getDatabaseInfo("dataInfo", "B001");
            
        }
        
        logger.log(Level.FINE, "init End");
    }

    @Override
    public void destroy() {
    	
    	logger.log(Level.FINE, "destroy Begin");
    	
        m_RTDBAccess = null;
        
        logger.log(Level.FINE, "destroy End");
    }

    @Override
    public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
    	
    	logger.log(Level.FINE, "setReadResult Begin");
    	
		if (values != null && values.length > 0) {
			if ( ! withQuery ) {
				getReadResult(key, values, errorCode, errorMessage);
			} else {
				this.wrapperScsRTDBAccessEvent.setReadResult(key, values, errorCode, errorMessage);
			}
			for(String value : values) {
				logger.log(Level.FINE, "setReadResult key["+key+"] value["+value+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");

			}
		} else {
			logger.log(Level.FINE, "setReadResult key["+key+"] values[ IS NULL ] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");

		}
		
		logger.log(Level.FINE, "setReadResult Begin");
    	
    }
    

	@Override
	public void setWriteValueRequestResult(String clientKey, int errorCode, String errorMessage) {

	}

    @Override
    public void setGetInstancesByClassIdResult(String key, String[] values, int errorCode, String errorMessage) {

    }

    @Override
    public void setGetClassesResult(String key, String[] values, int errorCode, String errorMessage) {

    }

    @Override
    public void setGetClassNameResult(String key, String className, int errorCode, String errorMessage) {

    }

    @Override
    public void setGetClassIdResult(String key, int cid, int errorCode, String errorMessage) {

    }

    @Override
    public void setGetUserClassIdResult(String key, int cuid, int errorCode, String errorMessage) {

    }

    @Override
    public void setGetClassInfoResult(String clientKey, DbmClassInfo cinfo, int errorCode, String errorMessage) {

    }
    
    class ReadRequest {
    	String key, scsEnvId, dbaddresses;
    	public ReadRequest(String key, String scsEnvId, String dbaddresses) {
    		this.key = key;
    		this.scsEnvId = scsEnvId;
    		this.dbaddresses = dbaddresses;
    	}
    }
    private HashMap<String, ReadRequest> keysQuery		= new HashMap<String, ReadRequest>();
    private HashMap<String, ReadRequest> keysWaiting	= new HashMap<String, ReadRequest>();
    private void addReadRequest(String key, String scsEnvId, String dbaddresses) {
    	
    	logger.log(Level.FINE, "addReadRequest Begin");
    	
    	ReadRequest readRequest = new ReadRequest(key, scsEnvId, dbaddresses);
    	
    	logger.log(Level.FINE, "addReadRequest readRequest.key["+readRequest.key+"] readRequest.scsEnvId["+readRequest.scsEnvId+"] readRequest.dbaddresses["+readRequest.dbaddresses+"]");

    	if ( keysQuery.get(readRequest.key) == null ) {
    		keysQuery.put(readRequest.key, readRequest);
    	}
    	setReadRequest();
    	
    	logger.log(Level.FINE, "addReadRequest End");
    }
    private void setReadRequest() {
    	
    	logger.log(Level.FINE, "setReadRequest Begin");
    	
    	if ( keysWaiting.size() == 0 ) {
    		if ( keysQuery.size() > 0 ) {
    			try {
    				Map.Entry<String, ReadRequest> entry = keysWaiting.entrySet().iterator().next();
    				//String key = entry.getKey();
    				ReadRequest readRequest = entry.getValue();
    			
    				keysQuery.remove(readRequest.key);
    				
    				logger.log(Level.FINE, "setReadRequest readRequest.key["+readRequest.key+"] readRequest.scsEnvId["+readRequest.scsEnvId+"] readRequest.dbaddresses["+readRequest.dbaddresses+"]");

					m_RTDBAccess.readValueRequest(readRequest.key, readRequest.scsEnvId, readRequest.dbaddresses);
					
    			} catch (NoSuchElementException e)  {
    				Window.alert(e.toString());
    			}
    		}
    	}
    	
    	logger.log(Level.FINE, "setReadRequest End");
    	
    }
    private void getReadResult(String key, String[] values, int errorCode, String errorMessage) {
    	
    	logger.log(Level.FINE, "getReadResult Begin");
    	
    	this.wrapperScsRTDBAccessEvent.setReadResult(key, values, errorCode, errorMessage);
    	
    	//ReadRequest readRequest = keysWaiting.remove(key);
    	
    	logger.log(Level.FINE, "getReadResult key["+key+"] values[0]["+values[0]+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
    	
    	setReadRequest();
    	
    	logger.log(Level.FINE, "getReadResult End");
    }
    
    private boolean withQuery = false;
    /**
     * @param key
     * 				Key - "ReadValue"
     * @param scsEnvId
     * 				ScsEnvName - "B001"
     * @param dbaddresses
     * 				DB Address - ":SITE1:B001:F001:ACCESS:DO001.UNIVNAME"
     */
    public void readValueRequest(String key, String scsEnvId, String dbaddresses) {
    	
    	logger.log(Level.FINE, "readValueRequest Begin");
    	
    	logger.log(Level.FINE, "readValueRequest key["+key+"] scsEnvId["+scsEnvId+"] dbaddresses["+dbaddresses+"]");
    	
    	if ( withQuery ) {
    		addReadRequest(key, scsEnvId, dbaddresses);
    	} else {
    		m_RTDBAccess.readValueRequest(key, scsEnvId, dbaddresses);
    	}
    	
    	logger.log(Level.FINE, "readValueRequest End");
    }
    
    private HashMap<String, String[]> hashMapCache = new HashMap<String, String[]>();
    public void cachePut(String dbaddresses, String[] values) {
    	hashMapCache.put(dbaddresses, values);
    }
    public String[] cacheGet(String dbaddresses) {
    	return hashMapCache.get(dbaddresses);
    }
    
    /**
     * @param key
     * 				Key - "ReadValue"
     * @param scsEnvId
     * 				ScsEnvName - "B001"
     * @param dbaddresses
     * 				DB Address - ":SITE1:B001:F001:ACCESS:DO001.UNIVNAME"
     */
    public void readValueRequestCache(String key, String scsEnvId, String dbaddresses) {
    	
    	logger.log(Level.FINE, "readValueRequestCache Begin");
    	
    	logger.log(Level.FINE, "readValueRequestCache key["+key+"] scsEnvId["+scsEnvId+"] dbaddresses["+dbaddresses+"]");  	
    	
    	String values[] = hashMapCache.get(dbaddresses);
    	int errorCode = 0;
    	String errorMessage = "";
    	if ( null != values ) {
    		this.wrapperScsRTDBAccessEvent.setReadResult(key, values, errorCode, errorMessage);
    	} else {
        	errorCode = 1;
        	errorMessage = "NOT_FOUND";
    		this.wrapperScsRTDBAccessEvent.setReadResult(key, values, errorCode, errorMessage);
    	}

    	
    	logger.log(Level.FINE, "readValueRequestCache End");
    }
    
    @Override
    public void terminate() {
    	logger.log(Level.FINE, "terminate Begin");
        try {
            m_RTDBAccess.terminate();
        } catch (final IllegalStatePresenterException e) {
            e.printStackTrace();
        }
//        clearHMI();
        logger.log(Level.FINE, "terminate End");
    }

    @Override
    public void setPresenter(HypervisorPresenterClientAbstract<?> presenter) {
    	/*
    	 * =================================
    	 */
       
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
		// TODO Auto-generated method stub
	}

	@Override
    public void setGetChildrenAliasesResult(String clientKey, String[] values, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
    }
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
	public void setGetAttributesResult(String clientKey, String[] attributes, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setGetAttributesDescriptionResult(String clientKey, ScsClassAttInfo[] attributesInfo, int errorCode,
			String errorMessage) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setGetAttributeStructureResult(String clientKey, String attType, int fieldCount, int recordCount,
			int recordSize, String[] fieldNames, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setGetUserFormulasNamesResult(String clientKey, String[] formulas, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetDatabaseInfoResult(String clientKey, long dbsize, int nbClasses, int nbFormula, int nbInstances,
			String scsPath, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setForceSnapshotResult(String clientKey, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setGetAttributeFormulasResult(String clientKey, String[] formulas, int errorCode, String errorMessage) {
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
}
