/*
 * LIWENX - LIght Web ENgine by Xela
 * Copyright (C) 2010 Alejandro Guerra Cabrera
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
package net.sf.liwenx;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

/**
 * LIWENX request. It wraps {@link HttpServletRequest} and adds some utility methods.
 * 
 * @author Alejandro Guerra Cabrera
 *
 */
public interface LiwenxRequest {
	
	Cookie[] getCookies();
	
	String getHeader(String name);
	
	Iterable<String> getHeaders(String name);

	Iterable<String> getHeaderNames();
	
	String getPathInfo();
	
	String getQueryString();
	
	Locator getRequestedLocator();
	
	Locator getCurrentLocator();
	
	String getRequestValue(String name);
	
	String[] getRequestValues(String name);
	
	Iterable<String> getRequestValueNames();
	
	<T> T getSessionValue(String name, Class<T> type);
	
	<T> T getLocalValue(String name, Class<T> type);

	<T> T getRequestValue(String name, Class<T> type) throws IllegalArgumentException;
	
	<T> T getValue(String name, Class<T> type);
	
	String getValueAsString(String name);
	byte getValueAsByte(String name, byte defaultValue);
	short getValueAsShort(String name, short defaultValue);
	int getValueAsInt(String name, int defaultValue);
	long getValueAsLong(String name, long defaultValue);
	float getValueAsFloat(String name, float defaultValue);
	double getValueAsDouble(String name, double defaultValue);
	boolean getValueAsBoolean(String name, boolean defaultValue);
	
	void setLocalValue(String name, Object value);
	void setSessionValue(String name, Object value);
	
	String getRemoteAddr();
	int getRemotePort();
	
	/**
	 * It returns current session id. If there's no session, it creates it.
	 * @return session id.
	 */
	String getSessionId();
	
	/**
	 * It returns original {@link HttpServletRequest}.
	 * @return original {@link HttpServletRequest}
	 */
	HttpServletRequest getRequest();
	/**
	 * It returns associated {@link HttpServletResponse}.
	 * @return associated {@link HttpServletResponse}
	 */
	HttpServletResponse getResponse();
	
	boolean isMultipart();
	
	MultipartFile getFile(String name);
	
	Iterator<String> getFileNames();
	
	Map<String, MultipartFile> getFileMap();
	
	String getUserAgentGroup();
}
