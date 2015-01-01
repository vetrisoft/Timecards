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