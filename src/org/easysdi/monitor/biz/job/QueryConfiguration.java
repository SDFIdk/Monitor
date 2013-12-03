package org.easysdi.monitor.biz.job;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.deegree.portal.owswatch.ConfigurationsException;
import org.deegree.portal.owswatch.Constants;
import org.deegree.portal.owswatch.ServiceConfiguration;
import org.easysdi.monitor.dat.dao.IServiceDao;
import org.easysdi.monitor.dat.dao.ServiceDaoHelper;


/**
 * Holds the configuration of a query.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * @see Query
 */
public class QueryConfiguration {
    
    private final Logger logger = Logger.getLogger(QueryConfiguration.class);

    private ServiceMethod   method;

    private long            methodId;

    private String          name;
    
    private String          soapUrl;

    private Set<QueryParam> params = new HashSet<QueryParam>();

    private Job             parentJob;

    private Query           query;
    
    private String			queryMethod;
    
    private String			queryServiceType;
    private String          url;
    
    private String 			login;
    
    private String 			password;


    /**
     * No-argument constructor, used by the persistance mechanism.
     */
    @SuppressWarnings("unused")
    private QueryConfiguration() {

    }

    /**
     * Creates a new query configuration.
     * 
     * @param   newParentJob    the job which the query is attached to
     * @param   newParentQuery  the query that this object configures
     * @param   queryName       the name that identifies the query
     * @param   methodName      the name of the method to test
     */
    public QueryConfiguration(Job newParentJob, Query newParentQuery,
                              String queryName, String methodName) {
        this.setQuery(newParentQuery);
        this.setParentJob(newParentJob);
        this.setQueryName(queryName);
        this.setMethod(methodName);
    }



    /**
     * Creates a new query configuration.
     * 
     * @param   newParentJob    the job which the query is attached to
     * @param   newParentQuery  the query that this object configures
     * @param   queryName       the name that identifies the query
     * @param   newMethod       the service method object
     */
    public QueryConfiguration(Job newParentJob, Query newParentQuery,
                              String queryName, ServiceMethod newMethod) {
        this.setQuery(newParentQuery);
        this.setParentJob(newParentJob);
        this.setQueryName(queryName);
        this.setMethod(newMethod);
    }

    /**
     * Creates a new query configuration.
     * 
     * @param   newParentJob    the job which the query is attached to
     * @param   newParentQuery  the query that this object configures
     * @param   queryName       the name that identifies the query
     * @param   newMethod       the service method object
     */
    public QueryConfiguration(Job newParentJob, Query newParentQuery,
                              String queryName, ServiceMethod newMethod, String soapUrl,
                              String queryMethod,String queryServiceType,String query_url,
                              String login,String password) {
        this.setQuery(newParentQuery);
        this.setParentJob(newParentJob);
        this.setQueryName(queryName);
        this.setMethod(newMethod);
        this.setSoapUrl(soapUrl);
        this.setQueryMethod(queryMethod);
        this.setQueryServiceType(queryServiceType);
        this.setUrl(query_url);
        this.setLogin(login);
        this.setPassword(password);
    }


    /**
     * Defines the web service method to test.
     * 
     * @param   queryMethod the service method object
     */
    public void setMethod(ServiceMethod queryMethod) {

        if (null == queryMethod) {
            throw new IllegalArgumentException(
                    String.format("Method can't be null"));
        }

        if (!this.checkMethodValidity(queryMethod)) {
            throw new IllegalArgumentException(String.format(
                    "Method %1$s isn't supported for this job type",
                    queryMethod.getName()));
        }

        this.method = queryMethod;
        this.setMethodId(queryMethod.getId());
    }



    /**
     * Defines the web service method to test.
     * 
     * @param   queryMethodName the method's name
     */
    public void setMethod(String queryMethodName) {
        final ServiceMethod newMethod 
            = ServiceMethod.getObject(queryMethodName);

        if (null == newMethod) {
            throw new IllegalArgumentException(String.format(
                    "Unknown method '%1$s'", queryMethodName));
        }

        this.setMethod(newMethod);
        this.setMethodId(newMethod.getId());
    }



    /**
     * Checks if the given method is compatible with the parent job's service
     * type.
     * 
     * @param   serviceMethod   the service method object to check
     * @return                  <code>true</code> if the method can be used by 
     *                          this query
     */
    private boolean checkMethodValidity(ServiceMethod serviceMethod) {
        final ServiceType jobServiceType 
            = this.getParentJob().getConfig().getServiceType();

        return serviceMethod.isValidForType(jobServiceType);
    }



    /**
     * Gets the method tested by this query.
     * 
     * @return  the method the service method object
     */
    public ServiceMethod getMethod() {
        final long currentMethodId = this.getMethodId();
        final boolean isMismatch 
            = (null == this.method || this.method.getId() != currentMethodId);

        if (0 < currentMethodId && isMismatch) {
            final IServiceDao daoObject = ServiceDaoHelper.getServiceDao(); 
            final ServiceMethod newMethod 
                = daoObject.getServiceMethod(currentMethodId); 
            this.setMethod(newMethod);
        }

        return this.method;
    }



    /**
     * Defines the tested method by its identifier.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use. It shouldn't be
     * called directly.</i>
     * 
     * @param   newMethodId the long identifying the service method
     */
    private void setMethodId(long newMethodId) {
        this.methodId = newMethodId;
    }



    /**
     * Gets the tested method's identifier.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use and shouldn't be
     * called directly. Its result isn't guaranteed to match the actual
     * method.</i>
     * 
     * @return  the methodId that the long identifying the service method
     */
    private long getMethodId() {
        return this.methodId;
    }



    /**
     * Defines the name used to identify this query.
     * <p>
     * A query can be identified either by a machine-friendly identifier or by a
     * more human-friendly name. The name must be unique among all the queries
     * for a given job.
     * 
     * @param   newName the name that identifies the query
     */
    public void setQueryName(String newName) {
        final Job currentParentJob = this.getParentJob();
        
        if (null == currentParentJob) {
            throw new UnsupportedOperationException(
                    "First define the query's parent job before " 
                    + "setting its name");
        }

        if (null != currentParentJob.getQueryByName(newName)) {

            throw new IllegalArgumentException(
                    "The job already has a query with this name");

        }

        this.name = newName;
    }



    /**
     * Defines the name of this query.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use (persistance)
     * and shouldn't be called directly. Please rather use the public
     * {@link QueryConfiguration#setQueryName(String) setQueryName(String)}
     * method instead.</i>
     * 
     * @param   queryName    the name that this query is identified by
     */
    @SuppressWarnings("unused")
    private void setName(String queryName) {
        this.name = queryName;
    }



    /**
     * Gets the name identifying this query.
     * 
     * @return  the name that identifies this query
     * @see     QueryConfiguration#setQueryName(String)
     */
    public String getQueryName() {
        return this.name;
    }



    /**
     * Gets the name of this query.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use (persistance)
     * and shouldn't be called directly. Please rather use the public
     * {@link QueryConfiguration#getQueryName() getQueryName()} method
     * instead.</i>
     * 
     * @return  the name that identifies this query
     */
    @SuppressWarnings("unused")
    private String getName() {
        return this.name;
    }



    /**
     * Defines the parameters used by this query.
     * <p>
     * Previous parameters for this query will be overwritten by this method.
     * 
     * @param   queryParams a set containing query parameters
     * @see     QueryParam
     */
    @SuppressWarnings("unused")
    private void setParams(Set<QueryParam> queryParams) {
        this.params = queryParams;
    }

    /**
     * Defines the soapUrl of this query.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use (persistance)
     * and shouldn't be called directly. 
     * 
     * @param   querySoapUrl    the soapUrl that this query is identified by
     */
    private void setSoapUrl(String querySoapUrl) {
        this.soapUrl = querySoapUrl;
    }


    /**
     * Defines the soapUrl of this query.
     * 
     * @param   querySoapUrl    the soapUrl that this query is identified by
     */
    public void setQuerySoapUrl(String querySoapUrl) {
    	setSoapUrl( querySoapUrl);
    }

    /**
     * Gets the soapUrl of this query.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use (persistance)
     * and shouldn't be called directly. P
     * 
     * @return  the soapUrl that identifies this query
     */
    @SuppressWarnings("unused")
    private String getSoapUrl() {
        return this.soapUrl;
    }

    
    /**
     * Gets the name identifying this query.
     * 
     * @return  the name that identifies this query
     * @see     QueryConfiguration#setQueryName(String)
     */
    public String getQuerySoapUrl() {
        return this.soapUrl;
    }
    
    
    /**
	 * @return the queryMethod
	 */
	public String getQueryMethod() {
		return queryMethod;
	}



	/**
	 * @param queryMethod the queryMethod to set
	 */
	public void setQueryMethod(String queryMethod) {
		this.queryMethod = queryMethod;
	}



	/**
	 * @return the queryServiceType
	 */
	public String getQueryServiceType() {
		return queryServiceType;
	}



	/**
	 * @param queryServiceType the queryServiceType to set
	 */
	public void setQueryServiceType(String queryServiceType) {
		this.queryServiceType = queryServiceType;
	}



	/**
     * Gets the parameters to be used by this query.
     * 
     * @return  a set containing the parameters for this query
     */
    public Set<QueryParam> getParams() {
        return this.params;
    }



    /**
     * Defines the job that this query is attached to.
     * 
     * @param   newParentJob    the job that this query defines
     */
    private void setParentJob(Job newParentJob) {
        this.parentJob = newParentJob;
    }



    /**
     * Gets the job that this query is attached to.
     * 
     * @return  the job that this query defines
     */
    public Job getParentJob() {
        return this.parentJob;
    }



    /**
     * Defines the query that this object configures.
     * 
     * @param   newParentQuery  the parent query
     */
    private void setQuery(Query newParentQuery) {

        if (null == newParentQuery) {
            throw new IllegalArgumentException("Parent query can't be null");
        }

        this.query = newParentQuery;
    }



    /**
     * Gets the query that this object configures.
     * 
     * @return the parent query
     */
    private Query getQuery() {
        return this.query;
    }



    /**
     * Checks if this configuration is valid.
     * <p>
     * A query configuration is valid if:
     * <ol>
     * <li>a valid parent job is set</li>
     * <li>a service method is set</li>
     * </ol>
     * <p>
     * This check is only a very basic one. It doesn't guarantee that the query
     * will successfully run.
     * 
     * @return  <code>true</code> if the query configuration is valid
     * @see     Job#isValid(boolean)
     */
    public boolean isValid() {
        final Job currentParentJob = this.getParentJob();
        final boolean isParentJobValid 
            = (null != currentParentJob && currentParentJob.isValid(false));
        final boolean isMethodValid = (null != this.getMethod()); 

        return (isParentJobValid && isMethodValid);

    }

    /**
     * Generates a custom configuration object suitable for soap/get/post requests, so it can be
     * polled.
     * 
     * @return  the owsWatch-compatible service configuration
     */
    public CustomServiceConfiguration toCustomConfig() {
    	CustomServiceConfiguration customConfig;
        final JobConfiguration parentJobConfig 
            = this.getParentJob().getConfig();

        try {
        	String httpMethod =  parentJobConfig.getHttpMethod().getName();
        	if(!"".equals(this.getQuery().getConfig().getQueryMethod()))
        	{
        		httpMethod = this.getQuery().getConfig().getQueryMethod();
        	}
        	
        	customConfig = new CustomServiceConfiguration(
                    (int) this.getQuery().getQueryId(), this.name,
                    httpMethod,
                    this.url, true,
                    parentJobConfig.getTestInterval(),
                    parentJobConfig.getTimeout(), this.getCustomParams(),this.getLogin(),this.getPassword());	
        	
        	customConfig.setUserCreds(parentJobConfig.getLogin(),
                                   parentJobConfig.getPassword());

        } catch (ConfigurationsException e) {
            this.logger.error(
                "An error occurred while the OWS configuration was generated.", 
                e);
            customConfig = null;
        }

        return customConfig;
    }
    
    /**
     * Generates a custom configuration object suitable for soap/get/post requests, so it can be
     * polled.
     * 
     * @return  the owsWatch-compatible service configuration
     */
    public CustomServiceConfiguration toCustomFTPConfig() {
    	CustomServiceConfiguration customConfig;
        final JobConfiguration parentJobConfig = this.getParentJob().getConfig();

        try {
        	String httpMethod =  parentJobConfig.getHttpMethod().getName();
        	if(!"".equals(this.getQuery().getConfig().getQueryMethod()))
        	{
        		httpMethod = this.getQuery().getConfig().getQueryMethod();
        	}
        	
        	customConfig = new CustomServiceConfiguration(
                    (int) this.getQuery().getQueryId(), this.name,
                    httpMethod,
                    this.url, true,
                    parentJobConfig.getTestInterval(),
                    parentJobConfig.getTimeout(), this.getCustomParams(),this.getLogin(),this.getPassword());	
        	
        	customConfig.setUserCreds(parentJobConfig.getLogin(),parentJobConfig.getPassword());
        } catch (ConfigurationsException e) {
            this.logger.error("An error occurred while the OWS configuration was generated.", e);
            customConfig = null;
        }
        return customConfig;
    }


    /**
     * Generates an owsWatch-compatible configuration object, so it can be
     * polled.
     * 
     * @return  the owsWatch-compatible service configuration
     */
    public ServiceConfiguration toOwsConfig() {
        ServiceConfiguration owsConfig;
        final JobConfiguration parentJobConfig 
            = this.getParentJob().getConfig();
        String httpMethodQuery = parentJobConfig.getHttpMethod().getName();
        if(!"".equals(this.getQueryMethod()))
        {
        	httpMethodQuery = this.getQueryMethod();
        }
        	
        try {
        	owsConfig = new ServiceConfiguration(
                    (int) this.getQuery().getQueryId(), this.name,
                    httpMethodQuery,
                    this.getUrl(), true,
                    parentJobConfig.getTestInterval(),
                    parentJobConfig.getTimeout(), this.getOwsParams(),this.getLogin(),this.getPassword());
        	
            owsConfig.setUserCreds(parentJobConfig.getLogin(),
                                   parentJobConfig.getPassword());

        } catch (ConfigurationsException e) {
            this.logger.error(
                "An error occurred while the OWS configuration was generated.", 
                e);
            owsConfig = null;
        }

        return owsConfig;
    }



    /**
     * Generates owsWatch-compatible query parameters.
     * 
     * @return  a properties object containing the owsWatch-compatible 
     *          parameters for this query
     */
    public Properties getOwsParams() {
        final Properties queryParams = new Properties();
        final ServiceType serviceType 
            = this.getParentJob().getConfig().getServiceType();
        String serviceTypeName = serviceType.getName();
        
        if(serviceType.getName().equals("ALL") && !"".equals(this.queryServiceType))
        {
        	serviceTypeName = this.queryServiceType;
        }
        
        String serviceTypeVersion = serviceType.getVersion();
        // ALL type
        if(serviceTypeVersion.equals("0"))
        {
        	QueryParam param = this.findParamCaseInsensitivity("version");
        	if(param != null)
        	{
        		serviceTypeVersion = param.getValue();
        	}
        }
        
        final String serviceMethodName = this.getMethod().getName();
        
        String httpMethod = this.getParentJob().getConfig().getHttpMethod().getName();
        if(serviceType.getName().equals("ALL") &&  !"".equals(this.queryMethod))
        {
        	httpMethod = this.queryMethod;
        }
        queryParams.put("SERVICE", serviceTypeName);
        queryParams.put("VERSION", serviceTypeVersion);
        queryParams.put("REQUEST", serviceMethodName);

        if ("GET".equals(httpMethod)) {
            final Iterator<QueryParam> paramsIterator 
                = this.getParams().iterator();

            while (paramsIterator.hasNext()) {
                final QueryParam currentParam = paramsIterator.next();
                queryParams.put(currentParam.getName(), 
                                currentParam.getValue());
            }
            
        } else if ("POST".equals(httpMethod)) {
            final QueryParam xmlRequestParam = this.findParam("XMLREQUEST");
            final StringBuilder xmlRequest 
                = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            xmlRequest.append(String.format(
                    "<%1$s xmlns=\"http://www.opengis.net/ows\" " 
                    + "service=\"%2$s\" version=\"%3$s\"> ", 
                    serviceMethodName, serviceTypeName, 
                    serviceTypeVersion));
            
            if (null != xmlRequestParam) {
                xmlRequest.append(xmlRequestParam.getValue());
            }else
            {
            	final QueryParam cswParam = this.findParam("cswparam");
            	if(cswParam != null)
            	{
            		final StringBuilder xmlRequest2 = new StringBuilder();
            		xmlRequest2.append(cswParam.getValue());
            		queryParams.put("XMLREQUEST", xmlRequest2.toString().replace("\"", "'"));
            		return queryParams;
            	}
            }	
            
            xmlRequest.append(String.format("</%1$s>", serviceMethodName));
            queryParams.put("XMLREQUEST", xmlRequest.toString());
        }
        return queryParams;
    }

    /**
     * Generates custom query parameters.Basically we return all params
     * 
     * @return  a properties object containing the owsWatch-compatible 
     *          parameters for this query
     */
    public Properties getCustomParams() {
        final Properties queryParams = new Properties();
        final ServiceType serviceType 
            = this.getParentJob().getConfig().getServiceType();
        final String serviceTypeName = serviceType.getName();
        final String serviceTypeVersion = serviceType.getVersion();
        final String serviceMethodName = this.getMethod().getName();
        final String soapUrl = this.getQuerySoapUrl();
        
        queryParams.put("SERVICE", serviceTypeName);
        queryParams.put("VERSION", serviceTypeVersion);
        queryParams.put("REQUEST", serviceMethodName);
        queryParams.put(Constants.XML_REQUEST, ""); // this has to be added otherwise serviceconfiguration will throw exception
        queryParams.put(CustomQueryConstants.SOAPURL, soapUrl);

        final Iterator<QueryParam> paramsIterator = this.getParams().iterator();

        while (paramsIterator.hasNext()) {
        	final QueryParam currentParam = paramsIterator.next();
        	queryParams.put(currentParam.getName(), 
        			currentParam.getValue());
        }



        return queryParams;
    }


    /**
     * Adds a parameter for the query.
     * 
     * @param   queryParam  the parameter to add
     * @see     QueryParam
     */
    public void addParam(QueryParam queryParam) {

        if (null == queryParam
            || queryParam.getParentQueryId() != this.getQuery().getQueryId()) {
            
            throw new IllegalArgumentException(
                   "Query and query parameter don't match");
        }

        if (null == this.findParam(queryParam.getName())) {
            this.getParams().add(queryParam);
        }

    }



    /**
     * Removes a parameter from those set for this query.
     * 
     * @param   paramName                       the name of the parameter to 
     *                                          remove
     * @throws  QueryParameterNotFoundException if no parameter with this 
     *                                          name is defined for this query
     * @see     QueryParam
     */
    public void removeParam(String paramName) 
        throws QueryParameterNotFoundException {
        
        final QueryParam paramToRemove = this.findParam(paramName);

        if (null == paramToRemove) {
            throw new QueryParameterNotFoundException(paramName, 
                   this.getQuery().getQueryId());
        }

        this.getParams().remove(paramToRemove);
    }



    /**
     * Sets one of the parameter's value.
     * 
     * @param   paramName                       the name of the parameter to 
     *                                          alter
     * @param   paramValue                      a string containing the new 
     *                                          value
     * @throws  QueryParameterNotFoundException if no parameter with this name 
     *                                          is defined for this query
     * @see     QueryParam
     */
    public void setParam(String paramName, String paramValue) 
        throws QueryParameterNotFoundException {
        
        final QueryParam param = this.findParam(paramName);

        if (null == param) {
            throw new QueryParameterNotFoundException(paramName, 
                    this.getQuery().getQueryId());
        }

        param.setValue(paramValue);
    }



    /**
     * Finds a parameter among those defined for this query.
     * 
     * @param   paramName    the searched parameter's name
     * @return               the query parameter if it's been found or<br>
     *                       <code>null</code> otherwise
     */
    public QueryParam findParam(String paramName) {
        final Set<QueryParam> paramsSet = this.getParams();

        if (null != paramsSet) {
            final Iterator<QueryParam> paramIterator = paramsSet.iterator();

            while (paramIterator.hasNext()) {
                final QueryParam currentParam = paramIterator.next();

                if (currentParam.getName().equals(paramName)) {
                    return currentParam;
                }
            }
        }

        return null;
    }
    
    /**
     * Finds a parameter among those defined for this query with case insensitivity.
     * 
     * @param   paramName    the searched parameter's name
     * @return               the query parameter if it's been found or<br>
     *                       <code>null</code> otherwise
     */
    public QueryParam findParamCaseInsensitivity(String paramName) {
        final Set<QueryParam> paramsSet = this.getParams();

        if (null != paramsSet) {
            final Iterator<QueryParam> paramIterator = paramsSet.iterator();

            while (paramIterator.hasNext()) {
                final QueryParam currentParam = paramIterator.next();

                if (currentParam.getName().toLowerCase().equals(paramName.toLowerCase())) {
                    return currentParam;
                }
            }
        }

        return null;
    }
    
    /**
     * Defines the web service's URL.
     * 
     * @param   newUrl  the web service's URL
     */
    public void setUrl(String newUrl) {
        this.url = newUrl;
    }



    /**
     * Gets the web service's URL.
     * 
     * @return  the web service's URL
     */
    public String getUrl() {
        return this.url;
    }
    
    /**
     * Defines the web service's login.
     * 
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }



    /**
     * Gets the web service's login.
     * 
     * @return  the web service's login
     */
    public String getLogin() {
        return this.login;
    }
    
    /**
     * Defines the web service's login.
     * 
     * @param login
     */
    public void setPassword(String password) {
        this.password = password;
    }



    /**
     * Gets the web service's password.
     * 
     * @return  the web service's password
     */
    public String getPassword() {
        return this.password;
    }
}
