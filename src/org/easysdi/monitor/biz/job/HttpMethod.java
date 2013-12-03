package org.easysdi.monitor.biz.job;

/**
 * The HTTP method used to poll a web service.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * 
 */
public class HttpMethod extends AbstractValueObject {

    /**
     * No-argument constructor, used by the persistance mechanism.
     */
    protected HttpMethod() {

    }



    /**
     * Gets the HttpMethod object representing a given method, if it exists.
     * 
     * @param   methodName  the HTTP method name
     * @return              the HttpMethod object if it exists or<br>
     *                      <code>null</code> otherwise
     */
    public static HttpMethod getObject(String methodName) {
        return (HttpMethod) HttpMethod.getObject(
             methodName, "org.easysdi.monitor.biz.job.HttpMethod");
    }

}
