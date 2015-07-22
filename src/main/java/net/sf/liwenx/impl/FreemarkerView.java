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


import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.liwenx.Device;
import net.sf.liwenx.LiwenxView;
import net.sf.liwenx.util.UnifiedLocaleMessageSource;
import nu.xom.Document;
import nu.xom.converters.DOMConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.web.context.ServletConfigAware;

import freemarker.cache.CacheStorage;
import freemarker.cache.MruCacheStorage;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;


/**
 * Freemarker based implementation of {@link LiwenxView}
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class FreemarkerView implements LiwenxView, InitializingBean, ServletConfigAware {

	private final Log log = LogFactory.getLog(getClass());

	private UnifiedLocaleMessageSource systemMessageSource;

	private String templatesDir;

	private String mainTemplate;

	private Configuration config;

	private ServletConfig servletConfig;

	private String encoding;

	private Map<String, Object> globals;

	private MessageSource userMessageSource;

	private boolean debug;

	/**
	 * (non-Javadoc)
	 * @see net.sf.liwenx.LiwenxView#processView(nu.xom.Document, java.util.Locale, Device, java.lang.String, java.io.Writer)
	 */
	@Override
	public void processView(Document xml, Locale locale, Device device, String userAgentGroup, Writer out) throws Exception {
		final Template t = config.getTemplate(mainTemplate, locale);
		if (t != null) {
			Map<Object, Object> rootModel = new HashMap<Object, Object>();
			final org.w3c.dom.Document w3cDoc = DOMConverter.convert(xml, DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().getDOMImplementation());
			rootModel.put("doc", w3cDoc);
			if(globals!=null){
				for(Entry<String,Object> g:globals.entrySet()){
					rootModel.put(g.getKey(), g.getValue());
				}
			}
			if(userMessageSource!=null){
				rootModel.put("translations", userMessageSource);
			}
			DecimalFormat df = new DecimalFormat();
			df.setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
			df.setMaximumFractionDigits(2);
			df.setMinimumFractionDigits(2);
			rootModel.put("decimalFormat", df);
			rootModel.put("localeObj", locale);
			rootModel.put("userAgentGroup",userAgentGroup);
			rootModel.put("device", device.name().toUpperCase());
			
			t.process(rootModel, out);
		} else {
			log.error(systemMessageSource.getMessage("freemarker.templateNotFound", templatesDir, mainTemplate, locale));
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		config = new Configuration();
		config.setTemplateLoader(new WebappTemplateLoader(servletConfig.getServletContext(), templatesDir));
		config.setObjectWrapper(new DefaultObjectWrapper());
		config.setLocalizedLookup(false);
		CacheStorage c;
		if (debug) {
			c = new MruCacheStorage(0, 0);
		} else {
			c = new MruCacheStorage(20, 250);
			config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		}
		config.setCacheStorage(c);
		config.setDefaultEncoding(encoding);
		config.setURLEscapingCharset(encoding);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.context.ServletConfigAware#setServletConfig(javax.servlet.ServletConfig)
	 */
	@Override
	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setSystemMessageSource(UnifiedLocaleMessageSource messageSource) {
		this.systemMessageSource = messageSource;
	}

	/**
	 * @param templatesDir the templatesDir to set
	 */
	public void setTemplatesDir(String templatesDir) {
		this.templatesDir = templatesDir;
	}

	/**
	 * @param mainTemplate the mainTemplate to set
	 */
	public void setMainTemplate(String mainTemplate) {
		this.mainTemplate = mainTemplate;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @param globals the globals to set
	 */
	public void setGlobals(Map<String, Object> globals) {
		this.globals = globals;
	}

	/**
	 * @param userMessageSource the userMessageSource to set
	 */
	public void setUserMessageSource(MessageSource userMessageSource) {
		this.userMessageSource = userMessageSource;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
