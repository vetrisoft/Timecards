<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<style type="text/css" title="currentStyle">
@import "./css/jquery.dataTables.css";
@import "./css/jquery.dataTables_themeroller.css";
@import "./css/jquery-ui-1.8.4.custom.css";
@import "./css/timecard.css";
</style>
<script type="text/javascript" language="javascript" src="./js/jquery.js"></script>
<script type="text/javascript" language="javascript" src="./js/jquery.dataTables.js"></script>
<script type="text/javascript" language="javascript" src="./js/employee.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		oTable = $('#employeeDetails').dataTable({
			"bJQueryUI" : true,
			"bPagination" : false

		});
	});
	
	/* Init DataTables */
	var oTable = $('#employeeDetails').dataTable();

	$('#employeeDetails tbody td').live('click',function (event) {
		/* Get the position of the current data from the node */
		var aPos = oTable.fnGetPosition(this);
	   
		/* Get the data array for this row */
	   var aData = oTable.fnGetData(aPos[0]);
	   
	  // clear any previous select highlighting, and highlight selected
	   $(oTable.fnSettings().aoData).each(function(){
		   $(this.nTr).removeClass('selected-row');
	   });
	          
	   $(event.target).parent().addClass('selected-row');
	  
	   var name = aData[1].split(',');
	   		$('#employeeNumber').val(aData[0]);
	   		$('#firstName').val(name[1]);
	   		$('#lastName').val(name[0]);
	   		$('#employeeMailId').val(aData[2]);
	   		$('#dateOfJoining').val(aData[3]);
	   		$('#relievingDate').val(aData[4]);
	   		$('#designation').val(aData[5]);
	   		$('#baseLocation').val(aData[6]);    
	   
	   //focus the employee first name field.
	   	$('#firstName').focus();
	   	this.innerHTML = 'Selected';
	});	
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Basic Datatable</title>
</head>
<body>
	<table cellpadding="0" cellspacing="0" border="0" id="employeeDetails" width="100%">
		<thead>
			<tr>
				<th>Employee #</th>
				<th>Employee Name</th>
				<th>Email Id</th>
				<th>Date Of Joining</th>
				<th>Relieving Date</th>
				<th>Designation</th>
				<th>Base Location</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="employee" items="${employeeList}">
				<tr>
					<td class="center">${employee.employeeNumber}</td>
					<td>${employee.employeeLastName},${employee.employeeFirstName}</td>
					<td>${employee.employeeMailId}</td>
					<td>${employee.dateOfJoining}</td>
					<td>${employee.relievingDate}</td>
					<td class="center">${employee.designation}</td>
					<td class="center">${employee.baseLocation}</td>
					<td><input type="submit" value="Edit" id="edit"></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>