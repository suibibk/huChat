package cn.forever.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Transactional
@Repository("objectDao")
public class ObjectDaoImpl implements ObjectDao{
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	private Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * 保存或者更新数据
	 */
	public void saveOrUpdate(Object obj) {
		this.getSession().saveOrUpdate(obj);
	}
	/**
	 * 根据对象明和ID删除数据
	 * @param ObjectName
	 * @param id
	 */
	public void deleteByObjectNameAndId(String ObjectName,Long id) {
		Object object = this.findObjectByObjectNameAndId(ObjectName, id);
		if(object!=null){
			this.getSession().delete(object);
		}
	}
	
	/**
	 * 根据对象名和ID查找数据
	 * @param ObjectName
	 * @param id
	 * @return
	 */
	public Object findObjectByObjectNameAndId(String ObjectName,Long id) {
		String hql = "from "+ObjectName+" p where p.id = ? ";
		Object object = getSession().createQuery(hql)//
		.setParameter(0, id).uniqueResult();
		return object;
	}

	/**
	 * 查询确定的一条记录
	 * @param hql：hql from objectName po where po.name=:name;
	 * @param map：参数map
	 * @return 分页查询返回的Object,没有则返回null
	 */
	public Object findObjectByHqlAndMap(String hql,
			Map<String, Object> map) {
		List list = findByHqlAndMap(hql, map);
		if(list!=null){
			return list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 查询多条记录
	 * @param hql：hql   from objectName po where po.name=:name;
	 * @param map：参数map
	 * @return 分页查询返回的list,没有则返回null
	 */
	public List findByHqlAndMap(String hql,
			Map<String, Object> map) {
		Query query = getSession().createQuery(hql);
		if (map != null ) {
            for(String key : map.keySet()){
                query.setParameter(key, map.get(key));
            }
        }
		List list = query.list();
		if(list!=null&&list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	/**
	 * 分页查询
	 * @param hql：hql   from objectName po where po.name=:name;
	 * @param map：参数map
	 * @param offset：查询的第多少条开始 从0是第一条开始
	 * @param length：每页查询的个数
	 * @return 分页查询返回的list,没有则返回null
	 */
	public List findPageByHqlAndMap(String hql,
			Map<String, Object> map, Integer offset, Integer length) {
		Query query = getSession().createQuery(hql);
		if (map != null ) {
            for(String key : map.keySet()){
                query.setParameter(key, map.get(key));
            }
        }
		 //起始，注意从0开始:0就是第一页
        if (offset >= 0 ) {
            query.setFirstResult(offset);
        }
        //返回记录数
        if (length > 0 ) {
            query.setMaxResults(length);
        }
        List list = query.list();
        if(list!=null&&list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	
	/**
	 * 查询多条记录
	 * @param hql：hql   from objectName po where po.name=?;
	 * @param args：参数args
	 * @return 分页查询返回的list,没有则返回null
	 */
	public List findByHqlAndArgs(String hql, Object[] args) {
		Query query = getSession().createQuery(hql);
		
		if (args != null ) {
           for (int i = 0; i < args.length; i++) {
        	   query.setParameter(i, args[i]);
           }
        }
		List list = query.list();
		if(list!=null&&list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	/**
	 * 查询确定的一条记录
	 * @param hql：hql from objectName po where po.name=?;
	 * @param args：参数args
	 * @return 分页查询返回的Object,没有则返回null
	 */
	public Object findObjectByHqlAndArgs(String hql, Object[] args) {
		List list = findByHqlAndArgs(hql, args);
		if(list!=null){
			return list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 分页查询
	 * @param hql：hql   from objectName po where po.name=?;
	 * @param args：参数args
	 * @param offset：查询的第几页 从0开始
	 * @param length：每页查询的个数
	 * @return 分页查询返回的list,没有则返回null
	 */
	public List findPageByHqlAndArgs(String hql, Object[] args, Integer offset,
			Integer length) {
		Query query = getSession().createQuery(hql);
		if (args != null ) {
	           for (int i = 0; i < args.length; i++) {
	        	   query.setParameter(i, args[i]);
	           }
	        }
		 //起始，注意从0开始:0就是第一页
        if (offset >= 0 ) {
            query.setFirstResult(offset);
        }
        //返回记录数
        if (length > 0 ) {
            query.setMaxResults(length);
        }
        List list = query.list();
        if(list!=null&&list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	
	/**
	 * 根据hql获取数量select count(*) from ...
	 */
	@SuppressWarnings("unchecked")
	public int getCount(String hql, Map<String, Object> map) {
		Query query = getSession().createQuery(hql);
		if (map != null ) {
            for(String key : map.keySet()){
                query.setParameter(key, map.get(key));
            }
        }
		List list = query.list();
		if(list!=null&&list.size()>0){
			if (list.get(0) instanceof BigDecimal){
	        	 return ((BigDecimal) list.get(0)).intValue();
	        }
			return ((Long) list.get(0)).intValue();
		}else{
			return 0;
		}
	}
	/**
	 * 根据hql获取数量select count(*) from ...
	 */
	@SuppressWarnings("unchecked")
	public int getCount(String hql, Object[] args) {
		Query query = getSession().createQuery(hql);
		if (args != null ) {
	           for (int i = 0; i < args.length; i++) {
	        	   query.setParameter(i, args[i]);
	           }
	        }
		List list = query.list();
		if(list!=null&&list.size()>0){
			if (list.get(0) instanceof BigDecimal){
	        	 return ((BigDecimal) list.get(0)).intValue();
	        }
			return ((Long) list.get(0)).intValue();
		}else{
			return 0;
		}
	}
	//删除
	public void delete(Object obj) {
		getSession().delete(obj);
	}

	public List findBySqlAndMap(String sql, Map<String, Object> map) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if (map != null ) {
            for(String key : map.keySet()){
            	query.setParameter(key, map.get(key));
            }
        }
		List list = query.list();
		if(list!=null&&list.size()>0){
			return list;
		}else{
			return null;
		}
	}

	public List findPageBySqlAndMap(String sql, Map<String, Object> map,
			Integer offset, Integer length) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if(query!=null){
			if (map != null ) {
	            for(String key : map.keySet()){
	            	query.setParameter(key, map.get(key));
	            }
	        }
		}
		 //起始，注意从0开始:0就是第一页
        if (offset >= 0 ) {
            query.setFirstResult(offset);
        }
        //返回记录数
        if (length > 0 ) {
            query.setMaxResults(length);
        }
		List list = query.list();
		if(list!=null&&list.size()>0){
			return list;
		}else{
			return null;
		}
	}

	public int getSqlCount(String sql, Map<String, Object> map) {
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		if(list!=null&&list.size()>0){
			if (list.get(0) instanceof BigDecimal){
	        	 return ((BigDecimal) list.get(0)).intValue();
	        }
			return ((Long) list.get(0)).intValue();
		}else{
			return 0;
		}
	}


}
