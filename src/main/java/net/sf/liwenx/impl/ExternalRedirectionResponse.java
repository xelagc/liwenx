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

import javax.servlet.http.HttpServletResponse;


/**
 * Implementation of an external redirection
 *
 * @author Alejandro Guerra Cabrera
 *
 */
public class ExternalRedirectionResponse extends AbstractLiwenxResponse{

	private String redirectTo;
	
	
	/**
	 * Constructor 
	 * 
	 * @param statusCode - status code
	 * @param redirectTo - target url.
	 */
	ExternalRedirectionResponse(int statusCode, String redirectTo) {
		super(statusCode);
		this.redirectTo = redirectTo;
	}


	/**
	 * Constructor
	 * 
	 * @param redirectTo - target url. 
	 */
	ExternalRedirectionResponse(String redirectTo) {
		super(HttpServletResponse.SC_MOVED_TEMPORARILY);
		this.redirectTo = redirectTo;
	}


	/**
	 * @return the redirectTo
	 */
	public String getRedirectTo() {
		return redirectTo;
	}
}
