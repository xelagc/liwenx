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

import nu.xom.Element;

/**
 * XML response.
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class XmlResponse extends AbstractLiwenxResponse {

	private Element xml;

	/**
	 * Constructor
	 * 
	 * @param xml - response xml.
	 */
	XmlResponse(Element xml) {
		super();
		this.xml = xml;
	}

	/**
	 * Constructor
	 * 
	 * @param statusCode - status code.
	 * @param xml - response xml.
	 */
	XmlResponse(int statusCode, Element xml) {
		super(statusCode);
		this.xml = xml;
	}

	/**
	 * @return the xml
	 */
	Element getXml() {
		return xml;
	}

}
