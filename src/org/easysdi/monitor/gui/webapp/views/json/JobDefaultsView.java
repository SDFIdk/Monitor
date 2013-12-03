package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.easysdi.monitor.biz.job.JobDefaultParameter;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import 
    org.easysdi.monitor.gui.webapp.views.json.serializers.JobDefaultsSerializer;

/**
 * Displays the job default parameters collection.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class JobDefaultsView extends AbstractJsonView {
    
    /**
     * Creates a new view.
     */
    public JobDefaultsView() {
        
    }
    

    
    /**
     * Generates the JSON code for the job defaults collection.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the job defaults collection
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the job defaults 
     *                                      collection to JSON</li>
     *                                      </ul>
     */
    @SuppressWarnings("unchecked")
    @Override
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale) 
        throws MonitorInterfaceException {

        if (model.containsKey("jobDefaultsCollection")) {
            final JsonNode paramsCollection 
                = JobDefaultsSerializer.serialize(
                      (Map<String, JobDefaultParameter>) model.get(
                           "jobDefaultsCollection"), this.getObjectMapper());

            return paramsCollection;
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
