package org.easysdi.monitor.dat.dao;

import java.util.List;
import java.util.Map;

import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.JobDefaultParameter;
//import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Provides job persistance operations.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public interface IJobDao {

	 
    /**
     * Finds jobs matching the given criteria.
     * <p>
     * Each parameter can be set to <code>null</code> if it must be ignored.
     * 
     * @param   automatic       <code>true</code> to fetch only the jobs that 
     *                          are executed automatically
     * @param   realTimeAllowed <code>true</code> to fetch only the jobs which
     *                          can be executed on demand
     * @param   published       <code>true</code> to fetch only the jobs that
     *                          are exposed to all users
     * @param   alertsEnabled   <code>true</code> to fetch only the jobs that
     *                          trigger an alert when their status change
     * @return                  a list containing the jobs matching the criteria
     */
    List<Job> findJobs(Boolean automatic, Boolean realTimeAllowed, 
                       Boolean published, Boolean alertsEnabled);

    /**
     * Finds jobs matching the given criteria with pagination constraints
     * <p>
     * Each parameter can be set to <code>null</code> if it must be ignored.
     * 
     * @param   automatic       <code>true</code> to fetch only the jobs that 
     *                          are executed automatically
     * @param   realTimeAllowed <code>true</code> to fetch only the jobs which
     *                          can be executed on demand
     * @param   published       <code>true</code> to fetch only the jobs that
     *                          are exposed to all users
     * @param   alertsEnabled   <code>true</code> to fetch only the jobs that
     *                          trigger an alert when their status change
     * @param   pageStart       start of pagination, thats the index of the first item to fetch
     * @param   pageLimit       limit of pagination, thats the number of results to fetch from pageStart.   
     * @param   pageLimit       limit of pagination, thats the number of results to fetch from pageStart.   
     * @param   sortField      	field to sort by                                          
     * @return                  a list containing the jobs matching the criteria
     */

     List<Job> findJobs(Boolean automatic, Boolean realTimeAllowed,
            Boolean published, Boolean alertsEnabled, Integer pageStart, Integer pageLimit, String sortField, String direction) ;


    /**
     * Erases a job.
     * <p>
     * This will also delete all the related objects (queries, alerts, actions, 
     * log entries, etc.)
     * 
     * @param   job the job
     * @return      <code>true</code> if the job was successfully deleted
     */
    boolean delete(Job job);



    /**
     * Gets the default parameters for a new job.
     * 
     * @return  a map of the job default parameters
     */
    Map<String, JobDefaultParameter> getDefaultParams();



    /**
     * Fetches a job from its identifier.
     * 
     * @param   jobId   the long identifying the searched job
     * @return          the job if it's been found, or<br>
     *                  <code>null</code> otherwise
     */
    Job getJobById(long jobId);



    /**
     * Fetches a job from its name.
     * 
     * @param   name    the name of the searched job
     * @return          the job if it's been found, or<br>
     *                  <code>null</code> otherwise
     */
    Job getJobByName(String name);



    /**
     * Gets all the jobs.
     * 
     * @return  a list containing all the existing jobs
     */
    List<Job> getAllJobs();



    /**
     * Gets the Spring transaction template for the persistance system.
     * 
     * @return  the transaction template
     */
    TransactionTemplate getTxTemplate();
    
    /**
     * Saves a job.
     * 
     * @param   job the job
     * @return      <code>true</code> if the job was successfully saved
     */
    boolean persistJob(Job job);

    /**
     * Fetches a job from an identifying string.
     * 
     * @param   identifyString  a string containing the job's identifier or its
     *                          name
     * @return                  the job if it's been found, or<br>
     *                          <code>null</code> otherwise
     */
    Job getJobFromIdString(String identifyString);

    

    /**
     * Saves a job default parameter.
     * 
     * @param   jobDefaultParameter the job default parameter
     * @return                      <code>true</code> if the parameter was
     *                              successfully saved
     */
    boolean persistJobDefault(JobDefaultParameter jobDefaultParameter);

}
