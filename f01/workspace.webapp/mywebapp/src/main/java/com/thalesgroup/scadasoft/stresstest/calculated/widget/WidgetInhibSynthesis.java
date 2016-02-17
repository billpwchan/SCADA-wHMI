package com.thalesgroup.scadasoft.stresstest.calculated.widget;

import java.util.Date;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadasoft.stresstest.calculated.SCSStatusComputer;

public class WidgetInhibSynthesis extends SCSStatusComputer {

    public WidgetInhibSynthesis() {
        m_statusSet.add("inhibsynthesis");

        m_name = "com.thalesgroup.scadasoft.stresstest.calculated.widget.WidgetInhibSynthesis";
    }

    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
        StringAttribute ret = new StringAttribute();
        ret.setAttributeClass(StringAttribute.class.getName());

        AttributeClientAbstract<?> inhibobj = inputStatusByName.get("inhibsynthesis");

        IntAttribute scsinhib = (inhibobj instanceof IntAttribute ? (IntAttribute) inhibobj : null);

        if (scsinhib != null) {
            int inhib = scsinhib.getValue();

            if (inhib != 0) {
                ret.setValue("1");
            } else {
                ret.setValue("0");
            }

            ret.setValid(true);
            ret.setTimestamp(new Date());
        }

        return ret;
    }

}
