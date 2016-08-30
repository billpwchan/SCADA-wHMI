package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorPage_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTag_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper.PointName;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;

public class UIPanelInspector implements UIInspector_i, UIInspectorTag_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelInspector.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	// Order
	private final String strTabNames [] 	= new String[] {"Info","Control","Tagging","Advance"};

	// Static Attribute List
	private final String staticAttibutes[]	= new String[] {PointName.label.toString()};

	private String scsEnvId		= null;
	private String parent		= null;
	private int periodMillis	= 500;
	
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
	public void setPeriod(String period) {
		final String function = "setPeriod";
		
		logger.begin(className, function);

		this.periodMillis = Integer.parseInt(period);
		
		logger.end(className, function);
	}

	@Override
	public void connect() {
		final String function = "connect";
		
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
							
							((UIInspectorHeader)	uiInspectorHeader)		.connect();
							
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
								
								((UIInspectorHeader)	uiInspectorHeader)		.updateValue(key, dynamicvalues);
								
								((UIInspectorInfo)		uiInspectorInfo)		.updateValue(key, dynamicvalues);
								((UIInspectorControl)	uiInspectorControl)		.updateValue(key, dynamicvalues);
								((UIInspectorTag)		uiInspectorTag)			.updateValue(key, dynamicvalues);
								((UIInspectorAdvance)	uiInspectorAdvance)		.updateValue(key, dynamicvalues);
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
										value = RTDB_Helper.removeDBStringWrapper(value);
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
		
		makeTabsDisconnect();
		
		((UIInspectorHeader)	uiInspectorHeader)		.disconnect();
		
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
		
		infoRegExpPatternWhileList		.add("(dci|aci|sci)");
		controlRegExpPatternWhileList	.add("(dio|aio)");
		tagRegExpPatternWhileList		.add("(dioECT-PTW)");
		advanceRegExpPatternWhileList	.add("(dci|aci)");
		
		
		controlRegExpPatternBlackList	.add("(dioECT-PTW)");
			
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
		
		uiInspectorInfo		.setParent(scsEnvId, parent);
		uiInspectorControl	.setParent(scsEnvId, parent);
		uiInspectorTag		.setParent(scsEnvId, parent);
		uiInspectorAdvance	.setParent(scsEnvId, parent);
		
		
		uiInspectorHeader	.setAddresses	(infos		.toArray(new String[0]));
		
		uiInspectorInfo		.setAddresses	(infos		.toArray(new String[0]));
		uiInspectorControl	.setAddresses	(controls	.toArray(new String[0]));
		uiInspectorTag		.setAddresses	(tags		.toArray(new String[0]));
		uiInspectorAdvance	.setAddresses	(advances	.toArray(new String[0]));
		
		logger.end(className, function);
	}
	
	@Override
	public void makeTabsBuildWidgets() {
		final String function = "makeTabsBuildWidgets";
		
		logger.begin(className, function);
		
		for ( UIInspectorPage_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.buildWidgets();
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
		
		for ( UIInspectorPage_i uiPanelInspector : uiInspectorTabs ) {
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
		
		for ( UIInspectorPage_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.disconnect();
			} else {
				logger.warn(className, function, "uiPanelInspector_i IS NULL");
			}
		}

		logger.end(className, function);
	}
	
	private UIInspectorPage_i uiInspectorHeader		= null;
	
	private UIInspectorPage_i uiInspectorInfo		= null;
	private UIInspectorPage_i uiInspectorControl	= null;
	private UIInspectorPage_i uiInspectorTag		= null;
	private UIInspectorPage_i uiInspectorAdvance	= null;
	
	private TabPanel panelTab 			= null;

	private ComplexPanel panelHeader	= null;
	private ComplexPanel panelInfo		= null;
	private ComplexPanel panelCtrl		= null;
	private ComplexPanel panelTag		= null;
	private ComplexPanel panelAdv		= null;
	
	private LinkedList<UIInspectorPage_i> uiInspectorTabs = null;

	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private VerticalPanel basePanel = null;
	@Override
	public void init() {
		final String function = "init";

		logger.begin(className, function);

		uiInspectorTabs 	= new LinkedList<UIInspectorPage_i>();
		
		uiInspectorHeader	= new UIInspectorHeader();
		
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
		

		uiInspectorHeader.setUINameCard(this.uiNameCard);
		uiInspectorHeader.init(null);
		panelHeader	= uiInspectorHeader.getMainPanel();
		
		uiInspectorInfo.setUINameCard(this.uiNameCard);
		uiInspectorInfo.init(null);
		panelInfo = uiInspectorInfo.getMainPanel();
		
		uiInspectorControl.setUINameCard(this.uiNameCard);
		uiInspectorControl.init(null);
		panelCtrl = uiInspectorControl.getMainPanel();
		
		uiInspectorTag.setUINameCard(this.uiNameCard);
		uiInspectorTag.init(null);
		panelTag = uiInspectorTag.getMainPanel();
		
		uiInspectorAdvance.setUINameCard(this.uiNameCard);
		uiInspectorAdvance.init(null);
		panelAdv = uiInspectorAdvance.getMainPanel();
		
		panelTab = new TabPanel();
		panelTab.addStyleName("project-gwt-button-inspector-tabpanel");
		
		panelTab.add(panelInfo	, strTabNames[0]);
		panelTab.add(panelCtrl	, strTabNames[1]);
		panelTab.add(panelTag	, strTabNames[2]);
		panelTab.add(panelAdv	, strTabNames[3]);
		panelTab.selectTab(0);
		
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
		bottomBar.addStyleName("project-gwt-panel-inspector-bottom");
		bottomBar.add(txtMsg);
		bottomBar.add(btnClose);
		
		basePanel = new VerticalPanel();
		basePanel.add(panelHeader);
		basePanel.add(panelTab);
		basePanel.add(bottomBar);
		basePanel.addStyleName("project-gwt-panel-inspector");

		logger.end(className, function);
	}
	
	private UIPanelInspectorDialogBoxEvent uiPanelInspectorEvent = null;
	public void setUIPanelInspectorEvent(UIPanelInspectorDialogBoxEvent uiPanelInspectorEvent) {
		this.uiPanelInspectorEvent = uiPanelInspectorEvent;
	}
	
	public void setText(String title) {
		if ( null != uiPanelInspectorEvent ) uiPanelInspectorEvent.setTitle(title);
	}
	
	public void hide() {
		if ( null != uiPanelInspectorEvent ) uiPanelInspectorEvent.hideDialogBox();
	}
	
}
