package com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.UIGenericServiceImpl_i;
import com.thalesgroup.scadagen.wrapper.wrapper.shared.UIGenericDto;
import com.thalesgroup.scadagen.wrapper.wrapper.shared.UIGenericDto_i;

/**
 * 
 * Generic RPC Channel with JSON Message format
 * 
 * @author syau
 *
 */
public class UIGenericMgr implements AsyncCallback<UIGenericDto_i> {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private final UIGenericServiceAsync uiGenericService = GWT.create(UIGenericService.class);
	
	private UIGenericMgrEvent request = null;
//	private Map<UIGenericDto, UIGenericMgrEvent> requests = new HashMap<UIGenericDto, UIGenericMgrEvent>();
	
	public void execute(JSONObject request, UIGenericMgrEvent event) {
		final String function = "execute";
		logger.begin(function);

		this.request = event;
//		requests.put(uiGenericDto, event);
		
        Date now = new Date();
        request.put(UIGenericServiceImpl_i.OperationAttribute1, new JSONString(UIGenericServiceImpl_i.REQUEST));
        request.put(UIGenericServiceImpl_i.OperationAttribute2, new JSONNumber(now.getTime()));
		
		UIGenericDto_i uiGenericDto = new UIGenericDto();
		uiGenericDto.setData(request.toString());
		
		uiGenericService.execute(uiGenericDto, this);
		
		logger.end(function);
	}

	@Override
	public void onSuccess(UIGenericDto_i uiGenericDto) {
		final String function = "onSuccess";
		logger.begin(function);
		if ( null != uiGenericDto) {
            if (uiGenericDto.getData() != null && JsonUtils.safeToEval(uiGenericDto.getData())) {
            	JSONObject o = JSONParser.parseStrict(uiGenericDto.getData()).isObject();
				if ( null != request ) {
					request.uiGenericMgrEventReady(o.isObject());
					request = null;
				}
            }
		}
		logger.end(function);
	}
	
	@Override
	public void onFailure(Throwable caught) {
		final String function = "onFailure";
		logger.begin(function);
		logger.end(function);
	}

}