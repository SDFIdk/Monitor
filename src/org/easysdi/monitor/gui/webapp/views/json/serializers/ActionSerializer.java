package org.easysdi.monitor.gui.webapp.views.json.serializers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.alert.AbstractAction;

/**
 * Transforms an action into JSON.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class ActionSerializer {

    /**
     * Dummy constructor used to prevent instantiation.
     */
    private ActionSerializer() {
        throw new UnsupportedOperationException(
                                            "This class can't be instantiated");
    }
    
    
    
    /**
     * Generates the JSON representation of an action.
     * 
     * @param   action  the action to represent in JSON
     * @param   mapper  the object used to map the data to JSON nodes
     * @return          the JSON node containing the data for the action
     */
    public static JsonNode serialize(AbstractAction action, 
                                     ObjectMapper mapper) {
        
        final ObjectNode jsonAction = mapper.createObjectNode();

        jsonAction.put("type", action.getType().getName());
        jsonAction.put("target", action.getTarget());
        jsonAction.put("lang", action.getLanguage());
        jsonAction.put("actionId", action.getActionId());

        return jsonAction;
    }
}
