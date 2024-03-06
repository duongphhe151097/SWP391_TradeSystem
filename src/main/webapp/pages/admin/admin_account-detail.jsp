<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 1/20/2024
  Time: 2:24 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <jsp:include page="../../common/common-css.jsp"/>
        <link rel="stylesheet" href="<c:url value="/css/admin.sidenav.css"/> ">
        <title>Chi tiết tài khoản</title>
    </head>
    <body>
        <div id="viewport">
            <jsp:include page="admin_sidebar.jsp"/>
            <!-- Content -->
            <div id="content">
                <jsp:include page="admin_navbar.jsp"/>

                <div class="container-fluid p-3 main-content">
                    <c:choose>
                        <c:when test="${requestScope.ERROR_NOTFOUND == true}">
                            <div class="row">
                                <div class="col-md-12">
                                    <h1>Chi tiết tài khoản</h1>
                                    <h2>Không tìm thấy tài khoản</h2>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <div class="col-md-12">
                                    <h1>Chi tiết tài khoản</h1>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="card mb-5">
                                        <div class="card-header">
                                            <div class="d-flex justify-content-between align-items-end">
                                                <div>
                                                    <h5>Thông tin tài khoản</h5>
                                                </div>

                                                <div>
                                                    <button type="button" class="btn btn-warning" id="edit">Sửa
                                                    </button>
                                                    <a href="" role="button" class="ml-2 btn btn-warning hidden-item" id="edit-save">Cập nhật</a>
                                                </div>

                                            </div>

                                        </div>

                                        <div class="card-body">
                                            <form action="<c:url value="/admin/account/info"/>" method="post" class="d-flex" id="user-info">
                                                <div class="col-md-6">
                                                    <div class="row">
                                                        <table class="table table-borderless">
                                                            <thead>
                                                                <tr>
                                                                    <th scope="col"></th>
                                                                    <th scope="col"></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>
                                                                    <th scope="row">Id:</th>
                                                                    <td>
                                                                        <input type="text" class="form-control"
                                                                               placeholder="Id"
                                                                               value="${requestScope.VAR_USERID}"
                                                                               aria-label="Username"
                                                                               disabled
                                                                        >
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <th scope="row">Tên đăng nhập:</th>
                                                                    <td>
                                                                        <input type="text" class="form-control"
                                                                               placeholder="Username"
                                                                               value="${requestScope.VAR_USERNAME}"
                                                                               aria-label="Username"
                                                                               disabled
                                                                        >
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <th scope="row">Email:</th>
                                                                    <td>
                                                                        <input type="email" class="form-control"
                                                                               value="${requestScope.VAR_EMAIL}"
                                                                               placeholder="Email" aria-label="Email"
                                                                               disabled
                                                                        >
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <th scope="row">Họ và tên:</th>
                                                                    <td>
                                                                        <input type="text" class="form-control can-edit"
                                                                               value="${requestScope.VAR_FULLNAME}"
                                                                               placeholder="Họ và tên"
                                                                               aria-label="Username"
                                                                               disabled
                                                                        >
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <th scope="row">Số điện thoại:</th>
                                                                    <td>
                                                                        <input type="text" class="form-control can-edit"
                                                                               value="${requestScope.VAR_PHONE}"
                                                                               placeholder="Username"
                                                                               aria-label="Username"
                                                                               disabled
                                                                        >
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="row">
                                                        <table class="table table-borderless">
                                                            <thead>
                                                                <tr>
                                                                    <th scope="col"></th>
                                                                    <th scope="col"></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>
                                                                    <th scope="row">Địa chỉ:</th>
                                                                    <td>
                                                                        <input type="text" class="form-control can-edit"
                                                                               value="${requestScope.VAR_ADDRESS}"
                                                                               placeholder="Địa chỉ"
                                                                               aria-label="Username"
                                                                               disabled
                                                                        >
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <th scope="row">Trạng thái:</th>
                                                                    <td>
                                                                        <select class="custom-select can-edit"
                                                                                aria-label="Example select with button addon"
                                                                                disabled>
                                                                            <option value="1"
                                                                                    <c:if test="${requestScope.VAR_STATUS == 1}">selected</c:if>>
                                                                                Hoạt động
                                                                            </option>
                                                                            <option value="2"
                                                                                    <c:if test="${requestScope.VAR_STATUS == 2}">selected</c:if>>
                                                                                Khóa tài khoản
                                                                            </option>
                                                                            <option value="3"
                                                                                    <c:if test="${requestScope.VAR_STATUS == 3}">selected</c:if>>
                                                                                Cấm tài khoản
                                                                            </option>
                                                                        </select>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <th scope="row">Số dư:</th>
                                                                    <td>
                                                                        <input type="number" class="form-control"
                                                                               value="${requestScope.VAR_BALANCE}"
                                                                               placeholder="Số dư"
                                                                               aria-label="Username"
                                                                               disabled
                                                                        >
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <th scope="row">Ngày tạo:</th>
                                                                    <td>
                                                                        <input type="text" class="form-control"
                                                                               value="${requestScope.VAR_CREATEDATE}"
                                                                               placeholder="Ngày tạo"
                                                                               aria-label="Username"
                                                                               disabled
                                                                        >
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <th scope="row">Uy tín:</th>
                                                                    <td>
                                                                        <input type="text" class="form-control can-edit"
                                                                               value="${requestScope.VAR_RATE}"
                                                                               placeholder="Uy tín"
                                                                               aria-label="Username"
                                                                               disabled
                                                                        >
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="card">
                                        <div class="card-header">
                                            <h5>Vai trò</h5>
                                        </div>
                                        <div class="card-body">
                                            <form action="<c:url value="/admin/account/role"/>" method="post" id="add-role">
                                                <div class="input-group">
                                                    <input type="hidden" name="uid" value="${requestScope.VAR_USERID}">
                                                    <select class="custom-select" name="rid" id="inputGroupSelect03"
                                                            aria-label="Example select with button addon">
                                                        <option selected>Chọn vai trò...</option>
                                                        <c:forEach var="role" items="${requestScope.ROLES}">
                                                            <option value="${role.roleId}">${role.roleName}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <div class="input-group-append">
                                                        <button class="btn btn-success" type="button" id="confirm_add-role">Thêm</button>
                                                    </div>
                                                </div>
                                            </form>

                                            <div class="mt-2">
                                                <ul class="list-group" id="role-group">
                                                    <c:forEach var="uRole" items="${requestScope.USER_ROLES}">
                                                        <li class="list-group-item" data-rid="${uRole.roleId}">
                                                            <div class="d-flex justify-content-between align-items-center">
                                                                <span class="mr-2">${uRole.roleName}</span>
                                                                <a class="btn btn-outline-danger remove-role" href="<c:url value="/admin/account/role?uid=${requestScope.VAR_USERID}&rid=${uRole.roleId}&type=remove"/> " role="button">X</a>
                                                            </div>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="card">
                                        <div class="card-header">
                                            <h5>Bảng điều khiển</h5>
                                        </div>
                                        <div class="card-body">
                                            <div class="mt-2">
                                                <ul class="list-group">
                                                    <li class="list-group-item">
                                                        <div class="d-flex justify-content-between align-items-center">
                                                            <span class="mr-2">Session hiện tại:</span>
                                                            <a id="cp-bsession" class="btn btn-outline-danger" href="<c:url value="/admin/account/session?uid=${requestScope.VAR_USERID}&type=remove" />" role="button">Hủy
                                                                session</a>
                                                        </div>
                                                    </li>
<%--                                                    <li class="list-group-item">--%>
<%--                                                        <div class="d-flex justify-content-between align-items-center">--%>
<%--                                                            <span class="mr-2">Mật khẩu:</span>--%>
<%--                                                            <button type="button" class="btn btn-outline-danger">Đặt lại--%>
<%--                                                                mật--%>
<%--                                                                khẩu--%>
<%--                                                            </button>--%>
<%--                                                        </div>--%>
<%--                                                    </li>--%>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <jsp:include page="/common/modal.jsp"/>
        <jsp:include page="/common/toast.jsp"/>
    </body>
    <jsp:include page="/common/common-js.jsp"/>
    <script type="module" src="<c:url value="/js/admin.accdetail.js"/>"></script>
</html>
