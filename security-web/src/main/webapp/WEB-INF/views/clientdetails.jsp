<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>client_details</title>
	<meta charset="UTF-8"/>
<%--     <link href="${ctx}/bootstrap.min.css" rel="stylesheet"/> --%>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    
    <style>
        .list-group li:hover {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
<a href="./">Home</a>

<div class="row">
    <div class="col-md-10">
        <h3>client_details</h3>
    </div>
    <div class="col-md-2">
        <div class="pull-right">
            <a href="${ctx}/client/registerClient" class="btn btn-success btn-sm">注册client</a>
        </div>
    </div>
</div>

<hr/>

<div>
    <ul class="list-group">
        <c:forEach items="${clientDetailsList}" var="cli">
            <li class="list-group-item">
                <%-- <div class="pull-right">
                    <c:if test="${not cli.archived}">
                        <a href="test_client/${cli.clientId}">test</a>
                        <a href="archive_client/${cli.clientId}" class="text-danger"
                           onclick="return confirm('Are you sure archive \'${cli.clientId}\'?')">archive</a>
                    </c:if>
                    <c:if test="${cli.archived}"><strong class="text-muted">Archived</strong></c:if>
                </div> --%>
                <h3 class="list-group-item-heading">
                        ${cli.clientId}
                    <small>${cli.authorizedGrantTypes}</small>
                </h3>

                <div class="list-group-item-text text-muted">
                    client_id: <span class="text-danger">${cli.clientId}</span>&nbsp;
                    client_secret: <span class="text-primary">${cli.clientSecret}</span>&nbsp;
                    <br/>
                    authorized_grant_types: <span class="text-primary">${cli.authorizedGrantTypes}</span>&nbsp;
                    resource_ids: <span class="text-primary">${cli.resourceIds}</span>&nbsp;
                    <br/>
                    scope: <span class="text-primary">${cli.scope}</span>&nbsp;
                    web_server_redirect_uri: <span class="text-primary">${cli.webServerRedirectUri}</span>&nbsp;
                    <br/>
                    authorities: <span class="text-primary">${cli.authorities}</span>&nbsp;
                    access_token_validity: <span class="text-primary">${cli.accessTokenValidity}</span>&nbsp;
                    refresh_token_validity: <span class="text-primary">${cli.refreshTokenValidity}</span>&nbsp;
                    <br/>
                    create_date: <span class="text-primary">${cli.createdDate}</span>&nbsp;
                    <%-- archived: <strong class="${cli.archived?'text-warning':'text-primary'}">${cli.archived}</strong>&nbsp;
                    trusted: <span class="text-primary">${cli.trusted}</span>&nbsp;
                    additional_information: <span class="text-primary">${cli.additionalInformation}</span>&nbsp; --%>
                </div>
            </li>
        </c:forEach>

    </ul>
    <p class="help-block">
        每一个item对应<code>oauth_client_details</code>表中的一条数据; 共<strong>${fn:length(clientDetailsList)}</strong>条数据.
        <br/>
        对spring-oauth-server数据库表的详细说明请访问
        <a href="http://andaily.com/spring-oauth-server/db_table_description.html" target="_blank">http://andaily.com/spring-oauth-server/db_table_description.html</a>
        (或访问项目others目录的db_table_description.html文件)
    </p>
</div>
</body>
</html>