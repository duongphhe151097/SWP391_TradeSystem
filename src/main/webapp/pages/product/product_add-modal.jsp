<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 3/19/2024
  Time: 3:22 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal" tabindex="-1" id="addProductModal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Tạo đơn trung gian!</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body">
                <form method="post" action="<c:url value="/product/sale"/>" id="product-data-form">
                    <div class="form-group">
                        <label for="title">Chủ đề trung gian (*)</label>
                        <input type="text" class="form-control" id="title" name="title" required>
                    </div>
                    <div class="form-group">
                        <label for="price">Giá tiền (*)</label>
                        <input type="text" class="form-control" id="price" name="price" required oninput="this.value = this.value.replace(/[^0-9]/g, '')" inputmode="numeric">
                    </div>

                    <div class="form-group">
                        <label for="role">Bên chịu phí trung gian (*)</label>
                        <select id="role" class="form-control" name="role" required>
                            <option value="buyer">Người mua</option>
                            <option value="seller">Người bán</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="product-description">Mô tả (*)</label>
                        <textarea id="product-description" name="description"></textarea>
                        <small>Càng chi tiết về sản phẩm càng tốt vì đây sẽ là cơ sở pháp lý giải quyết khiếu nại nếu có sau này</small>
                    </div>
                    <div class="form-group">
                        <label for="contact">Phương thức liên hệ</label>
                        <input type="text" class="form-control" id="contact" name="contact">
                    </div>
                    <div class="form-group">
                        <label for="product-secret">Nội dung ẩn (*)</label>
                        <textarea id="product-secret" name="secret" required></textarea>
                        <small>Các chi tiết nội dung ẩn mà bạn muốn bổ sung</small>
                    </div>
                    <div class="form-group">
                        <label>
                            Hiện công khai (*)
                        </label>
                        <input type="checkbox" id="public" name="public" checked>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button id="product-modal-ignore" type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                <button id="product-modal-confirm" type="button" class="btn btn-primary sendwork">Đồng ý</button>
            </div>
        </div>
    </div>
</div>
