package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.easysdi.monitor.biz.alert.AbstractAction;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.ActionSerializer;

/**
 * Displays a collection of actions in JSON format.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public class ActionsCollectionView extends AbstractJsonView {
    
    /**
     * Creates a new view.
     */
    public ActionsCollectionView() {
        
    }


    /**
     * Generates the actions collection JSON.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the actions collection
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the actions collection to
     *                                      JSON</li>
     *                                      </ul>                   
     */
    @SuppressWarnings("unchecked")
    @Override
    public JsonNode getResponseData(Map<String, ?> model, Locale locale)
        throws MonitorInterfaceException {

        if (model.containsKey("actionsCollection")) {
            final Set<AbstractAction> actionsSet 
                = (Set<AbstractAction>) model.get("actionsCollection");
            
            final ObjectMapper mapper = this.getObjectMapper();
            final ArrayNode actionsArray = mapper.createArrayNode();

            for (AbstractAction action : actionsSet) {
                actionsArray.add(ActionSerializer.serialize(action, mapper));
            }

            return actionsArray;
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
