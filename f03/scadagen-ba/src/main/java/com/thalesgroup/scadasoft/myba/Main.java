package com.thalesgroup.scadasoft.myba;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.MDC;
import org.apache.log4j.xml.DOMConfigurator;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.scadagen.bps.BPSException;
import com.thalesgroup.scadagen.bps.SCADAgenBPS;
import com.thalesgroup.scadagen.scadagenba.services.proxy.ScadagenConnectorProxy;
import com.thalesgroup.scadasoft.hvconnector.BAStateManager;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.services.proxy.ScsConnectorProxy;

public class Main {

    private static final Logger s_logger = LoggerFactory.getLogger(Main.class);

    private static Properties s_appConfig = new Properties();
    
    @Provider
    public static class CORSFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext request,
                ContainerResponseContext response) throws IOException {
            response.getHeaders().add("Access-Control-Allow-Origin", "*");
            response.getHeaders().add("Access-Control-Allow-Headers",
                    "origin, content-type, accept, authorization");
            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        }
    }
    
    /**
     * Enable Log4j Watch-dog
     */
    private static void enableLog4jWatchdog() {
        // Add a watcher on the log4j configuration file : the file will be
        // reloaded if any change occurs
        // Specify the check period of the watcher
        // If the file does not respect the DTD, it will not be reloaded
        try {
            final String filename = "log4j.xml";
            URL url = ClassLoader.getSystemClassLoader().getResource(filename);
            if (url != null) {
                final int delay = 10000; // every 10 s
                DOMConfigurator.configureAndWatch(url.getPath(), delay);
                s_logger.info("Log4j-Watchdog successfully setup for URL [{}] with delay [{}]",
                        new Object[] { url, delay });

            } else {
                s_logger.error("Log4j-Watchdog - Cannot load filename : " + filename);
            }
        } catch (final Exception e) {
            s_logger.error(e.getMessage(), e);
        }
    }
    
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8899/";

    public static HttpServer startRestServer() {

        // create a Jersey resource config that scans for JAX-RS resources and providers
        // in com.thalesgroup package
    	final ResourceConfig rc = new ResourceConfig().packages("com.thalesgroup");
    	rc.register(LoggingFeature.class);
    	rc.register(new CORSFilter());
    	
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
    	String rootURI = BASE_URI;
    	if (s_appConfig != null) {
    		String host = s_appConfig.getProperty("restapi.hostname", "0.0.0.0").trim();
    		String port = s_appConfig.getProperty("restapi.port", "8899").trim();
    		rootURI = "http://" + host + ":" + port + "/";
    	}
        HttpServer srv = GrizzlyHttpServerFactory.createHttpServer(URI.create(rootURI), rc);

        s_logger.info("Jersey app started with WADL available at {}application.wadl", BASE_URI);
        return srv;
    }

    /**
     * Entry point of the program
     * 
     * @param args
     *            the list of arguments of the program (not used)
     *
     */
    public static void main(final String[] args) {
    	
    	// init log4j
        RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
        MDC.put("PID", rt.getName());
        s_logger.info("");
        s_logger.info("==== Start SCADAgen Hypervisor Connector  ====");
        s_logger.info("");

        enableLog4jWatchdog();

        // load custom properties
        InputStream pstream = ClassLoader.getSystemClassLoader().getResourceAsStream("myba.properties");
        if (pstream != null) {
        	try {
				s_appConfig.load(pstream);
			} catch (IOException e) {
				s_logger.warn("Cannot read 'myba.properties' property file", e);
			}
        }
        
        // First initialize SCADAsoft environment
        // calculate default value
        String hostname = "localhost";
        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException e) {
        }
        String physicalEnvDefault;
        String scsenv = System.getenv("SCSENV");
        if (scsenv == null) {
            physicalEnvDefault = "SCDMWEB1" + "_" + hostname;
        } else {
            physicalEnvDefault = scsenv + "_" + hostname;
        }

        String serverName = System.getProperty("scadasoft.servername", "SCADAgen Connector");
        String physicalEnv = System.getProperty("scadasoft.physicalenv", physicalEnvDefault);
        s_logger.info("connects to Scadasoft env {} as {}", physicalEnv, serverName);

        BAStateManager smgr = BAStateManager.instance();

        ScsConnectorProxy.instance().initialize(physicalEnv, serverName, smgr);
        if (!ScsConnectorProxy.instance().isInitialized()) {
            s_logger.error("SCADAgen BA  - cannot initialize SCADAsoft: {}@{}", serverName, physicalEnv);
            System.exit(1);
        }
        
        ScadagenConnectorProxy proxy = ScadagenConnectorProxy.instance();
        if (proxy == null) {
        	s_logger.error("SCADAgen BA  - cannot initialize SCADAsoft: {}@{}", serverName, physicalEnv);
            System.exit(1);
        }
        proxy.initialize(physicalEnv, serverName, smgr);
        if (!ScadagenConnectorProxy.instance().isInitialized()) {
            s_logger.error("SCADAgen BA  - cannot initialize SCADAsoft: {}@{}", serverName, physicalEnv);
            System.exit(1);
        }
        s_logger.info("ScadagenConnectorProxy is initialized");

        // load scadasoft configuration
        SCSConfManager.instance().loadConfiguration();

        // then init hypervisor Generic Connector
        s_logger.info("init hypervisor connector");
        SCADAgenBA scadasoftBA = null;
        try {
            scadasoftBA = new SCADAgenBA();
            scadasoftBA.init();
            scadasoftBA.start();
        } catch (final Exception e) {
            s_logger.error("SCADAgen BA  - Error at instantiation: ", e);
            ScsConnectorProxy.instance().stop();
            System.exit(2);
        }

        // start Jersey server
        HttpServer restServer = startRestServer();
        
        // start SCADAgen BPS if configuration found
        startBPS(scadasoftBA.getConnector());
        
        // start main loop
        s_logger.info("start main loop");
        smgr.setBA(scadasoftBA);

        while (smgr.doRun()) {
            try {
                Thread.sleep(1000);
            } catch (final Exception e) {
                s_logger.error("SCADAgen BA  - Error at runtime: ", e);
            }
        }

        // stop system
        if (scadasoftBA != null) {
            if (!smgr.isEmergencyStop()) {
            	s_logger.info("Normal shutdown: Stop grizzly HTTP server");
            	restServer.shutdown();
                s_logger.info("Normal shutdown: Stop hypervisor connector for {}", serverName);
                scadasoftBA.stop();
                s_logger.info("Stop SCADAsoft main loop for {}", serverName);
                ScsConnectorProxy.instance().stop();
            } else {
                s_logger.info("Emergency shutdown: do not stop hypervisor connector for {}", serverName);
            }
        }

        s_logger.info("HV Connector is stopped");

        if (!smgr.isEmergencyStop()) {
            // try normal java exit
            System.exit(0);
        } else {
            // do a C _exit just in case something is stuck at the C level
            ScsConnectorProxy.instance().exit();
        }
    }
    
    private static void startBPS(Connector connector) {
    	
    	if (checkBPSConfig()) {

	        s_logger.info("init SCADAgen BPS");       
	        SCADAgenBPS bps = new SCADAgenBPS(connector);
	
			try {
				bps.init();
	
				bps.start();
			} catch (BPSException e) {
				s_logger.error("SCADAgen BA - Error staring BPS. Connector will start without BPS. ", e);
			}
    	} else {
    		s_logger.info("SCADAgen BA - No BPS configuration loaded. Connector will start without BPS.");
    	}
    }
    
    private static boolean checkBPSConfig() {
        
        try {
        	File bpsConfigFolder = new File("bpsConfig");
        
	        if (bpsConfigFolder != null && bpsConfigFolder.exists() && bpsConfigFolder.isDirectory()) {
	        	FilenameFilter filenameFilter = new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						if (name.length() > 4) {
							String ext = name.substring(name.length()-4);
							if (ext.compareToIgnoreCase(".xml") == 0) {
								return true;
							}
						}
						return false;
					}
	        	};
	        	
				String [] xmlFiles = bpsConfigFolder.list(filenameFilter);
				if (xmlFiles != null && xmlFiles.length > 0) {
					return true;
				}
				
	        }
        } catch (Exception e) {
        	s_logger.error("Error accessing files in folder bpsConfig. {}", e);
        }
        
        return false;
    }
}