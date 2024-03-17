<%--
  Created by IntelliJ IDEA.
  User: vuhai
  Date: 3/10/2024
  Time: 1:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Nạp tiền</title>
</head>
<body>
<h4 class="text">Tạo cài đặt</h4>

<div class="vh-100 d-flex justify-content-center align-items-center">
    <div class="form-bloat shadow rounded-3">
        <div class="mb-2 d-flex justify-content-between align-content-center">
            <div class="title d-flex align-content-center">
                <h3>Add setting</h3>
            </div>

            <div class="logo">
                <img src="<c:url value="/img/logo.png"/>" alt="Trade System Logo" class="img-fluid logo">
            </div>

        </div>
        <form action="<c:url value="/setting/add"/>" method="post">
            <div class="form-group">
                <label for="keynameInput">Tên cài đặt</label>
                <input type="text" class="form-control" id="keynameInput" aria-describedby="nameHelp"
                       placeholder="Nhập tên cài đặt" value="${requestScope.VAR_KEYNAME}" name="key_name">
                <c:if test="${requestScope.KEYNAME_ERROR != null}">
                    <small id="nameHelp"
                           class="form-text text-muted error-message">${requestScope.KEYNAME_ERROR}</small>
                </c:if>
            </div>

            <div class="form-group">
                <label for="ValueInput">Giá trị</label>
                <input type="value" class="form-control" id="valueInput" aria-describedby="valuelHelp"
                       placeholder="Nhập email" value="${requestScope.VAR_VALUE}" name="value">
                <c:if test="${requestScope.VALUE_ERROR != null}">
                    <small id="emailHelp"
                           class="form-text text-muted error-message">${requestScope.VALUE_ERROR}</small>
                </c:if>
            </div>


            <c:if test="${requestScope.SUCCESS_MESSAGE != null || requestScope.FAILED_MESSAGE != null}">
                <div class="form-group">
                    <c:if test="${requestScope.SUCCESS_MESSAGE != null}">
                        <p class="success-message">${requestScope.SUCCESS_MESSAGE}</p>
                    </c:if>
                    <c:if test="${requestScope.FAILED_MESSAGE != null}">
                        <p class="error-message">${requestScope.FAILED_MESSAGE}</p>
                    </c:if>
                </div>
            </c:if>

            <div class="form-group mt-5">
                <div class="d-flex justify-content-center">
                    <button type="submit" class="btn btn-primary">Tạo</button>
                </div>

                <div class="link mt-3">
                    <div class="form-row">
                        <div class="col d-flex justify-content-start ml-5">
                            <a href="<c:url value="/setting"/> ">Quay lại trang cài đặt</a>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

</body>
</html>
