package com.thalesgroup.scadasoft.myba.subscription;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.scadasoft.hvconnector.SCADAsoftBA;
import com.thalesgroup.scadasoft.hvconnector.subscription.DBMSubscription;
import com.thalesgroup.scadasoft.services.proxy.ScsPollerServicesProxy;

public class MyDBMSubscription extends DBMSubscription {
    static private final Logger s_logger = LoggerFactory.getLogger(MyDBMSubscription.class);

    public MyDBMSubscription(ScsPollerServicesProxy p, UUID subscriptionID, SCADAsoftBA scsBA) {
        super(p, subscriptionID, scsBA);
        s_logger.info("==== Create MyDBMSubscription ====");
    }

}
