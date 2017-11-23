package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.alarm;

import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.alm.AlmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.mgrfactory.MgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Observer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ols.OlsMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ols.OlsMgr_i;

public class AckAlarm {
	
	private final String className = UIWidgetUtil.getClassSimpleName(AckAlarm.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String strOlsMgr = AckAlarm_i.STR_OLS_MGR;
	private final String strOlsMgrKey = AckAlarm_i.STR_OLS_MGR_KEY;
	
	private final String strAlmServer = AckAlarm_i.STR_ALM_SERVER;
	private final String strListName = AckAlarm_i.STR_LIST_NAME;
	private final String strAlarmField = AckAlarm_i.STR_ALARM_FIELDS;
	
	private final String strKeyword = AckAlarm_i.STR_KEYWORD;
	private final String strFilterMsgLike = AckAlarm_i.STR_FILTER_MSG_LIKE;
	private final String strFilterConcat = AckAlarm_i.STR_FILTER_CONCAT;
	private final String strFilter = AckAlarm_i.STR_FILTER;
	
	private String scsEnvId = null;
	public AckAlarm(String scsEnvId) {
		this.scsEnvId = scsEnvId;
	}
	
	private void ackAlarmIds(String[] alarmIds) {
		final String function = "ackAlarmIds";
		logger.begin(className, function);
		
		if ( logger.isDebugEnabled() ) {
			logger.debug(className, function, "alarmIds.length[{}]", alarmIds.length);
			for ( int i = 0 ; i < alarmIds.length ; ++i ) {
				logger.debug(className, function, "alarmIds[{}][{}]", i, alarmIds[i]);
			}
		}
		
		String key = "ackalarms";
		String comment = "";
		int inUserId = 0;
		AlmMgr almMgr = AlmMgr.getInstance(className);
		almMgr.ackAlarms(key, scsEnvId, alarmIds, comment, inUserId);
		
		logger.end(className, function);
	}
	
	public void ack(int[] alarmIds) {
		final String function = "ack";
		logger.begin(className, function);
		
		if ( null != alarmIds ) {
			String [] strAlarmIds = null;
			if ( alarmIds.length > 0 ) {
				strAlarmIds = new String[alarmIds.length];
				for ( int i = 0 ; i < alarmIds.length ; ++i ) {
					strAlarmIds[i] = String.valueOf(alarmIds[i]);
				}
			} else {
				logger.warn(className, function, "alarmIds.length < 0");
			}
			
			if ( null != strAlarmIds ) {
				ackAlarmIds(strAlarmIds);
			} else {
				logger.warn(className, function, "strAlarmIds IS NULL");
			}
			
		} else {
			logger.warn(className, function, "alarmIds IS NULL");
		}
	
		logger.end(className, function);
	}
	
	private Subject getSubject() {
		final String function = "getSubject";
		logger.begin(className, function);
		
		Subject subject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				
				logger.begin(className, function);
				logger.debug(className, function, "this.subject.getState()[{}]", this.subject.getState());
				
				ack(ReadJson.readIntsFromArray(ReadJson.readArray(this.subject.getState(), OlsMgr_i.FIELD_DATA), strAlarmField));
				
				logger.end(className, function);
			}

		};
		observer.setSubject(subject);

		logger.end(className, function);
		
		return subject;
	}

	public void ack(String[] alarmDBAddresses) {
		final String function = "ack";
		logger.begin(className, function);
		
		if ( null != alarmDBAddresses ) {
			
			if ( logger.isDebugEnabled() ) {
				logger.debug(className, function, "alarmDBAddresses.length[{}]", alarmDBAddresses.length);
				for ( int i = 0 ; i < alarmDBAddresses.length ; ++i ) {
					logger.debug(className, function, "alarmDBAddresses[{}][{}]", i, alarmDBAddresses[i]);
				}
			}

			logger.debug(className, function, "strOlsMgr[{}] strOlsMgrKey[{}]", strOlsMgr, strOlsMgrKey);
			
			OlsMgr olsMgr = (OlsMgr) MgrFactory.getInstance().getMgr(strOlsMgr, strOlsMgrKey);
			
			final String olsApi = "ReadData";
			
			final String clientKey = className+"_"+scsEnvId;
			
			final String subjectKey = clientKey+olsApi;
			
			logger.debug(className, function, "subjectKey[{}]", subjectKey);
			
			olsMgr.setSubject(subjectKey, getSubject());

			final String fieldList[] = new String[]{strAlarmField};
			
			logger.debug(className, function, "strFilterMsgLike[{}]", strFilterMsgLike);
			logger.debug(className, function, "strKeyword[{}]", strKeyword);
			logger.debug(className, function, "strFilter[{}]", strFilter);

			String filters = null;
			String filterItems = "";
			for ( int i = 0 ; i < alarmDBAddresses.length ; ++i ) {
				if ( filterItems.length() > 0 ) filterItems += strFilterConcat;
				filterItems += strFilterMsgLike.replace(strKeyword, alarmDBAddresses[i]);
			}
			if ( filterItems.length() > 0 ) {
				filters = strFilter.replace(strKeyword, filterItems);
			}
			logger.debug(className, function, "filters[{}]", filters);
			
			if ( null != filters ) {
				olsMgr.readData(clientKey, scsEnvId, strAlmServer, strListName, fieldList, filters);
			} else {
				logger.warn(className, function, "filters IS NULL, SKIP TO GET Alarm IDs");
			}
		} else {
			logger.warn(className, function, "alarmDBAddresses IS NULL");
		}

		logger.end(className, function);
	}
}
