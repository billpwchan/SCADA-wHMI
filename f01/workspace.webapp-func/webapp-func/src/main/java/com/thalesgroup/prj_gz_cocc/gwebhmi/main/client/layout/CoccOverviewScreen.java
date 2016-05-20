package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.StringEnumFilterDescription;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterSetEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.enums.MatrixConfigType;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels.CoccAlarmListPanel;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels.CoccConnectionMatrixPanel;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels.CoccMatrixPanel;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels.CoccNavigationTreePanel;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels.CoccSituationViewPanel;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.util.ConfigurationConstantUtil;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.ScsAlarmListPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.ScsOlsListPanel;

/**
 * This class implements the layout of the left screen.
 */
public class CoccOverviewScreen extends ResizeComposite {

    /**
     * 
     * Main panel wrapping this widget and passed to its {@link ResizeComposite} parent
     */
    private final SplitLayoutPanel mainPanel_;
    /**
     * Tab container on the left screen
     */
    private final CoccTabLayoutPanel m_leftTabLayout;
    
    /**
     * The alarm list shown on the left screen
     */
    //private ScsAlarmListPanel m_alarmListPanelForListTab = null;
    /**
     * The event list shown on the left screen
     */
    private ScsOlsListPanel m_eventListPanelForListTab = null;
    
    //private ScsOlsListPanel m_dpcListPanelForListTab = null;
    
//    private ScsOlsListPanel m_connectionListPanelForListTab = null;
    private CoccConnectionMatrixPanel m_connectionListPanelForListTab = null;
    
    private CoccMatrixPanel m_alarmDiagramPanelForTab = null;
    
    //private CoccSituationViewPanel m_gisMapPanelForTab = null;
    
    private CoccSituationViewPanel m_power110KVPanelForTab = null;
    
    private CoccSituationViewPanel m_WaterPumpSummaryPanelForTab = null;


    /**
     * Constructor.
     */
    public CoccOverviewScreen() {
        // create container
        mainPanel_ = new SplitLayoutPanel(AppUtils.SPLIT_DRAGGER_SIZE_PX);
        mainPanel_.addStyleName("showcase-screen-overview");
        
        // build layout SplitLayout is DockLayout
        // Set critical alarm filter event      
        final Set<String> values = new HashSet<String>();
        values.add( "CRITICAL" );
        StringEnumFilterDescription filterdesc = new StringEnumFilterDescription(values);
        final FilterSetEvent filterEvent = new FilterSetEvent("scsalarmList_priority_name", filterdesc);
        final Set<FilterSetEvent> filterSet = new HashSet<FilterSetEvent>();
        filterSet.add(filterEvent);

        final CoccAlarmListPanel alarmBanner = new CoccAlarmListPanel(AppUtils.EVENT_BUS,ConfigurationConstantUtil.CRITICAL_ALARM_LIST_BANNER_ID,true,true,filterSet,true,null);
        
        // SOUTH: AlarmBanner
        mainPanel_.addSouth(alarmBanner, 300);
        
        // WEST: NavigationTreePanel
        final CoccNavigationTreePanel navigationTreePanel = new CoccNavigationTreePanel("navTreeOverview", AppUtils.EVENT_BUS);
        mainPanel_.addWest(navigationTreePanel, 250);
        // MAIN: TabLayout containing a GIS/SVG image
        final CoccSituationViewPanel networkViewPanel =
                new CoccSituationViewPanel("NetworkOverview", AppUtils.EVENT_BUS);
        InterchangeStationContextMenu contextMenu = new InterchangeStationContextMenu(AppUtils.EVENT_BUS);
		networkViewPanel.getPresenter().setEquipmentContextualMenu(contextMenu);
            
        m_leftTabLayout = new CoccTabLayoutPanel();
        m_leftTabLayout.add(networkViewPanel, "NetworkOverview", false);
        mainPanel_.add(m_leftTabLayout);
        
        // call GWT init code after building the layout
        initWidget(mainPanel_);
    }

    /**
     * Create one instance of {@link ScsAlarmListPanel} with the id {@linkplain ConfigurationConstantUtil#ALARM_LIST_ID} and add in the left tabLayout
     */
//    public void addAlarmListTab(){
//        if(m_alarmListPanelForListTab == null || m_alarmListPanelForListTab.isTerminated()){
//        	m_alarmListPanelForListTab = new ScsAlarmListPanel(AppUtils.EVENT_BUS,ConfigurationConstantUtil.ALARM_LIST_ID,false,true);
//        }
//    	m_leftTabLayout.add(m_alarmListPanelForListTab, "alarmListTab_caption", true);
//    
//    }
    
    /**
     * Create one instance of {@link ScsAlarmListPanel} with the id {@linkplain ConfigurationConstantUtil#EVENT_LIST_ID} and add in the left tabLayout
     */
    public void addEventListTab(){
        if(m_eventListPanelForListTab == null || m_eventListPanelForListTab.isTerminated()){
            m_eventListPanelForListTab = new ScsOlsListPanel(AppUtils.EVENT_BUS,ConfigurationConstantUtil.EVENT_LIST_ID,false);
        }
        m_leftTabLayout.add(m_eventListPanelForListTab, "eventListTab_caption", true);
    
    }
    
//    public void addDpcListTab(){
//        if(m_dpcListPanelForListTab == null || m_dpcListPanelForListTab.isTerminated()){
//            m_dpcListPanelForListTab = new ScsOlsListPanel(AppUtils.EVENT_BUS,ConfigurationConstantUtil.DPC_LIST_ID,false);
//        }
//        m_leftTabLayout.add(m_dpcListPanelForListTab, "dpcListTab_caption", true);
//    }
    
    public void addConnectionListTab(){
        if(m_connectionListPanelForListTab == null || m_connectionListPanelForListTab.isTerminated()){
        	
        	//String [] configIds = {"connection-matrix-1"};

        	Map<String,String> properties = ConfigProvider.getInstance().getClientData().getProjectConfigurationMap();
        	String [] configIds = {};
        	String countStr = properties.get("ConnectionMatrixIdCount");
        	if (countStr != null) {
        	   int configIdCount = Integer.parseInt( countStr );
        	   if (configIdCount > 0) {
        	       configIds = new String [configIdCount];

        	       int i=0;
        	       while (i < configIdCount) {
        	         configIds[i] = properties.get( "ConnectionMatrixId" + Integer.toString( i+1 ) );
        	         i++;
        	       }
        	   }
        	}

            m_connectionListPanelForListTab = new  CoccConnectionMatrixPanel(configIds, AppUtils.EVENT_BUS, false);
        }
        m_leftTabLayout.add(m_connectionListPanelForListTab, "connectionListTab_caption", true);
    }
    
    public void addAlarmDiagramTab(){
        if (m_alarmDiagramPanelForTab == null || m_alarmDiagramPanelForTab.isTerminated()){
            m_alarmDiagramPanelForTab  = new CoccMatrixPanel(AppUtils.EVENT_BUS, ConfigurationConstantUtil.ALARM_MATRIX_ID, "Line", "Subsystem", MatrixConfigType.DYNAMIC ,false);
        }
        m_leftTabLayout.add(m_alarmDiagramPanelForTab, "alarmDiagramTab_caption", true);
    }
    
//    public void addGisMapTab(){
//        if (m_gisMapPanelForTab == null || m_gisMapPanelForTab.isTerminated()){
//        	m_gisMapPanelForTab  = new CoccSituationViewPanel("gz_map", AppUtils.EVENT_BUS);
//        }
//        m_leftTabLayout.add(m_gisMapPanelForTab, "gisMapTab_caption", true);
//    }
    
/*    public void addPowerTab(){
        if (m_powerPanelForTab == null){
        	m_powerPanelForTab  = new SituationViewPanel("COCC_POW", AppUtils.EVENT_BUS);
        }
        m_leftTabLayout.add(m_powerPanelForTab, "powerTab_caption", true);
    }*/

    public void addPower110KVTab()
    {
        if (m_power110KVPanelForTab == null || m_power110KVPanelForTab.isTerminated()){
            m_power110KVPanelForTab  = new CoccSituationViewPanel("COCC_POW_110KV", AppUtils.EVENT_BUS);
        }
        m_leftTabLayout.add(m_power110KVPanelForTab, "power110KVTab_caption", true);
    }
    
    public void addWaterPumpSummaryTab()
    {
        if (m_WaterPumpSummaryPanelForTab == null || m_WaterPumpSummaryPanelForTab.isTerminated()){
            m_WaterPumpSummaryPanelForTab  = new CoccSituationViewPanel("WaterPumpSummary", AppUtils.EVENT_BUS);
        }
        m_leftTabLayout.add(m_WaterPumpSummaryPanelForTab, "WaterPumpSummaryTab_caption", true);
    }
 
}
