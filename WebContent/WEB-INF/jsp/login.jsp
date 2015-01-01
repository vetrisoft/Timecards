<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
  <link rel="stylesheet" href="./css/timecard.css">
<title>Login Page</title>
</head>
<body onload='document.f.j_username.focus();'>

 <div class="login" id="loginForm">
	<h3>Nisum Timecards Login</h3>

	<c:if test="${not empty error}">
		<div class="errorblock">
			Your login attempt was not successful, try again.<br /> Cause:
			${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>
	
	<form name='login' action="<c:url value='j_spring_security_check' />"
		method='POST'>
		<table border="0" width="50%" height="50%">
			<tr>
				<td>Username:</td>
				<td><input type='text' name='j_username' value='admin'>
				</td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type='password' name='j_password' value="nisum$123" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;
				</td>
				<td colspan='2'>
					<input name="submit" type="submit" value="login" />
					<input name="reset" type="reset"   value="Reset" />
				</td>				
			</tr>
		</table>

	</form>
	</div>
</body>
</html>