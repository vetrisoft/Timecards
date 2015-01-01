<%@ page session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<head>
    <link rel="stylesheet" type="text/css" media="screen" href="/Timecards/css/holiday.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="/Timecards/css/timecard.css">
</head>
<script>
$(document).ready(function () {
	$("#timeCardDataTable a").click(function(event) { 
		 var attributeId = $(this).attr('id'); 
		 if (attributeId !== 'undefined' && attributeId === 'updateWeekReport'){
			$("#iframeWeekReport").attr("src",$(this).attr("href")); 
			$( "#outerdiv" ).show();
			$( "#outerdiv" ).focus();
			window.scrollTo(0,0);
			 } 
		});
});
</script>
<form name="searchForm" action="viewall.htm">
	<table width="100%" border="0" cellspacing="0" class="container">
		<tr>
			<td align="center">
				<div class="searchPanel">
				 	<div class="searchRow">Select Months:
						<select name=monthOption>
					 	  <option value="all">All</option>
					    	<c:forEach items="${filterForm.months}" var="month">
					    		 <option value="${month}" <c:if test="${month==filterForm.monthOption}"> selected = "true"</c:if>>${month} Months</option>
					    	</c:forEach>
						</select>
					</div>
					<div class="searchRow">Employee Name: <input name="empName" value="${filterForm.empName}"></div>
					<div class="searchRow">Select Status:
					 <select name=filterStatus>
					  <option value="all">All</option>
				        <c:forEach var="status" items="${statusMap}">
				           	 <option value="${status.key}" <c:if test="${status.key==filterForm.filterStatus}"> selected = "true"</c:if>>${status.value}</option>
						</c:forEach>
					 </select>
					</div>
			    </div>
		    </td>
		</table>
		<table  width="100%" border="0" cellspacing="0">
   			<tr>
   				<td align="center">
   					<input type="submit" name="Search" value="Search" style="width:100px;"/>
   				</td>
   			</tr>
		</table>							
	</form>
	<br></br>
	<br></br>
	<table width="100%" border="0" cellspacing="0" class="container">
	<tr>
		<td>
			<form name="timesheetReport" action="updateStatus.htm" method="post">
			<input name="monthOption" type="hidden" value="${filterForm.monthOption}">
			<input name="empName"  type="hidden"  value="${filterForm.empName}">
			<input name="filterStatus"  type="hidden"  value="${filterForm.filterStatus}">
  			<div id='outerdiv' style="top:0px; left:160px;display:none"> 
   				<iframe src="" id='iframeWeekReport' name="iframeWeekReport" frameBorder="0" seamless scrolling="no"></iframe> <br/>
 			</div>
 			<div id="timeCardDataTable" class="divTable clear">
            	 <div class="headRow">
                	<div class="divCell" >Work Order Id</div>
                	<div class="divCell">Employee ID</div>
                	<div class="divCell empName">Employee Name</div>
                	<div class="divCell">No. of Hours</div>
                	<div class="divCell status">Week Ending</div>
                	<div class="divCell status">Status</div>
                	<div class="divCell">Remarks</div>
             	</div>
    		</div>
    		<c:forEach items="${timeCardWeekReprotData}" var="timeCardWeek">
    			<div class="divRow">
                  <div class="divCell"><a id="updateWeekReport" href="/Timecards/weekReport.htm?empId=${timeCardWeek.employeeId}&weekEndDate=${timeCardWeek.timeEntryWeekEndDate}" target="iframeWeekReport">${timeCardWeek.workOrderId}</a></div>
                  <div class="divCell">${timeCardWeek.employeeId}</div>
                  <div class="divCell empName">${timeCardWeek.fullName}</div>
                  <div class="divCell ">${timeCardWeek.totalHoursForWeek}</div>
                  <div class="divCell">${timeCardWeek.timeEntryWeekEndDate}</div>
                  <div class="divCell status">
					<select name=status#${timeCardWeek.employeeId}#${timeCardWeek.timeEntryWeekEndDate}>
					<c:forEach var="status" items="${statusMap}">
						<option value="${status.key}" <c:if test="${status.key==timeCardWeek.hoursStatusTypeCode}"> selected = "true"</c:if>>${status.value}</option>
					</c:forEach>
				  </select>
                  </div>
                  <div class="divCellComments"><textarea class="textarea1" readonly  cols="48" maxlength="500" name="comments#${timeCardWeek.employeeId}#${timeCardWeek.timeEntryWeekEndDate}">${timeCardWeek.comments}</textarea></div>
               </div>
    		</c:forEach>
	        <br>
			</form>
			<hr>
			<a id="exportAllRecords"  href="exportExcel.htm" target="_self">Export Time Cards to Excel (All Records)</a>
			<br>
			<a id="exportFilterdRecords" target="_self" href="exportExcel.htm?monthOption=${filterForm.monthOption}&empName=${filterForm.empName}&filterStatus=${filterForm.filterStatus}">Export Time Cards to Excel (Filter Results)</a>
		</td>
	</tr>
</table>