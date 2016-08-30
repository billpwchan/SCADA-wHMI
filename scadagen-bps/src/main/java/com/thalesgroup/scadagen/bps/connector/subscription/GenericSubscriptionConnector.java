package com.thalesgroup.scadagen.bps.connector.subscription;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.hv.sdk.connector.IConnectorTools;
import com.thalesgroup.hv.sdk.connector.api.notification.consumer.INotificationListConsumerCallback;
import com.thalesgroup.hv.ws.notification_v1.xsd.FilterType;

public class GenericSubscriptionConnector implements IGenericSubscriptionConnector {
	
	private static final Logger logger_ = LoggerFactory.getLogger(GenericSubscriptionConnector.class);
	
	private Connector connector_ = null;
	INotificationListConsumerCallback notificationHandler_ = null;
	
	public GenericSubscriptionConnector(Connector connector) {
		connector_ = connector;
	}

	@Override
	public UUID startSubscription(FilterType subscription) throws HypervisorException {
		UUID uuid;
		if (connector_ == null) {
			logger_.error("Cannot start subscription, the connector is not initialized.");
			return null;
		}
		
		uuid = connector_.startSubscription(subscription);
		if (uuid == null) {
			throw new HypervisorException("Cannot start subscription, the subscription UUID is null.");
		}
		
		if (notificationHandler_ == null) {
			logger_.error("Notification callback is not set for subscription");
		}

		return uuid;
	}

	@Override
	public void stopSubscription(UUID subscriptionId) throws HypervisorException {
		connector_.stopSubscription(subscriptionId);
	}

	@Override
	public void setOnNotificationListCallback(INotificationListConsumerCallback callback)
			throws HypervisorException {
		connector_.setOnNotificationListCallback(callback);
		notificationHandler_ = callback;
	}

	@Override
	public IConnectorTools getTools() {
		return connector_;
	}

}
