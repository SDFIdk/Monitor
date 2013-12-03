package org.easysdi.monitor.gui.webapp;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * Manages the search parameters for log entries as specified in a web request.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class LogSearchParams {

    private Calendar       minDate;
    private Calendar       maxDate;
    private Integer        maxResults;
    private Integer        startIndex;
    private final String[] acceptedDateFormats = new String[] {"yyyy-MM-dd"};



    /**
     * Constructor used by the persistence mechanism.
     * 
     * @see #createFromRequest(HttpServletRequest)
     */
    private LogSearchParams() {
        
    }



    /**
     * Defines the date starting from which the logs shall be searched.
     * 
     * @param   newMinDate                  the string containing the lower 
     *                                      bound date
     * @throws  MonitorInterfaceException   the date format is not YYYY-MM-DD
     */
    private void setMinDate(String newMinDate) 
        throws MonitorInterfaceException {

        try {
            this.minDate = this.parseDateString(newMinDate);

        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(
                    "Minimum date format is invalid", "log.minDate.invalid");
        }
    }



    /**
     * Gets the date starting from which the logs shall be searched.
     * 
     * @return  the lower bound date
     */
    public Calendar getMinDate() {
        return this.minDate;
    }



    /**
     * Defines the date up to which the logs shall be searched.
     * 
     * @param   newMaxDate                  the string containing the upper 
     *                                      bound date
     * @throws  MonitorInterfaceException   the date format isn't YYYY-MM-DD
     */
    private void setMaxDate(String newMaxDate) 
        throws MonitorInterfaceException {

        try {
            this.maxDate = this.parseDateString(newMaxDate);

        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(
                    "Maximum date format is invalid", "log.maxDate.invalid");
        }
    }



    /**
     * Gets the date up to which the logs shall be searched.
     * 
     * @return  the upper bound date
     */
    public Calendar getMaxDate() {
        return this.maxDate;
    }



    /**
     * Defines the maximum number of entries to return.
     * 
     * @param   newMaxResults               a string containing the maximum 
     *                                      number of entries
     * @throws  MonitorInterfaceException   the number format is invalid   
     */
    private void setMaxResults(String newMaxResults)
        throws MonitorInterfaceException {

        try {
            this.maxResults = this.parseIntegerString(newMaxResults);

        } catch (IllegalArgumentException e) {
            
            throw new MonitorInterfaceException(
                    "Maximum result number format is invalid", 
                    "log.maxResults.invalid");
        }
    }



    /**
     * Gets the maximum number of entries to return.
     * 
     * @return  the maximum number of entries
     */
    public Integer getMaxResults() {
        return this.maxResults;
    }



    /**
     * Defines the 0-based index of the first entry to return among those 
     * meeting the other criteria.
     * <p>
     * For instance, if this parameter is set to 5, the 5 most recent log 
     * entries meeting the other criteria will be ignored.
     * <p>
     * This is primarily useful for paging purposes.
     * 
     * @param   newStartIndex               the index of the first entry
     * @throws  MonitorInterfaceException   the number format is invalid
     */
    private void setStartIndex(String newStartIndex)
        throws MonitorInterfaceException {

        try {
            this.startIndex = this.parseIntegerString(newStartIndex);

        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(
                    "Start index number format is invalid", 
                    "log.startIndex.invalid");
        }
    }



    /**
     * Gets the 0-based index of the first entry to return among those 
     * meeting the other criteria.
     * 
     * @return  the index of the first entry
     */
    public Integer getStartIndex() {
        return this.startIndex;
    }


    /**
     * Builds a log search parameters object from a servlet request.
     * <p>
     * The considered request parameters are:
     * <p>
     * <table border="1">
     * <tbody><tr>
     * <th align="left">Parameter</th>
     * <th align="left">Value</th>
     * <th align="left">Description</th>
     * </tr><tr>
     * <td>minDate</td>
     * <td>Date (YYYY-MM-DD)</td>
     * <td>
     * Date from which the logs are fetched. It is included in the span.
     * </td>
     * </tr><tr>
     * <td>maxDate</td>
     * <td>Date (YYYY-MM-DD)</td>
     * <td>
     * Date up to which the logs are fetched. It is included in the span.
     * </td>
     * </tr><tr>
     * <td>maxResults</td>
     * <td>Strictly positive integer</td>
     * <td>Maximum number of log entries to fetch</td>
     * </tr><tr>
     * <td>startIndex</td>
     * <td>Strictly positive integer</td>
     * <td>
     * Index of the first entry to fetch. This is essentially useful for paging
     * purposes.
     * </td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * All parameters are optional.
     * 
     * @param   requestParams               the request parameters specifying
     *                                      the log subset to fetch
     * @return                              the search parameters object
     * @throws MonitorInterfaceException    one of the parameters value is 
     *                                      invalid    
     */
    public static LogSearchParams createFromParametersMap(
            Map<String, String> requestParams)
        throws MonitorInterfaceException {

        final LogSearchParams params = new LogSearchParams();
        params.setMinDate(requestParams.get("minDate"));
        params.setMaxDate(requestParams.get("maxDate"));
        params.setMaxResults(requestParams.get("maxResults"));
        params.setStartIndex(requestParams.get("startIndex"));

        return params;
    }



    /**
     * Parses a string looking for a YYYY-MM-DD date.
     * 
     * @param   dateString  the string containing the date to extract
     * @return              the date if the string matches the format or<br>
     *                      <code>null</code> otherwise
     */
    private Calendar parseDateString(String dateString) {

        if (!StringUtils.isBlank(dateString)) {
            final Calendar parsedDate = Calendar.getInstance();

            try {
                parsedDate.setTime(
                    DateUtils.parseDate(dateString, this.acceptedDateFormats));

                return parsedDate;

            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format");
            }
        }

        return null;
    }



    /**
     * Parses a string looking for an integer value.
     * 
     * @param   intString   the string containing the integer to extract
     * @return              the integer if the format is valid or<br>
     *                      <code>null</code> otherwise
     */
    private Integer parseIntegerString(String intString) {

        if (!StringUtils.isBlank(intString)) {

            try {
                return NumberUtils.createInteger(intString);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer format");
            }
        }

        return null;
    }
    
}
