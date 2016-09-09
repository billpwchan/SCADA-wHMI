package com.thalesgroup.rtt.configuration;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.thalesgroup.rtt.ws.consumers.ConsumerServer;


@Configuration
public class ConsumerEndpointConfiguration {
	
	@Autowired
	private ApplicationContext ctx;
	
	@Bean
	public SpringBus cxf() {        
	    return new SpringBus();
	}
	
	@Bean
    public ConsumerServer consumerServer() {
       return new ConsumerServer();
    }
	
	@DependsOn("cxfServletRegistration")
	@Bean
	public EndpointImpl notificationServer(ConsumerServer consumerServer) {
		Bus bus = (Bus) ctx.getBean(Bus.DEFAULT_BUS_ID);
		EndpointImpl endpoint = new EndpointImpl(bus, consumerServer);
		endpoint.publish("/consumer");
		return endpoint;
	}

	@Bean
    public ServletRegistrationBean cxfServletRegistration() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new CXFServlet(), "/ws/*");
        servletRegistrationBean.setLoadOnStartup(1);
        return servletRegistrationBean;
    }
}