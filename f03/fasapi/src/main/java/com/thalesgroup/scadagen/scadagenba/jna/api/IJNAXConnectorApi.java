
package com.thalesgroup.scadagen.scadagenba.jna.api;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.thalesgroup.scadasoft.jna.api.IJNAConnectorApi.ASCChangeStateNotification;

/**
 * The JNA API for initialization and connection to Scadasoft server and its
 * services.<BR>
 * </BR>
 * This interface is used to create and initialize a connection with a Scadasoft
 * server and manage several connections to Scadasoft services.<BR>
 * </BR>
 * It also provides a means to retrieve and freeing Scadasoft events allocated
 * by the native side.
 */
public interface IJNAXConnectorApi extends Library {  //NOSONAR

	/**
     * This interface is used to receive callback when a new state appears
     * 
     * @author s0055708
     *
     */
//    public interface ASCChangeStateNotification extends Callback {
//        /**
//         * callback called when a new remote service state appears.
//         * 
//         * @param newState
//         */
//        void ascNewState(String newState);
//    }

    /**
     * This interface is used to receive callback when remote service state changed.
     * 
     * @author s0055708
     *
     */
    public interface StateNotification extends Callback {
        /**
         * callback called when remote service state changed.
         * 
         * @param newState
         * the new remote service state 
         */
        void stateChangeCallback(int newState);
    }

    /**
     * Creates and initializes a connection to the Scadasoft env.
     * 
     * @param physicalEnv
     *            The physical environment name.
     * @param serverName
     *            The server name.
     * @param statLoopPeriod
     *            period for event loop statistics traces (0 is no trace)
     * @param cb
     *            the notification callback
     * @return The operation result.
     */
    public int JNA_SCSInitialize(String physicalEnv, String serverName, int statLoopPeriod,
            ASCChangeStateNotification cb);

    /**
     * Call SCADAsoft stop sequence: - stop event loop - stop statistics loop -
     * stop CORBA and release all C++ objects
     *
     * @return always 0.
     */
    public int JNA_SCSExit();

    /**
     * Call _exit C function, for emergency shutdown.
     * 
     * @param code
     *            return code of process.
     */
    public void JNA_Exit(int code);
    
    /**
     * Establishes a connection to a SCADAsoft service.
     * 
     * @param remEnvName
     *            The SCADAsoft server environment name.
     * @param remSrvName
     *            The Scadasoft service name.
     * @param cb
     *            callback called when remote service state changed.
     * @return handle to server to used in other JNA API.
     */
    public int JNA_ConnectToHILCServer(String remEnvName, String remSrvName, StateNotification cb);
    /**
     * Establishes a connection to a SCADAsoft service.
     * 
     * @param remEnvName
     *            The SCADAsoft server environment name.
     * @param remSrvName
     *            The Scadasoft service name.
     * @param cb
     *            callback called when remote service state changed.
     * @return handle to server to used in other JNA API.
     */
    public int JNA_ConnectToServer(String remEnvName, String remSrvName, StateNotification cb);

    /**
     * Removes a connection to a Scadasoft service.
     * 
     * @param srvHandle
     *            handle return by JNA_ConnectToXXXServer.
     * @return The operation result.
     */
    public int JNA_Disconnect(int srvHandle);
    
}
