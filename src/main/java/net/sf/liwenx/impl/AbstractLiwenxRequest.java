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

import net.sf.liwenx.LiwenxRequest;
import net.sf.liwenx.Locator;

/**
 *
 * @author Alejandro Guerra Cabrera
 *
 */
public abstract class AbstractLiwenxRequest implements LiwenxRequest{

	private Locator requestedLocator;

	private Locator currentLocator;

	/** (non-Javadoc)
	 * @see net.sf.liwenx.LiwenxRequest#getCurrentLocator()
	 */
	@Override
	public Locator getCurrentLocator() {
		return currentLocator;
	}

	/** (non-Javadoc)
	 * @see net.sf.liwenx.LiwenxRequest#getRequestedLocator()
	 */
	@Override
	public Locator getRequestedLocator() {
		return requestedLocator;
	}

	/**
	 * @param requestedLocator the requestedLocator to set
	 */
	void setRequestedLocator(Locator requestedLocator) {
		this.requestedLocator = requestedLocator;
	}

	/**
	 * @param currentLocator the currentLocator to set
	 */
	void setCurrentLocator(Locator currentLocator) {
		this.currentLocator = currentLocator;
	}

	
}
