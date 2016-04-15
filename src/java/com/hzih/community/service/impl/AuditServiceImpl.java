package com.hzih.community.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.AccountOperLogDao;
import com.hzih.community.domain.AccountOperLog;
import com.hzih.community.service.AuditService;
import com.hzih.community.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-19
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */
public class AuditServiceImpl implements AuditService {
    private AccountOperLogDao accountOperLogDao;

    /**
     * 分页读取用户日志--并以json形式返回
     */
    public String selectUserAudit(int pageIndex, int limit, Date startDate, Date endDate,
                                  String logLevel, String userName) throws Exception {
        PageResult pageResult = accountOperLogDao.listLogsByParams(pageIndex,limit,startDate, endDate, logLevel, userName);
        List<AccountOperLog> userOperLogs = pageResult.getResults();
        int total = pageResult.getAllResultsAmount();
        String json = "{success:true,total:"+ total + ",rows:[";
        for (AccountOperLog u : userOperLogs) {
            json +="{id:'"+u.getId()+"',userName:'"+u.getUserName()+"',level:'"+u.getLevel()+
                    "',auditModule:'"+u.getAuditModule()+"',auditInfo:'"+u.getAuditInfo()+
                    "',logTime:'"+ DateUtils.formatDate(u.getLogTime(), "yy-MM-dd HH:mm:ss")+"'},";
        }
        json += "]}";
        return json;
    }

    public AccountOperLogDao getAccountOperLogDao() {
        return accountOperLogDao;
    }

    public void setAccountOperLogDao(AccountOperLogDao accountOperLogDao) {
        this.accountOperLogDao = accountOperLogDao;
    }
}
