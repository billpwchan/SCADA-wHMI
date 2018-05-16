package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.MessageBoxEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.page.PageCounter;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.UIButtonToggle;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorTag_i.Attribute;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointType;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTabClickEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.CtlMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.Stability;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Observer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;

public class UIInspectorTag implements UIInspectorTab_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static final String DICTIONARY_CACHE_NAME = UIInspector_i.strUIInspector;
	private static final String DICTIONARY_FILE_NAME = "inspectorpanel.tag.properties";
	private static final String CONFIG_PREFIX = "inspectorpanel.tag.";
	
	private final String INSPECTOR = UIInspectorTag_i.INSPECTOR;
	
	public static final String STR_EMPTY                = Database_i.STR_EMPTY;
	public static final String STR_UNDERSCORE           = Database_i.STR_UNDERSCORE;
	public static final String STR_COLON                = Database_i.STR_COLON;
	
	public static final String STR_INITCONDGL_VALID		= "1";	
	
	public static final String STR_UP					= "▲";
	public static final String STR_SPLITER				= " / ";
	public static final String STR_DOWN					= "▼";
	public static final String STR_PAGER_DEFAULT		= "1 / 1";
	
	public static final String STR_EXECUTE				= "Execute";
	
	public static final String STR_ALIAS				= Database_i.STR_ALIAS;
	public static final String STR_INITCONDGL			= PointName.initCondGL.toString();
	
	private static final String strPrefix				= "Executing command : ";
	private static final String strInit					= strPrefix+" "+"Initing Command";
	private static final String strSending				= strPrefix+" "+"Sending Command";
	private static final String strSucceed				= strPrefix+" "+"Succeed";
	private static final String strFailed				= strPrefix+" "+"Failed";

	private int byPassInitCond = 0;
	private int byPassRetCond = 0;
	private int sendAnyway = 0;
	
	// Static Attribute List
	private final String aioStaticAttibutes[]	= new String[] {PointName.label.toString()};
	private final String dioStaticAttibutes[]	= new String[] {PointName.label.toString()
			, PointName.valueTableDovName.toString(), PointName.valueTableLabel.toString(), PointName.valueTableValue.toString()};
	private final String sioStaticAttibutes[]	= new String[] {PointName.label.toString()};

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
		logger.begin(function);
		if ( null == attributesList.get(type) ) attributesList.put(type, new HashMap<String, String>());
		attributesList.get(type).put(key, value);
		logger.end(function);
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
		logger.debug(function, "this.parent[{}] this.scsEnvId[{}]", this.parent, this.scsEnvId);
	}
	
	@Override
	public void setAddresses(String[] addresses) {
		final String function = "setAddresses";
		logger.begin(function);
		
		this.addresses = addresses;
		
		if ( logger.isDebugEnabled() ) {
			logger.debug(function, "this.addresses.length[{}]", this.addresses.length);
			for ( int i = 0 ; i < this.addresses.length ; ++i ) {
				logger.debug(function, "this.addresses({})[{}]", i, this.addresses[i]);
			}
		}	
		
		logger.end(function);
	}
	
	@Override
	public String[] getAddresses() {
		return this.addresses;
	}
	
	private void updatePager() {
		final String function = "updatePager";
		logger.begin(function);
		
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
		
		logger.end(function);
	}
	
	private void updateLayout() {
		final String function = "updateLayout";
		logger.begin(function);
		
		pageCounter.calc(pageIndex);
		
		int pageSize = pageCounter.pageSize;
		
		int stopper = pageCounter.pageRowCount;
		
		logger.debug(function
				, "pageIndex[{}] pageSize[{}], stopper[{}]"
				, new Object[]{pageIndex, pageSize, stopper});
		
		for ( int i = 0 ; i < pageSize ; i++ ) {
			boolean visible = true;
			if ( i > stopper ) {
				visible = false;
			}
			widgetBoxes[i].setVisible(visible);
		}

		logger.end(function);
	}
	
	@Override
	public void buildWidgets(int numOfPointEachPage) {
		buildWidgets(this.addresses.length, numOfPointEachPage);
	}
	
	private int pageIndex = 0;
	private PageCounter pageCounter = null;
	
	private Panel[] widgetBoxes = null;
	private InlineLabel[] inlineLabels = null;
	private Panel[] controlboxes = null;
	private void buildWidgets(int numOfWidgets, int numOfPointEachPage) {
		final String function = "buildWidgets";
		logger.begin(function);
		
		logger.debug(function, "numOfWidgets[{}] numOfPointEachPage[{}]", numOfWidgets, numOfPointEachPage);
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, numOfPointEachPage);
			pageCounter.calc(pageIndex);
			
			int pageSize = pageCounter.pageSize;
			
			int stopper = pageCounter.pageRowCount;
			
			logger.debug(function, "pageIndex[{}] pageSize[{}], stopper[{}]"
					, new Object[]{pageIndex, pageSize, stopper});
			
			widgetBoxes	= new Panel[pageSize];
			for ( int i = 0 ; i < pageSize ; i++ ) {
				widgetBoxes[i] = new FlowPanel();
				widgetBoxes[i].addStyleName(strCssPrefix+"panel-widgetbox-"+i);
			}
			
			updatePager();
			updateLayout();

		} else {
			logger.warn(function, "vpCtrls IS NULL");
		}
		
		logger.end(function);
	}
	
	class ControlPoint {
		
		public static final String POINT		= "point";
		public static final String SCSENVID		= "scsenvid";
		public static final String DBADDRESS	= "dbaddress";
		public static final String VALUE		= "value";
		public static final String TEXTBOX		= "textbox";
		
		private Map<String, Object> map = new HashMap<String, Object>();
		public ControlPoint(String point, String scsenvid, String dbaddress) {
			map.put(POINT, point);
			map.put(SCSENVID, scsenvid);
			map.put(DBADDRESS, dbaddress);
		}
		public ControlPoint(String point, String scsenvid, String dbaddress, String value) {
			this(point, scsenvid, dbaddress);
			map.put(VALUE, value);
		}
		public ControlPoint(String point, String scsenvid, String dbaddress, TextBox textbox) {
			this(point, scsenvid, dbaddress);
			map.put(TEXTBOX, textbox);
		}
		public String getValue(String key) { return (String)map.get(key); }
		public TextBox getTextBox(String key) { return (TextBox) map.get(key); }
	}
	
	private Map<String, Map<String, String>> keyAndValuesStatic		= new LinkedHashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, Map<String, String>>();
	private Map<String, String> dbvalues							= new HashMap<String, String>();
	
	private Map<Widget, ControlPoint> widgetPoints		= new HashMap<Widget, ControlPoint>();
	private Map<String, Widget[]> widgetGroups			= new HashMap<String, Widget[]>();
	
	private Map<String, Widget> initCondGLAndWidget		= new HashMap<String, Widget>();
	private Map<String, String[]> addressAndInitCongGL	= new HashMap<String, String[]>();
	
	@Override
	public void updateValue(String strClientKey, Map<String, String> keyAndValue) {
		final String function = "updateValue";
		logger.begin(function);
		
		logger.trace(function, "strClientKey[{}]", strClientKey);
		
		DataBaseClientKey clientKey = new DataBaseClientKey(STR_UNDERSCORE, strClientKey);
		
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
		
		logger.end(function);
	}
	
	private void updateValue(boolean withStatic) {
		final String function = "updateValue";
		logger.begin(function);
		
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
		
		logger.end(function);
	}
	
	private void updateValueStatic(String key, Map<String, String> keyAndValue) {
		final String function = "updateValueStatic";
		logger.begin(function);

		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(INSPECTOR + tabName);
		clientKey.setStability(Stability.STATIC);
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(scsEnvId);
		clientKey.setAdress(parent);
		
		String strClientKey = clientKey.getClientKey();

		if ( strClientKey.equalsIgnoreCase(key) ) {
		
			initCondGLAndWidget.clear();
			addressAndInitCongGL.clear();
			
			vpCtrls.clear();

			pageCounter.calc(pageIndex);
			
			updatePager();

			int rowBegin	= pageCounter.pageRowBegin;
			int rowEnd		= pageCounter.pageRowEnd;
			
			int numOfWidgetShow	= rowEnd - rowBegin;
			
			if ( numOfWidgetShow > 0 ) {

//				widgetBoxes		= new VerticalPanel[numOfWidgetShow];
				inlineLabels	= new InlineLabel[numOfWidgetShow];
				controlboxes	= new FlowPanel[numOfWidgetShow];
				
				widgetPoints.clear();
				widgetGroups.clear();
				
				for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
					
					Panel widget = null;
					
					String dbaddress = this.addresses[x];
					String point = DatabaseHelper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = DatabaseHelper.getPointType(point);
						if ( PointType.dio == pointType ) {
							widget = updateDioControl(dbaddress, y);
						} else if ( PointType.aio == pointType ) {
							widget = updateAioControl(dbaddress, y);
						} else if ( PointType.sio == pointType ) {
							widget = updateSioControl(dbaddress, y);
						}
					}
					
					if ( null != widget ) {
						vpCtrls.add(widget);
					}
				}
			}
			
			createInitConds();
			connectInitConds();
			
		}

		logger.end(function);
	}
	
	private Panel updateDioControl(String dbaddress, int row) {
		final String function = "updateDioControl";
		logger.begin(function);
		
		logger.trace(function, "address[{}] row[{}]", dbaddress, row);
		
		inlineLabels[row] = new InlineLabel();
		inlineLabels[row].addStyleName(strCssPrefix+"label-"+row);

		String label = DatabaseHelper.getAttributeValue(dbaddress, PointName.label.toString(), dbvalues);
		label = DatabaseHelper.removeDBStringWrapper(label);
		logger.trace(function, "label[{}]", label);
		if ( null != label ) inlineLabels[row].setText(label);
		
		controlboxes[row] = new FlowPanel();
		controlboxes[row].addStyleName(strCssPrefix+"panel-controlbox-"+row);
		
		final String valueTableDovName = DatabaseHelper.getAttributeValue(dbaddress, PointName.valueTableDovName.toString(), dbvalues);
		logger.trace(function, "valueTableDovName[{}]", valueTableDovName);
		
		final String valueTableLabel = DatabaseHelper.getAttributeValue(dbaddress, PointName.valueTableLabel.toString(), dbvalues);
		logger.trace(function, "valueTableLabel[{}]", valueTableLabel);
		
		final String valueTableValue = DatabaseHelper.getAttributeValue(dbaddress, PointName.valueTableValue.toString(), dbvalues);
		logger.trace(function, "valueTableValue[{}]", valueTableValue);
		
		if(null!=valueTableDovName&&null!=valueTableLabel&&null!=valueTableValue) {
			final String dovNames []    = DatabaseHelper.getValueTableColumn(valueTableDovName);
			final String labels []      = DatabaseHelper.getValueTableColumn(valueTableLabel);
			final String values []      = DatabaseHelper.getValueTableColumn(valueTableValue);

			// Loop Control Point
			List<UIButtonToggle> btnOptions = new LinkedList<UIButtonToggle>();
			for ( int i = 0 ; i < dovNames.length ; ++i ) {
				
				if ( labels[i].length() == 0  ) break;
				
				UIButtonToggle btnOption = new UIButtonToggle(labels[i]);
				btnOption.addStyleName(strCssPrefix+"button-"+row+"-"+i);
				btnOption.setHightLightStyleName(strCssPrefix+"button-selected-"+row+"-"+i);
				btnOption.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						onButton(event);
					}
				});
				
				btnOption.setEnabled(false);
				
				btnOptions.add(btnOption);
				
				controlboxes[row].add(btnOption);
				
				widgetGroups.put(dbaddress, btnOptions.toArray(new UIButtonToggle[0]));
				
				widgetPoints.put(btnOption, new ControlPoint(PointType.dio.toString(), scsEnvId, dbaddress, values[i]));

				initCondGLAndWidget.put(dbaddress+STR_COLON+dovNames[i]+STR_INITCONDGL, btnOption);
			}
		}

//		widgetBoxes[row] = new VerticalPanel();
//		widgetBoxes[row].addStyleName("project-gwt-panel-inspector-"+tabName+"-control");
		widgetBoxes[row].clear();
		widgetBoxes[row].add(inlineLabels[row]);
		widgetBoxes[row].add(controlboxes[row]);
		
		logger.end(function);
		
		return widgetBoxes[row];
	}
	
	private Panel updateAioControl(String dbaddress, int row) {
		final String function = "updateAioControl";
		logger.begin(function);
		
		logger.trace(function, "address[{}] row[{}]", dbaddress, row);
		
		inlineLabels[row] = new InlineLabel();
		inlineLabels[row].addStyleName(strCssPrefix+"label-"+row);

		String label = DatabaseHelper.getAttributeValue(dbaddress, PointName.label.toString(), dbvalues);
		label = DatabaseHelper.removeDBStringWrapper(label);
		logger.trace(function, "label[{}]", label);
		if ( null != label ) inlineLabels[row].setText(label);
		
		controlboxes[row] = new FlowPanel();
		controlboxes[row].addStyleName(strCssPrefix+"panel-controlbox-"+row);

		List<UIButtonToggle> btnOptions = new LinkedList<UIButtonToggle>();
		
		TextBox textBox = new TextBox();
		textBox.addStyleName(strCssPrefix+"textbox-"+row);
		controlboxes[row].add(textBox);

		UIButtonToggle btnOption = new UIButtonToggle(label);
		btnOption.addStyleName(strCssPrefix+"button-"+row);
		btnOption.setHightLightStyleName(strCssPrefix+"button-selected-"+row);
		btnOption.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				onButton(event);
			}
		});
		
		btnOption.setEnabled(false);
		
		btnOptions.add(btnOption);
		
		controlboxes[row].add(btnOption);
		
		widgetGroups.put(dbaddress, btnOptions.toArray(new UIButtonToggle[0]));
		
		widgetPoints.put(btnOption, new ControlPoint(PointType.aio.toString(), scsEnvId, dbaddress, textBox));

		initCondGLAndWidget.put(dbaddress+STR_INITCONDGL, btnOption);
		
//		widgetBoxes[row] = new VerticalPanel();
//		widgetBoxes[row].addStyleName("project-gwt-panel-inspector-"+tabName+"-control-"+row);
		widgetBoxes[row].clear();
		widgetBoxes[row].add(inlineLabels[row]);
		widgetBoxes[row].add(controlboxes[row]);
		
		logger.end(function);
		
		return widgetBoxes[row];
	}
		
	private Panel updateSioControl(String dbaddress, int row) {
		final String function = "updateSioControl";
		logger.begin(function);
		
		logger.trace(function, "address[{}] row[{}]", dbaddress, row);
		
		inlineLabels[row] = new InlineLabel();
		inlineLabels[row].addStyleName(strCssPrefix+"label-"+row);

		String label = DatabaseHelper.getAttributeValue(dbaddress, PointName.label.toString(), dbvalues);
		label = DatabaseHelper.removeDBStringWrapper(label);
		logger.trace(function, "label[{}]", label);
		if ( null != label ) inlineLabels[row].setText(label);
		
		controlboxes[row] = new FlowPanel();
		controlboxes[row].addStyleName(strCssPrefix+"panel-controlbox-"+row);

		List<UIButtonToggle> btnOptions = new LinkedList<UIButtonToggle>();
		
		TextBox textBox = new TextBox();
		textBox.addStyleName(strCssPrefix+"textbox-"+row);
		controlboxes[row].add(textBox);

		UIButtonToggle btnOption = new UIButtonToggle(label);
		btnOption.addStyleName(strCssPrefix+"button-"+row);
		btnOption.setHightLightStyleName(strCssPrefix+"button-selected-"+row);
		btnOption.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				onButton(event);
			}
		});
		
		btnOption.setEnabled(false);
		
		btnOptions.add(btnOption);
		
		controlboxes[row].add(btnOption);
		
		widgetGroups.put(dbaddress, btnOptions.toArray(new UIButtonToggle[0]));
		
		widgetPoints.put(btnOption, new ControlPoint(PointType.sio.toString(), scsEnvId, dbaddress, textBox));

		initCondGLAndWidget.put(dbaddress+STR_INITCONDGL, btnOption);
		
//		widgetBoxes[row] = new VerticalPanel();
//		widgetBoxes[row].addStyleName("project-gwt-panel-inspector-"+tabName+"-control-"+row);
		widgetBoxes[row].clear();
		widgetBoxes[row].add(inlineLabels[row]);
		widgetBoxes[row].add(controlboxes[row]);
		
		logger.end(function);
		
		return widgetBoxes[row];
	}
	
	private void updateValueDynamic(String clientKey, Map<String, String> keyAndValue) {
		final String function = "updateValueDynamic";
		logger.begin(function);
		
		pageCounter.calc(pageIndex);
//		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		for ( int x = rowBegin ; x < rowEnd ; ++x ) {
			String dbaddress = this.addresses[x];
			
			String [] initCondGLs = addressAndInitCongGL.get(dbaddress);
			if ( null != initCondGLs ) {
				for ( String initCondGL : initCondGLs ) {
					String value = dbvalues.get(initCondGL);
					if ( null != value ) {
						UIButtonToggle widget = (UIButtonToggle)initCondGLAndWidget.get(initCondGL);
						if ( null != widget ) {
							if ( value.equals(STR_INITCONDGL_VALID) ) {
								widget.setEnabled(true);
							} else {
								if ( widget.isHightLight() ) widget.setHightLight(false);
								widget.setEnabled(false);
							}
						} else {
							logger.warn(function, "uiButtonToggle IS NULL");
						}
					} else {
						logger.warn(function, "value IS NULL");
					}
				}				
			}
		}
		
		updateBtn(dbvalues);
		updateMsgBox(dbvalues);
		
		logger.end(function);
	}
	
//	private LinkedList<String> controldios = new LinkedList<String>();
	
	private CtlMgr ctlMgr = null;
	private Observer observer = null;
	private Subject controlMgrSubject = null;
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(function);
		
		readProperties();
		
		String[] dbaddresses = null;
		
		logger.debug(function, "this.addresses.length[{}]", this.addresses.length);

		ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
		for ( int i = 0 ; i < this.addresses.length ; ++i ) {
			String dbaddress = this.addresses[i];
			logger.debug(function, "dbaddress[{}]", dbaddress);
			
			String point = DatabaseHelper.getPoint(dbaddress);
			PointType pointType = DatabaseHelper.getPointType(point);
				
			if ( PointType.dio == pointType ) {
				for ( String attribute : dioStaticAttibutes ) {
					dbaddressesArrayList.add(dbaddress+attribute);
				}
			} else if ( PointType.aio == pointType ) {
				for ( String attribute : aioStaticAttibutes ) {
					dbaddressesArrayList.add(dbaddress+attribute);
				}
			} else if ( PointType.sio == pointType ) {
				for ( String attribute : sioStaticAttibutes ) {
					dbaddressesArrayList.add(dbaddress+attribute);
				}
			}
		}
		dbaddresses = dbaddressesArrayList.toArray(new String[0]);

		
		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(INSPECTOR + tabName);
		clientKey.setStability(Stability.STATIC);
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(scsEnvId);
		clientKey.setAdress(parent);
		
		String strClientKey = clientKey.toClientKey();
		
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
					
				String strClientKey = clientKey.getClientKey();

				if ( strClientKey.equalsIgnoreCase(key) ) {
					
						String [] dbaddresses	= database.getKeyAndAddress(key);
						String [] dbvalues		= database.getKeyAndValues(key);
					Map<String, String> keyAndValue = new HashMap<String, String>();
					for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
						keyAndValue.put(dbaddresses[i], dbvalues[i]);
					}
					updateValue(key, keyAndValue);
				}
			}
		});
		
		{
			ctlMgr = CtlMgr.getInstance(tabName);
			controlMgrSubject = ctlMgr.getSubject();
			
			observer = new Observer() {
				@Override
				public void setSubject(Subject subject){
					this.subject = subject;
					this.subject.attach(this);
				}
				
				@Override
				public void update() {
					if ( null != messageBoxEvent ) {
						
//						String message = subject.getState();
//						
//						String msgPrefix	= "Executing command : ";
//						String msgSending	= "Sending Command";
//						String msgSucceed	= "Succeed";
//						String msgFailed	= "Failed";
//						if ( message.equals("COMMAND SENT") 
//								|| message.equals("\"SendIntCommand sent\"") 
//								|| message.equals("\"SendStringCommand sent\"") 
//								|| message.equals("\"SendFloatCommand sent\"") ) {
//							message = msgPrefix + msgSending;
//						} else if ( message.equals("\"command executed successfully\"") ) {
//							message = msgPrefix + msgSucceed;
//						} else if ( message.equals("COMMAND FAILED") ) {
//							message = msgPrefix + msgFailed;
//						}
//						
//						messageBoxEvent.setMessage(message);
					}
				}
			};
			
			observer.setSubject(controlMgrSubject);			
		}
		
		logger.end(function);
	}
	
	void createInitConds() {
		final String function = "createInitConds";
		logger.begin(function);
		
		for ( int x = 0 ; x < this.addresses.length ; ++x ) {
			String dbaddress = this.addresses[x];

			String point = DatabaseHelper.getPoint(dbaddress);
			PointType pointType = DatabaseHelper.getPointType(point);
				
			if ( PointType.dio == pointType ) {				
				final String valueTableDovName = DatabaseHelper.getAttributeValue(dbaddress, PointName.valueTableDovName.toString(), dbvalues);
				logger.trace(function, "valueTableDovName[{}]", valueTableDovName);
				
				final String valueTableLabel = DatabaseHelper.getAttributeValue(dbaddress, PointName.valueTableLabel.toString(), dbvalues);
				logger.trace(function, "valueTableLabel[{}]", valueTableLabel);
				
				final String valueTableValue = DatabaseHelper.getAttributeValue(dbaddress, PointName.valueTableValue.toString(), dbvalues);
				logger.trace(function, "valueTableValue[{}]", valueTableValue);
				
				if(null!=valueTableDovName&&null!=valueTableLabel&&null!=valueTableValue) {
					final String dovNames []    = DatabaseHelper.getValueTableColumn(valueTableDovName);
					final String labels []      = DatabaseHelper.getValueTableColumn(valueTableLabel);
					
					List<String> initCondGLList = new LinkedList<String>();
					for ( int i = 0 ; i < dovNames.length ; ++i ) {
						
						if ( labels[i].length() == 0  ) break;
						
						String pointaddress = dbaddress+STR_COLON+dovNames[i];
						initCondGLList.add(pointaddress+STR_INITCONDGL);
					}

					addressAndInitCongGL.put(dbaddress, initCondGLList.toArray(new String[0]));
				} else {
					logger.warn(function, "valueTable IS NULL");
				}
			} else if ( PointType.aio == pointType ) {
				
				addressAndInitCongGL.put(dbaddress, new String[]{dbaddress+STR_INITCONDGL});
				
			} else if ( PointType.sio == pointType ) {
				
				addressAndInitCongGL.put(dbaddress, new String[]{dbaddress+STR_INITCONDGL});
				
			} else {
				logger.warn(function, "pointType[{}] IS UNKNOW", pointType);
			}
						
		}
		
		logger.end(function);
	}
	
	void connectInitConds() {
		final String function = "connectInitConds";
		logger.begin(function);
		
		// Read dynamic
		{
			final String functionEmb = "multiReadValue";
			logger.begin(functionEmb);
			
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
				for ( String address : addressAndInitCongGL.keySet() ) {
					for ( String initCondGL : addressAndInitCongGL.get(address) ) {
						dbaddressesArrayList.add(initCondGL);
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}
			
			if ( logger.isDebugEnabled() ) {
				logger.debug(function, "strClientKey[{}] scsEnvId[{}]", strClientKey, scsEnvId);
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.debug(function, "dbaddresses({})[{}]", i, dbaddresses[i]);
				}				
			}

			database.subscribe(strClientKey, dbaddresses, new DatabaseEvent() {
				
				@Override
				public void update(String key, String[] value) {
					// TODO Auto-generated method stub
					
				}
				
			});
			logger.end(function);
		}
	}
	
	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(function);
		
		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(INSPECTOR + tabName);
		clientKey.setStability(Stability.DYNAMIC);
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(scsEnvId);
		clientKey.setAdress(parent);
		
		String strClientKey = clientKey.toClientKey();
		database.unSubscribe(strClientKey);
		
		logger.end(function);
	}
	
	private Button btnExecute = null;
	
	private Button btnUp			= null;
	private InlineLabel lblPageNum	= null;
	private Button btnDown			= null;
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private Panel vpCtrls = null;
	private DockLayoutPanel basePanel = null;
	private Panel rootPanel = null;
	@Override
	public void init() {
		final String function = "init";
		logger.begin(function);

		vpCtrls  = new FlowPanel();
		vpCtrls.addStyleName(strCssPrefix+"panel-vpCtrls");
		
		btnExecute = new Button();
		btnExecute.getElement().getStyle().setPadding(10, Unit.PX);
		btnExecute.setText(STR_EXECUTE);
		btnExecute.addStyleName(strCssPrefix+"button-execute");
		btnExecute.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				logger.begin(function);
				
				onButton(event);
				
				logger.end(function);
			}
		});
//		btnExecute.setEnabled(false);
		
		btnUp = new Button();
		btnUp.addStyleName(strCssPrefix+"button-up");
		btnUp.setText(STR_UP);
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				logger.begin(function);
				
				onButton(event);
				
				logger.end(function);
			}
		});
		
		lblPageNum = new InlineLabel();
		lblPageNum.addStyleName(strCssPrefix+"button-pagenum");
		lblPageNum.setText(STR_PAGER_DEFAULT);
		
		btnDown = new Button();
		btnDown.addStyleName(strCssPrefix+"button-down");
		btnDown.setText(STR_DOWN);
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				logger.begin(function);
				
				onButton(event);
				
				logger.end(function);
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
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(btnExecute);
		
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
					logger.debug(function, "event IS NULL");
				}
			}
		});
		rootPanel.addStyleName(strCssPrefix+"panel-root");
		
		logger.end(function);
	}
	
	@Override
	public Panel getMainPanel() {
		return rootPanel;
	}
	
	private void untoggleButtonGroup (UIButtonToggle[] btnGroup ) {
		for ( UIButtonToggle btn : btnGroup ) {
			btn.setHightLight(false);
		}
	}
	private void toggleButtonGroup ( UIButtonToggle btnSource, UIButtonToggle[] btnGroup ) {
		boolean toggled = btnSource.isHightLight();
		if ( !toggled ) {
			for ( UIButtonToggle btn : btnGroup ) {
				if ( btn != btnSource )
					btn.setHightLight(false);
			}
			btnSource.setHightLight(true);
		} else {
			btnSource.setHightLight(false);
		}
	}
	private void toggleButtonGroup ( Widget btnSource, Widget[] btnGroup ) {
		toggleButtonGroup((UIButtonToggle)btnSource, (UIButtonToggle[])btnGroup);
	}
	
	private Map<String, Widget[]> getGroups() { return widgetGroups; }
	private Widget[] getSelectedGroup(Widget btn) {
		Widget[] targetGroups = null;
		for ( String address : widgetGroups.keySet() ) {
			if ( targetGroups != null ) break;
			Widget[] widgets = widgetGroups.get(address);
			for ( Widget widget : widgets ) {
				if ( targetGroups != null ) break;
				if ( widget == btn ) {
					targetGroups = widgets;
				}
			}
		}
		return targetGroups;
	}
	
	private Widget getActivateWidget() {
		final String function = "getActivateWidget";
		logger.begin(function);
		
		Widget target = null;
		for ( String address : widgetGroups.keySet() ) {
			if ( target != null ) break;
			Widget[] widgets = widgetGroups.get(address);
			for ( Widget widget : widgets ) {
				if ( target != null ) break;
				if ( null != widget ) {
					if ( widget instanceof TextBox ) {
						TextBox txt = (TextBox)widget;
						String s = txt.getText();
						if ( s.length() > 0 ) {
							target = widget;
						}
					} else if ( widget instanceof UIButtonToggle ) {
						UIButtonToggle btn = (UIButtonToggle)widget;
						boolean b = btn.isHightLight();
						if ( b ) {
							target = widget;
						}
					} else {
						logger.warn(function, "widget IS NOT SUPPORTED");
					}
				} else {
					logger.warn(function, "btn IS NULL");
				}
			}
		}
		logger.end(function);
		return target;
	}
	
	private void onButton(ClickEvent event) {
		final String function = "onButton";
		logger.begin(function);

		Button btn = (Button)event.getSource();
		if ( btn instanceof UIButtonToggle ) {
			
			logger.debug(function, "Button IS UIButtonToggle");
			
			Widget[] targetGroups = getSelectedGroup(btn);
			if ( null != targetGroups ) {
				toggleButtonGroup(btn, targetGroups);
				
				logger.debug(function, "Un-toggle Buttons");
				Map<String, Widget[]> groups = getGroups();
				if ( null != groups ) {
					for ( Entry<String, Widget[]> keyValue : groups.entrySet() ) {
						if ( null != keyValue ) {
							String key = keyValue.getKey();
							logger.debug(function, "Un-toggle Buttons group[{}]", key);
							Widget[] value = keyValue.getValue();
							if ( value != targetGroups ) {
								untoggleButtonGroup((UIButtonToggle[])value);
							}
							else {
								logger.warn(function, "Un-toggle Buttons group[{}] group IS NULL", key);
							}
						}
					}
				}
			}
			
		} else if ( btn instanceof Button ) {
			logger.debug(function, "Button IS Button");
			
			if ( btnExecute == btn ) {
				
				logger.debug(function, "btn IS btnExecute");

				// Clean-up MessageBox
				if ( null != messageBoxEvent ) messageBoxEvent.setMessage(STR_EMPTY);
				
				Widget widget = getActivateWidget();
				
				if ( null != widget ) {
					
					// Reset the Widget
					if ( widget instanceof UIButtonToggle ) {
						((UIButtonToggle) widget).setHightLight(false);
					} else if ( widget instanceof TextBox ) {
						((TextBox) widget).setText(STR_EMPTY);
					}
					
					// Send Control
					if ( widgetPoints.containsKey(widget) ) {

						ControlPoint controlPoint = widgetPoints.get(widget);
						
						if ( null != controlPoint ) {
							
							String sPoint		= controlPoint.getValue(ControlPoint.POINT);
							String sScsEnvId	= controlPoint.getValue(ControlPoint.SCSENVID);
							String sDbAddress	= controlPoint.getValue(ControlPoint.DBADDRESS);
							
							logger.debug(function, "sPoint[{}]", sPoint);
							logger.debug(function, "sScsEnvId[{}]", sScsEnvId);
							logger.debug(function, "sDbAddress[{}]", sDbAddress);
							
							subscribe(sDbAddress+PointName.execStatus.toString());
							
							if ( ! sDbAddress.startsWith(STR_ALIAS) ) sDbAddress = STR_ALIAS + sDbAddress;
							
							btnExecute.setEnabled(false);
							
							if ( 0 == sPoint.compareTo(PointType.dio.toString()) ) {
								
								String sValue		= controlPoint.getValue(ControlPoint.VALUE);
								logger.debug(function, "sValue[{}]", sValue);
								
								logger.debug(function, "controlPoint.dbaddress[{}] Integer [{}]", sDbAddress, Integer.parseInt(sValue));
								
								logger.debug(function, "byPassInitCond[{}] byPassRetCond[{}] sendAnyway[{}]", new Object[]{byPassInitCond, byPassRetCond, sendAnyway});
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, Integer.valueOf(sValue), byPassInitCond, byPassRetCond, sendAnyway);
								
							} else if ( 0 == sPoint.compareTo(PointType.aio.toString()) ) {
								
								TextBox textBox	= controlPoint.getTextBox(ControlPoint.TEXTBOX);
								String sValue = textBox.getText();
								logger.debug(function, "controlPoint.dbaddress[{}] Float [{}]", sDbAddress, Float.parseFloat(sValue));
								
								logger.debug(function, "byPassInitCond[{}] byPassRetCond[{}] sendAnyway[{}]", new Object[]{byPassInitCond, byPassRetCond, sendAnyway});
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, Float.parseFloat(sValue), byPassInitCond, byPassRetCond, sendAnyway);
								
							} else if ( 0 == sPoint.compareTo(PointType.sio.toString()) ) {
								
								TextBox textBox	= controlPoint.getTextBox(ControlPoint.TEXTBOX);
								String sValue = textBox.getText();
								logger.debug(function, "controlPoint.dbaddress[{}] String [{}]", sDbAddress, sValue);
								
								logger.debug(function, "byPassInitCond[{}] byPassRetCond[{}] sendAnyway[{}]", new Object[]{byPassInitCond, byPassRetCond, sendAnyway});
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, sValue, byPassInitCond, byPassRetCond, sendAnyway);

							} else {
								logger.warn(function, "INVALID controlPoint.point");
							}
						} else {
							logger.warn(function, "controlPoint IS NULL");
						}
					} else {
						logger.warn(function, "controlPoint NOT CONTAIN the Key");
					}
				} else {
					logger.warn(function, "controlPoint widget IS NULL");
				}
			} else if ( btn == btnUp || btn == btnDown ) {
				if ( btn == btnUp) {
					--pageIndex;
				} else if ( btn == btnDown ) {
					++pageIndex;
				}
				updatePager();
				updateLayout();
				updateValue(true);
			}
		}
		logger.end(function);
	}

	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
		this.messageBoxEvent = messageBoxEvent;
	}
	
	private UIInspectorTabClickEvent uiInspectorTabClickEvent = null;
	@Override
	public void setUIInspectorTabClickEvent(UIInspectorTabClickEvent uiInspectorTabClickEvent) {
		this.uiInspectorTabClickEvent = uiInspectorTabClickEvent;
	}
	
	private void updateBtn(Map<String, String> keyAndValue) {
		final String function = "updateMsgBox";
		logger.begin(function);
		for ( Entry<String, String> entry : keyAndValue.entrySet() ) {
			String key = entry.getKey();
			String value = entry.getValue();
			if ( null != key && null != value ) {
				if ( key.endsWith(PointName.execStatus.toString()) ) {
					logger.debug(function, "key[{}] value[{}]", key, value);
					if ( null != value && value.length() > 0 ) {
						int intValue = Integer.parseInt(value);
						switch (intValue) {
						case 0: 
						case 1: btnExecute.setEnabled(false);	break;
						case 2: btnExecute.setEnabled(false);	break;
						case 3: btnExecute.setEnabled(true);	break;
						case 4:
						case 5:
						case 6: btnExecute.setEnabled(true);
								break;
						}
					}
				}
			}
		}
		logger.end(function);
	}
	
	private void updateMsgBox(Map<String, String> keyAndValue) {
		final String function = "updateMsgBox";
		logger.begin(function);
		for ( Entry<String, String> entry : keyAndValue.entrySet() ) {
			String key = entry.getKey();
			String value = entry.getValue();
			if ( null != key && null != value ) {
				if ( key.endsWith(PointName.execStatus.toString()) ) {
					logger.debug(function, "key[{}] value[{}]", key, value);
					if ( null != value && value.length() > 0 ) {
						int intValue = Integer.parseInt(value);
						String msg = null;
						switch (intValue) {
						case 0: 
						case 1: msg = strInit;		break;
						case 2: msg = strSending;	break;
						case 3: msg = strSucceed;
								unsubscribe(key);
								break;
						case 4:
						case 5:
						case 6: msg = strFailed;
								unsubscribe(key);
								break;
						}
						// Update MessageBox
						if ( null != messageBoxEvent ) messageBoxEvent.setMessage(msg);
					}
				}
			}
		}
		logger.end(function);
	}
	
	private void subscribe(String address) {
		final String function = "subscribe";
		logger.begin(function);
		logger.debug(function, "address[{}]", address);
		
		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(INSPECTOR);
		clientKey.setStability(Stability.DYNAMIC);
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(scsEnvId);
		clientKey.setAdress(address);
		
		String strClientKey = clientKey.getClientKey();
		logger.debug(function, "strClientKey[{}]", strClientKey);
		database.subscribe(strClientKey, new String[]{address}, new DatabaseEvent() {
			
			@Override
			public void update(String key, String[] value) {
				// TODO Auto-generated method stub
				
			}
		});
		logger.end(function);
	}
	private void unsubscribe(String address) {
		final String function = "unsubscribe";
		logger.begin(function);
		logger.debug(function, "address[{}]", address);
		
		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(INSPECTOR);
		clientKey.setStability(Stability.DYNAMIC);
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(scsEnvId);
		clientKey.setAdress(address);
		
		String strClientKey = clientKey.toClientKey();
		logger.debug(function, "strClientKey[{}]", strClientKey);
		database.unSubscribe(strClientKey);
		
		logger.end(function);
	}
	
	@Override
	public void setDatabase(Database db) {
		database = db;
	}
	
	private void readProperties() {
		final String function = "readProperties";
		logger.begin(function);
		
		byPassInitCond	= ReadProp.readInt(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, CONFIG_PREFIX+Attribute.byPassInitCond.toString(), 0);
		byPassRetCond	= ReadProp.readInt(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, CONFIG_PREFIX+Attribute.byPassRetCond.toString(), 0);
		sendAnyway		= ReadProp.readInt(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, CONFIG_PREFIX+Attribute.sendAnyway.toString(), 0);
		
		logger.debug(function, "byPassInitCond[{}] byPassRetCond[{}] sendAnyway[{}]", new Object[]{byPassInitCond, byPassRetCond, sendAnyway});

		logger.end(function);
	}
}
