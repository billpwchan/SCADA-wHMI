package com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskProfile;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;

public class UITaskMgr {
	
	private static Logger logger = Logger.getLogger(UITaskMgr.class.getName());
	
	public static UITask_i getTask(String taskname) {
		
		logger.log(Level.FINE, "getTask Begin");
		
		UITask_i uitask_i = null;
		
		if ( 0 == taskname.compareTo("UITaskLaunch") ) {
			uitask_i = new UITaskLaunch();
		} else if ( 0 == taskname.compareTo("UITaskHistory") ) {
			uitask_i = new UITaskHistory();
		} else if ( 0 == taskname.compareTo("UITaskOperator") ) {
			uitask_i = new UITaskProfile();
		} else if ( 0 == taskname.compareTo("UITaskTitle") ) {
			uitask_i = new UITaskTitle();
		}

		/*
		switch (taskname) {
		case "UITaskLaunch": {
			uitask_i = new UITaskLaunch();
		}
			break;
		case "UITaskTitle": {
			uitask_i = new UITaskTitle();
		}
			break;
		case "UITaskHistory": {
			uitask_i = new UITaskHistory();
		}
			break;
		default:
			break;
		}
		*/
		
		logger.log(Level.FINE, "getTask End");

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

	public static <T> boolean isInstanceOf(Class<T> type, Object object) {

		logger.log(Level.FINE, "isInstanceOf Begin");
		logger.log(Level.FINE, "isInstanceOf type["+type+"]");
		logger.log(Level.FINE, "isInstanceOf object["+object+"]");
		
		boolean result = false;
		
		String strClassName1 = "";
		String strFullClassName1 = type.getName();
		int firstChar1;
		firstChar1 = strFullClassName1.lastIndexOf ('.') + 1;
		if ( firstChar1 > 0 ) {
			strClassName1 = strFullClassName1.substring ( firstChar1 );
		}
		
		String strClassName2 = "";
		String strFullClassName2 = object.getClass().getName();
		int firstChar2;
		firstChar2 = strFullClassName2.lastIndexOf ('.') + 1;
		if ( firstChar2 > 0 ) {
			strClassName2 = strFullClassName2.substring ( firstChar2 );
		}
		
		String strTypeClassName = strClassName1;

		String strObjectClassName = strClassName2;
		
		//String strTypeClassName = type.getName();
		
		//String strObjectClassName = object.getClass().getName();
		
		logger.log(Level.FINE, "isInstanceOf strTypeClassName["+strTypeClassName+"] == strObjectClassName["+strObjectClassName+"]");
		
		if ( 0 == strTypeClassName.compareTo(strObjectClassName) ) {
			result = true;
		}
		
//		try {
//			T objectAsType = (T) object;
//			result = true;
//		} catch (ClassCastException exception) {
//
//		}
		
		logger.log(Level.FINE, "isInstanceOf result["+result+"]");
		
		logger.log(Level.FINE, "isInstanceOf End");
		
		return result;
	}
}
