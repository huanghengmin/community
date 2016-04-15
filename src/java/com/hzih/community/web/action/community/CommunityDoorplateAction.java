package com.hzih.community.web.action.community;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityBuildUnitDao;
import com.hzih.community.dao.CommunityDoorplateDao;
import com.hzih.community.domain.*;
import com.hzih.community.service.LogService;
import com.hzih.community.utils.FileUtil;
import com.hzih.community.utils.StringContext;
import com.hzih.community.utils.StringUtils;
import com.hzih.community.utils.qrcode.ZXingCode;
import com.hzih.community.web.SessionUtils;
import com.hzih.community.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Blob;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CommunityDoorplateAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(CommunityDoorplateAction.class);
    private CommunityDoorplateDao communityDoorplateDao;
    private CommunityBuildUnitDao communityBuildUnitDao;

    public CommunityBuildUnitDao getCommunityBuildUnitDao() {
        return communityBuildUnitDao;
    }

    public void setCommunityBuildUnitDao(CommunityBuildUnitDao communityBuildUnitDao) {
        this.communityBuildUnitDao = communityBuildUnitDao;
    }

    private LogService logService;

    public CommunityDoorplateDao getCommunityDoorplateDao() {
        return communityDoorplateDao;
    }

    public void setCommunityDoorplateDao(CommunityDoorplateDao communityDoorplateDao) {
        this.communityDoorplateDao = communityDoorplateDao;
    }

    private CommunityDoorplate communityDoorplate;

    private int start;
    private int limit;

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

    public CommunityDoorplate getCommunityDoorplate() {
        return communityDoorplate;
    }

    public void setCommunityDoorplate(CommunityDoorplate communityDoorplate) {
        this.communityDoorplate = communityDoorplate;
    }

    public String findByAccount() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        Account account = SessionUtils.getAccount(request);
        String company_build_id = request.getParameter("company_build_id");
        String company_build_unit_id = request.getParameter("company_build_unit_id");
        String doorplate = request.getParameter("doorplate");
        try {
            Long community_id = account.getCommunity().getId();
            PageResult pageResult = communityDoorplateDao.find(start, limit, String.valueOf(community_id), company_build_id, company_build_unit_id, doorplate);
            if (pageResult != null) {
                List<CommunityDoorplate> list = pageResult.getResults();
                int count = pageResult.getAllResultsAmount();
                if (list != null) {
                    json = build(list, count);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String community_id = request.getParameter("community_id");
        String company_build_id = request.getParameter("company_build_id");
        String company_build_unit_id = request.getParameter("company_build_unit_id");
        String doorplate = request.getParameter("doorplate");
        String json = null;
        try {
            PageResult pageResult = communityDoorplateDao.find(start, limit, community_id, company_build_id, company_build_unit_id, doorplate);
            if (pageResult != null) {
                List<CommunityDoorplate> companyList = pageResult.getResults();
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

    private String build(List<CommunityDoorplate> districts, int count) {
        StringBuilder json = new StringBuilder("{total:" + count + ",rows:[");
        Iterator<CommunityDoorplate> iterator = districts.iterator();
        while (iterator.hasNext()) {
            CommunityDoorplate communityDoorplate = iterator.next();
            int roomCount = 0;
            int tenantCount = 0;
            String tenant_ids = "";
            String lardLord_ids = "";

            roomCount = communityDoorplate.getCommunityRoomSet().size();

            Set<LardLord> lardLords = communityDoorplate.getLardLords();
            for (LardLord l : lardLords) {
                lardLord_ids += l.getId() + ",";
            }

            Set<CommunityRoom> communityRoomSet = communityDoorplate.getCommunityRoomSet();
            for (CommunityRoom room : communityRoomSet) {
                Set<Tenant> lardLordSet = room.getTenants();
                tenantCount += lardLordSet.size();
                for (Tenant t : lardLordSet) {
                    tenant_ids += t.getId() + ",";
                }
            }

            tenant_ids = StringUtils.isBlank(tenant_ids) ? "": tenant_ids.substring(0,tenant_ids.lastIndexOf(","));
            lardLord_ids = StringUtils.isBlank(lardLord_ids) ? "": lardLord_ids.substring(0,lardLord_ids.lastIndexOf(","));

            if (iterator.hasNext()) {
                json.append("{");
                json.append("id:'" + communityDoorplate.getId() + "',");
                json.append("community_id:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getId() + "',");
                json.append("community_name:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getName() + "',");
                json.append("community_address:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getAddress() + "',");
                json.append("community_build:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getValue() + "',");
                json.append("community_build_unit:'" + communityDoorplate.getCommunityBuildUnit().getValue() + "',");
                json.append("community_ssxq:'" + Region.getName(communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getRegion()) + "',");
                json.append("community_principal:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal() + "',");
                json.append("community_principal_phone:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal_phone() + "',");
                json.append("status:'" + communityDoorplate.getStatus() + "',");
                json.append("tenant_ids:'" + tenant_ids + "',");
                json.append("lardLord_ids:'" + lardLord_ids + "',");
                json.append("roomCount:'" + roomCount + "',");
                json.append("tenantCount:'" + tenantCount + "',");
                json.append("doorplate:'" + communityDoorplate.getDoorplate() + "'");
                json.append("},");
            } else {
                json.append("{");
                json.append("id:'" + communityDoorplate.getId() + "',");
                json.append("community_id:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getId() + "',");
                json.append("community_name:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getName() + "',");
                json.append("community_address:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getAddress() + "',");
                json.append("community_build:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getValue() + "',");
                json.append("community_build_unit:'" + communityDoorplate.getCommunityBuildUnit().getValue() + "',");
                json.append("community_ssxq:'" + Region.getName(communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getRegion()) + "',");
                json.append("community_principal:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal() + "',");
                json.append("community_principal_phone:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal_phone() + "',");
                json.append("status:'" + communityDoorplate.getStatus() + "',");
                json.append("tenant_ids:'" + tenant_ids + "',");
                json.append("lardLord_ids:'" + lardLord_ids + "',");
                json.append("roomCount:'" + roomCount + "',");
                json.append("tenantCount:'" + tenantCount + "',");
                json.append("doorplate:'" + communityDoorplate.getDoorplate() + "'");
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
        String json = "{success:false,msg:'删除门牌失败'}";
        String msg = null;
        String id = request.getParameter("id");
        try {
            if (id != null) {
                boolean flag = communityDoorplateDao.remove(Long.parseLong(id));
                if (flag) {
                    msg = "删除门牌信息成功" + id;
                    json = "{success:true,msg:'删除门牌成功'}";
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "门牌管理", msg);
                } else {
                    msg = "删除门牌信息失败" + id + ",请删除关联数据后重试！";
                    json = "{success:false,msg:'" + msg + "'}";
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "门牌管理", msg);
                }
            }
        } catch (Exception e) {
            msg = "删除门牌信息失败" + id;
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "门牌管理", msg);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String insert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try {
            long id = communityDoorplate.getCommunityBuildUnit().getId();
            CommunityBuildUnit unit = communityBuildUnitDao.findById(id);
            communityDoorplate.setCommunityBuildUnit(unit);
            communityDoorplateDao.create(communityDoorplate);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "门牌管理", "用户新增门牌" + communityDoorplate.getDoorplate() + "信息成功");
            msg = "<font color=\"green\">新增成功,点击[确定]返回列表!</font>";
        } catch (Exception e) {
            logger.error("门牌管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "门牌管理", "用户新增门牌" + communityDoorplate.getDoorplate() + "信息失败");
            msg = "<font color=\"red\">新增失败</font>";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String qrcode() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String msg = null;
        String id = request.getParameter("id");
        String url = null;
        try {
            if (id != null) {
                CommunityDoorplate communityDoorplate = communityDoorplateDao.findById(Long.parseLong(id));
                Blob logo = communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getLogo();
                String ssxq = communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getRegion().getCode();
                long community_id = communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getId();
                long build_value = communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getId();
                long unit_value = communityDoorplate.getCommunityBuildUnit().getId();
                long doorplate_value = communityDoorplate.getId();
                String path = StringContext.systemPath + "/qrCode/" + ssxq + "/" + community_id + "/" + build_value + "/" + unit_value + "/" + doorplate_value;
                String logo_path = StringContext.systemPath + "/qrCode/" + ssxq + "/" + community_id + "/";
                File dir = new File(path);
                if (!dir.exists())
                    dir.mkdirs();
                File logo_dir = new File(logo_path);
                if (!logo_dir.exists())
                    logo_dir.mkdirs();
                String content = "CXSQRK-" + ssxq + "-" + community_id + "-" + build_value + "-" + unit_value + "-" + doorplate_value;
                if (!content.equals(communityDoorplate.getQrCode())) {
                    communityDoorplate.setQrCode(content);
                    communityDoorplateDao.update(communityDoorplate);
                }
                String qrcodeDir = StringContext.systemPath + "/qrCode/" + ssxq + "-" + community_id + "/" + build_value + "/" + unit_value + "/" + doorplate_value;
                File qrdir = new File(qrcodeDir);
                if (!qrdir.exists()) {
                    qrdir.mkdirs();
                }
                String qrcodepath = qrcodeDir + "/no_logo_" + content + ".jpg";
                if (logo != null) {
                    String logopath = logo_path + "/logo.jpg";
                    String logoqrcodepath = qrcodeDir + "/logo_" + content + ".jpg";
                    File f = new File(logoqrcodepath);
                    if (!f.exists()) {
                        InputStream inputStream = logo.getBinaryStream();
                        FileUtil.copy(inputStream, logopath);
                        ZXingCode.buildQRCode(content, qrcodepath, logopath, logoqrcodepath);
                    }
                    url = logoqrcodepath;
                } else {
                    File f = new File(qrcodepath);
                    if (!f.exists()) {
                        ZXingCode.buildQRCode(content, qrcodepath, null, null);
                    }
                    url = qrcodepath;
                }
                FileInputStream inputStream = new FileInputStream(url);
                FileUtil.copy(inputStream, response);
                msg = "获取二维码信息成功" + id;
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "二维码管理", msg);
            }

        } catch (Exception e) {
            msg = "获取二维码信息失败" + id;
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "二维码管理", msg);
        }
        return null;
    }

    public String checkByAccount() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        Account account = SessionUtils.getAccount(request);
        String build = request.getParameter("build");
        String unit = request.getParameter("unit");
        String doorplate = request.getParameter("doorplate");
        try {
            Long community_id = account.getCommunity().getId();
            List<CommunityDoorplate> companyList = communityDoorplateDao.find(String.valueOf(community_id), build, unit, doorplate);
            if (companyList != null && companyList.size() > 0) {
                msg = unit + "门牌已经存在";
            } else {
                msg = "true";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String check() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        String community_id = request.getParameter("community_id");
        String build = request.getParameter("build");
        String unit = request.getParameter("unit");
        String doorplate = request.getParameter("doorplate");
        try {
            List<CommunityDoorplate> companyList = communityDoorplateDao.find(community_id, build, unit, doorplate);
            if (companyList != null && companyList.size() > 0) {
                msg = unit + "门牌已经存在";
            } else {
                msg = "true";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String zzDoorplate() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'修改为自住房失败'}";
        String msg = null;
        String id = request.getParameter("id");
        try {
            if (id != null) {
                boolean flag = communityDoorplateDao.zzDoorplate(id);
                if(flag) {
                    msg = "修改为自住房成功";
                    json = "{success:true,msg:\"" + msg + "\"}";
                }else {
                    msg = "修改为自住房失败";
                    json = "{success:false,msg:\"" + msg + "\"}";
                }
            }
        } catch (Exception e) {
            msg = "修改为自住房失败";
            json = "{success:false,msg:\"" + msg + "\"}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String czDoorplate() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'修改为出租房失败'}";
        String msg = null;
        String id = request.getParameter("id");
        try {
            if (id != null) {
                boolean flag = communityDoorplateDao.czDoorplate(id);
                if(flag) {
                    msg = "修改为出租房成功";
                    json = "{success:true,msg:\"" + msg + "\"}";
                }else {
                    msg = "修改为出租房失败";
                    json = "{success:false,msg:\"" + msg + "\"}";
                }
            }
        } catch (Exception e) {
            msg = "修改为出租房失败";
            json = "{success:false,msg:\"" + msg + "\"}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findByClient() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String idx = request.getParameter("id");
        StringBuilder builder = new StringBuilder();
        try {
            String sss[] = idx.split("-");
            CommunityDoorplate communityDoorplate = communityDoorplateDao.findById(Long.parseLong(sss[sss.length-1]));
            String tenant_ss = null;
            StringBuilder tenants = new StringBuilder();
            tenants.append("tenants:[");
            Set<CommunityRoom> communityRoomSet = communityDoorplate.getCommunityRoomSet();
            Iterator<CommunityRoom> iterator_room = communityRoomSet.iterator();
            while (iterator_room.hasNext()) {
                CommunityRoom room = iterator_room.next();
                Set<Tenant> lardLords = room.getTenants();
                for (Tenant lord1 : lardLords) {
                        tenants.append("{");
                        tenants.append("id:'" + lord1.getId() + "',");
                        tenants.append("name:'" + lord1.getName() + "',");
                        tenants.append("sex:'" + lord1.getSex() + "',");
                        tenants.append("idCard:'" + lord1.getIdCard() + "',");
                        tenants.append("mz:'" + lord1.getMz() + "',");
                        tenants.append("birth:'" + lord1.getBirth() + "',");
                        tenants.append("sign:'" + lord1.getSign() + "',");
                        tenants.append("address:'" + lord1.getAddress() + "',");
                        tenants.append("DN:'" + lord1.getDN() + "',");
                        tenants.append("validity:'" + lord1.getValidity() + "',");
                        tenants.append("phone:'" + lord1.getPhone() + "',");
                        tenants.append("description:'" + lord1.getDescription() + "',");
                        tenants.append("status:'" + lord1.getStatus() + "',");
                        tenants.append("attention:'" + lord1.getAttention() + "',");
                        tenants.append("room:'" + lord1.getCommunityRoom().getRoom() + "'");
                        tenants.append("}");
                        tenants.append(",");
                }
            }
            if (tenants.toString().endsWith(",")) {
                tenant_ss = tenants.toString();
                tenant_ss = tenant_ss.substring(0, tenant_ss.lastIndexOf(","));
            }else {
                tenant_ss=tenants.toString();
            }
            tenant_ss+="]";


            String lardLord_ss = null;
            StringBuilder lardLords = new StringBuilder();
            lardLords.append("lardLords:[");
            Set<LardLord>  lardLordSet = communityDoorplate.getLardLords();
            for (LardLord lord1 : lardLordSet) {
                if(lord1.getStatus()==1) {
                    lardLords.append("{");
                    lardLords.append("id:'" + lord1.getId() + "',");
                    lardLords.append("name:'" + lord1.getName() + "',");
                    lardLords.append("sex:'" + lord1.getSex() + "',");
                    lardLords.append("idCard:'" + lord1.getIdCard() + "',");
                    lardLords.append("mz:'" + lord1.getMz() + "',");
                    lardLords.append("birth:'" + lord1.getBirth() + "',");
                    lardLords.append("sign:'" + lord1.getSign() + "',");
                    lardLords.append("address:'" + lord1.getAddress() + "',");
                    lardLords.append("DN:'" + lord1.getDN() + "',");
                    lardLords.append("validity:'" + lord1.getValidity() + "',");
                    lardLords.append("xzz:'" + lord1.getXzz() + "',");
                    lardLords.append("phone:'" + lord1.getPhone() + "',");
                    lardLords.append("description:'" + lord1.getDescription() + "',");
                    lardLords.append("status:'" + lord1.getStatus() + "',");
                    lardLords.append("attention:'" + lord1.getAttention() + "'");
                    lardLords.append("}");
                    lardLords.append(",");
                }
            }
            if (lardLords.toString().endsWith(",")) {
                lardLord_ss = lardLords.toString();
                lardLord_ss = lardLord_ss.substring(0, lardLord_ss.lastIndexOf(","));
            }else {
                lardLord_ss=lardLords.toString();
            }
            lardLord_ss+="]";


            builder.append("{");
            builder.append("success:" + true).append(",");
            builder.append("id:'" + communityDoorplate.getId() + "',");
            builder.append("community_id:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getId() + "',");
            builder.append("community_name:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getName() + "',");
            builder.append("community_address:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getAddress() + "',");
            builder.append("community_build:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getValue() + "',");
            builder.append("community_build_unit:'" + communityDoorplate.getCommunityBuildUnit().getValue() + "',");
            builder.append(tenant_ss).append(",");
            builder.append(lardLord_ss).append(",");
            builder.append("community_ssxq:'" + Region.getName(communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getRegion()) + "',");
            builder.append("community_principal:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal() + "',");
            builder.append("community_principal_phone:'" + communityDoorplate.getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal_phone() + "',");
            builder.append("doorplate:'" + communityDoorplate.getDoorplate() + "'");
            builder.append("}");
            writer.write(builder.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        writer.flush();
        writer.close();
        return null;
    }

}
