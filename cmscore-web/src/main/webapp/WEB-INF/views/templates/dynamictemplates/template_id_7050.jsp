<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
    <!--#begin block1 #-->
    <c:set var="nameContainer" scope="request" value="${'block1'}"/>
    <jsp:include page="../../manager/dynamicpage/containerForBody.jsp"/>
    <!--# block1 end#-->
</div>
<div>
    <!--#begin block2 #-->
    <c:set var="nameContainer" scope="request" value="${'block2'}"/>
    <jsp:include page="../../manager/dynamicpage/containerForBody.jsp"/>
    <!--# block2 end#-->
</div>
