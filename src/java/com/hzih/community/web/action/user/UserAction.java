package com.hzih.community.web.action.user;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityDao;
import com.hzih.community.dao.UserDao;
import com.hzih.community.dao.UserOperLogDao;
import com.hzih.community.domain.Account;
import com.hzih.community.domain.Community;
import com.hzih.community.domain.User;
import com.hzih.community.service.LogService;
import com.hzih.community.utils.Md5Key;
import com.hzih.community.utils.ResultObj;
import com.hzih.community.web.SessionUtils;
import com.hzih.community.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class UserAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(UserAction.class);
	private LogService logService;
	private UserDao userDao;
	private User user;
	private UserOperLogDao userOperLogDao;
	private int start;
	private int limit;
	private CommunityDao communityDao;

	public CommunityDao getCommunityDao() {
		return communityDao;
	}

	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public UserOperLogDao getUserOperLogDao() {
		return userOperLogDao;
	}

	public void setUserOperLogDao(UserOperLogDao userOperLogDao) {
		this.userOperLogDao = userOperLogDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/*检查*/
	public String check() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String phone = request.getParameter("phone");
		boolean flag = userDao.check(phone);
		if(flag == true){
			String msg = "用户检测手机号已注册";
			out.print("{success:false,msg:\""+msg+"\"}");
			userOperLogDao.newLog("用户检测手机号已注册", phone);
		}else {
			String msg = "用户检测手机号未注册";
			out.print("{success:true,msg:\""+msg+"\"}");
			userOperLogDao.newLog("用户检测手机号未注册", phone);
		}
		//刷新流
		out.flush();
		//关闭流
		out.close();
		return null;
	}

	/*注册*/
	public String register() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		boolean flag = userDao.register(phone,password);
		if(flag == true){
			out.print("{success:true}");
			userOperLogDao.newLog("用户注册成功", phone);
		}else {
			out.print("{success:false}");
			userOperLogDao.newLog("用户注册失败", phone);
		}
		//刷新流
		out.flush();
		//关闭流
		out.close();
		return null;
	}

	/*修改密码*/
	public String modifyPassword() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String phone = request.getParameter("phone");
		String oldPwd = request.getParameter("oldPwd");
		String password = request.getParameter("password");
		if(oldPwd!=null) {
			ResultObj obj = userDao.modifyPassword(phone,oldPwd, password);
			if (obj.isFlag()) {
				out.print("{success:true,msg:\""+obj.getMsg()+"\"}");
				userOperLogDao.newLog(obj.getMsg(), phone);
			} else {
				out.print("{success:false,msg:\""+obj.getMsg()+"\"}");
				userOperLogDao.newLog(obj.getMsg(), phone);
			}
		}else {
			boolean flag = userDao.modifyPassword(phone, password);
			if (flag == true) {
				out.print("{success:true}");
				userOperLogDao.newLog("用户修改密码成功", phone);
			} else {
				out.print("{success:false}");
				userOperLogDao.newLog("用户修改密码失败", phone);
			}
		}
		//刷新流
		out.flush();
		//关闭流
		out.close();
		return null;
	}

	/*登录*/
	public String login() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		User user = userDao.login(phone, password);
		if(user != null){
			out.print("{success:true}");
			userOperLogDao.newLog("用户登陆成功", phone);
		}else{
			out.print("{success:false}");
			userOperLogDao.newLog("用户登陆失败", phone);
		}
		//刷新流
		out.flush();
		//关闭流
		out.close();

		return null;
	}

	public String findUser()throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String phone = request.getParameter("phone");
		User user = userDao.find(phone);
		if(user != null){
			StringBuilder builder = new StringBuilder();
			builder.append("{");
			builder.append("success:"+true).append(",");
			builder.append("idCard:"+"\""+user.getIdCard()+"\"").append(",");
			builder.append("name:" + "\"" + user.getName() + "\"").append(",");
			builder.append("community_id:"+"\""+user.getCommunity().getId()+"\"").append(",");
			builder.append("community_name:"+"\""+user.getCommunity().getName()+"\"").append(",");
			builder.append("community_address:"+"\""+user.getCommunity().getAddress()+"\"").append(",");
			builder.append("number:"+"\""+user.getNumber()+"\"").append(",");
			builder.append("phone:"+"\""+user.getPhone()+"\"").append(",");
			builder.append("register_time:"+"\""+user.getRegister_time()+"\"").append(",");
			builder.append("modify_time:"+"\""+user.getModify_time() + "\"").append(",");
			builder.append("status:"+"\""+user.getStatus() + "\"");
			builder.append("}");
			out.print(builder.toString());
		}else{
			StringBuilder builder = new StringBuilder();
			builder.append("{");
			builder.append("success:"+false).append(",");
			builder.append("}");
			out.print(builder.toString());
		}
		//刷新流
		out.flush();
		//关闭流
		out.close();

		return null;
	}

	public String modifyUser()throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String phone = request.getParameter("phone");
		String idCard = request.getParameter("idCard");
		String name = request.getParameter("name");
		String number = request.getParameter("number");
		User user = userDao.find(phone);
		if(user != null){
			user.setIdCard(idCard);
			user.setName(name);
			user.setNumber(number);
			user.setModify_time(new Date());
			boolean modify = userDao.modify(user);
			if(modify){
				StringBuilder builder = new StringBuilder();
				builder.append("{");
				builder.append("success:"+true);
				builder.append("}");
				out.print(builder.toString());
			}else {
				StringBuilder builder = new StringBuilder();
				builder.append("{");
				builder.append("success:"+false);
				builder.append("}");
				out.print(builder.toString());
			}
		}else{
			StringBuilder builder = new StringBuilder();
			builder.append("{");
			builder.append("success:"+false);
			builder.append("}");
			out.print(builder.toString());
		}
		//刷新流
		out.flush();
		//关闭流
		out.close();

		return null;
	}

	public String find()throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase actionBase = new ActionBase();
		String result =	actionBase.actionBegin(request);
		String json =  "{'success':true,'total':0,'rows':[]}";
		try {
			String idCard = request.getParameter("idCard");
			String name = request.getParameter("name");
			String number = request.getParameter("number");
			String community_id = request.getParameter("community_id");
			String phone = request.getParameter("phone");

			PageResult pageResult = userDao.listByParams(start / limit + 1, limit, idCard, name, number, community_id, phone);

			List<User> userList = pageResult.getResults();
			int total = pageResult.getAllResultsAmount();
			json = "{success:true,total:"+ total + ",rows:[";
			for (User u : userList) {
				json +="{id:'"+u.getId()+"'" +
						",idCard:'"+u.getIdCard()+"'" +
						",name:'"+u.getName()+"'" +
						",number:'"+u.getNumber()+"'" +
						",community_id:'"+u.getCommunity().getId() +"'" +
						",community_name:'"+u.getCommunity().getName() +"'" +
						",community_address:'"+u.getCommunity().getAddress() +"'" +
						",phone:'"+u.getPhone()+"'" +
						",register_time:'"+u.getRegister_time() +"'" +
						",modify_time:'"+u.getModify_time() +"'" +
						",status:'"+ u.getStatus() +"'" ;
				json+= "},";
			}
			json += "]}";

		} catch (Exception e) {
			logger.error("用户信息查询", e);
			logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "用户信息","用户信息查询失败 ");
		}
		actionBase.actionEnd(response, json, result);
		return null;
	}


	public String findByAccount()throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase actionBase = new ActionBase();
		String result =	actionBase.actionBegin(request);
		String json =  "{'success':true,'total':0,'rows':[]}";
		try {
			Account account = SessionUtils.getAccount(request);
			String idCard = request.getParameter("idCard");
			String name = request.getParameter("name");
			String number = request.getParameter("number");
//			String community_id = request.getParameter("community_id");
			String phone = request.getParameter("phone");

			PageResult pageResult = userDao.listByParams(start / limit + 1, limit, idCard, name, number, account.getCommunity().getId(), phone);

			List<User> userList = pageResult.getResults();
			int total = pageResult.getAllResultsAmount();
			json = "{success:true,total:"+ total + ",rows:[";
			for (User u : userList) {
				json +="{id:'"+u.getId()+"'" +
						",idCard:'"+u.getIdCard()+"'" +
						",name:'"+u.getName()+"'" +
						",number:'"+u.getNumber()+"'" +
						",community_id:'"+u.getCommunity().getId() +"'" +
						",community_name:'"+u.getCommunity().getName() +"'" +
						",community_address:'"+u.getCommunity().getAddress() +"'" +
						",phone:'"+u.getPhone()+"'" +
						",register_time:'"+u.getRegister_time() +"'" +
						",modify_time:'"+u.getModify_time() +"'" +
						",status:'"+ u.getStatus() +"'" ;
				json+= "},";
			}
			json += "]}";

		} catch (Exception e) {
			logger.error("用户信息查询", e);
			logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "用户信息","用户信息查询失败 ");
		}
		actionBase.actionEnd(response, json, result);
		return null;
	}

	public String findByPoint()throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase actionBase = new ActionBase();
		String result =	actionBase.actionBegin(request);
		String json =  "{'success':true,'total':0,'rows':[]}";
		try {
			Account account = SessionUtils.getAccount(request);
			Community community = account.getCommunity();
			String idCard = request.getParameter("idCard");
			String name = request.getParameter("name");
			String number = request.getParameter("number");
			String phone = request.getParameter("phone");

			PageResult pageResult = userDao.listByParams(start / limit + 1, limit, idCard, name, number, community.getId(), phone);

			List<User> userList = pageResult.getResults();
			int total = pageResult.getAllResultsAmount();
			json = "{success:true,total:"+ total + ",rows:[";
			for (User u : userList) {
				json +="{id:'"+u.getId()+"'" +
						",idCard:'"+u.getIdCard()+"'" +
						",name:'"+u.getName()+"'" +
						",number:'"+u.getNumber()+"'" +
						",community_id:'"+u.getCommunity().getId() +"'" +
						",community_name:'"+u.getCommunity().getName() +"'" +
						",community_address:'"+u.getCommunity().getAddress() +"'" +
						",phone:'"+u.getPhone()+"'" +
						",register_time:'"+u.getRegister_time() +"'" +
						",modify_time:'"+u.getModify_time() +"'" +
						",status:'"+ u.getStatus() +"'" ;
				json+= "},";
			}
			json += "]}";

		} catch (Exception e) {
			logger.error("用户信息查询", e);
			logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "用户信息","用户信息查询失败 ");
		}
		actionBase.actionEnd(response, json, result);
		return null;
	}

	public String remove() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase actionBase = new ActionBase();
		String result = actionBase.actionBegin(request);
		String json = "{success:false,msg:'删除失败'}";
		String msg = null;
		String id = request.getParameter("id");
		try {
			if(id!=null) {
				userDao.remove(new User(Long.parseLong(id)));
				msg = "删除用户信息成功"+id;
				json = "{success:true,msg:'删除成功'}";
				logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
				logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户信息", msg);
			}
		}catch (Exception e){
			msg = "删除用户信息失败"+id;
			logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
			logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户信息", msg);
		}
		actionBase.actionEnd(response, json, result);
		return null;
	}

	public String insert() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase actionBase = new ActionBase();
		String result =	actionBase.actionBegin(request);
		String msg = null;
		//进行md5加密
		String md5_pwd = Md5Key.changeMd5Psd(user.getPassword());
		Community community = user.getCommunity();
		Community companyP  = communityDao.findById(community.getId());
		user.setCommunity(companyP);
		user.setPassword(md5_pwd);
		user.setRegister_time(new Date());
		user.setModify_time(new Date());
		try {
			userDao.create(user);
			logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户新增账户" + user.getPhone() + "信息成功");
			msg =  "<font color=\"green\">注册成功,点击[确定]返回列表!</font>";
		}catch (Exception e){
			logger.error("用户管理", e);
			logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户管理","用户新增账户"+user.getPhone()+"信息失败");
			msg = "<font color=\"red\">注册失败</font>";
		}
		String json = "{success:true,msg:'"+msg+"'}";
		actionBase.actionEnd(response, json, result);
		return null;
	}


	public String insertByAccount() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase actionBase = new ActionBase();
		String result =	actionBase.actionBegin(request);
		String msg = null;
		//进行md5加密
		Account account = SessionUtils.getAccount(request);
		String md5_pwd = Md5Key.changeMd5Psd(user.getPassword());
//		Community community = user.getCommunity();
		Community companyP  = communityDao.findById(account.getCommunity().getId());
		user.setCommunity(companyP);
		user.setPassword(md5_pwd);
		user.setRegister_time(new Date());
		user.setModify_time(new Date());
		try {
			userDao.create(user);
			logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户新增账户" + user.getPhone() + "信息成功");
			msg =  "<font color=\"green\">注册成功,点击[确定]返回列表!</font>";
		}catch (Exception e){
			logger.error("用户管理", e);
			logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户管理","用户新增账户"+user.getPhone()+"信息失败");
			msg = "<font color=\"red\">注册失败</font>";
		}
		String json = "{success:true,msg:'"+msg+"'}";
		actionBase.actionEnd(response, json, result);
		return null;
	}
}
