package com.thalesgroup.scadagen.wrapper.wrapper.client.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

public class Database {
	
	private static Logger logger = Logger.getLogger(Database.class.getName());
	
	private static Database instance = null;
	public static Database getInstance () {
		if ( null == instance ) instance = new Database();
		return instance;
	}
	private Database() {}
	
	private String scsEnvId;
	private String parent;
	
	public void setDynamic(String scsEnvId, String parent) {
		this.scsEnvId = scsEnvId;
		this.parent = parent;
	}
	
	private DatabaseEvent dynaimcDatabaseEvent = null;
	public void setDynamicEvent(DatabaseEvent dynaimcDatabaseEvent) {
		this.dynaimcDatabaseEvent = dynaimcDatabaseEvent;
	}
	
	private HashMap<String, String[]> KeyAndAddress			= new HashMap<String, String[]>();
	public String[] getKeyAndAddress(String key) { return this.KeyAndAddress.get(key); }
	
	private HashMap<String, String[]> KeyAndValues			= new HashMap<String, String[]>();
	public String[] getKeyAndValues(String key) { return this.KeyAndValues.get(key); }
	
	private ScsRTDBComponentAccess rtdb = null;
	
	private LinkedList<JSONRequest> requestStatics			= new LinkedList<JSONRequest>();
	private HashMap<String, String[]> requestDynamics		= new HashMap<String, String[]>();
	private HashMap<String, DatabaseEvent> databaseEvents	= new HashMap<String, DatabaseEvent>();
	
	public void addStaticRequest(String api, String clientKey, String scsEnvId, String dbaddress, String [] dbaddresses, DatabaseEvent databaseEvent) {
		requestStatics.add(new JSONRequest(api, clientKey, scsEnvId, dbaddress));
		KeyAndAddress.put(clientKey, dbaddresses);
		databaseEvents.put(clientKey, databaseEvent);
	}
	public void addStaticRequest(String api, String clientKey, String scsEnvId, String [] dbaddresses, DatabaseEvent databaseEvent) {
		requestStatics.add(new JSONRequest(api, clientKey, scsEnvId, dbaddresses));
		KeyAndAddress.put(clientKey, dbaddresses);
		databaseEvents.put(clientKey, databaseEvent);
	}
	public void addStaticRequest(String api, String clientKey, String scsEnvId, String dbaddresses, DatabaseEvent databaseEvent) {
		requestStatics.add(new JSONRequest(api, clientKey, scsEnvId, dbaddresses));
		KeyAndAddress.put(clientKey, new String[]{dbaddresses});
		databaseEvents.put(clientKey, databaseEvent);
	}
	
	public void addDynamicRequest(String clientKey, String[] dbaddresses, DatabaseEvent databaseEvent) {
		requestDynamics.put(clientKey, dbaddresses);
		KeyAndAddress.put(clientKey, dbaddresses);
		databaseEvents.put(clientKey, databaseEvent);
	}
	
	public void connect() {
		
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
				
				logger.log(Level.FINE, "setReadResult Begin");
				
//		    	logger.log(Level.FINE, "setReadResult key["+key+"]");
//		    	logger.log(Level.FINE, "setReadResult errorCode["+errorCode+"]");
//		    	logger.log(Level.FINE, "setReadResult errorMessage["+errorMessage+"]");
//		    	
//				for(int i = 0; i < value.length; ++i) {
//					logger.log(Level.FINE, "setReadResult value["+i+"]["+value[i]+"]");
//				}
		    	
		    	KeyAndValues.put(key, value);
		    	
//		    	updateValue(key, value);
				
		    	// Static
				DatabaseEvent databaseEvent = databaseEvents.get(key);
				if ( null != databaseEvent ) databaseEvent.update(key, value);
				
				// Dynamic
				if ( "dynamic".equals(key.split("_")[2]) ) {
					if ( null != dynaimcDatabaseEvent ) {
						dynaimcDatabaseEvent.update(key, value);
					}
				}

				logger.log(Level.FINE, "setReadResult End");
				
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
				
				logger.log(Level.FINE, "setGetChildrenResult Begin");

//		    	logger.log(Level.FINE, "setGetChildrenResult clientKey["+clientKey+"]");
//		    	logger.log(Level.FINE, "setGetChildrenResult errorCode["+errorCode+"]");
//		    	logger.log(Level.FINE, "setGetChildrenResult errorMessage["+errorMessage+"]");
//		    	
//				for(int i = 0; i < instances.length; ++i) {
//					logger.log(Level.FINE, "setGetChildrenResult instances["+i+"]["+instances[i]+"]");
//				}		    	
		    	
				KeyAndValues.put(clientKey, instances);
				
//				updateValue(clientKey, instances);
				
				// Static
				DatabaseEvent databaseEvent = databaseEvents.get(clientKey);
				if ( null != databaseEvent ) databaseEvent.update(clientKey, instances);
				
				logger.log(Level.FINE, "setGetChildrenResult End");
				
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
	}
	
	private Timer timer = null;
	public void connectTimer(int periodMillis) {
		
		if ( null == timer ) {
			timer = new Timer() {

				@Override
				public void run() {
					if ( null != rtdb ) {
					
						if ( requestStatics.size() > 0 ) {
							
							logger.log(Level.FINE, "sendJSONRequest Begin");
							
							JSONRequest jsonRequest = requestStatics.removeFirst();
							
							if ( 0 == "GetChildren".compareTo(jsonRequest.api) ) {
								
								String api = jsonRequest.api;
								String clientKey = jsonRequest.clientKey;
								String scsEnvId = jsonRequest.scsEnvId;
								String dbaddress = jsonRequest.dbaddress;
								
								logger.log(Level.FINE, "api["+api+"] key["+clientKey+"] scsEnvId["+scsEnvId+"]");
						    	
								JSONObject jsparam = new JSONObject();
								
								// build param list
								jsparam.put("dbaddress", new JSONString(dbaddress));
								
								JSONObject jsdata = rtdb.buildJSONRequest(api, jsparam);
								    
								rtdb.sendJSONRequest(clientKey, scsEnvId, jsdata.toString());
								
							} else if ( 0 == "multiReadValue".compareTo(jsonRequest.api) ) {
								
								String api = jsonRequest.api;
								String clientKey = jsonRequest.clientKey;
								String scsEnvId = jsonRequest.scsEnvId;
								String[] dbaddresses = jsonRequest.dbaddresses;
								
								logger.log(Level.FINE, "api["+api+"] key["+clientKey+"] scsEnvId["+scsEnvId+"]");
								
								JSONObject jsparam = new JSONObject();
								
								// build dbaddress param with a list of address
								JSONArray addr = new JSONArray();
								for(int i = 0; i < dbaddresses.length; ++i) {
									addr.set(i, new JSONString(dbaddresses[i]));
								}
								
								jsparam.put("dbaddress", addr);
								
								JSONObject jsdata = rtdb.buildJSONRequest(api, jsparam);

								rtdb.sendJSONRequest(clientKey, scsEnvId, jsdata.toString());
							}

							logger.log(Level.FINE, "sendJSONRequest End");

							
						} else if ( requestDynamics.size() > 0 ) {
							
							LinkedList<String> dbaddresslist = new LinkedList<String>();
							for ( String key : requestDynamics.keySet() ) {
								for ( String dbaddress : requestDynamics.get(key) ) {
									dbaddresslist.add(dbaddress);
								}
							}
							String[] dbaddresses = dbaddresslist.toArray(new String[0]);
							
							String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "dynamic" + "_" + parent;
							
							KeyAndAddress.put(clientKey, dbaddresses);
									
							String api = "multiReadValue";
		
							logger.log(Level.FINE, "api["+api+"] key["+clientKey+"] scsEnvId["+scsEnvId+"]");
									
							JSONObject jsparam = new JSONObject();
									
							// build dbaddress param with a list of address
							JSONArray addr = new JSONArray();
							for(int i = 0; i < dbaddresses.length; ++i) {
								addr.set(i, new JSONString(dbaddresses[i]));
							}
									
							jsparam.put("dbaddress", addr);
									
							JSONObject jsdata = rtdb.buildJSONRequest(api, jsparam);

							rtdb.sendJSONRequest(clientKey, scsEnvId, jsdata.toString());
						}
					}
				}
				
			};
			
			timer.scheduleRepeating(periodMillis);
		}

	}
	
	public void disconnectTimer() {
		requestStatics.clear();
		requestDynamics.clear();
		
		if ( null != timer ) timer.cancel();
		timer = null;
	}
	
	public void disconnect() {

		try {
			rtdb.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rtdb=null;
	}

	
}