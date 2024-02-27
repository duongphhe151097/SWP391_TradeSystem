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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>External Transactions</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<body>

<div class="container mt-4">
    <h2>External Transactions</h2>
    <c:set value="${requestScope.VIEW_PAGING.paging}" var="paging"/>
    <c:set value="${requestScope.VIEW_PAGING.items}" var="externalTran"/>

    <form method="get" action="<c:url value="/admin/payment/history"/>">
        <div class="form-row">
            <div class="form-group col-md-3">
                <label for="amount_from">Search by Amount From</label>
                <input type="number" class="form-control" value="${requestScope.FILTER_AmountFrom}" id="amount_from" name="f_amountFrom">
            </div>
            <div class="form-group col-md-3">
                <label for="amount_to">Search by Amount To</label>
                <input type="number" class="form-control" value="${requestScope.FILTER_AmountTo}" id="amount_to" name="f_amountTo">
            </div>
            <div class="form-group col-md-3">
                <label for="start_date">Search by Start Date</label>
                <input type="date" class="form-control" value="${requestScope.FILTER_STARTDATE}" id="start_date" name="f_start">
            </div>
            <div class="form-group col-md-3">
                <label for="end_date">Search by End Date</label>
                <input type="date" class="form-control" value="${requestScope.FILTER_ENDDATE}" id="end_date" name="f_end">
            </div>
        </div>
        <button type="submit" class="btn btn-primary">Search</button>
    </form>

    <table class="table mt-4">
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
        <c:forEach items="${externalTran}" var="transaction">
            <tr>
                <td>${transaction.id}</td>
                <td>${transaction.type}</td>
                <td>${transaction.command}</td>
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



<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<!-- Bootstrap JS -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>


