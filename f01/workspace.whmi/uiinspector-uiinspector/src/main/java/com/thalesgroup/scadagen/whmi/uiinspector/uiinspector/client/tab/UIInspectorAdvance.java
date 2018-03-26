package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.MessageBoxEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.page.PageCounter;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.UIButtonToggle;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorAdvance_i.Attribute;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointType;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTabClickEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.Stability;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Observer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;

public class UIInspectorAdvance implements UIInspectorTab_i {
	
	private final String cls = this.getClass().getName();
	private final String className = UIWidgetUtil.getClassSimpleName(cls);
	private UILogger logger = UILoggerFactory.getInstance().getLogger(UIWidgetUtil.getClassName(cls));
	
	private static final String DICTIONARY_CACHE_NAME = UIInspector_i.strUIInspector;
	private static final String DICTIONARY_FILE_NAME = "inspectorpanel.advance.properties";
	private static final String CONFIG_PREFIX = "inspectorpanel.advance.";
	
	private final String INSPECTOR = UIInspectorAdvance_i.INSPECTOR;
	
	public static final String STR_UNDERSCORE			= "_";
	public static final String STR_INITCONDGL_VALID		= "1";
	
	public static final String STR_EMPTY				= "";
	
	public static final String STR_UP					= "▲";
	public static final String STR_SPLITER				= " / ";
	public static final String STR_DOWN					= "▼";
	public static final String STR_PAGER_DEFAULT		= "1 / 1";
	
	public static final String STR_EQP_POINT	= "Equipment Point";
	public static final String STR_AI			= "AI";
	public static final String STR_SS			= "SS";
	public static final String STR_MO			= "MO";
	public static final String STR_VALUE		= "Value";
	
	// Create Header
	public static final String [] STR_HEADERS	= new String[] {STR_EQP_POINT, STR_AI, STR_SS, STR_MO, STR_VALUE, STR_EMPTY};
	
	public static final String STR_APPLY 		= "Apply";
	public static final String STR_CANCEL 		= "Cancel";
	
	public static final String STR_INHIBIT 		= "Inhibit";
	public static final String STR_SUSPEND 		= "Suspend";

	public static final String STR_RESTORE 		= "Restore";
	public static final String STR_OVERRIDE 	= "Override";
	
	public static final String STR_ATTRIBUTE_LABEL = "ATTRIBUTE_LABEL_";

	private int indexAI		= UIInspectorAdvance_i.CHKBOX_INDEX_AI;
	private int indexSS		= UIInspectorAdvance_i.CHKBOX_INDEX_SS;
	private int indexMO		= UIInspectorAdvance_i.CHKBOX_INDEX_MO;

	private int dalValueTableLength = 12;
	
	private int dalValueTableLabelColIndex = 4;
	private int dalValueTableValueColIndex = 1;
	
	private boolean moApplyWithoutReset			= false;
	
	// Static Attribute List
	private final String staticDciAttibutes []	= new String[] {PointName.label.toString(), PointName.dalValueTable.toString(), PointName.hmiOrder.toString()};
	private final String staticAciAttibutes []	= new String[] {PointName.label.toString(), PointName.aalValueTable.toString(), PointName.hmiOrder.toString()};
	private final String staticSciAttibutes []	= new String[] {PointName.label.toString(), PointName.salValueTable.toString(), PointName.hmiOrder.toString()};
	
	// Dynamic Attribute List
	private final String dynamicDciAttibutes []	= new String[] {PointName.value.toString(), PointName.validity.toString(), PointName.dfoForcedStatus.toString()};
	private final String dynamicAciAttibutes []	= new String[] {PointName.value.toString(), PointName.validity.toString(), PointName.afoForcedStatus.toString()};
	private final String dynamicSciAttibutes []	= new String[] {PointName.value.toString(), PointName.validity.toString(), PointName.sfoForcedStatus.toString()};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	private Database database	= null;

	private String strCssPrefix;
	
	private String tabName = null;
	@Override
	public void setTabName(String tabName) { 
		this.tabName = tabName;
		this.strCssPrefix = "project-inspector-"+tabName+"-";
	}
	
	private Map<String, Map<String, String>> attributesList = new HashMap<String, Map<String, String>>();
	@Override
	public void setAttribute(String type, String key, String value) {
		final String function = "setAttribute";
		logger.begin(className, function);
		if ( null == attributesList.get(type) ) attributesList.put(type, new HashMap<String, String>());
		attributesList.get(type).put(key, value);
		logger.end(className, function);
	}
	
	@Override
	public void setRight(Map<String, Boolean> rights) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void applyRight() {
		// TODO Auto-generated method stub
		
	}
	
	private boolean equipmentReserveHasScreen = false;
	@Override
	public void setEquipmentReserveHasScreen(boolean equipmentReserveHasScreen) {
		this.equipmentReserveHasScreen = equipmentReserveHasScreen;
	}
	
	@Override
	public void setParent(String scsEnvId, String parent) {
		final String function = "setParent";
		
		this.scsEnvId = scsEnvId;
		this.parent = parent;
		logger.debug(className, function, "this.parent[{}] this.scsEnvId[{}]", this.parent, this.scsEnvId);
	}
	
	@Override
	public void setAddresses(String[] addresses) {
		final String function = "setAddresses";
		logger.begin(className, function);
		
		this.addresses = addresses;
		
		logger.end(className, function);
	}
	
	@Override
	public String[] getAddresses() {
		return this.addresses;
	}
	
	private void updatePager() {
		final String function = "updatePager";
		logger.begin(className, function);
		
		pageCounter.calc(pageIndex);
		
		if ( pageCounter.hasPreview || pageCounter.hasNext ) {
			btnUp.setEnabled(pageCounter.hasPreview);
			lblPageNum.setText(String.valueOf(pageIndex+1) + STR_SPLITER + String.valueOf(pageCounter.pageCount));
			btnDown.setEnabled(pageCounter.hasNext);
		} else {
			btnUp.setVisible(false);
			lblPageNum.setVisible(false);
			btnDown.setVisible(false);
		}
		
		logger.end(className, function);
	}
	
	private void updateLayout() {
		final String function = "updateLayout";
		logger.begin(className, function);
		
		pageCounter.calc(pageIndex);
		
		int pageSize = pageCounter.pageSize;
		
		int stopper = pageCounter.pageRowCount;
		
		logger.debug(className, function
				, "pageIndex[{}] pageSize[{}], stopper[{}]"
				, new Object[]{pageIndex, pageSize, stopper});
		
		for ( int i = 0 ; i < pageSize ; i++ ) {
			boolean visible = true;
			if ( i >= stopper ) {
				visible = false;
			}
			lblAttibuteLabel[i]		.setVisible(visible);
			lstValues[i]			.setVisible(visible);
			txtValues[i]			.setVisible(visible);
			for ( int j = 0 ; j < 3 ; ++j ) {
				chkDPMs[i][j]		.setVisible(visible);
			}
			btnApplys[i]			.setVisible(visible);

		}
		logger.end(className, function);
	}

	private void onButton(Button btn) {
		final String function = "onButton";
		logger.begin(className, function);
		
		if (  btn == btnUp || btn == btnDown ) {
			if ( btn == btnUp) {
				--pageIndex;
			} else if ( btn == btnDown ) {
				++pageIndex;
			}
			updatePager();
			updateLayout();
			updateValue(true);
		}
		
		logger.end(className, function);
	}
	
	private DpcMgr dpcMgr = null;
	private Observer observer = null;
	private Subject subject = null;
	
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		
		readProperties();

		// Read static
		{
			logger.begin(className, function);
			
			DataBaseClientKey clientKey = new DataBaseClientKey();
			clientKey.setAPI(API.multiReadValue);
			clientKey.setWidget(INSPECTOR + tabName);
			clientKey.setStability(Stability.STATIC);
			clientKey.setScreen(uiNameCard.getUiScreen());
			clientKey.setEnv(scsEnvId);
			clientKey.setAdress(parent);
			
			String strClientKey = clientKey.getClientKey();

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for ( String dbaddress : this.addresses ) {
					String point = DatabaseHelper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = DatabaseHelper.getPointType(point);
						if ( pointType == PointType.dci ) {
							for ( String attribute : staticDciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.aci ) {
							for ( String attribute : staticAciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.sci ) {
							for ( String attribute : staticSciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else {
							logger.warn(className, function, "dbaddress IS UNKNOW TYPE");
						}
					}
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
			database.addStaticRequest(strApi, strClientKey, scsEnvId, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					
					DataBaseClientKey clientKey = new DataBaseClientKey();
					clientKey.setAPI(API.multiReadValue);
					clientKey.setWidget(INSPECTOR + tabName);
					clientKey.setStability(Stability.STATIC);
					clientKey.setScreen(uiNameCard.getUiScreen());
					clientKey.setEnv(scsEnvId);
					clientKey.setAdress(parent);
					
					String strClientKey = clientKey.toClientKey();
					
					if ( strClientKey.equalsIgnoreCase(key) ) {

						String [] dbaddresses	= database.getKeyAndAddress(key);
						String [] dbvalues		= database.getKeyAndValues(key);
						HashMap<String, String> keyAndValue = new HashMap<String, String>();
						for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
							keyAndValue.put(dbaddresses[i], dbvalues[i]);
						}

						updateValue(key, keyAndValue);
					}
				}
			});
			
			logger.end(className, function);
		}
		
		// Read dynamic
		{
			logger.begin(className, function);
			
			DataBaseClientKey clientKey = new DataBaseClientKey();
			clientKey.setAPI(API.multiReadValue);
			clientKey.setWidget(INSPECTOR + tabName);
			clientKey.setStability(Stability.DYNAMIC);
			clientKey.setScreen(uiNameCard.getUiScreen());
			clientKey.setEnv(scsEnvId);
			clientKey.setAdress(parent);
			
			String strClientKey = clientKey.getClientKey();

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for ( String dbaddress : this.addresses ) {
					String point = DatabaseHelper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = DatabaseHelper.getPointType(point);
						if ( pointType == PointType.dci ) {
							for ( String attribute : dynamicDciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.aci ) {
							for ( String attribute : dynamicAciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.sci ) {
							for ( String attribute : dynamicSciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else {
							logger.warn(className, function, "dbaddress IS UNKNOW TYPE");
						}
					} else {
						logger.warn(className, function, "point IS NULL");
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}
			
			
			if ( logger.isDebugEnabled() ) {
				logger.debug(className, function, "strClientKey[{}] scsEnvId[{}]", strClientKey, scsEnvId);
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
				}
			}
			
			database.subscribe(strClientKey, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					// TODO Auto-generated method stub
					
				}
				
			});
		
			logger.end(className, function);
		}
		
		{
			dpcMgr = DpcMgr.getInstance(className);
			subject = dpcMgr.getSubject();
			
			observer = new Observer() {
				@Override
				public void setSubject(Subject subject){
					this.subject = subject;
					this.subject.attach(this);
				}
				
				@Override
				public void update() {
	
				}
			};
			
			observer.setSubject(subject);			
		}

		logger.end(className, function);
	}
	
	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(className, function);
		{
			DataBaseClientKey clientKey = new DataBaseClientKey();
			clientKey.setAPI(API.multiReadValue);
			clientKey.setWidget(INSPECTOR + tabName);
			clientKey.setStability(Stability.DYNAMIC);
			clientKey.setScreen(uiNameCard.getUiScreen());
			clientKey.setEnv(scsEnvId);
			clientKey.setAdress(parent);
			
			String strClientKey = clientKey.getClientKey();
			database.unSubscribe(strClientKey);
		}
		logger.end(className, function);
	}

	@Override
	public void buildWidgets(int numOfPointEach) {
		buildWidgets(this.addresses.length, numOfPointEach);
	}
	
	private int pageIndex = 0;
	private PageCounter pageCounter = null;

	
	private InlineLabel[] lblAttibuteLabel		= null;
	private ListBox[] lstValues					= null;
	private int[] intValuesOri					= null;
	private TextBox[] txtValues					= null;
	private String[] strValuesOri				= null;
	
	private CheckBox[][] chkDPMs 				= null;
	
	private UIButtonToggle[] btnApplys			= null;
	
	private void buildWidgets(int numOfWidgets, int numOfPointEachPage) {
		final String function = "buildWidgets";
		logger.begin(className, function);
		
		logger.debug(className, function, "numOfWidgets[{}]", numOfWidgets);
		logger.debug(className, function, "numOfPointEach[{}]", numOfPointEachPage);
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, numOfPointEachPage);
			pageCounter.calc(pageIndex);
			
			flexTableAttibutes = new FlexTable();
			flexTableAttibutes.addStyleName(strCssPrefix+"flextable-attribute");
			
			int pageSize = pageCounter.pageSize;
			
			logger.debug(className, function, "pageSize[{}]", pageSize);
				
			lblAttibuteLabel	= new InlineLabel[pageSize];
			
			lstValues			= new ListBox[pageSize];
			intValuesOri		= new int[pageSize];
			txtValues			= new TextBox[pageSize];
			strValuesOri		= new String[pageSize];
			
			chkDPMs				= new CheckBox[pageSize][3];
			
			btnApplys			= new UIButtonToggle[pageSize];

			{
				InlineLabel labels[] = new InlineLabel[STR_HEADERS.length];
				for ( int i = 0 ; i < STR_HEADERS.length ; ++i ) {
					labels[i] = new InlineLabel();
					labels[i].addStyleName(strCssPrefix+"inlinelabel-header-"+i);
					labels[i].setText(STR_HEADERS[i]);
					flexTableAttibutes.setWidget(1, i, labels[i]);
					DOM.getParent(labels[i].getElement()).setClassName(strCssPrefix+"inlinelabel-header-parent-"+i);
				}
			}
								
			for( int i = 0 ; i < pageSize ; ++i ) {
				
				int r = 0;
				
				logger.debug(className, function, "i[{}]", i);
					
				lblAttibuteLabel[i] = new InlineLabel();
				lblAttibuteLabel[i].addStyleName(strCssPrefix+"inlinelabel-points-"+i);
				lblAttibuteLabel[i].setText(STR_ATTRIBUTE_LABEL+(i+1)+":");
				flexTableAttibutes.setWidget(i+1+1, r++, lblAttibuteLabel[i]);
				DOM.getParent(lblAttibuteLabel[i].getElement()).setClassName(strCssPrefix+"inlinelabel-points-parent-"+i);
				
				chkDPMs[i] = new CheckBox[3];
				chkDPMs[i][0] = new CheckBox();
				chkDPMs[i][0].addStyleName(strCssPrefix+"checkbox-points-ai-"+i);
				
				flexTableAttibutes.setWidget(i+1+1, r++, chkDPMs[i][0]);
				DOM.getParent(chkDPMs[i][0].getElement()).setClassName(strCssPrefix+"checkbox-points-ai-parent-"+i);
				
				chkDPMs[i][0].addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						selectCheckbox(event, indexAI);
					}
				});
				
				chkDPMs[i][1] = new CheckBox();
				chkDPMs[i][1].addStyleName(strCssPrefix+"checkbox-points-ss-"+i);
				
				flexTableAttibutes.setWidget(i+1+1, r++, chkDPMs[i][1]);
				DOM.getParent(chkDPMs[i][1].getElement()).setClassName(strCssPrefix+"checkbox-points-ss-parent-"+i);
				
				chkDPMs[i][1].addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						selectCheckbox(event, indexSS);
					}
				});
				
				chkDPMs[i][2] = new CheckBox();
				chkDPMs[i][2].addStyleName(strCssPrefix+"checkbox-points-mo-"+i);
				flexTableAttibutes.setWidget(i+1+1, r++, chkDPMs[i][2]);
				DOM.getParent(chkDPMs[i][2].getElement()).setClassName(strCssPrefix+"checkbox-points-mo-parent-"+i);
				
				chkDPMs[i][2].addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						selectCheckbox(event, indexMO);
					}
				});
				
				lstValues[i] = new ListBox();
				lstValues[i].setVisibleItemCount(1);
				lstValues[i].addStyleName(strCssPrefix+"listbox-point-label-"+i);

				txtValues[i] = new TextBox();
				txtValues[i].setVisible(false);
				txtValues[i].addStyleName(strCssPrefix+"textbox-point-value-"+i);

				HorizontalPanel hp = new HorizontalPanel();
				hp.addStyleName(strCssPrefix+"panel-points-value-"+i);
				hp.add(lstValues[i]);
				hp.add(txtValues[i]);
				
				flexTableAttibutes.setWidget(i+1+1, r++, hp);
				DOM.getParent(hp.getElement()).setClassName(strCssPrefix+"panel-points-value-parent-"+i);
				
				btnApplys[i] = new UIButtonToggle(STR_APPLY);
				btnApplys[i].addStyleName(strCssPrefix+"button-point-apply-"+i);
				
				btnApplys[i].addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {

						logger.begin(className, function + "onClick");
						
						sendControl(event);
						
						logger.end(className, function + "onClick");
					}
				});
				flexTableAttibutes.setWidget(i+1+1, r++, btnApplys[i]);
				DOM.getParent(btnApplys[i].getElement()).setClassName(strCssPrefix+"button-point-apply-parent-"+i);
			}
			
			for ( int i = 0 ; i < 8 ; ++i ) {
				flexTableAttibutes.getColumnFormatter().addStyleName(i, strCssPrefix+"table-column-status-col-"+i);
			}

			vpCtrls.add(flexTableAttibutes);
			
			updatePager();
			updateLayout();
			
		} else {
			logger.warn(className, function, "points IS NULL");
		}
		
		logger.end(className, function);
	}
	
	private boolean valueRefreshed = false;
	private Map<String, Map<String, String>> keyAndValuesStatic		= new LinkedHashMap<String,Map<String, String>>();
	private Map<String, Map<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, Map<String, String>>();
	private Map<String, String> dbvalues = new HashMap<String, String>();
	@Override
	public void updateValue(String strClientKey, Map<String, String> keyAndValue) {
		final String function = "updateValue";

		logger.begin(className, function);
		logger.trace(className, function, "strClientKey[{}]", strClientKey);
		
		DataBaseClientKey clientKey = new DataBaseClientKey("_", strClientKey);
		
		for ( String key : keyAndValue.keySet() ) {
			dbvalues.put(key, keyAndValue.get(key));
		}
	
		if ( clientKey.isStatic() ) {
			
			keyAndValuesStatic.put(strClientKey, keyAndValue);
			
			updateValue(true);
			
		} else if ( clientKey.isDynaimc() ) {
			
			keyAndValuesDynamic.put(strClientKey, keyAndValue);
			
			updateValue(false);
			
		}

		logger.end(className, function);
	}
	
	private void updateValue(boolean withStatic) {
		final String function = "updateValue";
		
		logger.begin(className, function);
		
		if ( withStatic ) {
			for ( String clientKey : keyAndValuesStatic.keySet() ) {
				
				Map<String, String> keyAndValue = keyAndValuesStatic.get(clientKey);
				
				updateValueStatic(clientKey, keyAndValue);
			}//End of for keyAndValuesStatic
		}
	
		for ( String clientKey : keyAndValuesDynamic.keySet() ) {
			
			Map<String, String> keyAndValue = keyAndValuesDynamic.get(clientKey);
			
			updateValueDynamic(clientKey, keyAndValue);
			
		}// End of keyAndValuesDynamic
		
		logger.end(className, function);
	}
	
	public void updateValueStatic(String key, Map<String, String> keyAndValue) {
		final String function = "updateValueStatic";
		
		logger.begin(className, function);
		logger.trace(className, function, "key[{}]", key);
		
		valueRefreshed = false;
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;
		
		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(INSPECTOR + tabName);
		clientKey.setStability(Stability.STATIC);
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(scsEnvId);
		clientKey.setAdress(parent);
		
		String strClientKey = clientKey.toClientKey();
		
		if ( strClientKey.equalsIgnoreCase(key) ) {
	
			for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
				String address = addresses[x];

				// Update the Label
				String label = DatabaseHelper.getAttributeValue(address, PointName.label.toString(), dbvalues);
				label = DatabaseHelper.removeDBStringWrapper(label);
				logger.trace(className, function, "label[{}]", label);

				lblAttibuteLabel[y].setText(label);
				
				// ACI, SCI Show the TextBox
				// DCI, Show the ListBox and store the valueTable

				String point = DatabaseHelper.getPoint(address);
				PointType pointType = DatabaseHelper.getPointType(point);
				
				if ( PointType.dci == pointType ) {
					
					txtValues[y].setVisible(false);
					lstValues[y].setVisible(true);
					
					// Update the Label
					String valueTable = DatabaseHelper.getAttributeValue(address, PointName.dalValueTable.toString(), dbvalues);
					logger.trace(className, function, "valueTable[{}]", valueTable);
					
					if ( null != valueTable ) {

						String labels[]	= new String[dalValueTableLength];
						String values[]	= new String[dalValueTableLength];
							for( int r = 0 ; r < dalValueTableLength ; ++r ) {
								values[r]	= DatabaseHelper.getArrayValues(valueTable, dalValueTableValueColIndex, r );
								values[r]	= DatabaseHelper.removeDBStringWrapper(values[r]);
								labels[r]	= DatabaseHelper.getArrayValues(valueTable, dalValueTableLabelColIndex, r );
								labels[r]	= DatabaseHelper.removeDBStringWrapper(labels[r]);
							}					
						
						lstValues[y].clear();
						for( int r = 0 ; r < dalValueTableLength ; ++r ) {
							
							if ( 0 == labels[r].compareTo(STR_EMPTY) ) break;
							
							lstValues[y].addItem(labels[r]);
							
							logger.trace(className, function, "names[{}][{}] values[{}][{}]", new Object[]{r, labels[r], r, values[r]});
						}
					} else {
						logger.warn(className, function, "valueTable IS NULL!");
					}
				} else if ( PointType.aci == pointType ) {
					
					txtValues[y].setVisible(true);
					lstValues[y].setVisible(false);

					// Update the Label
					String value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
					value = DatabaseHelper.removeDBStringWrapper(value);
					logger.trace(className, function, "value[{}]", value);
					
					txtValues[y].setText(value);
					
				} else if ( PointType.sci == pointType ) {
					
					txtValues[y].setVisible(true);
					lstValues[y].setVisible(false);

					String value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
					value = DatabaseHelper.removeDBStringWrapper(value);
					logger.trace(className, function, "value[{}]", value);
					
					txtValues[y].setText(value);
					
				}
			}
		}
		
		logger.end(className, function);
	}
	
	public void updateValueDynamic(String clientKey, Map<String, String> keyAndValue) {
		final String function = "updateValueDynamic";
		logger.begin(className, function);
		logger.trace(className, function, "clientkey[{}]", clientKey);
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
			String address = addresses[x];
			
			// Update the AI, SS and MO status
			{
				String stylename = strCssPrefix+"checkbox-points-activated-"+y;
				
				String point = DatabaseHelper.getPoint(address);
				PointType pointType = DatabaseHelper.getPointType(point);
				
				String sForcedStatusPoint = null;
				if ( PointType.dci == pointType ) {
					sForcedStatusPoint = PointName.dfoForcedStatus.toString();
				} else if ( PointType.aci == pointType ){
					sForcedStatusPoint = PointName.afoForcedStatus.toString();
				} else if ( PointType.sci == pointType ) {
					sForcedStatusPoint = PointName.sfoForcedStatus.toString();
				}
				logger.trace(className, function, "pointType[{}] sForcedStatusPoint[{}]", pointType, sForcedStatusPoint);
				
				String sForcedStatus = null;
				sForcedStatus = getStatusValue(address, sForcedStatusPoint);
				
				if ( null != sForcedStatus ) {
					int forcedStatus = Integer.parseInt(sForcedStatus);
					
					if ( null != chkDPMs[y][indexAI] ) {
						if ( DatabaseHelper.isAI(forcedStatus) ) {
							chkDPMs[y][indexAI].addStyleName(stylename);
							if ( !valueRefreshed ) {
								chkDPMs[y][indexAI].setValue(true);
							}
						} else {
							chkDPMs[y][indexAI].removeStyleName(stylename);
							if ( !valueRefreshed ) {
								chkDPMs[y][indexAI].setValue(false);
							}
						}
					}
					if ( null != chkDPMs[y][indexSS] ) {
						if ( DatabaseHelper.isSS(forcedStatus) ) {
							chkDPMs[y][indexSS].addStyleName(stylename);
							if ( !valueRefreshed ) {
								chkDPMs[y][indexSS].setValue(true);
							}
						} else {
							chkDPMs[y][indexSS].removeStyleName(stylename);
							if ( !valueRefreshed ) {
								chkDPMs[y][indexSS].setValue(false);
							}
						}
					}
					if ( null != chkDPMs[y][indexMO] ) {
						if ( DatabaseHelper.isMO(forcedStatus) ) {
							chkDPMs[y][indexMO].addStyleName(stylename);
							if ( !valueRefreshed ) {
								chkDPMs[y][indexMO].setValue(true);
							}
						} else {
							chkDPMs[y][indexMO].removeStyleName(stylename);
							if ( !valueRefreshed ) {
								chkDPMs[y][indexMO].setValue(false);
							}
						}
					}
				} else {
					logger.warn(className, function, "sForcedStatus IS NULL!");
				}

			}

			// Update the value Once time
			if ( !valueRefreshed ) {
				
				String value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
				value = DatabaseHelper.removeDBStringWrapper(value);
				logger.trace(className, function, "value[{}]", value);
				
				if ( null != value ) {
					String point = DatabaseHelper.getPoint(address);
					if ( null != point ) {
						PointType pointType = DatabaseHelper.getPointType(point);

						logger.trace(className, function, "point[{}]", point);
						
						if ( PointType.dci == pointType ) {

							String valueTable = DatabaseHelper.getAttributeValue(address, PointName.dalValueTable.toString(), dbvalues);
							logger.trace(className, function, "valueTable[{}]", valueTable);
							
							if ( null != valueTable ) {
								String sValue = String.valueOf(value);
								String tValue = "";
								for( int r = 0 ; r < dalValueTableLength ; ++r ) {
									tValue	= DatabaseHelper.getArrayValues(valueTable, dalValueTableValueColIndex, r );
									tValue	= DatabaseHelper.removeDBStringWrapper(tValue);
									
									if ( 0 == sValue.compareTo(tValue) ) {
										lstValues[y].setSelectedIndex(r);
										intValuesOri[y] = r;
										break;
									}
								}
							}
						} else if ( PointType.aci == pointType ) {
							txtValues[y].setValue(value);
							strValuesOri[y] = value;
						} else if ( PointType.sci == pointType ) {
							txtValues[y].setValue(value);
							strValuesOri[y] = value;
						}

					}
				} else {
					logger.warn(className, function, "value IS NULL!");
				}
			}
		}
		
		if ( !valueRefreshed ) {
			valueRefreshed = true;
		}
		
		logger.end(className, function);
	}
	
	private String getStatusValue(String address, String strForcedStatus) {
		final String function = "getStatusValue";
		logger.begin(className, function);

		String sforcedStatus = DatabaseHelper.removeDBStringWrapper(DatabaseHelper.getAttributeValue(address, strForcedStatus, dbvalues));
		logger.trace(className, function, "sforcedStatus[{}]", sforcedStatus);
		
		logger.end(className, function);
		
		return sforcedStatus;
	}
	
	private void verifyAndSendAI(final int y, final String dbaddress, final int forcedStatus) {
		final String function = "verifyAndSendAI";
		logger.begin(className, function);
		
		boolean isAI = DatabaseHelper.isAI(forcedStatus);
		logger.debug(className, function, "isAI[{}]", new Object[]{isAI});
		
		boolean isTicked = chkDPMs[y][indexAI].getValue();
		logger.debug(className, function, "isTicked[{}]", new Object[]{isTicked});

		boolean isAIApply	= !isAI && isTicked;
		boolean isAICancel	= isAI && !isTicked;
		
		logger.debug(className, function, "isAIApply[{}] isAICancel[{}]", new Object[]{isAIApply, isAICancel});

		// Alarm Inhibit
		if ( isAIApply ) {
			
			// SCSDPC ALARM_INHIBIT_VAR
			String key = "changeEqpStatus" + "_"+ "inspector" + tabName + "_"+ "alarminhibit" + "_"+ "true" + "_" + dbaddress;
			
			dpcMgr.sendChangeVarStatus(key, scsEnvId, dbaddress, DCP_i.ValidityStatus.ALARM_INHIBIT_VAR);
			
			chkDPMs[y][indexAI].setValue(true);
		} else if (isAICancel ) {
			// SCSDPC NO_ALARM_INHIBIT_VAR
			String key = "changeEqpStatus" + "_"+ "inspector" + tabName + "_"+ "alarminhibit" + "_"+ "false" + "_" + dbaddress;
			
			dpcMgr.sendChangeVarStatus(key, scsEnvId, dbaddress, DCP_i.ValidityStatus.NO_ALARM_INHIBIT_VAR);

			chkDPMs[y][indexAI].setValue(false);
		} else {
			logger.warn(className, function, "Not a AI Action");
		}
		
		logger.end(className, function);
	}
	
	private void verifyAndSendSS(final int y, final String dbaddress, final int forcedStatus) {
		final String function = "verifyAndSendSS";
		logger.begin(className, function);
		
		boolean isSS = DatabaseHelper.isSS(forcedStatus);
		logger.debug(className, function, "isSS[{}]", new Object[]{isSS});
		
		boolean isTicked = chkDPMs[y][indexSS].getValue();
		logger.debug(className, function, "isTicked[{}]", new Object[]{isTicked});
		
		boolean isSSApply	= !isSS && isTicked;
		boolean isSSCancel	= isSS && !isTicked;
		
		logger.debug(className, function, "isSSApply[{}] isSSCancel[{}]", new Object[]{isSSApply, isSSCancel});
		
		// Scan Suspend
		if ( isSSApply ) {
			// SS
			String key = "changeEqpStatus" + "_"+ "inspector" + tabName + "_"+ "scansuspend" + "_"+ "true" + "_" + dbaddress;
			
			dpcMgr.sendChangeVarStatus(key, scsEnvId, dbaddress, DCP_i.ValidityStatus.OPERATOR_INHIBIT);
			
			chkDPMs[y][indexSS].setValue(true);
			
		} else if ( isSSCancel ) {
						
			String key = "changeEqpStatus" + "_"+ "inspector" + tabName + "_"+ "scansuspend" + "_"+ "false" + "_" + dbaddress;
			
			dpcMgr.sendChangeVarStatus(key, scsEnvId, dbaddress, DCP_i.ValidityStatus.VALID);
			
			chkDPMs[y][indexSS].setValue(false);
		} else {
			logger.warn(className, function, "Not a SS Action");
		}
		
		logger.end(className, function);
	}
	
	private void verifyAndSendMO(final int y, final String dbaddress, final int forcedStatus, final PointType pointType) {
		final String function = "verifyAndSendMO";
		logger.begin(className, function);
		logger.debug(className, function, "y[{}]", new Object[]{y, dbaddress, forcedStatus});
		logger.debug(className, function, "pointType[{}]", pointType);

		boolean isMO = DatabaseHelper.isMO(forcedStatus);
		logger.debug(className, function, "isMO[{}]", new Object[]{isMO});
		
		boolean isTicked = chkDPMs[y][indexMO].getValue();
		logger.debug(className, function, "isTicked[{}]", new Object[]{isTicked});
		
		boolean isMOApply = !isMO && isTicked;
		boolean isMOCancel = isMO && !isTicked;
		boolean isMOChanged = isMO && isTicked;
		
		logger.debug(className, function, "isMOApply[{}] isMOCancel[{}] isMOChanged[{}]", new Object[]{isMOApply, isMOCancel, isMOChanged});
		
		if ( moApplyWithoutReset && isMOChanged ) {	
			boolean changed = false;
			if ( PointType.dci == pointType ) {
				int moIndex = lstValues[y].getSelectedIndex();
				if ( moIndex != intValuesOri[y] ) {
					intValuesOri[y] = moIndex;
					changed = true;
				}
			} else if ( PointType.aci == pointType || PointType.sci == pointType ) {
				String moSValue = txtValues[y].getText();
				if ( moSValue != strValuesOri[y] ) {
					strValuesOri[y] = moSValue;
					changed = true;
				}
			}
			if ( changed ) isMOChanged = true;
		}
		
		if ( isMOApply || isMOChanged || isMOCancel ) {

			int moIValue = 0;
			
			float moFValue	= 0.0f;
			
			String moSValue	= "";	
			
			if ( PointType.dci == pointType ) {
				
				int moIndex = lstValues[y].getSelectedIndex();
				
				logger.debug(className, function, "moIndex[{}]", moIndex);
				
				String valueTable = DatabaseHelper.getAttributeValue(dbaddress, PointName.dalValueTable.toString(), dbvalues);
				logger.debug(className, function, "sforcedStatus[{}]", valueTable);

				String sValue	= DatabaseHelper.getArrayValues(valueTable, dalValueTableValueColIndex, moIndex);
				sValue			= DatabaseHelper.removeDBStringWrapper(sValue);
				
				logger.debug(className, function, "sValue[{}]", sValue);
				try {
					moIValue = Integer.parseInt(sValue);
				} catch ( NumberFormatException e ) {
					logger.warn(className, function, "NumberFormatException[{}]", e.toString());
					logger.warn(className, function, "Integer.parseInt({})", sValue);
				}

			} else if ( PointType.aci == pointType || PointType.sci == pointType ) {
				
				moSValue = txtValues[y].getText();
				
				logger.debug(className, function, "moSValue[{}]", moSValue);
				
				if ( PointType.aci == pointType ) {
					
					try {
						moFValue = Float.parseFloat(moSValue);
					} catch ( NumberFormatException e ) {
						logger.warn(className, function, "NumberFormatException[{}]", e.toString());
						logger.warn(className, function, "Float.parseFloat({})", moSValue);
					}
				}
			}
			
			boolean forceAction = isMOApply || isMOChanged;
			if ( isMOCancel ) forceAction = false;
			
			logger.debug(className, function, "moIValue[{}] moFValue[{}] moSValue[{}]", new Object[]{moIValue, moFValue, moSValue});
			
			String key = "changeEqpStatus" + "_"+ "inspector" + tabName + "_"+ "manualoverride" + "_"+ forceAction + "_" + dbaddress;
			logger.debug(className, function, "key[{}]", key);
			
			if ( PointType.dci == pointType ) {
				
				logger.debug(className, function, "key[{}] scsEnvId[{}] dbaddress[{}] forceAction[{}] moIValue[{}]"
						, new Object[]{key, scsEnvId, dbaddress, forceAction, moIValue});
				
				dpcMgr.sendChangeVarForce ( key, scsEnvId, dbaddress, forceAction, moIValue );
				
			} else if ( PointType.aci == pointType ) {
				
				logger.debug(className, function, "key[{}] scsEnvId[{}] dbaddress[{}] forceAction[{}] moFValue[{}]"
						, new Object[]{key, scsEnvId, dbaddress, forceAction, moFValue});
				
				dpcMgr.sendChangeVarForce ( key, scsEnvId, dbaddress, forceAction, moFValue );
				
			} else if ( PointType.sci == pointType ) {
				
				logger.debug(className, function, "key[{}] scsEnvId[{}] dbaddress[{}] forceAction[{}] moSValue[{}]"
						, new Object[]{key, scsEnvId, dbaddress, forceAction, moSValue});
				
				dpcMgr.sendChangeVarForce ( key, scsEnvId, dbaddress, forceAction, moSValue );
				
			}
			
			chkDPMs[y][indexMO].setValue(forceAction);
			
		} else {
			logger.warn(className, function, "Not a MO Action");
		}

		logger.end(className, function);
	}
	
	private void selectCheckbox(ClickEvent event, int col) {
		final String function = "selectCheckbox";
		logger.begin(className, function);
		
		logger.debug(className, function, "col[{}]", col);

		CheckBox chk = (CheckBox) event.getSource();
		
		if ( null != chk ) {
			int rowBegin	= pageCounter.pageRowBegin;
			int rowEnd		= pageCounter.pageRowEnd;
			
			CheckBox selected = null;
			int row = -1;
			for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
				logger.debug(className, function, "y[{}] col[{}]", y, col);
				if ( chk == chkDPMs[y][col] ) {
					row = y;
					selected = chk;
					break;
				}
			}
			logger.debug(className, function, "row[{}] col[{}]", row, col);
			
			if ( null != selected ) {
				
				pageCounter.calc(pageIndex);
				int x = pageCounter.pageRowBegin + row;

				logger.debug(className, function, "pageCounter.pageRowBegin[{}] x[{}] row[{}]", new Object[]{pageCounter.pageRowBegin, x, row});
				
				if ( -1 != row ) {
					
					String dbaddress = addresses[x];
					String point	= DatabaseHelper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = DatabaseHelper.getPointType(point);
						Integer iForcedStatus = getForcedStatus(dbaddress, row, pointType);
						if ( null != iForcedStatus ) {
							
							int forcedStatus = iForcedStatus;

							boolean isAI = DatabaseHelper.isAI(forcedStatus);
							boolean isAITicked = chkDPMs[row][indexAI].getValue();
							logger.debug(className, function, "isAI[{}] isAITicked[{}]", new Object[]{isAI, isAITicked});

							boolean isSS = DatabaseHelper.isSS(forcedStatus);
							boolean isSSTicked = chkDPMs[row][indexSS].getValue();
							logger.debug(className, function, "isSS[{}] isSSTicked[{}]", new Object[]{isSS, isSSTicked});

							boolean isMO = DatabaseHelper.isMO(forcedStatus);
							boolean isMOTicked = chkDPMs[row][indexMO].getValue();
							logger.debug(className, function, "isMO[{}] isMOTicked[{}]", new Object[]{isMO, isMOTicked});
							
							boolean invalid = false;
							if ( isAITicked && ( ( isSS || isSSTicked ) || ( isMO || isMOTicked ) ) ) {
								logger.warn(className, function, "AI Tick IS INVALID");
								
								invalid = true;
							} else 
							if ( isSSTicked && ( ( isAI || isAITicked ) || ( isMO || isMOTicked ) ) ) {
								logger.warn(className, function, "SS Tick IS INVALID");
								
								invalid = true;
							} else 
							if ( isMOTicked && ( ( isAI || isAITicked ) || ( isSS || isSSTicked ) ) ) {
								logger.warn(className, function, "MO Tick IS INVALID");
								
								invalid = true;
							}
							
							logger.debug(className, function, "invalid[{}]", invalid);
							if ( invalid ) {
								logger.debug(className, function, "set value to false");
								chk.setValue(false);
							}

						} else {
							logger.warn(className, function, "iForcedStatus IS NULL");
						}
					} else {
						logger.warn(className, function, "point IS NULL");
					}
				} else {
					logger.warn(className, function, "row IS -1");
				}
			} else {
				logger.warn(className, function, "selected IS NULL");
			}
		} else {
			logger.warn(className, function, "chk IS NULL");
		}
		
		logger.end(className, function);
	}
	
	private Integer getForcedStatus(final String dbaddress, final int col, final PointType pointType) {
		final String function = "getForcedStatus";
		logger.begin(className, function);
		
		Integer ret = null;
		
		logger.debug(className, function, "dbaddress[{}] col[{}] pointType[{}]", new Object[]{dbaddress, col, pointType});
		
		if ( null != chkDPMs[col] ) {

			String sForcedStatusPoint = null;
			if ( PointType.dci == pointType ) {
				sForcedStatusPoint = PointName.dfoForcedStatus.toString();
			} else if ( PointType.aci == pointType ){
				sForcedStatusPoint = PointName.afoForcedStatus.toString();
			} else if ( PointType.sci == pointType ) {
				sForcedStatusPoint = PointName.sfoForcedStatus.toString();
			}
			logger.trace(className, function, "pointType[{}] sForcedStatusPoint[{}]", pointType, sForcedStatusPoint);
			
			String sForcedStatus = getStatusValue(dbaddress, sForcedStatusPoint);
			logger.trace(className, function, "sForcedStatus[{}]", sForcedStatus);
			
			if ( null != sForcedStatus ) {
				try {
					ret = Integer.parseInt(sForcedStatus);
					logger.trace(className, function, "ret[{}]", ret);
				} catch ( NumberFormatException e ) {
					logger.warn(className, function, "NumberFormatException[{}]", e.toString());
					logger.warn(className, function, "Integer.parseInt({})", sForcedStatus);
				}
			} else {
				logger.trace(className, function, "sForcedStatus IS NULL");
			}
			
		} else {
			logger.warn(className, function, "chkDPMs[{}] IS NULL", dbaddress);
		}
			
		logger.end(className, function);
		
		return ret;
	}
	
	private void sendControl(ClickEvent event) {
		final String function = "sendControl";
		logger.begin(className, function);
		
		UIButtonToggle button = (UIButtonToggle) event.getSource();
		int btnIndex = -1;
		for ( int i = 0 ; i < btnApplys.length ; ++i ) {
			if ( btnApplys[i] == button ) {
				btnIndex = i;
				break;
			}
		}
		
		logger.debug(className, function, "btnIndex[{}]", btnIndex);
		
		pageCounter.calc(pageIndex);
	
		int x = pageCounter.pageRowBegin + btnIndex;
		int y = btnIndex;

		logger.debug(className, function, "pageCounter.pageRowBegin[{}] x[{}] y[{}]", new Object[]{pageCounter.pageRowBegin, x, y});
		
		if ( -1 != btnIndex ) {

			String dbaddress = addresses[x];
			String point	= DatabaseHelper.getPoint(dbaddress);
			if ( null != point ) {
				PointType pointType = DatabaseHelper.getPointType(point);
				Integer iForcedStatus = getForcedStatus(dbaddress, y, pointType);
				if ( null != iForcedStatus ) {
					int forcedStatus = iForcedStatus;
					verifyAndSendAI(y, dbaddress, forcedStatus);
					verifyAndSendSS(y, dbaddress, forcedStatus);
					verifyAndSendMO(y, dbaddress, forcedStatus, pointType);
				} else {
					logger.debug(className, function, "iForcedStatus[{}] IS NULL");
				}
			}
		} else {
			logger.warn(className, function, "btnIndex[{}] IS INVALID", btnIndex);
		}
		logger.end(className, function);
		
	}
	
//	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
//		this.messageBoxEvent = messageBoxEvent;
	}
	
	private Button btnUp			= null;
	private InlineLabel lblPageNum	= null;
	private Button btnDown			= null;

	private FlexTable flexTableAttibutes = null;
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private VerticalPanel vpCtrls = null;
	private DockLayoutPanel basePanel = null;
	private Panel rootPanel = null;
	@Override
	public void init() {
		final String function = "init";
		logger.begin(className, function);

		vpCtrls = new VerticalPanel();
		vpCtrls.addStyleName(strCssPrefix+"panel-vpCtrls");

		btnUp = new Button();
		btnUp.addStyleName(strCssPrefix+"button-up");
		btnUp.setText(STR_UP);
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				logger.begin(className, function+" Begin");

				onButton((Button)event.getSource());

				logger.end(className, function+" End");
			}
		});
		
		lblPageNum = new InlineLabel();
		lblPageNum.addStyleName(strCssPrefix+"label-pagenum");
		lblPageNum.setText(STR_PAGER_DEFAULT);
		
		btnDown = new Button();
		btnDown.addStyleName(strCssPrefix+"button-down");
		btnDown.setText(STR_DOWN);
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				onButton((Button)event.getSource());

			}
		});	

		HorizontalPanel pageBar = new HorizontalPanel();
		pageBar.addStyleName(strCssPrefix+"panel-pageBar");

		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(btnUp);
		
		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(lblPageNum);
		
		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(btnDown);
		
		HorizontalPanel bottomBar = new HorizontalPanel();
		bottomBar.addStyleName(strCssPrefix+"panel-bottomBar");
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(pageBar);
		
		basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName(strCssPrefix+"panel-base");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(vpCtrls);

		// Auto close handle
		rootPanel = new FocusPanel();
		rootPanel.add(basePanel);
		((FocusPanel)rootPanel).addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ( null != event ) {
					if ( null != uiInspectorTabClickEvent ) uiInspectorTabClickEvent.onClick(); 
				} else {
					logger.debug(className, function, "event IS NULL");
				}
			}
		});
		rootPanel.addStyleName(strCssPrefix+"panel-root");
		
		logger.end(className, function);
	}
	
	@Override
	public Panel getMainPanel() {
		return rootPanel;
	}
	
	private UIInspectorTabClickEvent uiInspectorTabClickEvent = null;
	@Override
	public void setUIInspectorTabClickEvent(UIInspectorTabClickEvent uiInspectorTabClickEvent) {
		this.uiInspectorTabClickEvent = uiInspectorTabClickEvent;
	}
	
	@Override
	public void setDatabase(Database db) {
		database = db;
	}

	private void readProperties() {
		final String function = "readProperties";
		logger.begin(className, function);

		dalValueTableLength = ReadProp.readInt(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME
				, CONFIG_PREFIX+Attribute.dalValueTableLength.toString(), 12);
		
		dalValueTableLabelColIndex = ReadProp.readInt(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME
				, CONFIG_PREFIX+Attribute.dalValueTableLabelColIndex.toString(), 1);
		dalValueTableValueColIndex = ReadProp.readInt(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME
				, CONFIG_PREFIX+Attribute.dalValueTableValueColIndex.toString(), 4);
		
		moApplyWithoutReset = ReadProp.readBoolean(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME
				, CONFIG_PREFIX+Attribute.moApplyWithoutReset.toString(), false);
		
		logger.debug(className, function, "dalValueTableLength[{}]", dalValueTableLength);
		logger.debug(className, function, "dalValueTableLabelColIndex[{}]", dalValueTableLabelColIndex);
		logger.debug(className, function, "dalValueTableValueColIndex[{}]", dalValueTableValueColIndex);

		logger.debug(className, function, "moApplyWithoutReset[{}]", moApplyWithoutReset);
		
		logger.end(className, function);
	}
}
