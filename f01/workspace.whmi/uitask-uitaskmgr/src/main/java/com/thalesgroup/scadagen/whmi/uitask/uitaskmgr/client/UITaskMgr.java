package com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client;

import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskProfile;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uitask.uitasksplit.client.UITaskSplit;

public class UITaskMgr {
	
	private final static String className = UIWidgetUtil.getClassSimpleName(UITaskMgr.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	
	public static UITask_i getTask(String taskname) {
		final String function = "getTask";
		
		logger.begin(className, function);
		
		logger.info(className, function, " taskname[{}]", taskname);
		
		UITask_i uitask_i = null;
		
		if ( 0 == taskname.compareTo("UITaskLaunch") ) {
			uitask_i = new UITaskLaunch();
		} else if ( 0 == taskname.compareTo("UITaskHistory") ) {
			uitask_i = new UITaskHistory();
		} else if ( 0 == taskname.compareTo("UITaskOperator") ) {
			uitask_i = new UITaskProfile();
		} else if ( 0 == taskname.compareTo("UITaskTitle") ) {
			uitask_i = new UITaskTitle();
		} else if ( 0 == taskname.compareTo("UITaskSplit") ) {
			uitask_i = new UITaskSplit();
		}

		logger.end(className, function);

		return uitask_i;
	}

	// private String getSimpleName(Class<T> type) {
		// String strClassName = "";
		// String strFullClassName = type.getName();
		// int firstChar;
		// firstChar = strFullClassName.lastIndexOf ('.') + 1;
		// if ( firstChar > 0 ) {
			// strClassName = strFullClassName.substring ( firstChar );
		// }
		// return strClassName;
	// }

}
