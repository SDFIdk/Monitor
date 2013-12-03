package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.QueryResult;

/**
 * Utility class for representing query results in JSON format.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.1, 2010-04-30
 *
 */
public final class QueryResultSerializer {
    
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * No-argument private constructor used to prevent instantiation.
     */
    private QueryResultSerializer() {
        throw new UnsupportedOperationException(
                                           "This class can't be instantiated.");
    }

    
    
    /**
     * Generates a JSON representation of a query result object.
     * 
     * @param   resultItem      the query result to represent in JSON
     * @param   locale          the locale to use to display the information
     * @param   mapper          the object used to map the data to JSON nodes
     * @return                  a JSON object containing the query result
     *                          information
     */
    public static JsonNode serialize(QueryResult resultItem, Locale locale, 
                                     ObjectMapper mapper) {
        
        final ObjectNode jsonResult = mapper.createObjectNode();
        final SimpleDateFormat dateFormat 
            = new SimpleDateFormat(QueryResultSerializer.DATE_TIME_FORMAT);
        jsonResult.put("testedUrl", resultItem.getTestedUrl());
        jsonResult.put("status",
                       resultItem.getStatus().getDisplayString(locale));
        jsonResult.put("statusCode",
                resultItem.getStatusValue().name());
        jsonResult.put("responseDelay", resultItem.getResponseDelay());
        jsonResult.put("requestTime",
               dateFormat.format(resultItem.getRequestTime().getTime()));
        jsonResult.put("queryName", resultItem.getQueryName());
        jsonResult.put("message", resultItem.getMessage());
        jsonResult.put("httpCode", resultItem.getHttpCode());
        jsonResult.put("serviceExceptionCode", 
                       StringUtils.defaultString(
                             resultItem.getServiceExceptionCode()));

        return jsonResult;
    }

}
