package org.easysdi.monitor.gui.webapp.views.json;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.JobSerializer;

/**
 * Displays the JSON representation of a job collection.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public class JobsCollectionView extends AbstractJsonView {

    /**
     * Creates a new view.
     */
    public JobsCollectionView() {
        
    }
    
    
    /**
     * Generates the JSON code for the job collection.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the job collection
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the job collection to 
     *                                      JSON</li>
     *                                      </ul>
     */
    @Override
    @SuppressWarnings("unchecked")
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale) 
        throws MonitorInterfaceException {

        if (model.containsKey("jobList")) {
            final ObjectMapper mapper = this.getObjectMapper();
            final ArrayNode jobsList = mapper.createArrayNode();

            for (Job job : (List<Job>) model.get("jobList")) {
                jobsList.add(new JobSerializer(job).serialize(true, true, 
                                                              locale, mapper));

            }

            return jobsList;

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
