package org.easysdi.monitor.biz.job;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.easysdi.monitor.biz.util.HashCodeConstants;

/**
 * Represents a relation between a service type and a service method.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class ServiceTypeMethod implements Serializable {

    private static final long serialVersionUID = -7365141941142437778L;

    private long              serviceTypeId;
    private long              serviceMethodId;
    
    
    
    /**
     * Creates a new service type <-> service method relation.
     */
    private ServiceTypeMethod() {
        
    }



    /**
     * Defines the service type identifier.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes and 
     * shouldn't be called directly.</i> 
     * 
     * @param   typeId  the long identifying the service type
     */
    @SuppressWarnings("unused")
    private void setServiceTypeId(long typeId) {
        this.serviceTypeId = typeId;
    }



    /**
     * Gets the identifier for the service type.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes and 
     * shouldn't be called directly.</i>
     * 
     * @return  the service type identifier
     */
    private long getServiceTypeId() {
        return this.serviceTypeId;
    }



    /**
     * Defines the service method identifier.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes and 
     * shouldn't be called directly.</i>
     *
     * @param   methodId    the long identifying the service method
     */
    @SuppressWarnings("unused")
    private void setServiceMethodId(long methodId) {
        this.serviceMethodId = methodId;
    }



    /**
     * Gets the identifier for the service method.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes and 
     * shouldn't be called directly.</i>
     * 
     * @return  the service method identifier
     */
    private long getServiceMethodId() {
        return this.serviceMethodId;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object thatObject) {

        if (thatObject instanceof ServiceTypeMethod) {
            final ServiceTypeMethod that = (ServiceTypeMethod) thatObject;

            return (that.getServiceMethodId() == this.getServiceMethodId() 
                    && that.getServiceTypeId() == this.getServiceTypeId());
        }

        return false;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder 
            = new HashCodeBuilder(HashCodeConstants.SEED, 
                                  HashCodeConstants.MULTIPLIER);
        
        hashCodeBuilder.append(this.getServiceTypeId());
        hashCodeBuilder.append(this.getServiceMethodId());
        
        return hashCodeBuilder.toHashCode();
    }
}
