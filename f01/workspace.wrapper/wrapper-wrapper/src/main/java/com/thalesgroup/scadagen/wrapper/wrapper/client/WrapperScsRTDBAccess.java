package com.thalesgroup.scadagen.wrapper.wrapper.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

public class WrapperScsRTDBAccess {
	
	private static Logger logger = Logger.getLogger(WrapperScsRTDBAccess.class.getName());
	
	private final String strWaitingList		= "waitingList";
	private final String strReading			= "reading";
	private final String strSubscriptions	= "subscriptions";
	private final String strGetChildrens	= "getChildrens";
	
	private HashMap<String, LinkedList<ReadRequest>> lists = null;

	// List
	private LinkedList<ReadRequest> waitingList		= new LinkedList<ReadRequest>();
	private LinkedList<ReadRequest> reading			= new LinkedList<ReadRequest>();
	private LinkedList<ReadRequest> subscriptions	= new LinkedList<ReadRequest>();
	private LinkedList<ReadRequest> getChildrens	= new LinkedList<ReadRequest>();
	
	public void reset() {
		logger.log(Level.SEVERE, "reset Begin");
		remove("");
		logger.log(Level.SEVERE, "reset End");
	}
    
	public void remove(String prefix) {
		logger.log(Level.SEVERE, "remove Begin");
		remove(strWaitingList, prefix);
		remove(strReading, prefix);
		remove(strSubscriptions, prefix);
		remove(strGetChildrens, prefix);	
		logger.log(Level.SEVERE, "remove End");
	}
	
	public void remove(String key, String prefix) {
		LinkedList<ReadRequest> list = lists.get(key);
		logger.log(Level.SEVERE, "remove Begin");
		logger.log(Level.SEVERE, "remove list["+key+"] prefix["+prefix+"]");
    	Iterator<ReadRequest> readRequests = list.iterator();
    	while ( readRequests.hasNext() ) {
    		ReadRequest readRequest = readRequests.next();
    		if ( null != readRequest && readRequest.clientKey != null && readRequest.clientKey.startsWith(prefix) ) {
    			readRequests.remove();
    		}
    	}
    	logger.log(Level.SEVERE, "remove End");
	}
	
	private static WrapperScsRTDBAccess instance = null;
	public static WrapperScsRTDBAccess getInstance() {
		if ( null == instance ) instance = new WrapperScsRTDBAccess();
		return instance;
	}
	
	private Timer timer = null;
	private ScsRTDBComponentAccess rtdb = null;
	private WrapperScsRTDBAccess () {
		
		logger.log(Level.SEVERE, "WrapperScsRTDBAccess3 Begin");
		
		lists = new HashMap<String, LinkedList<ReadRequest>>();

		// List
		waitingList		= new LinkedList<ReadRequest>();
		reading			= new LinkedList<ReadRequest>();
		subscriptions	= new LinkedList<ReadRequest>();
		getChildrens	= new LinkedList<ReadRequest>();
		
		lists.put(strWaitingList, waitingList);
		lists.put(strReading, reading);
		lists.put(strSubscriptions, subscriptions);
		lists.put(strGetChildrens, getChildrens);
		
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
				// TODO Auto-generated method stub
				
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
		
		if ( null == timer ) {
			timer = new Timer() {
				@Override
				public void run() {
					
					boolean b1 = false;
					boolean b2 = false;
					boolean b3 = false;
					
					b1 = walkThrought(strWaitingList, true, true, true);
					
					if ( !b1 )
						b2 = walkThrought(strReading, true, true, true);
					
					if ( !b1 && !b2 )
						b3 = walkThrought(strSubscriptions, true, false, true);
					
					if ( !b1 && !b2 && !b3 )
						walkThrought(strGetChildrens, true, true, false);
				};
			};
			timer.scheduleRepeating(1000);			
		} else {
			logger.log(Level.SEVERE, "multiReadValueRequest timer EXISTS");
		}
		
		logger.log(Level.SEVERE, "WrapperScsRTDBAccess3 End");
	}
	
	private boolean walkThrought(String key, boolean execute, boolean remove, boolean once) {
		boolean executed = false;
		LinkedList<ReadRequest> list = lists.get(key);
		// Subscription
		if ( null != list && list.size() > 0 ) {
			Iterator<ReadRequest> iterator = list.iterator();
			while ( iterator.hasNext() ) {
				ReadRequest readRequest = iterator.next();
				if ( null != readRequest ) {
					if ( execute ) {
						executed = true;
						multiReadValueRequest(readRequest);
					}
				} else {
					logger.log(Level.SEVERE, "multiReadValueRequest readRequest IS NULL");
				}
				if ( remove ) iterator.remove();
				if ( once ) break;
			}
		}
		
		return executed;
	}

	private boolean multiReadValueRequest(ReadRequest readRequest) {
		logger.log(Level.SEVERE, "multiReadValueRequest Begin");
		String clientKey = readRequest.clientKey;
		String scsEnvId = readRequest.scsEnvId;
		String[] dbaddresses = readRequest.dbaddresses;
		Result readResult = readRequest.result;
		boolean result = multiReadValueRequest(clientKey, scsEnvId, dbaddresses, readRequest.resultName, readResult);
		logger.log(Level.SEVERE, "multiReadValueRequest End");
		return result;
	}

    public boolean multiReadValueRequest(String key, String scsEnvId, String[] dbaddresses, String resultName, Result readResult) {
    	
    	boolean result = false;
    	
    	logger.log(Level.SEVERE, "multiReadValueRequest Begin");
    	
    	logger.log(Level.SEVERE, "multiReadValueRequest key["+key+"]");
    	logger.log(Level.SEVERE, "multiReadValueRequest scsEnvId["+scsEnvId+"]");
    	
    	ReadRequest readRequest = new ReadRequest(key, scsEnvId, dbaddresses, resultName, readResult);
    	
    	if ( reading.size() > 1 ) {
    		
    		logger.log(Level.SEVERE, "multiReadValueRequest reading.size()["+reading.size()+"] > 1");
    		logger.log(Level.SEVERE, "multiReadValueRequest putting to waiting list");
    		
    		// Remove the same request from list
    		
    		waitingList.add(readRequest);
    		
    		return true;
    	}
    	
    	for(int i = 0; i < dbaddresses.length; ++i ) {
    		logger.log(Level.SEVERE, "multiReadValueRequest dbaddresses("+i+")["+dbaddresses[i]+"]");
    	}
    	
    	reading.add(readRequest);
    	
    	performMultiReadValueRequest(readRequest);
    	
        logger.log(Level.SEVERE, "multiReadValueRequest End");
        
		return result;
    }
    
    /**
     * @param key
     * @param scsEnvId
     * @param dbaddresses
     */
    private void performMultiReadValueRequest(ReadRequest readRequest) {

		String clientKey = readRequest.clientKey;
		String scsEnvId = readRequest.scsEnvId;
		String[] dbaddresses = readRequest.dbaddresses;
		
		logger.log(Level.SEVERE, "performMultiReadValueRequest Begin");
		
		logger.log(Level.SEVERE, "performMultiReadValueRequest key["+clientKey+"]");
		logger.log(Level.SEVERE, "performMultiReadValueRequest scsEnvId["+scsEnvId+"]");
		
		for(int i = 0; i < dbaddresses.length; ++i ) {
			logger.log(Level.SEVERE, "performMultiReadValueRequest dbaddresses("+i+")["+dbaddresses[i]+"]");
		}

		JSONObject jsparam = new JSONObject();
		
		// build dbaddress param with a list of address
		JSONArray addr = new JSONArray();
		for(int i = 0; i < dbaddresses.length; ++i) {
			addr.set(i, new JSONString(dbaddresses[i]));
		}
		
		jsparam.put("dbaddress", addr);
		
		JSONObject jsdata = this.rtdb.buildJSONRequest("multiReadValue", jsparam);
		
		this.rtdb.sendJSONRequest(clientKey, scsEnvId, jsdata.toString());
		
		logger.log(Level.SEVERE, "performMultiReadValueRequest End");
    }
    
    /**
     * 
     * @param key
     * @param scsEnvId
     * @param dbaddresses
     * @param readResult
     */
    public void multiReadValueRequestCaches(String clientKey, String scsEnvId, String[] dbaddresses, String resultName, ReadResult readResult) {
		logger.log(Level.SEVERE, "multiReadValueRequestCaches Begin");
		multiReadValueRequest(clientKey, scsEnvId, dbaddresses, resultName, readResult);
		logger.log(Level.SEVERE, "multiReadValueRequestCaches End");
    }
    
    private void setReadResult2(String clientKey, String[] values, int errorCode, String errorMessage) {
    	logger.log(Level.SEVERE, "setReadResult2 Begin");
    	logger.log(Level.SEVERE, "setReadResult2 clientKey["+clientKey+"]");
    	logger.log(Level.SEVERE, "setReadResult2 errorCode["+errorCode+"]");
    	logger.log(Level.SEVERE, "setReadResult2 errorMessage["+errorMessage+"]");

    	logger.log(Level.SEVERE, "setReadResult2 ReadResult.class.getName()["+ReadResult.class.getName()+"]");
    	logger.log(Level.SEVERE, "setReadResult2 SubscriptionResult.class.getName()["+SubscriptionResult.class.getName()+"]");		
		
		// Remove the result from the reading list
    	Iterator<ReadRequest> readRequests = reading.iterator();
    	while ( readRequests.hasNext() ) {
    		ReadRequest readRequest = readRequests.next();

    		logger.log(Level.SEVERE, "setReadResult2 readRequest.clientKey["+readRequest.clientKey+"]");
    		
    		if ( readRequest.equalToKey(clientKey) ) {
    			Result result = readRequest.result;
    			
    			logger.log(Level.SEVERE, "setReadResult2 readRequest.resultName["+readRequest.resultName+"]");
    			
    			String results[][] = new String[values.length][];
    			for ( int i = 0 ; i < values.length ; ++i ) {
    				results[i] = new String[]{readRequest.dbaddresses[i], values[i]};
    			}
    			if ( 0 == ReadResult.class.getName().compareTo(readRequest.resultName) ) {
    				((ReadResult)result).setReadResult(clientKey, results, errorCode, errorMessage);
    			} else if ( 0 == SubscriptionResult.class.getName().compareTo(readRequest.resultName) ) {
    				((SubscriptionResult)result).setReadResultSubscription(clientKey, results, errorCode, errorMessage);
    			}
    			readRequests.remove();
    		}
    	}
		for(int i = 0 ; i < values.length ; ++i ) {
			logger.log(Level.SEVERE, "setReadResult2 values("+i+") values["+values[i]+"]");
		}
		logger.log(Level.SEVERE, "setReadResult2 End");
    }
	
    public boolean subscriptionRequest(String key, String scsEnvId, String[] dbaddresses, SubscriptionResult subscription) {
        	
    	logger.log(Level.SEVERE, "subscriptionRequest Begin");
    	
    	logger.log(Level.SEVERE, "subscriptionRequest key["+key+"]");
    	logger.log(Level.SEVERE, "subscriptionRequest scsEnvId["+scsEnvId+"]");
    	
    	for(int i = 0; i < dbaddresses.length; ++i ) {
    		logger.log(Level.SEVERE, "subscriptionRequest dbaddresses("+i+")["+dbaddresses[i]+"]");
    	}
    	
    	Iterator<ReadRequest> iterator = subscriptions.iterator();
    	while ( iterator.hasNext() ) {
    		ReadRequest readRequest = iterator.next();
    		
    		if ( readRequest.equalToKey(key) ) {
        		logger.log(Level.SEVERE, "subscriptionRequest key["+key+"] ALREADY EXISTS, subscription CANCELED");
        		return false;
    		}
    	}
    	
    	ReadRequest readRequest = new ReadRequest(key, scsEnvId, dbaddresses, SubscriptionResult.class.getName(), subscription);
    	
    	logger.log(Level.SEVERE, "subscriptionRequest add to subscriptions list");
    	
    	subscriptions.add(readRequest);
    	
//    	multiReadValueRequest(readRequest);
    	
        logger.log(Level.SEVERE, "subscriptionRequest End");
            
        return true;
    }

    public void getChildren(String key, String scsEnvId, String dbaddress, ChildrenResult readChildren) {
    	
    	logger.log(Level.SEVERE, "getChildren Begin");
    	logger.log(Level.SEVERE, "getChildren key["+key+"]");
    	logger.log(Level.SEVERE, "getChildren scsEnvId["+scsEnvId+"]");
    	logger.log(Level.SEVERE, "getChildren dbaddress["+dbaddress+"]");
    	
        JSONObject jsparam = new JSONObject();

        // build param list
        jsparam.put("dbaddress", new JSONString(dbaddress));

        JSONObject jsdata = this.rtdb.buildJSONRequest("GetChildren", jsparam);
        
        ReadRequest readRequest = new ReadRequest(key, scsEnvId, new String[]{dbaddress}, ChildrenResult.class.getName(), readChildren);
        
        getChildrens.add(readRequest);
        
        this.rtdb.sendJSONRequest(key, scsEnvId, jsdata.toString());
        
        logger.log(Level.SEVERE, "getChildren End");
    }
    
    private void setGetChildrenResult2(String clientKey, String[] instances, int errorCode, String errorMessage) {
    	logger.log(Level.SEVERE, "setGetChildrenResult2 Begin");
    	logger.log(Level.SEVERE, "setGetChildrenResult2 clientKey["+clientKey+"]");
    	logger.log(Level.SEVERE, "setGetChildrenResult2 errorCode["+errorCode+"]");
    	logger.log(Level.SEVERE, "setGetChildrenResult2 errorMessage["+errorMessage+"]");
    	
        logger.log(Level.SEVERE, "setGetChildrenResult2 ChildrenResult.class.getName()["+ChildrenResult.class.getName()+"]");     	
    	
    	if ( null != instances ) {
    		// Remove the result from the reading list
        	Iterator<ReadRequest> readRequests = getChildrens.iterator();
        	while ( readRequests.hasNext() ) {
        		ReadRequest readRequest = readRequests.next();
        		
        		logger.log(Level.SEVERE, "setGetChildrenResult2 readRequest.clientKey["+readRequest.clientKey+"]");
        		
        		if ( readRequest.equalToKey(clientKey) ) {
        			Result result = readRequest.result;
        			
        			logger.log(Level.SEVERE, "setGetChildrenResult2 readRequest.resultName["+readRequest.resultName+"]");
        			
        			if ( 0 == ChildrenResult.class.getName().compareTo(readRequest.resultName) ) {
        				((ChildrenResult)result).setGetChildrenResult(clientKey, instances, errorCode, errorMessage);
        			}
        			readRequests.remove();
        		}
        	}
			for(int i = 0 ; i < instances.length ; ++i ) {
				logger.log(Level.SEVERE, "setGetChildrenResult2 instances("+i+") instances["+instances[i]+"]");
			}
    	} else {
    		logger.log(Level.SEVERE, "setGetChildrenResult2 instances IS NULL");
    	}
		logger.log(Level.SEVERE, "setGetChildrenResult2 End");
    }
}
