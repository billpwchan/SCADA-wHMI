package com.thalesgroup.scadasoft.stresstest.calculated.widget;

import java.util.Date;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.BooleanAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadasoft.stresstest.calculated.SCSStatusComputer;

public class ColorChooser extends SCSStatusComputer {

    public static String C_COLOR_0 = "#00FF00";
    public static String C_COLOR_1 = "#FF0000";
    public static String C_COLOR_NOT_CONNECTED = "none";
    public static String C_COLOR_NOT_CONNECTED_TO_CON = "#FF00FF";

    public ColorChooser() {
        m_statusSet.add("smoke");
        m_statusSet.add("online");
        m_name = "com.thalesgroup.scadasoft.stresstest.calculated.widget.ColorChooser";
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

        // get real input value
        AttributeClientAbstract<?> obj = inputStatusByName.get("smoke");
        if (obj != null && obj instanceof IntAttribute) {
            inValue = ((IntAttribute) obj).getValue();
        }

        // get online status
        Boolean isConnectedToSRV = false;
        Boolean isConnectedToConnector = false;
        AttributeClientAbstract<?> conobj = inputStatusByName.get("online");
        if (conobj != null && conobj instanceof BooleanAttribute) {
            isConnectedToSRV = ((BooleanAttribute) conobj).getValue();
            if (!conobj.isValid()) {
                isConnectedToConnector = false;
            } else {
                isConnectedToConnector = true;
            }
        }

        // do calculation
        if (!isConnectedToConnector) {
            ret.setValue(C_COLOR_NOT_CONNECTED_TO_CON);
        } else if (!isConnectedToSRV) {
            ret.setValue(C_COLOR_NOT_CONNECTED);
        } else {
            if (inValue == 1) {
                ret.setValue(C_COLOR_1);
            }
        }

        return ret;
    }

}
