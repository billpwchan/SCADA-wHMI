package com.thalesgroup.scadasoft.stresstest.calculated.widget;

import java.util.Date;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.BooleanAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadasoft.stresstest.calculated.SCSStatusComputer;

public class DoorColorChooser extends SCSStatusComputer {

    public static String C_COLOR_0 = "#00FFFF";
    public static String C_COLOR_1 = "#FF0000";
    public static String C_COLOR_2 = "#00FF00";
    public static String C_COLOR_3 = "#FFFF00";
    public static String C_COLOR_NOT_CONNECTED = "#FF00FF";

    public DoorColorChooser() {
        m_statusSet.add("openstatus");
        m_statusSet.add("online");
        m_name = "com.thalesgroup.scadasoft.stresstest.calculated.widget.DoorColorChooser";
    }

    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
        // return valid
        StringAttribute ret = new StringAttribute();
        ret.setValue(C_COLOR_0);
        ret.setValid(true);
        ret.setTimestamp(new Date());

        // default input value
        int inValue = 0;
        Boolean isConnected = false;

        // get real input value
        AttributeClientAbstract<?> obj = inputStatusByName.get("openstatus");
        if (obj != null && obj instanceof IntAttribute) {
            inValue = ((IntAttribute) obj).getValue();
        }
        AttributeClientAbstract<?> conobj = inputStatusByName.get("online");
        if (conobj != null && conobj instanceof BooleanAttribute) {
            isConnected = ((BooleanAttribute) conobj).getValue();
        }

        // do calculation
        if (!isConnected) {
            ret.setValue(C_COLOR_NOT_CONNECTED);
        } else {
            if (inValue == 1) {
                ret.setValue(C_COLOR_1);
            } else if (inValue == 2) {
                ret.setValue(C_COLOR_2);
            } else if (inValue == 3) {
                ret.setValue(C_COLOR_3);
            }
        }

        return ret;
    }

}
