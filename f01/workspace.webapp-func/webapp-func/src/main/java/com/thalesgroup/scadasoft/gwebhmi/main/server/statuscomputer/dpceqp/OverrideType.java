package com.thalesgroup.scadasoft.gwebhmi.main.server.statuscomputer.dpceqp;

import java.util.Date;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadasoft.gwebhmi.main.server.statuscomputer.SCSStatusComputerEmb;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.util.DpcEqpUtil;

public class OverrideType extends SCSStatusComputerEmb {

    public OverrideType() {
        m_statusSet.add("inhibitstatus");
        m_statusSet.add("taggingstatus");
        m_statusSet.add("variabletype");

        m_name = "overrideType";
    }

    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
        StringAttribute ret = new StringAttribute();
        ret.setAttributeClass(StringAttribute.class.getName());

        AttributeClientAbstract<?> ovtypeobj = inputStatusByName.get("inhibitstatus");
        IntAttribute ovtype = (ovtypeobj instanceof IntAttribute ? (IntAttribute) ovtypeobj : null);

        AttributeClientAbstract<?> tagtypeobj = inputStatusByName.get("taggingstatus");
        IntAttribute tagtype = (tagtypeobj instanceof IntAttribute ? (IntAttribute) tagtypeobj : null);

        if (ovtype != null && tagtype != null) {
            int varStatus = ovtype.getValue();
            String msg = "";
            if (DpcEqpUtil.isVarStatusAlaInhEqp(varStatus) || DpcEqpUtil.isEqpStatusAlarmInh(varStatus)) {
                msg = "Alarm Inhibit";
            }

            if (DpcEqpUtil.isVarStatusInvOperator(varStatus) || DpcEqpUtil.isVarStatusInvEqp(varStatus)) {
                if (msg.length() > 0) {
                    msg += " + Scan Inhibit";
                } else {
                    msg = "Scan Inhibit";
                }
            }

            if (DpcEqpUtil.isVarStatusForced(varStatus)) {
                if (msg.length() > 0) {
                    msg += " + Manual Override";
                } else {
                    msg = "Manual Override";
                }
            }

            if (DpcEqpUtil.isVarStatusInvChattering(varStatus)) {
                if (msg.length() > 0) {
                    msg += " + Chatter Inhibit";
                } else {
                    msg = "Chatter Inhibit";
                }
            }

            if (msg.length() == 0) {
                msg = Integer.toString(varStatus);
            }

            ret.setValue(msg);
            ret.setValid(true);
            ret.setTimestamp(new Date());
        }

        return ret;
    }

}
