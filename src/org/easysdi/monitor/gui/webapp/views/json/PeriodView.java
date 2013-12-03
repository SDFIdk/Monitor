/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.easysdi.monitor.biz.job.Period;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.PeriodSerializer;

/**
 * @author berg3428
 *
 */
public class PeriodView extends AbstractJsonView {

	public PeriodView() {
	}

	@Override
	protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
			throws MonitorInterfaceException {
		if (model.containsKey("period")) {
            final Period periodview = (Period) model.get("period");
            return new PeriodSerializer(periodview).serialize(locale,this.getObjectMapper());
		}   
		throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
	}

	@Override
	protected Boolean isSuccess() {
		return true;
	}

}
