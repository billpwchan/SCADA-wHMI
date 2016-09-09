package com.thalesgroup.rtt.registry;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.google.common.collect.Tables;
import com.google.common.collect.TreeBasedTable;
import com.thalesgroup.rtt.beans.SubscriptionRequest;

@Component
public class SubscriptionTracker {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final RequestInfoRegistry requestInfoRegistry = new RequestInfoRegistry();
	
	private final RequestLinkRegistry requestLinkRegistry = new RequestLinkRegistry();
	
	public void addOrReplaceSubscriptionRequest(SubscriptionRequest subReq) {
		requestLinkRegistry.addLinkInternal(subReq.getQueryKey(), subReq.getCallerId(), subReq.getLink());
		requestInfoRegistry.addSubscriptionInternal(subReq.getCallerId(), subReq.getLink(), subReq);
	}
	
	public Collection<SubscriptionRequest> getSubscriptions(String callerId) {
		return requestInfoRegistry.getSubscriptionMappings(callerId);
	}
	
	public SubscriptionRequest getSubscription(String callerId, String link) {
		return requestInfoRegistry.getSubscriptionInternal(callerId, link);
	}
	
	public void modifyLastUpdate(String link, String time, String value) {
		for (Map.Entry<String, SubscriptionRequest> entry :
			requestInfoRegistry.getSubscriptionByLinkInternal(link)) 
		{
			entry.getValue().setLastUpdateTime(time);
			entry.getValue().setLastUpdateValue(value);
		}
	}
	
	public void removeSubscriptionLink(String queryKey, String callerId) {
		//SubscriptionRequest subReq = requestInfoRegistry.getSubscriptionInternal(callerId, link);
		//if (subReq != null) {
			requestLinkRegistry.removeLinkInternal(queryKey, callerId);
			//requestInfoRegistry.removeSubscriptionInternal(callerId, subReq.getLink());
		//}
	}
	
	
	public String getLink(String queryKey, String callerId ) {
		return requestLinkRegistry.getLinkInternal(queryKey, callerId);
	}
	
	public void removeExpiredSubscriptions() {
		Set<Cell<String, String, SubscriptionRequest>> chartValueCells = requestInfoRegistry.getActiveChartTable().cellSet();
		Iterator<Cell<String, String, SubscriptionRequest>> iter = chartValueCells.iterator();
		Map<String, String> expiryMap = new TreeMap<String, String>();
		while(iter.hasNext()) {
			Cell<String, String, SubscriptionRequest> cell = iter.next();
			String callerId = cell.getValue().getCallerId();
			String link = cell.getValue().getLink();
			Long termTime = cell.getValue().getTerminationTime();
			if (termTime < new Date().getTime()) {
				expiryMap.put(callerId, link);
			}
		}
		for (Map.Entry<String, String> entry : expiryMap.entrySet()) {
			SubscriptionRequest subReq = requestInfoRegistry.getSubscriptionInternal(entry.getKey(), entry.getValue());
			if (subReq != null) {
				requestLinkRegistry.removeLinkInternal(subReq.getQueryKey(), subReq.getCallerId());
				requestInfoRegistry.removeSubscriptionInternal(subReq.getCallerId(), subReq.getLink());
				log.info("Subscription Removed: callerId = " + entry.getKey() + " link = " + entry.getValue());
			}
		}
	}
	
	private class RequestLinkRegistry {
		
		//Query key, Caller ID, Bus Link (/env/subId)
		private final Table<String, String, String> requestLinkTable = HashBasedTable.create();
		
		public void addLinkInternal(String queryKey, String callerId, String busLink) {
			requestLinkTable.put(queryKey, callerId, busLink);
		}
		
		public String getLinkInternal(String queryKey, String callerId) {
			return requestLinkTable.get(queryKey, callerId);
		}
		
		public void removeLinkInternal(String queryKey, String callerId) {
			requestLinkTable.remove(queryKey, callerId);
		}
		
	}
	
	private class RequestInfoRegistry {
		
		//Caller ID, Bus Link (/env/subId), Subscription Object
		private final Table<String, String, SubscriptionRequest> requestInfoTable = TreeBasedTable.create();
		
		public void addSubscriptionInternal(String callerId, String busLink, SubscriptionRequest subscriptionRequest) {
			requestInfoTable.put(callerId, busLink, subscriptionRequest);
		}
		
		public SubscriptionRequest getSubscriptionInternal(String callerId, String busLink) {
			return requestInfoTable.get(callerId, busLink);
		}
		
		public Map<String, SubscriptionRequest> getSubscriptionInternal(String busLink) {
			return requestInfoTable.columnMap().get(busLink);
		}
		
		public Set<Entry<String, SubscriptionRequest>> getSubscriptionByLinkInternal(String busLink) {
			return Tables.transpose(requestInfoTable).row(busLink).entrySet();
		}
		
		public void removeSubscriptionInternal(String callerId, String busLink) {
			requestInfoTable.remove(callerId, busLink);
		}
		
		public Collection<SubscriptionRequest> getSubscriptionMappings(String callerId) {
			return requestInfoTable.row(callerId).values();
		}
		
		public Table<String, String, SubscriptionRequest> getActiveChartTable() {
			return Tables.unmodifiableTable(requestInfoTable);
		}
		
	}
}