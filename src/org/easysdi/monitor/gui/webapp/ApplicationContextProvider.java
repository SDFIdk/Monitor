package org.easysdi.monitor.gui.webapp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Provides a way to define the application context.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class ApplicationContextProvider implements ApplicationContextAware {
    
    /**
     * Creates a new context provider.
     */
    public ApplicationContextProvider() {
        
    }

    
    
    /**
     * Defines the current application context.
     * 
     * @param   appContext  the current application context
     */
    public void setApplicationContext(ApplicationContext appContext) {
        
        AppContext.setContext(appContext);
    }
}
