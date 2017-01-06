package com.thalesgroup.scadagen.wrapper.wrapper.client.ctl;

public interface GrcMgr_i {
	  public enum GrcExecMode { 
		    Auto(1)
		  , StopOnFailed(2)
		  , StopOnFirstBRCExecuted(3)
		  ;
		  private int value;
		  private GrcExecMode(int value) {
			  this.value = value;
		  }
		  public int getValue() { return this.value; }
	  }
}
