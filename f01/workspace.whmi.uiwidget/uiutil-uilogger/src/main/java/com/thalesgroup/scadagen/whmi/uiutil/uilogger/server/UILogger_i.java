package com.thalesgroup.scadagen.whmi.uiutil.uilogger.server;

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
	public String getName();

	public boolean isTraceEnabled();
	public boolean isInfoEnabled();
	public boolean isDebugEnabled();
    /**
     * trace without throwable
     * 
     * @param message message to log
     */
    public void trace(final String message);
    public void trace(String format, Object arg);
    public void trace(String format, Object arg1, Object arg2);
    public void trace(final String message, Object[] argArray);
    
    /**
     * info without throwable
     * 
     * @param message message to log
     */
    public void info(final String message);
    public void info(String format, Object arg);
    public void info(String format, Object arg1, Object arg2);
    public void info(final String message, Object[] argArray);
    
    /**
     * debug without throwable
     * 
     * @param message message to log
     */
    public void debug(final String message);
    public void debug(String format, Object arg);
    public void debug(String format, Object arg1, Object arg2);
    public void debug(String format, Object[] argArray);

    /**
     * debug with throwable
     * 
     * @param message message to log
     * @param throwable throwable
     */
    public void debug(final String message, final Throwable throwable);
    
    /**
     * warn without throwable
     * 
     * @param message message to log
     */
     public void warn(final String message);
     
     public void warn(String format, Object arg);
     public void warn(String format, Object arg1, Object arg2);
     public void warn(String format, Object[] argArray);
     
    /**
     * warn with throwable
     * 
     * @param message message to log
     * @param throwable throwable
     */
    public void warn(final String message, final Throwable throwable);
    
    /**
     * error without throwable
     * 
     * @param message message to log
     */
    public void error(final String message);
    
    public void error(String format, Object arg);
    public void error(String format, Object arg1, Object arg2);
    public void error(String format, Object[] argArray);
    
    /**
     * error with throwable
     * 
     * @param message message to log
     * @param throwable throwable
     */
    public void error(final String message, final Throwable throwable);
}
