package com.thalesgroup.scadasoft.stresstest.calculated.widget;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.BooleanAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadasoft.stresstest.calculated.SCSStatusComputer;

public class DoorStrokeColorChooser extends SCSStatusComputer {

    public static String C_COLOR_0 = "#00FF00";
    public static String C_COLOR_1 = "#FF0000";
    public static String C_COLOR_NOT_CONNECTED = "#FF00FF";
    
    static final Logger logger = LoggerFactory.getLogger("DoorStrokeColorChooser");
    
    public DoorStrokeColorChooser() {
        m_statusSet.add("locked");
        m_statusSet.add("online");
        m_name = "com.thalesgroup.scadasoft.stresstest.calculated.widget.DoorStrokeColorChooser";
    }


    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo,
                                                String entityId,
                                                Map<String, AttributeClientAbstract<?>> inputStatusByName,
                                                Map<String, Object> inputPropertiesByName) 
                                                
   {
    	logger.debug("Enter calculator");
        // return valid
        StringAttribute ret = new StringAttribute();
        ret.setValue(C_COLOR_NOT_CONNECTED);
        ret.setValid(true);
        ret.setTimestamp(new Date());
        
        // default input value
        int inValue = 0;
        Boolean isConnected = false;
        
        // get real input value
        AttributeClientAbstract<?> obj = inputStatusByName.get("locked");
        if (obj != null && obj instanceof IntAttribute) {
            inValue = ((IntAttribute)obj).getValue();
            logger.debug("IN locked = {}", inValue);
        }
        AttributeClientAbstract<?> conobj = inputStatusByName.get("online");
        if (conobj != null && conobj instanceof BooleanAttribute) {
            isConnected = ((BooleanAttribute)conobj).getValue();
            logger.debug("IN online = {}", isConnected);
        }
        
        // do calculation
        if (!isConnected) {
            ret.setValue(C_COLOR_NOT_CONNECTED);
        } else {
            if (inValue == 0) {
                ret.setValue(C_COLOR_0);
            } else if (inValue == 1) {
                ret.setValue(C_COLOR_1);
            }
        }

        logger.debug("Exit calculator: {}", ret.getValue());
        return ret;
    }

}
