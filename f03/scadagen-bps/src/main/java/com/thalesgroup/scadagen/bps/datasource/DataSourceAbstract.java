package com.thalesgroup.scadagen.bps.datasource;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.IConnectorTools;
import com.thalesgroup.scadagen.bps.SCADAgenBPS;
import com.thalesgroup.scadagen.bps.conf.bps.BpsConfig;
import com.thalesgroup.scadagen.bps.conf.common.DataSource;
import com.thalesgroup.scadagen.bps.conf.subscription_data_source.SubscriptionDataSource;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.IGenericSubscriptionConnector;

public abstract class DataSourceAbstract<T extends DataSource> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAbstract.class);

	private Lock readLock_;

	private Lock writeLock_;

	private String configName_;

	@SuppressWarnings("unused")
	private Class<T> type_;

	private SCADAgenBPS bps_;

	private IGenericSubscriptionConnector subscriptionConnector_;

	private IGenericOperationConnector operationConnector_;

	private IConnectorTools connectorTools_;

	protected final void setLocks(Lock readLock, Lock writeLock) {
		if ((readLock_ != null) || (writeLock_ != null)) {
			LOGGER.warn(
					"Warning, trying to override the write and read lock of the data source, those locks should be set only once.");
		}

		readLock_ = readLock;
		writeLock_ = writeLock;
	}

	protected final Lock getReadLock() {
		return readLock_;
	}

	protected final Lock getWriteLock() {
		return writeLock_;
	}

	public DataSourceAbstract(Class<T> type) {
		type_ = type;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final void initProtected(BpsConfig config, Set<DataSource> dataSources) throws HypervisorException {
		LOGGER.trace("initProtected");
		Set<SubscriptionDataSource> ds = new HashSet(dataSources);

		init(config, (Set<T>) ds);

		if ((readLock_ == null) || (writeLock_ == null)) {
			LOGGER.trace("BPS data source [{}]: not any lock was set, create the default lock.", getDataSourceType());

			ReentrantReadWriteLock lock = new ReentrantReadWriteLock(false);
			setLocks(lock.readLock(), lock.writeLock());
		}
	}

	public void setBPS(SCADAgenBPS bps) {
		bps_ = bps;
	}

	protected SCADAgenBPS getBPS() {
		return bps_;
	}

	public void setSubscriptionConnector(IGenericSubscriptionConnector subscriptionConnector) {
		subscriptionConnector_ = subscriptionConnector;
	}

	protected IGenericSubscriptionConnector getSubscriptionConnector() {
		return subscriptionConnector_;
	}

	public void setOperationConnector(IGenericOperationConnector operationConnector) {
		operationConnector_ = operationConnector;
	}

	protected IGenericOperationConnector getOperationConnector() {
		return operationConnector_;
	}

	public void setConnectorTools(IConnectorTools connectorTools) {
		connectorTools_ = connectorTools;
	}

	protected IConnectorTools getConnectorTools() {
		return connectorTools_;
	}

	protected abstract void init(BpsConfig config, Set<T> paramT) throws HypervisorException;

	public final void startSubscriptionProtected() throws HypervisorException {
		getWriteLock().lock();
		try {
			LOGGER.trace("startSubscriptionProtected");
			startSubscription();
		} catch (Exception e) {
			LOGGER.error("Error while starting the BPS [" + configName_ + "] using the data source ["
					+ getDataSourceType() + "].", e);
		} finally {
			getWriteLock().unlock();
		}
	}

	protected abstract void startSubscription() throws HypervisorException;

	public final void pauseSubscriptionProtected() throws HypervisorException {
		getWriteLock().lock();
		try {
			LOGGER.trace("pauseSubscriptionProtected");
			pauseSubscription();
		} catch (Exception e) {
			LOGGER.error("Error while pausing the BPS [" + configName_ + "] using the data source ["
					+ getDataSourceType() + "].", e);
		} finally {
			getWriteLock().unlock();
		}
	}

	protected abstract void pauseSubscription() throws HypervisorException;

	public final void resumeSubscriptionProtected() throws HypervisorException {
		getWriteLock().lock();
		try {
			LOGGER.trace("resumeSubscriptionProtected");
			resumeSubscription();
		} catch (Exception e) {
			LOGGER.error("Error while resuming the BPS [" + configName_ + "] using the data source ["
					+ getDataSourceType() + "].", e);
		} finally {
			getWriteLock().unlock();
		}
	}

	protected abstract void resumeSubscription() throws HypervisorException;

	public final void stopProtectedSubscription() throws HypervisorException {
		getWriteLock().lock();
		try {
			LOGGER.trace("stopProtectedSubscription");
			stopSubscription();
		} catch (Exception e) {
			LOGGER.error("Error while stopping the BPS [" + configName_ + "] using the data source ["
					+ getDataSourceType() + "].", e);
		} finally {
			getWriteLock().unlock();
		}
	}

	protected abstract void stopSubscription() throws HypervisorException;

	private String getDataSourceType() {
		return getClass().getSimpleName();
	}
}
