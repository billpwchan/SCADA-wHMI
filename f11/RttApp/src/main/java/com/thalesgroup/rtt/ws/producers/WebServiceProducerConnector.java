package com.thalesgroup.rtt.ws.producers;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thalesgroup.hv.core.tools.communication.notification.WSNotificationTools;
import com.thalesgroup.hv.ws.dialect.entity_content_v1.QueryList;
import com.thalesgroup.hv.ws.notification_v1.NotificationProducer;
import com.thalesgroup.hv.ws.notification_v1.ResourceUnknownFault;
import com.thalesgroup.hv.ws.notification_v1.UnableToDestroySubscriptionFault;
import com.thalesgroup.hv.ws.notification_v1.UnableToGetSubscriptionStatusFault;
import com.thalesgroup.hv.ws.notification_v1.UnacceptableTerminationTimeFault;
import com.thalesgroup.hv.ws.notification_v1.xsd.FilterType;
import com.thalesgroup.hv.ws.notification_v1.xsd.GetSubscriptionStatusResponse;
import com.thalesgroup.hv.ws.notification_v1.xsd.Renew;
import com.thalesgroup.hv.ws.notification_v1.xsd.RenewResponse;
import com.thalesgroup.hv.ws.notification_v1.xsd.Subscribe;
import com.thalesgroup.hv.ws.notification_v1.xsd.SubscribeResponse;
import com.thalesgroup.hv.ws.notification_v1.xsd.Unsubscribe;
import com.thalesgroup.hv.ws.notification_v1.xsd.UnsubscribeResponse;
import com.thalesgroup.rtt.beans.SubscriptionRequest;
import com.thalesgroup.rtt.registry.ConnectorAddrManager;
import com.thalesgroup.rtt.registry.SubscriptionTracker;
import com.thalesgroup.rtt.tools.QueryBuilder;
import com.thalesgroup.rtt.tools.RttUtil;
import com.thalesgroup.hv.ws.notification_v1.xsd.GetSubscriptionStatus;

@Component
public class WebServiceProducerConnector {
	
	
	private int subscribePeriod;
	
	private String consumerUrl;
	
	private final ProducerRegistry producerRegistry = new ProducerRegistry();
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ConnectorAddrManager addrManager;
	
	@Autowired
	private SubscriptionTracker subscriptionTracker;
	
	@Autowired
	public WebServiceProducerConnector(@Value("${subscribe.period}") int subscribePeriod,
									   @Value("${consumer.url}") String consumerUrl,
									   ConnectorAddrManager addrManager) 
	throws MalformedURLException {
		this.subscribePeriod = subscribePeriod;
		this.consumerUrl = consumerUrl;
		this.addrManager = addrManager;
		init();
	}
	
	private void init() {
		Set<Entry<String, String>> entry = addrManager.getAddrMappings();
		Iterator<Map.Entry<String, String>> iter = entry.iterator();
		while (iter.hasNext()) {
			Entry<String, String> mapping = iter.next();
			String env = mapping.getKey();
			String addr = mapping.getValue();
			NotificationProducer producer = initConnection(addr);
			if (producer != null) {
				producerRegistry.addProducer(env, producer);
			} else {
				log.error("Connection to env: " + env + " failed");
			}
		}
	}
	
	private NotificationProducer initConnection(String producerUrl) {

			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.setServiceClass(NotificationProducer.class);
			factory.setAddress(producerUrl);
			NotificationProducer producer = (NotificationProducer) factory.create();
			return producer;
	}
	
	public SubscribeResponse subscribe(SubscriptionRequest obj) 
	throws Exception 
	{
		QueryList queryList = new QueryList();
		Date tempDate = RttUtil.addMinutesToDate(subscribePeriod, new Date());
		
		XMLGregorianCalendar terminationTime = RttUtil.DateToXMLGregorianCalendar(tempDate);
		queryList = QueryBuilder.getInstance().buildQuery(obj.getField(), obj.getTopic(), obj.getType(), obj.getHypervisorId());
		
		FilterType filterType = WSNotificationTools.createFilterFromQueryList(queryList, obj.getTopicExpression());
					
		Subscribe subscribeRequest = WSNotificationTools.createSubscribe(consumerUrl, filterType, terminationTime);
		
		NotificationProducer producer = producerRegistry.getProducer(obj.getEnv());
		if (producer != null) {
			return producer.subscribe(subscribeRequest);
		}
		
		return null;
	}
	
	public UnsubscribeResponse unsubscribe(SubscriptionRequest subReq) 
	throws UnableToDestroySubscriptionFault, ResourceUnknownFault 
	{
		NotificationProducer producer = producerRegistry.getProducer(subReq.getEnv());
		if (producer != null) {
			Unsubscribe unsubscribe = WSNotificationTools.createUnsubscribe(subReq.getSubscriptionId());
			return producer.unsubscribe(unsubscribe);
		}
		
		return null;
	}
	
	public GetSubscriptionStatusResponse getSubscriptionStatus(String env, String subscriptionId) 
	throws UnableToGetSubscriptionStatusFault, ResourceUnknownFault 
	{
		NotificationProducer producer = producerRegistry.getProducer(env);
		if (producer != null) {
			GetSubscriptionStatus subStatus = WSNotificationTools.createGetSubscriptionStatus(subscriptionId);
			return producer.getSubscriptionStatus(subStatus);
		}
		return null;
	}
	
	public RenewResponse renew(SubscriptionRequest subReq) 
	throws UnacceptableTerminationTimeFault, ResourceUnknownFault, DatatypeConfigurationException, ParseException 
	{
		NotificationProducer producer = producerRegistry.getProducer(subReq.getEnv());
		if (producer != null) {
			Date newTermDate = RttUtil.addMinutesToDate(subscribePeriod, new Date());
			XMLGregorianCalendar terminationTime = RttUtil.DateToXMLGregorianCalendar(newTermDate);
			Renew renew = WSNotificationTools.createRenew(subReq.getSubscriptionId(), terminationTime);
			return producer.renew(renew);
		}
		
		return null;
	}
	
	private class ProducerRegistry {
		
		private final Map<String, NotificationProducer> producerMap = new HashMap<String, NotificationProducer>();
		
		public void addProducer(String env, NotificationProducer producer) {
			producerMap.put(env, producer);
		}
		
		public NotificationProducer getProducer(String env) {
			return producerMap.get(env);
		}
	}
}