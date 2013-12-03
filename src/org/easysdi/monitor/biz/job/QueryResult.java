package org.easysdi.monitor.biz.job;

import java.util.Calendar;
import java.util.Date;

import org.deegree.portal.owswatch.ValidatorResponse;
import org.easysdi.monitor.biz.job.Status.StatusValue;
import org.easysdi.monitor.biz.logging.Constants;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.biz.util.HttpCodeUtils;
import org.easysdi.monitor.dat.dao.QueryDaoHelper;

/**
 * Holds the result of a query polling.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.1, 2010-04-30
 */
public class QueryResult {

    private static final int MILLIS_PER_SECOND = 1000;

    private Integer         httpCode;
    private String          httpMethod;   
    private Long            logEntryId;
    private String          message;
    private Query           parentQuery;
    private long            queryId;
    private String          queryName;
    private Calendar        requestTime;
    private float           responseDelay;
    private String          serviceExceptionCode;
    private Status          status 
        = Status.getStatusObject(StatusValue.NOT_TESTED);
    private String          testedUrl;
    private long           size;


    /**
     * No-argument constructor, used by the persistance mechanism.
     */
    @SuppressWarnings("unused")
    private QueryResult() {

    }



    /**
     * Creates a new query result.
     * 
     * @param   newQueryId          the long identifying the query that was 
     *                              polled
     * @param   newQueryName        the name of the polled query
     * @param   owsResponse         the owsWatch-produced response object
     * @param   testedQueryUrl      the full tested url
     * @param   testedHttpMethod    the HTTP method used for testing
     */
    public QueryResult(long newQueryId, String newQueryName, 
                       ValidatorResponse owsResponse, String testedQueryUrl, 
                       String testedHttpMethod) {
        this.setLogEntryId(null);
        this.setQueryId(newQueryId);
        this.setQueryName(newQueryName);
        this.setRequestTime(owsResponse.getLastTest());
        this.setResponseDelay(owsResponse.getLastLapse() 
                              / (float) QueryResult.MILLIS_PER_SECOND);
        this.setTestedUrl(testedQueryUrl);
        this.setHttpCode(owsResponse.getHttpStatusCode());
        this.setHttpMethod(testedHttpMethod);
        this.setStatusAndMessageFromOws(owsResponse);
        this.setServiceExceptionCode(owsResponse.getServiceExceptionCode());
        if(owsResponse.getData() != null)
        {
        	this.setSize(owsResponse.getData().length);
        }
        else
        {
        	this.setSize(owsResponse.getResponseLength());        
        }
    }



    /**
     * Creates a log entry for this result.
     * 
     * @return the created log entry
     */
    public RawLogEntry createRawLogEntry() {

        return new RawLogEntry(this.getQueryId(), this.getRequestTime(),
                               this.getResponseDelay(), this.getStatusValue(),
                               this.getMessage(), this.getHttpCode(), 
                               this.getServiceExceptionCode(), this.getSize());
    }



    /**
     * Defines the HTTP status code produced by the query execution.
     * 
     * @param   newHttpCode the HTTP status code produced by the query execution
     */
    private void setHttpCode(Integer newHttpCode) {
        
        if (0 != newHttpCode && !HttpCodeUtils.isCodeValid(newHttpCode)) {
            throw new IllegalArgumentException("Invalid HTTP status code.");
        }
        
        this.httpCode = newHttpCode;
    }



    /**
     * Gets the HTTP status code produced by the query execution.
     * 
     * @return  the HTTP status code produced by the query execution
     */
    public Integer getHttpCode() {
        return this.httpCode;
    }



    /**
     * Defines the HTTP method used to test the query.
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
     * Gets the HTTP method used to test the query.
     * 
     * @return  the HTTP method name
     */
    public String getHttpMethod() {
        return this.httpMethod;
    }



    /**
     * Gets the identifier for the log entry that reflects this result.
     * 
     * @return  the log entry's identifier
     */
    public Long getLogEntryId() {
        return this.logEntryId;
    }



    /**
     * Gets the message produced by the polling.
     * 
     * @return  the result message
     */
    public String getMessage() {
        return this.message;
    }



    /**
     * Gets the polled query's identifier.
     * 
     * @return the query's identifier
     */
    public long getQueryId() {
        return this.queryId;
    }



    /**
     * Gets when the query was polled.
     * 
     * @return the time of the request
     */
    public Calendar getRequestTime() {
        return this.requestTime;
    }



    /**
     * Gets how long the web service took to answer.
     * 
     * @return the response delay
     */
    public float getResponseDelay() {
        return this.responseDelay;
    }



    /**
     * Gets the status produced by this result.
     * 
     * @return the status object
     */
    public Status getStatus() {
        return this.status;
    }



    /**
     * Gets the status value produced by this result.
     * 
     * @return the status value object
     */
    public StatusValue getStatusValue() {
        return this.status.getStatusValue();
    }



    /**
     * Get the full tested URL.
     * 
     * @return the tested URL, with parameter if it was a GET
     */
    public String getTestedUrl() {
        return this.testedUrl;
    }



    /**
     * Sets the message explaining the result.
     * 
     * @param   newMessage  the result message
     */
    public void setMessage(String newMessage) {
        this.message = newMessage;
    }



    /**
     * Determines the Monitor-compatible status and message from the owsWatch 
     * response.
     * 
     * @param   owsResponse the response object returned by owsWatch
     */
    public void setStatusAndMessageFromOws(ValidatorResponse owsResponse) {
        final JobConfiguration parentJobConfig 
            = this.getParentQuery().getConfig().getParentJob().getConfig();
        
        final boolean isHttpError 
            = !HttpCodeUtils.isSuccess(owsResponse.getHttpStatusCode());
        final boolean isBizError 
            = this.isOwsStatusBizError(owsResponse.getStatus());
        
        if (isHttpError && parentJobConfig.isHttpErrorChecked()) {
            this.setMessage(HttpCodeUtils.getCodeMessage(this.getHttpCode()));
            this.setStatus(StatusValue.UNAVAILABLE);
            
        } else if (isBizError && parentJobConfig.isBizErrorChecked()) {
            this.setMessage(owsResponse.getMessage());
            this.setStatus(StatusValue.UNAVAILABLE);

        } else {
            final int timeoutInMillis = parentJobConfig.getTimeout() 
                                      * QueryResult.MILLIS_PER_SECOND;

            if (owsResponse.getLastLapse() > timeoutInMillis) {
                this.setMessage(Constants.LOG_MESSAGE_TIMEOUT);
                this.setStatus(StatusValue.OUT_OF_ORDER);
            } else {
                this.setMessage(Constants.LOG_MESSAGE_NO_ERROR);
                this.setStatus(StatusValue.AVAILABLE);
            }
        }
    }
    
    
    
    /**
     * Determines if the status of the owsWatch query was a business error.
     * 
     * @param   owsStatus   the status produced by the owsWatch query
     * @return              <code>true</code> if the query produced a business
     *                      error 
     */
    private boolean isOwsStatusBizError(
        org.deegree.portal.owswatch.Status owsStatus) {
        
        switch (owsStatus) {
            case RESULT_STATE_BAD_RESPONSE:
            case RESULT_STATE_INVALID_XML:
            case RESULT_STATE_UNEXPECTED_CONTENT:
            case RESULT_STATE_ERROR_UNKNOWN:
            case RESULT_STATE_NOT_IMPLEMENTED:
            case RESULT_STATE_SERVICE_UNAVAILABLE:
                return true;

            default:
                return false;
        }
    }



    /**
     * Defines the query that this result applies to from its identifier.
     * 
     * @param   polledQueryId  the query's identifier
     */
    public void setQueryId(long polledQueryId) {

        if (1 > polledQueryId) {
            throw new IllegalArgumentException("Invalid query identifier");
        }

        this.queryId = polledQueryId;
    }



    /**
     * Defines the name of the polled query.
     * 
     * @param   polledQueryName the name of the polled query
     */
    public void setQueryName(String polledQueryName) {
        this.queryName = polledQueryName;
    }



    /**
     * Gets the name of the polled query.
     * 
     * @return  the name of the polled query
     */
    public String getQueryName() {
        return this.queryName;
    }



    /**
     * Defines when the polling occurred.
     * 
     * @param   newRequestTime  the time of the request
     */
    public void setRequestTime(Calendar newRequestTime) {
        this.requestTime = newRequestTime;
    }



    /**
     * Defines when the polling occurred.
     * 
     * @param   newRequestTime  the requestTime to set
     */
    public void setRequestTime(Date newRequestTime) {

        if (null == this.getRequestTime()) {
            this.setRequestTime(Calendar.getInstance());
        }

        this.requestTime.setTime(newRequestTime);
    }



    /**
     * Defines how long the query took to answer.
     * 
     * @param   newResponseDelay    the delay in seconds
     */
    public void setResponseDelay(float newResponseDelay) {
        this.responseDelay = newResponseDelay;
    }



    /**
     * Defines the code identifying the service exception, if any occurred.
     * 
     * @param newServiceExceptionCode   the service exception code
     */
    public void setServiceExceptionCode(String newServiceExceptionCode) {
        this.serviceExceptionCode = newServiceExceptionCode;
    }



    /**
     * Gets the code identifying the service exception, if any occurred.
     * 
     * @return  the code if a service exception occcurred, or<br>
     *          <code>null</code> otherwise
     */
    public String getServiceExceptionCode() {
        return this.serviceExceptionCode;
    }



    /**
     * Defines the status produced by the query execution.
     * 
     * @param   newStatusValue  the status value produced by the query
     */
    public void setStatus(Status.StatusValue newStatusValue) {
        
        final Status newStatus = Status.getStatusObject(newStatusValue);

        if (null == newStatus) {
            throw new IllegalArgumentException(String.format(
                     "Unknown status '%1$s'.", newStatusValue.name()));
        }

        this.status = newStatus;
    }



    /**
     * Defines the status produced by the query execution.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes only and
     * shouldn't be called directly.</i>
     * 
     * @param   newStatus   the status produced by the query
     * @see     #setStatus(StatusValue)
     */
    @SuppressWarnings("unused")
    private void setStatus(Status newStatus) {
        this.status = newStatus;
    }


    /**
     * Gets the query that produced this result.
     * 
     * @return  the query whose execution produced this result
     */
    public Query getParentQuery() {

        if (null == this.parentQuery) {
            
            if (1 > this.getQueryId()) {
                throw new IllegalStateException(
                    "Parent query identifier is invalid.");
            }

            this.setParentQuery(
                    QueryDaoHelper.getQueryDao().getQuery(this.getQueryId()));
        }

        return this.parentQuery;
    }



    /**
     * Defines the corresponding log entry's identifier.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes only and 
     * shouldn't be called directly. Identifiers definition is best left to
     * the persistance mechanism.</i>
     * 
     * @param   newLogEntryId   the long identifying the log entry that matches
     *                          this result
     */
    private void setLogEntryId(Long newLogEntryId) {

        if (null != newLogEntryId && 1 > newLogEntryId) {
            throw new IllegalArgumentException("Invalid log entry identifier");
        }

        this.logEntryId = newLogEntryId;
    }



    /**
     * Defines the query that produced this result.
     * 
     * @param   query   the query whose execution produced this result
     */
    private void setParentQuery(Query query) {

        if (null == query) {
            throw new IllegalArgumentException("Parent query can't be null.");
        }

        if (query.getQueryId() == this.getQueryId()) {
            this.parentQuery = query;
        }
    }



    /**
     * Defines the URL that was tested to produce this result.
     * 
     * @param   newTestedUrl    the tested URL
     */
    private void setTestedUrl(String newTestedUrl) {
        this.testedUrl = newTestedUrl;
    }
    
    /**
     * Defines the size of the result.
     * 
     * @param size
     */
	public void setSize(long size) {
		this.size = size;
	}
	
	/**
	 * Gets the size of the result
	 *  
	 * @return
	 */
	public long getSize() {
		return size;
	}
}
