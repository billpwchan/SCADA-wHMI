package com.thalesgroup.scadasoft.myba.operation;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.scadasoft.hvconnector.operation.IOperationExecutor;
import com.thalesgroup.scadasoft.hvconnector.operation.SCSOperationResponder;

public class SCADAgenOperationResponder extends SCSOperationResponder {
	/** Logger **/
    private static final Logger s_logger = LoggerFactory.getLogger(SCADAgenOperationResponder.class);
    //private static final String LOG_PREFIX = "[SCADAgenOperationResponder] ";
    
    private Map<String, Class<? extends IOperationExecutor>> m_executorBuilderMap = new HashMap<String, Class<? extends IOperationExecutor>>();
    
    private HILCExecutor m_hilcExec = null;
    
	@Override
	protected IOperationExecutor getExecutor(String execname) {
        // look for user defined executor
        IOperationExecutor exec = null;
        try {
            if (m_executorBuilderMap.get(execname) != null) {
                exec = m_executorBuilderMap.get(execname).newInstance();
            }
        } catch (InstantiationException e) {
            s_logger.warn("SCADAgen BA - cannot instantiate user class for operation executor {}" + execname, e);
            exec = null;
        } catch (IllegalAccessException e) {
            s_logger.warn("SCADAgen BA - illegal access while instantiating user class for operation executor {}" + execname, e);
            exec = null;
        }

        // if null search well known executor
        if (exec == null) {
            if ("HILCServer".equals(execname)) {
            	if (m_hilcExec == null) {
            		m_hilcExec = new HILCExecutor();
            	}
                return m_hilcExec;
            } else {
            	return super.getExecutor(execname);
            }
        }

        return exec;
    }
	
}
