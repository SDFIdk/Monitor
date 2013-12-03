package org.easysdi.monitor.biz.job;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.dat.dao.JobDaoHelper;
import org.easysdi.monitor.dat.dao.ServiceDaoHelper;

/**
 * Holds the configuration of a job.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 */
public final class JobConfiguration {

    private final Logger logger = Logger.getLogger(JobConfiguration.class);
    
    private boolean     alertsActivated;
    private boolean     automatic;
    private boolean     bizErrorChecked;
    private boolean     httpErrorChecked;
    private HttpMethod  httpMethod;
    private Job         job;
    private String      login;
    private String      jobName;
    private String      password;
    private boolean     published;
    private boolean     realTimeAllowed;
    private ServiceType serviceType;
    private long        serviceTypeId;
    private GregorianCalendar    slaEndTime;
    private GregorianCalendar    slaStartTime;
    private int         testInterval;
    private int         timeout;
    private String      url;
    private boolean		saveResponse;
    private boolean 	runSimultaneous;



    /**
     * No-argument constructor, used by the persistance mechanism.
     */
    private JobConfiguration() {

    }



    /**
     * Instantiates a configuration object for a job.
     * 
     * @param parentJob the job to be configured
     * @param name      the job's name
     */
    private JobConfiguration(Job parentJob, String name) {
        this.setJob(parentJob);
        this.setJobName(name);
        this.setDefaultValues();
    }



    /**
     * Defines if alerts are activated for the job.
     * <p>
     * An alert is raised when the job's status changes. Defined actions (if
     * any) are then triggered.
     * 
     * @param newAlertsActivated   <code>true</code> to activate the alerts
     */
    public void setAlertsActivated(boolean newAlertsActivated) {
        this.alertsActivated = newAlertsActivated;
    }



    /**
     * Gets whether alerts are activated for the job.
     * 
     * @return  <code>true</code> if a status change for this job will raise an
     *          alert
     */
    public boolean isAlertsActivated() {
        return this.alertsActivated;
    }



    /**
     * Defines if the job should be executed automatically.
     * 
     * @param   newAutomatic    <code>true</code> to excecute the job 
     *                          automatically
     */
    public void setAutomatic(boolean newAutomatic) {

        if (newAutomatic != this.automatic && null != this.getJob()) {
            this.automatic = newAutomatic;
            this.getJob().updateScheduleState();
        }
    }



    /**
     * Gets whether the job should be executed automatically.
     * 
     * @return  <code>true</code> if the job is executed automatically
     */
    public boolean isAutomatic() {
        return this.automatic;
    }



    /**
     * Defines whether business errors returned by the job are considered.
     * 
     * @param   newBizErrorChecked  <code>true</code> to check business errors
     */
    public void setBizErrorChecked(boolean newBizErrorChecked) {
        this.bizErrorChecked = newBizErrorChecked;
    }



    /**
     * Gets whether business errors returned by the job are considered.
     * 
     * @return  <code>true</code> if business errors are checked
     */
    public boolean isBizErrorChecked() {
        return this.bizErrorChecked;
    }



    /**
     * Defines if HTTP errors returned by the job are considered.
     * 
     * @param   newHttpErrorChecked <code>true</code> to check HTTP errors
     */
    public void setHttpErrorChecked(boolean newHttpErrorChecked) {
        this.httpErrorChecked = newHttpErrorChecked;
    }



    /**
     * Gets if HTTP errors returned by the job are considered.
     * 
     * @return  <code>true</code> if HTTP errors are checked
     */
    public boolean isHttpErrorChecked() {
        return this.httpErrorChecked;
    }



    /**
     * Defines the HTTP method used to poll the job's queries.
     * <p>
     * <i><b>Note:</b> You shouldn't call this method directly. It is meant to 
     * be used by the persistance mechanism. Please use 
     * {@link JobConfiguration#setHttpMethod(String)} instead.</i>
     * 
     * @param   newHttpMethod   the HTTP method object
     */
    private void setHttpMethod(HttpMethod newHttpMethod) {
        this.httpMethod = newHttpMethod;
    }



    /**
     * Defines the HTTP method used to poll the job's queries.
     * 
     * @param   newHttpMethodName   the HTTP method's name
     */
    public void setHttpMethod(String newHttpMethodName) {
        final HttpMethod newMethod = HttpMethod.getObject(newHttpMethodName);

        if (null == newMethod) {
            throw new IllegalArgumentException(String.format(
                     "Unknown method '%1$s'", newHttpMethodName));
        }

        this.setHttpMethod(newMethod);
    }



    /**
     * Gets the HTTP method used to poll the job's queries.
     * 
     * @return  the HTTP method object
     */
    public HttpMethod getHttpMethod() {
        return this.httpMethod;
    }



    /**
     * Defines the job that this configuration defines.
     * <p>
     * <i><b>Note:</b> You shouldn't call this method directly. It is meant to 
     * be used by the persistance mechanism.</i>
     * 
     * @param   newJob  the job defined by this configuration
     */
    private void setJob(Job newJob) {

        if (null == newJob) {
            throw new IllegalArgumentException("Parent job cannot be null");
        }

        this.job = newJob;
    }



    /**
     * Gets the job that this configuration defines.
     * 
     * @return  the job defined by this configuration
     */
    private Job getJob() {
        return this.job;
    }



    /**
     * Defines the user name used to poll this job's queries.
     * <p>
     * This property can be null if no credentials are used. If either the login
     * or password is null, no authentification is carried.
     * 
     * @param   newLogin    the user name used to poll the queries or 
     *                      <code>null</code> if no authentification is used
     */
    public void setLogin(String newLogin) {
        this.login = newLogin;
    }



    /**
     * Gets the user name used to poll this job's queries.
     * 
     * @return  the user name used to poll the queries or 
     *          <code>null</code> if no authentication is used
     */
    public String getLogin() {
        return this.login;
    }



    /**
     * Defines the job's name.
     * <p>
     * A job is identified both by a machine-friendly id and a more
     * human-friendly name.
     * 
     * @param   newName the name uniquely identifying the job
     */
    public void setJobName(String newName) {
        this.jobName = newName;
    }



    /**
     * Gets the job's name.
     * <p>
     * A job is identified both by a machine-friendly id and a more
     * human-friendly name.
     * 
     * @return  the name uniquely identifying the job
     */
    public String getJobName() {
        return this.jobName;
    }



    /**
     * Defines the password used to poll this job's queries.
     * <p>
     * This property can be null if no credentials are used. If either the login
     * or password is null, no authentification is carried.
     * 
     * @param   newPassword the password used to poll the queries or 
     *                      <code>null</code> if no authentication is used
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }



    /**
     * Gets the password used to poll this job's queries.
     * 
     * @return  the password used to poll the queries or 
     *          <code>null</code> if no authentication is used
     */
    public String getPassword() {
        return this.password;
    }



    /**
     * Defines if this job is publicly accessible.
     * <p>
     * If a job is publicly accessible, its configuration, its logs and its
     * alerts can be read without an authentication. If the job isn't published,
     * only admins can carry the aforementionned actions
     * 
     * @param   newPublished    <code>true</code> to allow public access to the 
     *                          job
     */
    public void setPublished(boolean newPublished) {
        this.published = newPublished;
    }



    /**
     * Gets whether this job is publicly accessible.
     * 
     * @return  <code>true</code> if public access is allowed for this job
     */
    public boolean isPublished() {
        return this.published;
    }



    /**
     * Defines if this job can be poll at a user's request.
     * 
     * @param   newRealTimeAllowed  <code>true</code> to allow users to manually
     *                              poll this job
     */
    public void setRealTimeAllowed(boolean newRealTimeAllowed) {
        this.realTimeAllowed = newRealTimeAllowed;
    }



    /**
     * Gets whether this job's can be polled at a user's request.
     * 
     * @return  <code>true</code> if users are allowed to manually poll this job
     */
    public boolean isRealTimeAllowed() {
        return this.realTimeAllowed;
    }



    /**
     * Defines the type of OGC web service monitored by this job (WMS or WFS,
     * for instance).
     * <p>
     * <i><b>Note:</b> You shouldn't call this method directly. It is meant to 
     * be used by the persistance mechanism. Please rather use {@link 
     * JobConfiguration#setServiceType(String)} instead.</i>
     * 
     * @param   newServiceType  the OGC service type object
     */
    private void setServiceType(ServiceType newServiceType) {

        this.serviceType = newServiceType;
    }



    /**
     * Defines the type of OGC web service monitored by this job (WMS or WFS,
     * for instance).
     * 
     * @param   newTypeName the name of the OGC web service type
     */
    public void setServiceType(String newTypeName) {

        if (this.getJob().hasQueries()) {
            throw new UnsupportedOperationException(
                "Can't change a job's service type if it has queries");
        }

        final ServiceType currentType = this.getServiceType();

        if (null == currentType || !currentType.getName().equals(newTypeName)) {
            final ServiceType newServiceType 
                = ServiceType.getObject(newTypeName);

            if (null == newServiceType) {
                throw new IllegalArgumentException(String.format(
                     "Unknown service type '%1$s'", newTypeName));
            }

            this.setServiceType(newServiceType);
            this.setServiceTypeId(this.serviceType.getId());
        }
    }



    /**
     * Gets the type of OGC web service that this job monitors.
     * 
     * @return  the service type object
     */
    public ServiceType getServiceType() {
        final long typeId = this.getServiceTypeId();

        if (1 > typeId) {
            this.setServiceType((ServiceType) null);
            
        } else if (null == this.serviceType
                   || this.serviceType.getId() != typeId) {
            
            this.setServiceType(
                    ServiceDaoHelper.getServiceDao().getServiceType(typeId));
        }

        return this.serviceType;
    }



    /**
     * Defines the web service type by its identifier.
     * <p>
     * <i><b>Note:</b> You shouldn't call this method directly. It is meant for 
     * internal use.</i>
     * 
     * @param   newServiceTypeId    the long identifying the web service type
     */
    private void setServiceTypeId(long newServiceTypeId) {
        this.serviceTypeId = newServiceTypeId;
    }



    /**
     * Gets the web service type identifier.
     * <p>
     * <i><b>Note:</b> You shouldn't call this method directly. It is meant to 
     * be used by the persistance mechanism. Its result isn't guaranteed to 
     * match the actual web service.</i>
     * 
     * @return  the long identifying the web service type.
     */
    private long getServiceTypeId() {
        return this.serviceTypeId;
    }



    /**
     * Defines the end of the SLA validated time.
     * <p>
     * The SLA validated time is a daily span specified in the Service Level
     * Agreement during which the service must be available.
     * 
     * @param   newSlaEndTime   the time at which the SLA validated time ends. 
     *                          (The date part is indifferent.)
     */
    public void setSlaEndTime(GregorianCalendar newSlaEndTime) {
        this.slaEndTime = newSlaEndTime;
    }



    /**
     * Gets the time at which the SLA validated time ends.
     * 
     * @return  the SLA validated time end. (The date part shouldn't be
     *          considered.)
     */
    public Calendar getSlaEndTime() {
        return this.slaEndTime;
    }



    /**
     * Defines the start of the SLA validated time.
     * <p>
     * The SLA validated time is a daily span specified in the Service Level
     * Agreement during which the service must be available.
     * 
     * @param   newSlaStartTime the time at which the SLA validated time starts.
     *                          (The date part is indifferent.)
     */
    public void setSlaStartTime(GregorianCalendar newSlaStartTime) {
        this.slaStartTime = newSlaStartTime;
    }



    /**
     * Gets the time at which the SLA validated time starts.
     * 
     * @return  the SLA validated time start. (The date part shouldn't be
     *          considered.)
     */
    public Calendar getSlaStartTime() {
        return this.slaStartTime;
    }



    /**
     * Sets a test interval for the job.
     * <p>
     * This property is ignored if the job isn't automatically executed.
     * <p>
     * <b>Important:</b> Note that the scheduled job won't be automatically
     * updated. You must call the {@link Job#updateScheduleState()} method when
     * you're done with your changes. If you don't, the old interval will remain
     * in effect until the server is next restarted.
     * 
     * @param   newTestInterval the strictly positive interval in seconds at 
     *                          which the job is executed
     */
    public void setTestInterval(int newTestInterval) {

        if (1 > newTestInterval) {
            throw new IllegalArgumentException(
                   "Test interval must be strictly positive.");
        }

        this.testInterval = newTestInterval;
    }



    /**
     * Gets the test interval for the job.
     * <p>
     * This property is ignored if the job isn't automatically executed.
     * 
     * @return  the interval in seconds at which the job is executed
     */
    public int getTestInterval() {
        return this.testInterval;
    }

    /**
     * Defines if this jobs last qurry response should be saved.
     * 
     * @param   newSaveResponse  <code>true</code> to allow reponse to be saved
     */
    public void setSaveResponse(boolean newSaveResponse) {
        this.saveResponse = newSaveResponse;
    }   
    
    /**
     * Gets the save response for the job.
     * <p>
     * This property is ignored if the job isn't automatically executed.
     * 
     * @return  <code>true</code> if job has to save last reposne
     */
    public boolean getSaveResponse() {
        return this.saveResponse;
    } 
    
    

    /**
     * Gets if the jobs queries should run simultaneous
     * 
	 * @return  <code>true</code> if jobs queries should be run 
	 * simultaneous
	 */
	public boolean isRunSimultaneous() {
		return runSimultaneous;
	}



	/**
	 * Sets if the jobs queries should be run simultaneous
	 * @param   runSimultaneous  <code>true</code> to allow jobs queries to be executed 
	 * in simultaneous mode with the same start time.
	 */
	public void setRunSimultaneous(boolean runSimultaneous) {
		this.runSimultaneous = runSimultaneous;
	}



	/**
     * Defines how much time an answer can take before considering the job as 
     * out of order.
     * 
     * @param   newTimeout  the strictly positive timeout in seconds
     */
    public void setTimeout(int newTimeout) {

        if (0 < newTimeout) {
            this.timeout = newTimeout;
        }

    }



    /**
     * Defines how much time an answer can take before considering that the job
     * is out of order.
     * 
     * @return  the timeout in seconds
     */
    public int getTimeout() {
        return this.timeout;
    }



    /**
     * Defines the web service's URL.
     * 
     * @param   newUrl  the web service's URL
     */
    public void setUrl(String newUrl) {
        this.url = newUrl;
    }



    /**
     * Gets the web service's URL.
     * 
     * @return  the web service's URL
     */
    public String getUrl() {
        return this.url;
    }



    /**
     * Checks the configuration's validity
     * <p>
     * A job configuration is valid if:
     * <ol>
     * <li>the job name is set and isn't empty</li>
     * <li>the HTTP method is set</li>
     * <li>the web service's URL is set and isn't empty</li>
     * <li>the web service type is set</li>
     * <li>the timeout is strictly positive</li>
     * <li>the test interval is strictly positive</li>
     * </ol>
     * <p>
     * Some operations aren't allowed for jobs whose configuration is invalid.
     * 
     * @return <code>true</code> if the configuration is valid<br>
     *         <code>false</code> otherwise
     * @see Job#isValid(boolean)
     */
    public boolean isValid() {
        
        final boolean mandatoryDefined = (
               !StringTools.isNullOrEmpty(this.getJobName()) 
               && null != this.getHttpMethod()
               //&& !StringTools.isNullOrEmpty(this.getUrl())
               && null != this.getServiceType()); 
        
        final boolean noInvalidValue = (0 < this.getTimeout() 
                                        && 0 < this.getTestInterval());

        return (mandatoryDefined && noInvalidValue);
    }



    /**
     * Returns the default configuration parameters.
     * 
     * @return  a map containing the default configuration parameters, with the
     *          parameter name as key
     */
    public static Map<String, JobDefaultParameter> getDefaultParams() {

        return JobDaoHelper.getJobDao().getDefaultParams();

    }
    
    
    
    /**
     * Defines one of this object's properties from a job defaults map.
     * 
     * @param   params  the map containing the job defaults  
     * @param   key     the map key for the value to assign
     * @param   setter  the setter method to invoke
     * @return          <code>true</code> if the value was successfully assigned
     */
    private boolean defineProperty(Map<String, JobDefaultParameter> params, 
                                   String key, Method setter) {
        
        if (params.containsKey(key)) {
            
            try {
                setter.invoke(this, params.get(key).getValue());           
                return true;
                
            } catch (InvocationTargetException e) {
                this.logger.error(String.format(
                        "Setter '%1$s' couldn't be accessed.", 
                        setter.getName()), e);
                
            } catch (IllegalAccessException e) {
                this.logger.error(String.format(
                        "Setter '%1$s' couldn't be accessed.", 
                        setter.getName()), e);
            }
        }
        
        return false;
    }



    /**
     * Sets this configuration's parameters to default.
     */
    private void setDefaultValues() {
        final Map<String, JobDefaultParameter> defaultParams 
            = JobConfiguration.getDefaultParams();

        try {
            this.defineProperty(defaultParams, "IS_PUBLIC", 
                       this.getClass().getMethod("setPublished", 
                                                 boolean.class));

            this.defineProperty(defaultParams, "IS_AUTOMATIC", 
                           this.getClass().getMethod("setAutomatic", 
                                                     boolean.class));
            this.defineProperty(defaultParams, "ALLOWS_REALTIME", 
                           this.getClass().getMethod("setRealTimeAllowed", 
                                                     boolean.class));
    
            this.defineProperty(defaultParams, "TRIGGERS_ALERTS", 
                           this.getClass().getMethod("setAlertsActivated", 
                                                     boolean.class));
            
            this.defineProperty(defaultParams, "SAVE_RESPONSE", 
                    this.getClass().getMethod("setSaveResponse", 
                                              boolean.class));
            
            this.defineProperty(defaultParams, "RUN_SIMULTANEOUS",
            		this.getClass().getMethod("setRunSimultaneous",
            								boolean.class));
            
            this.defineProperty(defaultParams, "BUSINESS_ERRORS", 
                           this.getClass().getMethod("setBizErrorChecked", 
                                                     boolean.class));
            
            this.defineProperty(defaultParams, "HTTP_ERRORS", 
                           this.getClass().getMethod("setHttpErrorChecked", 
                                                     boolean.class));

            this.defineProperty(defaultParams, "TEST_INTERVAL", 
                           this.getClass().getMethod("setTestInterval", 
                                                     int.class));

            this.defineProperty(defaultParams, "TIMEOUT", 
                           this.getClass().getMethod("setTimeout", 
                                                     int.class));

            this.defineProperty(defaultParams, "SLA_START_TIME", 
                           this.getClass().getMethod("setSlaStartTime", 
                                                     GregorianCalendar.class));

            this.defineProperty(defaultParams, "SLA_END_TIME", 
                           this.getClass().getMethod("setSlaEndTime", 
                        		   GregorianCalendar.class));

        } catch (NoSuchMethodException e) {
            this.logger.warn(String.format(
                    "An error occurred while setting the default values for " 
                    + "job #%1$s", this.getJob().getJobId()), 
                e);
                        
        }

    }



    /**
     * Creates a job configuration with default values
     * <p>
     * <i><b>Note:</b> The configuration returned by this function isn't valid.
     * You must first define the web service's URL, the service type and the
     * HTTP method used to poll the queries.</i>
     * 
     * @param aParentJob
     *            the job to be configured
     * @param name
     *            the job's name
     * @return the job configuration object
     */
    public static JobConfiguration createDefault(Job aParentJob, String name) {

        return new JobConfiguration(aParentJob, name);

    }

}
