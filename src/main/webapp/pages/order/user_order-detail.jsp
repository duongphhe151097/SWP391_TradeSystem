<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 3/14/2024
  Time: 2:46 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="f" uri="tradesys.functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <jsp:include page="/common/common-css.jsp"/>
        <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
        <title>Chi tiết đơn mua</title>
    </head>
    <body>
        <jsp:include page="/common/header.jsp"/>

        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="card mt-5">
                        <div class="card-header d-flex align-items-center">
                            <div>
                                <a href="<c:url value="/order"/>">
                                    <span class="material-symbols-outlined">
                                        arrow_back_ios
                                    </span>
                                </a>
                            </div>
                            <div>
                                <h4 class="text">Chi tiết đơn mua</h4>
                            </div>
                        </div>

                        <div class="card-body">
                            <table class="table table-borderless">
                                <thead>
                                    <tr>
                                        <th scope="col"></th>
                                        <th scope="col"></th>
                                    </tr>
                                </thead>
                                <c:choose>
                                    <c:when test="${not empty requestScope.ERROR_MESSAGE}">
                                        <tbody>
                                            <tr>
                                                <td colspan="2">
                                                    <div class="d-flex justify-content-center">
                                                        <h2><c:out value="${requestScope.ERROR_MESSAGE}"/></h2>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </c:when>

                                    <c:otherwise>
                                        <c:set value="${requestScope.VAR_ORDER}" var="order"/>
                                        <tbody>
                                            <tr>
                                                <th scope="row">Mã đơn mua:</th>
                                                <td>
                                                    <c:out value="${order.id}"/>
                                                </td>
                                            </tr>

                                            <tr>
                                                <th scope="row">Đơn trung gian:</th>
                                                <td>
                                                    <a href="<c:url value="/product/detail?id=${order.productId}"/>">
                                                        Xem chi tiết đơn trung gian</a>
                                                </td>
                                            </tr>

                                            <tr>
                                                <th scope="row">Trạng thái:</th>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${order.status eq 1}">
                                                            Người mua đang kiểm tra
                                                        </c:when>
                                                        <c:when test="${order.status eq 2}">
                                                            Đơn trung gian bị khiếu nại
                                                        </c:when>
                                                        <c:when test="${order.status eq 3}">
                                                            Giao dịch thành công
                                                        </c:when>
                                                        <c:otherwise>
                                                            Giao dịch bị hủy
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>

                                            <c:if test="${requestScope.VAR_REPORT ne null}">
                                                <tr>
                                                    <th scope="row">Khiếu nại:</th>
                                                    <td>
                                                        <a href="<c:url value="/report/detail?id=${requestScope.VAR_REPORT.id}"/>">Xem chi tiết khiếu nại</a>
                                                    </td>
                                                </tr>
                                            </c:if>

                                            <tr>
                                                <th scope="row">Phí mua:</th>
                                                <td>${f:formatCurrency(order.fee)}</td>
                                            </tr>

                                            <tr>
                                                <th scope="row">Giá đơn trung gian:</th>
                                                <td>
                                                    <c:out value="${f:formatCurrency(order.amount)}"/>
                                                </td>
                                            </tr>

                                            <tr>
                                                <th scope="row">Ngày tạo:</th>
                                                <td>${f:formatLocalDateTime(order.createAt, 'dd/MM/yyyy hh:mm:ss')}</td>
                                            </tr>
                                        </tbody>
                                    </c:otherwise>
                                </c:choose>
                            </table>
                        </div>

                        <div class="card-footer d-flex">
                            <c:choose>
                                <c:when test="${order.status eq 1}">
                                    <div class="mr-2">
                                        <a class="btn btn-danger" href="<c:url value="/report?pid=${order.productId}"/>"
                                           role="button" id="order-report">Khiếu nại</a>
                                    </div>

                                    <div class="mr-2">
                                        <a class="btn btn-success" href="<c:url value="/order/detail?id=${order.productId}"/>"
                                           role="button" id="order-confirm">Xác nhận thành công</a>
                                    </div>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="/common/modal.jsp"/>
        <jsp:include page="/common/toast.jsp"/>
        <jsp:include page="/pages/report/user_report-product-modal.jsp"/>
    </body>
    <jsp:include page="/common/common-js.jsp"/>
    <script type="module" src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
    <script type="module" src="<c:url value="/js/summernote.vi-vn.js" />"></script>
    <script type="module" src="<c:url value="/js/order.js"/>"></script>
</html>
