package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.JobConfiguration;
import org.easysdi.monitor.biz.job.Query;

/**
 * Generates JSON code to represent a job.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class JobSerializer {
    
    private Job job;
    
    /**
     * Creates a new JSON job transformer.
     * 
     * @param   theJob  the job to represent in JSON
     */
    public JobSerializer(Job theJob) {
        this.setJob(theJob);
    }
    
    

    /**
     * Creates a job's JSON representation.
     * 
     * @param   includeQueries      <code>true</code> to add the job's
     *                              queries to the representation
     * @param   includeQueryParams  <code>true</code> to add the job queries'
     *                              parameters to the representation.
     *                              <p>
     *                              <i><b>Note:</b> Obviously, this parameter 
     *                              has no effect if <code>includeQueries</code>
     *                              is false.</i>
     * @param   locale              the locale that the job's informations must 
     *                              be displayed in
     * @param   mapper              the object used to map the data to JSON 
     *                              nodes
     * @return                      a JSON object containing the object's 
     *                              representation
     */
    public JsonNode serialize(boolean includeQueries, 
                                boolean includeQueryParams, Locale locale, 
                                ObjectMapper mapper) {

        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        final SimpleDateFormat dateFormat 
        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final ObjectNode jsonJob = mapper.createObjectNode();
        final JobConfiguration jobConfig = this.getJob().getConfig();
        jsonJob.put("id", this.getJob().getJobId());
        jsonJob.put("name", jobConfig.getJobName());
        jsonJob.put("status", 
                    this.getJob().getStatus().getDisplayString(locale)); 
        jsonJob.put("statusCode", this.getJob().getStatusValue().name());
        jsonJob.put("triggersAlerts", jobConfig.isAlertsActivated());
        jsonJob.put("isAutomatic", jobConfig.isAutomatic());
        jsonJob.put("bizErrors", jobConfig.isBizErrorChecked());
        jsonJob.put("httpErrors", jobConfig.isHttpErrorChecked());
        jsonJob.put("httpMethod", jobConfig.getHttpMethod().getName());
        jsonJob.put("login", StringUtils.defaultString(jobConfig.getLogin()));
        jsonJob.put("password", 
                    StringUtils.defaultString(jobConfig.getPassword()));
        jsonJob.put("isPublic", jobConfig.isPublished());
        jsonJob.put("allowsRealTime", jobConfig.isRealTimeAllowed());
        jsonJob.put("serviceType", jobConfig.getServiceType().getName());
        jsonJob.put("slaStartTime",
                    timeFormat.format(jobConfig.getSlaStartTime().getTime()));
        jsonJob.put("slaEndTime",
                    timeFormat.format(jobConfig.getSlaEndTime().getTime()));
        jsonJob.put("testInterval", jobConfig.getTestInterval());
        jsonJob.put("timeout", jobConfig.getTimeout());
        jsonJob.put("url", jobConfig.getUrl());
        jsonJob.put("lastStatusUpdate", dateFormat.format(this.getJob().getStatusUpdateTime().getTime()));
        jsonJob.put("saveResponse",jobConfig.getSaveResponse());
        jsonJob.put("runSimultaneous",jobConfig.isRunSimultaneous());
        
        if (includeQueries) {
            jsonJob.put("queries", 
                        this.buildQueriesArray(includeQueryParams, locale, 
                                               mapper));
        }

        return jsonJob;

    }



    /**
     * Creates a JSON representation of a job's queries.
     * 
     * @param   includeQueryParams  <code>true</code> to add the queries' 
     *                              parameters to the representation
     * @param   locale              the locale that the queries' information
     *                              must be displayed in
     * @param   mapper              the object used to map the data to JSON 
     *                              nodes
     * @return                      a JSON array containing the job's queries'
     *                              representations
     */
    private ArrayNode buildQueriesArray(boolean includeQueryParams, 
                                        Locale locale, ObjectMapper mapper) {
        
        final ArrayNode queriesArray = mapper.createArrayNode();

        for (Query jobQuery : this.getJob().getQueriesList()) {
            queriesArray.add(QuerySerializer.serialize(jobQuery,
                                                       includeQueryParams,
                                                       locale, mapper));
        }

        return queriesArray;
    }



    /**
     * Defines the job to represent.
     * 
     * @param   newJob the job to represent in JSON
     */
    private void setJob(Job newJob) {

        if (null == newJob) {
            throw new IllegalArgumentException("Job can't be null");
        }

        this.job = newJob;
    }



    /**
     * Gets the job to represent.
     * 
     * @return  the job to represent in JSON
     */
    private Job getJob() {
        return this.job;
    }
}
