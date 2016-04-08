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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.liwenx.Device;
import net.sf.liwenx.Liwenx;
import net.sf.liwenx.LiwenxRequest;
import net.sf.liwenx.LiwenxResponse;
import net.sf.liwenx.LiwenxView;
import net.sf.liwenx.Locator;
import net.sf.liwenx.PageComponent;
import net.sf.liwenx.PageNotFoundException;
import net.sf.liwenx.PostRouter;
import net.sf.liwenx.RedirectLocator;
import net.sf.liwenx.Router;
import net.sf.liwenx.TooManyRedirectionsException;
import net.sf.liwenx.UserAgentGroupResolver;
import net.sf.liwenx.config.ConfigLoader;
import net.sf.liwenx.config.ErrorPage;
import net.sf.liwenx.config.Page;
import net.sf.liwenx.util.LiwenxXml;
import net.sf.liwenx.util.UnifiedLocaleMessageSource;
import net.sf.liwenx.util.XmlUtil;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.Serializer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

/**
 * Implementation of {@link Liwenx}
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
@Service
public class LiwenxImpl implements Liwenx, ApplicationContextAware {

	private ConfigLoader configLoader;

	private Router router;

	private int maxRedirections = 10;

	private UnifiedLocaleMessageSource messageSource;

	private ApplicationContext context;

	private String encoding = "UTF-8";

	private LiwenxView view;

	private Map<Device, LiwenxView> viewsByDevice;

	private UserAgentGroupResolver userAgentGroupResolver;

	/**
	 * (non-Javadoc)
	 * 
	 * @see net.sf.liwenx.Liwenx#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userAgentGroup = processUserAgent(request);
		final LiwenxRequest lRequest = new LiwenxRequestImpl(request, response, messageSource, userAgentGroup);
		Locator loc = router.route(lRequest);
		if (loc instanceof RedirectLocator) {
			response.sendRedirect(((RedirectLocator) loc).getPage());
		} else {
			((AbstractLiwenxRequest) lRequest).setRequestedLocator(loc);
			try {
				LiwenxResponse resp = processPage(lRequest, lRequest.getRequestedLocator(), maxRedirections);
				processHeaders(resp, response);
				processCookies(resp, response);
				if (resp instanceof ExternalRedirectionResponse) {
					response.sendRedirect(((ExternalRedirectionResponse) resp).getRedirectTo());
				} else if (resp instanceof BinaryResponse) {
					BinaryResponse bResp = (BinaryResponse) resp;
					response.setContentType(bResp.getMimeType().toString());
					IOUtils.copy(bResp.getInputStream(), response.getOutputStream());
				} else if (resp instanceof XmlResponse || resp instanceof JsonResponse) {
					final Locator l = lRequest.getCurrentLocator();
					response.setCharacterEncoding(encoding);
					response.setStatus(resp.getStatusCode());
					// TODO: set headers (, ...)

					if (resp instanceof XmlResponse) {
						// XmlResponse
						final Device device = l.getDevice();
						response.setContentType(calculateContentType(device));
						final Element root = ((XmlResponse) resp).getXml();
						Document doc = root.getDocument();
						if (doc == null) {
							doc = new Document(root);
						}
						if (device == Device.XML) {
							// Write bytes
							Serializer s = new Serializer(response.getOutputStream(), encoding);
							s.write(doc);
						} else {
							LiwenxView v = null;
							if (!CollectionUtils.isEmpty(viewsByDevice)) {
								v = viewsByDevice.get(device);
							}
							if (v == null) {
								v = view;
							}
							v.processView(lRequest, doc, l.getLocale(), device, userAgentGroup, response.getWriter());
						}
					} else {
						// JsonResponse
						JsonResponse jsonResp = (JsonResponse) resp;
						response.setContentType("application/json");
						Gson gson = jsonResp.getBuilder().create();
						gson.toJson(jsonResp.getJson(), Map.class, new JsonWriter(response.getWriter()));
					}

				}
			} catch (PageNotFoundException e) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (TooManyRedirectionsException e) {
				throw e;
			}
		}

	}

	private String processUserAgent(HttpServletRequest request) {
		String userAgentGroup;
		if (userAgentGroupResolver != null) {
			userAgentGroup = userAgentGroupResolver.findOutUserAgentGroup(request);
			if (StringUtils.isBlank(userAgentGroup)) {
				userAgentGroup = LiwenxXml.DEFAULT_USER_AGENT_GROUP;
			}
		} else {
			userAgentGroup = LiwenxXml.DEFAULT_USER_AGENT_GROUP;
		}
		return userAgentGroup;
	}

	private void processCookies(LiwenxResponse resp, HttpServletResponse response) {
		if (resp != null) {
			AbstractLiwenxResponse absResp = (AbstractLiwenxResponse) resp;
			final List<Cookie> cookies = absResp.getCookies();
			if (cookies != null) {
				for (Cookie c : cookies) {
					response.addCookie(c);
				}
			}
		}
	}

	private void processHeaders(LiwenxResponse resp, HttpServletResponse response) {
		if (resp != null) {
			AbstractLiwenxResponse absResp = (AbstractLiwenxResponse) resp;
			final Map<String, String> headers = absResp.getHeaders();
			if (headers != null) {
				for (Entry<String, String> h : headers.entrySet()) {
					response.addHeader(h.getKey(), h.getValue());
				}
			}
		}
	}

	private String calculateContentType(Device device) {
		final String contentType;
		switch (device) {
		case RSS:
			contentType = "application/rss+xml";
			break;
		case XML:
			contentType = "text/xml";
			break;
		case HTML:
		default:
			contentType = "text/html";
		}

		return contentType;
	}

	private LiwenxResponse processPage(LiwenxRequest request, Locator l, int remainingRedirections) throws Exception {
		LiwenxResponse resp;
		if (remainingRedirections >= 0) {
			((AbstractLiwenxRequest) request).setCurrentLocator(l);
			Page p = configLoader.getPage(l.getPage());
			if (p != null && (remainingRedirections != maxRedirections || !p.isPrivatePage())) {
				try {
					Locator newLocator = processPostRouters(request, p.getPostRouters());
					if (newLocator == null) {
						// process page components
						resp = processPageComponents(request, p);
						if (resp instanceof InternalRedirectionResponse) {
							List<Cookie> cookies = ((InternalRedirectionResponse) resp).getCookies();
							resp = processPage(request, ((InternalRedirectionResponse) resp).getLocator(), remainingRedirections - 1);
							if (!CollectionUtils.isEmpty(cookies)) {
								if (resp instanceof AbstractLiwenxResponse) {
									((AbstractLiwenxResponse) resp).addCookies(0, cookies);
								} else {
									for (Cookie c : cookies) {
										resp.addCookie(c);
									}
								}
							}
						}
					} else if (newLocator instanceof RedirectLocator) {
						resp = LiwenxResponseFactory.externalRedirection(((RedirectLocator) newLocator).getPage());
					} else {
						resp = processPage(request, newLocator, remainingRedirections - 1);
					}
				} catch (Exception e) {
					resp = processError(request, e, remainingRedirections);
					if (resp == null) {
						throw e;
					}
				}
			} else {
				throw new PageNotFoundException(messageSource.getMessage("liwenxImpl.process.pageNotFound", l.getPage()));
			}
		} else {
			throw new TooManyRedirectionsException(messageSource.getMessage("liwenxImpl.process.maxRedirections"));
		}

		return resp;
	}

	private LiwenxResponse processError(LiwenxRequest request, Exception e, int remainingRedirections) throws Exception {
		LiwenxResponse resp;
		ErrorPage p = configLoader.getErrorPage(e);
		if (p != null) {
			resp = processPage(request, request.getCurrentLocator().newLocator(p.getName()), remainingRedirections - 1);
			if (resp != null && p.getStatusCode() != null) {
				((AbstractLiwenxResponse) resp).setStatusCode(p.getStatusCode());
			}
		} else {
			resp = null;
		}

		return resp;
	}

	private LiwenxResponse processPageComponents(LiwenxRequest request, Page p) throws Exception {
		LiwenxResponse resp;
		if (p.isSingleComponentPage()) {
			final PageComponent c = (PageComponent) context.getBean(p.getSource(), PageComponent.class);
			resp = c.process(request);
		} else {
			final Element xml = p.getXml().getRootElement();
			resp = new XmlResponse(xml);
			final Nodes nodes = xml.query(ConfigLoader.XPATH_PAGE_COMPONENTS, LiwenxXml.LIWENX_XP_CONTEXT);
			// User agent group
			final String userAgentGroup = request.getUserAgentGroup();
			for (int i = 0; i < nodes.size(); i++) {
				// Component element
				final Element comp = (Element) nodes.get(i);
				final LiwenxResponse cResp;
				if (pageComponentMustBeProcessed(comp, userAgentGroup)) {
					// Component name
					final PageComponent c = (PageComponent) context.getBean(comp.getAttributeValue(LiwenxXml.NAME), PageComponent.class);
					cResp = c.process(request);
				} else {
					cResp = null;
				}
				// Handle request
				// TODO: handle headers, status code and redirections
				if (cResp instanceof BinaryResponse) {
					// Invalid response
					// TODO: handle binary response
				} else if (cResp instanceof XmlResponse) {
					Element root = ((XmlResponse) cResp).getXml();
					if (root != null) {
						if (root.getNamespaceURI().equals(LiwenxXml.LIWENX_NS_URI) && root.getLocalName().equals(LiwenxXml.NULL)) {
							XmlUtil.replaceElementByChildren(comp, root);
						} else {
							comp.getParent().replaceChild(comp, root);
						}
					}
					int statusCode = cResp.getStatusCode();
					if (statusCode > resp.getStatusCode()) {
						((XmlResponse) resp).setStatusCode(statusCode);
					}
					List<Cookie> cookies = ((XmlResponse) cResp).getCookies();
					if (!CollectionUtils.isEmpty(cookies)) {
						for (Cookie c : cookies) {
							resp.addCookie(c);
						}
					}
				} else if (cResp == null) {
					comp.detach();
				} else {
					resp = cResp;
					break;
				}
			}
		}
		return resp;
	}

	private boolean pageComponentMustBeProcessed(Element comp, String userAgentGroup) {
		final boolean mustBeProcessed;
		String ignoringGroups = comp.getAttributeValue(LiwenxXml.IGNORED_BY_USER_AGENT_GROUPS);
		if (StringUtils.isNotBlank(ignoringGroups)) {
			Pattern p = Pattern.compile("\\b" + userAgentGroup + "\\b");
			Matcher m = p.matcher(ignoringGroups);
			mustBeProcessed = !m.find();
		} else {
			mustBeProcessed = true;
		}

		return mustBeProcessed;
	}

	private Locator processPostRouters(LiwenxRequest request, List<PostRouter> p) throws Exception {
		Locator newLocator = null;
		if (p != null && !p.isEmpty()) {
			Iterator<PostRouter> it = p.iterator();
			while (it.hasNext() && newLocator == null) {
				newLocator = it.next().processRequest(request);
			}
		}

		return newLocator;
	}

	/**
	 * @param configLoader the configLoader to set
	 */
	@Autowired(required = true)
	public void setConfigLoader(ConfigLoader configLoader) {
		this.configLoader = configLoader;
	}

	/**
	 * @param router the router to set
	 */
	@Autowired(required = true)
	public void setRouter(Router router) {
		this.router = router;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	@Autowired(required = true)
	public void setMessageSource(UnifiedLocaleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(LiwenxView view) {
		this.view = view;
	}

	/**
	 * @param maxRedirections the maxRedirections to set
	 */
	public void setMaxRedirections(int maxRedirections) {
		this.maxRedirections = maxRedirections;
	}

	/**
	 * @param userAgentGroupResolver the userAgentGroupResolver to set
	 */
	public void setUserAgentGroupResolver(UserAgentGroupResolver userAgentGroupResolver) {
		this.userAgentGroupResolver = userAgentGroupResolver;
	}

	/**
	 * @param viewsByDevice the viewsByDevice to set
	 */
	public void setViewsByDevice(Map<String, LiwenxView> viewsByDevice) {
		this.viewsByDevice = new HashMap<Device, LiwenxView>();
		for (Entry<String, LiwenxView> v : viewsByDevice.entrySet()) {
			Device d = Device.valueOf(v.getKey());
			if (d != null) {
				this.viewsByDevice.put(d, v.getValue());
			}
		}
	}
}
