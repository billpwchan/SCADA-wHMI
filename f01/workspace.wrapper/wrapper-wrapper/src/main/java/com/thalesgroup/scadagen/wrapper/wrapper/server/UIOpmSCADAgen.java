package com.thalesgroup.scadagen.wrapper.wrapper.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.server.rpc.session.OpmSessionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.server.rpc.session.SessionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OpmRequestDto;

public class UIOpmSCADAgen implements UIOpm_i {

	private final static Logger logger = LoggerFactory.getLogger(UIOpmSCADAgen.class.getName());
	
	private static UIOpm_i instance = null;
	public static UIOpm_i getInstance() { 
		if ( null == instance ) instance = new UIOpmSCADAgen();
		return instance;
	}
	private UIOpmSCADAgen () {}

	@Override
	public boolean checkAccess(String function, String location, String action, String mode) {
		logger.info("function[{}] location[{}] action[{}] mode[{}]", new Object[] { function, action, action, mode});
		String sessionId = null;
		try {
			sessionId = SessionManager.getRequestCxtSessionId();
		} catch (Exception e) {
			logger.warn("checkAccess  e e[{}]", e.toString());
		}
		logger.info("sessionId[{}]", sessionId);

		boolean result = checkAccess(sessionId, mode, action, function, location);
		
		logger.info("function[{}] location[{}] action[{}] mode[{}] result[{}]", new Object[] { function, action, action, mode, result});
		
		return result;
	}

	private boolean checkAccess(String sessionId, String mode, String action, String function, String location) {
		logger.debug("checkAccess - sessionId={}, mode={}, action={}, function={}, location={}",
				new Object[] { sessionId, mode, action, function, location });

		boolean result = false;
		if ((sessionId != null) && (!sessionId.isEmpty())) {
			OperatorOpmInfo operatorOpmInfo = SessionManager.getOperatorOpmInfo(sessionId);
			result = checkAccess(operatorOpmInfo, mode, action, function, location);
		} else {
			logger.error("sessionId={} is null, or empty !!, checkAccess return 'false'", sessionId);
		}
		return result;
	}

	private boolean checkAccess(OperatorOpmInfo operatorOpmInfo, String mode, String action, String function,
			String location) {
		logger.debug("checkAccess - operatorOpmInfo={}, mode={}, action={}, function={}, location={}",
				new Object[] { operatorOpmInfo, mode, action, function, location });

		boolean result = false;
		if (operatorOpmInfo != null) {
			OpmRequestDto dto = new OpmRequestDto();
			dto.addParameter("mode", mode);
			dto.addParameter("action", action);
			dto.addParameter("function", function);
			dto.addParameter("location", location);

			result = OpmSessionManager.checkOperationIsPermitted(operatorOpmInfo, dto);
		} else {
			logger.error("operatorOpmInfo={} is null !!, checkAccess return 'false'", operatorOpmInfo);
		}
		return result;
	}
}
