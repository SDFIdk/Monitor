package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.QuerySerializer;

/**
 * Displays the JSON representation of a query collection.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class QueryCollectionView extends AbstractJsonView {

    /**
     * Creates a new view.
     */
    public QueryCollectionView() {
        
    }
    
    
   
    /**
     * Generates the JSON code for the query collection.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the query collection
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the query collection to 
     *                                      JSON</li>
     *                                      </ul>
     */
    @SuppressWarnings("unchecked")
    @Override
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale) 
        throws MonitorInterfaceException {

        if (model.containsKey("queryCollection")) {
            final ObjectMapper mapper = this.getObjectMapper();
            final ArrayNode queriesList = mapper.createArrayNode();

            for (Query query 
                        : (Collection<Query>) model.get("queryCollection")) {

                queriesList.add(QuerySerializer.serialize(query, true,
                                                          locale, mapper));

            }

            return queriesList;

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
