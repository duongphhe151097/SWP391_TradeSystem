<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 2/9/2024
  Time: 4:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <jsp:include page="../../common/common-css.jsp"/>
        <title>Thông báo giao dịch</title>
    </head>
    <body>
        <jsp:include page="../../common/header.jsp"/>

        <div class="container">
            <div class="header clearfix">
                <h3 class="text-muted">KẾT QUẢ THANH TOÁN</h3>
            </div>
            <div class="table-responsive">
                <div class="form-group">
                    <label>Mã giao dịch thanh toán:</label>
                    <label>${requestScope.VAR_TxnRef}
                    </label>
                </div>
                <div class="form-group">
                    <label>Số tiền:</label>
                    <label>${requestScope.VAR_Amount}
                    </label>
                </div>
                <div class="form-group">
                    <label>Mô tả giao dịch:</label>
                    <label>${requestScope.VAR_OrderInfo}
                    </label>
                </div>
                <div class="form-group">
                    <label>Mã lỗi thanh toán:</label>
                    <label>${requestScope.VAR_RespCode}
                    </label>
                </div>
                <div class="form-group">
                    <label>Mã giao dịch tại CTT VNPAY-QR:</label>
                    <label>${requestScope.VAR_TransNo}
                    </label>
                </div>
                <div class="form-group">
                    <label>Mã ngân hàng thanh toán:</label>
                    <label>${requestScope.VAR_BankCode}
                    </label>
                </div>
                <div class="form-group">
                    <label>Thời gian thanh toán:</label>
                    <label>${requestScope.VAR_PayDate}
                    </label>
                </div>
                <div class="form-group">
                    <label>Tình trạng giao dịch:</label>
                    <label>
                        ${requestScope.MESSAGE_NOTI}
                    </label>
                </div>
            </div>
            <p>
                &nbsp;
            </p>
            <footer class="footer">
                <p>&copy; VNPAY 2020</p>
            </footer>
        </div>
    </body>
</html>
