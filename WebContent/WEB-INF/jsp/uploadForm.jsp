<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<head>
    <link rel="stylesheet" type="text/css" media="screen" href="/Timecards/css/timecard.css">
	<script type="text/javascript" src="/Timecards/js/jquery-1.5.2.min.js"></script>
	<script type="text/javascript" src="/Timecards/js/jquery-ui.min.js"></script>
</head>
<div id="successMessage"
	style="color: green; margin-left: 25px; margin-top: 15px; font-size: 14px;">
	<c:if test="${not empty successMessage}">
		<b><c:out value="${successMessage}" /></b>
	</c:if>
</div>
<body>
<table width="90%" cellspacing="0" cellpadding="20" class="frmborder">
<tr>
	<td align="center">
	<form name="timeSheetFileForm" method="post" enctype="multipart/form-data" action="uploadForm.htm">
	<table width="100%" border="0" cellspacing="0" cellpadding="2" class="container">
		<tr>
			<td> 
				Please choose the file to upload
			</td>
			<td>
				<input type="file" name="file" id="file"></input>
			</td>
			<td>
			&nbsp;
			</td>
			<td>
				<input type="submit" value="Upload File" name="submit" style="margin-left: 10px;" />
			</td>
		</tr>		
	</table>
	</form>
	</td>
</tr>
</table>
</body>
<%@include file="/WEB-INF/jsp/footer.jsp"%>