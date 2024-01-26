<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Activation Error</title>
    </head>
    <body>
        <h1>Error Activating Account</h1>
        <p>An error occurred while activating your account. Please try again later or contact support.</p>
        <p>Error Message: ${requestScope.errorMessage}</p>
    </body>
</html>
