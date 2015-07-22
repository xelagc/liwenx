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

import java.util.List;

import net.sf.liwenx.PostRouter;

/**
 * Abstract utility class that groups commons attributes in Page and PageTemplate
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public abstract class AbstractConfigDefinition {

	/**
	 * Page name
	 */
	private String name;

	/**
	 * Page post-routers
	 */
	private List<PostRouter> postRouters;

	/**
	 * Path to xml resource
	 */
	private String source;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the postRouters
	 */
	public List<PostRouter> getPostRouters() {
		return postRouters;
	}

	/**
	 * @param postRouters the postRouters to set
	 */
	void setPostRouters(List<PostRouter> postRouters) {
		this.postRouters = postRouters;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	void setSource(String source) {
		this.source = source;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		final boolean equals;
		if (obj!=null && name != null && this.getClass().isAssignableFrom(obj.getClass())) {
			equals = name.equals(((AbstractConfigDefinition) obj).getName());
		} else {
			equals = false;
		}
		return equals;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int hash;
		if (name != null) {
			hash = name.hashCode();
		} else {
			hash = 0;
		}

		return hash;
	}
	
}
