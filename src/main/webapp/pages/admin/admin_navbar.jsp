<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 2/20/2024
  Time: 9:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar d-flex align-items-center justify-content-between">
    <div class="navbar-logo">
        <a href="#">TradeSystemAdmin</a>
    </div>

    <div class="nav-item dropdown d-none d-lg-block user-dropdown">
        <a class="nav-link" id="user-dropdown" href="#" data-bs-toggle="dropdown" aria-expanded="false">
            <img class="img-xs rounded-circle" src="<c:url value="/img/default_male.jpg"/>"
                 alt="Profile image"/>
            <span class="user-name">${sessionScope.SESSION_USERFULLNAME}</span>
        </a>
        <div class="dropdown-menu dropdown-menu-right navbar-dropdown" aria-labelledby="user-dropdown">
            <div class="dropdown-header text-center">
                <img class="img-md rounded-circle" src="<c:url value="/img/default_male.jpg"/>"
                     alt="Profile image"/>
                <p class="mb-1 mt-3 font-weight-semibold">${sessionScope.SESSION_USERFULLNAME}</p>
                <p class="fw-light text-muted mb-0">${sessionScope.SESSION_USEREMAIL}</p>
            </div>
            <a href="<c:url value="/profile"/>" class="dropdown-item">
                            <span class="d-flex align-items-center">
                                <span class="material-symbols-outlined">
                                    person
                                </span>
                                <span>
                                    Thông tin tài khoản
                                </span>
                            </span>
            </a>
            <a href="<c:url value="/logout"/>" class="dropdown-item">
                            <span class="d-flex align-items-center">
                                <span class="material-symbols-outlined">
                                    logout
                                </span>
                                <span>
                                    Đăng xuất
                                </span>
                            </span>
            </a>
        </div>
    </div>
</nav>

