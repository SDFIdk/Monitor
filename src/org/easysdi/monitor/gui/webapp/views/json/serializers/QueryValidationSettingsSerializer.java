package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.QueryValidationSettings;

public class QueryValidationSettingsSerializer {
	private QueryValidationSettings queryValidationSettings;

	public QueryValidationSettingsSerializer(QueryValidationSettings queryValidationSettings){
		this.setQueryValidationSettings(queryValidationSettings);
	}

	public void setQueryValidationSettings(QueryValidationSettings queryValidationSettings) {
		this.queryValidationSettings = queryValidationSettings;
	}

	public QueryValidationSettings getQueryValidationSettings() {
		return queryValidationSettings;
	}

	public JsonNode serialize(Locale locale, ObjectMapper mapper) {
		final ObjectNode jsonOverview = mapper.createObjectNode();

		if(this.getQueryValidationSettings()==null)
		{
			jsonOverview.put("id", (String)null);
			jsonOverview.put("queryID",(String)null);
			jsonOverview.put("useSizeValidation", (String)null);
			jsonOverview.put("normSize", (String)null);
			jsonOverview.put("normSizeTolerance", (String)null);
			jsonOverview.put("useTimeValidation", (String)null);
			jsonOverview.put("normTime", (String)null);
			jsonOverview.put("useXpathValidation", (String)null);
			jsonOverview.put("xpathExpression", (String)null);
			jsonOverview.put("expectedXpathOutput", (String)null);
			jsonOverview.put("useTextValidation", (String)null);
			jsonOverview.put("expectedTextMatch", (String)null);
		}
		else {
			jsonOverview.put("id", this.getQueryValidationSettings().getQueryValidationSettingsId());
			jsonOverview.put("queryID",this.getQueryValidationSettings().getQueryId());
			if(this.getQueryValidationSettings().isUseSizeValidation() == null) {
				jsonOverview.put("useSizeValidation", (String)null);
			}
			else {
				jsonOverview.put("useSizeValidation", this.getQueryValidationSettings().isUseSizeValidation());
			}

			if(this.getQueryValidationSettings().getNormSize() == 0) {
				jsonOverview.put("normSize", (String)null);
			}
			else {
				jsonOverview.put("normSize", this.getQueryValidationSettings().getNormSize());
			}

			if(this.getQueryValidationSettings().getNormSizeTolerance() == null) {
				jsonOverview.put("normSizeTolerance", (String)null);
			}
			else {
				jsonOverview.put("normSizeTolerance", this.getQueryValidationSettings().getNormSizeTolerance());
			}

			if(this.getQueryValidationSettings().isUseTimeValidation() == null) {
				jsonOverview.put("useTimeValidation", (String)null);
			}
			else {
				jsonOverview.put("useTimeValidation", this.getQueryValidationSettings().isUseTimeValidation());
			}

			if(this.getQueryValidationSettings().getNormTime() == null) {
				jsonOverview.put("normTime", (String)null);
			}
			else {
				jsonOverview.put("normTime", this.getQueryValidationSettings().getNormTime());
			}

			if(this.getQueryValidationSettings().isUseXpathValidation()== null) {
				jsonOverview.put("useXpathValidation", (String)null);
			}
			else {
				jsonOverview.put("useXpathValidation", this.getQueryValidationSettings().isUseXpathValidation());
			}
			jsonOverview.put("xpathExpression", this.getQueryValidationSettings().getXpathExpression());
			jsonOverview.put("expectedXpathOutput", this.getQueryValidationSettings().getExpectedXpathOutput());
			
			if(this.getQueryValidationSettings().isUseTextValidation()== null) {
				jsonOverview.put("useTextValidation", (String)null);
			}
			else {
				jsonOverview.put("useTextValidation", this.getQueryValidationSettings().isUseTextValidation());
			}
			jsonOverview.put("expectedTextMatch", this.getQueryValidationSettings().getExpectedTextMatch());
			
		}

		return jsonOverview;

	}

}
