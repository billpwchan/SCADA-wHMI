package com.thalesgroup.scadasoft.stresstest.calculated.widget;

import java.util.Date;
import java.util.Map;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.scadasoft.stresstest.calculated.SCSStatusComputer;

public class WidgetInhibSynthesis extends SCSStatusComputer {
    
    private static final Logger s_logger = LoggerFactory.getLogger(WidgetInhibSynthesis.class.getName());
    
    public WidgetInhibSynthesis() {
        m_statusSet.add("inhibsynthesis");

        m_name = "com.thalesgroup.scadasoft.stresstest.calculated.widget.WidgetInhibSynthesis";
    }

    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo,
                                                String entityId,
                                                Map<String, AttributeClientAbstract<?>> inputStatusByName,
                                                Map<String, Object> inputPropertiesByName)

    {
        s_logger.debug("WidgetGlobalState::compute status: [" + inputStatusByName.toString() + "] properties: [" + inputPropertiesByName.toString() + "]");
        
        IntAttribute ret = new IntAttribute();
        ret.setAttributeClass(IntAttribute.class.getName());
        
        AttributeClientAbstract<?> inhibobj = inputStatusByName.get("inhibsynthesis");
        
        IntAttribute scsinhib = (inhibobj instanceof IntAttribute ? (IntAttribute)inhibobj : null);
                
        if (scsinhib != null) {
            int inhib = scsinhib.getValue();
            
            if (inhib != 0) {
                ret.setValue(1);
            } else {
                ret.setValue(0);
            }
            
            ret.setValid(true);
            ret.setTimestamp(new Date());
        }

        s_logger.debug("WidgetGlobalState::compute results: " + ret.toString());
        return ret;
    }

    
}
