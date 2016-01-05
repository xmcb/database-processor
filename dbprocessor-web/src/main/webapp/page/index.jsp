<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%
String path = request.getContextPath();
String dwzPath = path + "/WEB-INF/page/dwz";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>卷皮网</title>

<link href="<%=path %>/dwz/themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="<%=path %>/dwz/themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="<%=path %>/dwz/themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
<link href="<%=path %>/dwz/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen"/>
<!--[if IE]>
<link href="themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
<![endif]-->

<!--[if lte IE 9]>
<script src="js/speedup.js" type="text/javascript"></script>
<![endif]-->

<script src="<%=path %>/dwz/js/jquery-1.7.2.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/jquery.cookie.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/jquery.validate.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/jquery.bgiframe.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/xheditor/xheditor-1.2.1.min.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/xheditor/xheditor_lang/zh-cn.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/uploadify/scripts/jquery.uploadify.js" type="text/javascript"></script>

<!-- svg图表  supports Firefox 3.0+, Safari 3.0+, Chrome 5.0+, Opera 9.5+ and Internet Explorer 6.0+ -->
<script type="text/javascript" src="<%=path %>/dwz/chart/raphael.js"></script>
<script type="text/javascript" src="<%=path %>/dwz/chart/g.raphael.js"></script>
<script type="text/javascript" src="<%=path %>/dwz/chart/g.bar.js"></script>
<script type="text/javascript" src="<%=path %>/dwz/chart/g.line.js"></script>
<script type="text/javascript" src="<%=path %>/dwz/chart/g.pie.js"></script>
<script type="text/javascript" src="<%=path %>/dwz/chart/g.dot.js"></script>

<script src="<%=path %>/dwz/js/dwz.core.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.util.date.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.validate.method.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.barDrag.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.drag.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.tree.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.accordion.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.ui.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.theme.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.switchEnv.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.alertMsg.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.contextmenu.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.navTab.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.tab.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.resize.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.dialog.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.dialogDrag.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.sortDrag.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.cssTable.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.stable.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.taskBar.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.ajax.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.pagination.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.database.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.datepicker.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.effects.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.panel.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.checkbox.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.history.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.combox.js" type="text/javascript"></script>
<script src="<%=path %>/dwz/js/dwz.print.js" type="text/javascript"></script>

<!-- 可以用dwz.min.js替换前面全部dwz.*.js (注意：替换是下面dwz.regional.zh.js还需要引入)
<script src="bin/dwz.min.js" type="text/javascript"></script>
-->
<script src="<%=path %>/dwz/js/dwz.regional.zh.js" type="text/javascript"></script>

<script type="text/javascript">
$(function(){
	DWZ.init("<%=path %>/dwz/dwz.frag.xml", {
		loginUrl:"login_dialog.html", loginTitle:"登录",	// 弹出登录对话框
//		loginUrl:"login.html",	// 跳到登录页面
		statusCode:{ok:200, error:300, timeout:301}, //【可选】
		pageInfo:{pageNum:"pageNum", numPerPage:"numPerPage", orderField:"orderField", orderDirection:"orderDirection"}, //【可选】
		keys: {statusCode:"statusCode", message:"message"}, //【可选】
		ui:{hideMode:'offsets'}, //【可选】hideMode:navTab组件切换的隐藏方式，支持的值有’display’，’offsets’负数偏移位置的值，默认值为’display’
		debug:false,	// 调试模式 【true|false】
		callback:function(){
			initEnv();
			$("#themeList").theme({themeBase:"themes"}); // themeBase 相对于index页面的主题base路径
		}
	});
});

</script>
</head>

<body scroll="no">
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<a class="logo" style="font-size:40px;font-weight:bold;color:white;text-decoration:none;">卷皮Logo</a>
			</div>
		</div>

		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse"><div></div></div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse"><h2>DBPROCESSOR-调度配置中心</h2><div>收缩</div></div>

				<div class="accordion" fillSpace="sidebar">
					<div class="accordionHeader">
						<h2><span>Folder</span>调度配置</h2>
					</div>
					<div class="accordionContent">
						<ul class="tree treeFolder">
							<li><a href="tabsPage.html" target="navTab">菜单</a>
								<ul>
									<%-- <li><a href="<%=path %>/configController/upload" target="navTab" rel="page1">上传TOPIC配置</a></li> --%>
									<li><a href="<%=path %>/configController/topicList" target="navTab" rel="page1">TOPIC列表</a></li>
									<li><a id="dbprocessora" href="<%=path %>/configController/dbprocessorList" target="navTab" rel="page1">DBPROCESSOR列表</a></li>
								</ul>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent"><!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span class="home_icon">声明</span></span></a></li>
						</ul>
					</div>
				</div>
				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox">
						<div class="pageFormContent" layoutH="80" style="margin-right:230px;font-size:25px;font-weight:bold;text-align: center; padding-top: 20%;">
							

							
							关联到生产数据，请谨慎操作！

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="footer">武汉奇米网络科技有限公司 鄂ICP备10209250号　　Copyright © 2010 -2015 JuanPi.com All Rights Reserved</div>
</body>
</html>