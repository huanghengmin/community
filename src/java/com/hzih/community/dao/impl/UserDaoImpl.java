package com.hzih.community.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.UserDao;
import com.hzih.community.domain.User;
import com.hzih.community.utils.ResultObj;
import com.hzih.community.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jerry
 * @Date 2012-12-6
 * @Email 1457049206@qq.com
 *
 *        与数据库交互的实现类
 */
public class UserDaoImpl extends MyDaoSupport implements UserDao {
	@Override
	public void setEntityClass() {
		this.entityClass = User.class;
	}

	@Override
	public boolean check(String phone) {
		String hql = "from User u where u.phone= '" + phone+"'";
		List<User> list = this.getHibernateTemplate().find(hql);
		if(list != null && list.size() >= 1){
			return true;
		}
		return false;
	}

	@Override
	public boolean register(String phone, String password) {
		User user = new User(password,phone);
		user.setRegister_time(new Date());
		user.setModify_time(new Date());
		try {
			getHibernateTemplate().save(user);
			return true;
		}catch (Exception e){
			return false;
		}
	}

	@Override
	public User login(String phone, String password) {
		String hql = "from User u where u.phone= '" + phone+"' and u.password='"+password+"'";
		List<User> list = this.getHibernateTemplate().find(hql);
		if(list != null && list.size() >= 1)
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public boolean modifyPassword(String phone, String password) {
		String hql = "from User u where u.phone= '" + phone+"'";
		List<User> list = this.getHibernateTemplate().find(hql);
		if(list != null && list.size() >= 1){
			User u =  list.get(0);
			u.setPassword(password);
			u.setModify_time(new Date());
			try {
				getHibernateTemplate().saveOrUpdate(u);
				return true;
			}catch (Exception e){
				return false;
			}
		}else {
			return false;
		}
	}

	@Override
	public User find(String phone) {
		String hql = "from User u where u.phone= '" + phone+"'";
		List<User> list = this.getHibernateTemplate().find(hql);
		if(list != null && list.size() >= 1){
			User u =  list.get(0);
			return u;
		}else {
			return null;
		}
	}

	@Override
	public boolean modify(User user) {
		try {
			getHibernateTemplate().saveOrUpdate(user);
			return true;
		}catch (Exception e){
			return false;
		}
	}

	@Override
	public ResultObj modifyPassword(String phone, String oldPwd, String password) {
		String hql = "from User u where u.phone= '" + phone+"'";
		List<User> list = this.getHibernateTemplate().find(hql);
		if(list != null && list.size() >= 1){
			User u =  list.get(0);
			if(u.getPassword().equals(oldPwd)){
				if(password.equals(oldPwd)){
					return new ResultObj(false,"密码相同，无须修改");
				}else {
					u.setPassword(password);
					try {
						getHibernateTemplate().saveOrUpdate(u);
						return new ResultObj(true,"修改成功");
					}catch (Exception e){
						return new ResultObj(true,"修改失败");
					}
				}
			}else {
				return new ResultObj(false,"密码修改失败，请输入正确的原始密码");
			}
		}else {
			return new ResultObj(false,"无此用户信息");
		}
	}

	@Override
	public PageResult listByParams(int pageIndex, int pageLength, String idCard, String name, String number, String community_id, String phone) {
		StringBuffer sb = new StringBuffer(" from User s where 1=1");
		List params = new ArrayList(4);// 手动指定容量，避免多次扩容

		if (StringUtils.isNotBlank(idCard)) {
			sb.append(" and idCard like ?");
			params.add("%" + idCard + "%");
		}
		if (StringUtils.isNotBlank(name)) {
			sb.append(" and name like ?");
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(number)) {
			sb.append(" and number like ?");
			params.add("%" + number + "%");
		}

		if (StringUtils.isNotBlank(community_id)) {
			sb.append(" and s.community.id = ?");
			params.add(Long.parseLong(community_id));
		}

		if (StringUtils.isNotBlank(phone)) {
			sb.append(" and phone like ?");
			params.add("%" + phone + "%");
		}

		String countString = "select count(*) " + sb.toString();
		String queryString = sb.toString();

		PageResult ps = this.findByPage(queryString, countString, params
				.toArray(), pageIndex, pageLength);
		logger.debug(ps == null ? "ps=null" : "ps.results.size:"
				+ ps.getResults().size());
		return ps;
	}

	@Override
	public PageResult listByParams(int pageIndex, int pageLength, String idCard, String express_name, String express_number, long community_id, String phone) {
		StringBuffer sb = new StringBuffer(" from User s where 1=1");
		List params = new ArrayList(4);// 手动指定容量，避免多次扩容

		if (StringUtils.isNotBlank(idCard)) {
			sb.append(" and idCard like ?");
			params.add("%" + idCard + "%");
		}
		if (StringUtils.isNotBlank(express_name)) {
			sb.append(" and name like ?");
			params.add("%" + express_name + "%");
		}
		if (StringUtils.isNotBlank(express_number)) {
			sb.append(" and number like ?");
			params.add("%" + express_number + "%");
		}

		sb.append(" and s.community.id = ?");
		params.add(community_id);

		if (StringUtils.isNotBlank(phone)) {
			sb.append(" and phone like ?");
			params.add("%" + phone + "%");
		}

		String countString = "select count(*) " + sb.toString();
		String queryString = sb.toString();

		PageResult ps = this.findByPage(queryString, countString, params
				.toArray(), pageIndex, pageLength);
		logger.debug(ps == null ? "ps=null" : "ps.results.size:"
				+ ps.getResults().size());
		return ps;
	}


	@Override
	public void remove(User user) {
		getHibernateTemplate().delete(user);
	}
}
