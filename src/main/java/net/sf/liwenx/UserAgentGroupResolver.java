/**
 * 
 */
package net.sf.liwenx;

import javax.servlet.http.HttpServletRequest;

/**
 * It categorizes a request by user agent.
 * 
 * @author Alejandro Guerra Cabrera
 *
 */
public interface UserAgentGroupResolver {
	
	/**
	 * It returns an user agent group from request, typically by user-agent header.
	 * 
	 * @param request - request
	 * @return user agent group. Null value means default group.
	 */
	String findOutUserAgentGroup(HttpServletRequest request);

}
