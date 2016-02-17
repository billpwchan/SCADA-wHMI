package com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client;

import java.util.Date;

import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.i18n.client.DateTimeFormat;

public class UINameCard {
	private int uiScreen = 0;
	private String uiPath = "";
	private ResettableEventBus uiEventBus = null;
	private String createDateTimeLabel = "";
	public UINameCard(UINameCard uiNameCard) {
		this.setUINameCard(uiNameCard.getUiScreen(), uiNameCard.getUiPath(), uiNameCard.getUiEventBus());
	}
	public UINameCard(int uiScreen, String uiPath, ResettableEventBus uiEventBus) {
		this.setCreateDateTimeLabel(DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		this.setUINameCard(uiScreen, uiPath, uiEventBus);
	}
	private void setUINameCard(int uiScreen, String uiPath, ResettableEventBus uiEventBus) {
		this.setUiScreen(uiScreen);
		this.setUiPath(uiPath);
		this.setUiEventBus(uiEventBus);
	}
	public int getUiScreen() {
		return this.uiScreen;
	}
	public void setUiScreen(int uiScreen) {
		this.uiScreen = uiScreen;
	}
	public String getUiPath() {
		return this.uiPath;
	}
	public void setUiPath(String uiPath) {
		this.uiPath = uiPath;
	}
	private String getSimpleName(Object object) {
		String strSimpleClassName = "";
		String strFullClassName = object.getClass().getName();
		int firstChar;
		firstChar = strFullClassName.lastIndexOf ('.') + 1;
		if ( firstChar > 0 ) {
			strSimpleClassName = strFullClassName.substring ( firstChar );
		}
		return strSimpleClassName;
	}
	public void appendMgr(Object object) {
		this.appendUIPath(getSimpleName(object));
	}
	public void appendUIPanel(Object object) {
		this.appendUIPath(getSimpleName(object));
	}
	public void appendUIPath(String uiPath) {
		this.uiPath += ":" + uiPath;
	}
	public ResettableEventBus getUiEventBus() {
		return this.uiEventBus;
	}
	public void setUiEventBus(ResettableEventBus uiEventBus) {
		this.uiEventBus = uiEventBus;
	}
	public String getCreateDateTimeLabel() {
		return this.createDateTimeLabel;
	}
	public void setCreateDateTimeLabel(String createDateTimeLabel) {
		this.createDateTimeLabel = createDateTimeLabel;
	}
}
