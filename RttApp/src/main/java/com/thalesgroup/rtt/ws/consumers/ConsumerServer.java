package com.thalesgroup.rtt.ws.consumers;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.notification.list.NotificationList;
import com.thalesgroup.hv.ws.notification_v1.NotificationConsumer;
import com.thalesgroup.hv.ws.notification_v1.OverloadFault;
import com.thalesgroup.hv.ws.notification_v1.ResourceUnknownFault;
import com.thalesgroup.hv.ws.notification_v1.xsd.NotificationMessageHolderType;
import com.thalesgroup.hv.ws.notification_v1.xsd.Notify;
import com.thalesgroup.hv.ws.notification_v1.xsd.NotifyAck;
import com.thalesgroup.rtt.beans.RttEvent;
import com.thalesgroup.rtt.tools.MessageResolver;
import com.thalesgroup.rtt.transports.RttMessagePublisher;
import com.thalesgroup.rtt.ws.producers.WebServiceProducerConnector;

import java.util.Date;
import java.util.Map;









import javax.jws.WebService;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@WebService(endpointInterface = "com.thalesgroup.hv.ws.notification_v1.NotificationConsumer")
public class ConsumerServer implements NotificationConsumer
{
  @Autowired
  private RttMessagePublisher publisher;
  
  @Autowired
  private WebServiceProducerConnector connector;
  
  @Autowired
  private MessageResolver messageResolver;
  
  
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  
 
  public NotifyAck notify(Notify message)
  throws OverloadFault, ResourceUnknownFault
  {
    NotificationMessageHolderType messageHolderType = message.getNotificationMessage().get(0);
    String subscriptionId = messageResolver.getSubscriptionId(messageHolderType);
    try {
    	NotificationList messageList = messageResolver.getNotificationList(messageHolderType);
    	String hvId = messageResolver.getHypervisorId(messageList);
		Map<String, AbstractAttributeType> messageMap = messageResolver.getEntityAttributesMap(messageList);
		for (Map.Entry<String, AbstractAttributeType> entry : messageMap.entrySet()) {
			if (entry.getValue() != null) {
				String field = entry.getKey();
				String time = Long.toString(entry.getValue().getTimestamp());
				String value = messageResolver.getNumericValueToString(entry.getValue());
				RttEvent event = new RttEvent(publisher, 1, subscriptionId, field, hvId, time, value, "some_env");
				this.publisher.publishWsMessage(event);
			}
		}
	} catch (EntityManipulationException e) {
		log.error(e.getMessage());
	} catch (HypervisorConversionException e) {
		log.error(e.getMessage());
	}
	return null;
  }
}
