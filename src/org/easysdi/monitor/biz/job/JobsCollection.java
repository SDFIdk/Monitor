package org.easysdi.monitor.biz.job;

import java.util.Collections;
import java.util.List;

import org.easysdi.monitor.biz.logging.LogManager;
import org.easysdi.monitor.dat.dao.JobDaoHelper;

/**
 * Represents the collection of all jobs. Contains some generic methods.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * 
 */
public class JobsCollection {

    private List<Job> jobs;
    
    
    
    /**
     * Creates a new object to manage the jobs collection.
     */
    public JobsCollection() {
        
    }

    

    /**
     * Looks for jobs meeting some criteria.
     * <p>
     * Each parameter can be <code>null</code> if its value is indifferent. 
     * 
     * @param   automatic           whether the jobs searched are automatic
     * @param   realTimeAllowed     whether the jobs searched can be executed
     *                              on demand 
     * @param   published           whether the jobs searched are accessible
     *                              to anybody
     * @param   alertsEnabled       whether the job triggers alerts
     * @return                      a list containing the jobs meeting the 
     *                              criteria
     */
    public List<Job> findJobs(Boolean automatic, Boolean realTimeAllowed,
                              Boolean published, Boolean alertsEnabled) {

        return JobDaoHelper.getJobDao().findJobs(automatic, realTimeAllowed,
                                                 published, alertsEnabled);

    }
    /**
     * Looks for jobs meeting some criteria.
     * <p>
     * Each parameter can be <code>null</code> if its value is indifferent. 
     * 
     * @param   automatic           whether the jobs searched are automatic
     * @param   realTimeAllowed     whether the jobs searched can be executed
     *                              on demand 
     * @param   published           whether the jobs searched are accessible
     *                              to anybody
     * @param   alertsEnabled       whether the job triggers alerts
     * @param   pageStart       start of pagination, thats the index of the first item to fetch
     * @param   pageLimit       limit of pagination, thats the number of results to fetch from pageStart.   
     * @param   sortField      	field to sort by
     * @param   direction       direction of sort   
     * @return                      a list containing the jobs meeting the 
     *                              criteria
     */

    public List<Job> findJobs(Boolean automatic, Boolean realTimeAllowed,
            Boolean published, Boolean alertsEnabled, Integer pageStart, Integer pageLimit, String sortField, String direction) {

    	return JobDaoHelper.getJobDao().findJobs(automatic, realTimeAllowed,
                               published, alertsEnabled, pageStart, pageLimit,   sortField,  direction);

    }


    /**
     * Gets all the jobs.
     * 
     * @return a list containing all the jobs
     */
    public List<Job> getJobs() {

        if (null == this.jobs) {
            this.loadJobs();
        }

        return Collections.unmodifiableList(this.jobs);
    }



    /**
     * Gets all the jobs from the database.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use (lazy fetching).
     * If you need the job collection, please use the
     * {@link JobsCollection#getJobs() getJobs()} method.</i>
     */
    private void loadJobs() {

        this.setJobs(JobDaoHelper.getJobDao().getAllJobs());

    }



    /**
     * Sets the job collection.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use. It shouldn't be
     * called directly.</i>
     * 
     * @param   jobsList    a collection containing all the jobs
     */
    private void setJobs(List<Job> jobsList) {
        this.jobs = jobsList;
    }



    /**
     * Starts the log aggregation process for each job.
     */
    public void aggregateLogs() {

        for (Job job : this.getJobs()) {
            final LogManager jobLogManager = new LogManager(job);
            jobLogManager.getLogAggregator().aggregateRawLogs();     
            for (Query query : job.getQueriesList()) {
                final LogManager queryLogManager = new LogManager(query);
                queryLogManager.getLogAggregator().aggregateRawLogs();
            }
        }
    
        // Saves hourly aggregated logs
        for (Job job : this.getJobs()) {
            final LogManager jobLogManager = new LogManager(job);
            jobLogManager.getLogAggregator().aggregateHourRawLogs();
            for (Query query : job.getQueriesList()) {
                final LogManager queryLogManager = new LogManager(query);
                queryLogManager.getLogAggregator().aggregateHourRawLogs();
            }
        }
    }
}
