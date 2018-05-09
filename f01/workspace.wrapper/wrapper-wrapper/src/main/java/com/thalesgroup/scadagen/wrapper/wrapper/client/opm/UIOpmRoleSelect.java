package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import java.util.Iterator;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.RoleDto;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;

public class UIOpmRoleSelect {
	
	private final String name = this.getClass().getName();
	private final String className = this.getClass().getSimpleName();
	private UILogger logger = UILoggerFactory.getInstance().getLogger(name);
	
	public void update(final String role) {
		final String f = "update";
		logger.begin(className, f);
		
		if(logger.isDebugEnabled()) dump(ConfigProvider.getInstance().getOperatorOpmInfo(), "before");

        // update the client cache with the new role
		final OperatorOpmInfo operatorOpmInfo = ConfigProvider.getInstance().getOperatorOpmInfo();
		final Map<String, RoleDto> roleToUpdate = operatorOpmInfo.getOperator().getRoleId();
		Iterator<Map.Entry<String, RoleDto>> iter = roleToUpdate.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<String, RoleDto> entry = iter.next();
		    if(!role.equalsIgnoreCase(entry.getKey())){
		        iter.remove();
		    }
		}
		ConfigProvider.getInstance().updateOperatorOpmInfo( operatorOpmInfo );
		
		if(logger.isDebugEnabled()) dump(ConfigProvider.getInstance().getOperatorOpmInfo(), "after");
		
		logger.end(className, f);
	}
	
	private void dump(final OperatorOpmInfo updatedOperatorOpmInfo, final String msg) {
		final String f = "dump";
		logger.begin(className, f);
		
		final Map<String, RoleDto> roles = updatedOperatorOpmInfo.getOperator().getRoleId();
        String string = "";
        for( String s : roles.keySet() ) {
        	string += s + ", ";
        }
        string = string.substring( 0, string.length()-2 );
		logger.debug(className, f, "RoleMngtPresenterClient - " + msg +" update 'OperatorOpmInfo' Object, roles='" + string + "'" );
		
		logger.end(className, f);
	}
}
