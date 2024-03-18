<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="header.jsp" />
	<script src="${pageContext.request.contextPath}/js/writeform.js"></script>
	<style>
	h1{font-size: 1.5rem; text-align: center; color:#1a92b9}
	.container{width:60%}
	label{font-weight: bold}
	#upfile{display:none}
	img{width:20px}
	</style>
</head>
<body>
	<div class="container">
		<form action="BoardAddAction.bo" method="post" enctype="multipart/form-data" 
			  name="boardform">
			<h1>MVC 게시판 - write 페이지</h1>
			<div class="form-group">
				<label for="board_name">글쓴이</label>
				<input name="board_name" id="board_name" value="${id}" readOnly 
						type="text" class="form-control"
						placeholder="Enter board_name">
			</div>
		</form>
	</div>
</body>
</html>