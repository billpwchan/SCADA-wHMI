package com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client;

public class UITaskProfile extends UITaskDictionary {
	public UITaskProfile() {
		super();
	}
	public String getOperator() { return (String) getValue("operator"); }
	public void setOperator(String operator) { setValue("operator", operator); }
	public String getProfile() { return (String) getValue("profile"); }
	public void setProfile(String profile){ setValue("profile", profile); }
}
