package com.thalesgroup.scadasoft.myba.operation;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.data_v1.operation.AbstractEntityOperationRequestType;
import com.thalesgroup.hv.data_v1.operation.ResponseStatusTypeEnum;
import com.thalesgroup.scadagen.services.proxy.ScadagenHILCServicesProxy;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.hvconnector.operation.IOperationExecutor;
import com.thalesgroup.scadasoft.hvconnector.operation.SCSOperationResponder;

public class HILCExecutor implements IOperationExecutor {
	 /** Logger **/
    private static final Logger s_logger = LoggerFactory.getLogger(HILCExecutor.class);

    private ScadagenHILCServicesProxy m_hilcService = null;

    private static final String CmdType_HILCPreparationRequest = "HILCPreparationRequest";
    private static final String CmdType_HILCConfirmRequest = "HILCConfirmRequest";

    /**
     * Constructor
     */
    public HILCExecutor() {
        m_hilcService = new ScadagenHILCServicesProxy(SCSConfManager.instance().getRemoteEnv(), "SigHILCServer");
    }
    
	@Override
	public void executeOperation(UUID correlationID, AbstractEntityOperationRequestType operationRequest,
            String[] param) {
		if (param.length < 8) {
            s_logger.error("HILCExecutor FAILURE: not enough parameter {} (min 8)", param.length);
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
        String alias = scsid.replaceAll(":", "");

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
     * scs="HILCServer,HILCPreparationRequest,$workstationName,cmdType,cmdValue,cmdValueDiv,equipmentType,cmdName"/>
     * 
     * param[0] (mand)= always "HILCServer"
     * param[1] (mand)= always "HILCPreparationRequest"
     * param[2] (mand)= workstationName
     * param[3] (mand)= cmdType
     * param[4] (mand)= cmdValue
     * param[5] (mand)= cmdValueDiv
     * param[6] (mand)= equipmentType
     * param[7] (mand)= cmdName
     * 
     */
    //public static final String WorkingStatus_OUT_OF_SERVICE = "0";

    private ResponseStatusTypeEnum doHILCPreparationRequest(AbstractEntityOperationRequestType operationRequest, String scsid,
            String operator, String[] param) {
        ResponseStatusTypeEnum result = ResponseStatusTypeEnum.FAILURE;
        if (param.length < 8) {
            s_logger.error("HILCExecutor doHILCPreparationRequest: not enough parameter {} (min 8)", param.length);
            return result;
        }

        String workstationName = param[2];
        int cmdType = Integer.parseInt(param[3]);
        int cmdValue = Integer.parseInt(param[4]);;
        int cmdValueDiv = Integer.parseInt(param[5]);;
        String equipmentAlias = scsid;
        String equipmentType = param[6];
        String cmdName = param[7];

        int res = m_hilcService.hilcPreparationRequest(operator, workstationName, cmdType, cmdValue, cmdValueDiv, equipmentAlias, equipmentType, cmdName);
        if (res >= 0) {
            result = ResponseStatusTypeEnum.SUCCESS;
        }

        return result;
    }
    
    
    /*
     * Exemple of HILC command mapping
     * 
     * <operation hv=
     * "com.thalesgroup.lusail.data.scs.configuration.equipment.luscb.operation.OpCbCmdExeOpenRequest"
     * scs="HILCServer,HILCConfirmRequest,$workstationName,cmdType,cmdValue,cmdValueDiv,equipmentType,cmdName"/>
     * 
     * param[0] (mand)= always "HILCServer"
     * param[1] (mand)= always "HILCConfirmRequest"
     * param[2] (mand)= workstationName
     * param[3] (mand)= cmdType
     * param[4] (mand)= cmdValue
     * param[5] (mand)= cmdValueDiv
     * param[6] (mand)= equipmentType
     * param[7] (mand)= cmdName
     * 
     */
    //public static final String WorkingStatus_OUT_OF_SERVICE = "0";

    private ResponseStatusTypeEnum doHILCConfirmRequest(AbstractEntityOperationRequestType operationRequest, String scsid,
            String operator, String[] param) {
        ResponseStatusTypeEnum result = ResponseStatusTypeEnum.FAILURE;
        if (param.length < 8) {
            s_logger.error("HILCExecutor doHILCConfirmRequest: not enough parameter {} (min 8)", param.length);
            return result;
        }
        
        String workstationName = param[2];
        int cmdType = Integer.parseInt(param[3]);
        int cmdValue = Integer.parseInt(param[4]);;
        int cmdValueDiv = Integer.parseInt(param[5]);;
        String equipmentAlias = scsid;
        String equipmentType = param[6];
        String cmdName = param[7];

        int res = m_hilcService.hilcConfirmRequest(operator, workstationName, cmdType, cmdValue, cmdValueDiv, equipmentAlias, equipmentType, cmdName);
        if (res >= 0) {
            result = ResponseStatusTypeEnum.SUCCESS;
        }

        return result;
    }

}
