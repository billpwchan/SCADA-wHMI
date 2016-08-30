package com.thalesgroup.scadagen.wrapper.wrapper.client.ctl;

import java.util.HashMap;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ICTLComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ScsCTLComponentAccess;

/**
 * @author syau
 *
 */
public class CtlMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(CtlMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static HashMap<String, CtlMgr> instances = new HashMap<String, CtlMgr>();
	public static CtlMgr getInstance(String key) {
		if ( ! instances.containsKey(key) ) {	instances.put(key, new CtlMgr()); }
		CtlMgr instance = instances.get(key);
		return instance;
	}
	
	private Subject subject = null;
	public Subject getSubject() { return subject; }
	
	private ScsCTLComponentAccess ctlAccess = null;
	private CtlMgr () {
		final String function = "CtlMgr";
		
		logger.begin(className, function);
		
		this.subject = new Subject();
		
		this.ctlAccess = new ScsCTLComponentAccess(new ICTLComponentClient() {
			
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
		        // TODO Auto-generated method stub

		        if (errorCode != 0) {
		        	if ( null != subject ) {
		        		subject.setState("COMMAND FAILED");
		        	}
		        } else {
		        	if ( null != subject ) {
		        		subject.setState(message);
		        	}
		        }
		    }

		    @Override
		    public void setSendFloatCommandResult(String clientKey, String message, int errorCode, String errorMessage) {
		        // TODO Auto-generated method stub

		        if (errorCode != 0) {
		        	if ( null != subject ) {
		        		subject.setState("COMMAND FAILED");
		        	}
		        } else {
		        	if ( null != subject ) {
		        		subject.setState(message);
		        	}
		        }
		    }

		    @Override
		    public void setSendStringCommandResult(String clientKey, String message, int errorCode, String errorMessage) {
		        // TODO Auto-generated method stub
		        if (errorCode != 0) {
		        	if ( null != subject ) {
		        		subject.setState("COMMAND FAILED");
		        	}
		        } else {
		        	if ( null != subject ) {
		        		subject.setState(message);
		        	}
		        }
		    }
		});
		
		logger.end(className, function);
	}
	
	public void connect() {
		final String function = "connect";
		
		logger.beginEnd(className, function);
	}
	public void disconnect() {
		final String function = "disconnect";
		
		
		logger.begin(className, function);
		try {
			ctlAccess.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ctlAccess=null;
		logger.end(className, function);
	}
	
	public void sendControl ( String envName, String [] address, float commandValue, int bypassInitCond, int bypassRetCond, int sendAnyway) {
		final String function = "sendControl";
		
		logger.begin(className, function);
		
		logger.info(className, function, "envName[{}]", envName);
		for ( int i = 0 ; i < address.length ; ++i ) {
			logger.info(className, function, "address({})[{}] BF", i, address[i]);
			
			address[i] = address[i].replace(":", "");
			
			logger.info(className, function, "address({})[{}] AF", i, address[i]);
		}
		logger.info(className, function, "commandValue[{}] bypassInitCond[{}] bypassRetCond[{}] sendAnyway[{}] ", new Object[]{commandValue, bypassInitCond, bypassRetCond, sendAnyway});
		
//		if ( null != subject ) {
//    		subject.setState("send message to envName["+envName+"] address[0]["+address[0]+"] commandValue["+commandValue+"] bypassInitCond["+bypassInitCond+"] bypassRetCond["+bypassRetCond+"] sendAnyway["+sendAnyway+"]");
//    	}
		
		String [] analogValueAddress = address;
		
		if ( null != ctlAccess ) {
			ctlAccess.sendFloatCommand("sendFloatCommand", envName, analogValueAddress, commandValue, bypassInitCond, bypassRetCond, sendAnyway);
			if ( null != subject ) {
        		subject.setState("COMMAND SENT");
	    	}				
		} else {
			logger.warn(className, function, "m_CTLAccess IS NULL");
		}

		
		logger.end(className, function);
		
	}
	
	public void sendControl (String envName, String [] address, int commandValue, int bypassInitCond, int bypassRetCond, int sendAnyway) {
		final String function = "sendControl";
		
		logger.begin(className, function);
		
		logger.info(className, function, "envName[{}]", envName);
		for ( int i = 0 ; i < address.length ; ++i ) {
			logger.info(className, function, "address({})[{}] BF", i, address[i]);
			
			address[i] = address[i].replace(":", "");
			
			logger.info(className, function, "address({})[{}] AF", i, address[i]);
		}
		logger.info(className, function, "commandValue[{}] bypassInitCond[{}] bypassRetCond[{}] sendAnyway[{}] ", new Object[]{commandValue, bypassInitCond, bypassRetCond, sendAnyway});
		
//		if ( null != subject ) {
//    		subject.setState("send message to envName["+envName+"] address[0]["+address[0]+"] commandValue["+commandValue+"] bypassInitCond["+bypassInitCond+"] bypassRetCond["+bypassRetCond+"] sendAnyway["+sendAnyway+"]");
//    	}
		
		String [] digitalValueAddress = address;
		
		if ( null != ctlAccess ) {
			ctlAccess.sendIntCommand("sendIntCommand", envName, digitalValueAddress, commandValue, bypassInitCond, bypassRetCond, sendAnyway);
			if ( null != subject ) {
        		subject.setState("COMMAND SENT");
	    	}				
		} else {
			logger.warn(className, function, "m_CTLAccess IS NULL");
		}
		
		logger.end(className, function);
		
	}
	
	public void sendControl (String envName, String [] address, String commandValue, int bypassInitCond, int bypassRetCond, int sendAnyway) {
		final String function = "sendControl";
		
		logger.begin(className, function);
		
		logger.info(className, function, "envName[{}]", envName);
		
		for ( int i = 0 ; i < address.length ; ++i ) {
			logger.info(className, function, "address({})[{}] BF", i, address[i]);
			
			address[i] = address[i].replace(":", "");
			
			logger.info(className, function, "address({})[{}] AF", i, address[i]);
		}
		logger.info(className, function, "commandValue[{}] bypassInitCond[{}] bypassRetCond[{}] sendAnyway[{}] ", new Object[]{commandValue, bypassInitCond, bypassRetCond, sendAnyway});
		
//		if ( null != subject ) {
//    		subject.setState("send message to envName["+envName+"] address[0]["+address[0]+"] commandValue["+commandValue+"] bypassInitCond["+bypassInitCond+"] bypassRetCond["+bypassRetCond+"] sendAnyway["+sendAnyway+"]");
//    	}

		String [] structuredValueAddress = address;
		
		logger.info(className, function, "structuredValueAddress(0)[{}]", structuredValueAddress[0]);
		
		if ( null != ctlAccess ) {
			if ( null != subject ) {
        		subject.setState("COMMAND SENT");
	    	}				
		} else {
			logger.warn(className, function, "m_CTLAccess IS NULL");
		}
		ctlAccess.sendStringCommand("sendStringCommand", envName, structuredValueAddress, commandValue, bypassInitCond, bypassRetCond, sendAnyway);

		logger.end(className, function);
	}
}
