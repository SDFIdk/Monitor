package org.easysdi.monitor.biz.job;

import org.easysdi.monitor.dat.dao.QueryValidationSettingsDaoHelper;

public class QueryValidationSettings {
	
	private long queryValidationSettingsId;
	private Query parentQuery;
	private long queryId;
	private Boolean useSizeValidation;
	private long normSize;
	private Float normSizeTolerance;
	private Boolean useTimeValidation;
	private Float normTime;
	private Boolean useXpathValidation;
	private String xpathExpression;
	private String expectedXpathOutput;
	private Boolean useTextValidation;
	private String expectedTextMatch;
	
	public void setQueryValidationSettingsId(Long queryValidationSettingsId) {
		this.queryValidationSettingsId = queryValidationSettingsId;
	}
	public Long getQueryValidationSettingsId() {
		return queryValidationSettingsId;
	}
	public void setParentQuery(Query parentQuery) {
		this.parentQuery = parentQuery;
	}
	public Query getParentQuery() {
		return parentQuery;
	}
	public void setUseSizeValidation(Boolean useSizeValidation) {
		this.useSizeValidation = useSizeValidation;
	}
	public Boolean isUseSizeValidation() {
		return useSizeValidation;
	}
	public void setNormSize(long normSize) {
		this.normSize = normSize;
	}
	public long getNormSize() {
		return normSize;
	}
	public void setNormSizeTolerance(Float normSizeTolerance) {
		this.normSizeTolerance = normSizeTolerance;
	}
	public Float getNormSizeTolerance() {
		return normSizeTolerance;
	}
	public void setUseTimeValidation(Boolean useTimeValidation) {
		this.useTimeValidation = useTimeValidation;
	}
	public Boolean isUseTimeValidation() {
		return useTimeValidation;
	}
	public void setNormTime(Float normTime) {
		this.normTime = normTime;
	}
	public Float getNormTime() {
		return normTime;
	}
	public void setUseXpathValidation(Boolean useXpathValidation) {
		this.useXpathValidation = useXpathValidation;
	}
	public Boolean isUseXpathValidation() {
		return useXpathValidation;
	}
	public void setXpathExpression(String xpathExpression) {
		this.xpathExpression = xpathExpression;
	}
	public String getXpathExpression() {
		return xpathExpression;
	}
	public void setExpectedXpathOutput(String expectedXpathOutput) {
		this.expectedXpathOutput = expectedXpathOutput;
	}
	public String getExpectedXpathOutput() {
		return expectedXpathOutput;
	}
	public void setUseTextValidation(Boolean useTextValidation) {
		this.useTextValidation = useTextValidation;		
	}
	public Boolean isUseTextValidation() {
		return useTextValidation;
	}
	public void setExpectedTextMatch (String expectedTextMatch) {
		this.expectedTextMatch = expectedTextMatch;
	}
	public String getExpectedTextMatch() {
		return expectedTextMatch;
	}
	public void setQueryId(long queryId) {
		this.queryId = queryId;
	}
	public long getQueryId() {
		return queryId;
	}
	
	public boolean persist()
	{
	       return QueryValidationSettingsDaoHelper.getDao().persist(this);
	}
	
	public static QueryValidationSettings createDefault(long parentQueryId)
	{
		final QueryValidationSettings qvs = new QueryValidationSettings();
		qvs.setQueryId(parentQueryId);
		qvs.setUseSizeValidation(false);
		qvs.setUseTimeValidation(false);
		qvs.setUseXpathValidation(false);
		qvs.setUseTextValidation(false);
		
		return qvs;
	}
}
