<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 2/20/2024
  Time: 9:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Sidebar -->
<div id="sidebar">
    <ul class="nav d-flex">
        <li class=" w-100">
            <a href="<c:url value="/admin/account"/>">
                <span class="d-flex align-items-center">
                    <span class="material-symbols-outlined">
                        person
                    </span>
                    <span class="item-name">
                        Quản lý người dùng
                    </span>
                </span>
            </a>
        </li>
        <li class=" w-100">
            <a href="<c:url value="/admin/payment/history"/>">
                <span class="d-flex align-items-center">
                    <span class="material-symbols-outlined">
                        receipt_long
                    </span>
                    <span class="item-name">
                        Quản lý giao dịch
                    </span>
                </span>
            </a>
        </li>
    </ul>
</div>
