<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isErrorPage="true" %>
<%--<html>--%>
<%--<head>--%>
    <%--<title><spring:message code="errors.error"/></title>--%>
<%--</head>--%>
<%--<body>--%>
<button onclick="history.back()">Вернуться на предыдущую страницу</button>
<h2>404 - страница не найдена</h2>
<br/>
<p><b>Код:</b> ${pageContext.errorData.statusCode}</p>
<p><b>Запрашиваемый URI:</b> ${pageContext.request.scheme}://${header.host}${pageContext.errorData.requestURI}</p>

<%--<div align="center">--%>
<%--<span><img src="/resources/images/error404.jpg"></span>--%>
<%--</div>--%>

<%--</body>--%>
<%--</html>--%>