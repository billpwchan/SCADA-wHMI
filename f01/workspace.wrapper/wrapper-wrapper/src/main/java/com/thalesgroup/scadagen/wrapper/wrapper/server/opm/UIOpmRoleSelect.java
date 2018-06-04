package com.thalesgroup.scadagen.wrapper.wrapper.server.opm;

import java.util.HashSet;
import java.util.Set;

import com.thalesgroup.hypervisor.mwt.core.util.common.session.ISessionListContainer;
import com.thalesgroup.hypervisor.mwt.core.util.common.session.SessionContainer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.server.rpc.session.SessionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorZoneSelectionInfoC;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.ldap.LdapOperatorC;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.server.builder.IOpmDtoBuilder;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.server.builder.OpmDtoBuilder;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.server.exception.OpmValidationException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.server.loader.OpmConfigJaxb;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.server.tools.SessionContainerCst;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;

public class UIOpmRoleSelect {

	private UILogger_i logger = UILoggerFactory.getInstance().get(this.getClass().getName());
	
	public OperatorOpmInfo update( String role ) {
		logger.debug( "IN RoleMngtPresenterServer - private OperatorOpmInfo update( String role )" );
		
		logger.debug( "RoleMngtPresenterServer - update - Selected role is : {}", role );
		
		OperatorOpmInfo ret = null;
		
		String sessionId = null;
		try {
			sessionId = SessionManager.getRequestCxtSessionId();
		} catch (Exception e) {
			logger.warn("checkAccess Exception e[{}]", e.toString());
		}
		if ((sessionId != null) && (!sessionId.isEmpty())) {
			
			Set<String> roles = SessionManager.getOperatorOpmInfo(sessionId).getLdapOperator().getListRoles();
			String tmp = "";
			for( String r: roles ) {
				tmp += r + ' ';
			}
			logger.debug( "RoleMngtPresenterServer - update - Origin role is : {}", tmp );
			
			ISessionListContainer sessionListContainer =
					ServicesImplFactory.getInstance().getService( ISessionListContainer.class );
			SessionContainer sessionContainer = sessionListContainer.getSessionContainer( sessionId );

			LdapOperatorC ldapOperator = (LdapOperatorC)sessionContainer.getAttribute( SessionContainerCst.LDAP_OPERATOR );
			
			roles = new HashSet<String>();
			roles.add( role );
			ldapOperator.setListRoles( roles );
			
			OpmConfigJaxb opmConfigJaxb =
		            sessionContainer.getAttributeTyped(OpmConfigJaxb.class, SessionContainerCst.OPM_CONFIG_JAXB);
			
			OperatorZoneSelectionInfoC operatorZoneSelectionInfo = SessionManager.getOperatorOpmInfo(sessionId).getOperatorZoneSelectionInfo();
			
			IOpmDtoBuilder builder = null;
			try {
				builder = new OpmDtoBuilder( ldapOperator, opmConfigJaxb, operatorZoneSelectionInfo );
			} catch( OpmValidationException e ) {
			}
			
			OperatorOpmInfo operatorOpmInfo = SessionManager.getOperatorOpmInfo(sessionId);
			if( builder != null ) {
				operatorOpmInfo = builder.getOperatorOpmInfo( sessionId );
				sessionContainer.setAttribute( SessionContainerCst.OPERATOR_IN_SESSION, operatorOpmInfo );
			}
			
			roles = operatorOpmInfo.getOperator().getRoleId().keySet();
			tmp = "";
			for( String r: roles ) {
				tmp += r + ' ';
			}
			logger.debug( "RoleMngtPresenterServer - The new role is : {}", tmp );
			
			ret = SessionManager.getOperatorOpmInfo(sessionId);
			
			logger.debug( "OUT RoleMngtPresenterServer - private OperatorOpmInfo update( String role )" );
		}
		
		return ret;
	}
}
