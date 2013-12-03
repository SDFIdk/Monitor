package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.easysdi.monitor.biz.alert.AbstractAction;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.ActionSerializer;

/**
 * Displays an action in JSON format.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public class ActionView extends AbstractJsonView {
    
    /**
     * Creates a new view.
     */
    public ActionView() {
        
    }
    


    /**
     * Generates the action JSON.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              the JSON node for the action
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the action to JSON</li>
     *                                      </ul>                   
     */
    @Override
    public JsonNode getResponseData(Map<String, ?> model, Locale locale)
        throws MonitorInterfaceException {

        if (model.containsKey("action")) {
            final AbstractAction action = (AbstractAction) model.get("action");
            
            return ActionSerializer.serialize(action, 
                                              this.getObjectMapper());
        }
            
        throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean isSuccess() {
        return true;
    }
}
