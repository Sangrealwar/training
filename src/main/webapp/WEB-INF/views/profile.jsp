<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/view; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/view; charset=UTF-8">
<title>用户信息</title>
</head>
<body>
username:<c:out value="${spitter.username}" /> <br/>
firesName:<c:out value="${spitter.firstName}" /> <br/>
lastName:<c:out value="${spitter.lastName}" /> <br/>
</body>
</html>