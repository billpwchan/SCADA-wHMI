package com.thalesgroup.scadasoft.myba;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.log4j.MDC;
import org.apache.log4j.xml.DOMConfigurator;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.scadagen.bps.BPSException;
import com.thalesgroup.scadagen.bps.SCADAgenBPS;
import com.thalesgroup.scadasoft.hvconnector.BAStateManager;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.services.proxy.ScsConnectorProxy;

public class Main {

    private static final Logger s_logger = LoggerFactory.getLogger(Main.class);

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
    public static final String BASE_URI = "http://localhost:8899/scs/";

    public static HttpServer startRestServer() {

        // create a Jersey resource config that scans for JAX-RS resources and providers
        // in com.thalesgroup package
    	final ResourceConfig rc = new ResourceConfig().packages("com.thalesgroup");
    	rc.register(LoggingFeature.class);
    	
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        HttpServer srv = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

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
        RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
        MDC.put("PID", rt.getName());
        s_logger.info("");
        s_logger.info("==== Start SCADAgen Hypervisor Connector  ====");
        s_logger.info("");

        enableLog4jWatchdog();

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

        String serverName = System.getProperty("scadasoft.servername", "SCSConnector");
        String physicalEnv = System.getProperty("scadasoft.physicalenv", physicalEnvDefault);
        s_logger.info("connects to Scadasoft env {} as {}", physicalEnv, serverName);

        BAStateManager smgr = BAStateManager.instance();

        ScsConnectorProxy.instance().initialize(physicalEnv, serverName, smgr);
        if (!ScsConnectorProxy.instance().isInitialized()) {
            s_logger.error("SCADAgen BA  - cannot initialize SCADAsoft: {}@{}", serverName, physicalEnv);
            System.exit(1);
        }

        // load scadasoft configuration
        SCSConfManager.instance().loadConfiguration();

        // then init hypervisor Generic Connector
        s_logger.info("init hypervisor connector");
        MySCADAsoftBA scadasoftBA = null;
        try {
            scadasoftBA = new MySCADAsoftBA();
            scadasoftBA.init();
            scadasoftBA.start();
        } catch (final Exception e) {
            s_logger.error("SCADAgen BA  - Error at instantiation: ", e);
            ScsConnectorProxy.instance().stop();
            System.exit(2);
        }

        // start Jersey server
        HttpServer restServer = startRestServer();
        
        // start SCADAgen BPS (Business Process Service)
        s_logger.info("init SCADAgen BPS");       
        SCADAgenBPS bps = new SCADAgenBPS(scadasoftBA.getConnector());

		try {
			bps.init();

			bps.start();
		} catch (BPSException e) {
			s_logger.error("SCADAgen BA  - Error staring BPS: ", e);
			scadasoftBA.stop();
            ScsConnectorProxy.instance().stop();
            System.exit(2);
		}
        
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
}
