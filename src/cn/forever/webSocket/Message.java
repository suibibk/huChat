package cn.forever.webSocket;

import java.util.Set;

public class Message {
	/*private String come;//加入聊天发送的消息
	private String leave;//离开后发送的消息
*/	private Set<String> usernames;//用户列表，用户只有新加入才会有值
	private String content;//正式聊天发送的消息的内容
	private String msgType;//消息类型1是加入,2是离开，3正常聊天
	private String imgUrl;//头像
	private String create_datetime;//消息创建时间
	private String username;//消息发起者
	private String userId;//用户ID
	private String path;//这条消息对应的文件
	private String fileType;//文件类型1是图片，2是音乐，3是视频
	private String uuid;//用来加密的校验信息是否正确的
	private String visible;//是否显示，这个主要用于保存1显示，0不显示
	
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/*public String getCome() {
		return come;
	}
	public void setCome(String come) {
		this.come = come;
	}*/
	/*public String getLeave() {
		return leave;
	}
	public void setLeave(String leave) {
		this.leave = leave;
	}*/
	public Set<String> getUsernames() {
		return usernames;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public void setUsernames(Set<String> usernames) {
		this.usernames = usernames;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreate_datetime() {
		return create_datetime;
	}
	public void setCreate_datetime(String create_datetime) {
		this.create_datetime = create_datetime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	@Override
	public String toString() {
		return "Message [usernames=" + usernames + ", content=" + content
				+ ", msgType=" + msgType + ", create_datetime="
				+ create_datetime + ", username=" + username + ", path=" + path
				+ ", fileType=" + fileType + "]";
	}
	
}
