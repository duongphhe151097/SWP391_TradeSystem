<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 3/20/2024
  Time: 1:02 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal" tabindex="-1" id="report-acp-seller">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Phản hồi cho bên mua!</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form method="post" action="<c:url value="/report"/>" id="report-data-form">
                    <div class="form-group">
                        <label for="modal-seller-response">Phản hồi cho người mua:</label>
                        <textarea id="modal-seller-response" ></textarea>
                        <small>Cung cấp lại thông tin ẩn của đơn trung gian!</small>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="report-acp-seller-modal-ignore" type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                <button id="report-acp-seller-modal-confirm" type="button" class="btn btn-primary sendwork">Đồng ý</button>
            </div>
        </div>
    </div>
</div>
