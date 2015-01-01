<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css" title="currentStyle">
@import "./css/jquery.dataTables.css";
@import "./css/jquery.dataTables_themeroller.css";
@import "./css/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="./js/jquery.js"></script>
<script type="text/javascript" language="javascript" src="./js/jquery.dataTables.js"></script>
<script type="text/javascript" language="javascript" src="./js/client.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		oTable = $('#clientDetails').dataTable({
			"bJQueryUI" : true,
			"bPagination" : false

		});
	});
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Basic Datatable</title>
</head>
<body>
	<table cellpadding="0" cellspacing="0" border="0" id="clientDetails" width="100%">
		<thead>
			<tr>
				<!-- <th>Select Client</th>-->
				<th>Client ID</th>
				<th>Client Name</th>
				<th>Location</th>
				<th>Manager</th>
				<th>Mail ID</th>
				<th>Phone Number</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="client" items="${clientList}">
				<tr>
					<!-- <td class="center"><input type="radio" value="${client.clientId}" id="clientId" name="clientId"></td>--->
					<td class="center">${client.clientId}</td>
					<td>${client.clientName}</td>
					<td>${client.clientLocation}</td>
					<td>${client.clientManager}</td>
					<td>${client.clientMailId}</td>
					<td class="center">${client.phoneNumber}</td>
					<td><input type="submit" value="Edit" id="edit"></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>