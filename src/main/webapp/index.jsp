<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="common/common-css.jsp"/>
        <title>Trang chủ</title>
    </head>
    <body>
        <jsp:include page="common/header.jsp"/>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12 d-flex justify-content-center">
                    <div>
                        <img src="<c:url value="/img/handshake.png"/> " alt="handshake"/>
                    </div>
                </div>
                <div class="col-md-12 d-flex flex-column justify-content-center align-items-center">
                    <h3>Trade System</h3>
                    <h5>Nơi trao đổi trung gian uy tín</h5>
                </div>
                <div class="col-md-12 mt-2 d-flex justify-content-center align-items-center">
                    <div>
                        <a class="btn btn-success" href="<c:url value="/market" />" role="button">Đến chợ trung gian</a>
                    </div>
                </div>

<%--                <div class="col-md-12 d-flex justify-content-center">--%>
<%--                    <div>--%>
<%--                        <a href="<c:url value="/register"/>">Đăng kí nếu chưa có tài khoản</a>--%>
<%--                    </div>--%>
<%--                </div>--%>
            </div>
        </div>

<%--        <h1>Home</h1>--%>
        <%--        <a href="">Chợ Công Khai</a>--%>
        <%--        <a href="<c:url value="/sale" />">Đơn bán của bạn</a>--%>
    </body>
    <jsp:include page="common/common-js.jsp"/>
</html>
