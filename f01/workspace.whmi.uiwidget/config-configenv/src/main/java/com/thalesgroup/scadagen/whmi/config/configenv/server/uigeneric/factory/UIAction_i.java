package com.thalesgroup.scadagen.whmi.config.configenv.server.uigeneric.factory;

import javax.servlet.http.HttpServletRequest;

import com.google.gwt.json.client.JSONObject;

public interface UIAction_i {
	
	JSONObject execute(HttpServletRequest httpServletRequest, JSONObject jsonObject);
}
