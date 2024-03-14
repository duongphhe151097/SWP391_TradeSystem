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
<div class="col-md-12 mt-4">
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th>Mã giao dịch</th>
            <th>Thanh toán bằng</th>
            <th>Lệnh</th>
            <th>Số tiền</th>
            <th>Trạng thái</th>
            <th>Thời gian tạo</th>
            <th>Người tạo</th>
            <th>Hành động</th>

        </tr>
        </thead>
        <c:choose>
            <c:when test="${}">
                <tbody>

                <tr>
                    <td colspan="8">
                        <div class="d-flex justify-content-center">
                            <h2>Không có kết quả!</h2>
                        </div>
                    </td>
                </tr>
                </tbody>
            </c:when>
            <c:otherwise>
                <tbody>

                <c:forEach items="${transaction}" var="transaction">
                    <tr>
                        <td>${}</td>
                        <td>${}</td>
                        <td>

                        </td>
                        <td>${}</td>
                        <td>

                        </td>
                        <td>${}</td>
                        <td>${}</td>
                        <td><a href="">Chi tiết</a></td>

                    </tr>
                </c:forEach>

                </tbody>
            </c:otherwise>
        </c:choose>
    </table>

</div>
</body>
</html>
