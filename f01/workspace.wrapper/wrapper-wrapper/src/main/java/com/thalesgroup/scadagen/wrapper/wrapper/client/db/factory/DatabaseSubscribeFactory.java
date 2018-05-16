package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabasePolling;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabasePollingSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabaseSubscription;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.DatabaseSubscriptionSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.DatabaseGroupPolling;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.DatabaseGroupPollingSingleton;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.diff.DatabaseGroupPollingDiff;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.diff.DatabaseGroupPollingDiffSingleton;

/**
 * Database Subscribe Class Factory
 * 
 * @author syau
 *
 */
public class DatabaseSubscribeFactory {

	private static final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(DatabaseSubscribeFactory.class.getName());
	
	/**
	 * Factory Method to return the instance of the Database Subscribe Object
	 * 
	 * @param key Name of the Database Subscribe Class Request
	 * @return    Instance of the Database Subscribe Object 
	 */
	public static DatabaseSubscribe_i get(String key) {
		final String function = "get";
		logger.begin(function);
		logger.debug(function, "key[{}]", key);
		DatabaseSubscribe_i databaseSubscribe_i = null;
		
		if ( null != key ) {

			if ( 0 == DatabaseSubscription.class.getSimpleName().compareTo(key) ) {
				databaseSubscribe_i = new DatabaseSubscription();
			}
			else if ( 0 == DatabaseSubscriptionSingleton.class.getSimpleName().compareTo(key) ) {
				databaseSubscribe_i = DatabaseSubscriptionSingleton.getInstance();
			}
			else if ( 0 == DatabasePolling.class.getSimpleName().compareTo(key) ) {
				databaseSubscribe_i = new DatabasePolling();
			}
			else if ( 0 == DatabasePollingSingleton.class.getSimpleName().compareTo(key) ) {
				databaseSubscribe_i = DatabasePollingSingleton.getInstance();
			} 
			else if ( 0 == DatabaseGroupPolling.class.getSimpleName().compareTo(key) ) {
				databaseSubscribe_i = new DatabaseGroupPolling();
			}
			else if ( 0 == DatabaseGroupPollingSingleton.class.getSimpleName().compareTo(key) ) {
				databaseSubscribe_i = DatabaseGroupPollingSingleton.getInstance();
			}
			else if ( 0 == DatabaseGroupPollingDiff.class.getSimpleName().compareTo(key) ) {
				databaseSubscribe_i = new DatabaseGroupPollingDiff();
			}
			else if ( 0 == DatabaseGroupPollingDiffSingleton.class.getSimpleName().compareTo(key) ) {
				databaseSubscribe_i = DatabaseGroupPollingDiffSingleton.getInstance();
			}
		}
		logger.end(function);
		return databaseSubscribe_i;
	}
}
