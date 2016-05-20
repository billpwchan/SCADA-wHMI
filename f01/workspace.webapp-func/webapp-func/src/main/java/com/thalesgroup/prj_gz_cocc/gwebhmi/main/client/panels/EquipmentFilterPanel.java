package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.monitor.AsyncCallbackMwtAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.rpc.ConfigProxy;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.rpc.action.IOperatorActionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.configuration.action.GetAreaIds;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.configuration.action.GetServiceIds;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.configuration.action.IdsResponse;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.event.EqptFilterChangeEvent;

public class EquipmentFilterPanel extends Composite implements
		IClientLifeCycle {
	
	/** Logger */
    private static final ClientLogger logger_ = ClientLogger.getClientLogger();
    
    //private LayoutPanel panel_;
    
    private FlexTable flexTable_;
    
    private ArrayList<String> lineList_;
    
    private ListBox lineListBox_;
    
    private ArrayList<String> stationList_;

    private ListBox stationListBox_;
    
    private Map<Integer, String> displayIdToStationId;
    
    private ArrayList<String> subsystemList_;
    
    private ListBox subsystemListBox_;
    
    private TextBox eqpLabelTextBox_;
    
    private ArrayList<String> eqpNameList_;
    
    private ListBox eqpNameListBox_;
    
    private Button submitButton_;
    
    private EventBus eventBus_;

	public EquipmentFilterPanel (final EventBus eventBus) {

		eventBus_ = eventBus;
		
		lineList_ = new ArrayList<String>();
		stationList_ = new ArrayList<String>();
		subsystemList_ = new ArrayList<String>();
		eqpNameList_ = new ArrayList<String>();
		displayIdToStationId = new HashMap<Integer, String>();
		createLayout();
		initLayout();
	}

	private void createLayout() {
		
		flexTable_ = new FlexTable();

		Label lineLabel = new Label();
		lineLabel.setText(Dictionary.getWording("line_desc"));
		flexTable_.setWidget(0, 0, lineLabel);

		lineListBox_ = new ListBox();
		lineListBox_.setVisibleItemCount(1);
		lineListBox_.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				stationListBox_.clear();
				displayIdToStationId.clear();
				
				if (lineListBox_.getSelectedIndex() > 0) {
					String lineId = lineList_.get(lineListBox_.getSelectedIndex()-1);
					String linePrefix = lineId.substring(4);
					
					Integer displayId = 0;
					stationListBox_.addItem("");
					
					// Filter station list by prefix (e.g. MCS_GZ5 -> prefix=GZ5 -> filter all stations start with GZ5
					for (String areaId : stationList_) {
						
						if (areaId.startsWith(linePrefix)) {
							stationListBox_.addItem(Dictionary.getWording(areaId));
							displayIdToStationId.put(displayId++, areaId);
						}
					}
					
				} else {
					// no line is selected
					// insert full station list	
					Integer displayId = 0;
					stationListBox_.addItem("");
					
					for (String areaId : stationList_) {				
						stationListBox_.addItem(Dictionary.getWording(areaId));
						displayIdToStationId.put(displayId++, areaId);
					}
				}

			}
		});
		flexTable_.setWidget(1, 0, lineListBox_);
		
		Label stationLabel = new Label();
		stationLabel.setText(Dictionary.getWording("station_desc"));
		flexTable_.setWidget(0, 1, stationLabel);
		
		stationListBox_ = new ListBox();
		stationListBox_.setVisibleItemCount(1);
		flexTable_.setWidget(1, 1, stationListBox_);
		
		Label subsystemLabel = new Label();
		subsystemLabel.setText(Dictionary.getWording("Subsystem"));
		flexTable_.setWidget(0, 2, subsystemLabel);
		
		subsystemListBox_ = new ListBox();
		subsystemListBox_.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				subsystemListBox_.getSelectedIndex();

				eqpNameListBox_.clear();
				initEquipmentNameList(subsystemListBox_.getSelectedIndex());
			}
			
		});
		flexTable_.setWidget(1, 2, subsystemListBox_);

		Label eqpNameLabel = new Label();
		eqpNameLabel.setText(Dictionary.getWording("EquipmentName"));
		flexTable_.setWidget(0, 3, eqpNameLabel);
		
		eqpNameListBox_ = new ListBox();
		flexTable_.setWidget(1, 3, eqpNameListBox_);

		Label eqpLabelLabel = new Label();
		eqpLabelLabel.setText(Dictionary.getWording("EquipmentLabel"));
		flexTable_.setWidget(0, 4, eqpLabelLabel);
		
		eqpLabelTextBox_ = new TextBox();
		flexTable_.setWidget(1, 4, eqpLabelTextBox_);
		
		submitButton_ = new Button(Dictionary.getWording("Submit"));
		submitButton_.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
            	String lineId = new String();
            	String stationId = new String();
            	String subsystemId = new String();
            	String eqpNameId = new String();
            	String eqptLabel = new String();
            	
            	try {
            		// Skipping the empty string at index 0
	            	if (lineListBox_.getSelectedIndex() > 0) {
	            		lineId = lineList_.get(lineListBox_.getSelectedIndex()-1);
	            	}
	            	if (stationListBox_.getSelectedIndex() > 0) {
	            		stationId = displayIdToStationId.get(stationListBox_.getSelectedIndex()-1);
	            	}
	            	if (subsystemListBox_.getSelectedIndex() > 0) {
	            		subsystemId = subsystemList_.get(subsystemListBox_.getSelectedIndex()-1);
	            	}
	            	if (eqpNameListBox_.getSelectedIndex() > 0) {
	            		eqpNameId = eqpNameList_.get(eqpNameListBox_.getSelectedIndex()-1);
	            	}
            	} catch (Exception e) {
            		e.printStackTrace();
            		return;
            	}
            	eqptLabel = eqpLabelTextBox_.getText();
            	
            	final EqptFilterChangeEvent eqptFilterEvent = new EqptFilterChangeEvent(lineId, stationId, subsystemId, eqptLabel, eqpNameId);
            	eventBus_.fireEventFromSource(eqptFilterEvent, this);
            }
        });
		flexTable_.setWidget(1, 6, submitButton_);
		
		initWidget(flexTable_);
	}

	private void initLayout() {
		initLineList();
		initStationList();
		initSubsystemList();
		initEquipmentNameList(0);
	}
	private void initLineList() {
		lineListBox_.addItem("");
		
		GetServiceIds action = new GetServiceIds();
		
		ConfigProxy.getInstance().sendApplicationOperatorAction(action,
			new AsyncCallbackMwtAbstract<IOperatorActionReturn>() {
			
			@Override
			public void onSuccessMwt(IOperatorActionReturn result) {
				if (result instanceof IdsResponse) {
					final IdsResponse response = (IdsResponse) result;
					
					for (String lineId : response.getIds()) {
						if (!lineId.startsWith("MCS_")) // skip non-MCS service
							continue;

						lineListBox_.addItem(Dictionary.getWording(lineId));
						lineList_.add(lineId);
					}
				}
			}
			
			@Override
			public void onFailureMwt(Throwable caught) {
				logger_.warn("Unable to get serviceIds");
			}
		});
	}

	private void initStationList() {
		stationListBox_.addItem("");

		GetAreaIds action = new GetAreaIds();
		
		ConfigProxy.getInstance().sendApplicationOperatorAction(action,
			new AsyncCallbackMwtAbstract<IOperatorActionReturn>() {
			
			@Override
			public void onSuccessMwt(IOperatorActionReturn result) {
				if (result instanceof IdsResponse) {
					final IdsResponse response = (IdsResponse) result;
					
					Integer displayId = 0;
					displayIdToStationId.clear();
					for (String areaId : response.getIds()) {
						stationList_.add(areaId);						
						stationListBox_.addItem(Dictionary.getWording(areaId));
						displayIdToStationId.put(displayId++, areaId);
					}
				}
			}
			
			@Override
			public void onFailureMwt(Throwable caught) {
				logger_.warn("Unable to get areaIds");
			}
		});
	}

	private void initSubsystemList() {
		String [] list = new String [] {"AFC", "BAS", "CCTV", "FAS", "FG", "PSCADA", "PSD", "SIG", "Station"};

		subsystemListBox_.addItem("");
		
		for (String s : list) {
			subsystemListBox_.addItem(Dictionary.getWording(s));
			subsystemList_.add(s);
		}
	}
	
	private void initEquipmentNameList(Integer subsystemIdx) {
		// To do : change to get eqpt list of each subsystem from configuration
		String [] afcEqpts = { "Gate" };
		String [] basEqpts = { "AirConditioner", "Elevator", "Escalator", "ExitEsc", "ModeState", "Mode",
				"TemperatureHumiditySensor", "TunnelVentilationFan", "TunnelVentilationFan2", "TVF_Bidirection",
				"TVFVarFreqSpeed", "Valve", "WaterPumpX2", "WaterPumpX3" };
		String [] cctvEqpts = { "Camera"};
		String [] fasEqpts = {};
		String [] fgEqpts = {};
		String [] pscadaEqpts = { "Breaker", "Earth", "HVBBreaker", "Isolator", "LINKSTATE"};
		String [] psdEqpts = { "Asd", "Eed" };
		String [] sigEqpts = { "Platform", "Quantity", "SigFail", "Train" };
		String [] stationEqpts = { "Station" };
		
		eqpNameListBox_.addItem("");
		eqpNameList_.clear();
		
		String subsystem;
		if (subsystemIdx < 1 || subsystemIdx > subsystemList_.size()) {
			subsystem = "";
		} else {
			subsystem = subsystemList_.get(subsystemIdx-1);
		}
		if (subsystem == "AFC" || subsystem == "") {
			for (String s : afcEqpts) {
				eqpNameListBox_.addItem(Dictionary.getWording("equipmentType_" + s));
				eqpNameList_.add(s);
			}
		}
		if (subsystem == "BAS" || subsystem == "") {
			for (String s : basEqpts) {
				eqpNameListBox_.addItem(Dictionary.getWording("equipmentType_" + s));
				eqpNameList_.add(s);
			}
		}
		if (subsystem == "CCTV" || subsystem == "") {
			for (String s : cctvEqpts) {
				eqpNameListBox_.addItem(Dictionary.getWording("equipmentType_" + s));
				eqpNameList_.add(s);
			}
		}
		if (subsystem == "FAS" || subsystem == "") {
			for (String s : fasEqpts) {
				eqpNameListBox_.addItem(Dictionary.getWording("equipmentType_" + s));
				eqpNameList_.add(s);
			}
		}
		if (subsystem == "FG" || subsystem == "") {
			for (String s : fgEqpts) {
				eqpNameListBox_.addItem(Dictionary.getWording("equipmentType_" + s));
				eqpNameList_.add(s);
			}
		}
		if (subsystem == "PSCADA" || subsystem == "") {
			for (String s : pscadaEqpts) {
				eqpNameListBox_.addItem(Dictionary.getWording("equipmentType_" + s));
				eqpNameList_.add(s);
			}
		}
		if (subsystem == "PSD" || subsystem == "") {
			for (String s : psdEqpts) {
				eqpNameListBox_.addItem(Dictionary.getWording("equipmentType_" + s));
				eqpNameList_.add(s);
			}
		}
		if (subsystem == "SIG" || subsystem == "") {
			for (String s : sigEqpts) {
				eqpNameListBox_.addItem(Dictionary.getWording("equipmentType_" + s));
				eqpNameList_.add(s);
			}
		}
		if (subsystem == "Station" || subsystem == "") {
			for (String s : stationEqpts) {
				eqpNameListBox_.addItem(Dictionary.getWording("equipmentType_" + s));
				eqpNameList_.add(s);
			}
		}

	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}

}
