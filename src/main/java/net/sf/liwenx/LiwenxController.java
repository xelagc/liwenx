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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * LIWENX controller. It processes each request.
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class LiwenxController extends AbstractController {
	
	private Liwenx liwenx;

	/**
	 * (non-Javadoc)
	 * 
	 * @see AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		liwenx.process(request, response);

		// It returns null because response is handled directly
		return null;
	}
	
	/**
	 * @param liwenx the liwenx to set
	 */
	@Autowired
	public void setLiwenx(Liwenx liwenx) {
		this.liwenx = liwenx;
	}

}
