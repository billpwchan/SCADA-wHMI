package com.thalesgroup.scadagen.bps.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
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
}
