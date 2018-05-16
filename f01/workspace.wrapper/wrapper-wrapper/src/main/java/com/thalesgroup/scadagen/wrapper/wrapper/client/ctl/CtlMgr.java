package com.thalesgroup.scadagen.wrapper.wrapper.client.ctl;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ICTLComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ScsCTLComponentAccess;

/**
 * @author syau
 *
 */
public class CtlMgr {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static Map<String, CtlMgr> instances = new HashMap<String, CtlMgr>();
	public static CtlMgr getInstance(String key) {
		if ( ! instances.containsKey(key) ) {	instances.put(key, new CtlMgr()); }
		CtlMgr instance = instances.get(key);
		return instance;
	}
	
	private Subject subject = null;
	public Subject getSubject() { return subject; }
	
	private ScsCTLComponentAccess scsCTLComponentAccess = null;
	private CtlMgr () {
		final String function = "CtlMgr";
		
		logger.begin(function);
		
		this.subject = new Subject();
		
		this.scsCTLComponentAccess = new ScsCTLComponentAccess(new ICTLComponentClient() {
			
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
		    public void setSendIntCommandResult(String clientKey, String message, int errorCode, String errorMessage) {
		    	
		    	final String function = "setSendIntCommandResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "message[{}]", message);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);
		    	
		    	JSONObject jsObject = new JSONObject();
		    	jsObject.put("function", new JSONString(function));
		    	jsObject.put("clientKey", new JSONString(clientKey));
		    	jsObject.put("message", new JSONString(message));
		    	jsObject.put("errorCode", new JSONNumber(errorCode));
		    	jsObject.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsObject);
		    	}
		        
		        logger.end(function);
		    }

		    @Override
		    public void setSendFloatCommandResult(String clientKey, String message, int errorCode, String errorMessage) {
		    	
		    	final String function = "setSendFloatCommandResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "message[{}]", message);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);
		    	
		    	JSONObject jsObject = new JSONObject();
		    	jsObject.put("function", new JSONString(function));
		    	jsObject.put("clientKey", new JSONString(clientKey));
		    	jsObject.put("message", new JSONString(message));
		    	jsObject.put("errorCode", new JSONNumber(errorCode));
		    	jsObject.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsObject);
		    	}
		        
		        logger.end(function);
		    }

		    @Override
		    public void setSendStringCommandResult(String clientKey, String message, int errorCode, String errorMessage) {
		    	
		    	final String function = "setSendStringCommandResult";
		    	
		    	logger.begin(function);
		    	
		    	logger.info(function, "clientKey[{}]", clientKey);
		    	logger.info(function, "message[{}]", message);
		    	logger.info(function, "errorCode[{}]", errorCode);
		    	logger.info(function, "errorMessage[{}]", errorMessage);
		    	
		    	JSONObject jsObject = new JSONObject();
		    	jsObject.put("function", new JSONString(function));
		    	jsObject.put("clientKey", new JSONString(clientKey));
		    	jsObject.put("message", new JSONString(message));
		    	jsObject.put("errorCode", new JSONNumber(errorCode));
		    	jsObject.put("errorMessage", new JSONString(errorMessage));
		    	
		    	if ( null != subject ) {
		    		subject.setState(jsObject);
		    	}
		        
		        logger.end(function);
		    }
		});
		
		logger.end(function);
	}
	
	public void connect() {
		final String function = "connect";
		
		logger.beginEnd(function);
	}
	public void disconnect() {
		final String function = "disconnect";
		
		
		logger.begin(function);
		try {
			scsCTLComponentAccess.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scsCTLComponentAccess=null;
		logger.end(function);
	}
	
	public void sendControl ( String envName, String [] address, float commandValue, int bypassInitCond, int bypassRetCond, int sendAnyway) {
		final String function = "sendControl";
		
		logger.begin(function);
		
		logger.info(function, "envName[{}]", envName);
		for ( int i = 0 ; i < address.length ; ++i ) {
			logger.info(function, "address({})[{}] BF", i, address[i]);
			
			address[i] = address[i].replace(":", "");
			
			logger.info(function, "address({})[{}] AF", i, address[i]);
		}
		logger.info(function, "commandValue[{}] bypassInitCond[{}] bypassRetCond[{}] sendAnyway[{}] ", new Object[]{commandValue, bypassInitCond, bypassRetCond, sendAnyway});

		String [] analogValueAddress = address;
		
		if ( null != scsCTLComponentAccess ) {
			scsCTLComponentAccess.sendFloatCommand("sendFloatCommand", envName, analogValueAddress, commandValue, bypassInitCond, bypassRetCond, sendAnyway);
			
	        JSONArray addressJSArray = new JSONArray();
	        for ( int i = 0 ; i < address.length ; ++i ) {
	        	addressJSArray.set(0, new JSONString(address[i]));
	        }
			
			JSONObject jsObject = new JSONObject();
		    jsObject.put("function", new JSONString(function));
		    jsObject.put("envName", new JSONString(envName));
		    jsObject.put("address", addressJSArray);
		    jsObject.put("commandValue", new JSONNumber(commandValue));
		    jsObject.put("bypassInitCond", new JSONNumber(bypassInitCond));
		    jsObject.put("bypassRetCond", new JSONNumber(bypassRetCond));
		    jsObject.put("sendAnyway", new JSONNumber(sendAnyway));
		    
	    	if ( null != subject ) {
	    		subject.setState(jsObject);
	    	}
				
		} else {
			logger.warn(function, "m_CTLAccess IS NULL");
		}

		
		logger.end(function);
		
	}
	
	public void sendControl (String envName, String [] address, int commandValue, int bypassInitCond, int bypassRetCond, int sendAnyway) {
		final String function = "sendControl";
		
		logger.begin(function);
		
		logger.info(function, "envName[{}]", envName);
		for ( int i = 0 ; i < address.length ; ++i ) {
			logger.info(function, "address({})[{}] BF", i, address[i]);
			
			address[i] = address[i].replace(":", "");
			
			logger.info(function, "address({})[{}] AF", i, address[i]);
		}
		logger.info(function, "commandValue[{}] bypassInitCond[{}] bypassRetCond[{}] sendAnyway[{}] ", new Object[]{commandValue, bypassInitCond, bypassRetCond, sendAnyway});
		
		String [] digitalValueAddress = address;
		
		if ( null != scsCTLComponentAccess ) {
			scsCTLComponentAccess.sendIntCommand("sendIntCommand", envName, digitalValueAddress, commandValue, bypassInitCond, bypassRetCond, sendAnyway);
			
	        JSONArray addressJSArray = new JSONArray();
	        for ( int i = 0 ; i < address.length ; ++i ) {
	        	addressJSArray.set(0, new JSONString(address[i]));
	        }
			
			JSONObject jsObject = new JSONObject();
		    jsObject.put("function", new JSONString(function));
		    jsObject.put("envName", new JSONString(envName));
		    jsObject.put("address", addressJSArray);
		    jsObject.put("commandValue", new JSONNumber(commandValue));
		    jsObject.put("bypassInitCond", new JSONNumber(bypassInitCond));
		    jsObject.put("bypassRetCond", new JSONNumber(bypassRetCond));
		    jsObject.put("sendAnyway", new JSONNumber(sendAnyway));
		    
	    	if ( null != subject ) {
	    		subject.setState(jsObject);
	    	}
				
		} else {
			logger.warn(function, "m_CTLAccess IS NULL");
		}
		
		logger.end(function);
		
	}
	
	public void sendControl (String envName, String [] address, String commandValue, int bypassInitCond, int bypassRetCond, int sendAnyway) {
		final String function = "sendControl";
		
		logger.begin(function);
		
		logger.info(function, "envName[{}]", envName);
		
		for ( int i = 0 ; i < address.length ; ++i ) {
			logger.info(function, "address({})[{}] BF", i, address[i]);
			
			address[i] = address[i].replace(":", "");
			
			logger.info(function, "address({})[{}] AF", i, address[i]);
		}
		logger.info(function, "commandValue[{}] bypassInitCond[{}] bypassRetCond[{}] sendAnyway[{}] ", new Object[]{commandValue, bypassInitCond, bypassRetCond, sendAnyway});

		String [] structuredValueAddress = address;
		
		logger.info(function, "structuredValueAddress(0)[{}]", structuredValueAddress[0]);
		
		if ( null != scsCTLComponentAccess ) {
			
	        JSONArray addressJSArray = new JSONArray();
	        for ( int i = 0 ; i < address.length ; ++i ) {
	        	addressJSArray.set(0, new JSONString(address[i]));
	        }
		
			JSONObject jsObject = new JSONObject();
		    jsObject.put("function", new JSONString(function));
		    jsObject.put("envName", new JSONString(envName));
		    jsObject.put("addressJSArray", addressJSArray);
		    jsObject.put("commandValue", new JSONString(commandValue));
		    jsObject.put("bypassInitCond", new JSONNumber(bypassInitCond));
		    jsObject.put("bypassRetCond", new JSONNumber(bypassRetCond));
		    jsObject.put("sendAnyway", new JSONNumber(sendAnyway));
		    
	    	if ( null != subject ) {
	    		subject.setState(jsObject);
	    	}
					
		} else {
			logger.warn(function, "m_CTLAccess IS NULL");
		}
		scsCTLComponentAccess.sendStringCommand("sendStringCommand", envName, structuredValueAddress, commandValue, bypassInitCond, bypassRetCond, sendAnyway);

		logger.end(function);
	}
}
