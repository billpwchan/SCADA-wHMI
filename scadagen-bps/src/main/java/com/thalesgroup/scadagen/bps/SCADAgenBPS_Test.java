package com.thalesgroup.scadagen.bps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.scadagen.bps.SCADAgenBPS;
import com.thalesgroup.scadagen.bps.BPSException;

public class SCADAgenBPS_Test {
	private static final Logger LOGGER = LoggerFactory.getLogger(SCADAgenBPS_Test.class);
	
	public static void main(String[] args) {
		Connector connector = null;
		try {
			connector = new Connector("BAConnectorConfiguration.xml", "BASystemConfiguration", "BASystemConfiguration", true);
		} catch (HypervisorException e) {
			e.printStackTrace();
		}
		connector.getConnectorConfiguration().setEnableWSValidation(false);

		SCADAgenBPS bps = new SCADAgenBPS(connector);

		try {
			bps.init();

			bps.start();
		} catch (BPSException e) {
			LOGGER.error("SCADAgen BPS  - Error during init or start: ", e);
		}
		
		while (true) {
            try {
                Thread.sleep(1000);
            } catch (final Exception e) {
                LOGGER.error("SCADAgen BPS  - Error at runtime: ", e);
            }
        }
	}

}
