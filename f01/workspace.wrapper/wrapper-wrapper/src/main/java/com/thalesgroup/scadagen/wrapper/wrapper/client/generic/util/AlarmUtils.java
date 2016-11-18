package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.util;

import java.util.Collection;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.admin.client.rpc.AdministrationProxy;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.action.AckAlarmOperatorAction;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;

public class AlarmUtils {
    /**
     * Logger
     */
    private static final ClientLogger s_logger = ClientLogger.getClientLogger();
    /**
     * This attribute is provided with Hypervisor alarms. The alarm list should
     * be subscribed to this attribute (see xml configuration file).
     */
    private static final String creatorIdAttribute = "serviceOwnerID";

    public static void acknowledge(Collection<EntityClient> entities) {
        for (EntityClient entity : entities) {
            // Create ack alarm command with data retrieved from the last
            // updateMenu call
            AckAlarmOperatorAction ackAlarmAction = new AckAlarmOperatorAction();
            ackAlarmAction.setEntityId(entity.getId());
            ackAlarmAction.setComment("");
            AttributeClientAbstract<Object> creatorId = entity.getAttribute(creatorIdAttribute);
            if (creatorId != null) {
                ackAlarmAction.setServiceOwnerID((String) creatorId.getValue());
            } else {
                ackAlarmAction.setServiceOwnerID("unknown creator id");
                s_logger.error(AlarmUtils.class.getName() + "  acknowledge error unknown creator id");
            }
            ackAlarmAction.setEntityTypeId(entity.getEntityClass());

            // Send alarm acknowledgment
            AdministrationProxy.getInstance().sendApplicationOperatorAction(ackAlarmAction);
        }
    }

}
