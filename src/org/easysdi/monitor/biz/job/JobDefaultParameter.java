package org.easysdi.monitor.biz.job;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.deegree.framework.util.DateUtil;
import org.easysdi.monitor.dat.dao.JobDaoHelper;

/**
 * Default value for a job configuration parameter.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2009-03-19
 * 
 */
public final class JobDefaultParameter {

    private String paramName;
    private long   paramId;
    private String stringValue;
    private Object value;
    private String valueType;

    
    
    /**
     * The value types allowed for default parameters.
     * <p>
     * As of version 1.0, it is:
     * <ul>
     * <li>BOOL (boolean)</li>
     * <li>DATE (Calendar, only the date part is pertinent)</li>
     * <li>FLOAT (float)</li>
     * <li>INT (int)</li>
     * <li>STRING (String)</li>
     * <li>TIME (Calendar, only the time part is pertinent)</li>
     * </ul>
     */
    static enum ValueType {
        BOOL,
        DATE,
        FLOAT,
        INT,
        STRING,
        TIME
    }
    
    
    
    /**
     * No-argument constructor. This is intended to be used by the persistance
     * mechanism only.
     */
    private JobDefaultParameter() {
        
    }



    /**
     * Defines the parameter's internal name
     * <p>
     * <i><b>Note:</b> Parameter names shouldn't be changed in the application.
     * This method is intended for internal use.</i>
     * 
     * @param   newParamName    this parameter's internal name
     */
    @SuppressWarnings("unused")
    private void setParamName(String newParamName) {
        this.paramName = newParamName;
    }



    /**
     * Gets this parameter's internal name.
     * 
     * @return  this parameter's internal name
     */
    public String getParamName() {
        return this.paramName;
    }



    /**
     * Defines this parameter's identifier.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use and shouldn't be
     * called directly.</i>
     * 
     * @param   newParamId  the long identifying the parameter
     */
    @SuppressWarnings("unused")
    private void setParamId(long newParamId) {
        this.paramId = newParamId;
    }



    /**
     * Gets this parameter's identifier.
     * 
     * @return  the long identifying this parameter
     */
    public long getParamId() {
        return this.paramId;
    }



    /**
     * Defines this parameter's value from a string.
     * 
     * @param   newStringValue  a string containing the parameter value. It must
     *                          be convertible to the parameter's value type
     */
    private void setStringValue(String newStringValue) {
        this.stringValue = newStringValue;
    }



    /**
     * Gets this parameter's value as a string.
     * 
     * @return  a string containing this parameter's value
     */
    public String getStringValue() {
        return this.stringValue;
    }



    /**
     * Defines this parameter's value.
     * 
     * @param   newValue    an object containing the parameter's value. It must 
     *                      be of the correct type, or must be convertible.
     */
    public void setValue(Object newValue) {
        
        if (this.checkValueType(newValue)) {
            this.value = newValue;
            this.setStringValue(this.convertToStringValue(newValue));
        } else {
            throw new IllegalArgumentException("Invalid value type");
        }
    }
    

    
    /**
     * Verifies if the given value is correct for this parameter's type.
     * 
     * @param   newValue    the value to check
     * @return              <code>true</code> if the value type is correct
     */
    private boolean checkValueType(Object newValue) {
        boolean correctType;
        
        switch (this.getValueTypeFromEnum()) {

            case BOOL:
                correctType = (newValue instanceof Boolean);
                break;

            case DATE:
            case TIME:
                correctType = (newValue instanceof Calendar);
                break;

            case FLOAT:
                correctType = (newValue instanceof Float);
                break;

            case INT:
                correctType = (newValue instanceof Integer);
                break;

            case STRING:
                correctType = (newValue instanceof String);
                break;
                
            default:
                correctType = false;
                break;
                
        }
        
        return correctType;

    }
    
    
    
    /**
     * Converts a value to a string.
     * <p>
     * <i><b>Note:</b> This method alone doesn't guarantee that the value is of
     * the correct type for this parameter. You should first use {@link 
     * #checkValueType(Object)} if you plan to use the result to set this
     * parameter's string value.</i>
     * 
     * @param   newValue    the value to convert  
     * @return              the string equivalent if the conversion succeeded or
     *                      <code>null</code> otherwise
     */
    private String convertToStringValue(Object newValue) {
        String stringResult = null;

        if (this.isValueNumber(newValue)) {
            stringResult = newValue.toString();
            
        } else if (newValue instanceof Calendar) {
            SimpleDateFormat format;
            final ValueType currentValueType = this.getValueTypeFromEnum();
            
            if (JobDefaultParameter.ValueType.DATE == currentValueType) {
                format = new SimpleDateFormat("yyyy-MM-dd");
                stringResult = format.format(((Calendar) newValue).getTime());
                
            } else if (JobDefaultParameter.ValueType.TIME == currentValueType) {
                format = new SimpleDateFormat("HH:mm:ss");
                stringResult = format.format(((Calendar) newValue).getTime());
            }

        } else if (newValue instanceof String) {
            stringResult = (String) newValue;
            
        }
        
        return stringResult;
    }
    
    
    
    /**
     * Determines if the value object passed is of a number type.
     * 
     * @param   object  the value to test
     * @return          <code>true</code> if the value object is a number
     */
    private boolean isValueNumber(Object object) {
        
        return object instanceof Boolean 
                || object instanceof Float 
                || object instanceof Integer; 
    }



    /**
     * Gets this parameter's value.
     * 
     * @return  an object containing the parameter value
     */
    public Object getValue() {
        final String currentStringValue = this.getStringValue();

        if (null == this.value && null != currentStringValue) {

            this.value = this.convertFromStringValue(currentStringValue);
        }

        return this.value;
    }



    /**
     * Defines the type of this parameter's value.
     * <p>
     * The value type string must match the allowed values.
     * <p>
     * <i><b>Note:</b> The parameter type shouldn't be altered in the
     * application. This method is intended for internal purpose.</i>
     * 
     * @param   newValueType    this parameter's value type
     * @see     ValueType
     */
    @SuppressWarnings("unused")
    private void setValueType(String newValueType) {
        final ValueType newType 
            = JobDefaultParameter.ValueType.valueOf(newValueType.toUpperCase());

        if (null == newType) {
            throw new IllegalArgumentException("Unknown parameter value type");
        }

        this.valueType = newValueType.toLowerCase();
    }



    /**
     * Gets this parameter's value type as a string.
     * 
     * @return  this parameter's value type
     * @see     ValueType
     */
    private String getValueType() {
        return this.valueType;
    }



    /**
     * Gets the parameter's value type.
     * 
     * @return the parameter's value type
     */
    public ValueType getValueTypeFromEnum() {

        return JobDefaultParameter.ValueType.valueOf(
                 this.getValueType().toUpperCase());
    }



    /**
     * Attempts to convert a string to the type expected by the parameter.
     * 
     * @param   string  the string to convert
     * @return          an object containing the value if the conversion 
     *                  succeeded or
     *                  <code>null</code> otherwise
     */
    private Object convertFromStringValue(String string) {
        Object realValue;

        switch (this.getValueTypeFromEnum()) {

            case BOOL:
                realValue = Boolean.valueOf(string);
                break;

            case DATE:
                realValue = Date.valueOf(string);
                break;

            case FLOAT:
                realValue = Float.valueOf(string);
                break;

            case INT:
                realValue = Integer.valueOf(string);
                break;

            case STRING:
                realValue = string;
                break;

            case TIME:
                realValue = DateUtil.setTime(Calendar.getInstance(), 
                                             string);
                break;

            default:
                realValue = null;
                break;
        }
        
        return realValue;
    }



    /**
     * Saves this default parameter.
     * 
     * @return <code>true</code> if this parameter has been successfully saved
     */
    public boolean persist() {
        return JobDaoHelper.getJobDao().persistJobDefault(this);
    }

}
