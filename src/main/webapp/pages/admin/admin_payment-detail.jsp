<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 3/4/2024
  Time: 3:56 PM
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
        <title>Chi tiết giao dịch</title>
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
                            <h1>Chi tiết giao dịch</h1>
                        </div>
                    </div>
                    <c:choose>
                        <c:when test="${not empty requestScope.ERR_MESSAGE}">
                            <div class="row">
                                <div class="col-md-12 d-flex justify-content-center">
                                    <h3>${requestScope.ERR_MESSAGE}</h3>
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${not empty requestScope.ERR_NOTFOUND}">
                            <div class="row">
                                <div class="col-md-12 d-flex justify-content-center">
                                    <h3>${requestScope.ERR_NOTFOUND}</h3>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <div class="col-md-12">
                                    <c:choose>
                                        <c:when test="${requestScope.VAR_TYPE eq 'VNPAY'}">
                                            <c:import url="admin_payment-detail-vnpay.jsp"/>
                                        </c:when>
                                        <c:when test="${requestScope.VAR_TYPE eq 'ITN'}">
                                            <h1>INTERNAL</h1>
                                        </c:when>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="row mt-5">
                                <div class="col-md-12">
                                    <div class="card">
                                        <div class="card-header bg-secondary">
                                            <h5 class="text-white">Thông tin truy xuất từ VNPAY</h5>
                                        </div>
                                        <div class="card-body">
                                            <div class="col-md-6">
                                                <div class="row">
                                                    <table class="table table-borderless">
                                                        <thead>
                                                            <tr>
                                                                <th scope="col"></th>
                                                                <th scope="col"></th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>

                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>

                                            <div class="col-md-6">
                                                <div class="row">
                                                    <table class="table table-borderless">
                                                        <thead>
                                                            <tr>
                                                                <th scope="col"></th>
                                                                <th scope="col"></th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>

                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
        </div>

        <jsp:include page="/common/modal.jsp" />
        <jsp:include page="/common/toast.jsp" />
    </body>
    <jsp:include page="/common/common-js.jsp"/>
    <script type="module" src="<c:url value="/js/admin.account.js"/>"></script>
</html>
