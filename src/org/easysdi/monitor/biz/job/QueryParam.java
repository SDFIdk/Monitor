package org.easysdi.monitor.biz.job;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.easysdi.monitor.biz.util.HashCodeConstants;
import org.easysdi.monitor.dat.dao.QueryDaoHelper;

/**
 * Represents a parameter used by a query to retrieve a result
 * <p>
 * The required parameters and the correct value that they should hold depend on
 * many factors, some of which are difficult to check by this application.
 * <p>
 * Consequently, it is the user's responsibility to ensure that the parameters
 * are correct for the request.
 * <p>
 * Query parameters are defined through the {@link QueryConfiguration} object
 * <p>
 * <b>GET requests</b>
 * <p>
 * The following parameters are managed by the application and don't need to be
 * set through a <code>QueryParam</code> object:
 * <ul>
 * <li>the service type (WFS, CSW, etc.) and its version</li>
 * <li>the HTTP method used to poll the service (GET, in this case)</li>
 * <li>the service method to be polled (GetCapabilities, for instance)</li>
 * </ul>
 * <p>
 * All the other required parameters should be defined.
 * <p>
 * <b>POST request</b>
 * <p>
 * One parameter should be defined with the name XMLREQUEST. Its value is the
 * (full) XML stating the request parameters.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * @see QueryConfiguration#addParam(QueryParam)
 * @see QueryConfiguration#removeParam(String)
 * @see QueryConfiguration#setParam(String, String)
 * 
 */
public class QueryParam implements Serializable {

    private static final long serialVersionUID = 4031072066993778466L;
    
    private String            name;
    private long              parentQueryId;
    private String            value;



    /**
     * No-argument constructor, used by the persistance mechanism.
     */
    @SuppressWarnings("unused")
    private QueryParam() {

    }



    /**
     * Creates a new query parameter.
     * 
     * @param   parentQuery the query that the parameter applies to
     * @param   paramName   the parameter name
     * @param   paramValue  the parameter value
     */
    public QueryParam(Query parentQuery, String paramName, String paramValue) {

        if (null == paramName || paramName.equals("")) {
            throw new IllegalArgumentException(
                    "Parameter name cannot be null or blank");
        }

        this.setParentQuery(parentQuery);
        this.setName(paramName);
        this.setValue(paramValue);
    }



    /**
     * Defines this parameter's name.
     * 
     * @param   newName the parameter's name
     */
    public void setName(String newName) {
        this.name = newName;
    }



    /**
     * Gets this parameter's name.
     * 
     * @return  the parameter's name
     */
    public String getName() {
        return this.name;
    }



    /**
     * Defines the query that this parameter applies to.
     * 
     * @param   parentQuery the parent query
     */
    public void setParentQuery(Query parentQuery) {

        if (null == parentQuery || !parentQuery.isValid()) {
            throw new IllegalArgumentException(
                    "Parameter must have a valid parent query");
        }

        this.setParentQueryId(parentQuery.getQueryId());
    }



    /**
     * Defines the query that this parameter applies to from its identifier.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes and 
     * shouldn't be called directly.</i>
     * 
     * @param   queryId the long identifying the parent query
     */
    private void setParentQueryId(long queryId) {

        if (1 > queryId) {
            throw new IllegalArgumentException("Invalid query identifier");
        }

        this.parentQueryId = queryId;
    }



    /**
     * Gets the identifier of the query that this parameter applies to.
     * 
     * @return the parent query's identifier
     */
    public long getParentQueryId() {
        return this.parentQueryId;
    }



    /**
     * Defines this parameter's value.
     * 
     * @param   newValue    a string containing this parameter's value
     */
    public void setValue(String newValue) {
        this.value = newValue;
    }



    /**
     * Gets this parameter's value.
     * 
     * @return  the string containing this parameter's value
     */
    public String getValue() {
        return this.value;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object thatObject) {

        if (thatObject instanceof QueryParam) {
            final QueryParam that = (QueryParam) thatObject;

            return (this.getName() == that.getName() 
                    && this.getParentQueryId() == that.getParentQueryId());

        }

        return false;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder 
            = new HashCodeBuilder(HashCodeConstants.SEED, 
                                  HashCodeConstants.MULTIPLIER);
        
        hashCodeBuilder.append(this.getParentQueryId());
        hashCodeBuilder.append(this.getName());
        
        return hashCodeBuilder.toHashCode();
    }



    /**
     * Erases the parameter through the persistance mechanism.
     * 
     * @return  <code>true</code> if the parameter has been successfully deleted
     */
    public boolean delete() {

        return QueryDaoHelper.getQueryDao().deleteQueryParam(this);

    }

}
