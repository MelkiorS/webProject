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
 * Created on Jan 3, 2005 1:20:24 PM
 * The JForum Project
 * http://www.jforum.net
 */
package ua.homca.jforum.sso;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import ua.homca.jforum.JForumExecutionContext;
import ua.homca.jforum.dao.UserDAO;
import ua.homca.jforum.entities.User;
import ua.homca.jforum.exceptions.ForumException;
import ua.homca.jforum.util.DbUtils;
import ua.homca.jforum.util.MD5;
import ua.homca.jforum.util.preferences.SystemGlobals;

/**
 * Default login authenticator for JForum.
 * This authenticator will validate the input against
 * <i>jforum_users</i>. 
 * 
 * @author Rafael Steil
 * @version $Id$
 */
public class DefaultLoginAuthenticator implements LoginAuthenticator
{
	private transient UserDAO userModel;

	/**
	 * @see ua.homca.jforum.sso.LoginAuthenticator#setUserModel(ua.homca.jforum.dao.UserDAO)
	 */
	public void setUserModel(final UserDAO userModel)
	{
		this.userModel = userModel;
	}

	/**
	 * @see ua.homca.jforum.sso.LoginAuthenticator#validateLogin(String, String, java.util.Map)
	 */
	public User validateLogin(final String username, final String password, final Map<?, ?> extraParams)
	{
		User user = null;
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		
		try 
		{
			pstmt = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("UserModel.login"));
			pstmt.setString(1, username);
			pstmt.setString(2, MD5.crypt(password));

			resultSet = pstmt.executeQuery();
			if (resultSet.next() && resultSet.getInt("user_id") > 0) {
				user = this.userModel.selectById(resultSet.getInt("user_id"));
			}
		}
		catch (SQLException e)
		{
			throw new ForumException(e);
		}
		finally
		{
			DbUtils.close(resultSet, pstmt);
		}

		if (user != null && !user.isDeleted() && (user.getActivationKey() == null || user.isActive())) {
			return user;
		}

		return null;
	}
}
