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


public class WidgetGlobalState extends SCSStatusComputer {

    public static final String C_NORMAL_ACKED = "0";
    public static final String C_ALARM_ACKED = "1";
    public static final String C_ALARM_NOT_ACKED = "2";
    public static final String C_INVALID = "3";
    public static final String C_NORMAL_NOT_ACKED = "4";
    public static final String C_NOT_CONNECTED = "5";
    public static final String C_DEFAULT = C_NORMAL_ACKED;
    
    static final Logger logger = LoggerFactory.getLogger("WidgetGlobalState");
    
    public WidgetGlobalState() {

        m_statusSet.add("validity"); // .validSynthesis
        m_statusSet.add("alarm");    // .alarmSynthesis(0)
        m_statusSet.add("needack");  // .alarmSynthesis(2)
        m_statusSet.add("online");

        m_name = "com.thalesgroup.scadasoft.stresstest.calculated.widget.WidgetGlobalState";
    }

    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo,
                                                String entityId,
                                                Map<String, AttributeClientAbstract<?>> inputStatusByName,
                                                Map<String, Object> inputPropertiesByName) 
                                                
   {
    	logger.debug("BEGIN calculator");
        // return valid
        StringAttribute ret = new StringAttribute();
        ret.setValue(C_DEFAULT);
        ret.setValid(true);
        ret.setTimestamp(new Date());
        ret.setAttributeClass(StringAttribute.class.getName());
        
        // default input value
        int inAlarm = 0;
        int needAck = 0;
        int valid = 1;
        Boolean isConnected = false;
        
        // get real input value
        AttributeClientAbstract<?> validobj = inputStatusByName.get("validity");
        if (validobj != null && validobj instanceof IntAttribute) {
            valid = ((IntAttribute)validobj).getValue();
            logger.debug("IN validity = {}", valid);
        } else {
        	logger.warn("Status 'validity' missing from input statuses");
        }
        
        AttributeClientAbstract<?> alarmobj = inputStatusByName.get("alarm");
        if (alarmobj != null && alarmobj instanceof IntAttribute) {
            inAlarm = ((IntAttribute)alarmobj).getValue();
            logger.debug("IN alarm = {}", inAlarm);
        } else {
        	logger.warn("Status 'alarm' missing from input statuses");
        } 
        
        AttributeClientAbstract<?> needAckObj = inputStatusByName.get("needack");
        if (needAckObj != null && needAckObj instanceof IntAttribute) {
            needAck = ((IntAttribute)needAckObj).getValue();
            logger.debug("IN needack = {}", needAck);
        } else {
        	logger.warn("Status 'needack' missing from input statuses");
        } 
        
        AttributeClientAbstract<?> conobj = inputStatusByName.get("online");
        if (conobj != null && conobj instanceof BooleanAttribute) {
            BooleanAttribute ba = ((BooleanAttribute)conobj);
            isConnected = ba.getValue();
            logger.debug("IN online = {}", isConnected);
            if (!ba.isValid()) {
                isConnected = false;
            }
            logger.debug("IN online is valid = {}", ba.isValid());
        } else {
        	logger.warn("Status 'online' missing from input statuses");
        } 
        
        // do calculation
        String computedValue = C_DEFAULT;
        if (!isConnected) {
            computedValue = C_NOT_CONNECTED;
            logger.debug("C_NOT_CONNECTED");
        } else {
            if (inAlarm == 1) {
                if (needAck == 1) {
                    computedValue = C_ALARM_NOT_ACKED;
                    logger.debug("C_ALARM_NOT_ACKED");
                } else {
                    computedValue = C_ALARM_ACKED;
                    logger.debug("C_ALARM_ACKED");
                }

            } else {
                if (valid == 0) {
                    if (needAck == 1) {
                        computedValue = C_NORMAL_NOT_ACKED;
                        logger.debug("C_NORMAL_NOT_ACKED");
                    } else {
                        computedValue = C_NORMAL_ACKED;
                        logger.debug("C_NORMAL_ACKED");
                    }
                } else {
                    computedValue = C_INVALID;
                    logger.debug("C_INVALID");
                }
            }
        }

        ret.setValue(computedValue);
        logger.debug("END calculator: {}", ret.getValue());
        return ret;
    }

}
