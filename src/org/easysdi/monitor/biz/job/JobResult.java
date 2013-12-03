package org.easysdi.monitor.biz.job;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.easysdi.monitor.biz.job.Status.StatusValue;

/**
 * Represents the result of a job execution.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2009-03-19
 */
public class JobResult {

    private final Logger    logger = Logger.getLogger(JobResult.class);
    
    private String                 httpMethod;
    private String                 jobName;
    private Status                 status;
    private final Set<QueryResult> queryResults = new HashSet<QueryResult>();
    private ServiceType            serviceType;
    private String                 serviceUrl;
    private String                 statusCause;
    private float                  responseDelay;
    private Integer                httpCode;
    
    public float getResponseDelay() {
		return responseDelay;
	}

	public void setResponseDelay(float responseDelay) {
		this.responseDelay = responseDelay;
	}

	
	public Integer getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(Integer httpCode) {
		this.httpCode = httpCode;
	}

    /**
     * No-argument constructor, used by the persistance mechanism.
     */
    @SuppressWarnings("unused")
    private JobResult() {

    }



    /**
     * Creates an empty job result.
     * 
     * @param   parentJob   the parent job. It must be valid.
     * @see     Job#isValid(boolean)
     */
    public JobResult(Job parentJob) {

        if (null == parentJob || !parentJob.isValid(false)) {
            throw new IllegalArgumentException(
                   "Parent job must be a valid one.");
        }

        final JobConfiguration parentJobConfig = parentJob.getConfig();

        this.setJobName(parentJobConfig.getJobName());
        this.setServiceType(parentJobConfig.getServiceType());
        this.setServiceUrl(parentJobConfig.getUrl());
        this.setHttpMethod(parentJobConfig.getHttpMethod().getName());
    }



    /**
     * Creates a job result.
     * 
     * @param   parentJob       the parent job. It must be valid.
     * @param   newQueryResults a collection containing the result of the 
     *                          executed queries
     * @see     Job#isValid(boolean)
     */
    public JobResult(Job parentJob, Collection<QueryResult> newQueryResults) {
        this(parentJob);
        Collections.addAll(this.queryResults,
                           newQueryResults.toArray(
                                    new QueryResult[newQueryResults.size()]));
    }



    /**
     * Adds a query result to this job result.
     * 
     * @param   queryResult the query result to add
     */
    public void addQueryResult(QueryResult queryResult) {

        if (!this.queryResults.contains(queryResult)) {
            this.queryResults.add(queryResult);
            this.updateStatus();
        }
    }



    /**
     * Gets the individual query results.
     * 
     * @return  a set containing the result of each executed query
     */
    public Set<QueryResult> getQueryResults() {

        return Collections.unmodifiableSet(this.queryResults);

    }



    /**
     * Defines the HTTP method used to test the job.
     * 
     * @param   testedHttpMethod    the HTTP method name
     */
    private void setHttpMethod(String testedHttpMethod) {
        
        if (null == HttpMethod.getObject(testedHttpMethod)) {
            throw new IllegalArgumentException("Unknown HTTP method : " 
                                               + testedHttpMethod);
        }
        
        this.httpMethod = testedHttpMethod;
    }



    /**
     * Gets the HTTP method used to test the job.
     * 
     * @return  the HTTP method name
     */
    public String getHttpMethod() {
        return this.httpMethod;
    }



    /**
     * Sets the parent job's name.
     * <p>
     * <i><b>Note:</b> This property is intended for internal use and shouldn't
     * be called directly. It doesn't alter the job's name itself.</i>
     * 
     * @param   parentJobName   the parent's job name
     */
    private void setJobName(String parentJobName) {
        this.jobName = parentJobName;
    }



    /**
     * Gets the parent job's name.
     * 
     * @return  the parent job's name
     */
    public String getJobName() {
        return this.jobName;
    }



    /**
     * Defines a status according to the query results.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use and shouldn't be
     * called directly. It doesn't update the job status itself.</i>
     * 
     * @param   statusValue the status produced by the query results
     */
    private void setStatus(StatusValue statusValue) {
        this.status = Status.getStatusObject(statusValue);
    }



    /**
     * Gets the status defined from the query results.
     * 
     * @return  the status produced by the query results
     */
    public Status getStatus() {
        return this.status;
    }



    /**
     * Gets the status value defined from the query results.
     * 
     * @return the status value produced by the query results
     */
    public StatusValue getStatusValue() {
        final Status currentStatus = this.getStatus();
        
        return ((currentStatus != null) 
               ? currentStatus.getStatusValue() 
               : null);
    }



    /**
     * Defines the tested web service type.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use and shouldn't be
     * called directly. It doesn't update the job's service type itself.</i>
     * 
     * @param   jobServiceType the tested web service type
     */
    private void setServiceType(ServiceType jobServiceType) {
        this.serviceType = jobServiceType;
    }



    /**
     * Gets the tested web service type.
     * 
     * @return  the tested web service type
     */
    public ServiceType getServiceType() {
        return this.serviceType;
    }



    /**
     * Defines the tested web service's URL
     * <p>
     * <i><b>Note:</b> This method is intended for internal use and shouldn't be
     * called directly. It doesn't update the job's service URL itself.</i>
     * 
     * @param   jobServiceUrl  the tested web service's URL
     */
    private void setServiceUrl(String jobServiceUrl) {
        this.serviceUrl = jobServiceUrl;
    }



    /**
     * Gets the tested web service's URL.
     * <p>
     * The full URL of each tested query can be found in the corresponding query
     * result.
     * 
     * @return  the tested web service's URL
     * @see     QueryResult#getTestedUrl()
     */
    public String getServiceUrl() {
        return this.serviceUrl;
    }



    /**
     * Updates the status and its cause according to the query results.
     */
    private void updateStatus() {
        StatusValue currentStatus = StatusValue.NOT_TESTED;
        String cause = "Not tested yet";
        float delay = 0f;
        int code = 0;

        for (QueryResult queryResult : this.getQueryResults()) {

            switch (queryResult.getStatusValue()) {

                case AVAILABLE:

                    if (StatusValue.NOT_TESTED == currentStatus) {
                        currentStatus = StatusValue.AVAILABLE;
                        cause = queryResult.getMessage();
                        delay = queryResult.getResponseDelay();
                        code = queryResult.getHttpCode();
                    }
                    break;

                case OUT_OF_ORDER:

                    if (Status.isStatusValueOK(currentStatus)) {
                        currentStatus = StatusValue.OUT_OF_ORDER;
                        cause = queryResult.getMessage();
                        delay = queryResult.getResponseDelay();
                        code = queryResult.getHttpCode();
                    }
                    break;

                case UNAVAILABLE:
                    this.setStatus(StatusValue.UNAVAILABLE);
                    this.setStatusCause(queryResult.getMessage());
                    this.setHttpCode(queryResult.getHttpCode());
                    this.setResponseDelay(queryResult.getResponseDelay());
                    return;
                    
                default:
                    this.logger.warn(String.format(
                                      "Invalid status value type: %1$s.",
                                      queryResult.getStatusValue().name()));
                    break;

            }
        }
        
        this.setStatus(currentStatus);
        this.setStatusCause(cause);
        this.setHttpCode(code);
        this.setResponseDelay(delay);
    }



    /**
     * Defines the cause for this job result's status.
     * 
     * @param   newStatusCause a message stating the reason for this result's 
     *                      status
     */
    private void setStatusCause(String newStatusCause) {
        this.statusCause = newStatusCause;
    }



    /**
     * Gets the cause for this job result's status.
     * 
     * @return  a message stating the reason for this result's status
     */
    public String getStatusCause() {
        return this.statusCause;
    }



    /**
     * Defines the status for a query-less job execution.
     */
    public void defineAsNoQuery() {
        this.setStatus(null);
        this.setStatusCause("No query");
    }
}
