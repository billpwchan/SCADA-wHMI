package com.thalesgroup.scadagen.bps.conf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.notification.tools.operator.IOperator;
import com.thalesgroup.scadagen.bps.conf.common.AttributeDataSourceType;
import com.thalesgroup.scadagen.bps.conf.common.Operator;
import com.thalesgroup.scadagen.bps.conf.common.PropertyDataSourceType;
import com.thalesgroup.scadagen.bps.conf.common.StatusDataSourceType;
import com.thalesgroup.scadagen.bps.conf.subscription_data_source.SubscriptionDataSource;
import com.thalesgroup.scadagen.bps.conf.subscription_data_source.SubscriptionDataSource.ConfiguredDataSource;
import com.thalesgroup.scadagen.bps.data.ConfiguredEntityStatusesDataDescriptionAbstract;
import com.thalesgroup.scadagen.bps.data.ConfiguredEntityStatusesInstancesDataDescription;
import com.thalesgroup.scadagen.bps.data.ConfiguredEntityStatusesTypeDataDescription;
import com.thalesgroup.scadagen.bps.data.TransientEntityDataDescription;
import com.thalesgroup.scadagen.bps.data.subscription.OperatorTool;


public class SubscriptionDataSourceConfFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionDataSourceConfFactory.class);

	//private BpsConfig config_;

	private SubscriptionDataSource dataSource_;
	
	private Operator criteria_;

	public SubscriptionDataSourceConfFactory(SubscriptionDataSource dataSource, Operator criteria) {
		dataSource_ = dataSource;
		criteria_ = criteria;
	}

	public boolean isConfiguredSubject() {
		return dataSource_.getConfiguredDataSource() != null;
	}

	public Set<ConfiguredEntityStatusesDataDescriptionAbstract> getConfiguredEntityDataSubscription() {
		ConfiguredDataSource configuredDataSource = dataSource_.getConfiguredDataSource();

		return getConfiguredEntityDataSubscription(configuredDataSource, criteria_,
				getBpsDataSourceStatuses(configuredDataSource));
	}

	private List<AttributeDataSourceType> getBpsDataSourceStatuses(ConfiguredDataSource configuredDataSource) {
		List<AttributeDataSourceType> listSources = new ArrayList<AttributeDataSourceType>();

		for (AttributeDataSourceType att: configuredDataSource.getStatuses()) {
			listSources.add(att);
		}

		return listSources;
	}

	public static Set<ConfiguredEntityStatusesDataDescriptionAbstract> getConfiguredEntityDataSubscription(
			ConfiguredDataSource configuredDataSource, Operator confCriteria,
			List<AttributeDataSourceType> listSources) {
		Set<ConfiguredEntityStatusesDataDescriptionAbstract> toReturn = new HashSet<ConfiguredEntityStatusesDataDescriptionAbstract>();

		Set<String> statusesName = new HashSet<String>();
		Set<String> properties = new HashSet<String>();

		for (AttributeDataSourceType source : listSources) {
			if ((source instanceof StatusDataSourceType)) {
				statusesName.add(source.getName());
			} else if ((source instanceof PropertyDataSourceType)) {
				properties.add(source.getName());
			}
		}

		ConfiguredEntityStatusesInstancesDataDescription desc;

		if (confCriteria == null) {
			desc = new ConfiguredEntityStatusesInstancesDataDescription(new HashSet<String>(configuredDataSource.getId()),
					properties, statusesName);
		} else {
			Set<IOperator> criteriaToAdd = new HashSet<IOperator>();

			try {
				criteriaToAdd = OperatorTool.convert(confCriteria);
			} catch (HypervisorException e) {
				LOGGER.error("Error while creating the subscription.", e);
			}
			desc = new ConfiguredEntityStatusesInstancesDataDescription(new HashSet<String>(configuredDataSource.getId()),
					properties, statusesName, criteriaToAdd);
		}

		toReturn.add(desc);

		for (String type : configuredDataSource.getType()) {
			ConfiguredEntityStatusesTypeDataDescription typeDesc;
			if (confCriteria == null) {
				typeDesc = new ConfiguredEntityStatusesTypeDataDescription(type, properties, statusesName);
			} else {
				Set<IOperator> criteriaToAdd = new HashSet<IOperator>();
				try {
					criteriaToAdd = OperatorTool.convert(confCriteria);
				} catch (HypervisorException e) {
					LOGGER.error("Error while creating the subscription.", e);
				}
				typeDesc = new ConfiguredEntityStatusesTypeDataDescription(type, properties, statusesName,
						criteriaToAdd);
			}

			toReturn.add(typeDesc);
		}

		return toReturn;
	}

	public Set<TransientEntityDataDescription> getTransientEntityDataSubscription() {
		SubscriptionDataSource.TransientDataSource transientDataSource = dataSource_.getTransientDataSource();

		return getTransientEntityDataSubscription(transientDataSource, criteria_);
	}

	private static Set<TransientEntityDataDescription> getTransientEntityDataSubscription(
			SubscriptionDataSource.TransientDataSource transientDataSource, Operator confCriteria) {
		Set<TransientEntityDataDescription> toReturn = new HashSet<TransientEntityDataDescription>();

		for (String type : transientDataSource.getType()) {
			TransientEntityDataDescription desc;

			if ((confCriteria == null)) {
				desc = new TransientEntityDataDescription(type);
			} else {
				Set<IOperator> criteriaToAdd = new HashSet<IOperator>();
				try {
					criteriaToAdd = OperatorTool.convert(confCriteria);
				} catch (HypervisorException e) {
					LOGGER.error("Error while creating the subscription.", e);
				}
				desc = new TransientEntityDataDescription(type, criteriaToAdd);
			}

			toReturn.add(desc);
		}

		return toReturn;
	}
}
