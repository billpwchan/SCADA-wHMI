package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.sub;

import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPrioritySCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i;

public class UIControlPriorityPTW implements UIControlPriority_i {
	
	UIControlPriority_i cp = UIControlPrioritySCADAgen.getInstance(this.getClass().getSimpleName());
	
	private static UIControlPriority_i instance = null;
	public static UIControlPriority_i getInstance() {
		if(null==instance) instance = new UIControlPriorityPTW();
		return instance;
	}
	private UIControlPriorityPTW() {}

	@Override
	public void requestReservation(String scsEnvId, String dbAddress, UIControlPriorityCallback callBack) {
		cp.requestReservation(scsEnvId, dbAddress, callBack);
	}

	@Override
	public void withdrawReservation(String scsEnvId, String dbAddress, UIControlPriorityCallback callBack) {
		cp.withdrawReservation(scsEnvId, dbAddress, callBack);
	}

	@Override
	public void getCurrentReservationBy(String scsEnvId, String dbAddress, UIControlPriorityCallback callBack) {
		cp.getCurrentReservationBy(scsEnvId, dbAddress, callBack);
	}

	@Override
	public int checkReservationLevel(String identity) {
		return cp.checkReservationLevel(identity);
	}

	@Override
	public void checkReservationAvailability(String scsEnvId, String dbAddress, UIControlPriorityCallback callBack) {
		cp.checkReservationAvailability(scsEnvId, dbAddress, callBack);
	}

	@Override
	public int checkReservationAvailability(String identity) {
		return cp.checkReservationAvailability(identity);
	}

	@Override
	public String getUsrIdentity() {
		return cp.getUsrIdentity();
	}

	@Override
	public String getReservationKey() {
		return cp.getReservationKey();
	}

	@Override
	public String getDisplayIdentity(String valueFromModel) {
		return cp.getDisplayIdentity(valueFromModel);
	}

	@Override
	public void init() {
		cp.init();
	}

}
