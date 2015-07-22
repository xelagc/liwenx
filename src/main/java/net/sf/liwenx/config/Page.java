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
package net.sf.liwenx.config;

import nu.xom.Document;

/**
 * A page in the website.
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class Page extends AbstractConfigDefinition {

	/**
	 * Template xml
	 */
	private Document xml;

	private String templateName;

	private boolean singleComponentPage;

	private boolean privatePage;

	/**
	 * @return the xml
	 */
	public Document getXml() {
		return (Document) xml.copy();
	}

	/**
	 * @param xml the xml to set
	 */
	void setXml(Document xml) {
		this.xml = xml;
	}

	/**
	 * @return the templateName
	 */
	String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the singleComponentPage
	 */
	public boolean isSingleComponentPage() {
		return singleComponentPage;
	}

	/**
	 * @param singleComponentPage the singleComponentPage to set
	 */
	void setSingleComponentPage(boolean singleComponentPage) {
		this.singleComponentPage = singleComponentPage;
	}

	/**
	 * @return the privatePage
	 */
	public boolean isPrivatePage() {
		return privatePage;
	}

	/**
	 * @param privatePage the privatePage to set
	 */
	void setPrivatePage(boolean privatePage) {
		this.privatePage = privatePage;
	}

}
