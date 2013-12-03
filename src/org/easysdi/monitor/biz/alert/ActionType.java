package org.easysdi.monitor.biz.alert;

import org.easysdi.monitor.dat.dao.ActionDaoHelper;

/**
 * The type of action to be triggered when an alert is raised.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 */
public class ActionType {

    private String name;
    private long   typeId;



    /**
     * No-argument constructor, used by the persistance mechanism.
     */
    protected ActionType() {

    }



    /**
     * Defines this action type's name.
     * <p>
     * <i><b>Note:</b> This method is meant to be used by the persistance 
     * mechanism.</i>
     * 
     * @param newName   the type name
     */
    @SuppressWarnings("unused")
    private void setName(String newName) {
        this.name = newName;
    }



    /**
     * Gets this action type's name.
     * 
     * @return the type name
     */
    public String getName() {
        return this.name;
    }



    /**
     * Defines this action type's identifier
     * <p>
     * <i><b>Note:</b> This method shouldn't be used directly. The identifier
     * should be defined through the persistance mechanism.</i>
     * 
     * @param   newTypeId   a long uniquely identifying the action type
     */
    @SuppressWarnings("unused")
    private void setTypeId(long newTypeId) {
        this.typeId = newTypeId;
    }



    /**
     * Gets the identifier for this action type.
     * 
     * @return  the unique identifier
     */
    @SuppressWarnings("unused")
    private long getTypeId() {
        return this.typeId;
    }



    /**
     * Finds an action type from its name.
     * 
     * @param   name    the name of the desired action type
     * @return          the action type, or<br>
     *                  <code>null</code> if it doesn't exist
     */
    public static ActionType getFromName(String name) {

        return ActionDaoHelper.getActionDao().getType(name);
    }

}
