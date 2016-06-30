package org.easysdi.monitor.biz.logging;

import java.util.Calendar;
import org.easysdi.monitor.dat.dao.LastLogQueryDaoHelper;

public class LastLog {
	
    private long     queryId;
    private String   status;
    private Calendar updateTime;
    
    /**
     * No-argument constructor, meant to be used by the persistance mechanism. 
     */
    @SuppressWarnings("unused")
    private LastLog() {

    }
    
    public LastLog(long queryId, String status, Calendar updateTime){
    	this.queryId = queryId;
    	this.status = status;
    	this.updateTime = updateTime;
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
     * Defines the status produced by this query execution.
     * 
     * @param   newStatusValue  the value of the produced status
     */
    public void setStatus(String newStatusValue) {

        this.status = newStatusValue;
    }
    
    /**
     * Gets the status that was produced by this query execution.
     * 
     * @return  the produced status
     */
    public String getStatus() {
        return this.status;
    }
    

    /**
     * Defines when the query was executed.
     * 
     * @param   newRequestTime  the date and time when the execution took place
     */
    public void setUpdateTime(Calendar newRequestTime) {
        this.updateTime = newRequestTime;
    }

    /**
     * Gets when the query was executed.
     * 
     * @return  the date and time when the execution took place
     */
    public Calendar getUpdateTime() {
        return this.updateTime;
    }
    
    public static LastLog getLastLogQuery(long queryId) {
		final LastLog lastlog = LastLogQueryDaoHelper.getLastLogQueryDao().getLastlogByQueryId(queryId);
		return lastlog;
	}
    
}
