

$(document).ready(function(){
	
	$('#example2').DataTable({
		"processing": true,
        "serverSide": true,
        "ajax": "../../../servlet/index1",
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