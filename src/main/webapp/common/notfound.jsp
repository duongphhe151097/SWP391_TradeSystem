<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 1/13/2024
  Time: 3:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="common-css.jsp"/>
    <link rel="stylesheet" href="<c:out value="${pageContext.request.contextPath}/css/notfound.css"/>"/>
    <title>Không tìm thấy trang</title>
</head>
<body>
<div class="page_404 h-100 d-flex align-items-center justify-content-center">
    <div class="container">
        <div class="row">
            <div class="col-sm-12 d-flex justify-content-center">
                <div class="col-sm-10 col-sm-offset-1 text-center">
                    <div class="four_zero_four_bg">
                        <h1 class="text-center">404</h1>
                    </div>

                    <div class="contant_box_404">
                        <h3 class="h2">
                            Trang không tồn tại
                        </h3>

                        <a href="<c:url value="/home" />" class="link_404">Trở về trang chủ</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
