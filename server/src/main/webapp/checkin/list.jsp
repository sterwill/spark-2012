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
<script type="text/javascript" src="${root}static/crud.js"></script>
</head>
<body>
	<c:if test="${user != null}">
		<p>Showing only checkins for user <strong>${user.email}</strong></p>
	</c:if>
	<c:if test="${location != null}">
		<p>Showing only checkins for location <strong>${location.name}</strong></p>
	</c:if>

	<p>
		<a href="<c:url value='checkin/create' />">Create Checkin</a>
	<p>
	<table>
		<tr>
			<th>time</th>
			<th>location</th>
			<th>user</th>
		</tr>
		<c:forEach var="checkin" items="${checkinList}">
			<tr>
				<td><c:out value="${checkin.time}" /></td>
				<td><a
					href="${root}web/checkin?locationId=${checkin.location.id}">${checkin.location.name}</a></td>
				<td>${checkin.user.fullName} (<a href="${root}web/checkin?userEmail=${checkin.user.email}">${checkin.user.email}</a>)</td>
				<td><button
						onclick="ajaxDelete('/api/checkin/${checkin.id}', '${checkin.id}')">delete</button>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
