package com.thalesgroup.scadasoft.gwebhmi.main.client.layout;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.AlarmListPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.InfoCommandMobilePanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.ScsAlarmListPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.ScsOlsListPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.ScsSituationViewPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.util.ConfigurationConstantUtil;

/**
 * Implement the mobile layout
 */
public class MobileOverviewScreen extends ResizeComposite {

    /**
     * 
     * Main panel wrapping this widget and passed to its {@link ResizeComposite}
     * parent
     */
    private final SplitLayoutPanel mainPanel_;

    /**
     * A widget displaying equipment info panel and command panel
     */
    private InfoCommandMobilePanel infoCommandTabPanel_;
    private SimpleLayoutPanel centerPanel;
    private List<ICurrentMobileViewListener> viewMobileListeners_ = new ArrayList<ICurrentMobileViewListener>();
    private String currentViewId = "unknown";

    public MobileOverviewScreen() {
        mainPanel_ = new SplitLayoutPanel(AppUtils.SPLIT_DRAGGER_SIZE_PX);
        mainPanel_.addStyleName("showcase-screen-overview");

        addAlarmListBanner();
        addInfoCommandPanel();

        addCenterPanel();
        addSituationViewPanel();
        initWidget(mainPanel_);
    }

    private void addCenterPanel() {
        centerPanel = new SimpleLayoutPanel();
        mainPanel_.add(centerPanel);
    }

    private void addSituationViewPanel() {
        displaySituationView(ConfigurationConstantUtil.CONCOURSE);
    }

    /**
     * Creates and add {@link AlarmListPanel} panel.
     */
    private void addAlarmListBanner() {
        mainPanel_.addSouth(new ScsAlarmListPanel(AppUtils.EVENT_BUS,
                ConfigurationConstantUtil.MOBILE_ALARM_LIST_BANNER_ID, true, true), 300);
    }

    /**
     * Create and add the info command panel.
     */
    private void addInfoCommandPanel() {

        infoCommandTabPanel_ = new InfoCommandMobilePanel(AppUtils.EVENT_BUS);
        mainPanel_.addEast(infoCommandTabPanel_, 300);
    }

    public void displaySituationView(String navId) {
        final ScsSituationViewPanel situationViewPanel = new ScsSituationViewPanel(navId, AppUtils.EVENT_BUS);

        displayMainPanel(situationViewPanel);
        displayViewChanged(navId);
    }

    public void displayAlarmList() {
        ScsAlarmListPanel alarmList = new ScsAlarmListPanel(AppUtils.EVENT_BUS, ConfigurationConstantUtil.ALARM_LIST_ID,
                false, true);
        displayMainPanel(alarmList);
        displayViewChanged(ConfigurationConstantUtil.ALARM_LIST_ID);
    }

    public void displayEventList() {
        ScsOlsListPanel eventListPanel = new ScsOlsListPanel(AppUtils.EVENT_BUS,
                ConfigurationConstantUtil.EVENT_LIST_ID, false);

        displayMainPanel(eventListPanel);
        displayViewChanged(ConfigurationConstantUtil.EVENT_LIST_ID);
    }

    /**
     * Must be call to add a widget at the center of the screen
     * 
     * @param widget
     *            to be displayed
     */
    private void displayMainPanel(Widget widget) {
        // delete previous widget
        Widget w = centerPanel.getWidget();
        if (w != null && w instanceof IClientLifeCycle) {
            ((IClientLifeCycle) widget).terminate();
        }

        centerPanel.setWidget(widget);
    }

    private void displayViewChanged(String viewId) {
        currentViewId = viewId;
        for (ICurrentMobileViewListener listener : viewMobileListeners_) {
            listener.currentViewChanged(viewId);
        }
    }

    public void addCurrentViewListener(ICurrentMobileViewListener listener) {
        viewMobileListeners_.add(listener);
    }

    public void removeCurrentViewListener(ICurrentMobileViewListener listener) {
        viewMobileListeners_.remove(listener);
    }

    public String getCurrentViewId() {
        return currentViewId;
    }

}
