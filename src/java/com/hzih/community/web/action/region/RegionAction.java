package com.hzih.community.web.action.region;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.RegionDao;
import com.hzih.community.domain.Region;
import com.hzih.community.service.LogService;
import com.hzih.community.web.SessionUtils;
import com.hzih.community.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 16-3-18.
 */
public class RegionAction extends ActionSupport {

    private static Logger logger = Logger.getLogger(RegionAction.class);
    private RegionDao regionDao;
    private LogService logService;
    private int start;
    private int limit;

    public RegionDao getRegionDao() {
        return regionDao;
    }

    public void setRegionDao(RegionDao regionDao) {
        this.regionDao = regionDao;
    }

    public LogService getLogService() {
        return logService;
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

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public StringBuilder resultJson(List<Region> list){
        StringBuilder json = new StringBuilder("{totalCount:"+list.size()+",root:[");
        Iterator<Region> iterator = list.iterator();
        while (iterator.hasNext()){
            Region govCode = iterator.next();
            if(iterator.hasNext()){
                json.append("{");
                json.append("id:'"+govCode.getId()+"',");
                json.append("code:'"+govCode.getCode()+"',");
                json.append("enName:'"+govCode.getEnName()+"',");
                json.append("name:'"+Region.getName(govCode)+"'");
                json.append("},");
            }else {
                json.append("{");
                json.append("id:'"+govCode.getId()+"',");
                json.append("code:'"+govCode.getCode()+"',");
                json.append("name:'"+Region.getName(govCode)+"'");
                json.append("}");
            }
        }
        json.append("]}");
        return json;
    }

    //所有的省
    public String province() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        StringBuilder json = null;
        try {
            PageResult pageResult = regionDao.findProvince(start, limit);
            if (pageResult != null) {
                List<Region> govCodes = pageResult.getResults();
                if (govCodes != null) {
                    json = resultJson(govCodes);
                }
            }
            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "省市级联","获取省级信息成功!");
        } catch (Exception e) {
            logger.error("用户管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "省市级联","获取省级信息失败!");
        }
        actionBase.actionEnd(response,json.toString(),result);
        return null;
    }

    //省下市
    public String  cityByProvince()throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String parentCode = request.getParameter("parentCode");
        ActionBase actionBase = new ActionBase();     //改变请求时间
        String result =	actionBase.actionBegin(request);
        StringBuilder json = null;
        try {
            PageResult pageResult = regionDao.findChild(start, limit, parentCode);
            if (pageResult != null) {
                List<Region> govCodes = pageResult.getResults();
                if (govCodes != null) {
                    json = resultJson(govCodes);
                }
            }
            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "省市级联","获取市级信息成功!");
        } catch (Exception e) {
            logger.error("用户管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "省市级联","获取市级信息失败!");
        }
        actionBase.actionEnd(response,json.toString(),result);
        return null;
    }

    public String  districtByCity()throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String parentCode = request.getParameter("parentCode");
        ActionBase actionBase = new ActionBase();     //改变请求时间
        String result =	actionBase.actionBegin(request);
        StringBuilder json = null;
        try {
            PageResult pageResult = regionDao.findChild(start, limit, parentCode);
            if (pageResult != null) {
                List<Region> govCodes = pageResult.getResults();
                if (govCodes != null) {
                    json = resultJson(govCodes);
                }
            }
            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "省市级联","获取市级信息成功!");
        } catch (Exception e) {
            logger.error("用户管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "省市级联","获取市级信息失败!");
        }
        actionBase.actionEnd(response,json.toString(),result);
        return null;
    }
}
