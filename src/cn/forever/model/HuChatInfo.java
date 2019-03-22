package cn.forever.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hu_chat_info")
public class HuChatInfo  implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;//自增的ID
	private String content;//正式聊天发送的消息的内容
	private String msgType;//消息类型1是加入,2是离开，3正常聊天
	private String imgUrl;//头像
	private String create_datetime;//消息创建时间
	private String username;//消息发起者
	private String path;//这条消息对应的文件
	private String userId;//占时用不上
	private String fileType;//文件类型1是图片，2是音乐，3是视频
	private String visible;//是否可以显示，文字可以显示，其他要审核0不可以，1可以
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	@Override
	public String toString() {
		return "HuChatInfo [id=" + id + ", content=" + content + ", msgType="
				+ msgType + ", create_datetime=" + create_datetime
				+ ", username=" + username + ", path=" + path + ", userId="
				+ userId + ", fileType=" + fileType + "]";
	}
	
}
