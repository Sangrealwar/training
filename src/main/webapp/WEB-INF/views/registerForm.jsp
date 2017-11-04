<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page language="java" contentType="text/view; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/view; charset=UTF-8">
		<title>用户注册</title>
		<link rel="stylesheet" type="text/css"
			href="<c:url value="/resources/style.css" />" />
	</head>
	<body>
		<h1>
			用户注册
		</h1>
		<form method="Post">
			<label th:class="${#fields.hasErrors('firstName')}? 'error'">
				姓:
			</label>
			<input type="text" th:field="*{firstName}"
				th:class="${#fields.hasErrors('firstName')}? 'error'" />
			<br />
			名：
			<input tyoe="text" name="lastName" />
			<br />
			用户名：
			<input tyoe="text" name="username" />
			<br />
			密码：
			<input tyoe="password" name="password" />
			<br />

			<input type="submit" value="注册" />
		</form>
	</body>
</html>