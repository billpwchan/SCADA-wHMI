package com.thalesgroup.scadasoft.gwebhmi.main.server.statuscomputer;

import java.util.Date;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadasoft.gwebhmi.main.server.statuscomputer.SCSStatusComputerEmb;

public class GDGIsValid extends SCSStatusComputerEmb {

    public GDGIsValid() {
        m_statusSet.add("serviceOwnerID");
        m_name = "gdg_validity";
    }

    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
        StringAttribute ret = new StringAttribute();
        ret.setValue("Invalid");
        ret.setValid(true);
        ret.setTimestamp(new Date());
        ret.setAttributeClass(StringAttribute.class.getName());

        AttributeClientAbstract<?> msgobj = inputStatusByName.get("serviceOwnerID");
        StringAttribute msg = (msgobj instanceof StringAttribute ? (StringAttribute) msgobj : null);
        if (msg != null) {
            if (msg.isValid()) {
                ret.setValue("Valid");
            }
        }

        return ret;
    }

}
