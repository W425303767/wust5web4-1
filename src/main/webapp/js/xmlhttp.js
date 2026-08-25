/**
 * This is package the XMLHTTP project
 */
function createxmlhttp(){

	var xmlhttp;
	
	if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	
	return xmlhttp;
}

/**
 * Sends a request and hands the response text to onSuccess once it arrived.
 */
function sendRequest(method, url, onSuccess){

	var xmlhttp = createxmlhttp();

	xmlhttp.onreadystatechange = function(){
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
			onSuccess(xmlhttp.responseText);
	};
	xmlhttp.open(method, url, true);
	xmlhttp.send();
}

/**
 * Today as YYYYMMDD.
 */
function today(){

	var nowdate = new Date();
	var year = nowdate.getFullYear();
	var month = nowdate.getMonth()+1;
	if( month < 10)
		month = "0"+month;
	var day = nowdate.getDate();
	if(day<10)
		day="0"+day;

	return year+""+month+day;
}

/**
 * Registers the requested amount of accounts and follows the redirect on success.
 */
function sendRegistration(method, whereid, numid, checkid, successText, redirect){

	var where = document.getElementById(whereid).selectedIndex;
	var num = document.getElementById(numid).value;

	sendRequest(method, "../servlet/Registe?year="+today()+"&where="+where+"&num="+num+"&checkid="+checkid,
		function(responseText){
			if(responseText==successText)
				window.location=redirect;
		});
}

function get(){

	var username = document.getElementById("user");
	var psw = document.getElementById("psw");

	sendRequest("POST","../servlet/myservlet?username="+username.value+"&psw="+psw.value, function(responseText){
		if(responseText=="success1")
			window.location="../html/index1.html";
		if(responseText=="success2")
			window.location="../html/pages/tables/managerpage.html";
		document.getElementById("myDiv").innerHTML=responseText;
	});
}


function post(){

	var username = document.getElementById("user");
	var psw = document.getElementById("psw");

	sendRequest("GET","/wust5web4-1/servlet/Test?username="+username.value+"&password="+psw.value, function(responseText){
		document.getElementById("myDiv").innerHTML=responseText;
	});
}

function getinfo(){

	var year =document.getElementById("year").value.toString();
	var where = document.getElementById("where").selectedIndex;
	var num = document.getElementById("num").value;

	sendRequest("POST","/wust5web4-1/servlet/Registe?year="+year+"&where="+where+"&num="+num, function(responseText){
		var message=JSON.parse(responseText);
		for(var i=0;i<message.length;i++)
			document.getElementById("numlist").innerHTML+=message[i].where+message[i].StuNo+"\n";
	});
}

function StuSend(){

	sendRegistration("GET","academy","num_stu",1,"success1","../html/pages/tables/formdata.html");
}

function ManagerSend(){

	sendRegistration("POST","position","num_manage",2,"success2","../html/pages/tables/managerdata.html");
}
