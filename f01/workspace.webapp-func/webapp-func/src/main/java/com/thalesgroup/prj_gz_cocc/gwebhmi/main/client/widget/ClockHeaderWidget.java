package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.widget;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;

public class ClockHeaderWidget extends HTML {
	private Timer t;
	private DateTimeFormat format_;
    
	public ClockHeaderWidget() {
		this(DateTimeFormat.getFormat(Dictionary.getWording("clock-widget-format")));
	}
    
	public ClockHeaderWidget(DateTimeFormat format) {
          
		this.setStyleName("gwt-Button");
          
		if(format != null) {
			format_ = format;
		}
          
		// Display current date/time
		this.updateClock();
          
		// Create a new timer that calls Window.alert().
		t = new Timer() {
			@Override
			public void run() {updateClock();}
		};

		// Schedule the timer to run every second.
		t.scheduleRepeating(1000);
	}

	private void updateClock() {
		this.setHTML(format_.format(new Date()));
	}

}
