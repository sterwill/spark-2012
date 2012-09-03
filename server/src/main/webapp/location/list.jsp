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
	<p>
		<c:choose>
			<c:when test="${currentLocation != null}">
			You have a cookie that identifes your current location as <strong>${currentLocation.name}</strong> (<a
					href="location/uncookie/${currentLocation.id}">delete this cookie</a>).
			</c:when>
			<c:otherwise>You do not have a location cookie set.</c:otherwise>
		</c:choose>
	</p>
	<p>
		<a href="<c:url value='/web/location/create' />">Create Location</a>
	<p>
	<table>
		<tr>
			<th>id</th>
			<th>name</th>
		</tr>
		<c:forEach var="location" items="${locationList}">
			<tr>
				<td><c:out value="${location.id}" /></td>
				<td><a href="${root}web/location/edit/${location.id}">${location.name}</a></td>
				<td><button
						onclick="ajaxDelete('/api/location/${location.id}', '${location.name}')">delete</button>
					<button onclick="window.location='location/cookie/${location.id}'">set
						cookie</button></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
