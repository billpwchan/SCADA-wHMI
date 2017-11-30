package com.thalesgroup.scadagen.bps.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.notification.tools.TransientSubscriptionBuilder;
import com.thalesgroup.hv.ws.notification_v1.xsd.FilterType;
import com.thalesgroup.scadagen.bps.conf.bps.NotificationHandlingMode;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.IGenericSubscriptionConnector;

public class TransientEntityManager extends EntityManagerAbstract<TransientEntityDataDescription> {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(TransientEntityManager.class);

	public TransientEntityManager(IGenericSubscriptionConnector subscriptionConnector, IGenericOperationConnector operationConnector) {
		super(subscriptionConnector, operationConnector);
		
		// Override default NotificationHandlingMode for transient (alarm, event, olslist) type
		notificationHandlingMode_ = NotificationHandlingMode.NEW_CHANGE_ONLY;
	}

	public void startSubscription(Set<TransientEntityDataDescription> entityDataDescriptions)
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

}
