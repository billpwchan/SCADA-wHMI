package com.thalesgroup.prj_gz_cocc.gwebhmi.main.server.presenter.datagrid;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.formatter.IFormatter;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.notification.NotificationElementType;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.preprocessor.EntityPreProcessor;

public class CoccGDGEntityPreProcessor implements EntityPreProcessor
{
private static final Logger logger_ = LoggerFactory.getLogger(CoccGDGEntityPreProcessor.class);
       
    private Map<String, IFormatter> formatters_ = new HashMap<String, IFormatter>();

    public CoccGDGEntityPreProcessor(Map<String, IFormatter> formatters) {
                              formatters_ = formatters;
                          }
    
    @Override
    public void preProcess( EntityClient entityClient, NotificationElementType type )
    {
    	// line (same value as serviceOwnerID but will be translated when displayed)
    	StringAttribute lineAttribute = new StringAttribute();
    	lineAttribute.setAttributeClass(StringAttribute.class.getName());
    	lineAttribute.setValid(true);
    	lineAttribute.setTimestamp(new Date());
    	String line = "Undefined";
    	AttributeClientAbstract<Object> att = entityClient.getAttribute("serviceOwnerID");
    	if (att != null) {
    		line = (String)att.getValue();    
    	}
    	lineAttribute.setValue(line);
        entityClient.addAttribute("line", translateValue("line", lineAttribute));

        // Subsystem
        StringAttribute subsystemAttribute = new StringAttribute();
        subsystemAttribute.setAttributeClass(StringAttribute.class.getName());
        subsystemAttribute.setValid(true);
        subsystemAttribute.setTimestamp(new Date());
        String subsystem = entityClient.getEntityClass().substring(0, entityClient.getEntityClass().lastIndexOf('.'));
        subsystem = subsystem.substring(subsystem.lastIndexOf('.')+1).toUpperCase();        
        // Special handling for
        // com.thalesgroup.prj_gz_cocc.data.cocc.alarm.COCCAlarmType
        // com.thalesgroup.scadasoft.data.exchange.entity.alarm.ScseventType
        if (subsystem.equalsIgnoreCase( "alarm" ))
            subsystemAttribute.setValue( "MCS" );
        else
            subsystemAttribute.setValue(subsystem);

        entityClient.addAttribute("subsystem", translateValue("subsystem", subsystemAttribute));
    }
    
    private AttributeClientAbstract<?> translateValue(final String attName, final AttributeClientAbstract<?> value) {
        AttributeClientAbstract<?> toReturn = value;
        if (this.formatters_.containsKey(attName))
            try {
                toReturn = formatters_.get(attName).format(value);
            } catch (HypervisorException e) {
                logger_.error("Error while preprocessing the attribute " + attName, e);
            }
        return toReturn;
    }

}
