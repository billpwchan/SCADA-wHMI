package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class RTDB_Helper {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(RTDB_Helper.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static final int valueAlarmVectorIndex = 1;
	
	public enum PointType {
		unknow("POINT_TYPE_UNKNOW")
		, aci("aci")
		, dci("dci")
		, sci("sci")
		, aio("aio")
		, dio("dio")
		, sio("sio")
		;
		
		private final String text;
		private PointType(final String text) { this.text = text; }
//		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}

	public enum PointName {
		
		// Static 
		label(".label")
		, shortLabel(".shortLabel")
		, geographicalCat(".geographicalCat")
		, aalValueTable(":aal.valueTable")
		, dalValueTable(":dal.valueTable")
		, salValueTable(":sal.valueTable")
		
		, hmiOrder(".hmiOrder")
		, valueTable(".valueTable")
		
		// Dynamic
		, value(".value")
		, validity(".validity") // 0=invalid, 1=valid
		, isControlable(".isControlable")
		
		, aalValueAlarmVector(":aal.valueAlarmVector") // (0,1)==0 = normal, (0,1)==1 = alarm
		, dalValueAlarmVector(":dal.valueAlarmVector") // (0,1)==0 = normal, (0,1)==1 = alarm
		, salValueAlarmVector(":sal.valueAlarmVector") // (0,1)==0 = normal, (0,1)==1 = alarm
		
		, afoForcedStatus(":afo.forcedStatus") // 2=MO, AI=8, 512=SS //dfo.forcedStatus
		, dfoForcedStatus(":dfo.forcedStatus") // 2=MO, AI=8, 512=SS //dfo.forcedStatus
		, sfoForcedStatus(":sfo.forcedStatus") // 2=MO, AI=8, 512=SS //dfo.forcedStatus
		
		// Equipment Reservation
		, reserved(".reserved")
		, resrvReservedID(".resrvReservedID")
		, resrvReserveReqID(".resrvReserveReqID")
		, resrvUnreserveReqID(".resrvUnreserveReqID")
		;
		private final String text;
		private PointName(final String text) { this.text = text; }
//		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
	
	public static String getPoint(String dbaddress) {
		String dbaddressTokenes[] = dbaddress.split(":");
		return dbaddressTokenes[dbaddressTokenes.length-1];
	}

	public static PointType getPointType(String point) {
		if ( null != point ) {
			if ( point.startsWith(PointType.aci.toString()) ) {
				return PointType.aci;
			}
			if ( point.startsWith(PointType.dci.toString()) ) {
				return PointType.dci;
			}
			if ( point.startsWith(PointType.sci.toString()) ) {
				return PointType.sci;
			}
			if ( point.startsWith(PointType.aio.toString()) ) {
				return PointType.aio;
			}
			if ( point.startsWith(PointType.dio.toString()) ) {
				return PointType.dio;
			}
			if ( point.startsWith(PointType.sio.toString()) ) {
				return PointType.sio;
			}
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
			logger.warn(className, function, "addresses IS NULL");
		}		
		return result;
	}
	public static boolean addressesIsValid (String[] addresses) {
		final String function = "addressesIsValid";
		boolean result = false;
		if ( null != addresses ) {
			if ( addresses.length > 0 ) {
				result = true;
				if ( logger.isDebugEnabled() ) {
					logger.debug(className, function, "addresses.length[{}]", addresses.length);
					for ( int i = 0 ; i < addresses.length ; ++i ) {
						logger.debug(className, function, "addresses({})[{}]", i, addresses[i]);
					}					
				}
			} else {
				logger.warn(className, function, "addresses length IS ZERO");
			}
		} else {
			logger.warn(className, function, "addresses IS NULL");
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
		
		logger.begin(className, function);
		logger.debug(className, function, "string[{}] col[{}] row[{}]", new Object[]{string, col, row});
				
		if (null != string && string.length() > 0) {

			string = removeBegin(string, '[');
			string = removeBegin(string, ']');
					
			String[] strs = string.split("\\],\\[");
			if (strs.length > 0 && col < strs.length) {
				String s = strs[col];
				s = removeBegin(s, '[');
				s = removeBegin(s, ']');
				logger.debug(className, function, "s[{}]", s);
//				String str2s[] = s.split(",");
				String str2s[] = s.split("\\s*,\\s*");
				logger.debug(className, function, "str2s[{}][{}]", row, str2s[row]);
				if ( str2s.length > 0 && row < str2s.length ) {
					str = str2s[row];
					logger.debug(className, function, "str[{}]", str);				
				}
			} else {
				// Invalid str length or index
				logger.warn(className, function, "Invalid str length or index");
			}
		}
		
		logger.debug(className, function, "str[{}]", str);
		
		logger.end(className, function);

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
		final String function = "getColorCSS";
		
		logger.begin(className, function);
		
		String colorCSS	= RTDB_i.strCSSStatusGrey;
		
		if ( null != alarmVector && null != validity && null != forcedStatus ) {
			if ( alarmVector.length() > 0 && validity.length() > 0 && forcedStatus.length() > 0 ) {
				
				int intAlarmVector	= 0;
				int intValidity		= 0;
				int intForcedStatus	= 0;
				try {
		
					intAlarmVector	= Integer.parseInt(alarmVector.split(",")[valueAlarmVectorIndex]);
					intValidity		= Integer.parseInt(validity);
					intForcedStatus	= Integer.parseInt(forcedStatus);
				} catch ( NumberFormatException e ) {
					logger.warn(className, function, "NumberFormatException[{}]", e.toString());
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
				logger.warn(className, function, "alarmVector[{}] validity[{}] forcedStatus[{}] length IS INVALID",
						new Object[]{alarmVector.length(), validity.length(), forcedStatus.length()});
			}
		} else {
			logger.warn(className, function, "alarmVector[{}] validity[{}] forcedStatus[{}] IS NULL", new Object[]{alarmVector, validity, forcedStatus});
		}
		
		logger.debug(className, function, "colorCode[{}]", colorCSS);
		
		logger.end(className, function);

		return colorCSS;
	}
}
