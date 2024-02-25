<%--
  Created by IntelliJ IDEA.
  User: ManhLD
  Date: 26-02-2024
  Time: 00:25
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="f" uri="tradesys.functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/css/notification.css"/> ">
    <title>Notifications</title>
</head>
<ul>
    <h2>Thông báo</h2>
    <c:set value="${requestScope.VIEW_PAGING.paging}" var="paging"/>
    <c:set value="${requestScope.VIEW_PAGING.items}" var="notification"/>


    <ul class="list-group">
        <c:forEach items="${notification}" var="notification">
        <a href="#" class="list-group-item list-group-item-action >
        <div class="d-flex w-100 justify-content-between">
        <h5 class="mb-1">${notification.title}</h5>
        <small>${notification.status}</small>
    </ul>
    <p class="mb-1">${notification.description}</p>
    <small>${notification.create_date}</small>
    </a>
    </c:forEach>
</ul>
<div class="pagination">
    <c:if test="${paging.totalPage > 0}">
        <nav aria-label="Page navigation example">
            <ul>
                <li class="page-item <c:if test="${paging.currentPage == paging.startPage}">disabled</c:if>">
                    <c:set value="${f:pagingUrlGenerate(loop.index, paging.pageSize, paging.pageRangeOutput)}"
                           var="previous"/>
                    <a class="page-link"
                       href="<c:url value="/notification${previous}"/>">
                        Về trang trước
                    </a>
                </li>

                <c:forEach begin="1" end="${paging.totalPage}" varStatus="loop">
                    <li class="page-item <c:if test="${loop.index == paging.currentPage}">active</c:if>">
                        <c:set value="${f:pagingUrlGenerate(loop.index, paging.pageSize, paging.pageRangeOutput}"
                               var="current"/>
                        <a class="page-link"
                           href="<c:url value="/notification${current}"/>">${loop.index}</a>
                    </li>
                </c:forEach>

                <li class="page-item <c:if test="${paging.currentPage == paging.endPage}">disabled</c:if>">
                    <c:set value="${f:pagingUrlGenerate(paging.currentPage+1, paging.pageSize, paging.pageRangeOutput}"
                           var="next"/>
                    <a class="page-link"
                       href="<c:url value="/notification${next}"/>">
                        Đến trang tiếp
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>
</div>
</body>
</html>



