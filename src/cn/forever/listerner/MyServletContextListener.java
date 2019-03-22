package cn.forever.listerner;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.forever.utils.CommonFunction;
import cn.forever.webSocket.MessageManager;

public class MyServletContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("服务器关闭");
		MessageManager.saveMessageWhenShutdown();
		
	}
	
	/**
	 * application在服务器启动后就放入
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext application = sce.getServletContext();
		CommonFunction.setApplication(application);
		System.out.println("服务器启动");
		MessageManager.initMessageWhenStartUp();
	}

}
