
package com.thalesgroup.scadagen.services.listeners;

/**
 * The listener interface for receiving service state events.<BR>
 * </BR>
 * A class that is interested in processing a service state event implements
 * this interface and an object of that class sees its handleState method
 * invoked when a service state event occurs.
 */
public interface IServerStateListener {

	public static final int SERVER_STATE_NotInitialized = 0;
	public static final int SERVER_STATE_Up = 1;
	public static final int SERVER_STATE_Down = 2;

    /**
     * Invoked when a service state event occurs.
     * 
     * @param state
     *            The service state.
     */
    public void handleState(final int state);
}
