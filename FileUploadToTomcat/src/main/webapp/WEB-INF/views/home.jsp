<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>
<script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
</head>
<body>
	<h1>Hello world!</h1>
	<div align="center">
		<div style="text-align: center; padding: 30px; border: 10px solid black; width: 1000px;">
			<div class="res_btn">
				<div class="status"><c:out value="${requestScope.status}" /></div>
				<form:form action="/myapp/saveNewRoom" commandName="fileDetails" method="post"
					enctype="multipart/form-data">

					<table style="padding: 30px">
						
						<tr>
							<td><label>Image:</label> <form:input type="file"
									path="file" id="file" style="height : 30px" required="required" /></td>
						</tr>
			

					</table>


					<br>
					<input type="submit" id="imageSubmit" value="AddRoom" class="btn btn-success" />

				</form:form>
			</div>
		</div>
	</div>

</body>
</html>
