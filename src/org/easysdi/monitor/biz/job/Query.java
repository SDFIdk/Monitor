package org.easysdi.monitor.biz.job;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.deegree.framework.util.StringTools;
import org.deegree.portal.owswatch.ServiceConfiguration;
import org.deegree.portal.owswatch.ServiceInvoker;
import org.easysdi.monitor.biz.job.Status.StatusValue;
import org.easysdi.monitor.biz.logging.MonitorServiceLog;
import org.easysdi.monitor.dat.dao.JobDaoHelper;
import org.easysdi.monitor.dat.dao.QueryDaoHelper;

/**
 * Represents a web service method to be tested.
 * <p>
 * A query is bound to a job (representing the web service itself) and specifies
 * which method should be tested and with which parameters. The timeout, test
 * interval, HTTP method and other such configuration is set for the job and
 * applies to all its queries.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * @see Job
 * @see QueryParam
 */
public class Query {
    
    private final Logger logger = Logger.getLogger(Query.class);

    private QueryConfiguration   config;
    private ServiceInvoker       owsInvoker;
    private ServiceConfiguration owsConfig;
    private MonitorServiceLog    owsLogger;
    private long                 queryId;
    private Status               status;
    private OverviewLastQueryResult last_query_result;
    private QueryValidationSettings queryValidationSettings;
    private QueryValidationResult queryValidationResult;

    /**
	 * @return the last_query_result
	 */
	public OverviewLastQueryResult getLast_query_result() {
		last_query_result.setQueryid(getQueryId());
		return last_query_result;
	}

	/**
	 * @param lastQueryResult the last_query_result to set
	 */
	public void setLast_query_result(OverviewLastQueryResult lastQueryResult) {
		this.last_query_result = lastQueryResult;
	}

    /**
     * No-argument constructor, used by the persistance mechanism.
     */
    private Query() {

        try {
            this.setOwsLogger(new MonitorServiceLog());
        } catch (IOException e) {
            this.logger.error("Unable to set the Monitor service logger.", e);
            this.setOwsLogger(null);
        }
    }



    /**
     * Creates a new query.
     * 
     * @param   parentJob   the job that this query is attached to
     * @param   queryName   the name of the query. (This must be unique among 
     *                      all queries for a given job.)
     * @param   methodName  the name of the method to be tested
     */
    public Query(Job parentJob, String queryName, String methodName) {
        this();
        this.setStatus(StatusValue.NOT_TESTED);
        final QueryConfiguration newConfig 
            = new QueryConfiguration(parentJob, this, queryName, methodName);
        this.setConfig(newConfig);
    }

    


    /**
     * Creates a new query.
     * 
     * @param   parentJob   the job that this query is attached to
     * @param   queryName   the name of the query. (This must be unique among 
     *                      all queries for a given job.)
     * @param   method      the service method object
     */
    public Query(Job parentJob, String queryName, ServiceMethod method) {
        this();
        this.setStatus(StatusValue.NOT_TESTED);
        final QueryConfiguration newConfig 
            = new QueryConfiguration(parentJob, this, queryName, method);
        this.setConfig(newConfig);
    }


    /**
     * Creates a new query.
     * 
     * @param   parentJob   the job that this query is attached to
     * @param   queryName   the name of the query. (This must be unique among 
     *                      all queries for a given job.)
     * @param   method      the service method object
     */
    public Query(Job parentJob, String queryName, ServiceMethod method, String soapUrl,
    		String queryMethod,String queryServiceType,String query_url,String login,String password) {
        this();
        this.setStatus(StatusValue.NOT_TESTED);
        final QueryConfiguration newConfig 
        = new QueryConfiguration(parentJob, this, queryName, method, soapUrl,queryMethod,
            		queryServiceType,query_url,login,password);
        this.setConfig(newConfig);
    }


    /**
     * Defines this query's configuration object.
     * 
     * @param   newConfig   the query configuration object
     */
    private void setConfig(QueryConfiguration newConfig) {

        this.config = newConfig;

    }



    /**
     * Gets this query's configuration object.
     * 
     * @return the config this query's configuration
     */
    public QueryConfiguration getConfig() {

        return this.config;

    }



    /**
     * Defines this query's owsWatch-compatible configuration object.
     * 
     * @param   newOwsConfig    the owsWatch service configuration object 
     *                          corresponding to this query
     * @see     ServiceConfiguration
     */
    private void setOwsConfig(ServiceConfiguration newOwsConfig) {
        this.owsConfig = newOwsConfig;
    }



    /**
     * Gets this query's owsWatch-compatible configuration object.
     * 
     * @return  the owsWatch service configuration object
     * @see     ServiceConfiguration
     */
    private ServiceConfiguration getOwsConfig() {
        this.setOwsConfig(this.getConfig().toOwsConfig());

        return this.owsConfig;
    }

    /**
     * Gets this query's  configuration object. This will be used by normal get/post and soap requests
     * 
     * @return  the  service configuration object
     * @see     ServiceConfiguration
     */
    private ServiceConfiguration getCustom_SOAP_GET_POST_Config() {
        this.setOwsConfig(this.getConfig().toCustomConfig());

        return this.owsConfig;
    }
    
    /**
     * Gets this query's ftp configuration object.
     * 
     * @return  the  service configuration object
     * @see     ServiceConfiguration
     */
    private ServiceConfiguration getCustom_FTP_Config() {
        this.setOwsConfig(this.getConfig().toCustomFTPConfig());
        return this.owsConfig;
    }
    

    /**
     * Defines the owsWatch service invoker for this query.
     * 
     * @param   newOwsInvoker   the owsWatch service invoker
     * @see     ServiceInvoker
     */
    private void setOwsInvoker(ServiceInvoker newOwsInvoker) {
        this.owsInvoker = newOwsInvoker;
    }



    /**
     * Gets the owsWatch service invoker for this query.
     * 
     * @return  the owsWatch service invoker
     * @see     ServiceInvoker
     */
    private ServiceInvoker getOwsInvoker() {
        return this.owsInvoker;
    }



    /**
     * Defines the owsWatch-compatible result logger.
     * 
     * @param   newOwsLogger    the owsWatch-compatible result logger
     * @see     MonitorServiceLog
     * @see     QueryResult
     */
    private void setOwsLogger(MonitorServiceLog newOwsLogger) {
        this.owsLogger = newOwsLogger;
    }



    /**
     * Gets the owsWatch-compatible result logger.
     * 
     * @return  the owsWatch-compatible result logger
     * @see     MonitorServiceLog
     * @see     QueryResult
     */
    private MonitorServiceLog getOwsLogger() {
        return this.owsLogger;
    }



    /**
     * Defines this query's identifier.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use and shouldn't be
     * called directly. The identifier is usually assigned through the
     * persistance mechanism.</i>
     * 
     * @param   newQueryId  a long uniquely identifying the query
     */
    @SuppressWarnings("unused")
    private void setQueryId(long newQueryId) {

        if (1 > newQueryId) {
            throw new IllegalArgumentException("Invalid query identifier.");
        }

        this.queryId = newQueryId;
    }



    /**
     * Gets this query's identifier.
     * 
     * @return  the long uniquely identifying the query
     */
    public long getQueryId() {
        return this.queryId;
    }



    /**
     * Defines the current status of this query.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use and shouldn't be
     * called directly. The query status is usually defined when this query is
     * executed.</i>
     * 
     * @param   newStatusValue  the status value for this query
     */
    private void setStatus(Status.StatusValue newStatusValue) {

        if (null == newStatusValue) {
            throw new IllegalArgumentException("Status can't be null");
        }

        final Status newStatus = Status.getStatusObject(newStatusValue);

        if (null == newStatus) {
            throw new IllegalArgumentException(String.format(
                    "Unknown status '%1$s'.", newStatusValue.name()));
        }

        this.status = newStatus;
    }



    /**
     * Defines the current status of this query.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use and shouldn't be
     * called directly. The query status is usually defined when this query is
     * executed.</i>
     * 
     * @param   newStatus   the status for this query
     */
    @SuppressWarnings("unused")
    private void setStatus(Status newStatus) {

        if (null == newStatus) {
            throw new IllegalArgumentException("Status can't be null.");
        }

        this.status = newStatus;
    }



    /**
     * Gets this query's current status.
     * 
     * @return this query's current status
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * Gets this query's current status value.
     * <p>
     * This is the status that resulted from the last automatic execution.
     * 
     * @return  this query's status value
     */
    public StatusValue getStatusValue() {
        return ((this.status != null) ? this.status.getStatusValue() : null);
    }

    /**
     * Checks this query's validity.
     * <p>
     * A query is valid if:
     * <ol>
     * <li>its status isn't null</li>
     * <li>a valid configuration object is set</li>
     * </ol>
     * 
     * @return  <code>true</code> if the query is valid
     * @see     QueryConfiguration#isValid()
     */
    public boolean isValid() {
        final QueryConfiguration queryConfig = this.getConfig();
        final boolean isStatusValid = (null != this.status);
        final boolean isConfigValid 
            = (null != queryConfig && queryConfig.isValid());
        return (isStatusValid && isConfigValid);
    }



    /**
     * Polls this query.
     * 
     * @param   resultLogging   <code>true</code> if the result of this polling 
     *                          should be kept in the logs
     * @return                  the result of this query's polling
     */
    public QueryResult execute(boolean resultLogging, boolean testresult) {
        final MonitorServiceLog thisOwsLogger = this.getOwsLogger();
        thisOwsLogger.setResultLogged(resultLogging);
        thisOwsLogger.setSaveTestResult(testresult);
        
        final ServiceType serviceType = this.getConfig().getParentJob().getConfig().getServiceType();
         
        final String serviceTypeName = serviceType.getName();
        final String serviceMethodName = this.getConfig().getMethod().getName();
        if((serviceTypeName.equals(CustomQueryConstants.ALL)) && 
        		(serviceMethodName.equals(CustomQueryConstants.SOAP_1_1)||
        		serviceMethodName.equals(CustomQueryConstants.SOAP_1_2) ||
        		serviceMethodName.equals(CustomQueryConstants.HTTP_POST)||
        		serviceMethodName.equals(CustomQueryConstants.HTTP_GET) ||
        		serviceMethodName.equals(CustomQueryConstants.FTP)
        		)){
        	// execute non ows queries.
        	if(serviceMethodName.equals(CustomQueryConstants.FTP))
        	{
        		this.setOwsInvoker(new CustomFtpInvoker(this.getCustom_FTP_Config(), thisOwsLogger));
        	}else
        	{
        		this.setOwsInvoker(new CustomQueryInvoker(this.getCustom_SOAP_GET_POST_Config(), thisOwsLogger));
        	}
        }        	
        else{       
        	this.setOwsInvoker(new ServiceInvoker(this.getOwsConfig(), thisOwsLogger));
        }
        this.getOwsInvoker().executeTest();
        	
        final QueryResult result = thisOwsLogger.getLastResult();
        this.setStatus(result.getStatusValue());
        return result;
    }
    
    /**
     * 
     * @param queries
     * @return
     */
    public static List<QueryResult> executeSimultaneous(Map<Long, Query> queries, boolean resultLogging)
    {
    	List<QueryResult> result = new ArrayList<QueryResult>(); 
    	for (Query query : queries.values()) {
    		MonitorServiceLog thisOwsLogger = query.getOwsLogger();
    		thisOwsLogger.setResultLogged(resultLogging);
    		 final ServiceType serviceType = query.getConfig().getParentJob().getConfig().getServiceType();
    	     final String serviceTypeName = serviceType.getName();
    	     final String serviceMethodName = query.getConfig().getMethod().getName();
    		 if((serviceTypeName.equalsIgnoreCase(CustomQueryConstants.ALL)) && 
    	        		(serviceMethodName.equalsIgnoreCase(CustomQueryConstants.SOAP_1_1)||
    	        		serviceMethodName.equalsIgnoreCase(CustomQueryConstants.SOAP_1_2)||
    	        		serviceMethodName.equalsIgnoreCase(CustomQueryConstants.HTTP_POST)||
    	        		serviceMethodName.equalsIgnoreCase(CustomQueryConstants.HTTP_GET) ||
        				serviceMethodName.equalsIgnoreCase(CustomQueryConstants.FTP)
    	        		))
    		 {
    			// execute non ows queries. ONLY DO SIM FOR SAME TYPE
    			if(serviceMethodName.equalsIgnoreCase(CustomQueryConstants.FTP))
    			{
    				query.setOwsInvoker(new CustomFtpInvoker(query.getCustom_FTP_Config(), thisOwsLogger));
    				query.getOwsInvoker().setSimultaneousRun(true);
    			}else
    			{
    				query.setOwsInvoker(new CustomQueryInvoker(query.getCustom_SOAP_GET_POST_Config(),thisOwsLogger));
    				query.getOwsInvoker().setSimultaneousRun(true);
    				query.getOwsInvoker().setupHTTPClient();
    			}
    		 }else
    		 {
    			 query.setOwsInvoker(new ServiceInvoker(query.getOwsConfig(), thisOwsLogger));
    			 query.getOwsInvoker().setSimultaneousRun(true);
    			 query.getOwsInvoker().setupHTTPClient();
    		 }
    	}
    	
    	// Start all query with with the same start time
    	long startTime = System.currentTimeMillis();
    	for(Query query : queries.values())
    	{
    		query.getOwsInvoker().setStartTimeSync(startTime);
    		query.getOwsInvoker().start();
    		
    	}
   	
    	for(Query query : queries.values())
    	{
    		// Wait for thread to die
    		int i = 0;
    		while(query.getOwsInvoker().isAlive() && i < 60)
    		{
    			try {
    					i++;
    					Thread.sleep(1000);
    			    } catch (InterruptedException iex) 
    			    {	    	
    			    }
    		}
    		QueryResult lastResult = query.owsLogger.getLastResult();
    		result.add(lastResult);
    		query.setStatus(lastResult.getStatusValue());
    	}
    	return result;
    }



    /**
     * Saves this query.
     * 
     * @return <code>true</code> if this query's has been successfully saved
     */
    public boolean persist() {
        return QueryDaoHelper.getQueryDao().persistQuery(this);
    }


    /**
     * Retrieves a query from identifying strings.
     * 
     * @param   jobIdString     a string containing either the parent job's 
     *                          identifier or its name
     * @param   queryIdString   a string containing either the sought query's 
     *                          identifier or its name
     * @return                  the query if it has been found or<br>
     *                          <code>null</code> otherwise
     */
    public static Query getFromIdStrings(String jobIdString,
                                         String queryIdString) {

        if (StringTools.isNullOrEmpty(jobIdString)) {
            throw new IllegalArgumentException(
                    "Job identifier string can't be null or empty");
        }

        if (StringTools.isNullOrEmpty(queryIdString)) {
            throw new IllegalArgumentException(
                    "Query identifier string can't be null or empty");
        }

        final Job parentJob 
            = JobDaoHelper.getJobDao().getJobFromIdString(jobIdString);

        if (null == parentJob) {
            return null;
        }

        return parentJob.getQueryFromIdString(queryIdString);
    }


    /**
     * Erases this query from the database.
     * 
     * @return  <code>true</code> if this query has been successfully deleted
     */
    public boolean delete() {

        if (QueryDaoHelper.getQueryDao().deleteQuery(this)) {
            final Job job = this.getConfig().getParentJob();
            job.removeQuery(this);
            final JobConfiguration jobConfig = job.getConfig();

            if (jobConfig.isAutomatic() && 0 < jobConfig.getTestInterval()) {
                job.updateScheduleState();
            }
            return true;
        }

        return false;
    }
    
    /**
     * 
     * @param queryValidationSettings
     */
	public void setQueryValidationSettings(QueryValidationSettings queryValidationSettings) {
		this.queryValidationSettings = queryValidationSettings;
	}


	/**
	 * 
	 * @return
	 */
	public QueryValidationSettings getQueryValidationSettings() {
		return queryValidationSettings;
	}


	/**
	 * 
	 * @param queryValidationResult
	 */
	public void setQueryValidationResult(QueryValidationResult queryValidationResult) {
		this.queryValidationResult = queryValidationResult;
	}

	/**
	 * 
	 * @return
	 */
	public QueryValidationResult getQueryValidationResult() {
		return queryValidationResult;
	}
}
