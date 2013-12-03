package org.easysdi.monitor.biz.alert;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.dat.dao.ActionDaoHelper;

/**
 * Represents an action to be triggered when an alert is raised.
 * 
 * @author  Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 */
public abstract class AbstractAction {

    private long       actionId;
    private String     language;
    private Job        parentJob;
    private String     target;
    private ActionType type;



    /**
     * No-argument constuctor, used by the persistance mechanism to instantiate
     * subclasses.
     */
    protected AbstractAction() {

    }



    /**
     * Creates an action for a given job.
     * 
     * @param job
     *            the job attached to this action
     * @param actionTarget
     *            the target of the action (e-mail addresses list, for instance)
     */
    public AbstractAction(Job job, String actionTarget) {
        this.setParentJob(job);
        this.setTarget(actionTarget);
    }



    /**
     * Executes this action.
     * 
     * @param alert the alert responsible for triggering the action
     */
    public abstract void trigger(Alert alert);



    /**
     * Defines the action's identifier.
     * <p>
     * <i><b>Note:</b> This method shouldn't be called directly. The identifier
     * should be defined through the persistance mechanism.</i>
     * 
     * @param newActionId  a long uniquely identifying the action
     */
    protected void setActionId(long newActionId) {

        if (1 > newActionId) {
            throw new IllegalArgumentException("Invalid action identifier");
        }

        this.actionId = newActionId;
    }



    /**
     * Gets the action's identifier.
     * 
     * @return the long uniquely identifying the action
     */
    public long getActionId() {
        return this.actionId;
    }



    /**
     * Defines in which language the action must be carried.
     * 
     * @param newLanguage
     *            a two-letter language code (for example "en" for english or
     *            "fr" for french). If no resource file is defined for the given
     *            language, the action will fall back to the default one.
     */
    public void setLanguage(String newLanguage) {
        
        if (null == newLanguage) {
            this.language = null;
            return;
        }

        if (2 != newLanguage.length()) {
            throw new IllegalArgumentException(
                    "Language must be a two character code");
        }
        
        this.language = newLanguage.toLowerCase();
    }



    /**
     * Gets the language in which the action must be carried.
     * 
     * @return the two-letter code for the language
     */
    public String getLanguage() {
        return this.language;
    }



    /**
     * Defines the action's target.
     * <p>
     * The exact content expected for this property depends on the subclass. It
     * could be a list of e-mail addresses or a file path, for instance.
     * 
     * @param newTarget
     *            a string containing the target(s) for this action
     */
    public void setTarget(String newTarget) {
        this.target = newTarget;
        this.processTargetSpecific(newTarget);
    }
    
    
    /**
     * Executes action-specific operations on the target.
     * 
     * @param actionTarget    the target to process
     */
    protected void processTargetSpecific(String actionTarget) {
        
    }



    /**
     * Gets the action's target.
     * <p>
     * The exact content expected for this property depends on the subclass. It
     * could be a list of e-mail addresses or a file path, for instance.
     * 
     * @return the target(s) for this action
     */
    public String getTarget() {
        return this.target;
    }



    /**
     * Attaches this action to a job.
     * <p>
     * If the job triggers alerts, it will the trigger this action.
     * 
     * @param newParentJob
     *            the job which this action shall be attached to
     */
    protected void setParentJob(Job newParentJob) {

        if (null == newParentJob || !newParentJob.isValid(false)) {
            throw new IllegalArgumentException(
                    "Parent job must be a valid one.");
        }

        this.parentJob = newParentJob;
    }



    /**
     * Gets the job responsible for triggering this action.
     * 
     * @return the job which this action is attached to
     */
    public Job getParentJob() {
        return this.parentJob;
    }



    /**
     * Defines the type of this action.
     * <p>
     * <i><b>Note:</b> You shouldn't use this method directly. The <code>type
	 * </code> parameter is intended for internal use.</i>
     * 
     * @param newType  the action type
     */
    public void setType(ActionType newType) {
        // TODO : Changing the type should be only be realizable through a new
        // instantiation
        this.type = newType;
    }



    /**
     * Gets the type of this action.
     * 
     * @return the action type
     */
    public ActionType getType() {
        return this.type;
    }



    /**
     * Saves this action through the persistance mechanism.
     * 
     * @return <code>true</code> if the object has been successfully 
     *         persisted<br>
     *         <code>false</code> otherwise
     */
    public boolean persist() {
        
        return ActionDaoHelper.getActionDao().persistAction(this);
        
    }



    /**
     * Loads an action from identifying strings.
     * 
     * @param jobIdString       a string identifying the job which the action is
     *                          attached to. It can be the job's identifier or 
     *                          name
     * @param actionIdString    a string containing the action's identifier
     * @return                  the action if it's been found<br>
     *                          <code>null</code> otherwise
     */
    public static AbstractAction getFromIdStrings(String jobIdString,
                    String actionIdString) {

        if (StringUtils.isBlank(jobIdString)) {
            throw new IllegalArgumentException(
                    "Job identifier must be defined");
        }

        if (StringUtils.isBlank(actionIdString)) {
            throw new IllegalArgumentException(
                   "Action identifier must be defined");
        }

        long actionId;

        try {
            actionId = Long.parseLong(actionIdString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Action identifier format is invalid");
        }

        final Job job = Job.getFromIdString(jobIdString);

        if (null == job) {
            return null;
        }

        return job.getActionById(actionId);
    }



    /**
     * Erases this action through the persistance mechanism.
     * 
     * @return <code>true</code> if this action has been successfully 
     *         deleted<br>
     *         <code>false</code> otherwise
     */
    public boolean delete() {

        if (ActionDaoHelper.getActionDao().deleteAction(this)) {
            this.parentJob.getActions().remove(this);
            return true;
        }

        return false;
    }
}
