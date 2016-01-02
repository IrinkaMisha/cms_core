<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!-- begin container_block-->
<c:if test="${empty dynContent.getContainerByName(nameContainer)}">
    <div class="container-block block-empty" id="${dynContent.id}">
        <jsp:include page="blockidentification.jsp"/>
            <%--<span class="block-id block-show" data-ct="${container.type}" data-cid="${dynContent.id}" data-csn="${dynContent.shablonName}" data-name="${nameContainer}"></span>--%>
            <%--<span class="block-id block-show" data-ct="${container.type}" data-cid="${dynContent.id}" data-csn="${dynContent.shablonName}" data-name="${container.name}">type=${container.type} , id=${dynContent.id} shablon=${dynContent.shablonName} name=${container.name} content=${container.content}</span>--%>
            <%--<jsp:include page="../../../pages/blockLoremIpsum.jsp"/>--%>
    </div>
</c:if>
<c:if test="${not empty dynContent.getContainerByName(nameContainer)}">
    <div data-cn="${nameContainer}" class="container-block clearfix cbname_${nameContainer} block-notempty ">
        <c:set var="oldContainer" scope="page" value="${container}"/>
        <c:set var="container" scope="request" value="${dynContent.getContainerByName(nameContainer)}"/>
        <jsp:include page="blockidentification.jsp"/>
            <%--<span class="block-id block-show" data-ct="${container.type}" data-cid="${dynContent.id}" data-csn="${dynContent.shablonName}" data-name1="${container.name}" data-name="${nameContainer}"></span>--%>
            <%--<span class="block-id block-show" data-ct="${container.type}" data-cid="${dynContent.id}" data-csn="${dynContent.shablonName}" data-name="${container.name}">type=${container.type} , id=${dynContent.id} shablon=${dynContent.shablonName} name=${container.name} content=${container.content}</span>--%>
        <c:catch var="catchException">
            <c:choose>
                <c:when test="${container.type == 'text'}">
                    ${container.content}
                </c:when>
                <c:when test="${container.type == 'jsp'}">
                    <jsp:include page="${container.content}"/>
                    <%--<%@ include file="static.html" %>--%>
                </c:when>
                <c:when test="${container.type == 'container'}">
                    <c:set var="oldDynContent" scope="page" value="${dynContent}"/>
                    <c:set var="dynContent" scope="request" value="${dynContent.getDynamicContent(container.content)}"/>
                    <jsp:include
                            page="/WEB-INF/views/templates/dynamictemplates/template_id_${dynContent.shablonName}.jsp"/>
                    <c:set var="dynContent" scope="request" value="${oldDynContent}"/>
                </c:when>
                <c:when test="${container.type == 'tiles'}">
                    <c:catch var="catchException">
                        <tiles:insertDefinition name="${container.content}"/>
                    </c:catch>
                    <c:if test="${catchException != null}">
                        <jsp:include page="../../${container.content}.jsp"/>
                    </c:if>
                </c:when>
                <c:when test="${container.type == 'page'}">
                    <jsp:include page="${container.content}"/>
                </c:when>
                <c:otherwise>
                    ${container.content}
                </c:otherwise>
            </c:choose>
        </c:catch>
        <c:if test="${catchException != null}">
            <p>The exception is : ${catchException} <br/>
                There is an exception: ${catchException.message}</p>
        </c:if>

        <c:set var="container" scope="request" value="${oldContainer}"/>
    </div>
</c:if>
<!-- end container_block-->


