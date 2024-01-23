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

            <c:choose>
                <c:when test="${empty resetToken}">
                    <form action="change" method="post">
                        <div class="form-group">
                            <label for="newPasswordInput">Mật khẩu mới</label>
                            <input type="password" class="form-control" id="newPasswordInput" aria-describedby="newPasswordHelp"
                                   placeholder="Nhập mật khẩu mới (ít nhất 8 kí tự)" name="newpassword">
                            <c:if test="${requestScope.NEWPASSWORD_ERROR != null}">
                                <small id="newPasswordHelp" class="form-text text-muted error-message">${requestScope.NEWPASSWORD_ERROR}</small>
                            </c:if>
                        </div>

                        <div class="form-group">
                            <label for="reNewPasswordInput">Nhập lại mật khẩu mới</label>
                            <input type="password" class="form-control" id="reNewPasswordInput" aria-describedby="reNewPasswordHelp"
                                   placeholder="Nhập lại mật khẩu mới" name="renewpassword">
                            <c:if test="${requestScope.RENEWPASSWORD_ERROR != null}">
                                <small id="reNewPasswordHelp" class="form-text text-muted error-message">${requestScope.RENEWPASSWORD_ERROR}</small>
                            </c:if>
                        </div>

                        <c:if test="${requestScope.SUCCESS_MESSAGE != null || requestScope.FAILED_MESSAGE != null}">
                            <div class="form-group">
                                <c:if test="${requestScope.FAILED_MESSAGE != null}">
                                    <p class="error-message">${requestScope.FAILED_MESSAGE}</p>
                                </c:if>
                            </div>
                        </c:if>

                        <input type="hidden" name="resetToken" value="<c:out value="${param.t}"/>">

                        <div class="form-group mt-5">
                            <div class="d-flex justify-content-center">
                                <button type="submit" class="btn btn-primary">Đổi Mật Khẩu</button>
                            </div>

                            <div class="link mt-3">
                                <div class="form-row">
                                    <div class="col d-flex justify-content-start ml-5">
                                        <a href="/tradesys_war_exploded/login">Quay lại Đăng nhập</a>
                                    </div>
                                    <div class="col d-flex justify-content-start ml-5">
                                        <a href="/tradesys_war_exploded/register">Chưa có tài khoản? Đăng kí</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </c:when>

                <c:when test="${not empty resetToken}">
                    <form action="change" method="post">
                        <div class="form-group">
                            <label for="newpassword">Mật khẩu mới</label>
                            <input type="password" class="form-control" id="newpassword" placeholder="Nhập mật khẩu mới" name="newpassword">
                            <!-- Add other input fields as needed -->
                        </div>

                        <!-- Add other form elements as needed -->

                        <button type="submit" class="btn btn-primary">Đổi Mật Khẩu</button>
                    </form>
                </c:when>
            </c:choose>
        </div>
    </div>
</div>
</body>
<jsp:include page="../common/common-js.jsp"/>
</html>
