<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi">
<head>
    <jsp:include page="../common/common-css.jsp"/>
    <link rel="stylesheet" href="<c:out value="${pageContext.request.contextPath}/css/reset.css"/>">
    <title>Đặt lại mật khẩu</title>
</head>
<body>
<div class="vh-100 d-flex justify-content-center align-items-center">
    <div class="col-md-5 p-5 shadow rounded-3">
        <div class="reset-password">
            <div class="mb-2 d-flex justify-content-between align-items-center">
                <div class="title">
                    <h3>Đặt lại mật khẩu</h3>
                </div>

                <div class="logo">
                    <img src="${pageContext.request.contextPath}/img/logo.png" alt="Trade System Logo" class="img-fluid logo">
                </div>
            </div>

            <form action="<c:url value='/resetpassword'/>" method="post">
                <input type="hidden" name="resetToken" value="${requestScope.resetToken}">
                <div class="form-group">
                    <label for="newpasswordInput">Mật khẩu mới</label>
                    <input type="password" class="form-control" id="newpasswordInput" placeholder="Nhập mật khẩu mới" name="newpassword">
                    <c:if test="${not empty requestScope.NEWPASSWORD_ERROR}">
                        <small class="form-text text-muted error-message">${requestScope.NEWPASSWORD_ERROR}</small>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="renewpasswordInput">Nhập lại mật khẩu mới</label>
                    <input type="password" class="form-control" id="renewpasswordInput" placeholder="Nhập lại mật khẩu mới" name="renewpassword">
                    <c:if test="${not empty requestScope.RENEWPASSWORD_ERROR}">
                        <small class="form-text text-muted error-message">${requestScope.RENEWPASSWORD_ERROR}</small>
                    </c:if>
                </div>

                <c:if test="${not empty requestScope.SUCCESS_MESSAGE or not empty requestScope.FAILED_MESSAGE}">
                    <div class="form-group">
                        <c:if test="${not empty requestScope.SUCCESS_MESSAGE}">
                            <p class="success-message">${requestScope.SUCCESS_MESSAGE}</p>
                        </c:if>
                        <c:if test="${not empty requestScope.FAILED_MESSAGE}">
                            <p class="error-message">${requestScope.FAILED_MESSAGE}</p>
                        </c:if>
                    </div>
                </c:if>

                <div class="form-group mt-5">
                    <div class="d-flex justify-content-center">
                        <input type="submit" class="btn btn-primary" value="Đặt lại mật khẩu">
                    </div>

                    <div class="link mt-3">
                        <div class="form-row">
                            <div class="col d-flex justify-content-end">
                                <a href="<c:url value='/'/>">Đăng xuất</a>
                            </div>
                            <div class="col d-flex justify-content-start ml-5">
                                <a href="<c:url value='/home'/>">Trang chủ</a>
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
