package org.easysdi.monitor.gui.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provides an access to the application localized string.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.1.1, 2010-06-02
 *
 */
public class Messages {
    
    private static final String BUNDLE_NAME 
        = "org.easysdi.monitor.gui.i18n.Monitor";
    
    private static final Locale DEFAULT_LOCALE = new Locale("en");

    private Locale         locale;
    private ResourceBundle bundle;



    /**
     * Creates a new localized string fetcher.
     * 
     * @param   stringsLocale   the locale to use to fetch the strings
     */
    public Messages(Locale stringsLocale) {
        this.setLocale(stringsLocale);
    }



    /**
     * Defines the ressource bundle containing the strings to fetch.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes and 
     * shouldn't be called directly.</i>
     * 
     * @param   newBundle   the ressource bundle
     */
    private void setBundle(ResourceBundle newBundle) {
        this.bundle = newBundle;
    }



    /**
     * Gets the ressource bundle containing the strings to fetch.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes and 
     * shouldn't be called directly. To get a string, please use the 
     * {@link #getMessage(String)} function.</i>
     * 
     * @return  the ressource bundle
     */
    private ResourceBundle getBundle() {
        return this.bundle;
    }



    /**
     * Defines the locale of the strings to fetch.
     * 
     * @param   newLocale   the locale
     */
    public void setLocale(Locale newLocale) {

        if (null == newLocale) {
            throw new IllegalArgumentException("Locale can't be null");
        }

        this.locale = newLocale;
        this.loadBundle();
    }



    /**
     * Loads a bundle corresponding to the locale. If it doesn't exist, 
     * the standard fallback schema is applied.
     */
    private void loadBundle() {
        ResourceBundle localeBundle;
        
        try {
            localeBundle = ResourceBundle.getBundle(Messages.BUNDLE_NAME, 
                                                    this.getLocale());
            this.setBundle(localeBundle);
            
        } catch (MissingResourceException exception) {
            
            if (!this.getLocale().equals(Messages.DEFAULT_LOCALE)) {
                this.setLocale(Messages.DEFAULT_LOCALE);
            } else {
                throw exception;
            }
        }
    }



    /**
     * Gets the locale used to fetch the strings.
     * 
     * @return  the locale
     */
    public Locale getLocale() {
        return this.locale;
    }



    /**
     * Gets a localized string.
     * 
     * @param   key the string identifying the string to fetch
     * @return      the string
     */
    public String getMessage(String key) {
        return this.getBundle().getString(key);
    }

}
