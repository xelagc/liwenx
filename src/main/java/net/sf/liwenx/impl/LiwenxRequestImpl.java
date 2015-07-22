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
package net.sf.liwenx.impl;


import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.liwenx.LiwenxRequest;
import net.sf.liwenx.util.UnifiedLocaleMessageSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


/**
 * Implementation of {@link LiwenxRequest} for non-multipart request.
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class LiwenxRequestImpl extends AbstractLiwenxRequest {

	private static final String MSG_CONVERT_REQUEST_VALUE = "request.param.convert";

	private UnifiedLocaleMessageSource messageSource;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private Map<String, Object> localBag = new HashMap<String, Object>();

	private String userAgentGroup;

	/**
	 * Constructor
	 */
	LiwenxRequestImpl(HttpServletRequest request, HttpServletResponse response,
		UnifiedLocaleMessageSource messageSource, String userAgentGroup) {
		this.request = request;
		this.response = response;
		this.messageSource = messageSource;
		this.userAgentGroup = userAgentGroup;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getCookies()
	 */
	@Override
	public Cookie[] getCookies() {
		return request.getCookies();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		return request.getHeader(name);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getHeaderNames()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Iterable<String> getHeaderNames() {
		return Collections.list((Enumeration<String>) request.getHeaderNames());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getHeaders()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Iterable<String> getHeaders(String name) {
		return Collections.list((Enumeration<String>) request.getHeaders(name));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getRequestValueNames()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Iterable<String> getRequestValueNames() {
		return Collections.list((Enumeration<String>) request.getParameterNames());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getLocalValue(java.lang.String, java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getLocalValue(String name, Class<T> type) {
		return (T) localBag.get(name);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getPathInfo()
	 */
	@Override
	public String getPathInfo() {
		return request.getPathInfo();
	}

	/**
	 * (non-Javadoc)
	 * @see net.sf.liwenx.LiwenxRequest#getQueryString()
	 */
	@Override
	public String getQueryString() {
		return request.getQueryString();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getRequestValue(java.lang.String)
	 */
	@Override
	public String getRequestValue(String name) {
		return request.getParameter(name);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getRequestValues(java.lang.String)
	 */
	@Override
	public String[] getRequestValues(String name) {
		return request.getParameterValues(name);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getSessionValue(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getSessionValue(String name, Class<T> type) {
		final HttpSession session = request.getSession();
		final T value;
		if (session != null) {
			value = (T) session.getAttribute(name);
		} else {
			value = null;
		}
		return value;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getSessionId()
	 */
	public String getSessionId() {
		return request.getSession().getId();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getValue(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T getValue(String name, Class<T> type) {

		// Check for local value
		T value = getLocalValue(name, type);
		if (value == null) {
			// Check for request value
			value = getRequestValue(name, type);
		}

		if (value == null) {
			// Check for session value
			value = getSessionValue(name, type);
		}

		return value;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getRequestValue(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T getRequestValue(String name, Class<T> type) throws IllegalArgumentException {
		T value = null;
		if (String[].class.isAssignableFrom(type)) {
			value = (T) request.getParameterValues(name);
		} else {
			final String objValue = request.getParameter(name);
			if (StringUtils.isBlank(objValue)) {
				value = null;
			} else if (Number.class.isAssignableFrom(type)) {
				try {
					value = type.getConstructor(String.class).newInstance(objValue);
				} catch (Exception e) {
					throw new IllegalArgumentException(messageSource.getMessage(MSG_CONVERT_REQUEST_VALUE, name, type
						.getName()), e);
				}
			} else if (Boolean.class.isAssignableFrom(type)) {
				value = (T) Boolean.valueOf(objValue);
			} else if (UUID.class.isAssignableFrom(type)) {
				value = (T) UUID.fromString(objValue);
			} else {
				value = (T) objValue;
			}
		}

		return value;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getValueAsBoolean(java.lang.String, boolean)
	 */
	@Override
	public boolean getValueAsBoolean(String name, boolean defaultValue) {
		Boolean value = null;
		try {
			value = getValue(name, Boolean.class);
		} catch (IllegalArgumentException e) {
			value = null;
		}

		final boolean boolValue;
		if (value != null) {
			boolValue = value.booleanValue();
		} else {
			boolValue = defaultValue;
		}

		return boolValue;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getValueAsByte(java.lang.String, byte)
	 */
	@Override
	public byte getValueAsByte(String name, byte defaultValue) {
		Byte value = null;
		try {
			value = getValue(name, Byte.class);
		} catch (IllegalArgumentException e) {
			value = null;
		}

		final byte byteValue;
		if (value != null) {
			byteValue = value.byteValue();
		} else {
			byteValue = defaultValue;
		}

		return byteValue;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getValueAsDouble(java.lang.String, double)
	 */
	@Override
	public double getValueAsDouble(String name, double defaultValue) {
		Double value = null;
		try {
			value = getValue(name, Double.class);
		} catch (IllegalArgumentException e) {
			value = null;
		}

		final double doubleValue;
		if (value != null) {
			doubleValue = value.doubleValue();
		} else {
			doubleValue = defaultValue;
		}

		return doubleValue;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getValueAsFloat(java.lang.String, float)
	 */
	@Override
	public float getValueAsFloat(String name, float defaultValue) {
		Float value = null;
		try {
			value = getValue(name, Float.class);
		} catch (IllegalArgumentException e) {
			value = null;
		}
		final float floatValue;
		if (value != null) {
			floatValue = value.floatValue();
		} else {
			floatValue = defaultValue;
		}

		return floatValue;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getValueAsInt(java.lang.String, int)
	 */
	@Override
	public int getValueAsInt(String name, int defaultValue) {
		Integer value = null;
		try {
			value = getValue(name, Integer.class);
		} catch (IllegalArgumentException e) {
			value = null;
		}
		final int intValue;
		if (value != null) {
			intValue = value.intValue();
		} else {
			intValue = defaultValue;
		}

		return intValue;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getValueAsLong(java.lang.String, long)
	 */
	@Override
	public long getValueAsLong(String name, long defaultValue) {
		Long value = null;
		try {
			value = getValue(name, Long.class);
		} catch (IllegalArgumentException e) {
			value = null;
		}

		final long longValue;
		if (value != null) {
			longValue = value.longValue();
		} else {
			longValue = defaultValue;
		}

		return longValue;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getValueAsShort(java.lang.String, short)
	 */
	@Override
	public short getValueAsShort(String name, short defaultValue) {
		Short value = null;
		try {
			value = getValue(name, Short.class);
		} catch (IllegalArgumentException e) {
			value = null;
		}

		final short shortValue;
		if (value != null) {
			shortValue = value.shortValue();
		} else {
			shortValue = defaultValue;
		}

		return shortValue;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getValueAsString(java.lang.String)
	 */
	@Override
	public String getValueAsString(String name) {
		return getValue(name, String.class);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#setLocalValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setLocalValue(String name, Object value) {
		localBag.put(name, value);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#setSessionValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setSessionValue(String name, Object value) {
		request.getSession(true).setAttribute(name, value);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getRemoteAddr()
	 */
	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getRemotePort()
	 */
	@Override
	public int getRemotePort() {
		return request.getRemotePort();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getRequest()
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.LiwenxRequest#getResponse()
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * (non-Javadoc)
	 * @see net.sf.liwenx.LiwenxRequest#isMultipart()
	 */
	@Override
	public boolean isMultipart() {
		return (request instanceof MultipartHttpServletRequest);
	}

	/**
	 * (non-Javadoc)
	 * @see net.sf.liwenx.LiwenxRequest#getFile(java.lang.String)
	 */
	@Override
	public MultipartFile getFile(String name) {
		MultipartFile file;
		if (isMultipart()) {
			file = ((MultipartHttpServletRequest) request).getFile(name);
		} else {
			file = null;
		}
		return file;
	}

	/**
	 * (non-Javadoc)
	 * @see net.sf.liwenx.LiwenxRequest#getFileMap()
	 */
	@Override
	public Map<String, MultipartFile> getFileMap() {
		Map<String, MultipartFile> map;
		if (isMultipart()) {
			map = ((MultipartHttpServletRequest) request).getFileMap();
		} else {
			map = null;
		}
		return map;
	}

	/**
	 * (non-Javadoc)
	 * @see net.sf.liwenx.LiwenxRequest#getFileNames()
	 */
	@Override
	public Iterator<String> getFileNames() {
		Iterator<String> names;
		if (isMultipart()) {
			names = ((MultipartHttpServletRequest) request).getFileNames();
		} else {
			names = null;
		}
		return names;
	}

	/**
	 * @return the userAgentGroup
	 */
	public String getUserAgentGroup() {
		return userAgentGroup;
	}

}
