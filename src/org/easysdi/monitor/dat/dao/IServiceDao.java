package org.easysdi.monitor.dat.dao;

import org.easysdi.monitor.biz.job.ServiceMethod;
import org.easysdi.monitor.biz.job.ServiceType;

/**
 * Provides OGC service information persistance operations.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public interface IServiceDao {

    /**
     * Fetches a service type object from its identifier.
     * 
     * @param   id  the long identifying a service type
     * @return      the service type it it's been found, or<br>
     *              <code>null</code> otherwise
     */
    ServiceType getServiceType(long id);



    /**
     * Fetches a service method object from its identifier.
     * 
     * @param   id  the long identifying the service method
     * @return      the service method if it's been found, or<br>
     *              <code>null</code> otherwise
     */
    ServiceMethod getServiceMethod(long id);



    /**
     * Checks if a service method and a service type are compatible.
     * 
     * @param   methodId    the long identifying the service method
     * @param   typeId      the long identifying the service type
     * @return              <code>true</code> if the method can be used with
     *                      the service type
     */
    boolean isMethodValidForType(long methodId, long typeId);

}
