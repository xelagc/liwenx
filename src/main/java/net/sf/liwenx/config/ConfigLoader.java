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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.liwenx.ConfigResourceNotFoundException;
import net.sf.liwenx.DuplicatedItemException;
import net.sf.liwenx.InvalidConfigResourceException;
import net.sf.liwenx.PageComponent;
import net.sf.liwenx.PostRouter;
import net.sf.liwenx.TemplateNotFoundException;
import net.sf.liwenx.util.LiwenxXml;
import net.sf.liwenx.util.UnifiedLocaleMessageSource;
import net.sf.liwenx.util.XmlUtil;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * It reads configuration of project
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class ConfigLoader implements InitializingBean, ApplicationContextAware {

	private static final Log LOG = LogFactory.getLog(ConfigLoader.class);

	private static final String DOUBLE_SLASH = "//";
	private static final String COLON = ":";

	/**
	 * XPath to search for page component calls.
	 */
	public static final String XPATH_PAGE_COMPONENTS = DOUBLE_SLASH + LiwenxXml.LIWENX_NS_PREFIX + COLON
			+ LiwenxXml.COMPONENT;

	private static final String XPATH_EXTENSION_POINTS = DOUBLE_SLASH + LiwenxXml.LIWENX_NS_PREFIX + COLON
			+ LiwenxXml.EXTENSION_POINT;

	private UnifiedLocaleMessageSource messageSource;

	/**
	 * Set of config resource paths
	 */
	private Set<String> configResources;

	/**
	 * Current application context
	 */
	private ApplicationContext context;

	private Map<String, Page> pagesMap = new LinkedHashMap<String, Page>();

	private Map<Class<? extends Exception>, ErrorPage> errorPagesMap = new LinkedHashMap<Class<? extends Exception>, ErrorPage>();

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	private void init() throws Exception {

		if (LOG.isTraceEnabled()) {
			LOG.trace(messageSource.getMessage("configLoader.init.initializing", configResources));
		}

		// Loading pages and templates config
		final Set<Template> templates = new LinkedHashSet<Template>();
		final Set<Page> pages = new LinkedHashSet<Page>();
		final Set<ErrorPage> errorPages = new LinkedHashSet<ErrorPage>();
		for (String resource : configResources) {
			loadConfigResource(resource, templates, pages, errorPages);
		}

		// Processing templates
		final Map<String, Template> tMap = processTemplates(templates);

		// Processing pages
		processPages(pages, tMap);

		processErrorPages(errorPages, tMap);

	}

	private void processErrorPages(Set<ErrorPage> errorPages, Map<String, Template> tMap)
			throws TemplateNotFoundException, ConfigResourceNotFoundException, InvalidConfigResourceException {

		for (ErrorPage p : errorPages) {
			if (LOG.isTraceEnabled()) {
				LOG.trace(messageSource.getMessage("configLoader.init.processingErrorPage", p.getName()));
			}
			if (p.isSingleComponentPage()) {
				// I try to get page component. If there's none component, throws exception
				context.getBean(p.getSource(), PageComponent.class);
			} else {
				processMultiComponentPage(p, tMap);
			}
			errorPagesMap.put(p.getException(), p);
			pagesMap.put(p.getName(), p);
		}
	}

	/**
	 * It process pages. If page is based on a template, it generates final xml for that page.
	 * 
	 * @param pages - set of loaded pages.
	 * @param tMap - templates map.
	 * @throws TemplateNotFoundException - if a configured template is not found.
	 * @throws InvalidConfigResourceException - if page resource is invalid.
	 * @throws ConfigResourceNotFoundException - if page resource is not found.
	 */
	private void processPages(Set<Page> pages, Map<String, Template> tMap) throws TemplateNotFoundException,
			ConfigResourceNotFoundException, InvalidConfigResourceException {

		for (Page p : pages) {
			if (LOG.isTraceEnabled()) {
				LOG.trace(messageSource.getMessage("configLoader.init.processingPage", p.getName()));
			}
			if (p.isSingleComponentPage()) {
				// I try to get page component. If there's none component, throws exception
				context.getBean(p.getSource(), PageComponent.class);
			} else {
				processMultiComponentPage(p, tMap);
			}
			pagesMap.put(p.getName(), p);
		}
	}

	private void processMultiComponentPage(Page p, Map<String, Template> tMap) throws TemplateNotFoundException,
			ConfigResourceNotFoundException, InvalidConfigResourceException {

		// non-template based page
		final Document doc = parseXmlResource(context.getResource(p.getSource()));
		// I check component calls. If there's no exception, page it's ok.
		checkPageComponents(doc, p);
		// At this point, page xml it's ok
		p.setXml(doc);

		final String tName = p.getTemplateName();
		if (tName != null) {
			// template based page
			final Template t = tMap.get(tName);
			if (t != null) {
				processTemplateBasedPage(p, t);
			} else {
				throw new TemplateNotFoundException(messageSource.getMessage("configLoader.init.templateNotFound",
						tName, p.getName()));
			}
		}

	}

	private void checkPageComponents(Document doc, AbstractConfigDefinition p) throws InvalidConfigResourceException {
		// I check for component call
		final Nodes nodes = doc.query(XPATH_PAGE_COMPONENTS, LiwenxXml.LIWENX_XP_CONTEXT);
		for (int i = 0; i < nodes.size(); i++) {
			// Component element
			final Element comp = (Element) nodes.get(i);
			// Component name
			final String compName = comp.getAttributeValue(LiwenxXml.NAME);
			if (compName != null) {
				// I check component bean exists in application context
				context.getBean(compName, PageComponent.class);
			} else {
				// Unnamed component call
				throw new InvalidConfigResourceException(messageSource.getMessage(
						"configLoader.init.invalidComponentCall", p.getName(), p.getClass().getSimpleName()));
			}
		}
	}

	private void processTemplateBasedPage(Page p, Template t) throws InvalidConfigResourceException {
		// Extension points map
		final Map<String, Element> epMap = new HashMap<String, Element>();
		// I search for extension points in template and gets template document.
		final Document tDoc = searchForExtensionPointsInTemplate(t, epMap);

		// I read page doc
		final Document pDoc = p.getXml();
		// I read all extension points definitions in page
		final Nodes epNodes = pDoc.query(XPATH_EXTENSION_POINTS, LiwenxXml.LIWENX_XP_CONTEXT);
		for (int i = 0; i < epNodes.size(); i++) {
			// Extension point in page
			final Element epp = (Element) epNodes.get(i);
			// Extension point name
			final String epName = epp.getAttributeValue(LiwenxXml.NAME);
			// Extension point in template
			final Element ept = epMap.remove(epName);
			if (ept != null) {
				// I must replace extension point element in template by extension point content in page
				XmlUtil.replaceElementByChildren(ept, epp);
			} else {
				// Extension point not found in template
				throw new InvalidConfigResourceException(messageSource.getMessage(
						"configLoader.init.invalidExtensionPointDefinition", epName, p.getName(), t.getName()));
			}
		}

		if (epMap.isEmpty()) {
			// All extension point has been processed.
			// I check page components in page
			checkPageComponents(p.getXml(), p);
			// At this point page is ok.
			p.setXml(tDoc);
			List<PostRouter> tPostRouters = t.getPostRouters();
			if (tPostRouters != null && !tPostRouters.isEmpty()) {
				if (p.getPostRouters() != null) {
					p.getPostRouters().addAll(0, tPostRouters);
				} else {
					p.setPostRouters(new ArrayList<PostRouter>(tPostRouters));
				}
			}
		} else {
			throw new InvalidConfigResourceException(messageSource.getMessage(
					"configLoader.init.unsatisfiedExtensionPointDefinition", epMap.keySet(), p.getName()));
		}

	}

	private Document searchForExtensionPointsInTemplate(Template t, Map<String, Element> epMap)
			throws InvalidConfigResourceException {
		final Document tDoc = t.getXml();
		// I read all extension points definitions in template
		final Nodes nodes = tDoc.query(XPATH_EXTENSION_POINTS, LiwenxXml.LIWENX_XP_CONTEXT);
		for (int i = 0; i < nodes.size(); i++) {
			final Element ep = (Element) nodes.get(i);
			final String epName = ep.getAttributeValue(LiwenxXml.NAME);
			if (!epMap.containsKey(epName)) {
				epMap.put(epName, ep);
			} else {
				throw new InvalidConfigResourceException(messageSource.getMessage(
						"configLoader.init.duplicatedExtensionPointDefinition", t.getName(), epName));
			}
		}
		return tDoc;
	}

	/**
	 * It processes read templates reading its xml.
	 * 
	 * @param templates - set of templates.
	 * @return map containing template names as keys and processed template objects as values.
	 * @throws InvalidConfigResourceException
	 * @throws ConfigResourceNotFoundException
	 */
	private Map<String, Template> processTemplates(Set<Template> templates) throws ConfigResourceNotFoundException,
			InvalidConfigResourceException {

		final Map<String, Template> tMap = new HashMap<String, Template>();
		for (Template t : templates) {
			if (LOG.isTraceEnabled()) {
				LOG.trace(messageSource.getMessage("configLoader.init.processingTemplate", t.getName()));
			}
			final Document doc = parseXmlResource(context.getResource(t.getSource()));
			checkPageComponents(doc, t);
			// At this point, template it's ok
			t.setXml(doc);
			tMap.put(t.getName(), t);
		}

		return tMap;
	}

	/**
	 * It loads configuration from specified resource.
	 * 
	 * @param resource - resource path.
	 * @param templates - set of loaded templates. It stores new templates in this set.
	 * @param pages - set of loaded pages. It stores new pages in this set.
	 * @param errorPages - set of loaded error pages. It stores new error pages in this set.
	 * @throws ConfigResourceNotFoundException - if specified resource is not found.
	 * @throws DuplicatedItemException - if a duplicate page or duplicate template is found.
	 * @throws InvalidConfigResourceException - if configuration resource is not a valid and well-formed XML
	 * @throws ClassNotFoundException
	 */
	private void loadConfigResource(String resource, Set<Template> templates, Set<Page> pages, Set<ErrorPage> errorPages)
			throws ConfigResourceNotFoundException, DuplicatedItemException, InvalidConfigResourceException,
			ClassNotFoundException {
		if (LOG.isTraceEnabled()) {
			LOG.trace(messageSource.getMessage("configLoader.init.loadingResource", resource));
		}

		final Resource res = context.getResource(resource);
		final Document doc = parseXmlResource(res);
		loadTemplates(resource, doc, templates);
		loadPages(doc, pages, resource);
		loadErrorPages(doc, errorPages, resource);
	}

	private Document parseXmlResource(Resource res) throws ConfigResourceNotFoundException,
			InvalidConfigResourceException {
		final Document doc;
		if (res.exists()) {
			try {
				doc = XmlUtil.parseXml(res.getInputStream());
			} catch (ValidityException e) {
				throw new InvalidConfigResourceException(e);
			} catch (ParsingException e) {
				throw new InvalidConfigResourceException(e);
			} catch (IOException e) {
				throw new InvalidConfigResourceException(e);
			}
		} else {
			// resource not found
			throw new ConfigResourceNotFoundException(messageSource.getMessage("configLoader.init.resourceNotFound",
					res));
		}

		return doc;

	}

	/**
	 * It loads all pages in specified document
	 * 
	 * @param doc document that contains pages
	 * @param templates set containing existent pages. In this set loaded templates will be stored
	 */
	private void loadPages(Document doc, Set<Page> pages, String resource) throws InvalidConfigResourceException,
			DuplicatedItemException {
		if (LOG.isTraceEnabled()) {
			LOG.trace(messageSource.getMessage("configLoader.init.loadingPages", resource));
		}
		final Elements templElems = doc.getRootElement().getChildElements(LiwenxXml.PAGE, LiwenxXml.LIWENX_NS_URI);
		for (int i = 0; i < templElems.size(); i++) {
			final Element templElem = templElems.get(i);
			final Page p = loadCommonFieldsOnDefinition(templElem, Page.class, resource);
			if (!pages.add(p)) {
				throw new DuplicatedItemException(messageSource.getMessage("configLoader.init.duplicatePage", resource,
						p.getName()));
			}

			// template
			p.setTemplateName(templElem.getAttributeValue(LiwenxXml.TEMPLATE));
			// private
			final String privateStr = templElem.getAttributeValue(LiwenxXml.PRIVATE);
			if (privateStr != null) {
				p.setPrivatePage(Boolean.parseBoolean(privateStr));
			}
			// Single component page
			final String singleComponent = templElem.getAttributeValue(LiwenxXml.SINGLE_COMPONENT_PAGE);
			if (singleComponent != null) {
				p.setSingleComponentPage(Boolean.parseBoolean(singleComponent));
			}
		}
	}

	private void loadErrorPages(Document doc, Set<ErrorPage> errorPages, String resource)
			throws InvalidConfigResourceException, DuplicatedItemException, ClassNotFoundException {
		if (LOG.isTraceEnabled()) {
			LOG.trace(messageSource.getMessage("configLoader.init.loadingErrorPages", resource));
		}
		final Elements templElems = doc.getRootElement()
				.getChildElements(LiwenxXml.ERROR_PAGE, LiwenxXml.LIWENX_NS_URI);
		for (int i = 0; i < templElems.size(); i++) {
			final Element templElem = templElems.get(i);
			final ErrorPage p = loadCommonFieldsOnDefinition(templElem, ErrorPage.class, resource);
			if (!errorPages.add(p)) {
				throw new DuplicatedItemException(messageSource.getMessage("configLoader.init.duplicateErrorPage",
						resource, p.getName()));
			}

			Class<?> clazz = Class.forName(p.getName());
			if (Exception.class.isAssignableFrom(clazz)) {
				@SuppressWarnings("unchecked")
				Class<? extends Exception> clazz2 = (Class<? extends Exception>) clazz;
				p.setException(clazz2);
			} else {
				throw new InvalidConfigResourceException(messageSource.getMessage(
						"configLoader.init.invalidExceptionClass", clazz));
			}

			// template
			p.setTemplateName(templElem.getAttributeValue(LiwenxXml.TEMPLATE));

			// All error pages are private
			p.setPrivatePage(true);

			// Status code
			String statusCode = templElem.getAttributeValue(LiwenxXml.STATUS_CODE);
			if (StringUtils.isNotBlank(statusCode) && StringUtils.isNumeric(statusCode)) {
				p.setStatusCode(Integer.parseInt(statusCode));
			} else {
				throw new InvalidConfigResourceException(messageSource.getMessage(
						"configLoader.init.invalidStatusCode", clazz, statusCode));
			}

			// Single component page
			final String singleComponent = templElem.getAttributeValue(LiwenxXml.SINGLE_COMPONENT_PAGE);
			if (singleComponent != null) {
				p.setSingleComponentPage(Boolean.parseBoolean(singleComponent));
			}
		}
	}

	/**
	 * It loads all templates in specified document
	 * 
	 * @param doc document that contains templates
	 * @param templates set containing existent templates. In this set loaded templates will be stored
	 */
	private void loadTemplates(String resource, Document doc, Set<Template> templates)
			throws InvalidConfigResourceException, DuplicatedItemException {
		if (LOG.isTraceEnabled()) {
			LOG.trace(messageSource.getMessage("configLoader.init.loadingTemplates", resource));
		}

		final Elements templElems = doc.getRootElement().getChildElements(LiwenxXml.TEMPLATE, LiwenxXml.LIWENX_NS_URI);
		for (int i = 0; i < templElems.size(); i++) {
			final Element templElem = templElems.get(i);
			final Template t = loadCommonFieldsOnDefinition(templElem, Template.class, resource);
			if (!templates.add(t)) {
				throw new DuplicatedItemException(messageSource.getMessage("configLoader.init.duplicateTemplate",
						resource, t.getName()));
			}
		}

	}

	/**
	 * It loads commons fields defined in {@link AbstractConfigDefinition}
	 * 
	 * @param <T> - config definition type
	 * @param elem - xml element where config is defined
	 * @param type - expected returning type
	 * @param resource - resource whose configuration is loading.
	 * @return expected type instance
	 * @throws InvalidConfigResourceException - if definition is not valid.
	 */
	@SuppressWarnings("unchecked")
	private <T extends AbstractConfigDefinition> T loadCommonFieldsOnDefinition(Element elem, Class<T> type,
			String resource) throws InvalidConfigResourceException {
		final AbstractConfigDefinition def;
		if (Template.class.isAssignableFrom(type)) {
			def = new Template();
		} else if (ErrorPage.class.isAssignableFrom(type)) {
			def = new ErrorPage();
		} else if (Page.class.isAssignableFrom(type)) {
			def = new Page();
		} else {
			// reaching this point is impossible
			throw new ClassCastException(messageSource.getMessage("configLoader.init.classCastAbstractDefinition",
					type, AbstractConfigDefinition.class, resource));
		}
		String nameAttr;
		if (def instanceof ErrorPage) {
			nameAttr = LiwenxXml.EXCEPTION;
		} else {
			nameAttr = LiwenxXml.NAME;
		}
		def.setName(elem.getAttributeValue(nameAttr));
		def.setSource(elem.getAttributeValue(LiwenxXml.SOURCE));
		validateConfigDefinition(def, resource); // throws exception in case of invalid template
		if (LOG.isTraceEnabled()) {
			LOG.trace(messageSource.getMessage("configLoader.init.loadingConfigDefinition", def.getClass()
					.getSimpleName(), def.getName(), resource));
		}
		// At this point, template is valid and it's added to set of templates.
		// Reading post-routers, if any.
		def.setPostRouters(loadPostRouters(elem));

		return (T) def;
	}

	/**
	 * It loads post-routers definitions under specified xml element.
	 * 
	 * @param elem - parent element of post-routers definitions
	 * @return set of post-routers founds if any. Null set is returned if none post-router is found.
	 * @throws InvalidConfigResourceException - if post-router definition doesn't specify a post-router name.
	 */
	private List<PostRouter> loadPostRouters(Element elem) throws InvalidConfigResourceException {
		final Elements elems = elem.getChildElements(LiwenxXml.POST_ROUTER, LiwenxXml.LIWENX_NS_URI);
		List<PostRouter> pRouters = null;
		for (int i = 0; i < elems.size(); i++) {
			final Element e = elems.get(i);
			final String name = e.getAttributeValue(LiwenxXml.NAME);
			if (name != null) {
				final PostRouter pRouter = (PostRouter) context.getBean(name, PostRouter.class);
				if (pRouters == null) {
					pRouters = new LinkedList<PostRouter>();
				}
				pRouters.add(pRouter);
			} else {
				throw new InvalidConfigResourceException(
						messageSource.getMessage("configLoader.init.invalidPostRouterDefinition"));
			}
		}

		return pRouters;
	}

	private void validateConfigDefinition(AbstractConfigDefinition t, String resource)
			throws InvalidConfigResourceException {
		if (t.getName() == null || t.getSource() == null) {
			throw new InvalidConfigResourceException(messageSource.getMessage(
					"configLoader.init.invalidAbstractDefinition", resource, t.getClass().getSimpleName()));
		}
	}

	/**
	 * It returns a loaded page
	 * 
	 * @param name - page name
	 * @return page
	 */
	public Page getPage(String name) {
		return pagesMap.get(name);
	}

	public ErrorPage getErrorPage(Exception exc) {
		Class<? extends Exception> clazz = exc.getClass();
		ErrorPage p = errorPagesMap.get(clazz);
		if (p == null) {
			Iterator<Entry<Class<? extends Exception>, ErrorPage>> it = errorPagesMap.entrySet().iterator();
			while (it.hasNext() && p == null) {
				Entry<Class<? extends Exception>, ErrorPage> e = it.next();
				Class<? extends Exception> epClass = e.getKey();
				if (clazz.isAssignableFrom(epClass)) {
					p = e.getValue();
				}
			}
		}

		return p;
	}

	/**
	 * It returns loaded pages names
	 * 
	 * @return set of pages names
	 */
	public Set<String> getLoadedPageNames() {
		return new LinkedHashSet<String>(pagesMap.keySet());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		this.context = appContext;
	}

	/**
	 * It sets config resource paths.
	 * 
	 * @param configResources set that contains path of configuration resources. They must be paths that
	 *            {@link ResourceLoader#getResource(String)} method can accept.
	 */
	public void setConfigResources(Set<String> configResources) {
		this.configResources = configResources;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	@Autowired(required = true)
	public void setMessageSource(UnifiedLocaleMessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
