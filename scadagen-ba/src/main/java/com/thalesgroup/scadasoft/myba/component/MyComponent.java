package com.thalesgroup.scadasoft.myba.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.scadasoft.jsoncomponent.AbstractJSComponent;
import com.thalesgroup.scadasoft.jsoncomponent.IJSComponent;
import com.thalesgroup.scadasoft.jsoncomponent.IJSNotifier;
import com.thalesgroup.scadasoft.jsoncomponent.INotifManager;
import com.thalesgroup.scadasoft.jsoncomponent.JSComponentMgr;
import com.thalesgroup.scadasoft.jsoncomponent.SCSJSONParameter.SCSJSONParamType;
import com.thalesgroup.scadasoft.jsoncomponent.SCSJSONRequest;
import com.thalesgroup.scadasoft.jsoncomponent.SCSJSONParameter;

public class MyComponent extends AbstractJSComponent {

    static private final Logger s_logger = LoggerFactory.getLogger(MyComponent.class);
    
    @Override
    public String getName() {
        return "MyComponent";
    }

    @Override
    public String getDescription() {
        return "Hello World test component";
    }

    /*
     * HelloWorld Component:
     * 
     * expect the following JSON request
     * 
     * {"component":"MyComponent", "request":"hello", "source"="chief" }
     * 
     * Build the following answer {"component":"MyComponent", "request":"hello",
     * "errorCode":0, "response":{"msg":"Hello chief"}}
     * 
     */

    @SCSJSONRequest(value = "hello")
    public ObjectNode doHelloRequest(ObjectNode req, String sourceId) {
        s_logger.info("MyComponent doHelloRequest IN:" + req.toString());

        ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();
        if ("".equals(sourceId)) {
            resp.put("msg", "Who are you?");
        } else {
            resp.put("msg", "Hello " + sourceId);
        }

        s_logger.info("MyComponent doHelloRequest OUT:" + resp.toString());
        return resp;
    }

    public static class MyCountDownManager implements Runnable, IJSNotifier {

		private final String m_correlationId;
		private final INotifManager m_notifManager;
		private int m_currentValue;
		private Thread m_thread;
		private final String m_name;
		
		public MyCountDownManager(String correlationId, String name, int startValue, INotifManager nm) {
			m_correlationId = correlationId;
			m_notifManager = nm;
			m_currentValue = startValue;
			m_name = name;
			m_thread = new Thread(this);
			m_thread.start();
		}

		@Override
		public void stop() {
			
			m_thread.interrupt();
			try {
				m_thread.join();
			} catch (InterruptedException e) {
				
			}
			if (m_notifManager != null) {
        		m_notifManager.stopLongOperation(m_correlationId);
        		m_notifManager.unregisterLongRunningOpe(this);
        	}
		}

		@Override
		public void run() {
			boolean run = true;
		    sendFirstMessage();
	        while (run) {
	            try {
	                Thread.sleep((long) 1000);
	                m_currentValue = m_currentValue - 1;
	                sendMessage();
	                if (m_currentValue == 0) {
	                	run = false;
	                	if (m_notifManager != null) {
	                		m_notifManager.stopLongOperation(m_correlationId);
	                		m_notifManager.unregisterLongRunningOpe(this);
	                	}
	                }
	            } catch (InterruptedException e) {
	                run = false;
	            }
	        }
			
		}

		private void sendFirstMessage() {
			 
			ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();
			resp.put(IJSComponent.c_JSON_ERRORCODE_ARG, 0);
			resp.put(IJSComponent.c_JSON_REQUEST_ARG, "countdown");
			
			ObjectNode data = JSComponentMgr.s_json_factory.objectNode();
			data.put("value", m_currentValue);
			data.put("name", m_name);
			resp.set(IJSComponent.c_JSON_RESPONSE_ARG, data);
			if (m_notifManager != null) {
				m_notifManager.sendLongOperationInsert(m_correlationId, "MyComponent", resp);
			}
		}
		
		private void sendMessage() {
			ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();
			resp.put(IJSComponent.c_JSON_ERRORCODE_ARG, 0);
			resp.put(IJSComponent.c_JSON_REQUEST_ARG, "countdown");
			
			ObjectNode data = JSComponentMgr.s_json_factory.objectNode();
			data.put("value", m_currentValue);
			data.put("name", m_name);
			resp.set(IJSComponent.c_JSON_RESPONSE_ARG, data);
			if (m_notifManager != null) {
				m_notifManager.sendLongOperationUpdate(m_correlationId, "MyComponent", resp);
			}
		}
    	
    }
    
    @SCSJSONRequest(value = "countdown", longRunning = true,
            inputParam = {
            		@SCSJSONParameter(value = "name", type = SCSJSONParamType.STRING, mandatory = false, defaultValue = "counter"),
                    @SCSJSONParameter(value = "start", type = SCSJSONParamType.NUMBER, mandatory = true) 
    },
            outputParam = {
            		@SCSJSONParameter(value = "name", type = SCSJSONParamType.STRING),
                    @SCSJSONParameter(value = "value", type = SCSJSONParamType.NUMBER)
    }
            )
    public ObjectNode doCountdownRequest(ObjectNode req, String sourceId) {
        s_logger.info("MyComponent doCountdownRequest IN:" + req.toString());

        int startValue = getIntParam("start", req);
        String name = getStringParam("name", req);
        
        // first answer is init value
        ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();
        resp.put("value", startValue);
        
        // start thread and register object to long running list
        String correlationId = getUUID(req);
        
        MyCountDownManager cd = new MyCountDownManager(correlationId, name, startValue, m_notifManager);
        m_notifManager.registerLongRunningOpe(cd);
        
        s_logger.info("MyComponent doCountdownRequest OUT:" + resp.toString());
        return resp;
    }

	@Override
	protected void doComponentInit() {
	}

}
