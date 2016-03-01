package com.thalesgroup.scadasoft.stresstest.calculated.widget;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.BooleanAttribute;
import com.thalesgroup.scadasoft.stresstest.calculated.SCSStatusComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;

public class C1166B_ECS_TESA_Color extends SCSStatusComputer {
	
	private static Logger logger = Logger.getLogger(C1166B_ECS_TESA_Color.class);
	
	private static final String validity 	= "validity"; // .validSynthesis
	private static final String alarm 		= "alarm"; // .alarmSynthesis(0)
	private static final String needack 	= "needack"; // .alarmSynthesis(2)
	private static final String online 		= "online";

    public C1166B_ECS_TESA_Color() {
        m_statusSet.add(validity);
        m_statusSet.add(alarm);
        m_statusSet.add(needack);
        m_statusSet.add(online);
        m_name = "com.thalesgroup.scadasoft.stresstest.calculated.widget.C1166B_ECS_TESA_Color";
//        m_name = C1166B_ECS_TESA_Color.class.getName();
    }

    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
    	
    	logger.error("compute Begin");
    	
		// return valid
        IntAttribute ret = new IntAttribute();

		
        // default input value
        int inValueAlarm = 0;
        int inValueValidity = 0;// 1= valid, 3 = invalid
        int inValueNeedAck = 0;
        Boolean isConnected = false;
		
        AttributeClientAbstract<?> obj1 = inputStatusByName.get(alarm);
        if (obj1 != null && obj1 instanceof IntAttribute) {
        	inValueAlarm = ((IntAttribute) obj1).getValue();
        }
        
        logger.error("compute inValueAlarm = ["+inValueAlarm+"]");
        
        AttributeClientAbstract<?> obj2 = inputStatusByName.get(validity);
        if (obj2 != null && obj2 instanceof IntAttribute) {
        	inValueValidity = ((IntAttribute) obj2).getValue();
        }
        
        logger.error("compute inValueValidity = ["+inValueValidity+"]");
        
        AttributeClientAbstract<?> obj3 = inputStatusByName.get(needack);
        if (obj3 != null && obj3 instanceof IntAttribute) {
        	inValueNeedAck = ((IntAttribute) obj3).getValue();
        }
        
        logger.error("compute inValueNeedAck = ["+inValueNeedAck+"]");
        
        AttributeClientAbstract<?> conobj = inputStatusByName.get(online);
        if (conobj != null && conobj instanceof BooleanAttribute) {
            isConnected = ((BooleanAttribute) conobj).getValue();
        }
        
        logger.error("compute isConnected = ["+isConnected+"]");
        
        int value = 0;
		
        // do calculation
        if (!isConnected) {
        	
        	logger.error("compute DISCONNECTED");
        	
            value = 0;
        } else {
        	
			if ( 0 == inValueValidity ) {
				
				logger.error("compute VALID");
				
				if ( 1 == inValueAlarm ) {
					
					logger.error("compute ALARM");
					
					value = 2;
				} else {
					
					logger.error("compute NORMAL");
					
					value = 1;
				}
				if ( 1 == inValueNeedAck ) {
					
					logger.error("compute UNACK");
					
					value += 10;
				} else {
					
					logger.error("compute ACK");
					
				}
			} else {
				
				logger.error("compute INVALID");
				
				value = 0;
			}
        }
        
        ret.setValue(value);
        ret.setValid(true);
        ret.setTimestamp(new Date());
        
        logger.error("compute color["+value+"]");
        
        logger.error("compute ret.getValue()["+ret.getValue()+"]");
        
        
        logger.error("compute End");
		
		return ret;
	}

}
