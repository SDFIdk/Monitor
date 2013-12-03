/**
 * 
 */
package org.easysdi.monitor.biz.job;

import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.dat.dao.LastLogDaoHelper;



/**
 * @author Thomas Bergstedt
 *
 */
public class OverviewLastQueryResult {

		private long lastQueryResultID;
		private String pictureUrl;
		private String xmlResult;
		private String textResult;
		private long queryid;
		private byte[] data;
		private String contentType;

		/**
		 * Dummy constructor
		 */
		public OverviewLastQueryResult() {
		}
		
		/**
		 * @return the queryid
		 */
		public long getQueryid() {
			return queryid;
		}
		/**
		 * @param queryid the queryid to set
		 */
		public void setQueryid(long queryid) {
			this.queryid = queryid;
		}



		public long getLastQueryResultID() {
			return lastQueryResultID;
		}

		public void setLastQueryResultID(long lastQueryResultID) {
			this.lastQueryResultID = lastQueryResultID;
		}

		

		/**
		 * @return the data
		 */
		public byte[] getData() {
			return data;
		}

		/**
		 * @param data the data to set
		 */
		public void setData(byte[] data) {
			this.data = data;
		}

		/**
		 * @return the contentType
		 */
		public String getContentType() {
			return contentType;
		}

		/**
		 * @param contentType the contentType to set
		 */
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		/**
		 * @return the image
		 */
		public String getPictureUrl() {
			return pictureUrl;
		}

		/**
		 * @param image the image to set
		 */
		public void setPictureUrl(String picture) {
			this.pictureUrl = picture;
		}

		/**
		 * @return the xmlResult
		 */
		public String getXmlResult() {
			return xmlResult;
		}

		/**
		 * @param xmlResult the xmlResult to set
		 */
		public void setXmlResult(String xmlResult) {
			this.xmlResult = xmlResult;
		}

		/**
		 * @return the textResult
		 */
		public String getTextResult() {
			return textResult;
		}

		/**
		 * @param textResult the textResult to set
		 */
		public void setTextResult(String textResult) {
			this.textResult = textResult;
		}
		
		/**
		* Create the newlastrequest in DB 
		* 
		* @return <code>true</code> if this overviewpage has successfully been saved
		*/
		public boolean createNewLastRequest()
		{
			return LastLogDaoHelper.getLastLogDao().create(this);
		}
		
		/**
		 *  Maybe noot needed
		 * @return
		 */
		public OverviewLastQueryResult createLastResult()
		{
			final OverviewLastQueryResult newLastQueryResult = new OverviewLastQueryResult();
			// Create const with this objects
			return newLastQueryResult;
		}
		
		public static OverviewLastQueryResult getFromIdString(String idString)
	    {
	        if (StringTools.isNullOrEmpty(idString)) {
	            throw new IllegalArgumentException(
	                   "OverviewLastQuery identifier string can't be null or empty.");
	        }
	        try
	        {
	        	final long queryId = Long.parseLong(idString);
	        	return LastLogDaoHelper.getLastLogDao().exist(queryId);
	        }catch(NumberFormatException e)
	        {
	        	return null;
	        }
	    } 

}
