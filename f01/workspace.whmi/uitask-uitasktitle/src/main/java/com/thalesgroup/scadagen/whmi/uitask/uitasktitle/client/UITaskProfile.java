package com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client;

import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITaskDictionary;

public class UITaskProfile extends UITaskDictionary {
	public UITaskProfile() {
		super();
	}
	public UITaskProfile(UITaskProfile uiTaskProfile) {
		super(uiTaskProfile);
		setOperator(uiTaskProfile.getOperator());
		setProfile(uiTaskProfile.getProfile());
	}
	
	private String strOperator = "Operator";
	private String strProfile = "Profile";
	public String getOperator() { return (String) getValue(strOperator); }
	public void setOperator(String operator) { setValue(strOperator, operator); }
	public String getProfile() { return (String) getValue(strProfile); }
	public void setProfile(String profile){ setValue(strProfile, profile); }
}
