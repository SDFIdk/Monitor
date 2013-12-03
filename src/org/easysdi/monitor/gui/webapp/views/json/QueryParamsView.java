package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.easysdi.monitor.biz.job.QueryParam;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import 
    org.easysdi.monitor.gui.webapp.views.json.serializers.QueryParamSerializer;

/**
 * Displays the JSON representation of query parameters collection.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public class QueryParamsView extends AbstractJsonView {
    
    /**
     * Creates a new view.
     */
    public QueryParamsView() {
        
    }
    
    

    /**
     * Generates the JSON code for the query parameters collection.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the query parameters collection
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the parameters collection
     *                                      to JSON</li>
     *                                      </ul>
     */
    @SuppressWarnings("unchecked")
    @Override
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale) 
        throws MonitorInterfaceException {

        if (model.containsKey("queryParamsCollection")) {
            final ObjectMapper mapper = this.getObjectMapper();
            final ArrayNode jsonParamsCollection = mapper.createArrayNode();

            for (QueryParam queryParam 
                    : (Set<QueryParam>) model.get("queryParamsCollection")) {
                
                jsonParamsCollection.add(
                         QueryParamSerializer.serialize(queryParam, mapper));
            }

            return jsonParamsCollection;

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
