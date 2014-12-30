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
 * Created on Jan 11, 2005 11:44:06 PM
 * The JForum Project
 * http://www.jforum.net
 */
package ua.homca.jforum.view.forum;

import ua.homca.jforum.Command;
import ua.homca.jforum.JForumExecutionContext;
import ua.homca.jforum.SessionFacade;
import ua.homca.jforum.dao.DataAccessDriver;
import ua.homca.jforum.dao.KarmaDAO;
import ua.homca.jforum.dao.PostDAO;
import ua.homca.jforum.entities.Karma;
import ua.homca.jforum.entities.KarmaStatus;
import ua.homca.jforum.entities.Post;
import ua.homca.jforum.repository.PostRepository;
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
public class KarmaAction extends Command
{
	public void insert()
	{
		if (!SecurityRepository.canAccess(SecurityConstants.PERM_KARMA_ENABLED)) {
			this.error("Karma.featureDisabled", null);
			return;
		}

		int postId = this.request.getIntParameter("post_id");
		int fromUserId = SessionFacade.getUserSession().getUserId();

		PostDAO pm = DataAccessDriver.getInstance().newPostDAO();
		Post post = pm.selectById(postId);

		if (fromUserId == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
			this.error("Karma.anonymousIsDenied", post);
			return;
		}

		if (post.getUserId() == fromUserId) {
			this.error("Karma.cannotSelfVote", post);
			return;
		}

		KarmaDAO km = DataAccessDriver.getInstance().newKarmaDAO();
		
		if (!km.userCanAddKarma(fromUserId, postId)) {
			this.error("Karma.alreadyVoted", post);
			return;
		}
		
		// Check range
		int points = this.request.getIntParameter("points");
		
		if (points < SystemGlobals.getIntValue(ConfigKeys.KARMA_MIN_POINTS)
				|| points > SystemGlobals.getIntValue(ConfigKeys.KARMA_MAX_POINTS)) {
			this.error("Karma.invalidRange", post);
			return;
		}

		Karma karma = new Karma();
		karma.setFromUserId(fromUserId);
		karma.setPostUserId(post.getUserId());
		karma.setPostId(postId);
		karma.setTopicId(post.getTopicId());
		karma.setPoints(points);

		km.addKarma(karma);
		
		post.setKarma(new KarmaStatus(post.getId(), points));
		
		if (SystemGlobals.getBoolValue(ConfigKeys.POSTS_CACHE_ENABLED)) {
			PostRepository.update(post.getTopicId(), PostCommon.preparePostForDisplay(post));
		}

		JForumExecutionContext.setRedirect(this.urlToTopic(post));
	}

	private void error(String message, Post post)
	{
		this.setTemplateName(TemplateKeys.KARMA_ERROR);

		if (post != null) {
			this.context.put("message", I18n.getMessage(message, new String[] { this.urlToTopic(post) }));
		}
		else {
			this.context.put("message", I18n.getMessage(message));
		}
	}

	private String urlToTopic(Post post)
	{
		return JForumExecutionContext.getRequest().getContextPath() + "/posts/list/" 
			+ ViewCommon.getStartPage()
			+ "/" + post.getTopicId()
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION)
			+ "#p" + post.getId();
	}

	/**
	 * @see ua.homca.jforum.Command#list()
	 */
	public void list() 
	{
		this.setTemplateName(TemplateKeys.KARMA_LIST);
		this.context.put("message", I18n.getMessage("invalidAction"));
	}
}
