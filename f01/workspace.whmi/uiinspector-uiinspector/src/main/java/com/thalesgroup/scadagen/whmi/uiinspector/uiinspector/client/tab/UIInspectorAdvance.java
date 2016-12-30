package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.ReadProp;
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
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Observer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;

public class UIInspectorAdvance implements UIInspectorTab_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorAdvance.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String tagname				= "advance";
	
	private final String inspAdvPropPrefix = "inspectorpanel.advance.";
	private final String inspAdvProp = inspAdvPropPrefix+"properties";

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
	
	@Override
	public void setParent(String scsEnvId, String parent) {
		final String function = "setParent";
		
		this.scsEnvId = scsEnvId;
		this.parent = parent;
		logger.info(className, function, "this.scsEnvId[{}]", this.scsEnvId);
		logger.info(className, function, "this.parent[{}]", this.parent);
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
			lblPageNum.setText(String.valueOf(pageIndex+1) + " / " + String.valueOf(pageCounter.pageCount));
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
		
		logger.info(className, function
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

		// Read static
		{
			logger.begin(className, function);
			
			String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;

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
			
			logger.debug(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
			}
			
			String api = "multiReadValue";
			database.addStaticRequest(api, clientKey, scsEnvId, dbaddresses, new DatabaseEvent() {
				
				@Override
				public void update(String key, String[] value) {
					String clientKeyStatic = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;
					if ( clientKeyStatic.equals(key) ) {
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
			
			String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "dynamic" + "_" + parent;

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
			
			logger.debug(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
			}
			
			database.subscribe(clientKey, dbaddresses, new DatabaseEvent() {

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
			String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "dynamic" + "_" + parent;
			database.unSubscribe(clientKey);
		}
		logger.end(className, function);
	}

	@Override
	public void buildWidgets(int numOfPointEach) {
		buildWidgets(this.addresses.length, numOfPointEach);
	}
	
	private int pageIndex = 0;
	private PageCounter pageCounter = null;
	
	String strApply = "Apply";
	String strCancel = "Cancel";
	
	String strInhibit = "Inhibit";
	String strSuspend = "Suspend";

	String strRestore = "Restore";
	String strOverride = "Override";
	
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
		
		logger.warn(className, function, "numOfWidgets[{}]", numOfWidgets);
		logger.warn(className, function, "numOfPointEach[{}]", numOfPointEachPage);
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, numOfPointEachPage);
			pageCounter.calc(pageIndex);
			
			flexTableAttibutes = new FlexTable();
			flexTableAttibutes.setWidth("100%");			
			
			int pageSize = pageCounter.pageSize;
			
			logger.info(className, function, "pageSize[{}]", pageSize);
				
			lblAttibuteLabel	= new InlineLabel[pageSize];
			
			lstValues			= new ListBox[pageSize];
			intValuesOri		= new int[pageSize];
			txtValues			= new TextBox[pageSize];
			strValuesOri		= new String[pageSize];
			
			chkDPMs				= new CheckBox[pageSize][3];
			
			btnApplys			= new UIButtonToggle[pageSize];

			{
				// Create Header
				String [] header = new String[] {"Equipment Point", "AI", "SS", "MO", "Value", ""};
				
				for ( int i = 0 ; i < header.length ; ++i ) {
					InlineLabel label = new InlineLabel();
					label.setWidth("100%");
					label.addStyleName("project-gwt-inlinelabel-inspector-"+tagname+"-header-label");
					label.setText(header[i]);
					flexTableAttibutes.setWidget(1, i, label);
				}
			}
								
			for( int i = 0 ; i < pageSize ; ++i ) {
				
				int r = 0;
				
				logger.info(className, function, "i[{}]", i);
					
				lblAttibuteLabel[i] = new InlineLabel();
				lblAttibuteLabel[i].addStyleName("project-gwt-inlinelabel-inspector-"+tagname+"-label");
				lblAttibuteLabel[i].setText("ATTRIBUTE_LABEL_"+(i+1)+":");
				flexTableAttibutes.setWidget(i+1+1, r++, lblAttibuteLabel[i]);
				
				chkDPMs[i] = new CheckBox[3];
				chkDPMs[i][0] = new CheckBox();
				chkDPMs[i][0].addStyleName("project-gwt-checkbox-inspector-"+tagname+"-points-ai");
				
				flexTableAttibutes.setWidget(i+1+1, r++, chkDPMs[i][0]);
				
				chkDPMs[i][1] = new CheckBox();
				chkDPMs[i][1].addStyleName("project-gwt-checkbox-inspector-"+tagname+"-points-ss");
				
				flexTableAttibutes.setWidget(i+1+1, r++, chkDPMs[i][1]);
				
				chkDPMs[i][2] = new CheckBox();
				chkDPMs[i][2].addStyleName("project-gwt-checkbox-inspector-"+tagname+"-points-mo");
				
				flexTableAttibutes.setWidget(i+1+1, r++, chkDPMs[i][2]);
				
				lstValues[i] = new ListBox();
				lstValues[i].setVisibleItemCount(1);
				lstValues[i].addStyleName("project-gwt-listbox-inspector-"+tagname+"-points-value");

				txtValues[i] = new TextBox();
				txtValues[i].setVisible(false);
				txtValues[i].addStyleName("project-gwt-textbox-inspector-"+tagname+"-points-value");

				HorizontalPanel hp = new HorizontalPanel();
				hp.add(lstValues[i]);
				hp.add(txtValues[i]);
				
				flexTableAttibutes.setWidget(i+1+1, r++, hp);
				
				btnApplys[i] = new UIButtonToggle(strApply);
				btnApplys[i].addStyleName("project-gwt-button-inspector-"+tagname+"-point-apply");
				
				btnApplys[i].addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {

						logger.begin(className, function + "onClick");
						
						sendControl(event);
						
						logger.end(className, function + "onClick");
					}
				});
				flexTableAttibutes.setWidget(i+1+1, r++, btnApplys[i]);

			}
			
			for ( int i = 0 ; i < 8 ; ++i ) {
				flexTableAttibutes.getColumnFormatter().addStyleName(i, "project-gwt-flextable-inspector-"+tagname+"-status-col"+i);
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
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesStatic	= new LinkedHashMap<String, HashMap<String, String>>();
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, HashMap<String, String>>();
	private HashMap<String, String> dbvalues = new HashMap<String, String>();
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValue";

		logger.begin(className, function);
		logger.debug(className, function, "clientkey[{}]", clientKey);
		
		for ( String key : keyAndValue.keySet() ) {
			dbvalues.put(key, keyAndValue.get(key));
		}
	
		if ( "static".equals(clientKey.split("_")[2]) ) {
			
			keyAndValuesStatic.put(clientKey, keyAndValue);
			
			updateValue(true);
			
		} else if ( "dynamic".equals(clientKey.split("_")[2]) ) {
			
			keyAndValuesDynamic.put(clientKey, keyAndValue);
			
			updateValue(false);
			
		}

		logger.end(className, function);
	}
	
	private void updateValue(boolean withStatic) {
		final String function = "updateValue";
		
		logger.begin(className, function);
		
		if ( withStatic ) {
			for ( String clientKey : keyAndValuesStatic.keySet() ) {
				
				HashMap<String, String> keyAndValue = keyAndValuesStatic.get(clientKey);
				
				updateValueStatic(clientKey, keyAndValue);
			}//End of for keyAndValuesStatic
		}
	
		for ( String clientKey : keyAndValuesDynamic.keySet() ) {
			
			HashMap<String, String> keyAndValue = keyAndValuesDynamic.get(clientKey);
			
			updateValueDynamic(clientKey, keyAndValue);
			
		}// End of keyAndValuesDynamic
		
		logger.end(className, function);
	}
	
	public void updateValueStatic(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValueStatic";
		
		logger.begin(className, function);
		logger.info(className, function, "clientkey[{}]", clientKey);
		
		valueRefreshed = false;
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;
		
		String clientKeyStatic = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;
		
		logger.info(className, function, "clientKeyStatic[{}]", clientKeyStatic);
		
		if ( clientKeyStatic.equals(clientKey) ) {
	
			for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
				String address = addresses[x];

				// Update the Label
				String label = DatabaseHelper.getAttributeValue(address, PointName.label.toString(), dbvalues);
				label = DatabaseHelper.removeDBStringWrapper(label);
				logger.debug(className, function, "label[{}]", label);

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
					logger.debug(className, function, "valueTable[{}]", valueTable);
					
					if ( null != valueTable ) {
						int valueCol = 0, labelCol = 1;
						String labels[]	= new String[12];
						String values[]	= new String[12];
						{
							for( int r = 0 ; r < 12 ; ++r ) {
								values[r]	= DatabaseHelper.getArrayValues(valueTable, valueCol, r );
								values[r]	= DatabaseHelper.removeDBStringWrapper(values[r]);
								labels[r]	= DatabaseHelper.getArrayValues(valueTable, labelCol, r );
								labels[r]	= DatabaseHelper.removeDBStringWrapper(labels[r]);
							}					
						}
						
						lstValues[y].clear();
						for( int r = 0 ; r < 12 ; ++r ) {
							
							if ( 0 == labels[r].compareTo("") ) break;
							
							lstValues[y].addItem(labels[r]);
							
							logger.info(className, function, "names[{}][{}] values[{}][{}]", new Object[]{r, labels[r], r, values[r]});
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
					logger.debug(className, function, "value[{}]", value);
					
					txtValues[y].setText(value);
					
				} else if ( PointType.sci == pointType ) {
					
					txtValues[y].setVisible(true);
					lstValues[y].setVisible(false);

					String value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
					value = DatabaseHelper.removeDBStringWrapper(value);
					logger.debug(className, function, "value[{}]", value);
					
					txtValues[y].setText(value);
					
				}
			}
			
		}
		
		logger.end(className, function);
	}
	
	public void updateValueDynamic(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValueDynamic";
		
		logger.begin(className, function);
		logger.debug(className, function, "clientkey[{}]", clientKey);
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
			String address = addresses[x];
			
			// Update the AI, SS and MO status
			{
				String stylename = "project-gwt-checkbox-inspector" + tagname + "-points-activated";
				
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
				logger.debug(className, function, "pointType[{}] sForcedStatusPoint[{}]", pointType, sForcedStatusPoint);
				
				String sForcedStatus = null;
				sForcedStatus = getStatusValue(address, sForcedStatusPoint);
				
				if ( null != sForcedStatus ) {
					int forcedStatus = Integer.parseInt(sForcedStatus);
					
					int indexAI = 0;
					int indexSS = 1;
					int indexMO = 2;
					
					if ( null != chkDPMs[y][indexAI] ) {
						if ( DatabaseHelper.isAI(forcedStatus) ) {
							chkDPMs[y][indexAI].addStyleName(stylename);
							if ( !valueRefreshed ) {
								chkDPMs[y][indexAI].setValue(true);
							}
						} else {
							chkDPMs[y][indexAI].removeStyleName(stylename);
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
						}
					}
				} else {
					logger.warn(className, function, "sForcedStatus IS NULL!");
				}

			}

			// Update the value Once time
			{
				if ( !valueRefreshed ) {
					
					String value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
					value = DatabaseHelper.removeDBStringWrapper(value);
					logger.debug(className, function, "value[{}]", value);
					
					
					if ( null != value ) {
						String point = DatabaseHelper.getPoint(address);
						if ( null != point ) {
							PointType pointType = DatabaseHelper.getPointType(point);

							logger.debug(className, function, "point[{}]", point);
							
							if ( PointType.dci == pointType ) {

								String valueTable = DatabaseHelper.getAttributeValue(address, PointName.dalValueTable.toString(), dbvalues);
								logger.info(className, function, "valueTable[{}]", valueTable);
								
								if ( null != valueTable ) {
									int valueCol = 0;
									String sValue = String.valueOf(value);
									String tValue = "";
									for( int r = 0 ; r < 12 ; ++r ) {
										tValue	= DatabaseHelper.getArrayValues(valueTable, valueCol, r );
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
		}
		
		if ( !valueRefreshed ) {
			
			valueRefreshed = true;
		
		}
		
		logger.end(className, function);
	}
	
	private String getStatusValue(String address, String strForcedStatus) {
		final String function = "getStatusValue";
		
		logger.begin(className, function);
		
		String sforcedStatus = DatabaseHelper.getAttributeValue(address, strForcedStatus, dbvalues);
		sforcedStatus = DatabaseHelper.removeDBStringWrapper(sforcedStatus);
		logger.debug(className, function, "sforcedStatus[{}]", sforcedStatus);
		
		logger.end(className, function);
		
		return sforcedStatus;
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
		
		logger.info(className, function, "btnIndex[{}]", btnIndex);
		
		pageCounter.calc(pageIndex);
	
		int x = pageCounter.pageRowBegin + btnIndex;
		int y = btnIndex;

		logger.info(className, function, "pageCounter.pageRowBegin[{}] x[{}] y[{}]", new Object[]{pageCounter.pageRowBegin, x, y});
		
		if ( -1 != btnIndex ) {
			
			String dbaddress = addresses[x];
			
			logger.info(className, function, "dbaddress[{}]", dbaddress);
			
			if ( null != dbaddress ) {
				
				String point	= DatabaseHelper.getPoint(dbaddress);

				if ( null != point ) {
					PointType pointType = DatabaseHelper.getPointType(point);
					
					String alias	= "<alias>" + dbaddress.replace(":", "");
					
					logger.info(className, function, "scsEnvId[{}] alias[{}]", scsEnvId, alias);				
					
					logger.info(className, function, "point[{}] type[{}]", point, pointType);
					
					if ( null != chkDPMs[y] ) {
						
						int indexAI = 0;
						int indexSS = 1;
						int indexMO = 2;
						
						boolean isAIApply	= false;
						boolean isAICancel	= false;
						
						boolean isSSApply	= false;
						boolean isSSCancel	= false;
						
						boolean isManualOverrideApply	= false;
						boolean isManualOverrideChange	= false;
						boolean isManualOverrideCancel	= false;
						
						String sForcedStatusPoint = null;
						if ( PointType.dci == pointType ) {
							sForcedStatusPoint = PointName.dfoForcedStatus.toString();
						} else if ( PointType.aci == pointType ){
							sForcedStatusPoint = PointName.afoForcedStatus.toString();
						} else if ( PointType.sci == pointType ) {
							sForcedStatusPoint = PointName.sfoForcedStatus.toString();
						}
						logger.debug(className, function, "pointType[{}] sForcedStatusPoint[{}]", pointType, sForcedStatusPoint);
						
						String sForcedStatus = null;
						sForcedStatus = getStatusValue(dbaddress, sForcedStatusPoint);
						
						if ( null != sForcedStatus ) {
							int forcedStatus = 0;
							try {
								forcedStatus = Integer.parseInt(sForcedStatus);
							} catch ( NumberFormatException e ) {
								logger.warn(className, function, "NumberFormatException[{}]", e.toString());
								logger.warn(className, function, "Integer.parseInt({})", sForcedStatus);
							}
							if ( !DatabaseHelper.isAI(forcedStatus) && chkDPMs[y][indexAI].getValue() ) {	isAIApply = true;	}
							if ( DatabaseHelper.isAI(forcedStatus) && !chkDPMs[y][indexAI].getValue() ) {	isAICancel = true;	}
							
							if ( !DatabaseHelper.isSS(forcedStatus) && chkDPMs[y][indexSS].getValue() ) {	isSSApply = true;	}
							if ( DatabaseHelper.isSS(forcedStatus) && !chkDPMs[y][indexSS].getValue() ) {	isSSCancel = true;	}
							
							if ( !DatabaseHelper.isMO(forcedStatus) && chkDPMs[y][indexMO].getValue() ) {	isManualOverrideApply = true;	}
							if ( DatabaseHelper.isMO(forcedStatus) && !chkDPMs[y][indexMO].getValue() ) {	isManualOverrideCancel = true;	}
							if ( moApplyWithoutReset && DatabaseHelper.isMO(forcedStatus) && chkDPMs[y][indexMO].getValue() ) {	
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
								if ( changed ) {
									isManualOverrideChange = true;
								}
								
							}
						}
						
						int moIValue = 0;
						
						float moFValue	= 0.0f;
						
						String moSValue	= "";					
						
						if ( isAIApply || isAICancel || isSSApply || isSSCancel || isManualOverrideApply || isManualOverrideChange || isManualOverrideCancel ) {

							
							if ( PointType.dci == pointType ) {
								
								int moIndex = lstValues[y].getSelectedIndex();
								
								logger.info(className, function, "moIndex[{}]", moIndex);
								
								String valueTable = DatabaseHelper.getAttributeValue(dbaddress, PointName.dalValueTable.toString(), dbvalues);
								logger.info(className, function, "sforcedStatus[{}]", valueTable);

								int valueCol = 0;
								String sValue	= DatabaseHelper.getArrayValues(valueTable, valueCol, moIndex);
								sValue			= DatabaseHelper.removeDBStringWrapper(sValue);
								
								logger.info(className, function, "sValue[{}]", sValue);
								try {
									moIValue = Integer.parseInt(sValue);
								} catch ( NumberFormatException e ) {
									logger.warn(className, function, "NumberFormatException[{}]", e.toString());
									logger.warn(className, function, "Integer.parseInt({})", sValue);
								}

							} else if ( PointType.aci == pointType || PointType.sci == pointType ) {
								
								moSValue = txtValues[y].getText();
								
								logger.info(className, function, "moSValue[{}]", moSValue);
								
								if ( PointType.aci == pointType ) {
									
									try {
										moFValue = Float.parseFloat(moSValue);
									} catch ( NumberFormatException e ) {
										logger.warn(className, function, "NumberFormatException[{}]", e.toString());
										logger.warn(className, function, "Float.parseFloat({})", moSValue);
									}
								}
							}
							
							logger.info(className, function, "moIValue[{}] moFValue[{}] moSValue[{}]", new Object[]{moIValue, moFValue, moSValue});
						}
						
						// Alarm Inhibit
						if ( isAIApply ) {
							
							// SCSDPC ALARM_INHIBIT_VAR
							String key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "alarminhibit" + "_"+ "true" + "_" + dbaddress;
							
							dpcMgr.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.ALARM_INHIBIT_VAR);
							
							chkDPMs[y][indexAI].setValue(true);
						} 

						if (isAICancel ) {
							// SCSDPC NO_ALARM_INHIBIT_VAR
							String key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "alarminhibit" + "_"+ "false" + "_" + dbaddress;
							
							dpcMgr.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.NO_ALARM_INHIBIT_VAR);

							chkDPMs[y][indexAI].setValue(false);
						}
						
						// Scan Suspend
						if ( isSSApply ) {
							// SS
							String key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "scansuspend" + "_"+ "true" + "_" + dbaddress;
							
							dpcMgr.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.OPERATOR_INHIBIT);
							
							chkDPMs[y][indexSS].setValue(true);
							
						} 

						if ( isSSCancel ) {
							
							String key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "scansuspend" + "_"+ "false" + "_" + dbaddress;
							
							dpcMgr.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.VALID);
							
							chkDPMs[y][indexSS].setValue(false);
						}
						
						// Manual Override
						{
							String key = null;
							boolean forceAction = false;
							if ( isManualOverrideApply || isManualOverrideChange ) {
								key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "manualoverride" + "_"+ "true" + "_" + dbaddress;
								forceAction = true;
							}
							if ( isManualOverrideCancel ){
								key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "manualoverride" + "_"+ "false" + "_" + dbaddress;
								forceAction = false;
							}
							
							if ( null != key ) {
								
								logger.info(className, function, "pointType[{}]", pointType);
								
								if ( PointType.dci == pointType ) {
									
									logger.info(className, function, "key[{}] scsEnvId[{}] alias[{}] forceAction[{}] moIValue[{}]"
											, new Object[]{key, scsEnvId, alias, forceAction, moIValue});
									
									dpcMgr.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moIValue );
									
								} else if ( PointType.aci == pointType ) {
									
									logger.info(className, function, "key[{}] scsEnvId[{}] alias[{}] forceAction[{}] moFValue[{}]"
											, new Object[]{key, scsEnvId, alias, forceAction, moFValue});
									
									dpcMgr.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moFValue );
									
								} else if ( PointType.sci == pointType ) {
									
									logger.info(className, function, "key[{}] scsEnvId[{}] alias[{}] forceAction[{}] moSValue[{}]"
											, new Object[]{key, scsEnvId, alias, forceAction, moSValue});
									
									dpcMgr.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moSValue );
									
								}
								
								chkDPMs[y][indexMO].setValue(forceAction);
								
							} else {
								logger.warn(className, function, "Manual Override Point Type IS INVALID");
							}
						}
					} else {
						logger.warn(className, function, "chkDPMs[{}] IS NULL", dbaddress);
					}					
				}
			} else {
				logger.warn(className, function, "dbaddress[{}] IS NULL", dbaddress);
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
		
		moApplyWithoutReset = ReadProp.readBoolean(UIInspector_i.strUIInspector, inspAdvProp, inspAdvPropPrefix+"moApplyWithoutReset", false);
		
		logger.info(className, function, "moApplyWithoutReset[{}]", moApplyWithoutReset);

		vpCtrls = new VerticalPanel();
		vpCtrls.setWidth("100%");

		btnUp = new Button();
		btnUp.addStyleName("project-gwt-button-inspector-"+tagname+"-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				logger.begin(className, function+" Begin");

				onButton((Button)event.getSource());

				logger.end(className, function+" End");
			}
		});
		
		lblPageNum = new InlineLabel();
		lblPageNum.addStyleName("project-gwt-inlinelabel-inspector-"+tagname+"-pagenum");
		lblPageNum.setText("1 / 1");
		
		btnDown = new Button();
		btnDown.addStyleName("project-gwt-button-inspector-"+tagname+"-down");
		btnDown.setText("▼");
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				onButton((Button)event.getSource());

			}
		});	

		HorizontalPanel pageBar = new HorizontalPanel();

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
		bottomBar.setWidth("100%");
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(pageBar);
		
		basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-"+tagname+"-inspector");
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
					logger.info(className, function, "event IS NULL");
				}
			}
		});
		rootPanel.addStyleName("project-gwt-panel-inspector-dialogbox");
		
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

}
