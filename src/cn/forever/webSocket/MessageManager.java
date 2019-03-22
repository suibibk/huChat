package cn.forever.webSocket;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import cn.forever.service.HuChatService;
import cn.forever.utils.Const;
import cn.forever.utils.SpringBeanManager;

import com.google.gson.Gson;

/**
 *消息的生命
 */
public class MessageManager {
	private static Gson gson=new Gson();
	/**
	 * 初始化消息，在服务器重新启动后，将txt中服务器关闭时的消息读取回来
	 */
	public static void initMessageWhenStartUp(){
		//服务器启动后，将当前消息加载回来
		/*try {
				String str = FileUtils.readFileToString(new File(Const.MESSAGE_PATH),"utf-8");
				if(str!=null&&!"".equals(str)){
						JSONArray jsonArray= JSONArray.fromObject(str);
						for(int i=0; i<jsonArray.size(); i++){  
						    //得到json数组中的每一个json对象
						    JSONObject obj = (JSONObject) jsonArray.get(i);  
						    //然后用Iterator迭代器遍历取值
						    String create_datetime =obj.optString("create_datetime", "");
						    String username =obj.optString("username", "");
						    String content =obj.optString("content", "");
						    String path =obj.optString("path", "");
						    String fileType =obj.optString("fileType", "");
						    String userId =obj.optString("userId", "");
						    //默认是聊天的文件
						    String msgType =obj.optString("msgType", Const.MSG);
						    Message message =new Message();
						    message.setCreate_datetime(create_datetime);
						    message.setContent(content);
						    message.setUsername(username);
						    message.setPath(path);
						    message.setMsgType(msgType);
						    message.setFileType(fileType);
						    message.setUserId(userId);
						    MyWebSocket.setMessage(message);
						}  
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println();
				}*/
		System.out.println("初始化消息完成");
	}
	/**
	 * 服务器关闭后保存的消息
	 */
	public static void saveMessageWhenShutdown(){
		/*List<Message> messages = MyWebSocket.getMessages();
		if(messages!=null&&messages.size()>0){
			try {
				//将当前内存中的消息全量替换到
				FileUtils.writeStringToFile(new File(Const.MESSAGE_PATH), gson.toJson(messages), "utf-8",false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Message> fileMessages = MyWebSocket.getFileMessages();
		if(fileMessages!=null&&fileMessages.size()>0){
			try {
				//将当前要保存在文件中的消息追加到chat.txt中
				FileUtils.writeStringToFile(new File(Const.PATH), gson.toJson(fileMessages), "utf-8",true);
				//同时写入数据库
				HuChatService huChatService =(HuChatService) SpringBeanManager.getBean("huChatService");
				if(huChatService!=null){
					huChatService.saveMessageToDB(fileMessages);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("消息保存完成");*/
	}
}
