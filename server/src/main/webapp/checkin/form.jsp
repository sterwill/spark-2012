<!DOCTYPE html>
<%@ page import="org.tailfeather.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:url var="root" value="/" />
<html>
<head>
<meta charset="UTF-8" />
<title>The Tail Feather Group</title>
<link rel="stylesheet" type="text/css" href="${root}static/main.css" />
<script type="text/javascript" src="${root}static/jquery-1.8.1.js"></script>
</head>
<body>
	<p>
		<c:choose>
			<c:when test="${currentLocation != null}">
			You have a cookie that identifes your current location as <strong>${currentLocation.name}</strong> (<a
					href="/web/location/uncookie/${currentLocation.id}">delete this
					cookie</a>).
			</c:when>
			<c:otherwise>You do not have a location cookie set.  You need to <a
					href="/web/location">set a cookie</a> before you create checkins.</c:otherwise>
		</c:choose>
	</p>
	<form:form method="POST" modelAttribute="checkin">
		<form:hidden path="id" />
		<fieldset>
			<table>
				<tr>
					<td>User email:</td>
					<td><input name="userEmail" value="${userEmail}" /> <form:errors path="*" /></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" id="save" name="action" value="Save" />
					</td>
				</tr>
			</table>
		</fieldset>
	</form:form>
</body>
</html>
