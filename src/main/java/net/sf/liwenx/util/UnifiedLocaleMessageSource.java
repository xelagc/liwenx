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

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/**
 * A way of {@link MessageSource} that unifies hide locale stuff.
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public interface UnifiedLocaleMessageSource {

	/**
	 * Try to resolve the message. Treat as an error if the message can't be found.
	 * 
	 * @param code - the code to lookup up, such as 'calculator.noRateSet'
	 * @param args - Array of arguments that will be filled in for params within the message (params look like "{0}",
	 *            "{1,date}", "{2,time}" within a message), or null if none.
	 * @return the resolved message
	 * @throws NoSuchMessageException - if the message wasn't found
	 */
	String getMessage(String code, Object... args) throws NoSuchMessageException;
}
