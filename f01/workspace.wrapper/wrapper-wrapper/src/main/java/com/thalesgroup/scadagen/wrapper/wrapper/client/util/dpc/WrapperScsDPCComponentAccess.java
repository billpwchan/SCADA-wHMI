package com.thalesgroup.scadagen.wrapper.wrapper.client.util.dpc;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dpc.IDPCComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dpc.ScsDPCComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.update.ScsComponentUpdate;

public class WrapperScsDPCComponentAccess extends ScsDPCComponentAccess {

	public WrapperScsDPCComponentAccess(IDPCComponentClient view) {
        super(view);
        
        m_jsdata = new JSONObject();
        m_jsdata.put(ScsComponentUpdate.c_JSON_COMPONENT_ARG, new JSONString("DpcComponent"));

	}

}
