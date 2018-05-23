package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

/**
 * UILogger interface
 * 
 * @author t0096643
 *
 */
public interface UILogger_i {
	
    /**
     * Get Logger name
     * @return Logger name
     */
    String getName();
    
    void clear();
    
    /**
     * Set which log level is enabled.
     */
    void setCurrentLogLevel(int level);
    
    /**
     * Get which log level is enabled.
     */
    int getCurrentLogLevel();

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
     * fatal without throwable
     * 
     * @param message message to log
     */
    void fatal(final String message);
    
    
    /**
     * warn with throwable
     * 
     * @param message message to log
     * @param throwable throwable
     */
    void warn(final String message, final Throwable throwable);
    
    /**
     * To know if the level is enabled at class name.
     * 
     * @return true if the level enabled at class name
     */
    boolean isLevelEnabled(int level);
    
    /**
     * To know if the fatal level is enabled.
     * 
     * @return true if the fatal level is enabled
     */
    @Deprecated boolean isFatalEnabled();
    
    /**
     * To know if the error level is enabled.
     * 
     * @return true if the error level is enabled
     */
    @Deprecated boolean isErrorEnabled();
    
    /**
     * To know if the warning level is enabled.
     * 
     * @return true if the warning level is enabled
     */
    @Deprecated boolean isWarnEnabled();
    
    /**
     * To know if the info level is enabled.
     * 
     * @return true if the info level is enabled
     */
    @Deprecated boolean isInfoEnabled();
    
    /**
     * To know if the trace level is enabled.
     * 
     * @return true if the info level is enabled
     */
    @Deprecated boolean isTraceEnabled();
    
    /**
     * To know if the debug level is enabled.
     * 
     * @return true if the debug level is enabled
     */
    @Deprecated boolean isDebugEnabled();

	void begin		(String function);
	void end		(String function);
	void beginEnd	(String function);
	void beginEnd	(String function, String message);
	void beginEnd	(String function, String message, Object arg1);
	void beginEnd	(String function, String message, Object arg1, Object arg2);
	void beginEnd	(String function, String message, Object[] args);
	void trace		(String function, String message);
	void trace		(String function, String message, Object arg1);
	void trace		(String function, String message, Object arg1, Object arg2);
	void trace		(String function, String message, Object[] args);
	void info		(String function, String message);
	void info		(String function, String message, Object arg1);
	void info		(String function, String message, Object arg1, Object arg2);
	void info		(String function, String message, Object[] args);
	void debug		(String function, String message);
	void debug		(String function, String message, Object arg1);
	void debug		(String function, String message, Object arg1, Object arg2);
	void debug		(String function, String message, Object[] args);
	void warn		(String function, String message);
	void warn		(String function, String message, Object arg1);
	void warn		(String function, String message, Object arg1, Object arg2);
	void warn		(String function, String message, Object[] args);
	void error		(String function, String message);
	void error		(String function, String message, Object arg1);
	void error		(String function, String message, Object arg1, Object arg2);
	void error		(String function, String message, Object[] args);
	void fatal		(String function, String message);
	void fatal		(String function, String message, Object arg1);
	void fatal		(String function, String message, Object arg1, Object arg2);
	void fatal		(String function, String message, Object[] args);
}
