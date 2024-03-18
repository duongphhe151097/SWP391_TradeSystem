<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chi tiết sản phẩm</title>
        <!-- CKEditor CDN -->
        <jsp:include page="/common/common-css.jsp"/>
    </head>
    <body>
        <jsp:include page="/common/header.jsp"/>

        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="card mt-5">
                        <div class="card-header d-flex align-items-center">
                            <div>
                                <a href="<c:url value="/market"/>">
                                    <span class="material-symbols-outlined">
                                        arrow_back_ios
                                    </span>
                                </a>
                            </div>
                            <div>
                                <h4 class="text">Chi tiết sản phẩm</h4>
                            </div>
                        </div>

                        <div class="card-body">
                            <form action="<c:url value="/product/detail"/>" method="post">
                                <table class="table table-borderless">
                                    <thead>
                                        <tr>
                                            <th scope="col"></th>
                                            <th scope="col"></th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <tr>
                                            <input type="hidden" value="<c:out value="${product.id}"/>" name="id"/>
                                            <th scope="row">Mã trung gian (*):</th>
                                            <td><c:out value="${product.id}"/></td>
                                        </tr>

                                        <tr>
                                            <th scope="row">Chủ đề trung gian (*):</th>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${product.userId.equals(sessionScope.SESSION_USERID)}">
                                                        <input type="text" class="form-control w-100" id="title" name="title"
                                                               value="<c:out value="${product.title}"/>"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="${product.title}"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>

                                        <tr>
                                            <th scope="row">Trạng thái (*):</th>
                                            <td>
<%--                                                <c:choose>--%>
<%--                                                    <c:when test="${product.userId.equals(sessionScope.SESSION_USERID)}">--%>
<%--                                                        <input type="text" class="form-control w-100" id="status" name="status"--%>
<%--                                                               value="${product.status}" readonly>--%>
<%--                                                    </c:when>--%>
<%--                                                    <c:otherwise>--%>
<%--                                                        <c:choose>--%>
<%--                                                            <c:when test="${product.status eq 1}">--%>
<%--                                                                Có thể giao dịch--%>
<%--                                                            </c:when>--%>

<%--                                                            <c:otherwise>--%>
<%--                                                                Dừng giao dịch--%>
<%--                                                            </c:otherwise>--%>
<%--                                                        </c:choose>--%>
<%--                                                    </c:otherwise>--%>
<%--                                                </c:choose>--%>

                                                <c:choose>
                                                    <c:when test="${product.status eq 1}">
                                                        Có thể giao dịch
                                                    </c:when>

                                                    <c:otherwise>
                                                        Dừng giao dịch
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>

                                        <tr>
                                            <th scope="row">Giá tiền (*):</th>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${product.userId.equals(sessionScope.SESSION_USERID)}">
                                                        <input type="text" class="form-control w-100" id="price" name="price"
                                                               value="${product.price}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="${product.price}"/>
                                                    </c:otherwise>
                                                </c:choose>

                                            </td>
                                        </tr>

                                        <tr>
                                            <th scope="row">Mô tả (*):</th>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${product.userId.equals(sessionScope.SESSION_USERID)}">
                                                    <textarea id="description" class="form-control w-100"
                                                              name="description">${product.description}</textarea>

                                                        <small>
                                                            Càng chi tiết về sản phẩm càng tốt vì đây sẽ là cơ sở pháp lý giải quyết khiếu
                                                            nại nếu có sau này
                                                        </small>
                                                    </c:when>
                                                    <c:otherwise>
                                                    <textarea id="description" class="form-control w-100"
                                                              name="description" disabled><c:out value="${product.description}"/></textarea>

                                                        <small>
                                                            Càng chi tiết về sản phẩm càng tốt vì đây sẽ là cơ sở pháp lý giải quyết khiếu
                                                            nại nếu có sau này
                                                        </small>

                                                    </c:otherwise>
                                                </c:choose>

                                            </td>
                                        </tr>

                                        <tr>
                                            <th scope="row">Người Bán (*):</th>
                                            <td>
                                                <c:out value="${product.createBy}"/>
                                            </td>
                                        </tr>

                                        <tr>
                                            <th scope="row">Phương thức liên hệ:</th>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${product.userId.equals(sessionScope.SESSION_USERID)}">
                                                        <input type="text" class="form-control w-100" id="contact" name="contact"
                                                               value="${product.contact}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="${product.contact}"/>
                                                    </c:otherwise>
                                                </c:choose>

                                            </td>
                                        </tr>

                                        <tr>
                                            <th scope="row">Bên chịu phí:</th>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${product.userId.equals(sessionScope.SESSION_USERID)}">
                                                        <input type="checkbox" id="public" name="isSeller" checked>
                                                        Người bán chịu phí (*)
                                                    </c:when>

                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${product.isSeller() == true}">
                                                                Người bán chịu phí
                                                            </c:when>
                                                            <c:otherwise>
                                                                Người mua chịu phí
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>

                                        <tr>
                                            <th scope="row">
                                                Hiển thị:
                                            </th>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${product.userId.equals(sessionScope.SESSION_USERID)}">
                                                        <input type="checkbox" id="public" name="public" checked>
                                                        Hiện công khai (*)
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:if test="${product.isPublic() == true}">
                                                            <input type="checkbox" id="public" name="public" checked disabled>
                                                        </c:if>

                                                        <c:if test="${product.isPublic() == false}">
                                                            <input type="checkbox" id="public" name="public" checked disabled>
                                                        </c:if>
                                                        Hiện công khai (*)
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        <c:if test="${requestScope.VAR_CANVIEWSECRET}">
                                            <tr>
                                                <th scope="row">Nội dung ẩn (*):</th>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${product.userId.equals(sessionScope.SESSION_USERID)}">
                                                            <textarea id="secret" class="form-control w-100" name="secret"
                                                                      class="secret">${product.secret}</textarea>
                                                            <small>Các chi tiết nội dung ẩn mà bạn muốn bổ sung</small>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <textarea id="secret" class="form-control w-100" name="secret"
                                                                      class="secret" disabled>${product.secret}</textarea>
                                                            <small>Các chi tiết nội dung ẩn mà bạn muốn bổ sung</small>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:if>

                                        <c:if test="${product.isPublic() == false && product.userId.equals(sessionScope.SESSION_USERID)}">
                                            <tr>
                                                <th scope="row">Link trung gian:</th>
                                                <td>
                                                    <a href="<c:url value="/product/detail?id=${product.id}"/> ">${requestScope.VAR_SECRETURL}</a>
                                                </td>
                                            </tr>
                                        </c:if>

                                        <tr>
                                            <th scope="row">Thời gian tạo (*):</th>
                                            <td>
                                                <c:out value="${product.createAt}"/>
                                            </td>
                                        </tr>

                                        <tr>
                                            <th scope="row">Cập nhật lần cuối (*):</th>
                                            <td>
                                                <c:out value="${product.updateAt}"/>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>

                                <c:if test="${not empty resultMessage}">
                                    <div class="form-group">
                                        <p class="message">${resultMessage}</p>
                                    </div>
                                </c:if>

                                <c:if test="${not product.userId.equals(sessionScope.SESSION_USERID)}">
                                    <button type="submit" class="btn btn-success">Mua</button>
                                </c:if>

                                <c:if test="${product.userId.equals(sessionScope.SESSION_USERID)}">
                                    <button type="submit" class="btn btn-success">Cập nhật</button>
                                </c:if>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/common/modal.jsp"/>
        <jsp:include page="/common/toast.jsp"/>
        <jsp:include page="/common/common-js.jsp"/>
        <script src="https://cdn.ckeditor.com/4.16.2/standard/ckeditor.js"></script>

        <script>
            CKEDITOR.replace('description');
            CKEDITOR.replace('secret');
        </script>
    </body>
</html>