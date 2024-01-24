<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 1/20/2024
  Time: 2:23 AM
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
        <title>Quản lý tài khoản</title>
    </head>
    <body>
        <div id="viewport">
            <!-- Sidebar -->
            <div id="sidebar">
                <header>
                    <a href="<c:url value="/admin"/>">TradeSystemAdmin</a>
                </header>
                <ul class="nav d-flex">
                    <li class=" w-100">
                        <a href="<c:url value="/admin/account"/>">
                        <span class="d-flex align-items-center">
                            <span class="material-symbols-outlined">
                                person
                            </span>
                            <span class="item-name">
                                Quản lý người dùng
                            </span>
                        </span>
                        </a>
                    </li>
                </ul>
            </div>
            <!-- Content -->
            <div id="content">
                <c:set value="${requestScope.VIEW_PAGING.paging}" var="paging"/>
                <c:set value="${requestScope.VIEW_PAGING.items}" var="users"/>

                <nav class="navbar d-flex align-items-center justify-content-end">
                    <div class="nav-item dropdown d-none d-lg-block user-dropdown">
                        <a class="nav-link" id="user-dropdown" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                            <img class="img-xs rounded-circle" src="<c:url value="/img/default_male.jpg"/>"
                                 alt="Profile image"/>
                            <span class="user-name">${sessionScope.SESSION_USERFULLNAME}</span>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right navbar-dropdown" aria-labelledby="user-dropdown">
                            <div class="dropdown-header text-center">
                                <img class="img-md rounded-circle" src="<c:url value="/img/default_male.jpg"/>"
                                     alt="Profile image"/>
                                <p class="mb-1 mt-3 font-weight-semibold">${sessionScope.SESSION_USERFULLNAME}</p>
                                <p class="fw-light text-muted mb-0">${sessionScope.SESSION_USEREMAIL}</p>
                            </div>
                            <a href="<c:url value="/profile"/>" class="dropdown-item">
                            <span class="d-flex align-items-center">
                                <span class="material-symbols-outlined">
                                    person
                                </span>
                                <span>
                                    Thông tin tài khoản
                                </span>
                            </span>
                            </a>
                            <a href="<c:url value="/logout"/>" class="dropdown-item">
                            <span class="d-flex align-items-center">
                                <span class="material-symbols-outlined">
                                    logout
                                </span>
                                <span>
                                    Đăng xuất
                                </span>
                            </span>
                            </a>
                        </div>
                    </div>
                </nav>

                <div class="container-fluid p-3">
                    <div class="row">
                        <div class="col-md-12">
                            <h1>Quản lý tài khoản</h1>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-12">
                            <form method="get" action="<c:url value="/admin/account"/> ">
                                <div class="d-flex justify-content-end">
                                    <input type="hidden" value="${paging.currentPage}" name="current">
                                    <input type="hidden" value="${paging.pageSize}" name="size">
                                    <input type="hidden" value="${paging.pageRangeOutput}" name="range">
                                    <div class="input-group mb-3 d-flex flex-column">
                                        <label for="start_date">Từ ngày</label>
                                        <input type="date" class="form-control w-100"
                                               value="${requestScope.FILTER_STARTDATE}" id="start_date" name="f_start">
                                    </div>
                                    <div class="input-group mb-3 d-flex flex-column">
                                        <label for="end_date">Đến ngày</label>
                                        <input type="date" class="form-control w-100"
                                               value="${requestScope.FILTER_ENDDATE}" id="end_date" name="f_end">
                                    </div>
                                    <div class="input-group mb-3 d-flex flex-column">
                                        <label for="status">Trạng thái</label>
                                        <select class="custom-select w-100" id="status" name="f_status">
                                            <c:set value="${requestScope.FILTER_STATUS}" var="filter"/>
                                            <c:set value="ALL" var="ALL"/>
                                            <c:set value="0" var="PENDING"/>
                                            <c:set value="1" var="ACTIVE"/>
                                            <c:set value="2" var="LOCKED"/>
                                            <c:set value="3" var="BANNED"/>

                                            <option value="ALL" <c:if test="${filter.equals(ALL)}">selected</c:if>>
                                                Tất cả
                                            </option>
                                            <option value="0" <c:if test="${filter.equals(PENDING)}">selected</c:if>>
                                                Chưa kích hoạt
                                            </option>
                                            <option value="1" <c:if test="${filter.equals(ACTIVE)}">selected</c:if>>
                                                Đang hoạt động
                                            </option>
                                            <option value="2" <c:if test="${filter.equals(LOCKED)}">selected</c:if>>
                                                Đang bị khóa
                                            </option>
                                            <option value="3" <c:if test="${filter.equals(BANNED)}">selected</c:if>>
                                                Đã bị chặn
                                            </option>
                                        </select>
                                    </div>
                                    <div class="input-group mb-3 d-flex flex-column">
                                        <label for="search_input">Tìm kiếm</label>
                                        <input type="text" class="form-control w-100"
                                               placeholder="Nhập email hoặc username"
                                               value="${requestScope.FILTER_SEARCH}" name="search" id="search_input">
                                    </div>
                                    <div class="ml-3 input-group mb-3 d-flex flex-column justify-content-end">
                                        <button type="submit" class="btn btn-primary">Tìm</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="col-md-12">
                            <table class="table">
                                <thead class="thead-dark">
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Họ và tên</th>
                                        <th scope="col">Tên đăng nhập</th>
                                        <th scope="col">Email</th>
                                        <th scope="col">Trạng thái</th>
                                        <th scope="col">Ngày tạo</th>
                                        <th scope="col">Hành động</th>
                                    </tr>
                                </thead>
                                <c:choose>
                                    <c:when test="${paging.totalItem <= 0}">
                                        <tbody>
                                            <tr>
                                                <td colspan="7">
                                                    <div class="d-flex justify-content-center">
                                                        <h2>Không có kết quả!</h2>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </c:when>
                                    <c:otherwise>
                                        <tbody>
                                            <c:forEach items="${users}" var="user" begin="0" end="${paging.totalItem}"
                                                       varStatus="loop">
                                                <tr class="table-active">
                                                    <th scope="row">${loop.index + 1}</th>
                                                    <td>${user.fullName}</td>
                                                    <td>${user.username}</td>
                                                    <td>${user.email}</td>
                                                    <td>
                                                        <span class="d-flex align-items-center">
                                                            <c:choose>
                                                                <c:when test="${user.status == 0}">
                                                                    <span class="material-symbols-outlined" style="color: #6c757d">fiber_manual_record</span>
                                                                    <span>Chưa kích hoạt</span>
                                                                </c:when>

                                                                <c:when test="${user.status == 1}">
                                                                    <span class="material-symbols-outlined" style="color: #39ac31">fiber_manual_record</span>
                                                                    <span>Đang hoạt động</span>
                                                                </c:when>

                                                                <c:when test="${user.status == 2}">
                                                                    <span class="material-symbols-outlined" style="color: #ffc107">fiber_manual_record</span>
                                                                    <span>Đang khóa</span>
                                                                </c:when>

                                                                <c:when test="${user.status == 3}">
                                                                    <span class="material-symbols-outlined" style="color: #ffc107">fiber_manual_record</span>
                                                                    <span>Bị chặn</span>
                                                                </c:when>

                                                                <c:otherwise>
                                                                    <span class="material-symbols-outlined" style="color: #007bff">fiber_manual_record</span>
                                                                    <span>Không rõ</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </span>
                                                    </td>
                                                    <td> ${f:formatLocalDateTime(user.createAt, 'dd/MM/yyyy hh:mm:ss')}</td>
                                                    <td>
                                                        <div class="d-flex">
                                                            <a href="">
                                                                <span class="d-flex align-items-center mr-2">
                                                                    <span class="material-symbols-outlined">info</span>
                                                                    <span>Chi tiết</span>
                                                                </span>
                                                            </a>

                                                            <a href="">
                                                                <span class="d-flex align-items-center">
                                                                    <span class="material-symbols-outlined">do_not_disturb_on</span>
                                                                    <span>Chặn tài khoản</span>
                                                                </span>
                                                            </a>
                                                        </div>
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
                                    <ul class="pagination">
                                        <li class="page-item <c:if test="${paging.currentPage == paging.startPage}">disabled</c:if>">
                                            <c:set value="${f:pagingUrlGenerate(paging.currentPage-1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                                                   var="previous"/>
                                            <a class="page-link"
                                               href="<c:url value="/admin/account${previous}"/>">
                                                Về trang trước
                                            </a>
                                        </li>

                                        <c:forEach begin="1" end="${paging.totalPage}" varStatus="loop">
                                            <li class="page-item <c:if test="${loop.index == paging.currentPage}">active</c:if>">
                                                <c:set value="${f:pagingUrlGenerate(loop.index, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                                                       var="current"/>
                                                <a class="page-link"
                                                   href="<c:url value="/admin/account${current}"/>">${loop.index}</a>
                                            </li>
                                        </c:forEach>

                                        <li class="page-item <c:if test="${paging.currentPage == paging.endPage}">disabled</c:if>">
                                            <c:set value="${f:pagingUrlGenerate(paging.currentPage+1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                                                   var="next"/>
                                            <a class="page-link"
                                               href="<c:url value="/admin/account${next}"/>">
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
        </div>
    </body>
    <jsp:include page="../../common/common-js.jsp"/>
</html>
