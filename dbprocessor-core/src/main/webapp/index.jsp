<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
     <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
     <title>service-gate</title>
     <script src="<%=basePath%>/jquery.js" type="text/javascript"></script>    
	 <script type="text/javascript">
	 /* var dataStr = "{'u_avatar':'头像','u_city':'沙坪坝区'}";
	 $.ajax({
            type:'post',	 
			url:'http://localhost:8088/service-gate/user/userRegister.do',
			//contentType:"application/json;charset=UTF-8",
			contentType:"text/html;charset=UTF-8",
			data:JSON.stringify(dataStr),
			dataType:'jsonp',
			//async:false,
			complete:function(XMLHttpRequest, textStatus){
				alert(textStatus + "a" + XMLHttpRequest);
			},
			success:function(data){
				alert(data + "aaaaaaaa");
			},
			 error : function() {       
				  alert("异常");    
			 }    
		}); */
	 
	
	 </script>
  </head>
  <body>
   <div>
		<p>service-gate</p>
   </div>
  </body>