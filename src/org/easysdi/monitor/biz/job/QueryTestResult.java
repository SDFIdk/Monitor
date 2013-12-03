/**
 * 
 */
package org.easysdi.monitor.biz.job;



import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.dat.dao.QueryTestResultDaoHelper;

//import org.easysdi.monitor.dat.dao.LastLogDaoHelper;

/**
 * @author berg3428
 *
 */
public class QueryTestResult {

	private long queryid;
	private byte[] data;
	private String contentType;
	private String xpathresult;
    private float    responseDelay;
    private long    responseSize;
	
	/**
	 * 
	*/
	public QueryTestResult() {
	}

	public long getQueryid() {
		return queryid;
	}

	public void setQueryid(long queryid) {
		this.queryid = queryid;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getXpathresult() {
		return xpathresult;
	}

	public void setXpathresult(String xpathResult) {
		xpathresult = xpathResult;
	}
	
	/**
	 * @return the responseDelay
	 */
	public float getResponseDelay() {
		return responseDelay;
	}

	/**
	 * @param responseDelay the responseDelay to set
	 */
	public void setResponseDelay(float responseDelay) {
		this.responseDelay = responseDelay;
	}

	/**
	 * @return the responseSize
	 */
	public long getResponseSize() {
		return responseSize;
	}

	/**
	 * @param responseSize the responseSize to set
	 */
	public void setResponseSize(long responseSize) {
		this.responseSize = responseSize;
	}

	/**
	* Create a new query test result 
	* 
	* @return <code>true</code>
	*/
	public boolean saveTestResult()
	{
		return QueryTestResultDaoHelper.getQueryTestResultDao().persistResult(this);
	}
	
	public boolean delete()
	{
		return QueryTestResultDaoHelper.getQueryTestResultDao().delete(this);
	}
	
	public static QueryTestResult getFromIdString(String idString)
	{
		 if (StringTools.isNullOrEmpty(idString)) {
	            throw new IllegalArgumentException(
	                   "Period identifier string can't be null or empty.");
	     }
		 return QueryTestResultDaoHelper.getQueryTestResultDao().getFromIdString(idString);
	}
}
