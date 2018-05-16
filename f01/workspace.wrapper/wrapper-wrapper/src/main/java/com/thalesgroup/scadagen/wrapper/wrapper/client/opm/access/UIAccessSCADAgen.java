package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.access;

import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.checker.AuthorizationCheckerC;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.checker.IAuthorizationCheckerC;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OpmRequestDto;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class UIAccessSCADAgen implements UIAccess_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static UIAccess_i instance = null;
	public static UIAccess_i getInstance() { 
		if ( null == instance ) instance = new UIAccessSCADAgen();
		return instance;
	}
	private UIAccessSCADAgen () {}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#checkAccess(java.util.Map)
	 */
	@Override
	public boolean checkAccess(Map<String, String> parameter) {
		String f = "checkAccess";
		logger.begin(f);
		
		boolean ret = false;
		
		if ( null != parameter ) {
			
			if ( logger.isTraceEnabled() ) {
				for ( Map.Entry<String, String> entry : parameter.entrySet() ) {
					String k = entry.getKey();
					String v = entry.getValue();
					logger.trace(f, "k[{}] v[{}]", k, v);
					if ( k == null || k.isEmpty() || v == null || v.isEmpty() ) {
						logger.warn(f, "k[{}] OR v[{}] is INVALID", k, v);
					}
				}
			}

			OpmRequestDto dto = new OpmRequestDto();
			dto.setRequestParameters(parameter);
			OperatorOpmInfo operatorOpmInfo = ConfigProvider.getInstance().getOperatorOpmInfo();
			IAuthorizationCheckerC checker = new AuthorizationCheckerC();
			ret = checker.checkOperationIsPermitted( operatorOpmInfo, dto );

		} else {
			logger.warn(f,  "parameter IS NULL");
		}
		logger.debug(f, "ret[{}]", ret);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#checkAccess(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkAccess(
			  String opmName1, String opmValue1
			, String opmName2, String opmValue2
			, String opmName3, String opmValue3
			, String opmName4, String opmValue4) {
		String f = "checkAccess";
		logger.begin(f);
		
		logger.debug(f, "opmName1[{}] opmValue1[{}]", opmName1, opmValue1);
		logger.debug(f, "opmName2[{}] opmValue2[{}]", opmName2, opmValue2);
		logger.debug(f, "opmName3[{}] opmValue3[{}]", opmName3, opmValue3);
		logger.debug(f, "opmName4[{}] opmValue4[{}]", opmName4, opmValue4);
		
		boolean ret = false;
		
		if ( 
				   opmName1 != null && ! opmName1.isEmpty() 
				&& opmValue1 != null && !opmValue1.isEmpty()
				
				&& opmName2 != null && !opmName2.isEmpty()
				&& opmValue2 != null && !opmValue2.isEmpty()
				
				&& opmName3 != null && !opmName3.isEmpty()
				&& opmValue3 != null && !opmValue3.isEmpty()
				
				&& opmName4 != null && !opmName4.isEmpty()
				&& opmValue4 != null && !opmValue4.isEmpty()
		) {
			OpmRequestDto dto = new OpmRequestDto();
			dto.addParameter( opmName1, opmValue1 );
			dto.addParameter( opmName2, opmValue2 );
			dto.addParameter( opmName3, opmValue3 );
			dto.addParameter( opmName4, opmValue4 );
		
			OperatorOpmInfo operatorOpmInfo = ConfigProvider.getInstance().getOperatorOpmInfo();
			
			// TO: remove the non target role in here
		
			IAuthorizationCheckerC checker = new AuthorizationCheckerC();
			ret = checker.checkOperationIsPermitted( operatorOpmInfo, dto );
		} else {
			logger.warn(f, "args null, or empty - " 
				+ "  "+opmName1+"=" + opmValue1 
				+ ", "+opmName2+"=" + opmValue2 
				+ ", "+opmName3+"=" + opmValue3 
				+ ", "+opmName4+"=" + opmValue4
				+ " - checkAccess return 'false'" );
		}
		logger.debug(f, "ret[{}]", ret);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#checkAccess(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkAccess(String function, String location, String action, String mode) {
		final String f = "checkAccess";
		logger.begin(f);
		logger.debug(f, "function[{}] location[{}] action[{}] mode[{}]  ", new Object[]{function, location, action, mode});

		boolean ret = checkAccess(
								UIOpm_i.FUNCTION, function
								, UIOpm_i.LOCATION, location
								, UIOpm_i.ACTION, action
								, UIOpm_i.MODE, mode
								);
		logger.debug(f, "function[{}] location[{}] action[{}] mode[{}] ret[{}] ", new Object[]{function, location, action, mode, ret});
		logger.end(f);
		return ret;
	}

	@Override
	public void init() {
	}
}
