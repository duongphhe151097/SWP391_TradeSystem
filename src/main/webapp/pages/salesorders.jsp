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

        .button {
            background-color: #4CAF50; /* Green */
            border: none;
            color: white;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 4px 2px;
            cursor: pointer;
            border-radius: 5px;
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
        <th>Thao tác</th>
    </tr>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>${product.id}</td>
            <td>${product.status}</td>
            <td>${product.title}</td>
            <td>${product.contact}</td>
            <td>${product.price}</td>
            <td>${product.quantity}</td>
            <td>${product.createAt}</td>
            <td>${product.updateAt}</td>
            <td>
                <a href="updateProduct?id=${product.id}" class="button">Cập nhật</a>
                <a href="addproduct" class="button">Thêm</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
