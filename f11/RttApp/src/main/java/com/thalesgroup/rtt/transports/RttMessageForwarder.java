package com.thalesgroup.rtt.transports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Component;

import com.thalesgroup.rtt.beans.RttEvent;
import com.thalesgroup.rtt.beans.SubscriptionRequest;
import com.thalesgroup.rtt.registry.ConnectorConfManager;
import com.thalesgroup.rtt.registry.SubscriptionTracker;
import com.thalesgroup.rtt.tools.RttUtil;

@Component
public class RttMessageForwarder {
	
    private final MessageSendingOperations<String> messagingTemplate;
    
    private final ConnectorConfManager confManager;
    
    private final SubscriptionTracker subscriptionTracker;
    
    private static final String destinationPrefix = "/";
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    public RttMessageForwarder(final MessageSendingOperations<String> messagingTemplate,
    		final ConnectorConfManager confManager,
    		final SubscriptionTracker subscriptionTracker) {
        this.messagingTemplate = messagingTemplate;
        this.confManager = confManager;
        this.subscriptionTracker = subscriptionTracker;
    }
	
    @EventListener
	public void handleRttData(RttEvent event) {
//    	String env = confManager.getSubsystem(event.getHvId());
    	String env = event.getEnv();
    	String link = destinationPrefix + env + destinationPrefix + event.getSubId();
    	String value = String.valueOf((Double.parseDouble(event.getValue())));
    	log.debug("checking link: " + link);
    	log.debug("RTTEvent. hvid: " + event.getHvId() + ", time" + event.getTime() + ", timestamp:" + event.getTimestamp() + ", value: " + event.getValue()); 
    	subscriptionTracker.modifyLastUpdate(link, event.getTime(), value);
    	event.setValue(value);
        messagingTemplate.convertAndSend(link, event);
    }
    
    
}