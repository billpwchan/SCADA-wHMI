package com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.factory;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface UIAction_i {
	
	ObjectNode execute(HttpServletRequest httpServletRequest, ObjectNode request);
}
