<%--
  Created by IntelliJ IDEA.
  User: vuhai
  Date: 1/19/2024
  Time: 1:47 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi">
    <head>
        <jsp:include page="../common/common-css.jsp"/>
        <link rel="stylesheet" href="<c:url value="/css/register.css"/>">
        <title>Kích hoạt tài khoản</title>
    </head>
    <body>
        <jsp:include page="../common/header.jsp"/>
        <div class="vh-100 d-flex justify-content-center align-items-center">
            <div class="form-bloat shadow rounded-3">
                <div class="mb-2 d-flex justify-content-between align-content-center">
                    <div class="title d-flex align-content-center">
                        <h3>Kích hoạt tài khoản</h3>
                    </div>

                    <div class="logo">
                        <img src="<c:url value="/img/logo.png"/>" alt="Trade System Logo" class="img-fluid logo">
                    </div>

                </div>

                <c:if test="${requestScope.SUCCESS_MESSAGE != null || requestScope.FAILED_MESSAGE != null}">
                    <div class="form-group">
                        <c:if test="${requestScope.SUCCESS_MESSAGE != null}">
                            <h3 class="success-message">${requestScope.SUCCESS_MESSAGE}</h3>
                            <p><a href="<c:url value="/login"/> ">Chuyển sang trang login!</a></p>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <c:if test="${requestScope.FAILED_MESSAGE != null}">
                            <h3 class="error-message">${requestScope.FAILED_MESSAGE}</h3>
                        </c:if>
                    </div>
                </c:if>
            </div>
        </div>

    </body>
    <jsp:include page="../common/common-js.jsp"/>
</html>
