package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper.PointName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper.PointType;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorPage_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.database.Database;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.database.DatabaseEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.dpc.DCP_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.dpc.DpcMgr;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Observer;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Subject;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIInspectorAdvance implements UIInspectorPage_i {
	
	private final Logger logger = Logger.getLogger(UIInspectorAdvance.class.getName());
	
	private final String tagname				= "advance";

	// Static Attribute List
	private final String staticDciAttibutes []		= new String[] {PointName.label.toString(), PointName.dalValueTable.toString(), PointName.hmiOrder.toString()};
	private final String staticAciAttibutes []		= new String[] {PointName.label.toString(), PointName.aalValueTable.toString(), PointName.hmiOrder.toString()};
	
	// Dynamic Attribute List
	private final String dynamicDciAttibutes []	= new String[] {PointName.value.toString(), PointName.validity.toString(), PointName.dfoForcedStatus.toString()};
	private final String dynamicAciAttibutes []	= new String[] {PointName.value.toString(), PointName.validity.toString(), PointName.afoForcedStatus.toString()};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String scsEnvId, String parent) {
		this.scsEnvId = scsEnvId;
		this.parent = parent;
		logger.log(Level.FINE, "setConnection this.scsEnvId["+this.scsEnvId+"]");
		logger.log(Level.FINE, "setConnection this.parent["+this.parent+"]");
	}
	
	@Override
	public void setAddresses(String[] addresses) {
		logger.log(Level.FINE, "setAddresses Begin");
		
		this.addresses = addresses;
		
		logger.log(Level.FINE, "setAddresses End");
	}
	
	@Override
	public String[] getAddresses() {
		return this.addresses;
	}
	
	private void updatePager() {
		
		logger.log(Level.FINE, "updatePager Begin");
		
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
		
		logger.log(Level.FINE, "updatePager End");
	}

	private void onButton(Button btn) {
		
		logger.log(Level.FINE, "onButton Begin");
		
		if (  btn == btnUp || btn == btnDown ) {
			if ( btn == btnUp) {
				--pageIndex;
			} else if ( btn == btnDown ) {
				++pageIndex;
			}
			updatePager();
			updateValue(true);
		}
		
		logger.log(Level.FINE, "onButton End");
	}
	
	private DpcMgr dpcAccess = null;
	private Observer observer = null;
	private Subject dpcMgrSubject = null;
	
	private Database database = Database.getInstance();
	
	@Override
	public void connect() {
		logger.log(Level.FINE, "connect Begin");

		// Read static
		{
			logger.log(Level.FINE, "multiReadValue Begin");
			
			String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for ( String dbaddress : this.addresses ) {
					String point = RTDB_Helper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = RTDB_Helper.getPointType(point);
						if ( pointType == PointType.RTDB_DCI ) {
							for ( String attribute : staticDciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.RTDB_ACI ) {
							for ( String attribute : staticAciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.RTDB_SCI ) {

						} else {
							logger.log(Level.SEVERE, "connectInspector"+tagname+" dbaddress IS UNKNOW TYPE");
						}
					}
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
			
			logger.log(Level.FINE, "multiReadValue End");
		}
		
		// Read dynamic
		{
			logger.log(Level.FINE, "multiReadValue Begin");
			
			String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "dynamic" + "_" + parent;

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for ( String dbaddress : this.addresses ) {
					String point = RTDB_Helper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = RTDB_Helper.getPointType(point);
						if ( pointType == PointType.RTDB_DCI ) {
							for ( String attribute : dynamicDciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.RTDB_ACI ) {
							for ( String attribute : dynamicAciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.RTDB_SCI ) {

						} else {
							logger.log(Level.SEVERE, "connectInspector"+tagname+" dbaddress IS UNKNOW TYPE");
						}
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}
			
			logger.log(Level.FINE, "multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.log(Level.FINE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
			}
			
			Database database = Database.getInstance();
			database.addDynamicRequest(clientKey, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					// TODO Auto-generated method stub
					
				}
				
			});
		
			logger.log(Level.FINE, "multiReadValue End");
		}
		
		{
			dpcAccess = DpcMgr.getInstance("advance");
			dpcMgrSubject = dpcAccess.getSubject();
			
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

		logger.log(Level.FINE, "connect End");
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.FINE, "disconnect Begin");
		
		logger.log(Level.FINE, "disconnect End");
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
	private TextBox[] txtValues					= null;
	
	private CheckBox[][] chkDPMs 				= null;
	
	private UIButtonToggle[] btnApplys			= null;
	
	private void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.FINE, "updateDisplay Begin");
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, 10);
			pageCounter.calc(pageIndex);
			
			updatePager();
			
			int numOfWidgetShow = pageCounter.pageRowCount;
			
			if ( RTDB_Helper.addressesIsValid(this.addresses) ) {
				
				lblAttibuteLabel	= new InlineLabel[numOfWidgetShow];
				
				lstValues			= new ListBox[numOfWidgetShow];
				txtValues			= new TextBox[numOfWidgetShow];
				
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
					
					logger.log(Level.FINE, "buildWidgets i["+i+"]");
						
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

							logger.log(Level.FINE, "buildWidgets onClick Begin");
							
							sendControl(event);
							
							logger.log(Level.FINE, "buildWidgets onClick End");
						}
					});
					flexTableAttibutes.setWidget(i+1+1, r++, btnApplys[i]);

				}
				
				for ( int i = 0 ; i < 8 ; ++i ) {
					flexTableAttibutes.getColumnFormatter().addStyleName(i, "project-gwt-flextable-inspector-"+tagname+"-status-col"+i);
				}

			} else {
				logger.log(Level.FINE, "buildWidgets this.pointStatics IS NULL");
			}
			
			vpCtrls.add(flexTableAttibutes); 

		} else {
			logger.log(Level.SEVERE, "updateDisplay points IS NULL");
		}
		
		logger.log(Level.FINE, "updateDisplay End");
	}
	
	private boolean valueRefreshed = false;
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesStatic	= new LinkedHashMap<String, HashMap<String, String>>();
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, HashMap<String, String>>();
	private HashMap<String, String> dbvalues = new HashMap<String, String>();
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {

		logger.log(Level.FINE, "updateValue Begin");
		logger.log(Level.FINE, "updateValue clientkey["+clientKey+"]");
		
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

		logger.log(Level.FINE, "updateValue End");
	}
	
	private void updateValue(boolean withStatic) {
		
		logger.log(Level.FINE, "updateValue Begin");
		
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
		
		logger.log(Level.FINE, "updateValue End");
	}
	
	public void updateValueStatic(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.FINE, "updateValue Begin");
		logger.log(Level.FINE, "updateValue clientkey["+clientKey+"]");
		
		valueRefreshed = false;
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;
		
		String clientKeyStatic = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;
		
		logger.log(Level.FINE, "updateValue clientKeyStatic["+clientKeyStatic+"]");
		
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
					logger.log(Level.FINE, "updateValue label["+label+"]");
					
					// Set the Label
					lblAttibuteLabel[y].setText(label);
				}
				
				// ACI, SCI Show the TextBox
				// DCI, Show the ListBox and store the valueTable

				String point = RTDB_Helper.getPoint(address);
				PointType pointType = RTDB_Helper.getPointType(point);
				
				if ( PointType.RTDB_DCI == pointType ) {
					
					txtValues[y].setVisible(false);
					lstValues[y].setVisible(true);
					
					String valueTable = null;
					{
						String dbaddress = address + PointName.dalValueTable.toString();
						logger.log(Level.FINE, "updateValue PointName.value.toString()Table["+PointName.dalValueTable.toString()+"] dbaddress["+dbaddress+"]");
						if ( dbvalues.containsKey(dbaddress) ) {
							valueTable = dbvalues.get(dbaddress);
						} else {
							logger.log(Level.FINE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
						}
					}
					logger.log(Level.FINE, "updateValue valueTable["+valueTable+"]");
					
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
							
							logger.log(Level.FINE, "updateValue names["+r+"]["+labels[r]+"] values["+r+"]["+values[r]+"]");
						}
					} else {
						logger.log(Level.SEVERE, "updateValue valueTable IS NULL!");
					}
				} else if ( PointType.RTDB_ACI == pointType ) {
					
					txtValues[y].setVisible(true);
					lstValues[y].setVisible(false);
					
					String value = null;
					{
						String dbaddress = address + PointName.value.toString();
						if ( dbvalues.containsKey(dbaddress) ) {
							value = dbvalues.get(dbaddress);
						} else {
							logger.log(Level.FINE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
						}
					}
					
					txtValues[y].setText(value);
					
				} 
			}
			
		}
	}
	
	public void updateValueDynamic(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.FINE, "updateValue Begin");
		logger.log(Level.FINE, "updateValue clientkey["+clientKey+"]");
		
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
				
				if ( PointType.RTDB_DCI == pointType ) {
					sForcedStatus = getStatusValue(address, PointName.dfoForcedStatus.toString());
				} else if ( PointType.RTDB_ACI == pointType ){
					sForcedStatus = getStatusValue(address, PointName.afoForcedStatus.toString());
				} else if ( PointType.RTDB_SCI == pointType ) {
					
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
					logger.log(Level.SEVERE, "updateValue sForcedStatus IS NULL!");
				}

			}

			// Update the value Once time
			{
				if ( !valueRefreshed ) {
				
					String value = null;
					{
						String dbaddress = address + PointName.value.toString();
						logger.log(Level.FINE, "updateValue PointName.value.toString()["+PointName.value.toString()+"] dbaddress["+dbaddress+"]");
						if ( dbvalues.containsKey(dbaddress) ) {
							value = dbvalues.get(dbaddress);
						} else {
							logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
						}
					}
					logger.log(Level.FINE, "updateValue value["+value+"]");
					
					
					if ( null != value ) {
						String point = RTDB_Helper.getPoint(address);
						if ( null != point ) {
							PointType pointType = RTDB_Helper.getPointType(point);

							logger.log(Level.FINE, "updateValue point["+point+"]");
							
							if ( PointType.RTDB_DCI == pointType ) {
								
								String valueTable = null;
								{
									String dbaddress = address + PointName.dalValueTable.toString();
									logger.log(Level.FINE, "updateValue PointName.value.toString()Table["+PointName.dalValueTable.toString()+"] dbaddress["+dbaddress+"]");
									if ( dbvalues.containsKey(dbaddress) ) {
										valueTable = dbvalues.get(dbaddress);
									} else {
										logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
									}
								}
								logger.log(Level.FINE, "updateValue valueTable["+valueTable+"]");
								
								if ( null != valueTable ) {
									int valueCol = 0;
									String sValue = String.valueOf(value);
									String tValue = "";
									for( int r = 0 ; r < 12 ; ++r ) {
										tValue	= RTDB_Helper.getArrayValues(valueTable, valueCol, r );
										tValue	= RTDB_Helper.removeDBStringWrapper(tValue);
										
										if ( 0 == sValue.compareTo(tValue) ) {
											lstValues[y].setSelectedIndex(r);
											break;
										}
									}
								}
							} else if ( PointType.RTDB_ACI == pointType ) {
								txtValues[y].setValue(value);
							} else if ( PointType.RTDB_SCI == pointType ) {
								
							}

						}
					} else {
						logger.log(Level.SEVERE, "updateValue value IS NULL!");
					}
				}
			}
		}
		
		if ( !valueRefreshed ) {
			
			valueRefreshed = true;
		
		}
	}
	
	private String getStatusValue(String address, String strForcedStatus) {
		String sforcedStatus = null;
		{
			String dbaddress = address + strForcedStatus;
			logger.log(Level.FINE, "updateValue strForcedStatus["+PointName.dfoForcedStatus.toString()+"] dbaddress["+dbaddress+"]");
			
			if ( dbvalues.containsKey(dbaddress) ) {
				sforcedStatus = dbvalues.get(dbaddress);	
			} else {
				logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
			}
			
			logger.log(Level.FINE, "updateValue forcedStatus["+sforcedStatus+"]");
		}
		return sforcedStatus;
	}
	
	private void sendControl(ClickEvent event) {
		
		logger.log(Level.FINE, "sendControl Begin");
		
		UIButtonToggle button = (UIButtonToggle) event.getSource();
		int index = -1;
		for ( int i = 0 ; i < btnApplys.length ; ++i ) {
			if ( btnApplys[i] == button ) {
				index = i;
				break;
			}
		}
		
		logger.log(Level.FINE, "sendControl index["+index+"]");
		
		if ( -1 != index ) {
			
			String dbaddress = addresses[index];
			
			logger.log(Level.FINE, "sendControl dbaddress["+dbaddress+"]");
			
			if ( null != dbaddress ) {
				
				String point	= RTDB_Helper.getPoint(dbaddress);

				if ( null != point ) {
					PointType pointType = RTDB_Helper.getPointType(point);
					
					String alias	= "<alias>" + dbaddress.replace(":", "");
					
					logger.log(Level.FINE, "sendControl scsEnvId["+scsEnvId+"] alias["+alias+"]");				
					
					logger.log(Level.FINE, "sendControl point["+point+"] type["+pointType+"]");
					
					if ( null != chkDPMs[index] ) {
						
						int indexAI = 0;
						int indexSS = 1;
						int indexMO = 2;
						
						boolean isAIApply	= false;
						boolean isAICancel	= false;
						
						boolean isSSApply	= false;
						boolean isSSCancel	= false;
						
						boolean isManualOverrideApply	= false;
						boolean isManualOverrideCancel	= false;
						
						String sForcedStatus = null;
						
						if ( PointType.RTDB_DCI == pointType ) {
							sForcedStatus = getStatusValue(dbaddress, PointName.dfoForcedStatus.toString());
						} else if ( PointType.RTDB_ACI == pointType ){
							sForcedStatus = getStatusValue(dbaddress, PointName.afoForcedStatus.toString());
						} else if ( PointType.RTDB_SCI == pointType ) {
							
						}
						
						if ( null != sForcedStatus ) {
							int forcedStatus = 0;
							try {
								forcedStatus = Integer.parseInt(sForcedStatus);
							} catch ( NumberFormatException e ) {
								logger.log(Level.SEVERE, "sendControl NumberFormatException["+e.toString()+"]");
								logger.log(Level.SEVERE, "sendControl Integer.parseInt("+sForcedStatus+")");
							}
							if ( !RTDB_Helper.isAI(forcedStatus) && chkDPMs[index][indexAI].getValue() ) {	isAIApply = true;	}
							if ( RTDB_Helper.isAI(forcedStatus) && !chkDPMs[index][indexAI].getValue() ) {	isAICancel = true;	}
							
							if ( !RTDB_Helper.isSS(forcedStatus) && chkDPMs[index][indexSS].getValue() ) {	isSSApply = true;	}
							if ( RTDB_Helper.isSS(forcedStatus) && !chkDPMs[index][indexSS].getValue() ) {	isSSCancel = true;	}
							
							if ( !RTDB_Helper.isMO(forcedStatus) && chkDPMs[index][indexMO].getValue() ) {	isManualOverrideApply = true;	}
							if ( RTDB_Helper.isMO(forcedStatus) && !chkDPMs[index][indexMO].getValue() ) {	isManualOverrideCancel = true;	}						
						}
						
						int moIValue = 0;
						
						float moFValue	= 0.0f;
						
						String moSValue	= "";					
						
						if ( isAIApply || isAICancel || isSSApply || isSSCancel || isManualOverrideApply || isManualOverrideCancel ) {

							
							if ( PointType.RTDB_DCI == pointType ) {
								
								int moIndex = lstValues[index].getSelectedIndex();
								
								logger.log(Level.FINE, "sendControl moIndex["+moIndex+"]");
								
								String valueTable = null;
								{
									String address = dbaddress + PointName.dalValueTable.toString();
									logger.log(Level.FINE, "sendControl PointName.value.toString()Table["+PointName.dalValueTable.toString()+"] dbaddress["+address+"]");
									if ( dbvalues.containsKey(address) ) {
										valueTable = dbvalues.get(address);
									} else {
										logger.log(Level.FINE, "sendControl dbaddress["+address+"] VALUE NOT EXISTS!");
									}
								}
								logger.log(Level.FINE, "sendControl valueTable["+valueTable+"]");

								int valueCol = 0;
								String sValue	= RTDB_Helper.getArrayValues(valueTable, valueCol, moIndex);
								sValue			= RTDB_Helper.removeDBStringWrapper(sValue);
								
								logger.log(Level.FINE, "sendControl sValue["+sValue+"]");
								try {
									moIValue = Integer.parseInt(sValue);
								} catch ( NumberFormatException e ) {
									logger.log(Level.SEVERE, "sendControl NumberFormatException["+e.toString()+"]");
									logger.log(Level.SEVERE, "sendControl Integer.parseInt("+sValue+")");
								}

							} else if ( PointType.RTDB_ACI == pointType || PointType.RTDB_SCI == pointType ) {
								
								moSValue = txtValues[index].getText();
								
								logger.log(Level.FINE, "sendControl moSValue["+moSValue+"]");
								
								if ( PointType.RTDB_ACI == pointType ) {
									
									try {
										moFValue = Float.parseFloat(moSValue);
									} catch ( NumberFormatException e ) {
										logger.log(Level.SEVERE, "sendControl NumberFormatException["+e.toString()+"]");
										logger.log(Level.SEVERE, "sendControl Float.parseFloat("+moSValue+")");
									}
								}
							}
							
							logger.log(Level.FINE, "sendControl moIValue["+moIValue+"] setValue moFValue["+moFValue+"] moSValue["+moSValue+"]");
						}
						
						// Alarm Inhibit
						if ( isAIApply ) {
							
							// SCSDPC ALARM_INHIBIT_VAR
							String key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "alarminhibit" + "_"+ "true" + "_" + dbaddress;
							
							dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.ALARM_INHIBIT_VAR);
							
							chkDPMs[index][indexAI].setValue(true);
						} 

						if (isAICancel ) {
							// SCSDPC NO_ALARM_INHIBIT_VAR
							String key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "alarminhibit" + "_"+ "false" + "_" + dbaddress;
							
							dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.NO_ALARM_INHIBIT_VAR);

							chkDPMs[index][indexAI].setValue(false);
						}
						
						// Scan Suspend
						if ( isSSApply ) {
							// SS
							String key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "scansuspend" + "_"+ "true" + "_" + dbaddress;
							
							dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.OPERATOR_INHIBIT);
							
							chkDPMs[index][indexSS].setValue(true);
							
						} 

						if ( isSSCancel ) {
							
							String key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "scansuspend" + "_"+ "false" + "_" + dbaddress;
							
							dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.VALID);
							
							chkDPMs[index][indexSS].setValue(false);
						}
						
						// Manual Override
						{
							String key = null;
							boolean forceAction = false;
							if ( isManualOverrideApply ) {
								key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "manualoverride" + "_"+ "true" + "_" + dbaddress;
								forceAction = true;
							}
							if ( isManualOverrideCancel ){
								key = "changeEqpStatus" + "_"+ "inspector" + tagname + "_"+ "manualoverride" + "_"+ "false" + "_" + dbaddress;
								forceAction = false;
							}
							
							if ( null != key ) {
								
								logger.log(Level.SEVERE, "sendControl pointType["+pointType+"]");
								
								if ( PointType.RTDB_DCI == pointType ) {
									
									logger.log(Level.SEVERE, "sendControl key["+key+"] scsEnvId["+scsEnvId+"] alias["+alias+"] forceAction["+forceAction+"] moIValue["+moIValue+"]");
									
									dpcAccess.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moIValue );
									
								} else if ( PointType.RTDB_ACI == pointType ) {
									
									logger.log(Level.SEVERE, "sendControl key["+key+"] scsEnvId["+scsEnvId+"] alias["+alias+"] forceAction["+forceAction+"] moFValue["+moFValue+"]");
									
									dpcAccess.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moFValue );
									
								} else if ( PointType.RTDB_SCI == pointType ) {
									
									logger.log(Level.SEVERE, "sendControl key["+key+"] scsEnvId["+scsEnvId+"] alias["+alias+"] forceAction["+forceAction+"] moSValue["+moSValue+"]");
									
									dpcAccess.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moSValue );
									
								}
								
								chkDPMs[index][indexMO].setValue(forceAction);
								
							} else {
								logger.log(Level.SEVERE, "sendControl Manual Override Point Type IS INVALID");
							}
						}
					} else {
						logger.log(Level.SEVERE, "sendControl chkDPMs["+dbaddress+"] IS NULL");
					}					
				}
			} else {
				logger.log(Level.SEVERE, "sendControl dbaddress["+dbaddress+"] IS NULL");
			}
		} else {
			logger.log(Level.SEVERE, "sendControl index["+index+"] IS INVALID");
		}
		logger.log(Level.FINE, "sendControl End");
		
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
	private VerticalPanel vpCtrls = null;
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		vpCtrls = new VerticalPanel();
		vpCtrls.setWidth("100%");

		btnUp = new Button();
		btnUp.addStyleName("project-gwt-button-inspector-"+tagname+"-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				onButton((Button)event.getSource());

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
		
		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-"+tagname+"-inspector");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.FINE, "getMainPanel End");
		
		return vp;
	}

}
