$(document).ready(function(){
	
	$('#messages').DataTable({
		"processing": true,
        "serverSide": true,
        "ajax": {
            "url": "../../../servlet/managerdata",
            "error": function(xhr){
                alert("Unable to load the manager data (HTTP "+xhr.status+" "+xhr.statusText+")");
            }
        },
        "columns": [
            { "data": "place" },
            { "data": "num" },
            { "data": "psw" }
        ]
	
	})
});