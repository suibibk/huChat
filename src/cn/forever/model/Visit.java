package cn.forever.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "visit")
public class Visit implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;//自增的ID
	private String create_datetime;//访问时间
	private String url;//访问的链接
	private String ip;//访问者电脑ip
	private String userId;//访问者ID
	private String value;//预留
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCreate_datetime() {
		return create_datetime;
	}
	public void setCreate_datetime(String createDatetime) {
		create_datetime = createDatetime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Visit [create_datetime=" + create_datetime + ", id=" + id
				+ ", ip=" + ip + ", url=" + url + ", userId=" + userId
				+ ", value=" + value + "]";
	}
	
}
