package com.thalesgroup.scadagen.wrapper.wrapper.server.opm;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.server.rpc.session.OpmSessionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.server.rpc.session.SessionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OpmRequestDto;
import com.thalesgroup.scadagen.wrapper.wrapper.server.net.Network;

public class UIOpmSCADAgen implements UIOpm_i {

	private final static Logger logger = LoggerFactory.getLogger(UIOpmSCADAgen.class.getName());
	
	private static UIOpm_i instance = null;
	public static UIOpm_i getInstance() { 
		if ( null == instance ) instance = new UIOpmSCADAgen();
		return instance;
	}
	private UIOpmSCADAgen () {}
	
	private Network network = new Network();

	@Override
	public boolean checkAccess(String function, String location, String action, String mode) {
		logger.debug("checkAccess - function=[{}], location=[{}], action=[{}], mode=[{}]",
				new Object[] {function, location, action, mode});
		boolean result = false;
		result = checkAccess(
				  UIOpm_i.FUNCTION, function
				, UIOpm_i.LOCATION, location
				, UIOpm_i.ACTION, action
				, UIOpm_i.MODE, mode);

		return result;
	}
	
	@Override
	public boolean checkAccess(OperatorOpmInfo operatorOpmInfo, String function, String location, String action, String mode) {
		logger.debug("checkAccess - function=[{}], location=[{}], action=[{}], mode=[{}]",
				new Object[] {function, location, action, mode});
		boolean result = false;
		result = checkAccess(
				  operatorOpmInfo
				, UIOpm_i.FUNCTION, function
				, UIOpm_i.LOCATION, location
				, UIOpm_i.ACTION, action
				, UIOpm_i.MODE, mode);

		return result;
	}

	@Override
	public boolean checkAccess(
			  String opmName1, String opmValue1
			, String opmName2, String opmValue2
			, String opmName3, String opmValue3
			, String opmName4, String opmValue4
			) {
		logger.debug("checkAccess - [{}]=[{}], [{}]=[{}], [{}]=[{}], [{}]=[{}]",
				new Object[] { 
						  opmName1, opmValue1
						, opmName2, opmValue2
						, opmName3, opmValue3
						, opmName4, opmValue4
						});

		boolean result = false;
		String sessionId = null;
		try {
			sessionId = SessionManager.getRequestCxtSessionId();
		} catch (Exception e) {
			logger.warn("checkAccess Exception e[{}]", e.toString());
		}
		if ((sessionId != null) && (!sessionId.isEmpty())) {
			OperatorOpmInfo operatorOpmInfo = SessionManager.getOperatorOpmInfo(sessionId);
			result = checkAccess(
					operatorOpmInfo
					, opmName1, opmValue1
					, opmName2, opmValue2
					, opmName3, opmValue3
					, opmName4, opmValue4
					);
		}
		return result;
	}

	@Override
	public boolean checkAccess(
			OperatorOpmInfo operatorOpmInfo
			, String opmName1, String opmValue1
			, String opmName2, String opmValue2
			, String opmName3, String opmValue3
			, String opmName4, String opmValue4
			) {
		
		logger.debug("checkAccess - operatorOpmInfo[{}]", operatorOpmInfo);
		
		logger.debug("checkAccess - [{}]=[{}], [{}]=[{}], [{}]=[{}], [{}]=[{}]",
				new Object[] { 
						  opmName1, opmValue1
						, opmName2, opmValue2
						, opmName3, opmValue3
						, opmName4, opmValue4
						});

		boolean result = false;
		if (operatorOpmInfo != null) {
			OpmRequestDto dto = new OpmRequestDto();
			dto.addParameter(opmName1, opmValue1);
			dto.addParameter(opmName2, opmValue2);
			dto.addParameter(opmName3, opmValue3);
			dto.addParameter(opmName4, opmValue4);

			result = OpmSessionManager.checkOperationIsPermitted(operatorOpmInfo, dto);
		} else {
			logger.error("operatorOpmInfo=[{}] is null !!, checkAccess return 'false'", operatorOpmInfo);
		}
		return result;
	}
	
	@Override
	public String getRemoteHostName(HttpServletRequest httpServletRequest) {
		String ret = network.getRemoteHostName(httpServletRequest);
		logger.debug("getRemoteHostName ret[{}]", ret);
		return ret;
	}
	@Override
	public String getRemoteIPAddress(HttpServletRequest httpServletRequest) {
		String ret = network.getRemoteIPAddress(httpServletRequest);
		logger.debug("getRemoteIPAddress ret[{}]", ret);
		return ret;
	}
	@Override
	public String getHostName() {
		String ret = network.getHostName();
		logger.debug("getHostName ret[{}]", ret);
		return ret;
	}
	@Override
	public String getIPAddress() {
		String ret = network.getIPAddress();
		logger.debug("getIPAddress ret[{}]", ret);
		return ret;
	}

}
