package com.thalesgroup.rtt.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class DisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		log.info("Disconnect detected from socket: " + headerAccessor.getSessionId());
		//tracker.unregisterSubscriber(headerAccessor.getSessionId());
	}
	
}