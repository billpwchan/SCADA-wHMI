package com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric.UIGenericService;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.factory.UIActionMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.factory.UIAction_i;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.util.JsonUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.shared.UIGenericDto;
import com.thalesgroup.scadagen.wrapper.wrapper.shared.UIGenericDto_i;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UIGenericServiceImpl extends RemoteServiceServlet implements UIGenericService {

	private UILogger_i logger					= UILoggerFactory.getInstance().get(this.getClass().getName());
	
	public static JsonNodeFactory s_json_factory = new JsonNodeFactory(false);
	
	protected static ObjectMapper s_json_mapper = null;
	
	public UIGenericServiceImpl() {
		logger.debug("Begin");
    	if (s_json_mapper == null) {
            s_json_mapper = new ObjectMapper();
            // to prevent exception when encountering unknown property:
            s_json_mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // to allow coercion of JSON empty String ("") to null Object value:
            s_json_mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }
    	logger.debug("End");
	}

	@Override
	public UIGenericDto_i execute(UIGenericDto_i uiGenericDto) {
		logger.debug("Begin");
		
		String data = null;
		
        if (uiGenericDto.getData() != null) {
        	try {
				ObjectNode request = (ObjectNode) s_json_mapper.readTree(uiGenericDto.getData());
				
				if ( null != request) {
					String ot1	= JsonUtil.getString(request, UIGenericServiceImpl_i.OperationAttribute1);
					long ot2	= JsonUtil.getLong(request, UIGenericServiceImpl_i.OperationAttribute2);
					String ot3	= JsonUtil.getString(request, UIGenericServiceImpl_i.OperationAttribute3);
					String ot4	= JsonUtil.getString(request, UIGenericServiceImpl_i.OperationAttribute4);

					if ( logger.isDebugEnabled() ) {
						logger.debug("ot1[{}]", ot1);
						logger.debug("ot2[{}]", ot2);
						logger.debug("ot3[{}]", ot3);
						logger.debug("ot4[{}]", ot4);
					}
					
					if ( ot1.equals(UIGenericServiceImpl_i.REQUEST) ) {
						if ( null != ot3 ) {
							UIAction_i uiAction_i = UIActionMgr.getInstance().getUIAction(ot3);
							if ( null != uiAction_i ) {
								ObjectNode jsdata = uiAction_i.execute(getThreadLocalRequest(), request);
								if ( null != jsdata ) {
							        Date now = new Date();		
							        jsdata.put(UIGenericServiceImpl_i.OperationAttribute1, UIGenericServiceImpl_i.RESPONSE);
							        jsdata.put(UIGenericServiceImpl_i.OperationAttribute2, now.getTime());
							        try {
							        	data = s_json_mapper.writeValueAsString(jsdata);
									} catch (JsonProcessingException e1) {
										logger.warn("Cannot marshal json data: JsonProcessingException[{}]", e1.getMessage());
									}
								} else {
									logger.warn("response IS NULL", ot3);
								}
							} else {
								logger.warn("ot3[{}] uiAction_i IS NULL", ot3);
							}
						} else {
							logger.warn("ot3 IS NULL");
						}
					} else {
						logger.warn("ot1[{}] equal[{}] IS INVALUD", ot1, UIGenericServiceImpl_i.REQUEST);
					}

				} else {
					logger.warn("request IS NULL");
				}
			} catch (JsonProcessingException e) {
				logger.warn("JsonProcessingException[{}]", e.toString());
			} catch (IOException e) {
				logger.warn("IOException[{}]", e.toString());
			}
        } else {
        	logger.warn("uiGenericDto IS NULL");
        }
        
        if ( null == data ) { logger.warn("data IS NULL"); }

        UIGenericDto_i request = new UIGenericDto();
        request.setData(data);
        
        logger.debug("End");
        return request;
	}
}
