package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.easysdi.monitor.biz.job.OverviewQueryView;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.OverviewQuerySerializer;

public class OverviewQueryViewView extends AbstractJsonView {

	public OverviewQueryViewView() {
	}

	@Override
	protected Boolean isSuccess() {
		return true;
	}

	@Override
	protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
			throws MonitorInterfaceException {

        if (model.containsKey("overviewQueryView")) {
            final OverviewQueryView overviewQuery = (OverviewQueryView) model.get("overviewQueryView");   
            return new OverviewQuerySerializer(overviewQuery).serialize(locale,this.getObjectMapper());
        }   
        throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
 	}

}
