package cn.forever.utils;
/**
 * 常量Memory中valueA是功能锁的类型 valueB是是否开始0未开始1开始
 * 下面的常量都是valueA,key_name:
 * FUNCTION_LOCK:是功能锁
 * @author forever
 *
 */
public class Const {
	public final static  String REDIS_STATUS = "00";//REDIS的开启状态 01开启
	public final static int SIZE=1000;//只会保留五千条数据长度
	public final static int REMOVE_SIZE=500;//到五千条后，会移除的条目数
	public final static int FILE_SIZE=3000;//多少条后就会写入文件,一万条后写入文件
	public final static String PATH ="/home/itweb/tomcat/webapps/huChat/chat.txt";//保存的消息
	public final static String MESSAGE_PATH="/home/itweb/tomcat/webapps/huChat/messages.txt";//服务器关闭后的消息
	
	//消息类型
	public final static  String COME="1";//到来
	public final static  String LEAVE="2";//离开
	public final static  String MSG="3";//聊天
	
	public final static String IMG="1";//图片
	public final static String AUDIO="2";//语音
	public final static String VIDEO="3";//视频
	
	public final static String IMG_OPEN="1";//是否可以上传图片0不可以，1可以
	public final static String AUDIO_OPEN="1";//是否可以上传音频0不可以，1可以 
	public final static String VEDIO_OPEN="1";//是否可以上传视频0不可以，1可以 
	
	public final static int IMG_SIZE=10485760;//图片大小不能超过10M
	public final static int AUDIO_SIZE=10485760*3;//音频大小不能超过30M
	public final static int VEDIO_SIZE=10485760*5;//视频大小不能超过50M
	
	
	public final static int NUM=20;//一下加载二十条数据
	
	public final static float FREQUENCY=2f;//2s中发送一条是属于正常防范，否则就要限制
	public final static int COUNT=5;//相同的条件超过五次就会被刷，如果有一次不同，就从新开始计算
	
	//占时写在这里
	public final static String QQ_APP_ID="101522241";
	public final static String QQ_APP_KEY="380d5187c6c345bc87b1c4cad1c25f02";
}
