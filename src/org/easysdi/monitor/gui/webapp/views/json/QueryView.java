package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.QuerySerializer;

/**
 * Displays the JSON representation of a query.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class QueryView extends AbstractJsonView {

    /**
     * Creates a new view.
     */
    public QueryView() {
        
    }
    
    
    
    /**
     * Generates the JSON code for the query.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the query
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the query to JSON</li>
     *                                      </ul>
     */
    @Override
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale) 
        throws MonitorInterfaceException {

        if (model.containsKey("query")) {
            final Query query = (Query) model.get("query");
            
            return QuerySerializer.serialize(query, true, locale, 
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
