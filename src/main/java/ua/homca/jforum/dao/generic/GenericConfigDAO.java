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
 * Created on Dec 29, 2004 1:29:52 PM
 * The JForum Project
 * http://www.jforum.net
 */
package ua.homca.jforum.dao.generic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.homca.jforum.JForumExecutionContext;
import ua.homca.jforum.entities.Config;
import ua.homca.jforum.exceptions.DatabaseException;
import ua.homca.jforum.util.DbUtils;
import ua.homca.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id$
 */
public class GenericConfigDAO implements ua.homca.jforum.dao.ConfigDAO
{
	/**
	 * @see ua.homca.jforum.dao.ConfigDAO#insert(ua.homca.jforum.entities.Config)
	 */
	public void insert(final Config config)
	{
		PreparedStatement pstmt = null;
		try {
			pstmt = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("ConfigModel.insert"));
			pstmt.setString(1, config.getName());
			pstmt.setString(2, config.getValue());
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(pstmt);
		}
	}

	/**
	 * @see ua.homca.jforum.dao.ConfigDAO#update(ua.homca.jforum.entities.Config)
	 */
	public void update(final Config config)
	{
		PreparedStatement pstmt = null;
		try {
			pstmt = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("ConfigModel.update"));
			pstmt.setString(1, config.getValue());
			pstmt.setString(2, config.getName());
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(pstmt);
		}
	}

	/**
	 * @see ua.homca.jforum.dao.ConfigDAO#delete(ua.homca.jforum.entities.Config)
	 */
	public void delete(final Config config)
	{
		PreparedStatement pstmt = null;
		try {
			pstmt = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("ConfigModel.delete"));
			pstmt.setInt(1, config.getId());
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(pstmt);
		}
	}

	/**
	 * @see ua.homca.jforum.dao.ConfigDAO#selectAll()
	 */
	public List<Config> selectAll()
	{
		final List<Config> list = new ArrayList<Config>();

		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			pstmt = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("ConfigModel.selectAll"));
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				list.add(this.makeConfig(resultSet));
			}

			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(resultSet, pstmt);
		}
	}

	/**
	 * @see ua.homca.jforum.dao.ConfigDAO#selectByName(java.lang.String)
	 */
	public Config selectByName(final String name)
	{
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			pstmt = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ConfigModel.selectByName"));
			pstmt.setString(1, name);
			resultSet = pstmt.executeQuery();
			Config config = null;

			if (resultSet.next()) {
				config = this.makeConfig(resultSet);
			}

			return config;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(resultSet, pstmt);
		}
	}

	protected Config makeConfig(final ResultSet resultSet) throws SQLException
	{
		final Config config = new Config();
		config.setId(resultSet.getInt("config_id"));
		config.setName(resultSet.getString("config_name"));
		config.setValue(resultSet.getString("config_value"));

		return config;
	}
}
