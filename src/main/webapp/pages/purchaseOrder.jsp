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
    <p>Tên đơn: ${order.title}</p>
    <p>Mô tả: ${order.description}</p>
    <p>Nội dung ẩn: ${order.secret}</p>
    <p>Giá bán: ${order.price}</p>
    <p>Phương thức liên hệ: ${order.contact}</p>
    <p>Số lượng: ${order.quantity}</p>
    <p>Tình trạng: ${order.status}</p>

    <form action="<c:url value="/purchaseOrder"/>" method="post">
        <input type="hidden" name="orderId" value="${order.id}">
        <input type="submit" value="Purchase">
    </form>
</c:if>
</body>
</html>
