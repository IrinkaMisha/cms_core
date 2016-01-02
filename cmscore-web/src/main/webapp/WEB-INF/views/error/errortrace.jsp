<%@ page isErrorPage="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
    .tftable {
        font-size: 12px;
        color: #333333;
        width: 100%;
        border-width: 1px;
        border-color: #729ea5;
        border-collapse: collapse;
    }

    .tftable th {
        font-size: 12px;
        background-color: #acc8cc;
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #729ea5;
        text-align: left;
    }

    .tftable tr {
        background-color: #d4e3e5;
        valign: top
    }

    .tftable td {
        font-size: 12px;
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #729ea5;
    }
</style>

<table class="tftable" border="1">
    <tr>
        <td><b>Error:</b></td>
        <td>${pageContext.exception}</td>
    </tr>
    <c:if test="${not empty datetime}">
        <tr>
            <td><b>datetime:</b></td>
            <td>${datetime}</td>
        </tr>
    </c:if>
    <tr>
        <td><b>URI:</b></td>
        <td>${pageContext.errorData.requestURI}</td>
    </tr>
    <tr>
        <td><b>Status code:</b></td>
        <td>${pageContext.errorData.statusCode}</td>
    </tr>
    <tr>
        <td><b>Stack trace:</b></td>
        <td>
            <c:forEach var="trace"
                       items="${pageContext.exception.stackTrace}">
                <p>${trace}</p>
            </c:forEach>
        </td>
    </tr>
</table>
<script>
    $('.loading').hide();
</script>