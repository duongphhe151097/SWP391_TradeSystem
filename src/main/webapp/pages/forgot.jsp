<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 1/14/2024
  Time: 1:46 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi">
    <head>
        <jsp:include page="../common/common-css.jsp"/>
        <link rel="stylesheet" href="<c:url value="/css/register.css"/>">
        <title>Quên mật khẩu</title>
    </head>
    <body>
        <div class="vh-100 d-flex justify-content-center align-items-center">
            <div class="form-bloat shadow rounded-3">
                <div class="mb-2 d-flex justify-content-between align-content-center">
                    <div class="title d-flex align-content-center">
                        <h3>Quên mật khẩu</h3>
                    </div>

                    <div class="logo">
                        <img src="<c:url value="/img/logo.png"/>" alt="Trade System Logo" class="img-fluid logo">
                    </div>

                </div>
                <form action="<c:url value="/forgot"/>" method="post">

                    <div class="form-group">
                        <label for="emailInput">Email</label>
                        <input type="email" class="form-control" id="emailInput" aria-describedby="emailHelp"
                               placeholder="Nhập email" value="${requestScope.VAR_EMAIL}" name="email">
                        <c:if test="${requestScope.EMAIL_ERROR != null}">
                            <small id="emailHelp"
                                   class="form-text text-muted error-message">${requestScope.EMAIL_ERROR}</small>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <div class="form-row">
                            <div class="col mt-4" id="captcha">
                                <!-- Đây là nơi để hiển thị ảnh Captcha -->
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
                            <c:if test="${requestScope.SUCCESS_MESSAGE != null}">
                                <p class="success-message">${requestScope.SUCCESS_MESSAGE}</p>
                            </c:if>
                            <c:if test="${requestScope.FAILED_MESSAGE != null}">
                                <p class="error-message">${requestScope.FAILED_MESSAGE}</p>
                            </c:if>
                        </div>
                    </c:if>

                    <div class="form-group mt-5">
                        <div class="d-flex justify-content-center">
                            <button type="submit" class="btn btn-primary">Gửi yêu cầu</button>
                        </div>

                        <div class="link mt-3">
                            <div class="form-row">
                                <div class="col d-flex justify-content-end">
                                    <a href="<c:url value="/login"/> ">Đã có tài khoản? Đăng nhập</a>
                                </div>
                                <div class="col d-flex justify-content-start ml-5">
                                    <a href="<c:url value="/register"/> ">Đăng kí</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>

    </body>
    <jsp:include page="../common/common-js.jsp"/>
    <script type="module" src="<c:url value="/js/captcha.js"/>"></script>
</html>
