package com.thalesgroup.scadagen.bps.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
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
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.IGenericSubscriptionConnector;

public class TransientEntityManager extends EntityManagerAbstract<TransientEntityDataDescription> {
	
	protected Set<TransientEntityDataDescription> desc_ = null;

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
										action.execute(getOperationConnector(), new HashSet<EntityDataDescriptionAbstract>(desc_), actionHandler.getActionConfig(), notifElem.getEntity());
									} else {
										LOGGER.error("Error getting action handler [{}] is null", actionHandler.getActionHandler());
									}
								}
							}
						}
					} else {
						for (ActionType actionHandler: trigger.getAction()) {
							IAction action = ActionsManager.getInstance().getAction(actionHandler.getActionHandler());
							if (action != null) {
								LOGGER.trace("Execute action [{}] with config [{}]", actionHandler.getActionHandler(), actionHandler.getActionConfig());
								action.execute(getOperationConnector(), new HashSet<EntityDataDescriptionAbstract>(desc_), actionHandler.getActionConfig(), notifElem.getEntity());
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
