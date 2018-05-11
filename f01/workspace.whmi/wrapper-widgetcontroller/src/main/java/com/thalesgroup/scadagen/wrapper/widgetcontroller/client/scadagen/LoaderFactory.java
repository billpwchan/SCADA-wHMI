package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen;

import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;

public class LoaderFactory {

	public static InitProcess_i getInitProcess(String key) {
		
		InitProcess_i loader = null;
		
		if ( PhaseALoader.class.getSimpleName().equals(key) ) {
			loader = PhaseALoader.getInstance().getLoader();
		}
		else if ( PhaseBLoader.class.getSimpleName().equals(key) ) {
			loader = PhaseBLoader.getInstance().getLoader();
		}
		else if ( SingleLoader.class.getSimpleName().equals(key) ) {
			loader = SingleLoader.getInstance().getLoader();
		}
		return loader;
	}
}
