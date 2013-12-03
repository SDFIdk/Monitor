package org.easysdi.monitor.gui.webapp;

import org.springframework.context.ApplicationContext;

/**
 * Provides an access to the application context.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class AppContext {

    private static ApplicationContext context;
    
    
    /**
     * Dummy constructor used to prevent instantiation.
     */
    private AppContext() {
        throw new UnsupportedOperationException(
                                            "This class can't be instantiated");
    }



    /**
     * Defines the application context.
     * 
     * @param   newContext  the Spring application context
     */
    public static void setContext(ApplicationContext newContext) {
        AppContext.context = newContext;
    }



    /**
     * Gets the application context.
     * 
     * @return  the Spring application context
     */
    public static ApplicationContext getContext() {
        return AppContext.context;
    }

}
