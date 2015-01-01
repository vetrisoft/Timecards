
<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <link rel="stylesheet" type="text/css" href="./css/timecard.css" media="screen" />
 <script src="//code.jquery.com/jquery-1.9.1.js"></script>
 <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<title>Week Report</title>
<script type="text/javascript">
$(document).ready(function () {

$("#simple-post").click(function()
{
	var frm = $('#weekReportForm');
	frm.submit(function (ev) {
	    $.ajax({
	        type: frm.attr('method'),
	        url: frm.attr('action'),
	        data: frm.serialize(),
	        success: function (data) {
	        	window.parent.location.href = window.parent.location.href;
	        }
	    });
	    ev.preventDefault();
	    ev.unbind();
	});
//	$("#weekReportForm").submit();
}),

$("#cancelBtn").click(function(ev)
		{
	        ev.preventDefault();
			$( "#outerdiv", window.parent.document).hide();
		});
});


</script>
</head>
<body>
 <div style="padding-bottom: 5px;"><b> Employee Name :</b> ${employeeName}</div>
<form:form method="post" id="weekReportForm" action="weekTimecardUpdate.htm" modelAttribute="timeCardWeekReprotData">
		<div class="divTable clear">
             <div class="headRow">
                <div class="hdivCell" >Date</div>
                <div class="hdivCell"># Billed Hours</div>
                <div class="hdivCell"># Non-Billable Hours</div>
                <div class="hdivCell nbReason">Non-Billable Reason</div>
                <div class="hdivCellComments1">Remarks (Max. 500 char)</div>
             </div>
             <input type="hidden" type="text" name="weekEndDate" value="${weekEndDate}"/>
             <c:forEach items="${timeCardWeekReprotData.weekTimeCardReport}" var="timeCardDay" varStatus="status">
             <input type="hidden" name="weekTimeCardReport[${status.index}].employeeId" value="${timeCardDay.employeeId}"/>
             <input type="hidden" name="weekTimeCardReport[${status.index}].timeEntryDayDate" value="${timeCardDay.timeEntryDayDate}"/>
             <div class="divRow">
              	<div class="divCell" ><fmt:formatDate pattern="yyyy-MM-dd" value="${timeCardDay.timeEntryDayDate}" /></div>
                <div class="divCell">${timeCardDay.hours}</div>
				<div class="divCell" >
				<input name="weekTimeCardReport[${status.index}].nonBillableHours" type="text" value="${timeCardDay.nonBillableHours}" size="4" maxlength = "5"/>
				</div>
				<div class="divCell nbReason">
                 <select name="weekTimeCardReport[${status.index}].nonBillableReson">
                  <option value="">Select Reason</option>
                  <c:forEach var="nbReson" items="${nonBillableReasons}">
                  	 <option value="${nbReson.nonBillableReson}" <c:if test="${nbReson.nonBillableReson==timeCardDay.nonBillableReson}"> selected = "true"</c:if>>${nbReson.nonBillableReson}</option>
				  </c:forEach>
				  </select>
				 </div>
                <div class="divCellComments1"><textarea class="textarea2" cols="35" maxlength="500" name="weekTimeCardReport[${status.index}].comments">${timeCardDay.comments}</textarea></div>
                </div>
             </c:forEach>
       </div>
       <br>
         <div align="left"><button id="simple-post">Update</button>&nbsp;&nbsp;&nbsp;&nbsp;<button id="cancelBtn">Cancel</button></div>
</form:form>
</body>
</html>