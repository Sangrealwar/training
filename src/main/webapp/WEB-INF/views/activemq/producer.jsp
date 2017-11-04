<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JMS-生产者</title>
</head>
<body>
<h1>JMS-生产者</h1>
<form action="onsend" method="post">

    消息内容:<textarea name="message">${time }</textarea>

    <input type="submit" value="提交" />
</form>
<form action="topic_onsend" method="post">

    主题内容:<textarea name="message">${time }</textarea>

    <input type="submit" value="发布" />
</form>
<h2><a href="welcome">返回主页</a></h2>
</body>
</html>