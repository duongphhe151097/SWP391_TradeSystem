<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 2/9/2024
  Time: 4:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="tradesys.functions" %>
<html>
    <head>
        <jsp:include page="../../common/common-css.jsp"/>
        <link rel="stylesheet" href="<c:url value="/css/payment.return.css" />">
        <title>Thông báo giao dịch</title>
    </head>

    <body>
        <jsp:include page="../../common/header.jsp"/>

        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="card mt-5">
                        <div class="card-header">
                            <h4 class="text">Kết quả giao dịch</h4>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-center">
                                <div>
                                    <div class="d-flex justify-content-center">
                                        <c:if test="${requestScope.IS_SUCCESS}">
                                            <span class="material-symbols-outlined text-success noti-icon">
                                                check_circle
                                            </span>
                                        </c:if>

                                        <c:if test="${not requestScope.IS_SUCCESS}">
                                            <span class="material-symbols-outlined text-danger noti-icon">
                                                cancel
                                            </span>
                                        </c:if>
                                    </div>

                                    <div class="d-flex justify-content-center mt-3">
                                        <h5>Số tiền: <c:out value="${f:formatCurrency(requestScope.VAR_Amount)}"/></h5>
                                    </div>
                                    <div class="d-flex justify-content-center">
                                        <h4>${requestScope.MESSAGE_NOTI}</h4>
                                    </div>

                                    <div class="d-flex justify-content-center mt-5">
                                        <p>Mã giao dịch: ${requestScope.VAR_TxnRef}</p>
                                    </div>
                                    <div class="d-flex justify-content-center">
                                        <p>Thời gian: ${requestScope.VAR_PayDate}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../../common/common-js.jsp"/>
</html>
