package com.thalesgroup.scadagen.bps.datasource;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.IConnectorTools;
import com.thalesgroup.scadagen.bps.SCADAgenBPS;
import com.thalesgroup.scadagen.bps.conf.bps.BpsConfig;
import com.thalesgroup.scadagen.bps.conf.common.DataSource;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.IGenericSubscriptionConnector;

public abstract class DataSourceAbstract<T extends DataSource> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAbstract.class);

	private Lock readLock_;

	private Lock writeLock_;

	private String configName_;

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

	public final void initProtected(BpsConfig config, DataSource dataSource) throws HypervisorException {
		T cast;
		try {
			cast = (T) type_.cast(dataSource);
		} catch (Exception e) {
			throw new HypervisorException("Error while casting the BPS Data Source from [" + dataSource.getClass()
					+ "] to [" + type_.getName() + "].", e);
		}

		init(config, cast);

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

	protected abstract void init(BpsConfig config, T paramT) throws HypervisorException;

	public final void startProtected() throws HypervisorException {
		getWriteLock().lock();
		try {
			start();
		} catch (Exception e) {
			LOGGER.error("Error while starting the BPS [" + configName_ + "] using the data source ["
					+ getDataSourceType() + "].", e);
		} finally {
			getWriteLock().unlock();
		}
	}

	protected abstract void start() throws HypervisorException;

	public final void pauseProtected() throws HypervisorException {
		getWriteLock().lock();
		try {
			pause();
		} catch (Exception e) {
			LOGGER.error("Error while pausing the BPS [" + configName_ + "] using the data source ["
					+ getDataSourceType() + "].", e);
		} finally {
			getWriteLock().unlock();
		}
	}

	protected abstract void pause() throws HypervisorException;

	public final void resumeProtected() throws HypervisorException {
		getWriteLock().lock();
		try {
			resume();
		} catch (Exception e) {
			LOGGER.error("Error while resuming the BPS [" + configName_ + "] using the data source ["
					+ getDataSourceType() + "].", e);
		} finally {
			getWriteLock().unlock();
		}
	}

	protected abstract void resume() throws HypervisorException;

	public final void stopProtected() throws HypervisorException {
		getWriteLock().lock();
		try {
			stop();
		} catch (Exception e) {
			LOGGER.error("Error while stopping the BPS [" + configName_ + "] using the data source ["
					+ getDataSourceType() + "].", e);
		} finally {
			getWriteLock().unlock();
		}
	}

	protected abstract void stop() throws HypervisorException;

	private String getDataSourceType() {
		return getClass().getSimpleName();
	}
}
