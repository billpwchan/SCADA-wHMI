package com.thalesgroup.rtt.tasks;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thalesgroup.rtt.beans.RttEvent;
import com.thalesgroup.rtt.beans.Subscribed;
import com.thalesgroup.rtt.beans.SubscriptionRequest;
import com.thalesgroup.rtt.beans.SubscriptionResponse;
import com.thalesgroup.rtt.registry.SubscriptionTracker;
import com.thalesgroup.rtt.transports.RttMessagePublisher;

@Component
public class ScheduledTask {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SubscriptionTracker tracker;
	
	@Autowired
	private RttMessagePublisher publisher;
	
	@Value("${readols.link}")
	private String olsServerUrl;
	
	@Value("${readdbm.link}")
	private String dbmServerUrl;

	@Scheduled(fixedRate = 60000)
	private void checkExpired() {
		tracker.removeExpiredSubscriptions();
	}
	 
	@Scheduled(fixedRate = 1000)
	private void refreshData() {
		this.refreshAllSubscribedData();
	}
		
	//will need to separate scheduled tasks into different times instead of refreshing all at once
	//e.g. refresh based on archivenames
	public void refreshAllSubscribedData() {	
		//-loop through all subscriptiontrackers and load the archivenames into a set
		Subscribed subscribed = tracker.getAllSubscriptions();
		List<SubscriptionRequest> allSubs = subscribed.getAllSubReqs();
		//-using the set of subscribed listnames (aka archivename) and get the ols
//		String olsURLStr = olsServerUrl+"?listServer=HisServer&listName=";
		String dbmURLStr = dbmServerUrl+"?dbaddress=";
		StringBuilder querySb = new StringBuilder("[");
		String comma = "";
	    for (SubscriptionRequest subs : allSubs) {
	    	querySb.append(comma);
	    	querySb.append("\"");
	    	querySb.append(subs.getHypervisorId());
	    	querySb.append(".value");
	    	querySb.append("\"");	    	
	    	comma = ",";
	    }
	    querySb.append("]");
	    String queryParams = querySb.toString();
	    
    	//1) build the query param to submit to DbmComponent
    	//2) get the most recent value of the subscribed points
    	//3) update the subscription requests in the tracker
    	//4) publish the results to the rtt
	    	
	    	
    	dbmURLStr = dbmURLStr
    			+ URLEncoder.encode(queryParams); 
//    	log.info("retrieving from url: " +olsURLStr);
		try {							
		    ObjectMapper mapper = new ObjectMapper();
		    URL dbmUrl = new URL(dbmURLStr);
		    log.debug("checking dbm url: " + dbmUrl);
		    JsonNode root = mapper.readTree(dbmUrl);
		    JsonNode response = root.get("response");
		    SubscriptionResponse subRes = mapper.readValue(response.toString(), SubscriptionResponse.class);
		    List<String> dbValueList = subRes.getDbvalue();
		    log.debug("allSubs size: " + allSubs.size() + ", allDbValues: " + dbValueList.size());
		    for (int i = 0; i < allSubs.size(); i++) {
		    	log.debug("checking refreshed values. callerId: " + allSubs.get(i).getCallerId() + ", sub id: " + allSubs.get(i).getSubscriptionId() + ", field: " + allSubs.get(i).getField() + ", hv id: " + allSubs.get(i).getHypervisorId() + ", value: " + dbValueList.get(i));
		    	RttEvent rtt = new RttEvent(
	    				publisher, 
	    				1, 
	    				allSubs.get(i).getSubscriptionId(), 
	    				allSubs.get(i).getField(), 
	    				allSubs.get(i).getHypervisorId(),
	    				String.valueOf(System.currentTimeMillis()),
	    				dbValueList.get(i),
	    				allSubs.get(i).getEnv()
    				);
		    	publisher.publishWsMessage(rtt);
		    }

		} catch (Exception e) {
			log.error("Exception refresh data: ", e);
		}

    }		
}