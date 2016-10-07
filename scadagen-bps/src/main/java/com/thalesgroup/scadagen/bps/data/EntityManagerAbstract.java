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
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.attribute.BooleanAttributeType;
import com.thalesgroup.hv.data_v1.attribute.DoubleAttributeType;
import com.thalesgroup.hv.data_v1.attribute.FloatAttributeType;
import com.thalesgroup.hv.data_v1.attribute.IntAttributeType;
import com.thalesgroup.hv.data_v1.attribute.StringAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.notification.ElementModificationType;
import com.thalesgroup.hv.data_v1.notification.EntityNotificationElementType;
import com.thalesgroup.hv.data_v1.notification.list.NotificationList;
import com.thalesgroup.hv.sdk.connector.api.notification.consumer.INotificationListConsumerCallback;
import com.thalesgroup.hv.ws.notification_v1.xsd.FilterType;
import com.thalesgroup.scadagen.bps.conf.bps.TriggerType;
import com.thalesgroup.scadagen.bps.conf.common.And;
import com.thalesgroup.scadagen.bps.conf.common.Equals;
import com.thalesgroup.scadagen.bps.conf.common.In;
import com.thalesgroup.scadagen.bps.conf.common.Operator;
import com.thalesgroup.scadagen.bps.conf.common.StatusOperator;
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
	
	protected List<FilterType> filters_ = null;

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
			filters_ = filters;
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
		LOGGER.trace("onNotificationListReception [{}]", subscriptionId.toString());
		if (subscriptionId.compareTo(subscriptionId_) == 0) {
			onNotificationList(notificationList);
		}
	}

	public void onNotificationList(NotificationList notificationList) {
		LOGGER.trace("onNotificationList [{}] ", notificationList.toString());

		getWriteLock().lock();
		try {
			NotificationList filteredNotifList = new NotificationList();

			// Filter out entities not needed to update
			for (EntityNotificationElementType notifElem : notificationList.getEntityNotificationElement()) {
				if (needEntityUpdate(notifElem)) {
					LOGGER.trace("needEntityUpdate return true");
					//onNotificationElement(notifElem);
					filteredNotifList.getEntityNotificationElement().add(notifElem);
				} else {
					LOGGER.trace("needEntityUpdate return false");
				}
			}
			
			for (EntityNotificationElementType notifElem : filteredNotifList.getEntityNotificationElement()) {
				onNotificationElement(notifElem);
			}

		} catch (HypervisorException e) {
			LOGGER.error("Error updating status from notification. [{}]", e);
		} finally {
			getWriteLock().unlock();

		}
	}

	protected boolean needEntityUpdate(EntityNotificationElementType notifElem) throws HypervisorException {		
		return true;
	}

	protected void onNotificationElement( EntityNotificationElementType notifElem) {
		AbstractEntityStatusesType entity = notifElem.getEntity();
		
		if (notifElem.getModificationType() == ElementModificationType.INSERT ||
				notifElem.getModificationType() == ElementModificationType.UPDATE) {
			entityMap_.put(entity.getId(), entity);
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
	
	protected boolean CompareOperator(AbstractEntityStatusesType entity, Operator op) {
		if (op instanceof And) {
			return CompareOperator(entity, ((And)op).getFirstOperand()) && CompareOperator(entity, ((And)op).getSecondOperand());
		} else if (op instanceof StatusOperator){
			LOGGER.trace("Compare operator [{}]", ((StatusOperator) op).getStatus());
			StatusOperator statusOp = (StatusOperator)op;
			String statusName = statusOp.getStatus();

			try {
				AbstractAttributeType att = getOperationConnector().getTools().getDataHelper().getAttribute(entity, statusName);

				if (statusOp instanceof Equals) {
					if (att instanceof IntAttributeType) {
						int val = ((IntAttributeType) att).getValue();
						if (Integer.toString(val).compareTo(((Equals) statusOp).getValue()) == 0) {
							LOGGER.trace("Compare int value [{}] return true", statusName);
							return true;
						} else {
							LOGGER.trace("Compare int value [{}] return false", statusName);
							return false;
						}
					} else if (att instanceof StringAttributeType) {
						if (((StringAttributeType)att).getValue().compareTo(((Equals) statusOp).getValue()) == 0) {
							LOGGER.trace("Compare String value [{}] return true", statusName);
							return true;
						} else {
							LOGGER.trace("Compare String value [{}] return false", statusName);
							return false;
						}
					} else if (att instanceof FloatAttributeType) {
						float val = ((FloatAttributeType) att).getValue();
						if (Float.toString(val).compareTo(((Equals) statusOp).getValue()) == 0) {
							LOGGER.trace("Compare float value [{}] return true", statusName);
							return true;
						} else {
							LOGGER.trace("Compare float value [{}] return false", statusName);
							return false;
						}
					} else if (att instanceof DoubleAttributeType) {
						double val = ((DoubleAttributeType) att).getValue();
						if (Double.toString(val).compareTo(((Equals) statusOp).getValue()) == 0) {
							LOGGER.trace("Compare double value [{}] return true", statusName);
							return true;
						} else {
							LOGGER.trace("Compare double value [{}] return false", statusName);
							return false;
						}
					} else if (att instanceof BooleanAttributeType) {
						boolean val = ((BooleanAttributeType) att).isValue();
						if (Boolean.toString(val).compareTo(((Equals) statusOp).getValue()) == 0) {
							LOGGER.trace("Compare boolean value [{}] return true", statusName);
							return true;
						} else {
							LOGGER.trace("Compare boolean value [{}] return false", statusName);
							return false;
						}
					}

				} else if (statusOp instanceof In) {
					List<String> list = ((In) statusOp).getValue();		
					if (att instanceof IntAttributeType) {
						int val = ((IntAttributeType) att).getValue();						
						if (list.contains(Integer.toString(val))) {
							return true;
						} else {
							return false;
						}
					} else if (att instanceof StringAttributeType) {		
						if (list.contains(((StringAttributeType) att).getValue())) {
							return true;
						} else {
							return false;
						}
					} else if (att instanceof FloatAttributeType) {		
						float val = ((FloatAttributeType) att).getValue();						
						if (list.contains(Float.toString(val))) {
							return true;
						} else {
							return false;
						}
					} else if (att instanceof DoubleAttributeType) {		
						double val = ((DoubleAttributeType) att).getValue();						
						if (list.contains(Double.toString(val))) {
							return true;
						} else {
							return false;
						}
					} else if (att instanceof BooleanAttributeType) {		
						boolean val = ((BooleanAttributeType) att).isValue();						
						if (list.contains(Boolean.toString(val))) {
							return true;
						} else {
							return false;
						}
					}
				}
			} catch (SecurityException e) {
				LOGGER.error("Error getting entity type. [{}]", e);
			} catch (IllegalArgumentException e) {
				LOGGER.error("Error getting entity type. [{}]", e);
			} catch (EntityManipulationException e) {
				LOGGER.error("Error getting entity type. [{}]", e);
			}	
		}
		return false;
	}

}
