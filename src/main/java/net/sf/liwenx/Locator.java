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

import java.util.Locale;

/**
 * It represents the target of a request.
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class Locator {

	private String page;

	private Locale locale;

	private Device device;

	/**
	 * Constructor
	 * 
	 * @param page page name
	 * @param device device
	 * @param locale locale
	 */
	public Locator(String page, Device device, Locale locale) {
		super();
		this.page = page;
		this.device = device;
		this.locale = locale;
	}

	/**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the device
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(Device device) {
		this.device = device;
	}

	/**
	 * It returns a new locator for the specified page with same device and locale.
	 * 
	 * @param newPage - new page
	 * @return a new locator
	 */
	public Locator newLocator(String newPage){
		return new Locator(newPage, device, locale);
	}
	
}
