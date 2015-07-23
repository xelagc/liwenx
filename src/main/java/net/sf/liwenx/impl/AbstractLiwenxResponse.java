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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import net.sf.liwenx.LiwenxResponse;

/**
 * It implements {@link LiwenxResponse} basic methods.
 *
 * @author Alejandro Guerra Cabrera
 *
 */
public abstract class AbstractLiwenxResponse implements LiwenxResponse{

	private int statusCode;
	
	private List<Cookie> cookies;

	private Map<String,String> headers;
	
	/**
	 * Constructor
	 * @param statusCode - status code.
	 */
	AbstractLiwenxResponse(int statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * Default constructor 
	 */
	AbstractLiwenxResponse() {
		statusCode = HttpServletResponse.SC_OK;
	}
	
	/** (non-Javadoc)
	 * @see net.sf.liwenx.LiwenxResponse#addCookie(javax.servlet.http.Cookie)
	 */
	@Override
	public void addCookie(Cookie cookie) {
		if(cookies==null){
			cookies = new LinkedList<Cookie>();
		}
		cookies.add(cookie);
	}
	
	void addCookies(int pos,List<Cookie> cookies){
		if(this.cookies==null){
			this.cookies = new LinkedList<Cookie>();
		}
		this.cookies.addAll(pos, cookies);
	}

	/** (non-Javadoc)
	 * @see net.sf.liwenx.LiwenxResponse#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) {
		if(headers==null){
			headers=new HashMap<String, String>();
		}
		headers.put(name, value);
	}

	/** (non-Javadoc)
	 * @see net.sf.liwenx.LiwenxResponse#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		return statusCode;
	}
	
	/**
	 * @param statusCode the statusCode to set
	 */
	void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * @return the headers
	 */
	Map<String, String> getHeaders() {
		return headers;
	}
	
	/**
	 * @return the cookies
	 */
	List<Cookie> getCookies() {
		return cookies;
	}
}
