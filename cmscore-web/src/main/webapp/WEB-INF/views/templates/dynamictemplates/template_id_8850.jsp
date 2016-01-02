<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="description" content="главный шаблон для отображения">
    <title>
        <%--Если есть атрибут titlePage - то вставляется он иначе вставляется атрибут titleKey --%>
        <%--<c:set var="container" value="containers2"/>--%>
    </title>
    <style>
        /*
              (\_/)
             (='.'=)
         E[:I/\/\/\/I:]3
             (")_(")
        */
        * {
            margin: 0;
            padding: 0;
        }

        html {
            height: 100%;
        }

        body {
            height:auto !important;
            height:100%;
            min-height:100%;
            margin-top: -5px;
            padding-top: 5px;
        }

    </style>
    <!--#begin headblock #-->
    <c:set var="nameContainer" scope="request" value="${'headblock'}"/>
    <jsp:include page="../../manager/dynamicpage/containerSelHead.jsp"/>
    <!--# headblock end#-->
</head>
<body>
<!--#begin block1 #-->
<c:set var="nameContainer" scope="request" value="${'block1'}"/>
<jsp:include page="../../manager/dynamicpage/containerForBody.jsp"/>
<!--# block1 end#-->
</body>
</html>