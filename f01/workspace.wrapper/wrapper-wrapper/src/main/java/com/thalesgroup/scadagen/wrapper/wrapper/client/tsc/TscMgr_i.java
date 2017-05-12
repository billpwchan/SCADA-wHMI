package com.thalesgroup.scadagen.wrapper.wrapper.client.tsc;

public interface TscMgr_i {
	  public enum Request { 
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
		  private String value;
		  private Request(String value) {
			  this.value = value;
		  }
		  public String getValue() { return this.value; }
	  }
}
