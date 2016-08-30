package com.thalesgroup.scadasoft.myba.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.scadasoft.hvconnector.component.AbstractSCSComponent;
import com.thalesgroup.scadasoft.hvconnector.operation.ISCSLongRunningOperation;
import com.thalesgroup.scadasoft.hvconnector.operation.SCADAExecutor;
import com.thalesgroup.scadasoft.hvconnector.operation.SCSOperationResponder;
import com.thalesgroup.scadasoft.hvconnector.operation.spi.ISCSComponent;

public class MyComponent extends AbstractSCSComponent {

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
     * {"component":"MyComponent", "request":"hello", source="chief" }
     * 
     * Build the following answer {"component":"MyComponent", "request":"hello",
     * "errorCode":0, "response":{"msg":"Hello chief"}}
     * 
     */

    @SCSJSONRequest(value = "hello")
    public ObjectNode doHelloRequest(ObjectNode req, String sourceId) {
        s_logger.info("MyComponent doHelloRequest IN:" + req.toString());

        ObjectNode resp = SCADAExecutor.s_json_factory.objectNode();
        if ("".equals(sourceId)) {
            resp.put("msg", "Who are you?");
        } else {
            resp.put("msg", "Hello " + sourceId);
        }

        s_logger.info("MyComponent doHelloRequest OUT:" + resp.toString());
        return resp;
    }

    public static class MyCountDownManager implements ISCSLongRunningOperation, Runnable {

		private final String m_correlationId;
		private final SCSOperationResponder m_opeManager;
		private int m_currentValue;
		private Thread m_thread;
		private final String m_name;
		
		public MyCountDownManager(String correlationId, String name, int startValue, SCSOperationResponder opeManager) {
			m_correlationId = correlationId;
			m_opeManager = opeManager;
			m_currentValue = startValue;
			m_name = name;
			sendFirstMessage();
			m_thread = new Thread(this);
			m_thread.start();
		}

		@Override
		public String getName() {
			return "MyCountDownManager";
		}

		@Override
		public String getDescription() {
			return "Simple count down in seconds. " + m_name + " current value: " + m_currentValue;
		}

		@Override
		public void stop() {
			m_thread.interrupt();
			try {
				m_thread.join();
			} catch (InterruptedException e) {
				
			}
		}

		@Override
		public void run() {
			boolean run = true;
	        while (run) {
	            try {
	                Thread.sleep((long) 1000);
	                m_currentValue = m_currentValue - 1;
	                sendMessage();
	                if (m_currentValue == 0) {
	                	run = false;
	                	m_opeManager.unregisterLongRunningOpe(this);
	                	m_opeManager.stopLongOperation(m_correlationId);
	                }
	            } catch (InterruptedException e) {
	                run = false;
	            }
	        }
			
		}

		private void sendFirstMessage() {
			 
			ObjectNode resp = SCADAExecutor.s_json_factory.objectNode();
			resp.put(ISCSComponent.c_JSON_ERRORCODE_ARG, 0);
			resp.put(ISCSComponent.c_JSON_REQUEST_ARG, "countdown");
			
			ObjectNode data = SCADAExecutor.s_json_factory.objectNode();
			data.put("value", m_currentValue);
			data.put("name", m_name);
			resp.set(ISCSComponent.c_JSON_RESPONSE_ARG, data);
			
	        m_opeManager.sendLongOperationInsert(m_correlationId, "MyComponent", resp);
		}
		
		private void sendMessage() {
			ObjectNode resp = SCADAExecutor.s_json_factory.objectNode();
			resp.put(ISCSComponent.c_JSON_ERRORCODE_ARG, 0);
			resp.put(ISCSComponent.c_JSON_REQUEST_ARG, "countdown");
			
			ObjectNode data = SCADAExecutor.s_json_factory.objectNode();
			data.put("value", m_currentValue);
			data.put("name", m_name);
			resp.set(ISCSComponent.c_JSON_RESPONSE_ARG, data);
	        m_opeManager.sendLongOperationUpdate(m_correlationId, "MyComponent", resp);
		}
    	
    }
    @SCSJSONRequest(value = "countdown", longRunning = true,
            inputParam = {
            		@SCSJSONParameter(value = "name", type = SCSJSONParamType.STRING, mandatory = true),
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
        ObjectNode resp = SCADAExecutor.s_json_factory.objectNode();
        resp.put("value", startValue);
        
        // start thread and register object to long running list
        String correlationId = getUUID(req);
        
        m_opeManager.registerLongRunningOpe(new MyCountDownManager(correlationId, name, startValue, m_opeManager));
        
        s_logger.info("MyComponent doCountdownRequest OUT:" + resp.toString());
        return resp;
    }

}
