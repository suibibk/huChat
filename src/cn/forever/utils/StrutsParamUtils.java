package cn.forever.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class StrutsParamUtils {
	/**
	 * 获取request对象
	 * @return
	 */
	public static HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	/**
	 * 获取response对象
	 * @return
	 */
	public static HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	/**
	 * 获取页面传过来的参数值
	 * @param paramName
	 * @param defaultValue
	 * @return
	 */
	public static String getPraramValue(String paramName, String defaultValue){
		String value = getRequest().getParameter(paramName);
		if(value==null){
			value = defaultValue;
		}
		System.out.println("参数："+paramName+"的值是："+value);
		return value;
	}
	/**
	 * 向页面写入数据
	 * @param str
	 */
	public static void writeStr(String str){  
		HttpServletResponse response = getResponse();
		response.setContentType("text/plain; charset=utf-8");
        PrintWriter pw = null;  
        try {  
            pw = response.getWriter();  
            pw.write(str);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally{  
            if(pw!=null) pw.close();  
        }  
    }
}
