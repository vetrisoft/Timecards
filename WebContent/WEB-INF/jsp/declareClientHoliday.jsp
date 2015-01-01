<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>Nisum Technologies</title>
	<link href="http://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" media="screen" href="css/holiday.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="/Timecards/css/timecard.css">
	<script type="text/javascript" src="/Timecards/js/jquery-1.5.2.min.js"></script>
	<script type="text/javascript" src="/Timecards/js/jquery-ui.min.js"></script>
<script>
  $(function() {
	  $(".datepicker").datepicker({dateFormat: "yy-mm-dd" });
  });
</script>
<script type="text/javascript">
$(document).ready(function(){
	var id = 2,max = 15,append_data;
	/*If the add icon was clicked*/
	$(".add").live('click',function(){
		if($("div[id^='txt_']").length <15){ //Don't add new textbox if max limit exceed
		$(this).remove(); //remove the add icon from current text box
		var dateval = "dateval["+ id + "]";
		var reasonval = "reasonval["+ id +"]";
		
		var append_data = '<div id="txt_'+id+'" class="txt_div" style="display:none;"><div class="left-align">Date: <input type="text" id="dateval_'+id+'" name="'+dateval+'" style="width:150px"/> Reason: <input type="text" id="reason_'+id+'" style="width:350px" name="'+reasonval+'"/></div><div class="right"><img src="/Timecards/images/add.png" class="add"/> <img src="/Timecards/images/remove.png" class="remove"/></div></div>';
		$("#text_boxes").append(append_data); //append new text box in main div
	    $('#txt_'+id).find('input[id^=dateval]').datepicker({dateFormat: 'yy-mm-dd'});	
		$("#txt_"+id).effect("bounce", { times:1 }, 300); //display block appended text box with silde down
		id++;
		} else {
			alert("Maximum 15 textboxes are allowed");
		}
	})
	
	/*If remove icon was clicked*/
	$(".remove").live('click',function(){
		var prev_obj = $(this).parents().eq(1).prev().attr('id');  //previous div id of this text box
		$(this).parents().eq(1).slideUp('medium',function() { $(this).remove(); //remove this text box with slide up
		if($("div[id^='txt_']").length > 1){
			append_data = '<img src="/Timecards/images/remove.png" class="remove"/>'; //Add remove icon if number of text boxes are greater than 1
		}else{
			append_data = '';
		}
		if($(".add").length < 1 ){
			$("#"+prev_obj+" .right").html('<img src="/Timecards/images/add.png" class="add"/> '+append_data);
		}
		});
	})
});
</script>
</head>
<body>
	<table width="100%" border="0" cellspacing="0" cellpadding="20">
		<tr>
			<td align="center">
				<table width="100%" cellspacing="0" cellpadding="20" class="frmborder">
					<tr>
						<td align="center">
							<c:if test="${not empty message}">
								<div id="successblock" class="successblock">
									<c:out value="${message}" />
								</div>
							</c:if> 
							<c:if test="${not empty error}">
								<div id="errorblock" class="errorblock">
									<c:out value="${message}" />
								</div>
							</c:if>
							<form name="clientForm" method="post" width="100%" action="updateClientHolidays.htm">
								<table width="100%" border="0" cellspacing="0" cellpadding="2" class="container">
									<tr>
										<td>Select Client Name</td>
										<td colspan="2">
											<select name="clientId" id="clientId">
												<option value="">Select</option>
												<c:forEach var="clients" items="${clientList}">
													<option value="${clients.clientId}">${clients.clientId}-${clients.clientName}</option>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr>
										<td>Client Holiday</td>
										<td colspan="4">
											<div style="margin:auto;">
													<div id="text_boxes" style="width:900px;height:auto;">
														<div id="txt_1" class="txt_div">
															<div class="left-align">
																Date:
																<input type="text" id="dateval_1" name="dateval[1]" class="datepicker" style="width:150px"/>
																Reason:
																<input type="text" id="reason_1" name="reasonval[1]" style="width:350px"/>
															</div>
															<div class="right">
																<img src="/Timecards/images/add.png" class="add"/>
															</div>
														</div>
													</div>
											</div>
										</td>
									</tr>
								</table>
   							   <table>
   							   	<tr>
   							   		<td align="text-align: right">
   							   			<input type="submit" name="Save" value="Save" style="width:100px;"/>
   							   		</td>
   							   	</tr>
								</table>
								<input type="hidden" name="clientName" id="clientName"/>
							</form></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>