package cn.forever.utils;

import java.security.SecureRandom;
//import java.util.Random;

public class CashCode {
	private static String[] array;
	private static String[] array_number_small_big={"2","3","4","5","6","7","8","9"//
		,"a","b","c","d","e","f","g","h","i","j","k","m","n","p","q"//
		,"r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H"//
		,"J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y"//
		,"Z"};
	private static String[] array_number_big={"2","3","4","5","6","7","8","9"//
		,"A","B","C","D","E","F","G","H"//
		,"J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y"//
		,"Z"};
	private static String[] array_number_small={"2","3","4","5","6","7","8","9"//
		,"a","b","c","d","e","f","g","h","i","j","k","m","n","p","q"//
		,"r","s","t","u","v","w","x","y","z"};
	private static String[] array_number={"0","1","2","3","4","5","6","7","8","9"};
	/**
	 * 传入生成随机数的数组名字已经位数(数字加字母的情况下，去掉0和o,1和l)
	 * @param arrrays_name数组名称
	 * @param num位数
	 * @return
	 */
	public static String getCashCode(String arrrays_name,int num){
		if("array_number".equals(arrrays_name)){
			array=array_number;
		}else if("array_number_small".equals(arrrays_name)){
			array=array_number_small;
		}else if("array_number_big".equals(arrrays_name)){
			array=array_number_big;
		}else{
			array=array_number_small_big;
		}
		SecureRandom r = new SecureRandom();
		int index = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <num; i++) {//只需要8位
			index=r.nextInt(array.length);
			sb.append(array[index]);
		}
		return sb.toString();
	}
	
	/**
	 * 生成兑奖码，兑奖码：数字8位
	 * @return
	 */
	public  static String getCashCode_number(){
		SecureRandom r = new SecureRandom();
		int index = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <8; i++) {//只需要8位
			index=r.nextInt(10);
			sb.append(array_number[index]);
		}
		return sb.toString();
	}
	/**
	 * 数字+小写字母8位
	 * @return
	 */
	public  static String getCashCode_number_small(){
		SecureRandom r = new SecureRandom();
		int index = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <8; i++) {//只需要8位
			index=r.nextInt(36);
			sb.append(array_number_small[index]);
		}
		return sb.toString();
	}
	/**
	 * 兑奖码：数字+小写字母+大写字母8位
	 * @return
	 */
	public  static String getCashCode_number_small_big(){
		SecureRandom r = new SecureRandom();
		int index = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <8; i++) {//只需要8位
			index=r.nextInt(62);
			sb.append(array_number_small_big[index]);
		}
		return sb.toString();
	}
	/**
	 * 生成兑奖码，兑奖码：时间戳+三位数的随机数16位
	 * @return
	 */
	public static String getNumberCashCode(){
		long timestamp=System.currentTimeMillis();
		int random = new SecureRandom().nextInt(900)+100;
		String cash_code=""+timestamp+random;
		return cash_code;
	}
}
