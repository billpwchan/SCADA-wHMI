package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary;

public interface UILayoutSummary_i {
	
	public enum WidgetAttribute{
		  SimpleEventBus("SimpleEventBus")
		;
		private final String text;
		private WidgetAttribute (final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}

	public enum ParameterName {
		  EventBusName("EventBusName")
		, EventBusScope("EventBusScope")
		
		, ScsEnvIds("ScsEnvIds")
		
		, DatabaseReadingSingletonKey("DatabaseReadingSingletonKey")
		, DatabaseSubscribeSingletonKey("DatabaseSubscribeSingletonKey")
		, DatabaseSubscribeSingletonPeriodMillis("DatabaseSubscribeSingletonPeriodMillis")
		, DatabaseWritingSingleton("DatabaseWritingSingleton")
		
		, DisableInitDelay("DisableInitDelay")
		, DisableEnvUpDelay("DisableEnvUpDelay")
		
		, Init("Init")
		, EnvUp("EnvUp")
		, EnvDown("EnvDown")
		, Terminate("Terminate")
		
		, InitDelayMS("InitDelayMS")
		, InitDelay("InitDelay")
		
		, EnvUpDelayMS("EnvUpDelayMS")
		, EnvUpDelay("EnvUpDelay")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum ParameterValue {
		  Global("Global")
		;
		private final String text;
		private ParameterValue(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum LifeValue {
		    init("init")
		  , envup("envup")
		  , envdown("envdown")
		  , terminate("terminate")
		  , init_delay("init_delay")
		  , envup_delay("envup_delay")
		;
		private final String text;
		private LifeValue(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
