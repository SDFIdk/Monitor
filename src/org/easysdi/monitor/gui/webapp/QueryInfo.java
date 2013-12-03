package org.easysdi.monitor.gui.webapp;

import java.util.Map;

import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.job.QueryConfiguration;
import org.easysdi.monitor.biz.job.ServiceMethod;
import org.easysdi.monitor.biz.job.ServiceType;

/**
 * Holds and validates user input for query modification or creation.
 * <p>
 * All the properties can be set to <code>null</code> to indicate that the job's
 * corresponding parameter should not be altered. In consequence, all getters 
 * can return <code>null</code> as a legit (non-error) value. For non-mandatory
 * parameters, you must use an empty string to indicate no value.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class QueryInfo {

    private ServiceMethod method;
    private String        name;
    private Job           parentJob;
    private String 		  soapUrl;
    private String 		  queryMethod;
    private String		  queryServiceType;	
    private String        url;
    private String		  login;
    private String		  password;
    
    
    
    /**
     * Creates a new query information object.
     */
    private QueryInfo() {
        
    }



    /**
     * Defines the service method used by this query.
     * 
     * @param   methodName  the name of the method
     */
    private void setMethod(String methodName) {

        if (StringTools.isNullOrEmpty(methodName)) {
            throw new IllegalArgumentException(
                                           "Service method must be defined");
        }

        final ServiceMethod newMethod = ServiceMethod.getObject(methodName);

        if (null == newMethod) {
            throw new IllegalArgumentException("Unknown service method");
        }
        
        final ServiceType jobServiceType = 
            this.getParentJob().getConfig().getServiceType();

        if (!newMethod.isValidForType(jobServiceType)) {
            throw new IllegalArgumentException(
                          "This method isn't valid for the job's service type");
        }

        this.method = newMethod;
    }



    /**
     * Gets the service method to be used by this query.
     * 
     * @return  the method
     */
    private ServiceMethod getMethod() {
        return this.method;
    }



    /**
     * Defines the name of this query.
     * 
     * @param   newName the query name
     */
    private void setName(String newName) {

        if (StringTools.isNullOrEmpty(newName)) {
            throw new IllegalArgumentException("Name can't be null or empty");
        }

        this.name = newName;
    }



    /**
     * Gets the new name of this query.
     * 
     * @return  the query name
     */
    private String getName() {
        return this.name;
    }

    /**
     * Defines the soapUrlof this query.
     * 
     * @param   querySoapUrl the query soapUrl
     */
    private void setSoapUrl(String querySoapUrl) {

    
        this.soapUrl = querySoapUrl;
    }



    /**
     * Gets the new soapUrl of this query.
     * 
     * @return  the query soapUrl
     */
    private String getSoapUrl() {
        return this.soapUrl;
    }
    
    /**
     * Gets the new query's LOGIN.
     * 
     * @return  <ul>
     *          <li>the LOGIN for the polled web service</li>
     *          </ul>
     */
    private String getLogin() {
        return this.login;
    }
    
    /**
     * Gets the new query's Password
     * 
     * @return  <ul>
     *          <li>the password for the polled web service</li>
     *          </ul>
     */
    private String getPassword() {
        return this.password;
    }
    
    /**
     * Gets the new query's URL.
     * 
     * @return  <ul>
     *          <li>the URL for the polled web service</li>
     *          <li><code>null</code> if the job's URL shouldn't be changed</li>
     *          </ul>
     */
    private String getUrl() {
        return this.url;
    }

    /**
	 * @return the queryMethod
	 */
	private String getQueryMethod() {
		return queryMethod;
	}



	/**
	 * @param queryMethod the queryMethod to set
	 */
	private void setQueryMethod(String queryMethod) {
		this.queryMethod = queryMethod;
	}



	/**
	 * @return the queryServiceType
	 */
	private String getQueryServiceType() {
		return queryServiceType;
	}



	/**
	 * @param queryServiceType the queryServiceType to set
	 */
	private void setQueryServiceType(String queryServiceType) {
		this.queryServiceType = queryServiceType;
	}
	
    /**
     * Defines the polled web query's login.
     * 
     * @param   login  the login of the web service polled by the job
     */
    private void setLogin(String login) {
        this.login = login;
    }
    
    /**
     * Defines the polled web query's URL.
     * 
     * @param   newUrl  the URL of the web service polled by the job
     */
    private void setUrl(String newUrl) {

        if (StringTools.isNullOrEmpty(newUrl)) {
            throw new IllegalArgumentException("URL must be defined");
        }

        this.url = newUrl;
    }
    
    /**
     * Defines the polled web query's Password.
     * 
     * @param   password  the password of the web service polled by the job
     */
    private void setPassword(String password) {
        this.password = password;
    }


	/**
     * Defines the job that this query defines.
     * 
     * @param   job the parent job 
     */
    private void setParentJob(Job job) {

        if (null == job || !job.isValid(false)) {
            throw new IllegalArgumentException("Job must be a valid one");
        }

        this.parentJob = job;
    }



    /**
     * Gets the job that this query defines.
     * 
     * @return  the parent job
     */
    private Job getParentJob() {
        return this.parentJob;
    }


    
    /**
     * Creates a query information object from the parameters of a request.
     * 
     * @param   requestParams       a map containing the request parameters 
     * @param   parentJob           the job that this query defines
     * @param   enforceMandatory    <code>true</code> to throw an exception
     *                              if a mandatory parameter is not set
     * @return                      the action information object
     */
    public static QueryInfo createFromParametersMap(
            Map<String, String> requestParams, Job parentJob, 
            boolean enforceMandatory) {
        final QueryInfo newQueryInfo = new QueryInfo();

        newQueryInfo.setParentJob(parentJob);

        final String nameString = requestParams.get("name");
        final String soapActionString = requestParams.get("soapUrl");
        final String queryServiceTypeString = requestParams.get("queryServiceType");
        final String queryMethodString = requestParams.get("queryMethod");
        newQueryInfo.setSoapUrl(soapActionString);
        newQueryInfo.setQueryMethod(queryMethodString);
        newQueryInfo.setQueryServiceType(queryServiceTypeString);
        if (enforceMandatory || !StringTools.isNullOrEmpty(nameString)) {
            newQueryInfo.setName(nameString);
        }

        final String methodString = requestParams.get("serviceMethod");
        
        if (enforceMandatory || !StringTools.isNullOrEmpty(methodString)) {
            newQueryInfo.setMethod(methodString);
        }
        newQueryInfo.setUrl(requestParams.get("url"));
        newQueryInfo.setLogin(requestParams.get("login"));
        newQueryInfo.setPassword(requestParams.get("password"));
        return newQueryInfo;
    }



    /**
     * Updates a query with the informations contained in this object.
     * 
     * @param   query   the query to update
     * @return          <code>true</code> if the update succeeded
     */
    public boolean modifyQueryConfig(Query query) {

        if (null == query) {
            throw new IllegalArgumentException("Query can't be null");
        }

        final QueryConfiguration config = query.getConfig();

        if (null != this.getName()) {
            config.setQueryName(this.getName());
        }

        if (null != this.getMethod()) {
            config.setMethod(this.getMethod());
        }
        
        if (null != this.getSoapUrl()) {
            config.setQuerySoapUrl(this.getSoapUrl());
        }
        
        if (null != this.getQueryMethod()) {
            config.setQueryMethod(this.queryMethod);
        }
        if (null != this.getQueryServiceType()) {
            config.setQueryServiceType(this.queryServiceType); 
        }
        if(this.getUrl() != null)
        {
        	config.setUrl(this.getUrl());
        }
        if(this.getLogin() != null)
        {
        	config.setLogin(this.getLogin());
        }
        if(this.getPassword() != null)
        {
        	config.setPassword(this.getPassword());
        }
        
        
        return query.persist();
    }



    /**
     * Creates a new query based on the informations contained in this object.
     * 
     * @return  the new query
     */
    public Query createQuery() {
        final Query newQuery = new Query(this.getParentJob(), this.getName(),
                                         this.getMethod(), this.getSoapUrl(),this.queryMethod,
                                         this.queryServiceType,this.url,this.login,this.password);
        if (newQuery.isValid()) {

            if (newQuery.persist()) {
                return newQuery;
            }
        }
        return null;
    }
}
