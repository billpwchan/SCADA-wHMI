package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabasePolling;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabasePollingSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabaseSubscription;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabaseSubscriptionSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.DatabaseGroupPolling;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.DatabaseGroupPollingSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.diff.DatabaseGroupPollingDiff;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.diff.DatabaseGroupPollingDiffSingleton;

public class DatabaseSubscribeFactory {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(DatabaseSubscribeFactory.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static DatabaseSubscribe_i get(String key) {
		final String function = "get";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}]", key);
		DatabaseSubscribe_i databaseSubscribe_i = null;
		
		if ( null != key ) {
			
			String strDatabaseSubscription				= UIWidgetUtil.getClassSimpleName(DatabaseSubscription.class.getName());
			String strDatabaseSubscriptionSingleton		= UIWidgetUtil.getClassSimpleName(DatabaseSubscriptionSingleton.class.getName());
			
			String strDatabasePolling					= UIWidgetUtil.getClassSimpleName(DatabasePolling.class.getName());
			String strDatabasePollingSingleton			= UIWidgetUtil.getClassSimpleName(DatabasePollingSingleton.class.getName());
			
			String strDatabaseGroupPolling				= UIWidgetUtil.getClassSimpleName(DatabaseGroupPolling.class.getName());
			String strDatabaseGroupPollingSingleton		= UIWidgetUtil.getClassSimpleName(DatabaseGroupPollingSingleton.class.getName());
			
			String strDatabaseGroupPollingDiff			= UIWidgetUtil.getClassSimpleName(DatabaseGroupPollingDiff.class.getName());
			String strDatabaseGroupPollingDiffSingleton	= UIWidgetUtil.getClassSimpleName(DatabaseGroupPollingDiffSingleton.class.getName());
			
			if ( 0 == key.compareTo(strDatabaseSubscription) ) {
				databaseSubscribe_i = new DatabaseSubscription();
			}
			else if ( 0 == key.compareTo(strDatabaseSubscriptionSingleton) ) {
				databaseSubscribe_i = DatabaseSubscriptionSingleton.getInstance();
			}
			else if ( 0 == key.compareTo(strDatabasePolling) ) {
				databaseSubscribe_i = new DatabasePolling();
			}
			else if ( 0 == key.compareTo(strDatabasePollingSingleton) ) {
				databaseSubscribe_i = DatabasePollingSingleton.getInstance();
			} 
			else if ( 0 == key.compareTo(strDatabaseGroupPolling) ) {
				databaseSubscribe_i = new DatabaseGroupPolling();
			}
			else if ( 0 == key.compareTo(strDatabaseGroupPollingSingleton) ) {
				databaseSubscribe_i = DatabaseGroupPollingSingleton.getInstance();
			}
			else if ( 0 == key.compareTo(strDatabaseGroupPollingDiff) ) {
				databaseSubscribe_i = new DatabaseGroupPollingDiff();
			}
			else if ( 0 == key.compareTo(strDatabaseGroupPollingDiffSingleton) ) {
				databaseSubscribe_i = DatabaseGroupPollingDiffSingleton.getInstance();
			}
		}
		logger.end(className, function);
		return databaseSubscribe_i;
	}
}
