package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointType;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class DatabaseHelper {

	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(DatabaseHelper.class.getName());
	
	public static final String STR_EMPTY				= Database_i.STR_EMPTY;
	public static final String STR_UNDERSCORE			= Database_i.STR_UNDERSCORE;
	public static final String STR_COLON				= Database_i.STR_COLON;
	public static final String STR_COMMA				= Database_i.STR_COMMA;

	
	public static String getPoint(String dbaddress) {
		String dbaddressTokenes[] = dbaddress.split(STR_COLON);
		return dbaddressTokenes[dbaddressTokenes.length-1];
	}
	
	public static boolean isACIPoint(String point) { return PointType.aci == getPointType(point); }
	public static boolean isDCIPoint(String point) { return PointType.dci == getPointType(point); }
	public static boolean isSCIPoint(String point) { return PointType.sci == getPointType(point); }
	public static boolean isAIOPoint(String point) { return PointType.aio == getPointType(point); }
	public static boolean isDIOPoint(String point) { return PointType.dio == getPointType(point); }
	public static boolean isSIOPoint(String point) { return PointType.sio == getPointType(point); }

	public static PointType getPointType(String point) {
		if ( null != point ) {
			if ( point.startsWith(PointType.aci.toString()) ) { return PointType.aci; }
			if ( point.startsWith(PointType.dci.toString()) ) { return PointType.dci; }
			if ( point.startsWith(PointType.sci.toString()) ) { return PointType.sci; }
			if ( point.startsWith(PointType.aio.toString()) ) { return PointType.aio; }
			if ( point.startsWith(PointType.dio.toString()) ) { return PointType.dio; }
			if ( point.startsWith(PointType.sio.toString()) ) { return PointType.sio; }
		}
		return PointType.unknow;
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
		final String function = "addressIsValid";
		boolean result = false;
		if ( null != addresses ) {
			result = true;
		} else {
			logger.warn(function, "addresses IS NULL");
		}		
		return result;
	}
	public static boolean addressesIsValid (String[] addresses) {
		final String function = "addressesIsValid";
		boolean result = false;
		if ( null != addresses ) {
			if ( addresses.length > 0 ) {
				result = true;
				if ( logger.isTraceEnabled() ) {
					logger.trace(function, "addresses.length[{}]", addresses.length);
					for ( int i = 0 ; i < addresses.length ; ++i ) {
						logger.trace(function, "addresses({})[{}]", i, addresses[i]);
					}					
				}
			} else {
				logger.warn(function, "addresses length IS ZERO");
			}
		} else {
			logger.warn(function, "addresses IS NULL");
		}		
		return result;
	}
	
	private static String removeBegin(String string, char begin) {
		if (string.charAt(0) == begin)
			string = string.substring(1);
		return string;
	}
	
//	private static String removeEnd(String string, char end) {
//		if (string.charAt(string.length() - 1) == end)
//			string = string.substring(0, string.length() - 1);
//		return string;
//	}
	
	public static String getArrayValues(String string, int col, int row) {
		final String function = "getArrayValues";
		String str = null;
		logger.begin(function);
		logger.trace(function, "string[{}] col[{}] row[{}]", new Object[]{string, col, row});
				
		if (null != string && string.length() > 0) {

			string = removeBegin(string, '[');
			string = removeBegin(string, ']');
					
			String[] strs = string.split("\\],\\[");
			if (strs.length > 0 && col < strs.length) {
				String s = strs[col];
				s = removeBegin(s, '[');
				s = removeBegin(s, ']');
				logger.trace(function, "s[{}]", s);
//				String str2s[] = s.split(STR_COMMA);
				String str2s[] = s.split("\\s*,\\s*");
				logger.trace(function, "str2s[{}][{}]", row, str2s[row]);
				if ( str2s.length > 0 && row < str2s.length ) {
					str = str2s[row];
					logger.trace(function, "str[{}]", str);				
				}
			} else {
				// Invalid str length or index
				logger.warn(function, "Invalid str length or index");
			}
		}
		
		logger.trace(function, "str[{}]", str);
		logger.end(function);
		return str;
	}
	
	public static String[] getValueTableColumn(final String string) {
		String s = string;
		s = removeDBStringWrapper(s);
		return s.split(STR_COMMA);
	}

	public static String getAttributeValue(String address, String point, Map<String, String> dbvalues) {
		final String function = "getAttributeValue";
		logger.begin(function);
		String value = null;
		String dbaddress = address + point;
		logger.trace(function, "address[{}] point[{}] = dbaddress[{}]", new Object[]{address, point, dbaddress});
		if ( dbvalues.containsKey(dbaddress) ) {
			value = dbvalues.get(dbaddress);
		} else {
			logger.warn(function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
		}
		logger.end(function);
		return value;
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
	
	// 0=invalid, 1=valid
	public static boolean isValid(String validity) {
		int intValidity = string2int(validity, "isValid");
		return 1 == intValidity;
	}
	
	// 2=MO, AI=8, 512=SS
	public static boolean isForced(String forcedStatus) {
		int intForcedStatus = string2int(forcedStatus, "isForced");
		return ( isMO(intForcedStatus) || isAI(intForcedStatus) || isSS(intForcedStatus) );
	}
	
	// 0=normal, 1=alarm
	public static boolean isAlarm(String alarmVector) {
		boolean result = false;
		if ( null != alarmVector && alarmVector.length() > 0 ) {
			int intAlarmVector = string2int(alarmVector.split(STR_COMMA)[Database_i.isAlarmVectorRowIndex], "isAlarm");
			result = (1 == intAlarmVector);
		}
		return result;
	}
	
	// 0=Acked, 1=needAck
	public static boolean isNeedAck(String alarmVector) {
		boolean result = false;
		if ( null != alarmVector && alarmVector.length() > 0 ) {
			int intAlarmVector = string2int(alarmVector.split(STR_COMMA)[Database_i.isNeedAckAlarmVectorRowIndex], "isNeedAck");
			result = (1 == intAlarmVector);
		}
		return result;
	}
	public static int string2int(String string, String logprefix) {
		final String function = "getColorCSS";
		int result = 0;
		if ( null != string && string.length() > 0 ) {
			try {
				result	= Integer.parseInt(string);
			} catch ( NumberFormatException e ) {
				logger.warn(function, logprefix+" NumberFormatException[{}]", e.toString());
			}
		} else {
			logger.warn(function, logprefix+" string[{}] IS INVALID", string);
		}
		return result;
	}
	public static int string2int(String string) {
		return string2int(string, "");
	}
	
	public static final String strDynamic			= "dynamic";
	public static final String strStatic			= "static";
	public static final String strMultiReadValue	= "multiReadValue";
	public static final String strGetChildren		= "GetChildren";
	
	public static String getGetChildenClientKey(String source, String address, String uiPath, int screen, String tag) {
		final String function = "getGetChildenClientKey";
		String key = strGetChildren + STR_UNDERSCORE + source + STR_UNDERSCORE + strStatic + STR_UNDERSCORE + uiPath + STR_UNDERSCORE + screen + STR_UNDERSCORE + tag + STR_UNDERSCORE + address;
		logger.beginEnd(function, "key[{}]", key);
		return key;
	}

	public static String getStaticReadClienyKey(String source, String address, String uiPath, int screen, String tag) {
		final String function = "getStaticReadClienyKey";
		String key = strMultiReadValue + STR_UNDERSCORE + source + STR_UNDERSCORE + strStatic + STR_UNDERSCORE + uiPath + STR_UNDERSCORE + screen + STR_UNDERSCORE + tag + STR_UNDERSCORE + address;
		logger.beginEnd(function, "key[{}]", key);
		return key;
	}

	public static String getDyanimcClientKey(String source, String address, String uiPath, int screen, String tag) {
		final String function = "getSubscribeClientKey";
		String key = strMultiReadValue + STR_UNDERSCORE + source + STR_UNDERSCORE + strDynamic + STR_UNDERSCORE + uiPath + STR_UNDERSCORE + screen + STR_UNDERSCORE + tag + STR_UNDERSCORE + address;
		logger.beginEnd(function, "key[{}]", key);
		return key;
	}
	
	public static String getSubscribeClientKey(String source, String address, String uiPath, int screen, String tag) {
		final String function = "getSubscribeClientKey";
		String key = strMultiReadValue + STR_UNDERSCORE + source + STR_UNDERSCORE + strDynamic + STR_UNDERSCORE + uiPath + STR_UNDERSCORE + screen + STR_UNDERSCORE + tag + STR_UNDERSCORE + address;
		logger.beginEnd(function, "key[{}]", key);
		return key;
	}
}
