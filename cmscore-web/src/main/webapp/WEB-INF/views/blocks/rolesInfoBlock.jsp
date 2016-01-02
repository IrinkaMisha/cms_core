<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="rw.ktc.cms.web.security.UserWeb" %>
<%@ page import="org.springframework.security.core.GrantedAuthority" %>
<%@ page import="java.io.PrintWriter" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<sec:authorize access="isAuthenticated()" >
<div>
    <%
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PrintWriter outr = response.getWriter();
        if(null != auth && auth.getPrincipal() instanceof UserWeb){
            for (GrantedAuthority ga : ((UserWeb) auth.getPrincipal()).getAuthorities()) {
                outr.println(ga.getAuthority()+"<br>");
            }
        }
    %>
</div>
</sec:authorize>




