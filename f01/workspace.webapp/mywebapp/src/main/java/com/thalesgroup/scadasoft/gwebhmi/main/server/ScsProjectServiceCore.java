package com.thalesgroup.scadasoft.gwebhmi.main.server;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ProjectServiceCore;

// Project specific ProjectServiceCore, this class is used to choose the presenter server
// corresponding to the presenter client (see Hypervisor MVP documentation)

public class ScsProjectServiceCore extends ProjectServiceCore {

	/** Logger */
    private final Logger s_logger = LoggerFactory.getLogger(ScsProjectServiceCore.class);

	public void beanStart() {
		s_logger.info("Bean Start");
	}

	public void beanStop() {
		s_logger.info("Bean Stop");
	}

	public void onSessionAuthenticatedCreated(HttpSession session, String userName) {
		s_logger.info("Bean new session: {} for user {}", session.getId(), userName);
	}

	public void onSessionAuthenticatedDestroyed(HttpSession session) {
		s_logger.info("Bean stop session: {}", session.getId());
	}
}
