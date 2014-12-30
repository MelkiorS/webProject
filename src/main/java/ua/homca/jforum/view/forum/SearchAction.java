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
 * Created on 14/01/2004 / 22:02:56
 * The JForum Project
 * http://www.jforum.net
 */
package ua.homca.jforum.view.forum;

import java.util.*;

import ua.homca.jforum.Command;
import ua.homca.jforum.SessionFacade;
import ua.homca.jforum.context.RequestContext;
import ua.homca.jforum.context.ResponseContext;
import ua.homca.jforum.context.web.WebRequestContext;
import ua.homca.jforum.entities.UserSession;
import ua.homca.jforum.repository.ForumRepository;
import ua.homca.jforum.search.ContentSearchOperation;
import ua.homca.jforum.search.NewMessagesSearchOperation;
import ua.homca.jforum.search.SearchArgs;
import ua.homca.jforum.search.SearchOperation;
import ua.homca.jforum.search.SearchResult;
import ua.homca.jforum.util.I18n;
import ua.homca.jforum.util.preferences.ConfigKeys;
import ua.homca.jforum.util.preferences.SystemGlobals;
import ua.homca.jforum.util.preferences.TemplateKeys;
import ua.homca.jforum.view.forum.common.Stats;
import ua.homca.jforum.view.forum.common.TopicsCommon;
import ua.homca.jforum.view.forum.common.ViewCommon;

import freemarker.template.SimpleHash;

import org.apache.log4j.Logger;

/**
 * @author Rafael Steil
 * @version $Id$
 */

public class SearchAction extends Command 
{
    private static final Logger log = Logger.getLogger(SearchAction.class);

	public SearchAction() { }
	
	public SearchAction(RequestContext request, ResponseContext response, SimpleHash context) 
	{
		this.request = request;
		this.response = response;
		this.context = context;
	}
	
	public void filters()
	{
		this.setTemplateName(TemplateKeys.SEARCH_FILTERS);
		this.context.put("categories", ForumRepository.getAllCategories());
		this.context.put("pageTitle", I18n.getMessage("ForumBase.search"));

        int forumId = -1;
        try {
            forumId = this.request.getIntParameter("forum_id");
        } catch (NumberFormatException nfex) { }
        this.context.put("forum_id", forumId);
	}
	
    public void newMessages() {
        UserSession userSession = SessionFacade.getUserSession();
        Date lastVisit = userSession.getLastVisit();

        SearchArgs args = this.buildSearchArgs();
        args.setDateRange(lastVisit, new Date());

        search(new NewMessagesSearchOperation(), args);
    }

    public void search() {
		SearchArgs args = null;
		try {
			args = this.buildSearchArgs();
		} catch (RuntimeException rex) {
			context.put("error", "The search request was malformed. Please use sensible values only.");
			filters();
			return;
		}
		Stats.record("Search", args.rawKeywords());
		this.search(new ContentSearchOperation(), args);
    }

    private void search (SearchOperation operation, SearchArgs args)
	{
		int start = args.startFrom();
		int recordsPerPage = SystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);

		//operation.performSearch(args);
		UserSession userSession = SessionFacade.getUserSession();
		SearchResult<?> searchResults = operation.performSearch(args, userSession.getUserId());
		operation.prepareForDisplay();
		List<?> results = operation.filterResults(operation.getResults());
		this.setTemplateName(operation.viewTemplate());
		
		this.context.put("results", results);
		this.context.put("categories", ForumRepository.getAllCategories());
		this.context.put("searchArgs", args);
		this.context.put("fr", new ForumRepository());
		this.context.put("pageTitle", I18n.getMessage("ForumBase.search"));
		this.context.put("openModeration", "1".equals(this.request.getParameter("openModeration")));
		this.context.put("postsPerPage", Integer.valueOf(SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE)));
		
		//ViewCommon.contextToPagination(start, results.size(), recordsPerPage);
		ViewCommon.contextToPagination(start, searchResults.getNumberOfHits(), recordsPerPage);
		TopicsCommon.topicListingBase();
	}

	private SearchArgs buildSearchArgs() {
        SearchArgs args = new SearchArgs();

        args.setKeywords(this.request.getParameter("search_keywords"));

        args.setSearchIn(request.getParameter("search_in"));

        args.setOrderBy(this.request.getParameter("sort_by"));
        args.setOrderDir(this.request.getParameter("sort_dir"));
        args.startFetchingAtRecord(ViewCommon.getStartPage());
        args.setMatchType(this.request.getParameter("match_type"));

        // setter handles these optional properties if not passed
        args.setGroupByForum("true".equals(this.request.getParameter("groupByForum")));
        args.setSearchDate(this.request.getParameter("search_date"));
        setDateRange(args, this.request.getParameter("search_date"));

        args.setUserId(this.request.getParameter("user_id"));
        args.setUsername(this.request.getParameter("username"));

		if (this.request.getParameter("search_forum") != null && !"".equals(this.request.getParameter("search_forum"))) {
			args.setForumId(this.request.getIntParameter("search_forum"));
		}

        return args;
    }

    private void setDateRange (SearchArgs args, String requestDateRange) {
        if (requestDateRange == null || requestDateRange.equals("ALL")) {
            args.setDateRange(null, null);
        } else {
            Calendar cal = new GregorianCalendar();
            try {
                int numDaysAgo = Integer.parseInt(requestDateRange);
                cal.add(Calendar.DATE, -numDaysAgo);
                Date fromDate = cal.getTime();
                Date toDate = new Date();
                args.setDateRange(fromDate, toDate);
            } catch (NumberFormatException e) {
				String ip = null;
				if (request instanceof WebRequestContext) {
					ip = ((WebRequestContext) request).getRemoteAddr();
				}
				log.error(requestDateRange + " is not a date. Not possible through the UI. IP " + ip);
				throw new RuntimeException("The search request was malformed. Please use sensible values only.");
            }
        }
    }

	/** 
	 * @see ua.homca.jforum.Command#list()
	 */
	public void list()  
	{
		this.filters();
	}
}
