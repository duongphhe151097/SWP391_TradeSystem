<%--
  Created by IntelliJ IDEA.
  User: vuhai
  Date: 3/8/2024
  Time: 1:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cài đặt</title>
</head>
<body>
<button><a href="pages/add_setting"></a></button>
<div class="col-md-12 mt-4">
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th>Id</th>
            <th>Tên</th>
            <th>Giá trị</th>
            <th>Ngày tạo</th>


        </tr>
        </thead>
        <tbody>
<c:forEach items="${settingData}" var="settingData">

    <tr>
        <td>${settingData.id}</td>
        <td>${settingData.key_name}</td>
        <td>${settingData.value}</td>
        <td>${settingData.create_at}</td>
    </tr>

</c:forEach>
        </tbody>
    </table>

</div>
</body>
</html>
