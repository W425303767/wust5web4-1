/**
 * 
 */

$(document).ready(function(){
	console.log("laile");
	$('#exmaple2').DataTable({
		"processing": true,
        "serverSide": true,
        "ajax": "/servlet/index1",
        "columns": [
            { "data": "first_name" },
            { "data": "last_name" },
            { "data": "position" },
            { "data": "office" },
            { "data": "start_date" },
            { "data": "salary" }
        ]
	
	})
	console.log($('#exmaple2'));
	console.log("go");
});