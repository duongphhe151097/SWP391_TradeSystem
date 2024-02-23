<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 2/9/2024
  Time: 4:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <jsp:include page="../../common/common-css.jsp"/>
        <title>Nạp tiền</title>
    </head>
    <body>
        <jsp:include page="../../common/header.jsp"/>

        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="card mt-5">
                        <div class="card-header">
                            <h4 class="text">Tạo yêu cầu nạp tiền</h4>
                        </div>
                        <div class="card-body">
                            <form action="<c:url value="/payment/vnpay/create"/> " method="post">
                                <input type="hidden" name="language" value="vn">

                                <table class="table table-borderless">
                                    <thead>
                                        <tr>
                                            <th scope="col"></th>
                                            <th scope="col"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <th scope="row">Chọn phương thức:</th>
                                            <td>
                                                <div class="btn-group btn-group-toggle d-flex" data-toggle="buttons">
                                                    <label class="btn btn-primary active mr-2">
                                                        <input type="radio" name="bankCode" value="" checked>
                                                        Thanh toán bằng VNPAY
                                                    </label>
                                                    <label class="btn btn-primary">
                                                        <input type="radio" name="bankCode" value="">
                                                        Chuyển khoản ngân hàng
                                                    </label>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Số tiền:</th>
                                            <td>
                                                <input class="form-control" data-val="true"
                                                       data-val-number="Số tiền phải là số"
                                                       data-val-required="Trường này là bắt buộc" id="amount"
                                                       placeholder="Số tiền tối thiểu là 10,000đ"
                                                       max="10000000" min="10000" name="amount" type="number"
                                                       value="10000"/>
                                                <c:if test="${requestScope.ERROR_AMOUNT != null}">
                                                    <p class="error-message">${requestScope.ERROR_AMOUNT}</p>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>

                                <div class="d-flex justify-content-center">
                                    <c:if test="${requestScope.ERROR_MESSAGE != null}">
                                        <p class="error-message">${requestScope.ERROR_MESSAGE}</p>
                                    </c:if>
                                </div>

                                <div class="d-flex justify-content-center">
                                    <button type="submit" class="btn btn-success">Nạp tiền</button>
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
    <jsp:include page="../../common/common-js.jsp"/>
</html>
