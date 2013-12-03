package org.easysdi.monitor.biz.job.auto;

/**
 * Represents a scheduler error. 
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class JobSchedulerException extends Exception {

    private static final long serialVersionUID = -355818694490830809L;



    /**
	 * Creates a new scheduler exception.
	 */
    public JobSchedulerException() {
        super();
    }



    /**
     * Creates a new scheduler exception.
     * 
     * @param   message the message explaining why this exception occurred
     * @param   cause   the inner cause of this exception
     */
    public JobSchedulerException(String message, Throwable cause) {
        super(message, cause);
    }



    /**
     * Creates a new scheduler exception.
     * 
     * @param   message the message explaining why this exception occurred
     */
    public JobSchedulerException(String message) {
        super(message);
    }



    /**
     * Creates a new scheduler exception.
     * 
     * @param   cause   the inner cause of this exception
     */
    public JobSchedulerException(Throwable cause) {
        super(cause);
    }

}
