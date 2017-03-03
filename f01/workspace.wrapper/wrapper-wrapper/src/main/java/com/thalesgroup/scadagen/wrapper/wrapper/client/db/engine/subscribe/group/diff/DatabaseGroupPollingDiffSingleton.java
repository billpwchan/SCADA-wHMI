package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.diff;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i;

public class DatabaseGroupPollingDiffSingleton extends DatabaseGroupPollingDiff implements DatabaseSingleton_i {

	private final String className = UIWidgetUtil.getClassSimpleName(DatabaseGroupPollingDiffSingleton.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static DatabaseGroupPollingDiffSingleton instance = null;
	private DatabaseGroupPollingDiffSingleton() {}
	public static DatabaseGroupPollingDiffSingleton getInstance() { 
		if ( null == instance ) instance = new DatabaseGroupPollingDiffSingleton();
		return instance;
	}
	
	
	@Override
	public void connect() {
		
	}
	
	@Override
	public void disconnect() {
		
	}
	
	@Override
	public void connectOnce() {
		final String function = "connectOnce";
		logger.begin(className, function);
		super.connect();
		logger.end(className, function);
	}
	
	@Override
	public void disconnectOnce() {
		final String function = "disconnectOnce";
		logger.begin(className, function);
		super.disconnect();
		logger.end(className, function);
	}
}
