package com.thalesgroup.scadagen.wrapper.wrapper.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.poller.SubscribeResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.poller.UnSubscribeResult;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.poller.IPollerComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.poller.ScsPollerComponentAccess;

public class WrapperScsPollerAccess {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(WrapperScsPollerAccess.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private static WrapperScsPollerAccess instance = null;
	public static WrapperScsPollerAccess getInstance() {
		if ( null == instance ) instance = new WrapperScsPollerAccess();
		return instance;
	}
	
	private ScsPollerComponentAccess poller_ = null;
	
	private Map<String, SubscribeResult> subMap = new HashMap<String, SubscribeResult>();
	
	private Map<String, UnSubscribeResult> unSubMap = new HashMap<String, UnSubscribeResult>();
	
	private WrapperScsPollerAccess () {
		poller_ = new ScsPollerComponentAccess(new IPollerComponentClient() {

			@Override
			public void setPresenter(HypervisorPresenterClientAbstract<? extends HypervisorView> presenter) {

			}

			@Override
			public Widget asWidget() {
				return null;
			}

			@Override
			public void destroy() {
			}

			@Override
			public void setDeleteGroupResult(String key, int errorCode, String errorMessage) {
				
			}

			@Override
			public void setSubscribeResult(String clientKey, String subUUID, String pollerState, String[] dbaddresses,
					String[] values, int errorCode, String errorMessage) {
				
				SubscribeResult subRes = subMap.get(clientKey);

				if (subRes != null) {
					subRes.setSubscribeResult(clientKey, subUUID, pollerState, dbaddresses, values, errorCode, errorMessage);
					
					if (errorCode != 0) {
						logger.error("subscribe failed. clientKey=" + clientKey + "  " + errorMessage);
						subRes.notifyError();
					} else {
						logger.debug("subscribe success. clientKey=" + clientKey + " subscriptionID=" + subUUID);
						subRes.update();
					}
				}
			}

			@Override
			public void setUnSubscribeResult(String clientKey, int errorCode, String errorMessage) {
				
				UnSubscribeResult unSubRes = unSubMap.get(clientKey);
				
				if (unSubRes != null) {
					unSubRes.setUnSubscribeResult(clientKey, errorCode, errorMessage);

					if (errorCode != 0) {
						logger.error("unsubscribe failed. clientKey=" + clientKey + "  " + errorMessage);
						unSubRes.notifyError();
					} else {
						logger.debug("unsubscribe success. clientKey=" + clientKey);
						unSubRes.update();
					}
				}
			}
		});
	}
	
	public void deleteGroup(String key, String scsEnvId, String groupName) {
		poller_.deleteGroup(key, scsEnvId, groupName);
	}
	
	public void subscribe(String key, String scsEnvId, String groupName, String[] dataFields, int periodMS, SubscribeResult subResult) {
		poller_.subscribe(key, scsEnvId, groupName, dataFields, periodMS);
		subMap.put(key, subResult);
	}
	
	public void unSubscribe(String key, String scsEnvId, String groupName, String subscriptionId, UnSubscribeResult unsubResult) {
		poller_.unSubscribe(key, scsEnvId, groupName, subscriptionId);
		unSubMap.put(key, unsubResult);
	}

}
