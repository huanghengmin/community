package com.hzih.community.service;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-19
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
public interface AuditService {

    public String selectUserAudit(int pageIndex, int limit, Date startDate, Date endDate,
                                  String logLevel, String userName) throws Exception;

}
