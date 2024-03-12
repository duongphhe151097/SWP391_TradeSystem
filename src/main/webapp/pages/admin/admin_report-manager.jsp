<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 3/10/2024
  Time: 3:57 AM
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
        <title>Quản lý báo cáo</title>
    </head>
    <body>
        <div id="viewport">
            <jsp:include page="admin_sidebar.jsp"/>
            <!-- Content -->
            <div id="content">
                <jsp:include page="admin_navbar.jsp"/>
                <div class="container-fluid p-3 main-content">
                    <div class="row">
                        <div class="col-md-12">
                            <h1>Quản lý báo cáo</h1>
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
                            <c:set value="${requestScope.VIEW_PAGING.paging}" var="paging"/>
                            <c:set value="${requestScope.VIEW_PAGING.items}" var="reports"/>

                            <table class="table">
                                <thead class="thead-dark">
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Tiêu đề</th>
                                        <th scope="col">Ngày tạo</th>
<%--                                        <th scope="col">Trạng thái</th>--%>
                                        <th scope="col">Người tạo</th>
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
                                            <c:forEach items="${reports}" var="report" begin="0" end="${paging.totalItem}"
                                                       varStatus="loop">
                                                <tr class="table-active">
                                                    <th scope="row">${loop.index + 1}</th>
                                                    <td><c:out value="${report.title}"/></td>
                                                    <td><c:out value="${report.createAt}"/></td>
<%--                                                    <td><c:out value="${report.createAt}"/></td>--%>
                                                    <td><c:out value="${report.createBy}"/></td>
                                                    <td><a href="<c:url value="/admin/report/detail?id=${report.id}" />">Xem chi tiết</a></td>
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

        <jsp:include page="/common/modal.jsp" />
        <jsp:include page="/common/toast.jsp" />
    </body>
    <jsp:include page="/common/common-js.jsp"/>
    <script type="module" src="<c:url value="/js/admin.payhisdetail.js"/>"></script>
</html>
