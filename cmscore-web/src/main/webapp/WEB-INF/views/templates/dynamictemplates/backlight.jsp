<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="rw.ktc.cms.web.Const" %>
<script type="text/javascript" src="<c:url value="<%=Const.JQUERY_JS%>"/>"></script>
<link href="/resources/css/adminMenu.css?v2" rel="stylesheet">
<script type="text/javascript" src="<c:url value="<%=Const.JQUERY_JS%>"/>"></script>
<script>
  $(function () {
//      prepend("<span class='container-setting ui-icon ui-icon-wrench'></span>")
        $('div.block-empty').addClass("select-empty");
        $('div.block-center').addClass("select-center");
  });
</script>
