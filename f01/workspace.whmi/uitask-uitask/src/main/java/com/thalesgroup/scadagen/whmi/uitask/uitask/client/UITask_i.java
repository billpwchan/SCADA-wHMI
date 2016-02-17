package com.thalesgroup.scadagen.whmi.uitask.uitask.client;

public interface UITask_i {
	public String getUiScreen();
	public int getTaskUiScreen();
	public void setTaskUiScreen(int uiScreen);
	public void setUiScreen(String uiScreen);
	public String getUiPath();
	public void setUiPath(String uiPath);

}
