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
<title>dbprocessor</title>
</head>
<body>
<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="<%=path %>/configController/dbprocessorList" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>
						TOPIC类型
					</td>
					<td>
					<select class="combox" name="type">
					   <c:forEach var="type" items="${topicTypeList}">
					   	<option value="${type}">${type}</option>
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
				<th width="200">IP</th>
				<th width="200">最近心跳时间</th>
				<th width="100">是否存活</th>
				<th width="300">已监听Topic类型</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${dbprocessorList}" var="dbprocessor" varStatus="status">
		  <tr target="sid_user" rel="1" style="height:70px;">
				<td>${status.index + 1}</td>
				<td>${dbprocessor.host}</td>
				<td>
					<fmt:formatDate value="${dbprocessor.hearBeatTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>${dbprocessor.alive}</td>
				<td>
					<c:forEach items="${dbprocessor.topicTypeList}" var="type" varStatus="status">
						[<b style="font-weight:bold;">${type}</b>]
					</c:forEach>
				</td>
				<td>
				  <a class="button" style="margin-left:5%;" 
				  	 href="<%=path %>/configController/config?host=${dbprocessor.host}" 
				  	 target="dialog" rel="dlg_page10" 
				  	 mask="true" title="请选择${dbprocessor.host}监听的TOPIC类型">
				  <span>配置TOPIC</span>
				  </a>
				</td>
		  </tr>
		</c:forEach>
		</tbody>
	</table>
	<%-- <div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="20">20</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
			<span>条，共${totalCount}条</span>
		</div>
		<div class="pagination" targetType="navTab" totalCount="200" numPerPage="20" pageNumShown="10" currentPage="1"></div>
	</div> --%>
</div>

<script type="text/javascript">
	var message = "${message}";
	if(message.length > 0){
		alertMsg.correct(message)
	}
</script>
</body>