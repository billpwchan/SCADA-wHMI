package com.thalesgroup.scadagen.wrapper.wrapper.client.alm;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.alm.IALMComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.alm.ScsALMComponentAccess;

/**
 * 
 * Wrapper for the alm components
 * 
 * @author syau
 *
 */
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
	
	/**
	 * Instance of the ScsALMComponentAccess
	 */
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
				logger.beginEnd(className, function);
				return null;
			}
			
			@Override
			public void setPresenter(HypervisorPresenterClientAbstract<? extends HypervisorView> presenter) {
				// TODO Auto-generated method stub
				final String function = "setPresenter";
				logger.beginEnd(className, function);
			}
			
			@Override
			public void setUnshelveAlarmsResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setUnshelveAlarmsResult";
				logger.begin(className, function);
				logger.debug(className, function, "clientKey[{}]", clientKey);
				logger.debug(className, function, "errorCode[{}]", errorCode);
				logger.debug(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setUnshelveAlarmResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setUnshelveAlarmResult";
				logger.begin(className, function);
				logger.debug(className, function, "clientKey[{}]", clientKey);
				logger.debug(className, function, "errorCode[{}]", errorCode);
				logger.debug(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setShelveAlarmsResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setShelveAlarmsResult";
				logger.begin(className, function);
				logger.debug(className, function, "clientKey[{}]", clientKey);
				logger.debug(className, function, "errorCode[{}]", errorCode);
				logger.debug(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setShelveAlarmResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setShelveAlarmResult";
				logger.begin(className, function);
				logger.debug(className, function, "clientKey[{}]", clientKey);
				logger.debug(className, function, "errorCode[{}]", errorCode);
				logger.debug(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setNotifyExternalEventResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setNotifyExternalEventResult";
				logger.begin(className, function);
				logger.debug(className, function, "clientKey[{}]", clientKey);
				logger.debug(className, function, "errorCode[{}]", errorCode);
				logger.debug(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setNotifyExternalAlarmResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setNotifyExternalAlarmResult";
				logger.begin(className, function);
				logger.debug(className, function, "clientKey[{}]", clientKey);
				logger.debug(className, function, "errorCode[{}]", errorCode);
				logger.debug(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setGetMessageFormatResult(String clientKey, int errorCode, String errorMessage, String format) {
				// TODO Auto-generated method stub
				final String function = "setGetMessageFormatResult";
				logger.begin(className, function);
				logger.debug(className, function, "clientKey[{}]", clientKey);
				logger.debug(className, function, "errorCode[{}]", errorCode);
				logger.debug(className, function, "errorMessage[{}]", errorMessage);
				logger.debug(className, function, "format[{}]", format);
				logger.end(className, function);
			}
			
			@Override
			public void setAckAlarmsResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setAckAlarmsResult";
				logger.begin(className, function);
				logger.debug(className, function, "clientKey[{}]", clientKey);
				logger.debug(className, function, "errorCode[{}]", errorCode);
				logger.debug(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
			
			@Override
			public void setAckAlarmResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				final String function = "setAckAlarmResult";
				logger.begin(className, function);
				logger.debug(className, function, "clientKey[{}]", clientKey);
				logger.debug(className, function, "errorCode[{}]", errorCode);
				logger.debug(className, function, "errorMessage[{}]", errorMessage);
				logger.end(className, function);
			}
		});
		
		logger.end(className, function);
	}
	
	
	/**
	 * Wrapper method for the ScsALMComponentAccess.ackAlarm
	 * 
     * @param key Equal to the SCADAsoft API Parameter "key"
     * @param scsEnvId Equal to the SCADAsoft API Parameter "scsEnvId"
	 * @param alarmId Equal to the SCADAsoft API Parameter "alarmId"
	 * @param comment Equal to the SCADAsoft API Parameter "comment"
	 * @param inUserId Equal to the SCADAsoft API Parameter "inUserId"
	 */
	public void ackAlarm(String key, String scsEnvId, String alarmId, String comment, int inUserId) {
		final String function = "ackAlarm";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}]", key);
		logger.debug(className, function, "scsEnvId[{}]", scsEnvId);
		logger.debug(className, function, "alarmId[{}]", alarmId);
		logger.debug(className, function, "comment[{}]", comment);
		logger.debug(className, function, "inUserId[{}]", inUserId);
		almComponent_.ackAlarm(key, scsEnvId, alarmId, comment, inUserId);
		logger.end(className, function);
	}
	
	/**
	 * Wrapper method for the ScsALMComponentAccess.ackAlarms
	 * 
     * @param key Equal to the SCADAsoft API Parameter "key"
     * @param scsEnvId Equal to the SCADAsoft API Parameter "scsEnvId"
	 * @param alarmIds Equal to the SCADAsoft API Parameter "alarmIds"
	 * @param comment Equal to the SCADAsoft API Parameter "comment"
	 * @param inUserId Equal to the SCADAsoft API Parameter "inUserId"
	 */
	public void ackAlarms(String key, String scsEnvId, String[] alarmIds, String comment, int inUserId) {
		final String function = "ackAlarms";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}]", key);
		logger.debug(className, function, "scsEnvId[{}]", scsEnvId);
		logger.debug(className, function, "alarmIds.length[{}]", alarmIds.length);
		for(String alarmId : alarmIds ) {
			logger.debug(className, function, "alarmId[{}]", alarmId);
		}
		logger.debug(className, function, "alarmIds[{}]", alarmIds);
		logger.debug(className, function, "comment[{}]", comment);
		logger.debug(className, function, "inUserId[{}]", inUserId);
		almComponent_.ackAlarms(key, scsEnvId, alarmIds, comment, inUserId);
		logger.end(className, function);
	}
	
    /**
     * Wrapper method for the ScsALMComponentAccess.notifyExternalAlarm
     * 
     * @param key Equal to the SCADAsoft API Parameter "key"
     * @param scsEnvId Equal to the SCADAsoft API Parameter "scsEnvId"
     * @param configFileName Equal to the SCADAsoft API Parameter "configFileName"
     * @param classId Equal to the SCADAsoft API Parameter "classId"
     * @param pointAlias Equal to the SCADAsoft API Parameter "pointAlias"
     * @param objectId Equal to the SCADAsoft API Parameter "objectId"
     * @param extSourceId Equal to the SCADAsoft API Parameter "extSourceId"
     * @param isAlarm Equal to the SCADAsoft API Parameter "isAlarm"
     * @param message Equal to the SCADAsoft API Parameter "message"
     */
    public void notifyExternalAlarm(String key, String scsEnvId, String configFileName, int classId, String pointAlias,
            int objectId, int extSourceId, boolean isAlarm, String message) {
    	final String function = "notifyExternalAlarm";
    	logger.begin(className, function);
    	
    	logger.debug(className, function, "key[{}]", key);
		logger.debug(className, function, "scsEnvId[{}]", scsEnvId);
		
		logger.debug(className, function, "configFileName[{}]", configFileName);
		logger.debug(className, function, "classId[{}]", classId);
		logger.debug(className, function, "pointAlias[{}]", pointAlias);
		logger.debug(className, function, "objectId[{}]", objectId);
		logger.debug(className, function, "extSourceId[{}]", extSourceId);
		logger.debug(className, function, "isAlarm[{}]", isAlarm);
		logger.debug(className, function, "message[{}]", message);
		
		notifyExternalAlarm(key, scsEnvId
    			, configFileName, classId, pointAlias
    			, objectId, extSourceId, isAlarm, message
    			, true);
    	logger.end(className, function);
    }
	
    /**
     * Wrapper method for the ScsALMComponentAccess.notifyExternalAlarm
     * 
     * @param key Equal to the SCADAsoft API Parameter "key"
     * @param scsEnvId Equal to the SCADAsoft API Parameter "scsEnvId"
     * @param configFileName Equal to the SCADAsoft API Parameter "configFileName"
     * @param classId Equal to the SCADAsoft API Parameter "classId"
     * @param pointAlias Equal to the SCADAsoft API Parameter "pointAlias"
     * @param objectId Equal to the SCADAsoft API Parameter "objectId"
     * @param extSourceId Equal to the SCADAsoft API Parameter "extSourceId"
     * @param isAlarm Equal to the SCADAsoft API Parameter "isAlarm"
     * @param message Equal to the SCADAsoft API Parameter "message"
     * @param translation
     */
    public void notifyExternalAlarm(String key, String scsEnvId, String configFileName, int classId, String pointAlias,
            int objectId, int extSourceId, boolean isAlarm, String message, boolean translation) {
    	final String function = "notifyExternalAlarm";
    	logger.begin(className, function);
    	
    	logger.debug(className, function, "key[{}]", key);
		logger.debug(className, function, "scsEnvId[{}]", scsEnvId);
		
		logger.debug(className, function, "configFileName[{}]", configFileName);
		logger.debug(className, function, "classId[{}]", classId);
		logger.debug(className, function, "pointAlias[{}]", pointAlias);
		logger.debug(className, function, "objectId[{}]", objectId);
		logger.debug(className, function, "extSourceId[{}]", extSourceId);
		logger.debug(className, function, "isAlarm[{}]", isAlarm);
		logger.debug(className, function, "message[{}]", message);
		logger.debug(className, function, "translation[{}]", translation);
		
		if ( translation ) { message = TranslationMgr.getInstance().getTranslation(message); }
		
    	almComponent_.notifyExternalAlarm(key, scsEnvId
    			, configFileName, classId, pointAlias
    			, objectId, extSourceId, isAlarm, message
    			);
    	logger.end(className, function);
    }
    
    /**
     * Wrapper method for the ScsALMComponentAccess.notifyExternalEvent
     * 
     * @param key Equal to the SCADAsoft API Parameter "key"
     * @param scsEnvId Equal to the SCADAsoft API Parameter "scsEnvId"
     * @param configFileName Equal to the SCADAsoft API Parameter "configFileName"
     * @param classId Equal to the SCADAsoft API Parameter "classId"
     * @param pointAlias Equal to the SCADAsoft API Parameter "pointAlias"
     * @param objectId Equal to the SCADAsoft API Parameter "objectId"
     * @param extSourceId Equal to the SCADAsoft API Parameter "extSourceId"
     */
    public void notifyExternalEvent(String key, String scsEnvId, String configFileName, int classId, String pointAlias,
            int objectId, int extSourceId, String message) {
    	final String function = "notifyExternalEvent";
    	logger.begin(className, function);
    	
    	logger.debug(className, function, "key[{}]", key);
		logger.debug(className, function, "scsEnvId[{}]", scsEnvId);
		
		logger.debug(className, function, "configFileName[{}]", configFileName);
		logger.debug(className, function, "classId[{}]", classId);
		logger.debug(className, function, "pointAlias[{}]", pointAlias);
		logger.debug(className, function, "objectId[{}]", objectId);
		logger.debug(className, function, "extSourceId[{}]", extSourceId);
		logger.debug(className, function, "message[{}]", message);
		
		notifyExternalEvent(key, scsEnvId
    			, configFileName, classId, pointAlias
    			, objectId, extSourceId, message
    			, true);
    	logger.end(className, function);
    }
    
    /**
     * Wrapper method for the SCADAsoft API Parameter ScsALMComponentAccess.notifyExternalEvent
     * 
     * @param key Equal to the SCADAsoft API Parameter "key"
     * @param scsEnvId Equal to the SCADAsoft API Parameter "scsEnvId"
     * @param configFileName Equal to the SCADAsoft API Parameter "configFileName"
     * @param classId Equal to the SCADAsoft API Parameter "classId"
     * @param pointAlias Equal to the SCADAsoft API Parameter "pointAlias"
     * @param objectId Equal to the SCADAsoft API Parameter "objectId"
     * @param extSourceId Equal to the SCADAsoft API Parameter "extSourceId"
     * @param message Equal to the SCADAsoft API Parameter "extSourceId"
     * @param translation Enable translation function on message field
     */
    public void notifyExternalEvent(String key, String scsEnvId, String configFileName, int classId, String pointAlias,
            int objectId, int extSourceId, String message, boolean translation) {
    	final String function = "notifyExternalEvent";
    	logger.begin(className, function);
    	
    	logger.debug(className, function, "key[{}]", key);
		logger.debug(className, function, "scsEnvId[{}]", scsEnvId);
		
		logger.debug(className, function, "configFileName[{}]", configFileName);
		logger.debug(className, function, "classId[{}]", classId);
		logger.debug(className, function, "pointAlias[{}]", pointAlias);
		logger.debug(className, function, "objectId[{}]", objectId);
		logger.debug(className, function, "extSourceId[{}]", extSourceId);
		logger.debug(className, function, "message[{}]", message);
		logger.debug(className, function, "translation[{}]", translation);
		
		if ( translation ) { message = TranslationMgr.getInstance().getTranslation(message); }
		
    	almComponent_.notifyExternalEvent(key, scsEnvId
    			, configFileName, classId, pointAlias
    			, objectId, extSourceId, message
    			);
    	logger.end(className, function);
    }
}
