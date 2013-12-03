package org.easysdi.monitor.biz.job.auto.quartz;

/*import org.easysdi.monitor.biz.job.JobsCollection;
import org.easysdi.monitor.dat.dao.IJobDao;
import org.easysdi.monitor.dat.dao.JobDaoHelper;*/


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
/*import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
*/
/**
 * The scheduled task responsible for testing an automatic job.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class AutomaticJob implements Job {
    
    /**
     * Creates a new job test task.
     */
    public AutomaticJob() {
        
    }
    
    /**
     * Tests a given job.
     * <p>
     * The identifier of the job to test must be specified in the job details 
     * data map, through a parameter named <code>monitorJobId</code>. The 
     * details are passed in the execution context object.
     * 
     * @param   context the context in which the task is executed
     * @throws  JobExecutionException   a lower-level error prevented the 
     *                                  execution of the scheduled task 
     */
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
    	org.easysdi.monitor.biz.job.Job.executeAllQueriesForJob(
    			  context.getJobDetail().getJobDataMap().getLongValue("monitorJobId"));

    }
    
    /* Does not work when running many jobs with short intervals */
    /**
     * Tests a given job.
     * <p>
     * The identifier of the job to test must be specified in the job details 
     * data map, through a parameter named <code>monitorJobId</code>. The 
     * details are passed in the execution context object.
     * 
     * @param   context the context in which the task is executed
     * @throws  JobExecutionException   a lower-level error prevented the 
     *                                  execution of the scheduled task 
     */
 /*   public void execute(final JobExecutionContext context)
        throws JobExecutionException {
        
        final IJobDao jobDao = JobDaoHelper.getJobDao();

        jobDao.getTxTemplate().execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {

                org.easysdi.monitor.biz.job.Job.executeAllQueriesForJob(
                        context.getJobDetail().getJobDataMap().getLongValue(
                                "monitorJobId"));
            }
        });

    }
   */ 
}
