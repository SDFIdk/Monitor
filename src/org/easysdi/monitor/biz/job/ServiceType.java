package org.easysdi.monitor.biz.job;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.easysdi.monitor.biz.util.HashCodeConstants;

/**
 * Represents an OGC web service type.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class ServiceType extends AbstractValueObject {

    private String      version;
    private Set<String> methods;



    /**
     * Creates a new service type.
     * <p>
     * <i><b>Note:</b> This constructor is meant to be used by the persistance
     * mechanism.</i>
     */
    protected ServiceType() {

    }



    /**
     * Defines the methods used by this web service type.
     * <p>
     * <i><b>Note:</b> This method is meant to be used by the persistance
     * mechanism.</i>
     * 
     * @param   typeMethods the service methods for this type
     */
    @SuppressWarnings("unused")
    private void setMethods(Set<String> typeMethods) {
        this.methods = typeMethods;
    }



    /**
     * Gets the methods used by this web service type.
     * 
     * @return  the service methods for this type
     */
    public Set<String> getMethods() {
        return Collections.unmodifiableSet(this.methods);
    }



    /**
     * Defines the version of this web service type.
     * <p>
     * <i><b>Note:</b> This method is meant to be used by the persistance
     * mechanism.</i>
     * 
     * @param   typeVersion the version string
     */
    public void setVersion(String typeVersion) {
        this.version = typeVersion;
    }



    /**
     * Gets the version of this web service type.
     * 
     * @return the version string
     */
    public String getVersion() {
        return this.version;
    }



    /**
     * Gets the service type object corresponding to the given type name.
     * 
     * @param   typeName    the type name
     * @return              the corresponding service type, or<br>
     *                      <code>null</code> if it doesn't exist
     */
    public static ServiceType getObject(String typeName) {
        return (ServiceType) AbstractValueObject.getObject(
                   typeName, "org.easysdi.monitor.biz.job.ServiceType");
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object that) {

        if (null != that && that instanceof ServiceType) {

            return (this.getName() == ((ServiceType) that).getName());
        }

        return false;
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(
                HashCodeConstants.SEED, HashCodeConstants.MULTIPLIER);
        
        hashCodeBuilder.append(this.getName());
        
        return hashCodeBuilder.toHashCode();
    }

}
