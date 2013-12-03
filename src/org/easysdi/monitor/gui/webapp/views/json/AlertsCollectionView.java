package org.easysdi.monitor.gui.webapp.views.json;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.easysdi.monitor.biz.alert.Alert;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.AlertSerializer;

/**
 * Displays a collection of alerts in JSON format.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class AlertsCollectionView extends AbstractJsonView {

    /**
     * Creates a new view.
     */
    public AlertsCollectionView() {
        
    }
    
    
    
    /**
     * Generates the alerts collection JSON.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the alerts collection
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the alerts collection to
     *                                      JSON</li>
     *                                      </ul>                   
     */
    @SuppressWarnings("unchecked")
    @Override
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
        throws MonitorInterfaceException {

        if (model.containsKey("alertsCollection")) {
            final List<Alert> alertsList 
                = (List<Alert>) model.get("alertsCollection");
            final ObjectMapper mapper = this.getObjectMapper();
            final ArrayNode jsonAlerts = mapper.createArrayNode();

            for (Alert alert : alertsList) {
                jsonAlerts.add(AlertSerializer.serialize(alert, locale, 
                                                         mapper));
            }

            return jsonAlerts;
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
