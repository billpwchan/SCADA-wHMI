package com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.qf;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.IGRCComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ScsGRCComponentAccess;

public class ScsGRCComponentAccessQF extends ScsGRCComponentAccess {
	
	private final ClientLogger s_logger = ClientLogger.getClientLogger();

	public ScsGRCComponentAccessQF(IGRCComponentClient view) {
		super(view);
		// TODO Auto-generated constructor stub
	}
	
    /**
     * Build JSON object to launch a Grc.
     * 
     * @param key
     *            The id to recognize the response
     * @param scsEnvId
     *            Specifies the environment name that supports the Database
     * @param name
     *            The database point corresponding to the name of the grc
     * @param grcExecMode
     *            Specifies the GRC launching mode, possible values are:
     *            <ul>
     *            <li>Auto: The GRC is excuted sequentially</li>
     *            <li>StopOnFailed: The GRC execution stopped when a step
     *            execution failed</li>
     *            <li>StopOnFirstBRCExecuted: The GRC execution stopped when the
     *            first step is executed (Not necessary the BRC with index=1).
     *            This allows to execute only one BRC of a GRC.</li>
     *            </ul>
     * @param firstStep
     *            The first step to execute
     * @param grcStepsToSkip
     *            the number of steps to skip
     */
    public void launchGrc(String key, String scsEnvId, String name, short grcExecMode, int firstStep, int [] grcStepsToSkip) {
        s_logger.debug("ScsGRCComponentAccess launchGrc: " + key + ", " + scsEnvId + ", " + name + ", " + grcExecMode
                + ", " + firstStep + ", " + grcStepsToSkip);
        JSONObject jsparam = new JSONObject();

        jsparam.put("name", new JSONString(name));
        jsparam.put("mode", new JSONNumber(grcExecMode));
        jsparam.put("first", new JSONNumber(firstStep));

        JSONArray steps = new JSONArray();
        for ( int i = 0 ; i < grcStepsToSkip.length ; ++i ) {
        	steps.set(i, new JSONNumber(grcStepsToSkip[i]));
			jsparam.put("skipped", steps);       	
        }

        JSONObject jsdata = buildJSONRequest("LaunchGrc", jsparam);

        sendJSONRequest(key, scsEnvId, jsdata);
    }

}
