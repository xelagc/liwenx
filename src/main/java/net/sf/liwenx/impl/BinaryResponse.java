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

import java.io.InputStream;

import javax.activation.MimeType;

/**
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class BinaryResponse extends AbstractLiwenxResponse {

	private InputStream inputStream;

	private MimeType mimeType;

	/**
	 * Constructor
	 * 
	 * @param inputStream - intputStream from which read bytes of response.
	 * @param mimeType - mime type.
	 */
	BinaryResponse(InputStream inputStream, MimeType mimeType) {
		super();
		this.inputStream = inputStream;
		this.mimeType = mimeType;
	}

	/**
	 * Constructor
	 * 
	 * @param statusCode - status code.
	 * @param inputStream - intputStream from which read bytes of response.
	 * @param mimeType - mime type.
	 */
	BinaryResponse(int statusCode, InputStream inputStream, MimeType mimeType) {
		super(statusCode);
		this.inputStream = inputStream;
		this.mimeType = mimeType;

	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}
	
	/**
	 * @return the mimeType
	 */
	public MimeType getMimeType() {
		return mimeType;
	}
}
