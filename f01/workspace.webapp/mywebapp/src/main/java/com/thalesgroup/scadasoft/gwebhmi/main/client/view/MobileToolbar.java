package com.thalesgroup.scadasoft.gwebhmi.main.client.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.ICurrentMobileViewListener;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.MobileOverviewScreen;
import com.thalesgroup.scadasoft.gwebhmi.main.client.util.ConfigurationConstantUtil;

/**
 * Toolbar for the mobile layout. It replaces the navigation menu.
 */
public class MobileToolbar extends Composite implements IMobileToolbarView, ICurrentMobileViewListener {
    /**
     * CSS class applied to toolbar's items
     */
    public static final String CSS_TOOLBAR_ITEM = "showcase-toolbar-item";
    /**
     * 
     */
    public static final String CSS_MENU_HORIZONTAL = "scs-menubar-horizontal";
    /**
     * 
     */
    public static final String CSS_MENU_VERTICAL = "scs-menubar-vertical";
    /**
     * 
     */
    public static final String CSS_TOOLBAR_ITEM_CLICKED = "showcase-toolbar-item-clicked";

    /**
     * Logout button
     */
    private Button logoutBtn_;

    /**
     * Display the event list
     */
    private Button eventListButton_;
    /**
     * List of situation view navigation buttons
     */
    private Map<String, Button> navButtonMap_ = new HashMap<String, Button>();

    /**
     * The mobile screen
     */
    private MobileOverviewScreen overviewScreen_;

    /**
     * Constructor.
     * 
     * @param overviewScreen
     *            the overview screen instance
     */
    public MobileToolbar(MobileOverviewScreen overviewScreen) {
        overviewScreen_ = overviewScreen;
        overviewScreen_.addCurrentViewListener(this);
        FlowPanel mainPanel = new FlowPanel();
        mainPanel.addStyleName("showcase-toolbar");

        addLogoutButton(mainPanel);
        addItemsSeparator(mainPanel);
        addLists(mainPanel);
        initWidget(mainPanel);
    }

    /**
     * Creates and add the logout button to the toolbar.
     * 
     * @param mainPanel
     */
    private void addLogoutButton(FlowPanel mainPanel) {
        logoutBtn_ = new Button(Dictionary.getWording("toolbar_btn_logout"));
        logoutBtn_.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                applyClickedStyle(logoutBtn_);
                Window.Location.replace("j_spring_security_logout");
            }
        });
        logoutBtn_.addStyleName(CSS_TOOLBAR_ITEM);

        mainPanel.add(logoutBtn_);
    }

    /**
     * Creates and add a separator to the toolbar.
     * 
     * @param mainPanel
     */
    private void addItemsSeparator(FlowPanel mainPanel) {
        final Label sep = new Label();
        sep.addStyleName("showcase-toolbar-sep");
        mainPanel.add(sep);
    }

    /**
     * Add all buttons to display the lists (e.g. : alarm or event list)
     * 
     * @param mainPanel
     */
    private void addLists(FlowPanel mainPanel) {

        eventListButton_ = new Button(Dictionary.getWording("toolBarItemEvenList"));
        eventListButton_.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                overviewScreen_.displayEventList();
            }
        });
        eventListButton_.addStyleName(CSS_TOOLBAR_ITEM);

        mainPanel.add(eventListButton_);
        addItemsSeparator(mainPanel);

    }

    /**
     * Add a button for each navigation entry
     * 
     * @param navMap
     *            Map of situation view configured in the navigation
     */
    @Override
    public void setNavMap(Map<String, String> navMap, FlowPanel mainPanel) {

        List<String> l = new ArrayList<String>(navMap.keySet());
        Collections.sort(l, String.CASE_INSENSITIVE_ORDER);

        for (String k : l) {
            addNavButton(Dictionary.getWording(navMap.get(k)), k, mainPanel);
        }
        currentViewChanged(overviewScreen_.getCurrentViewId());
    }

    @Override
    public FlowPanel getMainPanel() {
        return (FlowPanel) getWidget();
    }

    /**
     * Create and add a navigation button in the tool bar
     * 
     * @param label
     *            Label to display in the button
     * @param navId
     *            the id of the situation view
     * @param mainPanel
     */
    private void addNavButton(String label, final String navId, FlowPanel mainPanel) {
        final Button navButton = new Button(label);
        navButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                overviewScreen_.displaySituationView(navId);
            }
        });
        navButton.addStyleName(CSS_TOOLBAR_ITEM);
        navButtonMap_.put(navId, navButton);
        mainPanel.add(navButton);
        addItemsSeparator(mainPanel);
    }

    /**
     * Add or remove CSS styles according to the button state
     * 
     * @param button
     *            the button to styled
     */
    private void applyClickedStyle(Button button) {
        logoutBtn_.removeStyleName(CSS_TOOLBAR_ITEM_CLICKED);

        eventListButton_.removeStyleName(CSS_TOOLBAR_ITEM_CLICKED);
        for (Button navButton : navButtonMap_.values()) {
            navButton.removeStyleName(CSS_TOOLBAR_ITEM_CLICKED);
        }
        button.addStyleName(CSS_TOOLBAR_ITEM_CLICKED);
    }

    /**
     * Needed by the interface but nothing to do
     * 
     * @param presenter
     *            the presenter connected by this view
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void setPresenter(HypervisorPresenterClientAbstract presenter) {
        // nothing to do
    }

    @Override
    public void currentViewChanged(String viewId) {
        if (ConfigurationConstantUtil.EVENT_LIST_ID.equals(viewId)) {
            applyClickedStyle(eventListButton_);
        } else {
            Button navButton = navButtonMap_.get(viewId);
            if (navButton != null) {
                applyClickedStyle(navButton);
            }
        }
    }

    @Override
    public void destroy() {
        logoutBtn_ = null;
        eventListButton_ = null;
        navButtonMap_.clear();
        overviewScreen_ = null;
    }

}
