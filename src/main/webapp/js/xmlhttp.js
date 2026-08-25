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
 * Show a failed request instead of ignoring it: without this a server error or a
 * dropped connection leaves the page unchanged and the user waiting forever.
 */
function showrequesterror(xmlhttp, message){

	var text = message;
	if (xmlhttp && xmlhttp.status)
		text += " (HTTP " + xmlhttp.status + " " + xmlhttp.statusText + ")";
	if (xmlhttp && xmlhttp.responseText)
		text += ": " + xmlhttp.responseText;
	if (window.console && console.error)
		console.error(text);
	var div = document.getElementById("myDiv");
	if (div)
		div.innerHTML = text;
	else
		alert(text);
}

/**
 * Call onsuccess with the response text once the request completed successfully,
 * and report every other outcome to the user.
 */
function onrequestdone(xmlhttp, message, onsuccess){

	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState!=4)
			return;
		if (xmlhttp.status==200)
			onsuccess(xmlhttp.responseText);
		else
			showrequesterror(xmlhttp, message);
	}
	xmlhttp.onerror=function()
	{
		showrequesterror(xmlhttp, message);
	}
}

function get(){

	var username = document.getElementById("user");
	var psw = document.getElementById("psw");
	var xmlhttp =createxmlhttp();

	onrequestdone(xmlhttp, "Login failed", function(responseText){
		if(responseText=="success1")
			window.location="../html/index1.html";
		if(responseText=="success2")
			window.location="../html/pages/tables/managerpage.html";
		document.getElementById("myDiv").innerHTML=responseText;
	});
    xmlhttp.open("POST","../servlet/myservlet?username="+encodeURIComponent(username.value)+"&psw="+encodeURIComponent(psw.value),true);
	xmlhttp.send();
}


function post(){
	
	var xmlhttp=createxmlhttp();
	var username = document.getElementById("user");
	var psw = document.getElementById("psw");

	onrequestdone(xmlhttp, "Login failed", function(responseText){
		document.getElementById("myDiv").innerHTML=responseText;
	});
	xmlhttp.open("GET","/wust5web4-1/servlet/Test?username="+encodeURIComponent(username.value)+"&password="+encodeURIComponent(psw.value),true);
	xmlhttp.send();
}

function getinfo(){
	
	var xmlhttp = createxmlhttp();
	var year =document.getElementById("year").value.toString();
	var where = document.getElementById("where").selectedIndex;
	var num = document.getElementById("num").value;

	onrequestdone(xmlhttp, "Registration failed", function(responseText){
		var message=JSON.parse(responseText);
		for(var i=0;i<message.length;i++)
			document.getElementById("numlist").innerHTML+=message[i].where+message[i].StuNo+"\n";
	});
    xmlhttp.open("POST","/wust5web4-1/servlet/Registe?year="+year+"&where="+where+"&num="+num,true);
	xmlhttp.send();
	
}

function StuSend(){
	
	var xmlhttp = createxmlhttp();
	var nowdate = new Date();
	var year = nowdate.getFullYear();
	var month = nowdate.getMonth()+1;
	if( month < 10)
		month = "0"+month;
	var day = nowdate.getDate();
	if(day<10)
		day="0"+day;
	var where = document.getElementById("academy").selectedIndex;
	var num = document.getElementById("num_stu").value;

	onrequestdone(xmlhttp, "Student registration failed", function(responseText){
		if(responseText=="success1")
			window.location="../html/pages/tables/formdata.html";
		else
			showrequesterror(xmlhttp, "Student registration failed");
	});
	xmlhttp.open("GET","../servlet/Registe?year="+year+month+day+"&where="+where+"&num="+num+"&checkid=1",true);
	xmlhttp.send();
	  
}
function ManagerSend(){
	
	var xmlhttp = createxmlhttp();
	var nowdate = new Date();
	var year = nowdate.getFullYear();
	var month = nowdate.getMonth()+1;
	if( month < 10)
		month = "0"+month;
	var day = nowdate.getDate();
	if(day<10)
		day="0"+day;
	var where = document.getElementById("position").selectedIndex;
	var num = document.getElementById("num_manage").value;

	onrequestdone(xmlhttp, "Manager registration failed", function(responseText){
		if(responseText=="success2")
			window.location="../html/pages/tables/managerdata.html";
		else
			showrequesterror(xmlhttp, "Manager registration failed");
	});
	xmlhttp.open("POST","../servlet/Registe?year="+year+month+day+"&where="+where+"&num="+num+"&checkid=2",true);
	xmlhttp.send();
	  
}
