package com.thalesgroup.scadagen.calculated.common;

import java.util.HashSet;
import java.util.Set;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer;

public abstract class SCSStatusComputer implements StatusComputer {
    protected Set<String> m_statusSet = new HashSet<String>();
    protected Set<String> m_propertySet = new HashSet<String>();
    protected String m_name = "";
 
    @Override
    public String getComputerId() {
        return m_name;
    }

    @Override
    public Set<String> getInputStatuses() {
        return m_statusSet;
    }

    @Override
    public Set<String> getInputProperties() {
        return m_propertySet;
    }
    
    protected String getFieldKey(String listName, String field) {
    	return "." + listName + field;
    }
    
    protected String getListName(String entityId) {
    	String listName = null;
    	if ( null != entityId ) {
    		String entityIds[] = entityId.split("\\.");
    		if ( null != entityIds && entityIds.length > 0 ) {
    			listName = entityIds[0];
    		}
    	}
    	return listName;
    }

}
