<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%
    String sessionid =  session.getId();
	String path = request.getContextPath();
	String dwzPath = path + "/WEB-INF/page/dwz";
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>上传配置文件</title>

	<style type="text/css" media="screen">
		.my-uploadify-button {
			background:none;
			border: none;
			text-shadow: none;
			border-radius:0;
		}
		
		.uploadify:hover .my-uploadify-button {
			background:none;
			border: none;
		}
		
		.fileQueue {
			width: 400px;
			height: 150px;
			overflow: auto;
			border: 1px solid #E5E5E5;
			margin-bottom: 10px;
		}
	</style>
</head>
<body>



<div class="pageContent" style="margin: 0 10px" layoutH="50">
	
	<div class="divider"></div>
		<input id="testFileInput2" type="file" name="file" 
			uploaderOption="{
				swf:'<%=path %>/dwz/uploadify/scripts/uploadify.swf',
				uploader:'<%=path %>/configController/uploadFile?jsessionid=<%=sessionid%>',
				queueID:'fileQueue',
				buttonImage:'<%=path %>/dwz/uploadify/img/add.jpg',
				buttonClass:'my-uploadify-button',
				fileTypeDesc:'1440038145130application.properties;',
			    fileTypeExts:'*.properties;',
				width:102,
				auto:false,
				multi:false,
				fileObjName:'file',
				progressData : 'percentage',
				onClearQueue:function(event,data){
					alertMsg.correct(data);
				},
                onUploadSuccess : function(file,data, response) {
                	alertMsg.correct(data);
                }
			}"
		/>
	<div id="fileQueue" class="fileQueue"></div>
	<c:if test="${upload == true}">
		<input id="aa" type="image" src="<%=path %>/dwz/uploadify/img/upload.jpg" onclick="$('#testFileInput2').uploadify('upload', '*');"/>
	</c:if>
	<c:if test="${upload == false}">
		<input type="image" src="<%=path %>/dwz/uploadify/img/upload.jpg" onclick="errorMessage()"/>
	</c:if>
	<%-- <input type="image" src="<%=path %>/dwz/uploadify/img/cancel.jpg" onclick="$('#testFileInput2').uploadify('cancel', '*');"/> --%>

	

</div>

<script type="text/javascript">
	function errorMessage(){
		alertMsg.error('DBPROCESSOR已经监听TOPIC，请解除监听后再上传新的配置！', {
			okCall:function(){}
		});
	}
</script>






	<%-- <div class="pageContent">
		<form  action="<%=path %>/configController/uploadFile" method="post" enctype="multipart/form-data" class="pageForm required-validate" >
			<div class="pageFormContent" layoutH="56">
				<p>
					<label>文件：</label>
					<input id="file" name="file" type="file" />
				</p>
				<!-- <p><label>多文件上传：</label>
					<a rel="w_uploadify" target="navTab" href="w_uploadify.html" class="button"><span>uploadify上传示例</span></a>
				</p> -->
			</div>
			<div class="formBar">
				<ul>
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit" class="button">提交</button>
							</div>
						</div>
					</li>
					<li>
						<div class="button">
							<div class="buttonContent">
								<button type="button" class="close" class="button">取消</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</form>
	</div> --%>
</body>
</html>