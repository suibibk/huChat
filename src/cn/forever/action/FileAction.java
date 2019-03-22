package cn.forever.action;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import cn.forever.dao.ObjectDao;
import cn.forever.model.User;
import cn.forever.utils.CommonFunction;
import cn.forever.utils.Const;
import cn.forever.utils.StrutsParamUtils;
/**
 * 20170508
 * @author lwh
 * 用于博客展示
 */
@ParentPackage(value = "struts-default")
@Namespace(value = "/huChat")
@Action(value = "fileAction",results = {
})
public class FileAction {
	private static final Logger log = Logger.getLogger(FileAction.class);
	@Resource(name="objectDao")
	private ObjectDao objectDao;
	//文件上传
	private File file;//对应的就是表单中文件上传的那个输入域的名称，Struts2框架会封装成File类型的
    private String fileFileName;//   上传输入域FileName  文件名
    private String fileContentType;// 上传文件的MIME类型
    
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	//文件上传
	public void uploadFile(){
		Long size =0L;
		if(fileFileName!=null&&!"".equals(fileFileName)){
			//文件的后缀
			String fileType = fileFileName.substring(fileFileName.lastIndexOf(".")+1);
			size=file.length();
			String dir="";
			String subType =CommonFunction.getSubTypeByFileType(fileType);
			if(subType.equals(Const.IMG)){
				if("0".equals(Const.IMG_OPEN)){
					StrutsParamUtils.writeStr("no_open");
					log.info("没有开放图片上传功能");
					return ;
				}
				if(size>Const.IMG_SIZE){//大于10M
					StrutsParamUtils.writeStr("file_size");
					log.info("上传图片失败,文件超过10M");
					return ;
				}
				dir="images";
			}else if(subType.equals(Const.AUDIO)){
				if("0".equals(Const.AUDIO_OPEN)){
					StrutsParamUtils.writeStr("no_open");
					log.info("没有开放音频上传功能");
					return ;
				}
				if(size>Const.AUDIO_SIZE){//大于10M
					StrutsParamUtils.writeStr("file_size");
					log.info("上传音频文件失败,文件超过30M");
					return ;
				}
				dir="audios";
			}else if(subType.equals(Const.VIDEO)){
				if("0".equals(Const.VEDIO_OPEN)){
					StrutsParamUtils.writeStr("no_open");
					log.info("没有开放视频上传功能");
					return ;
				}
				if(size>Const.VEDIO_SIZE){//大于10M
					StrutsParamUtils.writeStr("file_size");
					log.info("上传视频文件失败,文件超过50M");
					return ;
				}
				dir="videos";
			}else{
				StrutsParamUtils.writeStr("error");
				log.info("文件格式不对，请上传图片，MP3或者MP4");
				return ;
			}
			//用户的内容放置路径，按用户和文件类型划分
			String path= "/uploadFile/"+dir+"/"+CommonFunction.getDateTime()+"/";
			String uuid=CommonFunction.getUUID();
			String filePath = path+uuid+"."+fileType;
			String truePath = CommonFunction.getApplication().getRealPath(path)+"/"+uuid+"."+fileType;
			log.info("size:"+size);//这个是保存在数据库的路径
			log.info("fileFileName:"+fileFileName);//这个是保存在数据库的路径
			log.info("fileType:"+fileType);//这个是保存在数据库的路径
			log.info("subType:"+subType);//这个是保存在数据库的路径
			log.info("filePath:"+filePath);//这个是保存在数据库的路径
			log.info("truePath:"+truePath);//这个是保存在磁盘上的真实路径
			//获取页面传过来的参数
			if(file!=null){
				log.info("有文件上传");
				File destFile = new File(truePath);
				try {
					FileUtils.copyFile(file, destFile);
				} catch (IOException e) {
					StrutsParamUtils.writeStr("error");
					log.info("上传文件失败");
					return;
				}
			}
			StrutsParamUtils.writeStr(filePath);
			log.info("保存文件成功");
		}else{
			StrutsParamUtils.writeStr("error");
			log.info("文件不存在");
			return;
		}
		
	}
}
