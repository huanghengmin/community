package com.hzih.community.syslog;

import com.hzih.community.web.servlet.SiteContextLoaderServlet;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-27
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class SysLogSend {

    private static final String charset = "utf-8";
    /**
     * 发送日志方法
     * @param msg
     */
    public static void sysLog(String msg) {
        if(SiteContextLoaderServlet.sysLogSendService.isRunning()){
            SiteContextLoaderServlet.sysLogSendService.sysLogSend(msg, charset);
        }
    }
}
