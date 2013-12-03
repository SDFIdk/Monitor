package org.easysdi.monitor.biz.util;


/**
 * Provides helpful values for hash code calculation.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class HashCodeConstants {

    /**
     * Start value for hash code calculation, minimizing collision risk.
     */
    public static final int SEED = 29;
    
    /**
     * Multiplier for hash code calculation.
     */
    public static final int MULTIPLIER = 37;
    
    
    
    /**
     * Dummy constructor preventing instantiation.
     */
    private HashCodeConstants() {
        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
    }
}
