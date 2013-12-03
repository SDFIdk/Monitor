package org.easysdi.monitor.biz.job;

import java.util.Collections;
import java.util.Set;

import org.easysdi.monitor.dat.dao.IServiceDao;
import org.easysdi.monitor.dat.dao.ServiceDaoHelper;

/**
 * Picture an OGC web service method.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class ServiceMethod extends AbstractValueObject {

    private Set<String> allowedTypes;


    /**
     * Creates a new service method.
     * <p>
     * <i><b>Note:</b> This constructor is meant to be used by the persistance
     * mechanism.</i>
     */
    protected ServiceMethod() {

    }



    /**
     * Checks if this method can be used with a web service of the given type. 
     * 
     * @param   checkedType the web service typed trying to use this method
     * @return              <code>true</code> if this method is appropriate for
     *                      the passed service type
     */
    public boolean isValidForType(ServiceType checkedType) {

        if (null != checkedType) {
            final IServiceDao dao = ServiceDaoHelper.getServiceDao(); 
            return dao.isMethodValidForType(this.getId(), checkedType.getId());
        }

        return false;
    }



    /**
     * Gets the service method object corresponding to the given method name. 
     * 
     * @param   methodName  the method name
     * @return              the corresponding service method object, or<br>
     *                      <code>null</code> if it doesn't exist
     */
    public static ServiceMethod getObject(String methodName) {
        return (ServiceMethod) AbstractValueObject.getObject(
                 methodName, "org.easysdi.monitor.biz.job.ServiceMethod");
    }



    /**
     * Defines which service types can use this method.
     * <p>
     * <i><b>Note:</b> This method is meant to be used by the persistance
     * mechanism.</i>
     * 
     * @param   newAllowedTypes    a set containing the allowed service types
     */
    @SuppressWarnings("unused")
    private void setAllowedTypes(Set<String> newAllowedTypes) {
        this.allowedTypes = newAllowedTypes;
    }



    /**
     * Gets which service types can use this method.
     * 
     * @return a set containing the allowed service types
     */
    public Set<String> getAllowedTypes() {
        return Collections.unmodifiableSet(this.allowedTypes);
    }

}
