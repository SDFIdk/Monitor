package org.easysdi.monitor.biz.logging;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.dat.dao.ILogDao.ParentType;
import org.easysdi.monitor.dat.dao.LogDaoHelper;

/**
 * Gives access to the logs of a query.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class QueryLogFetcher implements ILogFetcher {

    private final Logger logger = Logger.getLogger(QueryLogFetcher.class);
    
    private Query parentQuery;
    


    /**
     * Creates a new query log fetcher.
     * 
     * @param   logsParentQuery the query whose logs are to be accessed
     */
    public QueryLogFetcher(Query logsParentQuery) {
        this.setParentQuery(logsParentQuery);
    }



    /**
     * Defines the query whose logs are accessed by this fetcher.
     * 
     * @param   logsParentQuery the query
     */
    private void setParentQuery(Query logsParentQuery) {
        this.parentQuery = logsParentQuery;
    }



    /**
     * Gets the query whose logs are accessed by this fetcher.
     * 
     * @return  the query
     */
    public Query getParentQuery() {
        return this.parentQuery;
    }



    /**
     * {@inheritDoc}
     */
    public Map<Date, AbstractAggregateLogEntry> fetchAggregLogs() {
        return this.fetchAggregLogsSubset(null, null, null, null);
    }



    /**
     * {@inheritDoc}
     */
    public Map<Date, AbstractAggregateLogEntry> fetchAggregLogsSubset(
                    Calendar minDate, Calendar maxDate, Integer maxResults,
                    Integer startIndex) {

        try {

            return LogDaoHelper.getLogDao().fetchAggregLogs(
                   ParentType.QUERY, this.getParentQuery().getQueryId(),
                   minDate, maxDate, maxResults, startIndex);

        } catch (LogFetcherException e) {
            this.logger.error(
                     "An error occurred while the logs were fetched.", e);
            return null;

        }

    }



    /**
     * {@inheritDoc}
     */
    public Set<RawLogEntry> fetchRawLogs() {
        return this.fetchRawLogsSubset(null, null, null, null);
    }



    /**
     * {@inheritDoc}
     */
    public Set<RawLogEntry> fetchRawLogsSubset(Calendar minDate,
            Calendar maxDate, Integer maxResults, Integer startIndex) {

        try {
            return LogDaoHelper.getLogDao().fetchRawLogs(
                    new Long[] {this.getParentQuery().getQueryId()}, minDate, 
                    maxDate, maxResults, startIndex);
            
        } catch (LogFetcherException e) {
            this.logger.error(
                     "An error occurred while the logs number was calculated", 
                     e); 
            return null;
        }

    }



    /**
     * {@inheritDoc}
     */
    public RawLogEntry fetchLastLogBeforeDate(Calendar aDate) {

        return LogDaoHelper.getLogDao().fetchLastLogBeforeDate(
                new Long[] {this.getParentQuery().getQueryId()}, aDate);
    }


    
    /**
     * {@inheritDoc}
     */
    public long getAggregateLogsItemsNumber(Calendar minDate, Calendar maxDate,
                    Integer maxResults, Integer startIndex) {

        return LogDaoHelper.getLogDao().getAggregateLogsItemsNumber(
                ParentType.QUERY, this.getParentQuery().getQueryId(), minDate, 
                maxDate, maxResults, startIndex);
    }



    /**
     * {@inheritDoc}
     */
    public long getRawLogsItemsNumber(Calendar minDate, Calendar maxDate,
                    Integer maxResults, Integer startIndex) {

        return LogDaoHelper.getLogDao().getRawLogsItemsNumber(
                new Long[] {this.getParentQuery().getQueryId()}, minDate, 
                maxDate, maxResults, startIndex);
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<Date, AbstractAggregateHourLogEntry> fetchAggregHourLogs()
    {
    	return this.fetchAggregHourLogsSubset(null,null,null,null);
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<Date, AbstractAggregateHourLogEntry> fetchAggregHourLogsSubset(Calendar minDate,
            Calendar maxDate, Integer maxResults, Integer startIndex)
    {
    	   try {

               return LogDaoHelper.getLogDao().fetchAggregHourLogs(
                      ParentType.QUERY, this.getParentQuery().getQueryId(),
                      minDate, maxDate, maxResults, startIndex);

           } catch (LogFetcherException e) {
               this.logger.error(
                        "An error occurred while the logs were fetched.", e);
               return null;

           }
    }
    
    /**
     * {@inheritDoc}
     */
    public long getAggregateHourLogsItemsNumber(Calendar minDate, Calendar maxDate,
                    Integer maxResults, Integer startIndex) {

        return LogDaoHelper.getLogDao().getAggregateHourLogsItemsNumber(
                ParentType.QUERY, this.getParentQuery().getQueryId(), minDate, 
                maxDate, maxResults, startIndex);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Calendar> fetchMaxMinDateLog(){
        return LogDaoHelper.getLogDao().getRawlogMaxMinDate(
    		new Long[] {this.getParentQuery().getQueryId()});
    }
    
}
