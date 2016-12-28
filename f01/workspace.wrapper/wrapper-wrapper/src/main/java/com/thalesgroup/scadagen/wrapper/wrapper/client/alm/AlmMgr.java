package com.thalesgroup.scadagen.wrapper.wrapper.client.alm;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.alm.IALMComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.alm.ScsALMComponentAccess;

public class AlmMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(AlmMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private static HashMap<String, AlmMgr> instances = new HashMap<String, AlmMgr>();
	public static AlmMgr getInstance(String key) {
		if ( ! instances.containsKey(key) ) {	instances.put(key, new AlmMgr()); }
		AlmMgr instance = instances.get(key);
		return instance;
	}
	
	private Subject subject = null;
	public Subject getSubject() { return subject; }
	
	private ScsALMComponentAccess almComponent_;
	
	private AlmMgr() {
		
		final String function = "AlmMgr";
		
		logger.begin(className, function);
		
		this.subject = new Subject();
		
		almComponent_ = new ScsALMComponentAccess(new IALMComponentClient() {
			
			@Override
			public void destroy() {
				// TODO Auto-generated method stub
				final String function = "destroy";
				logger.begin(className, function);
				logger.end(className, function);
			}
			
			@Override
			public Widget asWidget() {
				// TODO Auto-generated method stub
				final String function = "asWidget";
				logger.begin(className, function);
				logger.end(className, function);
				return null;
			}
			
			@Override
			public void setPresenter(HypervisorPresenterClientAbstract<? extends HypervisorView> presenter) {
				// TODO Auto-generated method stub
				final String function = "setPresenter";
				logger.begin(className, function);
				logger.end(className, function);
			}
			
			@Override
			public void setUnshelveAlarmsResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setUnshelveAlarmsResult";
				logger.begin(className, function);
				logger.info(className, function, "clientKey[{}]", clientKey);
				logger.info(className, function, "errorCode[{}]", errorCode);
				logger.info(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setUnshelveAlarmResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setUnshelveAlarmResult";
				logger.begin(className, function);
				logger.info(className, function, "clientKey[{}]", clientKey);
				logger.info(className, function, "errorCode[{}]", errorCode);
				logger.info(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setShelveAlarmsResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setShelveAlarmsResult";
				logger.begin(className, function);
				logger.info(className, function, "clientKey[{}]", clientKey);
				logger.info(className, function, "errorCode[{}]", errorCode);
				logger.info(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setShelveAlarmResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setShelveAlarmResult";
				logger.begin(className, function);
				logger.info(className, function, "clientKey[{}]", clientKey);
				logger.info(className, function, "errorCode[{}]", errorCode);
				logger.info(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setNotifyExternalEventResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setNotifyExternalEventResult";
				logger.begin(className, function);
				logger.info(className, function, "clientKey[{}]", clientKey);
				logger.info(className, function, "errorCode[{}]", errorCode);
				logger.info(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setNotifyExternalAlarmResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setNotifyExternalAlarmResult";
				logger.begin(className, function);
				logger.info(className, function, "clientKey[{}]", clientKey);
				logger.info(className, function, "errorCode[{}]", errorCode);
				logger.info(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setGetMessageFormatResult(String clientKey, int errorCode, String errorMessage, String format) {
				// TODO Auto-generated method stub
				final String function = "setGetMessageFormatResult";
				logger.begin(className, function);
				logger.info(className, function, "clientKey[{}]", clientKey);
				logger.info(className, function, "errorCode[{}]", errorCode);
				logger.info(className, function, "errorMessage[{}]", errorMessage);
				logger.info(className, function, "format[{}]", format);
				logger.end(className, function);
			}
			
			@Override
			public void setAckAlarmsResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setAckAlarmsResult";
				logger.begin(className, function);
				logger.info(className, function, "clientKey[{}]", clientKey);
				logger.info(className, function, "errorCode[{}]", errorCode);
				logger.info(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setAckAlarmResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setAckAlarmResult";
				logger.begin(className, function);
				logger.info(className, function, "clientKey[{}]", clientKey);
				logger.info(className, function, "errorCode[{}]", errorCode);
				logger.info(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
		});
		
		logger.end(className, function);
	}
	
	
	public void ackAlarm(String key, String scsEnvId, String alarmId, String comment, int inUserId) {
		final String function = "ackAlarm";
		logger.begin(className, function);
		logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		logger.info(className, function, "alarmId[{}]", alarmId);
		logger.info(className, function, "comment[{}]", comment);
		logger.info(className, function, "inUserId[{}]", inUserId);
		almComponent_.ackAlarm(key, scsEnvId, alarmId, comment, inUserId);
		logger.end(className, function);
	}
	
	public void ackAlarms(String key, String scsEnvId, String[] alarmIds, String comment, int inUserId) {
		final String function = "ackAlarms";
		logger.begin(className, function);
		logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		logger.info(className, function, "alarmIds.length[{}]", alarmIds.length);
		for(String alarmId : alarmIds ) {
			logger.info(className, function, "alarmId[{}]", alarmId);
		}
		logger.info(className, function, "alarmIds[{}]", alarmIds);
		logger.info(className, function, "comment[{}]", comment);
		logger.info(className, function, "inUserId[{}]", inUserId);
		almComponent_.ackAlarms(key, scsEnvId, alarmIds, comment, inUserId);
		logger.end(className, function);
	}
	
    public void notifyExternalAlarm(String key, String scsEnvId, String configFileName, int classId, String pointAlias,
            int objectId, int extSourceId, boolean isAlarm, String message) {
    	final String function = "notifyExternalAlarm";
    	logger.begin(className, function);
    	
    	logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		
		logger.info(className, function, "configFileName[{}]", configFileName);
		logger.info(className, function, "classId[{}]", classId);
		logger.info(className, function, "pointAlias[{}]", pointAlias);
		logger.info(className, function, "objectId[{}]", objectId);
		logger.info(className, function, "extSourceId[{}]", extSourceId);
		logger.info(className, function, "isAlarm[{}]", isAlarm);
		logger.info(className, function, "message[{}]", message);
    	almComponent_.notifyExternalAlarm(key, scsEnvId
    			, configFileName, classId, pointAlias
    			, objectId, extSourceId, isAlarm, message
    			);
    	logger.end(className, function);
    }
    
    
    public void notifyExternalEvent(String key, String scsEnvId, String configFileName, int classId, String pointAlias,
            int objectId, int extSourceId, String message) {
    	final String function = "notifyExternalEvent";
    	logger.begin(className, function);
    	
    	logger.info(className, function, "key[{}]", key);
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		
		logger.info(className, function, "configFileName[{}]", configFileName);
		logger.info(className, function, "classId[{}]", classId);
		logger.info(className, function, "pointAlias[{}]", pointAlias);
		logger.info(className, function, "objectId[{}]", objectId);
		logger.info(className, function, "extSourceId[{}]", extSourceId);
		logger.info(className, function, "message[{}]", message);
    	almComponent_.notifyExternalEvent(key, scsEnvId
    			, configFileName, classId, pointAlias
    			, objectId, extSourceId, message
    			);
    	logger.end(className, function);
    }
}
