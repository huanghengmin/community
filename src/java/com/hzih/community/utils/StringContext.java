package com.hzih.community.utils;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-7
 * Time: 上午10:56
 * To change this template use File | Settings | File Templates.
 */
public class StringContext {
    public final static String systemPath = System.getProperty("community.home");
    public final static String INTERFACE = "/etc/network/interfaces";//linux下IP信息存储文件
    public final static String IFSTATE = "/etc/network/run/ifstate"; //linux下DNS信息
    public final static String localLogPath = systemPath + "/logs";   //日志文件目录
    public final static String webPath = systemPath+"/tomcat/webapps"; //war服务文件存储目录
    public final static String tempPath = systemPath+"/tomcat/temp"; //缓存目录
    public static final String SECURITY_CONFIG = StringContext.systemPath +"/tomcat/conf/server.xml";

    public static final String syslog_xml = StringContext.systemPath +"/config/syslog.xml";
}
