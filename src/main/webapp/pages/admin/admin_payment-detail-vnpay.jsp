<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 3/9/2024
  Time: 3:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="f" uri="tradesys.functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set value="${requestScope.VAR_TRANSDETAIL}" var="tranDetail"/>
<c:set value="${requestScope.VAR_TRANSEXT}" var="tranExt"/>

<div class="card">
    <div class="card-header bg-secondary">
        <h5 class="text-white">Thông tin giao dịch</h5>
    </div>
    <div class="card-body bg-light">
        <div class="d-flex">
            <div class="col-md-6">
                <div class="row">
                    <table class="table table-borderless">
                        <thead>
                            <tr>
                                <th scope="col"></th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th>Mã giao dịch:</th>
                                <td><c:out value="${tranDetail.transactionId}"/></td>
                            </tr>
                            <tr>
                                <th>Phương thức:</th>
                                <td><c:out value="${requestScope.VAR_TRANSEXT.type}"/></td>
                            </tr>
                            <tr>
                                <th>Hành động:</th>
                                <c:choose>
                                    <c:when test="${tranExt.command eq 1}">
                                        <td>Nạp tiền</td>
                                    </c:when>
                                    <c:when test="${tranExt.command eq 2}">
                                        <td>Rút tiền</td>
                                    </c:when>
                                    <c:when test="${tranExt.command eq 3}">
                                        <td>Hoàn tiền</td>
                                    </c:when>
                                </c:choose>

                            </tr>
                            <tr>
                                <th>Trạng thái:</th>
                                <c:choose>
                                    <c:when test="${tranExt.status eq 2}">
                                        <td class="text-success font-weight-bold">Thành công</td>
                                    </c:when>
                                    <c:when test="${tranExt.status eq 3}">
                                        <td class="text-danger font-weight-bold">Không thành công</td>
                                    </c:when>
                                    <c:when test="${tranExt.status eq 1}">
                                        <td class="text-info font-weight-bold">Đang xử lý</td>
                                    </c:when>
                                </c:choose>
                            </tr>
                            <tr>
                                <th>Số tiền:</th>
                                <td><c:out value="${f:formatCurrency(tranDetail.amount)}"/></td>
                            </tr>
                            <tr>
                                <th>Đơn vị tiền tệ:</th>
                                <td><c:out value="${tranDetail.currentCode}"/></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="col-md-6">
                <div class="row">
                    <table class="table table-borderless">
                        <thead>
                            <tr>
                                <th scope="col"></th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th>Nội dung giao dịch:</th>
                                <td><c:out value="${tranDetail.orderInfo}"/></td>
                            </tr>
                            <tr>
                                <th>Ngày tạo:</th>
                                <fmt:parseDate var="parsedCreateDate" value="${tranDetail.createDate}" pattern="yyyyMMddHHmmss" />
                                <td><fmt:formatDate value="${parsedCreateDate}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                            </tr>
                            <tr>
                                <th>Ngày hết hạn:</th>
                                <fmt:parseDate var="parsedExpireDate" value="${tranDetail.expireDate}" pattern="yyyyMMddHHmmss" />
                                <td><fmt:formatDate value="${parsedExpireDate}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                            </tr>
                            <tr>
                                <th>Thời gian thanh toán:</th>
                                <c:choose>
                                    <c:when test="${tranDetail.payDate == null}">
                                        <td>Không có</td>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:parseDate var="parsedPayDate" value="${tranDetail.payDate}" pattern="yyyyMMddHHmmss" />
                                        <td><fmt:formatDate value="${parsedPayDate}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                            <tr>
                                <th>Mã giao dịch (cổng thanh toán):</th>
                                <c:choose>
                                    <c:when test="${empty tranDetail.transactionNo}">
                                        <td>Không có</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td><c:out value="${tranDetail.transactionNo}"/></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                            <tr>
                                <th>Người tạo:</th>
                                <td>
                                    <a href="<c:url value="/admin/account/detail?uname=${tranDetail.createBy}"/>">
                                        <c:out value="${tranDetail.createBy}"/>
                                    </a>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="col-md-12 d-flex">
            <div class="mr-2">
                <a id="payment-check" class="btn btn-primary" href="<c:url value="/payment/vnpay/query?txn=${tranDetail.transactionId}&td=${tranDetail.createDate}"/>" role="button">Kiểm tra giao dịch (VNPAY)</a>
            </div>
            <div>
                <a id="payment-refund" class="btn btn-primary" href="#" role="button">Hoàn tiền</a>
            </div>
        </div>
    </div>
</div>
