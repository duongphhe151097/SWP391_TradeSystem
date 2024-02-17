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
        <title>Title</title>
    </head>
    <body>
        <div class="container">
            <div class="header clearfix">

                <h3 class="text-muted">VNPAY DEMO</h3>
            </div>
            <h3>Tạo mới đơn hàng</h3>
            <div class="table-responsive">
                <form action="<c:url value="/payment/vnpay/create"/> " id="frmCreateOrder" method="post">
                    <div class="form-group">
                        <label for="amount">Số tiền</label>
                        <input class="form-control" data-val="true" data-val-number="The field Amount must be a number."
                               data-val-required="The Amount field is required." id="amount" max="100000000" min="1"
                               name="amount" type="number" value="10000"/>
                    </div>
                    <h4>Chọn phương thức thanh toán</h4>
                    <div class="form-group">
                        <h5>Cách 1: Chuyển hướng sang Cổng VNPAY chọn phương thức thanh toán</h5>
                        <input type="radio" Checked="True" id="bankCode" name="bankCode" value="">
                        <label for="bankCode">Cổng thanh toán VNPAYQR</label><br>

                        <h5>Cách 2: Tách phương thức tại site của đơn vị kết nối</h5>
                        <input type="radio" id="bankCode" name="bankCode" value="VNPAYQR">
                        <label for="bankCode">Thanh toán bằng ứng dụng hỗ trợ VNPAYQR</label><br>

                        <input type="radio" id="bankCode" name="bankCode" value="VNBANK">
                        <label for="bankCode">Thanh toán qua thẻ ATM/Tài khoản nội địa</label><br>

                        <input type="radio" id="bankCode" name="bankCode" value="INTCARD">
                        <label for="bankCode">Thanh toán qua thẻ quốc tế</label><br>

                    </div>
                    <div class="form-group">
                        <h5>Chọn ngôn ngữ giao diện thanh toán:</h5>
                        <input type="radio" id="language" Checked="True" name="language" value="vn">
                        <label for="language">Tiếng việt</label><br>
                        <input type="radio" id="language" name="language" value="en">
                        <label for="language">Tiếng anh</label><br>

                    </div>
                    <button type="submit" class="btn btn-default" id="submitVnpOrder">Thanh toán</button>
                </form>
            </div>
            <p>
                &nbsp;
            </p>
            <footer class="footer">
                <p>&copy; VNPAY 2020</p>
            </footer>
        </div>
    </body>
    <jsp:include page="../../common/common-js.jsp"/>
</html>
