package cn.forever.redis;

import java.util.List;

public interface CacheUtil {
	/**
	 * 将字符串放入缓存
	 * @param key
	 * @param value
	 */
	public void put(String key, String value);
	
	/**
	 * 将对象放入缓存
	 * @param key
	 * @param value 对象
	 */
	public void put(String key, Object value);
	public void putHash(String key, String field,String value);
	public String getHash(String key, String field);
	/**
	 * 根据key获取值
	 * @param key
	 * @return
	 */
	public String get(String key);
	
	/**
	 * 根据key和类名强转为对象
	 * @param key
	 * @param className
	 * @return
	 */
	public <T>  T get(String key, Class<T> className);
	/**
	 * 获取List
	 * @param key
	 * @param className
	 * @return
	 */
	public <T> List<T>  getList(String key, Class<T> className);
	
	
	
}
