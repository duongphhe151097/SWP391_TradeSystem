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
    <title>AdminPaymentHistory</title>
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
                    <h1>Payment History</h1>
                </div>
            </div>

                <form method="get" action="<c:url value="/admin/payment/history"/>">
                <div class="form-row">
                    <div class="col-md-3 mb-3">
                        <label for="amount_from">Tìm theo giá từ</label>
                        <input type="number" class="form-control" value="${requestScope.FILTER_AmountFrom}" id="amount_from" name="f_amountFrom">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="amount_to">Tìm theo giá đến</label>
                        <input type="number" class="form-control" value="${requestScope.FILTER_AmountTo}" id="amount_to" name="f_amountTo">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="start_date">Từ ngày</label>
                        <input type="date" class="form-control" value="${requestScope.FILTER_STARTDATE}" id="start_date" name="f_start">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="end_date">Đến ngày</label>
                        <input type="date" class="form-control" value="${requestScope.FILTER_ENDDATE}" id="end_date" name="f_end">
                    </div>
                </div>
                <div class="ml-3 input-group mb-3 d-flex flex-column justify-content-end">
                    <button type="submit" class="btn btn-primary">Tìm</button>
                </div>
            </form>

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
                        <th>Thời gian sửa cuối</th>
                        <th>Nguời sửa</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${transaction}" var="transaction">
                        <tr>
                            <td>${transaction.id}</td>
                            <td>${transaction.type}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${transaction.command eq 1}">Nạp tiền</c:when>
                                    <c:otherwise>rút tiền</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${transaction.amount}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${transaction.status eq 1}">Được tạo</c:when>
                                    <c:when test="${transaction.status eq 2}">Thành công</c:when>
                                    <c:otherwise>Đã hủy</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${transaction.createAt}</td>
                            <td>${transaction.createBy}</td>
                            <td>${transaction.updateAt}</td>
                            <td>${transaction.updateBy}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <li class="page-item <c:if test="${paging.currentPage == paging.startPage}">disabled</c:if>">
                        <c:set value="${f:pagingUrlGenerate(paging.currentPage-1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                               var="previous"/>
                        <a class="page-link"
                           href="<c:url value="/admin/payment/history${previous}"/>">Previous</a>
                    </li>
                    <c:forEach begin="1" end="${paging.totalPage}" varStatus="loop">
                        <li class="page-item <c:if test="${loop.index == paging.currentPage}">active</c:if>">
                            <c:set value="${f:pagingUrlGenerate(loop.index, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                                   var="current"/>
                            <a class="page-link"
                               href="<c:url value="/admin/payment/history${current}"/>">${loop.index}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item <c:if test="${paging.currentPage == paging.endPage}">disabled</c:if>">
                        <c:set value="${f:pagingUrlGenerate(paging.currentPage+1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SAEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                               var="next"/>
                        <a class="page-link"
                           href="<c:url value="/admin/payment/history${next}"/>">Next</a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</div>

<jsp:include page="/common/modal.jsp" />
<jsp:include page="/common/toast.jsp" />
<jsp:include page="/common/common-js.jsp"/>
<script type="module" src="<c:url value="/js/admin.account.js"/>"></script>
</body>
</html>





