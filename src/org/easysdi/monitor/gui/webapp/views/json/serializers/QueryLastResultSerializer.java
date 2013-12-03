package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.util.Locale;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.OverviewLastQueryResult;

public class QueryLastResultSerializer {
	private OverviewLastQueryResult lastResult;

	public QueryLastResultSerializer(OverviewLastQueryResult lastResult){
		this.setLastResult(lastResult);
	}

	public JsonNode serialize(Locale locale, ObjectMapper mapper) {
		final ObjectNode jsonOverview = mapper.createObjectNode();
		if(this.getLastResult() != null){
			jsonOverview.put("text_result", this.getLastResult().getTextResult());
			jsonOverview.put("content_type", this.getLastResult().getContentType());
		}
		else
		{
			jsonOverview.put("text_result", (String)null);
			jsonOverview.put("content_type", (String)null);
		}
		return jsonOverview;
	}

	public void setLastResult(OverviewLastQueryResult lastResult) {
		this.lastResult = lastResult;
	}

	public OverviewLastQueryResult getLastResult() {
		return lastResult;
	}
}
