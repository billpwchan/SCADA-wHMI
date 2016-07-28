package com.thalesgroup.scadagen.calculated.common;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;

public abstract class OlsDecoder extends SCSStatusComputer {

	public static ObjectMapper s_json_mapper = null;
	
    public OlsDecoder() {
        m_statusSet.add("data");
        if (s_json_mapper == null) {
        	s_json_mapper = new ObjectMapper();
            // to prevent exception when encountering unknown property:
            s_json_mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // to allow coercion of JSON empty String ("") to null Object value:
            s_json_mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }
    }

    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
        // prepare default response
    	StringAttribute ret = new StringAttribute();
        ret.setAttributeClass(StringAttribute.class.getName());
        ret.setValue("");
        ret.setValid(true);
        ret.setTimestamp(new Date());
        
        // get value of data
        AttributeClientAbstract<?> dataobj = inputStatusByName.get("data");
        StringAttribute jsdata = (dataobj instanceof StringAttribute ? (StringAttribute) dataobj : null);

        if (jsdata != null) {
            String data = jsdata.getValue();
            // decode json
            ObjectNode root = null;
			try {
				root = (ObjectNode) s_json_mapper.readTree(data);
				JsonNode v = root.get(m_name);
	            ret.setValue(v.asText());
	            ret.setValid(true);
	            ret.setTimestamp(new Date());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return ret;
    }

}
