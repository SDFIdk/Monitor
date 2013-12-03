package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import org.easysdi.monitor.biz.job.QueryValidationSettings;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.QueryValidationSettingsSerializer;

public class QueryValidationSettingsView extends AbstractJsonView {

	@Override
	protected Boolean isSuccess() {
		return true;
	}

	@Override
	protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
			throws MonitorInterfaceException {
	       if (model.containsKey("queryValidationSettings")) {
	            final QueryValidationSettings queryValidationSettings = (QueryValidationSettings) model.get("queryValidationSettings");   
	            return new QueryValidationSettingsSerializer(queryValidationSettings).serialize(locale,this.getObjectMapper());
	        }   
	        throw new MonitorInterfaceException("An internal error occurred",
	                                            "internal.error");
	}

}
