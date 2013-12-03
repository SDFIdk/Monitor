package org.easysdi.monitor.gui.webapp;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.alert.AbstractAction;
import org.easysdi.monitor.biz.alert.ActionType;
import org.easysdi.monitor.biz.alert.EmailAction;
import org.easysdi.monitor.biz.alert.RssAction;
import org.easysdi.monitor.biz.job.Job;

/**
 * Collects and validate information to create or modify an action.
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
public class ActionInfo {

    private String     language;
    private Job        parentJob;
    private String     target;
    private ActionType type;
    
    
    
    /**
     * Creates a new action information object.
     */
    private ActionInfo() {
        
    }



    /**
     * Defines in which language this action shall be carried.
     * 
     * @param   newLanguage    the new language of this action
     */
    private void setLanguage(String newLanguage) {

        if (StringUtils.isEmpty(newLanguage)) {
            this.language = null;

        } else {
            
            if (2 != newLanguage.length()) {
                throw new IllegalArgumentException(
                                       "Language must be a two character code");
            }
    
            this.language = newLanguage.toLowerCase();
        }
    }



    /**
     * Gets the language in which this action shall be carried.
     * 
     * @return  the new language of this action
     */
    private String getLanguage() {
        return this.language;
    }



    /**
     * Defines the job on which this action operates.
     * 
     * @param   newParentJob    the parent job of this action
     */
    private void setParentJob(Job newParentJob) {

        if (null == newParentJob || !newParentJob.isValid(false)) {
            throw new IllegalArgumentException(
                                           "Parent job must be a valid one");
        }

        this.parentJob = newParentJob;
    }



    /**
     * Gets the job on which this action operates.
     * 
     * @return  the parent job of this action
     */
    private Job getParentJob() {
        return this.parentJob;
    }



    /**
     * Defines the target for this action.
     * <p>
     * The content of this parameter depends on the type of the action. For an 
     * e-mail action, for instance, it will consist in the recipients addresses.
     * 
     * @param   newTarget  the action target
     */
    private void setTarget(String newTarget) {
        
        if (StringUtils.isEmpty(newTarget)) {
            this.target = null;
        }
        
        this.target = newTarget;
    }



    /**
     * Gets the target for this action.
     * 
     * @return  the action target
     * @see #setTarget(String)
     */
    private String getTarget() {
        return this.target;
    }



    /**
     * Defines the type of this action, such as E-MAIL or RSS.
     * 
     * @param   typeName    the action type name
     */
    private void setType(String typeName) {
        final ActionType newType = ActionType.getFromName(typeName);

        if (null == newType) {
            throw new IllegalArgumentException("Unknown action type");
        }

        this.type = newType;
    }



    /**
     * Gets the type of this action, such as E-MAIL or RSS.
     * 
     * @return  the action type
     */
    private ActionType getType() {
        return this.type;
    }



    /**
     * Creates an action information object from the parameters of the request.
     * 
     * @param   params              the map containing the parameters
     * @param   parentJob           the parent job of the action
     * @param   enforceMandatory    <code>true</code> to throw an exception
     *                              if a mandatory parameter isn't set
     * @return                      the action information object
     */
    public static ActionInfo createFromParametersMap(Map<String, ?> params,
                    Job parentJob, boolean enforceMandatory) {

        final ActionInfo actionInfo = new ActionInfo();
        actionInfo.setParentJob(parentJob);

        final String targetValue = (String) params.get("target");
        
        if (enforceMandatory || null != targetValue) {
            actionInfo.setTarget(targetValue);
        }

        final String typeValue = (String) params.get("type");
        
        if (enforceMandatory || null != typeValue) {
            actionInfo.setType(typeValue);
        }

        actionInfo.setLanguage((String) params.get("lang"));

        return actionInfo;
    }



    /**
     * Creates an action from this action information object.
     * 
     * @return  the new action
     */
    public AbstractAction createAction() {
        final ActionType newType = this.getType();

        if (null == newType) {
            throw new IllegalArgumentException("Action type can't be null");
        }

        AbstractAction newAction = null;

        if (newType.getName().equals("E-MAIL")) {
            newAction = new EmailAction(this.getParentJob(), this.getTarget());

            if (null != this.getLanguage()) {
                newAction.setLanguage(this.getLanguage());
            }

        } else if (newType.getName().equals("RSS")) {
            newAction = new RssAction(this.getParentJob());
        }

        if (null != newAction && newAction.persist()) {
            return newAction;
        }

        return null;
    }



    /**
     * Updates an existing action with the information of this object.
     * 
     * @param   action  the action to update
     * @return          <code>true</code> if the update succeeded
     */
    public boolean modifyAction(AbstractAction action) {

        if (null == action) {
            throw new IllegalArgumentException("Action can't be null");
        }

        if (null != this.getType()) {
            action.setType(this.getType());
        }

        if (null != this.getTarget()) {
            action.setTarget(this.getTarget());
        }

        if (null != this.getLanguage()) {
            action.setLanguage(this.getLanguage());
        }

        return action.persist();
    }
}
