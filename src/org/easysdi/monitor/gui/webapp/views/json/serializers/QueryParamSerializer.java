package org.easysdi.monitor.gui.webapp.views.json.serializers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.QueryParam;

/**
 * Transforms the parameter of a query into JSON.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public class QueryParamSerializer {

    /**
     * Dummy constructor used to prevent instantiation.
     */
    private QueryParamSerializer() {
        throw new UnsupportedOperationException(
                                           "This class can't be instantiated.");
    }
    
    
    
    /**
     * Generates the JSON representation of a query parameter.
     * 
     * @param   param   the query parameter to represent in JSON
     * @param   mapper  the object used to map the data to JSON nodes
     * @return          a JSON node containing the data for the parameter
     */
    public static JsonNode serialize(QueryParam param, ObjectMapper mapper) {

        if (null == param) {
            throw new IllegalArgumentException("Query parameter can't be null");
        }

        final ObjectNode paramJson = mapper.createObjectNode();
        paramJson.put("name", param.getName());
        paramJson.put("value", param.getValue());

        return paramJson;
    }

}
