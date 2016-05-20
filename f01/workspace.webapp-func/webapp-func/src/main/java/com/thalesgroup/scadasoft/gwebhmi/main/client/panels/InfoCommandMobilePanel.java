
package com.thalesgroup.scadasoft.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EntitySelectionInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EqptSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EqptSelectionChangeEvent.EqptSelectionEventHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.commandPanel.ScsCommandPanel;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.infoPanel.ScsEquipmentInfoPanel;

/**
 * A widget handling the creation of an information panel and a command panel
 * according to equipment selection events.
 */
public class InfoCommandMobilePanel extends ResizeComposite implements IClientLifeCycle, EqptSelectionEventHandler {

    /**
     * Panel displaying equipments attributes
     */
    private ScsEquipmentInfoPanel equipmentInfoPanel_;

    /**
     * Panel enabling to send commands
     */
    private Widget commandPanel_;

    /**
     * Keeps tracks of registered handlers (used for unsubscription)
     */
    private final List<HandlerRegistration> listHandlerRegistrations_ = new ArrayList<HandlerRegistration>();

    private SplitLayoutPanel mainPanel_;

    /**
     * Builds an {@link InfoCommandMobilePanel} widget.
     *
     * @param eventBus
     *            a bus used to listen to equipments selection events
     */
    public InfoCommandMobilePanel(final EventBus eventBus) {
        mainPanel_ = new SplitLayoutPanel(AppUtils.SPLIT_DRAGGER_SIZE_PX);
        mainPanel_.addStyleName("showcase-screen-overview");

        final HandlerRegistration busRegistration = eventBus.addHandler(EqptSelectionChangeEvent.TYPE, this);
        listHandlerRegistrations_.add(busRegistration);

        initWidget(mainPanel_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {
        for (final HandlerRegistration handlerRegistration : listHandlerRegistrations_) {
            handlerRegistration.removeHandler();
        }
        listHandlerRegistrations_.clear();

        removeWidgets();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSelectionChange(final EqptSelectionChangeEvent event) {
        removeWidgets();

        final Map<String, EntitySelectionInfo> entityIds = event.getSelectionInfo();
        if (entityIds != null && entityIds.size() > 0) {

            final String equipmentType = getEquipmentType(entityIds);

            // For now, this is mandatory because KeySet class is not
            // serializable
            final Set<String> eqptIds = new HashSet<String>(entityIds.keySet());

            // Command panel
            commandPanel_ = new ScsCommandPanel(eqptIds, equipmentType);
            if (commandPanel_ != null) {
                mainPanel_.addSouth(commandPanel_, 280);
            }

            // Info panel
            equipmentInfoPanel_ = new ScsEquipmentInfoPanel(eqptIds, equipmentType);
            mainPanel_.add(equipmentInfoPanel_);

        }
    }

    /**
     * Returns the qualified name of an equipment type from a map of entities
     * ids / entities type.
     * <p>
     * Note that if the map of equipments contains different types, the returned
     * type is randomly selected.
     * </p>
     *
     * @param entityIds
     *            a map of entities ids / entities type
     * @return the qualified name of the type
     */
    @SuppressWarnings("static-method")
    private String getEquipmentType(final Map<String, EntitySelectionInfo> entityIds) {
        String eqptType = null;

        if (entityIds != null) {
            eqptType = entityIds.values().iterator().next().getEntityClassName();
        }
        return eqptType;
    }

    /**
     * Remove information panel and command panel.
     */
    public void removeWidgets() {
        // Info panel
        if (equipmentInfoPanel_ != null) {
            mainPanel_.remove(equipmentInfoPanel_);
            equipmentInfoPanel_ = null;
        }
        // Command panel
        if (commandPanel_ != null) {
            mainPanel_.remove(commandPanel_);
            commandPanel_ = null;
        }
    }
}
