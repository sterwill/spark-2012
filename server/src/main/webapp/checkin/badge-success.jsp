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
</head>
<body>
	<div
		style="font-size: smaller; background: #e0ffe0; padding: .25em; border: 1px solid #779977;">
		ID <strong>${checkin.user.id}</strong> checked into <strong>${checkin.location.name}</strong>
		at <strong><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss"
				value="${checkin.time}" /></strong>
	</div>
	<p style="font-size: larger;">${instructions}</p>
</body>
</html>
