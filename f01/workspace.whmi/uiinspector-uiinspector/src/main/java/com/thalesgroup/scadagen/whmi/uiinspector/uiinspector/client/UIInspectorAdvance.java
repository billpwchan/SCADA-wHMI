package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Observer;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Subject;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIInspectorAdvance implements UIInspectorTab_i, IClientLifeCycle {
	
	private static Logger logger = Logger.getLogger(UIInspectorAdvance.class.getName());
	
	String tagname								= "advance";

	// Static Attribute
	private final String strLabel				= ".label";
	private final String strValueTable			= ":dal.valueTable";
	private final String strHmiOrder			= ".hmiOrder";
	
	// Dynamic Attribute
	private final String strValue				= ".value";
	private final String strForcedStatus		= ":dfo.forcedStatus"; // 2=MO, AI=8, 512=SS //dfo.forcedStatus

	// Static Attribute List
	private String staticAttibutes [] = new String[] {strLabel, strValueTable, strHmiOrder};

	// Dynamic Attribute List
	private String dynamicAttibutes [] = new String[] {strValue, strForcedStatus};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	@Override
	public void setAddresses(String scsEnvId, String[] addresses, String period) {
		logger.log(Level.FINE, "setAddresses Begin");
		
		this.scsEnvId = scsEnvId;
		
		logger.log(Level.FINE, "setConnection this.scsEnvId["+this.scsEnvId+"]");
		
		this.addresses = addresses;
		
		logger.log(Level.FINE, "setAddresses End");
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
	@Override
	public void connect() {
		logger.log(Level.FINE, "connect Begin");
		
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
	
	private VerticalPanel[] widgetBoxes			= null;
	
	private InlineLabel[] lblAttibuteLabel		= null;
	private ListBox[] lstValues					= null;
	private TextBox[] txtValues					= null;
	
	private CheckBox[][] chkDPMs 				= null;
	
	private UIButtonToggle[] btnApplys			= null;
	
	private void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.FINE, "updateDisplay Begin");
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, 3);
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
	
		if ( 0 == "static".compareTo(clientKey.split("_")[2]) ) {
			
			keyAndValuesStatic.put(clientKey, keyAndValue);
			
			updateValue(true);
			
		} else if ( 0 == "dynamic".compareTo(clientKey.split("_")[2]) ) {
			
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
		
		String clientKey_multiReadValue_inspectorinfo_static = "multiReadValue" + "_" + "inspectorinfo" + "_" + "static" + "_" + parent;
		
		logger.log(Level.FINE, "updateValue clientKey_multiReadValue_inspectorinfo_static["+clientKey_multiReadValue_inspectorinfo_static+"]");
		
		if ( 0 == clientKey_multiReadValue_inspectorinfo_static.compareTo(clientKey) ) {
			
//			for ( int i = 0 ; i < this.addresses.length ; ++i ) {
//				String address = this.addresses[i];
			for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
				String address = addresses[x];
				
				// Update the Label
				{
					String label = null;
					{
						String dbaddress = address + strLabel;
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
				String type = RTDB_Helper.getPointType(point);
				
				if ( 0 != type.compareTo("dci") ) {
					
					txtValues[y].setVisible(true);
					lstValues[y].setVisible(false);

				} else if ( 0 == type.compareTo("dci") ) {
					
					txtValues[y].setVisible(false);
					lstValues[y].setVisible(true);
					
					String valueTable = null;
					{
						String dbaddress = address + strValueTable;
						logger.log(Level.FINE, "updateValue strValueTable["+strValueTable+"] dbaddress["+dbaddress+"]");
						if ( dbvalues.containsKey(dbaddress) ) {
							valueTable = dbvalues.get(dbaddress);
						} else {
							logger.log(Level.FINE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
						}
					}
					logger.log(Level.FINE, "updateValue valueTable["+valueTable+"]");
					
					if ( null != valueTable ) {
						String names[]	= new String[12];
						String values[]	= new String[12];
						{
							for( int r = 0 ; r < 12 ; ++r ) {
								values[r]	= RTDB_Helper.getArrayValues(valueTable, 4, r );
								values[r]	= RTDB_Helper.removeDBStringWrapper(values[r]);
								names[r]	= RTDB_Helper.getArrayValues(valueTable, 1, r );
								names[r]	= RTDB_Helper.removeDBStringWrapper(names[r]);
							}					
						}
						
						lstValues[y].clear();
						for( int r = 0 ; r < 12 ; ++r ) {
							
							if ( 0 == names[r].compareTo("") ) break;
							
							lstValues[y].addItem(names[r]);
							
							logger.log(Level.FINE, "updateValue names["+r+"]["+names[r]+"] values["+r+"]["+values[r]+"]");
						}
					} else {
						logger.log(Level.SEVERE, "updateValue valueTable IS NULL!");
					}
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
				String stylename = "project-gwt-checkbox-inspectoradvance-points-activated";
				
				String sForcedStatus = getStatusValue(address);
				
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
						String dbaddress = address + strValue;
						logger.log(Level.FINE, "updateValue strValue["+strValue+"] dbaddress["+dbaddress+"]");
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
							
							String type = RTDB_Helper.getPointType(point);
							
							logger.log(Level.FINE, "updateValue point["+point+"]");
							
							if ( null != type ) {
								
								logger.log(Level.FINE, "updateValue type["+type+"]");
								
								if ( 0 != type.compareTo("dci") ) {
									txtValues[y].setValue(value);
								} else {
//									int index = Integer.parseInt(value);
//									lstValues[y].setSelectedIndex(index);
									
									String valueTable = null;
									{
										String dbaddress = address + strValueTable;
										logger.log(Level.FINE, "updateValue strValueTable["+strValueTable+"] dbaddress["+dbaddress+"]");
										if ( dbvalues.containsKey(dbaddress) ) {
											valueTable = dbvalues.get(dbaddress);
										} else {
											logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
										}
									}
									logger.log(Level.FINE, "updateValue valueTable["+valueTable+"]");
									
									if ( null != valueTable ) {
										String sValue = String.valueOf(value);
										String tValue = "";
										for( int r = 0 ; r < 12 ; ++r ) {
											tValue	= RTDB_Helper.getArrayValues(valueTable, 4, r );
											tValue	= RTDB_Helper.removeDBStringWrapper(tValue);
											
											if ( 0 == sValue.compareTo(tValue) ) {
												lstValues[y].setSelectedIndex(r);
												break;
											}
										}
									}
								}
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
	
	private String getStatusValue(String address) {
		String sforcedStatus = null;
		{
			String dbaddress = address + strForcedStatus;
			logger.log(Level.FINE, "updateValue strForcedStatus["+strForcedStatus+"] dbaddress["+dbaddress+"]");
			
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
		
		logger.log(Level.FINE, "setValue Begin");
		
		UIButtonToggle button = (UIButtonToggle) event.getSource();
		int index = -1;
		for ( int i = 0 ; i < btnApplys.length ; ++i ) {
			if ( btnApplys[i] == button ) {
				index = i;
				break;
			}
		}
		
		logger.log(Level.FINE, "setValue index["+index+"]");
		
		if ( -1 != index ) {
			
			String dbaddress = addresses[index];
			
			logger.log(Level.FINE, "setValue dbaddress["+dbaddress+"]");
			
			if ( null != dbaddress ) {
				
				String point	= RTDB_Helper.getPoint(dbaddress);
					
				String type		= RTDB_Helper.getPointType(point);
				
				String alias	= "<alias>" + dbaddress.replace(":", "");
				
				logger.log(Level.FINE, "setValue scsEnvId["+scsEnvId+"] alias["+alias+"]");				
				
				logger.log(Level.FINE, "setValue point["+point+"] type["+type+"]");
				
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
					
					String sForcedStatus = getStatusValue(dbaddress);
					
					if ( null != sForcedStatus ) {
						int forcedStatus = Integer.parseInt(sForcedStatus);
						
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

						
						if ( 0 == type.compareTo("dci") ) {
							
							int moIndex = lstValues[index].getSelectedIndex();
							
							logger.log(Level.FINE, "updateValue moIndex["+moIndex+"]");
							
							String valueTable = null;
							{
								String address = dbaddress + strValueTable;
								logger.log(Level.FINE, "updateValue strValueTable["+strValueTable+"] dbaddress["+address+"]");
								if ( dbvalues.containsKey(address) ) {
									valueTable = dbvalues.get(address);
								} else {
									logger.log(Level.FINE, "updateValue dbaddress["+address+"] VALUE NOT EXISTS!");
								}
							}
							logger.log(Level.FINE, "updateValue valueTable["+valueTable+"]");

							String sValue	= RTDB_Helper.getArrayValues(valueTable, 4, moIndex);
							sValue			= RTDB_Helper.removeDBStringWrapper(sValue);
							
							logger.log(Level.FINE, "updateValue sValue["+sValue+"]");
							
							moIValue = Integer.parseInt(sValue);

						} else if ( 0 == type.compareTo("aci") || 0 == type.compareTo("sci")) {
							
							moSValue = txtValues[index].getText();
							
							if ( 0 == type.compareTo("aci") ) {
								
								moFValue = Float.parseFloat(moSValue);
							}
						}
						
						logger.log(Level.FINE, "setValue moIValue["+moIValue+"] setValue moFValue["+moFValue+"] moSValue["+moSValue+"]");
					}
					
					// Alarm Inhibit
					if ( isAIApply ) {
						
						// SCSDPC ALARM_INHIBIT_VAR
						String key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "alarminhibit" + "_"+ "true" + "_" + dbaddress;
						
						dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.ALARM_INHIBIT_VAR);
						
						chkDPMs[index][indexAI].setValue(true);
					} 

					if (isAICancel ) {
						// SCSDPC NO_ALARM_INHIBIT_VAR
						String key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "alarminhibit" + "_"+ "false" + "_" + dbaddress;
						
						dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.NO_ALARM_INHIBIT_VAR);

						chkDPMs[index][indexAI].setValue(false);
					}
					
					// Scan Suspend
					if ( isSSApply ) {
						// SS
						String key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "scansuspend" + "_"+ "true" + "_" + dbaddress;
						
						dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.OPERATOR_INHIBIT);
						
						chkDPMs[index][indexSS].setValue(true);
						
					} 

					if ( isSSCancel ) {
						
						String key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "scansuspend" + "_"+ "false" + "_" + dbaddress;
						
						dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.VALID);
						
						chkDPMs[index][indexSS].setValue(false);
					}
					
					// Manual Override
					{
						String key = null;
						boolean forceAction = false;
						if ( isManualOverrideApply ) {
							key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "manualoverride" + "_"+ "true" + "_" + dbaddress;
							forceAction = true;
						}
						if ( isManualOverrideCancel ){
							key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "manualoverride" + "_"+ "false" + "_" + dbaddress;
							forceAction = false;
						}
						
						if ( null != key ) {
							if ( 0 == type.compareTo("dci") ) {
								
								dpcAccess.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moIValue );
								
							} else if ( 0 == type.compareTo("aci") ) {
								
								dpcAccess.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moFValue );
								
							} else if ( 0 == type.compareTo("sci") ) {
								
								dpcAccess.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moSValue );
								
							}
							
							chkDPMs[index][indexMO].setValue(forceAction);
							
						} else {
							logger.log(Level.SEVERE, "setValue Manual Override Point Type IS INVALID");
						}
					
					}
				} else {
					logger.log(Level.SEVERE, "setValue chkDPMs["+dbaddress+"] IS NULL");
				}
			} else {
				logger.log(Level.SEVERE, "setValue dbaddress["+dbaddress+"] IS NULL");
			}
		} else {
			logger.log(Level.SEVERE, "setValue index["+index+"] IS INVALID");
		}
		logger.log(Level.FINE, "setValue End");
		
	}
	
	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
		this.messageBoxEvent = messageBoxEvent;
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
		
//		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//		bottomBar.add(btnExecute);
		
		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-inspector");
		basePanel.setHeight("400px");
		basePanel.setWidth("400px");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.FINE, "getMainPanel End");
		
		return vp;
	}

	@Override
	public void terminate() {
		logger.log(Level.FINE, "terminate Begin");

		logger.log(Level.FINE, "terminate End");
	}

}
