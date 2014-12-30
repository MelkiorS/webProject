<%@page import="ua.homca.jforum.util.preferences.*" %>
<%@page import="java.io.File" %>
<%
	String cfg = SystemGlobals.getValue(ConfigKeys.INSTALLATION_CONFIG);
	System.out.println("PATH == " +cfg);
	String redirect = "forums/list.page";
	
	if (cfg == null || !(new File(cfg).exists()) || !SystemGlobals.getBoolValue(ConfigKeys.INSTALLED)) {	
		redirect = "install.jsp";    
	}	
	response.setStatus(301);
	response.setHeader("Location", redirect);
	response.setHeader("Connection", "close"); 
%>