package com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
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

/**
 * Database Subscribe Class Factory
 * 
 * @author t0096643
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

			if ( UIWidgetUtil.isEqual(key, DatabaseSubscription.class.getSimpleName()) ) {
				databaseSubscribe_i = new DatabaseSubscription(key);
			}
			else if ( UIWidgetUtil.isEqual(key, DatabaseSubscriptionSingleton.class.getSimpleName()) ) {
				databaseSubscribe_i = DatabaseSubscriptionSingleton.getInstance(key);
			}
			else if ( UIWidgetUtil.isEqual(key, DatabasePolling.class.getSimpleName()) ) {
				databaseSubscribe_i = new DatabasePolling(key);
			}
			else if ( UIWidgetUtil.isEqual(key, DatabasePollingSingleton.class.getSimpleName()) ) {
				databaseSubscribe_i = DatabasePollingSingleton.getInstance(key);
			} 
			else if ( UIWidgetUtil.isEqual(key, DatabaseGroupPolling.class.getSimpleName()) ) {
				databaseSubscribe_i = new DatabaseGroupPolling(key);
			}
			else if ( UIWidgetUtil.isEqual(key, DatabaseGroupPollingSingleton.class.getSimpleName()) ) {
				databaseSubscribe_i = DatabaseGroupPollingSingleton.getInstance(key);
			}
			else if ( UIWidgetUtil.isEqual(key, DatabaseGroupPollingDiff.class.getSimpleName()) ) {
				databaseSubscribe_i = new DatabaseGroupPollingDiff(key);
			}
			else if ( UIWidgetUtil.isEqual(key, DatabaseGroupPollingDiffSingleton.class.getSimpleName()) ) {
				databaseSubscribe_i = DatabaseGroupPollingDiffSingleton.getInstance(key);
			}
		}
		logger.end(function);
		return databaseSubscribe_i;
	}
}
