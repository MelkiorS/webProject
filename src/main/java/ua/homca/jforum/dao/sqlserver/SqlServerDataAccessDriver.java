/*
 * Copyright (c) JForum Team
 * All rights reserved.

 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:

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
 * Created on 29/05/2004 00:12:37
 * The JForum Project
 * http://www.jforum.net
 */
package ua.homca.jforum.dao.sqlserver;

import ua.homca.jforum.dao.KarmaDAO;
import ua.homca.jforum.dao.ModerationLogDAO;
import ua.homca.jforum.dao.PostDAO;
import ua.homca.jforum.dao.TopicDAO;
import ua.homca.jforum.dao.UserDAO;

/**
 * @author Andre de Andrade da Silva (<a href="mailto:andre.de.andrade@gmail.com">andre.de.andrade@gmail.com</a>)
 * @author Dirk Rasmussen (<a href="mailto:d.rasmussen@bevis.de">d.rasmussen@bevis.de</a>)
 * @author Andowson Chang
 * @version $Id$
 */
public class SqlServerDataAccessDriver extends ua.homca.jforum.dao.generic.GenericDataAccessDriver
{
	private static PostDAO postDao = new SqlServerPostDAO();
	private static TopicDAO topicDao = new SqlServerTopicDAO();
	private static UserDAO userDao = new SqlServerUserDAO();
	private static KarmaDAO karmaDao = new SqlServerKarmaDAO();
	private static ModerationLogDAO moderationLogDao = new SqlServerModerationLogDAO();

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newPostDAO()
	 */
	public ua.homca.jforum.dao.PostDAO newPostDAO()
	{
		return postDao;
	}

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newTopicDAO()
	 */
	public ua.homca.jforum.dao.TopicDAO newTopicDAO()
	{
		return topicDao;
	}
	
	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newUserDAO()
	 */
	public ua.homca.jforum.dao.UserDAO newUserDAO()
	{
		return userDao;
	}	

	/** 
	 * @see ua.homca.jforum.dao.DataAccessDriver#newKarmaDAO()
	 */
	public ua.homca.jforum.dao.KarmaDAO newKarmaDAO()
	{
		return karmaDao;
	}
	
	/**
     * @see ua.homca.jforum.dao.generic.GenericDataAccessDriver#newModerationLogDAO()
     */
    public ModerationLogDAO newModerationLogDAO() 
    {
        return moderationLogDao;
    }
}
