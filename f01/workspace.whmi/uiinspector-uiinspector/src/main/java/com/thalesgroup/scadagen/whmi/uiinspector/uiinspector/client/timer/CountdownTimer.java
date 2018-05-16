package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.timer;

import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class CountdownTimer {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Timer timer = null;

	private int timerExpiredMillSecond = 60*1000;
	public void setTimerExpiredMillSecond(int timerExpiredMillSecond) {
		 this.timerExpiredMillSecond = timerExpiredMillSecond;
	}
	
	private CountdownTimerEvent countdownTimerEvent = null;
	
	public void setCountdownTimerEvent(CountdownTimerEvent countdownTimerEvent) {
		this.countdownTimerEvent = countdownTimerEvent;
	}
	
	public void init() {
		final String function = "init";
		logger.beginEnd(function);
	}
	
	public void update() {
		final String function = "update";
		
		logger.begin(function);
		
		if ( null != timer ) {
			logger.debug(function, "autoClose timer extended [{}]", timerExpiredMillSecond);
			timer.cancel();
			timer.schedule(timerExpiredMillSecond);
		} else {
			logger.warn(function, "autoCloseTimer IS NULL");
		}
		
		logger.end(function);
	}
	
	public void start() {
		final String function = "start";
		
		logger.begin(function);
		
		logger.debug(function, "timer enabled, creating timer");
		timer = new Timer() {
			@Override
			public void run() {
				final String function = "Timer run";
				logger.begin(function);
				logger.debug(function, "timer timerExpiredMillSecond[{}] trigged", timerExpiredMillSecond);
				close();
				
				if ( null != countdownTimerEvent ) countdownTimerEvent.timeup();
				
				logger.end(function);
			}
		};
		logger.debug(function, "timer enabled, schedule timer timerExpiredMillSecond[{}]", timerExpiredMillSecond);
		timer.schedule(timerExpiredMillSecond);			

		logger.end(function);
	}

	public void close() {
		final String function = "close";
		
		logger.begin(function);
		
		if ( null != timer ) {
			timer.cancel();
			timer = null;
		} else {
			logger.debug(function, "timer IS NULL");
		}
	
		
		logger.end(function);
	}
}
