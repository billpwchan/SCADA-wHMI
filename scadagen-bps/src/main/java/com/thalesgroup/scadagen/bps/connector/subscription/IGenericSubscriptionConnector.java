package com.thalesgroup.scadagen.bps.connector.subscription;

import java.util.UUID;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.IConnectorTools;
import com.thalesgroup.hv.sdk.connector.api.notification.consumer.INotificationListConsumerCallback;
import com.thalesgroup.hv.ws.notification_v1.xsd.FilterType;

// Generic interface to do subsciption with HV Connector SDK
public abstract interface IGenericSubscriptionConnector {

	public abstract UUID startSubscription(FilterType paramFilterType) throws HypervisorException;

	public abstract void stopSubscription(UUID paramUUID) throws HypervisorException;

	public abstract void setOnNotificationListCallback(
			INotificationListConsumerCallback paramINotificationListConsumerCallback) throws HypervisorException;

	public abstract IConnectorTools getTools();
}
