package com.thalesgroup.scadagen.bps.connector.subscription;

import com.thalesgroup.hv.data_v1.notification.list.NotificationList;

public abstract interface INotificationHandler {
	public abstract void onNotificationList (NotificationList paramNotificationList);
}
