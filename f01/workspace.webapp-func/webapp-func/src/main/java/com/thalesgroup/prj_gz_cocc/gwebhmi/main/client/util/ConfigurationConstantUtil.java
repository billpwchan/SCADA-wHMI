package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.util;

/**
 * Utility class for the configuration informations and constants
 *
 */
public class ConfigurationConstantUtil {

    
    /*
     * HMI Conf
     */

    public static final String WIDGET_CONFIG_ID = "scadasoft_nav_tree";
    public static final String CRITICAL_ALARM_LIST_BANNER_ID = "criticalAlarmListBanner";
    public static final String ALARM_LIST_BANNER_ID = "alarmListBanner";
    public static final String MOBILE_ALARM_LIST_BANNER_ID = "alarmListBannerMobile";
    public static final String CONCOURSE = "concourse";
    public static final String VELIZY_GEO_VIEW = "geo_velizy_view";
    public static final String ALARM_LIST_ID = "scsalarmList";
    public static final String EVENT_LIST_ID = "scseventList";
    public static final String DPC_LIST_ID = "scsdpcList";
    public static final String CONSTATE_LIST_ID = "connectionState";
    public static final String ALARM_MATRIX_ID = "alarmDiagram";
    
    public static final String ALARM_MATRIX_L5_ID = "alarmDiagramL5";
    public static final String ALARM_LIST_BANNER_L5_ID = "alarmListBannerL5";
    public static final String ALARM_MATRIX_L5_BAS_ID = "alarmDiagramL5Bas";
    public static final String ALARM_LIST_BANNER_L5_BAS_ID = "alarmListBannerL5Bas";
    
    public static final String EQUIPMENT_QUERY_PANEL_ID = "equipmentQueryPanel";
    public static final String COCC_POW_110KV = "COCC_POW_110KV";
    
    /*
     * Equipment type
     */
    
    public static final String QNAME_SEC = "com.thalesgroup.hypervisor.data.stresstest.config.equipment.DoorStatusesType";
    public static final String QNAME_CAM = "com.thalesgroup.hypervisor.data.stresstest.config.equipment.CamStatusesType";
    
    
    /**
     * HMI places requiring a specific processing.
     * <p>
     * For example:
     * <ul>
     * <li>the "Ile de France" geo view is displayed in a simple
     * {@link com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.SituationViewPanel
     * SituationViewPanel}, not in an
     * {@link com.thalesgroup.scadasoft.gwebhmi.main.client.layout.ActionPanel ActionPanel}</li>
     * <li>opening the history component requires creating a dedicated panel, not an
     * {@link com.thalesgroup.scadasoft.gwebhmi.main.client.layout.ActionPanel ActionPanel}</li>
     * </ul>
     * </p>
     */
    public enum SpecialNavPlaces {
        /** "Ile de France" geo view */
        //VIEW_GEO_IDF("geo_idf_view"),
        /** "Ile de France" geo view */
        //VIEW_GEO_VELIZY("geo_velizy_view"),
        /** History panel */
        HISTORY("history"),
        /**
         * Concourse view
         */
        VIEW_CONCOURSE("concourse");
        /**
         * Store
         */
        private final String value_;

        /**
         * Builds a places with a string.
         *
         * @param value the string value
         */
        private SpecialNavPlaces(final String value) {
            value_ = value;
        }

        /**
         * Returns the place value
         *
         * @return the value
         */
        public String getValue() {
            return value_;
        }
    }
}
