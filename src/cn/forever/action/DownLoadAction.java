package cn.forever.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.forever.dao.ObjectDao;
import cn.forever.redis.CacheUtil;
/**
 * 20170508
 * @author lwh
 * 用于博客管理
 */
@ParentPackage(value = "struts-default")
@Namespace(value = "/huChat")
@Action(value = "downLoadAction" ,results ={@Result(
				// result 名
				name = "success", 
				// result 类型
				type = "stream",
				params = {
				// 下载的文件格式
				"contentType", "application/octet-stream",   
				// 调用action对应的方法
				"inputName", "inputStream",   
				// HTTP协议，使浏览器弹出下载窗口
				"contentDisposition", "attachment;filename=\"${fileName}\"",   
				// 文件大小
				"bufferSize", "10240"}  
				)  ,
				@Result(name = "login",location = "/WEB-INF/blog/blogManage/common/login.jsp")}
)

public class DownLoadAction{
	private static final Logger log = Logger.getLogger(DownLoadAction.class);
	@Resource(name="objectDao")
	private ObjectDao objectDao;
	//加入缓存
	@Resource(name="cacheUtil")
	private CacheUtil cacheUtil;
	
	/**  
	* 下载文件名
	* 对应annotation注解里面的${fileName}，struts 会自动获取该fileName
	*/  
	private String fileName;   
	public String getFileName() {   
	        return fileName;   
	}   
	public void setFileName(String fileName) {   
            this.fileName = fileName;   
    }  
	private InputStream inputStream;
	public String downLoad(){
		String truePath="";
	    try {
			inputStream=FileUtils.openInputStream(new File(truePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    fileName="文件名。jps";
		return "success";
	}
	 /**  
	* 获取下载流
	* 对应 annotation 注解里面的 "inputName", "inputStream"
	* 假如 annotation 注解改为 "inputName", "myStream"，则下面的方法则应改为：getMyStream
	* @return InputStream  
	*/  
	public InputStream getInputStream() {   
		return inputStream;
	}   
}
