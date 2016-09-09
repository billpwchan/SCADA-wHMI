package com.thalesgroup.rtt.controllers;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thalesgroup.hv.ws.notification_v1.ResourceUnknownFault;
import com.thalesgroup.hv.ws.notification_v1.UnableToDestroySubscriptionFault;
import com.thalesgroup.hv.ws.notification_v1.UnableToGetSubscriptionStatusFault;
import com.thalesgroup.hv.ws.notification_v1.UnacceptableTerminationTimeFault;
import com.thalesgroup.hv.ws.notification_v1.xsd.GetSubscriptionStatusResponse;
import com.thalesgroup.hv.ws.notification_v1.xsd.RenewResponse;
import com.thalesgroup.hv.ws.notification_v1.xsd.SubscribeResponse;
import com.thalesgroup.hv.ws.notification_v1.xsd.UnsubscribeResponse;
import com.thalesgroup.rtt.beans.RttEvent;
import com.thalesgroup.rtt.beans.SubscriptionRequest;
import com.thalesgroup.rtt.beans.SubscriptionStatusRequest;
import com.thalesgroup.rtt.registry.SubscriptionTracker;
import com.thalesgroup.rtt.tools.RttUtil;
import com.thalesgroup.rtt.transports.RttMessagePublisher;
import com.thalesgroup.rtt.ws.producers.WebServiceProducerConnector;

@Controller
public class RttController {
	
	@Autowired
	private WebServiceProducerConnector connector;
	
	@Autowired
	private RttMessagePublisher publisher;
	
	@Autowired
	private SubscriptionTracker tracker;
	
	@Value("${link.prefix}")
	private String urlPrefix;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
    @RequestMapping("/hvSubscribe")
    @ResponseBody
    public String subscribe(@RequestParam(value="callerId", required=true, defaultValue="") String callerId,
    						@RequestParam(value="xaxisLabel", required=true, defaultValue="") String xaxisLabel,
    						@RequestParam(value="yaxisLabel1", required=true, defaultValue="") String yaxisLabel1,
    						@RequestParam(value="yaxisLabel2", required=false, defaultValue="") String yaxisLabel2,
    						@RequestParam(value="width", required=true, defaultValue="") String width,
    						@RequestParam(value="height", required=true, defaultValue="") String height,
    						@RequestParam(value="subscriptionInfo1", required=true, defaultValue="") String subscriptionInfo1, 
    						@RequestParam(value="subscriptionInfo2", required=false, defaultValue="") String subscriptionInfo2,
    						@RequestParam(value="subscriptionInfo3", required=false, defaultValue="") String subscriptionInfo3,
    						@RequestParam(value="subscriptionInfo4", required=false, defaultValue="") String subscriptionInfo4,
    						@RequestParam(value="subscriptionInfo5", required=false, defaultValue="") String subscriptionInfo5,
    						@RequestParam(value="subscriptionInfo6", required=false, defaultValue="") String subscriptionInfo6,
    						@RequestParam(value="subscriptionInfo7", required=false, defaultValue="") String subscriptionInfo7,
    						@RequestParam(value="subscriptionInfo8", required=false, defaultValue="") String subscriptionInfo8) 
    {
    	Collection<SubscriptionRequest> subReqCol = RttUtil.createSubscriptions(subscriptionInfo1, 
    			subscriptionInfo2, subscriptionInfo3, subscriptionInfo4, subscriptionInfo5, subscriptionInfo6, 
    			subscriptionInfo7, subscriptionInfo8);
    	int i = 0;
    	if (subReqCol.size() > 0) {
    		Iterator<SubscriptionRequest> iter = subReqCol.iterator();
    		while (iter.hasNext()) {
		        try {
		        	SubscriptionRequest subscriptionRequest = iter.next();
		        	subscriptionRequest.setCallerId(callerId);
		        	subscriptionRequest.setXaxisLabel(new String(xaxisLabel.getBytes("ISO-8859-1"), "UTF-8"));
		        	subscriptionRequest.setYaxisLabel1(new String(yaxisLabel1.getBytes("ISO-8859-1"), "UTF-8"));
		        	subscriptionRequest.setChartWidth(width);
		        	subscriptionRequest.setChartHeight(height);
		        	subscriptionRequest.setOrder(i);
		        	if (!yaxisLabel2.isEmpty()) {
		        		subscriptionRequest.setYaxisLabel2(new String(yaxisLabel2.getBytes("ISO-8859-1"), "UTF-8"));
		        	}
					SubscribeResponse subscriptionResponse = connector.subscribe(subscriptionRequest);
					String subscriptionId = subscriptionResponse.getSubscriptionReference().getReferenceParameters().getSubscriptionID();
					Long terminationTime = RttUtil.toUnixTime(subscriptionResponse.getTerminationTime());
					if (subscriptionId != null) {
						subscriptionRequest.setSubscriptionId(subscriptionId);
						subscriptionRequest.setTerminationTime(terminationTime);
						log.info("Subscription made to connector: " + subscriptionId + " terminationTime: " + subscriptionResponse.getTerminationTime());
						tracker.addOrReplaceSubscriptionRequest(subscriptionRequest);
					}
					i++;
				} catch (Exception e) {
					log.error("Subscription error: " + e.getMessage());
					return null;
				}
    		}
    	}
        return urlPrefix + callerId;
    }
    
    
    @RequestMapping("/Chart")
    public String chart(@RequestParam(value="callerId") String callerId, Model model) 
    {
    	Collection<SubscriptionRequest> subCol = tracker.getSubscriptions(callerId);
    	List<SubscriptionRequest> subList = new ArrayList<SubscriptionRequest>(subCol);
    	Collections.sort(subList);
    	int i = 0;
    	String tempField = "";
    	boolean fieldMapped = false;
    	//Iterator<SubscriptionRequest> iter = subscriptions.iterator();
    	Iterator<SubscriptionRequest> iter = subList.iterator();
    	while (iter.hasNext()) {
    		SubscriptionRequest subReq = iter.next();
			if (i == 0) {
				model.addAttribute("xaxisLabel", subReq.getXaxisLabel());
				model.addAttribute("yaxisLabel1", subReq.getYaxisLabel1());
				model.addAttribute("chartWidth", subReq.getChartWidth());
				model.addAttribute("chartHeight", subReq.getChartHeight());
				if (subReq.getYaxisLabel2() != null) {
					model.addAttribute("yaxisLabel2", subReq.getYaxisLabel2());
				}
				tempField = subReq.getField();
				model.addAttribute("yaxisField1", tempField);
			} else {
				if (!fieldMapped) {
					if (!subReq.getField().equals(tempField)) {
						model.addAttribute("yaxisField2", subReq.getField());
						fieldMapped = true;
					}
				}
			}
			model.addAttribute("subscriptionLink" + i, subReq.getLink());
			model.addAttribute("env" + i, subReq.getEnv());
			model.addAttribute("subscriptionId" + i, subReq.getSubscriptionId());
			model.addAttribute("subField" + i, subReq.getField());
			i++;
		
		model.addAttribute("callerId", callerId);
    	}
    	return "rtt";
    }
    
    @RequestMapping("/hvUnsubscribe")
    @ResponseBody
    public String unsubscribe(@RequestParam(value="callerId", required=true) String callerId)
    {
    	Collection<SubscriptionRequest> subscriptions = tracker.getSubscriptions(callerId);
    	if (subscriptions != null) {
	    	Iterator<SubscriptionRequest> iter = subscriptions.iterator();
	    	while(iter.hasNext()) {
	    		SubscriptionRequest subReq = iter.next();
	    		String queryKey = subReq.getQueryKey();
	    		try {
					UnsubscribeResponse resp = connector.unsubscribe(subReq);
					if (resp != null) {
						iter.remove();
						tracker.removeSubscriptionLink(queryKey, callerId);
					}
				} catch (UnableToDestroySubscriptionFault e) {
					log.error(e.getMessage());
					return "FAIL";
				} catch (ResourceUnknownFault e) {
					log.error(e.getMessage());
					return "FAIL";
				}
	    	}
	    	return "SUCCESS";
    	} else {
    		return "FAIL";
    	}
    }
    
    @RequestMapping("/hvRenew")
    @ResponseBody
    public String renew(@RequestParam(value="callerId", required=true) String callerId)
    {
    	Collection<SubscriptionRequest> subscriptions = tracker.getSubscriptions(callerId);
    	if (!subscriptions.isEmpty()) {
	    	for (SubscriptionRequest subReq : subscriptions) {
	    		try {
					RenewResponse resp = connector.renew(subReq);
					subReq.setTerminationTime(RttUtil.toUnixTime(resp.getTerminationTime()));
					tracker.addOrReplaceSubscriptionRequest(subReq);
					
				} catch (UnacceptableTerminationTimeFault e) {
					log.error(e.getMessage());
					return "FAIL";
				} catch (ResourceUnknownFault e) {
					log.error(e.getMessage());
					return "FAIL";
				} catch (DatatypeConfigurationException e) {
					log.error(e.getMessage());
					return "FAIL";
				} catch (ParseException e) {
					log.error(e.getMessage());
					return "FAIL";
				}
	    	}
    	} else {
    		return "FAIL";
    	}
    	return "SUCCESS";
    }
    
    @RequestMapping("/hvGetSubscriptionStatus")
    @ResponseBody
    public String getSubscriptionStatus(@RequestParam(value="callerId", required=true) String callerId,
    									@RequestParam(value="env", required=true) String env,
    									@RequestParam(value="hvId", required=true) String hvId,
    									@RequestParam(value="field", required=true) String field)
    {
    	String subReqKey = RttUtil.buildQueryKey(env, hvId, field);
    	SubscriptionRequest subReq = tracker.getSubscription(callerId, subReqKey);
    	try {
    		if (subReq != null) {
    			GetSubscriptionStatusResponse resp = connector.getSubscriptionStatus(subReq.getEnv(), subReq.getSubscriptionId());
    			if (resp != null) {
    				return "ACTIVE";
    			}
    		} else {
    			return "FAIL";
    		}
		} catch (UnableToGetSubscriptionStatusFault e) {
			log.error(e.getMessage());
			return "FAIL";
		} catch (ResourceUnknownFault e) {
			log.error(e.getMessage());
			return "FAIL";
		}
    	return "FAIL";
    }
    
    @MessageMapping("/status")
    public void notifyConnector(SubscriptionStatusRequest subStatusReq) {
    	String link = "/" + subStatusReq.getEnv() + "/" + subStatusReq.getSubscriptionId();
    	SubscriptionRequest subReq = tracker.getSubscription(subStatusReq.getCallerId(), link);
    	if (subReq.getLastUpdateTime() != null && subReq.getLastUpdateValue() != null) {
    		RttEvent event = new RttEvent(publisher, 2, subReq.getSubscriptionId(), subReq.getField(), subReq.getHypervisorId(),
    				subReq.getLastUpdateTime(), subReq.getLastUpdateValue());
    		publisher.publishWsMessage(event);
    	}
    	try {
    		GetSubscriptionStatusResponse resp = connector.getSubscriptionStatus(subStatusReq.getEnv(), subStatusReq.getSubscriptionId());
		} catch (UnableToGetSubscriptionStatusFault e) {
			log.error(e.getMessage());
		} catch (ResourceUnknownFault e) {
			log.error("Subscription in the HV connector does not exist: " + e.getMessage());
		}
    } 
}