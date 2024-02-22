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
    <link rel="stylesheet" href="<c:url value="/css/admin.externalTran.css"/> ">
    <title>External Transactions</title>
</head>
<body>

<h2>External Transactions</h2>
<c:set value="${requestScope.VIEW_PAGING.paging}" var="paging"/>
<c:set value="${requestScope.VIEW_PAGING.items}" var="externalTran"/>

<form method="get" action="<c:url value="/externalTransaction"/> ">
    <div class="d-flex justify-content-end">
        <input type="hidden" value="${paging.currentPage}" name="current">
        <input type="hidden" value="${paging.pageSize}" name="size">
        <input type="hidden" value="${paging.pageRangeOutput}" name="range">

        <div class="input-group mb-3 d-flex flex-column">
            <label for="start_date">Search theo giá Từ </label>
            <input type="number" class="form-control w-100"
                   value="${requestScope.FILTER_AmountFrom}" id="amount_from" name="f_amountFrom">
        </div>
        <div class="input-group mb-3 d-flex flex-column">
        <label for="start_date">Search theo giá đến </label>
        <input type="number" class="form-control w-100"
               value="${requestScope.FILTER_AmountTo}" id="amount_to" name="f_amountTo">
    </div>

        <div class="input-group mb-3 d-flex flex-column">
            <label for="start_date">Search theo ngày tạo Từ ngày</label>
            <input type="date" class="form-control w-100"
                   value="${requestScope.FILTER_STARTDATE}" id="start_date" name="f_start">
        </div>
        <div class="input-group mb-3 d-flex flex-column">
            <label for="end_date">Search theo ngày tạo Đến ngày</label>
            <input type="date" class="form-control w-100"
                   value="${requestScope.FILTER_ENDDATE}" id="end_date" name="f_end">
        </div>


        <div class="ml-3 input-group mb-3 d-flex flex-column justify-content-end">
            <button type="submit" class="btn btn-primary">Tìm</button>
        </div>
    </div>
</form>

<table id="externalTransactions">
    <thead>
    <tr>
        <th>Mã giao dịch</th>
        <th>Thanh toán bằng</th>
        <th>Ghi chú</th>
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
            <td>${transaction.status}</td>
            <td>${transaction.createAt}</td>
            <td>${transaction.createBy}</td>
            <td>${transaction.updateAt}</td>
            <td>${transaction.updateBy}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">
    <c:if test="${paging.totalPage > 0}">
        <nav aria-label="Page navigation example">
            <ul>
                <li class="page-item <c:if test="${paging.currentPage == paging.startPage}">disabled</c:if>">
                    <c:set value="${f:pagingUrlGenerate(paging.currentPage-1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                           var="previous"/>
                    <a class="page-link"
                       href="<c:url value="/externalTransaction${previous}"/>">
                        Về trang trước
                    </a>
                </li>

                <c:forEach begin="1" end="${paging.totalPage}" varStatus="loop">
                    <li class="page-item <c:if test="${loop.index == paging.currentPage}">active</c:if>">
                        <c:set value="${f:pagingUrlGenerate(loop.index, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                               var="current"/>
                        <a class="page-link"
                           href="<c:url value="/externalTransaction${current}"/>">${loop.index}</a>
                    </li>
                </c:forEach>

                <li class="page-item <c:if test="${paging.currentPage == paging.endPage}">disabled</c:if>">
                    <c:set value="${f:pagingUrlGenerate(paging.currentPage+1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"
                           var="next"/>
                    <a class="page-link"
                       href="<c:url value="/externalTransaction${next}"/>">
                        Đến trang tiếp
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>
</div>

</body>
</html>



