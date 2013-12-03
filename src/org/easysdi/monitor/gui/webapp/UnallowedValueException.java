package org.easysdi.monitor.gui.webapp;

import org.apache.commons.lang.StringUtils;

/**
 * Signals that a value isn't among those accepted in a given context.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class UnallowedValueException extends Exception {
    
    private static final long serialVersionUID = 9205311952867186305L;
    
    private final Object value;

    
    
    /**
     * Creates a new exception about an illegal value.
     * 
     * @param   illegalValue    the value that caused this exception
     */
    public UnallowedValueException(Object illegalValue) {
        super();
        this.value = illegalValue;
    }
    
    
    
    /**
     * Creates a new exception about an illegal value.
     * 
     * @param   illegalValue    the value that caused this exception
     * @param   message         a message explaining why this value isn't
     *                          allowed
     */
    public UnallowedValueException(Object illegalValue, String message) {
        super(message);
        this.value = illegalValue;
    }
    
    
    
    /**
     * Creates a new exception about an illegal value.
     * 
     * @param   illegalValue    the value that caused this exception
     * @param   cause           the inner exception that caused this exception
     *                          to be raised
     */
    public UnallowedValueException(Object illegalValue, Throwable cause) {
        super(cause);
        this.value = illegalValue;
    }
    
    
    
    /**
     * Creates a new exception about an illegal value.
     * 
     * @param   illegalValue    the value that caused this exception
     * @param   message         a message explaining why this value isn't
     *                          allowed
     * @param   cause           the inner exception that caused this exception
     *                          to be raised
     */
    public UnallowedValueException(Object illegalValue, String message, 
                                   Throwable cause) {
        
        super(message, cause);
        this.value = illegalValue;
    }



    /**
     * Gets the value that caused this exception.
     * 
     * @return the illegal value
     */
    public Object getValue() {
        return this.value;
    }
    
    
    
    /**
     * Gets a message explaining why this exception has been raised.
     * 
     * @return  the message explaining the exception 
     */
    @Override
    public String getMessage() {
        final String baseMessage 
            = StringUtils.defaultString(super.getMessage(), 
                                        "The value isn't allowed");
        final Object illegalValue = this.getValue();
        
        return String.format("%1$s: %2$s", baseMessage, 
                             ((null != illegalValue) 
                                 ? illegalValue.toString()
                                 : "null"));
    }
}
