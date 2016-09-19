package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.RTDB_Helper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.RTDB_Helper.PointName;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;

public class EquipmentReserve {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(EquipmentReserve.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static final String reservationName = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	
	public EquipmentReserve() {
		// TODO Auto-generated constructor stub
	}

	/** Return the EquipmentReservationID
	 * @return	reservationName
	 */
	public static String getEquipmentReservationName () {
		final String function = "getEquipmentReservationName";
		logger.begin(className, function);
		logger.info(className, function, "reservationName[{}]", reservationName);
		logger.end(className, function);
		return reservationName;
	}
	
	/** This procedure using to check equipment reservation status
	 * @param eqtReserved:	eqtReserved
	 * @return 			0 Not reserved
	 * 					1 Reserved by self
	 * 					2 Reserved by other
	 */
	public static int isEquipmentReservation(String eqtReserved) {
		final String function = "isEquipmentReservation";
		logger.begin(className, function);
		logger.info(className, function, "eqtReserved[{}]", eqtReserved);
		String reservationName = getEquipmentReservationName();
		int eqtReservedresult = 0;
		RTDB_Helper.removeDBStringWrapper(eqtReserved);
		if ( null != eqtReserved && eqtReserved.trim().length() > 0 && eqtReserved != reservationName ) {
			eqtReservedresult = 2;
		} else if ( null != eqtReserved && eqtReserved.trim().length() > 0 && eqtReserved.equals(reservationName) ) {
			eqtReservedresult = 1;
		} else if ( null != eqtReserved && eqtReserved.trim().length() == 0 ) {
			eqtReservedresult = 0;
		}
		logger.end(className, function);
		return eqtReservedresult;
	}
	
	/** This procedure using to reserve equipment
	 * @param scsEnvId:		
	 * @param dbaddress:
	 */
	public static void equipmentReservation ( String scsEnvId, String dbaddress ) {
		final String function = "equipmentReservation";
		logger.begin(className, function);
		
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		logger.info(className, function, "address[{}]", dbaddress);

		String reservationName = getEquipmentReservationName();
		String addressWrite = dbaddress + PointName.resrvReserveReqID.toString();
		String key = "addWriteStringValueRequest" + "_" + "inspector" + className + "_" + "dynamic" + "_" + dbaddress;
			
		Database database = Database.getInstance();
		database.addWriteStringValueRequest(key, scsEnvId, addressWrite, reservationName);

		logger.end(className, function);
		
	}
	
	/** This procedure using to un-reserve equipment
	 * @param env:		Server name
	 * @param address:	Equipment Point
	 */
	public static void equipmentUnreservation ( String scsEnvId, String dbaddress ) {
		final String function = "equipmentUnreservation";
		logger.begin(className, function);
		
		logger.info(className, function, "scsEnvId[{}]", scsEnvId);
		logger.info(className, function, "dbaddress[{}]", dbaddress);
		
		// Write
		String reservationName = getEquipmentReservationName();
		String addressWrite = dbaddress + PointName.resrvUnreserveReqID.toString();
		String key = "addWriteStringValueRequest" + "_" + "inspector" + className + "_" + "dynamic" + "_" + dbaddress;
		
		Database database = Database.getInstance();
		database.addWriteStringValueRequest(key, scsEnvId, addressWrite, reservationName);

		logger.end(className, function);
	}
}
