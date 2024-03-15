<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 페이지</title>
<link rel="icon" href="<%=request.getContextPath()%>/image/home.ico">
<script src="<%=request.getContextPath()%>/js/jquery-3.7.1.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/login.css">
<script>
$(function () {
	$(".join").click(function() {
		location.href = "join.net";
	});
	
	const id = '${cookie_id}';
	if(id){
		$("#id").val(id).css('font-weight', 'bold');
		$("#remember").prop('checked', true);
	}
})
</script>
</head>
<body>
	<div class="container">
		<form name="loginform" action="loginProcess.net" method="post">
			<h1>로그인</h1>
			<hr>
			<b>아이디</b>
			<input type="text" name="id" placeholder="Enter id" id="id" required>
			
			<b>Password</b>
			<input type="password" name="pass" placeholder="Enter password" required>
			<input type="checkbox" id="remember" name="remember" value="store">
			<span>remember</span>
			
			<div class="clearfix">
				<button type="submit" class="submitbtn">로그인</button>
				<button type="button" class="join">회원가입</button>
			</div>
		</form>
	</div>	
</body>
</html>