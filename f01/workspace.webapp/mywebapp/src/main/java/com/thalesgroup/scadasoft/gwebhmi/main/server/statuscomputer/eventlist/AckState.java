package com.thalesgroup.scadasoft.gwebhmi.main.server.statuscomputer.eventlist;

import java.util.Date;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadasoft.gwebhmi.main.server.statuscomputer.SCSStatusComputerEmb;

public class AckState extends SCSStatusComputerEmb {

    public AckState() {
        m_statusSet.add("state");
        m_name = "ackstate";
    }

    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
        StringAttribute ret = new StringAttribute();
        ret.setValue("yes");
        ret.setValid(true);
        ret.setTimestamp(new Date());
        ret.setAttributeClass(StringAttribute.class.getName());

        AttributeClientAbstract<?> stobj = inputStatusByName.get("state");
        StringAttribute state = (stobj instanceof StringAttribute ? (StringAttribute) stobj : null);
        if (state != null) {
            String val = state.getValue();

            if ("PNA".equals(val) || "NPNA".equals(val)) {
                ret.setValue("no");
            }
        }

        return ret;
    }

}
