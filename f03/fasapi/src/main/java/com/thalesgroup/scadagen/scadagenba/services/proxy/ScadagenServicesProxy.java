
package com.thalesgroup.scadagen.scadagenba.services.proxy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.thalesgroup.scadagen.scadagenba.jna.api.IJNAXConnectorApi;
import com.thalesgroup.scadagen.scadagenba.services.listeners.IServerStateListener;

/**
 * Abstract class for the Scadasoft proxies.<BR>
 * </BR>
 * It manages the connection and the disconnection to a Scadasoft service and
 * informs the observers through listeners when the connection state is
 * modified.
 */

public abstract class ScadagenServicesProxy {

    static private final Logger s_logger = LoggerFactory.getLogger(ScadagenServicesProxy.class);

    static protected IJNAXConnectorApi x_api = null;

    protected int m_srvHandle;

    private boolean m_isConnected;

    private String m_envName;

    private String m_srvName;

    private int m_currentState;

    protected MyStateNotification m_notificationCallback;

    private Map<String, IServerStateListener> m_stateListeners = new HashMap<String, IServerStateListener>();

    // ----------------------------------------------------------------------------
    // Responses - Scadasoft server state services
    // ----------------------------------------------------------------------------
    /**
     * This class defines the state of Scadasoft server services
     * 
     * @author s0055708
     *
     */
    public static class MyStateNotification implements IJNAXConnectorApi.StateNotification {

        private ScadagenServicesProxy m_proxy = null;

        /**
         * Constructor
         * 
         * @param proxy
         *            The proxy
         */
        public MyStateNotification(ScadagenServicesProxy proxy) {
            m_proxy = proxy;
            Native.setCallbackThreadInitializer(this, new CallbackThreadInitializer(true, false));
        }

        /**
         * Handles the connection state. Sets the new connection state and
         * informs all listeners with that state.
         * 
         * @param newState
         *            The new state.
         */
        @Override
        public synchronized void stateChangeCallback(int newState) {
            // check classloader
            // keep current class loader and set the one that has been memorized
            // in init context
            final ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
            if (currentLoader == null) {
                Thread.currentThread().setContextClassLoader(ScadagenConnectorProxy.instance().getJNAClassLoader());
            }

            try {
                m_proxy.stateChange(newState);
            } catch (Throwable e) {
                // we should not be there, log an error but do not kill the
                // notification thread
                s_logger.error("FATAL ERROR while execution C++ callback stateChangeCallback.  : {} ", e);
            }
        }
    }

    // ----------------------------------------------------------------------------
    protected ScadagenServicesProxy(String envName, String srvName) {
        this.m_envName = envName;
        this.m_srvName = srvName;
        this.m_isConnected = false;
        this.m_srvHandle = -1;
        this.m_currentState = IServerStateListener.SERVER_STATE_NotInitialized;
        this.m_notificationCallback = new MyStateNotification(this);

    }

    /**
     * Scadasoft environment name accessor.
     * 
     * @return The Scadasoft environment name.
     */
    final public String getEnvName() {
        return m_envName;
    }

    /**
     * Scadasoft service name accessor.
     * 
     * @return The Scadasoft service name.
     */
    final public String getSrvName() {
        return m_srvName;
    }

    final public void setCurrentState(int newState) {
        m_currentState = newState;
    }

    final public int getCurrentState() {
        return m_currentState;
    }

    /**
     * Indicates whether or not the connection to the Scadasoft service is
     * established.
     * 
     * @return true if the connection is on, false otherwise.
     */
    final public synchronized boolean isConnected() {
        return m_isConnected;
    }

    /**
     * Sets the connection state to the Scadasoft service.
     * 
     * @param set
     *            The state value.
     */
    final public synchronized void setConnected(boolean set) {
        m_isConnected = set;
    }

    /**
     * Add a connection state listener.
     * 
     * @param name
     *            The name of the listener.
     * @param listener
     *            The {@link IServerStateListener} listener instance.
     */
    final public synchronized void addStateListener(String name, IServerStateListener listener) {
        if (name != null && !name.isEmpty() && listener != null) {
            m_stateListeners.put(name, listener);
            listener.handleState(getCurrentState());
        }
    }

    /**
     * Remove a connection state listener.
     * 
     * @param name
     *            The name of the listener to remove.
     */
    final public synchronized void removeStateListener(String name) {
        if (name != null && !name.isEmpty() && m_stateListeners != null) {
            m_stateListeners.remove(name);
        }
    }

    // ----------------------------------------------------------------------------
    // Requests
    // ----------------------------------------------------------------------------
    /**
     * Send a request to remove the connection to the associated Scadasoft
     * service entity.
     */
    public synchronized void disconnect() {
        if (isConnected() && x_api != null) {
            s_logger.debug("disconnection from the server : '{}@{}'...", getSrvName(), getEnvName());
            int ret = x_api.JNA_Disconnect(m_srvHandle);
            if (ret == -1) {
                s_logger.error("disconnection from the server '{}@{}' fail with code={}",
                        new Object[] { getSrvName(), getEnvName(), ret });
            }
            setConnected(false);
            m_srvHandle = -1;
            stateChange(IServerStateListener.SERVER_STATE_Down);
        }
    }

    /**
     * Handles the connection state. Sets the new connection state and informs
     * all listeners with that state.
     * 
     * @param newState
     *            The new state.
     */
    protected synchronized void stateChange(int newState) {

        if (newState == IServerStateListener.SERVER_STATE_Up) {
            setConnected(true);
            s_logger.info("the server '{}@{}' is now 'UP'", getSrvName(), getEnvName());
        } else {
            setConnected(false);
            s_logger.info("the server '{}@{}' is now 'DOWN'", getSrvName(), getEnvName());
        }
        setCurrentState(newState);
        // call listeners
        Iterator<IServerStateListener> it = m_stateListeners.values().iterator();
        while (it.hasNext()) {
            IServerStateListener listener = it.next();
            listener.handleState(newState);
        }
    }

    /**
     * Loads the JNA API.
     */
    static {
        s_logger.info("load SCADAgen JNA API: IJNAXConnectorApi");
        try {
            x_api = (IJNAXConnectorApi) Native.loadLibrary(ScadagenConnectorProxy.LIBRARY_NAME, IJNAXConnectorApi.class);
        } catch (Throwable e) {
            s_logger.error("cannot load SCADAgen IJNAXConnectorApi : {}", e.getMessage());
        }
    }
}
