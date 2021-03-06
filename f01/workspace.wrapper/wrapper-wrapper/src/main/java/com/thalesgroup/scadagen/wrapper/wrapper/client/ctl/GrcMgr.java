package com.thalesgroup.scadagen.wrapper.wrapper.client.ctl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.qf.ScsGRCComponentAccessQF;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.IGRCComponentClient;

/**
 * @author syau
 *
 */
public class GrcMgr {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static Map<String, GrcMgr> instances = new HashMap<String, GrcMgr>();
	public static GrcMgr getInstance(String key) {
		if ( ! instances.containsKey(key) ) {
			GrcMgr grcMgr = new GrcMgr();
			instances.put(key, grcMgr);
		}
		GrcMgr instance = instances.get(key);
		return instance;
	}

	public static Set<Entry<String, GrcMgr>> getGrcMgrInstances() {
		return instances.entrySet();
	}
	
	private Map<String, Subject> subjectMap = new HashMap<String, Subject>();
	
	public void setSubject(String key, Subject subject) {
		final String function = "setSubject";
		subjectMap.put(key, subject);
		logger.debug(function, "SubjectMap subject count=[{}]", subjectMap.size());
	}
	
	public Subject getSubject(String key) { return subjectMap.get(key); }
	
	private ScsGRCComponentAccessQF scsGRCComponentAccess = null;
	private GrcMgr () {
		final String function = "GrcMgr";
		logger.begin(function);

		scsGRCComponentAccess = new ScsGRCComponentAccessQF(new IGRCComponentClient() {
			
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
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	logger.debug(function, "GrcMgr subjectMap subject count=[{}]", subjectMap.size());
		    	Subject subject = subjectMap.get(clientKey + function);
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        } else {
		        	logger.warn(function, "subject for key [{}] not found", clientKey + function);
		        }
		    	
		        logger.end(function);
			}
			
			@Override
			public void setStepStatusResult(String clientKey, int stepStatus, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
		    	final String function = "setStepStatusResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "stepStatus[{}]", stepStatus);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("stepStatus", new JSONNumber(stepStatus));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	logger.debug(function, "GrcMgr subjectMap subject count=[{}]", subjectMap.size());
		    	Subject subject = subjectMap.get(clientKey + function);
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        } else {
		        	logger.warn(function, "subject for key [{}] not found", clientKey + function);
		        }
		    	
		        logger.end(function);
			}
			
			@Override
			public void setStepResult(String clientKey, int step, int errorCode, String errorMessage) {
		    	final String function = "setStepResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "step[{}]", step);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("step", new JSONNumber(step));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	logger.debug(function, "GrcMgr subjectMap subject count=[{}]", subjectMap.size());
		    	Subject subject = subjectMap.get(clientKey + function);
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        } else {
		        	logger.warn(function, "subject for key [{}] not found", clientKey + function);
		        }
		    	
		        logger.end(function);
			}
			
			@Override
			public void setResumeGrcResult(String clientKey, int errorCode, String errorMessage) {
		    	final String function = "setResumeGrcResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	logger.debug(function, "GrcMgr subjectMap subject count=[{}]", subjectMap.size());
		    	Subject subject = subjectMap.get(clientKey + function);
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        } else {
		        	logger.warn(function, "subject for key [{}] not found", clientKey + function);
		        }
		    	
		        logger.end(function);
			}
			
			@Override
			public void setPrepareGrcResult(String clientKey, int errorCode, String errorMessage) {
		    	final String function = "setPrepareGrcResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	logger.debug(function, "GrcMgr subjectMap subject count=[{}]", subjectMap.size());
		    	Subject subject = subjectMap.get(clientKey + function);
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        } else {
		        	logger.warn(function, "subject for key [{}] not found", clientKey + function);
		        }
		    	
		        logger.end(function);
			}
			
			@Override
			public void setLaunchGrcResult(String clientKey, String name, int errorCode, String errorMessage) {
		    	final String function = "setLaunchGrcResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "name[{}]", name);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("name", new JSONString(name));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	logger.debug(function, "GrcMgr subjectMap subject count=[{}]", subjectMap.size());
		    	Subject subject = subjectMap.get(clientKey + function);
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        } else {
		        	logger.warn(function, "subject for key [{}] not found", clientKey + function);
		        }
		    	
		        logger.end(function);
			}
			
			@Override
			public void setGrcStatusResult(String clientKey, int grcStatus, int errorCode, String errorMessage) {
		    	final String function = "setGrcStatusResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("grcStatus", new JSONNumber(grcStatus));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	logger.debug(function, "GrcMgr subjectMap subject count=[{}]", subjectMap.size());
		    	Subject subject = subjectMap.get(clientKey + function);
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        } else {
		        	logger.warn(function, "subject for key [{}] not found", clientKey + function);
		        }
		    	
		        logger.end(function);
			}
			
			@Override
			public void setGetGrcStateResult(String clientKey, int state, int errorCode, String errorMessage) {
		    	final String function = "setGetGrcStateResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "state[{}]", state);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("state", new JSONNumber(state));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	logger.debug(function, "GrcMgr subjectMap subject count=[{}]", subjectMap.size());
		    	Subject subject = subjectMap.get(clientKey + function);
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        } else {
		        	logger.warn(function, "subject for key [{}] not found", clientKey + function);
		        }
		    	
		        logger.end(function);
			}
			
			@Override
			public void setGetGrcListResult(String clientKey, String[] grcList, int errorCode, String errorMessage) {
		    	final String function = "setGetGrcListResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);
		    	
		        JSONArray grcListJSArray = new JSONArray();
		        for ( int i = 0 ; i < grcList.length ; ++i ) {
		        	grcListJSArray.set(i, new JSONString(grcList[i]));
		        	logger.debug(function, "grcList [{}] = [{}]", i, grcList[i]);
		        }

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("grcList", grcListJSArray);
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	logger.debug(function, "GrcMgr subjectMap subject count=[{}]", subjectMap.size());
		    	Subject subject = subjectMap.get(clientKey + function);
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        } else {
		        	logger.warn(function, "subject for key [{}] not found", clientKey + function);
		        }
		    	
		        logger.end(function);
			}
			
			@Override
			public void setAbortGrcResult(String clientKey, int errorCode, String errorMessage) {
		    	final String function = "setAbortGrcResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	logger.debug(function, "GrcMgr subjectMap subject count=[{}]", subjectMap.size());
		    	Subject subject = subjectMap.get(clientKey + function);
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        } else {
		        	logger.warn(function, "subject for key [{}] not found", clientKey + function);
		        }
		    	
		        logger.end(function);
			}
			
			@Override
			public void setAbortGrcPreparationResult(String clientKey, int errorCode, String errorMessage) {
		    	final String function = "setAbortGrcPreparationResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);

		    	JSONObject jsdata = null;

		    	jsdata = new JSONObject();
		    	jsdata.put("function", new JSONString(function));
		    	jsdata.put("clientKey", new JSONString(clientKey));
		    	jsdata.put("errorCode", new JSONNumber(errorCode));
		    	jsdata.put("errorMessage", new JSONString(errorMessage));
		    	
		    	logger.debug(function, "GrcMgr subjectMap subject count=[{}]", subjectMap.size());
		    	Subject subject = subjectMap.get(clientKey + function);
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsdata);
		        } else {
		        	logger.warn(function, "subject for key [{}] not found", clientKey + function);
		        }
		    	
		        logger.end(function);
				
			}
		});
		
		logger.end(function);
	}

    public void getGrcList(String key, String scsEnvId) {
		String function = "getGrcList";
		logger.begin(function);
    	logger.info(function, "key[{}]", key);
		logger.info(function, "scsEnvId[{}]", scsEnvId);
		
		scsGRCComponentAccess.getGrcList(key, scsEnvId);

		logger.end(function);
    }

    public void getGrcState(String key, String scsEnvId, String grcName) {
		String function = "getGrcState";
		logger.begin(function);
    	logger.info(function, "key[{}]", key);
		logger.info(function, "scsEnvId[{}]", scsEnvId);
		logger.info(function, "grcName[{}]", grcName);
		
		scsGRCComponentAccess.getGrcState(key, scsEnvId, grcName);

		logger.end(function);
    }

    public void prepareGrc(String key, String scsEnvId, String name) {
		String function = "prepareGrc";
		logger.begin(function);
    	logger.info(function, "key[{}]", key);
		logger.info(function, "scsEnvId[{}]", scsEnvId);
		logger.info(function, "name[{}]", name);
		
		scsGRCComponentAccess.prepareGrc(key, scsEnvId, name);

		logger.end(function);
    }

    public void abortGrcPreparation(String key, String scsEnvId, String name) {
		String function = "abortGrcPreparation";
		logger.begin(function);
    	logger.info(function, "key[{}]", key);
		logger.info(function, "scsEnvId[{}]", scsEnvId);
		logger.info(function, "name[{}]", name);

		scsGRCComponentAccess.abortGrcPreparation(key, scsEnvId, name);

		logger.end(function);
    }

    public void launchGrc(String key, String scsEnvId, String name, short grcExecMode, int firstStep, int grcStepsToSkip) {
		String function = "launchGrc";
		logger.begin(function);
    	logger.info(function, "key[{}]", key);
		logger.info(function, "scsEnvId[{}]", scsEnvId);
		logger.info(function, "name[{}]", name);
		logger.info(function, "grcExecMode[{}]", grcExecMode);
		logger.info(function, "firstStep[{}]", firstStep);
		logger.info(function, "grcStepsToSkip[{}]", grcStepsToSkip);

		scsGRCComponentAccess.launchGrc(key, scsEnvId, name, grcExecMode, firstStep, grcStepsToSkip);

		logger.end(function);
    }
    
    public void launchGrc(String key, String scsEnvId, String name, short grcExecMode, int firstStep, int [] grcStepsToSkips) {
		String function = "launchGrc";
		logger.begin(function);
    	logger.info(function, "key[{}]", key);
		logger.info(function, "scsEnvId[{}]", scsEnvId);
		logger.info(function, "name[{}]", name);
		logger.info(function, "grcExecMode[{}]", grcExecMode);
		logger.info(function, "firstStep[{}]", firstStep);
		logger.info(function, "grcStepsToSkips.length[{}]", grcStepsToSkips.length);
		for ( int i = 0 ; i < grcStepsToSkips.length ; ++i ) {
			logger.info(function, "grcStepsToSkips({})[{}]", i, grcStepsToSkips[i]);
		}
		scsGRCComponentAccess.launchGrc(key, scsEnvId, name, grcExecMode, firstStep, grcStepsToSkips);

		logger.end(function);
    }
    
    public void abortGrc(String key, String scsEnvId, String name) {
		String function = "abortGrc";
		logger.begin(function);
    	logger.info(function, "key[{}]", key);
		logger.info(function, "scsEnvId[{}]", scsEnvId);
		logger.info(function, "name[{}]", name);
		
		scsGRCComponentAccess.abortGrc(key, scsEnvId, name);

		logger.end(function);
    }
    
    public void suspendGrc(String key, String scsEnvId, String name) {
		String function = "suspendGrc";
		logger.begin(function);
    	logger.info(function, "key[{}]", key);
		logger.info(function, "scsEnvId[{}]", scsEnvId);
		logger.info(function, "name[{}]", name);
		
		scsGRCComponentAccess.suspendGrc(key, scsEnvId, name);

		logger.end(function);
    }

    public void resumeGrc(String key, String scsEnvId, String name) {
		String function = "resumeGrc";
		logger.begin(function);
    	logger.info(function, "key[{}]", key);
		logger.info(function, "scsEnvId[{}]", scsEnvId);
		logger.info(function, "name[{}]", name);
		
		scsGRCComponentAccess.resumeGrc(key, scsEnvId, name);

		logger.end(function);
    }
	
}
