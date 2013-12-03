package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.util.Locale;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.JobResult;
import org.easysdi.monitor.biz.job.QueryResult;
import org.easysdi.monitor.biz.job.Status;
import org.easysdi.monitor.gui.i18n.Messages;

/**
 * Represents the result of a job execution in JSON format.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public final class JobResultSerializer {

    /**
     * Private constructor preventing instantiation.
     */
    private JobResultSerializer() {
        throw new UnsupportedOperationException(
                "This class can't be instantiated."); 
    }
    
    
    
    /**
     * Produces the JSON representation of a job execution result.
     * 
     * @param   jobResult   the job result to represent in JSON
     * @param   locale      the locale to use to display the job result
     *                      informations
     * @param   mapper      the object used to map the data to JSON nodes
     * @return              an object containing the JSON representation of
     *                      the job result
     */
    public static JsonNode serialize(JobResult jobResult, Locale locale, 
                                     ObjectMapper mapper) {
        
        final ObjectNode jsonJobResult = mapper.createObjectNode();

        if (null != jobResult) {
            jsonJobResult.put("url", jobResult.getServiceUrl());
            jsonJobResult.put("statusCause", jobResult.getStatusCause());
            final Status jobStatus = jobResult.getStatus();
            String jobStatusString;
    
            if (null != jobStatus) {
                jobStatusString = jobStatus.getDisplayString(locale);
            } else {
                jobStatusString 
                    = new Messages(locale).getMessage("status.not_tested");
            }
    
            jsonJobResult.put("status", jobStatusString);
            jsonJobResult.put("statusCode",jobResult.getStatusValue().name());
            jsonJobResult.put("serviceType", 
                              jobResult.getServiceType().getName());
            jsonJobResult.put("queriesResults",
                              JobResultSerializer.buildQueriesResultsArray(
                                   jobResult.getQueryResults(), locale, 
                                   mapper));
            jsonJobResult.put("jobName", jobResult.getJobName());
            jsonJobResult.put("httpMethod", jobResult.getHttpMethod());
        }

        return jsonJobResult;
    }



    /**
     * Creates a JSON array to represent a set of query execution results.
     * 
     * @param   queryResults    a set containing the query results to represent
     *                          in JSON
     * @param   locale          the locale to use to display the informations
     * @param   mapper          the object used to map the data to JSON nodes
     * @return                  a JSON array object containing the 
     *                          representation of each query
     */
    private static ArrayNode buildQueriesResultsArray(
            Set<QueryResult> queryResults, Locale locale, ObjectMapper mapper) {

        final ArrayNode jsonQueryResults = mapper.createArrayNode();

        for (QueryResult resultItem : queryResults) {
            jsonQueryResults.add(
                     QueryResultSerializer.serialize(resultItem, locale, 
                                                     mapper));
        }

        return jsonQueryResults;
    }

}
