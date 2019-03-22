package cn.forever.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

import sun.misc.BASE64Encoder;
/**
 * 密码加密：先base64-urlencode-md5-urlencode-base64-md5
 * @author Administrator
 *
 */
public class Encrypt {
	public static String encrypt(String msg){
		try {
			byte[] srcBytes1=msg.trim().getBytes();
			String base641 = new BASE64Encoder().encode(srcBytes1);
			System.out.println("base641:"+base641);
			String encoder1 = URLEncoder.encode(base641, "UTF-8");
			System.out.println("encoder1:"+encoder1);
			String md51 = md5(encoder1);
			System.out.println("md51:"+md51);
			String encoder2 = URLEncoder.encode(md51, "UTF-8");
			System.out.println("encoder2:"+encoder2);
			byte[] srcBytes2=encoder2.getBytes();
			String base642 = new BASE64Encoder().encode(srcBytes2);
			System.out.println("base642:"+base642);
			String md52 = md5(base642);
			System.out.println("password:"+md52);
			return md52;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	/**
	 * md5加密，32位小写结果 
	 * */
	public static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes("UTF-8");
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args) {
		//900150983cd24fb0d6963f7d28e17f72 lwh abc
		
		System.out.println(md5("abc"));//900150983cd24fb0d6963f7d28e17f72js的一样
		System.out.println(encrypt(md5("LWHMHM@8023")));//3b0749af814aa2792b4ef5b11ba4123f
		
	}
}

