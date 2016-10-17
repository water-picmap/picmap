<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>用户列表</title>
</head>
<body>
<a href="add">添加</a>${loginUser.nickname}
<br>
	<c:forEach items="${users}" var="um">
		${um.value.id} 
		${um.value.username}
		<a href="${um.value.username }">${um.value.nickname}</a>
		${um.value.password}
		${um.value.email} <a href="${um.value.username }/update" >修改</a> 
		<a href="${um.value.id }/delete">删除</a><br/>
	</c:forEach>
</body>
</html>