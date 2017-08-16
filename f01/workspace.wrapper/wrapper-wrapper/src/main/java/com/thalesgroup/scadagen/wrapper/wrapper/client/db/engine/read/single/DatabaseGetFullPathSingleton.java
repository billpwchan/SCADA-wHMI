package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i;

/**
 * Implementation the Database Get Children Operation in Singleton
 * 
 * @author syau
 *
 */
public class DatabaseGetFullPathSingleton extends DatabaseGetFullPath implements DatabaseSingleton_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DatabaseGetFullPathSingleton.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static DatabaseGetFullPathSingleton instance = null;
	private DatabaseGetFullPathSingleton() {}
	/**
	 * Get the Singleton instance
	 * 
	 * @return Singleton instance
	 */
	public static DatabaseGetFullPathSingleton getInstance() {
		if ( null == instance ) instance = new DatabaseGetFullPathSingleton();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetFullPath#connect()
	 */
	@Override
	public void connect() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.read.single.DatabaseGetFullPath#disconnect()
	 */
	@Override
	public void disconnect() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i#connectOnce()
	 */
	@Override
	public void connectOnce() {
		final String function = "connectOnce";
		logger.begin(className, function);
		super.connect();
		logger.end(className, function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i#disconnectOnce()
	 */
	@Override
	public void disconnectOnce() {
		final String function = "disconnectOnce";
		logger.begin(className, function);
		super.disconnect();
		logger.end(className, function);
	}
}
