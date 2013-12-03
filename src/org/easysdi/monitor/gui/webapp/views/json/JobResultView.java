package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.easysdi.monitor.biz.job.JobResult;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import 
    org.easysdi.monitor.gui.webapp.views.json.serializers.JobResultSerializer;

/**
 * Displays the JSON representation of a job result.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class JobResultView extends AbstractJsonView {
    
    /**
     * Creates a new view.
     */
    public JobResultView() {
        
    }
    
    

    /**
     * Generates the JSON code for the job result.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the job result
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the job result to JSON
     *                                      </li>
     *                                      </ul>
     */
    @Override
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale) 
        throws MonitorInterfaceException {

        if (model.containsKey("jobResult")) {
            final JobResult jobResult = (JobResult) model.get("jobResult");
            final ObjectMapper mapper = this.getObjectMapper();

//            if (null != jobResult) {
            return JobResultSerializer.serialize(jobResult, locale, mapper);
/*            } else {
                final Messages i18n = new Messages(locale);
                jsonJobResult = new JSONObject().put(
                       "message", i18n.getMessage("job.noResult"));
            }
*/

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
