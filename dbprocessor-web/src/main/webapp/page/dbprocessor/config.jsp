<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%
String path = request.getContextPath();
String dwzPath = path + "/dwz";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>config</title>
<script type="text/javascript">
	function closeWindow(){
		$("#closeButton").click();
	}
</script>
</head>
<body>

<div class="pageContent">
	<form method="post" onsubmit="return navTabSearch(this);" action="<%=path %>/configController/saveConfig" class="pageForm required-validate">
		<div class="pageFormContent" layoutH="56">
		   <c:forEach var="typeEntity" items="${topicTypeList}">
		   		<label style="font-weight:bold;font-size:20px;" <c:if test="${typeEntity.state == 2}">style="color:red;"</c:if>>
		   			<input name="type" 
			   			type="checkbox" 
			   			value="${typeEntity.name}" 
			   			<c:if test="${typeEntity.state == 2}">disabled="disabled"</c:if> 
			   			<c:if test="${typeEntity.state == 1}">checked="checked"</c:if>
			   			/>
		   			${typeEntity.name}
		   		</label>
		   </c:forEach>
		   <input type="hidden" name="host" value="${host}"/>
		</div>
		<div class="formBar">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent">
							<button type="submit" onclick="javascript:closeWindow();">保存</button>
						</div>
					</div>
				</li>
				<li>
					<div class="button"><div class="buttonContent"><button id="closeButton" type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>


</body>
