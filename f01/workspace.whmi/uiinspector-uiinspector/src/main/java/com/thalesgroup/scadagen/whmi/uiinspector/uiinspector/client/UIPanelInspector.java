package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

public class UIPanelInspector extends DialogBox implements UIInspectorTab_i, UIInspector_i {
	
	private static Logger logger = Logger.getLogger(UIPanelInspector.class.getName());
	
	private String strTabNames [] = new String[] {"Info","Control","Tagging","Advance"};

	private String strLabel			= ".label";
	private String strIsControlable	= ".isControlable";
	
	// Static Attribute List
	private String staticAttibutes[]	= new String[] {strLabel};

	// Dynamic Attribute List
	private String dynamicAttibutes[]	= new String[] {strIsControlable};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;

	@Override
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	@Override
	public void setAddresses(String scsEnvId, String[] addresses) {
		logger.log(Level.FINE, "setAddresses Begin");
		
		this.scsEnvId = scsEnvId;
		
		logger.log(Level.FINE, "setConnection this.scsEnvId["+this.scsEnvId+"]");
		
		this.addresses = addresses;
		
//		RTDB_Helper.addressesIsValid(this.addresses);
		
		logger.log(Level.FINE, "setAddresses End");
	}
	
	private HashMap<String, String> dynamicvalues = new HashMap<String, String>();
	void updateValue(String clientKey, String [] values) {
		String clientKey_GetChildren_inspectorn_static = "GetChildren" + "inspector" + "static" + parent;
		if ( 0 == clientKey_GetChildren_inspectorn_static.compareTo(clientKey) ) {
			buildTabsAddress(values);
			makeTabsSetAddress();
			makeTabsBuildWidgets();
			connectInspectorInfo();
		}
		String clientKey_multiReadValue_inspector_static = "multiReadValue" + "inspector" + "static" + parent;
		if ( 0 == clientKey_multiReadValue_inspector_static.compareTo(clientKey) ) {
			String [] dbaddresses = KeyAndAddress.get(clientKey);
			String [] dbvalues = KeyAndValues.get(clientKey);
			for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
				String dbaddress = dbaddresses[i];

				if ( dbaddress.endsWith(strLabel) ) {
					String value = dbvalues[i];
					value = RTDB_Helper.removeDBStringWrapper(value);
					if ( null != value) this.setText(value);
					break;
				}
			}
		}

		
		String clientKey_multiReadValue_inspectorinfo_static = "multiReadValue" + "inspectorinfo" + "static" + parent;
		if ( 0 == clientKey_multiReadValue_inspectorinfo_static.compareTo(clientKey) ) {
			String [] dbaddresses = KeyAndAddress.get(clientKey);
			String [] dbvalues = KeyAndValues.get(clientKey);
			HashMap<String, String> keyAndValue = new HashMap<String, String>();
			for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
				keyAndValue.put(dbaddresses[i], dbvalues[i]);
			}
			((UIInspectorInfo)uiInspectorInfo).updateValue(clientKey, keyAndValue);
		}
		
//		String clientKey_multiReadValue_inspector_dynamic = "multiReadValue" + "inspector" + "dynamic" + parent;
//		if ( 0 == clientKey_multiReadValue_inspector_dynamic.compareTo(key) ) {
//			String [] dbaddresses = KeyAndAddress.get(key);
//			String [] dbvalues = KeyAndValues.get(key);
//			for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
//				String dbaddress = dbaddresses[i];
//				if ( dbaddress.endsWith(strIsControlable) ) {
//					String value = dbvalues[i];
//					if ( null != value ) txtAttributeStatus[0].setText((0==value.compareTo("0")?"No":"Yes"));
//					break;
//				}
//			}
//		}
//		String clientKey_multiReadValue_inspectorinfo_dynamic = "multiReadValue" + "inspectorinfo" + "dynamic" + parent;
//		if ( 0 == clientKey_multiReadValue_inspectorinfo_dynamic.compareTo(key) ) {
//			String [] dbaddresses = KeyAndAddress.get(key);
//			String [] dbvalues = KeyAndValues.get(key);
//			HashMap<String, String> keyAndValue = new HashMap<String, String>();
//			for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
//				keyAndValue.put(dbaddresses[i], dbvalues[i]);
//			}
//			((UIInspectorInfo)uiInspectorInfo).updateValue(key, keyAndValue);
//		}
		
		String clientKey_multiReadValue_inspector_dynamic = "multiReadValue" + "inspector" + "dynamic" + parent;
		if ( 0 == clientKey_multiReadValue_inspector_dynamic.compareTo(clientKey) ) {
			String [] dbaddresses = KeyAndAddress.get(clientKey);
			String [] dbvalues = KeyAndValues.get(clientKey);
			for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
				dynamicvalues.put(dbaddresses[i], dbvalues[i]);
			}
			
			String key = parent+ strIsControlable;
			if ( dynamicvalues.containsKey(key) ) {
				String value = dynamicvalues.get(key);
				value = RTDB_Helper.removeDBStringWrapper(value);
				if ( null != value ) txtAttributeStatus[0].setText((0==value.compareTo("0")?"No":"Yes"));
			}
			
			((UIInspectorInfo)uiInspectorInfo).updateValue(key, dynamicvalues);
		}
		
		
	}
	
//	private boolean directly = false;
	
	private HashMap<String, String[]> KeyAndAddress = new HashMap<String, String[]>();
	private HashMap<String, String[]> KeyAndValues = new HashMap<String, String[]>();
	
	class JSONRequest {
		String api;
		String clientKey;
		String scsEnvId;
		String dbaddress;
		String[] dbaddresses;
		public JSONRequest (String api, String clientKey, String scsEnvId, String dbaddress) {
			this.api = api;
			this.clientKey = clientKey;
			this.scsEnvId = scsEnvId;
			this.dbaddress = dbaddress;
		}
		public JSONRequest (String api, String clientKey, String scsEnvId, String[] dbaddresses) {
			this.api = api;
			this.clientKey = clientKey;
			this.scsEnvId = scsEnvId;
			this.dbaddresses = dbaddresses;
		}
	}
	
	ScsRTDBComponentAccess rtdb = null;
	
	LinkedList<JSONRequest> requestStatics = new LinkedList<JSONRequest>();
	HashMap<String, String[]> requestDynamics = new HashMap<String, String[]>();
	
	private void connectRTDB() {
		
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
				
				logger.log(Level.SEVERE, "setReadResult Begin");
				
		    	logger.log(Level.SEVERE, "setReadResult Begin");
		    	logger.log(Level.SEVERE, "setReadResult key["+key+"]");
		    	logger.log(Level.SEVERE, "setReadResult errorCode["+errorCode+"]");
		    	logger.log(Level.SEVERE, "setReadResult errorMessage["+errorMessage+"]");
		    	
		    	KeyAndValues.put(key, value);
		    	
		    	updateValue(key, value);
		    	
				for(int i = 0; i < value.length; ++i) {
					logger.log(Level.SEVERE, "setReadResult value["+i+"]["+value[i]+"]");
				}
				
				logger.log(Level.SEVERE, "setReadResult End");
				
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
				
				logger.log(Level.SEVERE, "setGetChildrenResult Begin");
				
		    	logger.log(Level.SEVERE, "setGetChildrenResult Begin");
		    	logger.log(Level.SEVERE, "setGetChildrenResult clientKey["+clientKey+"]");
		    	logger.log(Level.SEVERE, "setGetChildrenResult errorCode["+errorCode+"]");
		    	logger.log(Level.SEVERE, "setGetChildrenResult errorMessage["+errorMessage+"]");

				KeyAndValues.put(clientKey, instances);
				
				updateValue(clientKey, instances);
				
				for(int i = 0; i < instances.length; ++i) {
					logger.log(Level.SEVERE, "setGetChildrenResult instances["+i+"]["+instances[i]+"]");
				}
				
				logger.log(Level.SEVERE, "setGetChildrenResult End");
				
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
	
	private void connectInspectorMain() {
		{
			logger.log(Level.SEVERE, "GetChildren Begin");

			String clientKey = "GetChildren" + "inspector" + "static" + parent;

			requestStatics.add(new JSONRequest("GetChildren", clientKey, scsEnvId, parent));
			
			KeyAndAddress.put(clientKey, new String[]{parent});
			
			logger.log(Level.SEVERE, "GetChildren End");
		}

		{
			logger.log(Level.SEVERE, "multiReadValue Begin");
			
			String clientKey = "multiReadValue" + "inspector" + "static" + parent;
			
			String[] parents = new String[]{parent};
			
			String[] dbaddresses = new String[parents.length*staticAttibutes.length];
			int r=0;
			for(int x=0;x<parents.length;++x) {
				for(int y=0;y<staticAttibutes.length;++y) {
					dbaddresses[r++]=parents[x]+staticAttibutes[y];
				}
			}
			logger.log(Level.SEVERE, "multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
			
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.log(Level.SEVERE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
			}

			requestStatics.add(new JSONRequest("multiReadValue", clientKey, scsEnvId, dbaddresses));

			KeyAndAddress.put(clientKey, dbaddresses);
			
			logger.log(Level.SEVERE, "multiReadValue End");
		}
		
		{
			
			logger.log(Level.SEVERE, "multiReadValue Begin");
			
			String clientKey = "multiReadValue" + "inspector" + "dynamic" + parent;

			String[] parents = new String[]{parent};
			
			String[] dbaddresses = new String[parents.length*dynamicAttibutes.length];
			int r=0;
			for(int x=0;x<parents.length;++x) {
				for(int y=0;y<dynamicAttibutes.length;++y) {
					dbaddresses[r++]=parents[x]+dynamicAttibutes[y];
				}
			}

			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.log(Level.SEVERE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
			}
			
			requestStatics.add(new JSONRequest("multiReadValue", clientKey, scsEnvId, dbaddresses));

			KeyAndAddress.put(clientKey, dbaddresses);
			
			logger.log(Level.SEVERE, "multiReadValue End");
		}
	
	}
		
	private void connectInspectorInfo() {
		String[] parents = new String[]{parent+":dciLocked"};
		
		if ( KeyAndValues.containsKey("GetChildren" + "inspector" + "static" + parent) ) {
			parents = KeyAndValues.get("GetChildren" + "inspector" + "static" + parent);
		}
		
		for(int i=0;i<parents.length;i++) {
			logger.log(Level.SEVERE, "multiReadValue parents("+i+")["+parents[i]+"]");
		}
		
		{
			logger.log(Level.SEVERE, "multiReadValue Begin");
			
			String clientKey = "multiReadValue" + "inspectorinfo" + "static" + parent;
			
			String strLabel				= ".label";
			String strValueTable		= ":dal.valueTable";
			String strHmiOrder			= ".hmiOrder";
			
			String strValue				= ".value";
			String strValidity			= ".validity"; // 0=invalid, 1=valid
			String strValueAlarmVector	= ":dal.valueAlarmVector"; // (0,1)==0 = normal, (0,1)==1 = alarm 
			String strForcedStatus		= ":dfo.forcedStatus"; // 2=MO, AI=8, 512=SS //dfo.forcedStatus
			
			String staticAttibutes [] = new String[] {strLabel, strValueTable, strHmiOrder};
			String dynamicAttibutes [] = new String[] {strValue, strValidity, strValueAlarmVector, strForcedStatus};
			
//			String clientKey = "inspectorinfo" + "multiReadValue" + "static" + "2" + Random.nextInt();
			
			String[] dbaddresses = new String[parents.length*staticAttibutes.length];
			int r=0;
			for(int x=0;x<parents.length;++x) {
				for(int y=0;y<staticAttibutes.length;++y) {
					dbaddresses[r++]=parents[x]+staticAttibutes[y];
				}
			}
			
			logger.log(Level.SEVERE, "multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
			
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.log(Level.SEVERE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
			}

			requestStatics.add(new JSONRequest("multiReadValue", clientKey, scsEnvId, dbaddresses));

			KeyAndAddress.put(clientKey, dbaddresses);
			
			logger.log(Level.SEVERE, "multiReadValue End");
		}
		
		{
			
			logger.log(Level.SEVERE, "multiReadValue Begin");
			
			String clientKey = "multiReadValue" + "inspectorinfo" + "dynamic" + parent;
			
			String strLabel				= ".label";
			String strValueTable		= ":dal.valueTable";
			String strHmiOrder			= ".hmiOrder";
			
			String strValue				= ".value";
			String strValidity			= ".validity"; // 0=invalid, 1=valid
			String strValueAlarmVector	= ":dal.valueAlarmVector"; // (0,1)==0 = normal, (0,1)==1 = alarm 
			String strForcedStatus		= ":dfo.forcedStatus"; // 2=MO, AI=8, 512=SS //dfo.forcedStatus

			String staticAttibutes [] = new String[] {strLabel, strValueTable, strHmiOrder};
			String dynamicAttibutes [] = new String[] {strValue, strValidity, strValueAlarmVector, strForcedStatus};
			
			String[] dbaddresses = new String[parents.length*dynamicAttibutes.length];
			int r=0;
			for(int x=0;x<parents.length;++x) {
				for(int y=0;y<dynamicAttibutes.length;++y) {
					dbaddresses[r++]=parents[x]+dynamicAttibutes[y];
				}
			}
			
			logger.log(Level.SEVERE, "multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
			
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.log(Level.SEVERE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
			}
			
			requestDynamics.put(clientKey, dbaddresses);

			KeyAndAddress.put(clientKey, dbaddresses);
		
			logger.log(Level.SEVERE, "multiReadValue End");
		}
	}
	
	private Timer timer = null;
	private void connectTimer() {
		
		if ( null == timer ) {
			timer = new Timer() {

				@Override
				public void run() {
					if ( null != rtdb ) {
					
						if ( requestStatics.size() > 0 ) {
							
							logger.log(Level.SEVERE, "sendJSONRequest Begin");
							
							JSONRequest jsonRequest = requestStatics.removeFirst();
							
							if ( 0 == "GetChildren".compareTo(jsonRequest.api) ) {
								
								String api = jsonRequest.api;
								String clientKey = jsonRequest.clientKey;
								String scsEnvId = jsonRequest.scsEnvId;
								String dbaddress = jsonRequest.dbaddress;
								
								logger.log(Level.SEVERE, "api["+api+"] key["+clientKey+"] scsEnvId["+scsEnvId+"]");
						    	
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
								
								logger.log(Level.SEVERE, "api["+api+"] key["+clientKey+"] scsEnvId["+scsEnvId+"]");
								
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

							logger.log(Level.SEVERE, "sendJSONRequest End");

							
						} else if ( requestDynamics.size() > 0 ) {
							
							LinkedList<String> dbaddresslist = new LinkedList<String>();
							for ( String key : requestDynamics.keySet() ) {
								for ( String dbaddress : requestDynamics.get(key) ) {
									dbaddresslist.add(dbaddress);
								}
							}
							String[] dbaddresses = dbaddresslist.toArray(new String[0]);
							
							String clientKey = "multiReadValue" + "inspector" + "dynamic" + parent;
							
							KeyAndAddress.put(clientKey, dbaddresses);
									
							String api = "multiReadValue";
		
							logger.log(Level.SEVERE, "api["+api+"] key["+clientKey+"] scsEnvId["+scsEnvId+"]");
									
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
			
			timer.scheduleRepeating(250);
		}

	}


	@Override
	public void connect() {
		
		logger.log(Level.FINE, "connect Begin");
				
		connectRTDB();
		connectInspectorMain();
		connectTimer();

		logger.log(Level.FINE, "connect End");
	}
	
	@Override
	public void buildWidgets() {
//		buildWidgets(this.addresses.length);
	}
	
	public void buildWidgets(int numOfWidgets) {
	
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.FINE, "removeConnection Begin");
		
		requestStatics.clear();
		requestDynamics.clear();
		
		if ( null != timer ) timer.cancel();
		
		logger.log(Level.FINE, "removeConnection End");
	}

	private LinkedList<String> infos	= new LinkedList<String>();
	private LinkedList<String> controls	= new LinkedList<String>();
	private LinkedList<String> tags		= new LinkedList<String>();
	private LinkedList<String> advances	= new LinkedList<String>();
	@Override
	public void buildTabsAddress(String[] instances) {
		
		logger.log(Level.FINE, "buildTabsAddress Begin");

		String[] infoPrefix				= new String[] {"dci", "aci", "sci"};
		String[] controlPrefix			= new String[] {"dio", "aio", "sio"};
		String[] tagsEnding				= new String[] {"PTW", "LR"};
		
		logger.log(Level.FINE, "buildTabsAddress Iterator Begin");
		
		for ( String dbaddress : instances ) {
			logger.log(Level.FINE, "buildTabsAddress Iterator dbaddress["+dbaddress+"]");
			
			if ( null != dbaddress ) {
				String dbaddressTokenes[] = dbaddress.split(":");
				String point = dbaddressTokenes[dbaddressTokenes.length-1];
				
				if ( null != point ) {
					for ( String s : controlPrefix ) {
						if ( point.startsWith(s) ) {
							controls.add(dbaddress);
							continue;
						}
					}
					for ( String s : tagsEnding ) {
						if ( point.endsWith(s) ) {
							tags.add(dbaddress);
							continue;
						}
					}
					for ( String s : infoPrefix ) {
						if ( point.startsWith(s) ) {
							infos.add(dbaddress);
							advances.add(dbaddress);
						}
					}
				} else {
					logger.log(Level.FINE, "buildTabsAddress Iterator point IS NULL");
				}
			} else {
				logger.log(Level.FINE, "buildTabsAddress Iterator dbaddress IS NULL");
			}
		}

		logger.log(Level.FINE, "buildTabsAddress End");
	}
	
	@Override
	public void makeTabsSetAddress() {
		
		logger.log(Level.FINE, "makeTabsSetAddress Begin");
		
		for ( String dbaddress : infos )	{ logger.log(Level.FINE, "makeTabsSetAddress infos dbaddress["+dbaddress+"]"); }
		for ( String dbaddress : controls )	{ logger.log(Level.FINE, "makeTabsSetAddress controls dbaddress["+dbaddress+"]"); }
		for ( String dbaddress : tags )		{ logger.log(Level.FINE, "makeTabsSetAddress tags dbaddress["+dbaddress+"]"); }
		for ( String dbaddress : advances )	{ logger.log(Level.FINE, "makeTabsSetAddress advances dbaddress["+dbaddress+"]"); }
		
		uiInspectorInfo.setParent(parent);
		uiInspectorControl.setParent(parent);
		uiInspectorTag.setParent(parent);
		uiInspectorAdvance.setParent(parent);
		
		uiInspectorInfo.setAddresses	(scsEnvId, infos.toArray(new String[0]));
		uiInspectorControl.setAddresses	(scsEnvId, controls.toArray(new String[0]));
		uiInspectorTag.setAddresses		(scsEnvId, tags.toArray(new String[0]));
		uiInspectorAdvance.setAddresses	(scsEnvId, advances.toArray(new String[0]));
		
		logger.log(Level.FINE, "makeTabsSetAddress End");
	}
	
	@Override
	public void makeTabsBuildWidgets() {
		logger.log(Level.FINE, "makeTabsBuildWidgets Begin");
		
		for ( UIInspectorTab_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.buildWidgets();
			} else {
				logger.log(Level.FINE, "makeTabsBuildWidgets uiPanelInspector_i IS NULL");
			}
		}

		logger.log(Level.FINE, "makeTabsBuildWidgets End");
	}
	
	@Override
	public void makeTabsConnect() {
		logger.log(Level.FINE, "makeTabsConnect Begin");
		
		for ( UIInspectorTab_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.connect();
			} else {
				logger.log(Level.FINE, "makeTabsConnect uiPanelInspector_i IS NULL");
			}
		}

		logger.log(Level.FINE, "makeTabsConnect End");
	}

	@Override
	public void makeTabsDisconnect() {
		logger.log(Level.FINE, "tagsDisconnect Begin");
		
		for ( UIInspectorTab_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.disconnect();
			} else {
				logger.log(Level.FINE, "tagsDisconnect uiPanelInspector_i IS NULL");
			}
		}

		logger.log(Level.FINE, "tagsDisconnect End");
	}
	
	private UIInspectorTab_i uiInspectorInfo	= null;
	private UIInspectorTab_i uiInspectorControl	= null;
	private UIInspectorTab_i uiInspectorTag		= null;
	private UIInspectorTab_i uiInspectorAdvance	= null;
	
	private LinkedList<UIInspectorTab_i> uiInspectorTabs = null;
	
	int baseBoderX = 28, baseBoderY = 28;
	int baseWidth = 400, baseHeight = 620;
	private VerticalPanel basePanel = null;
	private TextBox txtAttributeStatus[] = null;
	
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		String strHeadersLabel [] = new String[] { "Control Right","Control Right Reserved","Handover Right" };
		String strHeadersStatus [] = new String[] { "Yes / No","Not Reserved / Not", "Central / Station" };
		
		FlexTable flexTableHeader = new FlexTable();
		flexTableHeader.addStyleName("project-gwt-flextable-header");
		txtAttributeStatus = new TextBox[strHeadersStatus.length];
		for ( int i = 0 ; i < strHeadersLabel.length ; i++ ) {
			InlineLabel inlineLabel = new InlineLabel(strHeadersLabel[i]);
			inlineLabel.getElement().getStyle().setPadding(10, Unit.PX);	
			inlineLabel.addStyleName("project-gwt-inlinelabel-headerlabel");
			flexTableHeader.setWidget(i, 0, inlineLabel);
			txtAttributeStatus[i] = new TextBox();
			txtAttributeStatus[i].setText(strHeadersStatus[i]);
			txtAttributeStatus[i].setMaxLength(16);
			txtAttributeStatus[i].setReadOnly(true);
			txtAttributeStatus[i].addStyleName("project-gwt-textbox-headervalue");
			flexTableHeader.setWidget(i, 2, txtAttributeStatus[i]);
		}
	
		uiInspectorTabs 	= new LinkedList<UIInspectorTab_i>();
		
		uiInspectorInfo		= new UIInspectorInfo();
		uiInspectorControl	= new UIInspectorControl();
		uiInspectorTag		= new UIInspectorTag();
		uiInspectorAdvance	= new UIInspectorAdvance();

		uiInspectorTabs.add(uiInspectorInfo);
		uiInspectorTabs.add(uiInspectorControl);
		uiInspectorTabs.add(uiInspectorTag);
		uiInspectorTabs.add(uiInspectorAdvance);
		
		ComplexPanel panelInfo		= uiInspectorInfo.getMainPanel(this.uiNameCard);
		ComplexPanel panelCtrl		= uiInspectorControl.getMainPanel(this.uiNameCard);
		ComplexPanel panelTag		= uiInspectorTag.getMainPanel(this.uiNameCard);
		ComplexPanel panelAdv		= uiInspectorAdvance.getMainPanel(this.uiNameCard);
		
		TabPanel tabPanel = new TabPanel();
		tabPanel.getElement().getStyle().setWidth(400, Unit.PX);
		tabPanel.getElement().getStyle().setFontSize(16, Unit.PX);
		
		tabPanel.add(panelInfo, strTabNames[0]);
		tabPanel.add(panelCtrl, strTabNames[1]);
		tabPanel.add(panelTag, strTabNames[2]);
		tabPanel.add(panelAdv, strTabNames[3]);
		tabPanel.selectTab(0);
		
		Button btnClose = new Button("Close");
		btnClose.addStyleName("project-gwt-button-inspector-bottom-close");
		
		btnClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				disconnect();
//				
//				makeTabsDisconnect();
				
				hide();
			}
	    });
		
		TextBox txtMsg = new TextBox();
		txtMsg.setReadOnly(true);
		txtMsg.addStyleName("project-gwt-textbox-inspector-bottom-message");
		
		HorizontalPanel bottomBar = new HorizontalPanel();
//		bottomBar.setWidth("100%");
		bottomBar.addStyleName("project-gwt-panel-inspector-bottom");
		bottomBar.getElement().getStyle().setPadding(10, Unit.PX);	
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.add(txtMsg);
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(btnClose);
		
		basePanel = new VerticalPanel();
		basePanel.add(flexTableHeader);
		basePanel.add(tabPanel);
		basePanel.add(bottomBar);
		basePanel.getElement().getStyle().setWidth(baseWidth, Unit.PX);
		basePanel.getElement().getStyle().setHeight(baseHeight, Unit.PX);
		basePanel.addStyleName("project-gwt-panel-inspector");
		this.add(basePanel);
		
		this.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
		int left = (Window.getClientWidth() / 2) - ( baseWidth / 2 ) - (baseBoderX / 2);
        int top = (Window.getClientHeight() / 2) - ( baseHeight / 2 ) - (baseBoderY / 2);

        this.setPopupPosition(left, top);
    
        logger.log(Level.FINE, "getMainPanel End");
        
        return basePanel;
	}
	
	@Override
	public void onMouseMove(Widget sender, int x, int y) {
		super.onMouseMove(sender, x, y);
		if ( basePanel != null ) {
			boolean XtoMoveInvalid = false, YtoMoveInvalid = false;
			int XtoMove = 0, YtoMove = 0;
			if ( this.getPopupLeft() < 0 ) {
				XtoMove = 0;
				XtoMoveInvalid = true;
			}
			if ( this.getPopupLeft() + baseWidth + baseBoderX > Window.getClientWidth() ) {
				XtoMove = Window.getClientWidth() - baseWidth - baseBoderX;
				XtoMoveInvalid = true;
			}
			if ( this.getPopupTop() < 0 ) {
				YtoMove = 0;
				YtoMoveInvalid = true;
			}
			if ( this.getPopupTop() + baseHeight + baseBoderY > Window.getClientHeight() ) {
				YtoMove = Window.getClientHeight() - baseHeight - baseBoderY;
				YtoMoveInvalid = true;
			}
			if ( XtoMoveInvalid || YtoMoveInvalid ) {
				if ( ! XtoMoveInvalid ) XtoMove = this.getPopupLeft();
				if ( ! YtoMoveInvalid ) YtoMove = this.getPopupTop();
				this.setPopupPosition(XtoMove, YtoMove);
			}
		}
	}

}
