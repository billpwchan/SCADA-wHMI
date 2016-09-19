package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.RTDB_Helper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.ReadProp;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.RTDB_Helper.PointName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.RTDB_Helper.PointType;
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
			updateValue(true);
		}
		
		logger.end(className, function);
	}
	
	private DpcMgr dpcMgr = null;
	private Observer observer = null;
	private Subject dpcMgrSubject = null;
	
	private Database database = Database.getInstance();
	
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
					String point = RTDB_Helper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = RTDB_Helper.getPointType(point);
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
					Database database = Database.getInstance();
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
					String point = RTDB_Helper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = RTDB_Helper.getPointType(point);
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
			
			Database database = Database.getInstance();
			database.addDynamicRequest(clientKey, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					// TODO Auto-generated method stub
					
				}
				
			});
		
			logger.end(className, function);
		}
		
		{
			dpcMgr = DpcMgr.getInstance("advance");
			dpcMgrSubject = dpcMgr.getSubject();
			
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
			
			observer.setSubject(dpcMgrSubject);			
		}

		logger.end(className, function);
	}
	
	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.beginEnd(className, function);
	}

	@Override
	public void buildWidgets() {
		buildWidgets(this.addresses.length);
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
	
	private void buildWidgets(int numOfWidgets) {
		final String function = "buildWidgets";
		
		logger.begin(className, function);
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, 10);
			pageCounter.calc(pageIndex);
			
			updatePager();
			
			int numOfWidgetShow = pageCounter.pageRowCount;
			
			if ( RTDB_Helper.addressesIsValid(this.addresses) ) {
				
				lblAttibuteLabel	= new InlineLabel[numOfWidgetShow];
				
				lstValues			= new ListBox[numOfWidgetShow];
				intValuesOri		= new int[numOfWidgetShow];
				txtValues			= new TextBox[numOfWidgetShow];
				strValuesOri		= new String[numOfWidgetShow];
				
				chkDPMs				= new CheckBox[numOfWidgetShow][3];
				
				btnApplys			= new UIButtonToggle[numOfWidgetShow];

				flexTableAttibutes = new FlexTable();
				flexTableAttibutes.setWidth("100%");
				
				{
					String [] header = new String[] {"Equipment Point", "AI", "SS", "MO", "Value", ""};
					
					for ( int i = 0 ; i < header.length ; ++i ) {
						InlineLabel label = new InlineLabel();
						label.setWidth("100%");
						label.addStyleName("project-gwt-inlinelabel-inspector-"+tagname+"-header-label");
						label.setText(header[i]);
						flexTableAttibutes.setWidget(1, i, label);
					}
				}
								
				for( int i = 0 ; i < numOfWidgetShow ; ++i ) {
					
					int r = 0;
					
					logger.info(className, function, "i[{}]", i);
						
					lblAttibuteLabel[i] = new InlineLabel();
					lblAttibuteLabel[i].setWidth("100%");
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

			} else {
				logger.info(className, function, "this.addresses IS INVALID");
			}
			
			vpCtrls.add(flexTableAttibutes); 

		} else {
			logger.info(className, function, "points IS NULL");
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
				{
					String label = null;
					{
						String dbaddress = address + PointName.label.toString();
						if ( dbvalues.containsKey(dbaddress) ) {
							label = dbvalues.get(dbaddress);
							label = RTDB_Helper.removeDBStringWrapper(label);
						}
					}
					logger.info(className, function, "label[{}]", label);
					
					// Set the Label
					lblAttibuteLabel[y].setText(label);
				}
				
				// ACI, SCI Show the TextBox
				// DCI, Show the ListBox and store the valueTable

				String point = RTDB_Helper.getPoint(address);
				PointType pointType = RTDB_Helper.getPointType(point);
				
				if ( PointType.dci == pointType ) {
					
					txtValues[y].setVisible(false);
					lstValues[y].setVisible(true);
					
					String valueTable = null;
					{
						String dbaddress = address + PointName.dalValueTable.toString();
						logger.info(className, function, "PointName.value.toString()Table[{}] dbaddress[{}]", PointName.dalValueTable.toString(), dbaddress);
						if ( dbvalues.containsKey(dbaddress) ) {
							valueTable = dbvalues.get(dbaddress);
						} else {
							logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
						}
					}
					logger.info(className, function, "valueTable[{}]", valueTable);
					
					if ( null != valueTable ) {
						int valueCol = 0, labelCol = 1;
						String labels[]	= new String[12];
						String values[]	= new String[12];
						{
							for( int r = 0 ; r < 12 ; ++r ) {
								values[r]	= RTDB_Helper.getArrayValues(valueTable, valueCol, r );
								values[r]	= RTDB_Helper.removeDBStringWrapper(values[r]);
								labels[r]	= RTDB_Helper.getArrayValues(valueTable, labelCol, r );
								labels[r]	= RTDB_Helper.removeDBStringWrapper(labels[r]);
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
					
					String value = null;
					{
						String dbaddress = address + PointName.value.toString();
						if ( dbvalues.containsKey(dbaddress) ) {
							value = dbvalues.get(dbaddress);
						} else {
							logger.info(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
						}
					}
					
					txtValues[y].setText(value);
					
				} else if ( PointType.sci == pointType ) {
					
					txtValues[y].setVisible(true);
					lstValues[y].setVisible(false);
					
					String value = null;
					{
						String dbaddress = address + PointName.value.toString();
						if ( dbvalues.containsKey(dbaddress) ) {
							value = dbvalues.get(dbaddress);
						} else {
							logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
						}
					}
					
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
				
				String point = RTDB_Helper.getPoint(address);
				PointType pointType = RTDB_Helper.getPointType(point);
				
				String sForcedStatus = null;
				
				if ( PointType.dci == pointType ) {
					sForcedStatus = getStatusValue(address, PointName.dfoForcedStatus.toString());
				} else if ( PointType.aci == pointType ){
					sForcedStatus = getStatusValue(address, PointName.afoForcedStatus.toString());
				} else if ( PointType.sci == pointType ) {
					sForcedStatus = getStatusValue(address, PointName.sfoForcedStatus.toString());
				}
				
				if ( null != sForcedStatus ) {
					int forcedStatus = Integer.parseInt(sForcedStatus);
					
					int indexAI = 0;
					int indexSS = 1;
					int indexMO = 2;
					
					if ( null != chkDPMs[y][indexAI] ) {
						if ( RTDB_Helper.isAI(forcedStatus) ) {
							chkDPMs[y][indexAI].addStyleName(stylename);
							if ( !valueRefreshed ) {
								chkDPMs[y][indexAI].setValue(true);
							}
						} else {
							chkDPMs[y][indexAI].removeStyleName(stylename);
						}
					}
					if ( null != chkDPMs[y][indexSS] ) {
						if ( RTDB_Helper.isSS(forcedStatus) ) {
							chkDPMs[y][indexSS].addStyleName(stylename);
							if ( !valueRefreshed ) {
								chkDPMs[y][indexSS].setValue(true);
							}
						} else {
							chkDPMs[y][indexSS].removeStyleName(stylename);
						}
					}
					if ( null != chkDPMs[y][indexMO] ) {
						if ( RTDB_Helper.isMO(forcedStatus) ) {
							chkDPMs[y][indexMO].addStyleName(stylename);
							if ( !valueRefreshed ) {
								chkDPMs[y][indexMO].setValue(true);
							}
						} else {
							chkDPMs[y][indexMO].removeStyleName(stylename);
						}
					}
				} else {
					logger.info(className, function, "sForcedStatus IS NULL!");
				}

			}

			// Update the value Once time
			{
				if ( !valueRefreshed ) {
				
					String value = null;
					{
						String dbaddress = address + PointName.value.toString();
						logger.debug(className, function, "PointName.value.toString()[{}] dbaddress[{}]", PointName.value.toString(), dbaddress);
						if ( dbvalues.containsKey(dbaddress) ) {
							value = dbvalues.get(dbaddress);
						} else {
							logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
						}
					}
					logger.debug(className, function, "value["+value+"]");
					
					
					if ( null != value ) {
						String point = RTDB_Helper.getPoint(address);
						if ( null != point ) {
							PointType pointType = RTDB_Helper.getPointType(point);

							logger.debug(className, function, "point[{}]", point);
							
							if ( PointType.dci == pointType ) {
								
								String valueTable = null;
								{
									String dbaddress = address + PointName.dalValueTable.toString();
									logger.debug(className, function, "PointName.value.toString()Table[{}] dbaddress[{}]", PointName.dalValueTable.toString(), dbaddress);
									if ( dbvalues.containsKey(dbaddress) ) {
										valueTable = dbvalues.get(dbaddress);
									} else {
										logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
									}
								}
								logger.debug(className, function, "valueTable[{}]", valueTable);
								
								if ( null != valueTable ) {
									int valueCol = 0;
									String sValue = String.valueOf(value);
									String tValue = "";
									for( int r = 0 ; r < 12 ; ++r ) {
										tValue	= RTDB_Helper.getArrayValues(valueTable, valueCol, r );
										tValue	= RTDB_Helper.removeDBStringWrapper(tValue);
										
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
		
		String sforcedStatus = null;
		{
			String dbaddress = address + strForcedStatus;
			logger.debug(className, function, "strForcedStatus[{}] dbaddress[{}]", PointName.dfoForcedStatus.toString(), dbaddress);
			
			if ( dbvalues.containsKey(dbaddress) ) {
				sforcedStatus = dbvalues.get(dbaddress);	
			} else {
				logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
			}
			
			logger.debug(className, function, "forcedStatus[{}]", sforcedStatus);
		}
		
		logger.end(className, function);
		
		return sforcedStatus;
	}
	
	private void sendControl(ClickEvent event) {
		final String function = "sendControl";
		
		logger.begin(className, function);
		
		UIButtonToggle button = (UIButtonToggle) event.getSource();
		int index = -1;
		for ( int i = 0 ; i < btnApplys.length ; ++i ) {
			if ( btnApplys[i] == button ) {
				index = i;
				break;
			}
		}
		
		logger.info(className, function, "index[{}]", index);
		
		if ( -1 != index ) {
			
			String dbaddress = addresses[index];
			
			logger.info(className, function, "dbaddress[{}]", dbaddress);
			
			if ( null != dbaddress ) {
				
				String point	= RTDB_Helper.getPoint(dbaddress);

				if ( null != point ) {
					PointType pointType = RTDB_Helper.getPointType(point);
					
					String alias	= "<alias>" + dbaddress.replace(":", "");
					
					logger.info(className, function, "scsEnvId[{}] alias[{}]", scsEnvId, alias);				
					
					logger.info(className, function, "point[{}] type[{}]", point, pointType);
					
					if ( null != chkDPMs[index] ) {
						
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
						
						String sForcedStatus = null;
						
						if ( PointType.dci == pointType ) {
							sForcedStatus = getStatusValue(dbaddress, PointName.dfoForcedStatus.toString());
						} else if ( PointType.aci == pointType ){
							sForcedStatus = getStatusValue(dbaddress, PointName.afoForcedStatus.toString());
						} else if ( PointType.sci == pointType ) {
							sForcedStatus = getStatusValue(dbaddress, PointName.sfoForcedStatus.toString());
						}
						
						if ( null != sForcedStatus ) {
							int forcedStatus = 0;
							try {
								forcedStatus = Integer.parseInt(sForcedStatus);
							} catch ( NumberFormatException e ) {
								logger.warn(className, function, "NumberFormatException[{}]", e.toString());
								logger.warn(className, function, "Integer.parseInt({})", sForcedStatus);
							}
							if ( !RTDB_Helper.isAI(forcedStatus) && chkDPMs[index][indexAI].getValue() ) {	isAIApply = true;	}
							if ( RTDB_Helper.isAI(forcedStatus) && !chkDPMs[index][indexAI].getValue() ) {	isAICancel = true;	}
							
							if ( !RTDB_Helper.isSS(forcedStatus) && chkDPMs[index][indexSS].getValue() ) {	isSSApply = true;	}
							if ( RTDB_Helper.isSS(forcedStatus) && !chkDPMs[index][indexSS].getValue() ) {	isSSCancel = true;	}
							
							if ( !RTDB_Helper.isMO(forcedStatus) && chkDPMs[index][indexMO].getValue() ) {	isManualOverrideApply = true;	}
							if ( RTDB_Helper.isMO(forcedStatus) && !chkDPMs[index][indexMO].getValue() ) {	isManualOverrideCancel = true;	}
							if ( moApplyWithoutReset && RTDB_Helper.isMO(forcedStatus) && chkDPMs[index][indexMO].getValue() ) {	
								boolean changed = false;
								if ( PointType.dci == pointType ) {
									int moIndex = lstValues[index].getSelectedIndex();
									if ( moIndex != intValuesOri[index] ) {
										intValuesOri[index] = moIndex;
										changed = true;
									}
								} else if ( PointType.aci == pointType || PointType.sci == pointType ) {
									String moSValue = txtValues[index].getText();
									if ( moSValue != strValuesOri[index] ) {
										strValuesOri[index] = moSValue;
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
								
								int moIndex = lstValues[index].getSelectedIndex();
								
								logger.info(className, function, "moIndex[{}]", moIndex);
								
								String valueTable = null;
								{
									String address = dbaddress + PointName.dalValueTable.toString();
									logger.info(className, function, "PointName.value.toString()Table[{}] dbaddress[{}]", PointName.dalValueTable.toString(), address);
									if ( dbvalues.containsKey(address) ) {
										valueTable = dbvalues.get(address);
									} else {
										logger.info(className, function, "dbaddress[{}] VALUE NOT EXISTS!", address);
									}
								}
								logger.info(className, function, "valueTable[{}]", valueTable);

								int valueCol = 0;
								String sValue	= RTDB_Helper.getArrayValues(valueTable, valueCol, moIndex);
								sValue			= RTDB_Helper.removeDBStringWrapper(sValue);
								
								logger.info(className, function, "sValue[{}]", sValue);
								try {
									moIValue = Integer.parseInt(sValue);
								} catch ( NumberFormatException e ) {
									logger.warn(className, function, "NumberFormatException[{}]", e.toString());
									logger.warn(className, function, "Integer.parseInt({})", sValue);
								}

							} else if ( PointType.aci == pointType || PointType.sci == pointType ) {
								
								moSValue = txtValues[index].getText();
								
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
							
							chkDPMs[index][indexAI].setValue(true);
						} 

						if (isAICancel ) {
							// SCSDPC NO_ALARM_INHIBIT_VAR
							String key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "alarminhibit" + "_"+ "false" + "_" + dbaddress;
							
							dpcMgr.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.NO_ALARM_INHIBIT_VAR);

							chkDPMs[index][indexAI].setValue(false);
						}
						
						// Scan Suspend
						if ( isSSApply ) {
							// SS
							String key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "scansuspend" + "_"+ "true" + "_" + dbaddress;
							
							dpcMgr.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.OPERATOR_INHIBIT);
							
							chkDPMs[index][indexSS].setValue(true);
							
						} 

						if ( isSSCancel ) {
							
							String key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "scansuspend" + "_"+ "false" + "_" + dbaddress;
							
							dpcMgr.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.VALID);
							
							chkDPMs[index][indexSS].setValue(false);
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
								
								chkDPMs[index][indexMO].setValue(forceAction);
								
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
			logger.warn(className, function, "index[{}] IS INVALID", index);
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

		// Auto close handle
		rootPanel = new FocusPanel();
		rootPanel.add(vpCtrls);
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

}
