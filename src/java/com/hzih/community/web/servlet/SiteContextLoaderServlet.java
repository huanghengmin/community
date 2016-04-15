package com.hzih.community.web.servlet;

import com.hzih.community.constant.AppConstant;
import com.hzih.community.constant.ServiceConstant;
import com.hzih.community.domain.SafePolicy;
import com.hzih.community.myjfree.RunMonitorInfoList;
import com.hzih.community.myjfree.RunMonitorLiuliangBean2List;
import com.hzih.community.service.SafePolicyService;
import com.hzih.community.syslog.SysLogSendService;
import com.hzih.community.web.SiteContext;
import com.inetec.common.util.OSInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import javax.servlet.*;
import java.io.IOException;

public class SiteContextLoaderServlet extends DispatcherServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(SiteContextLoaderServlet.class);
	public static SysLogSendService sysLogSendService = new SysLogSendService();
	public static boolean isRunSyslogSendService = false;

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext servletContext = config.getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        SiteContext.getInstance().contextRealPath = config.getServletContext().getRealPath("/");
        servletContext.setAttribute("appConstant", new AppConstant());
        SafePolicyService service = (SafePolicyService)context.getBean(ServiceConstant.SAFEPOLICY_SERVICE);
        SafePolicy data = service.getData();
        SiteContext.getInstance().safePolicy = data;

		if(!sysLogSendService.isRunning()) {
			sysLogSendService.start();
			isRunSyslogSendService = true;
		}

		/**
		 * 读取网卡流量
		 */
		OSInfo osinfo = OSInfo.getOSInfo();
		if (osinfo.isLinux()) {
			new RunMonitorInfoList().start();
			new RunMonitorLiuliangBean2List().start();
		}
	}

	@Override
	public ServletConfig getServletConfig() {
		// do nothing
		return null;
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// do nothing
	}

	@Override
	public String getServletInfo() {
		// do nothing
		return null;
	}

	@Override
	public void destroy() {

	}

}
