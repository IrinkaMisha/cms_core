<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%--see AddMenuForAdminFilter--%>
<% boolean showOnlyForUrlTemplate = request.getServletPath().contains("template");
   request.setAttribute("showOnlyForUrlTemplate", showOnlyForUrlTemplate);
%>
<c:if test="${showOnlyForUrlTemplate}">
    <div class="block-center">Здесь будет отображено содержимое страницы, указанной в redirect URL-s</div>
</c:if>
<div class="container_block cbname_centerPage">
    <c:choose>
        <c:when test="${centerPage.type == 'text'}">
            ${centerPage.content}
        </c:when>
        <c:when test="${centerPage.type == 'jsp'}">
            <jsp:include page="../../${centerPage.content}.jsp"/>
        </c:when>
        <c:when test="${centerPage.type == 'tiles'}">
            <c:catch var ="catchException">
                <tiles:insertDefinition name="${centerPage.content}" />
            </c:catch>
            <c:if test = "${catchException != null}">
                <jsp:include page="../../${centerPage.content}.jsp"/>
            </c:if>
        </c:when>
        <c:when test="${centerPage.type == 'page'}">
            <jsp:include page="${centerPage.content}"/>
        </c:when>
        <c:otherwise>
            ${container.content}
        </c:otherwise>
    </c:choose>
</div>