package com.hzih.community.web.action.audit;

import cn.collin.commons.utils.DateUtils;
import com.hzih.community.service.AuditService;
import com.hzih.community.service.LogService;
import com.hzih.community.utils.StringUtils;
import com.hzih.community.web.SessionUtils;
import com.hzih.community.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-19
 * Time: 上午9:55
 * To change this template use File | Settings | File Templates.
 * 日志审计
 */
public class AuditAction extends ActionSupport{
    private static final Logger logger = Logger.getLogger(AuditAction.class);
    private LogService logService;

    public AuditService getAuditService() {
        return auditService;
    }

    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    private AuditService auditService;
    private int start;
    private int limit;

    public String selectUserAudit() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json =  "{'success':true,'total':0,'rows':[]}";
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String logLevel = request.getParameter("logLevel");
            String userName = request.getParameter("userName");
            Date startDate = StringUtils.isBlank(startDateStr) ? null : DateUtils
                    .parse(startDateStr, "yyyy-MM-dd");
            Date endDate = StringUtils.isBlank(endDateStr) ? null : DateUtils
                    .parse(endDateStr, "yyyy-MM-dd");

            json = auditService.selectUserAudit(start/limit+1, limit,startDate,endDate,logLevel,userName );
//            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户日志审计","用户读取用户日志审计信息成功 ");
        } catch (Exception e) {
            logger.error("用户日志审计", e);
            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "用户日志审计","用户读取用户日志审计信息失败 ");
        }
        actionBase.actionEnd(response, json, result);
        return null;
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
}
