<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 1/12/2024
  Time: 4:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi">
<head>
    <jsp:include page="../common/common-css.jsp"/>
    <link rel="stylesheet" href="<c:out value="${pageContext.request.contextPath}/css/register.css"/>">
    <title>Đăng nhập</title>
</head>
<body>
<div class="vh-100 d-flex justify-content-center align-items-center">
    <div class="col-md-5 p-5 shadow rounded-3">
        <div class="register">
            <div class="mb-2 d-flex justify-content-between align-content-center">
                <div class="title d-flex align-content-center">
                    <h3>Đăng nhập</h3>
                </div>

                <div class="logo">
                    <img src="<c:url value="/img/logo.png"/>" alt="Trade System Logo" class="img-fluid logo">
                </div>

            </div>
            <form action="<c:url value="/login"/>" method="post">

                <div class="form-group">
                    <label for="usernameInput">Tên đăng nhập</label>
                    <input type="text"
                           class="form-control" id="usernameInput" aria-describedby="usernameHelp"
                           placeholder="Nhập tên đăng nhập" value="${requestScope.VAR_USERNAME}" name="username">
                    <c:if test="${requestScope.USERNAME_ERROR != null}">
                        <small id="usernameHelp" class="form-text text-muted error-message">${requestScope.USERNAME_ERROR}</small>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="passwordInput">Mật khẩu</label>
                    <input type="password" class="form-control" id="passwordInput" aria-describedby="passwordHelp"
                           placeholder="Mật khẩu (ít nhất 8 kí tự)" name="password">
                    <c:if test="${requestScope.PASSWORD_ERROR != null}">
                        <small id="passwordHelp" class="form-text text-muted error-message">${requestScope.PASSWORD_ERROR}</small>
                    </c:if>
                </div>

                <div class="form-group">
                    <div class="form-row">
                        <div class="col mt-4" id="captcha">
                        </div>
                        <div class="col">
                            <label for="captchaInput">Mã captcha</label>
                            <input type="text" id="captchaInput" class="form-control" placeholder="Nhập mã captcha"
                                   name="captcha">
                            <c:if test="${requestScope.CAPTCHA_ERROR != null}">
                                <small class="form-text text-muted error-message">${requestScope.CAPTCHA_ERROR}</small>
                            </c:if>
                        </div>
                    </div>
                </div>
                <c:if test="${requestScope.SUCCESS_MESSAGE != null || requestScope.FAILED_MESSAGE != null}">
                    <div class="form-group">
                        <c:if test="${requestScope.FAILED_MESSAGE != null}">
                            <p class="error-message">${requestScope.FAILED_MESSAGE}</p>
                        </c:if>
                    </div>
                </c:if>

                <div class="form-group mt-5">
                    <div class="d-flex justify-content-center">
                        <button type="submit" class="btn btn-primary">Đăng nhập</button>
                    </div>

                    <div class="link mt-3">
                        <div class="form-row">
                            <div class="col d-flex justify-content-end">
                                <a href="<c:url value="/register"/>">Chưa có tài khoản? Đăng kí</a>
                            </div>
                            <div class="col d-flex justify-content-start ml-5">
                                <a href="<c:url value="/forgot"/>">Quên mật khẩu?</a>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>

</div>
</body>
<jsp:include page="../common/common-js.jsp"/>
</html>
