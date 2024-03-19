<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 3/19/2024
  Time: 6:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal" tabindex="-1" id="reportProductModal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Tạo khiếu nại!</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form method="post" action="<c:url value="/report"/>" id="report-data-form">
                    <input type="hidden" name="pid" value="${requestScope.VAR_ORDER.productId}">

                    <div class="form-group">
                        <label for="title">Tiêu đề khiếu nại (*):</label>
                        <input type="text" class="form-control" id="title" name="r_title" required>
                    </div>

                    <div class="form-group">
                        <label for="report-description">Bằng chứng khiếu nại (*):</label>
                        <textarea id="report-description" name="r_content"></textarea>
                        <small>Miêu tả rõ bằng hình ảnh, nếu có video vui lòng thêm link vào khiếu nại!</small>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="report-modal-ignore" type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                <button id="report-modal-confirm" type="button" class="btn btn-primary sendwork">Đồng ý</button>
            </div>
        </div>
    </div>
</div>
