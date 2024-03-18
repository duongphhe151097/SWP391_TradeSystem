<%--
  Created by IntelliJ IDEA.
  User: vuhai
  Date: 3/8/2024
  Time: 1:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="f" uri="tradesys.functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../../common/common-css.jsp"/>
    <link rel="stylesheet" href="<c:url value="/css/admin.sidenav.css"/> ">
    <link rel="stylesheet" href="<c:url value="/css/admin.manager.css"/> ">
    <title>Cài đặt</title>
</head>
<body>

<div id="viewport">
    <jsp:include page="./admin_sidebar.jsp"/>
    <!-- Content -->
    <div id="content">
        <c:set value="${requestScope.VIEW_PAGING.paging}" var="paging"/>
        <c:set value="${requestScope.VIEW_PAGING.items}" var="settingData"/>
        <jsp:include page="admin_navbar.jsp"/>
        <div class="container-fluid p-3 main-content">
            <div>
                <div class="col-md-12">
                    <h1>Quản lý cài đặt</h1>
                </div>
            </div>

            <div class="col-md-12 text-right">
                <a href="<c:url value="/admin/setting/add"/>" class="btn btn-primary">Tạo cài đặt</a>
            </div>

                    <div class="col-md-12 mt-4">
                        <table class="table">
                            <thead class="thead-dark">
                            <tr>
                                <th>ID</th>
                                <th>Tên</th>
                                <th>Giá trị</th>
                                <th>Người tạo</th>
                                <th>Ngày tạo</th>
                                <th>Ngày Sửa</th>
                                <th>Hành động</th>

                            </tr>
                            </thead>
                            <c:choose>
                                <c:when test="${empty settingData}">
                                    <tbody>
                                    <tr>
                                        <td colspan="4">
                                            <div class="d-flex justify-content-center">
                                                <h2>Không có kết quả!</h2>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </c:when>
                                <c:otherwise>
                                    <tbody>
                                    <c:forEach items="${settingData}" var="setting">
                                        <tr>
                                            <td>${setting.id}</td>
                                            <td>${setting.keyName}</td>
                                            <td>${setting.value}</td>
                                            <td>${setting.createBy}</td>
                                            <td>${setting.createAt}</td>
                                            <td>${setting.updateAt}</td>
                                            <td>
                                                <a href="<c:url value="/admin/setting/delete?id=${setting.id}&type=true"/> " class="delete-click">
                                                                        <span class="d-flex align-items-center">
                                                                            <span class="material-symbols-outlined">delete</span>
                                                                            <span>Xóa cài đặt</span>
                                                                        </span>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </c:otherwise>
                            </c:choose>
                        </table>
                    </div>

                    <c:if test="${paging.totalPage > 0}">
                        <div class="col-md-12">
                            <nav aria-label="Page navigation example">
                                <ul class="pagination justify-content-center">
                                    <li class="page-item <c:if test="${paging.currentPage == paging.startPage}">disabled</c:if>">
                                        <a class="page-link" href="<c:url value='/admin/setting${previous}'/>">
                                            Về trang trước
                                        </a>
                                    </li>

                                    <c:forEach begin="1" end="${paging.totalPage}" varStatus="loop">
                                        <li class="page-item <c:if test="${loop.index == paging.currentPage}">active</c:if>">
                                            <a class="page-link" href="<c:url value='/admin/setting${current}'/>">${loop.index}</a>
                                        </li>
                                    </c:forEach>

                                    <li class="page-item <c:if test="${paging.currentPage == paging.endPage}">disabled</c:if>">
                                        <a class="page-link" href="<c:url value='/admin/setting${next}'/>">
                                            Đến trang tiếp
                                        </a>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </c:if>

                </div>
            </div>

</div>


<jsp:include page="../../common/common-js.jsp"/>
</body>
</html>