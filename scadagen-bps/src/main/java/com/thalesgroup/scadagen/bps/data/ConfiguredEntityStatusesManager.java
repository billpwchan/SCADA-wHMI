package com.thalesgroup.scadagen.bps.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.notification.ElementModificationType;
import com.thalesgroup.hv.data_v1.notification.EntityNotificationElementType;
import com.thalesgroup.hv.sdk.connector.notification.tools.ConfiguredSubscriptionBuilder;
import com.thalesgroup.hv.ws.notification_v1.xsd.FilterType;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.IGenericSubscriptionConnector;

public class ConfiguredEntityStatusesManager
		extends EntityManagerAbstract<ConfiguredEntityStatusesDataDescriptionAbstract> {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ConfiguredEntityStatusesManager.class);
	
	public ConfiguredEntityStatusesManager(IGenericSubscriptionConnector subscriptionConnector, IGenericOperationConnector operationConnector) {
		super(subscriptionConnector, operationConnector);
	}

	public void startSubscription(Set<ConfiguredEntityStatusesDataDescriptionAbstract> entityDataDescriptions)
			throws HypervisorException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Ask subscribe to [{}].", entityDataDescriptions.toString());
		}

		if (!entityDataDescriptions.isEmpty()) {
			desc_ = new HashSet<EntityDataDescriptionAbstract>(entityDataDescriptions);
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
			for (EntityDataDescriptionAbstract desc: desc_) {
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
					statusesNames = ((ConfiguredEntityStatusesDataDescriptionAbstract)desc).getStatusesName();
	
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
