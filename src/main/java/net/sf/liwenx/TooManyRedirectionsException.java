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
 * It is thrown when the number of internal redirections in the same request reaches the max redirections number.
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class TooManyRedirectionsException extends Exception {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -8939359312688690069L;

	/**
	 * (non-Javadoc)
	 * 
	 * @see Exception#Exception()
	 */
	public TooManyRedirectionsException() {
		super();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Exception#Exception(String, Throwable)
	 */
	public TooManyRedirectionsException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Exception#Exception(String)
	 */
	public TooManyRedirectionsException(String message) {
		super(message);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Exception#Exception(Throwable)
	 */
	public TooManyRedirectionsException(Throwable cause) {
		super(cause);
	}

}
