package com.hzih.community.service;

import cn.collin.commons.domain.PageResult;

import java.util.Date;

public interface LogService {


    /**
     * 分页查询-用户日志审计
     *
     * @param pageIndex
     * @param pageLength
     * @param startDate
     * @param endDate
     * @param logLevel
     * @param userName
     * @return
     */
    public PageResult listUserOperLogByPage(int pageIndex, int pageLength,
                                            Date startDate, Date endDate, String logLevel, String userName);


    /**
     * 添加用户日志
     * @param level
     * @param userName
     * @param auditModule
     * @param auditInfo
     */
    public void newLog(String level, String userName, String auditModule,String auditInfo);


}
