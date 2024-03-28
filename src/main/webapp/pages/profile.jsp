<%--
  Created by IntelliJ IDEA.
  User: DTBK
  Date: 1/22/2024
  Time: 10:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi">
    <head>
        <jsp:include page="../common/common-css.jsp"/>
        <link rel="stylesheet" href="<c:url value="/css/register.css"/>">
        <title>Thông tin người dùng</title>
    </head>
    <body>
        <jsp:include page="../common/header.jsp"/>
        <div class="vh-100 d-flex justify-content-center align-items-center">
            <div class="form-bloat shadow rounded-3" style="width: 500px">
                <div class="mb-2 d-flex justify-content-between align-content-center">
                    <div class="title d-flex align-content-center">
                        <h3>Thông tin người dùng</h3>
                    </div>
                </div>
                <form action="<c:url value="/profile"/>" method="post">
                    <div class="form-group">
                        <label for="id">Mã số tài khoản:</label>
                        <input type="text" class="form-control" id="id" name="id" value="${user.id}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="username">Tên tài khoản:</label>
                        <input type="text" class="form-control" id="username" placeholder="Nhập tên tài khoản"
                               name="username" value="${user.username}" autocomplete="off" readonly>
                    </div>

                    <div class="form-group">
                        <label for="fullname">Họ và tên:</label>
                        <input type="text" class="form-control" id="fullname" placeholder="Nhập họ và tên"
                               name="fullname" value="${user.fullName}" autocomplete="off">
                    </div>

                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="text" class="form-control" id="email" name="email" value="${user.email}"
                               readonly>
                    </div>
                    <div class="form-group">
                        <label for="phone_number">Số điện thoại:</label>
                        <input type="text" class="form-control" id="phone_number"
                               placeholder="Nhập số điện thoại" name="phone_number" value="${user.phoneNumber}"
                               autocomplete="off">
                    </div>

                    <div class="form-group">
                        <label for="balance">Số dư tài khoản:</label>
                        <input type="text" class="form-control" id="balance" name="balance"
                               value="${user.balance}" readonly>
                    </div>
                    <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="exampleModalLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="exampleModalLabel">Update Successful</h5>
                                </div>
                                <div class="modal-body">
                                    Thông tin của bạn đã được cập nhật
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group mt-5">
                        <div class="d-flex justify-content-center">
                            <button type="submit" class="btn btn-primary" onclick="updateProfile()">Cập nhật
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <script>
            function updateProfile() {
                $('#successModal').modal('show');
            }
        </script>
    </body>
    <jsp:include page="../common/common-js.jsp"/>
</html>
