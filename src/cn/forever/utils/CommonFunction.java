package cn.forever.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**
 * 20170421 lwh
 * 公共方法 获取access_token 获取openId等等的
 */
public class CommonFunction {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
	private static ServletContext application;
	public static void setApplication(ServletContext application){
		CommonFunction.application=application;
	}
	public static ServletContext getApplication(){
		return application;
	}
	/**
	 * 这个根据url请求链接获取json格式返回的值
	 * @param link
	 * @return
	 */
	public static Object openConnection(String link){
		if(link==null||"".equals(link)){
			System.out.println("链接不存在");
			return null;
		}
		String json = (String) getConnectionObject(link);
		JsonParser parser = new JsonParser();
		JsonObject object=(JsonObject)parser.parse(json);
		System.out.println("链接返回的值："+object);
		return object;
	}
	/**
	 * 这个根据url请求链接获取返回的值
	 * @param link
	 * @return
	 */
	public static Object getConnectionObject(String link){
		if(link==null||"".equals(link)){
			System.out.println("链接不存在");
			return null;
		}
		HttpURLConnection conn=null;
		InputStream is=null;
		BufferedInputStream bis=null;
		try {
			URL url=new URL(link);
			conn=(HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
            conn.setRequestMethod("GET");
			is=conn.getInputStream();
			bis = new BufferedInputStream(is);
			byte[] bytes = new byte[1024];
			int len = -1;
			StringBuilder sb = new StringBuilder();
			while((len=bis.read(bytes))!=-1){
				sb.append(new String(bytes,0,len));
			}
			String str = sb.toString();
			System.out.println("返回的值是："+str);
			return str;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(conn!=null){
				conn.disconnect();
			}
		}
		return null;
	}

	/**
	 * 解析openId接口返回值
	 * @param openIdStr
	 * @return
	 */
	public static String  getOpenIdFromBackResult(String openIdStr){
		//String str ="callback( {\"client_id\":\"YOUR_APPID\",\"openid\":\"YOUR_OPENID\"} )";
		System.out.println("openIdStr:+"+openIdStr);
		String openId  =openIdStr.substring(openIdStr.lastIndexOf(":\"")+2,openIdStr.lastIndexOf("\"}"));
		return openId;
	}
	public static String getDateTime(){
		return format.format(new Date());
	}
	public static String getUrl(){
		HttpServletRequest request=StrutsParamUtils.getRequest();
		String q = request.getQueryString();
		String url = request.getScheme()+"://"+ request.getServerName()+request.getRequestURI();
		if(q!=null){
			url=url+"?"+q;
		}
		return url;
	}
	//重定向到一个链接
	public static void redirect(String url){
		try {
 			if(url != null && !url.equals("")){
 				System.out.println("要重定向的链接："+url);
 				StrutsParamUtils.getResponse().sendRedirect(url);
 			}
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 		return ;
	}
	
	
	public static String getUUID(){
		return (UUID.randomUUID()+"").replace("-", "");
	}
	/**
	 * 根据类型，返回对应的编号
	 * @param fileType exe  js  jsp  txt...
	 * @return 对应的Const的DHP_SUBTYPE...
	 */
	public static String getSubTypeByFileType(String fileType){
		//String txt ="txt";
		//String exe ="exe";
		//String code="js,jsp,java,css,properties,xml";
		//String html="html";
		String img="jpg,png,gif,jpeg";
		//String music="mp3,wma,rm,wav,midi,ape,flac";
		String music="mp3";
		//String video="wmv,asf,asx,rm,rmvb,mp4,3gp,mov,m4vavi,dat,mkv,flv,vob";
		String video="mp4";
		//String pdf="pdf";
		//String word="doc,docx";
		//String zip="zip,rar,jar,7z";
		//转换成小写
		fileType=fileType.toLowerCase();
		if(img.contains(fileType)){
			return Const.IMG;
		}else if(music.contains(fileType)){
			return Const.AUDIO;
		}else if(video.contains(fileType)){
			return Const.VIDEO;
		}else{
			return "";
		}
	}
	public static  String getIpAddr() {
		HttpServletRequest request =StrutsParamUtils.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
           ip = request.getRemoteAddr();  
       }
       if (ip != null && ip.indexOf(",") != -1) {  
            ip = (ip.split(","))[0];  
       }
       System.out.println("用户的访问ip是："+ip);
       return ip;  
    }
	
	//-------------------------//
	
	/**
	 * 获取用户openId以及放入session中
	 * 放入完后要跳转回来
	 * @throws UnsupportedEncodingException 
	 */
	public static void getOpenId() throws UnsupportedEncodingException{
		String backUrl = getUrl();
		//重定向到腾讯去获取授权码
		String redirect_uri = getRedirect_uri();
		redirect_uri=redirect_uri+"?backUrl="+backUrl;
		System.out.println("去腾讯获取授权码的地址："+redirect_uri);
		//进行UrlEncode编码
		redirect_uri=URLEncoder.encode(redirect_uri, "UTF-8");
		System.out.println("用户URLEncoder的回调地址为："+redirect_uri);
		getCodeFromRedirect_url(redirect_uri);
	}
	
	/**
	 * 获取回调地址
	 * @return
	 */
	public static String getRedirect_uri(){
		HttpServletRequest request=StrutsParamUtils.getRequest();
		//生产环境肯定是https的啦
		String basePath = request.getScheme()+"s://"+request.getServerName();
		String redirect_uri = basePath+"/huChat/huChat/getOpenIdAction!getOpenId.action";
		return redirect_uri;
	}
	/**
	 * 通过回调地址获取code
	 * @param backUrl
	 */
	public static void getCodeFromRedirect_url(String redirect_uri){
		//这里要去调用腾讯的授权接口
		String url = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id="+Const.QQ_APP_ID+
				"&redirect_uri="+redirect_uri+"&state="+System.currentTimeMillis();	
		System.out.println("Const.QQ_APP_ID:"+Const.QQ_APP_ID);
		CommonFunction.redirect(url);
		return ;
	}
	/**
	 * 通过授权码获取用户的openId
	 * @param code
	 * @param backUrl
	 * @return
	 */
	public static void getOpenIdFromCodeAndRedirect_url(String code,String redirect_uri){
		String access_token = getAccessTokenFromCodeAndRedirect_url(code,redirect_uri);
		String openId = getOpenIdFromAccessToken(access_token);
		//如果openId有效，则将这两个信息放入session中
		if(StringUtils.isNotBlank(openId)&&StringUtils.isNotBlank(access_token)){
				StrutsParamUtils.getRequest().getSession().setAttribute("openId", openId);
				StrutsParamUtils.getRequest().getSession().setAttribute("access_token",access_token);
		}
	}
	
	/**
	 * 获取用户的access_token
	 * @param code
	 * @param backUrl
	 * @return
	 */
	public static String getAccessTokenFromCodeAndRedirect_url(String code,String redirect_uri){
		//去获取accesstoken
		String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id="+Const.QQ_APP_ID+
						"&client_secret="+Const.QQ_APP_KEY+"&code="+code+"&redirect_uri="+redirect_uri;
		System.out.println("获取access_token的链接："+url);
		String result = (String) CommonFunction.getConnectionObject(url);
		System.out.println("返回的值："+result);
		//获取用户的access_token access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14
		String access_token="";
		String[] strs = result.split("&");
		if(strs.length>0){
			String str =strs[0];
			String[] ss = str.split("=");
			if(ss.length>0){
					access_token=ss[1];
			}
		}
				
		System.out.println("用户的access_token为："+access_token);
		return access_token;
	} 
	
	/**
	 * 根据access_token去获取openid
	 * @param access_token
	 * @return
	 */
	public static String getOpenIdFromAccessToken(String access_token){
		String openId = "";
		if(StringUtils.isNotBlank(access_token)){
			//去获取openid
			String getOpenIdUrl = "https://graph.qq.com/oauth2.0/me?access_token="+access_token;
			String openIdStr = (String) CommonFunction.getConnectionObject(getOpenIdUrl);
			System.out.println("返回值："+openIdStr);
			openId = getOpenIdFromBackResult(openIdStr);
		}
		return openId;
	}
	/**
	 * 通过openId和access_token获取用户信息
	 * @param openId
	 * @param access_token
	 * @return
	 */
	public static Object getUserInfoFromOpenIdAndAccessToken(String openId,
			String access_token) {
		String url = "https://graph.qq.com/user/get_user_info?access_token="+access_token+
				"&oauth_consumer_key="+Const.QQ_APP_ID+"&openid="+openId;
		System.out.println("获取用户信息的url:"+url);
		Object object =openConnection(url);
		return object;
	}
}





