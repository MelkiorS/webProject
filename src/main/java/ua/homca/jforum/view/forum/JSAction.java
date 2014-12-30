/*
 * Created on 27/06/2005 00:20:38
 */
package ua.homca.jforum.view.forum;

import ua.homca.jforum.Command;
import ua.homca.jforum.JForumExecutionContext;
/**
 * Loads and parse javascript files with FTL statements.
 * 
 * @author Rafael Steil
 * @version $Id$
 */
public class JSAction extends Command
{
	/**
	 * Loads and parses a javascript file. 
	 * The filename should be into the "js" directory and should
	 * have the extension ".js".
	 * @see ua.homca.jforum.Command#list()
	 */
	public void list() 
	{
		JForumExecutionContext.setContentType("text/javascript");
		
		final String filename = this.request.getParameter("js");
		
		this.templateName = "js/" + filename + ".js";
	}
}
