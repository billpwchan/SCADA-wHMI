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
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
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

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

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
	
	final private String INSPECTOR = UIInspector_i.INSPECTOR;
	
	private final static String strCssPrefixTabDisable = "project-gwt-inspector-tabpanel-tab-disable-";
	
	private String containTabDisableCss(Widget widget) {
		final String function = "containTabDisableCss";
		logger.begin(function);
		String ret = null;
		String target = strCssPrefixTabDisable;
		logger.trace(function, "addStyleName target[{}]", target);
		String cssName = widget.getStyleName();
		if ( null != cssName ) {
			logger.trace(function, "cssName[{}]", cssName);
			String [] cssNames = cssName.split(" ");
			if ( null != cssNames ) {
				for ( int i = 0 ; i < cssNames.length ; ++i ) {
					logger.trace(function, "cssNames({})[{}]", i, cssNames[i]);
					if ( cssNames[i].startsWith(target) ) {
						ret = cssNames[i];
						break;
					}
				}	
			} else {
				logger.warn(function, "cssNames IS NULL");
			}
		} else {
			logger.warn(function, "cssName IS NULL");
		}
		logger.trace(function, "ret[{}]", ret);
		logger.end(function);
		return ret;
	}
	
	private void modifyTabDisableCss(Widget widget, int index, boolean isAdd) {
		final String function = "modifyTabDisableCss";
		logger.begin(function);
		String cssNameNum = strCssPrefixTabDisable+index;
		logger.trace(function, "StyleName cssNameNum[{}] isAdd[{}]", cssNameNum, isAdd);
		if ( isAdd ) {
			widget.addStyleName(cssNameNum);
		} else {
			String cssToRemove = containTabDisableCss(widget);
			logger.trace(function, "cssToRemove[{}]", cssToRemove);
			if ( null != cssToRemove ) {
				widget.removeStyleName(cssToRemove);
			}
		}
		logger.end(function);
	}

	public void setPeriodMillis(int periodMillis) {
		this.periodMillis = periodMillis;
	}
	
	private Database database = new Database();

	@Override
	public void setParent(String scsEnvId, String parent) {
		final String function = "setParent";
		
		logger.begin(function);

		this.scsEnvId = scsEnvId;
		this.parent = parent;
		
		logger.debug(function, "this.scsEnvId[{}] this.parent[{}]", this.scsEnvId, this.parent);
		
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
		
		logger.end(function);
	}
	
	private String location = null;
	public String getLocation() { return this.location; }
	public void setLocation(String location) { this.location = location; }
	
	private String function = null;
	public String getFunction() { return this.function; }
	public void setFunction(String function) { this.function = function; }
	
	private boolean isIgnoreEmptyRemoveTab(String key) {
		final String function = "isIgnoreEmptyRemoveTab";
		logger.begin(function);
		logger.debug(function, "key[{}] ignoreEmptyRemoveTab[{}]", key, ignoreEmptyRemoveTab);
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
		logger.debug(function, "key[{}] ret[{}]", key, ret);
		logger.end(function);
		return ret;
	}
	
	private void connectTabs(String[] dbaddress) {
		final String function = "connectTabs";
		logger.begin(function);
		
		buildTabsAddress(dbaddress);
		makeTabsSetAddress();
		makeTabsBuildWidgets();
		makeTabsConnect();
		
		uiInspectorHeader.connect();

		for ( String k : tabDatas.keySet() ) {
			TabData d = tabDatas.get(k);
			if ( d.points.isEmpty() ) {
				if ( isIgnoreEmptyRemoveTab(k) ) {
					logger.debug(function, "k[{}] d.points.isEmpty() but in ignore list, ignore it", k);
				}
				else {
					logger.debug(function, "k[{}] d.points.isEmpty() Remove it", k);
					panelTab.remove(d.panel);
				}
			}
		}

		logger.end(function);
	}

	private void responseGetChilden(String key, String[] values) {
		final String function = "responseGetChilden";
		logger.begin(function);
		
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

		logger.end(function);
	}
	
	private void requestDynamic() {
		final String function = "requestDynamic";
		logger.begin(function);
		
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
					logger.begin(function);
					logger.trace(function, "{} key[{}]", new Object[]{function2, key});

					String [] dbaddresses	= database.getKeyAndAddress(key);
					String [] dbvalues		= database.getKeyAndValues(key);
					if (dbaddresses == null || dbvalues == null) {
						logger.error(function, "{} dbaddresses or dbvalues is null", function2);
						return;
					}
					if (logger.isTraceEnabled()) {
						for (int i=0; i<dbaddresses.length; i++) {
							logger.trace(function, "{} dbaddresses[{}]=[{}]", new Object[]{function2, i, dbaddresses[i]});
						}
						for (int i=0; i<dbvalues.length; i++) {
							logger.trace(function, "{} dbvalues[{}]=[{}]", new Object[]{function2, i, dbvalues[i]});
						}
						for (int i=0; i<value.length; i++) {
							logger.trace(function, "{} value[{}]=[{}]", new Object[]{function2, i, value[i]});
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
					
					logger.end(function);
				}
			});
		} else {
			logger.warn(function, "database IS NUL");
		}
		logger.end(function);
	}
	
	private void requestGetChilden() {
		final String function = "requestGetChilden";
		logger.begin(function);
		
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
			logger.warn(function, "database IS NULL");
		}

		logger.end(function);
	}
	
	private void repsonseHeader(String key, String[] values) {
		final String function = "repsonseHeader";
		logger.begin(function);
		
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

		logger.end(function);
	}
	
	private void requestHeader() {
		final String function = "requestHeader";
		logger.begin(function);
		
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
		
		if ( logger.isTraceEnabled() ) {
			logger.trace(function, "strClientKey[{}] scsEnvId[{}]", strClientKey, scsEnvId);
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.trace(function, "dbaddresses({})[{}]", i, dbaddresses[i]);
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
				logger.warn(function, "database IS NUL");
		}
		
		logger.end(function);
	}
	
	
	private UIOpm_i uiOpm_i = null;
	private void prepareOpm() {
		final String function = "prepareOpm";
		logger.begin(function);
		// OPM
		logger.debug(function, "opmapi[{}]", opmapi);
		uiOpm_i = OpmMgr.getInstance().getOpm(opmapi);
		if ( null == uiOpm_i ) {
			logger.warn(function, "uiOpm_i IS NULL");
		}
		logger.end(function);
	}

	private void checkRights() {
		final String function = "checkRights";
		logger.begin(function);
		
		if ( null != uiOpm_i ) {

			rights = new HashMap<String, Boolean>();
			for ( String k : rightNames.keySet() ) {
				String action = rightNames.get(k);
				logger.debug(function, "k[{}] action[{}]", k, action);
				boolean right = uiOpm_i.checkAccess(this.function, this.location, action, mode);
				logger.debug(function, "k[{}] action[{}] right[{}]", new Object[]{k, action, right});
				rights.put(k, right);
			}

			for ( String k : tabDatas.keySet() ) {
				TabData d = tabDatas.get(k);
				d.uiInspectorTab_i.setRight(rights);
				d.uiInspectorTab_i.applyRight();
				if ( null != rights.get(k) && ! rights.get(k) ) {
					logger.warn(function, "k[{}] right, remove page", k);
					panelTab.remove(d.panel);
				}
			}

		} else {
			logger.warn(function, "uiOpm_i IS NULL");
		}
		logger.end(function);
	}
	
	private Map<String, Boolean> rights = null;
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(function);
		
		database.connect();
		database.connectTimer(this.periodMillis);
		
		checkRights();
		
		requestGetChilden();

		requestHeader();
		
		logger.end(function);
	}
	
	@Override
	public void disconnect() {
		final String function = "disconnect";
		
		logger.begin(function);
		
		// Unrerves equipment
		unReserveEquipment();
		
		makeTabsDisconnect();
		
		uiInspectorHeader.disconnect();
		
		database.disconnectTimer();
		database.disconnect();

		logger.end(function);
	}

	private TextBox txtMsg = null;
	@Override
	public void buildTabsAddress(String[] instances) {
		final String function = "buildTabsAddress";
		logger.begin(function);
		
		String dictionariesCacheName = "UIInspectorPanel";
		
		for ( String k : tabDatas.keySet() ) {
			TabData d = tabDatas.get(k);
			String tab = d.tabConfigName;
			logger.begin(function+" "+tab);
			
			d.regExpPatternBlackList = new LinkedList<String>();
			
			PointsFilter.prepareFilter(d.regExpPatternBlackList, dictionariesCacheName, tab, UIPanelInspector_i.strConfigNameBackList);
			
			d.regExpPatternWhileList = new LinkedList<String>();
			
			PointsFilter.prepareFilter(d.regExpPatternWhileList, dictionariesCacheName, tab, UIPanelInspector_i.strConfigNameWhileList);

			logger.end(function+" "+tab);
		}

		logger.end(function + " getLists");

		logger.debug(function,"Iterator Begin");
		
		for ( String dbaddress : instances ) {
			logger.debug(function,"Iterator dbaddress[{}]", dbaddress);
			if ( null != dbaddress ) {
				logger.debug(function, "Iterator dbaddress[{}]", dbaddress);
				for ( String k : tabDatas.keySet() ) {
					TabData d = tabDatas.get(k);
					PointsFilter.applyFiltedList(d.points, d.regExpPatternBlackList, d.regExpPatternWhileList, dbaddress);
					
					if ( logger.isDebugEnabled() ) {
						logger.debug(function, "k[{}] d.tabConfigName[{}] d.points.size()[{}]"
								, new Object[]{k, d.tabConfigName, d.points.size()});
						logger.debug(function, "");
						for ( String dba : d.points ) {
							logger.debug(function, "d.tabName[{}] dba[{}]", d.tabName, dba);
						}
					}	

				}
			} else {
				logger.warn(function, "Iterator dbaddress IS NULL");
			}
		}
		logger.end(function);
	}
	
	@Override
	public void makeTabsSetAddress() {
		final String function = "makeTabsSetAddress";
		
		logger.begin(function);
		
		if ( logger.isDebugEnabled() ) {
			for ( String k : tabDatas.keySet() ) {
				TabData d = tabDatas.get(k);
				logger.debug(function, "k[{}] d.tabConfigName[{}] d.points.size[{}]", new Object[]{k, d.tabConfigName, d.points.size()});
				for ( String dbaddress : d.points ) {
					logger.debug(function, "points[{}] dbaddress[{}]", d.tabName, dbaddress);
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
			logger.debug(function, "k[{}] d.tabConfigName[{}] d.points.size[{}]", new Object[]{k, d.tabConfigName, d.points.size()});
			d.uiInspectorTab_i.setAddresses	(d.points		.toArray(new String[0]));
		}

		logger.end(function);
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
				logger.warn(function, " uiPanelInspector_i IS NULL");
			}
		}

		logger.end(function);
	}
	
	@Override
	public void makeTabsConnect() {
		final String function = "makeTabsConnect";
		logger.begin(function);
		
		for ( String k : tabDatas.keySet() ) {
			TabData d = tabDatas.get(k);
			if ( null != d.uiInspectorTab_i ) {
				d.uiInspectorTab_i.connect();
			} else {
				logger.warn(function, "uiPanelInspector_i IS NULL");
			}
		}

		logger.end(function);
	}

	@Override
	public void makeTabsDisconnect() {
		final String function = "makeTabsDisconnect";
		logger.begin(function);

		for ( String k : tabDatas.keySet() ) {
			TabData d = tabDatas.get(k);
			if ( null != d.uiInspectorTab_i ) {
				d.uiInspectorTab_i.disconnect();
			} else {
				logger.warn(function, "uiPanelInspector_i IS NULL");
			}
		}

		logger.end(function);
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
		logger.begin(function);
		logger.debug(function, "tabName[{}] tabConfigName[{}] uiInspectorTabName[{}] isReserveEquipment[{}] hasSetMessageBoxEvent[{}]"
				, new Object[]{tabName, tabConfigName, uiInspectorTabName, isReserveEquipment, hasSetMessageBoxEvent});
		
		TabData d = new TabData();
		
		d.tabName = tabName;
		d.tabConfigName = tabConfigName;
		d.uiInspectorTab_i = UIInspectorTabFactory.getInstance().getUIInspectorTabFactory(uiInspectorTabName);
		d.uiInspectorTab_i.setTabName(d.tabConfigName);
		
		d.isReserveEquipment = isReserveEquipment;
		d.hasSetMessageBoxEvent = hasSetMessageBoxEvent;
		
		d.points = new LinkedList<String>();
		logger.end(function);
		return d;
	}

	private void loadConfigurationTabAndCreate(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationTabAndCreate";
		logger.begin(function);
		
		tabDatas = new LinkedHashMap<String, TabData>();
		
		String keyNumOfTab = prefix+UIPanelInspector_i.strNumOfTab;
		int numOfTab = ReadProp.readInt(dictionariesCacheName, fileName, keyNumOfTab, 0);
		logger.debug(function, "numOfTab[{}]", numOfTab);
		
		for ( int i = 0 ; i < numOfTab ; ++i ) {
			
			String prefix2 = prefix+UIPanelInspector_i.strTab+UIPanelInspector_i.strDot+i+UIPanelInspector_i.strDot;
			logger.debug(function, "prefix[{}] prefix2[{}]", prefix, prefix2);
			
			String keyTabConfigName = prefix2+UIPanelInspector_i.strTabConfigName;
			String tabConfigName = ReadProp.readString(dictionariesCacheName, fileName, keyTabConfigName, "");
			logger.debug(function, "tabConfigName[{}]", tabConfigName);
			
			String keyTabName = prefix2+UIPanelInspector_i.strTabName;
			String tabName = ReadProp.readString(dictionariesCacheName, fileName, keyTabName, "");
			logger.debug(function, "tabName[{}]", tabName);
			
			String keyUIInspectorTabName = prefix2+UIPanelInspector_i.strUIInspectorTabName;
			String uiInspectorTabName = ReadProp.readString(dictionariesCacheName, fileName, keyUIInspectorTabName, "");
			logger.debug(function, "uiInspectorTabName[{}]", uiInspectorTabName);

			String keyIsReserveEquipment = prefix2+UIPanelInspector_i.strIsReserveEquipment;
			boolean isReserveEquipment = ReadProp.readBoolean(dictionariesCacheName, fileName, keyIsReserveEquipment, false);
			logger.debug(function, "isReserveEquipment[{}]", isReserveEquipment);
			
			String keyHasSetMessageBoxEvent = prefix2+UIPanelInspector_i.strHasSetMessageBoxEvent;
			boolean hasSetMessageBoxEvent = ReadProp.readBoolean(dictionariesCacheName, fileName, keyHasSetMessageBoxEvent, false);
			logger.debug(function, "hasSetMessageBoxEvent[{}]", hasSetMessageBoxEvent);
			
			tabDatas.put(tabConfigName, getTabData(tabName, tabConfigName, uiInspectorTabName, isReserveEquipment, hasSetMessageBoxEvent));
		}
		
		String keyTabsIgnore = prefix+UIPanelInspector_i.strIgnoreEmptyRemoveTabs;
		ignoreEmptyRemoveTab = ReadProp.readString(dictionariesCacheName, fileName, keyTabsIgnore, "");
		logger.debug(function, "ignoreEmptyRemoveTab[{}]", ignoreEmptyRemoveTab);
		
		logger.end(function);
	}
	
	private String opmapi = null;
	private String mode = null;
	private Map<String, String> rightNames = null;
	private void loadConfigurationOpm(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationOpm";
		logger.begin(function);
		
		// OPM
		String keyopmapi = prefix+UIPanelInspector_i.strOpmApi;
		opmapi = ReadProp.readString(dictionariesCacheName, fileName, keyopmapi, "");
		logger.debug(function, "opmapi[{}]", opmapi);
		
		String keymode = prefix+UIPanelInspector_i.strMode;
		logger.debug(function, "dictionariesCacheName[{}] fileName[{}] keymode[{}]", new Object[]{dictionariesCacheName, fileName, keymode});
		mode = ReadProp.readString(dictionariesCacheName, fileName, keymode, "");
		logger.debug(function, "mode[{}]", mode);
		
		String keyNumOfAction = prefix+UIPanelInspector_i.strNumOfAction;
		int numOfAction = ReadProp.readInt(dictionariesCacheName, fileName, keyNumOfAction, 0);
		logger.debug(function, "numOfAction[{}]", numOfAction);
		
		rightNames = new HashMap<String, String>();
		for ( int i = 0 ; i < numOfAction ; ++i ) {
			
			String prefix2 = prefix+UIPanelInspector_i.strAction+UIPanelInspector_i.strDot+i+UIPanelInspector_i.strDot;
			logger.debug(function, "prefix[{}] prefix2[{}]", prefix, prefix2);
			
			String keyName = prefix2+UIPanelInspector_i.strName;
			logger.debug(function, "dictionariesCacheName[{}] fileName[{}] keymode[{}]", new Object[]{dictionariesCacheName, fileName, keyName});
			String name = ReadProp.readString(dictionariesCacheName, fileName, keyName, "");
			logger.debug(function, "name[{}]", name);
			
			String keyAction = prefix2+UIPanelInspector_i.strAction;
			logger.debug(function, "dictionariesCacheName[{}] fileName[{}] keymode[{}]", new Object[]{dictionariesCacheName, fileName, keyAction});
			String action = ReadProp.readString(dictionariesCacheName, fileName, keyAction, "");
			logger.debug(function, "action[{}]", action);
		
			rightNames.put(name, action);
		}
		
		logger.end(function);
	}
	
	private void loadConfigurationDatabase(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationDatabase";
		logger.begin(function);
		
		// Database polling
		String keyperiodmillis = prefix+UIPanelInspector_i.strPeriodMillis;
		periodMillis = ReadProp.readInt(dictionariesCacheName, fileName, keyperiodmillis, 250);
		logger.debug(function, "database pollor periodMillis[{}]", periodMillis);
		
		logger.end(function);
	}	
	private void loadConfigurationEquipmentReserve(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationEquipmentReserve";
		logger.begin(function);
		
		// EquipmentReserve
		String keystrEquipmentReserveHasScreen = prefix+UIPanelInspector_i.strEquipmentReserveHasScreen;
		equipmentReserveHasScreen = ReadProp.readBoolean(dictionariesCacheName, fileName, keystrEquipmentReserveHasScreen, false);
		logger.debug(function, "equipmentReserveHasScreen[{}]", equipmentReserveHasScreen);
		
		String keystrEquipmentReserveUseHostName = prefix+UIPanelInspector_i.strEquipmentReserveUseHostName;
		equipmentReserveUseHostName = ReadProp.readBoolean(dictionariesCacheName, fileName, keystrEquipmentReserveUseHostName, false);
		logger.debug(function, "equipmentReserveUseHostName[{}]", equipmentReserveUseHostName);
		
		String keyStrEquipmentReserveDefaultIndex = prefix+UIPanelInspector_i.strEquipmentReserveDefaultIndex;
		equipmentReserveDefaultIndex = ReadProp.readInt(dictionariesCacheName, fileName, keyStrEquipmentReserveDefaultIndex, 0);
		logger.debug(function, "equipmentReserveDefaultIndex[{}]",equipmentReserveDefaultIndex);
		
		logger.end(function);
	}	
	private void loadConfigurationHmiOrder(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationHmiOrder";
		logger.begin(function);
		
		// hmiOrder
		String strHmiOrderEnable = prefix+UIPanelInspector_i.strHmiOrderEnable;
		hmiOrderEnable = ReadProp.readBoolean(dictionariesCacheName, fileName, strHmiOrderEnable, false);
		logger.debug(function, "strHmiOrderEnable[{}] hmiOrderEnable[{}]", strHmiOrderEnable, hmiOrderEnable);
		
		String strHmiOrderAttribute = prefix+UIPanelInspector_i.strHmiOrderAttribute;
		hmiOrderAttribute = ReadProp.readString(dictionariesCacheName, fileName, strHmiOrderAttribute, "");
		logger.debug(function, "strHmiOrderAttribute[{}] hmiOrderAttribute[{}]", strHmiOrderAttribute, hmiOrderAttribute);
		
		String strHmiOrderFilterThreshold = prefix+UIPanelInspector_i.strHmiOrderFilterThreshold;
		hmiOrderFilterThreshold = ReadProp.readInt(dictionariesCacheName, fileName, strHmiOrderFilterThreshold, -1);
		logger.debug(function, "strHmiOrderFilterThreshold[{}] hmiOrderFilterThreshold[{}]", strHmiOrderFilterThreshold, hmiOrderFilterThreshold);
	
		logger.end(function);
	}
	
	private Map<Integer, String> tabHtmls = new HashMap<Integer, String>();
	private Map<String, Boolean> tabHomRights = new HashMap<String, Boolean>();
	private void checkAccessWithHomAndApplyTab(final String action, final int index, final String tabHtml) {
		final String function = "checkAccessWithHomAndApplyTab";
		logger.begin(function);
		
		logger.debug(function, "scsEnvId[{}] parent[{}]", new Object[]{scsEnvId, parent});
		logger.debug(function, "action[{}] index[{}] tabHtml[{}]", new Object[]{action, index, tabHtml});
		
		uiOpm_i.checkAccessWithHom(getFunction(), getLocation(), action, mode, scsEnvId, parent
				, new CheckAccessWithHOMEvent_i() {
			
			@Override
			public void result(boolean result) {
				final String function = "checkAccessWithHom result";
				logger.begin(function);
				logger.debug(function, "result[{}]", result);
				
				logger.debug(function, "index[{}] tabHtml[{}]", index, tabHtml);
				tabHtmls.put(index, tabHtml);
				logger.debug(function, "tabHtml[{}] result[{}]", tabHtml, result);
				tabHomRights.put(tabHtml, result);
				
				int selected = panelTab.getTabBar().getSelectedTab();
				logger.debug(function, "selected[{}]", selected);
				if ( ! result ) {
					if ( 0 != selected ) {
						logger.debug(function, "selectTab to 0");
						panelTab.getTabBar().selectTab(0);
					}
				}
				
				if ( ! result ) {
					modifyTabDisableCss((Widget)panelTab.getTabBar().getTab(index), index, true);
				} else {
					modifyTabDisableCss((Widget)panelTab.getTabBar().getTab(index), index, false);
				}
				
				logger.end(function);
			}
		});
		logger.end(function);
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
		logger.begin(function);
		
		loadConfigurationTabAndCreate(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationOpm(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationDatabase(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationEquipmentReserve(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationHmiOrder(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		
		prepareOpm();
		
		EquipmentReserve.loadConfiguration();
		
		if ( equipmentReserveUseHostName ) {
			logger.debug(function, "equipmentReserveUseHostName[{}] Using HostName as equipmentReserve key", equipmentReserveUseHostName); 
			EquipmentReserve.setReservationName(uiOpm_i.getCurrentHostName());
		}

		uiInspectorHeader	= UIInspectorTabFactory.getInstance().getUIInspectorTabFactory(strUIInspectorHeader);
		
		uiInspectorHeader.setTabName("header");

		if ( logger.isDebugEnabled() ) {
			for ( String k : tabDatas.keySet() ) {
				TabData d = tabDatas.get(k);
				if ( null != d ) { 
					logger.debug(function, "k[{}] d.tabName[{}] d.tabConfigName[{}]", new Object[]{k, d.tabName, d.tabConfigName}); 
				} else {
					logger.debug(function, "k[{}] IS NULL", k); 
				}
			}
		}

		((UIInspectorHeader)uiInspectorHeader).setEquipmentReserveEvent(new EquipmentReserveEvent() {
			
			@Override
			public void isAvaiable(int eqtReserved) {
				final String function = "isAvaiable";
				logger.begin(function);
				if ( null != panelTab ) {
					logger.debug(function, "eqtReserved[{}]", eqtReserved);
					int tabCount = panelTab.getTabBar().getTabCount();
					logger.debug(function, "tabCount[{}]", tabCount);
					if ( tabCount > 1 ) {
						int selectedIndex = panelTab.getTabBar().getSelectedTab();
						logger.debug(function, "selectedIndex[{}]", selectedIndex);
						if ( 2 == eqtReserved || 0 == eqtReserved ) {
							if ( equipmentReserveDefaultIndex != selectedIndex ) {
								logger.debug(function, "selectTab to equipmentReserveDefaultIndex[{}]", equipmentReserveDefaultIndex);
								panelTab.getTabBar().selectTab(0);
							}
						}

						for ( int index = 1 ; index < tabCount ; ++index ) {
							if ( 2 == eqtReserved ) {
								modifyTabDisableCss((Widget)panelTab.getTabBar().getTab(index), index, true);
							} else {
								modifyTabDisableCss((Widget)panelTab.getTabBar().getTab(index), index, false);
							}
						}
					}
				}
				logger.end(function);
			}
		});
		
		((UIInspectorHeader)uiInspectorHeader).setHomEvent(new HomEvent() {
			
			@Override
			public void isAvaiable(int eqtHom) {
				logger.begin(function);
				final String function = "isAvaiable";
				
				logger.debug(function, "eqtHom[{}]", eqtHom);
				
				boolean isByPassValue = uiOpm_i.isBypassValue(eqtHom);
				logger.debug(function, "eqtHom[{}] isByPassValue[{}]", eqtHom, isByPassValue);

				if ( isByPassValue ) {
					
					logger.debug(function, "isByPassValue[{}], Skip the HOM Checking", isByPassValue);
					
				} else {
					
					logger.debug(function, "isByPassValue[{}], Start HOM Checking...", isByPassValue);
					
					if ( null != panelTab ) {
						int tabCount = panelTab.getTabBar().getTabCount();
						if ( tabCount > 0 ) {
							for ( int i = 0 ; i < tabCount ; ++i ) {
								
								String tabHtml = panelTab.getTabBar().getTabHTML(i);
								logger.debug(function, "i[{}] title[{}]", i, tabHtml);
								
								for ( String k : tabDatas.keySet() ) {
									TabData d = tabDatas.get(k);
									logger.debug(function, "d.tabName[{}] == tabHtml[{}]", d.tabName, tabHtml);
									if ( d.tabName.equals(tabHtml) ) {
										String tabConfigName = d.tabConfigName;
										String action = rightNames.get(tabConfigName);
										logger.debug(function, "tabConfigName[{}] action[{}]", tabConfigName, action);
										checkAccessWithHomAndApplyTab(action, i, tabHtml);
									}
								}
							}
						}
					}
				}

				logger.end(function);
			}
		});
		
		for ( String k : tabDatas.keySet() ) {
			final TabData d = tabDatas.get(k);
			d.uiInspectorTab_i.setUIInspectorTabClickEvent(new UIInspectorTabClickEvent() {
				
				@Override
				public void onClick() {
					logger.debug(function, "d.tabConfigName[{}]", d.tabConfigName );
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
						logger.debug(function, "setMessage message[{}]", message);
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
						logger.debug(function, "setMessage message[{}]", message);
						String text = txtMsg.getText();
						logger.debug(function, "setMessage text[{}]", text);
						if ( text.length() > 0 ) text += "\n";
						logger.debug(function, "setMessage text + message[{}]", text + message);
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
				logger.begin(function);
				logger.debug(function, "event[{}] event.getItem()[{}]", event, event.getItem());
				int index = event.getItem().intValue();
				logger.debug(function, "index[{}]", index);
				
				boolean homInsufficientRight = false;
				String tabHtml = tabHtmls.get(index);
				logger.debug(function, "tabHtml[{}]", tabHtml);
				if ( null != tabHtml ) {
					if ( null != tabHomRights.get(tabHtml) ) {
						boolean homRight = tabHomRights.get(tabHtml);
						logger.debug(function, "tabHtml[{}] homRight[{}]", tabHtml, homRight);
						if ( ! homRight ) {
							event.cancel();
							logger.debug(function, "homRight[{}] IS FASLE, Event Cancelled", homRight);
							homInsufficientRight = true;
						}
					}
				}
				logger.debug(function, "homInsufficientRight[{}]", homInsufficientRight);
				
				
				// Equipment
				if ( equipmentReserveDefaultIndex == index ) {
					unReserveEquipment();
				} else {
					if ( !homInsufficientRight ) {
						int eqtReservedValue = ((UIInspectorHeader)uiInspectorHeader).getEqtReservedValue();
						logger.debug(function, "eqtReservedValue[{}]", eqtReservedValue);
						if ( 2 == eqtReservedValue ) {
							logger.debug(function, "eqtReservedValue equals to 2, Cancel event...");
							event.cancel();
						} else {
							logger.debug(function, "eqtReservedValue not equals to 2, reserve equipment...");
							reserveEquipment();
						}
					}
				}

				String cssApplied = containTabDisableCss((Widget)panelTab.getTabBar().getTab(index));
				if ( null != cssApplied ) {
					logger.debug(function, "cssNames[{}] FOUND, Skip SELECTION EVENT", cssApplied);
					event.cancel();
				} else {
					logger.debug(function, "cssNames IS NOT FOUND, continue");
				}

				logger.end(function);
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
		
		// Disable the tab by default
		if ( null != panelTab ) {
			int tabCount = panelTab.getTabBar().getTabCount();
			logger.debug(function, "tabCount[{}]", tabCount);
			if ( tabCount > 1 ) {
				for ( int index = 1 ; index < tabCount ; ++index ) {
					modifyTabDisableCss((Widget)panelTab.getTabBar().getTab(index), index, true);
				}
			}
		} else {
			logger.warn(function, "panelTab IS NULL, bypass dissable tab");
		}

		logger.end(function);
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
		logger.begin(function);
		onClose();
		logger.end(function);
	}
	
	private void reserveEquipment() {
		final String function = "reserveEquipment";
		logger.begin(function);
		EquipmentReserve.equipmentReservation(scsEnvId, parent, database, equipmentReserveHasScreen, uiNameCard.getUiScreen());
		logger.end(function);
	}
	
	private void unReserveEquipment() {
		final String function = "unReserveEquipment";
		logger.begin(function);
		EquipmentReserve.equipmentUnreservation(scsEnvId, parent, database, equipmentReserveHasScreen, uiNameCard.getUiScreen());
		logger.end(function);
	}

}
