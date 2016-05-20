package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout;

import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Command;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.EntityContextMenuAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.EntityContextMenuItem;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.scadasoft.gwebhmi.main.client.event.OpenSituationViewEvent;

public class InterchangeStationContextMenu extends EntityContextMenuAbstract {
    
    private EventBus eventBus_;
	
    /**
     * Logger
     */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();

    public InterchangeStationContextMenu(EventBus eventBus) {
        eventBus_ = eventBus;
        
        // rebuild menu because withAck_ was not set when super was called
        this.removeAll();
        this.createMenu();
    }
    
	@Override
	public void createMenu() {
		if (listSelectionInfoByEntityId_ != null) {
			Set<String> entitySet = listSelectionInfoByEntityId_.keySet();
			
			// If multiple entities are in the Set, only the first entry in the Set is used
			if (entitySet.size() > 0) {
				String entityId = entitySet.iterator().next();
				s_logger.debug("InterchangeStationContextMenu entityId=" + entityId);
				if (entityId != null) {
					// Get interchange station configuration
					Map<String,String> properties = ConfigProvider.getInstance().getClientData().getProjectConfigurationMap();
					String navIdStr = properties.get("ContextMenu_" + entityId);
					if (navIdStr != null) {
						
						String [] navIdList = navIdStr.split(",");
						
						for (String navId : navIdList) {
		
							EntityContextMenuItem openSituationViewContextMenuItem_ = new EntityContextMenuItem(Dictionary.getWording("tabLayout_" + navId), new OpenImageCommand(navId));
							openSituationViewContextMenuItem_.enable();
				
							addItem(openSituationViewContextMenuItem_);
							
						}
					}
				}
			}
		}
	}
	
	
	@Override
	public void updateMenu() {
		removeAll();
        createMenu();
	}

	class OpenImageCommand implements Command{

		String navId_;
		public OpenImageCommand(String navId)
		{ navId_ = navId; }
		
        @Override
        public void execute() {
            OpenSituationViewEvent event = new OpenSituationViewEvent(navId_);
            eventBus_.fireEventFromSource(event, this);
            closeMenu();
        }
        
    }

}
