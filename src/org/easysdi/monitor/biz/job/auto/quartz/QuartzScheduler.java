package org.easysdi.monitor.biz.job.auto.quartz;

import java.util.Date;

import org.apache.log4j.Logger;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.JobConfiguration;
import org.easysdi.monitor.biz.job.JobsCollection;
import org.easysdi.monitor.biz.job.auto.IJobScheduler;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

/**
 * Manages quarts tasks scheduling.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class QuartzScheduler implements IJobScheduler {

    private static final String GROUP_NAME = "Monitor";
    private static final int MILLISECONDS_IN_A_SECOND = 1000;

    private final Logger logger = Logger.getLogger(QuartzScheduler.class);
    private Scheduler    scheduler;


    
    /**
     * Creates a new scheduler. 
     */
    public QuartzScheduler() {

    }



    /**
     * Schedules a job task.
     * 
     * @param   jobId       the identifier for the (Monitor) job to schedule
     * @param   startTime   the start of the job scheduling
     * @param   interval    the number of seconds between two job executions
     * @return              <code>true</code> if the job was scheduled
     *                      successfully
     */
    public boolean scheduleJob(long jobId, Date startTime, int interval) {
        final JobDetail details = this.createJobDetails(jobId);
        final Trigger autoTrigger 
            = this.createTriggerForJob(jobId, startTime, interval, 
                                       details.getName());

        return this.addToScheduler(details, autoTrigger);
    }



    /**
     * Adds a job task to the Quartz scheduler.
     * 
     * @param   details the object containing the task details
     * @param   trigger the Quartz trigger for the job
     * @return  <code>true</code> it the task was successfully added
     */
    private boolean addToScheduler(JobDetail details, Trigger trigger) {

        try {
            final Scheduler quartzScheduler = this.getScheduler();

            final Trigger existingTrigger 
                = quartzScheduler.getTrigger(trigger.getName(), 
                                             trigger.getGroup());

            if (null != existingTrigger) {
                quartzScheduler.unscheduleJob(existingTrigger.getName(),
                                        existingTrigger.getGroup());
            }

            if (null != quartzScheduler.getJobDetail(details.getName(),
                                               details.getGroup())) {
                quartzScheduler.deleteJob(details.getName(), 
                                          details.getGroup());
            }

            quartzScheduler.addJob(details, true);
            quartzScheduler.scheduleJob(trigger);

            return true;

        } catch (SchedulerException e) {
            this.logger.error(String.format("Unable to schedule the job '%1$s'",
                                       details.getName()),
                         e);

            return false;
        }
    }



    /**
     * Creates the details object for a (Monitor) job.
     * 
     * @param   jobId       the identifier for the Monitor job to schedule
     * @return              the Quartz job details object
     */
    private JobDetail createJobDetails(long jobId) {
        final JobDetail details = new JobDetail(this.buildJobName(jobId), 
                                                QuartzScheduler.GROUP_NAME,
                                                AutomaticJob.class);
        details.getJobDataMap().put("monitorJobId", jobId);
        details.setDurability(true);

        return details;
    }



    /**
     * Builds the name of an automatic Quartz job.
     * 
     * @param   jobId   the identifier for the Monitor job to schedule
     * @return          the name for the Quartz job
     */
    private String buildJobName(long jobId) {
        return String.format("autoJob%1$d", jobId);
    }



    /**
     * Creates a Quartz trigger for a Monitor job.
     * 
     * @param   jobId       the identifier of the Monitor job
     * @param   startTime   the scheduling start time
     * @param   interval    the number of the seconds between two executions
     * @param   autoJobName the name of the matching Quartz job
     * @return              the Quartz trigger
     */
    private Trigger createTriggerForJob(long jobId, Date startTime, 
                                        int interval, String autoJobName) {

        final Trigger trigger 
            = new SimpleTrigger(this.buildTriggerName(jobId),
                    QuartzScheduler.GROUP_NAME, startTime, null,
                    org.quartz.SimpleTrigger.REPEAT_INDEFINITELY, 
                    interval * 1000L);

        trigger.setJobName(autoJobName);
        trigger.setJobGroup(QuartzScheduler.GROUP_NAME);

        return trigger;
    }



    /**
     * Builds the name of a Quartz trigger.
     * 
     * @param   jobId   the identifier of the Monitor job to execute
     * @return          the name of the trigger
     */
    private String buildTriggerName(long jobId) {
        return String.format("autoTrigger%1$d", jobId);
    }



    /**
     * Removes a job from the scheduler.
     * 
     * @param   jobId   the identifier for the Monitor job that must be 
     *                  unscheduled    
     * @return          <code>true</code> if the job has been unscheduled
     *                  successfully 
     */
    public boolean unscheduleJob(long jobId) {
        final String autoJobName = this.buildJobName(jobId);
        boolean success = true;

        try {

            if (null != this.getScheduler().getJobDetail(
                             autoJobName, QuartzScheduler.GROUP_NAME)) {
                
                success = this.getScheduler().deleteJob(
                                 autoJobName, QuartzScheduler.GROUP_NAME);
            }

        } catch (SchedulerException e) {
            this.logger.error(String.format(
                   "An error occurred while the job '%1$s' was unscheduled.",
                   autoJobName), e);

            success = false;
        }
        
        return success;
    }



    /**
     * Defines the underlying Quartz scheduler.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes and 
     * ahouldn't be called directly.</i>
     * 
     * @param   quartzScheduler the Quartz scheduler to use
     * 
     */
    public void setScheduler(Scheduler quartzScheduler) {
        this.scheduler = quartzScheduler;
    }



    /**
     * Gets the underlying quartz scheduler.
     * 
     * @return  the Quartz scheduler to use
     */
    private Scheduler getScheduler() {
        return this.scheduler;
    }



    /**
     * Checks the existing Monitor jobs and schedules those marked for 
     * automatic executions.
     * 
     * @return  <code>true</code> if all the automatic jobs have been scheduled
     */
    public boolean scheduleAllJobs() {
        boolean success = true;

        for (Job job : new JobsCollection().getJobs()) {

            if (!this.scheduleJob(job)) {
                success = false;
            }
        }

        return success;
    }



    /**
     * Schedules a Monitor job.
     * 
     * @param   job the Monitor job to schedule
     * @return      <code>true</code> if the job has been scheduled successfully
     */
    public boolean scheduleJob(Job job) {
        final JobConfiguration config = job.getConfig();
        final int testInterval = config.getTestInterval();

        if (config.isAutomatic() && job.isValid(false)) {
            this.scheduleJob(
                 job.getJobId(),
                 new Date(System.currentTimeMillis()
                          + (testInterval 
                             * QuartzScheduler.MILLISECONDS_IN_A_SECOND)), 
                 testInterval);

            return true;
        }

        return false;
    }



    /**
     * Updates the scheduling of a Monitor job.
     * 
     * @param   job the Monitor job whose scheduling must be updated
     * @return      <code>true</code> if the scheduling of the job has been
     *              updated successfully
     */
    public boolean updateScheduleState(Job job) {
        final JobConfiguration jobConfig = job.getConfig();
   
        boolean success = false;
        
        if (job.isValid(false)) {

            if (jobConfig.isAutomatic() && 0 < jobConfig.getTestInterval()) {
                success = this.scheduleJob(job);
            } else {
                success = this.unscheduleJob(job.getJobId());
            }
        }

        return success;
    }
}
