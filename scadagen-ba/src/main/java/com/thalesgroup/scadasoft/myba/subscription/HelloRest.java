package com.thalesgroup.scadasoft.myba.subscription;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.scadasoft.hvconnector.operation.SCADAExecutor;


@Path("/hello")
public class HelloRest {

	@GET
	@Path("/msg")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllplatform() {
		ObjectNode resp = SCADAExecutor.s_json_factory.objectNode();

		resp.put("name", "Agamemnon");
		resp.put("age", 32);
		return resp.toString();

	}   	

}

