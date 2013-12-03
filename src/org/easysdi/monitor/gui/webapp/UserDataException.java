package org.easysdi.monitor.gui.webapp;

/**
 * Signals an error due to incorrect user input.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class UserDataException extends Exception {

    /**
     * Serial version identifier (for serialization). 
     */
    private static final long serialVersionUID = -357113889121723330L;
    
    
    /**
     * Creates a new user data exception.
     */
    public UserDataException() {
        super();
    }
    
    
    
    /**
     * Creates a new user data exception.
     * 
     * @param   message a string explaining the error
     */
    public UserDataException(String message) {
        super(message);
    }
    
    
    
    /**
     * Creates a new user data exception.
     * 
     * @param   cause   the throwable that triggered this exception
     */
    public UserDataException(Throwable cause) {
        super(cause);
    }
    
    
    
    /**
     * Creates a new user data exception.
     * 
     * @param   message     a string explaining the error
     * @param   cause       the throwable that triggered this exception
     */
    public UserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
