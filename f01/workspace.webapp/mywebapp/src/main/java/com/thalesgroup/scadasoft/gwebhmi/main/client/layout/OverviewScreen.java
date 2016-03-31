package com.thalesgroup.scadasoft.gwebhmi.main.client.layout;

import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.chart.timeseries.panel.TimeSeriesGraphFactoryPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.NavigationTreePanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.ScsAlarmListPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.ScsOlsListPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.ScsSituationViewPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.util.ConfigurationConstantUtil;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.matrix.ScsMatrixPanel;

/**
 * This class implements the layout of the left screen.
 */
public class OverviewScreen extends ResizeComposite {

    /**
     * 
     * Main panel wrapping this widget and passed to its {@link ResizeComposite}
     * parent
     */
    private final SplitLayoutPanel mainPanel_;
    /**
     * Tab container on the left screen
     */
    private final TabLayoutPanel m_leftTabLayout;

    /**
     * The alarm list shown on the left screen
     */
    private ScsAlarmListPanel m_alarmListPanelForListTab = null;
    /**
     * The event list shown on the left screen
     */
    private ScsOlsListPanel m_eventListPanelForListTab = null;

    private ScsOlsListPanel m_dpcListPanelForListTab = null;
    private ScsOlsListPanel m_operationNotifPanelForListTab = null;
    private ScsOlsListPanel m_connectionListPanelForListTab = null;
    private ScsMatrixPanel m_alarmDiagramPanelForTab = null;
    private TimeSeriesGraphFactoryPanel m_TSSConfPanelForTab = null;

    /**
     * Constructor.
     */
    public OverviewScreen() {
        // create container
        mainPanel_ = new SplitLayoutPanel(AppUtils.SPLIT_DRAGGER_SIZE_PX);
        mainPanel_.addStyleName("showcase-screen-overview");

        // build layout SplitLayout is DockLayout
        // SOUTH: AlarmBanner
        final ScsAlarmListPanel alarmBanner = new ScsAlarmListPanel(AppUtils.EVENT_BUS,
                ConfigurationConstantUtil.ALARM_LIST_BANNER_ID, true, true);
        mainPanel_.addSouth(alarmBanner, 300);
        // WEST: NavigationTreePanel
        final NavigationTreePanel navigationTreePanel = new NavigationTreePanel(
                ConfigurationConstantUtil.WIDGET_CONFIG_ID, AppUtils.EVENT_BUS);
        mainPanel_.addWest(navigationTreePanel, 250);
        // MAIN: TabLayout containing a GIS image
        final ScsSituationViewPanel situationViewPanel = new ScsSituationViewPanel(
                ConfigurationConstantUtil.VELIZY_GEO_VIEW, AppUtils.EVENT_BUS);

        m_leftTabLayout = new TabLayoutPanel();
        m_leftTabLayout.add(situationViewPanel, "velizyGeoTab_caption", false);
        mainPanel_.add(m_leftTabLayout);

        // call GWT init code after building the layout
        initWidget(mainPanel_);
    }

    /**
     * Create one instance of {@link ScsAlarmListPanel} with the id
     * {@linkplain ConfigurationConstantUtil#ALARM_LIST_ID} and add in the left
     * tabLayout
     */
    public void addAlarmListTab() {
        if (m_alarmListPanelForListTab == null || m_alarmListPanelForListTab.isTerminated()) {
            m_alarmListPanelForListTab = new ScsAlarmListPanel(AppUtils.EVENT_BUS,
                    ConfigurationConstantUtil.ALARM_LIST_ID, false, true);
        }

        m_leftTabLayout.add(m_alarmListPanelForListTab, "alarmListTab_caption", true);

    }

    /**
     * Create one instance of {@link ScsAlarmListPanel} with the id
     * {@linkplain ConfigurationConstantUtil#EVENT_LIST_ID} and add in the left
     * tabLayout
     */
    public void addEventListTab() {
        if (m_eventListPanelForListTab == null || m_eventListPanelForListTab.isTerminated()) {
            m_eventListPanelForListTab = new ScsOlsListPanel(AppUtils.EVENT_BUS,
                    ConfigurationConstantUtil.EVENT_LIST_ID, false);
        }

        m_leftTabLayout.add(m_eventListPanelForListTab, "eventListTab_caption", true);

    }

    public void addDpcListTab() {
        if (m_dpcListPanelForListTab == null || m_dpcListPanelForListTab.isTerminated()) {
            m_dpcListPanelForListTab = new ScsOlsListPanel(AppUtils.EVENT_BUS, ConfigurationConstantUtil.DPC_LIST_ID,
                    false);
        }
        m_leftTabLayout.add(m_dpcListPanelForListTab, "dpcListTab_caption", true);
    }

    public void addOperationNotifListTab() {
        if (m_operationNotifPanelForListTab == null || m_operationNotifPanelForListTab.isTerminated()) {
        	m_operationNotifPanelForListTab = new ScsOlsListPanel(AppUtils.EVENT_BUS, ConfigurationConstantUtil.OPE_NOTIF_LIST_ID,
                    false);
        }
        m_leftTabLayout.add(m_operationNotifPanelForListTab, "operationNotifTab_caption", true);
    }
    
    
    
    public void addConnectionListTab() {
        if (m_connectionListPanelForListTab == null || m_connectionListPanelForListTab.isTerminated()) {
            m_connectionListPanelForListTab = new ScsOlsListPanel(AppUtils.EVENT_BUS,
                    ConfigurationConstantUtil.CONSTATE_LIST_ID, false);
        }

        m_leftTabLayout.add(m_connectionListPanelForListTab, "connectionListTab_caption", true);
    }

    public void addAlarmDiagramTab() {
        if (m_alarmDiagramPanelForTab == null || m_alarmDiagramPanelForTab.isTerminated()) {
            m_alarmDiagramPanelForTab = new ScsMatrixPanel(AppUtils.EVENT_BUS,
                    ConfigurationConstantUtil.ALARM_MATRIX_ID, false);
        }

        m_leftTabLayout.add(m_alarmDiagramPanelForTab, "alarmDiagramTab_caption", true);
    }

    public void addTSSConfTab() {
        if (m_TSSConfPanelForTab != null && m_leftTabLayout.contains(m_TSSConfPanelForTab)) {
            return;
        }
        
        m_TSSConfPanelForTab = new TimeSeriesGraphFactoryPanel(AppUtils.EVENT_BUS);
        
        m_leftTabLayout.add(m_TSSConfPanelForTab, "graph_factory", true);
        
    }
}
