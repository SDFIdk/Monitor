package org.easysdi.monitor.biz.alert;

import java.util.Calendar;

import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Status;
import org.easysdi.monitor.biz.job.Status.StatusValue;
import org.easysdi.monitor.dat.dao.AlertDaoHelper;

/**
 * Represents a job status change, if said job is parametrized to trigger alerts
 * in such a case.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 */
public class Alert {

    private long     alertId;
    private String   cause;
    private boolean  exposedToRss;
    private Status   newStatus;
    private Status   oldStatus;
    private Job      parentJob;
    private Calendar time;
    private float    responseDelay;
    private Integer  httpCode;
    private byte[] 	 imageError;
    private String contentType;  



    public float getResponseDelay() {
		return responseDelay;
	}



	public void setResponseDelay(float responseDelay) {
		this.responseDelay = responseDelay;
	}



	public Integer getHttpCode() {
		return httpCode;
	}



	public void setHttpCode(Integer httpCode) {
		this.httpCode = httpCode;
	}
	
	public byte[] getImageError()
	{
		return this.imageError;
	}
	
	public void setImageError(byte[] image)
	{
		this.imageError = image;
	}
	
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}
	
	public String getContentType()
	{
		return this.contentType;
	}
	

	/**
     * No-argument constructor, used by the persistance mechanism.
     */
    private Alert() {
        this.setExposedToRss(false);
    }



    /**
     * Instantiates an alert.
     * 
     * @param   oldJobStatus    the job status until now
     * @param   newJobStatus    the status produced by the job's last execution
     * @param   alertCause      the reason for the current status being what 
     *                          it is
     * @param   job             the job that raised the alert
     * @param   alertTime       the time of the alert
     */
    private Alert(StatusValue oldJobStatus, StatusValue newJobStatus, 
                  String alertCause, Job job, Calendar alertTime, float responseDelay, Integer httpCode,byte[] image, 
                  String contentType) {

        this();
        this.setCause(alertCause);
        this.setNewStatus(newJobStatus);
        this.setOldStatus(oldJobStatus);
        this.setParentJob(job);
        this.setTime(alertTime);
        this.setResponseDelay(responseDelay);
        this.setHttpCode(httpCode);
        this.setImageError(image);
        this.setContentType(contentType);
    }



    /**
     * Defines the identifier of the alert.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use. Defining the
     * identifier is best left to the persistance mechanism.</i>
     * 
     * @param   newAlertId  a long uniquely identifying the alert
     */
    @SuppressWarnings("unused")
    private void setAlertId(long newAlertId) {
        this.alertId = newAlertId;
    }



    /**
     * Gets the identifier of the alert.
     * 
     * @return the long uniquely identifying the alert
     */
    public long getAlertId() {
        return this.alertId;
    }



    /**
     * Defines the cause of the status change.
     * 
     * @param   newCause   the reason for the new status of the job
     */
    public void setCause(String newCause) {
        this.cause = newCause;
    }



    /**
     * Gets the cause of the status change.
     * 
     * @return the reason for the new status of the job
     */
    public String getCause() {
        return this.cause;
    }



    /**
     * Defines if this alert should be included in the RSS feed for the
     * concerned job.
     * 
     * @param   isExposedToRss  <code>true</code> to include this alert in the 
     *                          feed
     */
    public void setExposedToRss(boolean isExposedToRss) {
        this.exposedToRss = isExposedToRss;
    }



    /**
     * Indicates if this alert is included in the job's RSS feed.
     * 
     * @return <code>true</code> if this alert is included in the job feed
     */
    public boolean isExposedToRss() {
        return this.exposedToRss;
    }



    /**
     * Defines the new status of the job.
     * <p>
     * This method only affects the alert. The job status itself won't be
     * altered.
     * 
     * @param   newJobStatus    the new status of the job
     */
    private void setNewStatus(Status newJobStatus) {

        if (null == newJobStatus) {
            throw new IllegalArgumentException("Status can't be null");
        }

        this.newStatus = newJobStatus;
    }



    /**
     * Defines the new status of the job
     * <p>
     * This method only affects the alert. The job status itself won't be
     * altered.
     * 
     * @param   newJobStatusValue   the new status of the job
     */
    public void setNewStatus(StatusValue newJobStatusValue) {

        if (null == newJobStatusValue) {
            throw new IllegalArgumentException("Status can't be null");
        }

        final Status newJobStatus = Status.getStatusObject(newJobStatusValue);

        if (null == newJobStatus) {
            throw new IllegalArgumentException("Unknown status value.");
        }

        this.setNewStatus(newJobStatus);
    }



    /**
     * Gets the new status of the job.
     * <p>
     * This is the current status of the job at the time of this alert. It may 
     * not be its current status at the time of inquiry.
     * 
     * @return the status that the job changed to
     */
    public Status getNewStatus() {
        return this.newStatus;
    }



    /**
     * Defines the status of the job at the time of the execution that triggered
     * this alert.
     * 
     * @param   oldJobStatus    the old status of the job
     */
    private void setOldStatus(Status oldJobStatus) {

        if (null == oldJobStatus) {
            throw new IllegalArgumentException("Status can't be null");
        }

        this.oldStatus = oldJobStatus;
    }



    /**
     * Defines the status that the job had at the time of the execution that
     * triggered this alert.
     * 
     * @param   oldJobStatusValue   the old status value of the job
     */
    public void setOldStatus(StatusValue oldJobStatusValue) {

        if (null == oldJobStatusValue) {
            throw new IllegalArgumentException("Status can't be null");
        }

        final Status oldJobStatus = Status.getStatusObject(oldJobStatusValue);

        if (null == oldJobStatus) {
            throw new IllegalArgumentException("Unknown status value.");
        }

        this.setOldStatus(oldJobStatus);
    }



    /**
     * Gets the status that the job had at the time of the execution that
     * triggered this alert.
     * 
     * @return  the old status of the job
     */
    public Status getOldStatus() {
        return this.oldStatus;
    }



    /**
     * Defines the job that raised this alert.
     * 
     * @param   job the job whose status changed
     */
    public void setParentJob(Job job) {
        this.parentJob = job;
    }



    /**
     * Gets the job that raised this alert.
     * 
     * @return  the job whose status changed
     */
    public Job getParentJob() {
        return this.parentJob;
    }



    /**
     * Defines when the alert was raised.
     * 
     * @param   alertTime   when the atatus change occurred
     */
    public void setTime(Calendar alertTime) {
        this.time = alertTime;
    }



    /**
     * Gets when the alert was raised.
     * 
     * @return  when the atatus change occurred
     */
    public Calendar getTime() {
        return this.time;
    }



    /**
     * Creates an alert and saves it.
     * 
     * @param   oldStatus   the status that the job had until now
     * @param   newStatus   the status produced by last execution of the job
     * @param   cause       the reason for the last status being what it is
     * @param   parentJob   the job that raised the alert
     * @return              the new alert
     */
    public static Alert create(StatusValue oldStatus, StatusValue newStatus,
                    String cause, float responseDelay, Integer httpCode, Job parentJob,byte[] image,String contentType) {
        
    	final Alert alert = new Alert(oldStatus, newStatus, cause, parentJob,
                                Calendar.getInstance(), responseDelay, httpCode,image,contentType);
        
        AlertDaoHelper.getDaoObject().persist(alert);
        return alert;
    }
    
    public static Alert getFromIdString(String idString)
    {
        if (StringTools.isNullOrEmpty(idString)) {
            throw new IllegalArgumentException(
                   "Alert identifier string can't be null or empty.");
        }
        return AlertDaoHelper.getDaoObject().getAlertFromIdString(idString);
    }

}
