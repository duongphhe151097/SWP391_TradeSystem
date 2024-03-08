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


                </div>
            </div>
        </div>

        <jsp:include page="/common/modal.jsp" />
        <jsp:include page="/common/toast.jsp" />
    </body>
    <jsp:include page="/common/common-js.jsp"/>
    <script type="module" src="<c:url value="/js/admin.account.js"/>"></script>
</html>
