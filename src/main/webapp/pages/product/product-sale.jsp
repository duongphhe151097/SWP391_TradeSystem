<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="f" uri="tradesys.functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <jsp:include page="/common/common-css.jsp"/>
        <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
        <title>Đơn Bán</title>
    </head>
    <body>
        <jsp:include page="/common/header.jsp"/>

        <div class="container-fluid p-3 main-content">
            <div class="row">
                <div class="col-md-12">
                    <h1>Đơn bán của tôi</h1>
                </div>

            </div>
            <!-- Content -->
            <c:set var="paging" value="${requestScope.VIEW_PAGING.paging}"/>
            <c:set var="product" value="${requestScope.VIEW_PAGING.items}"/>

            <form method="get" action="<c:url value='/sale'/>">
                <div class="d-flex justify-content-between w-100">
                    <div class="col-md-6">
                        <div class="input-group mb-3 d-flex flex-column">
                            <div>
                                <label for="amount_from">Tìm theo giá từ</label>
                            </div>
                            <input type="number" class="form-control w-100"
                                   value="${requestScope.FILTER_AmountFrom}"
                                   id="amount_from" name="f_amountFrom">
                        </div>

                        <div class="input-group mb-3 d-flex flex-column">
                            <div>
                                <label for="amount_from">Tìm theo giá đến</label>
                            </div>
                            <input type="number" class="form-control w-100"
                                   value="${requestScope.FILTER_AmountTo}"
                                   id="amount_to" name="f_amountTo">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="input-group mb-3 d-flex flex-column">
                            <div>
                                <label for="start_date">Từ ngày</label>
                            </div>

                            <input type="date" class="form-control w-100" value="${requestScope.FILTER_STARTDATE}"
                                   id="start_date" name="f_start">
                        </div>

                        <div class="input-group mb-3 d-flex flex-column">
                            <div>
                                <label for="end_date">Đến ngày</label>
                            </div>
                            <input type="date" class="form-control w-100" value="${requestScope.FILTER_ENDDATE}"
                                   id="end_date" name="f_end">
                        </div>
                    </div>
                </div>
                <div class="col-md-12 d-flex justify-content-center">
                    <button type="submit" class="btn btn-primary mr-1">Tìm</button>
                    <!-- Button to add product -->
                    <button type="button" class="btn btn-primary" id="add-product">Thêm sản phẩm</button>
                </div>
            </form>

            <div class="col-md-12 mt-4">
                <table class="table">
                    <thead class="thead-dark">
                        <tr>
                            <th>Mã sản phẩm</th>
                            <th>Chủ đề trung gian</th>
                            <th>Trạng thái</th>
                            <th>Phương thức liên hệ</th>
                            <th>Giá tiền</th>
<%--                            <th>Tổng phí</th>--%>
                            <th>Thời gian tạo</th>
                            <th>Cập nhật lần cuối</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${product}" var="product">
                            <tr>
                                <td>${product.id}</td>
                                <td>${product.title}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${product.status eq 0}">Ngừng Giao Dịch.</c:when>
                                        <c:when test="${product.status eq 1}">Đã sẵn sàng.</c:when>
                                        <c:otherwise>Được tạo</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${product.contact}</td>
                                <td>
                                    <c:out value="${f:formatCurrency(product.price)}"/>
                                </td>
<%--                                <td>${product.quantity}</td>--%>
                                <td>
                                    <c:out value="${f:formatLocalDateTime(product.createAt, 'dd/MM/yyyy hh:mm:ss')}"/>
                                </td>
                                <td>
                                    <c:out value="${f:formatLocalDateTime(product.updateAt, 'dd/MM/yyyy hh:mm:ss')}"/>
                                </td>
                                <td>
                                    <a href="<c:url value='/product/detail?id=${product.id}'/>">Chi tiết</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <li class="page-item <c:if test="${paging.currentPage == paging.startPage}">disabled</c:if>">
                        <c:set var="previous"
                               value="${f:pagingUrlGenerate(paging.currentPage-1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"/>
                        <a class="page-link"
                           href="<c:url value='/sale${previous}'/>">Previous</a>
                    </li>
                    <c:forEach begin="1" end="${paging.totalPage}" varStatus="loop">
                        <li class="page-item <c:if test="${loop.index == paging.currentPage}">active</c:if>">
                            <c:set var="current"
                                   value="${f:pagingUrlGenerate(loop.index, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"/>
                            <a class="page-link"
                               href="<c:url value='/sale${current}'/>">${loop.index}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item <c:if test="${paging.currentPage == paging.endPage}">disabled</c:if>">
                        <c:set var="next"
                               value="${f:pagingUrlGenerate(paging.currentPage+1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SAEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"/>
                        <a class="page-link"
                           href="<c:url value='/sale${next}'/>">Next</a>
                    </li>
                </ul>
            </nav>
        </div>

        <jsp:include page="/common/modal.jsp"/>
        <jsp:include page="/common/toast.jsp"/>
        <jsp:include page="product_add-modal.jsp"/>
    </body>
    <jsp:include page="/common/common-js.jsp"/>
    <script type="module" src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
    <script type="module" src="<c:url value="/js/summernote.vi-vn.js" />"></script>
    <script type="module" src="<c:url value="/js/product.js"/>"></script>
</html>