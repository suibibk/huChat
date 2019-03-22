<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset='utf-8'>
<title>后台管理系统</title>
<script src="${pageContext.request.contextPath }/js/jquery-1.10.1.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath }/js/md5.js" type="text/javascript"></script>
<style type="text/css">
*{margin:0px;padding:0px;}
body{
	background-color:#000;
	font-family:"微软雅黑";
}
#login{
	width:100%;
	height:260px;
	margin:0px auto;
	background-color:#fff;
	margin-top:150px;
	border-radius:5px;
	box-shadow:#666 5px 5px 0px 0px;
}
#register{
	width:100%;
	height:300px;
	margin:0px auto;
	background-color:#fff;
	margin-top:150px;
	border-radius:5px;
	box-shadow:#666 5px 5px 0px 0px;
	display:none;
}
#title{
	width:100%;
	height:50px;
	background-color:#DAF9CA;
	text-align:center;
	line-height:50px;
	font-size:25px;
	border-radius: 5px 5px 0px 0px;
}
#tip{
	width:100%;
	height:30px;
	text-align:center;
	line-height:30px;
	font-size:18px;
	color:red;
}
#tip2{
	width:100%;
	height:30px;
	text-align:center;
	line-height:30px;
	font-size:18px;
	color:red;
}
.username_password{
	width:100%;
	height:100px;
	background-color:#ccc;
	font-size:18px;
}	
.div0{
	width:100%;
	height:50px;
}
.div1{
	float:left;
	width:20%;
	height:100%;
	text-align:right;
	line-height:50px;
}
.div2{
	float:right;
	width:79%;
	height:100%;
	text-align:left;
	line-height:50px;
}
.div2 input{
	width:80%;
	height:40px;
	border-radius:20px;
	padding-left:10px;
	font-size:18px;
}
#submit{
	width:100%;
	height:50px;
	text-align:center;
	line-height:50px;
}
#submit input{
	width:70px;
	height:40px;
	border-radius:20px;
	background-color:#DAF9CA;
}
.go:hover{cursor: pointer;}
.do:hover{cursor: pointer;}
#lo{
	text-align: center;
	font: 20px;
	vertical-align: middle;
}
#lo img{
	vertical-align: middle;
	height:20px;
	cursor: pointer;
}
</style>
<script type="text/javascript">
$(function(){
	var sUserAgent = navigator.userAgent.toLowerCase(); 
	var wechat = sUserAgent.match(/wechat/i) == "wechat";   
	var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";    
	var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";  
	var bIsMidp = sUserAgent.match(/midp/i) == "midp";  
	var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";  
	var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";  
	var bIsAndroid = sUserAgent.match(/android/i) == "android";  
	var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";  
	var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile"; 
	if(wechat||bIsIpad||bIsIphoneOs||bIsMidp||bIsUc7||bIsUc||bIsAndroid||bIsCE||bIsWM){
		var oMeta1 = document.createElement('meta');
		oMeta1.content ="width=device-width,initial-scale=1, maximum-scale=1,user-scalable=no";
		oMeta1.name = "viewport";
		var oMeta2 = document.createElement('meta');
		oMeta2.content ="black";
		oMeta2.name = "apple-mobile-web-app-capable";
		var oMeta3 = document.createElement('meta');
		oMeta3.content ="yes";
		oMeta3.name = "apple-mobile-web-app-capable";
		var oMeta4 = document.createElement('meta');
		oMeta4.content ="email=no";
		oMeta4.name = "format-detection";
		var oMeta5 = document.createElement('meta');
		oMeta5.content ="telephone=no";
		oMeta5.name = "format-detection";
		document.getElementsByTagName('head')[0].appendChild(oMeta1);
		document.getElementsByTagName('head')[0].appendChild(oMeta2);
		document.getElementsByTagName('head')[0].appendChild(oMeta3);
		document.getElementsByTagName('head')[0].appendChild(oMeta4);
		document.getElementsByTagName('head')[0].appendChild(oMeta5);
	}else{
		$("#login").css("width","400px");
		$("#register").css("width","400px");
	}
});

var one =true;//防止二次提交
function login(){
	if(!one){
		return;
	}
	one=false;
	$("#tip").html("");
	//获取用户名
	var username = $("#username").val();
	var password = $("#password").val();
	if(username==""){
		$("#tip").html("用户名不能为空");
		one=true;
		return;
	}
	if(password==""){
		$("#tip").html("密码不能为空");
		one=true;
		return;
	}
	password=hex_md5(password);
	$.ajax({ 
		url: "${pageContext.request.contextPath }/huChat/huChatAction!login.action",
		type: "POST",
		dataType:'json', 
		data : {"username":username,"password":password},
		success: function (data) {
			if(data.state=="success"){
				window.location.href='${pageContext.request.contextPath}/huChat/huChatAction!huChat.action';
			    return;
			}else{
				$("#tip").html("用户名或密码错误");
				one=true;
				return;
			}
		},
		error : function (data) {
		    $("#tip").html("系统异常，请稍后重试");
		    one=true;
			return;
		}
	});
}

function  toLogin(){
	$("#login").show();
	$("#register").hide();
}
function  toRegister(){
	$("#login").hide();
	$("#register").show();
}
var seven =true;//防止二次提交
function register(){
	if(!seven){
		return;
	}
	seven=false;
	//获取用户名
	var username2 = $("#username2").val();
	var password2 = $("#password2").val();
	var password3 = $("#password3").val();
	
	if(username2==""){
		$("#tip2").html("用户名不能为空");
		seven=true;
		return;
	}
	if(password2==""){
		$("#tip2").html("密码不能为空");
		seven=true;
		return;
	}
	if(password3==""){
		$("#tip2").html("请再次输入密码");
		seven=true;
		return;
	}
	if(password2!=password3){
		$("#tip2").html("两次输入的密码不同");
		seven=true;
		return;
	}
	password2=hex_md5(password2);
	password3=hex_md5(password3);
	$.ajax({ 
		url: "${pageContext.request.contextPath }/huChat/huChatAction!register.action",
		type: "POST", 
		data : {"username2":username2,"password2":password2,"password3":password3},
		success: function (data) {
			if(data=="success"){
				$("#tip2").html("注册成功");
				seven=true;
			    return;
			}else if(data=="no_same"){
				$("#tip2").html("两次输入的密码不同");
				seven=true;
			    return;
			}else if(data=="isHave"){
				$("#tip2").html("用户名已存在");
				seven=true;
			    return;
			}else{
				$("#tip2").html("系统异常，请重试");
				seven=true;
				return;
			}
		},
		error : function (data) {
			$("#tip2").html("系统异常，请稍后重试");
		    seven=true;
			return;
		}
	});
}
function loginQQ(){
	window.location.href='${pageContext.request.contextPath}/huChat/huChatAction!loginQQ.action';
}
</script>
</head>

<body>
	<div id="m" style="width:100%;height:0px;"></div>
	<div id="login">
		<div id="title">胡聊</div>
		<div id="tip"></div>
		<div id="username_password">
			<div class="div0">
				<div class="div1">用户名:</div>
				<div class="div2"><input type="text" placeholder="请输入用户名" id="username" name="username"/></div>
			</div>
			<div class="div0">
				<div class="div1">密码:</div>
				<div class="div2"><input type="password"  placeholder="请输入密码" id="password" name="password"/></div>
			</div>
		</div>
		<div id="submit"><input class="do" type="button" value="登录" onclick="login();"/><span class="go" onclick="toRegister();">&nbsp;-->去注册</span></div>
	<div id="lo">其他账号登录：<img onclick="loginQQ();" src="${pageContext.request.contextPath }/images/QQ.png"></div>
	</div>
	<div id="register">
		<div id="title">胡聊注册</div>
		<div id="tip2"></div>
		<div id="username_password">
			<div class="div0">
				<div class="div1">用户名:</div>
				<div class="div2"><input type="text" placeholder="请输入用户名" id="username2" name="username2"/></div>
			</div>
			<div class="div0">
				<div class="div1">密码:</div>
				<div class="div2"><input type="password"  placeholder="请输入密码" id="password2" name="password2"/></div>
			</div>
			<div class="div0">
				<div class="div1">确认密码:</div>
				<div class="div2"><input type="password"  placeholder="请输入密码" id="password3" name="password3"/></div>
			</div>
		</div>
		<div id="submit"><span class="go" onclick="toLogin();">登录<--&nbsp;</span><input class="do" type="button" value="注册" onclick="register();"/></div>
		<div id="lo">其他账号登录：<img onclick="loginQQ();" src="${pageContext.request.contextPath }/images/QQ.png"></div>
	</div>
</body>
</html>