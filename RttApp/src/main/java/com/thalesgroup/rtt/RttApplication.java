package com.thalesgroup.rtt;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
//public class RttApplication {
public class RttApplication extends SpringBootServletInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RttApplication.class);
    }
	
    public static void main(String[] args) throws Exception {
    	ConfigurableApplicationContext ctx = SpringApplication.run(RttApplication.class, args);
    }
    

}