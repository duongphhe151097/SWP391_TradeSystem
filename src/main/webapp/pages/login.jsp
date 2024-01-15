<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 1/12/2024
  Time: 4:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="../common/common-css.jsp"/>
</head>
<body>
<h1><c:out value="${requestScope.PAGE_TITLE}"/></h1>
<h1><c:out value="${requestScope.CAPTCHA_CODE}"/></h1>
<img src="<c:url value="/captcha"/> " alt="Captcha img">
</body>
<jsp:include page="../common/common-js.jsp"/>
</html>
