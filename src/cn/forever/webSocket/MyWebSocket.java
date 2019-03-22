package cn.forever.webSocket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.forever.service.HuChatService;
import cn.forever.utils.CommonFunction;
import cn.forever.utils.Const;
import cn.forever.utils.Encrypt;
import cn.forever.utils.SpringBeanManager;

import com.google.gson.Gson;
/**
 * 群聊系统服务器：暂时以群聊为目的
 * @author Administrator
 *
 */
@ServerEndpoint(value="/chatServer", configurator=GetHttpSessionConfigurator.class)
public class MyWebSocket {
	private static final Logger log = Logger.getLogger(MyWebSocket.class);
	//session也是唯一的
	private static Set<Session> sessions=new HashSet<Session>();
	//登录名不可能重复
	private static Set<String> usernames=new HashSet<String>();
	private static Map<String,Session> sessionMap = new HashMap<String, Session>();
	private static Map<String,String> usernameMap = new HashMap<String, String>();
	private String username;//用户名，传过来后就一直占有，这个是多线程的，每个用户一个
	private String imgUrl;//用户头像
	private String userId;//用户名，传过来后就一直占有，这个是多线程的，每个用户一个
	private int count=0;//相同频率的次数，如果相同频率的话，次数到了一定程度，就表明是刷
	private float lastFrequency =0f;
	private Long num=0l;//用户发送消息的次数
	private Long  startTime=0l;//单位是毫秒
	private Long  endTime=0l;//单位是毫秒
	private String md5;
	private Gson gson=new Gson();
	private Message message;
	private HuChatService huChatService =(HuChatService) SpringBeanManager.getBean("huChatService");
	//将数据都保存下来，保存当天的数据
	/*private static List<Message> messages = new ArrayList<Message>();
	private static List<Message> fileMessages = new ArrayList<Message>();*/
	//在服务器启动的时候，记得将现在的messages和fileMessages都保存
	/*public static  List<Message> getMessages(){
		return messages;
	}
	public static  List<Message> getFileMessages(){
		return fileMessages;
	}*/
	/**
	 * 服务器重启后，这个值不会变，重新取回来
	 * @param message
	 */
	/*public static void setMessage(Message message){
		messages.add(message);
		
	}*/
	/**
	 * 当用户打开聊天后会把用户名传过来，这里会保存对应的session,和username用两个集合来
	 * usernames返回为聊天的列表，也就是有多少人参与了聊天
	 * sessions则为管道，每个用户独有
	 * @param session
	 */
	@OnOpen
	public void open(Session session,EndpointConfig config){
		HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		//huChatService.saveMessageToDB(messages);
		if(httpSession==null){
			log.info("session为空，不可访问：");
			return;
		}
		log.info("start用户加入");
		message=new Message();
		username=(String) httpSession.getAttribute("username");
		imgUrl=(String) httpSession.getAttribute("imgUrl");
		userId=(String) httpSession.getAttribute("userId");
		if(StringUtils.isBlank(userId)||StringUtils.isBlank(username)){
			log.info("userId和username不存在，不可以访问");
			return;
		}
		log.info("userId:"+userId);
		log.info("username:"+username);
		log.info("imgUrl:"+imgUrl);
		if(sessionMap.containsKey(userId)){
			//对别的终端强制下线
			//发送下线信息
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("logon", "logon");
			sendMsg(sessionMap.get(userId),map);
			//强制下线
			try {
				sessionMap.get(userId).close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sessions.remove(sessionMap.get(userId));
			usernames.remove(usernameMap.get(userId));
			sessionMap.remove(userId);
			usernameMap.remove(userId);
		}
		//重新登录
		sessionMap.put(userId, session);
		usernameMap.put(userId, username);
		sessions.add(session);
		usernames.add(username);
		
		
		String come=username+"加入群聊";
		message.setUsernames(usernames);//联系人列表
		message.setMsgType(Const.COME);
		message.setUserId(userId);
		message.setContent(come);
		//先回显
		/*try {
			Map<String, List<Message>> map =new HashMap<String, List<Message>>();
			map.put("history", messages);
			if(messages.size()>0){
				log.info("map:"+map);
				session.getBasicRemote().sendText(gson.toJson(map));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		//理论上这里不会广播usernames
		broadcast(sessions, message);//广播出去
		log.info("sessionID-open:"+session.getId());
		log.info("userneme-come:"+username);
		
	}
	@OnMessage
	public void message(Session session,String msg){
		if(StringUtils.isBlank(userId)||StringUtils.isBlank(username)){
			log.info("userId和username不存在，不可以访问");
			return;
		}
		if(!sessions.contains(session)){
			return;
		}
		//获取内容和path
		log.info("start消息："+msg);
		JSONObject obj=new JSONObject();
		try {
			obj = JSONObject.fromObject(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
		String path =obj.optString("path", "");
		String msg2 =obj.optString("msg", "");
		String md52 =obj.optString("md5", "");
		//校验md5是否正确
		if(!md5.equals(md52)){
			log.info("MD5不正确");
			//提示用户发送过快
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("quick", "quick");
			sendMsg(session,map);
			return;
		}
		message=new Message();
		message.setMsgType(Const.MSG);
		message.setContent(msg2);
		message.setCreate_datetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		message.setUsername(username);
		message.setUserId(userId);
		message.setImgUrl(imgUrl);
		message.setVisible("1");//默认可以显示
		if(!"".equals(path)){
			message.setVisible("0");//需要审核（资源文件需要审核）
			String fileType=path.substring(path.lastIndexOf(".")+1, path.length());
			log.info("文件的后缀："+fileType);
			fileType= CommonFunction.getSubTypeByFileType(fileType);
			if(fileType.equals(Const.IMG)){
				if("0".equals(Const.IMG_OPEN)){
					log.info("没有开放图片上传功能");
				}else{
					message.setPath(path);
					message.setFileType(fileType);
				}
			}else if(fileType.equals(Const.AUDIO)){
				if("0".equals(Const.AUDIO_OPEN)){
					log.info("没有开放音频上传功能");
				}else{
					message.setPath(path);
					message.setFileType(fileType);
				}
			}else if(fileType.equals(Const.VIDEO)){
				if("0".equals(Const.VEDIO_OPEN)){
					log.info("没有开放视频上传功能");
				}else{
					message.setPath(path);
					message.setFileType(fileType);
				}
			}else{
				log.info("文件格式不对，请上传图片，MP3或者MP4");
			}
		}
		//这个msg保存到数据库
		/*if(messages.size()>=Const.SIZE){
			for (int i = 0; i <Const.REMOVE_SIZE; i++) {
				//多出来的添加到文件
				fileMessages.add(messages.get(i));
			}
			for (int i = 0; i <Const.REMOVE_SIZE; i++) {
				//然后移除前面10条
				messages.remove(0);
			}
		}*/
		/*if(fileMessages.size()>=Const.FILE_SIZE){
			//追加写入文件
			try {
				//zhe里是否要起一个新线程，到时候再优化
				FileUtils.writeStringToFile(new File(Const.PATH), gson.toJson(fileMessages), "utf-8",true);
				//同时写入数据库
				fileMessages.clear();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		/*messages.add(message);*/
		if(!checkFrequency()){
			log.info("频率超过限制，不可以再发");
			//提示用户发送过快
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("quick", "quick");
			sendMsg(session,map);
			return;
		}
		if(huChatService!=null){
			huChatService.saveMessageToDB(message);
		}
		//判断是否可以显示，不可以的话就表示需要审核
		if("0".equals(message.getVisible())){
			message.setPath("");
		}
		broadcast(sessions, message);
	}
	private boolean checkFrequency(){
		if(num==0l){
			startTime=System.currentTimeMillis();
		}else{
			endTime=System.currentTimeMillis();
		}
		num++;
		//当第二次就可以实行
		if(num>=2){
			//获取用户的平凉路
			log.info("num:"+num);
			log.info("time:"+(float)(endTime-startTime)/1000);
			float frequency = num/((float)(endTime-startTime)/1000);
			log.info("用户的发送频率："+frequency);
			//、查看次次频率和上次频率是否相同
			if(Math.round(frequency*1000)/1000==Math.round(lastFrequency*1000)/1000){
				//相同的计数加1
				count++;
				//判断计数有没有超过规定的数目，就是查看是否是刷
				if(count>Const.COUNT){
					log.info("用户按相同的频率发消息，超过"+Const.COUNT+"条，认定是程序刷");
					return false;
				}
			}else{
				//如果频率突然之间不同了，那么就表明，用户不是用机器刷，可通过
				count = 0;
				lastFrequency=frequency;
			}
			
			//查看频率是否小于0.5f，表明用户很久没有动这个了，此时要重新计算频率
			if(frequency<=1f){
				log.info("用户发送的频率小于0.1f：初始化"+frequency);
				//次数变为0；
				num=0l;
			}else{
				//判断频率有没有超过规定的频率,每秒钟最多发两条，超过就不给发
				if(frequency>Const.FREQUENCY){
					log.info("用户发送超过频率限制，补鞥呢发送这么快");
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * 当用户关闭时，要把sessions和usernames清空
	 * @param session
	 */
	@OnClose
	public void close(Session session){
		if(StringUtils.isBlank(userId)||StringUtils.isBlank(username)){
			log.info("userId和username不存在，不可以访问");
			return;
		}
		log.info("start用户离开");
		if(sessions.contains(session)){
			sessions.remove(session);
		}
		if(usernames.contains(username)){
			usernames.remove(username);
		}
		if(sessionMap.containsKey(userId)){
			sessionMap.remove(userId);
		}
		if(usernameMap.containsKey(userId)){
			usernameMap.remove(userId);
		}
		String leave=username+"离开群聊";
		message=new Message();
		message.setMsgType(Const.LEAVE);
		message.setUsernames(usernames);
		message.setContent(leave);
		log.info("session大小"+sessions.size());
		broadcast(sessions, message);
		log.info("session:"+session);
		/*try {
			session.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*log.info("sessionID-close:"+session.getId());
		log.info("userneme-leave:"+username);*/
	}
	@javax.websocket.OnError
	public void OnError(Session session,Throwable error) {
		log.info("start发送错误");
		log.info("用户断开连接");
		//error.printStackTrace();
		
	}
	/**
	 * 广播
	 * @param sessions
	 * @param msg
	 */
	public void broadcast(Set<Session> sessions,Message message){
		//只有类型三才需要
		try {
			String jsonMessage=gson.toJson(message);
			log.info(jsonMessage);
			Iterator<Session> iterator=sessions.iterator();
			while(iterator.hasNext()){
				Session session = iterator.next();
				if(sessionMap.get(userId)!=null){
					if(sessionMap.get(userId).equals(session)){
						sendMsg(session, new HashMap<String,Object>());
					}
				}
				//给当前这个人uuid
				session.getBasicRemote().sendText(jsonMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 广播
	 * @param sessions
	 * @param msg
	 */
	public void sendMsg(Session session,Map<String,Object> map){
		try {
			String uuid = CommonFunction.getUUID();
			md5=Encrypt.md5(uuid);
			log.info("这次的uuid是："+uuid);
			System.out.print("md5后的值是："+md5);
			map.put("uuid", uuid);
			String str =gson.toJson(map);
			log.info("str:"+str);
			session.getBasicRemote().sendText(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static byte[] image2byte(String path){
	    byte[] data = null;
	    FileImageInputStream input = null;
	    try {
	      input = new FileImageInputStream(new File(path));
	      ByteArrayOutputStream output = new ByteArrayOutputStream();
	      byte[] buf = new byte[1024];
	      int numBytesRead = 0;
	      while ((numBytesRead = input.read(buf)) != -1) {
	      output.write(buf, 0, numBytesRead);
	      }
	      data = output.toByteArray();
	      output.close();
	      input.close();
	    }
	    catch (FileNotFoundException ex1) {
	      ex1.printStackTrace();
	    }
	    catch (IOException ex1) {
	      ex1.printStackTrace();
	    }
	    return data;
	  }
	public static void main(String[] args) {
		String str="{\"msg\": \"啊啊\", \"path\": \"\",\"md5\":\"b97100a80e41894dbccd483c9b3d2847\"}";
		log.info(str);
		JSONObject obj = JSONObject.fromObject(str);
		log.info(obj);
	}
}
