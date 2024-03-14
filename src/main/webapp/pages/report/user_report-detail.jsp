<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 3/14/2024
  Time: 2:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="tradesys.functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <jsp:include page="/common/common-css.jsp"/>
        <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
        <title>Chi tiết báo cáo</title>
    </head>
    <body>
        <jsp:include page="/common/header.jsp"/>

        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="card mt-5">
                        <div class="card-header d-flex align-items-center">
                            <div>
                                <a href="<c:url value="/report"/>">
                                    <span class="material-symbols-outlined">
                                        arrow_back_ios
                                    </span>
                                </a>
                            </div>
                            <div>
                                <h4 class="text">Tạo yêu cầu nạp tiền</h4>
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
                                        <c:set value="${requestScope.VAR_REPORT_DETAIL}" var="report"/>
                                        <tbody>
                                            <tr>
                                                <th scope="row">Tiêu đề:</th>
                                                <td>
                                                    <c:out value="${report.title}"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th scope="row">Trạng thái:</th>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${report.status eq 1}">
                                                            Đang chờ xử lý
                                                        </c:when>
                                                        <c:when test="${report.status eq 2}">
                                                            Đang xử lý
                                                        </c:when>
                                                        <c:when test="${report.status eq 3}">
                                                            Đã xử lý (Báo cáo đúng)
                                                        </c:when>
                                                        <c:when test="${report.status eq 4}">
                                                            Đã xử lý (Báo cáo sai)
                                                        </c:when>
                                                        <c:when test="${report.status eq 5}">
                                                            Đã hủy
                                                        </c:when>
                                                        <c:otherwise>
                                                            Không rõ
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th scope="row">Ngày tạo:</th>
                                                <td>
                                                    <c:out value="${report.createAt}"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th scope="row">Đơn hàng:</th>
                                                <td>
                                                    <c:out value="${report.productTarget}"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th scope="row">Nội dung khiếu nại:</th>
                                                <td>
                                                    <textarea id="report-description" disabled>
                                                        <c:out value="${report.description}"/>
                                                    </textarea>
                                                </td>
                                            </tr>

                                            <tr>
                                                <th scope="row">Phản hồi của admin:</th>
                                                <td>
                                                    <textarea id="admin-response" disabled>
                                                        <c:out value="${report.adminResponse}"/>
                                                    </textarea>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </c:otherwise>
                                </c:choose>
                            </table>
                        </div>

                        <c:if test="${report.status == 1}">
                            <div class="card-footer">
                                <div>
                                    <a class="btn btn-danger" href="<c:url value="/report/detail?id=${report.id}"/>"
                                       role="button" id="abort-report">Hủy báo cáo</a>
                                </div>
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
    <script type="module" src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
    <script type="module" src="<c:url value="/js/summernote.vi-vn.js" />"></script>
    <script type="module" src="<c:url value="/js/report.detail.js" />"></script>
</html>
