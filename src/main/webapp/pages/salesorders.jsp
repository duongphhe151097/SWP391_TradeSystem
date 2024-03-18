<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="f" uri="tradesys.functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <jsp:include page="../common/common-css.jsp"/>
    <link rel="stylesheet" href="<c:url value='/css/admin.sidenav.css'/> ">
    <link rel="stylesheet" href="<c:url value='/css/admin.manager.css'/> ">
    <title>Đơn Bán</title>
    <style>
        /* CSS cho modal */
        /* CSS cho modal */
        .modal {
            display: none;
            position: fixed;
            z-index: 2;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.4);
        }

        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .modal-open {
            overflow: hidden;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }

        .button-container {
            text-align: right;
            margin-bottom: 20px;
        }

        .table-container {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        /* Button CSS */
        .add-product-button {
            display: inline-block;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<div id="content">
    <div class="container-fluid p-3 main-content">
        <div class="row">
            <div class="col-md-12">
                <h1>Đơn bán của tôi</h1>
            </div>

        </div>
    <!-- Content -->
        <c:set var="paging" value="${requestScope.VIEW_PAGING.paging}"/>
        <c:set var="product" value="${requestScope.VIEW_PAGING.items}"/>

        <form method="get" action="<c:url value='/sale'/>">
            <div class="form-row">
                <div class="col-md-3 mb-3">
                    <label for="amount_from">Tìm theo giá từ</label>
                    <input type="number" class="form-control" value="${requestScope.FILTER_AmountFrom}" id="amount_from" name="f_amountFrom">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="amount_to">Tìm theo giá đến</label>
                    <input type="number" class="form-control" value="${requestScope.FILTER_AmountTo}" id="amount_to" name="f_amountTo">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="start_date">Từ ngày</label>
                    <input type="date" class="form-control" value="${requestScope.FILTER_STARTDATE}" id="start_date" name="f_start">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="end_date">Đến ngày</label>
                    <input type="date" class="form-control" value="${requestScope.FILTER_ENDDATE}" id="end_date" name="f_end">
                </div>
            </div>
            <div class="ml-3 input-group mb-3 d-flex flex-column justify-content-end">
                <button type="submit" class="btn btn-primary">Tìm</button>
                <!-- Button to add product -->
                <button type="button" class="btn btn-primary mt-3" onclick="openModal()">Thêm sản phẩm</button>
            </div>
        </form>

        <div class="col-md-12 mt-4">
                <table class="table">
                    <thead class="thead-dark">
                    <tr>
                        <th>Mã sản phẩm</th>
                        <th>Trạng thái</th>
                        <th>Chủ đề trung gian</th>
                        <th>Phương thức liên hệ</th>
                        <th>Giá tiền</th>
                        <th>Tổng phí</th>
                        <th>Thời gian tạo</th>
                        <th>Cập nhật lần cuối</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${product}" var="product">
                        <tr>
                            <td>${product.id}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.status eq 0}">Ngừng Giao Dịch.</c:when>
                                    <c:when test="${product.status eq 1}">Đã sẵn sàng.</c:when>
                                    <c:otherwise>Được tạo</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${product.title}</td>
                            <td>${product.contact}</td>
                            <td>${product.price}</td>
                            <td>${product.quantity}</td>
                            <td>${product.createAt}</td>
                            <td>${product.updateAt}</td>
                            <td>
                                <a href="<c:url value='/detail?id=${product.id}'/>">Chi tiết</a>


                            </td>

                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <li class="page-item <c:if test="${paging.currentPage == paging.startPage}">disabled</c:if>">
                        <c:set var="previous" value="${f:pagingUrlGenerate(paging.currentPage-1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"/>
                        <a class="page-link"
                           href="<c:url value='/sale${previous}'/>">Previous</a>
                    </li>
                    <c:forEach begin="1" end="${paging.totalPage}" varStatus="loop">
                        <li class="page-item <c:if test="${loop.index == paging.currentPage}">active</c:if>">
                            <c:set var="current" value="${f:pagingUrlGenerate(loop.index, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"/>
                            <a class="page-link"
                               href="<c:url value='/sale${current}'/>">${loop.index}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item <c:if test="${paging.currentPage == paging.endPage}">disabled</c:if>">
                        <c:set var="next" value="${f:pagingUrlGenerate(paging.currentPage+1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_SAEARCH, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE, requestScope.FILTER_ENDDATE)}"/>
                        <a class="page-link"
                           href="<c:url value='/sale${next}'/>">Next</a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>

<!-- Modal -->
<div id="myModal" class="modal">
    <div class="modal-content">
        <!-- Nội dung modal -->
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Form thay đổi sản phẩm</title>
            <!-- CKEditor CDN -->
            <script src="https://cdn.ckeditor.com/4.16.2/standard/ckeditor.js"></script>
            <style>
                /* CSS styles */
                body, input, select, textarea {
                    font-family: Arial, sans-serif;
                }

                .container {
                    max-width: 800px;
                    margin: 10px auto;
                    padding: 20px;
                }

                .form-group {
                    margin-bottom: 20px;
                }

                .form-group label {
                    display: block;
                    font-weight: bold;
                    margin-bottom: 5px;
                }

                .form-group input[type="text"],
                .form-group textarea,
                .form-group select {
                    width: calc(100% - 20px);
                    padding: 8px;
                    border: 1px solid #ccc;
                    border-radius: 4px;
                    box-sizing: border-box;
                }

                .form-group textarea {
                    height: 150px;
                }

                .form-group .hidden-content {
                    display: none;
                }

                .message {
                    color: red;
                    font-weight: bold;
                }
            </style>
        </head>
        <body>
        <div class="container">
            <%-- Hiển thị thông báo nếu có --%>

            <form action="<c:url value='/sale'/>" method="post"> <!-- Bắt đầu biểu mẫu -->
                <div class="form-group">
                    <label for="title">Chủ đề trung gian (*)</label>
                    <input type="text" id="title" name="title" required>
                </div>
                <div class="form-group">
                    <label for="price">Giá tiền (*)</label>
                    <input type="text" id="price" name="price" required oninput="this.value = this.value.replace(/[^0-9]/g, '')" inputmode="numeric">
                </div>

                <div class="form-group">
                    <label for="role">Bên chịu phí trung gian (*)</label>
                    <select id="role" name="role" required>
                        <option value="buyer">Người mua</option>
                        <option value="seller">Người bán</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="description">Mô tả (*)</label>
                    <textarea id="description" name="description" required></textarea>
                    <small>Càng chi tiết về sản phẩm càng tốt vì đây sẽ là cơ sở pháp lý giải quyết khiếu nại nếu có sau này</small>
                </div>
                <div class="form-group">
                    <label for="contact">Phương thức liên hệ</label>
                    <input type="text" id="contact" name="contact">
                </div>
                <div class="form-group">
                    <label for="secret">Nội dung ẩn (*)</label>
                    <textarea id="secret" name="secret" class="secret" required></textarea>
                    <small>Các chi tiết nội dung ẩn mà bạn muốn bổ sung</small>
                </div>
                <div class="form-group">
                    <label>
                        <input type="checkbox" id="public" name="public" checked>
                        Hiện công khai (*)
                    </label>
                </div>
                <c:if test="${not empty resultMessage}">
                    <script>
                        window.onload = function() {
                            showToast("${resultMessage}");
                        }
                    </script>
                </c:if>
                <input type="hidden" name="userId" value="<%= session.getAttribute("userId") %>">
                <button type="submit">Gửi</button>
            </form> <!-- Kết thúc biểu mẫu -->
        </div>
        <script>
            CKEDITOR.replace('description');
            CKEDITOR.replace('secret');
        </script>
        </body>
    </div>
</div>

<jsp:include page="../common/modal.jsp" />
<jsp:include page="../common/toast.jsp" />
<jsp:include page="../common/common-js.jsp"/>

<script>
    function openModal() {
        var modal = document.getElementById("myModal");
        modal.style.display = "block";
        document.body.classList.add("modal-open"); // Thêm lớp modal-open vào body
    }

    function closeModal() {
        var modal = document.getElementById("myModal");
        modal.style.display = "none";
        document.body.classList.remove("modal-open"); // Loại bỏ lớp modal-open khỏi body
    }

    window.onclick = function(event) {
        var modal = document.getElementById("myModal");
        if (event.target == modal) {
            modal.style.display = "none";
            document.body.classList.remove("modal-open"); // Loại bỏ lớp modal-open khỏi body
        }
    }
</script>

</body>
</html>