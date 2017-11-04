<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/view; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/view; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"
type ="text/css"
href="<c:url value="/resources/style.css" />" />
</head>
<body>
<h1>欢迎光临</h1>
<a href="<c:url value="/spittles" />">Spittles</a> |
<a href="<c:url value="/spittles/register" />" >Register</a>
</body>
</html>