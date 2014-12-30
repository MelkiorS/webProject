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
 * Created on Feb 17, 2003 / 8:50:57 PM
 * The JForum Project
 * http://www.jforum.net
 */
package ua.homca.jforum.entities;

import java.io.Serializable;

/**
 * Represents a group in the system. 
 * 
 * @author Rafael Steil
 * @version $Id$
 */
public class Group implements Serializable
{
	private static final long serialVersionUID = 2605949396973145726L;
	private int id;
	private int parentId;
	private String name;
	private String description;
	
	/**
	 * Default constructor	 
	 * **/
	public Group() {
		// Empty Constructor
	}
	
	/**
	 * Create a new <code>Group</code> object.
	 *  
	 * @param id The Group ID
	 * @param parentId The parent ID for the group
	 * @param name The Group Name
	 * @param description The Group Description
 	 **/
	public Group(final int id, final int parentId, final String name, final String description) 
	{
		this.name = name;
		this.id = id;
		this.parentId = parentId;
		this.description = description;		
	}
	
	/**
	 * @return String
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return int
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * @return int
	 */
	public int getParentId() {
		return this.parentId;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the description.
	 * @param description The description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * Sets the parent id.
	 * @param parentId The parent id to set
	 */
	public void setParentId(final int parentId) {
		this.parentId = parentId;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/* 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object obj) 
	{
		if (!(obj instanceof Group)) {
			return false;
		}
		
		return (((Group)obj).getId() == this.id);
	}

	/* 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return this.id;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return this.name +" - "+ this.id;
	}

}