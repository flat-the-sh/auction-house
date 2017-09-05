<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="/customtags" %>

<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="/localization/message" var="msg"/>

<fmt:message bundle="${msg}" key="menu.bids" var="bids"/>

<fmt:message bundle="${msg}" key="message.noBidsYet" var="noBidsYet"/>

<fmt:message bundle="${msg}" key="form.addButton" var="addButton"/>

<fmt:message bundle="${msg}" key="form.item" var="item"/>
<fmt:message bundle="${msg}" key="item.actualPrice" var="actualPrice"/>
<fmt:message bundle="${msg}" key="form.yourBid" var="yourBid"/>

<c:if test="${requestScope.bidItemMap == null}">
    <jsp:forward page="${pageContext.request.contextPath}/controller">
        <jsp:param name="command" value="load-bids"/>
    </jsp:forward>
</c:if>

<html>

<%@ include file="/jsp/jspf/head.jsp" %>

<body>

<%@ include file="/jsp/jspf/header.jsp" %>

<main>

    <%@ include file="/jsp/jspf/sidebar.jsp" %>

    <div class="w3-main main-left-margin">

        <div class="content">

            <div class="w3-row-padding w3-margin-top">

                <div class="w3-col s6 w3-margin-top">
                    <div class="middle-title uppercase">
                        ${bids}
                    </div>
                </div>

            </div>

            <div class="w3-row-padding">

                <c:choose>
                    <c:when test="${empty requestScope.bidItemMap}">
                        <p>${noBidsYet}</p>
                    </c:when>
                    <c:otherwise>
                        <div class="pro-margin-bottom">

                            <div class="w3-responsive">
                                <table class="w3-table w3-bordered">
                                    <thead>
                                    <tr class="pro-green">
                                        <th>
                                                ${item}
                                        </th>
                                        <th>
                                                ${actualPrice}
                                        </th>
                                        <th>
                                                ${yourBid}
                                        </th>
                                    </tr>
                                    </thead>

                                    <c:forEach var="entry" items="${requestScope.bidItemMap}">
                                        <tr>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/controller">
                                                    <input type="hidden" name="command" value="load-item"/>
                                                    <input type="hidden" name="itemId"
                                                           value="${entry.value.id}"/>
                                                    <button class="link-button">
                                                            ${entry.value.name}
                                                    </button>
                                                </form>
                                            </td>
                                            <td>
                                                <div class="text-on-color money">
                                                        ${entry.value.actualPrice}
                                                </div>
                                            </td>
                                            <td>
                                                <div class="text-on-color money">
                                                        ${entry.key.bidValue}
                                                </div>
                                            </td>
                                        </tr>

                                    </c:forEach>

                                </table>
                            </div>
                        </div>

                        <div class="page-bar w3-center">
                            <div id="pageBar" class="w3-bar w3-small w3-margin-top" data-command="load-bids"
                                 data-page="${requestScope.page}" data-pages="${sessionScope.pages}">
                                <a id="prevLink" class="w3-button">&laquo;</a>

                                <a id="nextLink" class="w3-button">&raquo;</a>
                            </div>
                        </div>

                    </c:otherwise>
                </c:choose>

            </div>

        </div>

    </div>

</main>

<%@ include file="/jsp/jspf/footer.jsp" %>

<script src="${pageContext.request.contextPath}/js/pagination.js"></script>

</body>

</html>