package com.thalesgroup.scadagen.bps.datasource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.scadagen.bps.conf.SubscriptionDataSourceConfFactory;
import com.thalesgroup.scadagen.bps.conf.bps.BpsConfig;
import com.thalesgroup.scadagen.bps.conf.bps.TriggerType;
import com.thalesgroup.scadagen.bps.conf.subscription_data_source.SubscriptionDataSource;
import com.thalesgroup.scadagen.bps.data.ConfiguredEntityStatusesDataDescriptionAbstract;
import com.thalesgroup.scadagen.bps.data.ConfiguredEntityStatusesManager;
import com.thalesgroup.scadagen.bps.data.EntityManagerAbstract;
import com.thalesgroup.scadagen.bps.data.TransientEntityDataDescription;
import com.thalesgroup.scadagen.bps.data.TransientEntityManager;

public class SubscriptionDataSourceImpl extends DataSourceAbstract<SubscriptionDataSource> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionDataSourceImpl.class);

	private EntityManagerAbstract<?> entitiesManager_;

	private String bpsConfigId_;

	private List<SubscriptionDataSourceConfFactory> confFactoryList_;

	public SubscriptionDataSourceImpl() {
		super(SubscriptionDataSource.class);
	}

	protected void init(BpsConfig config, Set<SubscriptionDataSource> dataSources) {

		bpsConfigId_ = config.getBpsConfiguration().getName();
		
		confFactoryList_ = new ArrayList();
		
		if (confFactoryList_ != null) {
			
			for (SubscriptionDataSource dataSource: dataSources ) {
				SubscriptionDataSourceConfFactory confFactory = new SubscriptionDataSourceConfFactory(dataSource, config.getSubject().getCriteria());
				confFactoryList_.add(confFactory);
			}

			if (isCfgSubject()) {
				entitiesManager_ = getBPS().createCfgEntitiesManager();
			} else {
				entitiesManager_ = getBPS().createTransientEntitiesManager();
			}
			List<TriggerType> trigger = config.getTrigger();
			if (trigger != null) {
				entitiesManager_.setTrigger(trigger);
			}
	
			setLocks(entitiesManager_.getReadLock(), entitiesManager_.getWriteLock());
		}
	}

	private void subscribe() throws HypervisorException {
		if (isCfgSubject()) {
			LOGGER.trace("BPS config [{}] start subscription to configured entities.", bpsConfigId_);
			ConfiguredEntityStatusesManager manager = (ConfiguredEntityStatusesManager) entitiesManager_;
			
			Set<ConfiguredEntityStatusesDataDescriptionAbstract> entityDescSet = new HashSet<ConfiguredEntityStatusesDataDescriptionAbstract>();
			for (SubscriptionDataSourceConfFactory confFactory: confFactoryList_) {
				for (ConfiguredEntityStatusesDataDescriptionAbstract entityDesc: confFactory.getConfiguredEntityDataSubscription()) {
					entityDescSet.add(entityDesc);
				}
			}
			
			manager.startSubscription(entityDescSet);
		} else {
			LOGGER.trace("BPS config [{}] start subscription to transient entities.", bpsConfigId_);
			TransientEntityManager manager = (TransientEntityManager) entitiesManager_;
			
			Set<TransientEntityDataDescription> entityDescSet = new HashSet<TransientEntityDataDescription>();
			for (SubscriptionDataSourceConfFactory confFactory: confFactoryList_) {
				for (TransientEntityDataDescription entityDesc: confFactory.getTransientEntityDataSubscription()) {
					entityDescSet.add(entityDesc);
				}
			}

			manager.startSubscription(entityDescSet);
		}
	}

	private boolean isCfgSubject() {
		return confFactoryList_.get(0).isConfiguredSubject();
	}

	private void unsubscribe() throws HypervisorException {
		if (entitiesManager_ != null) {
			LOGGER.trace("BPS config [{}] stop subscription to entities.", bpsConfigId_);
			entitiesManager_.stopSubscription();
		}
	}

	public void startSubscription() throws HypervisorException {
		subscribe();
	}

	protected void pauseSubscription() throws HypervisorException {
	}

	protected void resumeSubscription() throws HypervisorException {
	}

	public void stopSubscription() throws HypervisorException {
		unsubscribe();
	}

	public EntityManagerAbstract<?> getEntitiesManager() {
		return entitiesManager_;
	}

}
