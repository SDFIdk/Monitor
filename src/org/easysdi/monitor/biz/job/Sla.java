/**
 * 
 */
package org.easysdi.monitor.biz.job;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.dat.dao.SlaDaoHelper;
/**
 * A sla definition 
 * <p>
 * A sla time is an object that can have multiple (include/exclude) periods 
 * 
 * @author Thomas Berstedt - arx iT
 * @version 1.0, 2011-04-14
 * @see Period
 */
public class Sla {
	private long slaId;
	private String name;
	private Map<Long,Period> slaPeriods;
	private boolean excludeWorst;
	private boolean measureTimeToFirst;
	
	/**
     * No-argument constructor.
    */
	public Sla()
	{
		
	}
	
	public Sla(String name, boolean excludeWorst, boolean
			measureTimeToFirst)
	{
		this.name = name;
		this.excludeWorst =excludeWorst;
		this.measureTimeToFirst = measureTimeToFirst;
		
	}
	
	/**
     * Gets this sla's identifier.
     * 
     * @return the long that uniquely identify this sla
     */
	public long getSlaId() {
		return slaId;
	}
	
    /**
     * Defines this sla's identifier.
     * <p>
     * <i><b>Note:</b> This method shouldn't be used directly. The identifier is
     * usually assigned by the persistence mechanism.</i>
     * 
     * @param   slaId  the long that uniquely identify this sla
     */
	public void setSlaId(long slaId) {
    	  if (1 > slaId) {
              throw new IllegalArgumentException("Invalid sla identifier");
          }
    	this.slaId = slaId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Test if the current sla is valid
	 * @return if the sla is valid
	 */
	 public boolean isValid() {
		 boolean valid = this.name != null && !StringUtils.isBlank(this.name);
		 return valid;
	 }
	
    /**
     * Gets the periods defined for this sla. 
     * @return  @return a collection containing all this sla's periods
     */
	public Collection<Period> getSlaPeriodList() {
        final Map<Long, Period> periodMap = this.getSlaPeriods();
        if (null != periodMap) {
            return Collections.unmodifiableCollection(periodMap.values());
        }
        return Collections.unmodifiableCollection(new HashSet<Period>());
    }
    
	/**
     * Gets the queries defined for this job.
     * <p>
     * This method returns a map with the requests' identifiers as key, to allow
     * quick search.
     * 
     * @return  a map containing this job's queries
     */
    private Map<Long, Period> getSlaPeriods() {
        return this.slaPeriods;
    }
	
    /**
     * Defines this sla's periods.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purpose and 
     * shouldn't be called directly.</i>
     * 
     * @param   slaPeriods  a map containing this sla's periods, with their 
     *                      identifier as key
     */
	public void setSlaPeriods(Map<Long, Period> slaPeriods) {
        this.slaPeriods = slaPeriods;
    }

	/**
     * Gets if inspire are activated for the sla.
     * 
     * @return  <code>true</code> if true 10% of the worst results is removed
     */
	public boolean isExcludeWorst() {
		return excludeWorst;
	}
	
	/**
     * Defines if inspire are activated for the sla.
     * <p>
     * The 10% worst results is removed is true
     * 
     * @param excludeWorst   <code>true</code> to activate inspire
     */
	public void setExcludeWorst(boolean excludeWorst) {
		this.excludeWorst = excludeWorst;
	}
	
	
	/**
	 * Gets if the delivery time should be measure from the first byte
	 * 
	 * @return <code>true</code> false if to measure from last byte
	 */
	public boolean isMeasureTimeToFirst() {
		return measureTimeToFirst;
	}

	/**
	 * Sets the if the measurement should be from first byte or last
	 * 
	 * @param measureTimeToFirst <code>true</code> to activate measurement on first byte received 
	 */
	public void setMeasureTimeToFirst(boolean measureTimeToFirst) {
		this.measureTimeToFirst = measureTimeToFirst;
	}
	
	 /**
     * Erases this sla
     * 
     * @return <code>true</code> if this sla has been successfully deleted
     */
    public boolean delete() {
        return SlaDaoHelper.getSlaDao().delete(this);
    }
    
    /**
     * Saves this sla.
     * 
     * @return <code>true</code> if this overview has successfully been saved
     */
    public boolean persist() {
        return SlaDaoHelper.getSlaDao().persistSla(this);
    }

    /**
     * Gets a sla from an identifying string.
     * 
     * @param   idString    a string containing either the sla's identifier or 
     *                      name
     * @return              the sla, if it's been found or<br>
     *                      <code>null</code> otherwise
     */
    public static Sla getFromIdString(String idString) {

        if (StringTools.isNullOrEmpty(idString)) {
            throw new IllegalArgumentException(
                   "Sla identifier string can't be null or empty.");
        }

        return SlaDaoHelper.getSlaDao().getSlaFromIdString(idString);
    }

}
