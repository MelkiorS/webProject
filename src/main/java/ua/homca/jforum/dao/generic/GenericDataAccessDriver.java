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
 * Created on Mar 3, 2003 / 2:19:47 PM
 * The JForum Project
 * http://www.jforum.net
 */
package ua.homca.jforum.dao.generic;

import ua.homca.jforum.dao.ApiDAO;
import ua.homca.jforum.dao.AttachmentDAO;
import ua.homca.jforum.dao.BanlistDAO;
import ua.homca.jforum.dao.BannerDAO;
import ua.homca.jforum.dao.BookmarkDAO;
import ua.homca.jforum.dao.CategoryDAO;
import ua.homca.jforum.dao.ConfigDAO;
import ua.homca.jforum.dao.DataAccessDriver;
import ua.homca.jforum.dao.ForumDAO;
import ua.homca.jforum.dao.GroupDAO;
import ua.homca.jforum.dao.GroupSecurityDAO;
import ua.homca.jforum.dao.KarmaDAO;
import ua.homca.jforum.dao.LuceneDAO;
import ua.homca.jforum.dao.MailIntegrationDAO;
import ua.homca.jforum.dao.ModerationDAO;
import ua.homca.jforum.dao.ModerationLogDAO;
import ua.homca.jforum.dao.PollDAO;
import ua.homca.jforum.dao.PostDAO;
import ua.homca.jforum.dao.PrivateMessageDAO;
import ua.homca.jforum.dao.RankingDAO;
import ua.homca.jforum.dao.SmilieDAO;
import ua.homca.jforum.dao.SpamDAO;
import ua.homca.jforum.dao.SummaryDAO;
import ua.homca.jforum.dao.TopicDAO;
import ua.homca.jforum.dao.TreeGroupDAO;
import ua.homca.jforum.dao.UserDAO;
import ua.homca.jforum.dao.UserSessionDAO;
import ua.homca.jforum.dao.generic.security.GenericGroupSecurityDAO;

/**
 * @author Rafael Steil
 */
public class GenericDataAccessDriver extends DataAccessDriver 
{
	private static GroupDAO groupDao = new GenericGroupDAO();
	private static PostDAO postDao = new GenericPostDAO();
	private static PollDAO pollDao = new GenericPollDAO();
	private static RankingDAO rankingDao = new GenericRankingDAO();
	private static TopicDAO topicDao = new GenericTopicDAO();
	private static UserDAO userDao = new GenericUserDAO();
	private static TreeGroupDAO treeGroupDao = new GenericTreeGroupDAO();
	private static SmilieDAO smilieDao = new GenericSmilieDAO();
	private static GroupSecurityDAO groupSecurityDao = new GenericGroupSecurityDAO();
	private static PrivateMessageDAO privateMessageDao = new GenericPrivateMessageDAO();
	private static UserSessionDAO userSessionDao = new GenericUserSessionDAO();
	private static KarmaDAO karmaDao = new GenericKarmaDAO();
	private static BookmarkDAO bookmarkDao = new GenericBookmarkDAO();
	private static AttachmentDAO attachmentDao = new GenericAttachmentDAO();
	private static ModerationDAO moderationDao = new GenericModerationDAO();
	private static ForumDAO forumDao = new GenericForumDAO();
	private static CategoryDAO categoryDao = new GenericCategoryDAO();
	private static ConfigDAO configDao = new GenericConfigDAO();
	private static BannerDAO bannerDao = new GenericBannerDAO();
    private static SummaryDAO summaryDao = new GenericSummaryDAO();
    private static MailIntegrationDAO mailIntegrationDao = new GenericMailIntegrationDAO();
    private static ApiDAO apiDAO = new GenericApiDAO();
    private static BanlistDAO banlistDao = new GenericBanlistDAO();
    private static ModerationLogDAO moderationLogDao = new GenericModerationLogDAO();
    private static LuceneDAO luceneDao = new GenericLuceneDAO();
    private static SpamDAO spamDao = new GenericSpamDAO();

	/**
	 * @see ua.homca.jforum.dao.DataAccessDriver#newForumDAO()
	 */
	public ForumDAO newForumDAO() 
	{
		return forumDao;
	}

	/**
	 * @see ua.homca.jforum.dao.DataAccessDriver#newGroupDAO()
	 */
	public GroupDAO newGroupDAO() 
	{
		return groupDao;
	}

	/**
	 * @see ua.homca.jforum.dao.DataAccessDriver#newPostDAO()
	 */
	public PostDAO newPostDAO() 
	{
		return postDao;
	}

	/**
	 * @see ua.homca.jforum.dao.DataAccessDriver#newPollDAO()
	 */
	public PollDAO newPollDAO() 
	{
		return pollDao;
	}

	/**
	 * @see ua.homca.jforum.dao.DataAccessDriver#newRankingDAO()
	 */
	public RankingDAO newRankingDAO() 
	{
		return rankingDao;
	}

	/**
	 * @see ua.homca.jforum.dao.DataAccessDriver#newTopicDAO()
	 */
	public TopicDAO newTopicDAO() 
	{
		return topicDao;
	}

	/**
	 * @see ua.homca.jforum.dao.DataAccessDriver#newUserDAO()
	 */
	public UserDAO newUserDAO() 
	{
		return userDao;
	}

	/**
	 * @see ua.homca.jforum.dao.DataAccessDriver#newCategoryDAO()
	 */
	public CategoryDAO newCategoryDAO() 
	{
		return categoryDao;
	}

	/**
	 * @see ua.homca.jforum.dao.DataAccessDriver#newTreeGroupDAO()
	 */
	public TreeGroupDAO newTreeGroupDAO() 
	{
		return treeGroupDao;
	}

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newSmilieDAO()
	 */
	public SmilieDAO newSmilieDAO() 
	{
		return smilieDao;
	}

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newGroupSecurityDAO()
	 */
	public GroupSecurityDAO newGroupSecurityDAO() 
	{
		return groupSecurityDao;
	}

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newPrivateMessageDAO()
	 */
	public PrivateMessageDAO newPrivateMessageDAO() 
	{
		return privateMessageDao;
	}

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newUserSessionDAO()
	 */
	public UserSessionDAO newUserSessionDAO()
	{
		return userSessionDao;
	}

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newConfigDAO()
	 */
	public ConfigDAO newConfigDAO()
	{
		return configDao;
	}

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newKarmaDAO()
	 */
	public KarmaDAO newKarmaDAO()
	{
		return karmaDao;
	}

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newBookmarkDAO()
	 */
	public BookmarkDAO newBookmarkDAO()
	{
		return bookmarkDao;
	}

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newAttachmentDAO()
	 */
	public AttachmentDAO newAttachmentDAO()
	{
		return attachmentDao;
	}

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newModerationDAO()
	 */
	public ModerationDAO newModerationDAO()
	{
		return moderationDao;
	}

    /**
     * @see ua.homca.jforum.dao.DataAccessDriver#newBannerDAO()
     */
	public BannerDAO newBannerDAO()
	{
		return bannerDao;
	}
    
    /**
     * @see ua.homca.jforum.dao.DataAccessDriver#newSummaryDAO()
     */
    public SummaryDAO newSummaryDAO()
    {
        return summaryDao;
    }
    
    /**
     * @see ua.homca.jforum.dao.DataAccessDriver#newMailIntegrationDAO()
     */
    public MailIntegrationDAO newMailIntegrationDAO()
    {
    	return mailIntegrationDao;
    }

    /**
     * @see ua.homca.jforum.dao.DataAccessDriver#newApiDAO()
     */
    public ApiDAO newApiDAO()
    {
    	return apiDAO;
    }

    /**
     * @see ua.homca.jforum.dao.DataAccessDriver#newBanlistDAO()
     */
    public BanlistDAO newBanlistDAO()
    {
    	return banlistDao;
    }

    /**
     * @see ua.homca.jforum.dao.DataAccessDriver#newModerationLogDAO()
     */
    public ModerationLogDAO newModerationLogDAO()
    {
    	return moderationLogDao;
    }

    /**
     * @see ua.homca.jforum.dao.DataAccessDriver#newLuceneDAO()
     */
    public LuceneDAO newLuceneDAO()
    {
    	return luceneDao;
    }

    /**
     * @see ua.homca.jforum.dao.DataAccessDriver#newSpamDAO()
     */
    public SpamDAO newSpamDAO()
    {
    	return spamDao;
    }
}
