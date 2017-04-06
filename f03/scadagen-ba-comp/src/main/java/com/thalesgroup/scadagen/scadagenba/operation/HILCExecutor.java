package com.thalesgroup.scadagen.scadagenba.operation;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.data_v1.operation.AbstractEntityOperationRequestType;
import com.thalesgroup.hv.data_v1.operation.ResponseStatusTypeEnum;
import com.thalesgroup.scadagen.scadagenba.services.proxy.ScadagenHILCServicesProxy;
import com.thalesgroup.scadasoft.services.proxy.ScsDbmServicesProxy;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.hvconnector.operation.IOperationExecutor;
import com.thalesgroup.scadasoft.hvconnector.operation.SCSOperationResponder;

public class HILCExecutor implements IOperationExecutor {
	 /** Logger **/
    private static final Logger s_logger = LoggerFactory.getLogger(HILCExecutor.class);

    private ScadagenHILCServicesProxy m_hilcService = null;
    private ScsDbmServicesProxy m_dbmService = null;

    private static final String CmdType_HILCPreparationRequest = "HILCPreparationRequest";
    private static final String CmdType_HILCConfirmRequest = "HILCConfirmRequest";

    /**
     * Constructor
     */
    public HILCExecutor() {
        m_hilcService = new ScadagenHILCServicesProxy(SCSConfManager.instance().getRemoteEnv(), "SigHILCServer");
        m_dbmService = new ScsDbmServicesProxy(SCSConfManager.instance().getRemoteEnv(), "DbmServer");
    }
    
	@Override
	public void executeOperation(UUID correlationID, AbstractEntityOperationRequestType operationRequest,
            String[] param) {
		if (param.length < 7) {
            s_logger.error("HILCExecutor FAILURE: not enough parameter {} (min 7)", param.length);
            if (correlationID != null) {
                SCSOperationResponder.sendOperationResponse(operationRequest, ResponseStatusTypeEnum.FAILURE, null,
                        correlationID);
            }
            return;
        }

        if (!m_hilcService.isConnected()) {
            s_logger.error("HILCExecutor FAILURE: no connection to SCS server");
            if (correlationID != null) {
                SCSOperationResponder.sendOperationResponse(operationRequest, ResponseStatusTypeEnum.FAILURE, null,
                        correlationID);
            }
            return;
        }

        String hveqpId = operationRequest.getEntityID();
        String operator = operationRequest.getOperatorID();
        String scsid = SCSConfManager.instance().getSCSIdFromHVId(hveqpId);

        if (scsid == null) {
            s_logger.error("HILCExecutor FAILURE: cannot get SCADAsoft id from {}", hveqpId);
            if (correlationID != null) {
                SCSOperationResponder.sendOperationResponse(operationRequest, ResponseStatusTypeEnum.FAILURE, null,
                        correlationID);
            }
            return;
        }
        
        // Convert scs path to alias
        //String alias = scsid.replaceAll(":", "");
    	String alias = m_dbmService.getAlias(scsid);
    	if (alias != null) {
    		scsid = alias;
    	}

        String cmdType = SCSOperationResponder.getValueFromOperation(operationRequest, param[1]);
        ResponseStatusTypeEnum cmdResult = ResponseStatusTypeEnum.FAILURE;

        if (CmdType_HILCPreparationRequest.equals(cmdType)) {
            cmdResult = doHILCPreparationRequest(operationRequest, alias, operator, param);
        } else if (CmdType_HILCConfirmRequest.equals(cmdType)) {
            cmdResult = doHILCConfirmRequest(operationRequest, alias, operator, param);
        } else {
            s_logger.error("HILCExecutor FAILURE: cannot get command type from P1: {}", param[1]);
            if (correlationID != null) {
                SCSOperationResponder.sendOperationResponse(operationRequest, ResponseStatusTypeEnum.FAILURE, null,
                        correlationID);
            }
            return;
        }

        if (correlationID != null) {
            SCSOperationResponder.sendOperationResponse(operationRequest, cmdResult, null, correlationID);
        }
	}
	
	/*
     * Exemple of HILC command mapping
     * 
     * <operation hv=
     * "com.thalesgroup.lusail.data.scs.configuration.equipment.luscb.operation.OpCbCmdSelOpenRequest"
     * scs="HILCServer,HILCPreparationRequest,$workstationName,cmdType,cmdValue,equipmentType,cmdName"/>
     * 
     * param[0] (mand)= always "HILCServer"
     * param[1] (mand)= always "HILCPreparationRequest"
     * param[2] (mand)= workstationName
     * param[3] (mand)= cmdType
     * param[4] (mand)= cmdValue
     * param[5] (mand)= equipmentType
     * param[6] (mand)= cmdName
     * 
     */
    //public static final String WorkingStatus_OUT_OF_SERVICE = "0";

    private ResponseStatusTypeEnum doHILCPreparationRequest(AbstractEntityOperationRequestType operationRequest, String scsid,
            String operator, String[] param) {
        ResponseStatusTypeEnum result = ResponseStatusTypeEnum.FAILURE;
        if (param.length < 7) {
            s_logger.error("HILCExecutor doHILCPreparationRequest: not enough parameter {} (min 7)", param.length);
            return result;
        }

        try {
	        String workstationName = SCSOperationResponder.getValueFromOperation(operationRequest, param[2]);
	        int cmdType = Integer.parseInt(SCSOperationResponder.getValueFromOperation(operationRequest, param[3]));
	        int cmdValue = 2;
	        int cmdValueDiv = 1;
	        if (cmdType == 2) {
	        	cmdValue = Integer.parseInt(SCSOperationResponder.getValueFromOperation(operationRequest, param[4]));
	        	cmdValueDiv = 1;
	        } else if (cmdType == 3) {
	        	// Process AIO value into cmdValue and cmdValueDiv
	        	String cValue = SCSOperationResponder.getValueFromOperation(operationRequest, param[4]);
	        	
	        	// Convert scientific notation to decimal places for string manipulation
	        	Double dValue = Double.parseDouble(cValue);
	        	String dValStr = String.format("%.12f", dValue); // Note: HILC server only supports 16bit max value
	        	
	        	int len = dValStr.length();
	    		int idx = dValStr.indexOf('.');
	    		cmdValueDiv = 1;
	    		if (idx >= 0) {
	    			// find extra trailing zero
	    			int pos=len-1;
	    			for (; pos>idx; pos--) {
	    				if (dValStr.charAt(pos) != '0') {
	    					break;
	    				}
	    			}

	    			int power = pos - idx;
	    			for (int i=0; i<power; i++) {
	    				cmdValueDiv = cmdValueDiv * 10;
	    			}
	    			
	    			String removeDecimalPointStr = dValStr.substring(0,idx) ;
	    			if (pos > idx) {
	    				removeDecimalPointStr = removeDecimalPointStr + dValStr.substring(idx+1, pos+1);
	    			}

	    			cmdValue = Integer.parseInt(removeDecimalPointStr);

	    		} else {
	    			cmdValue = Integer.parseInt(dValStr);
	    		}
	        }
	        String equipmentAlias = scsid;
	        String equipmentType = param[5];
	        String cmdName = SCSOperationResponder.getValueFromOperation(operationRequest, param[6]);
	
	        int res = m_hilcService.hilcPreparationRequest(operator, workstationName, cmdType, cmdValue, cmdValueDiv, equipmentAlias, equipmentType, cmdName);
	        if (res >= 0) {
	            result = ResponseStatusTypeEnum.SUCCESS;
	        }
        } catch(Exception e) {
        	s_logger.error("Error in doHILCConfirmRequest {}", e.getMessage());
        }

        return result;
    }
    
    
    /*
     * Exemple of HILC command mapping
     * 
     * <operation hv=
     * "com.thalesgroup.lusail.data.scs.configuration.equipment.luscb.operation.OpCbCmdExeOpenRequest"
     * scs="HILCServer,HILCConfirmRequest,$workstationName,cmdType,cmdValue,equipmentType,cmdName"/>
     * 
     * param[0] (mand)= always "HILCServer"
     * param[1] (mand)= always "HILCConfirmRequest"
     * param[2] (mand)= workstationName
     * param[3] (mand)= cmdType
     * param[4] (mand)= cmdValue
     * param[5] (mand)= equipmentType
     * param[6] (mand)= cmdName
     * 
     */
    //public static final String WorkingStatus_OUT_OF_SERVICE = "0";

    private ResponseStatusTypeEnum doHILCConfirmRequest(AbstractEntityOperationRequestType operationRequest, String scsid,
            String operator, String[] param) {
        ResponseStatusTypeEnum result = ResponseStatusTypeEnum.FAILURE;
        if (param.length < 7) {
            s_logger.error("HILCExecutor doHILCConfirmRequest: not enough parameter {} (min 7)", param.length);
            return result;
        }
        
        try {
	        String workstationName = SCSOperationResponder.getValueFromOperation(operationRequest, param[2]);
	        int cmdType = Integer.parseInt(SCSOperationResponder.getValueFromOperation(operationRequest, param[3]));
	        int cmdValue = 2;
	        int cmdValueDiv = 1;
	        if (cmdType == 2) {
	        	cmdValue = Integer.parseInt(SCSOperationResponder.getValueFromOperation(operationRequest, param[4]));
	        	cmdValueDiv = 1;
	        } else if (cmdType == 3) {
	        	// Process AIO value into cmdValue and cmdValueDiv
	        	String cValue = SCSOperationResponder.getValueFromOperation(operationRequest, param[4]);
	        	
	        	// Convert scientific notation to decimal places for string manipulation
	        	Double dValue = Double.parseDouble(cValue);
	        	String dValStr = String.format("%.12f", dValue);
	        	
	        	int len = dValStr.length();
	    		int idx = dValStr.indexOf('.');
	    		cmdValueDiv = 1;
	    		if (idx >= 0) {
	    			// find extra trailing zero
	    			int pos=len-1;
	    			for (; pos>idx; pos--) {
	    				if (dValStr.charAt(pos) != '0') {
	    					break;
	    				}
	    			}

	    			int power = pos - idx;
	    			for (int i=0; i<power; i++) {
	    				cmdValueDiv = cmdValueDiv * 10;
	    			}
	    			
	    			String removeDecimalPointStr = dValStr.substring(0,idx) ;
	    			if (pos > idx) {
	    				removeDecimalPointStr = removeDecimalPointStr + dValStr.substring(idx+1, pos+1);
	    			}

	    			cmdValue = Integer.parseInt(removeDecimalPointStr);

	    		} else {
	    			cmdValue = Integer.parseInt(dValStr);
	    		}
	        }
	        String equipmentAlias = scsid;
	        String equipmentType = param[5];
	        String cmdName = SCSOperationResponder.getValueFromOperation(operationRequest, param[6]);
	
	        int res = m_hilcService.hilcConfirmRequest(operator, workstationName, cmdType, cmdValue, cmdValueDiv, equipmentAlias, equipmentType, cmdName);
	        if (res >= 0) {
	            result = ResponseStatusTypeEnum.SUCCESS;
	        }
        } catch (Exception e) {
        	s_logger.error("Error in doHILCConfirmRequest {}", e.getMessage());
        }

        return result;
    }

}
