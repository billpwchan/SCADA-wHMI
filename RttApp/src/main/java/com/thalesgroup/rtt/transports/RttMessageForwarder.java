package com.thalesgroup.rtt.transports;

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
    	String env = confManager.getSubsystem(event.getHvId());
    	String link = destinationPrefix + env + destinationPrefix + event.getSubId();
    	subscriptionTracker.modifyLastUpdate(link, event.getTime(), event.getValue());
        messagingTemplate.convertAndSend(link, event);
    }
    
    
}