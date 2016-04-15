package com.hzih.community.web.action.community;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityBuildDao;
import com.hzih.community.dao.CommunityBuildUnitDao;
import com.hzih.community.domain.*;
import com.hzih.community.service.LogService;
import com.hzih.community.web.SessionUtils;
import com.hzih.community.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CommunityBuildUnitAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(CommunityBuildUnitAction.class);
    private CommunityBuildUnitDao communityBuildUnitDao;
    private LogService logService;
    private CommunityBuildUnit communityBuildUnit;
    private CommunityBuildDao communityBuildDao;

    public CommunityBuildDao getCommunityBuildDao() {
        return communityBuildDao;
    }

    public void setCommunityBuildDao(CommunityBuildDao communityBuildDao) {
        this.communityBuildDao = communityBuildDao;
    }

    public CommunityBuildUnitDao getCommunityBuildUnitDao() {
        return communityBuildUnitDao;
    }

    public void setCommunityBuildUnitDao(CommunityBuildUnitDao communityBuildUnitDao) {
        this.communityBuildUnitDao = communityBuildUnitDao;
    }

    public CommunityBuildUnit getCommunityBuildUnit() {
        return communityBuildUnit;
    }

    public void setCommunityBuildUnit(CommunityBuildUnit communityBuildUnit) {
        this.communityBuildUnit = communityBuildUnit;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    private int start;
    private int limit;

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

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String community_id = request.getParameter("community_id");
        String company_build_id = request.getParameter("company_build_id");
        String value = request.getParameter("value");
        String json = null;
        try {
            PageResult pageResult = communityBuildUnitDao.find(start, limit,community_id,company_build_id,value);
            if (pageResult != null) {
                List<CommunityBuildUnit> companyList = pageResult.getResults();
                int count = pageResult.getAllResultsAmount();
                if (companyList != null) {
                    json = build(companyList, count);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String check() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        String community_id = request.getParameter("community_id");
        String build = request.getParameter("build");
        String unit = request.getParameter("unit");
        try {
            List<CommunityBuildUnit> companyList = communityBuildUnitDao.find(community_id,build,unit);
            if(companyList!=null&&companyList.size()>0){
                msg = unit + "单元已经存在";
            } else {
                msg = "true";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String checkByAccount() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        Account account = SessionUtils.getAccount(request);
        String build = request.getParameter("build");
        String unit = request.getParameter("unit");
        try {
            Long community_id = account.getCommunity().getId();
            List<CommunityBuildUnit> companyList = communityBuildUnitDao.find(String.valueOf(community_id),build,unit);
            if(companyList!=null&&companyList.size()>0){
                msg = unit + "单元已经存在";
            } else {
                msg = "true";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findByAccount() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Account account = SessionUtils.getAccount(request);
        String company_build_id = request.getParameter("company_build_id");
        String value = request.getParameter("value");
        String json = null;
        try {
            PageResult pageResult = communityBuildUnitDao.find(start, limit, String.valueOf(account.getCommunity().getId()),company_build_id,value);
            if (pageResult != null) {
                List<CommunityBuildUnit> companyList = pageResult.getResults();
                int count = pageResult.getAllResultsAmount();
                if (companyList != null) {
                    json = build(companyList, count);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    private String build(List<CommunityBuildUnit> districts, int count) {
        StringBuilder json = new StringBuilder("{totalCount:" + count + ",root:[");
        Iterator<CommunityBuildUnit> iterator = districts.iterator();
        while (iterator.hasNext()) {
            CommunityBuildUnit company = iterator.next();
            int doorplateCount = 0;
            int roomCount = 0;
            int zzCount = 0 ;
            int czCount = 0 ;
            int tenantCount = 0;

                Set<CommunityDoorplate> communityDoorplateSet = company.getCommunityDoorplates();
                for (CommunityDoorplate doorplate : communityDoorplateSet) {
                    doorplateCount++;
                    if (doorplate.getStatus() == 1) {
                        czCount++;
                    } else {
                        zzCount++;
                    }
                    Set<CommunityRoom> communityRoomSet = doorplate.getCommunityRoomSet();
                    for (CommunityRoom room : communityRoomSet) {
                        Set<Tenant> lardLordSet = room.getTenants();
                        tenantCount += lardLordSet.size();
                    }
                }


            if (iterator.hasNext()) {
                json.append("{");
                json.append("id:'" + company.getId() + "',");
                json.append("value:'" + company.getValue() + "',");
                json.append("community_name:'" + company.getCommunityBuild().getCommunity().getName() + "',");
                json.append("community_address:'" + company.getCommunityBuild().getCommunity().getAddress() + "',");
                json.append("community_ssxq:'" + Region.getName(company.getCommunityBuild().getCommunity().getRegion()) + "',");
                json.append("community_principal:'" + company.getCommunityBuild().getCommunity().getPrincipal() + "',");
                json.append("community_build_value:'" + company.getCommunityBuild().getValue() + "',");
                json.append("community_principal_phone:'" + company.getCommunityBuild().getCommunity().getPrincipal_phone() + "',");
                json.append("doorplateCount:'"+doorplateCount+ "',");
                json.append("roomCount:'"+roomCount+ "',");
                json.append("zzCount:'" + zzCount + "',");
                json.append("czCount:'"+czCount+ "',");
                json.append("tenantCount:'" + tenantCount + "'");
                json.append("},");
            } else {
                json.append("{");
                json.append("id:'" + company.getId() + "',");
                json.append("value:'" + company.getValue() + "',");
                json.append("community_name:'" + company.getCommunityBuild().getCommunity().getName() + "',");
                json.append("community_address:'" + company.getCommunityBuild().getCommunity().getAddress() + "',");
                json.append("community_ssxq:'" + Region.getName(company.getCommunityBuild().getCommunity().getRegion()) + "',");
                json.append("community_principal:'" + company.getCommunityBuild().getCommunity().getPrincipal() + "',");
                json.append("community_build_value:'" + company.getCommunityBuild().getValue() + "',");
                json.append("community_principal_phone:'" + company.getCommunityBuild().getCommunity().getPrincipal_phone() + "',");
                json.append("doorplateCount:'"+doorplateCount+ "',");
                json.append("roomCount:'"+roomCount+ "',");
                json.append("zzCount:'" + zzCount + "',");
                json.append("czCount:'"+czCount+ "',");
                json.append("tenantCount:'" + tenantCount + "'");
                json.append("}");
            }
        }
        json.append("]}");
        return json.toString();
    }

    public String remove() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'删除失败'}";
        String msg = null;
        String id = request.getParameter("id");
        try {
            if(id!=null) {
                communityBuildUnitDao.remove(new CommunityBuildUnit(Integer.parseInt(id)));
                msg = "删除小区单元信息成功"+id;
                json = "{success:true,msg:'删除成功'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区单元管理", msg);
            }
        }catch (Exception e){
            msg = "删除小区单元信息失败"+id;
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区单元管理", msg);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String insert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            long id = communityBuildUnit.getCommunityBuild().getId();
            CommunityBuild communityBuild = communityBuildDao.findById(id);
            communityBuildUnit.setCommunityBuild(communityBuild);
            communityBuildUnitDao.create(communityBuildUnit);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区管理", "用户新增小区单元" + communityBuildUnit.getValue() + "信息成功");
            msg =  "<font color=\"green\">新增成功,点击[确定]返回列表!</font>";
        }catch (Exception e){
            logger.error("小区管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "小区管理","用户新增小区单元"+communityBuildUnit.getValue()+"信息失败");
            msg = "<font color=\"red\">新增失败</font>";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String update() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String id = request.getParameter("id");
        String msg = null;
        try {
            CommunityBuildUnit communityBuildUnit = communityBuildUnitDao.findById(Long.parseLong(id));
            if(communityBuildUnit.getPrincipal().equals(this.communityBuildUnit.getPrincipal())
                    &&communityBuildUnit.getPrincipal_phone().equals(this.communityBuildUnit.getPrincipal_phone())){
                msg =  "<font color=\"red\">单元信息未修改,点击[确定]返回列表!</font>";
            }else {
                communityBuildUnit.setPrincipal(this.communityBuildUnit.getPrincipal());
                communityBuildUnit.setPrincipal_phone(this.communityBuildUnit.getPrincipal_phone());
                communityBuildUnitDao.update(communityBuildUnit);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区管理", "用户修改小区单元" + communityBuildUnit.getValue() + "信息成功");
                msg =  "<font color=\"green\">修改成功,点击[确定]返回列表!</font>";
            }
        }catch (Exception e){
            logger.error("小区管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "小区管理","用户修改小区单元"+communityBuildUnit.getValue()+"信息失败");
            msg = "<font color=\"red\">修改失败</font>";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
