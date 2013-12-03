package org.easysdi.monitor.biz.job;

/**
 * Signals a problem while processing a query parameter.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.1, 2010-04-30
 *
 */
public class QueryParameterNotFoundException extends Exception {

    private static final long serialVersionUID = 5683518268207504040L;
    
    private final String parameterName;
    private final long queryId;

    
    
    /**
     * Creates a new query parameter exception.
     * 
     * @param   targetParamName   the name of the parameter that caused this 
     *                            exception
     * @param   parentQueryId     the long identifying the query whose 
     *                            parameters
     */
    public QueryParameterNotFoundException(String targetParamName, 
                                           long parentQueryId) {
        super();
        this.parameterName = targetParamName;
        this.queryId = parentQueryId;
    }


    
    /**
     * Creates a new query parameter exception.
     * 
     * @param   targetParamName the name of the parameter that caused this 
     *                          exception
     * @param   parentQueryId   the long identifying the query whose 
     *                          parameters
     * @param   cause           the exception or error that caused this 
     *                          exception
     */
    public QueryParameterNotFoundException(String targetParamName, 
                                           long parentQueryId, 
                                           Throwable cause) {
     
        super(cause);
        this.parameterName = targetParamName;
        this.queryId = parentQueryId;
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return String.format(
                 "The parameter '%1$s' isn't defined for query %2$d.", 
                 this.parameterName, this.queryId);
    }
    
    
    
    /**
     * Gets the name of the parameter that wasn't found.
     * 
     * @return the searched parameter name
     */
    public String getParameterName() {
        return this.parameterName;
    }
    
    
    
    /**
     * Gets the identifier of the query whose parameters have been searched.
     * 
     * @return  the long identifying the parent query
     */
    public long getQueryId() {
        return this.queryId;
    }
}
