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

import java.io.IOException;
import java.io.InputStream;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

/**
 * @author Alejandro Guerra Cabrera
 * 
 */
public final class XmlUtil {

	/**
	 * Private constructor
	 */
	private XmlUtil() {
		// nothing to do
	}

	/**
	 * It parses xml contained into specified {@link InputStream}
	 * 
	 * @param input
	 * @return
	 * @throws ValidityException if a validity error is detected; only thrown if the builder has been instructed to
	 *             validate
	 * @throws ParsingException if a well-formedness error is detected
	 * @throws IOException if an I/O error such as a broken socket prevents the document from being fully read.
	 */
	public static Document parseXml(InputStream input) throws ValidityException, ParsingException, IOException {
		final Builder builder = new Builder();
		return builder.build(input);
	}

	/**
	 * It replace oldNode by children of newParentNode
	 * 
	 * @param oldNode - node to remove
	 * @param newParentNode - parent of children to add
	 */
	public static void replaceElementByChildren(Element oldNode, Element newParentNode) {
		int insertionPoint = oldNode.getParent().indexOf(oldNode);
		for (int i = 0; i < newParentNode.getChildCount(); i++) {
			final Node child = newParentNode.getChild(i);
			oldNode.getParent().insertChild(child.copy(), ++insertionPoint);
		}
		oldNode.getParent().removeChild(oldNode);
	}

	public static void addAttribute(Element elem, String name, Object value) {
		elem.addAttribute(new Attribute(name, value.toString()));
	}

	public static void addAttribute(Element elem, String name, byte value) {
		addAttribute(elem, name, String.valueOf(value));
	}

	public static void addAttribute(Element elem, String name, short value) {
		addAttribute(elem, name, String.valueOf(value));
	}

	public static void addAttribute(Element elem, String name, int value) {
		addAttribute(elem, name, String.valueOf(value));
	}

	public static void addAttribute(Element elem, String name, long value) {
		addAttribute(elem, name, String.valueOf(value));
	}

	public static void addAttribute(Element elem, String name, float value) {
		addAttribute(elem, name, String.valueOf(value));
	}

	public static void addAttribute(Element elem, String name, double value) {
		addAttribute(elem, name, String.valueOf(value));
	}

	public static void addAttribute(Element elem, String name, boolean value) {
		addAttribute(elem, name, String.valueOf(value));
	}

	public static Element createElement(String name, String value, Element parent) {
		return createElement(name, null, value, parent);
	}

	public static Element createElement(String name, String uri, String value, Element parent) {
		Element elem;
		if (uri != null) {
			elem = new Element(name, uri);
		} else {
			elem = new Element(name);
		}
		if (value != null) {
			elem.appendChild(value);
		}
		if (parent != null) {
			parent.appendChild(elem);
		}

		return elem;
	}

}
