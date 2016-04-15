package com.hzih.community.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.UserOperLogDao;
import com.hzih.community.domain.UserOperLog;
import com.hzih.community.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserOperLogDaoImpl extends MyDaoSupport implements UserOperLogDao {

	@Override
	public void setEntityClass() {
		this.entityClass = UserOperLog.class;
	}

	/**
	 * 分页查询
	 * 
	 * @param pageIndex
	 * @param pageLength
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public PageResult listLogsByParams(int pageIndex, int pageLength,
			Date startDate, Date endDate, String phone) {
		StringBuffer sb = new StringBuffer(" from UserOperLog s where 1=1");
		List params = new ArrayList(4);// 手动指定容量，避免多次扩容
		if(startDate!=null){
			sb.append(" and date_format(logTime,'%Y-%m-%d')>= date_format(?,'%Y-%m-%d')");
			params.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and date_format(logTime,'%Y-%m-%d')<= date_format(?,'%Y-%m-%d')");
			params.add(endDate);
		}

		if (StringUtils.isNotBlank(phone)) {
			sb.append(" and phone = ?");
			params.add(phone);
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
	public void newLog(String logInfo, String phone) {
		UserOperLog entry = new UserOperLog();
		entry.setLogInfo(logInfo);
		entry.setPhone(phone);
		entry.setLogTime(new Date());
		try{
			create(entry);
		} catch (Exception e){
			logger.error("新增用户日志失败",e);
		}
	}
}
