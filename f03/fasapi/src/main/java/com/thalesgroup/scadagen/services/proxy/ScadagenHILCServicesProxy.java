
package com.thalesgroup.scadagen.services.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Native;
import com.thalesgroup.scadagen.jna.api.IJNAHILCServerAPI;

/**
 * Proxy designed for sending commands to the Scadasoft HILC server.
 */
public class ScadagenHILCServicesProxy extends ScadagenServicesProxy {

    static private final Logger s_logger = LoggerFactory.getLogger(ScadagenHILCServicesProxy.class);
    
    private static final int DEFAULTRESULT = -2;

    static private IJNAHILCServerAPI x_api = null;

    private int m_hilcServerHandle = -1;

    // ----------------------------------------------------------------------------
    // Commands
    // ----------------------------------------------------------------------------

    /**
     * HILC Preparation Request 
     * 
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
     * @return 0 if the method is correctly executed, -1 otherwise
     */
    public int hilcPreparationRequest(String operatorName, String workstationName, int cmdType, int cmdValue, int cmdValueDiv, String equipmentAlias, String equipmentType, String commandName) {
        int ret = DEFAULTRESULT;
        if (x_api != null) {
    		s_logger.debug("hilcServerHandle={} operatorName={} workstationName={} cmdType={} cmdValue={} cmdValueDiv={} equipmentAlias={} equipmentType={} commandName={} ", 
    				new Object[] { m_hilcServerHandle, operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, equipmentAlias, equipmentType, commandName});
            ret = x_api.JNA_HILCServer_HILCPreparationRequest(m_hilcServerHandle, operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, equipmentAlias, equipmentType, commandName);
            if (ret != 0) {
                s_logger.error("hilcPreparationRequest on server '{}@{}' failed!",
                        new Object[] { getSrvName(), getEnvName() });
            }
        } else {
        	s_logger.error("JNA interface for HILC service is null");
        }

        return ret;
    }

    /**
     * HILC confirm request
     * 
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
     * @return 0 if the method is correctly executed, -1 otherwise
     */
    public int hilcConfirmRequest(String operatorName, String workstationName, int cmdType, int cmdValue, int cmdValueDiv, String equipmentAlias, String equipmentType, String commandName) {
        int ret = DEFAULTRESULT;
        if (x_api != null) {
    		s_logger.debug("hilcServerHandle={} operatorName={} workstationName={} cmdType={} cmdValue={} cmdValueDiv={} equipmentAlias={} equipmentType={} commandName={} ", 
    				new Object[] { m_hilcServerHandle, operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, equipmentAlias, equipmentType, commandName});
            ret = x_api.JNA_HILCServer_HILCConfirmRequest(m_hilcServerHandle, operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, equipmentAlias, equipmentType, commandName);
            if (ret != 0) {
                s_logger.error("hilcConfirmRequest on server '{}@{}' failed!",
                        new Object[] { getSrvName(), getEnvName() });
            }
        } else {
        	s_logger.error("JNA interface for HILC service is null");
        }

        return ret;
    }
    
    /**
     * Creates a proxy for the HILC commands.
     * 
     * @param envName
     *            The Scadasoft server environment name.
     * @param srvName
     *            The Scadasoft service name.
     */
    public ScadagenHILCServicesProxy(String envName, String srvName) {
        super(envName, srvName);
        m_srvHandle = ScadagenServicesProxy.x_api.JNA_ConnectToHILCServer(getEnvName(), getSrvName(),
                m_notificationCallback);
        s_logger.debug("HILCServer server handle : {}", m_srvHandle);
        m_hilcServerHandle = m_srvHandle;
    }

    /**
     * Loads the JNA API.
     */
    static {
        s_logger.info("load JNA API: IJNAHILCServerAPI");
        try {
            x_api = (IJNAHILCServerAPI) Native.loadLibrary(ScadagenConnectorProxy.LIBRARY_NAME, IJNAHILCServerAPI.class);
        } catch (Throwable e) {
            s_logger.error("cannot load IJNAHILCServerAPI : {}", e.getMessage());
        }
    }

}
