<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <title>胡聊审核</title>
	<link href="" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.10.1.min.js"></script>
	<script>
	$(function(){
		if(true){
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
		}
		getHuChatInfos();
	});
	var page="0";
	var three=true;
	function getHuChatInfos(){
		if(!three){
			return;
		}
		three=false;
  		$.ajax({ 
  			 url: "${pageContext.request.contextPath }/huChat/huChatAction!getHuChatFileInfo.action",
  	         type: "POST", 
  	         dataType:'json',
  	         data : {"page" : page},
  	         success: function (data) {
  	        	 if(data!=''){
  	        		 if(data.result=="authority"){
  	        			 alert("没有权限");
  	        			three=true;
  	        			 return;
  	        		 }
  	        		 //获取data中的内容
  	        		 page =data.page;
  	        		 var infos =data.infos;
  	        		 if(infos==undefined){
  	        			alert("未查询到记录");
  	        			three=true;
  	        			return;
  	        		 }
  	        		 //循环遍历出来
  	        		 var str='<tr><th>内容</th><th>审核</th></tr>';
  	        		 for(var i=0;i<infos.length;i++){
  	        			var fileType=infos[i].fileType;
  	        			var id=infos[i].id;
  	        			str+='<tr id="'+id+'"><td>';
  	        			if(fileType=="1"){
	  						str+='<img  src="${pageContext.request.contextPath }'+infos[i].path+'">';
  	        			}
  	        			if(fileType=="2"){
  	        				str+='<audio  controls preload="metadata"><source src="${pageContext.request.contextPath }'+infos[i].path+'" type="audio/mpeg"></audio>';
  	        			}
  	        			if(fileType=="3"){
  	        				str+='<video  controls preload="none" poster="${pageContext.request.contextPath }/images/vedio.png"><source src="${pageContext.request.contextPath }'+infos[i].path+'" type="video/mp4"></video>';
  	        			}
  						str+='</td><td><button onclick="doP('+id+',1)">通过</button><button onclick="doP('+id+',2)">不通过</button></td></tr>';
  	        		 }
  	        		 $("#info table").html(str);
  	        		 $("#num").html(page);
  	        		three=true;
  	        	 }else{
  	        		three=true;
  	        		 alert("未查询到记录");
  	        	 }
  	         },
  	         error : function (data) {
  	        	 three=true;
  	        	 alert("未查询到记录");
  	         }
  	 	});
  	}
	function doPage(obj){
		var num=parseInt($("#num").html());
		if(obj==2){
			page=num+"";
			getHuChatInfos();
		}
		if(obj==0){
			if(num==1){
				alert("已经是第一页");
			}else{
				page=(num-2)+"";
				getHuChatInfos();
			}
		}
	}
	function doP(id,visible){
		if(visible==1){
			if(window.confirm("审核通过，用户将看的到")){
				doPVisible(id,visible);
			}
		}
		if(visible==2){
			if(window.confirm("审核不通过，将不会再出现在审核列表")){
				doPVisible(id,visible);
			}
		}
	}
	function doPVisible(id,visible){
		$.ajax({ 
 			 url: "${pageContext.request.contextPath }/huChat/huChatAction!doPVisible.action",
 	         type: "POST", 
 	         data : {"id" : id,"visible":visible+""},
 	         success: function (data) {
 	        	 if(data=="authority"){
 	        		 alert("没有权限审核");
 	        	 }
 	        	 if(data=="success"){
 	        		 //审核成功
 	        		 $("#"+id).hide();
 	        	 }
 	        	 if(data=="error_param"){
 	        		alert("参数异常");
 	        	 }
 	         }
		});
	}
	</script>
	<style type="text/css">
		#info{
			width:100%;
			margin: 0px auto;
		}
		table{
			width:100%;
			border-collapse:collapse;
		}
		table td{
			height:100px;
		}
		table td img{
			max-height: 100%;
			max-width: 100%;
		}
		table td video{
			max-height: 100%;
			max-width: 100%;
		}
		table tr td:nth-of-type(1){
			width:80%;
			text-align: center;
		}
		table tr td:nth-of-type(2){
			width:20%;
		}
		table td audio{
			max-height: 100%;
			max-width: 100%;
		}
		button{
			width:100%;
			height:30px;
			margin-top: 10px;
			margin-bottom: 10px;
		}
		#page{
			width:100%;
			height:30px;
			text-align: center;
			line-height: 30px;
		}
		#page input{
			margin-left:10px;
			margin-right:10px;
			height:25px;
		}
	</style>
</head>
<body>
	<!--头部-->
	<div id="info">
			<table border="1">
				<!-- <tr>
					<th>内容</th>
					<th>审核</th>
				</tr>
				<tr>
					<td><img  src="${pageContext.request.contextPath }/uploadFile/images/201811/0921dd5d14574bf3b55da897cbe4918f.png"></td>
					<td><button>通过</button><button>不通过</button></td>
				</tr>
				<tr>
					<td><audio  controls preload="metadata"><source src="${pageContext.request.contextPath }/uploadFile/images/201811/a28027331b3b432ca076455a28d94892.mp3" type="audio/mpeg"></audio></td>
					<td><button>通过</button><button>不通过</button></td>
				</tr>
				<tr>
					<td><video  controls preload="none" poster="${pageContext.request.contextPath }/images/vedio.png"><source src="${pageContext.request.contextPath }/uploadFile/videos/201811/b87c78f3f8af4c5d8fc7f7ee93e6b031.mp4" type="video/mp4"></video></td>
					<td><button>通过</button><button>不通过</button></td>
				</tr> -->
			</table>
			<div id="page"><input type="button" onclick="doPage(0)" value="上一页"><span id="num">1</span><input onclick="doPage(2)" type="button" value="下一页"></div>
		</div>
	<!--底部-->
</body>
</html>