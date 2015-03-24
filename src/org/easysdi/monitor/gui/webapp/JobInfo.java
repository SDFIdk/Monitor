package org.easysdi.monitor.gui.webapp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.deegree.framework.util.BooleanUtil;
import org.deegree.framework.util.DateUtil;
import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.biz.job.AbstractValueObject;
import org.easysdi.monitor.biz.job.HttpMethod;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.JobConfiguration;
import org.easysdi.monitor.biz.job.ServiceType;

/**
 * Holds and validates user input for job modification or creation.
 * <p>
 * All the properties can be set to <code>null</code> to indicate that the job's
 * corresponding parameter should not be altered. In consequence, all getters 
 * can return <code>null</code> as a legit (non-error) value. For non-mandatory
 * parameters (login or password, for instance), you must use an empty string
 * to indicate no value.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public final class JobInfo {

    /**
     * Format used for time strings (HH:mm:ss).
     */
    private static final String TIME_FORMAT = "HH:mm:ss";    
    
    
    private Boolean      alertActivated;
    private Boolean      automatic;
    private Boolean      bizErrorsChecked;
    private Boolean      httpErrorsChecked;
    private HttpMethod   httpMethod;
    private String       login;
    private String       name;
    private String       password;
    private Boolean      published;
    private Boolean      realTimeAllowed;
    private ServiceType  serviceType;
    private Calendar     slaEndTime;
    private Calendar     slaStartTime;
    private Integer      testInterval;
    private Integer      timeout;
    private String       url;
    private Boolean		 saveResponse;
    private Boolean      runSimultaneous;
    private int 		 testIntervalDown;
    private boolean      useTestIntervalDown;


    
    /**
     * Creates a new job user information object.
     */
    private JobInfo() {
        
    }
    
	/**
     * Gets the jobs test interval time when having errors
     * 
	 * @return  <code>true</code>
	 */
	public boolean getUseTestIntervalDown(){
		return this.useTestIntervalDown;
	}
	
	/**
	 * Sets if the jobs queries should use test interval when job is failing
	 * @param   useTestIntervalDown  <code>true</code>
	 */
	public void setUseTestIntervalDown(String useTestIntervalDown){
		this.useTestIntervalDown = BooleanUtil.parseBooleanStringWithNull(useTestIntervalDown);
	}
	
	/**
     * Gets the test interval time when job have errors
     * 
	 * @return  <code>int</code>
	 */
	public Integer getTestIntervalDown(){
		return this.testIntervalDown;
	}
	
	/**
	 * Sets the jobs queries test interval when job is failing
	 * @param   useTestIntervalDown  <code>int</code>
	 */
	public void setTestIntervalDown(String testIntervalDown){
		 try {
			 this.testIntervalDown = this.parseStrictlyPositiveInt(testIntervalDown);
		 } catch (NumberFormatException e) {
	            throw new IllegalArgumentException("Invalid down test interval");
	        } catch (IllegalArgumentException e) {
	            throw new IllegalArgumentException(
	                                     "Test interval must be strictly positive");
	        }catch (UserDataException e) {
	            throw new IllegalArgumentException("Invalid test interval value", 
                        e);
	        }
	}


    /**
     * Gets if alerts should be created by the job. 
     * 
     * @return  <ul>
     *          <li><code>true</code> if the job should create alerts<br></li>
     *          <li><code>false</code> if it shouldn't</li>
     *          <li><code>null</code> if the job's alerts creation parameter 
     *          shouldn't be changed</li>
     *          </ul>
     */
    private Boolean isAlertActivated() {
        return this.alertActivated;
    }



    /**
     * Gets if the job should be automatically executed.
     * 
     * @return  <ul>
     *          <li><code>true</code> if the job should be automatically 
     *          executed</li>
     *          <li><code>false</code> if it shouldn't</li>
     *          <li><code>null</code> if the job's automatic execution parameter
     *          shouldn't be changed</li>
     *          </ul>
     */
    private Boolean isAutomatic() {
        return this.automatic;
    }



    /**
     * Gets if the business errors should be checked.
     * 
     * @return  <ul>
     *          <li><code>true</code> if the job should check for business 
     *          errors</li>
     *          <li><code>false</code> if it shouldn't</li>
     *          <li><code>null</code> if the job's business errors parameter 
     *          shouldn't be changed</li>
     *          </ul>
     */
    private Boolean isBizErrorsChecked() {
        return this.bizErrorsChecked;
    }



    /**
     * Gets if the connection errors should be checked.
     * 
     * @return  <ul>
     *          <li><code>true</code> if the job should check for connection 
     *          errors</li>
     *          <li><code>false</code> if it shouldn't</li>
     *          <li><code>null</code> if the job's connection errors parameter 
     *          shouldn't be changed</li>
     *          </ul>
     */
    private Boolean isHttpErrorsChecked() {
        return this.httpErrorsChecked;
    }



    /**
     * Gets the new HTTP method to be used to poll the job.
     * 
     * @return  <ul>
     *          <li>the job's new HTTP method</li>
     *          <li><code>null</code> if the job's HTTP method parameter 
     *          shouldn't be changed</li>
     *          </ul>
     */
    private HttpMethod getHttpMethod() {
        return this.httpMethod;
    }



    /**
     * Gets the login that should be used to poll the job.
     * 
     * @return  <ul>
     *          <li>the login used to poll the job</li>
     *          <li>an empty string if no authentification should be used</li>
     *          <li><code>null</code> if the job's login parameter shouldn't be
     *          changed</li>
     *          </ul>
     */
    private String getLogin() {
        return this.login;
    }



    /**
     * Gets the job's new name.
     * 
     * @return  <ul>
     *          <li>the job's new name</li>
     *          <li><code>null</code> if the job's name shouldn't be 
     *          changed</li>
     *          </ul>
     */
    private String getName() {
        return this.name;
    }



    /**
     * Gets the password that should be used to poll the job.
     * 
     * @return  <ul>
     *          <li>the password used to poll the job</li>
     *          <li>an empty string if no authentification should be used</li>
     *          <li><code>null</code> if the job's password parameter shouldn't 
     *          be changed</li>
     *          </ul>
     */
    private String getPassword() {
        return this.password;
    }



    /**
     * Gets if the job should be publicly accessible.
     * 
     * @return  <ul>
     *          <li><code>true</code> if the job should be accessible to all
     *          users</li>
     *          <li><code>false</code> if it shouldn't</li>
     *          <li><code>null</code> if the job's public access parameter 
     *          shouldn't be changed</li>
     *          </ul>
     */
    private Boolean isPublished() {
        return this.published;
    }

    /**
     * Gets the save response for the job.
     * <p>
     * This property is ignored if the job isn't automatically executed.
     * 
     * @return <code>true</code> if job has to save last reposne
     */
    private Boolean getSaveResponse() {
        return this.saveResponse;
    } 


    /**
     * Gets if real-time execution of the job should be allowed.
     * 
     * @return  <ul>
     *          <li><code>true</code> if the job should be executable on 
     *          demand</li>
     *          <li><code>false</code> if it shouldn't</li>
     *          <li><code>null</code> if the job's real-time execution parameter
     *          shouldn't be changed</li>
     *          </ul>
     */
    private Boolean isRealTimeAllowed() {
        return this.realTimeAllowed;
    }



    /**
     * Gets the new web service type used by the job.
     * 
     * @return  <ul>
     *          <li>the job's new web service type</li>
     *          <li><code>null</code> if the job's service type shouldn't be 
     *          changed</li>
     *          </ul>
     */
    private ServiceType getServiceType() {
        return this.serviceType;
    }



    /**
     * Gets the end of the validated time (according to the SLA).
     * 
     * @return  <ul>
     *          <li>the time when the SLA validated time ends. (The date part 
     *          must not be considered.)</li>
     *          <li><code>null</code> if the job's SLA end time shouldn't be 
     *          changed</li>
     *          </ul>
     */
    private Calendar getSlaEndTime() {
        return this.slaEndTime;
    }



    /**
     * Gets the start of the validated time (according to the SLA).
     * 
     * @return  <ul>
     *          <li>the time when the SLA validated time kicks in. (The date 
     *          part must not be considered.)</li>
     *          <li><code>null</code> if the job's SLA start time shouldn't be 
     *          changed</li>
     *          </ul>
     */
    private Calendar getSlaStartTime() {
        return this.slaStartTime;
    }



    /**
     * Gets the new interval between job automatic executions. 
     * 
     * @return  <ul>
     *          <li>the number of seconds between two automatic executions</li>
     *          <li><code>null</code> if the job's automatic execution interval 
     *          shouldn't be changed</li>
     *          </ul>
     */
    private Integer getTestInterval() {
        return this.testInterval;
    }



    /**
     * Gets the new timeout before a job query is deemed unresponsive.
     * 
     * @return  <ul>
     *          <li>the number of seconds that a job gets to answer</li>
     *          <li><code>null</code> if the job's timeout shouldn't be 
     *          changed</li>
     *          </ul>
     */
    private Integer getTimeout() {
        return this.timeout;
    }



    /**
     * Gets the new job's URL.
     * 
     * @return  <ul>
     *          <li>the URL for the polled web service</li>
     *          <li><code>null</code> if the job's URL shouldn't be changed</li>
     *          </ul>
     */
    private String getUrl() {
        return this.url;
    }


    
    /**
     * Parses a string for an integer greater than 0.
     * 
     * @param   intString                   a string containing a strictly
     *                                      positive integer.
     * @return                              <ul>
     *                                      <li>the strictly positive integer
     *                                      found in the string</li>
     *                                      <li><code>null</code> if the string
     *                                      was null or empty
     *                                      </ul>
     * @throws  UserDataException           <ul>
     *                                      <li>no integer could be made out of 
     *                                      the string</li>
     *                                      <li>the parsed integer isn't 
     *                                      strictly positive</li>
     *                                      </ul>
     * @see     Integer#parseInt(String)
     */
    private Integer parseStrictlyPositiveInt(String intString) 
        throws UserDataException {
        
        Integer intValue;

        if (StringTools.isNullOrEmpty(intString)) {
            intValue = null;
        } else {
            
            try {
                intValue = Integer.parseInt(intString);
            
            } catch (NumberFormatException e) {
                throw new UserDataException(
                       "The string format isn't valid.", e);
            }

            if (1 > intValue) {
                throw new UserDataException(
                               "Value must be a strictly positive integer.");
            }
        }

        return intValue;
    }



    /**
     * Parses a string for a time value.
     * 
     * @param   timeString      a string containing a time value
     * @return                  <ul>
     *                          <li>a calendar containing the time found in the 
     *                          string. (The date part should be ignored.)</li>
     *                          <li><code>null</code> if the string was null or
     *                          empty</li>
     *                          </ul>
     * @throws  ParseException  no time could be made out of the string
     * @see                     JobInfo#TIME_FORMAT
     */
    private Calendar parseTimeString(String timeString) throws ParseException {

        if (StringTools.isNullOrEmpty(timeString)) {
            return null;
        }
        
        final SimpleDateFormat timeFormat 
            = new SimpleDateFormat(JobInfo.TIME_FORMAT);

        return DateUtil.dateToCalendar(timeFormat.parse(timeString));
    }



    /**
     * Defines if the job should create alerts.
     * 
     * @param   alertString <ul>
     *                      <li><code>"true"</code> if the job should create 
     *                      alerts</li>
     *                      <li><code>"false"</code> if it shouldn't</li>
     *                      <li><code>null</code> if the job's alert 
     *                      creation parameter shouldn't be changed</li>
     *                      </ul>
     */
    private void setAlertActivated(String alertString) {
        this.alertActivated 
            = BooleanUtil.parseBooleanStringWithNull(alertString);
    }
    

    /**
     * Defines if the job should be executed automatically.
     * 
     * @param   automaticString <ul>
     *                          <li><code>"true"</code> if the job should be 
     *                          executed automatically</li>
     *                          <li><code>"false"</code> if it shouldn't</li>
     *                          <li><code>null</code> if the job's automatic
     *                          execution parameter shouldn't be changed</li>
     *                          </ul>   
     */
    private void setAutomatic(String automaticString) {
        this.automatic 
            = BooleanUtil.parseBooleanStringWithNull(automaticString);
    }



    /**
     * Defines if business errors should be checked.
     * 
     * @param bizErrorsString   <ul>
     *                          <li><code>"true"</code> if the job should check
     *                          for business errors</li>
     *                          <li><code>"false"</code> if it shouldn't</li>
     *                          <li><code>null</code> if the job's business 
     *                          errors check parameter shouldn't be changed</li>
     *                          </ul>
     */
    private void setBizErrorsChecked(String bizErrorsString) {
        this.bizErrorsChecked 
            = BooleanUtil.parseBooleanStringWithNull(bizErrorsString);
    }


    /**
     * Defines if HTTP errors should be checked.
     * 
     * @param   httpErrorsString    <ul>
     *                              <li><code>"true"</code> if the job should 
     *                              create alerts</li>
     *                              <li><code>"false"</code> if it 
     *                              shouldn't</li>
     *                              <li><code>null</code> if the job's HTTP 
     *                              errors check parameter shouldn't be 
     *                              changed</li>
     *                              </ul>
     */
    private void setHttpErrorsChecked(String httpErrorsString) {
        this.httpErrorsChecked 
            = BooleanUtil.parseBooleanStringWithNull(httpErrorsString);
    }



    /**
     * Defines the job's new HTTP method.
     * 
     * @param   httpMethodName  the name of the HTTP method used to poll the job
     */
    @SuppressWarnings("unused")
    private void setHttpMethod(String httpMethodName) {

        if (StringTools.isNullOrEmpty(httpMethodName)) {
            throw new IllegalArgumentException("HTTP Method must be defined");
        }

        final HttpMethod methodObject = HttpMethod.getObject(httpMethodName);

        if (null == methodObject) {
            throw new IllegalArgumentException("Unknown HTTP Method");
        }

        this.httpMethod = methodObject;
    }



    /**
     * Defines the user name used to poll the job.
     * 
     * @param   newLogin    <ul>
     *                      <li>the user name to authentificate to</li>
     *                      <li>an empty string if no authentication should be
     *                      take place when pooling the job</li>
     *                      <li><code>null</code> if the job's login parameter 
     *                      shouldn't be changed</li>
     *                      </ul>
     */
    private void setLogin(String newLogin) {
        this.login = newLogin;
    }



    /**
     * Defines a new job name.
     * 
     * @param   newName the new name for the job
     */
    @SuppressWarnings("unused")
    private void setName(String newName) {

        if (StringTools.isNullOrEmpty(newName)) {
            throw new IllegalArgumentException("Name must be defined");
        }

        this.name = newName;
    }



    /**
     * Defines the new password to be used to poll the job.
     * 
     * @param   newPassword <ul>
     *                      <li>the new password to use for the 
     *                      authentication</li>
     *                      <li><code>null</code> if the job's HTTP errors check
     *                      parameter shouldn't be changed</li>
     *                      </ul>
     */
    private void setPassword(String newPassword) {
        this.password = newPassword;
    }

    

    /**
     * Gets the jobs queries runs in simultaneous mode.
     * <p>
     * This property is ignored if the job isn't automatically executed.
     * 
     * @return <code>true</code> if job has to save last reposne
     */
	private Boolean getRunSimultaneous() {
		return runSimultaneous;
	}



    /**
     * Defines if this jobs queries should be executed in 
     * a simultaneous mode
     * 
     * @param runSimultaneous <ul>
     *                          <li><code>"true"</code> if this jobs queries should 
     *                          be executed in 
     * 							a simultaneous mode</li>
     *                          <li><code>"false"</code> if it shouldn't</li>
     *                          </ul>
     */
	private void setRunSimultaneous(String runSimultaneous) {
		this.runSimultaneous = BooleanUtil.parseBooleanStringWithNull(runSimultaneous);
	}



	/**
     * Defines if the job should be publicly accessible.
     * 
     * @param   publishedString <ul>
     *                          <li><code>"true"</code> if the job should 
     *                          be accessible to all users</li>
     *                          <li><code>"false"</code> if it shouldn't</li>
     *                          <li><code>null</code> if the job's public access
     *                          parameter shouldn't be changed</li>
     *                          </ul>
     */
    private void setPublished(String publishedString) {
        this.published 
            = BooleanUtil.parseBooleanStringWithNull(publishedString);
    }

    /**
     * Defines if this jobs last qurry response should be saved.
     * 
     * @param saveResponseString <ul>
     *                          <li><code>"true"</code> if the job response should 
     *                          be saved</li>
     *                          <li><code>"false"</code> if it shouldn't</li>
     *                          <li><code>null</code> ??</li>
     *                          </ul>
     */
    private void setSaveResponse(String saveResponseString) {
        this.saveResponse = BooleanUtil.parseBooleanStringWithNull(saveResponseString);
    }
   

    /**
     * Defines if the job can be executed on demand.
     * 
     * @param   realTimeString  <ul>
     *                          <li><code>"true"</code> if the job should allow
     *                          on-demand execution</li>
     *                          <li><code>"false"</code> if it shouldn't</li>
     *                          <li><code>null</code> if the job's HTTP errors 
     *                          check parameter shouldn't be changed</li>
     *                          </ul>
     */
    private void setRealTimeAllowed(String realTimeString) {
        this.realTimeAllowed 
            = BooleanUtil.parseBooleanStringWithNull(realTimeString);
    }



    /**
     * Defines the type of web service polled by the job.
     * 
     * @param   serviceTypeName the web service type name
     */
    @SuppressWarnings("unused")
    private void setServiceType(String serviceTypeName) {

        if (StringTools.isNullOrEmpty(serviceTypeName)) {
            throw new IllegalArgumentException("Service type must be defined");
        }

        final ServiceType typeObject = ServiceType.getObject(serviceTypeName);

        if (null == typeObject) {
            throw new IllegalArgumentException("Unknown service type");
        }

        this.serviceType = typeObject;
    }



    /**
     * Defines when the validated time (according to the SLA) terminates.
     * 
     * @param   slaEndTimeString    <ul>
     *                              <li>a string containing the time that the 
     *                              SLA validated time ends at</li>
     *                              <li><code>null</code> if the job's SLA 
     *                              validated time end shouldn't be changed</li>
     *                              </ul>
     * @see     JobInfo#TIME_FORMAT
     */
    private void setSlaEndTime(String slaEndTimeString) {

        try {
            this.slaEndTime = this.parseTimeString(slaEndTimeString);

        } catch (ParseException e) {
            throw new IllegalArgumentException(
                   "Invalid or badly formatted SLA end time.");
        }
    }



    /**
     * Defines when the validated time (according to the SLA) kicks in.
     * 
     * @param   slaStartTimeString  <ul>
     *                              <li>a string containing the time that the 
     *                              SLA validated time starts at</li>
     *                              <li><code>null</code> if the job's SLA 
     *                              validated time start shouldn't be changed
     *                              </li>
     *                              </ul>
     * @see     JobInfo#TIME_FORMAT
     */
    private void setSlaStartTime(String slaStartTimeString) {

        try {
            this.slaStartTime = this.parseTimeString(slaStartTimeString);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                   "Invalid or badly formatted SLA start time.");
        }
    }



    /**
     * Defines the interval between two automatic executions of the job.
     * 
     * @param   intervalString  <ul>
     *                          <li>a string containing the interval in seconds
     *                          between two automatic executions</li>
     *                          <li><code>null</code> if the job's test interval
     *                          shouldn't be changed</li>
     *                          </ul>
     */
    private void setTestInterval(String intervalString) {

        try {
            this.testInterval = this.parseStrictlyPositiveInt(intervalString);

        } catch (UserDataException e) {
            throw new IllegalArgumentException("Invalid test interval value", 
                                               e);

        }
    }



    /**
     * Defines how much time can pass before a job is deemed unresponsive.
     * 
     * @param   timeoutString   <ul>
     *                          <li>a string containing the number of seconds
     *                          that a query gets to respond</li>
     *                          <li><code>null</code> if the job's timeout 
     *                          shouldn't be changed</li>
     *                          </ul>
     */
    private void setTimeout(String timeoutString) {

        try {
            this.timeout = this.parseStrictlyPositiveInt(timeoutString);

        } catch (UserDataException e) {
            throw new IllegalArgumentException("Invalid timeout value", e);

        }

    }



    /**
     * Defines the polled web service's URL.
     * 
     * @param   newUrl  the URL of the web service polled by the job
     */
    @SuppressWarnings("unused")
    private void setUrl(String newUrl) {

        if (StringTools.isNullOrEmpty(newUrl)) {
            throw new IllegalArgumentException("URL must be defined");
        }

        this.url = newUrl;
    }



    /**
     * Creates a job info object from a servlet request's parameters.
     * 
     * @param   requestParams       a map containing the request parameters
     * @param   enforceMandatory    <code>true</code> to fail if a mandatory
     *                              property isn't set
     *                              <p>
     *                              Basically, you should set this parameter 
     *                              to <code>true</code> if you intend to create
     *                              a new job and to <code>false</code> to 
     *                              modify an existing one.
     * @return                      <ul>
     *                              <li>the job info if it has been successfully
     *                              created</li>
     *                              <li><code>null</code> otherwise</li>
     *                              </ul>
     * @throws  MandatoryParameterException a null value was assigned to a 
     *                                      mandatory parameter 
     */
    public static JobInfo createFromParametersMap(
            Map<String, String> requestParams, Boolean enforceMandatory) 
        throws MandatoryParameterException {
        
        final JobInfo newJobInfo = new JobInfo();
        newJobInfo.setAlertActivated(requestParams.get("triggersAlerts"));
        newJobInfo.setAutomatic(requestParams.get("isAutomatic"));
        newJobInfo.setBizErrorsChecked(requestParams.get("bizErrors"));
        newJobInfo.setHttpErrorsChecked(requestParams.get("httpErrors"));

        JobInfo.copyValueToConfig(newJobInfo, "setHttpMethod", 
                                  requestParams.get("httpMethod"), 
                                  enforceMandatory, "httpMethod", false);

        newJobInfo.setLogin(requestParams.get("login"));

        JobInfo.copyValueToConfig(newJobInfo, "setName", 
                                  requestParams.get("name"), 
                                  enforceMandatory, "name", false);

        newJobInfo.setPassword(requestParams.get("password"));
        newJobInfo.setPublished(requestParams.get("isPublic"));
        newJobInfo.setSaveResponse(requestParams.get("saveResponse"));
        newJobInfo.setRunSimultaneous(requestParams.get("runSimultaneous"));
        newJobInfo.setRealTimeAllowed(requestParams.get("allowsRealTime"));

        JobInfo.copyValueToConfig(newJobInfo, "setServiceType", 
                                  requestParams.get("serviceType"), 
                                  enforceMandatory, "serviceType", false);

        newJobInfo.setSlaEndTime(requestParams.get("slaEndTime"));
        newJobInfo.setSlaStartTime(requestParams.get("slaStartTime"));
        newJobInfo.setTestInterval(requestParams.get("testInterval"));
        newJobInfo.setTimeout(requestParams.get("timeout"));
        newJobInfo.setTestIntervalDown(requestParams.get("testIntervalDown"));
        newJobInfo.setUseTestIntervalDown(requestParams.get("useTestIntervalDown"));
        JobInfo.copyValueToConfig(newJobInfo, "setUrl", 
                                  requestParams.get("url"), false,
                                  "url", false);

        return newJobInfo;
    }



    /**
     * Modify a job's properties from this object's information.
     * 
     * @param   job                 the job to modify
     * @param   enforceMandatory    <code>true</code> to fail if a mandatory
     *                              property isn't set
     *                              <p>
     *                              Basically, you should set this parameter to
     *                              <code>true</code> if you call this method
     *                              while creating a new job and to <code>false
     *                              </code> if the job is an already existing 
     *                              one.
     * @return                      <code>true</code> if the modification was
     *                              successful
     * @throws  MandatoryParameterException a null value was assigned to a 
     *                                      mandatory parameter 
     */
    public boolean modifyJobParams(Job job, Boolean enforceMandatory) 
        throws MandatoryParameterException {

        if (null == job) {
            throw new IllegalArgumentException("Job can't be null");
        }

        this.setJobValues(job, enforceMandatory);

        if (job.persist()) {            
            job.updateScheduleState();
           // System.out.println("Job:"+job.getConfig().getJobName()+" : "+job.getJobId()+" : "+job.getConfig().isAutomatic());
            return true;
        }

        return false;
    }
    
    

    /**
     * Copies the values from this object to a given job.
     * <p>
     * Null values are ignored, unless the parameter is mandatory. 
     * 
     * @param   job                 the job to modify
     * @param   enforceMandatory    <code>true</code> to fail if a mandatory
     *                              property isn't set
     *                              <p>
     *                              Basically, you should set this parameter to
     *                              <code>true</code> if you call this method
     *                              while creating a new job and to <code>false
     *                              </code> if the job is an already existing 
     *                              one.
     * @throws MandatoryParameterException  a null value was assigned to a
     *                                      mandatory parameter 
     */
    private void setJobValues(Job job, boolean enforceMandatory) 
        throws MandatoryParameterException {
        
        final JobConfiguration config = job.getConfig();

        
        JobInfo.copyValueToConfig(config, "setServiceType", 
                                  this.getServiceType(), enforceMandatory, 
                                  "serviceType", true);
        
        JobInfo.copyValueToConfig(config, "setHttpMethod", this.getHttpMethod(),
                                  enforceMandatory, "httpMethod", true);
        
        JobInfo.copyValueToConfig(config, "setUrl", this.getUrl(), 
                                  false, "url", false);

        JobInfo.copyValueToConfig(config, "setAlertsActivated", 
                                  this.isAlertActivated(), false, 
                                  "triggersAlerts", false);
        
        JobInfo.copyValueToConfig(config, "setAutomatic", this.isAutomatic(), 
                                  false, "isAutomatic", false);
        

        JobInfo.copyValueToConfig(config, "setBizErrorChecked", 
                                  this.isBizErrorsChecked(), false, "bizErrors",
                                  false);
        
        JobInfo.copyValueToConfig(config, "setHttpErrorChecked", 
                                  this.isHttpErrorsChecked(), false, 
                                  "httpErrors", false);
        
        JobInfo.copyValueToConfig(config, "setLogin", this.getLogin(), false, 
                                  "login", false);

        JobInfo.copyValueToConfig(config, "setJobName", this.getName(), 
                                  enforceMandatory, "name", false);

        JobInfo.copyValueToConfig(config, "setPassword", this.getPassword(), 
                                  false, "password", false);
        
        JobInfo.copyValueToConfig(config, "setPublished", this.isPublished(), 
                                  false, "isPublic", false);        
        
        JobInfo.copyValueToConfig(config, "setSaveResponse", this.getSaveResponse(), 
                false, "saveResponse", false);
        
        JobInfo.copyValueToConfig(config,"setRunSimultaneous",this.getRunSimultaneous(),
        		false,"runSimultaneous",false);
        
        JobInfo.copyValueToConfig(config, "setRealTimeAllowed", 
                                  this.isRealTimeAllowed(), false, 
                                  "allowsRealTime", false);
        
        JobInfo.copyValueToConfig(config, "setSlaEndTime", this.getSlaEndTime(),
                                  false, "slaEndTime", false);

        JobInfo.copyValueToConfig(config, "setSlaStartTime", 
                                  this.getSlaStartTime(), false, "slaStartTime",
                                  false);

        JobInfo.copyValueToConfig(config, "setTestInterval", 
                                  this.getTestInterval(), false, "testInterval",
                                  false);

        JobInfo.copyValueToConfig(config, "setTimeout", this.getTimeout(), 
                                  false, "timeout", false);
        JobInfo.copyValueToConfig(config,"setTestIntervalDown"
        		, this.getTestIntervalDown(), false, "testIntervalDown", false);
        JobInfo.copyValueToConfig(config,"setUseTestIntervalDown"
        		, this.getUseTestIntervalDown(), false, "useTestIntervalDown", false);
    }
        
    
    
    /**
     * Copies a value to the property of an object.
     * <p>
     * <i><b>Note:</b> Even if the property isn't mandatory, no assignment will
     * take place if the value is null, because it means per design that the 
     * parameter shouldn't be modified. The only difference caused by setting 
     * mandatory parameter to true is that a null value will throw an exception.
     * </i>
     * 
     * @param   targetObject        the object containing the property to update
     * @param   setterName          the name of the setter for the property to 
     *                              copy the value to
     * @param   value               the value to assign to the property
     * @param   mandatory           <code>true</code> if a null value isn't 
     *                              acceptable for the property
     * @param   paramName           the name of the parameter that the value is
     *                              taken from
     * @param   isParamValueObject  <code>true</code> if the parameter is a 
     *                              value object type
     * @throws  MandatoryParameterException the parameter is mandatory, but the
     *                                      passed value is null 
     */
    private static void copyValueToConfig(Object targetObject, 
                                          String setterName, Object value, 
                                          boolean mandatory, String paramName, 
                                          boolean isParamValueObject)
        throws MandatoryParameterException {
        
        final boolean isStringValid 
            = !(value instanceof String) 
                    || StringUtils.isNotBlank((String) value); 
             
        if (null != value && isStringValid) {
            
            final Object assignedValue = (isParamValueObject) 
                                       ? ((AbstractValueObject) value).getName()
                                       : value;
                                       
            final Class<?> valueClass 
                = JobInfo.getValueParameterClass(assignedValue);
            JobInfo.callSetter(targetObject, setterName, valueClass, 
                               assignedValue);
            
        } else if (mandatory) {
            throw new MandatoryParameterException(paramName);
        }
    }
    
    
    
   /**
    * Gets the parameter class for the given value.
    *  
    * @param    value   the value to be passed as a parameter
    * @return           the parameter class for the passed value
    */
    private static Class<?> getValueParameterClass(Object value) {
        Class<?> valueClass;
        
        if (null != value) {
            valueClass = value.getClass();
            
            if (Boolean.class.equals(valueClass)) {
                valueClass = boolean.class;
            } else if (Integer.class.equals(valueClass)) {
                valueClass = int.class;
            } else if (Long.class.equals(valueClass)) {
                valueClass = long.class;
            } else if (Float.class.equals(valueClass)) {
                valueClass = float.class;
            } else if (Double.class.equals(valueClass)) {
                valueClass = double.class;
            }
        } else {
            valueClass = Object.class;
        }
        
        return valueClass;
    }



    /**
     * Calls a setter on an object.
     * 
     * @param   targetObject    the object to update
     * @param   setterName      the name of the setter for the property to copy 
     *                          the value to
     * @param   valueType       the type of the value to copy
     * @param   value           the value to assign to the config property
     */
    private static void callSetter(Object targetObject, String setterName,
                                   Class<?> valueType, Object value) {
        
        Method setter;
        
        try {
            setter = targetObject.getClass().getDeclaredMethod(setterName, 
                                                               valueType);
            setter.setAccessible(targetObject instanceof JobInfo);
            setter.invoke(targetObject, value);
        } catch (SecurityException e1) {
            JobInfo.logError("Unable to get setter : ", e1);
            
        } catch (NoSuchMethodException e1) {
            JobInfo.logError("Unable to get setter : ", e1);

        } catch (IllegalAccessException e) {
            JobInfo.logError("Unable to call setter : ", e);
            
        } catch (InvocationTargetException e) {
            JobInfo.logError("Unable to call setter : ", e);
        }
    }
        
    
    
    /**
     * Logs an exception. 
     * 
     * @param   message     the message to introduce the exception. It shouldn't
     *                      be the exception message, which is printed 
     *                      afterwards
     * @param   e           the exception to log
     */
    private static void logError(String message, Exception e) {
        Logger.getLogger(JobInfo.class).error(message, e);
    }
    
   

    /**
     * Creates a new job from this object's information.
     * 
     * @return  <ul>
     *          <li>the job if it has been created successfully</li>
     *          <li><code>null</code> otherwise</li>
     *          </ul>
     * @throws  MandatoryParameterException a null value was assigned to a 
     *                                      mandatory parameter 
     */
    public Job createJob() throws MandatoryParameterException {
        final Job newJob = Job.createDefault(this.getName());

        if (this.modifyJobParams(newJob, true)) {
            return newJob;
        }

        return null;
    }
}
