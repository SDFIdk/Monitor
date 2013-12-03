package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.logging.RawLogEntry;

/**
 * Transforms a response time into JSON.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public class ResponseTimeSerializer {

    /**
     * Dummy constructor used to prevent instantiation.
     */
    private ResponseTimeSerializer() {
        throw new UnsupportedOperationException(
                                           "This class can't be instantiated.");
    }
    
    
    
    /**
     * Generates the JSON representation of a response time.
     * 
     * @param   logEntry    the raw log entry containing the response time 
     * @param   addQueryId  <code>true</code> to include the identifier of the 
     *                      query that produced the response time
     * @param   mapper      the object used to map the data to JSON nodes
     * @return              a JSON node containing the data for the response 
     *                      time
     */
    public static JsonNode serialize(RawLogEntry logEntry, boolean addQueryId,
                                       ObjectMapper mapper) {
        final ObjectNode respTimeObject = mapper.createObjectNode();
        final SimpleDateFormat dateFormat = new SimpleDateFormat(
                                                         "yyyy-MM-dd HH:mm:ss");
        respTimeObject.put("responseDelay", logEntry.getResponseDelay());
        respTimeObject.put(
                "requestTime",
                dateFormat.format(logEntry.getRequestTime().getTime()));

        if (addQueryId) {
            respTimeObject.put("queryId", logEntry.getQueryId());
        }

        return respTimeObject;
    }

}
