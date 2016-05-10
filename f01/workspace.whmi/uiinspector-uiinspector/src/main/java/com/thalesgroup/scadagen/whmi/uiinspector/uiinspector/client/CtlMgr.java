package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Subject;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ICTLComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ScsCTLComponentAccess;

public class CtlMgr {
	
	private static Logger logger = Logger.getLogger(CtlMgr.class.getName());
	
	private static CtlMgr instance = null;
	public static CtlMgr getInstance() {
		if ( null == instance ) instance = new CtlMgr(); 
		return instance;
	}
	
	private Subject subject = null;
	public Subject getSubject() { return subject; }
	
	private ScsCTLComponentAccess ctlAccess = null;
	private CtlMgr () {
		logger.log(Level.SEVERE, "CtlMgr Begin");
		
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
		
		logger.log(Level.SEVERE, "CtlMgr End");
	}
	
	public void connect() {
		logger.log(Level.SEVERE, "connect Begin");
		logger.log(Level.SEVERE, "connect End");
	}
	public void disconnect() {
		logger.log(Level.SEVERE, "disconnect Begin");
		try {
			ctlAccess.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ctlAccess=null;
		logger.log(Level.SEVERE, "disconnect End");
	}
	
	public void sendControl (String type, String envName, String [] address, String commandValue, int bypassInitCond, int bypassRetCond, int sendAnyway) {
		
		logger.log(Level.SEVERE, "sendControl Begin");
		
		logger.log(Level.SEVERE, "sendControl type["+type+"]");
		logger.log(Level.SEVERE, "sendControl envName["+envName+"]");
		for ( int i = 0 ; i < address.length ; ++i ) {
			logger.log(Level.SEVERE, "sendControl address("+i+")["+address[i]+"] BF");
			
			address[i] = "<alias>" + address[i].replace(":", "");
			
			logger.log(Level.SEVERE, "sendControl address("+i+")["+address[i]+"] AF");
		}
		logger.log(Level.SEVERE, "sendControl commandValue["+commandValue+"]");
		logger.log(Level.SEVERE, "sendControl bypassInitCond["+bypassInitCond+"]");
		logger.log(Level.SEVERE, "sendControl bypassRetCond["+bypassRetCond+"]");
		logger.log(Level.SEVERE, "sendControl sendAnyway["+sendAnyway+"]");
		
		if ( null != subject ) {
    		subject.setState("send message to type["+type+"] envName["+envName+"] address[0]["+address[0]+"] commandValue["+commandValue+"] bypassInitCond["+bypassInitCond+"] bypassRetCond["+bypassRetCond+"] sendAnyway["+sendAnyway+"]");
    	}
		
		if ( 0 == type.compareTo("aio") ) {
			String [] analogValueAddress = address;
			float floatCommandValue = Float.valueOf(commandValue);
			
			logger.log(Level.SEVERE, "sendControl type["+type+"] floatCommandValue["+floatCommandValue+"]");
			logger.log(Level.SEVERE, "sendControl type["+type+"] analogValueAddress(0)["+analogValueAddress[0]+"]");
			
			if ( null != ctlAccess ) {
				ctlAccess.sendFloatCommand("sendFloatCommand", envName, analogValueAddress, floatCommandValue, bypassInitCond, bypassRetCond, sendAnyway);
				if ( null != subject ) {
	        		subject.setState("sendFloatCommand sent");
		    	}				
			} else {
				logger.log(Level.SEVERE, "sendControl m_CTLAccess IS NULL");
			}

		} else if ( 0 == type.compareTo("sio") ) {
			String [] structuredValueAddress = address;
			
			logger.log(Level.SEVERE, "sendControl type["+type+"] structuredValueAddress(0)["+structuredValueAddress[0]+"]");
			
			if ( null != ctlAccess ) {
				if ( null != subject ) {
	        		subject.setState("sendStringCommand sent");
		    	}				
			} else {
				logger.log(Level.SEVERE, "sendControl m_CTLAccess IS NULL");
			}
			ctlAccess.sendStringCommand("sendStringCommand", envName, structuredValueAddress, commandValue, bypassInitCond, bypassRetCond, sendAnyway);

		} else if ( 0 == type.compareTo("dio") ) {
			String [] digitalValueAddress = address;
			int intCommandValue = Integer.valueOf(commandValue);
			
			logger.log(Level.SEVERE, "sendControl type["+type+"] intCommandValue["+intCommandValue+"]");
			logger.log(Level.SEVERE, "sendControl type["+type+"] digitalValueAddress(0)["+digitalValueAddress[0]+"]");
			
			if ( null != ctlAccess ) {
				ctlAccess.sendIntCommand("sendIntCommand", envName, digitalValueAddress, intCommandValue, bypassInitCond, bypassRetCond, sendAnyway);
				if ( null != subject ) {
	        		subject.setState("sendIntCommand sent");
		    	}				
			} else {
				logger.log(Level.SEVERE, "sendControl m_CTLAccess IS NULL");
			}
		}
		
		logger.log(Level.SEVERE, "sendControl End");
		
	}
	

}
