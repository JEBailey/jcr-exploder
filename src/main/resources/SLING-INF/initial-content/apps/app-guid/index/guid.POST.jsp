<%@page import="org.apache.jackrabbit.util.Text"%>
<%@page import="org.apache.jackrabbit.commons.JcrUtils"%>
<%@page import="org.apache.sling.jcr.resource.JcrResourceUtil"%>
<%@page import="java.util.UUID"%>
<%@page import="javax.jcr.Node"%>
<%@page import="javax.jcr.Session"%>
<%@ page language="java" session="false" contentType="text/plain"%>
<%@ taglib prefix="sling"
	uri="http://sling.apache.org/taglibs/sling/1.0"%>
<sling:defineObjects/>
<%
	Session session = resourceResolver.adaptTo(Session.class);
	String app_name = request.getParameter("create").toLowerCase();
	String guid = UUID.nameUUIDFromBytes(app_name.getBytes()).toString();
	String valid_app_name = Text.escapeIllegalJcrChars(app_name);//Text.escapeIllegalJcrChars(app_name);
	
	Node child = JcrResourceUtil.createPath("/srv/app/guid/"+valid_app_name, "sling:folder","nt:unstructured", session, true);
	JcrResourceUtil.setProperty(child, "uuid", guid);
	JcrResourceUtil.setProperty(child, "title", app_name);
	session.save();
%>