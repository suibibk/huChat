package cn.forever.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * 根据bean的名称返回这个bean的对象
 * @author lwh
 */
@Component("SpringBeanManager")
public class SpringBeanManager implements ApplicationContextAware {

	private static ApplicationContext context;
	public void setApplicationContext(ApplicationContext applicationContext)
		throws BeansException {
		synchronized (applicationContext) {
			context = applicationContext;
		}
	}
	public static ApplicationContext getApplicationContext() {
		return context;
	}
	public static Object getBean(String name) {
		System.out.println("要获取的对象："+name);
		return getApplicationContext().getBean(name);
	}

}
