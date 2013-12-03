package org.easysdi.monitor.dat.dao.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deegree.framework.util.DateUtil;
import org.easysdi.monitor.biz.logging.AbstractAggregateHourLogEntry;
import org.easysdi.monitor.biz.logging.AbstractAggregateLogEntry;
import org.easysdi.monitor.biz.logging.JobAggregateHourLogEntry;
import org.easysdi.monitor.biz.logging.JobAggregateLogEntry;
import org.easysdi.monitor.biz.logging.QueryAggregateHourLogEntry;
import org.easysdi.monitor.biz.logging.QueryAggregateLogEntry;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.dat.dao.ILogDao;
import org.easysdi.monitor.dat.dao.LogDaoHelper;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;



/**
 * Executes log-related persistance operations through Hibernate.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class LogDao extends AbstractDao implements ILogDao {

    /**
     * Creates a new log data access object. 
     * 
     * @param   sessionFactory  the Hibernate session factory
     */
    public LogDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        LogDaoHelper.setLogDao(this);
    }


    /**
     * {@inheritDoc}
     */
    public boolean deleteAggregLog(AbstractAggregateLogEntry aggregateLog) {

        return this.deleteObject(aggregateLog);

    }
    
    /**
     * {@inheritDoc}
     * Deletes hour log
     */
    public boolean deleteAggregHourLog(AbstractAggregateHourLogEntry aggregateLog) {

        return this.deleteObject(aggregateLog);

    }


    /**
     * {@inheritDoc}
     */
    public boolean deleteRawLog(RawLogEntry rawLog) {

        return this.deleteObject(rawLog);
    
    }


    /**
     * {@inheritDoc}
     */
    public Map<Date, AbstractAggregateLogEntry> fetchAggregLogs(ParentType type,
                    long parentId, Calendar minDate, Calendar maxDate, 
                    Integer maxResults, Integer startIndex) {
        final DetachedCriteria criteria 
            = this.buildAggregateLogSearchCriteria(type, parentId, minDate, 
                                                   maxDate);

        final List<?> queryResult 
            = this.fetchSearchResult(criteria, maxResults, startIndex);

        return this.createAggregLogMap(type, queryResult);
    }
    
    
    /**
     * Builds a set of criteria for searching aggregate log items.
     * 
     * @param   parentType      the type of the aggregate logs parent 
     * @param   parentId        the identifier of the aggregate logs parent
     * @param   minDate         the date from which the aggregate logs are 
     *                          fetched
     * @param   maxDate         the date up to which the aggregate logs are 
     *                          fetched
     * @return                  a criteria object
     */
    private DetachedCriteria buildAggregateLogSearchCriteria(
                    ParentType parentType, long parentId, Calendar minDate, 
                    Calendar maxDate) {

        DetachedCriteria search;

        switch (parentType) {

            case JOB:
                search = DetachedCriteria.forClass(JobAggregateLogEntry.class);
                search.add(Restrictions.eq("jobId", parentId));
                break;

            case QUERY:
                search 
                    = DetachedCriteria.forClass(QueryAggregateLogEntry.class);
                search.add(Restrictions.eq("queryId", parentId));
                break;

            default:
                throw new IllegalArgumentException(String.format(
                        "Unknown parent type '%1$s'", parentType.toString()));
        }
        
        return this.addDateCriteria(search, minDate, maxDate, "logDate");
    }



    /**
     * Transforms a list of results into an aggregate logs map.
     * 
     * @param   parentType  the type of parent for the log (job, query)
     * @param   queryResult the list containing the aggregate log entries
     * @return              a map of aggregate log results
     */
    private Map<Date, AbstractAggregateLogEntry> createAggregLogMap(
            ParentType parentType, List<?> queryResult) {
        
        final Map<Date, AbstractAggregateLogEntry> fetchedLogs 
            = new LinkedHashMap<Date, AbstractAggregateLogEntry>();

        for (Object resultObject : queryResult) {
            AbstractAggregateLogEntry logEntry = null;

            switch (parentType) {

                case JOB:

                    if (resultObject instanceof JobAggregateLogEntry) {
                        logEntry = (JobAggregateLogEntry) resultObject;
                    }

                    break;

                case QUERY:

                    if (resultObject instanceof QueryAggregateLogEntry) {
                        logEntry = (QueryAggregateLogEntry) resultObject;
                    }

                    break;

                default:
                    throw new UnsupportedOperationException(
                            "Invalid log parent type.");
            }

            if (null != logEntry) {
                fetchedLogs.put(
                        DateUtil.truncateTime(logEntry.getLogDate()).getTime(),
                        logEntry);
            }
        }

        queryResult.clear();

        return fetchedLogs;
    }



    /**
     * Builds a set of criteria for searching raw logs.
     * <p>
     * Each parameter can be <code>null</code> if it must be ignored.
     * 
     * @param   queryIds        an array containing the identifiers of the 
     *                          queries whose raw logs should be retrieved
     * @param   minDate         the date from which the raw logs are fetched
     * @param   maxDate         the date up to ehich the raw logs are fetched
     * @param   datePropName    the name of the property that contains the 
     *                          date of the entry
     * @return                  a list of objects found
     */
    private DetachedCriteria buildRawLogSearchCriteria(Long[] queryIds,
                    Calendar minDate, Calendar maxDate, String datePropName) {

        final DetachedCriteria criteria 
            = DetachedCriteria.forClass(RawLogEntry.class);
        criteria.add(Restrictions.in("queryId", queryIds));

        return this.addDateCriteria(criteria, minDate, maxDate, 
                                        datePropName);
    }
       
    
    
    /**
     * Adds date elements to a given set of search criteria.
     * 
     * @param originalCriteria  the criteria object to which the date criteria
     *                          must be added
     * @param   minDate         the date from which the raw logs are fetched
     * @param   maxDate         the date up to ehich the raw logs are fetched
     * @param   datePropName    the name of the property that contains the 
     *                          date of the entry
     * @return                  the modified set of criteria
     */
    private DetachedCriteria addDateCriteria(DetachedCriteria originalCriteria,
                    Calendar minDate, Calendar maxDate, String datePropName) {

        originalCriteria.addOrder(Order.desc(datePropName));

        if (null != minDate) {
            originalCriteria.add(Restrictions.ge(datePropName, minDate));
        }

        if (null != maxDate) {
            originalCriteria.add(Restrictions.le(datePropName, maxDate));
        }
        
        return originalCriteria;
    }
    
    
    
    /**
     * Retrieves the log items matching a given set of criteria.
     * 
     * @param   criteria        the search criteria 
     * @param   maxResults      the maximum number of results to return
     * @param   startIndex      the index of the first result to return
     * @return                  a list containing the log items matching
     *                          the criteria. It is not typed.
     */
    private List<?> fetchSearchResult(DetachedCriteria criteria, 
                                     Integer maxResults, Integer startIndex) {
        
        final int maxResultsNumber = this.processInteger(maxResults, 0, -1);
        final int startFrom = this.processInteger(startIndex, 1, 0);
        
        return this.getHibernateTemplate().findByCriteria(criteria, startFrom,
                                                          maxResultsNumber);
    }
    
    

    //FIXME Déplacer cette fonction dans une classe utilitaire
    /**
     * Ensures that an integer has a correct value, non-null value.
     * <p>
     * In case of a <code>null</code> or a value below the minimum, the default
     * value is returned. Other values are returned as is.
     * 
     * @param   input           the Integer to process
     * @param   minimumValue    the lowest non-default accepted value
     * @param   defaultValue    the default value
     * @return                  an int with a correct value
     */
    private int processInteger(Integer input, int minimumValue, 
                               int defaultValue) {

        if (null == input || minimumValue > input) {
            return defaultValue;
        }
        
        return input.intValue();
    }
    
    
    
    /**
     * Returns the number of items matching a given set of criteria.
     * 
     * @param   criteria      the search criteria
     * @param   maxResults    the maximum number of results to return
     * @param   startIndex    the index of the first result to return
     * @return                the number of log entries matching the 
     *                        criteria
     */
    private long getItemsNumber(DetachedCriteria criteria, 
                                Integer maxResults, Integer startIndex) {

        final int maxResultsNumber = this.processInteger(maxResults, 0, -1);
        final int startFrom = this.processInteger(startIndex, 1, 0);
        
        criteria.setProjection(Projections.rowCount());
        
        return ((Integer) this.getHibernateTemplate().findByCriteria(
                 criteria, startFrom, maxResultsNumber).get(0)).longValue();

    }



    /**
     * {@inheritDoc}
     */
    public Set<RawLogEntry> fetchRawLogs(Long[] queryIds, Calendar minDate,
                                         Calendar maxDate, Integer maxResults, 
                                         Integer startIndex) {

        if (null == queryIds || 1 > queryIds.length) {
            return new HashSet<RawLogEntry>();
        }

        final DetachedCriteria search 
            = this.buildRawLogSearchCriteria(queryIds, minDate, maxDate, 
                                             "requestTime");
        
        final List<?> queryResult 
            = this.fetchSearchResult(search, maxResults, startIndex);

        return this.createRawLogSet(queryResult);
    }


    /**
     * Creates a set of raw log entries from a search result.
     * 
     * @param   queryResult the result of a search returning raw logs
     * @return              a set containing the found raw log entries
     */
    private Set<RawLogEntry> createRawLogSet(List<?> queryResult) {
        final Set<RawLogEntry> fetchedLogs = new LinkedHashSet<RawLogEntry>();

        for (Object resultObject : queryResult) {

            if (resultObject instanceof RawLogEntry) {
                fetchedLogs.add((RawLogEntry) resultObject);
            }
        }

        return fetchedLogs;
    }


    /**
     * {@inheritDoc}
     */
    public boolean persistAggregLog(AbstractAggregateLogEntry aggregateLog) {
    	return this.persistObject(aggregateLog);
        
    }

    
    /**
     * {@inheritDoc}
     */
    public boolean persistRawLog(RawLogEntry rawLog) {

        return this.persistObject(rawLog);

    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public RawLogEntry fetchLastLogBeforeDate(Long[] queryIds, Calendar date) {
        final DetachedCriteria search 
            = DetachedCriteria.forClass(RawLogEntry.class);
        search.add(Restrictions.in("queryId", queryIds));
        search.add(Restrictions.lt("requestTime", date));
        search.addOrder(Order.desc("requestTime"));

        final List<RawLogEntry> result 
            = this.getHibernateTemplate().findByCriteria(search, 0, 1);

        if (null != result && 0 < result.size()) {
            return result.get(0);
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    public long getAggregateLogsItemsNumber(
                    ParentType parentType, long parentId, Calendar minDate,
                    Calendar maxDate, Integer maxResults, Integer startIndex) {

        final DetachedCriteria criteria 
            = this.buildAggregateLogSearchCriteria(parentType, parentId, 
                                                   minDate, maxDate);
        
        return this.getItemsNumber(criteria, maxResults, startIndex);
    }


    /**
     * {@inheritDoc}
     */
    public long getRawLogsItemsNumber(Long[] queryIds, Calendar minDate,
                    Calendar maxDate, Integer maxResults, Integer startIndex) {
        
        if (null == queryIds || 1 > queryIds.length) {
            return 0;
        }

        final DetachedCriteria criteria 
            = this.buildRawLogSearchCriteria(queryIds, minDate, maxDate, 
                                             "requestTime");
    
        return this.getItemsNumber(criteria, maxResults, startIndex);
    }
    
    /**
 
    * {@inheritDoc}
     */
    public boolean persistAggregHourLog(AbstractAggregateHourLogEntry aggregateLog) {
        if (null == aggregateLog) {
            throw new IllegalArgumentException(
                    "Null object can't be persisted.");
        }
            
    	return this.persistObject(aggregateLog);
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<Date,AbstractAggregateHourLogEntry> fetchAggregHourLogs(ParentType type,
            long parentId, Calendar minDate, Calendar maxDate, Integer maxResults, Integer startIndex) {
    	
    	final DetachedCriteria criteria = this.buildAggregateHourLogSearchCriteria(type, parentId, minDate, maxDate);

    	final List<?> queryResult = this.fetchSearchResult(criteria, maxResults, startIndex);
    	return this.createAggregHourLogMap(type, queryResult);
    }
    
    /**
     * Builds a set of criteria for searching aggregate hour log items.
     * 
     * @param   parentType      the type of the aggregate logs parent 
     * @param   parentId        the identifier of the aggregate logs parent
     * @param   minDate         the date from which the aggregate logs are 
     *                          fetched
     * @param   maxDate         the date up to which the aggregate logs are 
     *                          fetched
     * @return                  a criteria object
     */
    private DetachedCriteria buildAggregateHourLogSearchCriteria(
                    ParentType parentType, long parentId, Calendar minDate, 
                    Calendar maxDate) {

        DetachedCriteria search;

        switch (parentType) {

            case JOB:
                search = DetachedCriteria.forClass(JobAggregateHourLogEntry.class);
                search.add(Restrictions.eq("jobId", parentId));
                break;

            case QUERY:
                search 
                    = DetachedCriteria.forClass(QueryAggregateHourLogEntry.class);
                search.add(Restrictions.eq("queryId", parentId));
                break;

            default:
                throw new IllegalArgumentException(String.format(
                        "Unknown parent type '%1$s'", parentType.toString()));
        }
        
        return this.addDateCriteria(search, minDate, maxDate, "logDate");
    }
    
    /**
     * Transforms a list of results into an aggregate hour logs map.
     * 
     * @param   parentType  the type of parent for the log (job, query)
     * @param   queryResult the list containing the aggregate log entries
     * @return              a map of aggregate hour log results
     */
    private Map<Date, AbstractAggregateHourLogEntry> createAggregHourLogMap(
            ParentType parentType, List<?> queryResult) {
        
        final Map<Date, AbstractAggregateHourLogEntry> fetchedLogs 
            = new LinkedHashMap<Date, AbstractAggregateHourLogEntry>();

        for (Object resultObject : queryResult) {
            AbstractAggregateHourLogEntry logEntry = null;

            switch (parentType) {

                case JOB:

                    if (resultObject instanceof JobAggregateHourLogEntry) {
                        logEntry = (JobAggregateHourLogEntry) resultObject;
                    }

                    break;

                case QUERY:

                    if (resultObject instanceof QueryAggregateHourLogEntry) {
                        logEntry = (QueryAggregateHourLogEntry) resultObject;
                    }

                    break;

                default:
                    throw new UnsupportedOperationException(
                            "Invalid log parent type.");
            }
            // TODO fix Don't trunk cate
            if (null != logEntry) {
               /* fetchedLogs.put(
                        DateUtil.truncateTime(logEntry.getLogDate()).getTime(),
                        logEntry);*/
            	fetchedLogs.put(
                        logEntry.getLogDate().getTime(),
                        logEntry);
            }
        }
        queryResult.clear();
        return fetchedLogs;
    }
    
    /**
     * {@inheritDoc}
     */
    public long getAggregateHourLogsItemsNumber(
                    ParentType parentType, long parentId, Calendar minDate,
                    Calendar maxDate, Integer maxResults, Integer startIndex) {

        final DetachedCriteria criteria 
            = this.buildAggregateHourLogSearchCriteria(parentType, parentId, 
                                                   minDate, maxDate);
        
        return this.getItemsNumber(criteria, maxResults, startIndex);
    } 
    
    public List<Calendar> getRawlogMaxMinDate(Long[] queryIds)
    {
    	List<Calendar> dates =  new ArrayList<Calendar>();
    	if(queryIds == null || queryIds.length < 1)
    	{
    		return dates;
    	}
		final DetachedCriteria criteria = DetachedCriteria.forClass(RawLogEntry.class);
		criteria.add(Restrictions.in("queryId", queryIds));
		criteria.addOrder(Order.desc("requestTime"));
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.max("requestTime"));
		proList.add(Projections.min("requestTime"));
		criteria.setProjection(proList);
		final List<?> result = this.getHibernateTemplate().findByCriteria(criteria);
		 
    	 Iterator<?> itr = result.iterator();
    	 if (!itr.hasNext()) {
    		 return dates;
    	 }
    	 while (itr.hasNext()) {         
			 Object[] row = (Object[]) itr.next();          
			 for (int i = 0; i < row.length; i++)
			 {
				 if(i == 0)
				 {
					 Calendar maxDate = (Calendar) row[0];
					 dates.add(maxDate);
				 }
				 if(i == 1)
				 {
					 Calendar minDate = (Calendar) row[1];
					 dates.add(minDate);
				 }
			 }
    	 }
    	 return dates;
    }
}
