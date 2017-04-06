package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.MessageBoxEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTabClickEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTags_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIPanelInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.sorting.EquipmentsSorting;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.sorting.EquipmentsSortingEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.DataBaseClientKey;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorAdvance;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorControl;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorInfo;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorTag;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.DataBaseClientKey_i.Stability;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.EquipmentReserve;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.EquipmentReserveEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

/**
 * @author syau
 *
 */
public class UIPanelInspector extends UIWidget_i implements UIInspector_i, UIInspectorTags_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelInspector.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	// Order
	private final String strTabNames [] 	= new String[] {"Info","Control","Tagging","Advance"};
	private final String strTabConfigNames [] = {"info", "control", "tag", "advance"};
	
	// Static Attribute List
	private final String staticAttibutes[]	= new String[] {PointName.label.toString()};
	
	// hmiOrder
	private boolean hmiOrderEnable = false; 
	private String hmiOrderAttribute = null;
	private int hmiOrderFilterThreshold = -1;

	private String scsEnvId		= null;
	private String parent		= null;
	private int periodMillis	= 250;
	
	final private String INSPECTOR = "inspector";
	
	public void setPeriodMillis(int periodMillis) {
		this.periodMillis = periodMillis;
	}
	
	private Database database = new Database();

	@Override
	public void setParent(String scsEnvId, String parent) {
		final String function = "setParent";
		
		logger.begin(className, function);

		this.scsEnvId = scsEnvId;
		this.parent = parent;
		
		logger.debug(className, function, "this.scsEnvId[{}] this.parent[{}]", this.scsEnvId, this.parent);
		
		database.setDynamic(scsEnvId, parent);
		
		logger.end(className, function);
	}
	
	private String location = null;
	private String function = null;
	public void setFunctionLocation(String function2, String location2) {
		final String function = "setFunctionLocation";
		
		this.function = function2;
		this.location = location2;
		
		logger.debug(className, function, "this.function[{}] this.location[{}]", this.function, this.location);
	}
	
	private void connectTabs(String[] dbaddress) {
		final String function = "connectTabs";
		logger.begin(className, function);
		
		buildTabsAddress(dbaddress);
		makeTabsSetAddress();
		makeTabsBuildWidgets();
		makeTabsConnect();
		
		((UIInspectorHeader)			uiInspectorHeader)	.connect();
		((UIInspectorEquipmentReserve)	equipmentReserve)	.connect();
		
		if ( infos.size() <= 0 )		panelTab.remove(panelInfo);
		if ( controls.size() <= 0 )		panelTab.remove(panelCtrl);
		if ( tags.size() <= 0 )			panelTab.remove(panelTag);
		if ( advances.size() <= 0 )		panelTab.remove(panelAdv);
		
		logger.end(className, function);
	}

	private void responseGetChilden(String key, String[] values) {
		final String function = "responseGetChilden";
		logger.begin(className, function);
		
		if ( hmiOrderEnable ) {
			
			EquipmentsSorting equipmentSorting = new EquipmentsSorting();
			equipmentSorting.setDatabase(database);
			equipmentSorting.setParent(scsEnvId, parent);
			equipmentSorting.setDBAddresses(values, hmiOrderAttribute);
			equipmentSorting.setThreshold(hmiOrderFilterThreshold);
			equipmentSorting.setEquipmentsSortingEvent(new EquipmentsSortingEvent() {
				
				@Override
				public void onSorted(String[] dbaddress) {
					connectTabs(dbaddress);

				}
			});
			equipmentSorting.init();
		} else {
			
			connectTabs(values);
		}

		logger.end(className, function);
	}
	
	private void requestDynamic() {
		final String function = "requestDynamic";
		logger.begin(className, function);
		
		if ( null != database ) {
			database.setDynamicEvent(new DatabaseEvent() {
				
				@Override
				public void update(String key, String[] value) {
					final String function2 = "DatabaseEvent update";
					
					DataBaseClientKey clientKey = new DataBaseClientKey();
					clientKey.setAPI(API.multiReadValue);
					clientKey.setWidget(INSPECTOR);
					clientKey.setStability(Stability.DYNAMIC);
					clientKey.setAdress(parent);
					
					String strClientKey = clientKey.toClientKey();
					
					String [] dbaddresses	= database.getKeyAndAddress(strClientKey);
					String [] dbvalues		= database.getKeyAndValues(strClientKey);
					if (dbaddresses == null || dbvalues == null) {
						logger.error(className, function, "{} dbaddresses or dbvalues is null", function2);
						return;
					}
					if (logger.isDebugEnabled()) {
						for (int i=0; i<dbaddresses.length; i++) {
							logger.debug(className, function, "{} dbaddresses[{}]=[{}]", new Object[]{function2, i, dbaddresses[i]});
						}
						for (int i=0; i<dbvalues.length; i++) {
							logger.debug(className, function, "{} dbvalues[{}]=[{}]", new Object[]{function2, i, dbvalues[i]});
						}
						for (int i=0; i<value.length; i++) {
							logger.debug(className, function, "{} value[{}]=[{}]", new Object[]{function2, i, value[i]});
						}
					}
					
					HashMap<String, String> dynamicvalues = new HashMap<String, String>();
					for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
						dynamicvalues.put(dbaddresses[i], dbvalues[i]);
					}
					
					((UIInspectorHeader)			uiInspectorHeader)		.updateValue(key, dynamicvalues);
					((UIInspectorEquipmentReserve)	equipmentReserve)		.updateValue(key, dynamicvalues);
					
					((UIInspectorInfo)				uiInspectorInfo)		.updateValue(key, dynamicvalues);
					((UIInspectorControl)			uiInspectorControl)		.updateValue(key, dynamicvalues);
					((UIInspectorTag)				uiInspectorTag)			.updateValue(key, dynamicvalues);
					((UIInspectorAdvance)			uiInspectorAdvance)		.updateValue(key, dynamicvalues);
					
				}
			});
		} else {
			logger.warn(className, function, "database IS NUL");
		}
		logger.end(className, function);
	}
	
	private void requestGetChilden() {
		final String function = "requestGetChilden";
		logger.begin(className, function);
		
		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.GetChildren);
		clientKey.setWidget(INSPECTOR);
		clientKey.setStability(Stability.STATIC);
		clientKey.setAdress(parent);
		
		String strClientKey = clientKey.toClientKey();
		
		String strApi = clientKey.getApi().toString();

		String dbaddress = parent;
		
		if ( null != database ) {
			database.addStaticRequest(strApi, strClientKey, scsEnvId, dbaddress, new DatabaseEvent() {
				
				@Override
				public void update(String key, String[] values) {
					
					DataBaseClientKey clientKey = new DataBaseClientKey();
					clientKey.setAPI(API.GetChildren);
					clientKey.setWidget(INSPECTOR);
					clientKey.setStability(Stability.STATIC);
					clientKey.setAdress(parent);
					
					String strClientKey = clientKey.toString();

					if ( strClientKey.equalsIgnoreCase(key) ) {
						
						responseGetChilden(key, values);
						
					}
					
					requestDynamic();
	
				}
			});
		} else {
			logger.warn(className, function, "database IS NUL");
		}

		logger.end(className, function);
	}
	
	private void repsonseHeader(String key, String[] values) {
		final String function = "repsonseHeader";
		logger.begin(className, function);
		
		String [] dbaddresses	= database.getKeyAndAddress(key);
		String [] dbvalues		= database.getKeyAndValues(key);
		for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
			String dbaddress = dbaddresses[i];

			// Equipment Label
			if ( dbaddress.endsWith(PointName.label.toString()) ) {
				String value = dbvalues[i];
				value = DatabaseHelper.removeDBStringWrapper(value);
				if ( null != value) setText(value);
				break;
			}
		}

		logger.end(className, function);
	}
	
	private void requestHeader() {
		final String function = "requestHeader";
		logger.begin(className, function);
		
		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(INSPECTOR);
		clientKey.setStability(Stability.STATIC);
		clientKey.setAdress(parent);
		
		String strClientKey = clientKey.toClientKey();

		String[] dbaddresses = null;
		{
			ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
			for(int y=0;y<staticAttibutes.length;++y) {
				dbaddressesArrayList.add(parent+staticAttibutes[y]);
			}
			dbaddresses = dbaddressesArrayList.toArray(new String[0]);
		}
		
		if ( logger.isDebugEnabled() ) {
			logger.debug(className, function, "strClientKey[{}] scsEnvId[{}]", strClientKey, scsEnvId);
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
			}
		}
		
		String strApi = clientKey.getApi().toString();
		
		if ( null != database ) {
			database.addStaticRequest(strApi, strClientKey, scsEnvId, dbaddresses, new DatabaseEvent() {
	
				@Override
				public void update(String key, String[] values) {
					
					DataBaseClientKey clientKey = new DataBaseClientKey();
					clientKey.setAPI(API.multiReadValue);
					clientKey.setWidget(INSPECTOR);
					clientKey.setStability(Stability.STATIC);
					clientKey.setAdress(parent);
					
					String strClientKey = clientKey.toClientKey();

					if ( 0 == strClientKey.compareTo(key) ) {
					
						repsonseHeader(key, values);
					}
				}
	
			});
		} else {
				logger.warn(className, function, "database IS NUL");
		}
		
		logger.end(className, function);
	}
	
	private void removeTabWithoutRight(Map<String, String> rights, String rightname, String right, Panel tab) {
		final String function = "removeTabWithoutRight";
		logger.begin(className, function);
		
		logger.debug(className, function, "Checking rightname[{}] right[{}]", rightname, right);
		if ( null != right ) {
			if ( right.equals(String.valueOf(false))) {
				logger.warn(className, function, "right IS INSUFFICIENT RIGHT");
				
				panelTab.remove(tab);
			}
		} else {
			logger.warn(className, function, "right IS NULL");
		}
		
		logger.end(className, function);
	}
	
	@Override
	public void connect() {
		final String function = "connect";

		String fileName = UIPanelInspector_i.strConfigPrefixWODot+UIPanelInspector_i.strConfigExtension;
		String keyperiodmillis = UIPanelInspector_i.strConfigPrefix+UIPanelInspector_i.strPeriodMillis;
		periodMillis = ReadProp.readInt(UIInspector_i.strUIInspector, fileName, keyperiodmillis, 250);
		
		logger.debug(className, function, "database pollor periodMillis[{}]", periodMillis);
		
		logger.begin(className, function);

		database.connect();
		database.connectTimer(this.periodMillis);
		
		// OPM
		String dictionariesCacheName = "UIInspectorPanel";
		
		String keyopmapi = UIPanelInspector_i.strConfigPrefix+UIPanelInspector_i.strOpmApi;
		String keyopmapivalue = ReadProp.readString(dictionariesCacheName, fileName, keyopmapi, "");
		
		String opmapi = keyopmapivalue;

		logger.debug(className, function, "opmapi[{}]", opmapi);
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(opmapi);
		
		if ( null != uiOpm_i ) {

			String mode = null;
			String keymode = UIPanelInspector_i.strConfigPrefix+UIPanelInspector_i.strConfigMode;
			logger.debug(className, function, "dictionariesCacheName[{}] fileName[{}] keymode[{}]", new Object[]{dictionariesCacheName, fileName, keymode});
			String keymodevalue = ReadProp.readString(dictionariesCacheName, fileName, keymode, "");
			
			mode = keymodevalue;
			
			logger.debug(className, function, "mode[{}]", mode);
			
			String keynumofaction = UIPanelInspector_i.strConfigPrefix+UIPanelInspector_i.strNumOfAction;
			String keynumofactionvalue = ReadProp.readString(dictionariesCacheName, fileName, keynumofaction, "");

			int numofaction = 0;
			try {
				numofaction = Integer.parseInt(keynumofactionvalue);
			} catch ( NumberFormatException ex ) {
				logger.warn(className, function, "keynumofactionvalue[{}] IS INVALID", keynumofactionvalue);
				logger.warn(className, function, "keynumofactionvalue[{}] NumberFormatException.ex[{}]", keynumofactionvalue, ex.toString());
			}
			
			logger.debug(className, function, "numofaction[{}]", numofaction);
			
			String [] actions = null;
			String [] rightnames = null;
			
			if ( null == actions ) { actions = new String[numofaction]; }
			if ( null == rightnames ) { rightnames = new String[numofaction]; }
			
			HashMap<String, String> rights = new HashMap<String, String>();
			
			for ( int i = 1 ; i <= numofaction ; ++i ) {
				String actionname = UIPanelInspector_i.strConfigAction + i;
				rightnames[i] = actionname;
				
				String keyaction = UIPanelInspector_i.strConfigPrefix+actionname;
				
				logger.debug(className, function, "dictionariesCacheName[{}] fileName[{}] keyaction[{}]", new Object[]{dictionariesCacheName, fileName, keyaction});
				String keyactionvalue = ReadProp.readString(dictionariesCacheName, fileName, keyaction, "");
				actions[i] = keyactionvalue;
				
				boolean right = false;
				
				String action = actions[i];
				if ( null != action ) {
					logger.debug(className, function, "this.function[{}] this.location[{}] action[{}] mode[{}]", new Object[]{this.function, this.location, action, mode});
					right = uiOpm_i.checkAccess(this.function, this.location, action, mode);
					logger.debug(className, function, "i[{}] actionname[{}] right[{}]", new Object[]{i, actionname, right});
					
					rights.put(actionname, String.valueOf(right));
				} else {
					logger.warn(className, function, "action({}) IS NULL", i);
				}
			}
			
			uiInspectorInfo		.setRight(rights);
			uiInspectorControl	.setRight(rights);
			uiInspectorTag		.setRight(rights);
			uiInspectorAdvance	.setRight(rights);
			
			uiInspectorInfo		.applyRight();
			uiInspectorControl	.applyRight();
			uiInspectorTag		.applyRight();
			uiInspectorAdvance	.applyRight();
			
			// Right 1
			String rightname1 = UIPanelInspector_i.strConfigAction+1;
			String right1 = rights.get(UIPanelInspector_i.strConfigAction+1);
			removeTabWithoutRight(rights, rightname1, right1, panelInfo);
			
			// Right 2
			String rightname2 = UIPanelInspector_i.strConfigAction+2;
			String right2 = rights.get(UIPanelInspector_i.strConfigAction+2);
			removeTabWithoutRight(rights, rightname2, right2, panelCtrl);
			
			// Right 3
			String rightname3 = UIPanelInspector_i.strConfigAction+3;
			String right3 = rights.get(UIPanelInspector_i.strConfigAction+3);
			removeTabWithoutRight(rights, rightname3, right3, panelTag);
			
			// Right 4
			String rightname4 = UIPanelInspector_i.strConfigAction+4;
			String right4 = rights.get(rightname4);
			removeTabWithoutRight(rights, rightname4, right4, panelAdv);
		
		} else {
			logger.warn(className, function, "uiOpm_i IS NULL");
		}
		
		// hmiOrder
		String strHmiOrderEnable = UIPanelInspector_i.strConfigPrefix+UIPanelInspector_i.strHmiOrderEnable;
		hmiOrderEnable = ReadProp.readBoolean(dictionariesCacheName, fileName, strHmiOrderEnable, false);
		logger.debug(className, function, "strHmiOrderEnable[{}] hmiOrderEnable[{}]", strHmiOrderEnable, hmiOrderEnable);
		
		String strHmiOrderAttribute = UIPanelInspector_i.strConfigPrefix+UIPanelInspector_i.strHmiOrderAttribute;
		hmiOrderAttribute = ReadProp.readString(dictionariesCacheName, fileName, strHmiOrderAttribute, "");
		logger.debug(className, function, "strHmiOrderAttribute[{}] hmiOrderAttribute[{}]", strHmiOrderAttribute, hmiOrderAttribute);
		
		String strHmiOrderFilterThreshold = UIPanelInspector_i.strConfigPrefix+UIPanelInspector_i.strHmiOrderFilterThreshold;
		hmiOrderFilterThreshold = ReadProp.readInt(UIInspector_i.strUIInspector, fileName, strHmiOrderFilterThreshold, -1);
		logger.debug(className, function, "strHmiOrderFilterThreshold[{}] hmiOrderFilterThreshold[{}]", strHmiOrderFilterThreshold, hmiOrderFilterThreshold);

		requestGetChilden();

		requestHeader();
		
		logger.end(className, function);
	}
	
	@Override
	public void disconnect() {
		final String function = "disconnect";
		
		logger.begin(className, function);
		
		// Unrerves equipment
		unReserveEquipment();
		
		makeTabsDisconnect();
		
		((UIInspectorHeader)				uiInspectorHeader)		.disconnect();
		((UIInspectorEquipmentReserve)		equipmentReserve)		.disconnect();
		
		database.disconnectTimer();
		database.disconnect();

		logger.end(className, function);
	}
	
	private boolean isRegExpMatch(RegExp regExp, String input) {
		final String function = "isRegExpMatch";
		
		logger.begin(className, function);
		logger.debug(className, function, "regExp[{}] input[{}]", regExp, input);
		MatchResult matcher = regExp.exec(input);
		boolean matchFound = matcher != null;
		if ( matchFound ) {
			if ( logger.isDebugEnabled() ) {
				for ( int i = 0 ; i < matcher.getGroupCount(); i++) {
					String groupStr = matcher.getGroup(i);
					logger.debug(className, function, "input[{}] groupStr[{}]", input, groupStr);
				}
			}
		}
		logger.debug(className, function, "matchFound[{}]", matchFound);
		logger.end(className, function);
		return matchFound;
	}
	
	private boolean isRegExpMatch(List<String> regExpPatterns, String dbaddress) {
		boolean listMatch = false;
		for ( String regExpPattern : regExpPatterns ) {
			if ( isRegExpMatch( RegExp.compile(regExpPattern), dbaddress) )	{ 
				listMatch = true;
				break;
			}
		}
		return listMatch;
	}
	
	private void prepareBlackList(List<List<String>> backLists, String dictionariesCacheName, String tab, int i) {
		final String function = "prepareBlackList";
		logger.begin(className, function);
		logger.debug(className, function, " get"+UIPanelInspector_i.strBlack+"Lists");
		List<String> backList = backLists.get(i);
		String fileName = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigExtension;
		String keyNumName = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigNameBackList+UIPanelInspector_i.strConfigNameSize;
		logger.debug(className, function, "fileName[{}] keyNumName[{}]", fileName, keyNumName);
		int numOfBack = ReadProp.readInt(dictionariesCacheName, fileName, keyNumName, 0);
		for ( int y = 0 ; y < numOfBack ; ++y ) {
			String key = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigNameBackList+UIPanelInspector_i.strDot+y;
			String value = ReadProp.readString(dictionariesCacheName, fileName, key, "");
			logger.debug(className, function, "key[{}] value[{}]", key, value);
			backList.add(value);
		}

		logger.end(className, function);
	}
	
	private void prepareWhiteList(List<List<String>> lists, String dictionariesCacheName, String tab, int i) {
		final String function = "prepareBlackList";
		logger.begin(className, function);
		logger.debug(className, function, " get"+UIPanelInspector_i.strWhite+"Lists");
		List<String> whiteList = lists.get(i);
		String fileName = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigExtension;
		String keyNumName = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigNameWhileList+UIPanelInspector_i.strConfigNameSize;
		logger.debug(className, function, "fileName[{}] keyNumName[{}]", fileName, keyNumName);
		int numOfWhite = ReadProp.readInt(dictionariesCacheName, fileName, keyNumName, 0);
		for ( int y = 0 ; y < numOfWhite ; ++y ) {
			String key = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigNameWhileList+UIPanelInspector_i.strDot+y;
			String value = ReadProp.readString(dictionariesCacheName, fileName, key, "");
			logger.debug(className, function, "key[{}] value[{}]", key, value);
			whiteList.add(value);
		}

		logger.end(className, function);
	}
	
	private void applyFiltedList(List<String> list, List<String> regExpPatternBlackList, List<String> regExpPatternWhiteList, String dbaddress) {
		final String function = "applyFiltedList";
		logger.begin(className, function);
		boolean blackListMatch=false;
		boolean whileListMatch=false;
		blackListMatch = isRegExpMatch(regExpPatternBlackList, dbaddress);
		if ( !blackListMatch ) { 
			whileListMatch = isRegExpMatch(regExpPatternWhiteList, dbaddress);
		}
		if ( whileListMatch ) { list.add(dbaddress); }
		logger.end(className, function);
	}

	private TextBox txtMsg = null;
	private List<String> infos		= new LinkedList<String>();
	private List<String> controls	= new LinkedList<String>();
	private List<String> tags		= new LinkedList<String>();
	private List<String> advances	= new LinkedList<String>();
	@Override
	public void buildTabsAddress(String[] instances) {
		final String function = "buildTabsAddress";
		logger.begin(className, function);

		List<String> infoRegExpPatternBlackList		= new LinkedList<String>();
		List<String> controlRegExpPatternBlackList	= new LinkedList<String>();
		List<String> tagRegExpPatternBlackList		= new LinkedList<String>();
		List<String> advanceRegExpPatternBlackList	= new LinkedList<String>();
		
		List<String> infoRegExpPatternWhileList		= new LinkedList<String>();
		List<String> controlRegExpPatternWhileList	= new LinkedList<String>();
		List<String> tagRegExpPatternWhileList		= new LinkedList<String>();
		List<String> advanceRegExpPatternWhileList	= new LinkedList<String>();
		
		logger.begin(className, function + " getLists");
		
		String dictionariesCacheName = "UIInspectorPanel";

		List<List<String>> backLists = new LinkedList<List<String>>();
		backLists.add(infoRegExpPatternBlackList);
		backLists.add(controlRegExpPatternBlackList);
		backLists.add(tagRegExpPatternBlackList);
		backLists.add(advanceRegExpPatternBlackList);
		
		List<List<String>> whiteLists = new LinkedList<List<String>>();
		whiteLists.add(infoRegExpPatternWhileList);
		whiteLists.add(controlRegExpPatternWhileList);
		whiteLists.add(tagRegExpPatternWhileList);
		whiteLists.add(advanceRegExpPatternWhileList);
		
		for ( int i = 0 ; i < strTabConfigNames.length ; ++i ) {
			String tab = strTabConfigNames[i];
			logger.begin(className, function+" "+tab);
			
			prepareBlackList(backLists, dictionariesCacheName, tab, i);

			prepareWhiteList(whiteLists, dictionariesCacheName, tab, i);
			
			logger.end(className, function+" "+tab);
		}

		logger.end(className, function + " getLists");

		logger.debug(className, function,"Iterator Begin");
		
		for ( String dbaddress : instances ) {
			logger.debug(className, function,"Iterator dbaddress[{}]", dbaddress);
			
			if ( null != dbaddress ) {

				logger.debug(className, function, "Iterator dbaddress[{}]", dbaddress);
				
				applyFiltedList(infos, infoRegExpPatternBlackList, infoRegExpPatternWhileList, dbaddress);
				
				applyFiltedList(controls, controlRegExpPatternBlackList, controlRegExpPatternWhileList, dbaddress);
				
				applyFiltedList(tags, tagRegExpPatternBlackList, tagRegExpPatternWhileList, dbaddress);
				
				applyFiltedList(advances, advanceRegExpPatternBlackList, advanceRegExpPatternWhileList, dbaddress);

			} else {
				logger.warn(className, function, "Iterator dbaddress IS NULL");
			}
		}
		logger.end(className, function);
	}
	
	@Override
	public void makeTabsSetAddress() {
		final String function = "makeTabsSetAddress";
		
		logger.begin(className, function);
		
		if ( logger.isDebugEnabled() ) {
			for ( String dbaddress : infos )	{ logger.debug(className, function, "infos dbaddress[{}]", dbaddress); }
			for ( String dbaddress : controls )	{ logger.debug(className, function, "controls dbaddress[{}]", dbaddress); }
			for ( String dbaddress : tags )		{ logger.debug(className, function, "tags dbaddress[{}]", dbaddress); }
			for ( String dbaddress : advances )	{ logger.debug(className, function, "advances dbaddress[{}]", dbaddress); }			
		}

		
		uiInspectorHeader	.setParent(scsEnvId, parent);
		equipmentReserve	.setParent(scsEnvId, parent);
		
		uiInspectorInfo		.setParent(scsEnvId, parent);
		uiInspectorControl	.setParent(scsEnvId, parent);
		uiInspectorTag		.setParent(scsEnvId, parent);
		uiInspectorAdvance	.setParent(scsEnvId, parent);
		
		
		uiInspectorHeader	.setAddresses	(infos		.toArray(new String[0]));
		equipmentReserve	.setAddresses	(infos		.toArray(new String[0]));
		
		uiInspectorInfo		.setAddresses	(infos		.toArray(new String[0]));
		uiInspectorControl	.setAddresses	(controls	.toArray(new String[0]));
		uiInspectorTag		.setAddresses	(tags		.toArray(new String[0]));
		uiInspectorAdvance	.setAddresses	(advances	.toArray(new String[0]));
		
		
		logger.end(className, function);
	}
	
	@Override
	public void makeTabsBuildWidgets() {
		final String function = "makeTabsBuildWidgets";

		for ( int i = 0 ; i < uiInspectorTabs.size() ; ++i ) {
			UIInspectorTab_i uiPanelInspector = uiInspectorTabs.get(i);
			String dictionariesCacheName = "UIInspectorPanel";
			String tab = strTabConfigNames[i];
			String fileName = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigExtension;
			String key = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strDot+UIPanelInspector_i.strConfigNumberOfPointPerPage;
			int numOfPointForEachPage = ReadProp.readInt(dictionariesCacheName, fileName, key, 0);
			if ( null != uiPanelInspector ) {
				uiPanelInspector.buildWidgets(numOfPointForEachPage);
			} else {
				logger.warn(className, function, " uiPanelInspector_i IS NULL");
			}	
		}

		logger.end(className, function);
	}
	
	@Override
	public void makeTabsConnect() {
		final String function = "makeTabsConnect";
		
		logger.begin(className, function);
		
		for ( UIInspectorTab_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.connect();
			} else {
				logger.warn(className, function, "uiPanelInspector_i IS NULL");
			}
		}

		logger.end(className, function);
	}

	@Override
	public void makeTabsDisconnect() {
		final String function = "makeTabsDisconnect";
		
		logger.begin(className, function);
		
		for ( UIInspectorTab_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.disconnect();
			} else {
				logger.warn(className, function, "uiPanelInspector_i IS NULL");
			}
		}

		logger.end(className, function);
	}
	
	
	private UIInspectorTab_i uiInspectorHeader		= null;
	private UIInspectorTab_i equipmentReserve		= null;
	
	private UIInspectorTab_i uiInspectorInfo		= null;
	private UIInspectorTab_i uiInspectorControl		= null;
	private UIInspectorTab_i uiInspectorTag			= null;
	private UIInspectorTab_i uiInspectorAdvance		= null;

	private TabPanel panelTab = null;

	private Panel panelHeader		= null;
	private Panel panelInfo			= null;
	private Panel panelCtrl			= null;
	private Panel panelTag			= null;
	private Panel panelAdv			= null;
	
	private LinkedList<UIInspectorTab_i> uiInspectorTabs = null;

	@Override
	public void init() {
		final String function = "init";

		logger.begin(className, function);

		uiInspectorTabs 	= new LinkedList<UIInspectorTab_i>();
		
		uiInspectorHeader	= new UIInspectorHeader();
		equipmentReserve	= new UIInspectorEquipmentReserve();
		
		uiInspectorInfo		= new UIInspectorInfo();
		uiInspectorControl	= new UIInspectorControl();
		uiInspectorTag		= new UIInspectorTag();
		uiInspectorAdvance	= new UIInspectorAdvance();
		

		uiInspectorTabs.add(uiInspectorInfo);
		uiInspectorTabs.add(uiInspectorControl);
		uiInspectorTabs.add(uiInspectorTag);
		uiInspectorTabs.add(uiInspectorAdvance);
		
		
		((UIInspectorEquipmentReserve)equipmentReserve).setEquipmentReserveEvent(new EquipmentReserveEvent() {
			
			@Override
			public void isAvaiable(int eqtReserved) {
				final String function = "isAvaiable";
				if ( null != panelTab ) {
					logger.debug(className, function, "eqtReserved[{}]", eqtReserved);
					int tabCount = panelTab.getTabBar().getTabCount();
					logger.debug(className, function, "tabCount[{}]", tabCount);
					if ( tabCount > 1 ) {
						int selected = panelTab.getTabBar().getSelectedTab();
						logger.debug(className, function, "selected[{}]", selected);
						if ( 2 == eqtReserved || 0 == eqtReserved ) {
							if ( 0 != selected ) {
								logger.debug(className, function, "selectTab to 0");
								panelTab.getTabBar().selectTab(0);
							}
						}
						String cssName = "project-gwt-inspector-tabpanel-tab-disable-";
						for ( int i = 1 ; i < tabCount ; ++i ) {
							String cssNameNum = cssName + i;
							if ( 2 == eqtReserved ) {
								logger.debug(className, function, "addStyleName cssNameNum[{}]", cssNameNum);
								((Widget)panelTab.getTabBar().getTab(i)).addStyleName(cssNameNum);
							} else {
								logger.debug(className, function, "removeStyleName cssNameNum[{}]", cssNameNum);
								((Widget)panelTab.getTabBar().getTab(i)).removeStyleName(cssNameNum);
							}
						}
					}
				}
			}
		});
		
		uiInspectorInfo.setUIInspectorTabClickEvent(new UIInspectorTabClickEvent() {
			
			@Override
			public void onClick() {
				logger.debug(className, function, "onClick uiInspectorInfo");
				unReserveEquipment();
			}
		});

		
		uiInspectorControl.setMessageBoxEvent(new MessageBoxEvent() {
			
			@Override
			public void setMessage(String message) {
				if ( null != txtMsg ) {
					logger.debug(className, function, "setMessage message[{}]", message);
					if ( null != message ) {
						String msg = txtMsg.getText();
						if ( null != msg ) {
							if ( ! msg.equals(message) ) txtMsg.setText(message);
						}
					}
					
				}
			}
			
			@Override
			public void addMessage(String message) {
				if ( null != txtMsg ) {
					logger.debug(className, function, "setMessage message[{}]", message);
					String text = txtMsg.getText();
					logger.debug(className, function, "setMessage text[{}]", text);
					if ( text.length() > 0 ) text += "\n";
					logger.debug(className, function, "setMessage text + message[{}]", text + message);
					txtMsg.setText(text + message);
				}
			}
		});
		
		uiInspectorControl.setUIInspectorTabClickEvent(new UIInspectorTabClickEvent() {
			
			@Override
			public void onClick() {
				logger.debug(className, function, "onClick uiInspectorControl");
				reserveEquipment();
			}
		});
		
		uiInspectorTag.setMessageBoxEvent(new MessageBoxEvent() {
			
			@Override
			public void setMessage(String message) {
				if ( null != txtMsg ) {
					logger.debug(className, function, "setMessage message[{}]", message);
					txtMsg.setText(message);
				}
			}
			
			@Override
			public void addMessage(String message) {
				if ( null != txtMsg ) {
					logger.debug(className, function, "setMessage message[{}]", message);
					String text = txtMsg.getText();
					logger.debug(className, function, "setMessage text[{}]", text);
					if ( text.length() > 0 ) text += "\n";
					logger.debug(className, function, "setMessage text + message[{}]", text + message);
					txtMsg.setText(text + message);
				}
			}
		});
		
		uiInspectorTag.setUIInspectorTabClickEvent(new UIInspectorTabClickEvent() {
			
			@Override
			public void onClick() {
				logger.debug(className, function, "onClick uiInspectorTag");
				reserveEquipment();
			}
		});
		
		uiInspectorAdvance.setMessageBoxEvent(new MessageBoxEvent() {
			
			@Override
			public void setMessage(String message) {
				if ( null != txtMsg ) {
					logger.debug(className, function, "setMessage message[{}]", message);
					txtMsg.setText(message);
				}
			}
			
			@Override
			public void addMessage(String message) {
				if ( null != txtMsg ) {
					logger.debug(className, function, "setMessage message[{}]", message);
					String text = txtMsg.getText();
					logger.debug(className, function, "setMessage text[{}]", text);
					if ( text.length() > 0 ) text += "\n";
					logger.debug(className, function, "setMessage text + message[{}]", text + message);
					txtMsg.setText(text + message);
				}
			}
		});
		
		uiInspectorAdvance.setUIInspectorTabClickEvent(new UIInspectorTabClickEvent() {
			
			@Override
			public void onClick() {
				logger.debug(className, function, "onClick uiInspectorAdvance");
				reserveEquipment();
			}
		});
		

		uiInspectorHeader.setUINameCard(this.uiNameCard);
		uiInspectorHeader.init();
		uiInspectorHeader.setDatabase(database);
		panelHeader	= uiInspectorHeader.getMainPanel();
		
		equipmentReserve.setUINameCard(this.uiNameCard);
		equipmentReserve.init();
		equipmentReserve.setDatabase(database);
		equipmentReserve.getMainPanel();
		
		uiInspectorInfo.setUINameCard(this.uiNameCard);
		uiInspectorInfo.init();
		uiInspectorInfo.setDatabase(database);
		panelInfo = uiInspectorInfo.getMainPanel();
		
		uiInspectorControl.setUINameCard(this.uiNameCard);
		uiInspectorControl.init();
		uiInspectorControl.setDatabase(database);
		panelCtrl = uiInspectorControl.getMainPanel();
		
		uiInspectorTag.setUINameCard(this.uiNameCard);
		uiInspectorTag.init();
		uiInspectorTag.setDatabase(database);
		panelTag = uiInspectorTag.getMainPanel();
		
		uiInspectorAdvance.setUINameCard(this.uiNameCard);
		uiInspectorAdvance.init();
		uiInspectorAdvance.setDatabase(database);
		panelAdv = uiInspectorAdvance.getMainPanel();
		
		
		panelTab = new TabPanel();
		panelTab.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				final String function = "onBeforeSelection";
				logger.begin(className, function);
				logger.debug(className, function, "event[{}]", event);
				logger.debug(className, function, "event.getItem()[{}]", event.getItem());
				int intEvent = event.getItem().intValue();
				logger.debug(className, function, "intEvent[{}]", intEvent);
				if ( 0 == intEvent ) {
					unReserveEquipment();
				} else {
					int intEqtReserved = ((UIInspectorEquipmentReserve)equipmentReserve).getEqtReservedValue();
					logger.debug(className, function, "intEqtReserved[{}]", intEqtReserved);
					if ( 2 == intEqtReserved ) {
						logger.debug(className, function, "intReserve equals to 2, Cancel event...");
						event.cancel();
					} else {
						logger.debug(className, function, "intReserve not equals to 2, reserve equipment...");
						reserveEquipment();
					}
				}
						
				logger.end(className, function);
			}
		});
		
		panelTab.addStyleName("project-gwt-tabpanel-inspector-tabpanel");

		panelTab.add(panelInfo	, strTabNames[0]);
		panelTab.add(panelCtrl	, strTabNames[1]);
		panelTab.add(panelTag	, strTabNames[2]);
		panelTab.add(panelAdv	, strTabNames[3]);
		panelTab.selectTab(0);
		
		Button btnClose = new Button("Close");
		btnClose.addStyleName("project-gwt-button-inspector-bottom-close");
		
		btnClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				close();
			}
	    });
		
		txtMsg = new TextBox();
		txtMsg.setReadOnly(true);
		txtMsg.addStyleName("project-gwt-textbox-inspector-bottom-message");
		
		HorizontalPanel bottomBar = new HorizontalPanel();
		bottomBar.addStyleName("project-gwt-panel-inspector-bottom");
		bottomBar.add(txtMsg);
		bottomBar.add(btnClose);
		
		rootPanel = new VerticalPanel();
		rootPanel.add(panelHeader);
		rootPanel.add(panelTab);
		rootPanel.add(bottomBar);
		rootPanel.addStyleName("project-gwt-panel-inspector");

		logger.end(className, function);
	}
	
	private UIPanelInspectorEvent uiPanelInspectorEvent = null;
	public void setUIInspectorEvent(UIPanelInspectorEvent uiPanelInspectorEvent) {
		this.uiPanelInspectorEvent = uiPanelInspectorEvent;
	}
	
	public void setText(String title) {
		if ( null != uiPanelInspectorEvent ) uiPanelInspectorEvent.setTitle(title);
	}
	
	public void onClose() {
		if ( null != uiPanelInspectorEvent ) uiPanelInspectorEvent.onClose();
	}
	
	@Override
	public void close() {
		final String function = "close";
		logger.begin(className, function);
		onClose();
		logger.end(className, function);
	}
	
	private void reserveEquipment() {
		final String function = "reserveEquipment";
		logger.begin(className, function);
		EquipmentReserve.equipmentReservation(scsEnvId, parent, database);
		logger.end(className, function);
	}
	
	private void unReserveEquipment() {
		final String function = "unReserveEquipment";
		logger.begin(className, function);
		EquipmentReserve.equipmentUnreservation(scsEnvId, parent, database);
		logger.end(className, function);
	}

}
