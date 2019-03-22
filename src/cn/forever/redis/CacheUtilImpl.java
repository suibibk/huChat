package cn.forever.redis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.forever.utils.Const;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
@Component("cacheUtil")
public class CacheUtilImpl implements CacheUtil {
	@Resource(name="redisTemplate")
    private StringRedisTemplate redisTemplate;//redis操作模板  
	private static final Logger log = Logger.getLogger(CacheUtilImpl.class);
	public void put(String key, String value) {
		if(isCan()){
			if (key==null || "".equals(key)) {  
	            return;  
	        }  
	        //redisTemplate.opsForHash().put(key, key, value);  
	        try {
				redisTemplate.opsForValue().set(key, value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//String
	public void put(String key, Object value) {
		if(isCan()){
			if (key==null || "".equals(key)) {  
	            return;  
			}  
			//redisTemplate.opsForHash().put(key, key, new Gson().toJson(value)); 
			try {
				redisTemplate.opsForValue().set(key, new Gson().toJson(value));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void putHash(String key, String field,String value) {
		if(isCan()){
			if (key==null || "".equals(key)||field==null||"".equals(field)) {  
	            return;  
	        }  
	        //redisTemplate.opsForHash().put(key, key, value);  
	        try {
				redisTemplate.opsForHash().put(key,field,value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String getHash(String key, String field) {
		if(isCan()){
			if (key==null || "".equals(key)||field==null||"".equals(field)) {  
	            return null;  
	        }  
	        //redisTemplate.opsForHash().put(key, key, value);  
			System.out.println("key:"+key+";field:"+field);
	        Object obj;
			try {
				obj = redisTemplate.opsForHash().get(key,field);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				obj=null;
				e.printStackTrace();
			}
	        System.out.println(obj);
	        if(obj == null){  
		           return null;  
		     }else{  
		           return String.valueOf(obj);  
		     } 
		}
		return null;
	}
	public String get(String key) {
		if(isCan()){
			Object obj;
			try {
				obj = redisTemplate.opsForValue().get(key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				obj=null;
				e.printStackTrace();
			}  
			// Object obj = redisTemplate.opsForHash().get(key, key);  
		     if(obj == null){  
		           return null;  
		     }else{  
		           return String.valueOf(obj);  
		     }  
		}
		return null;
	}
	
	/**
	 * 因为方法中有个<T>,证明这个是一个泛型的方法，返回的类型也是T
	 * 都可以用这两种方式来获取
	 * String
	 * JavaBran
	 * Map<String,String>
	 * Map<String,Object>
	 * Map<String,JavaBean>
	 * List<String>
	 * list<JavaBean>
	 * List<Map<String,Model>> 
	 * List<Map<String,Object>> 
	 * 这些都可以用两种方式实现
	 */
	//new Gson().fromJson(cacheUtil.get("key"), new TypeToken<T>(){}.getType()); 
	public <T> T get(String key, Class<T> className) {
		if(isCan()){
			Object obj;
			try {
				obj = redisTemplate.opsForValue().get(key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				obj=null;
				e.printStackTrace();
			}  
	        if(obj == null){  
	            return null;  
	        }  
	        return new Gson().fromJson(""+obj, className); 
		}
		return null;
	}
	public <T> List<T> getList(String key, Class<T> className) {
		if(isCan()){
			Object obj;
			try {
				obj = redisTemplate.opsForValue().get(key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				obj=null;
				e.printStackTrace();
			}  
	        if(obj == null){  
	            return null;  
	        }  
	        List<T> list =fromJsonList(""+obj, className);
	        return list;
		}
		return null;
	}
	private <T> List<T> fromJsonList(String json, Class<T> cls) {
		Gson gson =new Gson();
		List<T> mList = new ArrayList<T>(); 
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		for(final JsonElement elem : array){
			mList.add(gson.fromJson(elem, cls));
		} 
		return mList;
	} 
	public Boolean isCan(){
		if("01".equals(Const.REDIS_STATUS)){
			log.info("REDIS_FUNCTION为开启状态");
			return true;
		}else{
			log.info("REDIS_FUNCTION未开启");
			return false;
		}
	}
	
}
