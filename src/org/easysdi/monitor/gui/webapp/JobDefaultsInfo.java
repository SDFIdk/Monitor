package org.easysdi.monitor.gui.webapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.deegree.framework.util.BooleanUtil;
import org.deegree.framework.util.DateUtil;
import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.biz.job.JobDefaultParameter;

/**
 * Holds and validates user input for job default parameters modification.
 * <p>
 * All the properties can be set to <code>null</code> to indicate that the job's
 * corresponding parameter should not be altered. In consequence, all getters 
 * can return <code>null</code> as a legit (non-error) value. For non-mandatory
 * parameters, you must use an empty string to indicate no value.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class JobDefaultsInfo {

    private static final String TIME_FORMAT = "HH:mm:ss";



    private Boolean      alertsEnabled;
    private Boolean      automatic;
    private Boolean      bizErrorsEnabled;
    private Boolean      httpErrorsEnabled;
    private Boolean      published;
    private Boolean      realTimeAllowed;
    private Calendar     slaEndTime;
    private Calendar     slaStartTime;
    private Integer      testInterval;
    private Integer      timeout;
    private Boolean		 saveResponse;
    private Boolean      runSimultaneous;
    

    /**
     * Creates a new job default modification object.
     */
    private JobDefaultsInfo() {
        
    }

    
    
    /**
     * Defines if a job should trigger alerts by default.
     * 
     * @param   alertsEnabledString   "true" if new jobs should trigger alerts 
     *                                when their status change
     */
    private void setAlertsEnabled(String alertsEnabledString) {
        this.alertsEnabled 
            = BooleanUtil.parseBooleanStringWithNull(alertsEnabledString);
    }



    /**
     * Gets the new value for the alerts triggering.
     * 
     * @return  a boolean if the value must be changed, or<br>
     *          <code>null</code> otherwise
     */
    private Boolean getAlertsEnabled() {
        return this.alertsEnabled;
    }



    /**
     * Defines if a job should be automatically executed by default.
     * 
     * @param   automaticString   "true" if new jobs should be executed 
     *                            automatically
     */
    private void setAutomatic(String automaticString) {
        this.automatic 
            = BooleanUtil.parseBooleanStringWithNull(automaticString);
    }



    /**
     * Gets the new value for the automatic execution.
     * 
     * @return  a boolean if the value must be changed, or<br>
     *          <code>null</code> otherwise
     */
    private Boolean getAutomatic() {
        return this.automatic;
    }



    /**
     * Defines whether a job should catch business errors by default.
     * 
     * @param   bizErrorsEnabledString  "true" if new jobs should catch business
     *                                  errors
     */
    private void setBizErrorsEnabled(String bizErrorsEnabledString) {
        this.bizErrorsEnabled 
            = BooleanUtil.parseBooleanStringWithNull(bizErrorsEnabledString);
    }



    /**
     * Gets the new value for catching the business errors.
     * 
     * @return  a boolean if the value must be changed, or<br>
     *          <code>null</code> otherwise
     */
    private Boolean getBizErrorsEnabled() {
        return this.bizErrorsEnabled;
    }



    /**
     * Defines whether jobs should catch HTTP errors by default.
     * 
     * @param   httpErrorsEnabledString "true" if new jobs should catch HTTP 
     *                                  errors
     */
    private void setHttpErrorsEnabled(String httpErrorsEnabledString) {
        this.httpErrorsEnabled 
            = BooleanUtil.parseBooleanStringWithNull(httpErrorsEnabledString);
    }



    /**
     * Gets the new value for catching HTTP errors.
     * 
     * @return  a boolean if the value must be changed, or<br>
     *          <code>null</code> otherwise
     */
    private Boolean getHttpErrorsEnabled() {
        return this.httpErrorsEnabled;
    }



    /**
     * Gets whether the job should be publicly accessible by default.
     * 
     * @param publishedString   "true" if new jobs should be publicly accessible
     */
    private void setPublished(String publishedString) {
        this.published 
            = BooleanUtil.parseBooleanStringWithNull(publishedString);
    }



    /**
     * Gets the new value for the public accessibility.
     * 
     * @return  a boolean if the value must be changed, or<br>
     *          <code>null</code> otherwise
     */
    private Boolean getPublished() {
        return this.published;
    }

    /**
     * Gets whether the job response should be saved.
     * 
     * @param saveResponseString "true" if new job response should be saved.
     */
    private void setSaveResponse(String saveResponseString) {
        this.saveResponse 
            = BooleanUtil.parseBooleanStringWithNull(saveResponseString);
    }



    /**
     * Gets the new value for the saveResponse.
     * 
     * @return  a boolean if the value must be changed, or<br>
     *          <code>null</code> otherwise
     */
    private Boolean getSaveResponse() {
        return this.saveResponse;
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
     * Defines whether jobs can be executed on demand by default.
     * 
     * @param realTimeAllowedString "true" if new jobs can be executed on demand
     */
    private void setRealTimeAllowed(String realTimeAllowedString) {
        this.realTimeAllowed 
            = BooleanUtil.parseBooleanStringWithNull(realTimeAllowedString);
    }



    /**
     * Gets the new value for on-demand execution.
     * 
     * @return  a boolean if the value must be changed, or<br>
     *          <code>null</code> otherwise
     */
    private Boolean getRealTimeAllowed() {
        return this.realTimeAllowed;
    }



    /**
     * Defines the end time of the validity period as defined in the SLA.
     * 
     * @param   slaEndTimeString    the validity period end time (HH:mm:ss)
     */
    private void setSlaEndTime(String slaEndTimeString) {

        try {
            this.slaEndTime = this.parseTimeString(slaEndTimeString, 
                                                   JobDefaultsInfo.TIME_FORMAT);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid SLA end time format");
        }
    }



    /**
     * Gets the new value for the validity period end time as defined in the 
     * SLA.
     * 
     * @return  a calendar value if the end time must be changed, or<br>
     *          <code>null</code> otherwise
     */
    private Calendar getSlaEndTime() {
        return this.slaEndTime;
    }



    /**
     * Defines the start time of the validity period as defined in the SLA.
     * 
     * @param   slaStartTimeString  the validity period start time (HH:mm:ss)
     */
    private void setSlaStartTime(String slaStartTimeString) {

        try {
            this.slaStartTime 
                = this.parseTimeString(slaStartTimeString,
                                       JobDefaultsInfo.TIME_FORMAT);
            
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid SLA start time format");
        }
    }



    /**
     * Gets the new value for the validity period start time as defined in the 
     * SLA.
     * 
     * @return  a calendar value if the start time must be changed, or<br>
     *          <code>null</code> otherwise
     */
    private Calendar getSlaStartTime() {

        return this.slaStartTime;
    }



    /**
     * Defines how many seconds should elapse between two automatic executions 
     * of a job.
     * 
     * @param   testIntervalString  a string containing the new number of 
     *                              seconds. It must be strictly positive
     */
    private void setTestInterval(String testIntervalString) {

        try {
            this.testInterval 
                = this.parseStrictlyPositiveInt(testIntervalString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid test interval");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                                     "Test interval must be strictly positive");
        }
    }



    /**
     * Gets the new value for the interval between two job executions.
     * 
     * @return  an integer if the value must be changed, or<br>
     *          <code>null</code> otherwise
     */
    private Integer getTestInterval() {
        return this.testInterval;
    }



    /**
     * Defines the number of seconds that a job execution will wait for a 
     * response.
     * 
     * @param   timeoutString   a string containing the new timeout duration. It
     *                          must be strictly positive
     */
    private void setTimeout(String timeoutString) {

        try {
            this.timeout = this.parseStrictlyPositiveInt(timeoutString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid timeout");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                                           "Timeout must be strictly positive");
        }
    }



    /**
     * Gets the new value for a job execution timeout.
     * 
     * @return  an integer if the value must be changed, or<br>
     *          <code>null</code> otherwise
     */
    private Integer getTimeout() {
        return this.timeout;
    }



    //FIXME Sortir dans une classe utilitaire
    /**
     * Transforms a correctly formatted string into a strictly positive integer
     * (0 < x).
     * 
     * @param   intString   a string containing a strictly positive integer
     * @return              the strictly positive integer
     */
    private Integer parseStrictlyPositiveInt(String intString) {
        
        Integer intValue;

        if (StringTools.isNullOrEmpty(intString)) {
            intValue = null;
        } else {
            intValue = Integer.parseInt(intString);

            if (1 > intValue) {
                //FIXME Lancer une exception vérifiée
                throw new IllegalArgumentException(
                                  "Value must be a strictly positive integer.");
            }
        }

        return intValue;
    }



    //FIXME Sortir dans une classe utilitaire
    /**
     * Transforms a correctly formatted string into a time value.
     * 
     * @param   timeString          the string containing the time value
     * @param   timeFormatString    the expected time format
     * @return                      a calendar containing the time value
     * @throws  ParseException      the time string isn't correctly formatted
     */
    private Calendar parseTimeString(String timeString, String timeFormatString)
        throws ParseException {

        if (StringTools.isNullOrEmpty(timeString)) {
            return null;
        }
        
        final SimpleDateFormat timeFormat 
            = new SimpleDateFormat(timeFormatString);

        return DateUtil.dateToCalendar(timeFormat.parse(timeString));
    }


    /**
     * Creates a job modification object from a map of parameters.
     * 
     * @param   requestParams   a map containing the parameters that must be
     *                          modified
     * @return                  a job modification object
     */
    public static JobDefaultsInfo createFromParametersMap(
                                            Map<String, String> requestParams) {
        final JobDefaultsInfo jobDefaultsInfo = new JobDefaultsInfo();

        jobDefaultsInfo.setAlertsEnabled(requestParams.get("triggersAlerts"));
        jobDefaultsInfo.setAutomatic(requestParams.get("isAutomatic"));
        jobDefaultsInfo.setBizErrorsEnabled(requestParams.get("bizErrors"));
        jobDefaultsInfo.setHttpErrorsEnabled(requestParams.get("httpErrors"));
        jobDefaultsInfo.setPublished(requestParams.get("isPublic"));
        jobDefaultsInfo.setSaveResponse(requestParams.get("saveResponse"));
        jobDefaultsInfo.setRealTimeAllowed(requestParams.get("allowsRealTime"));
        jobDefaultsInfo.setSlaEndTime(requestParams.get("slaEndTime"));
        jobDefaultsInfo.setSlaStartTime(requestParams.get("slaStartTime"));
        jobDefaultsInfo.setTestInterval(requestParams.get("testInterval"));
        jobDefaultsInfo.setTimeout(requestParams.get("timeout"));
        jobDefaultsInfo.setRunSimultaneous(requestParams.get("runSimultaneous"));

        return jobDefaultsInfo;
    }



    /**
     * Changes the job defaults with the values of this object.
     * 
     * @param   defaultsMap a map containing the default parameter objects
     * @return              <code>true</code> if the modification was successful
     */
    public boolean updateDefaults(Map<String, JobDefaultParameter> defaultsMap) 
    {
        boolean success = true;

        success &= this.updateParam(defaultsMap.get("TRIGGERS_ALERTS"),
                                    this.getAlertsEnabled());
        success &= this.updateParam(defaultsMap.get("IS_AUTOMATIC"),
                                    this.getAutomatic());
        success &= this.updateParam(defaultsMap.get("BUSINESS_ERRORS"),
                                    this.getBizErrorsEnabled());
        success &= this.updateParam(defaultsMap.get("HTTP_ERRORS"),
                                    this.getHttpErrorsEnabled());
        success &= this.updateParam(defaultsMap.get("IS_PUBLIC"),
                                    this.getPublished());
        success &= this.updateParam(defaultsMap.get("SAVE_RESPONSE"),
                this.getSaveResponse());
        success &= this.updateParam(defaultsMap.get("ALLOWS_REALTIME"),
                                    this.getRealTimeAllowed());
        success &= this.updateParam(defaultsMap.get("SLA_START_TIME"),
                                    this.getSlaStartTime());
        success &= this.updateParam(defaultsMap.get("SLA_END_TIME"),
                                    this.getSlaEndTime());
        success &= this.updateParam(defaultsMap.get("TEST_INTERVAL"),
                                    this.getTestInterval());
        success &= this.updateParam(defaultsMap.get("TIMEOUT"), 
                                    this.getTimeout());
        success &= this.updateParam(defaultsMap.get("runSimultaneous"), 
        		this.getRunSimultaneous());

        return success;
    }



    /**
     * Changes the value of a job default.
     * 
     * @param   parameter       the job default to alter
     * @param   value           the new value for the job default
     * @return                  <code>true</code> if the value was successfully 
     *                          changed
     */
    private boolean updateParam(JobDefaultParameter parameter, 
                                Object value) {

        if (null != value && null != parameter) {
            parameter.setValue(value);

            return parameter.persist();
        }

        return true;
    }
}
