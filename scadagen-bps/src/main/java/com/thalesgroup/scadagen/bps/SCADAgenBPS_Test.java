package com.thalesgroup.scadagen.bps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.Connector;

public class SCADAgenBPS_Test {
	private static final Logger LOGGER = LoggerFactory.getLogger(SCADAgenBPS_Test.class);
	
	public static void main(String[] args) {
		Connector connector = null;
		try {
			connector = new Connector("BAConnectorConfiguration.xml", "BASystemConfiguration", "BASystemConfiguration", true);
		} catch (HypervisorException e) {
			e.printStackTrace();
		}
		connector.getConnectorConfiguration().setEnableWSValidation(false);

		SCADAgenBPS bps = new SCADAgenBPS(connector);

		try {
			bps.init();

			bps.start();
			
			//TestBindingTVSModel();
			
			//TestBindingITMSModel();
			
		} catch (BPSException e) {
			LOGGER.error("SCADAgen BPS  - Error during init or start: ", e);
		}
		
		while (true) {
            try {
                Thread.sleep(1000);
            } catch (final Exception e) {
                LOGGER.error("SCADAgen BPS  - Error at runtime: ", e);
            }
        }
	}
	
	public static void TestBindingTVSModel() {
		// Test Hv2ScsBinding TVS model
//		PTZFICameraStatusesType entity1 = new PTZFICameraStatusesType();
//		entity1.setId("CTV-G01-ECA-005");
//		CamStateTypeAttributeType value1 = new CamStateTypeAttributeType();
//		value1.setValue("REACHABLE");
//		value1.setValid(true);
//		value1.setTimestamp(System.currentTimeMillis());
//		entity1.setState(value1);
//
//		Hv2ScsBindingEngine bindingEngine_ = ConfManager.getBindingEngine();
//		if (bindingEngine_ == null) {
//			LOGGER.error("binding engine is null");
//		} else {
//			IData data1 = ConfManager.getBindingEngine().getScsValue(entity1, "state");
//			LOGGER.debug("REACHABLE converted to {}", data1.getIntValue());
//		}
		
//		PTZFICameraStatusesType entity2 = new PTZFICameraStatusesType();
//		entity2.setId("CTV-G01-ECA-006");
//		CamStateTypeAttributeType value2 = new CamStateTypeAttributeType();
//		value2.setValue("NOT_REACHABLE");
//		value2.setValid(true);
//		value2.setTimestamp(System.currentTimeMillis());
//		entity2.setState(value2);
//
//		IData data2 = ConfManager.getBindingEngine().getScsValue(entity2, "state");
//		LOGGER.debug("NOT_REACHABLE converted to {}", data2.getIntValue());
//		
//		PTZFICameraStatusesType entity3 = new PTZFICameraStatusesType();
//		entity3.setId("CTV-G01-ECA-014");
//		CamStateTypeAttributeType value3 = new CamStateTypeAttributeType();
//		value3.setValue("UNKNOWN");
//		value3.setValid(true);
//		value3.setTimestamp(System.currentTimeMillis());
//		entity3.setState(value3);
//
//		IData data3 = ConfManager.getBindingEngine().getScsValue(entity3, "state");
//		LOGGER.debug("UNKNOWN converted to {}", data3.getIntValue());
	}
	
	public static void TestBindingITMSModel() {
		// Test Hv2ScsBinding ITMS model
//		ExtSysVariableStatusesType entity1 = new ExtSysVariableStatusesType();
//		entity1.setId("ROU_:B06C_B06E:Route_Disabled");
//		
//		StringAttributeType eqpID = new StringAttributeType();
//		eqpID.setValue("ROU_:B06C_B06E:Route_Disabled");
//		eqpID.setValid(true);
//		eqpID.setTimestamp(System.currentTimeMillis());
//		entity1.setEqpID(eqpID);
//		
//		StringAttributeType eqpType = new StringAttributeType();
//		eqpType.setValue("ROU_");
//		eqpType.setValid(true);
//		eqpType.setTimestamp(System.currentTimeMillis());
//		entity1.setEqpType(eqpType);
//		
//		StringAttributeType group = new StringAttributeType();
//		group.setValue("SIG_");
//		group.setValid(true);
//		group.setTimestamp(System.currentTimeMillis());
//		entity1.setGroup(group);
//		
//		StringAttributeType evarName = new StringAttributeType();
//		evarName.setValue("Route_Disabled");
//		evarName.setValid(true);
//		evarName.setTimestamp(System.currentTimeMillis());
//		entity1.setEvarName(evarName);
//		
//		StringAttributeType evarValue = new StringAttributeType();
//		evarValue.setValue("0");
//		evarValue.setValid(true);
//		evarValue.setTimestamp(System.currentTimeMillis());
//		entity1.setEvarValue(evarValue);
//		
//		EVValueTypeEnumerationAttributeType evarType = new EVValueTypeEnumerationAttributeType();
//		evarType.setValue("integer");
//		evarType.setValid(true);
//		evarType.setTimestamp(System.currentTimeMillis());
//		entity1.setEvarType(evarType);
//		
//		IntAttributeType evarSec = new IntAttributeType();
//		evarSec.setValue(0);
//		evarSec.setValid(true);
//		evarSec.setTimestamp(System.currentTimeMillis());
//		entity1.setEvarSec(evarSec);
//		
//		IntAttributeType evarMs = new IntAttributeType();
//		evarMs.setValue(0);
//		evarMs.setValid(true);
//		evarMs.setTimestamp(System.currentTimeMillis());
//		entity1.setEvarMs(evarMs);
//
//		Hv2ScsBindingEngine bindingEngine_ = ConfManager.getBindingEngine();
//		if (bindingEngine_ == null) {
//			LOGGER.error("binding engine is null");
//		} else {
//	    	String inputName = evarName.getValue();
//			IData data1 = null;
//			try {
//				data1 = ConfManager.getBindingEngine().getMappedScsValue(entity1, inputName);
//			} catch (HypervisorException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			LOGGER.debug("evarValue {} converted to {}", evarValue.getValue(), data1.getIntValue());
//		}
//		
	}

}
