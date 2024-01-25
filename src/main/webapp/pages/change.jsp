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
            <div class="mb-2 d-flex justify-content-between align-content-center">
                <div class="title d-flex align-content-center">
                    <h3>Đổi Mật Khẩu</h3>
                </div>

                <div class="logo">
                    <img src="${pageContext.request.contextPath}/img/logo.png" alt="Trade System Logo" class="img-fluid logo">
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/change" method="post">
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

                <c:if test="${not empty resultMessage}">
                    <div class="form-group">
                        <p class="${resultMessage.startsWith('Thay đổi mật khẩu thành công!') ? 'success-message' : 'error-message'}">${resultMessage}</p>
                    </div>
                </c:if>
                <c:if test="${not empty resultMessage}">
                    <div class="alert alert-success" role="alert">
                            ${resultMessage}
                    </div>
                </c:if>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">
                            ${errorMessage}
                    </div>
                </c:if>

                <input type="submit" class="btn btn-primary" value="Đổi Mật Khẩu">

                <div class="link mt-3">
                    <div class="form-row">
                        <div class="col d-flex justify-content-start ml-5">
                            <a href="${pageContext.request.contextPath}/login">Quay lại Đăng nhập</a>
                        </div>
                        <div class="col d-flex justify-content-start ml-5">
                            <a href="${pageContext.request.contextPath}/register">Chưa có tài khoản? Đăng kí</a>
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
