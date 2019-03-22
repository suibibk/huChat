package cn.forever.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ImageUtil {
	private static final String CHARSET = "utf-8";
	/**
	 * 这个是产生BufferImage，没有具体产生保存到目录
	 * @param url
	 * @param logoPath
	 * @param qrCode_size
	 * @param logo_width
	 * @param logo_height
	 * @param needCompress
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage createQrCodeImage(String url,int logo_width,int logo_height) throws Exception{
		//1、生成二维码图片
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 1);//空白边距
		BitMatrix bitMatrix = new MultiFormatWriter().encode(url,BarcodeFormat.QR_CODE, logo_width, logo_height, hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
						: 0xFFFFFFFF);
			}
		}
		return image;
	}
	
	/**
	 * 通过链接或者远程路径获取Image
	 * @param path
	 * @return
	 * @throws Exception 
	 */
	public static Image getImage(String path) throws Exception{
		File bgfile =new File(path);
		Image bg_src =null;
		if (!bgfile.exists()) {
			URL request_url = new URL(path);
			HttpURLConnection conn = null;
	        conn = (HttpURLConnection) request_url.openConnection();
	        conn.setDoInput(true);
	        conn.setRequestMethod("GET");
	        bg_src=ImageIO.read(conn.getInputStream());
	        conn.disconnect();
		}else{
			bg_src=ImageIO.read(bgfile);
		}
		return bg_src;
	}
	/**
	 * 合并图片
	 * @param img 模板Image
	 * @param logo 要合并上来的Image
	 * @param x 合并的横坐标
	 * @param y 合并的纵坐标
	 */
	public  static void comBineImage(BufferedImage img,BufferedImage logo,int x,int y) {
			Graphics2D g2d = img.createGraphics();
			//透明度设置开始 
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,1.0f));
			////画logo
			int logo_width = logo.getWidth(null);
			int logo_height = logo.getHeight(null);
			g2d.drawImage(logo,x,y,logo_width,logo_height, null);           
			//透明度设置 结束
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER)); 
	}
	
	/**
	 * 压缩图片
	 * @param sourcepath 需要压缩图片的路径
	 * @param width 压缩后的宽度 像素
	 * @param height 压缩后的高度 像素
	 * @return
	 * @throws Exception 
	 */
	public static BufferedImage resize(String sourcepath,int width,int height) throws Exception{
		Image src = getImage(sourcepath);
		int file_width = src.getWidth(null);
		int file_height = src.getHeight(null);
		if (file_width > width) {
			file_width = width;
		}
		if (file_height > height) {
			file_height = height;
		}
		Image image = src.getScaledInstance(width, height,Image.SCALE_SMOOTH);
		BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		Graphics g = tag.getGraphics();
		g.drawImage(image, 0, 0, null); // 绘制缩小后的图
		g.setColor(Color.BLACK); 
		g.dispose();
		return tag;
	}
	/**
	 * 合并文字
	 * @param img 合并的模板Image
	 * @param contentArr 文字
	 * @param color 文字颜色
	 * @param size 文字大小
	 * @param x 位置
	 * @param y 位置
	 */
	public static void  modifyImage(BufferedImage img, String contentArr,Color color,int size,int x, int y) {  
        try { 
            Graphics2D g = img.createGraphics();  
            g.setBackground(Color.WHITE);  
            g.setColor(color);  
            g.setFont(new Font("宋体", Font.PLAIN, size));// 添加字体的属性设置);  
            // 验证输出位置的纵坐标和横坐标  
            if (contentArr != null) {  
                g.drawString(contentArr, x, y);  
            }   
            g.dispose();
        } catch (Exception e) {  
            System.out.println(e.getMessage());  
        }  
    }  
	
	
	public static void main(String[] args) throws Exception {
		
		//下面的是流的处理方法
		//宽度750 高度1334
		//宽度80 高度80
   	 	String user = "http://thirdwx.qlogo.cn/mmopen/eXt8pGbz7XZd8quvVtDkql3iaLibVD8e3PoPoicx8DicNXNBLkbhfOelC8ibCEn12LyTlOssmuZtgEe6WI1gQ5yXsvFmXPCx4Uricy/132";
		String text = "哈哈哈哈";
		
		//创建二维码
		BufferedImage bi1 = ImageUtil.createQrCodeImage(text,100,100);
		
		//压缩头像
		BufferedImage touxiang  =resize(user,30, 30);
		//头像放置位置
		comBineImage(bi1,touxiang,20,20);
		//长按二维码领取
		FileOutputStream out  = new FileOutputStream("E:888.jpg");
		ImageIO.write(bi1, "jpg", out);//写图片*/
		
	}

}
