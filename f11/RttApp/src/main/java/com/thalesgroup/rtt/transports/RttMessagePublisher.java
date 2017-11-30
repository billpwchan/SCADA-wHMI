package com.thalesgroup.rtt.transports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.thalesgroup.rtt.beans.RttEvent;

@Component
@Async
public class RttMessagePublisher {
	
	private final ApplicationEventPublisher publisher;
	
	@Autowired
	public RttMessagePublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}
	
	public void publishWsMessage(RttEvent rttevent) {
		this.publisher.publishEvent(rttevent);
	}
}