package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.UIObject;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.menu.GDGContextMenuAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.menu.GDGContextMenuItem;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.event.ScsOlsListPanelMenuHandler;

public class ScsOlsListPanelMenu extends GDGContextMenuAbstract implements Command {

    /**
     * Logger
     */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[ScsOlsListPanelMenu] ";
	private GDGContextMenuItem gdgMenuItemCallImage = null;

	private String menuGdgAttribute = null;
	private String menuLabel = null;
			
    public ScsOlsListPanelMenu(String menuLabel, String menuGdgAttribute) {
    	this.menuLabel = menuLabel;
    	this.menuGdgAttribute = menuGdgAttribute;
		
		LOGGER.debug(LOG_PREFIX+"this.menuLabel = menuLabel["+this.menuLabel+"] this.menuGdgAttribute["+this.menuGdgAttribute+"]");	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMenu() {

    	gdgMenuItemCallImage = new GDGContextMenuItem(
                Dictionary.getWording(menuLabel), new CallImageCommand());
    	gdgMenuItemCallImage.disable();
    	addItem(gdgMenuItemCallImage);
    }

    private Set<Map<String, String>> getEntitieSet () {
    	String LOG_PREFIX = "getEntitieSet";
    	LOGGER.debug(LOG_PREFIX+"selected filter");
	    Set<Map<String, String>> set = new HashSet<Map<String, String>>();
	    for ( EntityClient ec : entities_ ) {
	    	Map<String, String> details = new HashMap<String, String>();
	    	for ( String attributeName : ec.attributeNames() ) {
	    		LOGGER.debug(LOG_PREFIX+"attributeName["+attributeName+"] value[" + ec.getAttribute(attributeName).getValue().toString() + "]");	
	    		details.put(attributeName, ec.getAttribute(attributeName).getValue().toString());
	    	}
	    	set.add(details);
	    }
	    return set;
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
            
            boolean isValid = false;
            
            Set<Map<String, String>> set = getEntitieSet();
            
            Map<String, String> hashMap = set.iterator().next();
            if ( hashMap.containsKey(menuGdgAttribute) ) {
            	isValid = true;
            }
            
			if ( isValid ) {
				gdgMenuItemCallImage.enable();
			} else {
				gdgMenuItemCallImage.disable();
			}
        } catch (final Exception e) {
            LOGGER.error(LOG_PREFIX + e.getMessage(), e);
        }

    }

    class CallImageCommand implements Command {

        @Override
        public void execute() {

            EntityClient entity = entities_.iterator().next();
            LOGGER.debug(LOG_PREFIX + "CallImageCommand Command entity.toString()["+entity.toString()+"]");

            if ( null != scsOlsListPanelMenuHandler ) {
            	scsOlsListPanelMenuHandler.onSelection(getEntitieSet());
            }
            
            closeMenu();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {

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
    
    private ScsOlsListPanelMenuHandler scsOlsListPanelMenuHandler = null;
    public void setScsOlsListPanelMenuHandler(ScsOlsListPanelMenuHandler scsOlsListPanelMenuHandler) {
    	this.scsOlsListPanelMenuHandler = scsOlsListPanelMenuHandler;
    }
}
