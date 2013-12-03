/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.easysdi.monitor.biz.job.Overview;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.OverviewSerializer;

/**
 * @author BERG3428
 *
 */
public class OverviewView extends AbstractJsonView {

	/**
     * Creates a new view.
     */
    public OverviewView() {
        
    }
    
    /**
     * Generates the JSON code for the job defaults collection.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the job
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the job to JSON</li>
     *                                      </ul>
     */
    @Override
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale) 
        throws MonitorInterfaceException {

        if (model.containsKey("overview")) {
            final Overview overview = (Overview) model.get("overview");   
            return new OverviewSerializer(overview).serialize(true,true,locale,this.getObjectMapper());
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
