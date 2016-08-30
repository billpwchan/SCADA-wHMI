package com.thalesgroup.scadagen.bps.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.notification.ElementModificationType;
import com.thalesgroup.hv.data_v1.notification.EntityNotificationElementType;
import com.thalesgroup.hv.data_v1.notification.list.NotificationList;
import com.thalesgroup.hv.sdk.connector.api.notification.consumer.INotificationListConsumerCallback;
import com.thalesgroup.hv.ws.notification_v1.xsd.FilterType;
import com.thalesgroup.scadagen.bps.conf.bps.TriggerType;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.operation.VoidOperationCallback;
import com.thalesgroup.scadagen.bps.connector.subscription.IGenericSubscriptionConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.INotificationHandler;

public abstract class EntityManagerAbstract<T extends EntityDataDescriptionAbstract>
		implements INotificationListConsumerCallback, INotificationHandler {
	protected static final Logger LOGGER = LoggerFactory.getLogger(EntityManagerAbstract.class);

	private final IGenericSubscriptionConnector subscriptionConnector_;
	
	private final IGenericOperationConnector operationConnector_;

	private final ReentrantReadWriteLock lock_ = new ReentrantReadWriteLock(true);

	private final Lock readLock_ = lock_.readLock();

	private final Lock writeLock_ = lock_.writeLock();

	private boolean subscriptionStarted_ = false;

	private UUID subscriptionId_ = null;
		
	protected Map<String,AbstractEntityStatusesType> entityMap_ = new HashMap<String, AbstractEntityStatusesType>();
	
	protected List<TriggerType> triggerList_ = new ArrayList<TriggerType>();

	public EntityManagerAbstract(IGenericSubscriptionConnector subscriptionConnector, IGenericOperationConnector operationConnector) {
		if (subscriptionConnector == null) {
			throw new IllegalArgumentException("SubscriptionConnector cannot be null.");
		}

		subscriptionConnector_ = subscriptionConnector;
		operationConnector_ = operationConnector;
	}

	public Lock getReadLock() {
		return readLock_;
	}

	public Lock getWriteLock() {
		return writeLock_;
	}

	protected IGenericSubscriptionConnector getSubscriptionConnector() {
		return subscriptionConnector_;
	}

	public abstract void startSubscription(Set<T> paramSet) throws HypervisorException;

	protected void startSubscription(List<FilterType> filters) throws HypervisorException {
		LOGGER.trace("start subscription with {} filters", filters.size());
		getWriteLock().lock();
		try {
			for (FilterType filter : filters) {
				startSubscription(filter);
			}
		} finally {
			getWriteLock().unlock();
		}
	}

	private void startSubscription(FilterType filter) throws HypervisorException {
		getSubscriptionConnector().setOnNotificationListCallback(this);
		subscriptionId_ = getSubscriptionConnector().startSubscription(filter);
		subscriptionStarted_ = true;
		
		getOperationConnector().setOnOperationResponseCallback(new VoidOperationCallback());
	}

	public void stopSubscription() throws HypervisorException {
		getWriteLock().lock();
		try {
			if (subscriptionStarted_) {
				getSubscriptionConnector().stopSubscription(subscriptionId_);
				subscriptionStarted_ = false;
			}
		} finally {
			getWriteLock().unlock();
		}
	}

	@Override
	public void onNotificationListReception(UUID subscriptionId, NotificationList notificationList) {
		LOGGER.trace("onNotificationListReception {}", subscriptionId.toString());
		if (subscriptionId.compareTo(subscriptionId_) == 0) {
			onNotificationList(notificationList);
		}
	}

	public void onNotificationList(NotificationList notificationList) {
		LOGGER.trace("onNotificationList {} ", notificationList.toString());

		getWriteLock().lock();
		try {
			for (EntityNotificationElementType notifElem : notificationList.getEntityNotificationElement()) {
				onNotificationElement(notifElem);
			}

		} finally {
			getWriteLock().unlock();

		}
	}

	protected void onNotificationElement( EntityNotificationElementType notifElem) {
		AbstractEntityStatusesType entity = notifElem.getEntity();
		
		if (notifElem.getModificationType() == ElementModificationType.INSERT ||
				notifElem.getModificationType() == ElementModificationType.UPDATE) {
			entityMap_.put(entity.getId(), entity);
	
//			if (!triggerList_.isEmpty()) {
//				for (TriggerType trigger: triggerList_) {
//					if (CompareOperator(trigger.getCriteria())) {
//						for (ActionType actionHandler: trigger.getAction()) {
//							IAction action = ActionsManager.getInstance().getAction(actionHandler.getActionHandler());
//							if (action != null) {
//								action.execute(operationConnector_, actionHandler.getActionConfig());
//							}
//						}
//					}
//				}
//			}
		} else if (notifElem.getModificationType() == ElementModificationType.DELETE) {
			entityMap_.remove(entity.getId());
		}
	}
	
	public Map<String, AbstractEntityStatusesType> getEntityMap() {
		return entityMap_;
	}

	public void setTrigger(List<TriggerType> trigger) {
		if (trigger != null) {
			triggerList_ = trigger;
		}
	}
	
	public IGenericOperationConnector getOperationConnector() {
		return operationConnector_;
	}

}
