

$(document).ready(function(){
	
	$('#example2').DataTable({
		"processing": true,
        "serverSide": true,
        "ajax": {
            "url": "../../../servlet/index1",
            "error": function(xhr){
                alert("Unable to load the dormitory data (HTTP "+xhr.status+" "+xhr.statusText+")");
            }
        },
        "columns": [
            { "data": "buildnum" },
            { "data": "housenum" },
            { "data": "members" },
            { "data": "grades" },
            { "data": "cherker" },
            { "data": "time" }
        ]
	
	})
});