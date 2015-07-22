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

import net.sf.liwenx.Locator;
import nu.xom.Element;

import com.google.gson.GsonBuilder;

/**
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public final class LiwenxResponseFactory {

	/**
	 * Default constructor
	 */
	private LiwenxResponseFactory() {
		// private constructor
	}

	/**
	 * It returns an xml response instance.
	 * 
	 * @param xml - main xml element.
	 * @return xml response.
	 */
	public static XmlResponse xmlResponse(Element xml) {
		return new XmlResponse(xml);
	}
	
	/**
	 * It returns an xml response instance.
	 * 
	 * @param statusCode - response status code.
	 * @param xml - main xml element.
	 * @return xml response.
	 */
	public static XmlResponse xmlResponse(int statusCode, Element xml) {
		return new XmlResponse(statusCode, xml);
	}

	/**
	 * It returns an json response instance
	 * 
	 * @return json response
	 */
	public static JsonResponse jsonResponse(){
		return new JsonResponse();
	}

	/**
	 * It returns an json response instance
	 * 
	 * @param builder - GsonBuilder
	 * @return json response
	 */
	public static JsonResponse jsonResponse(GsonBuilder builder){
		return new JsonResponse(builder);
	}

	/**
	 * It returns an json response instance
	 * 
	 * @param statusCode - response status code.
	 * @return json response
	 */
	public static JsonResponse jsonResponse(int statusCode){
		return new JsonResponse(statusCode);
	}
	
	/**
	 * It returns an json response instance
	 * 
	 * @param statusCode - response status code.
	 * @param builder - GsonBuilder
	 * @return json response
	 */
	public static JsonResponse jsonResponse(int statusCode, GsonBuilder builder){
		return new JsonResponse(statusCode, builder);
	}
	
	/**
	 * It returns a binary response instance.
	 * 
	 * @param inputStream - intputStream from which read bytes of response.
	 * @param mimeType - mime type.
	 * @return binary response
	 */
	public static BinaryResponse binaryResponse(InputStream inputStream, MimeType mimeType) {
		return new BinaryResponse(inputStream, mimeType);
	}

	/**
	 * It returns a binary response instance.
	 * 
	 * @param statusCode - status code.
	 * @param inputStream - intputStream from which read bytes of response.
	 * @param mimeType - mime type.
	 * @return binary response
	 */
	public static BinaryResponse binaryResponse(int statusCode, InputStream inputStream, MimeType mimeType) {
		return new BinaryResponse(statusCode, inputStream, mimeType);
	}

	/**
	 * It returns an internal redirection response.
	 * 
	 * @param locator - the next locator.
	 * @return internal redirection response.
	 */
	public static InternalRedirectionResponse internalRedirection(Locator locator) {
		return new InternalRedirectionResponse(locator);
	}

	/**
	 * It returns an internal redirection response.
	 * 
	 * @param statusCode - status code.
	 * @param locator - the next locator.
	 * @return internal redirection response.
	 */
	public static InternalRedirectionResponse internalRedirection(int statusCode, Locator locator) {
		return new InternalRedirectionResponse(statusCode, locator);
	}

	/**
	 * It returns an external redirection response.
	 * 
	 * @param redirectTo - target url.
	 * @return external redirection response.
	 */
	public static ExternalRedirectionResponse externalRedirection(String redirectTo) {
		return new ExternalRedirectionResponse(redirectTo);
	}

	/**
	 * It returns an external redirection response.
	 * 
	 * @param statusCode - status code.
	 * @param redirectTo - target url.
	 * @return external redirection response.
	 */
	public static ExternalRedirectionResponse externalRedirection(int statusCode, String redirectTo) {
		return new ExternalRedirectionResponse(statusCode, redirectTo);
	}

}
