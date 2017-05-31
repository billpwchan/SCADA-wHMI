package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen;

import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;

public class LoaderFactory {

	public static InitProcess_i getInitProcess(String key) {
		
		InitProcess_i loader = null;
		
		if ( UIWidgetUtil.getClassSimpleName(PhaseALoader.class.getName()).equals(key) ) {
			loader = PhaseALoader.getInstance().getLoader();
		}
		else if ( UIWidgetUtil.getClassSimpleName(PhaseBLoader.class.getName()).equals(key) ) {
			loader = PhaseBLoader.getInstance().getLoader();
		}
		else if ( UIWidgetUtil.getClassSimpleName(SingleLoader.class.getName()).equals(key) ) {
			loader = SingleLoader.getInstance().getLoader();
		}
		return loader;
	}
}
