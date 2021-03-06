package com.thalesgroup.scadasoft.myba.component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.scadasoft.jsoncomponent.IJSComponent;
import com.thalesgroup.scadasoft.jsoncomponent.JSComponentMgr;
import com.thalesgroup.scadasoft.myba.component.SCADARest;

@Path("/scs")
public class SCSRestRouter {
	private static final Logger s_logger = LoggerFactory.getLogger(SCSRestRouter.class);
	
	private static SCADARest m_compMgr = null;
    
    public SCSRestRouter() {
    	if (m_compMgr == null) {
    		m_compMgr = new SCADARest();
    	}
    }
    
	@GET
	@Path("/hello")
	@Produces(MediaType.APPLICATION_JSON)
	public String sayHello() {
		s_logger.debug("Calling SCSRestRouter.sayHello method");
		ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();
		Date now = new Date();
		resp.put("hello", now.toString());
		return resp.toString();

	} 
	
	@POST
	@Path("/request")
	@Produces(MediaType.APPLICATION_JSON)
	public String managePost(String jsonAction) {
		s_logger.debug("Calling SCSRestRouter.managePost method");
		JsonNode result = null;
		try {
			result = m_compMgr.executeRequest(jsonAction);
		} catch (Throwable e) {
			s_logger.error("SCADAExecutor unexpected error while parsing JSON: {} exc {}", jsonAction, e.getMessage());
			s_logger.trace(e.getMessage(), e);
			result = m_compMgr.buildErrorObject(4, e.getMessage());
		}

		String res = "";
		try {
			res = JSComponentMgr.GetObjectMapper().writeValueAsString(result);
		} catch (JsonProcessingException e) {
			res = e.getMessage();
		}
		return res;
	}   	

	@GET
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllUpdates() {
		s_logger.debug("Calling SCSRestRouter.getAllUpdates method");
		ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();
		ObjectNode n = m_compMgr.getAllUpdates();
		resp.set("update", n);
		return resp.toString();
	}
	
	@GET
	@Path("/update/{notifid}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUpdates(@PathParam("notifid") String notifid) {
		s_logger.debug("Calling SCSRestRouter.getUpdates method");
		ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();

		ArrayNode n = m_compMgr.getUpdate(notifid);
		resp.set("update", n);
		return resp.toString();

	} 
	
	// GET based API
	// use managePost for POST API
	@GET
	@Path("/service")
	@Produces(MediaType.APPLICATION_JSON)
	public String getComponentList() {
		s_logger.debug("Calling SCSRestRouter.getComponentList method");
		JsonNode result = null;
		String jsonAction = "{}";
		try {
			result = m_compMgr.executeRequest(jsonAction);
		} catch (Throwable e) {
			s_logger.error("SCADAExecutor unexpected error while parsing JSON: {} exc {}", jsonAction, e.getMessage());
			s_logger.trace(e.getMessage(), e);
			result = m_compMgr.buildErrorObject(4, e.getMessage());
		}

		String res = "";
		try {
			res = JSComponentMgr.GetObjectMapper().writeValueAsString(result);
		} catch (JsonProcessingException e) {
			res = e.getMessage();
		}
		return res;
	} 
	
	@GET
	@Path("/service/{serviceid}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getComponentDesc(@PathParam("serviceid") String serviceid) {
		s_logger.debug("Calling SCSRestRouter.getComponentDesc method");
		ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();
		IJSComponent comp = JSComponentMgr.getComponents().get(serviceid);
		
		if (comp != null) {
			resp.put(IJSComponent.c_JSON_COMPONENT_ARG, serviceid);
			resp = comp.doRequest(resp);
		} else {
			resp.put("error", "unknown service: " + serviceid);
		}
		
		return resp.toString();

	} 
	
	@GET
	@Path("/service/{serviceid}/{requestid}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getComponentReqDesc(@PathParam("serviceid") String serviceid, @PathParam("requestid") String requestid, @Context UriInfo info) {
		s_logger.debug("Calling SCSRestRouter.getComponentReqDesc method");
		ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();
		IJSComponent comp = JSComponentMgr.getComponents().get(serviceid);
		
		if (comp != null) {
			resp.put(IJSComponent.c_JSON_COMPONENT_ARG, serviceid);
			resp.put(IJSComponent.c_JSON_REQUEST_ARG, requestid);
			ObjectNode plist = JSComponentMgr.s_json_factory.objectNode();
			for(Entry<String, List<String>> p : info.getQueryParameters().entrySet()) {
				String param = p.getKey();
				String value = p.getValue().get(0);
				JsonNode v = null;
				try {
					v = JSComponentMgr.GetObjectMapper().readTree(value);
				} catch (JsonProcessingException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				if (v == null) {
					plist.put(param, value);
				} else {
					plist.set(param, v);
				}
				if ("help".equals(param)) {
					resp = comp.requestHelp(requestid);
					return resp.toString();
				}
				
			}
			resp.set(IJSComponent.c_JSON_REQUEST_PARAM_ARG, plist);
			resp = comp.doRequest(resp);
		} else {
			resp.put("error", "unknown service: " + serviceid);
		}
		
		return resp.toString();

	} 
	
}

