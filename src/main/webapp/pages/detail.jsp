<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi">
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
        input[readonly], textarea[readonly], select[readonly] {
            font-weight: bold;
        }
        input[readonly], textarea[readonly], select[readonly] {
            color: gray;
        }
    </style>
</head>
<body>
<div class="container">
    <%-- Hiển thị thông báo nếu có --%>

    <form action="<c:url value='/detail'/>" method="post"> <!-- Bắt đầu biểu mẫu -->
        <div class="form-group">
            <label for="id">Mã trung gian (*)</label>
            <input type="text" id="id" name="id" value="${product.id}" readonly>
        </div>
        <div class="form-group">
            <label for="title">Chủ đề trung gian (*)</label>
            <input type="text" id="title" name="title" value="${product.title}" >
        </div>
        <%--        <div class="form-group">--%>
        <%--            <label for="buyer">Người mua (*)</label>--%>
        <%--            <input type="text" id="buyer" name="buyer" value="${product.buyer}" readonly>--%>
        <%--        </div>--%>
        <div class="form-group">
            <label for="status">Trạng thái (*)</label>
            <input type="text" id="status" name="status" value="${product.status}" readonly>
        </div>
        <div class="form-group">
            <label for="price">Giá tiền (*)</label>
            <input type="text" id="price" name="price" value="${product.price}" >
        </div>
        <%--        <div class="form-group">--%>
        <%--            <label for="role">Bên chịu phí trung gian (*)</label>--%>
        <%--            <input type="text" id="role" name="role" value="${product.role}" readonly>--%>
        <%--        </div>--%>
        <div class="form-group">
            <label for="description">Mô tả (*)</label>
            <textarea id="description" name="description" >${product.description}</textarea>


            <small>Càng chi tiết về sản phẩm càng tốt vì đây sẽ là cơ sở pháp lý giải quyết khiếu nại nếu có sau này</small>
        </div>
        <div class="form-group">
            <label for="createBy">Người Bán (*)</label>
            <input type="text" id="createBy" name="createBy" value="${product.createBy}" readonly>
        </div>
        <div class="form-group">
            <label for="contact">Phương thức liên hệ</label>
            <input type="text" id="contact" name="contact" value="${product.contact}" >
        </div>
        <c:if test="${canViewSecret}">
            <!-- Hiển thị phần nội dung ẩn của sản phẩm -->
            <div class="form-group">
                <label for="secret">Nội dung ẩn (*)</label>
                <textarea id="secret" name="secret" class="secret">${product.secret}</textarea>
                <small>Các chi tiết nội dung ẩn mà bạn muốn bổ sung</small>
            </div>
        </c:if>
        <div class="form-group">
            <label>
                <input type="checkbox" id="public" name="public" checked>
                Hiện công khai (*)
            </label>
        </div>
        <div class="form-group">
            <label for="createAt">Thời gian tạo (*)</label>
            <input type="text" id="createAt" name="createAt" value="${product.createAt}" readonly>
        </div>
        <div class="form-group">
            <label for="UpdateAt">Cập nhật lần cuối (*)</label>
            <input type="text" id="UpdateAt" name="UpdateAt" value="${product.updateAt}" readonly>
        </div>
        <c:if test="${not empty resultMessage}">
            <div class="form-group">
                <p class="message">${resultMessage}</p>
            </div>
        </c:if>

        <c:if test="${not product.userId.equals(sessionScope.SESSION_USERID)}">
            <button type="submit">Mua</button>
        </c:if>

        <c:if test="${product.userId.equals(sessionScope.SESSION_USERID)}">
            <button type="submit">Cập nhật</button>
        </c:if>
    </form>
</div>
<script>
    CKEDITOR.replace('description');
    CKEDITOR.replace('secret');
</script>
</body>
</html>