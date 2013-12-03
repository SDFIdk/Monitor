package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.
       ResponseTimeSerializer;

/**
 * Displays the JSON representation of a response time collection.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public class ResponseTimeView extends AbstractJsonView {
    
    /**
     * Creates a new view.
     */
    public ResponseTimeView() {
        
    }
    

    
    /**
     * Generates the JSON code for the response time collection.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the response time collection
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the response time 
     *                                      collection to JSON</li>
     *                                      </ul>
     */
    @SuppressWarnings("unchecked")
    @Override
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale) 
        throws MonitorInterfaceException {

        if (model.containsKey("rawLogsCollection")
            && model.containsKey("addQueryId")) {
            final Set<RawLogEntry> rawLogsCollection 
                = (Set<RawLogEntry>) model.get("rawLogsCollection");
            final Long noPagingCount = (Long) model.get("noPagingCount");
            final Boolean addQueryId = (Boolean) model.get("addQueryId");
            final ObjectMapper mapper = this.getObjectMapper();
            final ObjectNode jsonDataObject = mapper.createObjectNode();
            jsonDataObject.put("noPagingCount", noPagingCount);
            final ArrayNode responseTimes = mapper.createArrayNode();

            for (RawLogEntry logEntry : rawLogsCollection) {
                responseTimes.add(
                      ResponseTimeSerializer.serialize(logEntry, addQueryId, 
                                                       mapper));
            }
            
            jsonDataObject.put("rows", responseTimes);

            return jsonDataObject;
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
