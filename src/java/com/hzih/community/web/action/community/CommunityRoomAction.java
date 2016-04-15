package com.hzih.community.web.action.community;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityDoorplateDao;
import com.hzih.community.dao.CommunityRoomDao;
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

public class CommunityRoomAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(CommunityRoomAction.class);
    private CommunityRoomDao communityRoomDao;
    private CommunityDoorplateDao communityDoorplateDao;
    private CommunityRoom communityRoom;
    private LogService logService;
    private int start;
    private int limit;

    public CommunityDoorplateDao getCommunityDoorplateDao() {
        return communityDoorplateDao;
    }

    public void setCommunityDoorplateDao(CommunityDoorplateDao communityDoorplateDao) {
        this.communityDoorplateDao = communityDoorplateDao;
    }

    public CommunityRoom getCommunityRoom() {
        return communityRoom;
    }

    public void setCommunityRoom(CommunityRoom communityRoom) {
        this.communityRoom = communityRoom;
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

    public CommunityRoomDao getCommunityRoomDao() {
        return communityRoomDao;
    }

    public void setCommunityRoomDao(CommunityRoomDao communityRoomDao) {
        this.communityRoomDao = communityRoomDao;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String community_id = request.getParameter("community_id");
        String company_build_id = request.getParameter("company_build_id");
        String company_build_unit_id = request.getParameter("company_build_unit_id");
        String company_doorplate_id = request.getParameter("company_doorplate_id");
        String room = request.getParameter("room");
        String json = null;
        try {
            PageResult pageResult = communityRoomDao.find(start, limit, community_id, company_build_id, company_build_unit_id, company_doorplate_id, room);
            if (pageResult != null) {
                List<CommunityRoom> companyList = pageResult.getResults();
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

    private String build(List<CommunityRoom> districts, int count) {
        StringBuilder json = new StringBuilder("{total:" + count + ",rows:[");
        Iterator<CommunityRoom> iterator = districts.iterator();
        while (iterator.hasNext()) {
            CommunityRoom communityRoom = iterator.next();
            String tenant_ids = "";
            int tenantCount = communityRoom.getTenants().size();
            for (Tenant t : communityRoom.getTenants()) {
                tenant_ids += t.getId() + ",";
            }
            tenant_ids = StringUtils.isBlank(tenant_ids) ? "": tenant_ids.substring(0,tenant_ids.lastIndexOf(","));

            if (iterator.hasNext()) {
                json.append("{");
                json.append("id:'" + communityRoom.getId() + "',");
                json.append("community_id:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getId() + "',");
                json.append("community_name:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getName() + "',");
                json.append("community_address:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getAddress() + "',");
                json.append("community_build:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getValue() + "',");
                json.append("community_build_unit:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getValue() + "',");
                json.append("community_ssxq:'" + Region.getName(communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getRegion()) + "',");
                json.append("community_principal:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal() + "',");
                json.append("community_principal_phone:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal_phone() + "',");
                json.append("status:'" + communityRoom.getCommunityDoorplate().getStatus() + "',");
                json.append("room:'" + communityRoom.getRoom() + "',");
                json.append("tenant_ids:'" + tenant_ids + "',");
                json.append("tenantCount:'" + tenantCount + "',");
                json.append("doorplate:'" + communityRoom.getCommunityDoorplate().getDoorplate() + "'");
                json.append("},");
            } else {
                json.append("{");
                json.append("id:'" + communityRoom.getId() + "',");
                json.append("community_id:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getId() + "',");
                json.append("community_name:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getName() + "',");
                json.append("community_address:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getAddress() + "',");
                json.append("community_build:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getValue() + "',");
                json.append("community_build_unit:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getValue() + "',");
                json.append("community_ssxq:'" + Region.getName(communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getRegion()) + "',");
                json.append("community_principal:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal() + "',");
                json.append("community_principal_phone:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal_phone() + "',");
                json.append("status:'" + communityRoom.getCommunityDoorplate().getStatus() + "',");
                json.append("room:'" + communityRoom.getRoom() + "',");
                json.append("tenant_ids:'" + tenant_ids + "',");
                json.append("tenantCount:'" + tenantCount + "',");
                json.append("doorplate:'" + communityRoom.getCommunityDoorplate().getDoorplate() + "'");
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
        String json = "{success:false,msg:'删除房间失败'}";
        String msg = null;
        String id = request.getParameter("id");
        try {
            if (id != null) {
                communityRoomDao.remove(Long.parseLong(id));
                msg = "删除房间信息成功" + id;
                json = "{success:true,msg:'删除房间成功'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "房间管理", msg);
            }
        } catch (Exception e) {
            msg = "删除房间信息失败" + id;
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "房间管理", msg);
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

            long id = communityRoom.getCommunityDoorplate().getId();
            CommunityDoorplate communityDoorplate = communityDoorplateDao.findById(id);
            communityRoom.setCommunityDoorplate(communityDoorplate);
            communityRoomDao.create(communityRoom);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "房间管理", "用户新增房间" + communityRoom.getRoom() + "信息成功");
            msg = "<font color=\"green\">新增房间成功,点击[确定]返回列表!</font>";
        } catch (Exception e) {
            logger.error("房间管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "房间管理", "用户新增房间" + communityRoom.getRoom() + "信息失败");
            msg = "<font color=\"red\">新增房间失败</font>";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String qrcode() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String id = request.getParameter("id");
        String url = null;
        String msg = null;
        try {
            if (id != null) {
                CommunityRoom communityRoom = communityRoomDao.findById(Long.parseLong(id));
                Blob logo = communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getLogo();
                String ssxq = communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getRegion().getCode();
                long community_id = communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getId();
                long build_value = communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getId();
                long unit_value = communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getId();
                long doorplate_value = communityRoom.getCommunityDoorplate().getId();
                long room = communityRoom.getId();
                String path = StringContext.systemPath + "/qrCode/" + ssxq + "/" + community_id + "/" + build_value + "/" + unit_value + "/" + doorplate_value;
                String logo_path = StringContext.systemPath + "/qrCode/" + ssxq + "/" + community_id + "/";
                File dir = new File(path);
                if (!dir.exists())
                    dir.mkdirs();
                File logo_dir = new File(logo_path);
                if (!logo_dir.exists())
                    logo_dir.mkdirs();
                String content = "CXSQRK-" + ssxq + "-" + community_id + "-" + build_value + "-" + unit_value + "-" + doorplate_value + "-" + room;
                if (!content.equals(communityRoom.getQrCode())) {
                    communityRoom.setQrCode(content);
                    communityRoomDao.update(communityRoom);
                }
                String qrcodeDir = StringContext.systemPath + "/qrCode/" + ssxq + "-" + community_id + "/" + build_value + "/" + unit_value + "/" + doorplate_value + "/" + room;
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

    public String findByClient() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String idx = request.getParameter("id");
        StringBuilder builder = new StringBuilder();
        try {
            String sss[] = idx.split("-");
            CommunityRoom communityRoom = communityRoomDao.findById(Long.parseLong(sss[sss.length-1]));
            String tenant_ss = null;
            StringBuilder tenants = new StringBuilder();
            tenants.append("tenants:[");
                Set<Tenant> lardLords = communityRoom.getTenants();
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
            if (tenants.toString().endsWith(",")) {
                tenant_ss = tenants.toString();
                tenant_ss = tenant_ss.substring(0, tenant_ss.lastIndexOf(","));
            }else {
                tenant_ss=tenants.toString();
            }
            tenant_ss+="]";


            builder.append("{");
            builder.append("success:" + true).append(",");
            builder.append("id:'" + communityRoom.getId() + "',");
            builder.append("community_id:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getId() + "',");
            builder.append("community_name:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getName() + "',");
            builder.append("community_address:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getAddress() + "',");
            builder.append("community_build:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getValue() + "',");
            builder.append("community_build_unit:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getValue() + "',");
            builder.append(tenant_ss).append(",");
            builder.append("community_ssxq:'" + Region.getName(communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getRegion()) + "',");
            builder.append("community_principal:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal() + "',");
            builder.append("community_principal_phone:'" + communityRoom.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getPrincipal_phone() + "',");
            builder.append("doorplate:'" + communityRoom.getCommunityDoorplate().getDoorplate() + "',");
            builder.append("room:'" + communityRoom.getRoom() + "'");
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
