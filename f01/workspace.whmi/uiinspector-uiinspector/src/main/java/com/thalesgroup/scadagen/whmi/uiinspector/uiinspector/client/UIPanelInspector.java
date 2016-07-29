package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;

public class UIPanelInspector implements UIInspector_i, UIInspectorTag_i {
	
	private final Logger logger = Logger.getLogger(UIPanelInspector.class.getName());

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

		if ( null == scsEnvId ) scsEnvId = parent;
		this.scsEnvId = scsEnvId;
		this.parent = parent;
		
		logger.log(Level.FINE, "setConnection this.scsEnvId["+this.scsEnvId+"]");
		logger.log(Level.FINE, "setConnection this.parent["+this.parent+"]");
		
		database.setDynamic(scsEnvId, parent);
	}
	
	@Override
	public void setPeriod(String period) {
		logger.log(Level.FINE, "setAddresses Begin");

		this.periodMillis = Integer.parseInt(period);
		
		logger.log(Level.FINE, "setAddresses End");
	}

	@Override
	public void connect() {
		
		logger.log(Level.FINE, "connect Begin");
		Database database = Database.getInstance();
		database.connect();
		database.connectTimer(this.periodMillis);

		{
			logger.log(Level.FINE, "GetChildren Begin");

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
			
			logger.log(Level.FINE, "GetChildren End");
		}
		
		{
			{
				logger.log(Level.FINE, "multiReadValue Begin");
				
				String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "static" + "_" + parent;
				
				String[] dbaddresses = null;
				{
					ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
					for(int y=0;y<staticAttibutes.length;++y) {
						dbaddressesArrayList.add(parent+staticAttibutes[y]);
					}
					dbaddresses = dbaddressesArrayList.toArray(new String[0]);
				}
				
				logger.log(Level.FINE, "multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.log(Level.FINE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
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
				
				logger.log(Level.FINE, "multiReadValue End");
			}
		}
		
		logger.log(Level.FINE, "connect End");
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.FINE, "removeConnection Begin");
		
		makeTabsDisconnect();
		
		((UIInspectorHeader)	uiInspectorHeader)		.disconnect();
		
		Database database = Database.getInstance();
		database.disconnectTimer();
		database.disconnect();

		logger.log(Level.FINE, "removeConnection End");
	}
	
	private boolean isRegExpMatch(RegExp regExp, String input) {
		logger.log(Level.FINE, "isRegExpMatch regExp["+regExp+"] input["+input+"]");
		MatchResult matcher = regExp.exec(input);
		boolean matchFound = matcher != null;
		if ( matchFound ) {
//			for ( int i = 0 ; i < matcher.getGroupCount(); i++) {
//				String groupStr = matcher.getGroup(i);
//				logger.log(Level.SEVERE, "isRegExpMatch input["+input+"] groupStr["+groupStr+"]");
//			}
		}
		logger.log(Level.FINE, "isRegExpMatch matchFound["+matchFound+"]");
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
		
		logger.log(Level.FINE, "buildTabsAddress Begin");

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
			
		logger.log(Level.FINE, "buildTabsAddress Iterator Begin");
		
		for ( String dbaddress : instances ) {
			logger.log(Level.FINE, "buildTabsAddress Iterator dbaddress["+dbaddress+"]");
			
			if ( null != dbaddress ) {

				logger.log(Level.FINE, "buildTabsAddress Iterator dbaddress["+dbaddress+"]");
				
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
		
		logger.log(Level.FINE, "makeTabsSetAddress End");
	}
	
	@Override
	public void makeTabsBuildWidgets() {
		logger.log(Level.FINE, "makeTabsBuildWidgets Begin");
		
		for ( UIInspectorPage_i uiPanelInspector : uiInspectorTabs ) {
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
		
		for ( UIInspectorPage_i uiPanelInspector : uiInspectorTabs ) {
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
		
		for ( UIInspectorPage_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.disconnect();
			} else {
				logger.log(Level.SEVERE, "tagsDisconnect uiPanelInspector_i IS NULL");
			}
		}

		logger.log(Level.FINE, "tagsDisconnect End");
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
	public void init(String xml) {

		logger.log(Level.FINE, "init Begin");

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
					logger.log(Level.FINE, "init setMessage message["+message+"]");
					txtMsg.setText(message);
				}
			}
			
			@Override
			public void addMessage(String message) {
				if ( null != txtMsg ) {
					logger.log(Level.FINE, "init setMessage message["+message+"]");
					String text = txtMsg.getText();
					logger.log(Level.FINE, "init setMessage text["+text+"]");
					if ( text.length() > 0 ) text += "\n";
					logger.log(Level.FINE, "init setMessage text + message["+text + message+"]");
					txtMsg.setText(text + message);
				}
			}
		});
		
		uiInspectorTag.setMessageBoxEvent(new MessageBoxEvent() {
			
			@Override
			public void setMessage(String message) {
				if ( null != txtMsg ) {
					logger.log(Level.FINE, "init setMessage message["+message+"]");
					txtMsg.setText(message);
				}
			}
			
			@Override
			public void addMessage(String message) {
				if ( null != txtMsg ) {
					logger.log(Level.FINE, "init setMessage message["+message+"]");
					String text = txtMsg.getText();
					logger.log(Level.FINE, "init setMessage text["+text+"]");
					if ( text.length() > 0 ) text += "\n";
					logger.log(Level.FINE, "init setMessage text + message["+text + message+"]");
					txtMsg.setText(text + message);
				}
			}
		});
		
		uiInspectorAdvance.setMessageBoxEvent(new MessageBoxEvent() {
			
			@Override
			public void setMessage(String message) {
				if ( null != txtMsg ) {
					logger.log(Level.FINE, "init setMessage message["+message+"]");
					txtMsg.setText(message);
				}
			}
			
			@Override
			public void addMessage(String message) {
				if ( null != txtMsg ) {
					logger.log(Level.FINE, "init setMessage message["+message+"]");
					String text = txtMsg.getText();
					logger.log(Level.FINE, "init setMessage text["+text+"]");
					if ( text.length() > 0 ) text += "\n";
					logger.log(Level.FINE, "init setMessage text + message["+text + message+"]");
					txtMsg.setText(text + message);
				}
			}
		});
		
		panelHeader		= uiInspectorHeader		.getMainPanel(this.uiNameCard);
		panelInfo		= uiInspectorInfo		.getMainPanel(this.uiNameCard);
		panelCtrl		= uiInspectorControl	.getMainPanel(this.uiNameCard);
		panelTag		= uiInspectorTag		.getMainPanel(this.uiNameCard);
		panelAdv		= uiInspectorAdvance	.getMainPanel(this.uiNameCard);
		
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

        logger.log(Level.FINE, "init End");
	}
	
	@Override
	public ComplexPanel getMainPanel() {
        return basePanel;
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
