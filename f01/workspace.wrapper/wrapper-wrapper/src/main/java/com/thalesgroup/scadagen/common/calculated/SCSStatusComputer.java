package com.thalesgroup.scadagen.common.calculated;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer;
import com.thalesgroup.scadagen.wrapper.wrapper.server.Translation;

public abstract class SCSStatusComputer implements StatusComputer {
    protected Set<String> m_statusSet = new HashSet<String>();
    protected Set<String> m_propertySet = new HashSet<String>();
    protected String m_name = "";
    
    protected String translatePatten = "&[0-9a-zA-Z/$_-]+";

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
    
	
    protected String getDBMessage(String regex, String input) {
//		logger.debug("{} regex[{}] input[{}]", new Object[]{"getDBMessage", regex, input});
		String ret = input;
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(input);
			while(m.find()) {
				String key = m.group();
//				logger.debug("{} m.group()[{}]", "getDBMessage", key);
				String translation = Translation.getWording(key);
				
//				logger.debug("{} key[{}] translation[{}]", new Object[]{"getDBMessage", key, translation});
				if ( null != translation ) {
					ret = ret.replaceAll(key, translation);
				}
			}
		} catch ( PatternSyntaxException e ) {
//			logger.error("{} PatternSyntaxException[{}]", "getDBMessage", e.toString());
		}

		return ret;
	}
}
