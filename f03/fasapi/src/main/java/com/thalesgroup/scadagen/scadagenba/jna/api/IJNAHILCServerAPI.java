
package com.thalesgroup.scadagen.scadagenba.jna.api;

import com.sun.jna.Library;

/**
 * The JNA API interface for Scadasoft Dpc service management.<BR>
 * </BR>
 * This interface is used to force variable value or to change the status of a
 * variable or of an equipment.
 */
public interface IJNAHILCServerAPI extends Library {  //NOSONAR
    /**
     * JNA API for HILC preparation request
     * 
     * @param hilcserverHandle
     *            Handle to server proxy.
     * @param operatorName
     *            identify the HMI operator.
     * @param workstationName
     *            identify workstation name.
     * @param cmdType
     *            identify if the command will use a DIO (2) or AIO(3).
     * @param cmdValue
     *            value to put in the DIO / AIO.
     * @param cmdValueDiv
     *            divisor (in case of AIO sending a float).
     * @param equipmentAlias
     *            equipment UNIVNAME.
     * @param equipmentType
     *            identify what is put between the dio and the Cmd in the dio UNIVNAME.
     * @param commandName
     *            name of the double command on which the Select step is called.
     * @return The operation result.
     */
    public int JNA_HILCServer_HILCPreparationRequest(
    		int hilcserverHandle,
            String operatorName,
            String workStationName,
            int cmdType,
            int cmdValue,
            int cmdValueDiv,
            String equipmentAlias,
            String equipmentType,
            String commandName);

    /**
     * JNA API for HILC confirm request
     * 
     * @param hilcserverHandle
     *            Handle to server proxy.
     * @param operatorName
     *            identify the HMI operator.
     * @param workstationName
     *            identify workstation name.
     * @param cmdType
     *            identify if the command will use a DIO (2) or AIO(3).
     * @param cmdValue
     *            value to put in the DIO / AIO.
     * @param cmdValueDiv
     *            divisor (in case of AIO sending a float).
     * @param equipmentAlias
     *            equipment UNIVNAME.
     * @param equipmentType
     *            identify what is put between the dio and the Cmd in the dio UNIVNAME.
     * @param commandName
     *            name of the double command on which the Execute step is called.
     * @return The operation result.
     */
    public int JNA_HILCServer_HILCConfirmRequest(
    		int hilcserverHandle,
            String operatorName,
            String workStationName,
            int cmdType,
            int cmdValue,
            int cmdValueDiv,
            String equipmentAlias,
            String equipmentType,
            String commandName);

}
