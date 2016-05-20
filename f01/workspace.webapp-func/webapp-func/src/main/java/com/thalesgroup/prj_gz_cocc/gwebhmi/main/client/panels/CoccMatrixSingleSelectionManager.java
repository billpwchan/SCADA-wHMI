package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.NavigationActivationEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.event.MatrixDoubleClickEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.selection.MatrixSingleSelectionManager;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.util.ConfigurationConstantUtil;

public class CoccMatrixSingleSelectionManager extends
		MatrixSingleSelectionManager {

	/** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();

	@Override
	protected void onMxItemDblClick(final MatrixDoubleClickEvent event) {
		int rowId = event.getSquare().getLocationKey().getRow();
		int columnId = event.getSquare().getLocationKey().getColumn();
		
		s_logger.debug(CoccMatrixSingleSelectionManager.class.getName() + " onMxItemDblClick : columnId = " + columnId + "  rowId = " + rowId);

		// column 0 = BAS, row 4 = Line 5
		if (columnId == 0 && rowId == 4) {
			final NavigationActivationEvent navigationEvent =
				new NavigationActivationEvent(ConfigurationConstantUtil.ALARM_MATRIX_L5_BAS_ID);

			AppUtils.EVENT_BUS.fireEvent(navigationEvent);
		}
	}
}

