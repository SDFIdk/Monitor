package org.easysdi.monitor.biz.job.auto;

import java.util.Date;

import org.easysdi.monitor.biz.job.Job;

/**
 * Interface for classes allowing to schedule tasks for background execution.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public interface IJobScheduler {

    /**
     * Schedules the execution of a Monitor job.
     * 
     * @param   jobId       the identifier of the job to schedule
     * @param   startTime   when the job should be first executed
     * @param   interval    the number of seconds between two executions
     * @return              <code>true</code> if the job was successfully 
     *                      scheduled
     */
    boolean scheduleJob(long jobId, Date startTime, int interval);


    /**
     * Schedules the execution of a Monitor job.
     * 
     * @param   job the job to schedule
     * @return      <code>true</code> if the job was successfully 
     *              scheduled
     */
    boolean scheduleJob(Job job);



    /**
     * Schedules or unschedules a job, based on its configurazion.
     * 
     * @param   job the job
     * @return      <code>true</code> if the scehduling status was successfully 
     *              updated
     */
    boolean updateScheduleState(Job job);



    /**
     * Schedules all the automatic jobs.
     * 
     * @return  <code>true</code> if the job were successfully scheduled
     */
    boolean scheduleAllJobs();



    /**
     * Unschedules a job.
     * 
     * @param   jobId   the identifier of the job to unschedule
     * @return          <code>true</code> if the job was successfully 
     *                  unscheduled
     */
    boolean unscheduleJob(long jobId);

}
