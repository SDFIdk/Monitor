package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.easysdi.monitor.biz.job.QueryValidationResult;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.QueryValidationResultSerializer;

public class QueryValidationResultView extends AbstractJsonView{

	@Override
	protected Boolean isSuccess() {
		return true;
	}

	@Override
	protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
			throws MonitorInterfaceException {
        if (model.containsKey("queryValidationResult")) {
            final QueryValidationResult queryValidationResult = (QueryValidationResult) model.get("queryValidationResult");   
            return new QueryValidationResultSerializer(queryValidationResult).serialize(locale,this.getObjectMapper());
        }   
        throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
	}

}
