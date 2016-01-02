<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="rw.ktc.cms.web.security.UserWeb" %>
<%@ page import="org.springframework.security.core.GrantedAuthority" %>
<%@ page import="java.io.PrintWriter" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div ><a href="/user/content/addpersonaldesire.html">Добавить анкету</a></div>
<div><a href="/user/content/allpersonaldesire.html">Список заполнивших анкету</a></div>




