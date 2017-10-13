package com.thalesgroup.scadagen.wrapper.wrapper.client.ols;

public interface OlsMgr_i {
	  public enum Request { 
		    ReadData("ReadData")
		  , DeleteData("DeleteData")
		  , InsertData("InsertData")
		  , UpdateData("UpdateData")
		  , SubscribeOlsList("SubscribeOlsList")
		  , UnsubscribeOlsList("UnsubscribeOlsList")
		  ;
		  private String value;
		  private Request(String value) {
			  this.value = value;
		  }
		  public String getValue() { return this.value; }
	  }
	  
	  public final static String FIELD_DATA = "data";
}
