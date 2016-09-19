package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.dialog;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.timer.CountdownTimer;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.timer.CountdownTimerEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.HVID2SCS;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.ReadProp;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIInspectorMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static HashMap<String, UIInspectorMgr> instances = new HashMap<String, UIInspectorMgr>();
	private UIInspectorMgr () {}
	public static UIInspectorMgr getInstance(String key) {
		if ( ! instances.containsKey(key) ) { instances.put(key, new UIInspectorMgr()); }
		UIInspectorMgr instance = instances.get(key);
		return instance;
	}
	
	private UIInspectorDialogBox uiInspectorDialogbox = null;
	private CountdownTimer countdownTimer = null;
	
	public boolean openInspectorDialog(UINameCard uiNameCard, String hvid, int mouseX, int mouseY) {
		
		final String function = "openInspectorDialog";
		logger.begin(className, function);
		
		boolean result = false;
		
		final String dictionariesCacheName = UIInspector_i.strUIInspector;
		final String inspDialogBoxPropPrefix = "inspectorpaneldialogbox.";
		final String inspDialogBoxProp = inspDialogBoxPropPrefix+"properties";

		final String strModal = "modal";
		boolean modal = ReadProp.readBoolean(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strModal, true);
		
		HVID2SCS hvid2scs = new HVID2SCS();
		hvid2scs.setHVID(hvid);
		hvid2scs.init();
		
		String scsEnvId		= hvid;
		String dbaddress	= hvid2scs.getDBAddress();
		
		logger.info(className, function, "hvid[{}]", hvid);
		logger.info(className, function, "scsEnvId[{}] dbaddress[{}]", scsEnvId, dbaddress);
		logger.info(className, function, "mouseX[{}] mouseY[{}]", mouseX, mouseY);

		
		logger.info(className, function, "modal[{}]", modal);
		
		if ( null == uiInspectorDialogbox ) {
			
			logger.info(className, function, "Creating uiInspectorDialogbox...");
			
			result = true;
			
			uiInspectorDialogbox = new UIInspectorDialogBox();
			
			uiInspectorDialogbox.setUINameCard(uiNameCard);
			uiInspectorDialogbox.init();
			uiInspectorDialogbox.getMainPanel();
			
			uiInspectorDialogbox.setMousePosition(mouseX, mouseY);
			uiInspectorDialogbox.show();
			
			uiInspectorDialogbox.setParent(scsEnvId, dbaddress);
			
			uiInspectorDialogbox.setModal(modal);
			
			uiInspectorDialogbox.setUIInspectorDialogBoxEvent(new UIInspectorDialogBoxEvent() {
				
				@Override
				public void onOpen() {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onClick() {
					final String function = "onClick";
					logger.begin(className, function);
					updateInspectorDialogTimer();
					logger.end(className, function);
				}
				
				@Override
				public void onClose() {	
					final String function = "onClose";
					logger.begin(className, function);
					closeInspectorDialog();
					logger.end(className, function);
				}
				
			});
							
			uiInspectorDialogbox.connect();
			
			
			final String strAutoCloseEnable = "autoCloseEnable";
			final String strAutoCloseExpiredMillSecond = "autoCloseExpiredMillSecond";
			
			boolean autoCloseEnable = ReadProp.readBoolean(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strAutoCloseEnable, false);
			int autoCloseExpiredMillSecond = ReadProp.readInt(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strAutoCloseExpiredMillSecond, 60*1000);
			
			logger.info(className, function, "autoCloseEnable[{}]", autoCloseEnable);
			logger.info(className, function, "autoCloseExpiredMillSecond[{}]", autoCloseExpiredMillSecond);
			
			if ( autoCloseEnable ) {
				
				countdownTimer = new CountdownTimer();
				countdownTimer.setTimerExpiredMillSecond(autoCloseExpiredMillSecond);
				countdownTimer.setCountdownTimerEvent(new CountdownTimerEvent() {
					
					@Override
					public void timeup() {
						final String function = "timeup";
						logger.begin(className, function);
						killInspectorDialogTimer();
						closeInspectorDialog();
						logger.end(className, function);
					}
				});
				
				countdownTimer.init();
				countdownTimer.close();
				countdownTimer.start();
				
			} else {
				logger.info(className, function, "timer disabled");
			}

		} else {
			logger.info(className, function, "uiInspectorDialogbox IS NOT NULL");
		}
		
		logger.end(className, function);
		
		return result;
	}
	
	private void updateInspectorDialogTimer() {
		final String function = "updateInspectorDialogTimer";
		logger.begin(className, function);
		if ( null != countdownTimer ) countdownTimer.update();
		logger.end(className, function);
	}
	
	private void killInspectorDialogTimer() {
		final String function = "killInspectorDialogTimer";
		logger.begin(className, function);
		if ( null != countdownTimer ) {
			countdownTimer.close();
			countdownTimer = null;
		}
		logger.end(className, function);
	}
	
	public void closeInspectorDialog() {
		final String function = "closeInspectorDialog";
		logger.begin(className, function);
		if ( null != uiInspectorDialogbox ) {
			killInspectorDialogTimer();
			uiInspectorDialogbox.disconnect();
			uiInspectorDialogbox.hide();
		}
		uiInspectorDialogbox = null;
		logger.end(className, function);
	}
}
