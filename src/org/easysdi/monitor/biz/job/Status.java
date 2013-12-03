package org.easysdi.monitor.biz.job;

import java.util.Locale;

import org.easysdi.monitor.dat.dao.IValueObjectDao;
import org.easysdi.monitor.dat.dao.ValueObjectDaoHelper;
import org.easysdi.monitor.gui.i18n.Messages;

/**
 * Indicates if a job works correctly.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class Status {

    /**
     * Enumerates the deegrees of responsiveness for a job.
     * 
     * @author Yves Grasset - arx iT
     * @version 1.0, 2010-03-19
     *
     */
    public static enum StatusValue {
        AVAILABLE,
        NOT_TESTED,
        OUT_OF_ORDER,
        UNAVAILABLE
    }

    private long   id;
    private String value;
    
    

	/**
     * Instantiates a new status.
     * <p>
     * <i><b>Note:</b> This constructor is meant to be used by the persistance
     * mechanism.</i>
     */
    protected Status() {
        
    }



    /**
     * Defines a status value from a string.
     * <p>
     * <i><b>Note:</b> This method is meant to be used by the persistance
     * mechanism.</i>
     * 
     * @param   status  a string containing the value for this status 
     */
    @SuppressWarnings("unused")
    private void setValue(String status) {

        if (null != Status.StatusValue.valueOf(status)) {
            this.value = status;
        }
    }



    /**
     * Gets the string value for this status.
     * 
     * @return the status as a string
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Defines the identifier for this status.
     * <p> 
     * <i><b>Note:</b> This method is meant to be used by the persistance
     * mechanism.</i>
     * 
     * @param   newId   the long uniquely idetifying this status
     */
    @SuppressWarnings("unused")
    private void setId(long newId) {
        this.id = newId;
    }



    /**
     * Gets the idetifier for this status.
     * <p>
     * <i><b>Note:</b> This method is meant to be used by the persistance
     * mechanism.</i>
     * 
     * @return  the long uniquely idetifying this status
     */
    @SuppressWarnings("unused")
    private long getId() {
        return this.id;
    }



    /**
     * Gets the enumeration value for this status.
     * 
     * @return the value fro this status
     */
    public StatusValue getStatusValue() {
        return Status.StatusValue.valueOf(this.getValue());
    }



    /**
     * Gets a localized string value for this status.
     * 
     * @param   locale  the locale to use to get the string value
     * @return          the localized value string
     */
    public String getDisplayString(Locale locale) {
        final Messages i18n = new Messages(locale);
        
        if (null == this.getValue()) {
            return i18n.getMessage("status.invalid");
        }

        return i18n.getMessage(String.format(
                  "status.%1$s", this.getStatusValue().name().toLowerCase()));
    }



    /**
     * Gets the status object corresponding to an enumeration value.
     * 
     * @param   statusValue the status value
     * @return              the status for the given value, or<br>
     *                      <code>null</code> if it doesn't exist
     */
    public static Status getStatusObject(StatusValue statusValue) {
        final IValueObjectDao dao = ValueObjectDaoHelper.getValueObjectDao();

        return dao.getStatusObject(statusValue);

    }
    
    
    
    /**
     * Determines if a given status value indicates a (possibly) responsive
     * service.
     * <p>
     * A service is considered responsive until proven unresponsive.
     * 
     * @param   statusValue the status value
     * @return              <code>true</code> if the service isn't currently
     *                      considered unresponsive
     */
    public static boolean isStatusValueOK(StatusValue statusValue) {
        
        return (Status.StatusValue.AVAILABLE == statusValue 
                || Status.StatusValue.NOT_TESTED == statusValue);
    }
}
