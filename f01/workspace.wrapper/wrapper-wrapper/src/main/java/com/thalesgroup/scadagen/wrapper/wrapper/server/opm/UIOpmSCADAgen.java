package com.thalesgroup.scadagen.wrapper.wrapper.server.opm;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

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
	
	private void dumpGetRemoteHeader(HttpServletRequest httpServletRequest) {
		logger.debug("dumpGetRemoteHeader begin");
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		if (headerNames != null) {
			logger.debug("dumpGetRemoteHeader Start to list out headerNames");
			while (headerNames.hasMoreElements()) {
				String element = headerNames.nextElement();
				logger.debug("dumpGetRemoteHeader element[{}]", element);
				if ( null != element ) {
					String elementValues =  httpServletRequest.getHeader(element);
					logger.debug("dumpGetRemoteHeader {}=[{}]", element, elementValues);
				} else {
					logger.debug("dumpGetRemoteHeader element IS NULL");
				}
			}
		} else {
			logger.debug("headerNames IS NULL");
		}
		logger.debug("dumpGetRemoteHeader end");
	}
	@Override
	public String getRemoteHostName(HttpServletRequest httpServletRequest) {
		return httpServletRequest.getRemoteHost();
	}
	private static final String STR_UNKNOWN = "unknown";
	private static final String[] STR_HEADERS_LIST = {
			"X-Forwarded-For",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR",
			"HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP",
			"HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED",
			"HTTP_VIA",
			"REMOTE_ADDR" 
	};
	@Override
	public String getRemoteIPAddress(HttpServletRequest httpServletRequest) {
		logger.debug("getRemoteIPAddress begin");
		String ret = null;
		
		dumpGetRemoteHeader(httpServletRequest);
		
		for (String header : STR_HEADERS_LIST) {
			logger.debug("getRemoteIPAddress header[{}]", header);
			ret = httpServletRequest.getHeader(header);
			logger.debug("getRemoteIPAddress {}=[{}]", header, ret);
			if (ret != null && ret.length() != 0 && !STR_UNKNOWN.equalsIgnoreCase(ret)) {
				break;
			}
		}
		if ( null == ret ) {
			ret = httpServletRequest.getRemoteAddr();
		}
		
		logger.debug("getRemoteIPAddress ret[{}]", ret);
		logger.debug("getRemoteIPAddress end");
		return ret;
	}
	@Override
	public String getHostName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getIPAddress() {
		// TODO Auto-generated method stub
		return null;
	}

}
