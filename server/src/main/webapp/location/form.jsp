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

	<form:form method="POST" modelAttribute="location">
		<form:hidden path="id" />
		<fieldset>
			<table>
				<tr>
					<td>name:</td>
					<td><form:input path="name" /> <form:errors path="name" /></td>
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
