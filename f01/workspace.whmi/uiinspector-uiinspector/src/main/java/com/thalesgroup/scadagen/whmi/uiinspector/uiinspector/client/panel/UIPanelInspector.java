package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorAdvance;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorControl;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorInfo;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorTag;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.EquipmentReserve;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.EquipmentReserveEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.ReadProp;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;

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
	
	private final String inspPropPrefix = "inspectorpanel.";
	private final String inspProp = inspPropPrefix+"properties";

	private String scsEnvId		= null;
	private String parent		= null;
	private int periodMillis	= 250;
	
	public void setPeriodMillis(int periodMillis) {
		this.periodMillis = periodMillis;
	}
	
	private Database database = Database.getInstance();

	@Override
	public void setParent(String scsEnvId, String parent) {
		final String function = "setParent";
		
		logger.begin(className, function);

		this.scsEnvId = scsEnvId;
		this.parent = parent;
		
		logger.info(className, function, "this.scsEnvId[{}]", this.scsEnvId);
		logger.info(className, function, "this.parent[{}]", this.parent);
		
		database.setDynamic(scsEnvId, parent);
		
		logger.end(className, function);
	}

	@Override
	public void connect() {
		final String function = "connect";

		periodMillis = ReadProp.readInt(UIInspector_i.strUIInspector, inspProp, inspPropPrefix+"periodMillis", 250);
		
		logger.info(className, function, "database pollor periodMillis[{}]", periodMillis);
		
		logger.begin(className, function);
		Database database = Database.getInstance();
		database.connect();
		database.connectTimer(this.periodMillis);

		{
			logger.begin(className, function+" GetChildren");

			String clientKey = "GetChildren" + "_" + "inspector" + "_" + "static" + "_" + parent;

			String api = "GetChildren";
			String dbaddress = parent;
			database.addStaticRequest(api, clientKey, scsEnvId, dbaddress, new DatabaseEvent() {
				
				@Override
				public void update(String key, String[] values) {
					
					Database database = Database.getInstance();
					
					{
						String clientKey_GetChildren_inspector_static = "GetChildren" + "_" + "inspector" + "_" + "static" + "_" + parent;
						if ( 0 == clientKey_GetChildren_inspector_static.compareTo(key) ) {
							buildTabsAddress(values);
							makeTabsSetAddress();
							makeTabsBuildWidgets();
							makeTabsConnect();
							
							((UIInspectorHeader)			uiInspectorHeader)	.connect();
							((UIInspectorEquipmentReserve)	equipmentReserve)	.connect();
							
							if ( infos.size() <= 0 )		panelTab.remove(panelInfo);
							if ( controls.size() <= 0 )		panelTab.remove(panelCtrl);
							if ( tags.size() <= 0 )			panelTab.remove(panelTag);
							if ( advances.size() <= 0 )		panelTab.remove(panelAdv);
				
						}			
					}
					
					{
						database.setDynamicEvent(new DatabaseEvent() {
							
							@Override
							public void update(String key, String[] value) {
								
								Database database = Database.getInstance();
								
								String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "dynamic" + "_" + parent;
								
								String [] dbaddresses	= database.getKeyAndAddress(clientKey);
								String [] dbvalues		= database.getKeyAndValues(clientKey);
								
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

					}

				}
			});
			
			logger.end(className, function+" GetChildren");
		}
		
		{
			{
				logger.begin(className, function+" multiReadValue");
				
				String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "static" + "_" + parent;
				
				String[] dbaddresses = null;
				{
					ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
					for(int y=0;y<staticAttibutes.length;++y) {
						dbaddressesArrayList.add(parent+staticAttibutes[y]);
					}
					dbaddresses = dbaddressesArrayList.toArray(new String[0]);
				}
				
				if ( logger.isDebugEnabled() ) {
					logger.debug(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
					for(int i = 0; i < dbaddresses.length; ++i ) {
						logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
					}
				}

				String api = "multiReadValue";
				
				database.addStaticRequest(api, clientKey, scsEnvId, dbaddresses, new DatabaseEvent() {
					
					@Override
					public void update(String key, String[] values) {
						{
							Database database = Database.getInstance();
							
							String clientKey_multiReadValue_inspector_static = "multiReadValue" + "_" + "inspector" + "_" + "static" + "_" + parent;
							if ( 0 == clientKey_multiReadValue_inspector_static.compareTo(key) ) {
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
							}
						}
					}
				});
				logger.end(className, function+" multiReadValue");
			}
		}

		logger.end(className, function);
	}
	
	@Override
	public void disconnect() {
		final String function = "disconnect";
		
		logger.begin(className, function);
		
		// Unrerves equipment
		unReserveEquipment();
		
		makeTabsDisconnect();
		
		((UIInspectorHeader)	uiInspectorHeader)		.disconnect();
		((UIInspectorEquipmentReserve)		equipmentReserve)		.disconnect();
		
		Database database = Database.getInstance();
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
	
	private boolean isRegExpMatch(LinkedList<String> regExpPatterns, String dbaddress) {
		boolean listMatch = false;
		for ( String regExpPattern : regExpPatterns ) {
			if ( isRegExpMatch( RegExp.compile(regExpPattern), dbaddress) )	{ 
				listMatch = true;
				break;
			}
		}
		return listMatch;
	}

	private TextBox txtMsg = null;
	private LinkedList<String> infos	= new LinkedList<String>();
	private LinkedList<String> controls	= new LinkedList<String>();
	private LinkedList<String> tags		= new LinkedList<String>();
	private LinkedList<String> advances	= new LinkedList<String>();
	@Override
	public void buildTabsAddress(String[] instances) {
		final String function = "buildTabsAddress";
		
		logger.begin(className, function);

		LinkedList<String> infoRegExpPatternBlackList		= new LinkedList<String>();
		LinkedList<String> controlRegExpPatternBlackList	= new LinkedList<String>();
		LinkedList<String> tagRegExpPatternBlackList		= new LinkedList<String>();
		LinkedList<String> advanceRegExpPatternBlackList	= new LinkedList<String>();
		
		LinkedList<String> infoRegExpPatternWhileList		= new LinkedList<String>();
		LinkedList<String> controlRegExpPatternWhileList	= new LinkedList<String>();
		LinkedList<String> tagRegExpPatternWhileList		= new LinkedList<String>();
		LinkedList<String> advanceRegExpPatternWhileList	= new LinkedList<String>();
		
		logger.begin(className, function + " getLists");
		
		String dictionariesCacheName = "UIInspectorPanel";
//		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strConfingInstance);
//		if ( null != dictionariesCache ) {

			LinkedList<LinkedList<String>> backLists = new LinkedList<LinkedList<String>>();
			backLists.add(infoRegExpPatternBlackList);
			backLists.add(controlRegExpPatternBlackList);
			backLists.add(tagRegExpPatternBlackList);
			backLists.add(advanceRegExpPatternBlackList);
			
			LinkedList<LinkedList<String>> whiteLists = new LinkedList<LinkedList<String>>();
			whiteLists.add(infoRegExpPatternWhileList);
			whiteLists.add(controlRegExpPatternWhileList);
			whiteLists.add(tagRegExpPatternWhileList);
			whiteLists.add(advanceRegExpPatternWhileList);
			
			for ( int i = 0 ; i < strTabConfigNames.length ; ++i ) {
				String tab = strTabConfigNames[i];
				logger.begin(className, function+" "+tab);
				
				{
					String functionEmb = function + " get"+UIPanelInspector_i.strBlack+"Lists";
					logger.begin(className, functionEmb);
					LinkedList<String> backList = backLists.get(i);
					String fileName = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigExtension;
					String keyNumName = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigNameBackList+UIPanelInspector_i.strConfigNameSize;
					logger.info(className, functionEmb, "fileName[{}] keyNumName[{}]", fileName, keyNumName);
					int numOfBack = ReadProp.readInt(dictionariesCacheName, fileName, keyNumName, 0);
					for ( int y = 0 ; y < numOfBack ; ++y ) {
						String key = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigNameBackList+UIPanelInspector_i.strDot+y;
						String value = ReadProp.readString(dictionariesCacheName, fileName, key, "");
						logger.info(className, functionEmb, "key[{}] value[{}]", key, value);
						backList.add(value);
					}

					logger.end(className, functionEmb);
				}

				{
					String functionEmb = function + " get"+UIPanelInspector_i.strWhite+"Lists";
					logger.begin(className, functionEmb);
					LinkedList<String> whiteList = whiteLists.get(i);
					String fileName = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigExtension;
					String keyNumName = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigNameWhileList+UIPanelInspector_i.strConfigNameSize;
					logger.info(className, functionEmb, "fileName[{}] keyNumName[{}]", fileName, keyNumName);
					int numOfWhite = ReadProp.readInt(dictionariesCacheName, fileName, keyNumName, 0);
					for ( int y = 0 ; y < numOfWhite ; ++y ) {
						String key = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigNameWhileList+UIPanelInspector_i.strDot+y;
						String value = ReadProp.readString(dictionariesCacheName, fileName, key, "");
						logger.info(className, functionEmb, "key[{}] value[{}]", key, value);
						whiteList.add(value);
					}

					logger.end(className, functionEmb);
				}
				
				logger.end(className, function+" "+tab);
			}
//		}

		logger.end(className, function + " getLists");

		logger.info(className, function,"Iterator Begin");
		
		for ( String dbaddress : instances ) {
			logger.info(className, function,"Iterator dbaddress[{}]", dbaddress);
			
			if ( null != dbaddress ) {

				logger.info(className, function, "Iterator dbaddress[{}]", dbaddress);
				
				{
					boolean infoBlackListMatch=false;
					boolean infoWhileListMatch=false;
					infoBlackListMatch = isRegExpMatch(infoRegExpPatternBlackList, dbaddress);
					if ( !infoBlackListMatch ) { 
						infoWhileListMatch = isRegExpMatch(infoRegExpPatternWhileList, dbaddress);
					}
					if ( infoWhileListMatch ) { infos.add(dbaddress); }
				}

				{
					boolean controlBlackListMatch=false;
					boolean controlWhileListMatch=false;
					controlBlackListMatch = isRegExpMatch(controlRegExpPatternBlackList, dbaddress);
					if ( !controlBlackListMatch ) { 
						controlWhileListMatch = isRegExpMatch(controlRegExpPatternWhileList, dbaddress);
					}
					if ( controlWhileListMatch ) { controls.add(dbaddress); }	
				}
				
				{
					boolean tagBlackListMatch=false;
					boolean tagWhileListMatch=false;
					tagBlackListMatch = isRegExpMatch(tagRegExpPatternBlackList, dbaddress);
					if ( ! tagBlackListMatch ) { 
						tagWhileListMatch = isRegExpMatch(tagRegExpPatternWhileList, dbaddress);
					}
					if ( tagWhileListMatch ) { tags.add(dbaddress); }	
				}
				
				{
					boolean advanceBlackListMatch=false;
					boolean advanceWhileListMatch=false;
					advanceBlackListMatch = isRegExpMatch(advanceRegExpPatternBlackList, dbaddress);
					if ( !advanceBlackListMatch ) { 
						advanceWhileListMatch = isRegExpMatch(advanceRegExpPatternWhileList, dbaddress);
					}
					if ( advanceWhileListMatch ) { advances.add(dbaddress); }	
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
	

//	private TabLayoutPanel panelTabLayout = null;
	
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
					logger.info(className, function, "eqtReserved[{}]", eqtReserved);
					int tabCount = panelTab.getTabBar().getTabCount();
					logger.info(className, function, "tabCount[{}]", tabCount);
					if ( tabCount > 1 ) {
						int selected = panelTab.getTabBar().getSelectedTab();
						logger.info(className, function, "selected[{}]", selected);
						if ( 2 == eqtReserved || 0 == eqtReserved ) {
							if ( 0 != selected ) {
								logger.info(className, function, "selectTab to 0");
								panelTab.getTabBar().selectTab(0);
							}
						}
						String cssName = "project-gwt-inspector-tabpanel-tab-disable-";
						for ( int i = 1 ; i < tabCount ; ++i ) {
							String cssNameNum = cssName + i;
							if ( 2 == eqtReserved ) {
								logger.info(className, function, "addStyleName cssNameNum[{}]", cssNameNum);
								((Widget)panelTab.getTabBar().getTab(i)).addStyleName(cssNameNum);
							} else {
								logger.info(className, function, "removeStyleName cssNameNum[{}]", cssNameNum);
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
				logger.info(className, function, "onClick uiInspectorInfo");
				unReserveEquipment();
			}
		});

		
		uiInspectorControl.setMessageBoxEvent(new MessageBoxEvent() {
			
			@Override
			public void setMessage(String message) {
				if ( null != txtMsg ) {
					logger.info(className, function, "setMessage message[{}]", message);
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
					logger.info(className, function, "setMessage message[{}]", message);
					String text = txtMsg.getText();
					logger.info(className, function, "setMessage text[{}]", text);
					if ( text.length() > 0 ) text += "\n";
					logger.info(className, function, "setMessage text + message[{}]", text + message);
					txtMsg.setText(text + message);
				}
			}
		});
		
		uiInspectorControl.setUIInspectorTabClickEvent(new UIInspectorTabClickEvent() {
			
			@Override
			public void onClick() {
				logger.info(className, function, "onClick uiInspectorControl");
				reserveEquipment();
			}
		});
		
		uiInspectorTag.setMessageBoxEvent(new MessageBoxEvent() {
			
			@Override
			public void setMessage(String message) {
				if ( null != txtMsg ) {
					logger.info(className, function, "setMessage message[{}]", message);
					txtMsg.setText(message);
				}
			}
			
			@Override
			public void addMessage(String message) {
				if ( null != txtMsg ) {
					logger.info(className, function, "setMessage message[{}]", message);
					String text = txtMsg.getText();
					logger.info(className, function, "setMessage text[{}]", text);
					if ( text.length() > 0 ) text += "\n";
					logger.info(className, function, "setMessage text + message[{}]", text + message);
					txtMsg.setText(text + message);
				}
			}
		});
		
		uiInspectorTag.setUIInspectorTabClickEvent(new UIInspectorTabClickEvent() {
			
			@Override
			public void onClick() {
				logger.info(className, function, "onClick uiInspectorTag");
				reserveEquipment();
			}
		});
		
		uiInspectorAdvance.setMessageBoxEvent(new MessageBoxEvent() {
			
			@Override
			public void setMessage(String message) {
				if ( null != txtMsg ) {
					logger.info(className, function, "setMessage message[{}]", message);
					txtMsg.setText(message);
				}
			}
			
			@Override
			public void addMessage(String message) {
				if ( null != txtMsg ) {
					logger.info(className, function, "setMessage message[{}]", message);
					String text = txtMsg.getText();
					logger.info(className, function, "setMessage text[{}]", text);
					if ( text.length() > 0 ) text += "\n";
					logger.info(className, function, "setMessage text + message[{}]", text + message);
					txtMsg.setText(text + message);
				}
			}
		});
		
		uiInspectorAdvance.setUIInspectorTabClickEvent(new UIInspectorTabClickEvent() {
			
			@Override
			public void onClick() {
				logger.info(className, function, "onClick uiInspectorAdvance");
				reserveEquipment();
			}
		});
		

		uiInspectorHeader.setUINameCard(this.uiNameCard);
		uiInspectorHeader.init();
		panelHeader	= uiInspectorHeader.getMainPanel();
		
		equipmentReserve.setUINameCard(this.uiNameCard);
		equipmentReserve.init();
		equipmentReserve.getMainPanel();
		
		uiInspectorInfo.setUINameCard(this.uiNameCard);
		uiInspectorInfo.init();
		panelInfo = uiInspectorInfo.getMainPanel();
		
		uiInspectorControl.setUINameCard(this.uiNameCard);
		uiInspectorControl.init();
		panelCtrl = uiInspectorControl.getMainPanel();
		
		uiInspectorTag.setUINameCard(this.uiNameCard);
		uiInspectorTag.init();
		panelTag = uiInspectorTag.getMainPanel();
		
		uiInspectorAdvance.setUINameCard(this.uiNameCard);
		uiInspectorAdvance.init();
		panelAdv = uiInspectorAdvance.getMainPanel();
		
		
		panelTab = new TabPanel();
		panelTab.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				final String function = "onBeforeSelection";
				logger.begin(className, function);
				logger.info(className, function, "event[{}]", event);
				logger.info(className, function, "event.getItem()[{}]", event.getItem());
				int intEvent = event.getItem().intValue();
				logger.info(className, function, "intEvent[{}]", intEvent);
				if ( 0 == intEvent ) {
					unReserveEquipment();
				} else {
					int intEqtReserved = ((UIInspectorEquipmentReserve)equipmentReserve).getEqtReservedValue();
					logger.info(className, function, "intEqtReserved[{}]", intEqtReserved);
					if ( 2 == intEqtReserved ) {
						logger.info(className, function, "intReserve equals to 2, Cancel event...");
						event.cancel();
					} else {
						logger.info(className, function, "intReserve not equals to 2, reserve equipment...");
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
		EquipmentReserve.equipmentReservation(scsEnvId, parent);
		logger.end(className, function);
	}
	
	private void unReserveEquipment() {
		final String function = "unReserveEquipment";
		logger.begin(className, function);
		EquipmentReserve.equipmentUnreservation(scsEnvId, parent);
		logger.end(className, function);
	}
}
