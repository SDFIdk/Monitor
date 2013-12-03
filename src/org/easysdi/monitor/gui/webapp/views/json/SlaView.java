/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.SlaSerializer;

/**
 * @author berg3428
 *
 */
public class SlaView extends AbstractJsonView {

	/**
     * Creates a new slaview.
     */
    public SlaView() {
    }
	
	@Override
	protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
			throws MonitorInterfaceException {		  
		if (model.containsKey("sla")) {
	            final Sla slaview = (Sla) model.get("sla");
	            return new SlaSerializer(slaview).serialize(false,locale,this.getObjectMapper());
	    }   
	    throw new MonitorInterfaceException("An internal error occurred",
	                                            "internal.error");
	}

	@Override
	protected Boolean isSuccess() {
		return true;
	}
}