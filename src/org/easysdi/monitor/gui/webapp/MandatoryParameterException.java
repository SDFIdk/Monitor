package org.easysdi.monitor.gui.webapp;


/**
 * Signals a problem with a parameter whose value should not be null.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.1, 2010-04-30
 *
 */
public class MandatoryParameterException extends Exception {

    private static final long serialVersionUID = 2907386273905211339L;
    
    private final String parameterName;
    
    
    /**
     * Creates a new exception concerning a mandatory parameter.
     * 
     * @param   paramName   the name of the mandatory parameter
     */
    public MandatoryParameterException(String paramName) {
        super();
        this.parameterName = paramName;
    }
    
    
    
    /**
     * Creates a new exception concerning a mandatory parameter.
     * 
     * @param   paramName   the name of the mandatory parameter
     * @param   cause       the inner exception that caused this one
     */
    public MandatoryParameterException(String paramName, Throwable cause) {
        super(cause);
        this.parameterName = paramName;
    }



    /**
     * Gets the name of the mandatory parameter that raised this exception.
     * 
     * @return  the name of the mandatory parameter
     */
    public String getParameterName() {
        return this.parameterName;
    }
    
    
    
    /**
     * Gets the default message for this exception.
     * 
     * @return  the exception message
     */
    @Override
    public String getMessage() {
        
        return String.format("Value of parameter '%1$s' cannot be null.", 
                             this.getParameterName());
        
    }
}
