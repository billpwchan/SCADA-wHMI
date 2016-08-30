package com.thalesgroup.scadagen.bps.datasource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.scadagen.bps.conf.SubscriptionDataSourceConfFactory;
import com.thalesgroup.scadagen.bps.conf.bps.BpsConfig;
import com.thalesgroup.scadagen.bps.conf.bps.TriggerType;
import com.thalesgroup.scadagen.bps.conf.subscription_data_source.SubscriptionDataSource;
import com.thalesgroup.scadagen.bps.data.ConfiguredEntityStatusesManager;
import com.thalesgroup.scadagen.bps.data.EntityManagerAbstract;
import com.thalesgroup.scadagen.bps.data.TransientEntityManager;

public class SubscriptionDataSourceImpl extends DataSourceAbstract<SubscriptionDataSource> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionDataSourceImpl.class);

	private EntityManagerAbstract<?> entitiesManager_;

	private String bpsConfigId_;

	private SubscriptionDataSourceConfFactory confFactory_;

	public SubscriptionDataSourceImpl() {
		super(SubscriptionDataSource.class);
	}

	protected void init(BpsConfig config, SubscriptionDataSource dataSource) {

		bpsConfigId_ = config.getBpsConfiguration().getName();

		confFactory_ = new SubscriptionDataSourceConfFactory(config, dataSource);

		if (confFactory_.isConfiguredSubject()) {
			entitiesManager_ = getBPS().createCfgEntitiesManager();
		} else {
			entitiesManager_ = getBPS().createTransientEntitiesManager();
		}
		List<TriggerType> trigger = config.getTrigger();
		if (trigger != null) {
			entitiesManager_.setTrigger(trigger);
		}

		setLocks(entitiesManager_.getReadLock(), entitiesManager_.getWriteLock());

		//entitiesManager_.addConsumer(getGdgDataModuleChain());
	}

	private void subscribe() throws HypervisorException {
		if (isCfgSubject()) {
			LOGGER.trace("BPS config {} start subscription to configured entities.", bpsConfigId_);
			ConfiguredEntityStatusesManager manager = (ConfiguredEntityStatusesManager) entitiesManager_;

			manager.startSubscription(confFactory_.getConfiguredEntityDataSubscription());
		} else {
			LOGGER.trace("BPS config {} start subscription to transient entities.", bpsConfigId_);
			TransientEntityManager manager = (TransientEntityManager) entitiesManager_;

			manager.startSubscription(confFactory_.getTransientEntityDataSubscription());
		}
	}

	private boolean isCfgSubject() {
		return confFactory_.isConfiguredSubject();
	}

	private void unsubscribe() throws HypervisorException {
		if (entitiesManager_ != null) {
			LOGGER.trace("BPS config {} stop subscription to entities.", bpsConfigId_);
			entitiesManager_.stopSubscription();
		}
	}

	public void start() throws HypervisorException {
		subscribe();
	}

	protected void pause() throws HypervisorException {
	}

	protected void resume() throws HypervisorException {
	}

	public void stop() throws HypervisorException {
		unsubscribe();
	}

	public EntityManagerAbstract<?> getEntitiesManager() {
		return entitiesManager_;
	}

}
