<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="by.imix.cms.web.Const" %>
<c:set var="ctx" scope="request" value="${pageContext.request.contextPath}"/>


<link href="${ctx}/resources/css/adminMenu.css?v2" rel="stylesheet">
<link href="<c:url value="<%=Const.JQUERY_UI_THEMES_CSS%>"/>" rel="stylesheet">
<link href="<c:url value="<%=Const.JQUERY_CONTEXT_MENU_CSS%>"/>" rel="stylesheet">
<link href="<c:url value="<%=Const.BOOTSTRAP_CSS%>"/>" rel="stylesheet">

<script type="text/javascript" src="<c:url value="<%=Const.JQUERY_JS%>"/>"></script>
<script type="text/javascript" src="<c:url value="<%=Const.JQUERY_UI_JS%>"/>"></script>

<%-- http://www.jstree.com/ --%>
<script type="text/javascript" src="${ctx}/webjars/jstree/3.1.1/jstree.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/webjars/jstree/3.1.1/themes/default/style.min.css"/>

<script type="text/javascript" src="/resources/script/injeditorwebjar.js"></script>
<%--<script type="text/javascript" src="/resources/script/injeditor.js"></script>--%>
<link rel="stylesheet" type="text/css" href="/resources/css/root.css"/>