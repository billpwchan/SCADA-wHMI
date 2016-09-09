package com.thalesgroup.rtt.tasks;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thalesgroup.rtt.beans.RttEvent;
import com.thalesgroup.rtt.registry.SubscriptionTracker;
import com.thalesgroup.rtt.transports.RttMessagePublisher;

@Component
public class ScheduledTask {
	
	@Autowired
	private SubscriptionTracker tracker;
	
	@Autowired
	private MessageSendingOperations<String> messagingTemplate;
	
	@Autowired
	private RttMessagePublisher publisher;

	@Scheduled(fixedRate = 60000)
	private void checkExpired() {
		tracker.removeExpiredSubscriptions();
	}
	

}