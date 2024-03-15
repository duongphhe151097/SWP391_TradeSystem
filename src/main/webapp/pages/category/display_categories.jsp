<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 3/15/2024
  Time: 1:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Display Categories</title>
</head>
<body>
<h2>Categories</h2>
<ul>
    <c:forEach var="category" items="${categories}">
        <li>${category.title}</li>
    </c:forEach>
</ul>
</body>
</html>


