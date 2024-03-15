<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 3/15/2024
  Time: 12:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Category</title>
</head>
<body>
<h2>Add Category</h2>
<form action="addCategory" method="post">
    Parent ID: <input type="text" name="parent_id"><br>
    Title: <input type="text" name="title"><br>
    <input type="submit" value="Add">
</form>
</body>
</html>

