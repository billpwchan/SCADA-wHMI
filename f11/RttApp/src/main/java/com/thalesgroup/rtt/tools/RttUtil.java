package com.thalesgroup.rtt.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.attribute.DoubleAttributeType;
import com.thalesgroup.hv.data_v1.attribute.FloatAttributeType;
import com.thalesgroup.hv.data_v1.attribute.IntAttributeType;
import com.thalesgroup.hv.data_v1.attribute.LongAttributeType;
import com.thalesgroup.hv.ws.notification_v1.xsd.SubscribeResponse;
import com.thalesgroup.rtt.beans.SubscriptionRequest;
import com.thalesgroup.rtt.constants.HypervisorConstants;



public class RttUtil {
	
	private static DateFormat df = new SimpleDateFormat("HH:mm:ss");
	
	private static final String delimiter = " ";
	
	static {
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	public static XMLGregorianCalendar DateToXMLGregorianCalendar(Date s) 
		throws  DatatypeConfigurationException, ParseException 
	{
		XMLGregorianCalendar result = null;
		//Date date;
		//SimpleDateFormat simpleDateFormat;
		GregorianCalendar gregorianCalendar;
		
		//simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    //date = simpleDateFormat.parse(s);        
	    gregorianCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
	    gregorianCalendar.setTime(s);
	    result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	    return result;
	}
	
	public static Collection<String> splitStringToList(String str, String delimiter) {
  	  Collection<String> tokenList = new ArrayList<String>();
  	  while (-1 != str.indexOf(delimiter)) {
  		  int idx = str.indexOf(delimiter);
  		  tokenList.add(str.substring(0, idx).trim());
  		  str = new String(str.substring(idx+1, str.length()));
  	  }
  	  tokenList.add(str);
  	  return tokenList;
    }
	
	public static Date addMinutesToDate(int minutes, Date beforeTime){
	    final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

	    long curTimeInMs = beforeTime.getTime();
	    Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
	    return afterAddingMins;
	}
	
	public static Collection<SubscriptionRequest> parseRequestQueryString(String requestQuery) throws UnsupportedEncodingException {
		Map<String, String> queryPairs = new LinkedHashMap<String, String>();
		String[] pairs = requestQuery.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
			String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
			queryPairs.put(key, value);
		}
		return parseRequestQuery(queryPairs);
		
	}
	
	public static Collection<SubscriptionRequest> createSubscriptions(String ... queries) {
		Collection<SubscriptionRequest> col = new ArrayList<SubscriptionRequest>();
		for (String query : queries) {
			SubscriptionRequest subReq = new SubscriptionRequest();
			if (!query.isEmpty() && query != null) {
				subReq = createSubscription(query);
			} else {
				subReq = null;
			}
			
			if (subReq != null) {
				col.add(subReq);
			}
		}
		return col;
		
	}
	
	public static SubscriptionRequest createSubscription_hv(String query) {
		String[] params = query.split(delimiter, 5);
		SubscriptionRequest subReq = new SubscriptionRequest();
		if (params.length == 5) {
			subReq.setEnv(params[0]);
			subReq.setHypervisorId(params[1]);
			subReq.setTopic(params[2]);
			subReq.setTopicExpression(getTopicExpression(params[2]));
			subReq.setType(params[3]);
			subReq.setField(params[4]);
		}
		
		if (validRequest(subReq)) {
			return subReq;
		}
		return null;
	}
	
	//ENV+Name+ID
	public static SubscriptionRequest createSubscription(String query) {
		String[] params = query.split(delimiter, 5);
		SubscriptionRequest subReq = new SubscriptionRequest();
//		if (params.length == 5) {
		//ENV+Name+ArchiveNametry 
			try {
			subReq.setEnv(params[0]);
			subReq.setHypervisorId(params[1]);
			subReq.setField(params[2]);
			
			if (checkValidColor(params[3])) {
				subReq.setColor(params[3]);
			}
			
			//not used
			subReq.setTopicExpression("DEFAULT_TOPIC_EXP");
			subReq.setType("DEFAULT_TYPE");			
			subReq.setTopic("DEFAULT_TOPIC");
				
			return subReq;
		} catch (Exception e){
			return null;
		}
	}
	
	public static boolean validRequest(SubscriptionRequest subReq) {
		if (subReq.getTopic().isEmpty() || subReq.getTopic() == null) {
			return false;
		}
		
		if (subReq.getTopicExpression().isEmpty() || subReq.getTopicExpression() == null) {
			return false;
		}
		
		if (subReq.getType().isEmpty() || subReq.getType() == null) {
			return false;
		}
		
		if (subReq.getHypervisorId().isEmpty() || subReq.getHypervisorId() == null) {
			return false;
		}
		
		if (subReq.getField().isEmpty() || subReq.getHypervisorId() == null) {
			return false;
		}
		
		if (subReq.getEnv().isEmpty() || subReq.getEnv() == null) {
			return false;
		}
		
		return true;
	}
	
	private static Collection<SubscriptionRequest> parseRequestQuery(Map<String, String> query) {
		
		Collection<SubscriptionRequest> subscriptionList = new ArrayList<SubscriptionRequest>();
		int num = 0;
		
		if (query.containsKey("num")) {
			num = Integer.parseInt(query.get("num"));
		}
		
		for (int i = 1; i <= num; ++i) {
			SubscriptionRequest sub = new SubscriptionRequest();
			
			if (query.containsKey("topic" + i)) {
				String topic = query.get("topic" + i);
				sub.setTopic(topic);
				sub.setTopicExpression(getTopicExpression(topic));
			} else {
				continue;
			};
			
			
			if (query.containsKey("type" + i)) {
				sub.setType(query.get("type" + i));
			} else {
				continue;
			};
			
			if (query.containsKey("field" + i)) {
				sub.setField(query.get("field" + i));
			} else {
				continue;
			};
			
			if (query.containsKey("hvid" + i)) {
				sub.setHypervisorId(query.get("hvid" + i));
			} else {
				continue;
			};
			
			if (query.containsKey("env" + i)) {
				sub.setEnv(query.get("env" + i));
			} else {
				continue;
			}
			
			subscriptionList.add(sub);
		}
		
		return subscriptionList;
	}
	
	public static String getTopicExpression(String topic) {
		if (topic.equals("Activity")) {
			return HypervisorConstants.TOPIC_EXPRESSION_ACTIVITY;
		} else if (topic.equals("Alarm")) {
			return HypervisorConstants.TOPIC_EXPRESSION_ALARM;
		} else if (topic.equals("Connection")) {
			return HypervisorConstants.TOPIC_EXPRESSION_CONNECTION;
		} else if (topic.equals("Equipment")) {
			return HypervisorConstants.TOPIC_EXPRESSION_EQPT;
		} else if (topic.equals("Event")) {
			return HypervisorConstants.TOPIC_EXPRESSION_EVENT;
		} else if (topic.equals("Subsystem")) {
			return HypervisorConstants.TOPIC_EXPRESSION_SUBSYSTEM;
		}
		return null;
	}
	
	public static String dateToTimeString(Date date) {
		return df.format(date);
	}
	
	public static String getNumericValueToString(AbstractAttributeType attribute) {
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
	
	public static String getSubscriptionIdFromResponse(SubscribeResponse subscribeResponse) {
		return subscribeResponse.getSubscriptionReference().getReferenceParameters().getSubscriptionID();
	}
	
	public static Long toUnixTime(XMLGregorianCalendar calendar) {
		return calendar.toGregorianCalendar().getTime().getTime();
	}
	
	public static String getSubscriptionLink(SubscriptionRequest subReq) {
		return "/" + subReq.getEnv() + "/" + subReq.getSubscriptionId();
	}
	
	public static String buildQueryKey(String env, String hvId, String field) {
		StringBuilder sb = new StringBuilder();
		sb.append(env);
		sb.append("+");
		sb.append(hvId);
		sb.append("+");
		sb.append(field);
		return sb.toString();
	}
	
	public static boolean checkValidColor(String hex) {
		if (null == hex || "".equals(hex)) 
			return false;
		
		return true;
	}
	
}