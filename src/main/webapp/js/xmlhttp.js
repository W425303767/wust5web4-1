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

function get(){

	var username = document.getElementById("user");
	var psw = document.getElementById("psw");
	var xmlhttp =createxmlhttp();
	  
	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
		  	if(xmlhttp.responseText=="success1")
		  		window.location="../html/index1.html";
		  	if(xmlhttp.responseText=="success2")
		  		window.location="../html/pages/tables/managerpage.html";
			document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
		}
	  }	
    xmlhttp.open("POST","../servlet/myservlet",true);
	xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlhttp.send("username="+encodeURIComponent(username.value)+"&psw="+encodeURIComponent(psw.value));
}

function getinfo(){
	
	var message={'where':'','StuNo':''};
	var xmlhttp = createxmlhttp();
	var year =document.getElementById("year").value.toString();
	var where = document.getElementById("where").selectedIndex;
	var num = document.getElementById("num").value;
	
	
	xmlhttp.onreadystatechange=function()
	  {
		
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
		  
			message=JSON.parse(xmlhttp.responseText);
			for(var i=0;i<message.length;i++)
			document.getElementById("numlist").innerHTML+=message[i].where+message[i].StuNo+"\n";
		}
	  }
    xmlhttp.open("POST","../servlet/Registe",true);
	xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlhttp.send("year="+encodeURIComponent(year)+"&where="+encodeURIComponent(where)
		+"&num="+encodeURIComponent(num)+"&checkid="+encodeURIComponent("1"));
	
}

function StuSend(){
	
	var message={'where':'','StuNo':''};
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
	
	
	xmlhttp.onreadystatechange=function()
	  {
		
			if (xmlhttp.readyState==4 && xmlhttp.status==200)
			{
			  if(xmlhttp.responseText=="success1")
			  		window.location="../html/pages/tables/formdata.html";
			}
		  
	  }
	  xmlhttp.open("POST","../servlet/Registe",true);
	  xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	  xmlhttp.send("year="+encodeURIComponent(year+month+day)+"&where="+encodeURIComponent(where)
	  		+"&num="+encodeURIComponent(num)+"&checkid="+encodeURIComponent("1"));
	  
}
function ManagerSend(){
	
	var message={'where':'','StuNo':''};
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
	
	
	xmlhttp.onreadystatechange=function()
	  {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
		  if(xmlhttp.responseText=="success2")
		  		window.location="../html/pages/tables/managerdata.html";
		}
	}
	
	xmlhttp.open("POST","../servlet/Registe",true);
	xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlhttp.send("year="+encodeURIComponent(year+month+day)+"&where="+encodeURIComponent(where)
			+"&num="+encodeURIComponent(num)+"&checkid="+encodeURIComponent("2"));
	  
}
