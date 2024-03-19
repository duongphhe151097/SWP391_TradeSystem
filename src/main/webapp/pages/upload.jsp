<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 3/18/2024
  Time: 4:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Title</title>
    </head>
    <body>
        <div><h1>S3 Upload File Example</h1></div>

        <div>
            <form action="<c:url value="/product/upload"/>" method="post" enctype="multipart/form-data">
                <p>Description: <input type="text" name="description" size="30" required /></p>
                <p><input type="file" name="file" required /></p>
                <p><button type="submit">Submit</button></p>
            </form>
        </div>
    </body>
</html>
