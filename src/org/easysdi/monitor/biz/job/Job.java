package org.easysdi.monitor.biz.job;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.biz.alert.AbstractAction;
import org.easysdi.monitor.biz.alert.Alert;
import org.easysdi.monitor.biz.job.Status.StatusValue;
import org.easysdi.monitor.biz.job.auto.IJobScheduler;
import org.easysdi.monitor.dat.dao.AlertDaoHelper;
import org.easysdi.monitor.dat.dao.IJobDao;
import org.easysdi.monitor.dat.dao.JobDaoHelper;
import org.easysdi.monitor.gui.webapp.AppContext;
import org.springframework.context.ApplicationContext;

/**
 * An OGC web service to be monitored
 * <p>
 * This is the application's core object. A job wraps a web service. To be
 * tested it must have queries (representing web service methods) defined. If no
 * query is defined, the job remains passive, since no method has to be tested.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2009-03-19
 * @see Query
 */
public class Job {

    private Set<AbstractAction>     actions;
    private JobConfiguration        config;
    private long                    jobId;
    private Map<Long, Query>        queries;
    private IJobScheduler           schedulerHelper;
    private Status                  status;
    private Calendar                statusUpdateTime;

	/**
     * No-argument constructor.
     */
    private Job() {

    }



    /**
     * Creates a job with default parameters
     * <p>
     * <i><b>Note:</b> The job returned by this function only has the minimal
     * parameters defined. To be valid, you must then set its URL, the HTTP
     * method to use and the type of OGC web service.</i>
     * 
     * @param name
     *            The job's name, which must be unique
     * @return A job with default parameters (see note above)
     */
    public static Job createDefault(String name) {
        final Job newJob = new Job();

        newJob.setStatus(StatusValue.NOT_TESTED);
        newJob.setStatusUpdateTime(Calendar.getInstance());
        newJob.setConfig(JobConfiguration.createDefault(newJob, name));

        return newJob;
    }



    /**
     * Erases this job.
     * 
     * @return <code>true</code> if this job has been successfully deleted
     */
    public boolean delete() {
        this.unschedule();
        return JobDaoHelper.getJobDao().delete(this);
    }



    /**
     * Polls each of this job's queries.
     * 
     * @param resultLogged
     *            <code>true</code> to log the result
     * @return the result of polling the queries
     */
    public JobResult executeAllQueries(boolean resultLogged) {
        final JobResult jobResult = new JobResult(this);
        
       
        if (this.hasQueries()) {
            if(this.config.isRunSimultaneous())
            {          	
            	 List<QueryResult> list = Query.executeSimultaneous(this.queries,resultLogged);
            	 for(QueryResult result : list)
            	 {
            		jobResult.addQueryResult(result); 
            	 }
            }else
            {
            	for (Query query : this.getQueries().values()) {
            	
            		final QueryResult queryResult = query.execute(resultLogged,false);
            		jobResult.addQueryResult(queryResult);
            	}
        	}

        } else {
            jobResult.defineAsNoQuery();
        }

        if (resultLogged) {
            this.updateStatusFromResult(jobResult);
        }

        return jobResult;
    }
    
    public QueryResult executeSingleQuery(Query query)
    {
    	final QueryResult queryResult = query.execute(false,true);
    	return queryResult;
    }


    
    /**
     * Gets this job's configuration informations.
     * 
     * @return the job configuration object
     */
    public JobConfiguration getConfig() {
        return this.config;
    }



    /**
     * Gets this job's identifier.
     * 
     * @return the long that uniquely identify this job
     */
    public long getJobId() {
        return this.jobId;
    }



    /**
     * Gets the identifier for each of this job's queries.
     * 
     * @return a set containing the query's identifiers
     */
    public Set<Long> getQueriesIds() {
        final Map<Long, Query> queriesMap = this.getQueries();

        if (null != queriesMap) {
            return queriesMap.keySet();
        }

        return new HashSet<Long>();
    }



    /**
     * Gets this job's queries.
     * 
     * @return a collection containing all this job's queries
     */
    public Collection<Query> getQueriesList() {
        final Map<Long, Query> queriesMap = this.getQueries();

        if (null != queriesMap) {
            return Collections.unmodifiableCollection(queriesMap.values());
        }

        return Collections.unmodifiableCollection(new HashSet<Query>());
    }



    /**
     * Gets one of the job's queries.
     * 
     * @param   queryId the long identifying the query to retrieve
     * @return          the query, if it's been found or <br>
     *                  <code>null</code> otherwise
     */
    public Query getQuery(long queryId) {

        if (1 > queryId) {
            throw new IllegalArgumentException("Invalid query identifier.");
        }

        final Map<Long, Query> jobQueries = this.getQueries();

        if (null != jobQueries && jobQueries.containsKey(queryId)) {
            return jobQueries.get(queryId);
        }

        return null;
    }



    /**
     * Gets the names of all this job's queries.
     * 
     * @return a set containing the name of each of this job's queries
     */
    public Set<String> getQueriesNames() {
        final Set<String> queriesNames = new HashSet<String>();

        if (this.hasQueries()) {

            for (Query jobQuery : this.getQueriesList()) {
                final String queryName = jobQuery.getConfig().getQueryName();

                if (!queriesNames.contains(queryName)) {
                    queriesNames.add(queryName);
                }
            }
        }

        return queriesNames;
    }



    /**
     * Defines the helper object used to schedule this job's monitoring.
     * 
     * @param   newSchedulerHelper  the job scheduler helper
     */
    @SuppressWarnings("unused")
    private void setSchedulerHelper(IJobScheduler newSchedulerHelper) {
        this.schedulerHelper = newSchedulerHelper;
    }

    

    /**
     * Gets the helper object used to schedule this job's monitoring.
     * 
     * @return  the job scheduler helper
     */
    private IJobScheduler getSchedulerHelper() {
        
        if (null == this.schedulerHelper) {
            final ApplicationContext appContext = AppContext.getContext();
                
            if (null != appContext) {
                this.schedulerHelper
                    = (IJobScheduler) appContext.getBean("schedulerHelper");
            }

        }
        
        return this.schedulerHelper;
    }



    /**
     * Gets this job's current status.
     * <p>
     * This is the status that resulted from the last automatic execution.
     * 
     * @return  this job's status
     */
    public Status getStatus() {
        return this.status;
    }



    /**
     * Gets this job's current status value.
     * <p>
     * This is the status that resulted from the last automatic execution.
     * 
     * @return  this job's status value
     */
    public StatusValue getStatusValue() {
        return ((this.status != null) ? this.status.getStatusValue() : null);
    }



    /**
     * Indicates if queries have been defined for this job.
     * 
     * @return <code>true</code> if queries are defined for this job
     */
    public boolean hasQueries() {
        final Map<Long, Query> jobQueries = this.getQueries();

        return (null != jobQueries && 0 < jobQueries.size());
    }



    /**
     * Checks this job's validity
     * <p>
     * A job is valid if:
     * <ol>
     * <li>it has an identifier (except if it's new)</li>
     * <li>its status isn't null</li>
     * <li>a configuration object is defined</li>
     * <li>the configuration is valid</li>
     * </ol>
     * <p>
     * Some operations, such as persisting or attaching queries, aren't allowed
     * for an invalid job.
     * 
     * @param   isNew   whether this job has just been created
     * @return          <code>true</code> if this job is valid
     * @see     JobConfiguration#isValid()
     */
    public boolean isValid(boolean isNew) {
        final boolean configOk = (null != this.config && this.config.isValid());
        final boolean jobIdOk = (isNew || 0 < this.getJobId());
        final boolean statusOk = (null != this.status);

        return (jobIdOk && statusOk && configOk);

    }



    /**
     * Defines this job's status.
     * <p>
     * <i><b>Note:</b> This function shouldn't be called directly. The status is
     * usually set when a query is executed.</i>
     * 
     * @param   newStatusValue  the new status value for the job
     */
    private void setStatus(Status.StatusValue newStatusValue) {
        Status newStatus;

        if (null == newStatusValue) {
            throw new IllegalArgumentException("Status can't be null");
        }

        newStatus = Status.getStatusObject(newStatusValue);

        if (null == newStatus) {
            throw new IllegalArgumentException(String.format(
                     "Unknown format '%1$s'.", newStatusValue.name()));
        }

        this.status = newStatus;
    }



    /**
     * Update this job's status according to an execution result and raises an
     * alert if required.
     * 
     * @param   result  the result of the job queries' polling
     */
    private void updateStatusFromResult(JobResult result) {

        if (!result.getJobName().equals(this.getConfig().getJobName())) {
            throw new IllegalArgumentException(
                   "The result doesn't belong to this job.");
        }

        final StatusValue newStatus = result.getStatusValue();
        final StatusValue oldStatus = this.getStatusValue();
        this.setStatusUpdateTime(Calendar.getInstance());
        if (newStatus != oldStatus) {
            this.setStatus(newStatus);
            if (this.getConfig().isAutomatic()
                && this.getConfig().isAlertsActivated()) {
                final Alert alert = Alert.create(oldStatus, newStatus,
                                                 result.getStatusCause(), result.getResponseDelay(), result.getHttpCode(), this,null,"");
                
                this.triggerActions(alert);
            }
        }
        
        // Replaces the trigger transaction update
        //this.updateStatusJob();
        this.persist();
    }



    /**
     * Gets the actions to be triggered when an alert is raised.
     * 
     * @return  a set containing the actions defined for this job
     */
    public Set<AbstractAction> getActions() {
        return this.actions;
    }



    /**
     * Gets the queries defined for this job.
     * <p>
     * This method returns a map with the requests' identifiers as key, to allow
     * quick search.
     * 
     * @return  a map containing this job's queries
     */
    private Map<Long, Query> getQueries() {
        return this.queries;
    }



    /**
     * Defines the actions to be taken when alert is raised for this job.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purpose and 
     * shouldn't be called directly.</i>
     * 
     * @param   jobActions  a set containing the actions for this job
     */
    @SuppressWarnings("unused")
    private void setActions(Set<AbstractAction> jobActions) {
        this.actions = jobActions;
    }



    /**
     * Defines this job's configuration.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purpose and 
     * shouldn't be called directly.</i>
     * 
     * @param   jobConfig   the job configuration object
     */
    private void setConfig(JobConfiguration jobConfig) {
        this.config = jobConfig;
    }



    /**
     * Defines this job's identifier.
     * <p>
     * <i><b>Note:</b> This method shouldn't be used directly. The identifier is
     * usually assigned by the persistance mechanism.</i>
     * 
     * @param   newJobId    the long that uniquely identify this job
     */
    @SuppressWarnings("unused")
    private void setJobId(long newJobId) {

        if (1 > newJobId) {
            throw new IllegalArgumentException("Invalid job identifier");
        }

        this.jobId = newJobId;
    }



    /**
     * Defines this job's queries.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purpose and 
     * shouldn't be called directly.</i>
     * 
     * @param   jobQueries  a map containing this job's queries, with their 
     *                      identifier as key
     */
    @SuppressWarnings("unused")
    private void setQueries(Map<Long, Query> jobQueries) {
        this.queries = jobQueries;
    }



    /**
     * Defines the job status.
     * <p>
     * <i><b>Note:</b> This function shouldn't be called directly The status is
     * usually set when a query is executed.</i>
     * 
     * @param   newStatus   the new status for the job
     */
    @SuppressWarnings("unused")
    private void setStatus(Status newStatus) {

        if (null == newStatus) {
            throw new IllegalArgumentException("Status can't be null");
        }
        
        this.status = newStatus;
        //this.status.setRequestTime(Calendar.getInstance());

    }

    public Calendar getStatusUpdateTime() {
		return statusUpdateTime;
	}



	public void setStatusUpdateTime(Calendar statusUpdateTime) {
		this.statusUpdateTime = statusUpdateTime;
	}

    /**
     * Executes all the actions defined for this job
     * <p>
     * <i><b>Note:</b> This function shouldn't be called directly. The actions
     * are usually executed when an alert is raised.</i>
     * 
     * @param   alert   the alert that commanded the action's execution
     */
    private void triggerActions(Alert alert) {
        final Set<AbstractAction> actionsSet = this.getActions();

        if (null != actionsSet) {

            for (AbstractAction action : actionsSet) {
                action.trigger(alert);
            }
        }
    }



    /**
     * Schedules this job for automatic execution.
     * 
     * @return <code>true</code> if this job has successfully been scheduled
     */
    public boolean schedule() {

        if (null != this.getSchedulerHelper()) {

            return this.getSchedulerHelper().scheduleJob(this);

        }

        return false;
    }



    /**
     * Removes the scheduled task automatically executing this job.
     * 
     * @return <code>true</code> if this job has successfully been unscheduled
     */
    public boolean unschedule() {

        if (null != this.getSchedulerHelper()) {

            return this.getSchedulerHelper().unscheduleJob(this.getJobId());

        }

        return false;
    }



    /**
     * Updates the job's automatic execution scheduling.
     * 
     * @return <code>true</code> if this job's scheduling has successfully been
     *         updated
     */
    public boolean updateScheduleState() {

        if (null != this.getSchedulerHelper()) {

            return this.getSchedulerHelper().updateScheduleState(this);

        }

        return false;
    }



    /**
     * Saves this job.
     * 
     * @return <code>true</code> if this job has successfully been saved
     */
    public boolean persist() {

        return JobDaoHelper.getJobDao().persistJob(this);

    }
    


    /**
     * Executes all the queries for a given job and logs the result.
     * <p>
     * <i><b>Note:</b> this method is intended for a job's automatic execution
     * ans shouldn't thus be called directly.</i>
     * 
     * @param   targetJobId   the long identifying the job to be executed
     */
    public static void executeAllQueriesForJob(long targetJobId) {

        if (1 > targetJobId) {
            throw new IllegalArgumentException("Invalid job id");
        }

        final IJobDao jobDao = JobDaoHelper.getJobDao();
        final Job job = jobDao.getJobById(targetJobId);
        
        if (null != job) {
            job.executeAllQueries(true);
        }
    }



    /**
     * Retrieves one of this job's queries from an identifying string.
     * 
     * @param   queryIdString   a string containing either the query's 
     *                          identifier or name
     * @return                  the query, if it's been found or<br>
     *                          <code>null</code> otherwise
     */
    public Query getQueryFromIdString(String queryIdString) {

        try {
            final long queryId = Long.parseLong(queryIdString);

            return this.getQuery(queryId);

        } catch (NumberFormatException e) {

            return this.getQueryByName(queryIdString);

        }
    }



    /**
     * Gets one of this job's queries from its name.
     * 
     * @param   queryName   the query's name
     * @return              the query, if it's been found or<br>
     *                      <code>null</code> otherwise
     */
    public Query getQueryByName(String queryName) {

        for (Query query : this.getQueriesList()) {

            if (query.getConfig().getQueryName().equals(queryName)) {
                return query;
            }
        }

        return null;
    }



    /**
     * Gets a job from an identifying string.
     * 
     * @param   idString    a string containing either the job's identifier or 
     *                      name
     * @return              the job, if it's been found or<br>
     *                      <code>null</code> otherwise
     */
    public static Job getFromIdString(String idString) {

        if (StringTools.isNullOrEmpty(idString)) {
            throw new IllegalArgumentException(
                   "Job identifier string can't be null or empty.");
        }

        return JobDaoHelper.getJobDao().getJobFromIdString(idString);
    }



    /**
     * Gets a job from its identifier.
     * 
     * @param   searchedJobId   the long identifying the job
     * @return                  the job if it's been found or<br>
     *                          <code>null</code> otherwise
     */
    public static Job getFromId(long searchedJobId) {

        return JobDaoHelper.getJobDao().getJobById(searchedJobId);

    }



    /**
     * Gets a job from its name.
     * 
     * @param   searchedJobName the name identifying the job
     * @return                  the job if it's been found or<br>
     *                          <code>null</code> otherwise
     */
    public Job getJob(String searchedJobName) {

        return JobDaoHelper.getJobDao().getJobByName(searchedJobName);

    }



    /**
     * Gets the alerts raised for this job.
     * 
     * @param   onlyRss <code>true</code> to get only the alerts exposed to this
     *                  job's RSS feed
     * @return          a list containing the alerts for this job
     */
    public List<Alert> getAlerts(boolean onlyRss) {
        
        return AlertDaoHelper.getDaoObject().getAlertsForJob(this.getJobId(),
                                                             onlyRss);
        
    }



    /**
     * Gets the alerts raised for this job.
     * 
     * @param   onlyRss <code>true</code> to get only the alerts exposed to this
     *                  job's RSS feed
     * @param	start   This is the start of paginated query
     * @param	limit	This is the limit of paginated query
     * @param	sortField	The field to sort by
     * @param	direction	The direction to sort by
     * @return          a list containing the alerts for this job
     */
    public List<Alert> getAlerts(boolean onlyRss, Integer start, Integer limit, String sortField, String direction) {
        
        return AlertDaoHelper.getDaoObject().getAlertsForJob(this.getJobId(),
                                                             onlyRss, start, limit, sortField, direction );
        
    }

    /**
     * Get one of this job's actions from its identifier.
     * 
     * @param   actionId    the long uniquely identifying the action
     * @return              the action, if it's been found or<br>
     *                      <code>null</code> otherwise
     */
    public AbstractAction getActionById(long actionId) {
        final Set<AbstractAction> actionsSet = this.getActions();

        if (null != actionsSet) {

            for (AbstractAction action : actionsSet) {
                if (action.getActionId() == actionId) {
                    return action;
                }
            }
        }

        return null;
    }



    /**
     * Detaches a query from this job.
     * 
     * @param   query   the query to detach
     */
    public void removeQuery(Query query) {

        this.getQueries().remove(query);

    }

}
