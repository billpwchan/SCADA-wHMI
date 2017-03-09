package com.thalesgroup.scadasoft.myba.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.scadasoft.jsoncomponent.AbstractJSComponent;
import com.thalesgroup.scadasoft.jsoncomponent.JSComponentMgr;
import com.thalesgroup.scadasoft.jsoncomponent.SCSJSONParameter;
import com.thalesgroup.scadasoft.jsoncomponent.SCSJSONRequest;
import com.thalesgroup.scadasoft.jsoncomponent.SCSJSONParameter.SCSJSONParamType;

public class MathComponent extends AbstractJSComponent {
	
	static private final Logger LOGGER = LoggerFactory.getLogger(MathComponent.class);
	
	@Override
	public String getName() {
		return MathComponent.class.getSimpleName();
	}

	@Override
	public String getDescription() {
		return "A very complex way to do basic mathematics";
	}

	@Override
	protected void doComponentInit() {
		// nothing special to init
	}

	@SCSJSONRequest(value = "add", description = "Add two integers",
       inputParam = {
			@SCSJSONParameter(value = "a", type = SCSJSONParamType.NUMBER, mandatory = true, description = "first number"),
	        @SCSJSONParameter(value = "b", type = SCSJSONParamType.NUMBER, mandatory = false, defaultValue = "1", description = "second number, if not set default to 1 (incr)" ) 
       },
       outputParam = {
            @SCSJSONParameter(value = "result", type = SCSJSONParamType.NUMBER)
    	}
            )
    public ObjectNode doAddRequest(ObjectNode req, String sourceId) {
		LOGGER.debug("MathComponent doAddRequest IN:" + req.toString());
 
        ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();

        int a = getIntParam("a", req);
        int b = getIntParam("b", req);
        
        resp.put("result", a+b);
        
        LOGGER.debug("MathComponent doAddRequest OUT:" + resp.toString());
        return resp;
    }
	
	@SCSJSONRequest(value = "stat", description = "Basic statistics",
       inputParam = {
			@SCSJSONParameter(value = "data", type = SCSJSONParamType.ARRAY, mandatory = true, description = "array of numbers")
	    },
       outputParam = {
            @SCSJSONParameter(value = "mean", type = SCSJSONParamType.NUMBER),
            @SCSJSONParameter(value = "min", type = SCSJSONParamType.NUMBER),
            @SCSJSONParameter(value = "max", type = SCSJSONParamType.NUMBER),
            @SCSJSONParameter(value = "stddev", type = SCSJSONParamType.NUMBER)
    	}
            )
    public ObjectNode doStatRequest(ObjectNode req, String sourceId) {
		LOGGER.debug("MathComponent doStatRequest IN:" + req.toString());
 
        ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();

        double[] data = getDoubleArrayParam("data", req);
        double min = 0;
        double max = 0;
        double mean = 0;		
        double stddev = 0;
        
        if (data.length == 0) {
        	return buildErrorObject(1001, "MathComponent FAILURE: cannot calculate stat on empty array");
        }
        
        min = data[0];
        max = data[0];	
        int i;
        for(i = 0; i < data.length; i++) {
        	mean = mean + data[i];
        	if (data[i] < min) min = data[i];
        	if (data[i] > max) max = data[i];
        }
        
        mean = mean / data.length;
        
        for(i = 0; i < data.length; i++) {
        	stddev = stddev + (data[i] - mean)*(data[i] - mean);
        }
        stddev = Math.sqrt(stddev / data.length);
        
        resp.put("mean", mean);
        resp.put("min", min);
        resp.put("max", max);
        resp.put("stddev", stddev);
        
        LOGGER.debug("MathComponent doStatRequest OUT:" + resp.toString());
        return resp;
    }

}
