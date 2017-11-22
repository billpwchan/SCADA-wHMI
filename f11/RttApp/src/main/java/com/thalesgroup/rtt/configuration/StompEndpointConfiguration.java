package com.thalesgroup.rtt.configuration;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


@Configuration
@EnableWebSocketMessageBroker
public class StompEndpointConfiguration implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/event").withSockJS()
			.setHttpMessageCacheSize(1024)
			.setInterceptors(new HttpSessionHandshakeInterceptor());
	}

	@Override
	public void configureWebSocketTransport(
			WebSocketTransportRegistration registry) {
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {	
	}

	@Override
	public void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
	}

	@Override
	public void addReturnValueHandlers(
			List<HandlerMethodReturnValueHandler> returnValueHandlers) {
	}

	@Override
	public boolean configureMessageConverters(
			List<MessageConverter> messageConverters) {
		return false;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
	}

}