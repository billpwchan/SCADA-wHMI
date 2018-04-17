package com.thalesgroup.scadasoft.opmmgt;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.boot.ApplicationPid;


/**
 * Entry point for springboot application.
 *
 */
@SpringBootApplication
@EnableSwagger2
public class OpmMgtApplication {

    /**
     * Class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OpmMgtApplication.class);

    /**
     * Save pid in a file so that external tools can monitor/stop application.
     *
     */
    @PostConstruct
    public void handlePid() {
        final File file = new File("application.pid");
        try {
            new ApplicationPid().write(file);
        } catch (IOException e) {
            LOGGER.error("Cannot write process PID in 'application.pid' file: {}", e.getMessage());
        }
        file.deleteOnExit();
    }

    /**
     * Start spring app.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(OpmMgtApplication.class, args);
    }
    
    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("opm")
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.regex("/opm.*"))
                .build();
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Profile Management Backend")
                .description("REST backend for Operator Profile Management")               
                .contact("JC Menchi")
                .license("Thales")
                .version("1.0")
                .build();
    }
}
