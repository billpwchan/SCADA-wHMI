package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.ArrayList;
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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.DCP_i.TaggingStatus;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Observer;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Subject;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

public class UIPanelInspector extends DialogBox implements UIInspectorTab_i, UIInspector_i, IClientLifeCycle {
	
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
//	private String[] addresses	= null;
	private int periodMillis	= 500;
	
	public void setPeriodMillis(int periodMillis) {
		this.periodMillis = periodMillis;
	}

	@Override
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	@Override
	public void setAddresses(String scsEnvId, String[] addresses, String period) {
		logger.log(Level.FINE, "setAddresses Begin");
		
		this.scsEnvId = scsEnvId;
		
		logger.log(Level.FINE, "setConnection this.scsEnvId["+this.scsEnvId+"]");
		
//		this.addresses = addresses;
		
//		RTDB_Helper.addressesIsValid(this.addresses);
		
		this.periodMillis = Integer.parseInt(period);
		
		logger.log(Level.FINE, "setAddresses End");
	}
	
	
	public HashMap<String, String> getHashMapStringString(String [] values1, String [] values2 ) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		for ( int i = 0 ; i < values1.length ; ++i ) {
			String value1 = values1[i];
			String value2 = values2[i];
			hashMap.put(value1, value2);
		}
		return hashMap;
	}
	private HashMap<String, String> dynamicvalues = new HashMap<String, String>();
	void updateValue(String clientKey, String [] values) {
		{
			String clientKey_GetChildren_inspector_static = "GetChildren" + "_" + "inspector" + "_" + "static" + "_" + parent;
			if ( 0 == clientKey_GetChildren_inspector_static.compareTo(clientKey) ) {
				buildTabsAddress(values);
				makeTabsSetAddress();
				makeTabsBuildWidgets();
				makeTabsConnect();
				
				connectInspectorInfo();
				connectInspectorControl();
				connectInspectorTag();
				
				if ( infos.size() <= 0 )		tabPanel.remove(panelInfo);
				if ( controls.size() <= 0 )		tabPanel.remove(panelCtrl);
				if ( tags.size() <= 0 )			tabPanel.remove(panelTag);
				if ( advances.size() <= 0 )		tabPanel.remove(panelAdv);
	
			}			
		}
		

		{
			String clientKey_multiReadValue_inspector_static = "multiReadValue" + "_" + "inspector" + "_" + "static" + "_" + parent;
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
		}

		{
			String clientKey_multiReadValue_inspectorinfo_static = "multiReadValue" + "_" + "inspectorinfo" + "_" + "static" + "_" + parent;
			if ( 0 == clientKey_multiReadValue_inspectorinfo_static.compareTo(clientKey) ) {
				String [] dbaddresses = KeyAndAddress.get(clientKey);
				String [] dbvalues = KeyAndValues.get(clientKey);
				HashMap<String, String> keyAndValue = new HashMap<String, String>();
				for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
					keyAndValue.put(dbaddresses[i], dbvalues[i]);
				}
				((UIInspectorInfo)		uiInspectorInfo)		.updateValue(clientKey, keyAndValue);
				((UIInspectorAdvance)	uiInspectorAdvance)		.updateValue(clientKey, keyAndValue);
			}			
		}
		
		{
			String clientKey_multiReadValue_inspectorcontrol_static = "multiReadValue" + "_" + "inspectorcontrol" + "_" + "static" + "_" + parent;
			if ( 0 == clientKey_multiReadValue_inspectorcontrol_static.compareTo(clientKey) ) {
				String [] dbaddresses = KeyAndAddress.get(clientKey);
				String [] dbvalues = KeyAndValues.get(clientKey);
				HashMap<String, String> keyAndValue = new HashMap<String, String>();
				for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
					keyAndValue.put(dbaddresses[i], dbvalues[i]);
				}
				((UIInspectorControl)	uiInspectorControl)		.updateValue(clientKey, keyAndValue);
			}			
		}
		
		{
			String clientKey_multiReadValue_inspectortag_static = "multiReadValue" + "_" + "inspectortag" + "_" + "static" + "_" + parent;
			if ( 0 == clientKey_multiReadValue_inspectortag_static.compareTo(clientKey) ) {
				String [] dbaddresses = KeyAndAddress.get(clientKey);
				String [] dbvalues = KeyAndValues.get(clientKey);
				HashMap<String, String> keyAndValue = new HashMap<String, String>();
				for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
					keyAndValue.put(dbaddresses[i], dbvalues[i]);
				}
				((UIInspectorTag)		uiInspectorTag)			.updateValue(clientKey, keyAndValue);
			}			
		}
		
		{
			String clientKey_multiReadValue_inspector_dynamic = "multiReadValue" + "_" + "inspector" + "_" + "dynamic" + "_" + parent;
			if ( 0 == clientKey_multiReadValue_inspector_dynamic.compareTo(clientKey) ) {
				String [] dbaddresses = KeyAndAddress.get(clientKey);
				String [] dbvalues = KeyAndValues.get(clientKey);
				for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
					dynamicvalues.put(dbaddresses[i], dbvalues[i]);
				}
				
				String key = parent + strIsControlable;
				if ( dynamicvalues.containsKey(key) ) {
					String value = dynamicvalues.get(key);
					value = RTDB_Helper.removeDBStringWrapper(value);
					if ( null != value ) txtAttributeStatus[0].setText((0==value.compareTo("0")?"No":"Yes"));
				}
				
				((UIInspectorInfo)		uiInspectorInfo)		.updateValue(clientKey, dynamicvalues);
				((UIInspectorControl)	uiInspectorControl)		.updateValue(clientKey, dynamicvalues);
				((UIInspectorTag)		uiInspectorTag)			.updateValue(clientKey, dynamicvalues);
				((UIInspectorAdvance)	uiInspectorAdvance)		.updateValue(clientKey, dynamicvalues);
			}			
		}

	}
	
//	private boolean directly = false;
	
	private HashMap<String, String[]> KeyAndAddress = new HashMap<String, String[]>();
	private HashMap<String, String[]> KeyAndValues = new HashMap<String, String[]>();
	
	private ScsRTDBComponentAccess rtdb = null;
	
	private LinkedList<JSONRequest> requestStatics = new LinkedList<JSONRequest>();
	private HashMap<String, String[]> requestDynamics = new HashMap<String, String[]>();
	
	private void connectScsRTDBComponentAccess() {
		
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
		    	
		    	updateValue(key, value);

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
				
				updateValue(clientKey, instances);
				
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
	
	private void connectInspectorMain() {
		{
			logger.log(Level.FINE, "GetChildren Begin");

			String clientKey = "GetChildren" + "_" + "inspector" + "_" + "static" + "_" + parent;

			requestStatics.add(new JSONRequest("GetChildren", clientKey, scsEnvId, parent));
			
			KeyAndAddress.put(clientKey, new String[]{parent});
			
			logger.log(Level.FINE, "GetChildren End");
		}

		{
			logger.log(Level.FINE, "multiReadValue Begin");
			
			String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "static" + "_" + parent;
			
			String[] parents = new String[]{parent};
			
//			String[] dbaddresses = new String[parents.length*staticAttibutes.length];
//			int r=0;
//			for(int x=0;x<parents.length;++x) {
//				for(int y=0;y<staticAttibutes.length;++y) {
//					dbaddresses[r++]=parents[x]+staticAttibutes[y];
//				}
//			}
			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for(int x=0;x<parents.length;++x) {
					if ( ! parents[x].endsWith("HMI") ) {
						for(int y=0;y<staticAttibutes.length;++y) {
							dbaddressesArrayList.add(parents[x]+staticAttibutes[y]);
						}
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}
			
			
			logger.log(Level.FINE, "multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
			
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.log(Level.FINE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
			}

			requestStatics.add(new JSONRequest("multiReadValue", clientKey, scsEnvId, dbaddresses));

			KeyAndAddress.put(clientKey, dbaddresses);
			
			logger.log(Level.FINE, "multiReadValue End");
		}
		
		{
			
			logger.log(Level.FINE, "multiReadValue Begin");
			
			String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "dynamic" + "_" + parent;

			String[] parents = new String[]{parent};
			
//			String[] dbaddresses = new String[parents.length*dynamicAttibutes.length];
//			int r=0;
//			for(int x=0;x<parents.length;++x) {
//				for(int y=0;y<dynamicAttibutes.length;++y) {
//					dbaddresses[r++]=parents[x]+dynamicAttibutes[y];
//				}
//			}
			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for(int x=0;x<parents.length;++x) {
					if ( ! parents[x].endsWith("HMI") ) {
						for(int y=0;y<dynamicAttibutes.length;++y) {
							dbaddressesArrayList.add(parents[x]+dynamicAttibutes[y]);
						}
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}

			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.log(Level.FINE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
			}
			
			requestStatics.add(new JSONRequest("multiReadValue", clientKey, scsEnvId, dbaddresses));

			KeyAndAddress.put(clientKey, dbaddresses);
			
			logger.log(Level.FINE, "multiReadValue End");
		}
	
	}
		
	private void connectInspectorInfo() {
		
		// Static Attribute
		String strLabel				= ".label";
		String strValueTable		= ":dal.valueTable";
		String strHmiOrder			= ".hmiOrder";
		
		// Dynamic Attribute
		String strValue				= ".value";
		String strValidity			= ".validity"; // 0=invalid, 1=valid
		String strValueAlarmVector	= ":dal.valueAlarmVector"; // (0,1)==0 = normal, (0,1)==1 = alarm 
		String strForcedStatus		= ":dfo.forcedStatus"; // 2=MO, AI=8, 512=SS //dfo.forcedStatus

		// Static Attribute List
		String staticAttibutes [] = new String[] {strLabel, strValueTable, strHmiOrder};

		// Dynamic Attribute List
		String dynamicAttibutes [] = new String[] {strValue, strValidity, strValueAlarmVector, strForcedStatus};
		
		logger.log(Level.FINE, "connectInspectorInfo Begin");
//		String[] parents = new String[]{parent+":dciLocked"};
		String[] parents = new String[]{parent};
		{
			String clientKey = "GetChildren" + "_" + "inspector" + "_" + "static" + "_" + parent;
			if ( KeyAndValues.containsKey(clientKey) ) {
				parents = KeyAndValues.get(clientKey);
			}			
		}
		
//		for(int i=0;i<parents.length;i++) {
//			logger.log(Level.FINE, "connectInspectorInfo parents("+i+")["+parents[i]+"]");
//		}
		
		// Read static
		{
			logger.log(Level.FINE, "multiReadValue Begin");
			
			String clientKey = "multiReadValue" + "_" + "inspectorinfo" + "_" + "static" + "_" + parent;
			
//			String[] dbaddresses = new String[parents.length*staticAttibutes.length];
//			int r=0;
//			for(int x=0;x<parents.length;++x) {
//				for(int y=0;y<staticAttibutes.length;++y) {
//					dbaddresses[r++]=parents[x]+staticAttibutes[y];
//				}
//			}
			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for(int x=0;x<parents.length;++x) {
					if ( ! parents[x].endsWith("HMI") ) {
						for(int y=0;y<staticAttibutes.length;++y) {
							dbaddressesArrayList.add(parents[x]+staticAttibutes[y]);
						}
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}			
			
			logger.log(Level.FINE, "multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
			
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.log(Level.FINE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
			}

			requestStatics.add(new JSONRequest("multiReadValue", clientKey, scsEnvId, dbaddresses));

			KeyAndAddress.put(clientKey, dbaddresses);
			
			logger.log(Level.FINE, "multiReadValue End");
		}
		
		// Read dynamic
		{
			logger.log(Level.FINE, "multiReadValue Begin");
			
			String clientKey = "multiReadValue" + "_" + "inspectorinfo" + "_" + "dynamic" + "_" + parent;

//			String[] dbaddresses = new String[parents.length*dynamicAttibutes.length];
//			int r=0;
//			for(int x=0;x<parents.length;++x) {
//				for(int y=0;y<dynamicAttibutes.length;++y) {
//					dbaddresses[r++]=parents[x]+dynamicAttibutes[y];
//				}
//			}
			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for(int x=0;x<parents.length;++x) {
					if ( ! parents[x].endsWith("HMI") ) {
						for(int y=0;y<dynamicAttibutes.length;++y) {
							dbaddressesArrayList.add(parents[x]+dynamicAttibutes[y]);
						}
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}
			
			logger.log(Level.FINE, "multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
			
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.log(Level.FINE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
			}
			
			requestDynamics.put(clientKey, dbaddresses);

			KeyAndAddress.put(clientKey, dbaddresses);
		
			logger.log(Level.FINE, "multiReadValue End");
		}
		logger.log(Level.FINE, "connectInspectorInfo End");
	}
	
	
	
	private String dio = "dio";
	private String aio = "aio";
	private String sio = "sio";
		
	private LinkedList<String> controldios = new LinkedList<String>();
	private LinkedList<String> aios = new LinkedList<String>();
	private LinkedList<String> sios = new LinkedList<String>();	
	private void connectInspectorControl() {
		
		logger.log(Level.FINE, "connectInspectorControl Begin");
		
		((UIInspectorControl)uiInspectorControl).buildWidgetList();
		
		String strLabel			= ".label";
		String strValueTable	= ".valueTable";
		
		// Static Attribute List
		String staticAttibutes[]	= new String[] {strLabel};
		
		// Static Attribute List
		String dioStaticAttibutes[]	= new String[] {strLabel, strValueTable};

		// Dynamic Attribute List
		String dynamicAttibutes[]	= new String[] {};
		
		for ( int i = 0 ; i < controls.size() ; ++i ) {
			String dbaddress = controls.get(i);
			if ( null != dbaddress ) {
				String dbaddressTokenes[] = dbaddress.split(":");
				String point = dbaddressTokenes[dbaddressTokenes.length-1];
				if ( null != point ) {
					if ( point.startsWith(dio) ) {
						controldios.add(dbaddress);
						continue;
					}
					if ( point.endsWith(aio) ) {
						aios.add(dbaddress);
						continue;
					}
					if ( point.startsWith(sio) ) {
						sios.add(dbaddress);
						continue;
					}
				}
			} else {
				logger.log(Level.SEVERE, "connectInspectorControl dbaddress IS NULL");
			}
		}
		
//		for(int i = 0; i < controldios.size(); ++i ) {
//			logger.log(Level.FINE, "multiReadValue dios.get("+i+")["+controldios.get(i)+"]");
//		}
//		for(int i = 0; i < aios.size(); ++i ) {
//			logger.log(Level.FINE, "multiReadValue aios.get("+i+")["+aios.get(i)+"]");
//		}
//		for(int i = 0; i < sios.size(); ++i ) {
//			logger.log(Level.FINE, "multiReadValue sios.get("+i+")["+sios.get(i)+"]");
//		}
		
		{
			String clientKey = "multiReadValue" + "_" + "inspectorcontrol" + "_" + "static" + "_" + parent;
			
			LinkedList<String> iolist = new LinkedList<String>();
			for ( int i = 0 ; i < controldios.size() ; ++i ) {
				for ( String attribute : dioStaticAttibutes ) {
					String dbaddress = controldios.get(i) + attribute;
					iolist.add(dbaddress);
				}
			}
			for ( int i = 0 ; i < aios.size() ; ++i ) {
				for ( String attribute : staticAttibutes ) {
					String dbaddress = aios.get(i) + attribute;
					iolist.add(dbaddress);
				}
			}
			for ( int i = 0 ; i < sios.size() ; ++i ) {
				for ( String attribute : staticAttibutes ) {
					String dbaddress = sios.get(i) + attribute;
					iolist.add(dbaddress);
				}
			}
			
			String [] dbaddress = iolist.toArray(new String[0]);
			
			for(int i = 0; i < dbaddress.length; ++i ) {
				logger.log(Level.FINE, "multiReadValue dbaddress("+i+")["+dbaddress[i]+"]");
			}
			
			requestStatics.add(new JSONRequest("multiReadValue", clientKey, scsEnvId, dbaddress));
			
			KeyAndAddress.put(clientKey, dbaddress);			
		}
		
		{
			for ( int i = 0 ; i < controldios.size() ; ++i ) {
				String dbaddress = controldios.get(i);
				{
					logger.log(Level.FINE, "GetChildren Begin");
					
					logger.log(Level.FINE, "GetChildren dbaddress["+dbaddress+"]");
	
					String clientKey = "GetChildren" + "_" + "inspectorcontrol" + "_" + "static" + "_" + dbaddress;
	
					requestStatics.add(new JSONRequest("GetChildren", clientKey, scsEnvId, dbaddress));
					
					KeyAndAddress.put(clientKey, new String[]{dbaddress});
					
					logger.log(Level.FINE, "GetChildren End");
				}			
			}
		}
		
		logger.log(Level.FINE, "connectInspectorControl End");

	}
	
	private void connectInspectorTag() {
		
		logger.log(Level.FINE, "connectInspectorTag Begin");
		
		((UIInspectorTag)uiInspectorTag).buildWidgetList();
		
		String strLabel			= ".label";
		String strValueTable	= ".valueTable";
		
		// Static Attribute List
		String staticAttibutes[]	= new String[] {strLabel};
		
		// Static Attribute List
		String dioStaticAttibutes[]	= new String[] {strLabel, strValueTable};

		// Dynamic Attribute List
		String dynamicAttibutes[]	= new String[] {};
		
		for ( int i = 0 ; i < tags.size() ; ++i ) {
			String dbaddress = tags.get(i);
			if ( null != dbaddress ) {
				String dbaddressTokenes[] = dbaddress.split(":");
				String point = dbaddressTokenes[dbaddressTokenes.length-1];
				if ( null != point ) {
					if ( point.startsWith(dio) ) {
						controldios.add(dbaddress);
						continue;
					}
				}
			} else {
				logger.log(Level.FINE, "connectInspectorTag dbaddress IS NULL");
			}
		}
		
		for(int i = 0; i < controldios.size(); ++i ) {
			logger.log(Level.FINE, "multiReadValue dios.get("+i+")["+controldios.get(i)+"]");
		}
		
		{
			String clientKey = "multiReadValue" + "_" + "inspectortag" + "_" + "static" + "_" + parent;
			
			LinkedList<String> iolist = new LinkedList<String>();
			for ( int i = 0 ; i < controldios.size() ; ++i ) {
				for ( String attribute : dioStaticAttibutes ) {
					String dbaddress = controldios.get(i) + attribute;
					iolist.add(dbaddress);
				}
			}
			
			String [] dbaddress = iolist.toArray(new String[0]);
			
			for(int i = 0; i < dbaddress.length; ++i ) {
				logger.log(Level.FINE, "multiReadValue dbaddress("+i+")["+dbaddress[i]+"]");
			}
			
			requestStatics.add(new JSONRequest("multiReadValue", clientKey, scsEnvId, dbaddress));
			
			KeyAndAddress.put(clientKey, dbaddress);			
		}
		
		{
			for ( int i = 0 ; i < controldios.size() ; ++i ) {
				String dbaddress = controldios.get(i);
				{
					logger.log(Level.FINE, "GetChildren Begin");
					
					logger.log(Level.FINE, "GetChildren dbaddress["+dbaddress+"]");
	
					String clientKey = "GetChildren" + "_" + "inspectortag" + "_" + "static" + "_" + dbaddress;
	
					requestStatics.add(new JSONRequest("GetChildren", clientKey, scsEnvId, dbaddress));
					
					KeyAndAddress.put(clientKey, new String[]{dbaddress});
					
					logger.log(Level.FINE, "GetChildren End");
				}			
			}
		}
		
		logger.log(Level.FINE, "connectInspectorControl End");

	}
	
	private Timer timer = null;
	private void connectTimer(int periodMillis) {
		
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

	@Override
	public void connect() {
		
		logger.log(Level.FINE, "connect Begin");

		connectScsRTDBComponentAccess();
		connectInspectorMain();
		connectTimer(this.periodMillis);
		
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
		
		makeTabsDisconnect();
		
		requestStatics.clear();
		requestDynamics.clear();
		
		if ( null != timer ) timer.cancel();
		
		logger.log(Level.FINE, "removeConnection End");
	}

	private TextBox txtMsg = null;
	private LinkedList<String> infos	= new LinkedList<String>();
	private LinkedList<String> controls	= new LinkedList<String>();
	private LinkedList<String> tags		= new LinkedList<String>();
	private LinkedList<String> advances	= new LinkedList<String>();
	@Override
	public void buildTabsAddress(String[] instances) {
		
		logger.log(Level.FINE, "buildTabsAddress Begin");

		String[] infoPrefix				= new String[] {"dci", "aci", "sci"};
		String[] controlPrefix			= new String[] {"dio", "aio", "sio"};
		String[] tagsPrefix				= new String[] {"dio"};
		
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
					for ( String s : tagsPrefix ) {
						if ( point.startsWith(s) ) {
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
		
		uiInspectorInfo		.setParent(parent);
		uiInspectorControl	.setParent(parent);
		uiInspectorTag		.setParent(parent);
		uiInspectorAdvance	.setParent(parent);
		
		uiInspectorInfo.setAddresses	(scsEnvId, infos		.toArray(new String[0]), "0");
		uiInspectorControl.setAddresses	(scsEnvId, controls		.toArray(new String[0]), "0");
		uiInspectorTag.setAddresses		(scsEnvId, tags			.toArray(new String[0]), "0");
		uiInspectorAdvance.setAddresses	(scsEnvId, advances		.toArray(new String[0]), "0");
		
		logger.log(Level.FINE, "makeTabsSetAddress End");
	}
	
	@Override
	public void makeTabsBuildWidgets() {
		logger.log(Level.FINE, "makeTabsBuildWidgets Begin");
		
		for ( UIInspectorTab_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.buildWidgets();
			} else {
				logger.log(Level.SEVERE, "makeTabsBuildWidgets uiPanelInspector_i IS NULL");
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
				logger.log(Level.SEVERE, "makeTabsConnect uiPanelInspector_i IS NULL");
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
				logger.log(Level.SEVERE, "tagsDisconnect uiPanelInspector_i IS NULL");
			}
		}

		logger.log(Level.FINE, "tagsDisconnect End");
	}
	
	private UIInspectorTab_i uiInspectorInfo	= null;
	private UIInspectorTab_i uiInspectorControl	= null;
	private UIInspectorTab_i uiInspectorTag		= null;
	private UIInspectorTab_i uiInspectorAdvance	= null;
	
	private TabPanel tabPanel 			= null;
	
	private ComplexPanel panelInfo		= null;
	private ComplexPanel panelCtrl		= null;
	private ComplexPanel panelTag		= null;
	private ComplexPanel panelAdv		= null;
	
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
		
		uiInspectorControl.setMessageBoxEvent(new MessageBoxEvent() {
			
			@Override
			public void setMessage(String message) {
				if ( null != txtMsg ) {
					logger.log(Level.FINE, "getMainPanel setMessage message["+message+"]");
					txtMsg.setText(message);
				}
			}
			
			@Override
			public void addMessage(String message) {
				if ( null != txtMsg ) {
					logger.log(Level.FINE, "getMainPanel setMessage message["+message+"]");
					String text = txtMsg.getText();
					logger.log(Level.FINE, "getMainPanel setMessage text["+text+"]");
					if ( text.length() > 0 ) text += "\n";
					logger.log(Level.FINE, "getMainPanel setMessage text + message["+text + message+"]");
					txtMsg.setText(text + message);
				}
			}
		});
		
		uiInspectorTag.setMessageBoxEvent(new MessageBoxEvent() {
			
			@Override
			public void setMessage(String message) {
				if ( null != txtMsg ) {
					logger.log(Level.FINE, "getMainPanel setMessage message["+message+"]");
					txtMsg.setText(message);
				}
			}
			
			@Override
			public void addMessage(String message) {
				if ( null != txtMsg ) {
					logger.log(Level.FINE, "getMainPanel setMessage message["+message+"]");
					String text = txtMsg.getText();
					logger.log(Level.FINE, "getMainPanel setMessage text["+text+"]");
					if ( text.length() > 0 ) text += "\n";
					logger.log(Level.FINE, "getMainPanel setMessage text + message["+text + message+"]");
					txtMsg.setText(text + message);
				}
			}
		});
		
		uiInspectorAdvance.setMessageBoxEvent(new MessageBoxEvent() {
			
			@Override
			public void setMessage(String message) {
				if ( null != txtMsg ) {
					logger.log(Level.FINE, "getMainPanel setMessage message["+message+"]");
					txtMsg.setText(message);
				}
			}
			
			@Override
			public void addMessage(String message) {
				if ( null != txtMsg ) {
					logger.log(Level.FINE, "getMainPanel setMessage message["+message+"]");
					String text = txtMsg.getText();
					logger.log(Level.FINE, "getMainPanel setMessage text["+text+"]");
					if ( text.length() > 0 ) text += "\n";
					logger.log(Level.FINE, "getMainPanel setMessage text + message["+text + message+"]");
					txtMsg.setText(text + message);
				}
			}
		});
		
		panelInfo		= uiInspectorInfo		.getMainPanel(this.uiNameCard);
		panelCtrl		= uiInspectorControl	.getMainPanel(this.uiNameCard);
		panelTag		= uiInspectorTag		.getMainPanel(this.uiNameCard);
		panelAdv		= uiInspectorAdvance	.getMainPanel(this.uiNameCard);
		
		tabPanel = new TabPanel();
		tabPanel.getElement().getStyle().setWidth(400, Unit.PX);
		tabPanel.getElement().getStyle().setFontSize(16, Unit.PX);
		
		tabPanel.add(panelInfo	, strTabNames[0]);
		tabPanel.add(panelCtrl	, strTabNames[1]);
		tabPanel.add(panelTag	, strTabNames[2]);
		tabPanel.add(panelAdv	, strTabNames[3]);
		tabPanel.selectTab(0);
		
		Button btnClose = new Button("Close");
		btnClose.addStyleName("project-gwt-button-inspector-bottom-close");
		
		btnClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				disconnect();
				
				makeTabsDisconnect();
				
				hide();
			}
	    });
		
		txtMsg = new TextBox();
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
	
	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
		this.messageBoxEvent = messageBoxEvent;
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

	@Override
	public void terminate() {
		logger.log(Level.FINE, "terminate Begin");
		try {
			rtdb.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rtdb=null;
		logger.log(Level.FINE, "terminate End");
	}

	@Override
	public void position(int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
