package com.thalesgroup.scadagen.wrapper.wrapper.client;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
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
			public void setSubscribeResult(String clientKey, String subUUID, String pollerState, String[] dbaddress,
					String[] values, int errorCode, String errorMessage) {
				if (errorCode != 0) {
					logger.error("subscribe failed. clientKey=" + clientKey + "  " + errorMessage);
				} else {
					logger.info("subscribe success. clientKey=" + clientKey + " subscriptionID=" + subUUID);
					
					setSubscribeResult(clientKey, subUUID, pollerState, dbaddress, values, errorCode, errorMessage);
				}
			}

			@Override
			public void setUnSubscribeResult(String clientKey, int errorCode, String errorMessage) {
				if (errorCode != 0) {
					logger.error("unsubscribe failed. clientKey=" + clientKey + "  " + errorMessage);
				} else {
					logger.info("unsubscribe success. clientKey=" + clientKey);
				}
			}

			
		});
	}
	
	public void deleteGroup(String key, String scsEnvId, String groupName) {
		poller_.deleteGroup(key, scsEnvId, groupName);
	}
	
	public void subscribe(String key, String scsEnvId, String groupName, String[] dataFields, int periodMS) {
		poller_.subscribe(key, scsEnvId, groupName, dataFields, periodMS);
	}
	
	public void unSubscribe(String key, String scsEnvId, String groupName, String subscriptionId) {
		poller_.unSubscribe(key, scsEnvId, groupName, subscriptionId);
	}

}
