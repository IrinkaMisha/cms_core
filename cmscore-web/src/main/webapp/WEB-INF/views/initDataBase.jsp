<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: sedler
  Date: 13.04.2015
  Time: 11:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1>Инициализация базы данных и первоначальная настройка</h1>
<form:form method="post" action="${pageContext.request.contextPath}/cms/initdatabase.html" modelAttribute="databaseForm">
    <div>
        <label for="login">Логин базы*</label>
        <form:input size="20" name="login" path="login" id="login"/>
        <label for="password">Пароль базы*</label>
        <form:password name="password" size="20" path="password" id="password"/>
    </div>
    <div>
        <label for="dialect">Диалект</label>
        <form:select name="driver" path="dialect"  id="dialect" items="${listDialect}"/>
        <label for="dataBase">Драйвер</label>
        <form:select name="dataBase" path="driverClassName"  id="dataBase" items="${listDatabase}"/>
    </div>
    <div>
        <label for="url">URL*</label>
        <form:input path="url" name="url" size="20" id="url"/>
    </div>
    <input type="submit" value="Отправить"/>
</form:form>
</body>
</html>
