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

import java.util.Set;

import junit.framework.TestCase;
import net.sf.liwenx.util.UnifiedLocaleMessageSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Sets;

/**
 * @author Alejandro Guerra Cabrera
 * 
 */
public class ConfigLoaderTest extends TestCase {

	private ConfigLoader configLoader;

	/**
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final ApplicationContext context = new ClassPathXmlApplicationContext("/net/sf/liwenx/config/config-beans.xml");
		configLoader = new ConfigLoader();
		configLoader.setApplicationContext(context);
		configLoader.setConfigResources(Sets.newHashSet("classpath:/net/sf/liwenx/config/page-template-config-1.xml"));
		configLoader.setMessageSource((UnifiedLocaleMessageSource) context.getBean(context
				.getBeanNamesForType(UnifiedLocaleMessageSource.class)[0]));
	}

	/**
	 * It tests configuration loading.
	 * 
	 * @throws Exception
	 */
	public void testLoadConfig() throws Exception {
		configLoader.afterPropertiesSet();
		final Set<String> names = configLoader.getLoadedPageNames();
		assertTrue(names.size()==4);
		for(String name:names){
			final Page p = configLoader.getPage(name);
			System.out.println("\n[Page: "+p.getName()+"]");
			System.out.println("[Source: "+p.getSource()+"]");
			System.out.println("[Template: "+p.getTemplateName()+"]");
			System.out.println("[Private: "+p.isPrivatePage()+"]");
			System.out.println("[Single-component: "+p.isSingleComponentPage()+"]");
			System.out.println("[Post-routers: "+p.getPostRouters()+"]");
			if(!p.isSingleComponentPage()){
				System.out.println(p.getXml().toXML());
			}else{
				
			}
		}
	}
}
