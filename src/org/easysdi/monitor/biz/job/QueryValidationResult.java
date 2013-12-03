package org.easysdi.monitor.biz.job;

import org.easysdi.monitor.dat.dao.QueryValidationResultDaoHelper;

public class QueryValidationResult {

	private long queryValidationResultId;
	private Query parentQuery;
	private long queryId;
	private Boolean sizeValidationResult;
	private long responseSize;
	private Boolean timeValidationResult;
	private Float deliveryTime;
	private Boolean xpathValidationResult;
	private String xpathValidationOutput;
	
	
	public void setQueryValidationResultId(Long queryValidationResultId) {
		this.queryValidationResultId = queryValidationResultId;
	}
	public Long getQueryValidationResultId() {
		return queryValidationResultId;
	}
	public void setParentQuery(Query parentQuery) {
		this.parentQuery = parentQuery;
	}
	public Query getParentQuery() {
		return parentQuery;
	}
	public void setSizeValidationResult(Boolean sizeValidationResult) {
		this.sizeValidationResult = sizeValidationResult;
	}
	public Boolean isSizeValidationResult() {
		return sizeValidationResult;
	}
	public void setResponseSize(long responseSize) {
		this.responseSize = responseSize;
	}
	public long getResponseSize() {
		return responseSize;
	}
	public void setTimeValidationResult(Boolean timeValidationResult) {
		this.timeValidationResult = timeValidationResult;
	}
	public Boolean isTimeValidationResult() {
		return timeValidationResult;
	}
	public void setDeliveryTime(Float deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public Float getDeliveryTime() {
		return deliveryTime;
	}
	public void setXpathValidationResult(Boolean xpathValidationResult) {
		this.xpathValidationResult = xpathValidationResult;
	}
	public Boolean isXpathValidationResult() {
		return xpathValidationResult;
	}
	public void setXpathValidationOutput(String xpathValidationOutput) {
		this.xpathValidationOutput = xpathValidationOutput;
	}
	public String getXpathValidationOutput() {
		return xpathValidationOutput;
	}
	public void setQueryId(long queryId) {
		this.queryId = queryId;
	}
	public long getQueryId() {
		return queryId;
	}
	
	public boolean persist()
	{
	       return QueryValidationResultDaoHelper.getDao().persist(this);
	}
	
}
