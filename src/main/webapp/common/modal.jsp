<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 2/19/2024
  Time: 2:47 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal" tabindex="-1" id="myModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p id="modal-body"></p>
            </div>
            <div class="modal-footer">
                <button id="modal-ignore" type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                <button id="modal-confirm" type="button" class="btn btn-primary sendwork">Đồng ý</button>
            </div>
        </div>
    </div>
</div>
