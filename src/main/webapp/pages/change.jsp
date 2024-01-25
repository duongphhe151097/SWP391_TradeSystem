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
    <link rel="stylesheet" href="<c:out value="${pageContext.request.contextPath}/css/changepassword.css"/>">
    <title>Đổi Mật Khẩu</title>
</head>
<body>
<div class="vh-100 d-flex justify-content-center align-items-center">
    <div class="col-md-5 p-5 shadow rounded-3">
        <div class="change">
            <div class="mb-2 d-flex justify-content-between align-items-center">
                <div class="title">
                    <h3>Đổi Mật Khẩu</h3>
                </div>

                <div class="logo">
                    <img src="<c:url value="/img/logo.png"/>" alt="Trade System Logo" class="img-fluid logo-small">
                </div>
            </div>

            <form action="<c:url value="/change"/>" method="post">
                <div class="form-group">
                    <label for="oldPasswordInput">Mật khẩu cũ</label>
                    <input type="password" class="form-control" id="oldPasswordInput" placeholder="Nhập mật khẩu cũ" name="oldPassword" required>
                </div>

                <div class="form-group">
                    <label for="newPasswordInput">Mật khẩu mới</label>
                    <input type="password" class="form-control" id="newPasswordInput" placeholder="Nhập mật khẩu mới (ít nhất 8 kí tự)" name="newPassword" required>
                </div>

                <div class="form-group">
                    <label for="reNewPasswordInput">Nhập lại mật khẩu mới</label>
                    <input type="password" class="form-control" id="reNewPasswordInput" placeholder="Nhập lại mật khẩu mới" name="reNewPassword" required>
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

                <c:if test="${not empty resultMessage}">
                    <div class="form-group">
                        <p class="${resultMessage.startsWith('Thay đổi mật khẩu thành công!') ? 'success-message' : 'error-message'}">${resultMessage}</p>
                    </div>
                </c:if>

                <div class="form-group mt-5">
                    <div class="d-flex justify-content-center">
                        <input type="submit" class="btn btn-primary" value="Đổi Mật Khẩu">
                    </div>


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
<script src="<c:url value="/js/captcha.js"/>"></script>
</html>
