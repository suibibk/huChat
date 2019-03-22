package cn.forever.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;//自增的ID
	private String username;//用户名
	private String userId;//这个用来存放用户的userId，不要用自增键太不安全
	private String password;//用户密码
	private String nickname;//昵称
	private String imgUrl;//用户头像
	private String sex;//用户性别0女，1是男
	private String age;//用户年龄
	private String create_datetime;//用户创建时间
	private String update_datetime;//用户修改时间
	private String type;//用户类别0是管理员，1是已登录用户，2是游客
	private String remark;//用户备注
	private String visible;//用户是否有效
	private String value;//预留字段
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getCreate_datetime() {
		return create_datetime;
	}
	public void setCreate_datetime(String createDatetime) {
		create_datetime = createDatetime;
	}
	public String getUpdate_datetime() {
		return update_datetime;
	}
	public void setUpdate_datetime(String updateDatetime) {
		update_datetime = updateDatetime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", userId="
				+ userId + ", password=" + password + ", nickname=" + nickname
				+ ", imgUrl=" + imgUrl + ", sex=" + sex + ", age=" + age
				+ ", create_datetime=" + create_datetime + ", update_datetime="
				+ update_datetime + ", type=" + type + ", remark=" + remark
				+ ", visible=" + visible + ", value=" + value + "]";
	}
	
	
}
