<%--
  Created by IntelliJ IDEA.
  User: hongd
  Date: 3/14/2024
  Time: 2:46 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="tradesys.functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <jsp:include page="/common/common-css.jsp"/>
        <title>Lịch sử báo cáo</title>
    </head>
    <body>
        <jsp:include page="/common/header.jsp"/>

        <div class="container">
            <div class="row">
                <div class="col-md-12 mt-5 mb-3">
                    <h2>Lịch sử báo cáo</h2>
                </div>

                <div class="col-md-12 p-0">
                    <form action="<c:url value="/report"/>" method="get">
                        <div class="d-flex justify-content-between w-100">
                            <div class="col-md-6">
                                <div class="input-group mb-3 d-flex flex-column">
                                    <div>
                                        <label for="f_title">Tiêu đề:</label>
                                    </div>
                                    <input type="text" class="form-control w-100"
                                           value="<c:out value="${requestScope.FILTER_TITLE}"/>"
                                           name="f_title"
                                           id="f_title"
                                           aria-describedby="basic-addon3">
                                </div>

                                <div class="input-group mb-3 d-flex flex-column">
                                    <label for="f_status">Trạng thái:</label>
                                    <select class="form-control w-100" id="f_status" name="f_status">
                                        <c:set value="${requestScope.FILTER_STATUS}" var="filter"/>
                                        <c:set value="0" var="ALL"/>
                                        <c:set value="1" var="REPORT_BUYER_CREATED"/>
                                        <c:set value="2" var="REPORT_BUYER_ABORT"/>
                                        <c:set value="3" var="REPORT_SELLER_ACCEPT"/>
                                        <c:set value="4" var="REPORT_SELLER_DENIED"/>
                                        <c:set value="5" var="REPORT_ADMIN_REQUEST"/>
                                        <c:set value="6" var="REPORT_ADMIN_CHECKING"/>
                                        <c:set value="7" var="REPORT_ADMIN_RESPONSE_BUYER_RIGHT"/>
                                        <c:set value="8" var="REPORT_ADMIN_RESPONSE_BUYER_WRONG"/>
                                        <c:set value="9" var="REPORT_BUYER_ACCEPT_SELLER_RESPONSE"/>

                                        <option value="0" <c:if test="${filter.equals(ALL)}">selected</c:if>>
                                            Tất cả
                                        </option>
                                        <option value="1" <c:if test="${filter.equals(REPORT_BUYER_CREATED)}">selected</c:if>>
                                            Đang chờ bên bán phản hồi
                                        </option>
                                        <option value="2" <c:if test="${filter.equals(REPORT_BUYER_ABORT)}">selected</c:if>>
                                            Đã hủy
                                        </option>
                                        <option value="3"
                                                <c:if test="${filter.equals(REPORT_SELLER_ACCEPT)}">selected</c:if>>
                                            Bên bán đồng ý với khiếu nại
                                        </option>
                                        <option value="4"
                                                <c:if test="${filter.equals(REPORT_SELLER_DENIED)}">selected</c:if>>
                                            Bên bán không đồng ý với khiếu nại
                                        </option>
                                        <option value="5" <c:if test="${filter.equals(REPORT_ADMIN_REQUEST)}">selected</c:if>>
                                            Bên mua yêu cầu admin kiểm tra
                                        </option>
                                        <option value="6" <c:if test="${filter.equals(REPORT_ADMIN_CHECKING)}">selected</c:if>>
                                            Admin đang kiểm tra
                                        </option>
                                        <option value="7" <c:if test="${filter.equals(REPORT_ADMIN_RESPONSE_BUYER_RIGHT)}">selected</c:if>>
                                            Đã xử lý (Báo cáo đúng)
                                        </option>
                                        <option value="8" <c:if test="${filter.equals(REPORT_ADMIN_RESPONSE_BUYER_WRONG)}">selected</c:if>>
                                            Đã xử lý (Báo cáo sai)
                                        </option>
                                        <option value="9" <c:if test="${filter.equals(REPORT_BUYER_ACCEPT_SELLER_RESPONSE)}">selected</c:if>>
                                            Người mua đồng ý với phản hồi của người bán
                                        </option>
                                    </select>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="input-group mb-3 d-flex flex-column">
                                    <label for="f_start">Từ ngày:</label>
                                    <input type="date" class="form-control w-100"
                                           id="f_start"
                                           name="f_start"
                                           value="<c:out value="${requestScope.FILTER_STARTDATE}"/>"
                                           aria-describedby="basic-addon3">
                                </div>
                                <div class="input-group mb-3 d-flex flex-column">
                                    <label for="f_end">Đến ngày:</label>
                                    <input type="date" class="form-control w-100"
                                           id="f_end"
                                           name="f_end"
                                           value="<c:out value="${requestScope.FILTER_ENDDATE}"/>"
                                           aria-describedby="basic-addon3">
                                </div>
                            </div>
                        </div>

                        <div class="col-md-12 d-flex justify-content-center">
                            <button type="submit" class="btn btn-primary">Tìm</button>
                        </div>
                    </form>
                </div>

                <div class="col-md-12">
                    <c:set value="${requestScope.VIEW_PAGING.paging}" var="paging"/>
                    <c:set value="${requestScope.VIEW_PAGING.items}" var="reports"/>
                    <table class="table">
                        <thead class="thead-dark">
                            <tr>
                                <th>#</th>
                                <th>Tiêu đề</th>
                                <th>Ngày tạo</th>
                                <th>Người tạo</th>
                                <th>Loại</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <c:choose>
                            <c:when test="${paging.totalItem <= 0}">
                                <tbody>
                                    <tr>
                                        <td colspan="7">
                                            <div class="d-flex justify-content-center">
                                                <h2>Không có kết quả!</h2>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </c:when>
                            <c:otherwise>
                                <tbody>
                                    <c:forEach items="${reports}" var="report" begin="0"
                                               end="${paging.totalItem}"
                                               varStatus="loop">
                                        <tr class="table-active">
                                            <th scope="row">${loop.index + 1}</th>
                                            <td class="text-break"><c:out value="${report.title}"/></td>
                                            <td>
                                                <c:out value="${f:formatLocalDateTime(report.createAt, 'dd/MM/yyyy hh:mm:ss')}"/>
                                            </td>
                                            <td><c:out value="${report.createBy}"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${report.userId eq sessionScope.SESSION_USERID}">
                                                        Báo cáo đơn trung gian
                                                    </c:when>

                                                    <c:otherwise>
                                                        Đơn trung gian bị báo cáo
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${report.status eq 1}">
                                                        Đang chờ bên bán phản hồi
                                                    </c:when>
                                                    <c:when test="${report.status eq 2}">
                                                        Đã hủy
                                                    </c:when>
                                                    <c:when test="${report.status eq 3}">
                                                        Bên bán đồng ý với khiếu nại
                                                    </c:when>
                                                    <c:when test="${report.status eq 4}">
                                                        Bên bán không đồng ý với khiếu nại
                                                    </c:when>
                                                    <c:when test="${report.status eq 5}">
                                                        Bên mua yêu cầu admin kiểm tra
                                                    </c:when>
                                                    <c:when test="${report.status eq 6}">
                                                        Admin đang kiểm tra
                                                    </c:when>
                                                    <c:when test="${report.status eq 7}">
                                                        Đã xử lý (Báo cáo đúng)
                                                    </c:when>
                                                    <c:when test="${report.status eq 8}">
                                                        Đã xử lý (Báo cáo sai)
                                                    </c:when>
                                                    <c:when test="${report.status eq 9}">
                                                        Người mua đồng ý với phản hồi của người bán
                                                    </c:when>
                                                    <c:otherwise>
                                                        Không rõ
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="<c:url value="/report/detail?id=${report.id}" />">
                                                    Xem chi tiết
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </c:otherwise>
                        </c:choose>

                    </table>
                </div>

                <c:if test="${paging.totalPage > 0}">
                    <div class="col-md-12">
                        <nav aria-label="Page navigation example">
                            <ul class="pagination">
                                <li class="page-item <c:if test="${paging.currentPage == paging.startPage}">disabled</c:if>">
                                    <c:set value="${f:pagingUrlGenerateUserReport(paging.currentPage-1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_TITLE, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE,requestScope.FILTER_ENDDATE)}"
                                           var="previous"/>
                                    <a class="page-link"
                                       href="<c:url value="/report${previous}"/>">
                                        Về trang trước
                                    </a>
                                </li>

                                <c:forEach begin="1" end="${paging.totalPage}" varStatus="loop">
                                    <li class="page-item <c:if test="${loop.index == paging.currentPage}">active</c:if>">
                                        <c:set value="${f:pagingUrlGenerateUserReport(loop.index, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_TITLE, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE,requestScope.FILTER_ENDDATE)}"
                                               var="current"/>
                                        <a class="page-link"
                                           href="<c:url value="/report${current}"/>">${loop.index}</a>
                                    </li>
                                </c:forEach>

                                <li class="page-item <c:if test="${paging.currentPage == paging.endPage}">disabled</c:if>">
                                    <c:set value="${f:pagingUrlGenerateUserReport(paging.currentPage+1, paging.pageSize, paging.pageRangeOutput, requestScope.FILTER_TITLE, requestScope.FILTER_STATUS, requestScope.FILTER_STARTDATE,requestScope.FILTER_ENDDATE)}"
                                           var="next"/>
                                    <a class="page-link"
                                       href="<c:url value="/report${next}"/>">
                                        Đến trang tiếp
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </c:if>
            </div>
        </div>

    </body>
    <jsp:include page="/common/common-js.jsp"/>
</html>