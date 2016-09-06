package com.thalesgroup.scadagen.bps.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.attribute.IntAttributeType;
import com.thalesgroup.hv.data_v1.attribute.StringAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.notification.ElementModificationType;
import com.thalesgroup.hv.data_v1.notification.EntityNotificationElementType;
import com.thalesgroup.hv.sdk.connector.notification.tools.TransientSubscriptionBuilder;
import com.thalesgroup.hv.ws.notification_v1.xsd.FilterType;
import com.thalesgroup.scadagen.bps.conf.actions.ActionsManager;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.conf.bps.ActionType;
import com.thalesgroup.scadagen.bps.conf.bps.CriteriaType;
import com.thalesgroup.scadagen.bps.conf.bps.TriggerType;
import com.thalesgroup.scadagen.bps.conf.common.And;
import com.thalesgroup.scadagen.bps.conf.common.Equals;
import com.thalesgroup.scadagen.bps.conf.common.In;
import com.thalesgroup.scadagen.bps.conf.common.Operator;
import com.thalesgroup.scadagen.bps.conf.common.StatusOperator;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.IGenericSubscriptionConnector;

public class TransientEntityManager extends EntityManagerAbstract<TransientEntityDataDescription> {
	
	Set<TransientEntityDataDescription> desc_ = null;

	public TransientEntityManager(IGenericSubscriptionConnector subscriptionConnector, IGenericOperationConnector operationConnector) {
		super(subscriptionConnector, operationConnector);
	}

	public void startSubscription(Set<TransientEntityDataDescription> entityDataDescriptions)
			throws HypervisorException {
		
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Ask subscribe to [{}].", entityDataDescriptions.toString());
		}

		if (!entityDataDescriptions.isEmpty()) {
			desc_ = entityDataDescriptions;
			List<FilterType> filters = getFilter(entityDataDescriptions);
			startSubscription(filters);
		}
	}

	public List<FilterType> getFilter(Set<TransientEntityDataDescription> entityDataDescriptions)
			throws HypervisorConversionException {
		TransientSubscriptionBuilder builder = new TransientSubscriptionBuilder(getSubscriptionConnector().getTools());

		for (TransientEntityDataDescription desc : entityDataDescriptions) {
			if (desc.getCriteria().isEmpty()) {
				builder.addSubscriptionToType(desc.getType());
			} else {
				builder.addSubscriptionToTypeWithCriteria(desc.getType(), desc.getCriteria());
			}
		}

		return builder.build();
	}
	
	@Override
	protected void onNotificationElement( EntityNotificationElementType notifElem) {
		super.onNotificationElement(notifElem);
		
		if (notifElem.getModificationType() == ElementModificationType.INSERT ||
				notifElem.getModificationType() == ElementModificationType.UPDATE) {
			
			AbstractEntityStatusesType entity = notifElem.getEntity();

			Set<AbstractEntityStatusesType> entities = new HashSet<AbstractEntityStatusesType>();
			entities.add(entity);
	
			if (!triggerList_.isEmpty()) {
				for (TriggerType trigger: triggerList_) {
					CriteriaType criteria = trigger.getCriteria();
					if (criteria != null) {
						if (criteria.getStatusCriteria() != null) {
							if (CompareOperator(entity, criteria.getStatusCriteria())) {
								LOGGER.trace("CompareOperator return true. trigger.getAction return {} entries", trigger.getAction().size());
								for (ActionType actionHandler: trigger.getAction()) {
									IAction action = ActionsManager.getInstance().getAction(actionHandler.getActionHandler());
									if (action != null) {
										LOGGER.trace("Execute action [{}] with config [{}]", actionHandler.getActionHandler(), actionHandler.getActionConfig());
										action.execute(getOperationConnector(), actionHandler.getActionConfig(), new HashSet<AbstractEntityStatusesType>(getEntityMap().values()));
									} else {
										LOGGER.error("Error getting action handler [{}] is null", actionHandler.getActionHandler());
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public boolean CompareOperator(AbstractEntityStatusesType entity, Operator op) {
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
