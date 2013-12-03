package org.easysdi.monitor.biz.job;

import org.easysdi.monitor.dat.dao.ValueObjectDaoHelper;


/**
 * An object consisting of a name and an identifier.
 * <p>
 * This class is a wrapper for an element from a coding table in a database. 
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public abstract class AbstractValueObject {

    private long   id;
    private String name;


    /**
     * Instantiates a new value object.
     * <p>
     * <i><b>Note:</b> You shouldn't instantiate a value object. This 
     * constructor is meant to be used by the persistance mechanism. If you need
     * an instance of a value object, please use the static <code>
     * getObject(String)</code> method of the corresponding concrete class 
     * instead.</i>
     */
    protected AbstractValueObject() {

    }



    /**
     * Defines the value object's identifier.
     * <p>
     * <i><b>Note:</b> You shouldn't call this method directly. Setting the
     * identifiers should be left to the persistance mechanism.</i>
     * 
     * @param   newId    the long identifying this value object
     */
    protected void setId(long newId) {
        
        if (1 > newId) {
            throw new IllegalArgumentException(
                   "The identifier must be strictly positive.");
        }
        
        this.id = newId;
    }



    /**
     * Gets the value object's identifier.
     * 
     * @return the long identifying the value object
     */
    protected long getId() {
        return this.id;
    }



    /**
     * Defines the human-friendly name of the value object.
     * <p>
     * <i><b>Note:</b> You shouldn't call this method directly. It is meant to 
     * be used by the persistance mechanism.</i>
     * 
     * @param   newName    the value object's name
     */
    protected void setName(String newName) {
        this.name = newName;
    }



    /**
     * Gets the human-friendly name of the value object.
     * 
     * @return the value object's name
     */
    public String getName() {
        return this.name;
    }



    /**
     * Gets a value object from its name and type.
     * <p>
     * <i><b>Note:</b> You shouldn't call this method directly. It is meant to 
     * for internal use. Please get value object instances through the static
     * <code>getObject(String)</code> method of the corresponding concrete class
     * </i>
     * 
     * @param   name            the sought value object's name
     * @param   classMapName    the sought value object's persistance class name
     * @return                  the value object if it's been found
     *                          <code>null</code> otherwise
     */
    protected static AbstractValueObject getObject(String name, 
                                                   String classMapName) {

        return ValueObjectDaoHelper.getValueObjectDao().getValueObject(
                                                           name, classMapName);
    }
}
