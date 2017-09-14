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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.filter.PointsFilter;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.hom.HomEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.reserve.EquipmentReserve;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.reserve.EquipmentReserveEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.sorting.PointsSorting;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.sorting.PointsSortingEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorHeader;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorTabFactory;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.Stability;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.CheckAccessWithHOMEvent_i;

/**
 * @author syau
 *
 */
public class UIPanelInspector extends UIWidget_i implements UIInspector_i, UIInspectorTags_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelInspector.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	// Static Attribute List
	private final String staticAttibutes[]	= new String[] {PointName.label.toString()};
	
	private String ignoreEmptyRemoveTab = null;
	
	// hmiOrder
	private boolean hmiOrderEnable		= false; 
	private String hmiOrderAttribute	= null;
	private int hmiOrderFilterThreshold	= -1;

	private String scsEnvId		= null;
	private String parent		= null;
	private int periodMillis	= 250;
	
	private boolean equipmentReserveHasScreen = false;
	private boolean equipmentReserveUseHostName = false;
	private int equipmentReserveDefaultIndex = 0;
	
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

		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(INSPECTOR);
		clientKey.setStability(Stability.DYNAMIC);
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(scsEnvId);
		clientKey.setAdress(parent);
		String strClientKey = clientKey.toClientKey();
		
		database.setDynamicClientKey(strClientKey);
		
		logger.end(className, function);
	}
	
	private String location = null;
	public String getLocation() { return this.location; }
	public void setLocation(String location) { this.location = location; }
	
	private String function = null;
	public String getFunction() { return this.function; }
	public void setFunction(String function) { this.function = function; }
	
	private boolean isIgnoreEmptyRemoveTab(String key) {
		final String function = "isIgnoreEmptyRemoveTab";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}] ignoreEmptyRemoveTab[{}]", key, ignoreEmptyRemoveTab);
		boolean ret = false;
		String [] ignoreEmptyRemoveTabs = null;
		if ( null != ignoreEmptyRemoveTab && ! ignoreEmptyRemoveTab.trim().isEmpty() ) {
			ignoreEmptyRemoveTabs = ignoreEmptyRemoveTab.split(",");
		}
		if ( null != ignoreEmptyRemoveTabs ) {
			for ( String tab : ignoreEmptyRemoveTabs ) {
				if ( 0 == tab.compareTo(key) ) {
					ret = true;
					break;
				}
			}
		}
		logger.debug(className, function, "key[{}] ret[{}]", key, ret);
		logger.end(className, function);
		return ret;
	}
	
	private void connectTabs(String[] dbaddress) {
		final String function = "connectTabs";
		logger.begin(className, function);
		
		buildTabsAddress(dbaddress);
		makeTabsSetAddress();
		makeTabsBuildWidgets();
		makeTabsConnect();
		
		uiInspectorHeader.connect();

		for ( String k : tabDatas.keySet() ) {
			TabData d = tabDatas.get(k);
			if ( d.points.isEmpty() ) {
				
				if ( isIgnoreEmptyRemoveTab(k) ) {
					logger.debug(className, function, "k[{}] d.points.isEmpty() but in ignore list, ignore it", k);
				}
				else {
					logger.debug(className, function, "k[{}] d.points.isEmpty() Remove it", k);
					panelTab.remove(d.panel);
				}
				
			}
		}

		logger.end(className, function);
	}

	private void responseGetChilden(String key, String[] values) {
		final String function = "responseGetChilden";
		logger.begin(className, function);
		
		if ( hmiOrderEnable ) {
			
			PointsSorting pointsSorting = new PointsSorting();
			pointsSorting.setUINameCard(uiNameCard);
			pointsSorting.setDatabase(database);
			pointsSorting.setParent(scsEnvId, parent);
			pointsSorting.setDBAddresses(values, hmiOrderAttribute);
			pointsSorting.setThreshold(hmiOrderFilterThreshold);
			pointsSorting.setEquipmentsSortingEvent(new PointsSortingEvent() {
				
				@Override
				public void onSorted(String[] dbaddress) {
					connectTabs(dbaddress);

				}
			});
			pointsSorting.init();
		} else {
			
			connectTabs(values);
		}

		logger.end(className, function);
	}
	
	private void requestDynamic() {
		final String function = "requestDynamic";
		logger.begin(className, function);
		
		if ( null != database ) {

			DataBaseClientKey clientKey = new DataBaseClientKey();
			clientKey.setAPI(API.multiReadValue);
			clientKey.setWidget(INSPECTOR);
			clientKey.setStability(Stability.DYNAMIC);
			clientKey.setScreen(uiNameCard.getUiScreen());
			clientKey.setEnv(scsEnvId);
			clientKey.setAdress(parent);
			
			database.setDynamicClientKey(clientKey.toClientKey());
			
			database.setDynamicEvent(new DatabaseEvent() {
				
				@Override
				public void update(String key, String[] value) {
					final String function2 = "DatabaseEvent update";
					logger.begin(className, function);
					logger.debug(className, function, "{} key[{}]", new Object[]{function2, key});

					String [] dbaddresses	= database.getKeyAndAddress(key);
					String [] dbvalues		= database.getKeyAndValues(key);
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
					
					Map<String, String> dynamicvalues = new HashMap<String, String>();
					for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
						dynamicvalues.put(dbaddresses[i], dbvalues[i]);
					}
					
					uiInspectorHeader	.updateValue(key, dynamicvalues);
					
					for ( String k : tabDatas.keySet() ) {
						tabDatas.get(k).uiInspectorTab_i	.updateValue(key, dynamicvalues);
					}
					
					logger.end(className, function);
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
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(scsEnvId);
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
					clientKey.setScreen(uiNameCard.getUiScreen());
					clientKey.setEnv(scsEnvId);
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
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(scsEnvId);
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
					clientKey.setScreen(uiNameCard.getUiScreen());
					clientKey.setEnv(scsEnvId);
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
	
	
	private UIOpm_i uiOpm_i = null;
	private void prepareOpm() {
		final String function = "prepareOpm";
		logger.begin(className, function);
		// OPM
		logger.debug(className, function, "opmapi[{}]", opmapi);
		uiOpm_i = OpmMgr.getInstance().getOpm(opmapi);
		if ( null == uiOpm_i ) {
			logger.warn(className, function, "uiOpm_i IS NULL");
		}
		logger.end(className, function);
	}

	private void checkRights() {
		final String function = "checkRights";
		logger.begin(className, function);
		
		if ( null != uiOpm_i ) {

			rights = new HashMap<String, Boolean>();
			for ( String k : rightNames.keySet() ) {
				String action = rightNames.get(k);
				logger.debug(className, function, "k[{}] action[{}]", k, action);
				boolean right = uiOpm_i.checkAccess(this.function, this.location, action, mode);
				logger.debug(className, function, "k[{}] action[{}] right[{}]", new Object[]{k, action, right});
				rights.put(k, right);
			}

			for ( String k : tabDatas.keySet() ) {
				TabData d = tabDatas.get(k);
				d.uiInspectorTab_i.setRight(rights);
				d.uiInspectorTab_i.applyRight();
				if ( null != rights.get(k) && ! rights.get(k) ) {
					logger.warn(className, function, "k[{}] right, remove page", k);
					panelTab.remove(d.panel);
				}
			}

		} else {
			logger.warn(className, function, "uiOpm_i IS NULL");
		}
		logger.end(className, function);
	}
	
	private Map<String, Boolean> rights = null;
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		
		database.connect();
		database.connectTimer(this.periodMillis);
		
		checkRights();
		
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
		
		database.disconnectTimer();
		database.disconnect();

		logger.end(className, function);
	}

	private TextBox txtMsg = null;
	@Override
	public void buildTabsAddress(String[] instances) {
		final String function = "buildTabsAddress";
		logger.begin(className, function);

		logger.begin(className, function + " getLists");
		
		String dictionariesCacheName = "UIInspectorPanel";
		
		for ( String k : tabDatas.keySet() ) {
			TabData d = tabDatas.get(k);
			String tab = d.tabConfigName;
			logger.begin(className, function+" "+tab);
			
			d.regExpPatternBlackList = new LinkedList<String>();
			
			PointsFilter.prepareFilter(d.regExpPatternBlackList, dictionariesCacheName, tab, UIPanelInspector_i.strConfigNameBackList);
			
			d.regExpPatternWhileList = new LinkedList<String>();
			
			PointsFilter.prepareFilter(d.regExpPatternWhileList, dictionariesCacheName, tab, UIPanelInspector_i.strConfigNameWhileList);

			logger.end(className, function+" "+tab);
		}

		logger.end(className, function + " getLists");

		logger.debug(className, function,"Iterator Begin");
		
		for ( String dbaddress : instances ) {
			logger.debug(className, function,"Iterator dbaddress[{}]", dbaddress);
			if ( null != dbaddress ) {
				logger.debug(className, function, "Iterator dbaddress[{}]", dbaddress);
				for ( String k : tabDatas.keySet() ) {
					TabData d = tabDatas.get(k);
					PointsFilter.applyFiltedList(d.points, d.regExpPatternBlackList, d.regExpPatternWhileList, dbaddress);
					
					if ( logger.isDebugEnabled() ) {
						logger.debug(className, function, "k[{}] d.tabConfigName[{}]", k, d.tabConfigName);
						logger.debug(className, function, "d.points.size()[{}]", d.points.size());
						for ( String dba : d.points ) {
							logger.debug(className, function, "d.tabName[{}] dba[{}]", d.tabName, dba);
						}
					}	

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
			for ( String k : tabDatas.keySet() ) {
				TabData d = tabDatas.get(k);
				logger.debug(className, function, "k[{}] d.tabConfigName[{}] d.points.size[{}]", new Object[]{k, d.tabConfigName, d.points.size()});
				for ( String dbaddress : d.points ) {
					logger.debug(className, function, "points[{}] dbaddress[{}]", d.tabName, dbaddress);
				}
			}	
		}
		
		uiInspectorHeader	.setParent(scsEnvId, parent);
		
		for ( String k : tabDatas.keySet() ) {
			tabDatas.get(k).uiInspectorTab_i.setParent(scsEnvId, parent);
		}

		uiInspectorHeader	.setAddresses	(tabDatas.get(info).points.toArray(new String[0]));

		for ( String k : tabDatas.keySet() ) {
			final TabData d = tabDatas.get(k);
			logger.debug(className, function, "k[{}] d.tabConfigName[{}] d.points.size[{}]", new Object[]{k, d.tabConfigName, d.points.size()});
			d.uiInspectorTab_i.setAddresses	(d.points		.toArray(new String[0]));
		}

		logger.end(className, function);
	}
	
	@Override
	public void makeTabsBuildWidgets() {
		final String function = "makeTabsBuildWidgets";
		
		for ( String k : tabDatas.keySet() ) {
			TabData d = tabDatas.get(k);
			
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
		
		for ( String k : tabDatas.keySet() ) {
			TabData d = tabDatas.get(k);
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

		for ( String k : tabDatas.keySet() ) {
			TabData d = tabDatas.get(k);
			if ( null != d.uiInspectorTab_i ) {
				d.uiInspectorTab_i.disconnect();
			} else {
				logger.warn(className, function, "uiPanelInspector_i IS NULL");
			}
		}

		logger.end(className, function);
	}
	
	class TabData {
		String tabName = null;
		String tabConfigName = null;
		UIInspectorTab_i uiInspectorTab_i = null;
		Panel panel = null;
		
		boolean isReserveEquipment = false;
		boolean hasSetMessageBoxEvent = false;
		
		List<String> points = null;
		
		List<String> regExpPatternBlackList = null;
		List<String> regExpPatternWhileList = null;
	}
	private Map<String, TabData> tabDatas = null;
	
	private UIInspectorTab_i uiInspectorHeader		= null;

	private TabPanel panelTab = null;

	private Panel panelHeader		= null;

	private TabData getTabData(String tabName, String tabConfigName, String uiInspectorTabName, boolean isReserveEquipment, boolean hasSetMessageBoxEvent) {
		final String function = "getTabData";
		logger.begin(className, function);
		logger.debug(className, function, "tabName[{}] tabConfigName[{}] uiInspectorTabName[{}] isReserveEquipment[{}] hasSetMessageBoxEvent[{}]"
				, new Object[]{tabName, tabConfigName, uiInspectorTabName, isReserveEquipment, hasSetMessageBoxEvent});
		
		TabData d = new TabData();
		
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

	private void loadConfigurationTabAndCreate(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationTabAndCreate";
		logger.begin(className, function);
		
		tabDatas = new LinkedHashMap<String, TabData>();
		
		String keyNumOfTab = prefix+UIPanelInspector_i.strNumOfTab;
		int numOfTab = ReadProp.readInt(dictionariesCacheName, fileName, keyNumOfTab, 0);
		logger.debug(className, function, "numOfTab[{}]", numOfTab);
		
		for ( int i = 0 ; i < numOfTab ; ++i ) {
			
			String prefix2 = prefix+UIPanelInspector_i.strTab+UIPanelInspector_i.strDot+i+UIPanelInspector_i.strDot;
			logger.debug(className, function, "prefix[{}] prefix2[{}]", prefix, prefix2);
			
			String keyTabConfigName = prefix2+UIPanelInspector_i.strTabConfigName;
			String tabConfigName = ReadProp.readString(dictionariesCacheName, fileName, keyTabConfigName, "");
			logger.debug(className, function, "tabConfigName[{}]", tabConfigName);
			
			String keyTabName = prefix2+UIPanelInspector_i.strTabName;
			String tabName = ReadProp.readString(dictionariesCacheName, fileName, keyTabName, "");
			logger.debug(className, function, "tabName[{}]", tabName);
			
			String keyUIInspectorTabName = prefix2+UIPanelInspector_i.strUIInspectorTabName;
			String uiInspectorTabName = ReadProp.readString(dictionariesCacheName, fileName, keyUIInspectorTabName, "");
			logger.debug(className, function, "uiInspectorTabName[{}]", uiInspectorTabName);

			String keyIsReserveEquipment = prefix2+UIPanelInspector_i.strIsReserveEquipment;
			boolean isReserveEquipment = ReadProp.readBoolean(dictionariesCacheName, fileName, keyIsReserveEquipment, false);
			logger.debug(className, function, "isReserveEquipment[{}]", isReserveEquipment);
			
			String keyHasSetMessageBoxEvent = prefix2+UIPanelInspector_i.strHasSetMessageBoxEvent;
			boolean hasSetMessageBoxEvent = ReadProp.readBoolean(dictionariesCacheName, fileName, keyHasSetMessageBoxEvent, false);
			logger.debug(className, function, "hasSetMessageBoxEvent[{}]", hasSetMessageBoxEvent);
			
			tabDatas.put(tabConfigName, getTabData(tabName, tabConfigName, uiInspectorTabName, isReserveEquipment, hasSetMessageBoxEvent));
		}
		
		String keyTabsIgnore = prefix+UIPanelInspector_i.strIgnoreEmptyRemoveTabs;
		ignoreEmptyRemoveTab = ReadProp.readString(dictionariesCacheName, fileName, keyTabsIgnore, "");
		logger.debug(className, function, "ignoreEmptyRemoveTab[{}]", ignoreEmptyRemoveTab);
		
		logger.end(className, function);
	}
	
	private String opmapi = null;
	private String mode = null;
	private Map<String, String> rightNames = null;
	private void loadConfigurationOpm(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationOpm";
		logger.begin(className, function);
		
		// OPM
		String keyopmapi = prefix+UIPanelInspector_i.strOpmApi;
		opmapi = ReadProp.readString(dictionariesCacheName, fileName, keyopmapi, "");
		logger.debug(className, function, "opmapi[{}]", opmapi);
		
		String keymode = prefix+UIPanelInspector_i.strMode;
		logger.debug(className, function, "dictionariesCacheName[{}] fileName[{}] keymode[{}]", new Object[]{dictionariesCacheName, fileName, keymode});
		mode = ReadProp.readString(dictionariesCacheName, fileName, keymode, "");
		logger.debug(className, function, "mode[{}]", mode);
		
		String keyNumOfAction = prefix+UIPanelInspector_i.strNumOfAction;
		int numOfAction = ReadProp.readInt(dictionariesCacheName, fileName, keyNumOfAction, 0);
		logger.debug(className, function, "numOfAction[{}]", numOfAction);
		
		rightNames = new HashMap<String, String>();
		for ( int i = 0 ; i < numOfAction ; ++i ) {
			
			String prefix2 = prefix+UIPanelInspector_i.strAction+UIPanelInspector_i.strDot+i+UIPanelInspector_i.strDot;
			logger.debug(className, function, "prefix[{}] prefix2[{}]", prefix, prefix2);
			
			String keyName = prefix2+UIPanelInspector_i.strName;
			logger.debug(className, function, "dictionariesCacheName[{}] fileName[{}] keymode[{}]", new Object[]{dictionariesCacheName, fileName, keyName});
			String name = ReadProp.readString(dictionariesCacheName, fileName, keyName, "");
			logger.debug(className, function, "name[{}]", name);
			
			String keyAction = prefix2+UIPanelInspector_i.strAction;
			logger.debug(className, function, "dictionariesCacheName[{}] fileName[{}] keymode[{}]", new Object[]{dictionariesCacheName, fileName, keyAction});
			String action = ReadProp.readString(dictionariesCacheName, fileName, keyAction, "");
			logger.debug(className, function, "action[{}]", action);
		
			rightNames.put(name, action);
		}
		
		logger.end(className, function);
	}
	
	private void loadConfigurationDatabase(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationDatabase";
		logger.begin(className, function);
		
		// Database polling
		String keyperiodmillis = prefix+UIPanelInspector_i.strPeriodMillis;
		periodMillis = ReadProp.readInt(dictionariesCacheName, fileName, keyperiodmillis, 250);
		logger.debug(className, function, "database pollor periodMillis[{}]", periodMillis);
		
		logger.end(className, function);
	}	
	private void loadConfigurationEquipmentReserve(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationEquipmentReserve";
		logger.begin(className, function);
		
		// EquipmentReserve
		String keystrEquipmentReserveHasScreen = prefix+UIPanelInspector_i.strEquipmentReserveHasScreen;
		equipmentReserveHasScreen = ReadProp.readBoolean(dictionariesCacheName, fileName, keystrEquipmentReserveHasScreen, false);
		logger.debug(className, function, "equipmentReserveHasScreen[{}]", equipmentReserveHasScreen);
		
		String keystrEquipmentReserveUseHostName = prefix+UIPanelInspector_i.strEquipmentReserveUseHostName;
		equipmentReserveUseHostName = ReadProp.readBoolean(dictionariesCacheName, fileName, keystrEquipmentReserveUseHostName, false);
		logger.debug(className, function, "equipmentReserveUseHostName[{}]", equipmentReserveUseHostName);
		
		String keyStrEquipmentReserveDefaultIndex = prefix+UIPanelInspector_i.strEquipmentReserveDefaultIndex;
		equipmentReserveDefaultIndex = ReadProp.readInt(dictionariesCacheName, fileName, keyStrEquipmentReserveDefaultIndex, 0);
		logger.debug(className, function, "equipmentReserveDefaultIndex[{}]",equipmentReserveDefaultIndex);
		
		logger.end(className, function);
	}	
	private void loadConfigurationHmiOrder(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationHmiOrder";
		logger.begin(className, function);
		
		// hmiOrder
		String strHmiOrderEnable = prefix+UIPanelInspector_i.strHmiOrderEnable;
		hmiOrderEnable = ReadProp.readBoolean(dictionariesCacheName, fileName, strHmiOrderEnable, false);
		logger.debug(className, function, "strHmiOrderEnable[{}] hmiOrderEnable[{}]", strHmiOrderEnable, hmiOrderEnable);
		
		String strHmiOrderAttribute = prefix+UIPanelInspector_i.strHmiOrderAttribute;
		hmiOrderAttribute = ReadProp.readString(dictionariesCacheName, fileName, strHmiOrderAttribute, "");
		logger.debug(className, function, "strHmiOrderAttribute[{}] hmiOrderAttribute[{}]", strHmiOrderAttribute, hmiOrderAttribute);
		
		String strHmiOrderFilterThreshold = prefix+UIPanelInspector_i.strHmiOrderFilterThreshold;
		hmiOrderFilterThreshold = ReadProp.readInt(dictionariesCacheName, fileName, strHmiOrderFilterThreshold, -1);
		logger.debug(className, function, "strHmiOrderFilterThreshold[{}] hmiOrderFilterThreshold[{}]", strHmiOrderFilterThreshold, hmiOrderFilterThreshold);
	
		logger.end(className, function);
	}
	
	private Map<Integer, String> tabHtmls = new HashMap<Integer, String>();
	private Map<String, Boolean> tabHomRights = new HashMap<String, Boolean>();
	private void checkAccessWithHomAndApplyTab(final String action, final int i, final String tabHtml) {
		final String function = "checkAccessWithHomAndApplyTab";
		logger.begin(className, function);
		
		logger.debug(className, function, "scsEnvId[{}] parent[{}]", new Object[]{scsEnvId, parent});
		logger.debug(className, function, "action[{}] i[{}] tabHtml[{}]", new Object[]{action, i, tabHtml});
		
		uiOpm_i.checkAccessWithHom(getFunction(), getLocation(), action, mode, scsEnvId, parent
				, new CheckAccessWithHOMEvent_i() {
			
			@Override
			public void result(boolean result) {
				final String function = "result";
				logger.begin(className, function);
				logger.debug(className, function, "result[{}]", result);
				
				logger.debug(className, function, "i[{}] tabHtml[{}]", i, tabHtml);
				tabHtmls.put(i, tabHtml);
				logger.debug(className, function, "tabHtml[{}] result[{}]", tabHtml, result);
				tabHomRights.put(tabHtml, result);
				
				int selected = panelTab.getTabBar().getSelectedTab();
				logger.debug(className, function, "selected[{}]", selected);
				if ( ! result ) {
					if ( 0 != selected ) {
						logger.debug(className, function, "selectTab to 0");
						panelTab.getTabBar().selectTab(0);
					}
				}
				
				String cssName = "project-gwt-inspector-tabpanel-tab-disable-";

				String cssNameNum = cssName + i;
				if ( ! result ) {
					logger.debug(className, function, "addStyleName cssNameNum[{}]", cssNameNum);
					((Widget)panelTab.getTabBar().getTab(i)).addStyleName(cssNameNum);
				} else {
					logger.debug(className, function, "removeStyleName cssNameNum[{}]", cssNameNum);
					((Widget)panelTab.getTabBar().getTab(i)).removeStyleName(cssNameNum);
				}
				
				logger.end(className, function);
			}
		});
		logger.end(className, function);
	}
	
	private final static String strUIInspectorHeader 			= "UIInspectorHeader";
	
	private final static String info							= "info";

	private final String strUIPanelInspector = "inspectorpanel";
	private final String dictionariesCacheName = UIInspector_i.strUIInspector;
	private final String dictionariesCacheName_fileName = strUIPanelInspector+UIPanelInspector_i.strConfigExtension;
	private final String dictionariesCacheName_prefix = strUIPanelInspector+UIPanelInspector_i.strDot;
	
	@Override
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		
		loadConfigurationTabAndCreate(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationOpm(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationDatabase(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationEquipmentReserve(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationHmiOrder(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		
		prepareOpm();
		
		EquipmentReserve.loadConfiguration();
		
		if ( equipmentReserveUseHostName ) {
			logger.debug(className, function, "equipmentReserveUseHostName[{}] Using HostName as equipmentReserve key", equipmentReserveUseHostName); 
			EquipmentReserve.setReservationName(uiOpm_i.getCurrentHostName());
		}

		uiInspectorHeader	= UIInspectorTabFactory.getInstance().getUIInspectorTabFactory(strUIInspectorHeader);

		if ( logger.isDebugEnabled() ) {
			for ( String k : tabDatas.keySet() ) {
				TabData d = tabDatas.get(k);
				if ( null != d ) { 
					logger.debug(className, function, "k[{}] d.tabName[{}] d.tabConfigName[{}]", new Object[]{k, d.tabName, d.tabConfigName}); 
				} else {
					logger.debug(className, function, "k[{}] IS NULL", k); 
				}
			}
		}

		((UIInspectorHeader)uiInspectorHeader).setEquipmentReserveEvent(new EquipmentReserveEvent() {
			
			@Override
			public void isAvaiable(int eqtReserved) {
				final String function = "isAvaiable";
				logger.begin(className, function);
				if ( null != panelTab ) {
					logger.debug(className, function, "eqtReserved[{}]", eqtReserved);
					int tabCount = panelTab.getTabBar().getTabCount();
					logger.debug(className, function, "tabCount[{}]", tabCount);
					if ( tabCount > 1 ) {
						int selectedIndex = panelTab.getTabBar().getSelectedTab();
						logger.debug(className, function, "selectedIndex[{}]", selectedIndex);
						if ( 2 == eqtReserved || 0 == eqtReserved ) {
							if ( equipmentReserveDefaultIndex != selectedIndex ) {
								logger.debug(className, function, "selectTab to equipmentReserveDefaultIndex[{}]", equipmentReserveDefaultIndex);
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
				logger.end(className, function);
			}
		});
		
		((UIInspectorHeader)uiInspectorHeader).setHomEvent(new HomEvent() {
			
			@Override
			public void isAvaiable(int eqtHom) {
				logger.begin(className, function);
				final String function = "isAvaiable";
				
				logger.debug(className, function, "eqtHom[{}]", eqtHom);
				
				boolean isByPassValue = uiOpm_i.isBypassValue(eqtHom);
				logger.debug(className, function, "eqtHom[{}] isByPassValue[{}]", eqtHom, isByPassValue);

				if ( isByPassValue ) {
					
					logger.debug(className, function, "isByPassValue[{}], Skip the HOM Checking", isByPassValue);
					
				} else {
					
					logger.debug(className, function, "isByPassValue[{}], Start HOM Checking...", isByPassValue);
					
					if ( null != panelTab ) {
						int tabCount = panelTab.getTabBar().getTabCount();
						if ( tabCount > 0 ) {
							for ( int i = 0 ; i < tabCount ; ++i ) {
								
								String tabHtml = panelTab.getTabBar().getTabHTML(i);
								logger.debug(className, function, "i[{}] title[{}]", i, tabHtml);
								
								for ( String k : tabDatas.keySet() ) {
									TabData d = tabDatas.get(k);
									logger.debug(className, function, "d.tabName[{}] == tabHtml[{}]", d.tabName, tabHtml);
									if ( d.tabName.equals(tabHtml) ) {
										String tabConfigName = d.tabConfigName;
										String action = rightNames.get(tabConfigName);
										logger.debug(className, function, "tabConfigName[{}] action[{}]", tabConfigName, action);
										checkAccessWithHomAndApplyTab(action, i, tabHtml);
									}
								}
							}
						}
					}
				}

				logger.end(className, function);
			}
		});
		
		for ( String k : tabDatas.keySet() ) {
			final TabData d = tabDatas.get(k);
			d.uiInspectorTab_i.setUIInspectorTabClickEvent(new UIInspectorTabClickEvent() {
				
				@Override
				public void onClick() {
					logger.debug(className, function, "d.tabConfigName[{}]", d.tabConfigName );
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
		uiInspectorHeader.setEquipmentReserveHasScreen(equipmentReserveHasScreen);
		uiInspectorHeader.init();
		uiInspectorHeader.setDatabase(database);
		panelHeader	= uiInspectorHeader.getMainPanel();
		
		for ( String k : tabDatas.keySet() ) {
			TabData d = tabDatas.get(k);
			
			d.uiInspectorTab_i.setUINameCard(this.uiNameCard);
			d.uiInspectorTab_i.setEquipmentReserveHasScreen(equipmentReserveHasScreen);
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
				logger.debug(className, function, "event[{}] event.getItem()[{}]", event, event.getItem());
				int index = event.getItem().intValue();
				logger.debug(className, function, "index[{}]", index);
				
				boolean homInsufficientRight = false;
				String tabHtml = tabHtmls.get(index);
				logger.debug(className, function, "tabHtml[{}]", tabHtml);
				if ( null != tabHtml ) {
					if ( null != tabHomRights.get(tabHtml) ) {
						boolean homRight = tabHomRights.get(tabHtml);
						logger.debug(className, function, "tabHtml[{}] homRight[{}]", tabHtml, homRight);
						if ( ! homRight ) {
							event.cancel();
							logger.debug(className, function, "homRight[{}] IS FASLE, Event Cancelled", homRight);
							homInsufficientRight = true;
						}
					}
				}
				logger.debug(className, function, "homInsufficientRight[{}]", homInsufficientRight);
				
				
				// Equipment
				if ( equipmentReserveDefaultIndex == index ) {
					unReserveEquipment();
				} else {
					if ( !homInsufficientRight ) {
						int eqtReservedValue = ((UIInspectorHeader)uiInspectorHeader).getEqtReservedValue();
						logger.debug(className, function, "eqtReservedValue[{}]", eqtReservedValue);
						if ( 2 == eqtReservedValue ) {
							logger.debug(className, function, "eqtReservedValue equals to 2, Cancel event...");
							event.cancel();
						} else {
							logger.debug(className, function, "eqtReservedValue not equals to 2, reserve equipment...");
							reserveEquipment();
						}
					}
				}

						
				logger.end(className, function);
			}
		});
		
		panelTab.addStyleName("project-gwt-tabpanel-inspector-tabpanel");

		for ( String k : tabDatas.keySet() ) {
			TabData d = tabDatas.get(k);
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
		EquipmentReserve.equipmentReservation(scsEnvId, parent, database, equipmentReserveHasScreen, uiNameCard.getUiScreen());
		logger.end(className, function);
	}
	
	private void unReserveEquipment() {
		final String function = "unReserveEquipment";
		logger.begin(className, function);
		EquipmentReserve.equipmentUnreservation(scsEnvId, parent, database, equipmentReserveHasScreen, uiNameCard.getUiScreen());
		logger.end(className, function);
	}

}
