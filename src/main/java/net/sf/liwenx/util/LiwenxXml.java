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
package net.sf.liwenx.util;

import nu.xom.XPathContext;

/**
 * Utility class for Liwenx XML
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public final class LiwenxXml {

	/**
	 * Private constructor
	 */
	private LiwenxXml() {
		// nothing to do
	}

	/**
	 * LIWENX namespace uri
	 */
	public static final String LIWENX_NS_URI = "http://liwenx.sourceforge.net";

	/**
	 * LIWENX namespace prefix
	 */
	public static final String LIWENX_NS_PREFIX = "liwenx";
	
	/**
	 * XPathContext within LIWENX namespace
	 */
	public static final XPathContext LIWENX_XP_CONTEXT = new XPathContext(LIWENX_NS_PREFIX, LIWENX_NS_URI);
	
	/**
	 * Element name for page definitions
	 */
	public static final String PAGE = "page";

	/**
	 * Element name for page definitions
	 */
	public static final String ERROR_PAGE = "error";

	/**
	 * Element name for template definitions
	 */
	public static final String TEMPLATE = "template";

	/**
	 * Element name for post router definitions
	 */
	public static final String POST_ROUTER = "post-router";

	/**
	 * Name attribute in xml configurations
	 */
	public static final String NAME = "name";
	
	/**
	 * Attribute to ignore the execution of page componentes by some user agent groups
	 */
	public static final String IGNORED_BY_USER_AGENT_GROUPS = "ignored-by-user-agent-groups";
	
	/**
	 * Default user agent group
	 */
	public static final String DEFAULT_USER_AGENT_GROUP = "default";
	
	/**
	 * "Single component page" attribute in xml configurations. It shows if a page has only one component or not.
	 */
	public static final String SINGLE_COMPONENT_PAGE = "single-component";

	/**
	 * Source attribute in xml configurations. It indicates the path to page xml. If a page is single component page,
	 * source attribute indicates the name of component instead of path to page xml.
	 */
	public static final String SOURCE = "source";

	/**
	 * Private attribute in xml configurations
	 */
	public static final String PRIVATE = "private";


	/**
	 * Element name for extension point definitions and extension point binding.
	 */
	public static final String EXTENSION_POINT = "extension-point";
	
	/**
	 * Element name for page component call
	 */
	public static final String COMPONENT = "component";
	
	/**
	 * Null element. This element will be processed as if it was a collection of elements: its child elements. 
	 */
	public static final String NULL = "null";

	/**
	 * Exception in error pages.
	 */
	public static final String EXCEPTION = "exception";

	/**
	 * Error code
	 * 
	 */
	public static final String STATUS_CODE = "code";


}
