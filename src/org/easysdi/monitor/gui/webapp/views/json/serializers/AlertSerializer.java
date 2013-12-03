package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.alert.Alert;

/**
 * Transforms an alert into JSON.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public class AlertSerializer {
    
    /**
     * Dummy constructor used to prevent instantiation.
     */
    private AlertSerializer() {
        throw new UnsupportedOperationException(
                                           "This class can't be instantiated.");
    }
    
    
    
    /**
     * Generates the JSON representation of an alert.
     * 
     * @param   alert   the alert to represent in JSON
     * @param   locale  the locale indicating the language in which localizable
     *                  information should be shown
     * @param   mapper  the object used to map the data to JSON nodes
     * @return          a JSON node containing the data for the alert
     */
    public static JsonNode serialize(Alert alert, Locale locale, 
                                       ObjectMapper mapper) {
        final ObjectNode jsonAlert = mapper.createObjectNode();
        final SimpleDateFormat dateFormat 
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonAlert.put("alertId", alert.getAlertId());
        jsonAlert.put("oldStatus",
                      alert.getOldStatus().getDisplayString(locale));
        jsonAlert.put("oldStatusCode",
                alert.getOldStatus().getStatusValue().name());
        jsonAlert.put("newStatus",
                      alert.getNewStatus().getDisplayString(locale));
        jsonAlert.put("newStatusCode",
                alert.getNewStatus().getStatusValue().name());
        jsonAlert.put("jobId", alert.getParentJob().getJobId());
        jsonAlert.put("isExposedToRss", alert.isExposedToRss());
        jsonAlert.put("dateTime", dateFormat.format(alert.getTime().getTime()));
        jsonAlert.put("cause", alert.getCause());
        jsonAlert.put("content_type",alert.getContentType());
        if(alert.getHttpCode() != null)
        {
        	jsonAlert.put("httpCode", alert.getHttpCode());
        }else
        {
        	jsonAlert.put("httpCode", "");
        }
        jsonAlert.put("responseDelay", alert.getResponseDelay());

        return jsonAlert;
    }
}
