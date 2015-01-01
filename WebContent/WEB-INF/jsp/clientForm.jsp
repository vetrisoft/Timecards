<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>Nisum Technologies</title>
<link rel="stylesheet" href="/Timecards/css/timecard.css">
<style>
.container {
	background-color: #fff;
	width: 1100px;
	position: relative;
	margin: 0 auto;
	padding: 10px 0;
	border: 1px solid #A39A9A;
	border-top-right-radius: 10px;
	border-bottom-right-radius: 10px;
	border-bottom-left-radius: 10px;
	border-top-left-radius: 10px;
}
</style>
<script type="text/javascript" language="javascript" src="./js/employee.js"></script>
</head>
<body>
	<table width="100%" border="0" cellspacing="0" cellpadding="10">
		<tr>
			<td align="left">
				<table width="100%" cellspacing="0" cellpadding="10" class="frmborder">
					<tr>
						<td align="center"><c:if test="${not empty message}">
								<div id="successblock" class="successblock">
									<c:out value="${message}" />
								</div>
							</c:if> <c:if test="${not empty error}">
								<div id="errorblock" class="errorblock">
									<c:out value="${message}" />
								</div>
							</c:if>
							<form name="clientForm" method="post" width="100%" action="updateClient.htm">
								<table width="100%" border="0" cellspacing="0" cellpadding="2" class="container">
									<tr>
										<td>Client Name</td>
										<td><input type="text" name="clientName" id="clientName"
											size="25" value="" /></td>
										<td>Client Location</td>
										<td><input type="text" name="clientLocation" id="clientLocation"
											size="25" value="" /></td>
									</tr>
									<tr>
										<td>Client Manager</td>
										<td><input type="text" name="clientManager" id="clientManager"
											size="25" value="" /></td>
										<td>Mail ID</td>
										<td><input type="text" name="clientMailId"
											id="clientMailId" size="25" value="" /></td>
									</tr>
									<tr>
										<td>Phone Number</td>
										<td><input type="text" name="phoneNumber"
											id="phoneNumber" size="25" value="" /></td>
									</tr>
								</table>
								<br>
								<table align="center">
									<input type="submit" name="Save" value="Save"/>
									<input type="button" name="reset" value="Reset"/>
									<input type="hidden" name="clientId" id="clientId" size="25" />
									
								</table>
								<br>
            					<br>
  					            <!--  Data Table Starts-->
					            <jsp:include page="/WEB-INF/jsp/clientDetailsDisplay.jsp">
			  						<jsp:param name="name" value="sos" />
								</jsp:include>
            				  <!-- Data Table End -->
						</form></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>