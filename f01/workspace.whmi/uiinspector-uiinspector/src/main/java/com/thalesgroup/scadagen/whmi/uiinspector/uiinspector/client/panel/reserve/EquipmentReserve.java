package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.reserve;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIPanelInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorHeader_i.AttributeName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.Stability;

public class EquipmentReserve {

	private final static String className = EquipmentReserve.class.getSimpleName();
	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(EquipmentReserve.class.getName());
	
	private static String reservationName = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	
	public static void setReservationName(String iReservationName) {
		reservationName = iReservationName;
	}
	
	private static Map<String, String> attributes = new HashMap<String, String>();
	public static void setAttribute(String key, String value) {
		final String function = "setAttribute";
		logger.begin(function);
		logger.debug(function, "key[{}] value[{}]", key, value);
		attributes.put(key, value);
		logger.end(function);
	}
	
	public static void loadConfiguration() {
		final String function = "loadConfiguration";
		logger.begin(function);
		
		String dictionariesCacheName = UIInspector_i.strUIInspector;
		String dictionariesCacheName_fileName = className+UIPanelInspector_i.strConfigExtension;
		String dictionariesCacheName_prefix = className+UIPanelInspector_i.strDot;
		
		{
			String keyResrvReserveReqIDAttribute= dictionariesCacheName_prefix+AttributeName.ResrvReserveReqIDAttribute.toString();
			String resrvReserveReqIDAttribute = ReadProp.readString(dictionariesCacheName, dictionariesCacheName_fileName, keyResrvReserveReqIDAttribute, "");
			logger.debug(function, "resrvReserveReqIDAttribute[{}]", resrvReserveReqIDAttribute);
			setAttribute(AttributeName.ResrvReserveReqIDAttribute.toString(), resrvReserveReqIDAttribute);
		}
		{
			String keyResrvUnreserveReqIDAttribute= dictionariesCacheName_prefix+AttributeName.ResrvUnreserveReqIDAttribute.toString();
			String resrvUnreserveReqIDAttribute = ReadProp.readString(dictionariesCacheName, dictionariesCacheName_fileName, keyResrvUnreserveReqIDAttribute, "");
			logger.debug(function, "resrvUnreserveReqIDAttribute[{}]", resrvUnreserveReqIDAttribute);
			setAttribute(AttributeName.ResrvUnreserveReqIDAttribute.toString(), resrvUnreserveReqIDAttribute);
		}
		
		logger.end(function);
	}

	/** Return the EquipmentReservationID
	 * @return	reservationName
	 */
	public static String getEquipmentReservationName (boolean hasScreen, int screen) {
		final String function = "getEquipmentReservationName";
		logger.begin(function);
		logger.debug(function, "reservationName[{}]", reservationName);
		logger.debug(function, "hasScreen[{}] screen[{}]", hasScreen, screen);
		String reservationkey = reservationName+(hasScreen?screen:"");
		logger.debug(function, "reservationkey[{}]", reservationkey);
		logger.end(function);
		return reservationkey;
	}
	
	/** This procedure using to check equipment reservation status
	 * @param eqtReserved:	eqtReserved
	 * @return 			0 Not reserved
	 * 					1 Reserved by self
	 * 					2 Reserved by other
	 */
	public static int isEquipmentReservation(String eqtReserved, boolean hasScreen, int screen) {
		final String function = "isEquipmentReservation";
		logger.begin(function);
		logger.debug(function, "eqtReserved[{}]", eqtReserved);
		logger.debug(function, "hasScreen[{}] screen[{}]", hasScreen, screen);
		String reservationName = getEquipmentReservationName(hasScreen, screen);
		int eqtReservedresult = 0;
		DatabaseHelper.removeDBStringWrapper(eqtReserved);
		if ( null != eqtReserved && eqtReserved.trim().length() > 0 && eqtReserved != reservationName ) {
			eqtReservedresult = 2;
		} else if ( null != eqtReserved && eqtReserved.trim().length() > 0 && eqtReserved.equals(reservationName) ) {
			eqtReservedresult = 1;
		} else if ( null != eqtReserved && eqtReserved.trim().length() == 0 ) {
			eqtReservedresult = 0;
		}
		logger.end(function);
		return eqtReservedresult;
	}
	
	/** This procedure using to reserve equipment
	 * @param scsEnvId:		
	 * @param dbaddress:
	 */
	public static void equipmentReservation ( String scsEnvId, String dbaddress, Database database, boolean hasScreen, int screen) {
		final String function = "equipmentReservation";
		logger.begin(function);
		logger.debug(function, "scsEnvId[{}] address[{}]", scsEnvId, dbaddress);
		logger.debug(function, "hasScreen[{}] screen[{}]", hasScreen, screen);
		
		String resrvReserveReqIDAttribute = attributes.get(AttributeName.ResrvReserveReqIDAttribute.toString());
		String reservationName = getEquipmentReservationName(hasScreen, screen);
		String addressWrite = dbaddress + resrvReserveReqIDAttribute;
		
		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.WriteStringValue);
		clientKey.setWidget(className);
		clientKey.setStability(Stability.DYNAMIC);
		clientKey.setScreen(screen);
		clientKey.setEnv(scsEnvId);
		clientKey.setAdress(dbaddress);
		
		String key = clientKey.getClientKey();
			
		database.addWriteStringValueRequest(key, scsEnvId, addressWrite, reservationName);

		logger.end(function);
	}
	
	/** This procedure using to un-reserve equipment
	 * @param env:		Server name
	 * @param address:	Equipment Point
	 */
	public static void equipmentUnreservation ( String scsEnvId, String dbaddress, Database database, boolean hasScreen, int screen ) {
		final String function = "equipmentUnreservation";
		logger.begin(function);
		logger.debug(function, "scsEnvId[{}] address[{}]", scsEnvId, dbaddress);
		logger.debug(function, "hasScreen[{}] screen[{}]", hasScreen, screen);
		
		// Write
		String resrvUnreserveReqIDAttribute = attributes.get(AttributeName.ResrvUnreserveReqIDAttribute.toString());
		String reservationName = getEquipmentReservationName(hasScreen, screen);
		String addressWrite = dbaddress + resrvUnreserveReqIDAttribute;
		
		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.WriteStringValue);
		clientKey.setWidget(className);
		clientKey.setStability(Stability.DYNAMIC);
		clientKey.setScreen(screen);
		clientKey.setEnv(scsEnvId);
		clientKey.setAdress(dbaddress);
		
		String key = clientKey.getClientKey();
		
		database.addWriteStringValueRequest(key, scsEnvId, addressWrite, reservationName);

		logger.end(function);
	}
}
