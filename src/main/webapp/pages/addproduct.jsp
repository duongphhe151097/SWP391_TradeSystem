<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
            margin: 0 auto;
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
            width: 100%;
            padding: 10px;
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
    </style>
</head>
<body>
<div class="container">
    <form action="<c:url value="/addproduct"/>" method="post">
        <div class="form-group">
            <label for="title">Chủ đề trung gian (*)</label>
            <input type="text" id="title" name="title" required>
        </div>
        <div class="form-group">
            <label for="price">Giá tiền (*)</label>
            <input type="text" id="price" name="price" required>
        </div>
        <div class="form-group">
            <label for="payer">Bên chịu phí trung gian (*)</label>
            <select id="payer" name="payer" required>
                <option value="">Chọn bên chịu phí</option>
                <option value="seller">Bên bán</option>
                <option value="buyer">Bên mua</option>
            </select>
        </div>
        <div class="form-group">
            <label for="description">Mô tả (*)</label>
            <textarea id="editor" name="description" required></textarea>
            <small>Càng chi tiết về sản phẩm càng tốt vì đây sẽ là cơ sở pháp lý giải quyết khiếu nại nếu có sau này</small>
        </div>
        <div class="form-group">
            <label for="contact">Phương thức liên hệ</label>
            <input type="text" id="contact" name="contact">
        </div>
        <div class="form-group">
            <label for="hidden-content">Nội dung ẩn (*)</label>
            <textarea id="hidden-editor" name="hidden-content" class="hidden-content" required></textarea>
            <small>Các chi tiết nội dung ẩn mà bạn muốn bổ sung</small>
        </div>
        <div class="form-group">
            <label>
                <input type="checkbox" id="public" name="public" checked>
                Hiện công khai (*)
            </label>
        </div>
        <button type="submit">Gửi</button>
    </form>
</div>

<script>
    // Gọi CKEditor trên textarea với ID là 'editor' và 'hidden-editor'
    CKEDITOR.replace('editor');
    CKEDITOR.replace('hidden-editor');
</script>
</body>
</html>
