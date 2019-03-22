package cn.forever.dao;

import java.util.List;
import java.util.Map;


public interface ObjectDao {
	public void saveOrUpdate(Object obj);//
	public void delete(Object obj);//
	public void deleteByObjectNameAndId(String ObjectName,Long id);
	public Object findObjectByObjectNameAndId(String ObjectName ,Long id);
	
	
	public Object findObjectByHqlAndMap(String hql,Map<String,Object> map);
	public List findByHqlAndMap(String hql,Map<String,Object> map);
	public List findPageByHqlAndMap(String hql,Map<String,Object> map,Integer offset,Integer length);
	public List findBySqlAndMap(String sql,Map<String, Object> map);
	public List findPageBySqlAndMap(String sql,Map<String, Object> map, Integer offset, Integer length);
	public Object findObjectByHqlAndArgs(String hql,Object[] args);
	public List findByHqlAndArgs(String hql,Object[] args);
	public List findPageByHqlAndArgs(String hql,Object[] args,Integer offset,Integer length);
	
	public int getCount(String hql,Object[] args);
	public int getCount(String hql,Map<String, Object> map);
	public int getSqlCount(String sql,Map<String, Object> map);
	
}
