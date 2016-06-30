package org.easysdi.monitor.biz.logging;

import java.util.Calendar;
import java.util.Date;

import org.deegree.framework.util.DateUtil;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.job.Status;
import org.easysdi.monitor.biz.job.Status.StatusValue;
import org.easysdi.monitor.biz.util.HttpCodeUtils;
import org.easysdi.monitor.dat.dao.IQueryDao;
import org.easysdi.monitor.dat.dao.LogDaoHelper;
import org.easysdi.monitor.dat.dao.QueryDaoHelper;

/**
 * An entry logging the result of a query execution.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.1, 2010-04-30
 *
 */
public class RawLogEntry implements Comparable<RawLogEntry> {

    private Integer  httpCode;
    private Long     logEntryId;
    private String   message;
    private Query    parentQuery;
    private long     queryId;
    private float    responseDelay;
    private Calendar requestTime;
    private String   serviceExceptionCode;
    private Status   status;
    private long    responseSize;
    private float maxTime;
    private int avCount = 0;
    private int fCount = 0;
    private int unavCount = 0;
    private int otherCount = 0;
    

    
    
    /**
     * No-argument constructor, meant to be used by the persistance mechanism. 
     */
    @SuppressWarnings("unused")
    private RawLogEntry() {
		
    }



    /**
     * Creates a new raw log entry.
     * 
     * @param newQueryId                the identifier of the query that was 
     *                                  executed
     * @param newRequestTime            when the request was started
     * @param newRequestDelay           the time (in seconds) it took to execute
     *                                  the query
     * @param newStatus                 the status produced by this query 
     *                                  execution
     * @param newMessage                the message produced by this query 
     *                                  execution
     * @param newHttpCode               the HTTP status code produced by the 
     *                                  query
     * @param newExceptionCode          the code identifying the service 
     *                                  exception, if any occurred
     */
    public RawLogEntry(long newQueryId, Calendar newRequestTime, 
                       float newRequestDelay, StatusValue newStatus, 
                       String newMessage, Integer newHttpCode, 
                       String newExceptionCode, long size) {

        this.setLogEntryId(null);
        this.setQueryId(newQueryId);
        this.setRequestTime(newRequestTime);
        this.setResponseDelay(newRequestDelay);
        this.setStatus(newStatus);
        this.setMessage(newMessage);
        this.setHttpCode(newHttpCode);
        this.setServiceExceptionCode(newExceptionCode);
        this.setResponseSize(size);
    }



    /**
     * Creates a new raw log entry.
     * 
     * @param newQueryId                the identifier of the query that was 
     *                                  executed
     * @param newRequestTime            the date when the request was started
     * @param newRequestDelay           the time (in seconds) it took to execute
     *                                  the query
     * @param newStatus                 the status produced by this query 
     *                                  execution
     * @param newMessage                the message produced by this query 
     *                                  execution
     * @param newHttpCode               the HTTP status code produced by the 
     *                                  query
     * @param newExceptionCode          the code identifying the service 
     *                                  exception, if any occurred
     */
    public RawLogEntry(long newQueryId, Date newRequestTime, 
                       float newRequestDelay, StatusValue newStatus, 
                       String newMessage, Integer newHttpCode, 
                       String newExceptionCode, long size) {
    
        this(newQueryId, DateUtil.dateToCalendar(newRequestTime), 
             newRequestDelay, newStatus, newMessage, newHttpCode, 
             newExceptionCode,size);
        
    }



    /**
     * Defines the HTTP status code for the query execution.
     * 
     * @param   newHttpCode the HTTP status code produced by the query execution
     */
    private void setHttpCode(Integer newHttpCode) {
/*        final boolean inCorrectSpan 
            = RawLogEntry.HTTP_LOWEST_VALID_CODE > newHttpCode 
                || RawLogEntry.HTTP_HIGHEST_VALID_CODE < newHttpCode; 
        
        if ((null != newHttpCode && !inCorrectSpan)) {
            this.httpCode = null;
        }*/
        
        this.httpCode = newHttpCode;
    }



    /**
     * Gets the HTTP status code for the query execution.
     * 
     * @return the HTTP status code produced by the query execution
     */
    public Integer getHttpCode() {
        return this.httpCode;
    }



    /**
     * Defines this entry's identifier.
     * <p>
     * <i><b>Note:</b> This method shouldn't be called directly. Defining the
     * identifier is best left to the persistance mechanism.</i>
     * 
     * @param newLogEntryId    the long uniquely identifying this entry
     */
    private void setLogEntryId(Long newLogEntryId) {

        if (null != newLogEntryId && 1 > newLogEntryId) {
            throw new IllegalArgumentException("Invalid log entry identifier");
        }

        this.logEntryId = newLogEntryId;
    }



    /**
     * Gets this entry's identifier.
     * 
     * @return  the long uniquely identifying this entry
     */
    public Long getLogEntryId() {
        return this.logEntryId;
    }



    /**
     * Defines the message produced by this query execution.
     * 
     * @param   newMessage  the message
     */
    public void setMessage(String newMessage) {
        this.message = newMessage;
    }



    /**
     * Gets the message produced by this query execution.
     * 
     * @return  the message
     */
    public String getMessage() {
        return this.message;
    }



    /**
     * Defines which query produced this entry, by its identifier.
     * 
     * @param   newQueryId  the identifier of the query
     */
    public void setQueryId(long newQueryId) {

        if (1 > newQueryId) {
            throw new IllegalArgumentException("Invalid query identifier");
        }

        this.queryId = newQueryId;
    }



    /**
     * Gets the identifier of the query that produced this entry.
     * 
     * @return  the long identifying the query
     */
    public long getQueryId() {
        return this.queryId;
    }



    /**
     * Defines which query produced this entry.
     * 
     * @param   newParentQuery  the parent query
     */
    private void setParentQuery(Query newParentQuery) {

        if (null == newParentQuery) {
            throw new IllegalArgumentException("Parent query can't be null.");
        }

        if (newParentQuery.getQueryId() == this.getQueryId()) {
            this.parentQuery = newParentQuery;
        }
    }



    /**
     * Gets the query that produced this entry.
     * 
     * @return  the parent query
     */
    @SuppressWarnings("unused")
    private Query getParentQuery() {

        if (null == this.parentQuery) {
            
            try {
                final IQueryDao daoObject = QueryDaoHelper.getQueryDao(); 
                this.setParentQuery(daoObject.getQuery(this.getQueryId()));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        return this.parentQuery;
    }



    /**
     * Defines the response delay for this query execution.
     * 
     * @param   newResponseDelay    the time (in seconds) it took to execute the
     *                              query
     */
    public void setResponseDelay(float newResponseDelay) {
        this.responseDelay = newResponseDelay;
    }



    /**
     * Gets the response delay for this query execution.
     * 
     * @return  the time (in seconds) it took to execute the query
     */
    public float getResponseDelay() {
        return this.responseDelay;
    }



    /**
     * Defines when the query was executed.
     * 
     * @param   newRequestTime  the date and time when the execution took place
     */
    public void setRequestTime(Calendar newRequestTime) {
        this.requestTime = newRequestTime;
    }



    /**
     * Defines when the query was executed.
     * 
     * @param   newRequestTime  the date and time when the execution took place
     */
    public void setRequestTime(Date newRequestTime) {

        if (null == this.getRequestTime()) {
            this.setRequestTime(Calendar.getInstance());
        }

        this.requestTime.setTime(newRequestTime);
    }



    /**
     * Gets when the query was executed.
     * 
     * @return  the date and time when the execution took place
     */
    public Calendar getRequestTime() {
        return this.requestTime;
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
     * Defines the status produced by this query execution.
     * 
     * @param   newStatusValue  the value of the produced status
     */
    public void setStatus(Status.StatusValue newStatusValue) {

        if (null == newStatusValue) {
            throw new IllegalArgumentException("Status can't be null");
        }

        final Status newStatus = Status.getStatusObject(newStatusValue);

        if (null == newStatus) {
            throw new IllegalArgumentException(String.format(
                 "Unknown format '%1$s'.", newStatusValue.name()));
        }

        this.status = newStatus;
    }



    /**
     * Defines the status produced by this query execution.
     * 
     * @param   newStatus   the produced status
     */
    @SuppressWarnings("unused")
    private void setStatus(Status newStatus) {

        if (null == newStatus) {
            throw new IllegalArgumentException("Status can't be null");
        }

        this.status = newStatus;
    }



    /**
     * Gets the status that was produced by this query execution.
     * 
     * @return  the produced status
     */
    public Status getStatus() {
        return this.status;
    }



    /**
     * Gets the value of the status that was produced by this query execution.
     * 
     * @return  the produced status value
     */
    public StatusValue getStatusValue() {
        return ((this.status != null) ? this.status.getStatusValue() : null);
    }
    
    /**
     * Gets the maxTime for a date
     * This method is only used when crate a summary report
	 * @return the maxTime
	 */
	public float getMaxTime() {
		return maxTime;
	}
	
	
	
	/**
	 * Sets the maxTime
	 * This method is only used when crate a summary report 
	 * @param maxTime the maxTime to set
	 */
	public void setMaxTime(float maxTime) {
		this.maxTime = maxTime;
	}
	
	

	/**
	 * Gets the AVAILABLE count
	 * @return the avCount
	 */
	public int getAvCount() {
		return avCount;
	}



	/**
	 * Sets the AVAILABLE count
	 * @param avCount the avCount to set
	 */
	public void setAvCount(int avCount) {
		this.avCount = avCount;
	}



	/**
	 * Gets the OUT_OF_ORDER count
	 * @return the fCount
	 */
	public int getfCount() {
		return fCount;
	}



	/**
	 * Set the OUT_OF_ORDER count 
	 * @param fCount the fCount to set
	 */
	public void setfCount(int fCount) {
		this.fCount = fCount;
	}



	/**
	 * Gets UNAVAILABLE count
	 * @return the unavCount
	 */
	public int getUnavCount() {
		return unavCount;
	}



	/**
	 * Sets the UNAVAILABLE count
	 * @param unavCount the unavCount to set
	 */
	public void setUnavCount(int unavCount) {
		this.unavCount = unavCount;
	}



	/**
	 * Gets the NOT_TESTED count
	 * @return the otherCount
	 */
	public int getOtherCount() {
		return otherCount;
	}



	/**
	 * Sets the NOT_TESTED count
	 * @param otherCount the otherCount to set
	 */
	public void setOtherCount(int otherCount) {
		this.otherCount = otherCount;
	}



	/**
     * Checks if this query execution resulted in a business error.
     * 
     * @return  <code>true</code> if this log entry is a business error
     */
    public boolean isBusinessError() {

        return (!this.isConnectError()) 
                && !Constants.LOG_MESSAGE_NO_ERROR.equals(this.getMessage());

    }



    /**
     * Checks if this query resulted in a connection error.
     * 
     * @return  <code>true</code> if this log entry is a connection error
     */
    public boolean isConnectError() {
        final int code = this.getHttpCode();
        
        return !HttpCodeUtils.isCodeValid(code) 
                || !HttpCodeUtils.isSuccess(code);

    }



    /**
     * Erases this log entry through the persistance mechanism.
     */
    public void delete() {
        LogDaoHelper.getLogDao().deleteRawLog(this);
    }
    
    /**
     * 
     * @param responseSize
     */
	public void setResponseSize(long responseSize) {
		this.responseSize = responseSize;
	}

	/**
	 * 
	 * @return
	 */
	public long getResponseSize() {
		return responseSize;
	}
	
	/**
	 * CompareTo RawLogEntry after responseDelay with
	 * the shortes time first
	 */
	public int compareTo(RawLogEntry obj) {
		int result;
		if(this.getResponseDelay() > obj.getResponseDelay())
		{
			result = 1;
		}else if(this.getResponseDelay() == obj.getResponseDelay())
		{
			result = 0;
			
		}else
		{
			result = -1;
		}
		return result;
	}
	
}
