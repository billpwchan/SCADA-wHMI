package com.thalesgroup.scadagen.wrapper.wrapper.client.subject;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;

public class SubjectMgr {
	private UILogger_i logger = null;
	public void setUILogger(UILogger_i logger) { this.logger = logger; }
	
	private Map<String, Subject> subjectMap = new HashMap<String, Subject>();
	
	public void setSubject(String key, Subject subject) {
		final String function = "setSubject";
		logger.debug(function, "key[{}]", key);
		subjectMap.put(key, subject);
		logger.debug(function, "SubjectMap subject count=[{}]", subjectMap.size());
	}
	
	public void removeSubject(String key) {
		final String function = "setSubject";
		subjectMap.remove(key);
		logger.debug(function, "SubjectMap subject count=[{}]", subjectMap.size());
	}
	
	protected Subject getSubject(String key) { return subjectMap.get(key); }
	
	public void setSubjectState(String function, String clientKey, JSONObject jsdata) {
		logger.begin(function);
		
		String key = clientKey + function;
    	logger.debug(function, "subjectMap subject key[{}] count=[{}]", key, subjectMap.size());
    	Subject subject = subjectMap.get(key); 
    	
    	if ( null != subject ) {
    		try {
    			subject.setState(jsdata);
    		} catch ( NullPointerException ex ) {
    			subjectMap.remove(key);
    			logger.warn(function, "subject for key [{}] NullPointerException, remove it, ex[{}]", new Object[]{key, ex});
    		}
        } else {
        	logger.warn(function, "subject for key [{}] not found", key);
        }
    	logger.end(function);
	}
	
	public JSONArray getJSONArray(String function, String [] strings) {
		logger.begin(function);
        JSONArray grcListJSArray = new JSONArray();
        for ( int i = 0 ; i < strings.length ; ++i ) {
        	grcListJSArray.set(i, new JSONString(strings[i]));
        	logger.debug(function, "i [{}] = strings [{}]", i, strings[i]);
        }
		logger.end(function);
		return grcListJSArray;
	}
	
	public JSONArray getJSONArray(String function, int [] ints) {
		logger.begin(function);
        JSONArray grcListJSArray = new JSONArray();
        for ( int i = 0 ; i < ints.length ; ++i ) {
        	grcListJSArray.set(i, new JSONNumber(ints[i]));
        	logger.debug(function, "i [{}] = ints [{}]", i, ints[i]);
        }
		logger.end(function);
		return grcListJSArray;
	}
	
	public JSONObject convert2Json(String prefix, String name1, JSONValue param1
			, String name2, JSONValue param2
			, String name3, JSONValue param3
			, String name4, JSONValue param4) {
		logger.begin(prefix);
		
		if ( logger.isDebugEnabled() ) {
	    	logger.debug(prefix, "name1[{}] param1[{}]", name1, param1);
	    	logger.debug(prefix, "name2[{}] param2[{}]", name2, param2);
	    	logger.debug(prefix, "name3[{}] param3[{}]", name3, param3);
	    	logger.debug(prefix, "name4[{}] param4[{}]", name4, param4);
		}
		
    	JSONObject jsdata = new JSONObject();

    	jsdata.put(name1, param1);
    	jsdata.put(name2, param2);
    	jsdata.put(name3, param3);
    	jsdata.put(name4, param4);
    	
    	logger.end(prefix);
    	
    	return jsdata;
	}
	
	public String strfunction = "function";
	public String strclientKey = "clientKey";
	public String strerrorCode = "errorCode";
	public String strerrorMessage = "errorMessage";
	
	public JSONObject convert2Json(String function, String clientKey, int errorCode, String errorMessage) {
		logger.begin(function);
		
    	logger.debug(function, "function[{}] clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{function, clientKey, errorCode, errorMessage});
    	
    	JSONObject jsdata = convert2Json(new JSONObject(), function, clientKey, errorCode, errorMessage);
    	
    	logger.end(function);
    	
    	return jsdata;
	}
	
	public JSONObject convert2Json(JSONObject jsdata, String function, String clientKey, int errorCode, String errorMessage) {
		logger.begin(function);
		
    	logger.debug(function, "function[{}] clientKey[{}] errorCode[{}] errorMessage[{}]", new Object[]{function, clientKey, errorCode, errorMessage});
    	
    	jsdata = convert2Json(function, strfunction, new JSONString(function), strclientKey, new JSONString(clientKey), strerrorCode, new JSONNumber(errorCode), strerrorMessage, new JSONString(errorMessage));
    	
    	logger.end(function);
    	
    	return jsdata;
	}
	
	public void handleAccessClient(String function, String clientKey, int errorCode, String errorMessage) {
    	logger.begin(function);

    	handleAccessClient(new JSONObject(), function, clientKey, errorCode, errorMessage);
    	
    	logger.end(function);
	}
	
	public void handleAccessClient(JSONObject jsdata, String function, String clientKey, int errorCode, String errorMessage) {
    	logger.begin(function);

    	jsdata = convert2Json(jsdata, function, clientKey, errorCode, errorMessage);
    	
    	setSubjectState(function, clientKey, jsdata);
    	
    	logger.end(function);
	}
	

}
