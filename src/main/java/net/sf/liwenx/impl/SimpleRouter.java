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

import java.util.Locale;

import net.sf.liwenx.Device;
import net.sf.liwenx.LiwenxRequest;
import net.sf.liwenx.Locator;
import net.sf.liwenx.Router;
import net.sf.liwenx.util.UnifiedLocaleMessageSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple router implementation.
 *
 * @author Alejandro Guerra Cabrera
 *
 */
public class SimpleRouter implements Router {

	private Log log = LogFactory.getLog(getClass());
	private UnifiedLocaleMessageSource messageSource;
	private String defaultPage = "index";
	
	/** (non-Javadoc)
	 * @see net.sf.liwenx.Router#route(net.sf.liwenx.LiwenxRequest)
	 */
	@Override
	public Locator route(LiwenxRequest request) {
		String path = request.getPathInfo();
		if(log.isTraceEnabled()){
			log.trace(messageSource.getMessage("router.path", path));
		}
		final String page;
		if(path!=null && !path.equals("/")){
			page=path.substring(1);
		}else{
			page=defaultPage;
		}
		
		String d = request.getRequestValue("d");
		Device dev;
		if(d!=null && d.trim().equals("xml")){
			dev = Device.XML;
		}else{
			dev = Device.HTML;
		}
		return new Locator(page, dev, Locale.getDefault());
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(UnifiedLocaleMessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	/**
	 * @param defaultPage the defaultPage to set
	 */
	public void setDefaultPage(String defaultPage) {
		this.defaultPage = defaultPage;
	}
}
