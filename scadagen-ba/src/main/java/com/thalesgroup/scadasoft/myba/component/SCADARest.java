package com.thalesgroup.scadasoft.myba.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.scadasoft.jsoncomponent.IJSNotifier;
import com.thalesgroup.scadasoft.jsoncomponent.INotifManager;
import com.thalesgroup.scadasoft.jsoncomponent.JSComponentMgr;

public class SCADARest implements INotifManager {
	
    /** Logger **/
    private static final Logger LOGGER = LoggerFactory.getLogger(SCADARest.class);
    
    private static JSComponentMgr m_compMgr = null;
    private Set<IJSNotifier> m_longRunningOperations = new HashSet<IJSNotifier>();
    private Map<String, ArrayList<ObjectNode>> m_notifList = new LinkedHashMap<String, ArrayList<ObjectNode>>();
    
    public SCADARest() {
    	if (m_compMgr == null) {
    		m_compMgr = new JSComponentMgr(this);
    	}
    }

	@Override
	public void sendLongOperationInsert(String notifid, String component, ObjectNode resp) {
		LOGGER.error("sendLongOperationInsert: " + notifid + " " + component + resp.toString());
		synchronized (m_notifList) {
			ArrayList<ObjectNode> al = m_notifList.get(notifid);
			if (al == null) {
				al = new ArrayList<ObjectNode>();
				m_notifList.put(notifid, al);
			}
			JsonNode n = resp.get("response");
			if (n != null && n.isObject()) {
				al.add((ObjectNode)n);
			}
		}
		
	}

	@Override
	public void sendLongOperationUpdate(String notifid, String component, ObjectNode resp) {
		LOGGER.error("sendLongOperationUpdate: " + notifid + " " + component + resp.toString());
		synchronized (m_notifList) {
			ArrayList<ObjectNode> al = m_notifList.get(notifid);
			if (al == null) {
				al = new ArrayList<ObjectNode>();
				m_notifList.put(notifid, al);
			}
			JsonNode n = resp.get("response");
			if (n != null && n.isObject()) {
				al.add((ObjectNode)n);
			}
		}
	}

	@Override
	public void stopLongOperation(String notifid) {
		LOGGER.error("stopLongOperation: " + notifid);
		synchronized (m_notifList) {
			m_notifList.remove(notifid);
		}
	}

	@Override
    public synchronized void registerLongRunningOpe(IJSNotifier lr) {
        m_longRunningOperations.add(lr);
    }
	
	@Override
    public synchronized void unregisterLongRunningOpe(IJSNotifier lr) {
        m_longRunningOperations.remove(lr);
    }

	public JsonNode executeRequest(String jsonAction) {
		return m_compMgr.executeRequest(jsonAction);
	}

	public JsonNode buildErrorObject(int errCode, String message) {
		return m_compMgr.buildErrorObject(errCode, message);
	}

	@Override
	public String convertEqpIdFromRemoteId(String remoteEqpId) {
		return remoteEqpId;
	}

	public ArrayNode getUpdate(String notifid) {
		List<ObjectNode> vl = null;
		
		synchronized (m_notifList) {
			// get list and replace by empty if it exists
			vl = m_notifList.remove(notifid);
			if (vl != null) {
				m_notifList.put(notifid, new ArrayList<ObjectNode>());
			}
		}
		
		if (vl != null) {
			ArrayNode n = JSComponentMgr.s_json_factory.arrayNode();
			for(ObjectNode v : vl) {
				n.add(v);
			}
			return n;
		}
		return null;
	}

	public ObjectNode getAllUpdates() {
		ObjectNode n = JSComponentMgr.s_json_factory.objectNode();
		
		synchronized (m_notifList) {
			for(Entry<String, ArrayList<ObjectNode>> v : m_notifList.entrySet()) {
				n.put(v.getKey(), v.getValue().size());
			}
		}
		
		return n;
	}

}
