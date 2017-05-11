package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public class UIEventActionTsc_i {
	public enum UIEventActionTscAction {
		  SetStartTime("SetStartTime")
		, SetFilter("SetFilter")
		, SetEndTime("SetEndTime")
		, SetDescription("SetDescription")
		, GetTaskNames("GetTaskNames")
		, GetStartTime("GetStartTime")
		, GetRemoveAtEnd("GetRemoveAtEnd")
		, GetLog("GetLog")
		, GetInterval("GetInterval")
		, GetFilter("GetFilter")
		, GetEndTime("GetEndTime")
		, GetDescription("GetDescription")
		, GetCommand("GetCommand")
		, GetArguments("GetArguments")
		, RemoveTask("RemoveTask")
		, IsEnabled("IsEnabled")
		, EnableTask("EnableTask")
		, DisableTask("DisableTask")
		, AddTask("AddTask")
		;
		private final String text;
		private UIEventActionTscAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
