<%--
  Created by IntelliJ IDEA.
  User: vuhai
  Date: 3/10/2024
  Time: 1:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/common/common-css.jsp"/>
    <link rel="stylesheet" href="<c:url value="/css/admin.sidenav.css"/> ">
    <title>Thêm cài đặt</title>
</head>
<body>

<div id="viewport">
    <!-- Include sidebar -->
    <jsp:include page="admin_sidebar.jsp"/>

    <!-- Content -->
    <div id="content">
        <!-- Include navbar -->
        <jsp:include page="admin_navbar.jsp"/>

        <div class="container-fluid p-3 main-content">
            <div class="container">
                <h4 class="text-center mt-3 mb-4">Tạo cài đặt</h4>

                <div class="row justify-content-center">
                    <div class="col-lg-6">
                        <div class="card shadow">

                            <div class="card-header d-flex align-items-center">
                                <div>
                                    <a href="<c:url value="/admin/setting"/>">
                                    <span class="material-symbols-outlined">
                                        arrow_back_ios
                                    </span>
                                    </a>

                                </div>
                                <div>
                                    <h5 class="mb-0">Thêm cài đặt</h5>
                                </div>
                            </div>
                            <div class="card-body">
                                <form action="<c:url value="/admin/setting/add"/>" method="post">
                                    <div class="form-group">
                                        <label for="keynameInput">Tên cài đặt</label>
                                        <input type="text" class="form-control" id="keynameInput"
                                               aria-describedby="nameHelp"
                                               placeholder="Nhập tên cài đặt" name="key_name">
                                    </div>
                                    <div class="form-group">
                                        <label for="valueInput">Giá trị</label>
                                        <input type="text" class="form-control" id="valueInput"
                                               aria-describedby="valueHelp"
                                               placeholder="Nhập giá trị" name="value">
                                    </div>
                                    <div class="text-center">
                                        <button type="submit" class="btn btn-primary">Tạo</button>
                                    </div>

                                    <c:if test="${requestScope.FAILED_MESSAGE}">
                                        <p style="color:red">thêm không thành công</p>
                                    </c:if>

                                </form>

                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<jsp:include page="/common/modal.jsp"/>
<jsp:include page="/common/toast.jsp"/>
<jsp:include page="/common/common-js.jsp"/>
</body>
</html>
