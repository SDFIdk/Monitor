package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.QueryValidationResult;

public class QueryValidationResultSerializer {

	private QueryValidationResult queryValidationResult;

	public QueryValidationResultSerializer(QueryValidationResult queryValidationResult) {
		this.setQueryValidationResult(queryValidationResult);
	}

	public void setQueryValidationResult(QueryValidationResult queryValidationResult) {
		this.queryValidationResult = queryValidationResult;
	}

	public QueryValidationResult getQueryValidationResult() {
		return queryValidationResult;
	}

	public JsonNode serialize(Locale locale, ObjectMapper mapper) {
		final ObjectNode jsonOverview = mapper.createObjectNode();

		if(this.getQueryValidationResult()==null) {
			jsonOverview.put("id",(String)null);
			jsonOverview.put("sizeValidationResult", (String)null);
			jsonOverview.put("responseSize", (String)null);
			jsonOverview.put("timeValidationResult", (String)null);
			jsonOverview.put("deliveryTime", (String)null);
			jsonOverview.put("xpathValidationResult", (String)null);
			jsonOverview.put("xpathValidationOutput", (String)null);

		}
		else{
			jsonOverview.put("id", this.getQueryValidationResult().getQueryValidationResultId());
			if(this.getQueryValidationResult().isSizeValidationResult() == null) {
				jsonOverview.put("sizeValidationResult", (String)null);
			}
			else {
				jsonOverview.put("sizeValidationResult", this.getQueryValidationResult().isSizeValidationResult());
			}

			if(this.getQueryValidationResult().getResponseSize() == 0) {
				jsonOverview.put("responseSize", (String)null);
			}
			else {
				jsonOverview.put("responseSize", this.getQueryValidationResult().getResponseSize());
			}


			if(this.getQueryValidationResult().isTimeValidationResult() == null) {
				jsonOverview.put("timeValidationResult", (String)null);
			}
			else {
				jsonOverview.put("timeValidationResult", this.getQueryValidationResult().isTimeValidationResult());
			}

			if( this.getQueryValidationResult().getDeliveryTime() == null) {
				jsonOverview.put("deliveryTime", (String)null);
			}
			else {
				jsonOverview.put("deliveryTime",  this.getQueryValidationResult().getDeliveryTime());
			}

			if( this.getQueryValidationResult().isXpathValidationResult() == null) {
				jsonOverview.put("xpathValidationResult", (String)null);
			}
			else {
				jsonOverview.put("xpathValidationResult",  this.getQueryValidationResult().isXpathValidationResult());
			}

			jsonOverview.put("xpathValidationOutput", this.getQueryValidationResult().getXpathValidationOutput());
		}
		return jsonOverview;

	}

}
