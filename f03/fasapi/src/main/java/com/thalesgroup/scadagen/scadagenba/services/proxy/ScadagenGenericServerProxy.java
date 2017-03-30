package com.thalesgroup.scadagen.scadagenba.services.proxy;

/**
 * This class defines constructor to connect to a server
 * 
 * @author s0055708
 *
 */
public class ScadagenGenericServerProxy extends ScadagenServicesProxy {

    /**
     * Constructor
     * 
     * @param envName
     *            the name of the environment
     * @param srvName
     *            the name of ther server
     */
    public ScadagenGenericServerProxy(String envName, String srvName) {
        super(envName, srvName);

        m_srvHandle = ScadagenServicesProxy.x_api.JNA_ConnectToServer(getEnvName(), getSrvName(), m_notificationCallback);

    }

}
