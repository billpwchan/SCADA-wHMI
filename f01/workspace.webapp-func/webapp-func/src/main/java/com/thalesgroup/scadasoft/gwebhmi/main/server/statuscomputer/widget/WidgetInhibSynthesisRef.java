package com.thalesgroup.scadasoft.gwebhmi.main.server.statuscomputer.widget;

import java.util.Date;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.scadasoft.gwebhmi.main.server.statuscomputer.SCSStatusComputerEmb;

public class WidgetInhibSynthesisRef extends SCSStatusComputerEmb {

    public WidgetInhibSynthesisRef() {
        m_statusSet.add("inhibsynthesis");

        m_name = "widgetinhibsynthesis";
    }

    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
        IntAttribute ret = new IntAttribute();
        ret.setAttributeClass(IntAttribute.class.getName());

        AttributeClientAbstract<?> inhibobj = inputStatusByName.get("inhibsynthesis");
        IntAttribute scsinhib = (inhibobj instanceof IntAttribute ? (IntAttribute) inhibobj : null);

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

        return ret;
    }

}
