package com.thalesgroup.prj_gz_cocc.gwebhmi.main.server;

import java.util.Set;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.configuration.api.IConfiguration;
import com.thalesgroup.hypervisor.mwt.core.connector.subscription.SubscriptionConnector;
import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.IComputersManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.server.checker.IAuthorizationChecker;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.ConfiguredEntityStatusesDataDescriptionAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.ConfiguredEntityStatusesManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.opm.ConfiguredEntityStatusesOPMFilter;

public class CoccEquipementManager extends ConfiguredEntityStatusesManager {

	public CoccEquipementManager(OperatorOpmInfo operatorOpmInfo,
			SubscriptionConnector subscriptionConnector,
			IComputersManager computerManager,
			IAuthorizationChecker authorizationChecker) {
		super(operatorOpmInfo, subscriptionConnector, computerManager,
				authorizationChecker);
	}
	
	@Override
	public void startSubscription(Set<ConfiguredEntityStatusesDataDescriptionAbstract> entityDataDescriptions)
			throws HypervisorException {
		
		IConfigLoader configLoader = ServicesImplFactory.getInstance().getService(IConfigLoader.class);
		IConfiguration configuration = configLoader.getSharedConfiguration();

		// avoid standard behavior
		ConfiguredEntityStatusesOPMFilter opmFilter = new ConfiguredEntityStatusesOPMFilter(configuration, getOperatorOpmInfo(), getAuthorizationChecker());
	    Set<ConfiguredEntityStatusesDataDescriptionAbstract> consolidatedDescription = getDataEnrichment().consolidateDescription(entityDataDescriptions);
	    opmFilter.apply(consolidatedDescription);

	}

}
