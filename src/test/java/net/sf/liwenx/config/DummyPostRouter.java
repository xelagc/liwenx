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

import net.sf.liwenx.LiwenxRequest;
import net.sf.liwenx.Locator;
import net.sf.liwenx.PostRouter;

/**
 * Dummy implementation of {@link PostRouter} for testing.
 * 
 * @author Alejandro Guerra Cabrera
 *
 */
public class DummyPostRouter implements PostRouter {

	/** (non-Javadoc)
	 * @see net.sf.liwenx.PostRouter#processRequest(net.sf.liwenx.LiwenxRequest)
	 */
	@Override
	public Locator processRequest(LiwenxRequest request) {
		// do nothing
		return null;
	}

}
