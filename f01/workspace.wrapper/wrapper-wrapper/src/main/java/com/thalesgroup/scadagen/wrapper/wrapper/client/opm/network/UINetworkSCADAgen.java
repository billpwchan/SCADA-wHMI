package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.network;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentIpAddressCallback_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentHostNameCallback_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric.UIGenericMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric.UIGenericMgrEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.uiaction.UIActionOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.UIGenericServiceImpl_i;

public class UINetworkSCADAgen implements UINetwork_i {
	

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static UINetwork_i instance = null;
	public static UINetwork_i getInstance() { 
		if ( null == instance ) instance = new UINetworkSCADAgen();
		return instance;
	}
	private UINetworkSCADAgen () {}
	
	private String currentHostName = null;

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentHostName()
	 */
	@Override
	public String getCurrentHostName() {
		final String function = "getCurrentHostName";
		logger.begin(function);
		
		if ( null == currentHostName ) {
			this.getCurrentHostName(new GetCurrentHostNameCallback_i() {

				@Override
				public void callback(String hostName) {
					
					currentHostName = hostName;
				}
			});
		}
		logger.debug(function, "currentHostName[{}]", currentHostName);
		logger.end(function);
		return currentHostName;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentHostName(com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.IGetCurrentHostNameCallback)
	 */
	@Override
	public void getCurrentHostName(final GetCurrentHostNameCallback_i cb) {
		final String function = "getCurrentHostName";
		logger.begin(function);
		
		if ( null == currentHostName ) {
			JSONObject request = new JSONObject();
	        request.put(UIGenericServiceImpl_i.OperationAttribute3, new JSONString(UIActionOpm_i.ComponentName));
	        request.put(UIGenericServiceImpl_i.OperationAttribute4, new JSONString(UIActionOpm_i.GetCurrentHostName));
			
			UIGenericMgr uiGenericMgr = new UIGenericMgr();
			uiGenericMgr.execute(request, new UIGenericMgrEvent() {
				
				@Override
				public void uiGenericMgrEventReady(JSONObject response) {
					final String function2 = function + " uiGenericMgrEventReady";
					logger.begin(function2);
					if ( null != response ) {
						logger.debug(function2, "response[{}]", response.toString());
						JSONValue v = response.get(UIGenericServiceImpl_i.OperationParameter1);
						if ( null != v && null != v.isObject() ) {
							JSONObject o = v.isObject();
							if ( null != o ) {
								JSONValue tv = o.get(UIGenericServiceImpl_i.OperationValue1);
								if ( null != tv && null != tv.isString() ) {
									currentHostName = tv.isString().stringValue();
								} else {
									logger.warn(function2, "tv[{}] IS INVALID", tv);
								}
							} else {
								logger.warn(function2, "o IS NULL");
							}
						} else {
							logger.warn(function2, "v[{}] IS INVALID", v);
						}
						logger.debug(function2, "currentHostName[{}]", currentHostName);
					} else {
						logger.warn(function2, "response IS NULL");
					}
					
					cb.callback(currentHostName);
					logger.end(function2);
				}
	
				@Override
				public void uiGenericMgrEventFailed(JSONObject response) {
					final String function2 = function + " uiGenericMgrEventFailed";
					logger.beginEnd(function2);
				}
			});			
		}
		logger.debug(function, "currentHostName[{}]", currentHostName);
		logger.end(function);
	}
	
	private String currentIPAddress = null;
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentIPAddress()
	 */
	@Override
	public String getCurrentIPAddress() {
		final String function = "getCurrentIPAddress";
		logger.begin(function);
		
		if ( null == currentIPAddress ) {
			
			this.getCurrentIPAddress(new GetCurrentIpAddressCallback_i() {

				@Override
				public void callback(String ipAddress) {
					
					currentIPAddress = ipAddress;
				}
				
			});
		}
		logger.debug(function, "currentIPAddress[{}]", currentIPAddress);
		logger.end(function);
		return currentIPAddress;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentIPAddress(com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentIpAddressCallback_i)
	 */
	@Override
	public void getCurrentIPAddress(final GetCurrentIpAddressCallback_i cb) {
		final String function = "getCurrentIPAddress";
		logger.begin(function);
		
		UIGenericMgr uiGenericMgr = new UIGenericMgr();
		JSONObject request = new JSONObject();
        request.put(UIGenericServiceImpl_i.OperationAttribute3, new JSONString(UIActionOpm_i.ComponentName));
        request.put(UIGenericServiceImpl_i.OperationAttribute4, new JSONString(UIActionOpm_i.GetCurrentIPAddress));
		
		uiGenericMgr.execute(request, new UIGenericMgrEvent() {
			
			@Override
			public void uiGenericMgrEventReady(JSONObject response) {
				final String function2 = function + " uiGenericMgrEventReady";
				logger.begin(function2);
				if ( null != response ) {
					logger.debug(function2, "response[{}]", response.toString());
					JSONValue v = response.get(UIGenericServiceImpl_i.OperationParameter1);
					if ( null != v && null != v.isObject() ) {
						JSONObject o = v.isObject();
						if ( null != o ) {
							JSONValue tv = o.get(UIGenericServiceImpl_i.OperationValue1);
							if ( null != tv && null != tv.isString() ) {
								currentIPAddress = tv.isString().stringValue();
							} else {
								logger.warn(function2, "tv[{}] IS INVALID", tv);
							}
						} else {
							logger.warn(function2, "o IS NULL");
						}
					} else {
						logger.warn(function2, "v[{}] IS INVALID", v);
					}
					logger.debug(function2, "currentIPAddress[{}]", currentIPAddress);
				} else {
					logger.debug(function2, "response IS NULL");
				}
				
				cb.callback(currentIPAddress);
				logger.end(function2);
			}

			@Override
			public void uiGenericMgrEventFailed(JSONObject response) {
				final String function2 = function + " uiGenericMgrEventFailed";
				logger.beginEnd(function2);
			}
		});

		logger.end(function);
	}

	@Override
	public void init() {
	}
}
