/*
 * Copyright (c) JForum Team
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following disclaimer.
 * 2) Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 * 
 * Created on 15/08/2003 / 20:56:33
 * The JForum Project
 * http://www.jforum.net
 */
package ua.homca.jforum.view.admin;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ua.homca.jforum.context.RequestContext;
import ua.homca.jforum.context.ResponseContext;
import ua.homca.jforum.entities.Category;
import ua.homca.jforum.entities.Forum;
import ua.homca.jforum.exceptions.ForumException;
import ua.homca.jforum.repository.ForumRepository;
import ua.homca.jforum.repository.TopicRepository;
import ua.homca.jforum.util.I18n;
import ua.homca.jforum.util.preferences.ConfigKeys;
import ua.homca.jforum.util.preferences.SystemGlobals;
import ua.homca.jforum.util.preferences.TemplateKeys;
import freemarker.template.SimpleHash;

/**
 * @author Rafael Steil
 * @version $Id$
 */
public class ConfigAction extends AdminCommand 
{
	public ConfigAction() {}
	
	public ConfigAction(RequestContext request,
			ResponseContext response, 
			SimpleHash context)
	{
		this.request = request;
		this.response = response;
		this.context = context;
	}
	
	public void list() {
		Properties p = new Properties();
		Iterator<Object> iter = SystemGlobals.fetchConfigKeyIterator();

		while (iter.hasNext()) {
			String key = (String) iter.next();
			String value = SystemGlobals.getValue(key);
			p.put(key, value);
		}

		Properties locales = new Properties();
		
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(SystemGlobals.getValue(ConfigKeys.CONFIG_DIR)
				+ "/languages/locales.properties");
			locales.load(fis);
		}
		catch (IOException e) {
			throw new ForumException(e);
		}
		finally {
			if (fis != null) {
				try { fis.close(); } catch (Exception e) { e.printStackTrace(); }
			}
		}
		
		List<Object> localesList = new ArrayList<Object>();

		for (Enumeration<Object> e = locales.keys(); e.hasMoreElements();) {
			localesList.add(e.nextElement());
		}

		this.context.put("config", p);
		this.context.put("locales", localesList);
		this.setTemplateName(TemplateKeys.CONFIG_LIST);
	}

	public void editSave()
	{
		this.updateData(this.getConfig());
		this.list();
	}
	
	protected Properties getConfig()
	{
		Properties p = new Properties();

		Enumeration<Object> e = this.request.getParameterNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();

			if (name.startsWith("p_")) {
				p.setProperty(name.substring(name.indexOf('_') + 1), this.request.getParameter(name));
			}
		}
		
		return p;
	}
	
	protected void updateData(Properties p)
	{
		int oldTopicsPerPage = SystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);

		for (Iterator<Map.Entry<Object, Object>>  iter = p.entrySet().iterator(); iter.hasNext(); ) {
			Map.Entry<Object, Object> entry = iter.next();
			
			SystemGlobals.setValue((String)entry.getKey(), (String)entry.getValue());
		}
		
		SystemGlobals.saveInstallation();
		I18n.changeBoardDefault(SystemGlobals.getValue(ConfigKeys.I18N_DEFAULT));
		
		// If topicsPerPage has changed, force a reload in all forums
		if (oldTopicsPerPage != SystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE)) {
			List<Category> categories = ForumRepository.getAllCategories();
			
			for (Iterator<Category> iter = categories.iterator(); iter.hasNext(); ) {
				Category category = iter.next();
				
				for (Iterator<Forum> iter2 = category.getForums().iterator(); iter2.hasNext(); ) {
					Forum forum = iter2.next();
					TopicRepository.clearCache(forum.getId());
				}
			}
		}
	}
}