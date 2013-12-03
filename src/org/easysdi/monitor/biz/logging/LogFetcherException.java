package org.easysdi.monitor.biz.logging;

/**
 * Represents a log fetching error. 
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class LogFetcherException extends Exception {

    private static final long serialVersionUID = 6447374719847371783L;


    /**
     * Creates a new log fetching exception.
     */
    public LogFetcherException() {

    }


    /**
     * Creates a new log fetching exception.
     * 
     * @param   message a string explaining while this exception occurred
     */

    public LogFetcherException(String message) {
        super(message);
    }


    /**
     * Creates a new log fetching exception.
     * 
     * @param   cause   the inner error for this exception
     */

    public LogFetcherException(Throwable cause) {
        super(cause);
    }


    /**
     * Creates a new log fetching exception.
     * 
     * @param   message a string explaining while this exception occurred
     * @param   cause   the inner error for this exception
     */
    public LogFetcherException(String message, Throwable cause) {
        super(message, cause);
    }

}
