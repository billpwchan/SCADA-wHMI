package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public class UIEventActionTsc_i {
	public enum UIEventActionTscAction {
		  SetStartTime("SetStartTime")
		, SetInterval("SetInterval")
		, SetFilter("SetFilter")
		, SetEndTime("SetEndTime")
		, SetDescription("SetDescription")
		, SetDates("SetDates")
		, SetCommand("SetCommand")
		, SetTaskArguments("SetTaskArguments")
		, SetArguments("SetArguments")
		, GetTaskType("GetTaskType")
		, GetTaskNames("GetTaskNames")
		, GetStartTime("GetStartTime")
		, GetRemoveAtEnd("GetRemoveAtEnd")
		, GetLog("GetLog")
		, GetInterval("GetInterval")
		, GetFilter("GetFilter")
		, GetEndTime("GetEndTime")
		, GetDescription("GetDescription")
		, GetDayGroupNamesAndIds("GetDayGroupNamesAndIds")
		, GetDates("GetDates")
		, GetCommand("GetCommand")
		, GetArguments("GetArguments")
		, RemoveTask("RemoveTask")
		, IsEnabled("IsEnabled")
		, EnableTask("EnableTask")
		, DisableTask("DisableTask")
		, AddCompleteTask("AddCompleteTask")
		, AddTask("AddTask")
		;
		private final String text;
		private UIEventActionTscAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			UIEventActionTscAction[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
}
