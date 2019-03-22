package cn.forever.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.forever.dao.ObjectDao;
import cn.forever.model.HuChatInfo;
import cn.forever.model.User;
import cn.forever.model.Visit;
import cn.forever.redis.CacheUtil;
import cn.forever.utils.CommonFunction;
import cn.forever.utils.Const;
import cn.forever.utils.Encrypt;
import cn.forever.utils.StrutsParamUtils;
import cn.forever.utils.UsernameFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
/**
 * 20170508
 * @author lwh
 * 用于博客展示
 */
@ParentPackage(value = "struts-default")
@Namespace(value = "/huChat")
@Action(value = "huChatAction",results = {
		@Result(name = "login",location = "/WEB-INF/login.jsp"),
		@Result(name = "chat",location = "/WEB-INF/chat.jsp"),
		@Result(name = "auditing",location = "/WEB-INF/auditing.jsp")
})
public class HuChatAction {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger log = Logger.getLogger(HuChatAction.class);
	@Resource(name="objectDao")
	private ObjectDao objectDao;
	//加入缓存
	@Resource(name="cacheUtil")
	private CacheUtil cacheUtil;
	public String huChat(){
		HttpServletRequest request =StrutsParamUtils.getRequest();
		HttpSession session =request.getSession();
		//1、先判断内存中是否有user
		User user =  (User) session.getAttribute("user");
		if(user==null){
			//判断内存中是否有userId
			String userId=(String) session.getAttribute("userId");
			if(userId==null){
				//重新生成userId和生成nickName
				userId = CommonFunction.getUUID();
				session.setAttribute("userId", userId);
				session.setAttribute("username",UsernameFactory.getFullName());
				session.setAttribute("imgUrl","NONE");
			}else{
				log.info("用户已经存在，但是没有真正的登录");
			}
			//把视频。图片。音频开关返回给页面判断。
			request.setAttribute("IMG_OPEN", Const.IMG_OPEN);
			request.setAttribute("VEDIO_OPEN", Const.VEDIO_OPEN);
			request.setAttribute("AUDIO_OPEN", Const.AUDIO_OPEN);
		}else{
			String userId =user.getUserId();
			String username = user.getNickname();
			//还要设置用户头像
			String imgUrl = user.getImgUrl();
			System.out.println("---------------------:"+imgUrl);
			if(imgUrl==null){
				imgUrl="NONE";
			}
			session.setAttribute("userId", userId);
			session.setAttribute("username",username);
			session.setAttribute("imgUrl",imgUrl);
			//放开所有的图片，音乐，视频权限，如果到时也可以设置等级
			request.setAttribute("IMG_OPEN","1");
			request.setAttribute("VEDIO_OPEN","1");
			request.setAttribute("AUDIO_OPEN", "1");
			request.setAttribute("login", "true");
			if("3".equals(user.getType())){
				request.setAttribute("authority", "YES");
			}
		}
		request.setAttribute("IMG_SIZE", Const.IMG_SIZE);
		request.setAttribute("VEDIO_SIZE", Const.VEDIO_SIZE);
		request.setAttribute("AUDIO_SIZE", Const.AUDIO_SIZE);
		request.setAttribute("create_datetime", format.format(new Date()));
		return "chat";
	}
	/**
	 * 页面的访问记录
	 */
	public void addVisit(){
		HttpServletRequest request = StrutsParamUtils.getRequest();
		//访问的时间
		String create_datetime=format.format(new Date());
		//访问的链接
		//3:获取链接和参数
		//String requestURI=request.getRequestURI(); 
        //String queryString = request.getQueryString();
        //4：拼接 链接
        //String url = requestURI+"?"+queryString;
		String url = StrutsParamUtils.getPraramValue("url", "");
        //5:IP地址
        String ip=CommonFunction.getIpAddr();
        //6：访问者
        Object obj = request.getSession().getAttribute("user");
        String userId = "";
        if(obj!=null){
        	userId=((User) obj).getUserId();
        	log.info("用户已经登录");
        }
        Visit visit = new Visit();
        visit.setCreate_datetime(create_datetime);
        visit.setIp(ip);
        visit.setUserId(userId);
        visit.setUrl(url);
        objectDao.saveOrUpdate(visit);
        //log.info("访问记录："+visit.toString());
	}
	/**
	 * 分页去获取闲聊信息
	 */
	@SuppressWarnings("unchecked")
	public void getHuChatInfos(){
		Map<String,Object> result =new HashMap<String,Object>();
		int page = Integer.parseInt(StrutsParamUtils.getPraramValue("page", "0"));
		String create_datetime = StrutsParamUtils.getPraramValue("create_datetime", "");
		int num =Const.NUM;;
		String hql="FROM HuChatInfo WHERE create_datetime<=? ORDER BY create_datetime desc";
		Object[] args =new Object[]{create_datetime};
		if(page>=0&&!"".equals(create_datetime)){
			int start_page = page*num;
			List<HuChatInfo> infos = objectDao.findPageByHqlAndArgs(hql, args,start_page, num);
				if(infos!=null){
					if(infos!=null){
						//过滤一下，若是不能显示的，就把路径去掉
						for (HuChatInfo huChatInfo : infos) {
							if("2".equals(huChatInfo.getVisible())){
								huChatInfo.setVisible("0");
							}
							if("0".equals(huChatInfo.getVisible())){
								huChatInfo.setPath("");
							}
						}
						result.put("infos", infos);
						//有页数
						page=page+1;
						result.put("page", page);
					}
				}
				
			}
			Gson gson = new Gson();
			StrutsParamUtils.writeStr(gson.toJson(result));
			//获取最新的帖子
			return;
		}
	
	/**
	 * 去审核
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String toAuditing() throws UnsupportedEncodingException{
		HttpServletRequest request = StrutsParamUtils.getRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if(user==null){
			String openId = (String) session.getAttribute("openId");
			if(StringUtils.isBlank(openId)){
				CommonFunction.getOpenId();
				return null;
			}
			System.out.println("用户的openid为："+openId);
			//判断是否是超级管理员
			String hql = "from User u where u.userId=? and u.type='3' and  u.visible='1'";
			Object[] args = new Object[]{openId};
			User user2 = (User) objectDao.findObjectByHqlAndArgs(hql, args);
			if(user2!=null){
				session.setAttribute("user", user2);
				return "auditing";
			}
		}else{
			if("3".equals(user.getType())){
				return "auditing";
			}else{
				//跳转到首页，用户没有权限，直接跳转到QQ登录页
				loginQQ();
			}
		}
		return null;
	}
	public void doPVisible(){
		HttpServletRequest request = StrutsParamUtils.getRequest();
		String id = StrutsParamUtils.getPraramValue("id", "");
		String visible = StrutsParamUtils.getPraramValue("visible", "");
		if("".equals(id)||"".equals(visible)){
			System.out.println("参数异常");
			StrutsParamUtils.writeStr("error_param");
			return;
		}
		HttpSession session = request.getSession();
		//只有后台管理的会调用这个（然后，注销的话肯定是）
		User user = (User) session.getAttribute("user");
		if(user==null||!"3".equals(user.getType())){
			//只有在乎聊系统中权限是3的才有资格审核
			System.out.println("用户没有审核权限");
			StrutsParamUtils.writeStr("authority");
			return;
		}
		//用户有审核权限
		String hql="FROM HuChatInfo WHERE id=?";
		Object[] args =new Object[]{Long.parseLong(id)};
		HuChatInfo huChatInfo = (HuChatInfo) objectDao.findObjectByHqlAndArgs(hql, args);
		if(huChatInfo!=null){
			huChatInfo.setVisible(visible);
			objectDao.saveOrUpdate(huChatInfo);
			System.out.println("审核成功");
		}
		StrutsParamUtils.writeStr("success");
	}
	/**
	 * 获取未审核的图片，音频，视频，到页面，然后审核，按时间倒序,按图片，音频，视频升序,只有有权限的才可以审核，那就是超级管理员
	 */
	public void getHuChatFileInfo(){
		Map<String,Object> result =new HashMap<String,Object>();
		HttpServletRequest request = StrutsParamUtils.getRequest();
		HttpSession session = request.getSession();
		Gson gson = new Gson();
		//只有后台管理的会调用这个（然后，注销的话肯定是）
		User user = (User) session.getAttribute("user");
		if(user==null||!"3".equals(user.getType())){
			//只有在乎聊系统中权限是3的才有资格审核
			result.put("result", "authority");
			StrutsParamUtils.writeStr(gson.toJson(result));
			return;
		}
		int page = Integer.parseInt(StrutsParamUtils.getPraramValue("page", "0"));
		int num =10;
		String hql="FROM HuChatInfo WHERE visible='0' ORDER BY create_datetime desc,fileType asc";
		if(page>=0){
			int start_page = page*num;
			List<HuChatInfo> infos = objectDao.findPageByHqlAndArgs(hql, null,start_page, num);
				if(infos!=null){
					if(infos!=null){
						
						result.put("infos", infos);
						//有页数
						page=page+1;
						result.put("page", page);
					}
				}
				
			}
			StrutsParamUtils.writeStr(gson.toJson(result));
			//获取最新的帖子
			return;
		}
	public String login1(){
		HttpServletRequest request = StrutsParamUtils.getRequest();
		HttpSession session = request.getSession();
		//只有后台管理的会调用这个（然后，注销的话肯定是）
		User user = (User) session.getAttribute("user");
		if(user==null){
			log.info("用户没有登录，去到登录页面");
			return "login";
		}else{
			log.info("用户已经登录");
			CommonFunction.redirect(request.getContextPath()+"/huChat/huChatAction!huChat.action");
			return null;
		}
	}
	/**
	 * 用户去登录3b0749af814aa2792b4ef5b11ba4123f
	 */
	public void register(){
		//获取页面传过来的参数
		String username2 = StrutsParamUtils.getPraramValue("username2", "");
		String password2 = StrutsParamUtils.getPraramValue("password2", "");
		String password3 = StrutsParamUtils.getPraramValue("password3", "");
		log.info("username2="+username2+"|password2="+password2+"|password3="+password3);
		if("".equals(username2)||"".equals(password2)||"".equals(password3)){
			StrutsParamUtils.writeStr("error");
			log.info("用户名或密码为空");
			return;
		}
		if(!password2.equals(password3)){
			StrutsParamUtils.writeStr("no_same");
			log.info("两次输入的密码不同");
			return;
		}
		//根据用户名和密码去数据库中查找是否有记录（这里不需要限定用户的类型）
		String hql = "from User u where u.username=?";
		Object[] args = new Object[]{username2};
		User user1 = (User) objectDao.findObjectByHqlAndArgs(hql, args);
		if(user1!=null){
			//将用户放到session中
			StrutsParamUtils.writeStr("isHave");
			log.info("用户名已经存在");
			return;
		}else{
			String encrypt_password = Encrypt.encrypt(password2);
			String userId =CommonFunction.getUUID();
			User user= new User();
			user.setUserId(userId);
			user.setCreate_datetime(format.format(new Date()));
			user.setAge("");
			user.setNickname(username2);
			user.setUsername(username2);
			user.setPassword(encrypt_password);
			user.setRemark("胡聊用户");
			user.setType("2");//胡聊用户
			user.setVisible("1");//不允许，需要管理员审核
			objectDao.saveOrUpdate(user);
			//这里就可以去新建文件夹啦
			log.info("用户注册成功");
			StrutsParamUtils.writeStr("success");
			return;
		}
	}
	/**
	 * 为了安全，这里注销所有
	 * @return
	 */
	public String logon(){
		HttpServletRequest request = StrutsParamUtils.getRequest();
		HttpSession session = request.getSession();
		//只有后台管理的会调用这个（然后，注销的话肯定是）
		User user = (User) session.getAttribute("user");
		if(user!=null){
			session.removeAttribute("user");
			session.removeAttribute("userId");
			session.removeAttribute("openId");
			session.removeAttribute("access_token");
			session.removeAttribute("username");
			session.removeAttribute("imgUrl");
		}
		CommonFunction.redirect(request.getContextPath()+"/huChat/huChatAction!huChat.action");
		return null;
		
	}
	
	//博客管理登录 且用户有效
		public void login(){
			//获取页面传过来的参数
			String str = "{\"state\":\"error\"}";
			String username = StrutsParamUtils.getPraramValue("username", "");
			String password = StrutsParamUtils.getPraramValue("password", "");
			HttpSession session = StrutsParamUtils.getRequest().getSession();
			log.info("username="+username+"|password="+password);
			if("".equals(username)||"".equals(password)){
				StrutsParamUtils.writeStr(str); 
				log.info("用户名或密码为空");
				return;
			}
			//获取加密后的密码
			String encrypt_password = Encrypt.encrypt(password);
			//根据用户名和密码去数据库中查找是否有记录
			String hql = "from User u where u.username=? and u.password=? and u.visible='1'";
			Object[] args = new Object[]{username,encrypt_password};
			User user = (User) objectDao.findObjectByHqlAndArgs(hql, args);
			if(user!=null){
				//胡聊
				if("0".equals(user.getType())||"2".equals(user.getType())||"3".equals(user.getType())){
					//用户所拥有的权限
					str= "{\"state\":\"success\"}";
					session.setAttribute("user", user);//博客管理
					StrutsParamUtils.writeStr(str); 
					log.info("用户登录成功");
				}else{
					//等于1的话是博客用户，不能使用管理系统
					StrutsParamUtils.writeStr(str); 
					log.info("用户名或密码错误");
				}
				return;
			}else{
				StrutsParamUtils.writeStr(str); 
				log.info("用户名或密码错误");
				return;
			}
			
		}
		
	/**
	 * 分页去获取闲聊信息
	 */
	public void getHuChatInfosNums(){
			String create_datetime = StrutsParamUtils.getPraramValue("create_datetime", "");
			String hql="FROM HuChatInfo WHERE create_datetime<=?";
			Object[] args =new Object[]{create_datetime};
			int nums = objectDao.getCount("select count(*) "+hql,args);
			StrutsParamUtils.writeStr(nums+"");
		}
	
	
	public String loginQQ() throws UnsupportedEncodingException{
		HttpServletRequest request = StrutsParamUtils.getRequest();
		HttpSession session = request.getSession();
		//只有后台管理的会调用这个（然后，注销的话肯定是）
		User user = (User) session.getAttribute("user");
		if(user==null){
			String openId = (String) session.getAttribute("openId");
			if(StringUtils.isBlank(openId)){
				CommonFunction.getOpenId();
				return null;
			}
			System.out.println("用户的openid为："+openId);
			String hql = "from User u where u.userId=? and u.visible='1'";
			Object[] args = new Object[]{openId};
			User user2 = (User) objectDao.findObjectByHqlAndArgs(hql, args);
			if(user2==null){
				//去获取用户的昵称和头像
				String access_token = (String) session.getAttribute("access_token");
				JsonObject object =(JsonObject) CommonFunction.getUserInfoFromOpenIdAndAccessToken(openId,access_token);
				String nickname="";
				String figureurl_qq_1="";
				if(object!=null){
					String ret = object.get("ret").getAsString();
					if(ret.equals("0")){
						//昵称
						nickname = object.get("nickname").getAsString();
						//头像
						figureurl_qq_1 = object.get("figureurl_qq_1").getAsString();
					}
				}
				//保存
				user2= new User();
				user2.setUserId(openId);
				user2.setCreate_datetime(format.format(new Date()));
				user2.setAge("");
				user2.setNickname(nickname);
				user2.setUsername(openId);
				user2.setPassword(openId);
				user2.setImgUrl(figureurl_qq_1);
				user2.setRemark("胡聊用户QQ登录");
				user2.setType("2");//胡聊用户
				user2.setVisible("1");//
				objectDao.saveOrUpdate(user2);
			}
			session.setAttribute("user", user2);
		}
		log.info("用户已经登录");
		CommonFunction.redirect(request.getContextPath()+"/huChat/huChatAction!huChat.action");
		return null;
	}
	
	
}
