<%--
  Created by IntelliJ IDEA.
  User: Theodore
  Date: 2020/6/27
  Time: 19:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Document</title>
</head>
<body>
    <h1>系统主页V1.O</h1>
    <a href="${pageContext.request.contextPath}/user/logout">退出</a>
    <ul>
        <li><a href="${pageContext.request.contextPath}/permission/1">1.jsp</a></li>
        <li><a href="${pageContext.request.contextPath}/permission/2">2.jsp</a></li>
        <li><a href="${pageContext.request.contextPath}/permission/3">3.jsp</a></li>
    </ul>
</body>
</html>
