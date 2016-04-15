package com.hzih.community.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.AccountOperLogDao;
import com.hzih.community.domain.AccountOperLog;
import com.hzih.community.service.LogService;
import org.apache.log4j.Logger;

import java.util.Date;

public class LogServiceImpl implements LogService {
	private final static Logger logger = Logger.getLogger(LogServiceImpl.class);

	public AccountOperLogDao getAccountOperLogDao() {
		return accountOperLogDao;
	}

	public void setAccountOperLogDao(AccountOperLogDao accountOperLogDao) {
		this.accountOperLogDao = accountOperLogDao;
	}

	private AccountOperLogDao accountOperLogDao;


	public PageResult listUserOperLogByPage(int pageIndex, int pageLength,
			Date startDate, Date endDate, String logLevel, String userName) {
		logger.debug("startDate:" + startDate + " endDate:" + endDate);
		PageResult ps = null;
		try {
			ps = this.accountOperLogDao.listLogsByParams(pageIndex,
                    pageLength, startDate, endDate, logLevel, userName);
		} catch (Exception e) {
			logger.error("查找管理员日志失败",e);
			return null;
		}
		return ps;
	}


	public void newLog(String level, String userName, String auditModule,String auditInfo) {
		AccountOperLog entry = new AccountOperLog();
		entry.setAuditInfo(auditInfo);
		entry.setAuditModule(auditModule);
		entry.setLevel(level);
		entry.setUserName(userName);
		entry.setLogTime(new Date());
        try{
			accountOperLogDao.create(entry);
        } catch (Exception e){
            logger.error("新增管理员日志失败",e);
        }
	}
}
