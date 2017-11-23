package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.dbm;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

public class UIWidgetVerifyScsRTDBComponentControl extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyScsRTDBComponentControl.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
//	private String strDatabase = "Database";
//	
//	private Subject getSubject() {
//		final String function = "getSubject";
//		
//		logger.begin(className, function);
//		
//		Subject subject = new Subject();
//		Observer observer = new Observer() {
//
//			@Override
//			public void setSubject(Subject subject) {
//				this.subject = subject;	
//				this.subject.attach(this);
//			}
//
//			@Override
//			public void update() {
//				logger.debug(className, function, "update");
//				JSONObject obj = this.subject.getState();
//				uiGeneric.setWidgetValue("resultvalue", obj.toString());
//			}
//			
//		};
//		observer.setSubject(subject);
//
//		logger.end(className, function);
//		
//		return subject;
//	}
	
	private void writeDateValueRequest() {
		final String function = "writeDateValueRequest";
		logger.begin(className, function);
		
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String address		= uiGeneric.getWidgetValue("addressvalue");
		long second			= Long.parseLong(uiGeneric.getWidgetValue("second"));
		long usecond		= Long.parseLong(uiGeneric.getWidgetValue("usecond"));
		
		scsRTDBComponentAccess.writeDateValueRequest(key, scsEnvId, address, second, usecond);
		
		logger.end(className, function);
	}
	
	private void writeIntValueRequest() {
		final String function = "writeIntValueRequest";
		logger.begin(className, function);
		
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String address		= uiGeneric.getWidgetValue("addressvalue");
		int value			= Integer.parseInt(uiGeneric.getWidgetValue("value"));
		
		scsRTDBComponentAccess.writeIntValueRequest(key, scsEnvId, address, value);
		
		logger.end(className, function);
	}
	
	private void writeFloatValueRequest() {
		final String function = "writeFloatValueRequest";
		logger.begin(className, function);
		
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String address		= uiGeneric.getWidgetValue("addressvalue");
		float value			= Float.parseFloat(uiGeneric.getWidgetValue("value"));
		
		scsRTDBComponentAccess.writeFloatValueRequest(key, scsEnvId, address, value);
		
		logger.end(className, function);
	}
	
	private void writeStringValueRequest() {
		final String function = "writeStringValueRequest";
		logger.begin(className, function);
		
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String address		= uiGeneric.getWidgetValue("addressvalue");
		String value		= uiGeneric.getWidgetValue("value");
		
		scsRTDBComponentAccess.writeStringValueRequest(key, scsEnvId, address, value);
		
		logger.end(className, function);
	}
	
	private void writeValueRequest() {		
		final String function = "writeValueRequest";
		logger.begin(className, function);
		
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String address		= uiGeneric.getWidgetValue("addressvalue");
		String value		= uiGeneric.getWidgetValue("value");
		
		scsRTDBComponentAccess.writeValueRequest(key, scsEnvId, address, value);
		
		logger.end(className, function);
	}

	private ScsRTDBComponentAccess scsRTDBComponentAccess = null;
	
	public void connect() {
		String function = "connect";
		logger.begin(className, function);
		
		scsRTDBComponentAccess = new ScsRTDBComponentAccess(new IRTDBComponentClient() {
			
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
			public void setWriteValueRequestResult(String clientKey, int errorCode, String errorMessage) {
				uiGeneric.setWidgetValue("resultvalue", "clientKey:[" + clientKey + "|] errorCode:[" + errorCode + "] errorMessage:[" + errorMessage + "]");
			}
			
			@Override
			public void setSetAttributeFormulaResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setReadResult(String key, String[] value, int errorCode, String errorMessage) {
//				if ( null != scsRTDBComponentAccessResult ) {
//					scsRTDBComponentAccessResult.setReadResult(key, value, errorCode, errorMessage);
//				}
			}
			
			@Override
			public void setQueryByNameResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetUserFormulasNamesResult(String clientKey, String[] formulas, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetUserClassIdResult(String key, int cuid, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetInstancesByUserClassIdResult(String clientKey, String[] instances, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetInstancesByClassNameResult(String clientKey, String[] instances, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetInstancesByClassIdResult(String key, String[] values, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetFullPathResult(String clientKey, String fullPath, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetDatabaseInfoResult(String clientKey, long dbsize, int nbClasses, int nbFormula, int nbInstances,
					String scsPath, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetClassesResult(String key, String[] values, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetClassNameResult(String key, String className, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetClassInfoResult(String clientKey, DbmClassInfo cinfo, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetClassIdResult(String key, int cid, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
//				if ( null != scsRTDBComponentAccessGetChildrenResult ) {
//					scsRTDBComponentAccessGetChildrenResult.setGetChildrenResult(clientKey, instances, errorCode, errorMessage);
//				}
			}
			
			@Override
			public void setGetChildrenAliasesResult(String clientKey, String[] values, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetCEOperResult(String clientKey, int[] ceModes, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetAttributesResult(String clientKey, String[] attributes, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetAttributesDescriptionResult(String clientKey, ScsClassAttInfo[] attributesInfo, int errorCode,
					String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetAttributeStructureResult(String clientKey, String attType, int fieldCount, int recordCount,
					int recordSize, String[] fieldNames, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetAttributeFormulasResult(String clientKey, String[] formulas, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setGetAliasResult(String clientKey, String alias, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setForceSnapshotResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setControlCEResult(String clientKey, int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		});
		
		logger.end(className, function);
	}
	
	private void disconnect() {
		final String function = "disconnect";
		logger.begin(className, function);
		
		try {
			scsRTDBComponentAccess.terminate();
		} catch (IllegalStatePresenterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.end(className, function);
	}
	
	private void launch(String element) {
		final String function = "launch";
		logger.begin(className, function);
		logger.debug(className, function, "element[{}]", element);
		
		if ( 0 == "connect".compareToIgnoreCase(element) ) {
			connect();
		}
		else if ( 0 == "disconnect".compareToIgnoreCase(element) ) {
			disconnect();
		}
		else if ( 0 == "writeDateValueRequest".compareToIgnoreCase(element) ) {
			writeDateValueRequest();
		}
		else if ( 0 == "writeIntValueRequest".compareToIgnoreCase(element) ) {
			writeIntValueRequest();
		}
		else if ( 0 == "writeFloatValueRequest".compareToIgnoreCase(element) ) {
			writeFloatValueRequest();
		}
		else if ( 0 == "writeStringValueRequest".compareToIgnoreCase(element) ) {
			writeStringValueRequest();
		}
		else if ( 0 == "writeValueRequest".compareToIgnoreCase(element) ) {
			writeValueRequest();
		}
		logger.end(className, function);
	}

	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);

		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				final String function = "onClick";
				logger.begin(className, function);
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.debug(className, function, "element[{}]", element);
						if ( null != element ) {
							launch(element);
						}
					}
				}
				logger.end(className, function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				// TODO Auto-generated method stub
				
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
				// TODO Auto-generated method stub
			}
		
			@Override
			public void envUp(String env) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void envDown(String env) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);
				envDown(null);
				logger.begin(className, function);
			};
		};

		logger.end(className, function);
	}
	
}
