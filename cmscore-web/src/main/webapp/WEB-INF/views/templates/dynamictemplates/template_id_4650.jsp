<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: miha
  Date: 28.01.13
  Time: 11:48
  Это шаблон для редиректов
  todo поичинить
--%>
<link href="${ctx}/resources/css/root.css" rel="stylesheet">
<div id="wrapper">
    <div class="container">
        <div id="centerpanel">
            <div id="leftbar">
                <!--#begin block1 #-->
                <c:set var="nameContainer" scope="request" value="${'block1'}"/>
                <jsp:include page="../../manager/dynamicpage/containerForBody.jsp"/>
                <!--# block1 end#-->
            </div>
            <%--Центральная страница--%>
            <div  id="content">
                <!--#begin centerPage #-->
                <jsp:include page="../../manager/dynamicpage/centerPage.jsp"/>
                <!--# centerPage end#-->

                <%--<!--#begin block2 #-->--%>
                <%--<c:set var="nameContainer" scope="request" value="${'block2'}"/>--%>
                <%--<jsp:include page="../../manager/dynamicpage/containerForBody.jsp"/>--%>
                <%--<!--# block2 end#-->--%>
            </div>
        </div>
    </div>
</div>