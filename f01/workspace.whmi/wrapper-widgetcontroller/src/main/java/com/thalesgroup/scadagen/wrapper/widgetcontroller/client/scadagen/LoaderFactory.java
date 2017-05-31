package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen;

import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;

public class LoaderFactory {

	public static InitProcess_i getInitProcess(String key) {
		
		InitProcess_i loader = null;
		
		String strPhaseALoaderclassName = UIWidgetUtil.getClassSimpleName(PhaseBLoader.class.getName());
		String strPhaseBLoaderclassName = UIWidgetUtil.getClassSimpleName(PhaseALoader.class.getName());
		String strSingleLoaderclassName = UIWidgetUtil.getClassSimpleName(SingleLoader.class.getName());
		
		if ( strPhaseALoaderclassName.equals(key) ) {
			loader = PhaseBLoader.getInstance().getLoader();
		}
		else if ( strPhaseBLoaderclassName.equals(key) ) {
			loader = PhaseALoader.getInstance().getLoader();
		}
		else if ( strSingleLoaderclassName.equals(key) ) {
			loader = SingleLoader.getInstance().getLoader();
		}
		return loader;
	}
}
