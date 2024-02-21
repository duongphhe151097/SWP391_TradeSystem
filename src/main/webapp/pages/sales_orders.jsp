<%@ page import="java.util.List" %>
<%@ page import="Models.ProductEntity" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách sản phẩm</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<h1>Danh sách sản phẩm</h1>
<table>
    <tr>
        <th>Mã sản phẩm</th>
        <th>Trạng thái</th>
        <th>Chủ đề trung gian</th>
        <th>Phương thức liên hệ</th>
        <th>Giá tiền</th>
        <th>Tổng phí</th>
        <th>Thời gian tạo</th>
        <th>Cập nhật lần cuối</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>${order.getId()}</td>
            <td>${order.getStatus()}</td>
            <td>${order.getTitle()}</td>
            <td>${order.getContact()}</td>
            <td>${order.getPrice()}</td>
            <td>${order.getQuantity()}</td>
            <td>${order.getCreateAt()}</td>
            <td>${order.getUpdateAt()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
