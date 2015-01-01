/* Init DataTables */
var oTable = $('#clientDetails').dataTable();
$('#clientDetails tbody td').live('click',function () {
/* Get the position of the current data from the node */
   var aPos = oTable.fnGetPosition(this);
/* Get the data array for this row */
   var aData = oTable.fnGetData(aPos[0]);
   $('#clientId').val(aData[0]);
   $('#clientName').val(aData[1]);
   $('#clientLocation').val(aData[2]);
   $('#clientManager').val(aData[3]);
   $('#clientMailId').val(aData[4]);
   $('#phoneNumber').val(aData[5]);
   
   //focus the client name field.
   $('#clientName').focus();
   this.innerHTML = 'Selected';
});

// Client Holiday page functionality is added.
$(document).ready(function(){
	var id = 2,max = 15,append_data;
	/*If the add icon was clicked*/
	$(".add").live('click',function(){
		if($("div[id^='txt_']").length <15){ //Don't add new textbox if max limit exceed
		$(this).remove(); //remove the add icon from current text box
		var append_data = '<div id="txt_'+id+'" class="txt_div" style="display:none;"><div class="left"><input type="text" id="input_'+id+'" name="txtval[]"/></div><div class="right"><img src="/Timecards/images/add.png" class="add"/> <img src="/Timecards/images/remove.png" class="remove"/></div></div>';
		$("#text_boxes").append(append_data); //append new text box in main div
		$("#txt_"+id).effect("bounce", { times:3 }, 300); //display block appended text box with silde down
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
		if($(".add").length < 1){
			$("#"+prev_obj+" .right").html('<img src="/Timecards/images/add.png" class="add"/> '+append_data);
		}
		});
	})
});


//if ($(oTable.fnGetNodes()).find("input[type='radio' name='clientId']:checked").val() !==null){
//	alert("inside the click");
//}