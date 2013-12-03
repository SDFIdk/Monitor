package org.easysdi.monitor.dat.dao;

import org.easysdi.monitor.biz.job.AbstractValueObject;
import org.easysdi.monitor.biz.job.Status;
import org.easysdi.monitor.biz.job.Status.StatusValue;

/**
 * Provides value object persistance operations.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public interface IValueObjectDao {

    /**
     * Fetches a value object.
     * 
     * @param   valueName       the name of the value
     * @param   classMapName    the class of the value object
     * @return                  the value object if it's been found, or<br>
     *                          <code>null</code> otherwise
     */
    AbstractValueObject getValueObject(String valueName,
                    String classMapName);



    /**
     * Gets a status object from its value.
     * 
     * @param   statusValue the value of the status
     * @return              the status object if it exists, or<br>
     *                      <code>null</code> otherwise
     */
    Status getStatusObject(StatusValue statusValue);

}
