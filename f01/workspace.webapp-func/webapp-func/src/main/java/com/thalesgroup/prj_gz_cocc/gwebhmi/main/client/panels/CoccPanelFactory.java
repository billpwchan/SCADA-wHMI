package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.enums.MatrixConfigType;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout.CoccActionPanel;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout.CoccAlarmActionPanel;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.util.ConfigurationConstantUtil;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.ScsOlsListPanel;

/**
 * Takes care of building panels according a navigation id.
 */
public class CoccPanelFactory  {

    /**
     * Creates a panel with an navigation id.
     *
     * @param navId id of the navigation item
     * @return a panel
     */
    public static Widget createPanelFromNavId(final String navId) {
        Widget panel = null;

        // Concourse
        if (ConfigurationConstantUtil.SpecialNavPlaces.VIEW_CONCOURSE.getValue().equals(navId)) {
            //panel = new SituationViewPanel(navId, null);
            panel = new CoccActionPanel(navId, AppUtils.EVENT_BUS);
        }
        // History panel
        //else if (ConfigurationConstantUtil.SpecialNavPlaces.HISTORY.getValue().equals(navId)) {
        //    panel = new HistoryPanel();
        //}
        else if (ConfigurationConstantUtil.EQUIPMENT_QUERY_PANEL_ID.equals(navId)) {
        	panel = new EquipmentQueryPanel(ConfigurationConstantUtil.EQUIPMENT_QUERY_PANEL_ID, AppUtils.EVENT_BUS);
        }
        else if (ConfigurationConstantUtil.EVENT_LIST_ID.equals(navId)) {
        	panel = new ScsOlsListPanel(AppUtils.EVENT_BUS, ConfigurationConstantUtil.EVENT_LIST_ID, false);
        }
        else if (ConfigurationConstantUtil.ALARM_LIST_BANNER_ID.equals(navId)) {
            panel = new CoccAlarmListPanel(AppUtils.EVENT_BUS, ConfigurationConstantUtil.ALARM_LIST_BANNER_ID, true, true, null, false, null );
        }
        // Action panel for a given image
        else {
            panel = new CoccActionPanel(navId, AppUtils.EVENT_BUS);
        }
        return panel;
    }
    
    public static Widget createMatrixPanel(final String configId, final String axisRowId, final String axisColId, final MatrixConfigType mxConfigType) {
    	Widget panel = null;
    	String gridId_ = configId + "_" + axisRowId + "_" + axisColId;
    	panel = new CoccAlarmActionPanel(configId, axisRowId, axisColId, mxConfigType, gridId_, AppUtils.EVENT_BUS);
    	return panel;
    }

}
