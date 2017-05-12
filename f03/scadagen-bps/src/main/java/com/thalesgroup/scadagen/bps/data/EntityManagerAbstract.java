package com.thalesgroup.scadagen.bps.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import com.thalesgroup.scadagen.bps.actionhandlers.ActionUtils;
import com.thalesgroup.scadagen.bps.conf.actions.ActionsManager;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.conf.bps.ActionType;
import com.thalesgroup.scadagen.bps.conf.bps.CriteriaType;
import com.thalesgroup.scadagen.bps.conf.bps.NotificationHandlingMode;
import com.thalesgroup.scadagen.bps.conf.bps.TriggerType;
import com.thalesgroup.scadagen.bps.conf.common.And;
import com.thalesgroup.scadagen.bps.conf.common.ComputeEquals;
import com.thalesgroup.scadagen.bps.conf.common.ComputeIn;
import com.thalesgroup.scadagen.bps.conf.common.ComputedStatusOperator;
import com.thalesgroup.scadagen.bps.conf.common.Equals;
import com.thalesgroup.scadagen.bps.conf.common.In;
import com.thalesgroup.scadagen.bps.conf.common.Operator;
import com.thalesgroup.scadagen.bps.conf.common.PropertyType;
import com.thalesgroup.scadagen.bps.conf.common.StatusOperator;
import com.thalesgroup.scadagen.bps.conf.computers.ComputersManager;
import com.thalesgroup.scadagen.bps.conf.computers.IComputer;
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
	
	protected Set<EntityDataDescriptionAbstract> desc_ = null;
	
	protected NotificationHandlingMode notificationHandlingMode_ = NotificationHandlingMode.FULL_SET;

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
		
		if (notifElem.getModificationType() == ElementModificationType.INSERT) {
			if (notificationHandlingMode_ == NotificationHandlingMode.FULL_SET) {
				entityMap_.put(entity.getId(), entity);
				triggerAction(entity);
			} else {
				LOGGER.debug("notification ignored - notificationHandlingMode not FULL_SET");
			}
		} else if (notifElem.getModificationType() == ElementModificationType.UPDATE) {
			entityMap_.put(entity.getId(), entity);
			triggerAction(entity);
		} else if (notifElem.getModificationType() == ElementModificationType.DELETE) {
			entityMap_.remove(entity.getId());
		}
	}
	
	protected void triggerAction( AbstractEntityStatusesType entity) {

		if (!triggerList_.isEmpty()) {
			for (TriggerType trigger: triggerList_) {
				CriteriaType criteria = trigger.getCriteria();
				
				Map<String, AbstractAttributeType> attributeMap = new HashMap<String, AbstractAttributeType>();
				Map<String, String>propertiesMap = new HashMap<String, String>();
				try {
					if (desc_ != null) {
						attributeMap = ActionUtils.getAttributeMapFromConfig(getOperationConnector(), entity, new HashSet<EntityDataDescriptionAbstract>(desc_));
						if (attributeMap.isEmpty()) {
							LOGGER.warn("Unable to get attributes from entity");
						} else {
							if (LOGGER.isTraceEnabled()) {
								for (String attName: attributeMap.keySet()) {
									LOGGER.trace("attribute name available = [{}]", attName);
								}
							}
						}
					}
				} catch (HypervisorException e) {
					LOGGER.error("Error getting entity attributes from config. {}", e);
				}
				
				if (criteria != null) {
					boolean criteriaOK = true;
					if (criteria.getStatusCriteria() != null) {
						if (!compareOperator(entity, criteria.getStatusCriteria())) {
							LOGGER.trace("StatusCriteria CompareOperator return false");
							continue;
						}
					}

					for (ComputedStatusOperator computedStatusOperator: criteria.getComputedStatusCriteria()) {
						IComputer computer = ComputersManager.getInstance().getComputer(computedStatusOperator.getStatusComputer());
						if (computer == null) {
							LOGGER.error("Error loading status computer [{}]", computedStatusOperator.getStatusComputer());
							criteriaOK = false;
							break;
						}
						
						for (PropertyType property: computedStatusOperator.getConfigProperty()) {
							propertiesMap.put(property.getName(), property.getValue());
						}
						
						AbstractAttributeType returnAtt = computer.compute(attributeMap, propertiesMap);
						attributeMap.put(computedStatusOperator.getStatus(), returnAtt);
						
						LOGGER.trace("Compare computed status [{}]", computedStatusOperator.getStatus());
						
						if (!compareOperator(returnAtt, computedStatusOperator)) {
							LOGGER.trace("ComputedStatusCriteria CompareOperator return false");
							criteriaOK = false;
							break;
						}
					}

					if (criteriaOK) {
						execActions(trigger.getAction(), entity, attributeMap);
					}
				} else {
					execActions(trigger.getAction(), entity, attributeMap);
				}
			}
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
	
	public void setNotificationHandlingMode(NotificationHandlingMode mode) {
		notificationHandlingMode_ = mode;
	}
	
	public IGenericOperationConnector getOperationConnector() {
		return operationConnector_;
	}
	
	protected boolean compareOperator(AbstractEntityStatusesType entity, Operator op) {
		if (op instanceof And) {
			return compareOperator(entity, ((And)op).getFirstOperand()) && compareOperator(entity, ((And)op).getSecondOperand());
		} else if (op instanceof StatusOperator){
			LOGGER.trace("Compare operator [{}]", ((StatusOperator) op).getStatus());
			StatusOperator statusOp = (StatusOperator)op;
			String statusName = statusOp.getStatus();

			try {
				AbstractAttributeType att = getOperationConnector().getTools().getDataHelper().getAttribute(entity, statusName);
				return compareOperator(att, statusOp);

			} catch (EntityManipulationException e) {
				LOGGER.error("Error getting entity type. [{}]", e);
			}	
		}
		return false;
	}

	protected boolean compareOperator(AbstractAttributeType att, StatusOperator statusOp) {
		LOGGER.trace("compareOperator");
		if (statusOp instanceof Equals) {
			LOGGER.trace("statusOp instanceof Equals");
			if (att instanceof IntAttributeType) {
				int val = ((IntAttributeType) att).getValue();
				if (Integer.toString(val).compareTo(((Equals) statusOp).getValue()) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (att instanceof StringAttributeType) {
				
				if (((StringAttributeType)att).getValue().compareTo(((Equals) statusOp).getValue()) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (att instanceof FloatAttributeType) {
				float val = ((FloatAttributeType) att).getValue();
				if (Float.toString(val).compareTo(((Equals) statusOp).getValue()) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (att instanceof DoubleAttributeType) {
				double val = ((DoubleAttributeType) att).getValue();
				if (Double.toString(val).compareTo(((Equals) statusOp).getValue()) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (att instanceof BooleanAttributeType) {
				boolean val = ((BooleanAttributeType) att).isValue();
				if (Boolean.toString(val).compareTo(((Equals) statusOp).getValue()) == 0) {
					return true;
				} else {
					return false;
				}
			}

		} else if ((statusOp instanceof In) || (statusOp instanceof ComputeIn)) {
			List<String> list;
			if (statusOp instanceof In) {
				LOGGER.trace("statusOp instanceof In");
				list = ((In) statusOp).getValue();
			} else {
				LOGGER.trace("statusOp instanceof ComputeIn");
				list = ((ComputeIn) statusOp).getValue();
			}
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
		} else if (statusOp instanceof ComputeEquals) {
			LOGGER.trace("statusOp instanceof ComputeEquals");
			if (att instanceof IntAttributeType) {
				int val = ((IntAttributeType) att).getValue();
				if (Integer.toString(val).compareTo(((ComputeEquals) statusOp).getValue()) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (att instanceof StringAttributeType) {
				
				if (((StringAttributeType)att).getValue().compareTo(((ComputeEquals) statusOp).getValue()) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (att instanceof FloatAttributeType) {
				float val = ((FloatAttributeType) att).getValue();
				if (Float.toString(val).compareTo(((ComputeEquals) statusOp).getValue()) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (att instanceof DoubleAttributeType) {
				double val = ((DoubleAttributeType) att).getValue();
				if (Double.toString(val).compareTo(((ComputeEquals) statusOp).getValue()) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (att instanceof BooleanAttributeType) {
				boolean val = ((BooleanAttributeType) att).isValue();
				if (Boolean.toString(val).compareTo(((ComputeEquals) statusOp).getValue()) == 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		LOGGER.trace("statusOp not match predefined operation");

		return false;
	}
	

	protected void execActions(List<ActionType> actionList, AbstractEntityStatusesType entity, Map<String, AbstractAttributeType> attributeMap) {	
		for (ActionType actionHandler: actionList) {
			IAction action = ActionsManager.getInstance().getAction(actionHandler.getActionHandler());
			if (action != null) {
				LOGGER.trace("Execute action [{}] with config [{}]", actionHandler.getActionHandler(), actionHandler.getActionConfig());
				action.execute(getOperationConnector(), entity, attributeMap, actionHandler.getActionConfig());
			} else {
				LOGGER.error("Error getting action handler [{}] is null", actionHandler.getActionHandler());
			}
		}
	}
}
