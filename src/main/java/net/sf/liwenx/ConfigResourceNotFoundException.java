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

/**
 * It is thrown when a configuration resource is not found.
 * 
 * @author Alejandro Guerra Cabrera
 *
 */
public class ConfigResourceNotFoundException extends Exception {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = -3564501317563692441L;

	/**
	 * (non-Javadoc)
	 * @see Exception#Exception()
	 */
	public ConfigResourceNotFoundException() {
		super();
	}

	/**
	 * (non-Javadoc)
	 * @see Exception#Exception(String, Throwable)
	 */
	public ConfigResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * (non-Javadoc)
	 * @see Exception#Exception(String)
	 */
	public ConfigResourceNotFoundException(String message) {
		super(message);
	}

	/**
	 * (non-Javadoc)
	 * @see Exception#Exception(Throwable)
	 */
	public ConfigResourceNotFoundException(Throwable cause) {
		super(cause);
	}

}
