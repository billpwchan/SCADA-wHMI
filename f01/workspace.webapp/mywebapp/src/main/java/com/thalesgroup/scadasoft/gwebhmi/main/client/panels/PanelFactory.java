package com.thalesgroup.scadasoft.gwebhmi.main.client.panels;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.chart.timeseries.panel.TimeSeriesGraphFactoryPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.MwtHistorizationPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.ActionPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.scscomponent.test.CTLTestPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.scscomponent.test.DBTestPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.scscomponent.test.GRCTestPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.util.ConfigurationConstantUtil;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.js.ScsJSPanelWrapper;

/**
 * Takes care of building panels according a navigation id.
 */
public class PanelFactory {

    /**
     * Creates a panel with an navigation id.
     *
     * @param navId
     *            id of the navigation item
     * @return a panel
     */
    public static Widget createPanelFromNavId(final String navId) {
        Widget panel = null;

        // Concourse
        if (ConfigurationConstantUtil.SpecialNavPlaces.VIEW_CONCOURSE.getValue().equals(navId)) {
            // panel = new SituationViewPanel(navId, null);
            panel = new ActionPanel(navId, AppUtils.EVENT_BUS);
        }
        // History panel
        else if (ConfigurationConstantUtil.SpecialNavPlaces.HISTORY.getValue().equals(navId)) {
            panel = new MwtHistorizationPanel();
        }
        // DBM example panel
        else if (ConfigurationConstantUtil.SpecialNavPlaces.DBM_SAMPLE.getValue().equals(navId)) {
            panel = new DBTestPanel();
        }
        // CTL example panel
        else if (ConfigurationConstantUtil.SpecialNavPlaces.CTL_SAMPLE.getValue().equals(navId)) {
            panel = new CTLTestPanel();
        }
        // GRC example panel
        else if (ConfigurationConstantUtil.SpecialNavPlaces.GRC_SAMPLE.getValue().equals(navId)) {
            panel = new GRCTestPanel();
        }
        // external resource
        else if (navId.startsWith("_frame_")) {
            String resName = navId.substring(7);
            panel = new ScsJSPanelWrapper(resName);
        }
        else if (ConfigurationConstantUtil.SpecialNavPlaces.GRAPH_FACTORY.getValue().equals(navId)) {
            panel = new TimeSeriesGraphFactoryPanel(AppUtils.EVENT_BUS);
        }
        // Action panel for a given image
        else {
            panel = new ActionPanel(navId, AppUtils.EVENT_BUS);
        }
        return panel;
    }

}
