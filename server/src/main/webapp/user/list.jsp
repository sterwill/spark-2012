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
		<a href="<c:url value='user/create' />">Create User</a>
	<p>
	<table>
		<tr>
			<th>id</th>
			<th>email</th>
			<th>full name</th>
			<th>actions</th>
		</tr>
		<c:forEach var="user" items="${userList}">
			<tr>
				<td><a href="${root}user/edit/${user.id}">${user.id}</a></td>
				<td>${user.email}</td>
				<td><c:out value="${user.fullName}" /></td>
				<td><a href="${root}user/qr/${user.id}">Badge</a>
					<button
						onclick="ajaxDelete('/api/user/${user.id}', '${user.fullName}')">delete</button></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
