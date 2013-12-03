package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.gui.i18n.Messages;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;

/**
 * Displays an application error in JSON format.
 * <p>
 * To send a non-error message to the user, please use {@link MessageView}.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class ErrorView extends AbstractJsonView {
    
    /**
     * Creates a new view.
     */
    public ErrorView() {
        
    }



    /**
     * Generates the error JSON.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the error
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the application error to
     *                                      JSON (hard luck!)</li>
     *                                      </ul>                   
     */
    @Override
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
        throws MonitorInterfaceException {
        
        final ObjectMapper mapper = this.getObjectMapper();
        final ObjectNode exceptionJson = mapper.createObjectNode();
        final Exception exception = this.getException(model);
        final Throwable cause = exception.getCause();
                
        if (null != cause) {
            exceptionJson.put("cause", cause.getMessage());
        }

        return exceptionJson;        
    }
    
    
    
    /**
     * Retrieves the exception to display as JSON.
     * 
     * @param   model   the model containing the data to display
     * @return          the exception to display
     */
    private Exception getException(Map<String, ?> model) {
        String modelExceptionKey;
        
        if (model.containsKey("monitorInterfaceException")) {
            modelExceptionKey = "monitorInterfaceException";

        } else if (model.containsKey("exception")) {
            modelExceptionKey = "exception";
            
        } else {
            return null;
        }
        
        return (Exception) model.get(modelExceptionKey);
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getResponseMessage(Map<String, ?> model, Locale locale) {
        final Messages i18n = new Messages(locale);
        final Exception exception = this.getException(model);
        String message;
        
        if (exception instanceof MonitorInterfaceException) {
            message = i18n.getMessage((
                          (MonitorInterfaceException) exception).getI18nCode());
        } else {
            message = exception.getLocalizedMessage();
        }
        
        return message;
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean isSuccess() {
        return false;
    }
}
