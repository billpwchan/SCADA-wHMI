package com.thalesgroup.scadasoft.stresstest.calculated.widget;

import java.util.Date;
import java.util.Map;

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

    public WidgetGlobalState() {

        m_statusSet.add("validity"); // .validSynthesis
        m_statusSet.add("alarm"); // .alarmSynthesis(0)
        m_statusSet.add("needack"); // .alarmSynthesis(2)
        m_statusSet.add("online");

        m_name = "com.thalesgroup.scadasoft.stresstest.calculated.widget.WidgetGlobalState";
    }

    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
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
            valid = ((IntAttribute) validobj).getValue();
        }

        AttributeClientAbstract<?> alarmobj = inputStatusByName.get("alarm");
        if (alarmobj != null && alarmobj instanceof IntAttribute) {
            inAlarm = ((IntAttribute) alarmobj).getValue();
        }

        AttributeClientAbstract<?> needAckObj = inputStatusByName.get("needack");
        if (needAckObj != null && needAckObj instanceof IntAttribute) {
            needAck = ((IntAttribute) needAckObj).getValue();
        }

        AttributeClientAbstract<?> conobj = inputStatusByName.get("online");
        if (conobj != null && conobj instanceof BooleanAttribute) {
            BooleanAttribute ba = ((BooleanAttribute) conobj);
            isConnected = ba.getValue();

            if (!ba.isValid()) {
                isConnected = false;
            }
        }

        // do calculation
        String computedValue = C_DEFAULT;
        if (!isConnected) {
            computedValue = C_NOT_CONNECTED;
        } else {
            if (inAlarm == 1) {
                if (needAck == 1) {
                    computedValue = C_ALARM_NOT_ACKED;
                } else {
                    computedValue = C_ALARM_ACKED;
                }

            } else {
                if (valid == 0) {
                    if (needAck == 1) {
                        computedValue = C_NORMAL_NOT_ACKED;
                    } else {
                        computedValue = C_NORMAL_ACKED;
                    }
                } else {
                    computedValue = C_INVALID;
                }
            }
        }

        ret.setValue(computedValue);

        return ret;
    }

}
