package com.hzih.community.web.action.community;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityDao;
import com.hzih.community.dao.RegionDao;
import com.hzih.community.domain.*;
import com.hzih.community.service.LogService;
import com.hzih.community.utils.FileUtil;
import com.hzih.community.web.SessionUtils;
import com.hzih.community.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CommunityAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(CommunityAction.class);
    private CommunityDao communityDao;
    private RegionDao regionDao;
    private LogService logService;
    private Community community;
    private File uploadFile;
    private String uploadFileFileName;
    private String uploadFileContentType;

    public RegionDao getRegionDao() {
        return regionDao;
    }

    public void setRegionDao(RegionDao regionDao) {
        this.regionDao = regionDao;
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFileFileName() {
        return uploadFileFileName;
    }

    public void setUploadFileFileName(String uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }

    public String getUploadFileContentType() {
        return uploadFileContentType;
    }

    public void setUploadFileContentType(String uploadFileContentType) {
        this.uploadFileContentType = uploadFileContentType;
    }

    public CommunityDao getCommunityDao() {
        return communityDao;
    }

    public void setCommunityDao(CommunityDao communityDao) {
        this.communityDao = communityDao;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
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
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String json = null;
        try {
            PageResult pageResult = communityDao.find(start, limit, name, address);
            if (pageResult != null) {
                List<Community> companyList = pageResult.getResults();
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

    private String build(List<Community> districts, int count) {
        StringBuilder json = new StringBuilder("{totalCount:" + count + ",root:[");
        Iterator<Community> iterator = districts.iterator();
        while (iterator.hasNext()) {
            ;
            Community company = iterator.next();
            String logo_path = ServletActionContext.getServletContext().getRealPath("/logo") + "/" + company.getId();
            String logopath = logo_path + "/logo.jpg";
            File logoFile = new File(logopath);
            if (iterator.hasNext()) {
                json.append("{");
                json.append("id:'" + company.getId() + "',");

                int unitCount = 0;
                int doorplateCount = 0;
                int roomCount = 0;
                int zzCount = 0 ;
                int czCount = 0 ;
                int tenantCount = 0;

                Set<CommunityBuild> communityBuildSet = company.getCommunityBuilds();
                Iterator<CommunityBuild> ite = communityBuildSet.iterator();
                while (ite.hasNext()) {
                    CommunityBuild build = ite.next();
                    Set<CommunityBuildUnit> communityBuildUnitSet = build.getCommunityBuildUnits();
                    for (CommunityBuildUnit unit : communityBuildUnitSet) {
                        unitCount++;
                        Set<CommunityDoorplate> communityDoorplateSet = unit.getCommunityDoorplates();
                        for (CommunityDoorplate doorplate : communityDoorplateSet) {
                            doorplateCount++;
                            if(doorplate.getStatus()==1){
                                czCount++;
                            }else {
                                zzCount++;
                            }
                            Set<CommunityRoom> communityRoomSet = doorplate.getCommunityRoomSet();
                            for (CommunityRoom room : communityRoomSet) {
                                Set<Tenant> lardLordSet = room.getTenants();
                                tenantCount += lardLordSet.size();
                            }
                        }
                    }
                }
                json.append("buildCount:'" + company.getCommunityBuilds().size() + "',");
                json.append("unitCount:'" + unitCount + "',");
                json.append("doorplateCount:'" + doorplateCount + "',");
                json.append("roomCount:'" + roomCount + "',");
                json.append("czCount:'" + czCount + "',");
                json.append("zzCount:'" + zzCount + "',");
                json.append("tenantCount:'" + tenantCount + "',");

                json.append("address:'" + company.getAddress() + "',");
                json.append("number:'" + company.getNumber() + "',");
                json.append("principal:'" + company.getPrincipal() + "',");
                json.append("principal_phone:'" + company.getPrincipal_phone() + "',");
                if (logoFile.exists()) {
                    String url = "/logo/" + company.getId() + "/logo.jpg";
                    json.append("logo:'" + url + "',");
                } else {
                    json.append("logo:'',");
                }
                json.append("ssxq:'" + Region.getName(company.getRegion()) + "',");
                json.append("name:'" + company.getName() + "'");
                json.append("},");
            } else {
                json.append("{");
                json.append("id:'" + company.getId() + "',");

                int unitCount = 0;
                int doorplateCount = 0;
                int roomCount = 0;
                int zzCount = 0 ;
                int czCount = 0 ;
                int tenantCount = 0;

                Set<CommunityBuild> communityBuildSet = company.getCommunityBuilds();
                Iterator<CommunityBuild> ite = communityBuildSet.iterator();
                while (ite.hasNext()) {
                    CommunityBuild build = ite.next();
                    Set<CommunityBuildUnit> communityBuildUnitSet = build.getCommunityBuildUnits();
                    for (CommunityBuildUnit unit : communityBuildUnitSet) {
                        unitCount++;
                        Set<CommunityDoorplate> communityDoorplateSet = unit.getCommunityDoorplates();
                        for (CommunityDoorplate doorplate : communityDoorplateSet) {
                            doorplateCount++;
                            if(doorplate.getStatus()==1){
                                czCount++;
                            }else {
                                zzCount++;
                            }
                            Set<CommunityRoom> communityRoomSet = doorplate.getCommunityRoomSet();
                            for (CommunityRoom room : communityRoomSet) {
                                Set<Tenant> lardLordSet = room.getTenants();
                                tenantCount += lardLordSet.size();
                            }
                        }
                    }
                }
                json.append("buildCount:'" + company.getCommunityBuilds().size() + "',");
                json.append("unitCount:'" + unitCount + "',");
                json.append("doorplateCount:'" + doorplateCount + "',");
                json.append("roomCount:'" + roomCount + "',");
                json.append("czCount:'" + czCount + "',");
                json.append("zzCount:'" + zzCount + "',");
                json.append("tenantCount:'" + tenantCount + "',");

                json.append("address:'" + company.getAddress() + "',");
                json.append("principal:'" + company.getPrincipal() + "',");
                json.append("principal_phone:'" + company.getPrincipal_phone() + "',");
                if (logoFile.exists()) {
                    String url = "/logo/" + company.getId() + "/logo.jpg";
                    json.append("logo:'" + url + "',");
                } else {
                    json.append("logo:'',");
                }
                json.append("ssxq:'" + Region.getName(company.getRegion()) + "',");
                json.append("name:'" + company.getName() + "'");
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
            if (id != null) {
                communityDao.remove(new Community(Long.parseLong(id)));
                msg = "删除小区信息成功" + id;
                json = "{success:true,msg:'删除成功'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区管理", msg);
            }
        } catch (Exception e) {
            msg = "删除小区信息失败" + id;
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区管理", msg);
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
            String code = community.getRegion().getCode();
            Region region = regionDao.findByCode(code);
            community.setRegion(region);
            communityDao.create(community);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区管理", "用户新增小区" + community.getName() + "信息成功");
            msg = "<font color=\"green\">新增成功,点击[确定]返回列表!</font>";
        } catch (Exception e) {
            logger.error("小区管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "小区管理", "用户新增小区" + community.getName() + "信息失败");
            msg = "<font color=\"red\">新增失败</font>";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String modify_logo() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        String msg = null;
        String json = null;
        FileInputStream fileInputStream = new FileInputStream(uploadFile);
        try {
            BufferedImage image = ImageIO.read(uploadFile);
            if (image == null) {
                msg = "图片文件无法解析，请重新上传图片";
                json = "{success:false,msg:'" + msg + "'}";
            } else {
                Community community = communityDao.findById(Long.parseLong(id));
                if (community != null) {
                    Blob photo = Hibernate.createBlob(fileInputStream, (int) uploadFile.length());
                    community.setLogo(photo);
                    communityDao.update(community);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "小区管理", "用户新增小区" + community.getName() + "信息成功");
                    msg = "更新图标完成";
                    json = "{success:true,msg:'" + msg + "'}";
                }
            }
        } catch (IOException ex) {
            msg = "图片文件无法解析，请重新上传图片";
            json = "{success:false,msg:'" + msg + "'}";
        } finally {
            fileInputStream.close();
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String loadLogo() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'加载失败'}";
        String msg = null;
        String id = request.getParameter("id");
        try {
            if (id != null) {
                Community expressLog = communityDao.findById(Long.parseLong(id));
                InputStream inputStream = expressLog.getLogo().getBinaryStream();
                FileUtil.copy(inputStream, response);
                msg = "加载信息成功" + id;
                json = "{success:true,msg:'加载信息成功'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "快递信息", msg);
            }
        } catch (Exception e) {
            msg = "加载信息失败" + id;
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "快递信息", msg);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
