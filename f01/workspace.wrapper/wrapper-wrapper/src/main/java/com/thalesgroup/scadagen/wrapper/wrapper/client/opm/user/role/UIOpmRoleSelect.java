package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.user.role;

import java.util.Iterator;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.RoleDto;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIOpmRoleSelect {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	public void update(final String role) {
		final String f = "update";
		logger.begin(f);
		
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
		
		logger.end(f);
	}
	
	private void dump(final OperatorOpmInfo updatedOperatorOpmInfo, final String msg) {
		final String f = "dump";
		logger.begin(f);
		
		final Map<String, RoleDto> roles = updatedOperatorOpmInfo.getOperator().getRoleId();
        String string = "";
        for( String s : roles.keySet() ) {
        	string += s + ", ";
        }
        string = string.substring( 0, string.length()-2 );
		logger.debug(f, "RoleMngtPresenterClient - " + msg +" update 'OperatorOpmInfo' Object, roles='" + string + "'" );
		
		logger.end(f);
	}
}
