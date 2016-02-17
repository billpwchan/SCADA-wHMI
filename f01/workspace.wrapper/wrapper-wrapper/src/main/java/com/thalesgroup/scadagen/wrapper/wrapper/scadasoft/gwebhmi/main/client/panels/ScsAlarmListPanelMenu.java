package com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.UIObject;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EntitySelectionInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EqptSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.menu.GDGContextMenuAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.menu.GDGContextMenuItem;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.event.OpenSituationViewEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.util.AlarmUtils;

public class ScsAlarmListPanelMenu extends GDGContextMenuAbstract implements Command {

    private GDGContextMenuItem openSituationViewContextMenuItem_;
    private GDGContextMenuItem selectEquipmentContextMenuItem_;
    private final static String DEFAULT_IMAGE = "defaultImage";

    private EventBus eventBus_;

    /**
     * This attribute is provided with Hypervisor alarms. The alarm list should
     * be subscribed to this attribute (see xml configuration file).
     */
    private static final String creatorIdAttribute = "serviceOwnerID";

    /**
     * This attribute is provided with Hypervisor alarms. The alarm list should
     * be subscribed to this attribute (see xml configuration file).
     */
    private static final String stateAttribute = "state";

    /**
     * Menu item used to acknowledge alarms
     */
    private GDGContextMenuItem ackMenuItem_;

    /**
     * Show or not the Acknowledge menu
     */
    private boolean withAck_ = false;

    /**
     * Logger
     */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();

    public ScsAlarmListPanelMenu(EventBus eventBus, boolean withAck) {
        eventBus_ = eventBus;
        withAck_ = withAck;

        // rebuild menu because withAck_ was not set when super was called
        this.removeAll();
        this.createMenu();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMenu() {

        openSituationViewContextMenuItem_ = new GDGContextMenuItem(
                Dictionary.getWording("alarmList_menu_open_situation_view"), new OpenImageCommand());
        openSituationViewContextMenuItem_.disable();

        selectEquipmentContextMenuItem_ = new GDGContextMenuItem(
                Dictionary.getWording("alarmList_menu_select_equipment"), new SelectEquipmentCommand());
        selectEquipmentContextMenuItem_.disable();
        addItem(openSituationViewContextMenuItem_);
        addItem(selectEquipmentContextMenuItem_);
        ackMenuItem_ = new GDGContextMenuItem(Dictionary.getWording("alarmList_menu_ack"), (Command) this);
        if (withAck_) {
            ackMenuItem_.disable();
            addItem(ackMenuItem_);
        }
    }

    /**
     * @return true if all attributes in all entityClient
     */
    private boolean checkEntitiesAttribute() {

        boolean result = false;
        try {
            for (EntityClient entity : entities_) {
                // Get and check creatorId attribute
                final AttributeClientAbstract<String> creatorIdAtt = entity.getAttribute(creatorIdAttribute);
                if (creatorIdAtt == null) {
                    throw new Exception("cannot get " + creatorIdAttribute
                            + " (AttributeClientAbstract=null) from alarm entity" + "Check alarm list configuration.");
                }
                final String creatorId = creatorIdAtt.getValue();
                if (creatorId == null || creatorId.isEmpty()) {
                    throw new Exception(creatorIdAttribute + " attribute should not be null or empty. " + "Check that "
                            + creatorIdAttribute + " attribute is filled in alarm notification messages");
                }

                // Get and check stateAtt attribute
                final AttributeClientAbstract<String> stateAtt = entity.getAttribute(stateAttribute);
                if (stateAtt == null) {
                    throw new Exception("cannot get " + stateAttribute
                            + " (AttributeClientAbstract=null) from alarm entity" + "Check alarm list configuration.");
                }
                final String state = stateAtt.getValue();
                if (state == null || state.isEmpty()) {
                    throw new Exception(stateAttribute + " attribute should not be null or empty. " + "Check that "
                            + stateAttribute + " attribute is filled in alarm notification messages");
                }

            }
            result = true;
        } catch (Exception e) {
            s_logger.error("AlarmListPanelMenu: " + e.getMessage());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMenu() {
        removeAll();
        createMenu();
        try {
            if (entities_ == null) {
                throw new Exception("the list of entities should not be null.");
            }
            boolean entitiesOk = checkEntitiesAttribute();
            if (entitiesOk) {
                if (entities_.size() == 1) {
                    selectEquipmentContextMenuItem_.enable();
                    final AttributeClientAbstract<String> areaIdAtt = entities_.iterator().next()
                            .getAttribute(DEFAULT_IMAGE);
                    if (areaIdAtt != null) {
                        openSituationViewContextMenuItem_.enable();
                    }

                } else {
                    openSituationViewContextMenuItem_.disable();
                    selectEquipmentContextMenuItem_.disable();
                }
                if (entities_.size() >= 1) {
                    ackMenuItem_.enable();
                } else {
                    ackMenuItem_.disable();
                }
            }
        } catch (final Exception e) {
            s_logger.error("AlarmListPanelMenu: " + e.getMessage());
        }

    }

    class OpenImageCommand implements Command {

        @Override
        public void execute() {
            EntityClient entity = entities_.iterator().next();
            OpenSituationViewEvent event = new OpenSituationViewEvent(
                    (String) entity.getAttribute(DEFAULT_IMAGE).getValue());
            eventBus_.fireEventFromSource(event, this);
            closeMenu();
        }

    }

    class SelectEquipmentCommand implements Command {

        @Override
        public void execute() {
            final Map<String, EntitySelectionInfo> eqptEntitiesMap = new HashMap<String, EntitySelectionInfo>();
            EntityClient entity = entities_.iterator().next();
            AttributeClientAbstract<String> eqptId = entity.getAttribute("sourceID");
            AttributeClientAbstract<String> equipmentType = entity.getAttribute("equipmentType");
            eqptEntitiesMap.put(eqptId.getValue(),
                    new EntitySelectionInfo(eqptId.getValue(), equipmentType.getValue()));

            EqptSelectionChangeEvent eqptSelectionEvent = new EqptSelectionChangeEvent(eqptEntitiesMap);
            eventBus_.fireEventFromSource(eqptSelectionEvent, this);
            closeMenu();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        AlarmUtils.acknowledge(entities_);
        ackMenuItem_.disable();
        closeMenu();
    }

    /**
     * Display the menu at the bottom of the target argument
     * 
     * @param entities
     *            selected entities
     * @param target
     *            the widget used to placed the menu
     */
    public void showRelativeTo(Set<EntityClient> entities, UIObject target) {
        entities_ = entities;
        updateMenu();
        showRelativeTo(target);
    }
}
