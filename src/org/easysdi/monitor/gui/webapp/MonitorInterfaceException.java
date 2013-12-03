package org.easysdi.monitor.gui.webapp;

import java.util.Locale;

import org.easysdi.monitor.gui.i18n.Messages;

/**
 * Signals an error during a Monitor operation. Wraps the enventual internal 
 * exceptions.
 * 
 * @author  Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class MonitorInterfaceException extends Exception {

    private static final long serialVersionUID = -6882265499244916082L;

    private final String i18nCode;



    /**
     * Creates a new Monitor exception. 
     * 
     * @param   genericMessage  the generic, locale-independent message 
     *                          explaining the exception
     * @param   messageKey      the key of the localized exception message
     */
    public MonitorInterfaceException(String genericMessage, String messageKey) {
        super(genericMessage);
        this.i18nCode = messageKey;
    }



    /**
     * Creates a new Monitor exception. 
     * 
     * @param   genericMessage  the generic, locale-independent message 
     *                          explaining the exception
     * @param   messageKey      the key of the localized exception message
     * @param   cause           the inner exception that caused this one to
     *                          be thrown
     */
    public MonitorInterfaceException(String genericMessage, String messageKey,
                    Throwable cause) {
        super(genericMessage, cause);
        this.i18nCode = messageKey;
    }



    /**
     * Gets the key for the localized message explaining the exception.
     * 
     * @return  the message key
     */
    public String getI18nCode() {
        return this.i18nCode;
    }



    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    public String getLocalizedMessage() {
        return this.getMessage();
    }



    /**
     * Gets a message explaining the exception in the current locale (if 
     * available).
     * 
     * @param   locale  the locale indicating in which language the message 
     *                  shall be retrieved
     * @return          the localized message
     */
    public String getLocalizedMessage(Locale locale) {
        final Messages i18n = new Messages(locale);

        return i18n.getMessage(this.getI18nCode());
    }
}
