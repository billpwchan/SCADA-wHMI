
package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EntitySelectionInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EqptSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EqptSelectionChangeEvent.EqptSelectionEventHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.TabLayoutPanel;

/**
 * A widget handling the creation of an information panel and a command panel according to equipment
 * selection events.
 */
public class CoccInfoCommandTabPanel extends ResizeComposite implements IClientLifeCycle, EqptSelectionEventHandler {

    /**
     * Widget containing the info panel and command panel
     */
    private final TabLayoutPanel tabLayoutPanel_;

    /**
     * Panel displaying equipments attributes
     */
//    private ScsEquipmentInfoPanel equipmentInfoPanel_;
    private CoccEquipmentInfoPanel equipmentInfoPanel_;

    /**
     * Panel enabling to send commands
     */
//    private Widget commandPanel_;

    /**
     * Keeps tracks of registered handlers (used for unsubscription)
     */
    private final List<HandlerRegistration> listHandlerRegistrations_ = new ArrayList<HandlerRegistration>();

    private int currentTab_ = 0;
    
    /** Logger */
    //private final ClientLogger s_logger = ClientLogger.getClientLogger();
    
    /**
     * Builds an {@link CoccInfoCommandTabPanel} widget.
     *
     * @param eventBus a bus used to listen to equipments selection events
     */
    public CoccInfoCommandTabPanel(final EventBus eventBus) {
        tabLayoutPanel_ = new TabLayoutPanel();

        final HandlerRegistration busRegistration = eventBus.addHandler(EqptSelectionChangeEvent.TYPE, this);
        listHandlerRegistrations_.add(busRegistration);
        
        initWidget(tabLayoutPanel_);
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

            // For now, this is mandatory because KeySet class is not serializable
            final Set<String> eqptIds = new HashSet<String>();

            // add only equip of same type
            for(Entry<String, EntitySelectionInfo> entry : entityIds.entrySet()) {
                if (equipmentType.equals(entry.getValue().getEntityClassName())) {
                    eqptIds.add(entry.getKey());
                }
            }
            
            // Info panel
//            equipmentInfoPanel_ = new ScsEquipmentInfoPanel(eqptIds, equipmentType);
            equipmentInfoPanel_ = new CoccEquipmentInfoPanel(eqptIds, equipmentType);
            tabLayoutPanel_.add(equipmentInfoPanel_, "Info", false);

            // Command panel
//            commandPanel_ = new ScsCommandPanel(eqptIds, equipmentType);
//            if (commandPanel_ != null) {
//                tabLayoutPanel_.add(commandPanel_, "Commands", false);
//            }

            tabLayoutPanel_.selectTab(currentTab_);
        }
    }

    /**
     * Returns the qualified name of an equipment type from a map of entities ids / entities type.
     * <p>
     * Note that if the map of equipments contains different types, the returned type is randomly
     * selected.
     * </p>
     *
     * @param entityIds a map of entities ids / entities type
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
            tabLayoutPanel_.remove(equipmentInfoPanel_);
            equipmentInfoPanel_ = null;
        }
        // Command panel
//        if (commandPanel_ != null) {
//            tabLayoutPanel_.remove(commandPanel_);
//            commandPanel_ = null;
//        }
    }
}
