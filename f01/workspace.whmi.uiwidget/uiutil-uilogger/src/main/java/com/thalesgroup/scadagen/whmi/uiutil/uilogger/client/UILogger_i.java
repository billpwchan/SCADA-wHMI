package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

/**
 * UILogger interface
 * 
 * @author syau
 *
 */
public interface UILogger_i {
	
    /**
     * Get Logger name
     * @return Logger name
     */
    String getName();

    /**
     * trace without throwable
     * 
     * @param message message to log
     */
    void trace(final String message);
    
    /**
     * trace with throwable
     * 
     * @param message message to log
     * @param throwable throwable
     */
    void trace(final String message, final Throwable throwable);
    
    /**
     * debug without throwable
     * 
     * @param message message to log
     */
    void debug(final String message);
    
    /**
     * debug without throwable
     * 
     * @param message message to log
     */
    void debug(final Object message);
    
    /**
     * debug with throwable
     * 
     * @param message message to log
     * @param throwable throwable
     */
    void debug(final String message, final Throwable throwable);

    /**
     * error without throwable
     * 
     * @param message message to log
     */
    void error(final String message);
    
    /**
     * error with throwable
     * 
     * @param message message to log
     * @param throwable throwable
     */
    void error(final String message, final Throwable throwable);
    
    /**
     * info without throwable
     * 
     * @param message message to log
     */
    void info(final String message);
    
    /**
     * warn without throwable
     * 
     * @param message message to log
     */
    void warn(final String message);
    
    /**
     * warn with throwable
     * 
     * @param message message to log
     * @param throwable throwable
     */
    void warn(final String message, final Throwable throwable);
    
    /**
     * To know if the error level is enabled.
     * 
     * @return true if the error level is enabled
     */
    boolean isErrorEnabled();
    
    /**
     * To know if the warning level is enabled.
     * 
     * @return true if the warning level is enabled
     */
    boolean isWarnEnabled();
    
    /**
     * To know if the info level is enabled.
     * 
     * @return true if the info level is enabled
     */
    boolean isInfoEnabled();
    
    /**
     * To know if the debug level is enabled.
     * 
     * @return true if the debug level is enabled
     */
    boolean isDebugEnabled();
}
