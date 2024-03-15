<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2/23/2024
  Time: 8:02 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang đơn mua</title>
    <link rel="stylesheet" href="<c:out value="${pageContext.request.contextPath}/css/purchase.css"/>">
</head>
<body>
<h1>Đơn mua của tôi</h1>
<c:if test="${empty order}">
    <p>Không tìm thấy đơn hàng</p>
</c:if>
<c:if test="${not empty order}">
    <p>Mã đơn: ${order.id}</p>
    <p>Mã sản phẩm: ${order.product_id}</p>
    <p>Mã người dùng: ${order.user_id}</p>
    <p>Tình trạng: ${order.status}</p>
    <p>Phi: ${order.fee}</p>
    <p>Số lượng: ${order.amount}</p>
    <p>Người tạo: ${order.create_by}</p>

    <form action="<c:url value="/acceptOrder"/>" method="get">
        <input type="hidden" name="id" value="${order.id}">
        <input type="submit" value="Accept">
    </form>

    <form action="<c:url value="/rejectOrder"/>" method="get">
        <input type="hidden" name="id" value="${order.id}">
        <input type="submit" value="Reject">
    </form>
</c:if>
</body>
</html>