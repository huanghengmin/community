package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;

import java.util.Date;

public interface AccountOperLogDao extends BaseDao {
	/**
	 * 分页查询
	 * 
	 */
	public PageResult listLogsByParams(int pageIndex, int pageLength,
                                       Date startDate, Date endDate, String logLevel, String userName);
}
