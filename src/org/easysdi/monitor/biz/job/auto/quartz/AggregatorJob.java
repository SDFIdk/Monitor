package org.easysdi.monitor.biz.job.auto.quartz;

import org.easysdi.monitor.biz.job.JobsCollection;
//import org.easysdi.monitor.dat.dao.IJobDao;
//import org.easysdi.monitor.dat.dao.JobDaoHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.QuartzJobBean;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * The scheduled task responsible for the daily aggregation of raw logs. 
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class AggregatorJob extends QuartzJobBean implements StatefulJob {
    
    /**
     * Creates a new log aggregation task.
     */
    public AggregatorJob() {
        
    }

    
    
    /**
     * Executes the scheduled aggregation.
     * 
     * @param   context the context in which the task is executed
     * @throws JobExecutionException   a lower-level error prevented the 
     *                                  execution of the scheduled task 
     */
    @Override
    protected void executeInternal(JobExecutionContext context)
        throws JobExecutionException {

        //final IJobDao jobDao = JobDaoHelper.getJobDao();
        new JobsCollection().aggregateLogs();
       /* jobDao.getTxTemplate().execute(new TransactionCallbackWithoutResult() {
    	   
        	
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {

                new JobsCollection().aggregateLogs();
            }
        });*/
    }

}
