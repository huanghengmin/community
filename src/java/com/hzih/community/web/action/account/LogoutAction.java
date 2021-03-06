package com.hzih.community.web.action.account;

import com.hzih.community.domain.Account;
import com.hzih.community.service.LogService;
import com.hzih.community.web.SessionUtils;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

/**
 *   用户退出系统
 */

public class LogoutAction extends ActionSupport {
	private LogService logService;

	public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		Account account = SessionUtils.getAccount(request);
		String userName = null;
		if(account!=null){
			userName = account.getUserName();
			SessionUtils.removeAccount(request);
			SessionUtils.invalidateSession(request);
            logService.newLog("INFO", userName, "用户登录", "用户退出成功");
        }
		return "success";
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}
}
