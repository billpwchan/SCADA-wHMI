package com.thalesgroup.scadasoft.gwebhmi.main.server.statuscomputer.eventlist;

import java.util.Date;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadasoft.gwebhmi.main.server.statuscomputer.SCSStatusComputerEmb;

public class DisplayMessage extends SCSStatusComputerEmb {

    public DisplayMessage() {
        m_statusSet.add("message");
        m_name = "displayMessage";
    }

    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
        StringAttribute ret = new StringAttribute();
        ret.setAttributeClass(StringAttribute.class.getName());

        AttributeClientAbstract<?> msgobj = inputStatusByName.get("message");
        StringAttribute msg = (msgobj instanceof StringAttribute ? (StringAttribute) msgobj : null);
        if (msg != null) {
            String[] ftab = msg.getValue().split("\\|\\|");
            ret.setValue(ftab[0]);
            ret.setValid(true);
            ret.setTimestamp(new Date());
        }

        return ret;
    }

}
