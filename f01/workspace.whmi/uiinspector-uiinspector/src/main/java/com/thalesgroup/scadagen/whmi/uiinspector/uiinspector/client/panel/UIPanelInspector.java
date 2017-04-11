package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorEquipmentReserve;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorHeader;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorInfo;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorTabFactory;
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

	// Static Attribute List
	private final String staticAttibutes[]	= new String[] {PointName.label.toString()};
	
	// hmiOrder
	private boolean hmiOrderEnable		= false; 
	private String hmiOrderAttribute	= null;
	private int hmiOrderFilterThreshold	= -1;

	private String scsEnvId		= null;
	private String parent		= null;
	private int periodMillis	= 250;
	
	final private String INSPECTOR		= "inspector";
	
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
		
		uiInspectorHeader.connect();
		equipmentReserve.connect();
		
		for ( String k : panelData.keySet() ) {
			PanelData d = panelData.get(k);
			if ( d.points.size() <= 0 ) {
				panelTab.remove(d.panel);
			}
		}

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
					
					((UIInspectorInfo)				panelData.get(info).uiInspectorTab_i)		.updateValue(key, dynamicvalues);
					((UIInspectorControl)			panelData.get(control).uiInspectorTab_i)	.updateValue(key, dynamicvalues);
					((UIInspectorTag)				panelData.get(tag).uiInspectorTab_i)		.updateValue(key, dynamicvalues);
					((UIInspectorAdvance)			panelData.get(advance).uiInspectorTab_i)	.updateValue(key, dynamicvalues);

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

	private boolean checkRight(String dictionariesCacheName, String fileName, UIOpm_i uiOpm_i, String actionname, String mode) {
		final String function = "checkRight";
		logger.begin(className, function);
		boolean right = false;
		String keyaction = UIPanelInspector_i.strConfigPrefix+UIPanelInspector_i.strConfigAction+"_"+actionname;
		
		logger.debug(className, function, "dictionariesCacheName[{}] fileName[{}] keyaction[{}]", new Object[]{dictionariesCacheName, fileName, keyaction});
		String action = ReadProp.readString(dictionariesCacheName, fileName, keyaction, null);
		logger.warn(className, function, "action[{}]", action);
		if ( null != action ) {
			logger.debug(className, function, "this.function[{}] this.location[{}] action[{}] mode[{}]"
					, new Object[]{this.function, this.location, action, mode});
			right = uiOpm_i.checkAccess(this.function, this.location, action, mode);
			logger.debug(className, function, "actionname[{}] right[{}]", new Object[]{actionname, right});
		} else {
			logger.warn(className, function, "action IS NULL");
		}
		logger.end(className, function);
		return right;
	}
	
	private Map<String, Boolean> rights = null;
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		String fileName = UIPanelInspector_i.strConfigPrefixWODot+UIPanelInspector_i.strConfigExtension;
		String keyperiodmillis = UIPanelInspector_i.strConfigPrefix+UIPanelInspector_i.strPeriodMillis;
		periodMillis = ReadProp.readInt(UIInspector_i.strUIInspector, fileName, keyperiodmillis, 250);
		
		logger.debug(className, function, "database pollor periodMillis[{}]", periodMillis);

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
			mode = ReadProp.readString(dictionariesCacheName, fileName, keymode, "");
			
			logger.debug(className, function, "mode[{}]", mode);

			rights = new HashMap<String, Boolean>();
			for ( String k : panelData.keySet() ) {
				PanelData d = panelData.get(k);
				d.opmright = checkRight(dictionariesCacheName, fileName, uiOpm_i, d.tabConfigName, mode);
				rights.put(d.tabConfigName, d.opmright);
			}
			
			boolean right = checkRight(dictionariesCacheName, fileName, uiOpm_i, "ackalarm", mode);
			rights.put("ackalarm", right);

			for ( String k : panelData.keySet() ) {
				PanelData d = panelData.get(k);
				
				d.uiInspectorTab_i.setRight(rights);
				d.uiInspectorTab_i.applyRight();
				
				if ( ! d.opmright ) panelTab.remove(d.panel);
			}

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
		
		uiInspectorHeader.disconnect();
		equipmentReserve.disconnect();
		
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
	
	private void prepareBlackList(List<String> backList, String dictionariesCacheName, String tab) {
		final String function = "prepareBlackList";
		logger.begin(className, function);
		logger.debug(className, function, " get"+UIPanelInspector_i.strBlack+"Lists");
		if ( null != backList ) {
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
		} else {
			logger.warn(className, function, "backList IS NULL");
		}
		logger.end(className, function);
	}

	private void prepareWhiteList(List<String> whiteList, String dictionariesCacheName, String tab) {
		final String function = "prepareBlackList";
		logger.begin(className, function);
		logger.debug(className, function, " get"+UIPanelInspector_i.strWhite+"Lists");
		if ( null != whiteList ) {
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
		} else {
			logger.warn(className, function, "whiteList IS NULL");
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
	@Override
	public void buildTabsAddress(String[] instances) {
		final String function = "buildTabsAddress";
		logger.begin(className, function);

		logger.begin(className, function + " getLists");
		
		String dictionariesCacheName = "UIInspectorPanel";

		for ( String k : panelData.keySet() ) {
			PanelData d = panelData.get(k);
			String tab = d.tabConfigName;
			logger.begin(className, function+" "+tab);
			
			d.regExpPatternBlackList = new LinkedList<String>();
			
			prepareBlackList(d.regExpPatternBlackList, dictionariesCacheName, tab);
			
			d.regExpPatternWhileList = new LinkedList<String>();
			
			prepareWhiteList(d.regExpPatternWhileList, dictionariesCacheName, tab);

			logger.end(className, function+" "+tab);
		}

		logger.end(className, function + " getLists");

		logger.debug(className, function,"Iterator Begin");
		
		for ( String dbaddress : instances ) {
			logger.debug(className, function,"Iterator dbaddress[{}]", dbaddress);
			if ( null != dbaddress ) {
				logger.debug(className, function, "Iterator dbaddress[{}]", dbaddress);
				for ( String k : panelData.keySet() ) {
					PanelData d = panelData.get(k);
					applyFiltedList(d.points, d.regExpPatternBlackList, d.regExpPatternWhileList, dbaddress);
				}
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
			for ( String k : panelData.keySet() ) {
				PanelData d = panelData.get(k);
				for ( String dbaddress : d.points ) {
					logger.debug(className, function, "points[{}] dbaddress[{}]", d.tabName, dbaddress);
				}
			}	
		}
		
		uiInspectorHeader	.setParent(scsEnvId, parent);
		equipmentReserve	.setParent(scsEnvId, parent);
		
		for ( String k : panelData.keySet() ) {
			PanelData d = panelData.get(k);
			d.uiInspectorTab_i.setParent(scsEnvId, parent);
		}

		uiInspectorHeader	.setAddresses	(panelData.get(info).points.toArray(new String[0]));
		equipmentReserve	.setAddresses	(panelData.get(info).points.toArray(new String[0]));

		for ( String k : panelData.keySet() ) {
			final PanelData d = panelData.get(k);
			d.uiInspectorTab_i.setAddresses	(d.points		.toArray(new String[0]));
		}

		logger.end(className, function);
	}
	
	@Override
	public void makeTabsBuildWidgets() {
		final String function = "makeTabsBuildWidgets";
		
		for ( String k : panelData.keySet() ) {
			PanelData d = panelData.get(k);
			
			UIInspectorTab_i uiPanelInspector = d.uiInspectorTab_i;
			String dictionariesCacheName = "UIInspectorPanel";
			String tab = d.tabConfigName;
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
		
		for ( String k : panelData.keySet() ) {
			PanelData d = panelData.get(k);
			if ( null != d.uiInspectorTab_i ) {
				d.uiInspectorTab_i.connect();
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

		for ( String k : panelData.keySet() ) {
			final PanelData d = panelData.get(k);
			if ( null != d.uiInspectorTab_i ) {
				d.uiInspectorTab_i.disconnect();
			} else {
				logger.warn(className, function, "uiPanelInspector_i IS NULL");
			}
		}

		logger.end(className, function);
	}
	
	class PanelData {
		String tabName = null;
		String tabConfigName = null;
		UIInspectorTab_i uiInspectorTab_i = null;
		Panel panel = null;
		
		boolean isReserveEquipment = false;
		boolean hasSetMessageBoxEvent = false;
		
		List<String> points = null;
		
		List<String> regExpPatternBlackList = null;
		List<String> regExpPatternWhileList = null;
		
		String opmaction = null;
		boolean opmright = false;
	}
	private Map<String, PanelData> panelData = null;
	
	private UIInspectorTab_i uiInspectorHeader		= null;
	private UIInspectorTab_i equipmentReserve		= null;

	private TabPanel panelTab = null;

	private Panel panelHeader		= null;

	private PanelData getPanelData(String tabName, String tabConfigName, String uiInspectorTabName, boolean isReserveEquipment, boolean hasSetMessageBoxEvent) {
		final String function = "getPanelData";
		logger.begin(className, function);
		logger.debug(className, function, "tabName[{}] tabConfigName[{}] uiInspectorTabName[{}] isReserveEquipment[{}] hasSetMessageBoxEvent[{}]"
				, new Object[]{tabName, tabConfigName, uiInspectorTabName, isReserveEquipment, hasSetMessageBoxEvent});
		PanelData d = new PanelData();
		d.tabName = tabName;
		d.tabConfigName = tabConfigName;
		d.uiInspectorTab_i = UIInspectorTabFactory.getInstance().getUIInspectorTabFactory(uiInspectorTabName);
		d.uiInspectorTab_i.setTabName(d.tabConfigName);
		
		d.isReserveEquipment = isReserveEquipment;
		d.hasSetMessageBoxEvent = hasSetMessageBoxEvent;
		
		d.points = new LinkedList<String>();
		logger.end(className, function);
		return d;
	}
	
	private final static String strUIInspectorHeader 			= "UIInspectorHeader";
	private final static String strUIInspectorEquipmentReserve 	= "UIInspectorEquipmentReserve";
	
	private final static String info							= "info";
	private final static String strInfo							= "Info";
	private final static String strUIInspectorInfo 				= "UIInspectorInfo";
	
	private final static String control 						= "control";
	private final static String strControl						= "Control";
	private final static String strUIInspectorControl 			= "UIInspectorControl";
	
	private final static String tag								= "tag";
	private final static String strTagging						= "Tagging";
	private final static String strUIInspectorTag 				= "UIInspectorTag";
	
	private final static String advance							= "advance";
	private final static String strAdvance						= "Advance";
	private final static String strUIInspectorAdvance 			= "UIInspectorAdvance";

	@Override
	public void init() {
		final String function = "init";
		logger.begin(className, function);

		uiInspectorHeader	= UIInspectorTabFactory.getInstance().getUIInspectorTabFactory(strUIInspectorHeader);
		equipmentReserve	= UIInspectorTabFactory.getInstance().getUIInspectorTabFactory(strUIInspectorEquipmentReserve);

		panelData = new LinkedHashMap<String, PanelData>();
		
		panelData.put(info, getPanelData(strInfo, info, strUIInspectorInfo, false, false));
		panelData.put(control, getPanelData(strControl, control, strUIInspectorControl, true, true));
		panelData.put(tag, getPanelData(strTagging, tag, strUIInspectorTag, true, true));
		panelData.put(advance, getPanelData(strAdvance, advance, strUIInspectorAdvance, true, true));

		if ( logger.isDebugEnabled() ) {
			for ( String k : panelData.keySet() ) {
				PanelData d = panelData.get(k);
				if ( null != d ) { 
					logger.debug(className, function, "k[{}] d.tabName[{}] d.tabConfigName[{}]", new Object[]{k, d.tabName, d.tabConfigName}); 
				} else {
					logger.debug(className, function, "k[{}] IS NULL", k); 
				}
			}
		}
		
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
		
		for ( String k : panelData.keySet() ) {
			final PanelData d = panelData.get(k);
			d.uiInspectorTab_i.setUIInspectorTabClickEvent(new UIInspectorTabClickEvent() {
				
				@Override
				public void onClick() {
					logger.debug(className, function, d.tabConfigName );
					if ( d.isReserveEquipment ) {
						reserveEquipment();
					} else {
						unReserveEquipment();
					}
					
				}
			});
			
			d.uiInspectorTab_i.setMessageBoxEvent(new MessageBoxEvent() {
				
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
		}

		uiInspectorHeader.setUINameCard(this.uiNameCard);
		uiInspectorHeader.init();
		uiInspectorHeader.setDatabase(database);
		panelHeader	= uiInspectorHeader.getMainPanel();
		
		equipmentReserve.setUINameCard(this.uiNameCard);
		equipmentReserve.init();
		equipmentReserve.setDatabase(database);
		equipmentReserve.getMainPanel();
		
		for ( String k : panelData.keySet() ) {
			PanelData d = panelData.get(k);
			
			d.uiInspectorTab_i.setUINameCard(this.uiNameCard);
			d.uiInspectorTab_i.init();
			d.uiInspectorTab_i.setDatabase(database);
			d.panel = d.uiInspectorTab_i.getMainPanel();
		}

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

		for ( String k : panelData.keySet() ) {
			PanelData d = panelData.get(k);
			panelTab.add(d.panel, d.tabName);
		}
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
