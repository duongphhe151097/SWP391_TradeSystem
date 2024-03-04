<%-- 
    Document   : error.jsp
    Created on : Jan 23, 2024, 6:28:28 PM
    Author     : toden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>There was something wrong happened</h1>
        <h3>Status: ${error.getStatus()}</h3>
        <h3>Error: ${error.getMesg()==null?"unknow":error.getMesg()}</h3>>
    </body>
</html>
