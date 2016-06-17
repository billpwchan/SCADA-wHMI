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
//				logger.log(Level.FINE, "addressesIsValid addresses.length["+addresses.length+"]");
//				for ( int i = 0 ; i < addresses.length ; ++i ) {
//					logger.log(Level.FINE, "addressesIsValid addresses("+i+")["+addresses[i]+"]");
//				}
			} else {
				logger.log(Level.SEVERE, "addressesIsValid addresses length IS ZERO");
			}
		} else {
			logger.log(Level.SEVERE, "addressesIsValid addresses IS NULL");
		}		
		return result;
	}
	
	private static String removeBegin(String string, char begin) {
		if (string.charAt(0) == begin)
			string = string.substring(1);
		return string;
	}
	
	private static String removeEnd(String string, char end) {
		if (string.charAt(string.length() - 1) == end)
			string = string.substring(0, string.length() - 1);
		return string;
	}
	
	public static String getArrayValues(String string, int col, int row) {
		String str = null;
		
		logger.log(Level.FINE, "getArrayValues Begin");
		logger.log(Level.SEVERE, "getArrayValues string["+string+"] col["+col+"] row["+row+"]");
				
		if (null != string && string.length() > 0) {

			string = removeBegin(string, '[');
			string = removeBegin(string, ']');
					
			String[] strs = string.split("\\],\\[");
			if (strs.length > 0 && col < strs.length) {
				String s = strs[col];
				s = removeBegin(s, '[');
				s = removeBegin(s, ']');
				logger.log(Level.FINE, "getArrayValues s["+s+"]");
//				String str2s[] = s.split(",");
				String str2s[] = s.split("\\s*,\\s*");
				logger.log(Level.FINE, "getArrayValues str2s["+row+"]["+str2s[row]+"]");
				if ( str2s.length > 0 && row < str2s.length ) {
					str = str2s[row];
					logger.log(Level.FINE, "getArrayValues str["+str+"]");				
				}
			} else {
				// Invalid str length or index
				logger.log(Level.SEVERE, "getArrayValues Invalid str length or index");
			}
		}
		
		logger.log(Level.SEVERE, "getArrayValues str["+str+"]");
		
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
		
		if ( null != alarmVector && null != validity && null != forcedStatus ) {
			if ( alarmVector.length() > 0 && validity.length() > 0 && forcedStatus.length() > 0 ) {
				
				int intAlarmVector	= 0;
				int intValidity		= 0;
				int intForcedStatus	= 0;
				try {
		
					intAlarmVector	= Integer.parseInt(alarmVector.split(",")[1]);
					intValidity		= Integer.parseInt(validity);
					intForcedStatus	= Integer.parseInt(forcedStatus);
				} catch ( NumberFormatException e ) {
					logger.log(Level.SEVERE, "getColorCSS NumberFormatException["+e.toString()+"]");
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
			} else {
				logger.log(Level.SEVERE, "getColorCSS alarmVector["+alarmVector.length()+"] validity["+validity.length()+"] forcedStatus["+forcedStatus.length()+"] length IS INVALID");
			}
		} else {
			logger.log(Level.SEVERE, "getColorCSS alarmVector["+alarmVector+"] validity["+validity+"] forcedStatus["+forcedStatus+"] IS NULL");
		}
		
		logger.log(Level.FINE, "getColorCSS colorCode["+colorCSS+"]");
		
		logger.log(Level.FINE, "getColorCSS End");

		return colorCSS;
	}
}
