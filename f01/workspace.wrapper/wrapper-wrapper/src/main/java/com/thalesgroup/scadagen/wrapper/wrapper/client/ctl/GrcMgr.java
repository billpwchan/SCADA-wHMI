package com.thalesgroup.scadagen.wrapper.wrapper.client.ctl;

import java.util.HashMap;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.IGRCComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ScsGRCComponentAccess;

/**
 * @author syau
 *
 */
public class GrcMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(GrcMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static HashMap<String, GrcMgr> instances = new HashMap<String, GrcMgr>();
	public static GrcMgr getInstance(String key) {
		if ( ! instances.containsKey(key) ) {	instances.put(key, new GrcMgr()); }
		GrcMgr instance = instances.get(key);
		return instance;
	}
	
	private Subject subject = null;
	public Subject getSubject() { return subject; }
	
	private ScsGRCComponentAccess scsGRCComponentAccess = null;
	private GrcMgr () {
		final String function = "GrcMgr";
		logger.begin(className, function);
		
		scsGRCComponentAccess = new ScsGRCComponentAccess(new IGRCComponentClient() {
			
			@Override
			public void destroy() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Widget asWidget() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void setPresenter(HypervisorPresenterClientAbstract<? extends HypervisorView> presenter) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setSuspendGrcResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setSuspendGrcResult";
		    	
		    	logger.begin(className, function);
		    	
		    	logger.info(className, function, "clientKey[{}]", clientKey);
		    	logger.info(className, function, "errorCode[{}]", errorCode);
		    	logger.info(className, function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        }
		    	
		        logger.end(className, function);
			}
			
			@Override
			public void setStepStatusResult(String clientKey, int stepStatus, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setStepStatusResult";
		    	
		    	logger.begin(className, function);
		    	
		    	logger.info(className, function, "clientKey[{}]", clientKey);
		    	logger.info(className, function, "stepStatus[{}]", stepStatus);
		    	logger.info(className, function, "errorCode[{}]", errorCode);
		    	logger.info(className, function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("stepStatus", new JSONNumber(stepStatus));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        }
		    	
		        logger.end(className, function);
			}
			
			@Override
			public void setStepResult(String clientKey, int step, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setStepResult";
		    	
		    	logger.begin(className, function);
		    	
		    	logger.info(className, function, "clientKey[{}]", clientKey);
		    	logger.info(className, function, "step[{}]", step);
		    	logger.info(className, function, "errorCode[{}]", errorCode);
		    	logger.info(className, function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("step", new JSONNumber(step));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        }
		    	
		        logger.end(className, function);
			}
			
			@Override
			public void setResumeGrcResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setResumeGrcResult";
		    	
		    	logger.begin(className, function);
		    	
		    	logger.info(className, function, "clientKey[{}]", clientKey);
		    	logger.info(className, function, "errorCode[{}]", errorCode);
		    	logger.info(className, function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        }
		    	
		        logger.end(className, function);
			}
			
			@Override
			public void setPrepareGrcResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setPrepareGrcResult";
		    	
		    	logger.begin(className, function);
		    	
		    	logger.info(className, function, "clientKey[{}]", clientKey);
		    	logger.info(className, function, "errorCode[{}]", errorCode);
		    	logger.info(className, function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        }
		    	
		        logger.end(className, function);
			}
			
			@Override
			public void setLaunchGrcResult(String clientKey, String name, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setLaunchGrcResult";
		    	
		    	logger.begin(className, function);
		    	
		    	logger.info(className, function, "clientKey[{}]", clientKey);
		    	logger.info(className, function, "name[{}]", name);
		    	logger.info(className, function, "errorCode[{}]", errorCode);
		    	logger.info(className, function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("name", new JSONString(name));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        }
		    	
		        logger.end(className, function);
			}
			
			@Override
			public void setGrcStatusResult(String clientKey, int grcStatus, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setGetGrcStateResult";
		    	
		    	logger.begin(className, function);
		    	
		    	logger.info(className, function, "clientKey[{}]", clientKey);
		    	logger.info(className, function, "errorCode[{}]", errorCode);
		    	logger.info(className, function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        }
		    	
		        logger.end(className, function);
			}
			
			@Override
			public void setGetGrcStateResult(String clientKey, int state, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setGetGrcStateResult";
		    	
		    	logger.begin(className, function);
		    	
		    	logger.info(className, function, "clientKey[{}]", clientKey);
		    	logger.info(className, function, "state[{}]", state);
		    	logger.info(className, function, "errorCode[{}]", errorCode);
		    	logger.info(className, function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("state", new JSONNumber(state));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        }
		    	
		        logger.end(className, function);
			}
			
			@Override
			public void setGetGrcListResult(String clientKey, String[] grcList, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setGetGrcListResult";
		    	
		    	logger.begin(className, function);
		    	
		    	logger.info(className, function, "clientKey[{}]", clientKey);
		    	logger.info(className, function, "errorCode[{}]", errorCode);
		    	logger.info(className, function, "errorMessage[{}]", errorMessage);
		    	
		        JSONArray grcListJSArray = new JSONArray();
		        for ( int i = 0 ; i < grcList.length ; ++i ) {
		        	grcListJSArray.set(0, new JSONString(grcList[i]));
		        }

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("grcList", grcListJSArray);
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        }
		    	
		        logger.end(className, function);
			}
			
			@Override
			public void setAbortGrcResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setAbortGrcResult";
		    	
		    	logger.begin(className, function);
		    	
		    	logger.info(className, function, "clientKey[{}]", clientKey);
		    	logger.info(className, function, "errorCode[{}]", errorCode);
		    	logger.info(className, function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        }
		    	
		        logger.end(className, function);
			}
			
			@Override
			public void setAbortGrcPreparationResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setAbortGrcPreparationResult";
		    	
		    	logger.begin(className, function);
		    	
		    	logger.info(className, function, "clientKey[{}]", clientKey);
		    	logger.info(className, function, "errorCode[{}]", errorCode);
		    	logger.info(className, function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        }
		    	
		        logger.end(className, function);
				
			}
		});
		
		logger.end(className, function);
	}

    public void getGrcList(String key, String scsEnvId) {
		String function = "getGrcList";
		logger.begin(className, function);
    	logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		scsGRCComponentAccess.getGrcList(key, scsEnvId);
		logger.end(className, function);
    }

    public void getGrcState(String key, String scsEnvId, String grcName) {
		String function = "getGrcState";
		logger.begin(className, function);
    	logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		logger.info(className, function, "grcName[{}]", grcName);
		scsGRCComponentAccess.getGrcState(key, scsEnvId, grcName);
		logger.end(className, function);
    }

    public void prepareGrc(String key, String scsEnvId, String name) {
		String function = "prepareGrc";
		logger.begin(className, function);
    	logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		logger.info(className, function, "name[{}]", name);
		scsGRCComponentAccess.prepareGrc(key, scsEnvId, name);
		logger.end(className, function);
    }

    public void abortGrcPreparation(String key, String scsEnvId, String name) {
		String function = "abortGrcPreparation";
		logger.begin(className, function);
    	logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		logger.info(className, function, "name[{}]", name);
		scsGRCComponentAccess.abortGrcPreparation(key, scsEnvId, name);
		logger.end(className, function);
    }

    public void launchGrc(String key, String scsEnvId, String name, short grcExecMode, int firstStep,
            int grcStepsToSkip) {
		String function = "launchGrc";
		logger.begin(className, function);
    	logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		logger.info(className, function, "name[{}]", name);
		logger.info(className, function, "grcExecMode[{}]", grcExecMode);
		logger.info(className, function, "firstStep[{}]", firstStep);
		logger.info(className, function, "grcStepsToSkip[{}]", grcStepsToSkip);
		scsGRCComponentAccess.launchGrc(key, scsEnvId, name, grcExecMode, firstStep, grcStepsToSkip);
		logger.end(className, function);
    }

    public void abortGrc(String key, String scsEnvId, String name) {
		String function = "abortGrc";
		logger.begin(className, function);
    	logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		logger.info(className, function, "name[{}]", name);
		scsGRCComponentAccess.abortGrc(key, scsEnvId, name);
		logger.end(className, function);
    }
    
    public void suspendGrc(String key, String scsEnvId, String name) {
		String function = "suspendGrc";
		logger.begin(className, function);
    	logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		logger.info(className, function, "name[{}]", name);
		scsGRCComponentAccess.suspendGrc(key, scsEnvId, name);
		logger.end(className, function);
    }

    public void resumeGrc(String key, String scsEnvId, String name) {
		String function = "resumeGrc";
		logger.begin(className, function);
    	logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		logger.info(className, function, "name[{}]", name);
		scsGRCComponentAccess.resumeGrc(key, scsEnvId, name);
		logger.end(className, function);
    }
	
}