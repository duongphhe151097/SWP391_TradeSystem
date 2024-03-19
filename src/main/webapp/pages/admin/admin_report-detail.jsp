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
        <jsp:include page="/common/common-css.jsp"/>
        <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
        <link rel="stylesheet" href="<c:url value="/css/admin.sidenav.css"/> ">
        <link rel="stylesheet" href="<c:url value="/css/admin.manager.css"/> ">
        <title>Chi tiết khiếu nại</title>
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
                            <h1>Chi tiết khiếu nại</h1>
                        </div>
                    </div>

                    <div class="row">
                        <c:choose>
                            <c:when test="${not empty requestScope.ERROR_MESSAGE}">
                                <div class="col-md-12 d-flex justify-content-center">
                                    <h5>${requestScope.ERROR_MESSAGE}</h5>
                                </div>
                            </c:when>

                            <c:otherwise>
                                <c:set value="${requestScope.VAR_DATA}" var="report"/>
                                <div class="col-md-12">
                                    <div class="card mb-5">
                                        <div class="card-body">
                                            <form action="<c:url value="/admin/report/detail"/>" method="post"
                                                  class="d-flex"
                                                  id="adm-resp-form">
                                                <input type="hidden" value="PROCESSED" name="type">
                                                <input type="hidden" value="${report.id}" name="id">
                                                <table class="table table-borderless">
                                                    <thead>
                                                        <tr>
                                                            <th scope="col"></th>
                                                            <th scope="col"></th>
                                                        </tr>
                                                    </thead>

                                                    <tbody>
                                                        <tr>
                                                            <th>Id:</th>
                                                            <td><c:out value="${report.id}"/></td>
                                                        </tr>

                                                        <tr>
                                                            <th>Tiêu đề:</th>
                                                            <td><c:out value="${report.title}"/></td>
                                                        </tr>

                                                        <tr>
                                                            <th>Người báo cáo:</th>
                                                            <td><c:out value="${report.createBy}"/></td>
                                                        </tr>

                                                        <tr>
                                                            <th>Ngày tạo:</th>
                                                            <td>
                                                                <c:out value="${f:formatLocalDateTime(report.createAt, 'dd/MM/yyyy hh:mm:ss')}"/>
                                                            </td>
                                                        </tr>

                                                        <tr>
                                                            <th>Trạng thái:</th>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${report.status eq 1}">
                                                                        Đang chờ bên bán phản hồi
                                                                    </c:when>
                                                                    <c:when test="${report.status eq 2}">
                                                                        Đã hủy
                                                                    </c:when>
                                                                    <c:when test="${report.status eq 3}">
                                                                        Bên bán đồng ý với khiếu nại
                                                                    </c:when>
                                                                    <c:when test="${report.status eq 4}">
                                                                        Bên bán không đồng ý với khiếu nại
                                                                    </c:when>
                                                                    <c:when test="${report.status eq 5}">
                                                                        Bên mua yêu cầu admin kiểm tra
                                                                    </c:when>
                                                                    <c:when test="${report.status eq 6}">
                                                                        Admin đang kiểm tra
                                                                    </c:when>
                                                                    <c:when test="${report.status eq 7}">
                                                                        Đã xử lý (Báo cáo đúng)
                                                                    </c:when>
                                                                    <c:when test="${report.status eq 8}">
                                                                        Đã xử lý (Báo cáo sai)
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        Không rõ
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>

                                                        <tr>
                                                            <th>Đơn hàng báo cáo:</th>
                                                            <td><a href="<c:url value="/product/detail?id=${report.productTarget}"/> ">Xem chi tiết đơn trung gian</a></td>
                                                        </tr>

                                                        <tr>
                                                            <th>Nội dung:</th>
                                                            <td>
                                                                <textarea id="report-description" disabled>
                                                                    <c:out value="${report.description}"/>
                                                                </textarea>
                                                            </td>
                                                        </tr>

                                                        <c:if test="${report.status == 6}">
                                                            <tr>
                                                                <th>Báo cáo sai:</th>
                                                                <td>
                                                                    <div class="custom-control custom-checkbox">
                                                                        <input type="checkbox"
                                                                               class="custom-control-input"
                                                                               id="customCheck1" name="right_report"
                                                                               value="checked">
                                                                        <label class="custom-control-label"
                                                                               for="customCheck1">Tích vào nếu bên mua
                                                                            khiếu nại sai!</label>
                                                                    </div>
                                                                </td>
                                                            </tr>

                                                            <tr>
                                                                <th>Phản hồi của admin:</th>
                                                                <td>
                                                                    <textarea id="report-adminres" name="adm_res">
                                                                        <c:if test="${report.status == 5 && not empty requestScope.VAR_ADMINRESPONSE}">
                                                                            <c:out value="${requestScope.VAR_ADMINRESPONSE}"/>
                                                                        </c:if>
                                                                    </textarea>
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                        <c:if test="${report.status == 7 || report.status == 8}">
                                                            <tr>
                                                                <th>Phản hồi của admin:</th>
                                                                <td>
                                                                    <textarea id="report-adminres" name="adm_res" class="editor-disable">
                                                                        <c:out value="${report.adminResponse}"/>
                                                                    </textarea>
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                    </tbody>
                                                </table>
                                            </form>
                                        </div>
                                        <div class="card-footer">
                                            <c:choose>
                                                <c:when test="${report.status == 5}">
                                                    <div class="d-flex">
                                                        <div>
                                                            <a class="btn btn-primary" id="admin-report-processing"
                                                               href="<c:url value="/admin/report/detail?type=PROCESSING&rid=${report.id}"/>"
                                                               role="button">
                                                                Tiến hành xử lý
                                                            </a>
                                                        </div>
                                                    </div>
                                                </c:when>

                                                <c:when test="${report.status == 6}">
                                                    <div class="d-flex">
                                                        <div>
                                                            <a class="btn btn-primary" id="adm-response"
                                                               href="<c:url value="/admin/report/detail"/>"
                                                               role="button">
                                                                Phản hồi
                                                            </a>
                                                        </div>
                                                    </div>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="/common/modal.jsp"/>
        <jsp:include page="/common/toast.jsp"/>
    </body>
    <jsp:include page="/common/common-js.jsp"/>
    <script type="module" src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
    <script type="module" src="<c:url value="/js/summernote.vi-vn.js" />"></script>
    <script type="module" src="<c:url value="/js/admin.report.js" />"></script>
</html>
