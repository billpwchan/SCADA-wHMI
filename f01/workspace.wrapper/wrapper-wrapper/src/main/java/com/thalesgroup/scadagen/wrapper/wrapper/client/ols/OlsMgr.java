package com.thalesgroup.scadagen.wrapper.wrapper.client.ols;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Mgr_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ols.OlsMgr_i.Request;
import com.thalesgroup.scadagen.wrapper.wrapper.client.subject.SubjectMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.LogUtil;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ols.IOLSComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ols.ScsOLSComponentAccess;

public class OlsMgr implements Mgr_i {
	
	private String className = UIWidgetUtil.getClassSimpleName(OlsMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private static HashMap<String, Mgr_i> instances = new HashMap<String, Mgr_i>();
	public static Mgr_i getInstance(String key) {
		if ( ! instances.containsKey(key) ) {
			Mgr_i mgr = new OlsMgr();
			instances.put(key, mgr);
		}
		Mgr_i instance = instances.get(key);
		return instance;
	}

	public static Set<Entry<String, Mgr_i>> getInstances() { return instances.entrySet(); }
	
	private SubjectMgr subjectMgr = null;
	@Override
	public void setSubject(String key, Subject subject) { subjectMgr.setSubject(key, subject); }
	@Override
	public void removeSubject(String key) { subjectMgr.removeSubject(key); }
	
	private ScsOLSComponentAccess scsOLSComponentAccess = null;
	private OlsMgr () {
		
		final String function = "OlsMgr";
		logger.begin(className, function);
		
		subjectMgr = new SubjectMgr();
		
		subjectMgr.setPrefix(className);
		subjectMgr.setUILogger(logger);
		
		scsOLSComponentAccess = new ScsOLSComponentAccess(new IOLSComponentClient() {
			
			@Override
			public void destroy() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Widget asWidget() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void setPresenter(HypervisorPresenterClientAbstract<? extends HypervisorView> presenter) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setUpdateDataResult(String clientKey, int operationResult, int errorCode, String errorMessage) {
				final String function = Request.UpdateData.toString();
				logger.begin(className, function);

		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	jsdata.put("operationResult", new JSONNumber(operationResult));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setUnsubscribeOlsListResult(String clientKey, int errorCode, String errorMessage) {
				final String function = Request.UnsubscribeOlsList.toString();
				logger.begin(className, function);

		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setSubscribeOlsListResult(String clientKey, String listServer, String subUUID, JSONArray created,
					JSONArray updated, JSONArray deleted, int errorCode, String errorMessage) {
				final String function = Request.SubscribeOlsList.toString();
				logger.begin(className, function);
				
		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	jsdata.put("listServer", new JSONString(listServer));
		    	jsdata.put("subUUID", new JSONString(subUUID));
		    	jsdata.put("created", created);
		    	jsdata.put("updated", updated);
		    	jsdata.put("deleted", deleted);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setReadDataResult(String clientKey, JSONArray data, int errorCode, String errorMessage) {
				final String function = Request.ReadData.toString();
				logger.begin(className, function);

		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put(OlsMgr_i.FIELD_DATA, data);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setInsertDataResult(String clientKey, int operationResult, int errorCode, String errorMessage) {
				final String function = Request.InsertData.toString();
				logger.begin(className, function);

		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	jsdata.put("operationResult", new JSONNumber(operationResult));
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
			}
			
			@Override
			public void setDeleteDataResult(String clientKey, int errorCode, String errorMessage) {
				final String function = Request.DeleteData.toString();
				logger.begin(className, function);

		    	JSONObject jsdata = subjectMgr.convert2Json(function, clientKey, errorCode, errorMessage);
		    	
		    	subjectMgr.setSubjectState(function, clientKey, jsdata);
		    	
		    	logger.end(className, function);
				
			}
		});
		logger.end(className, function);
	}
	
	private void logs(String function, String key, String scsEnvId) {
		logger.debug(className, function, "key[{}] scsEnvId[{}]", key, scsEnvId);
	}
	
	private void logs(String function, String key, String scsEnvId, String listServer, String listName) {
		logs(function, key, scsEnvId);
		logger.debug(className, function, "listServer[{}] listName[{}]", listServer, listName);
	}

	public void deleteData(String key, String scsEnvId, String listServer, String listName, String[] keyList) {
		final String function = Request.DeleteData.toString();
		logger.begin(className, function);
		
		logs(function, key, scsEnvId, listServer, listName);
		LogUtil.logArray(logger, className, function, "keyList", keyList);
		scsOLSComponentAccess.deleteData(key, scsEnvId, listServer, listName, keyList);
		
		logger.end(className, function);
	}
	public void subscribeOlsList(String key, String scsEnvId, String listServer, String listName, String[] fieldList, String filter) {
		final String function = Request.SubscribeOlsList.toString();
		logger.begin(className, function);
		
		logs(function, key, scsEnvId, listServer, listName);
		LogUtil.logArray(logger, className, function, "fieldList", fieldList);
		logger.debug(className, function, "filter", filter);
		scsOLSComponentAccess.subscribeOlsList(key, scsEnvId, listServer, listName, fieldList, filter);
		
		logger.end(className, function);
	}
	public void unsubscribeOlsList(String key, String scsEnvId, String listServer, String subUUID) {
		final String function = Request.UnsubscribeOlsList.toString();
		logger.begin(className, function);
		
		logs(function, key, scsEnvId);
		logger.debug(className, function, "subUUID", subUUID);
		scsOLSComponentAccess.unsubscribeOlsList(key, scsEnvId, listServer, subUUID);
		
		logger.end(className, function);
	}
	public void readData(String key, String scsEnvId, String listServer, String listName, String[] fieldList, String filter) {
		final String function = Request.ReadData.toString();
		logger.begin(className, function);
		
		logs(function, key, scsEnvId, listServer, listName);
		LogUtil.logArray(logger, className, function, "fieldList", fieldList);
		logger.debug(className, function, "filter", filter);
		scsOLSComponentAccess.readData(key, scsEnvId, listServer, listName, fieldList, filter);
		
		logger.end(className, function);
	}
	public void insertData(String key, String scsEnvId, String listServer, String listName, Map<String, String> olsEntry) {
		final String function = Request.InsertData.toString();
		logger.begin(className, function);
		
		logs(function, key, scsEnvId, listServer, listName);
		LogUtil.logArray(logger, className, function, "olsEntry", olsEntry);
		scsOLSComponentAccess.insertData(key, scsEnvId, listServer, listName, olsEntry);
		
		logger.end(className, function);
	}
	public void updateData(String key, String scsEnvId, String listServer, String listName, String olsEntryKey, Map<String, String> olsEntry) {
		final String function = Request.UpdateData.toString();
		logger.begin(className, function);
		
		logs(function, key, scsEnvId, listServer, listName);
		logger.debug(className, function, "olsEntryKey", olsEntryKey);
		LogUtil.logArray(logger, className, function, "olsEntry", olsEntry);
		scsOLSComponentAccess.updateData(key, scsEnvId, listServer, listName, olsEntryKey, olsEntry);
		
		logger.end(className, function);
	}
}
