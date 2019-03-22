<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>胡聊</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.10.1.min.js"></script>
<script src="${pageContext.request.contextPath }/js/jquery.lazyload.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath }/js/md5.js" type="text/javascript"></script>
<script type="text/javascript">
	var IMG_OPEN = '${requestScope.IMG_OPEN}';
	var VEDIO_OPEN = '${requestScope.VEDIO_OPEN}';
	var AUDIO_OPEN = '${requestScope.AUDIO_OPEN}';
	var IMG_SIZE = '${requestScope.IMG_SIZE}';
	var VEDIO_SIZE = '${requestScope.VEDIO_SIZE}';
	var AUDIO_SIZE = '${requestScope.AUDIO_SIZE}';
	var ws;
	var url = window.location.href;
	var s="ws";
	if(url.indexOf("https")>=0){
		s="wss";
	}
	//监控滚动的原始高度，这个指挥在滚动中加载一次
	var height=0;//滚动条初始高度
	var nowHeight=0;//滚动条现在的高度
	//var windowHeight=0;//可视高度
	var  flag=true;
	var fs=false;//是否是正在发送
	var scroll=true;
	var target=s+":"+window.location.host+"/huChat/chatServer";
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
		if(true){
		//if(wechat||bIsIpad||bIsIphoneOs||bIsMidp||bIsUc7||bIsUc||bIsAndroid||bIsCE||bIsWM){
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
			$("#right").hide();
			$("#left").width($("#info").width());
		}
		getHuChatInfosNums();
		getHuChatInfos();
		addVisit();
		connect();
	    document.onkeydown = function(e){
			 var ev = document.all ? window.event : e;
			   //换行
		       if(ev.keyCode==13){
		    	   send();
		    	   if (window.event) {
		    		      window.event.returnValue = false;
		    	   } else {
		    		      e.preventDefault(); //for firefox
		    		}
		       }
		 } 
		 
		function systemConsole() {
			$(".size span").html($(".backmsg").length);
			//$("#left #content").scrollTop($("#left #content")[0].scrollHeight );
	    }
	    setInterval(systemConsole, 1000);
	    
	    
		$("#info").scroll(function(){
			var scrollTop = $(this).scrollTop();
			if(flag){
				height=scrollTop;
				windowHeight = $(this).height();//可视高度
				flag=false;
			}
			nowHeight=scrollTop;
			console.log("height:"+height);
			console.log("nowHeight:"+nowHeight);
			//console.log("windowHeight:"+windowHeight);//滚动条高度 */
			if(height<nowHeight){
				//这个就相当于用户主动滑到最下面
				height=nowHeight;
			}
			if(height==scrollTop){
				//如果这两个等就证明用户已经查看了信息
				console.log("用户查看信息");
				$(".noread").hide();
            	$(".noread").html("0");
			}
			if(scrollTop==0){
				if(scroll){
					scroll=false;
					getHuChatInfos();
				}
			}
	    });
		/* var w = document.getElementById("content");
		w.onscroll=function(e)
		{
		    console.log(w.scrollTop);
		} */
		 
	});
	//window.onload=connect;
	function connect(){
		if('WebSocket' in window){
			ws=new WebSocket(target);
		}else if('MozWebSocket' in window){
			ws=new MozWebSocket(target);
		}else{
			alert('WebSocket is not supported by this browser.');
			return;
		}
		ws.onerror = function(){
			//alert("发送错误");
		}
		ws.onopen = function(){
			//alert("建立连接")
		}
		ws.onclose = function(){
			//alert("关闭WebSocket连接")
		}
		//在打开连接后开启消息监听
   		ws.onmessage=function(event){
        	 if(typeof(event.data)=="string"){  
        		 eval("var result="+event.data);
        		 if(result.uuid!=undefined){
					//当前用户进来以及发送东西，都是会有uuid的
        			 $("#uuid").val(result.uuid);
        		 }
        		 if(result.quick!=undefined){
        			 var str="<div class='tip'>发送频率太快</div>";
              		 $("#info").append(str);
		             $("#info").scrollTop($("#info")[0].scrollHeight );
        		 }
        		 if(result.logon!=undefined){
        			 var str="<div class='tip'>已下线，在别的终端登录</div>";
              		 $("#info").append(str);
              		 var gd=height-nowHeight;
	                    console.log("滚动的高度:"+gd);
	                    if(gd<50){
	                    	flag=true;//高度可以重新计算
		             		$("#info").scrollTop($("#info")[0].scrollHeight );
	                    }
        		 }
             	//根据消息类型判断
             	if(result.msgType!=undefined){
             		//到来
             		if(result.msgType=="1"||result.msgType=="2"){
             			var str="<div class='tip'>"+htmlEncode(result.content)+"</div>";
                 		$("#info").append(str);
                 		var gd=height-nowHeight;
	                    console.log("滚动的高度:"+gd);
	                    if(gd<50){
	                    	flag=true;//高度可以重新计算
		             		$("#info").scrollTop($("#info")[0].scrollHeight );
	                    }
             		}
					//正常聊天
					if(result.msgType=="3"){
						var str="";
	             		str+='<div class="backmsg">';
	             		str+='<div class="div_username">';
	             		var imgUrl = result.imgUrl;
	             		if(imgUrl=="NONE"){
	             			str+='<IMG SRC="${pageContext.request.contextPath }/images/huChat.png">&nbsp;';
	             		}else{
	             			str+='<IMG SRC="'+imgUrl+'">&nbsp;';
	             		}
	             		str+=htmlEncode(result.username)+'</div>';
	             		str+='<div class="div_time">'+htmlEncode(result.create_datetime)+'</div>';
	             		var visible = result.visible;
	             		if(visible=="1"){
	             			if(result.content!=""){
		             			str+='<div class="div_msg">'+htmlEncode(result.content)+'</div>';
	                 		}
		             	    if(result.fileType=="1"){
		             			//图片:这里加一个开关，以防万一可以关掉
		             			if(IMG_OPEN=="1"){
		             				
		             				str+='<div class="imges"><img src="${pageContext.request.contextPath }'+result.path+'"/></div>';
		             			}
		             		} 
	                 	 	 if(result.fileType=="2"){
		             			//图片:这里加一个开关，以防万一可以关掉
		             			if(AUDIO_OPEN=="1"){
		             				str+='<audio controlslist="nodownload" controls preload="metadata"><source src="${pageContext.request.contextPath }'+result.path+'" type="audio/mpeg"></audio>';
			             			//str+='<div class="imges"><img class="lazy" src="${pageContext.request.contextPath }/images/grey.gif"  data-original="${pageContext.request.contextPath }'+this.path+'"/></div>';
		             			}
		             		}
	                 		if(result.fileType=="3"){
		             			//图片:这里加一个开关，以防万一可以关掉
		             			if(VEDIO_OPEN=="1"){
		             				str+='<video controlslist="nodownload" controls preload="none" poster="${pageContext.request.contextPath }/images/vedio.png"><source src="${pageContext.request.contextPath }'+result.path+'" type="video/mp4"></video>';
			             			//str+='<div class="imges"><img class="lazy" src="${pageContext.request.contextPath }/images/grey.gif"  data-original="${pageContext.request.contextPath }'+this.path+'"/></div>';
		             			}
		             		}  
	             		}else{
	             			str+='<div class="vd"><div class="error">资源文件需要审核后才能显示</div></div>';
	             		}
	             		
	             		str+='</div>';
	             		$("#info").append(str);
	             		/*  $("img.lazy").lazyload({ 
	             			container: $("#info") 
	                    });  */
	                    //var num = $(".imges img").length;
	                   // var mh =$(".backmsg:last").height();
	                   // console.log("mh"+mh);
	                     var gd=height-nowHeight;
	                    console.log("滚动的高度:"+gd); 
	                    //console.log("可视范围高度："+windowHeight);
	                   /*  $("#info").scrollTop($("#info")[0].scrollHeight );
	                    $(".imges img").load(function() {
	                    	$("#info").scrollTop($("#info")[0].scrollHeight );
	                    }); */
	                    if(fs){
	                    	$(".imges img").load(function() {
		                    	$("#info").scrollTop($("#info")[0].scrollHeight );
		                    });
	                    	fs=false;
	                    }
	                    if(gd<50){
	                    //if(gd<=windowHeight){
	                    	flag=true;//高度可以重新计算
	                    	$("#info").scrollTop($("#info")[0].scrollHeight );
		                    $(".imges img").load(function() {
		                    	$("#info").scrollTop($("#info")[0].scrollHeight );
		                    });
	                    }else{
	                    	//不用,在发送消息后会自动同步高度,不滚动的话，要记录当前没有阅读的条数
	                    	$(".noread").show();
	                    	$(".noread").html(parseInt($(".noread").html())+1);
	                    }
	                    $(".all span").html(parseInt($(".all span").html())+1);
	                    //将uuid保存，必须等上一条记录发送完后再能继续发送
					}
					//看看文件列表
					if(result.usernames!=undefined){//若消息中含有用户列表，则加上用户列表
	             		var div=$(".user");
	             		var str="";
	             		var index=0;
	             		$(result.usernames).each(function(){
	             			//alert(this);
	             			str+='<div class="users">'+htmlEncode(this)+'</div>';
	             			index++;
	             		});
	             		div.html(str);
	             		$(".num span").html($(result.usernames).length);
	             		$(".user_num").html($(result.usernames).length);
	             	}
             	}
        	 }/* else{  
        		    //alert("二进制:"+event);
        	        var reader = new FileReader();  
        	        reader.onload = function(event){  
        	            if(event.target.readyState == FileReader.DONE){  
        	                var url = event.target.result;  
        	                $("#left #content").append("<img src = "+url+" />");
        	            }
        	       }  
        	       reader.readAsDataURL(event.data);  
        	}  */
        	
        }
  }
	function send(){
		var uuid=$("#uuid").val();
		if(uuid==""){
			var str="<div class='tip'>发送频率太快</div>";
     		$("#info").append(str);
            $("#info").scrollTop($("#info")[0].scrollHeight );
            return;
		}
		var md5 = hex_md5(uuid);
		var str=$("#msg").val().replace(/[\r\n]/g,"").replace(/^\s+|\s+$/g, "");//去掉回车换行和空格
		if(str!=""){
			//var msg=str+"|"+""+"|"+md5;
			var msg= "{\"msg\": \""+str+"\", \"path\": \"\",\"md5\":\""+md5+"\"}";
			ws.send(msg);
			$("#msg").val("");
			$("#msg").focus();
			$("#uuid").val("");
			return;
		}
    	getFilePath(md5);
    	fs=true;//正在发送
		$("#info").scrollTop($("#info")[0].scrollHeight);
	}
	
	//发送消息
   /*  var i=10485760;//图片大小不能超过10M
    var a=10485760*3;//音频大小不能超过30M
    var v=10485760*5;//视频大小不能超过50M */
    var i=parseInt(IMG_SIZE);
    var a=parseInt(AUDIO_SIZE);
    var v=parseInt(VEDIO_SIZE);
    function getFilePath(md5){
    	var dom = document.getElementById("file"); 
    	if(dom.value!=""){
    		var size =dom.files.item(0).size;
        	
        	console.log("dom:"+dom);
        	console.log("size:"+size);
        	var name = dom.files.item(0).name;
        	name = name.toLowerCase();
        	//用户只可以上传图片：jpg/jpeg/png/gif 音频mp3 视频 mp4
        	if(name.indexOf("jpg")!=-1||name.indexOf("jpeg")!=-1||name.indexOf("png")!=-1||name.indexOf("gif")!=-1){
        		//用户上传的是图片，判断有没有开放图片上传权限
        		if(IMG_OPEN=="0"){
        			alert("未开放图片上传功能，敬请期待");
        			return;
        		}
        		if(size>i){
        			alert("文件太大,不能超过10M");
            		return;
            	} 
        	}else if(name.indexOf("mp4")!=-1){
        		if(VEDIO_OPEN=="0"){
        			alert("未开放视频上传功能，敬请期待");
        			return;
        		}
        		if(size>v){
        			alert("文件太大,不能超过50M");
            		return;
            	} 
        	}else if(name.indexOf("mp3")!=-1){
        		if(AUDIO_OPEN=="0"){
        			alert("未开放音频上传功能，敬请期待");
        			return;
        		}
        		if(size>a){
            		alert("文件太大,不能超过30M");
            		return;
            	} 
        	}else{
        		alert("文件格式不对，请上传图片，MP3或者MP4");
        		return;
        	}
        	
        	console.log("name:"+name);
        	//到这里就没问题了 可以直接去上传
        	uploadFile(md5);
        	
    	}
    }
    var two = true;
	function uploadFile(md5) {  
		if(!two){
			return;
		}
		two=false;
		$("#fileName").html("发送中...");
	    var formData = new FormData($("#fileForm")[0]);
	    $.ajax({  
	          url: '${pageContext.request.contextPath }/huChat/fileAction!uploadFile.action' ,  
	          type: 'POST',  
	          data: formData,  
	          async: true,  
	          cache: false,  
	          contentType: false,  
	          processData: false,  
	          success: function (data) { 
	        	  $("#fileName").html("发送");
			      if(data=="file_size"){
			    	  alert("文件太大");
			    	  two=true;
		        	  return;
				  }
			      if(data=="error"){
			    	  alert("文件上传失败");
			    	  two=true;
		        	  return;
				  }
			      two=true;
			      console.log("文件路径："+data);
			     // var msg= {"msg": "", "path": data,"md5":md5}
			      var msg= "{\"msg\": \"\", \"path\":\""+data+"\",\"md5\":\""+md5+"\"}";
			      ws.send(msg);
			      $("#uuid").val("");
			      $("#file").val("");
			      $("#fileName").html("文件");
			    //  $("#path").val(data);
	          },  
	          error: function (data) { 
	        	  $("#fileName").html("发送");
	              alert("文件上传失败"); 
	              two=true;
	              return; 
	          }  
	     });  
	 }  
	function htmlEncode(html){
		  //1.首先动态创建一个容器标签元素，如DIV
	  var temp = document.createElement ("div");
	  //2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
	  (temp.textContent != undefined ) ? (temp.textContent = html) : (temp.innerText = html);
	  //3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
	  var output = temp.innerHTML;
	  temp = null;
	  return output;
	}	
	$(function(){
		$(".file").on("change","input[type='file']",function(){
		    var filePath=$(this).val();
		    if(filePath!=""){
		    	$("#fileName").html(filePath);
		    }else{
		    	$("#fileName").html("文件");
		    }
		})
	});
	
	function see_more(){
		$("#more").animate({width:"60%"});
	}
	function close_more(){
		if($("#more").width()!=0){
			$("#more").animate({width:"0px"});
		}
	}
	/**
	*去到某一主题
	*/
	function toBK(){
		window.location.href='/itsb/blog/blogViewAction!home.action';
	}
	/**
	*去到蛋黄盘
	*/
	function toDHP(){
		window.location.href='/itmgr/dhp/dhpAction!dhp.action';
	}
	/**
	*去到蛋黄盘
	*/
	function login(){
		window.location.href='${pageContext.request.contextPath }/huChat/huChatAction!login1.action';
	}
	/**
	*去到蛋黄盘
	*/
	function logon(){
		window.location.href='${pageContext.request.contextPath }/huChat/huChatAction!logon.action';
	}
	function colorBG(obj){
		$("#info").css("background",obj);
	}
	var page="0";
	var create_datetime='${requestScope.create_datetime}';
	var three=true;
	function getHuChatInfos(){
		if(!three){
			return;
		}
		three=false;
		 $(".ing").show();
  		$.ajax({ 
  			 url: "${pageContext.request.contextPath }/huChat/huChatAction!getHuChatInfos.action",
  	         type: "POST", 
  	         dataType:'json',
  	         data : {"page" : page,"create_datetime":create_datetime},
  	         success: function (data) {
  	        	//$(".msg").hide();
  	        	 if(data!=''){
  	        		 //获取data中的内容
  	        		 page =data.page;
  	        		 var infos =data.infos;
  	        		 if(infos==undefined){
  	        			 $(".ing").html("未查询到记录");
  	        			three=true;
  	        			scroll=false;
  	        			return;
  	        		 }
  	        		 $(".ing").remove();
  	        		 var str='<div class="ing">正在加载中...</div>';
  	        		 for(var i=infos.length-1;i>=0;i--){
  	        			str+='<div class="backmsg">';
  	        			str+='<div class="div_username">';
  	        			var imgUrl = infos[i].imgUrl;
	             		if(imgUrl=="NONE"){
	             			str+='<IMG SRC="${pageContext.request.contextPath }/images/huChat.png">&nbsp;';
	             		}else{
	             			str+='<IMG SRC="'+imgUrl+'">&nbsp;';
	             		}
                 		str+=htmlEncode(infos[i].username)+'</div>';
                 		str+='<div class="div_time">'+htmlEncode(infos[i].create_datetime)+'</div>';
                 		var visible = infos[i].visible;
                 		if(visible=="1"){
                 			if(infos[i].content!=""){
                     			str+='<div class="div_msg">'+htmlEncode(infos[i].content)+'</div>';
                     		}
                     		if(infos[i].fileType=="1"){
    	             			//图片:这里加一个开关，以防万一可以关掉
    	             			if(IMG_OPEN=="1"){
    		             			str+='<div class="imges"><img class="lazy" src="${pageContext.request.contextPath }/images/grey.gif"  data-original="${pageContext.request.contextPath }'+infos[i].path+'"></div>';
    	             			}
    	             		}
                     		if(infos[i].fileType=="2"){
    	             			//图片:这里加一个开关，以防万一可以关掉
    	             			if(AUDIO_OPEN=="1"){
    	             				str+='<div class="ad"><audio controlslist="nodownload" controls preload="metadata"><source src="${pageContext.request.contextPath }'+infos[i].path+'" type="audio/mpeg"></audio></div>';
    		             			//str+='<div class="imges"><img class="lazy" src="${pageContext.request.contextPath }/images/grey.gif"  data-original="${pageContext.request.contextPath }'+this.path+'"/></div>';
    	             			}
    	             		}
                     		if(infos[i].fileType=="3"){
    	             			//图片:这里加一个开关，以防万一可以关掉
    	             			if(VEDIO_OPEN=="1"){
    	             				//None：不进行预加载。使用此属性值，可能是页面制作者认为用户不期望此视频，或者减少HTTP请求。
    　　								//Metadata：部分预加载。使用此属性值，代表页面制作者认为用户不期望此视频，但为用户提供一些元数据（包括尺寸，第一帧，曲目列表，持续时间等等）。
    　　								//Auto：全部预加载。
    	             				str+='<div class="vd"><video controlslist="nodownload" controls preload="none" poster="${pageContext.request.contextPath }/images/vedio.png"><source src="${pageContext.request.contextPath }'+infos[i].path+'" type="video/mp4"></video></div>';
    		             			//str+='<div class="imges"><img class="lazy" src="${pageContext.request.contextPath }/images/grey.gif"  data-original="${pageContext.request.contextPath }'+this.path+'"/></div>';
    	             			}
    	             		}	
                 		
                 		}else{
                 			str+='<div class="vd"><div class="error">资源文件需要审核后才能显示</div></div>';
                 		}
                 		
                 		str+='</div>';
                 		if(i==0){
                 			str+='<div id="page_'+page+'" class="tip">'+htmlEncode(infos[i].create_datetime)+'</div>';
                 		}
                 		
  	        		 }
  	        		 $("#info").prepend(str);
            		 $("img.lazy").lazyload({ 
            			container: $("#info") 
                     }); 
            		 if(page=="1"){
                    	$(".lazy:last").load(function(){
                    	 	$("#info").scrollTop(document.getElementById("page_"+page).offsetTop-80);
                     	}); 
            		 }
            	     $("#info").scrollTop(document.getElementById("page_"+page).offsetTop-80);
  	        		 three=true;
  	        		 scroll=true;
  	        		 $(".ing").hide();
  	        		// alert(document.getElementById("page_"+page).offsetTop); 
  	        	 }else{
  	        		three=true;
  	        		scroll=false;
  	        		 $(".ing").html("未查询到记录");
  	        	 }
  	         },
  	         error : function (data) {
  	        	 three=true;
  	        	 scroll=false;
  	        	 $(".ing").html("未查询到记录");
  	         }
  	 	});
  	}
	//获取总记录数
	function getHuChatInfosNums(){
  		$.ajax({ 
  			 url: "${pageContext.request.contextPath }/huChat/huChatAction!getHuChatInfosNums.action",
  	         type: "POST", 
  	         data : {"create_datetime":create_datetime},
  	         success: function (data) {
  	        	 $(".all span").html(data);
  	         },
  	         error : function (data) {
  	         }
  	 	});
  	}
	//异步插入一条浏览记录
	function addVisit(){
		$.ajax({ 
				 url: "${pageContext.request.contextPath }/huChat/huChatAction!addVisit.action",
		         type: "POST", 
		         data : {"url":location.href},
		         success: function (data) {
		         },
		         error : function (data) {
		         }
		 });
	}
	function loginQQ(){
		window.location.href='${pageContext.request.contextPath}/huChat/huChatAction!loginQQ.action';
	}
	function toAuthority(){
		window.location.href='${pageContext.request.contextPath}/huChat/huChatAction!toAuditing.action';
	}
</script>
<style type="text/css">
* {
	padding: 0px;
	margin: 0px;
}

#div1 {
	width: 100%;
	margin:0px auto;
	height: 100%;
	/* display: fixed; */
	position:fixed;
	bottom: 0px;
	top:0px;
	background: #fff;
}

#top {
	width: 100%;
	height: 40px;
	line-height:40px;
	text-align: center;
	font-size: 19px;
	font-family: "微软雅黑";
	white-space:nowrap;
	overflow:hidden;
	text-overflow:ellipsis;
	background: #F5F5F5;
	position:fixed;
	top:0px;
	z-index:2;
	border-bottom: 1px solid #bbb;
}
#check{
   position:fixed;
   top:0px;
   z-index:2;
   right:0px;
   height:40px;
}
#huliao{
   position:fixed;
   top:0px;
   z-index:2;
   left:5px;
}
#huanyin{
	color:#CD6600;
	font-size: 18px;
}
#info {
	width: 100%;
	background: #fff;
	overflow: auto;
	-webkit-overflow-scrolling : touch;
	position:fixed;
    top: 41px;
    padding-top: 10px;
    bottom: 101px;
}
.backmsg {
	width:95%;
	margin:0 auto;
	margin-bottom: 30px;
}
.div_username{
	width:100%;
	color:#CD6600;
	font-size: 18px;
	height:30px;
	line-height: 30px;
	white-space:nowrap;
	overflow:hidden;
	text-overflow:ellipsis;
	padding-bottom: 2px;
}
.div_username img{
    HEIGHT: 100%;
    vertical-align: middle;
}
.div_time{
	width:100%;
	font-size: 15px;
	height:25px;
	line-height: 25px;
	white-space:nowrap;
	overflow:hidden;
	text-overflow:ellipsis;
}
.div_msg{
	overflow: auto;
	word-wrap: break-word; 
	font-size: 20px;
	background: #f4f4f4;
	padding: 5px 10px;
	border-radius: 10px;
}
.imges{
width:100%;
}
.imges img{
max-width: 100%;
height: auto;
border-top: 1px solid #fff;
}
.ad{
width:100%;
}
.vd{
width:100%;
}
video{
width:80%;
height:auto;
border-top: 1px solid #fff;
}
audio{
width:80%;
height:55px;
border-top: 1px solid #fff;
}
#info .tip {
	font-size: 14px;
	font-family: 微软雅黑;
	text-align: center;
	width: 100%;
	margin: 0px auto;
	margin-bottom: 10px;
	color:#CD6600;
}

#input {
	width: 100%;
	text-align: center;
	position:fixed;
	bottom:0px;
	height:101px;
	z-index: 2;
	/* border-top: 2px solid #bbb; */
	
}
#msg {
	width:100%;
	height:60px;
	position:fixed;
	bottom:40px;
	z-index: 2;
	left:0px;
	right:0px;
	font-size: 20px;
	outline:none;
	resize:none;
	overflow:auto;
	padding-right:10px;
	padding-left:10px;
	padding-top:1px;
	background-attachment:fixed;
	background-repeat:no-repeat;
	border-color:#FFFFFF;
	box-sizing:border-box;
	border: 1px solid #bbb;
}
#msg::-webkit-input-placeholder {
       /* placeholder颜色  */
       color: #aab2bd;
       /* placeholder字体大小  */
       font-size: 12px;
    }
#btn{
	position:fixed;
	bottom:5px;
	height:30px;
	line-height:30px;
	z-index: 2;
	background: #F5F5F5;
	display:block;
	right:10px;
	padding-left: 10px;
	padding-right: 10px;
	border: 1px solid #bbb;
}



.noread{
	background: #96D7F0;
	color:#fff;
	border-radius: 100%;
	display: inline-block;
	padding-left: 5px;
	padding-right: 5px;
	box-sizing:border-box;
	position: fixed;
	bottom: 101px;
	right:20px;
	display:none;
}
.ing{
	 height:30px;
	width:100%;
	 color:#CD6600;
	 text-align: center;
	 }
.num{
	background: #96D7F0;
	border-radius: 5px;
	display: inline-block;
	padding-left: 5px;
	padding-right: 5px;
	box-sizing:border-box;
	position: fixed;
	top: 50px;
	right:20px;
}
.num span{
	color:#CD6600;
}
.size span{
color:#CD6600;
}
.size{
	background: #96D7F0;
	border-radius: 5px;
	display: inline-block;
	padding-left: 5px;
	padding-right: 5px;
	box-sizing:border-box;
	position: fixed;
	top: 80px;
	right:20px;
}
.all span{
color:#CD6600;
}
.all{
	background: #96D7F0;
	border-radius: 5px;
	display: inline-block;
	padding-left: 5px;
	padding-right: 5px;
	box-sizing:border-box;
	position: fixed;
	top: 110px;
	right:20px;
}

/*************************/
.file {
    position:fixed;
    right: 80px;
	bottom:5px;
	height:30px;
	line-height:30px;
    display: inline-block;
    background: #F5F5F5;
    border-radius: 4px;
    overflow: hidden;
    padding-left: 10px;
	padding-right: 10px;
    text-decoration: none;
    text-indent: 0;
    border: 1px solid #bbb;
}
.file input {
    width:100%;
    position: absolute;
    font-size: 100px;
    right: 0;
    top: 0; 
    opacity: 0;
}
/* .file:hover {
    background: #AADFFD;
    border-color: #78C3F3;
    color: #004974;
    text-decoration: none;
} */
/*************查看更多*************/
#more{
	width: 0px;
	margin:0px auto;
	height: 100%;
	position:fixed;
	bottom: 0px;
	top:0px;
	right:0px;
	z-index: 2;
	border-left: 1px solid #bbb;
	background: #F5F5F5;
	overflow: auto;
	
}
#more_top{
	height:40px;
	width:100%;
	overflow: hidden;
	line-height: 40px;
}
.more_top_left{
	float: left;
	width:20%;
	height:100%;
	text-align: left;
}
.more_top_right{
	float: right;
	width:80%;
	height:100%;
	text-align: right;
	padding-right: 20px;
	box-sizing:border-box;
}
.more_top_right img{
	vertical-align: middle;
}
.more_top_right div{
	display: inline-block;
}
#close{
   height:100%;
}
.more_div{
	width:90%;
	margin: 0px auto;
	font-size: 16px;
	margin-bottom: 10px;
}
.more_div_title{
	color:#666;
}
.more_div_info{
	color:#000;
	margin-top: 5px;
}
.web{
	font-size: 0px;
}
.webs{
	display: inline-block;
	font-size: 16px;
	margin-right: 10px;
}
.webs:hover{
	 color:#CD6600;
	 cursor: pointer;
}
.user{
	overflow: auto;
	max-height: 200px; 
}
.users{
	height:25px;
	line-height: 25px;
	color:#CD6600;
	white-space:nowrap;
	overflow:hidden;
	text-overflow:ellipsis;
}
.user_num{
color:#CD6600;
}
.error{
color:red;
}
</style>
  </head>
  <body>
  <img src="${pageContext.request.contextPath }/images/huChat.png" style="height:0px;width:100%;">
  	<input type="hidden" id="path">
  	<input type="hidden" id="uuid">
	<div id="div1" onclick="close_more();">
		<div id="top">
			<span id="huliao">胡聊</span>
			<span id="huanyin">欢迎${sessionScope.username}</span>
			<img id="check" onclick="see_more();" src="${pageContext.request.contextPath }/images/check.png">
		</div>
		<div id="info">
			<div class="ing">正在加载中...</div>
		</div>
		<span class="noread">0</span>
		<span class="num">在线<span>0</span></span>
		<span class="size">记录数<span>0</span></span>
		<span class="all">总记录数<span>0</span></span>
		<div id="input">
			<textarea id="msg" placeholder="文字和文件不能同时发送"></textarea>
			<form id="fileForm">
			<span  class="file"><span id="fileName">文件</span>
    			  <input type="file" name="file" id="file">
    		</span>
				</form>
			<span id="btn" onclick="send();">发送</span>
		</div>
	</div>
	
	<div id="more">
		<div id="more_top">
			<div class="more_top_left">
				<img id="close" onclick="close_more();" src="${pageContext.request.contextPath }/images/close.png">
			</div>
			<s:if test="#request.login=='true'">
				<div class="more_top_right" onclick="logon();">
					注销
				</div>
			</s:if>
			<s:else>
				<div class="more_top_right">
					<div  onclick="login();">登录</div>&nbsp;|&nbsp;<img onclick="loginQQ();" src="${pageContext.request.contextPath }/images/qq_quick.png">
				</div>
			</s:else>
		</div>
		<div class="more_div">
			<div class="more_div_title">群名</div>
			<div class="more_div_info">胡聊</div>
		</div>
		<div class="more_div">
			<div class="more_div_title">创始人</div>
			<div class="more_div_info">林蛋黄</div>
		</div>
		<div class="more_div">
			<div class="more_div_title">公告</div>
			<div class="more_div_info">禁止发表不良言论，上传违法图像，音频，视频</div>
		</div>
		<div class="more_div">
			<div class="more_div_title">链接</div>
			<div class="more_div_info web">
				<s:if test="#request.authority=='YES'">
					<div class="webs" onclick="toAuthority();">审核</div>
				</s:if>
				<div class="webs" onclick="toBK();">博客</div><div class="webs" onclick="toDHP();">蛋黄盘</div>
			</div>
		</div>
		<div class="more_div">
			<div class="more_div_title">背景</div>
			<div class="more_div_info web">
				<div class="webs" onclick="colorBG('#fff');" style="color:#fff">白色</div><div class="webs" onclick="colorBG('#000');" style="color:#000">黑色</div>
			</div>
		</div>
		<div class="more_div">
			<div class="more_div_title">在线用户<span class="user_num">0</span>人</div>
			<div class="more_div_info user">
			</div>
		</div>
	</div>
  </body>
</html>