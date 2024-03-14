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
        <title>Chi tiết báo cáo</title>
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
                            <h1>Chi tiết báo cáo</h1>
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
                                                            <th>Đơn hàng báo cáo:</th>
                                                            <td><c:out value="${report.productTarget}"/></td>
                                                        </tr>

                                                        <tr>
                                                            <th>Nội dung:</th>
                                                            <td>
                                                                <textarea id="report-description" disabled>
                                                                    <c:out value="${report.description}"/>
                                                                </textarea>
                                                            </td>
                                                        </tr>

                                                        <c:if test="${report.status == 2}">
                                                            <tr>
                                                                <th>Báo cáo sai:</th>
                                                                <td>
                                                                    <div class="custom-control custom-checkbox">
                                                                        <input type="checkbox" class="custom-control-input" id="customCheck1" name="right_report" value="checked">
                                                                        <label class="custom-control-label" for="customCheck1">(Tích nếu báo cáo sai!)</label>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                        <c:if test="${report.status != 1}">
                                                            <tr>
                                                                <th>Phản hồi admin:</th>
                                                                <td>
                                                                    <textarea id="report-adminres" name="adm_res" <c:if test="${report.status == 3 || report.status == 4}">class="editor-disable"</c:if>>
                                                                        <c:choose>
                                                                            <c:when test="${report.status == 2 && not empty requestScope.VAR_ADMINRESPONSE}">
                                                                                <c:out value="${requestScope.VAR_ADMINRESPONSE}"/>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <c:out value="${report.adminResponse}"/>
                                                                            </c:otherwise>
                                                                        </c:choose>
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
                                                <c:when test="${report.status == 1}">
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

                                                <c:when test="${report.status == 2}">
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
