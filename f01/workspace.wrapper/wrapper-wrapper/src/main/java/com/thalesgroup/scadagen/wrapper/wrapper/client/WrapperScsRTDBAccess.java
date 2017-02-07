package com.thalesgroup.scadagen.wrapper.wrapper.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

public class WrapperScsRTDBAccess {
	
	//private static Logger logger = Logger.getLogger(WrapperScsRTDBAccess.class.getName());
	private static final String className = UIWidgetUtil.getClassSimpleName(WrapperScsRTDBAccess.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);	
	
	private final String strWaitingList		= "waitingList";
	private final String strSubscriptions	= "subscriptions";
	private final String strGetChildrens	= "getChildrens";
	
	private HashMap<String, LinkedList<ReadRequest>> lists = null;

	// List
	private LinkedList<ReadRequest> waitingList		= null;
	private LinkedList<ReadRequest> reading			= null;
	private LinkedList<ReadRequest> subscriptions	= null;
	private LinkedList<ReadRequest> getChildrens	= null;
	
	private Map<String, MultiReadResult> multiReadMap = new HashMap<String, MultiReadResult>();
	private Map<String, GetChildrenResult> getChildrenMap = new HashMap<String, GetChildrenResult>();
	
	public void remove(String prefix) {
		logger.debug("remove Begin");
		logger.debug("remove prefix["+prefix+"]");
		remove(strWaitingList, prefix);
		remove(strSubscriptions, prefix);
		remove(strGetChildrens, prefix);
		logger.debug("remove End");
	}
	
	public void remove(String key, String prefix) {
		LinkedList<ReadRequest> list = lists.get(key);
		logger.debug("remove Begin");
		logger.debug("remove list["+key+"] prefix["+prefix+"]");
    	Iterator<ReadRequest> readRequests = list.iterator();
    	while ( readRequests.hasNext() ) {
    		ReadRequest readRequest = readRequests.next();
    		if ( null != readRequest && readRequest.clientKey != null && readRequest.clientKey.startsWith(prefix) ) {
    			readRequests.remove();
    		}
    	}
    	logger.debug("remove End");
	}
	
	private boolean enableCache = false;
	public void setEnableCache(boolean enable) { this.enableCache = enable; }
	
	private static WrapperScsRTDBAccess instance = null;
	public static WrapperScsRTDBAccess getInstance() {
		if ( null == instance ) instance = new WrapperScsRTDBAccess();
		return instance;
	}
	
	private Timer timer = null;
	private ScsRTDBComponentAccess rtdb = null;
	private WrapperScsRTDBAccess () {
		
		logger.debug("WrapperScsRTDBAccess3 Begin");
		
		lists = new HashMap<String, LinkedList<ReadRequest>>();

		// List
		waitingList		= new LinkedList<ReadRequest>();
		subscriptions	= new LinkedList<ReadRequest>();
		getChildrens	= new LinkedList<ReadRequest>();
		
		lists.put(strWaitingList, waitingList);
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
			public void setReadResult(String clientKey, String[] value, int errorCode, String errorMessage) {
				
				logger.debug("setReadResult Begin");
				
				//setReadResponse(clientKey, value, errorCode, errorMessage);
				
				MultiReadResult multiReadRes = multiReadMap.get(clientKey);

				if (multiReadRes != null) {
					multiReadRes.setReadResult(clientKey, value, errorCode, errorMessage);
					multiReadMap.remove(clientKey);
				}
				
				logger.debug("setReadResult End");
				
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
				
				logger.debug("setGetChildrenResult Begin");
				
				//setGetChildrenResponse(clientKey, instances, errorCode, errorMessage);
				
				GetChildrenResult getChildrenRes = getChildrenMap.get(clientKey);

				if (getChildrenRes != null) {
					getChildrenRes.setGetChildrenResult(clientKey, instances, errorCode, errorMessage);
					getChildrenMap.remove(clientKey);
				} else {
					logger.warn("setGetChildrenResult result callback not found");
				}
				
				logger.debug("setGetChildrenResult End");
				
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
		

		logger.debug("WrapperScsRTDBAccess3 End");
	}
	
	public void startTimer(int interval) {
		if ( null == timer ) {
			timer = new Timer() {
				@Override
				public void run() {
					
					logger.info("startTimer.run waitingChildren["+waitingChildren+"]");
					
					logger.info("startTimer.run waitingMultiRead["+waitingMultiRead+"]");
					
					if ( ! waitingChildren ) {
						if ( ! waitingMultiRead ) {
							
							LinkedList<ReadRequest> list = lists.get(strWaitingList);
							if ( null != list && list.size() > 0 ) {
								ReadRequest readRequest = list.removeFirst();
								
								if ( 0 == ChildrenResult.class.getName().compareTo(readRequest.resultName) ) {
									getChildren(readRequest);
								} else {
									performMultiReadValueRequest(readRequest);
								}
													
							}

						}
					}
					
				};
			};
			timer.scheduleRepeating(interval);			
		} else {
			logger.debug("multiReadValueRequest timer EXISTS");
		}
		
	}
	

//	private boolean multiReadValueRequest(ReadRequest readRequest) {
//		logger.info("multiReadValueRequest Begin");
//		
//		String clientKey = readRequest.clientKey;
//		String scsEnvId = readRequest.scsEnvId;
//		String[] dbaddresses = readRequest.dbaddresses;
//		Result readResult = readRequest.result;
//		boolean result = multiReadValueRequest(clientKey, scsEnvId, dbaddresses, readRequest.resultName, readResult);
//		
//		logger.info("multiReadValueRequest End");
//		return result;
//	}
    
    public boolean multiReadValueRequest(String key, String scsEnvId, String[] dbaddresses, String resultName, Result readResult) {
    	
    	boolean result = false;
    	
    	logger.info("multiReadValueRequest Begin");
    	
    	logger.info("multiReadValueRequest key["+key+"] scsEnvId["+scsEnvId+"] putting to waiting list");
    	
    	ReadRequest readRequest = new ReadRequest(key, scsEnvId, dbaddresses, resultName, readResult);
    		
    	waitingList.add(readRequest);
    	
        logger.info("multiReadValueRequest End");
        
		return result;
    }
    
    private boolean waitingMultiRead = false;
    private void performMultiReadValueRequest(ReadRequest readRequest) {
    	
    	logger.info("performMultiReadValueRequest Begin");

		String clientKey = readRequest.clientKey;
		String scsEnvId = readRequest.scsEnvId;
		String[] dbaddresses = readRequest.dbaddresses;
		
		logger.info("performMultiReadValueRequest key["+clientKey+"] scsEnvId["+scsEnvId+"]");
		
		for(int i = 0; i < dbaddresses.length; ++i ) {
			logger.info("performMultiReadValueRequest dbaddresses("+i+")["+dbaddresses[i]+"]");
		}

		waitingMultiRead = true;
		
		reading.add(readRequest);
		
		this.rtdb.multiReadValueRequest(clientKey, scsEnvId, dbaddresses);
		
		logger.info("performMultiReadValueRequest End");
    }
    
    /**
     * 
     * @param key
     * @param scsEnvId
     * @param dbaddresses
     * @param readResult
     */
    public void multiReadValueRequestCaches(String clientKey, String scsEnvId, String[] dbaddresses, String resultName, ReadResult readResult) {
		logger.info("multiReadValueRequestCaches Begin");
		
		logger.info("multiReadValueRequestCaches enableCache["+enableCache+"]");

		if ( enableCache && readCashed.containsKey(clientKey) ) {
			
			logger.info("multiReadValueRequestCaches clientKey["+clientKey+"] FOUND");
			
			logger.info("multiReadValueRequestCaches setReadResult Begin");
			
			String[][] values = readCashed.get(clientKey);
			
			readResult.setReadResult(clientKey, values, 0, "");
			
			logger.info("multiReadValueRequestCaches setReadResult End");
			
		} else {
			
			multiReadValueRequest(clientKey, scsEnvId, dbaddresses, resultName, readResult);
			
		}
		
		logger.info("multiReadValueRequestCaches End");
    }
    
    private HashMap<String, String[][]> readCashed = new HashMap<String, String[][]>();
    private void setReadResponse(String clientKey, String[] values, int errorCode, String errorMessage) {
    	logger.info("setReadResponse Begin");
    	logger.info("setReadResponse clientKey["+clientKey+"]");
    	logger.info("setReadResponse errorCode["+errorCode+"]");
    	logger.info("setReadResponse errorMessage["+errorMessage+"]");

    	logger.info("setReadResponse ReadResult.class.getName()["+ReadResult.class.getName()+"]");
    	logger.info("setReadResponse SubscriptionResult.class.getName()["+SubscriptionResult.class.getName()+"]");
    	
    	waitingMultiRead = false;

		// Remove the result from the reading list
    	Iterator<ReadRequest> readRequests = reading.iterator();
    	while ( readRequests.hasNext() ) {
    		ReadRequest readRequest = readRequests.next();

    		logger.info("setReadResponse readRequest.clientKey["+readRequest.clientKey+"]");
    		
    		if ( readRequest.equalToKey(clientKey) ) {
    			Result result = readRequest.result;
    			
    			logger.info("setReadResponse readRequest.resultName["+readRequest.resultName+"]");
    			
    			String results[][] = new String[values.length][];
    			for ( int i = 0 ; i < values.length ; ++i ) {
    				results[i] = new String[]{readRequest.dbaddresses[i], values[i]};
    			}
    			if ( 0 == ReadResult.class.getName().compareTo(readRequest.resultName) ) {
    				
    				readCashed.put(clientKey, results);
    				
    				((ReadResult)result).setReadResult(clientKey, results, errorCode, errorMessage);
    			} else if ( 0 == SubscriptionResult.class.getName().compareTo(readRequest.resultName) ) {
    				((SubscriptionResult)result).setReadResultSubscription(clientKey, results, errorCode, errorMessage);
    			}

    			readRequests.remove();
    		}
    	}
		for(int i = 0 ; i < values.length ; ++i ) {
			logger.info("setReadResponse values("+i+") values["+values[i]+"]");
		}
		logger.info("setReadResponse End");
    }
	
    public boolean subscriptionRequest(String key, String scsEnvId, String[] dbaddresses, SubscriptionResult subscription) {
        	
    	logger.info("subscriptionRequest Begin");
    	
    	logger.info("subscriptionRequest key["+key+"]");
    	logger.info("subscriptionRequest scsEnvId["+scsEnvId+"]");
    	
    	for(int i = 0; i < dbaddresses.length; ++i ) {
    		logger.info("subscriptionRequest dbaddresses("+i+")["+dbaddresses[i]+"]");
    	}
    	
    	Iterator<ReadRequest> iterator = subscriptions.iterator();
    	while ( iterator.hasNext() ) {
    		ReadRequest readRequest = iterator.next();
    		
    		if ( readRequest.equalToKey(key) ) {
        		logger.info("subscriptionRequest key["+key+"] ALREADY EXISTS, subscription CANCELED");
        		return false;
    		}
    	}
    	
    	ReadRequest readRequest = new ReadRequest(key, scsEnvId, dbaddresses, SubscriptionResult.class.getName(), subscription);
    	
    	logger.info("subscriptionRequest add to subscriptions list");
    	
    	subscriptions.add(readRequest);
    	
        logger.info("subscriptionRequest End");
            
        return true;
    }

    
    private HashMap<String, String[]> childrenCached = new HashMap<String, String[]>();
    private boolean waitingChildren = false;
    public void getChildren(String clientKey, String scsEnvId, String dbaddress, ChildrenResult readChildren) {
    	
    	logger.info("getChildren Begin");
    	logger.info("getChildren clientKey["+clientKey+"]");
    	logger.info("getChildren scsEnvId["+scsEnvId+"]");
    	logger.info("getChildren dbaddress["+dbaddress+"]");
    	
    	logger.info("getChildren enableCache["+enableCache+"]");
    	
    	if ( enableCache && childrenCached.containsKey(clientKey) ) {
    		
    		logger.info("getChildren clientKey["+clientKey+"] FOUND");
    		
    		String[] instances = childrenCached.get(clientKey);
    		
    		readChildren.setGetChildrenResult(clientKey, instances, 0, "");
    		
    	} else {
    		
    		ReadRequest readRequest = new ReadRequest(clientKey, scsEnvId, new String[]{dbaddress}, ChildrenResult.class.getName(), readChildren);
    		
 			waitingList.add(readRequest);   		
    		
    	}

        logger.info("getChildren End");
    }
    
    private void getChildren(ReadRequest readRequest) {
    	
		String clientKey = readRequest.clientKey;
		String scsEnvId = readRequest.scsEnvId;
		String[] dbaddresses = readRequest.dbaddresses;
		
		String dbaddress = dbaddresses[0];
		    
		getChildrens.add(readRequest);
		
		waitingChildren = true;
		
		this.rtdb.getChildren(clientKey, scsEnvId, dbaddress);
    }
    
    private void setGetChildrenResponse(String clientKey, String[] instances, int errorCode, String errorMessage) {
    	logger.info("setGetChildrenResponse Begin");
    	logger.info("setGetChildrenResponse clientKey["+clientKey+"]");
    	logger.info("setGetChildrenResponse errorCode["+errorCode+"]");
    	logger.info("setGetChildrenResponse errorMessage["+errorMessage+"]");
    	
        logger.info("setGetChildrenResponse ChildrenResult.class.getName()["+ChildrenResult.class.getName()+"]");
        
        waitingChildren = false;
        
        childrenCached.put(clientKey, instances);
       
    	if ( null != instances ) {
    		// Remove the result from the reading list
        	Iterator<ReadRequest> readRequests = getChildrens.iterator();
        	while ( readRequests.hasNext() ) {
        		ReadRequest readRequest = readRequests.next();
        		
        		logger.info("setGetChildrenResponse readRequest.clientKey["+readRequest.clientKey+"]");
        		
        		if ( readRequest.equalToKey(clientKey) ) {
        			Result result = readRequest.result;
        			
        			logger.info("setGetChildrenResponse readRequest.resultName["+readRequest.resultName+"]");
        			
        			if ( 0 == ChildrenResult.class.getName().compareTo(readRequest.resultName) ) {
        				((ChildrenResult)result).setGetChildrenResult(clientKey, instances, errorCode, errorMessage);
        			}
	
        			logger.info("setGetChildrenResponse remove readRequest");

        		}
        	}
			for(int i = 0 ; i < instances.length ; ++i ) {
				logger.info("setGetChildrenResponse instances("+i+") instances["+instances[i]+"]");
			}
    	} else {
    		logger.info("setGetChildrenResponse instances IS NULL");
    	}
		logger.info("setGetChildrenResponse End");
    }
    
    public void getChildren(String key, String scsEnvId, String dbaddress, GetChildrenResult getChildrenRes) {
    	final String function = "getChildren";
    	getChildrenMap.put(key, getChildrenRes);
    	logger.debug(className, function, "key=[{}] scsEnvId=[{}]", key, scsEnvId);
    	
    	rtdb.getChildren(key, scsEnvId, dbaddress);
    }
    
    public void multiReadValue(String key, String scsEnvId, String [] dbaddress, MultiReadResult multiReadRes) {
    	final String function = "multiReadValue";
    	multiReadMap.put(key, multiReadRes);
    	
    	logger.debug(className, function, "key=[{}] scsEnvId=[{}]", key, scsEnvId);
    	
    	rtdb.multiReadValueRequest(key, scsEnvId, dbaddress);
    }
}
