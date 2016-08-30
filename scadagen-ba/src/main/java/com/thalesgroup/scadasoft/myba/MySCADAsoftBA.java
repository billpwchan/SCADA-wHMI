package com.thalesgroup.scadasoft.myba;

import java.util.UUID;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.scadasoft.hvconnector.SCADAsoftBA;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager.OLSConfiguration;
import com.thalesgroup.scadasoft.hvconnector.subscription.DBMSubscription;
import com.thalesgroup.scadasoft.hvconnector.subscription.OLSConverter;
import com.thalesgroup.scadasoft.hvconnector.subscription.OLSSubscription;
import com.thalesgroup.scadasoft.myba.subscription.MyDBMSubscription;
import com.thalesgroup.scadasoft.myba.subscription.OLSAlmConverter;
import com.thalesgroup.scadasoft.myba.subscription.OLSHistEventConverter;
import com.thalesgroup.scadasoft.services.proxy.ScsPollerServicesProxy;

public class MySCADAsoftBA extends SCADAsoftBA {

    public MySCADAsoftBA() {
    }

    @Override
    public void init() throws HypervisorException {
        super.init();
        // do other necessary init
    }

    @Override
    public void startMaster() {

        // get SCS alarm
        if (SCSConfManager.instance().getAlmServer() != null) {
            m_alarmSubscription = new OLSSubscription(SCSConfManager.instance().getAlmServer(),
                    SCSConfManager.instance().getAlmListName(), "", new OLSAlmConverter(m_HVGenericConnector));
            m_alarmSubscription.start();
        }

        // get SCS event for Historization
        if (SCSConfManager.instance().getEvtServer() != null) {
            m_eventSubscription = new OLSSubscription(SCSConfManager.instance().getEvtServer(),
                    SCSConfManager.instance().getEvtListName(), "", new OLSHistEventConverter(this, m_HVGenericConnector));
            m_eventSubscription.start();
        }

        // start all OLS type subscription
        for (OLSConfiguration cnf : SCSConfManager.instance().getOLSConfigurations()) {
        	
            OLSSubscription sub = null;
            if ("EventList".equals(cnf.m_listName)) {
            	sub = new OLSSubscription(SCSConfManager.instance().getRemoteEnv(), cnf.m_serverName,
	                    cnf.m_listName, cnf.m_filter, new OLSAlmConverter(cnf.m_hvclass, m_HVGenericConnector));
            } else {
	            sub = new OLSSubscription(SCSConfManager.instance().getRemoteEnv(), cnf.m_serverName,
	                    cnf.m_listName, cnf.m_filter, new OLSConverter(cnf.m_hvclass, m_HVGenericConnector));
            }
            
            sub.start();
            m_olsSubList.add(sub);
        }
    }

    @Override
    public DBMSubscription getDbmSubscription(ScsPollerServicesProxy dbpoller, UUID subscriptionID) {
        return new MyDBMSubscription(dbpoller, subscriptionID, this);
    }

}