<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="/customtags" %>

<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="/localization/message" var="msg"/>

<fmt:message bundle="${msg}" key="label.projectTitle" var="projectTitle"/>
<fmt:message bundle="${msg}" key="item.settings" var="itemSettings"/>
<fmt:message bundle="${msg}" key="form.itemTitle" var="itemTitle"/>
<fmt:message bundle="${msg}" key="form.itemTitleRule" var="itemTitleRule"/>
<fmt:message bundle="${msg}" key="form.description" var="description"/>
<fmt:message bundle="${msg}" key="form.descriptionRule" var="descriptionRule"/>
<fmt:message bundle="${msg}" key="form.startPrice" var="startPrice"/>
<fmt:message bundle="${msg}" key="form.blitzPrice" var="blitzPrice"/>
<fmt:message bundle="${msg}" key="form.priceRule" var="priceRule"/>
<fmt:message bundle="${msg}" key="form.startDate" var="startDate"/>
<fmt:message bundle="${msg}" key="form.startDateRule" var="startDateRule"/>
<fmt:message bundle="${msg}" key="form.closeDate" var="closeDate"/>
<fmt:message bundle="${msg}" key="form.closeDateRule" var="closeDateRule"/>
<fmt:message bundle="${msg}" key="item.update" var="updateItem"/>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${account}</title>
    <link rel="icon" href="${pageContext.request.contextPath}/img/ic_gavel_black.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/w3.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>

<%@ include file="/jsp/jspf/header.jsp" %>

<main>

    <%@ include file="/jsp/jspf/sidebar.jsp" %>

    <div class="w3-main main-left-margin">

        <div class="content">

            <div class="w3-container w3-margin-top w3-margin-bottom middle-title uppercase">
                ${itemSettings}
            </div>

            <form action="${pageContext.request.contextPath}/controller" method="post"
                  class="w3-border-bottom">
                <input type="hidden" name="command" value="update-item"/>

                <div class="w3-row-padding bottom-padding">
                    <div class="w3-col m6 w3-container bottom-padding">
                        <label><b>${itemTitle}</b></label>
                        <input class="w3-input" value="${sessionScope.item.name}" name="title" required
                               pattern="[A-Za-zА-Яа-яЁё0-9-_ ]{2,40}"
                               title="${itemTitleRule}"/>
                    </div>
                </div>

                <div class="w3-row-padding bottom-padding">
                    <div class="w3-col m6 w3-container">
                        <label><b>${description}</b></label>
                        <textarea class="w3-input w3-border" name="description" required
                                  title="${descriptionRule}">${sessionScope.item.description}</textarea>
                    </div>
                </div>

                <div class="w3-row-padding bottom-padding">
                    <div class="w3-col m5">
                        <label><b>${startPrice}</b></label>
                        <input name="start-price" class="w3-input" value="${sessionScope.item.startPrice}" type="number"
                               step="0.001" min="0.000"
                               max="99999999999999999999.999" required
                               title="${priceRule}"/>
                    </div>
                    <div class="w3-col m5">
                        <label><b>${blitzPrice}</b></label>
                        <input name="blitz-price" class="w3-input" value="${sessionScope.item.blitzPrice}" type="number"
                               step="0.001" min="0.000"
                               max="99999999999999999999.999" required
                               title="${priceRule}"/>
                    </div>
                </div>

                <div class="w3-row-padding bottom-padding">
                    <div class="w3-col m5">
                        <label><b>${startDate}</b></label>
                        <input id="start-date" name="start-date" class="w3-input" value="${sessionScope.item.startDate}"
                               type="date" required
                               title="${startDateRule}"/>
                    </div>
                    <div class="w3-col m5">
                        <label><b>${closeDate}</b></label>
                        <input id="close-date" name="close-date" class="w3-input" value="${sessionScope.item.closeDate}"
                               type="date" required
                               title="${closeDateRule}"/>
                    </div>
                </div>

                <div class="w3-container w3-margin">
                    <button class="w3-button pro-green w3-margin-bottom">${updateItem}</button>
                </div>

            </form>
        </div>

    </div>


</main>

<%@ include file="/jsp/jspf/message.jsp" %>

<%@ include file="/jsp/jspf/footer.jsp" %>

</body>

</html>