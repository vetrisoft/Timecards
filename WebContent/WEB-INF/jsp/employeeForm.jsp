<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>Nisum Technologies</title>
	<link href="http://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" media="screen" href="/Timecards/css/timecard.css">
	<script type="text/javascript" src="/Timecards/js/jquery-1.5.2.min.js"></script>
	<script type="text/javascript" src="/Timecards/js/jquery-ui.min.js"></script>
<script>
  $(function() {
	  $(".datepicker").datepicker({dateFormat: "yy-mm-dd" });
  });
</script>
</head>
<body>
	<table width="100%" border="0" cellspacing="0" cellpadding="10"><tr><td align="left">
	<table width="100%" cellspacing="0" cellpadding="10" class="frmborder"><tr><td align="center">
	
		<c:if test="${not empty message}">
			<div class="successblock">
				<c:out value="${message}"/>
			</div>
		</c:if>
		<c:if test="${not empty error}">
			<div class="errorblock">
				<c:out value="${message}"/>
			</div>
		</c:if>
		
       	<form name="updateEmployee" method="post" width="100%" action="updateEmployee.htm">            
            <table width="100%" border="0" cellspacing="0" cellpadding="2" class="container">
            <tr>
				<td>Employee First Name</td>
				<td><input type="text" name="firstName" id="firstName" size="25" value="" /></td>
				<td>Employee Last Name</td>
				<td><input type="text" name="lastName"  id="lastName"  size="25" value="" /></td>
			</tr>
			<tr>
				<td>Nisum Email ID</td>
				<td><input type="text" name="employeeMailId" id="employeeMailId" size="25" value="" /></td>
				<td>Date of Joining</td>
				<td><input type="text" name="dateOfJoining" id="dateOfJoining" size="25" value="" class="datepicker" /></td>
			</tr>
			<tr>
				<td>Date of RelievingDate</td>
				<td><input type="text" name="relievingDate" id="relievingDate" size="25" value="" class="datepicker" /></td>
				<td>Designation</td>
				<td><select name="designation" id="designation">
						<option value="">Select</option>
						<option value="SoftwareEngineer">Software Engineer</option>
						<option value="Sr.SoftwareEngineer">Senior Software Engineer</option>
						<option value="TeamLead">Team Lead</option>
						<option value="Sr.TeamLead">Senior Team Lead</option>
						<option value="Technical Lead">Technical Lead</option>
						<option value="Sr.Technical Lead">Senior Technical Lead</option>
						<option value="Sr.Architect">Senior Architect</option>						
						<option value="ProjectManager">Project Manager</option>
						<option value="Sr.ProjectManager">Senior Project Manager</option>
						<option value="QualityAnalyst">Quality Analyst</option>
						<option value="Sr.QualityAnalyst">Sr.Quality Analyst</option>
				</select></td>
			 </tr>
			<tr>
				<td>Base Location</td>
				<td><select name="baseLocation" id="baseLocation">
						<option value="">Select</option>
						<option value="USA">USA</option>
						<option value="CHILLE">CHILLE</option>
						<option value="HYDEBRABAD">HYDERABAD</option>
						<option value="BANGALORE">BANGALORE</option>
						<option value="PUNE">PUNE</option>
				</select></td>
			</tr>
		  </table>
          <br>
		<table align="center">
        	<input type="submit" name="Save" value="Save">
            <input type="button" name="reset" value="Reset">
            <input type="hidden" name="employeeNumber" id="employeeNumber" size="25" />
        </table>
            <br>
            <br>
            <!--  Data Table Starts-->
            <jsp:include page="/WEB-INF/jsp/employeeDetailsDisplay.jsp">
			  <jsp:param name="name" value="sos" />
			</jsp:include>
            <!-- Data Table End -->
            </form></td></tr></table></td></tr></table>
    </body>
</html>