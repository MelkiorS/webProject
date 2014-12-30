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
 * Created on 02/10/2005 20:04:15
 * The JForum Project
 * http://www.jforum.net
 */
package ua.homca.jforum.view.forum;

import java.util.Iterator;
import java.util.List;

import ua.homca.jforum.Command;
import ua.homca.jforum.JForumExecutionContext;
import ua.homca.jforum.dao.DataAccessDriver;
import ua.homca.jforum.dao.ModerationLogDAO;
import ua.homca.jforum.dao.PostDAO;
import ua.homca.jforum.dao.TopicDAO;
import ua.homca.jforum.entities.ModerationLog;
import ua.homca.jforum.entities.Post;
import ua.homca.jforum.entities.Topic;
import ua.homca.jforum.repository.ForumRepository;
import ua.homca.jforum.repository.SecurityRepository;
import ua.homca.jforum.security.SecurityConstants;
import ua.homca.jforum.util.I18n;
import ua.homca.jforum.util.preferences.ConfigKeys;
import ua.homca.jforum.util.preferences.SystemGlobals;
import ua.homca.jforum.util.preferences.TemplateKeys;
import ua.homca.jforum.view.forum.common.PostCommon;
import ua.homca.jforum.view.forum.common.ViewCommon;

/**
 * @author Rafael Steil
 * @version $Id$
 */
public class ModerationAction extends Command
{
	private static final String RETURN_URL = "returnUrl";
	
	/**
	 * @throws UnsupportedOperationException always
	 * @see ua.homca.jforum.Command#list()
	 */
	public void list()
	{
		throw new UnsupportedOperationException();
	}
	
	public void showActivityLog() 
	{
		if (!SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_LOG)) {
			this.denied();
			return;
		}
		
		final ModerationLogDAO dao = DataAccessDriver.getInstance().newModerationLogDAO();
		
		final int start = ViewCommon.getStartPage();
		final int recordsPerPage = SystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);
		
		final List<ModerationLog> list = dao.selectAll(start, recordsPerPage);
		final boolean canAccessFullModerationLog = SecurityRepository.canAccess(SecurityConstants.PERM_FULL_MODERATION_LOG);
		
		final PostDAO postDao = DataAccessDriver.getInstance().newPostDAO();
		final TopicDAO topicDao = DataAccessDriver.getInstance().newTopicDAO();
		
		for (final Iterator<ModerationLog> iter = list.iterator(); iter.hasNext();) {
			final ModerationLog log = iter.next();
			
			if (log.getPostId() > 0) {
				final Post post = postDao.selectById(log.getPostId());
				
				if (post.getId() > 0 && ForumRepository.getForum(post.getForumId()) == null) {
					iter.remove();
					continue;
				}
			}
			else if (log.getTopicId() > 0) {
				Topic topic = topicDao.selectRaw(log.getTopicId());
				
				if (topic.getId() > 0 && ForumRepository.getForum(topic.getForumId()) == null) {
					iter.remove();
					continue;
				}
			}
			
			if (log.getOriginalMessage() != null && canAccessFullModerationLog) {
				Post post = new Post();
				post.setText(log.getOriginalMessage());
				
				log.setOriginalMessage(PostCommon.preparePostForDisplay(post).getText());
			}
		}
		
		this.setTemplateName(TemplateKeys.MODERATION_SHOW_ACTIVITY_LOG);
		this.context.put("activityLog", list);
		this.context.put("canAccessFullModerationLog", canAccessFullModerationLog);
		
		int totalRecords = dao.totalRecords();
		
		ViewCommon.contextToPagination(start, totalRecords, recordsPerPage);
	}
	
	private void denied() {
		this.setTemplateName(TemplateKeys.MODERATION_LOG_DENIED);
		this.context.put("message", I18n.getMessage("ModerationLog.denied"));
	}
	
	public void doModeration()
	{
		String returnUrl = this.request.getParameter(RETURN_URL);
		
		new ModerationHelper().doModeration(returnUrl);
		
		this.context.put(RETURN_URL, returnUrl);
		
		if (JForumExecutionContext.getRequest().getParameter("topicMove") != null) {
			this.setTemplateName(TemplateKeys.MODERATION_MOVE_TOPICS);
		}
	}
	
	public void moveTopic()
	{
		new ModerationHelper().moveTopicsSave(this.request.getParameter(RETURN_URL));
	}
	
	public void moderationDone()
	{
		this.setTemplateName(new ModerationHelper().moderationDone(this.request.getParameter(RETURN_URL)));
	}
}
