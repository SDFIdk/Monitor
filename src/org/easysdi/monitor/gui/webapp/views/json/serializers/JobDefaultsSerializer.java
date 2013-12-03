package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.JobDefaultParameter;

/**
 * Transforms a collection of job parameters into JSON.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public class JobDefaultsSerializer {
    
    /**
     * Dummy constructor used to prevent instantiation.
     */
    private JobDefaultsSerializer() {
        throw new UnsupportedOperationException(
                                            "This class can't be instantiated");
    }
    
    

    /**
     * Generates the JSON representation of a collection of job parameters.
     * 
     * @param   paramsMap   a map containing the job parameters to represent in 
     *                      JSON
     * @param   mapper      the object used to map the job parameters to JSON
     *                      nodes
     * @return              a JSON object node containing the job parameters
     *                      data
     */
    public static ObjectNode serialize( 
             Map<String, JobDefaultParameter> paramsMap, ObjectMapper mapper) {

        final ObjectNode jsonParamsCollection = mapper.createObjectNode();

        jsonParamsCollection.put(
                 "allowsRealTime", 
                 paramsMap.get("ALLOWS_REALTIME").getStringValue());
        jsonParamsCollection.put(
                 "bizErrors", 
                 paramsMap.get("BUSINESS_ERRORS").getStringValue());
        jsonParamsCollection.put("httpErrors",
                                 paramsMap.get("HTTP_ERRORS").getStringValue());
        jsonParamsCollection.put(
                 "isAutomatic", paramsMap.get("IS_AUTOMATIC").getStringValue());
        jsonParamsCollection.put("isPublic",
                                 paramsMap.get("IS_PUBLIC").getStringValue());
        jsonParamsCollection.put(
                 "slaEndTime", paramsMap.get("SLA_END_TIME").getStringValue());
        jsonParamsCollection.put(
                 "slaStartTime",
                 paramsMap.get("SLA_START_TIME").getStringValue());
        jsonParamsCollection.put(
                 "testInterval",
                 paramsMap.get("TEST_INTERVAL").getStringValue());
        jsonParamsCollection.put("timeout",
                                 paramsMap.get("TIMEOUT").getStringValue());
        jsonParamsCollection.put(
                 "triggersAlerts", 
                 paramsMap.get("TRIGGERS_ALERTS").getStringValue());
        jsonParamsCollection.put(
        		"runSimultaneous",
        		paramsMap.get("RUN_SIMULATANEOUS").getStringValue());
        jsonParamsCollection.put(
        		"saveResponse",
        		paramsMap.get("SAVE_RESPONSE").getStringValue());
       
        

        return jsonParamsCollection;
    }
}
