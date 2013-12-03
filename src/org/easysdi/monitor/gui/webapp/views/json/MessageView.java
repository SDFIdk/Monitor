package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;

/**
 * Displays the JSON representation of a message.
 * <p>
 * The message view should be used to notify the user when the application is
 * following its normal flow. If it isn't, please use {@link ErrorView} instead.
 *
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public class MessageView extends AbstractJsonView {

    /**
     * Creates a new view.
     */
    public MessageView() {
        
    }
    
    
    
    /**
     * Generates the JSON code for a message.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the message
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the message to JSON</li>
     *                                      </ul>
     */
    @Override
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale) 
        throws MonitorInterfaceException {
            
        return this.getObjectMapper().createObjectNode();
            
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean isSuccess() {
        return true;
    }
}
