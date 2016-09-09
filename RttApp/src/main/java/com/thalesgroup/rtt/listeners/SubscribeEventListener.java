package com.thalesgroup.rtt.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		log.info("Session: " + headerAccessor.getSessionId() + " subscribe to: " + headerAccessor.getDestination());
	}
	
}