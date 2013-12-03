package org.easysdi.monitor.dat.dao;

import org.easysdi.monitor.biz.alert.AbstractAction;
import org.easysdi.monitor.biz.alert.ActionType;

/**
 * Provides action persistance operations.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public interface IActionDao {

    /**
     * Erases an action.
     * 
     * @param   action  the action
     * @return          <code>true</code> if the action was successfully deleted
     */
    boolean deleteAction(AbstractAction action);



    /**
     * Gets an action type object from its name.
     * 
     * @param   typeName    the name of the action type
     * @return              the action type object if it exists, or<br>
     *                      <code>null</code> otherwise
     */
    ActionType getType(String typeName);



    /**
     * Saves an action.
     * 
     * @param   action  the action
     * @return          <code>true</code> if the action was successfully 
     *                  persisted
     */
    boolean persistAction(AbstractAction action);

}
