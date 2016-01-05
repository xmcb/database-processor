<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%
String path = request.getContextPath();
String dwzPath = path + "/dwz";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>dbprocessor</title>
</head>
<body>
<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="<%=path %>/configController/topicList" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>
						TOPIC类型
					</td>
					<td>
					<select class="combox" name="topicType" style="width:10%;height:40px;">
					   <option value="">——请选择——</option>
					   <c:forEach var="type" items="${topicTypeList}">
					   	<option value="${type}" <c:if test="${type eq topicType}">selected="selected"</c:if> >
					   	${type}
					   	</option>
					   </c:forEach>
					</select>	
					</td>
					<td>
						<div class="buttonActive"><div class="buttonContent"><button type="submit">搜索</button></div></div>
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>
<div class="pageContent">
	<!-- <div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="demo_page4.html" target="navTab"><span>添加</span></a></li>
			<li><a class="delete" href="demo/common/ajaxDone.html?uid={sid_user}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
			<li><a class="edit" href="demo_page4.html?uid={sid_user}" target="navTab"><span>修改</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" href="demo/common/dwz-team.xls" target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出EXCEL</span></a></li>
		</ul>
	</div> -->
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="80"></th>
				<th width="120">类型</th>
				<th>名称</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${topicList}" var="topic" varStatus="status">
		  <tr target="sid_user" rel="1" style="height:40px;">
				<td>${status.index + 1}</td>
				<td style="font-weight:bold;">${topic.type}</td>
				<td>${topic.name}</td>
		  </tr>
		</c:forEach>
		</tbody>
	</table>
</div>
</body>