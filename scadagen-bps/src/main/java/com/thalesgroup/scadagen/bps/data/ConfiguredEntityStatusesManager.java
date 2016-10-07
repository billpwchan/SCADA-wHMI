package com.thalesgroup.scadagen.bps.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.notification.ElementModificationType;
import com.thalesgroup.hv.data_v1.notification.EntityNotificationElementType;
import com.thalesgroup.hv.sdk.connector.notification.tools.ConfiguredSubscriptionBuilder;
import com.thalesgroup.hv.ws.notification_v1.xsd.FilterType;
import com.thalesgroup.scadagen.bps.conf.actions.ActionsManager;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.conf.bps.ActionType;
import com.thalesgroup.scadagen.bps.conf.bps.CriteriaType;
import com.thalesgroup.scadagen.bps.conf.bps.EntityCriteriaType;
import com.thalesgroup.scadagen.bps.conf.bps.TriggerType;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.IGenericSubscriptionConnector;

public class ConfiguredEntityStatusesManager
		extends EntityManagerAbstract<ConfiguredEntityStatusesDataDescriptionAbstract> {

	protected Set<ConfiguredEntityStatusesDataDescriptionAbstract> desc_ = null;
	
	public ConfiguredEntityStatusesManager(IGenericSubscriptionConnector subscriptionConnector, IGenericOperationConnector operationConnector) {
		super(subscriptionConnector, operationConnector);
	}

	public void startSubscription(Set<ConfiguredEntityStatusesDataDescriptionAbstract> entityDataDescriptions)
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

	public List<FilterType> getFilter(Set<ConfiguredEntityStatusesDataDescriptionAbstract> entityDataDescriptions)
			throws HypervisorConversionException {
		ConfiguredSubscriptionBuilder builder = new ConfiguredSubscriptionBuilder(getSubscriptionConnector().getTools());

		for (ConfiguredEntityStatusesDataDescriptionAbstract entityClientDescription : entityDataDescriptions) {
			if ((entityClientDescription instanceof ConfiguredEntityStatusesInstancesDataDescription)) {
				ConfiguredEntityStatusesInstancesDataDescription description = (ConfiguredEntityStatusesInstancesDataDescription) entityClientDescription;

				if ((!description.getEntityIds().isEmpty()) && (!description.getStatusesName().isEmpty())) {

					if (description.getCriteria().isEmpty()) {
						builder.addSubscribtionToIds(description.getEntityIds(), description.getStatusesName());
					} else {
						builder.addSubscribtionToIdsWithCriteria(description.getEntityIds(),
								description.getStatusesName(), description.getCriteria());
					}

				}
			} else if ((entityClientDescription instanceof ConfiguredEntityStatusesTypeDataDescription)) {
				ConfiguredEntityStatusesTypeDataDescription description = (ConfiguredEntityStatusesTypeDataDescription) entityClientDescription;

				if (description.getCriteria().isEmpty()) {
					builder.addSubscribtionToType(description.getEntityType(), description.getStatusesName());
				} else {
					builder.addSubscribtionToTypeWithCriteria(description.getEntityType(),
							description.getStatusesName(), description.getCriteria());
				}
			}
		}

		return builder.build();
	}

	@Override
	protected void onNotificationElement( EntityNotificationElementType notifElem) {
		super.onNotificationElement(notifElem);
		
		if (notifElem.getModificationType() == ElementModificationType.INSERT ||
				notifElem.getModificationType() == ElementModificationType.UPDATE) {
	
			if (!triggerList_.isEmpty()) {
				for (TriggerType trigger: triggerList_) {
					CriteriaType criteria = trigger.getCriteria();
					if (criteria != null) {
						if (criteria.getStatusCriteria() != null) {
							if (CompareOperator(notifElem.getEntity(), criteria.getStatusCriteria())) {
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
						} else {
							if (CompareEntitiesCriteria(criteria.getEntityCriteria()) == true) {
								LOGGER.trace("CompareEntitiesCriteria return true. trigger.getAction return {} entries", trigger.getAction().size());
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

	private boolean CompareEntitiesCriteria(List<EntityCriteriaType> entityCriteria) {

		for (EntityCriteriaType criteria: entityCriteria) {
			for (String entityID: criteria.getEntityID()) {
				if (getEntityMap().containsKey(entityID)) {
					if (!CompareOperator(getEntityMap().get(entityID), criteria.getStatusCriteria())) {
						LOGGER.trace("CompareOperator entityID [{}] return false", entityID);
						return false;
					}
				} else {
					LOGGER.trace("EntityID [{}] not found. CompareEntitiesCriteria return false", entityID);
					return false;
				}
			}
		}
		LOGGER.trace("CompareEntitiesCriteria return true");
		return true;
	}
	
	//
	// This is a fix to handle duplicate notifications when putting different equipment types
	// in the same subscription request
	// This also handles the duplicate notifications sent repeatedly by TVS connector
	@Override
	protected boolean needEntityUpdate(EntityNotificationElementType notifElem) throws HypervisorException {
		if (notifElem.getModificationType() == ElementModificationType.INSERT ||
			notifElem.getModificationType() == ElementModificationType.UPDATE) {
			AbstractEntityStatusesType entity = notifElem.getEntity();

			if (!getEntityMap().containsKey(entity.getId())) {
				LOGGER.trace("Entity [{}] not exists in entityMap", entity.getId());
				return true;
			}
					
			Set<String> statusesNames = new HashSet<String>();
			boolean bFoundDesc = false;
			for (ConfiguredEntityStatusesDataDescriptionAbstract desc: desc_) {
				if (desc instanceof ConfiguredEntityStatusesInstancesDataDescription) {
					ConfiguredEntityStatusesInstancesDataDescription cfgEntity = (ConfiguredEntityStatusesInstancesDataDescription)desc;
					if (cfgEntity.getEntityIds().contains(entity.getId())) {
						bFoundDesc = true;
					}
				} else if (desc instanceof ConfiguredEntityStatusesTypeDataDescription) {
					ConfiguredEntityStatusesTypeDataDescription cfgEntity = (ConfiguredEntityStatusesTypeDataDescription)desc;
					String entityType = getOperationConnector().getTools().getEquipmentTypeFromId(entity.getId());
					if (cfgEntity.getEntityType().equals(entityType)) {
						bFoundDesc = true;
					}
				}
				
				if (bFoundDesc) {
					statusesNames = desc.getStatusesName();
	
					for (String statusName: statusesNames) {
						AbstractAttributeType attNew = getOperationConnector().getTools().getDataHelper().getAttribute(entity, statusName);
						AbstractAttributeType attSaved = getOperationConnector().getTools().getDataHelper().getAttribute(getEntityMap().get(entity.getId()), statusName);
						if (!attNew.isValueEqual(attSaved)) {
							LOGGER.trace("Entity [{}] [{}] value has changed", entity.getId(), statusName);
							return true;
						} else {
							LOGGER.trace("Entity [{}] [{}] value not changed", entity.getId(), statusName);
						}
					}
					break;
				}
			}

		} else if (notifElem.getModificationType() == ElementModificationType.DELETE) {
			return true;
		}
		
		return false;
	}

}
