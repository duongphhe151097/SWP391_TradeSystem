<%--
  Created by IntelliJ IDEA.
  User: vuhai
  Date: 2/18/2024
  Time: 9:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="f" uri="tradesys.functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../../common/common-css.jsp"/>
    <link rel="stylesheet" href="<c:url value="/css/admin.sidenav.css"/> ">
    <link rel="stylesheet" href="<c:url value="/css/admin.manager.css"/> ">
    <title>Lịch sử giao dịch</title>
</head>
<body>
<div id="viewport">
    <jsp:include page="./admin_sidebar.jsp"/>
    <!-- Content -->
    <div id="content">
        <c:set value="${requestScope.VIEW_PAGING.paging}" var="paging"/>
        <c:set value="${requestScope.VIEW_PAGING.items}" var="transaction"/>
        <jsp:include page="admin_navbar.jsp"/>
        <div class="container-fluid p-3 main-content">
            <div class="row">
                <div class="col-md-12">
                    <h1>Lịch sử giao dịch</h1>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <form method="get" action="<c:url value='/admin/payment/history'/>">

                        <div class="form-group row">
                            <label for="amount_from" class="col-sm-4 col-form-label">Tìm theo giá từ</label>
                            <div class="col-sm-8">
                                <input type="number" class="form-control" value="${requestScope.FILTER_AmountFrom}"
                                       id="amount_from" name="f_amountFrom">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="amount_to" class="col-sm-4 col-form-label">Tìm theo giá đến</label>
                            <div class="col-sm-8">
                                <input type="number" class="form-control" value="${requestScope.FILTER_AmountTo}"
                                       id="amount_to" name="f_amountTo">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="id" class="col-sm-4 col-form-label">Tìm theo Mã giao dịch</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" value="${requestScope.FILTER_ID}" id="id"
                                       name="id">
                            </div>
                        </div>

                </div> <!-- Close col-md-6 here to split into next column -->

                <div class="col-md-6"> <!-- Open new column here -->
                    <div class="form-group row">
                        <label for="user" class="col-sm-4 col-form-label">Tìm theo người tạo</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" value="${requestScope.FILTER_USER}" id="user"
                                   name="user">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="start_date" class="col-sm-4 col-form-label">Từ ngày</label>
                        <div class="col-sm-8">
                            <input type="date" class="form-control" value="${requestScope.FILTER_STARTDATE}"
                                   id="start_date" name="f_start">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="end_date" class="col-sm-4 col-form-label">Đến ngày</label>
                        <div class="col-sm-8">
                            <input type="date" class="form-control" value="${requestScope.FILTER_ENDDATE}" id="end_date"
                                   name="f_end">
                        </div>
                    </div>
                    <c:if test="${requestScope.ERROR_VALIDATE_ID}">

                            <p style="color:red">Mã giao dịch không hợp lệ</p>



                    </c:if>
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <button type="submit" class="btn btn-primary">Tìm</button>
                        </div>
                    </div>
                    </form>
                </div>
            </div>
        </div>

    </div>

    <div class="col-md-12 mt-4">
        <table class="table">
            <thead class="thead-dark">
            <tr>
                <th>Mã giao dịch</th>
                <th>Thanh toán bằng</th>
                <th>Lệnh</th>
                <th>Số tiền</th>
                <th>Trạng thái</th>
                <th>Thời gian tạo</th>
                <th>Người tạo</th>
                <th>Hành động</th>

            </tr>
            </thead>
            <c:choose>
                <c:when test="${paging.totalItem <= 0}">
                    <tbody>

                    <tr>
                        <td colspan="8">
                            <div class="d-flex justify-content-center">
                                <h2>Không có kết quả!</h2>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </c:when>
                <c:otherwise>
                    <tbody>

                    <c:forEach items="${transaction}" var="transaction">
                        <tr>
                            <td>${transaction.id}</td>
                            <td>${transaction.type}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${transaction.command eq 1}">Nạp tiền</c:when>
                                    <c:otherwise>Rút tiền</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${transaction.amount}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${transaction.status eq 1}">Đang xử lý</c:when>
                                    <c:when test="${transaction.status eq 2}">Thành công</c:when>
                                    <c:otherwise>Không Thành công</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${transaction.createAt}</td>
                            <td>${transaction.createBy}</td>
                            <td><a href="">Chi tiết</a></td>

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
                        <c:set value="${f:pagingUrlGenerateTransactionHistory(paging.currentPage - 1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_AmountFrom, requestScope.FILTER_AmountTo, requestScope.FILTER_ID, requestScope.FILTER_USER, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                               var="previous"/>
                        <a class="page-link" href="<c:url value='/admin/payment/history${previous}'/>">
                            Về trang trước
                        </a>
                    </li>

                    <c:forEach begin="1" end="${paging.totalPage}" varStatus="loop">
                        <li class="page-item <c:if test="${loop.index == paging.currentPage}">active</c:if>">
                            <c:set value="${f:pagingUrlGenerateTransactionHistory(loop.index, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_AmountFrom, requestScope.FILTER_AmountTo, requestScope.FILTER_ID, requestScope.FILTER_USER, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                                   var="current"/>
                            <a class="page-link"
                               href="<c:url value='/admin/payment/history${current}'/>">${loop.index}</a>
                        </li>
                    </c:forEach>

                    <li class="page-item <c:if test="${paging.currentPage == paging.endPage}">disabled</c:if>">
                        <c:set value="${f:pagingUrlGenerateTransactionHistory(paging.currentPage + 1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_AmountFrom, requestScope.FILTER_AmountTo, requestScope.FILTER_ID, requestScope.FILTER_USER, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                               var="next"/>
                        <a class="page-link" href="<c:url value='/admin/payment/history${next}'/>">
                            Đến trang tiếp
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </c:if>



</div>


<jsp:include page="/common/modal.jsp"/>
<jsp:include page="/common/toast.jsp"/>
<jsp:include page="/common/common-js.jsp"/>
<script type="module" src="<c:url value="/js/admin.account.js"/>"></script>
</body>
</html>
