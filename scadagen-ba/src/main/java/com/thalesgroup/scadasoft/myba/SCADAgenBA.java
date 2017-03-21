package com.thalesgroup.scadasoft.myba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.configuration.NotificationEntityBuilder;
import com.thalesgroup.hv.core.tools.communication.notification.QueryTools;
import com.thalesgroup.hv.core.tools.communication.notification.WSNotificationTools;
import com.thalesgroup.hv.data_v1.connection.ConnectionType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.histo.operation.HistorizeResponse;
import com.thalesgroup.hv.data_v1.notification.ElementModificationType;
import com.thalesgroup.hv.data_v1.notification.EntityNotificationElementType;
import com.thalesgroup.hv.data_v1.notification.list.NotificationList;
import com.thalesgroup.hv.data_v1.operation.ResponseStatusTypeEnum;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.hv.sdk.connector.api.notification.producer.SubscriptionStopReason;
import com.thalesgroup.hv.subscriptionmanager.core.exception.TopicDomQueryException;
import com.thalesgroup.hv.subscriptionmanager.core.exception.TopicExpressionException;
import com.thalesgroup.hv.subscriptionmanager.core.xpath.QueryCoreTools;
import com.thalesgroup.hv.subscriptionmanager.plugin.plugin.PluginNBConnection;
import com.thalesgroup.hv.subscriptionmanager.plugin.xpath.QueryConnectionTools;
import com.thalesgroup.hv.ws.dialect.entity_content_v1.QueryList;
import com.thalesgroup.hv.ws.dialect.entity_content_v1.QueryType;
import com.thalesgroup.hv.ws.notification_v1.xsd.FilterType;
import com.thalesgroup.scadasoft.common.binding.builder.AttributeBuilder;
import com.thalesgroup.scadasoft.common.binding.engine.BindingEngine;
import com.thalesgroup.scadasoft.common.binding.engine.BindingLoader;
import com.thalesgroup.scadasoft.hvconnector.SCADAsoftBA;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager.OLSConfiguration;
import com.thalesgroup.scadasoft.hvconnector.redundancy.RedundancyManager;
import com.thalesgroup.scadasoft.hvconnector.subscription.DBMSubscription;
import com.thalesgroup.scadasoft.hvconnector.subscription.OLSConverter;
import com.thalesgroup.scadasoft.hvconnector.subscription.OLSSubscription;
import com.thalesgroup.scadasoft.hvconnector.util.FilterUtil;
import com.thalesgroup.scadasoft.jsoncomponent.JSComponentMgr;
import com.thalesgroup.scadasoft.myba.operation.SCADAgenOperationResponder;
import com.thalesgroup.scadasoft.myba.subscription.MyDBMSubscription;
import com.thalesgroup.scadasoft.myba.subscription.OLSAlmConverter;
import com.thalesgroup.scadasoft.myba.subscription.OLSHistEventConverter;
import com.thalesgroup.scadasoft.services.proxy.ScsPollerServicesProxy;

public class SCADAgenBA extends SCADAsoftBA {
	
	/** Logger **/
    private static final Logger s_logger = LoggerFactory.getLogger(SCADAgenBA.class);
	
	

	/** The instance of the Hypervisor generic connector **/
    protected Connector m_HVGenericConnector = null;
    /** The unique identifier of the sub-system **/
    protected String m_serviceOwnerID;
    /** Manage HV redundancy based on terracotta cluster **/
    protected RedundancyManager m_redundancyManager = null;

    protected Map<UUID, DBMSubscription> m_activeDbmSubcriptions = new HashMap<UUID, DBMSubscription>();
    protected OLSSubscription m_alarmSubscription = null;
    protected OLSSubscription m_eventSubscription = null;
    
    protected List<OLSSubscription> m_olsSubList = new ArrayList<OLSSubscription>();
    protected List<UUID> m_subUUIDList = null;

    protected UUID m_histoStateSubID = null;
    protected volatile boolean m_isHistorianUP = false;
    
    protected SCADAgenOperationResponder m_opeResponder = null;
    
    protected BindingEngine m_engine;

    public SCADAgenBA() {
    }

    @Override
    public void init() throws HypervisorException {
        //super.init();
        // do other necessary init
    	if (m_HVGenericConnector != null) {
            s_logger.warn("SCADAgen BA - genericConnector already initialized");
            return;
        }

        // init Hypervisor libraries
        m_HVGenericConnector = new Connector("BAConnectorConfiguration.xml", "BASystemConfiguration",
                "BASystemConfiguration", false);
        m_HVGenericConnector.getConnectorConfiguration().setEnableWSValidation(false);

        s_logger.info("SCADAsoft BA - genericConnector created");

        // load mapping file between HV id and SCADAsoft id
        SCSConfManager.instance().setConnector(m_HVGenericConnector);
        m_serviceOwnerID = m_HVGenericConnector.getSystemConfiguration().getCurrentInstanceName();

        // load bindings
        final BindingLoader loader = new BindingLoader(getConnector()) {
            @Override
            protected String resolveBinding(String entityIdentifier) {
                return SCSConfManager.instance().getHVTypeFromHVID(entityIdentifier);
            }
        };
        m_engine = null;
        try {
	        loader.readBinding("scs_bindings.xml");
	        final NotificationEntityBuilder builder = new NotificationEntityBuilder(getConnector().getSystemConfiguration(), getConnector().getDataHelper());
	        m_engine = new BindingEngine(builder, loader, getConnector().getDataHelper(), new AttributeBuilder(getConnector().getDataHelper()));
        } catch (HypervisorConversionException e) {
        	s_logger.warn("SCADAsoft BA - no bindings defined: use scs2hvmodel.xml only");
        }
        
        // init redundancy manager
        m_redundancyManager = new RedundancyManager(m_serviceOwnerID, this);
        m_redundancyManager.init();

        // Overwrite SCSOperationResponder to insert CmdExecutor for SCADAgen operation
        m_opeResponder = new SCADAgenOperationResponder();

        m_HVGenericConnector.setOnOperationRequestCallback(m_opeResponder);

        // Set the callback (java objects) called on subscription reception
        m_HVGenericConnector.setOnSubscriptionCallback(this);

        // Set the callback (java objects) called on historization response
        // reception
        setHistorizationResponseCallback();

        // callback to manage CMS notification to monitor HistoryService state
        m_HVGenericConnector.setOnNotificationListCallback(ConnectionType.class, this);
    }
    
    public synchronized boolean isHistorianUP() {
        return m_isHistorianUP;
    }

    public synchronized Connector getConnector() {
        return m_HVGenericConnector;
    }

    public RedundancyManager getRedManager() {
        return m_redundancyManager;
    }

    public synchronized SCADAgenOperationResponder getOperationManager() {
        return m_opeResponder;
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
    public synchronized void start() throws Exception {
        // start thread pool of operation responder
        if (m_opeResponder != null) {
            m_opeResponder.start();
        }

        // start automatic subscription for master
        if (m_redundancyManager.isMaster()) {
            startMaster();
        }

        // delay real startup
        m_HVGenericConnector.start();
        
        // create query to CMS
        final QueryType query = QueryConnectionTools.createQueryConnectionType(null);
        final QueryList queryList = QueryTools.createQueryList(Arrays.asList(query));
        final FilterType filter = WSNotificationTools.createFilterFromQueryList(queryList,
                PluginNBConnection.TOPIC_EXPRESSION_CONNECTION);

        m_histoStateSubID = m_HVGenericConnector.startSubscription(filter);
    }
    
    @Override
    public synchronized void stop() {
    	if (m_histoStateSubID != null) {
            try {
                m_HVGenericConnector.stopSubscription(m_histoStateSubID);
            } catch (HypervisorException e) {
                s_logger.error("SCADAsoft BA  - cannot histo state stop subscriptions: ", e);
            }
            m_histoStateSubID = null;
        }

        if (m_subUUIDList != null) {
            for (UUID sub : m_subUUIDList) {
                try {
                    m_HVGenericConnector.stopSubscription(sub);
                } catch (HypervisorException e) {
                    s_logger.error("SCADAsoft BA  - cannot stop subscription: ", e);
                }
            }
            m_subUUIDList = null;
        }

        if (m_alarmSubscription != null) {
            m_alarmSubscription.stop();
            m_alarmSubscription = null;
        }

        if (m_eventSubscription != null) {
            m_eventSubscription.stop();
            m_eventSubscription = null;
        }

        for (OLSSubscription sub : m_olsSubList) {
            sub.stop();
        }
        m_olsSubList.clear();

        for (DBMSubscription dbmsub : m_activeDbmSubcriptions.values()) {
            dbmsub.stop();
        }
        m_activeDbmSubcriptions.clear();

        if (m_opeResponder != null) {
            m_opeResponder.stop();
            m_opeResponder = null;
        }

        if (m_HVGenericConnector != null) {
            m_HVGenericConnector.stop();
            m_HVGenericConnector = null;
        }
    }
    
    @Override
    synchronized public void onStartSubscriptionReception(UUID subscriptionID, FilterType filter) {
        // Analyze the list of queries provided in parameter
        QueryList queryList = null;
        String topicExpression = null;
        try {
            queryList = QueryCoreTools.getQueryList(filter);
            topicExpression = QueryCoreTools.getTopicExpressionFromFilter(filter);
        } catch (TopicDomQueryException e) {
            s_logger.error("SCADAsoft BA onStartSubscriptionReception - cannot get querylist: {}" + e.toString(), e);
            return;
        } catch (TopicExpressionException e) {
            s_logger.error("SCADAsoft BA onStartSubscriptionReception - cannot get topic: {}" + e.toString(), e);
            return;
        }

        // Display query
        if (s_logger.isDebugEnabled()) {
            String sqlQuery = FilterUtil.displayQuery(queryList);
            s_logger.trace("SCADAsoft BA onStartSubscriptionReception - query({}): {}", topicExpression, sqlQuery);
        }

        // manage equipment query (ignore other)
        if ("hv-t:Eqpt//.".equals(topicExpression)) {
            /*
             * Typical eqp query SELECT [dciSECOPENSTATUSScadaSpec] FROM
             * [Equipment] WHERE [ [id] IN [door_3063] AND [type] EQUALS
             * [SecType] ]
             * 
             */
            startEquipmentSubscription(subscriptionID, queryList);
        }
    }

    synchronized public void startEquipmentSubscription(UUID subscriptionID, QueryList queryList) {
        ScsPollerServicesProxy dbpoller = SCSConfManager.instance().getDbmPoller();
        if (dbpoller != null) {
            DBMSubscription dbmsub = getDbmSubscription(dbpoller, subscriptionID);

            if (dbmsub != null) {
                // each query match one type of equipment
                for (final QueryType query : queryList.getQuery()) {
                    final String eqpType = FilterUtil.getEqpType(query);
                    final List<String> eqpInstances = FilterUtil.getEqpInstances(query);
                    final List<String> eqpFields = FilterUtil.getEqpFields(query);
                    s_logger.trace("SCADAsoft BA - eqp subscription type '{}' fields '{}' instances '{}'",
                            new Object[] { eqpType, eqpFields.toString(), eqpInstances.toString() });

                    dbmsub.fillSubGroup(eqpInstances, eqpFields);
                }
                s_logger.debug("SCADAsoft BA - start sub '{}'", subscriptionID.toString());
                dbmsub.startSubscription();
                m_activeDbmSubcriptions.put(subscriptionID, dbmsub);

                m_redundancyManager.addEquipementSubscription(subscriptionID, queryList);
            }
        }
    }

    @Override
    synchronized public void onStopSubscriptionReception(UUID subscriptionID, SubscriptionStopReason reason) {
        s_logger.debug("SCADAsoft BA - stop sub ({}) '{}' ", reason.toString(), subscriptionID.toString());
        DBMSubscription dbmsub = m_activeDbmSubcriptions.remove(subscriptionID);
        if (dbmsub != null) {
            dbmsub.stop();
        }
        m_redundancyManager.removeEquipementSubscription(subscriptionID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onHistorizeDataResponseReception(final UUID correlationID, final HistorizeResponse response) {
        final ResponseStatusTypeEnum status = response.getStatus();
        if (status != ResponseStatusTypeEnum.SUCCESS) {
            s_logger.warn("SCADAsoft BA {} - HistorizeData error status={}", m_serviceOwnerID, status);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            if (m_isHistorianUP) {
                m_redundancyManager.retryWaitingRequest(correlationID);
            }
        } else {
            m_redundancyManager.removeWaitingRequest(correlationID);
        }
    }

    private void setHistorizationResponseCallback() {

        try {
            m_HVGenericConnector.setOnHistorizeDataResponseCallback(this);
        } catch (final HypervisorException e) {
            s_logger.error(e.getMessage(), e);
        }
    }

    synchronized public void displayInfo() {
        s_logger.info("SCADAsoft BA:  INFO ====================================");
        if (m_redundancyManager.isMaster()) {
            s_logger.info("== BA is master");
        } else {
            s_logger.info("== BA is secondary");
        }

        if (m_alarmSubscription != null) {
            s_logger.info("== Subscription for alarms");
            m_alarmSubscription.displayInfo();
        }

        if (m_eventSubscription != null) {
            s_logger.info("== Subscription for events");
            m_eventSubscription.displayInfo();
        }

        for (OLSSubscription sub : m_olsSubList) {
            sub.displayInfo();
        }

        if (m_activeDbmSubcriptions.size() == 0) {
            s_logger.info("== No subscription to dbmpoller");
        } else {
            s_logger.info("== Subscription to dbmpoller");
            for (DBMSubscription dbmsub : m_activeDbmSubcriptions.values()) {
                dbmsub.displayInfo();
            }
        }
        s_logger.info("SCADAsoft BA:  INFO ====================================");
    }

    synchronized public void getInfo(ObjectNode node) {
        node.put("name", m_HVGenericConnector.getSystemConfiguration().getCurrentInstanceName());

        if (m_redundancyManager.isMaster()) {
            node.put("mode", "master");
        } else {
            node.put("mode", "secondary");
        }

        if (m_isHistorianUP) {
            node.put("HisState", "UP");
        } else {
            node.put("HisState", "DOWN");
        }

        ArrayNode olsnode = JSComponentMgr.s_json_factory.arrayNode();

        if (m_alarmSubscription != null) {
            m_alarmSubscription.getInfo(olsnode);
        }

        if (m_eventSubscription != null) {
            m_eventSubscription.getInfo(olsnode);
        }

        for (OLSSubscription sub : m_olsSubList) {
            sub.getInfo(olsnode);
        }
        node.set("ols", olsnode);

        ArrayNode pollingnode = JSComponentMgr.s_json_factory.arrayNode();

        if (m_activeDbmSubcriptions.size() > 0) {
            for (DBMSubscription dbmsub : m_activeDbmSubcriptions.values()) {
                dbmsub.getInfo(pollingnode);
            }
        }
        node.set("dbmgroup", pollingnode);
    }

    @Override
    synchronized public void onNotificationListReception(UUID subscriptionID, NotificationList notificationList) {

        for (final EntityNotificationElementType notifElement : notificationList.getEntityNotificationElement()) {
            final AbstractEntityStatusesType entity = notifElement.getEntity();
            if (entity instanceof ConnectionType) {
                ConnectionType connection = (ConnectionType) entity;
                String serverid = connection.getServiceOwnerID().getValue();
                String clientid = connection.getMonitoredService().getValue();
                int currentAlive = connection.getNbAliveNodesCurrent().getValue();
                int maxAlive = connection.getNbAliveNodesMaximum().getValue();
                String newState = connection.getConnectionState().getValue();
                s_logger.debug("== On CMS Notif receive from {} client {} is {} {}/{}",
                        new Object[] { serverid, clientid, newState, currentAlive, maxAlive });

                // Each notification element can be an inert, an update or a
                // deletion of an entity
                if (notifElement.getModificationType() != ElementModificationType.DELETE) {
                    if (SCSConfManager.instance().getHVHistorian().equals(clientid)) {
                        boolean prevState = m_isHistorianUP;
                        if (currentAlive > 0) {
                            m_isHistorianUP = true;
                        } else {
                            m_isHistorianUP = false;
                        }

                        // change from DOWN to UP
                        if (!prevState && m_isHistorianUP) {
                            m_redundancyManager.flushHistoryRequest(m_HVGenericConnector);
                        }

                        // change from UP to DOWN
                        if (prevState && !m_isHistorianUP) {
                            m_redundancyManager.savePendingHistoRequest();
                        }
                    }

                } else {
                    s_logger.warn("== On CMS Notif receiving a delete is no expected for client {}",
                            connection.getMonitoredService().getValue());
                }
            }

        }
    }

	public BindingEngine getBindingsEngine() {
		return m_engine;
	}

	@Override
	public DBMSubscription getDbmSubscription(ScsPollerServicesProxy dbpoller, UUID subscriptionID) {
		return new MyDBMSubscription(dbpoller, subscriptionID, this);
	}

}
