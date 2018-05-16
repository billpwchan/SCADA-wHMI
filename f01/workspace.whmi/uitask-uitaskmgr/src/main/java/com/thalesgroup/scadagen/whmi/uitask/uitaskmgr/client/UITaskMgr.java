package com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client;

import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasksplit.client.UITaskSplit;

public class UITaskMgr {
	
	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(UITaskMgr.class.getName());

	
	public static UITask_i getTask(String taskname) {
		final String function = "getTask";
		
		logger.begin(function);
		
		logger.info(function, " taskname[{}]", taskname);
		
		UITask_i uitask_i = null;
		
		if ( 0 == taskname.compareTo("UITaskLaunch") ) {
			uitask_i = new UITaskLaunch();
		} 
		else if ( 0 == taskname.compareTo("UITaskHistory") ) {
			uitask_i = new UITaskHistory();
		}
		else if ( 0 == taskname.compareTo("UITaskTitle") ) {
			uitask_i = new UITaskTitle();
		} 
		else if ( 0 == taskname.compareTo("UITaskSplit") ) {
			uitask_i = new UITaskSplit();
		}

		logger.end(function);

		return uitask_i;
	}
}
