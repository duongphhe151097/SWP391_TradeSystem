<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 1/12/2024
  Time: 4:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="tradesys.functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header>
    <div class="sticky-top">
        <div class="navbar navbar-light bg-light">
            <div class="container">
                <a class="navbar-brand" href="<c:url value="/"/>">
                    <img src="<c:url value="/img/logo.png"/>" alt="Tradesystem logo" width="80" height="40"
                         class="d-inline-block align-text-top">

                </a>

                <form method="get" action="" class="search d-flex align-items-center nav-form-search">
                    <input class="form-control me-2" type="search" placeholder="Search"
                           aria-label="Search">
                    <button class="btn btn-primary ml-2">
                            <span class="d-flex align-items-center">
                                <span class="material-symbols-outlined">
                                    search
                                </span>
                                <span>
                                    Tìm
                                </span>
                            </span>
                    </button>
                </form>

                <!-- Pre Login -->
                <c:if test="${sessionScope.SESSION_USERID == null}">
                    <div class="d-flex">
                        <a class="btn btn btn-outline-success mr-2" href="<c:url value="/login"/>">Đăng nhập</a>
                        <a class="btn btn-primary" href="<c:url value="/register"/>">Đăng kí</a>
                    </div>
                </c:if>
                <!-- End Pre Login -->

                <!-- Post Login -->
                <c:if test="${sessionScope.SESSION_USERID != null}">

                    <div class="d-flex align-items-center">
                        <div class="nav-item dropdown d-none d-lg-block user-dropdown">
                            <a class="nav-link" id="user-dropdown" href="#" data-bs-toggle="dropdown"
                               aria-expanded="false">
                                <img class="img-xs rounded-circle" src="<c:url value="/img/default_male.jpg"/>"
                                     alt="Profile image"/>
                                <span class="user-name">
                                        ${sessionScope.SESSION_USERFULLNAME}
                                </span>
                            </a>
                            <div class="dropdown-menu dropdown-menu-right navbar-dropdown"
                                 aria-labelledby="user-dropdown">
                                <div class="dropdown-header text-center">
                                    <img class="img-md rounded-circle" src="<c:url value="/img/default_male.jpg"/>"
                                         alt="Profile image"/>
                                    <p class="mb-1 mt-3 font-weight-semibold">${sessionScope.SESSION_USERFULLNAME}</p>
                                    <p class="fw-light text-muted mb-0">${sessionScope.SESSION_USEREMAIL}</p>
                                    <div class="d-flex justify-content-center">
                                        <div>
                                            <a href="<c:url value="/user/balance"/> " id="refresh-balance">
                                                <span class="material-symbols-outlined">
                                                    refresh
                                                </span>
                                            </a>
                                        </div>
                                        <div>
                                            <p id="balance-amount">Số dư: ${f:formatCurrency(sessionScope.SESSION_USERBALANCE)}</p>
                                        </div>
                                    </div>
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
                                <a href="<c:url value="/payment/vnpay/create"/>" class="dropdown-item">
                                    <span class="d-flex align-items-center">
                                        <span class="material-symbols-outlined">
                                            person
                                        </span>
                                        <span>
                                            Nạp tiền
                                        </span>
                                    </span>
                                </a>
                                <a href="<c:url value="/change"/>" class="dropdown-item">
                                    <span class="d-flex align-items-center">
                                        <span class="material-symbols-outlined">
                                            vpn_key
                                        </span>
                                        <span>
                                            Đổi mật khẩu
                                        </span>
                                    </span>
                                </a>
                                <a href="<c:url value="/logout"/> " class="dropdown-item">
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
                            <div class="nav-item dropdown d-none d-lg-block notification-dropdown">
                                <a class="nav-link" id="notification-dropdown" href="#" data-bs-toggle="dropdown"
                                   aria-expanded="false">
                                    <span class="material-symbols-outlined">notifications</span>
                                    <!-- Badge for notification count -->
                                    <span id="notificationBadge" class="badge badge-pill badge-primary"></span>
                                </a>
                                <div class="dropdown-menu dropdown-menu-left navbar-dropdown"
                                     aria-labelledby="notification-dropdown" id="notificationDropdown">
                                    <ul class="dropdown-menu">
                                        <li><h6 class="dropdown-header">Dropdown header</h6></li>
                                        <li><hr class="dropdown-divider"></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <!-- End Notification dropdown -->


                </c:if>

                <!-- End Post Login -->
            </div>
            </div>
        </nav>
    </div>
    <script type="module" src="<c:url value="/js/notification.js"/>"></script>
</header>

