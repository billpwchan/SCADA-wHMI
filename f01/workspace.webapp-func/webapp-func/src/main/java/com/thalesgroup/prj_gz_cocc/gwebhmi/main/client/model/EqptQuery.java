package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EqptQuery implements Serializable {

	private String lineId_;
    
    private String stationId_;
    
    private String subsystemId_;
    
    private String eqptLabel_;
    
    private String eqptName_;

	public EqptQuery () {
		lineId_ = "";
		stationId_ = "";
		subsystemId_ = "";
		eqptLabel_ = "";
		eqptName_ = "";
	}
	
	public EqptQuery(String lineId, String stationId, String subsystemId,
			String eqptLabel, String eqptName) {
		lineId_ = lineId;
		stationId_ = stationId;
		subsystemId_ = subsystemId;
		eqptLabel_ = eqptLabel;
		eqptName_ = eqptName;
	}

	public String getLineId() {
		return lineId_;
	}

	public void setLineId(String lineId_) {
		this.lineId_ = lineId_;
	}

	public String getStationId() {
		return stationId_;
	}

	public void setStationId(String stationId_) {
		this.stationId_ = stationId_;
	}

	public String getSubsystemId() {
		return subsystemId_;
	}

	public void setSubsystemId(String subsystemId_) {
		this.subsystemId_ = subsystemId_;
	}

	public String getEqptLabel() {
		return eqptLabel_;
	}

	public void setEqptLabel(String eqptLabel_) {
		this.eqptLabel_ = eqptLabel_;
	}

	public String getEqptName() {
		return eqptName_;
	}

	public void setEqptName(String eqptName_) {
		this.eqptName_ = eqptName_;
	}
}
