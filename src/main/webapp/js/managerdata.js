$(document).ready(function(){
	
	$('#messages').DataTable({
		"processing": true,
        "serverSide": true,
        "ajax": "../../../servlet/managerdata",
        "columns": [
            { "data": "place" },
            { "data": "num" },
            { "data": "psw" }
        ]
	
	})
});