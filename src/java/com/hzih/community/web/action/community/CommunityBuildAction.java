package com.hzih.community.web.action.community;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityBuildDao;
import com.hzih.community.dao.CommunityDao;
import com.hzih.community.domain.*;
import com.hzih.community.service.LogService;
import com.hzih.community.web.SessionUtils;
import com.hzih.community.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CommunityBuildAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(CommunityBuildAction.class);
    private CommunityBuildDao communityBuildDao;
    private LogService logService;
    private CommunityBuild communityBuild;
    private CommunityDao communityDao;

    public CommunityDao getCommunityDao() {
        return communityDao;
    }

    public void setCommunityDao(CommunityDao communityDao) {
        this.communityDao = communityDao;
    }

    public CommunityBuildDao getCommunityBuildDao() {
        return communityBuildDao;
    }

    public void setCommunityBuildDao(CommunityBuildDao communityBuildDao) {
        this.communityBuildDao = communityBuildDao;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public CommunityBuild getCommunityBuild() {
        return communityBuild;
    }

    public void setCommunityBuild(CommunityBuild communityBuild) {
        this.communityBuild = communityBuild;
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
        String result =	actionBase.actionBegin(request);
        String community_id = request.getParameter("community_id");
        String value = request.getParameter("value");
        String json = null;
        try {
            PageResult pageResult = communityBuildDao.find(start, limit,community_id,value);
            if (pageResult != null) {
                List<CommunityBuild> companyList = pageResult.getResults();
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
        try {
            List<CommunityBuild> companyList = communityBuildDao.find(community_id,build);
            if(companyList!=null&&companyList.size()>0){
                msg = build + "幢(栋)已经存在";
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
    /**
     * account check build
     * @return
     * @throws Exception
     */
    public String checkByAccount() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        Account account = SessionUtils.getAccount(request);
        String build = request.getParameter("build");
        try {
            Long community_id = account.getCommunity().getId();
            List<CommunityBuild> companyList = communityBuildDao.find(String.valueOf(community_id),build);
            if(companyList!=null&&companyList.size()>0){
                msg = build + "幢(栋)已经存在";
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
        String result =	actionBase.actionBegin(request);
        String json = null;
        Account account = SessionUtils.getAccount(request);
        String value = request.getParameter("value");
        try {
            Long community_id = account.getCommunity().getId();
            PageResult pageResult = communityBuildDao.find(start, limit, String.valueOf(community_id),value);
            if (pageResult != null) {
                List<CommunityBuild> companyList = pageResult.getResults();
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

    private String build(List<CommunityBuild> districts, int count) {
        StringBuilder json = new StringBuilder("{totalCount:" + count + ",root:[");
        Iterator<CommunityBuild> iterator = districts.iterator();
        while (iterator.hasNext()) {
            CommunityBuild company = iterator.next();

            int unitCount = 0;
            int doorplateCount = 0;
            int roomCount = 0;
            int zzCount = 0 ;
            int czCount = 0 ;
            int tenantCount = 0;


                Set<CommunityBuildUnit> communityBuildUnitSet = company.getCommunityBuildUnits();
                for (CommunityBuildUnit unit : communityBuildUnitSet) {
                    unitCount++;
                    Set<CommunityDoorplate> communityDoorplateSet = unit.getCommunityDoorplates();
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
                }

            if (iterator.hasNext()) {
                json.append("{");
                json.append("id:'" + company.getId() + "',");
                json.append("value:'" + company.getValue() + "',");
                json.append("community_name:'" + company.getCommunity().getName() + "',");
                json.append("community_address:'" + company.getCommunity().getAddress() + "',");
                json.append("community_ssxq:'" + Region.getName(company.getCommunity().getRegion()) + "',");
                json.append("community_principal:'" + company.getCommunity().getPrincipal() + "',");
                json.append("community_principal_phone:'" + company.getCommunity().getPrincipal_phone() + "',");
                json.append("unitCount:'"+unitCount+ "',");
                json.append("doorplateCount:'"+doorplateCount+ "',");
                json.append("roomCount:'"+roomCount+ "',");
                json.append("zzCount:'"+zzCount+ "',");
                json.append("czCount:'" + czCount + "',");
                json.append("tenantCount:'" + tenantCount + "'");
                json.append("},");
            } else {
                json.append("{");
                json.append("id:'" + company.getId() + "',");
                json.append("value:'" + company.getValue() + "',");
                json.append("community_name:'" + company.getCommunity().getName() + "',");
                json.append("community_address:'" + company.getCommunity().getAddress() + "',");
                json.append("community_ssxq:'" + Region.getName(company.getCommunity().getRegion()) + "',");
                json.append("community_principal:'" + company.getCommunity().getPrincipal() + "',");
                json.append("community_principal_phone:'" + company.getCommunity().getPrincipal_phone() + "',");
                json.append("unitCount:'"+unitCount+ "',");
                json.append("doorplateCount:'"+doorplateCount+ "',");
                json.append("roomCount:'"+roomCount+ "',");
                json.append("zzCount:'"+zzCount+ "',");
                json.append("czCount:'" + czCount + "',");
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
                communityBuildDao.remove(new CommunityBuild(Integer.parseInt(id)));
                msg = "删除小区楼栋信息成功"+id;
                json = "{success:true,msg:'删除成功'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区楼栋管理", msg);
            }
        }catch (Exception e){
            msg = "删除小区楼栋信息失败"+id;
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区楼栋管理", msg);
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
            long pId = communityBuild.getCommunity().getId();
            Community community = communityDao.findById(pId);
            communityBuild.setCommunity(community);
            communityBuildDao.create(communityBuild);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区管理", "用户新增小区楼栋" + communityBuild.getValue() + "信息成功");
            msg =  "<font color=\"green\">新增成功,点击[确定]返回列表!</font>";
        }catch (Exception e){
            logger.error("小区管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "小区管理","用户新增小区楼栋"+communityBuild.getValue()+"信息失败");
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
            CommunityBuild communityBuild = communityBuildDao.findById(Long.parseLong(id));
            if(communityBuild.getPrincipal().equals(this.communityBuild.getPrincipal())
                    &&communityBuild.getPrincipal_phone().equals(this.communityBuild.getPrincipal_phone())){
                msg =  "<font color=\"red\">楼栋信息未修改,点击[确定]返回列表!</font>";
            }else {
                communityBuild.setPrincipal(this.communityBuild.getPrincipal());
                communityBuild.setPrincipal_phone(this.communityBuild.getPrincipal_phone());
                communityBuildDao.update(communityBuild);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区管理", "用户修改小区楼栋" + communityBuild.getValue() + "信息成功");
                msg =  "<font color=\"green\">修改成功,点击[确定]返回列表!</font>";
            }
        }catch (Exception e){
            logger.error("小区管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "小区管理","用户修改小区楼栋"+communityBuild.getValue()+"信息失败");
            msg = "<font color=\"red\">修改失败</font>";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String insertByAccount() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        Account account = SessionUtils.getAccount(request);
        String msg = null;
        try {
            long pId = account.getCommunity().getId();
            Community community = communityDao.findById(pId);
            communityBuild.setCommunity(community);
            communityBuildDao.create(communityBuild);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区管理", "用户新增小区楼栋" + communityBuild.getValue() + "信息成功");
            msg =  "<font color=\"green\">新增成功,点击[确定]返回列表!</font>";
        }catch (Exception e){
            logger.error("小区管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "小区管理","用户新增小区楼栋"+communityBuild.getValue()+"信息失败");
            msg = "<font color=\"red\">新增失败</font>";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

}
