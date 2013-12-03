package org.easysdi.monitor.biz.logging;

/**
 * Provides constants for logging pruposes.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class Constants {

    public static final String LOG_MESSAGE_HTTP_ERROR = "Connection error HTTP";
    public static final String LOG_MESSAGE_NO_ERROR   = "No error";
    public static final String LOG_MESSAGE_TIMEOUT    = "Service timed out";
    
    
    
    /**
     * Dummy constructor used to prevent instantiation.
     */
    private Constants() {
        throw new UnsupportedOperationException(
                                           "This class can't be instantiated.");
    }
}
