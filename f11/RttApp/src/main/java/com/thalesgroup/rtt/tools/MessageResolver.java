package com.thalesgroup.rtt.tools;

import java.util.Map;
import java.util.Set;

import javax.xml.transform.dom.DOMSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.tools.MarshallersPool;
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data.tools.helper.IDOMConverterHelper;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.attribute.DoubleAttributeType;
import com.thalesgroup.hv.data_v1.attribute.FloatAttributeType;
import com.thalesgroup.hv.data_v1.attribute.IntAttributeType;
import com.thalesgroup.hv.data_v1.attribute.LongAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.notification.EntityNotificationElementType;
import com.thalesgroup.hv.data_v1.notification.list.NotificationList;
import com.thalesgroup.hv.ws.notification_v1.xsd.NotificationMessageHolderType;

@Component
public class MessageResolver {
	
	private EntityDataHelper dataHelper;
	
	private MarshallersPool marshallersPool;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public MessageResolver() {
		dataHelper = new EntityDataHelper();
		IDOMConverterHelper domConverter = dataHelper.getDOMConverterHelper();
		marshallersPool = domConverter.getMarshallersPool(); 
	}
	
	
	public String getSubscriptionId(NotificationMessageHolderType messageHolderType) {
		return messageHolderType.getSubscriptionReference().getReferenceParameters().getSubscriptionID();
	}
	
	public String getHypervisorId(NotificationList messageList) {
		return messageList.getEntityNotificationElement().get(0).getEntity().getId();
	}
	
	public Map<String, AbstractAttributeType> getEntityAttributesMap(NotificationList messageList) 
	throws EntityManipulationException, HypervisorConversionException {
		AbstractEntityStatusesType entity = getMarshalledBean(messageList);
		return dataHelper.getAttributes(entity, getProperties(entity));
	}
	
	private Set<String> getProperties(AbstractEntityStatusesType entity) throws EntityManipulationException {
		return dataHelper.getDeclaredStatusNames(entity.getClass());
	}
	
	public Set<String> getProperties(String entity) throws EntityManipulationException {
		return dataHelper.getDeclaredStatusNames(entity);
	}
	
	
	private AbstractEntityStatusesType getMarshalledBean(NotificationList notificationList) {
		return ((EntityNotificationElementType)notificationList.getEntityNotificationElement().get(0)).getEntity();
	}
	
	public NotificationList getNotificationList(NotificationMessageHolderType messageHolderType) throws HypervisorConversionException {
		Object object = messageHolderType.getMessage().getAny();
		if ((object instanceof NotificationList))
	      {
			return (NotificationList) object;
	      }
	      else if ((object instanceof Node))
	      {
	        DOMSource source = new DOMSource((Node)object);
	        return (NotificationList) marshallersPool.unmarshal(source, NotificationList.class);
	      }
		return null;
	}
	
	public String getNumericValueToString(AbstractAttributeType attribute) {
		if (attribute instanceof IntAttributeType) {
			int value = ((IntAttributeType) attribute).getValue();
			return Integer.toString(value);
		} else if (attribute instanceof LongAttributeType) {
			Long value = ((LongAttributeType) attribute).getValue();
			return Long.toString(value);
		} else if (attribute instanceof DoubleAttributeType) {
			Double value = ((DoubleAttributeType) attribute).getValue();
			return Double.toString(value);
		} else if (attribute instanceof FloatAttributeType) {
			Float value = ((FloatAttributeType) attribute).getValue();
			return Float.toString(value);
		}
		return null;
	}
	
}