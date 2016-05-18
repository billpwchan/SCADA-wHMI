package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RTDB_Helper {
	
	private static Logger logger = Logger.getLogger(RTDB_Helper.class.getName());
	
	public static String getPoint(String dbaddress) {
		String dbaddressTokenes[] = dbaddress.split(":");
		return dbaddressTokenes[dbaddressTokenes.length-1];
	}
	
	public static String getPointType(String point) {
		if ( null != point ) {
			if ( point.startsWith("aci") ) {
				return "aci";
			}
			if ( point.startsWith("dci") ) {
				return "dci";
			}
			if ( point.startsWith("sci") ) {
				return "sci";
			}
			if ( point.startsWith("aio") ) {
				return "aio";
			}
			if ( point.startsWith("dio") ) {
				return "dio";
			}
			if ( point.startsWith("sio") ) {
				return "sio";
			}
		}
		return null;
	}
	
	public static String removeDBStringWrapper(String value) {
		if ( null != value  ) {
			value = value.replace("[", "");
			value = value.replace("]", "");
			value = value.replace("\"", "");
			value = value.replace("[", "");	
		}

		return value;
	}
	
	public static boolean addressIsValid (String addresses) {
		boolean result = false;
		if ( null != addresses ) {
			result = true;
		} else {
			logger.log(Level.SEVERE, "addressIsValid addresses IS NULL");
		}		
		return result;
	}
	public static boolean addressesIsValid (String[] addresses) {
		boolean result = false;
		if ( null != addresses ) {
			if ( addresses.length > 0 ) {
				result = true;
				logger.log(Level.SEVERE, "setAddresses addresses.length["+addresses.length+"]");
				for ( int i = 0 ; i < addresses.length ; ++i ) {
					logger.log(Level.SEVERE, "setAddresses addresses("+i+")["+addresses[i]+"]");
				}
			} else {
				logger.log(Level.SEVERE, "setAddresses addresses length IS ZERO");
			}
		} else {
			logger.log(Level.SEVERE, "setAddresses addresses IS NULL");
		}		
		return result;
	}
	
	public static String getArrayValues(String string, int row, int col) {
		String str = null;
		
		logger.log(Level.FINE, "getArrayValues Begin");
		logger.log(Level.FINE, "getArrayValues string["+string+"] x["+row+"] y["+row+"]");
				
		if (null != string && string.length() > 0) {
			if (string.charAt(0) == '[')
				string = string.substring(1);
			if (string.charAt(string.length() - 1) == ']')
				string = string.substring(0, string.length() - 1);
			String[] strs = string.split("\\],\\[");
			if (strs.length > 0 && row < strs.length) {
				String s = strs[row];
				logger.log(Level.FINE, "getArrayValues s["+s+"]");
				//for (String s : strs) {
					//System.out.println("s [" + s + "]");
					String str2s[] = s.split(",");
					if ( str2s.length > 0 && col < str2s.length ) {
						str = str2s[col];
						logger.log(Level.FINE, "getArrayValues str["+str+"]");
						//for (String s2 : str2s) {
						//	System.out.println("s2 [" + s2 + "]");
						//}						
					}
				//}
			} else {
				// Invalid str length or index
				logger.log(Level.SEVERE, "getArrayValues Invalid str length or index");
			}
		}
		
		logger.log(Level.FINE, "getArrayValues End");

		return str;
	}
	
	public static boolean isMO(int forcedStatus) {
		return (forcedStatus & RTDB_i.intMO) == RTDB_i.intMO;
	}
	
	public static boolean isAI(int forcedStatus) {
		return (forcedStatus & RTDB_i.intAI) == RTDB_i.intAI;
	}
	
	public static boolean isSS(int forcedStatus) {
		return (forcedStatus & RTDB_i.intSS) == RTDB_i.intSS;
	}
	
	public static String getColorCSS(String alarmVector, String validity, String forcedStatus) {
		
		logger.log(Level.FINE, "getColorCSS Begin");
		
		String colorCSS	= RTDB_i.strCSSStatusGrey;
		int intAlarmVector	= 0;
		int intValidity		= 0;
		int intForcedStatus	= 0;
		try {
			intAlarmVector	= Integer.parseInt(alarmVector.split(",")[1]);
			intValidity		= Integer.parseInt(validity);
			intForcedStatus	= Integer.parseInt(forcedStatus);
		} catch ( NumberFormatException e ) {
			logger.log(Level.FINE, "getColorCSS NumberFormatException["+e.toString()+"]");
		}
		
		// 2=MO, AI=8, 512=SS
		if ( isMO(intForcedStatus) || isAI(intForcedStatus) || isSS(intForcedStatus) ) {
			colorCSS = RTDB_i.strCSSStatusBlue;
			
		// 0=invalid, 1=valid	
		} else if ( intValidity == 0 ) {
			colorCSS = RTDB_i.strCSSStatusGrey;
			
		// 0=normal, 1=alarm
		} else if ( intAlarmVector == 1 ) {
			colorCSS = RTDB_i.strCSSStatusRed;
			
		// Grey
		} else {
			colorCSS = RTDB_i.strCSSStatusGreen;
		}
		
		logger.log(Level.FINE, "getColorCSS colorCode["+colorCSS+"]");
		
		logger.log(Level.FINE, "getColorCSS End");

		return colorCSS;
	}
}