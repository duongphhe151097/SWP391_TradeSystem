<%--
  Created by IntelliJ IDEA.
  User: vuhai
  Date: 2/18/2024
  Time: 9:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>External Transactions</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
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

<h2>External Transactions</h2>

<table id="externalTransactions">
    <thead>
    <tr>
        <th>Mã giao dịch </th>
        <th>Thanh toán bằng </th>
        <th>Ghi chú </th>
        <th>Số tiền</th>
        <th>Trạng thái </th>
        <th>Thời gian tạo </th>
        <th>Người tạo </th>
        <th>Thời gian sửa cuối </th>
        <th>Nguời sửa </th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${externalTransactions}" var="transaction">
        <tr>
            <td>${transaction.id}</td>
            <td>${transaction.type}</td>
            <td>${transaction.command}</td>
            <td>${transaction.amount}</td>
            <td>${transaction.status}</td>
            <td>${transaction.createAt}</td>
            <td>${transaction.createBy}</td>
            <td>${transaction.updateAt}</td>
            <td>${transaction.updateBy}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
