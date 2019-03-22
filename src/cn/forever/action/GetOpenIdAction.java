package cn.forever.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import cn.forever.utils.CommonFunction;
import cn.forever.utils.StrutsParamUtils;
/**
 * 20181122
 * @author lwh
 * 获取openId的中间Action
 */
@ParentPackage(value = "struts-default")
@Namespace(value = "/huChat")
@Action(value = "getOpenIdAction",results = {
})
public class GetOpenIdAction {
	
	public String getOpenId() throws UnsupportedEncodingException{
		String code = StrutsParamUtils.getPraramValue("code", "");
		String backUrl = StrutsParamUtils.getPraramValue("backUrl", "");
		if(StringUtils.isBlank(code)){
			StrutsParamUtils.writeStr("code不能为空");
			return "";
		}
		String redirect_uri = CommonFunction.getRedirect_uri();
		redirect_uri=redirect_uri+"&backUrl="+backUrl;
		System.out.println("去腾讯获取授权码的地址："+redirect_uri);
		//进行UrlEncode编码
		redirect_uri=URLEncoder.encode(redirect_uri, "UTF-8");
		//获取openId
		CommonFunction.getOpenIdFromCodeAndRedirect_url(code, redirect_uri);
		//将openId放session
		//重定向走
		CommonFunction.redirect(backUrl);
		return null;
	}
}
