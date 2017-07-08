/**
 * This is package the XMLHTTP project
 */
var image={"fname" : "Bill","lname":Gates}

function get(){
	var xmlhttp;
	if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }

	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
			document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
		}
	  }	
	  var username = document.getElementById("user");
	  var psw = document.getElementById("psw");
	xmlhttp.open("GET","/wust5web4-1/servlet/myservlet?username="+username.value+"&psw="+psw.value,true);
	xmlhttp.send();
}


function post(){
	var xmlhttp;
	if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }

	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
			document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
		}
	  }	
	xmlhttp.open("POST","/servlet/Test",true);
	xmlhttp.send();
	xmlhttp.setRequestHeader("Content-type",application/x-www-form-urlencoded);
	xmlhttp.send("fname=Bill&lname=Gates");
}