package com.hzih.community.web.action.tenant;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityDoorplateDao;
import com.hzih.community.dao.CommunityRoomDao;
import com.hzih.community.dao.TenantDao;
import com.hzih.community.dao.TenantLogDao;
import com.hzih.community.domain.*;
import com.hzih.community.service.LogService;
import com.hzih.community.utils.DateUtils;
import com.hzih.community.utils.FileUtil;
import com.hzih.community.web.SessionUtils;
import com.hzih.community.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import it.sauronsoftware.base64.Base64;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 16-2-26.
 */
public class TenantAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(TenantAction.class);
    private TenantDao tenantDao;
    private TenantLogDao tenantLogDao;
    private Tenant tenant;
    private int start;
    private int limit;
    private LogService logService;
    private CommunityRoomDao communityRoomDao;
    private CommunityDoorplateDao communityDoorplateDao;

    public TenantLogDao getTenantLogDao() {
        return tenantLogDao;
    }

    public void setTenantLogDao(TenantLogDao tenantLogDao) {
        this.tenantLogDao = tenantLogDao;
    }

    public CommunityRoomDao getCommunityRoomDao() {
        return communityRoomDao;
    }

    public void setCommunityRoomDao(CommunityRoomDao communityRoomDao) {
        this.communityRoomDao = communityRoomDao;
    }

    public TenantDao getTenantDao() {
        return tenantDao;
    }

    public void setTenantDao(TenantDao tenantDao) {
        this.tenantDao = tenantDao;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
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

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String findByAccount() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Account account = SessionUtils.getAccount(request);
        String name = request.getParameter("name");
        String idCard = request.getParameter("idCard");
        StringBuilder json = null;
        try {
            PageResult pageResult = tenantDao.find(start, limit,account.getCommunity().getId(),name,idCard);
            if (pageResult != null) {
                List<Tenant> companyList = pageResult.getResults();
                int count = pageResult.getAllResultsAmount();
                if (companyList != null) {
                    json = new StringBuilder("{total:" + count + ",rows:[");
                    Iterator<Tenant> iterator = companyList.iterator();
                    while (iterator.hasNext()) {
                        Tenant lardLord = iterator.next();
                        if (iterator.hasNext()) {
                            json.append("{");
                            json.append("id:'" + lardLord.getId() + "',");
                            json.append("name:'" + lardLord.getName() + "',");
                            json.append("sex:'" + lardLord.getSex() + "',");
                            json.append("idCard:'" + lardLord.getIdCard() + "',");
                            json.append("mz:'" + lardLord.getMz() + "',");
                            json.append("birth:'" + lardLord.getBirth() + "',");
                            json.append("sign:'" + lardLord.getSign() + "',");
                            json.append("address:'" + lardLord.getAddress() + "',");
                            json.append("DN:'" + lardLord.getDN() + "',");
                            json.append("validity:'" + lardLord.getValidity() + "',");
                            json.append("phone:'" + lardLord.getPhone() + "',");
                            json.append("description:'" + lardLord.getDescription() + "',");
                            json.append("status:'" + lardLord.getStatus() + "',");
                            json.append("attention:'" + lardLord.getAttention() + "',");
                            json.append("ocr:'" + lardLord.getOcr() + "',");
                            if(lardLord.getInitDate()==null){
                                json.append("initDate:'',");
                            } else {
                                json.append("initDate:'" + DateUtils.formatDate(lardLord.getInitDate(),"yyyy-MM-dd HH:mm:ss") + "',");
                            }
                            if(lardLord.getLastDate()==null){
                                json.append("lastDate:'',");
                            } else  {
                                json.append("lastDate:'" + DateUtils.formatDate(lardLord.getLastDate(),"yyyy-MM-dd HH:mm:ss") + "',");
                            }
                            json.append("community_name:'" + lardLord.getCommunityRoom().getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getName() + "',");
                            json.append("community_address:'" + lardLord.getCommunityRoom().getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getAddress() + "',");
                            json.append("community_build:'" + lardLord.getCommunityRoom().getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getValue() + "',");
                            json.append("community_build_unit:'" + lardLord.getCommunityRoom().getCommunityDoorplate().getCommunityBuildUnit().getValue() + "',");
                            json.append("community_doorplate:'" + lardLord.getCommunityRoom().getCommunityDoorplate().getDoorplate() + "',");
                            json.append("community_room:'" + lardLord.getCommunityRoom().getRoom() + "'");
                            json.append("},");
                        } else {
                            json.append("{");
                            json.append("id:'" + lardLord.getId() + "',");
                            json.append("name:'" + lardLord.getName() + "',");
                            json.append("sex:'" + lardLord.getSex() + "',");
                            json.append("idCard:'" + lardLord.getIdCard() + "',");
                            json.append("mz:'" + lardLord.getMz() + "',");
                            json.append("birth:'" + lardLord.getBirth() + "',");
                            json.append("sign:'" + lardLord.getSign() + "',");
                            json.append("address:'" + lardLord.getAddress() + "',");
                            json.append("DN:'" + lardLord.getDN() + "',");
                            json.append("validity:'" + lardLord.getValidity() + "',");
                            json.append("phone:'" + lardLord.getPhone() + "',");
                            json.append("description:'" + lardLord.getDescription() + "',");
                            json.append("status:'" + lardLord.getStatus() + "',");
                            json.append("attention:'" + lardLord.getAttention() + "',");
                            json.append("ocr:'" + lardLord.getOcr() + "',");
                            if(lardLord.getInitDate()==null){
                                json.append("initDate:'',");
                            } else {
                                json.append("initDate:'" + DateUtils.formatDate(lardLord.getInitDate(),"yyyy-MM-dd HH:mm:ss") + "',");
                            }
                            if(lardLord.getLastDate()==null){
                                json.append("lastDate:'',");
                            } else  {
                                json.append("lastDate:'" + DateUtils.formatDate(lardLord.getLastDate(),"yyyy-MM-dd HH:mm:ss") + "',");
                            }
                            json.append("community_name:'" + lardLord.getCommunityRoom().getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getName() + "',");
                            json.append("community_address:'" + lardLord.getCommunityRoom().getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getAddress() + "',");
                            json.append("community_build:'" + lardLord.getCommunityRoom().getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getValue() + "',");
                            json.append("community_build_unit:'" + lardLord.getCommunityRoom().getCommunityDoorplate().getCommunityBuildUnit().getValue() + "',");
                            json.append("community_doorplate:'" + lardLord.getCommunityRoom().getCommunityDoorplate().getDoorplate() + "',");
                            json.append("community_room:'" + lardLord.getCommunityRoom().getRoom() + "'");
                            json.append("}");
                        }
                    }
                    json.append("]}");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }

    public String remove() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'删除租客信息失败'}";
        String msg = null;
        String id = request.getParameter("id");
        try {
            if(id!=null) {
                tenantDao.remove(new Tenant(Long.parseLong(id)));
                msg = "删除租客信息成功"+id;
                json = "{success:true,msg:\""+msg+"\"}";
            }
        }catch (Exception e){
            msg = "删除租客信息失败"+id;
            json = "{success:false,msg:\""+msg+"\"}";
        }
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }

    public String insert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String communityDoorplateId = request.getParameter("communityDoorplateId");
        String bytes = request.getParameter("bytes");
        String msg = null;
        String json = null;
        try {
            Tenant sql_lard = tenantDao.findByIdCard(communityDoorplateId, tenant.getIdCard(),1);
            if(sql_lard!=null){
                msg = "租客信息已存在!";
                json = "{success:false,msg:'"+msg+"'}";
            }else {
                CommunityRoom community = communityRoomDao.findById(Long.parseLong(communityDoorplateId));
                tenant.setCommunityRoom(community);
                byte[] b = Base64.decode(bytes).getBytes();
                Blob photo = Hibernate.createBlob(b);
                tenant.setBytes(photo);
                tenantDao.create(tenant);
                msg = "新增租客信息成功,点击[确定]返回列表!";
                json = "{success:true,msg:'"+msg+"'}";
            }
        }catch (Exception e){
            logger.error("租客管理", e);
            msg = "新增租客信息失败";
            json = "{success:false,msg:'"+msg+"'}";
        }
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }

    public String modify() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'修改失败'}";
        String msg = null;
        String id = request.getParameter("id");
        try {
            if(id!=null) {
                Tenant dataTenant = tenantDao.findById(Long.parseLong(id));
                dataTenant.setName(tenant.getName());
                msg = "修改租客信息成功"+id;
                json = "{success:true,msg:'修改成功'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "租客管理", msg);
            }
        }catch (Exception e){
            msg = "修改租客信息失败"+id;
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "租客管理", msg);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findByIds() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String ids = request.getParameter("ids");
        StringBuilder json = null;
        try {
            List list = tenantDao.findByeIds(ids);
            if (list != null) {
                int count = list.size();
                json = new StringBuilder("{total:" + count + ",rows:[");
                Iterator<Tenant> iterator = list.iterator();
                while (iterator.hasNext()) {
                    Tenant lardLord = iterator.next();
                    if (iterator.hasNext()) {
                        json.append("{");
                        json.append("id:'" + lardLord.getId() + "',");
                        json.append("name:'" + lardLord.getName() + "',");
                        json.append("sex:'" + lardLord.getSex() + "',");
                        json.append("idCard:'" + lardLord.getIdCard() + "',");
                        json.append("mz:'" + lardLord.getMz() + "',");
                        json.append("birth:'" + lardLord.getBirth() + "',");
                        json.append("sign:'" + lardLord.getSign() + "',");
                        json.append("address:'" + lardLord.getAddress() + "',");
                        json.append("DN:'" + lardLord.getDN() + "',");
                        json.append("validity:'" + lardLord.getValidity() + "',");
                        json.append("phone:'" + lardLord.getPhone() + "',");
                        json.append("room:'" + lardLord.getCommunityRoom().getRoom() + "',");
                        json.append("description:'" + lardLord.getDescription() + "',");
                        json.append("status:'" + lardLord.getStatus() + "',");
                        if(lardLord.getInitDate()==null){
                            json.append("initDate:'',");
                        } else {
                            json.append("initDate:'" + DateUtils.formatDate(lardLord.getInitDate(), "yyyy-MM-dd HH:mm:ss") + "',");
                        }
                        if(lardLord.getLastDate()==null){
                            json.append("lastDate:''");
                        } else  {
                            json.append("lastDate:'" + DateUtils.formatDate(lardLord.getLastDate(),"yyyy-MM-dd HH:mm:ss") + "'");
                        }

                        json.append("},");
                    } else {
                        json.append("{");
                        json.append("id:'" + lardLord.getId() + "',");
                        json.append("name:'" + lardLord.getName() + "',");
                        json.append("sex:'" + lardLord.getSex() + "',");
                        json.append("idCard:'" + lardLord.getIdCard() + "',");
                        json.append("mz:'" + lardLord.getMz() + "',");
                        json.append("birth:'" + lardLord.getBirth() + "',");
                        json.append("sign:'" + lardLord.getSign() + "',");
                        json.append("address:'" + lardLord.getAddress() + "',");
                        json.append("DN:'" + lardLord.getDN() + "',");
                        json.append("validity:'" + lardLord.getValidity() + "',");
                        json.append("phone:'" + lardLord.getPhone() + "',");
                        json.append("room:'" + lardLord.getCommunityRoom().getRoom() + "',");
                        json.append("description:'" + lardLord.getDescription() + "',");
                        json.append("status:'" + lardLord.getStatus() + "',");
                        if(lardLord.getInitDate()==null){
                            json.append("initDate:'',");
                        } else {
                            json.append("initDate:'" + DateUtils.formatDate(lardLord.getInitDate(),"yyyy-MM-dd HH:mm:ss") + "',");
                        }
                        if(lardLord.getLastDate()==null){
                            json.append("lastDate:''");
                        } else  {
                            json.append("lastDate:'" + DateUtils.formatDate(lardLord.getLastDate(),"yyyy-MM-dd HH:mm:ss") + "'");
                        }
                        json.append("}");
                    }
                }
                json.append("]}");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }

    public String loadTenantHead() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        String msg = null;
        String id = request.getParameter("id");
        InputStream in = null;
        Tenant lardLord = tenantDao.findById(Long.parseLong(id));
        if (lardLord.getBytes() != null) {
            in = lardLord.getBytes().getBinaryStream();
        } else {
            String str = request.getServletContext().getRealPath("js") + "/ext/resources/images/default/s.gif";
            in = new FileInputStream(str);
        }
        FileUtil.copy(in, response);
        return null;
    }

    public String insertByClient() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String msg = null;
        String json = null;
        try {
            String qrCode = request.getParameter("qrCode");
            String sss[] = qrCode.split("-");
            CommunityRoom communityRoom = communityRoomDao.findById(Long.parseLong(sss[sss.length - 1]));
            if(communityRoom!=null){
                if (tenant.getOcr() == 1) {
                    String idCard_before = request.getParameter("idCard_before");
                    byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(idCard_before.getBytes());
                    Blob photo = Hibernate.createBlob(b);
                    tenant.setCardPicBefore(photo);
                    tenant.setCommunityRoom(communityRoom);
                    tenant.setInitDate(new Date());
                    tenantDao.create(tenant);
                    msg = "新增租客信息成功!";
                    json = "{success:true,msg:'" + msg + "'}";
                } else {
                    Tenant l = tenantDao.findByIdCard(communityRoom.getCommunityDoorplate().getId(), tenant.getIdCard());
                    if (l != null) {
                        l.setLastDate(new Date());
                        tenantDao.update(l);
                        msg = "租客信息在此门牌下已存在！";
                        json = "{success:false,msg:'" + msg + "'}";
                    } else {
                        String bytes = request.getParameter("bytes");
                        byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(bytes.getBytes());
                        Blob photo = Hibernate.createBlob(b);
                        tenant.setBytes(photo);
                        tenant.setCommunityRoom(communityRoom);
                        tenant.setInitDate(new Date());
                        tenantDao.create(tenant);
                        msg = "新增租客信息成功!";
                        json = "{success:true,msg:'" + msg + "'}";
                    }
                }
            }else {
                CommunityDoorplate communityDoorplate = communityDoorplateDao.findById(Long.parseLong(sss[sss.length - 1]));
                if (communityDoorplate != null) {
                    String room = request.getParameter("room");
                    CommunityRoom communityR = communityRoomDao.findByRoom(communityDoorplate.getId(), room);
                    if(communityR==null){
                        CommunityRoom communityRoom1 = new CommunityRoom();
                        communityRoom1.setRoom(Integer.parseInt(room));
                        communityRoom1.setCommunityDoorplate(communityDoorplate);
                        communityRoomDao.create(communityRoom1);
                        communityR = communityRoomDao.findByRoom(communityDoorplate.getId(), room);
                    }
                    if (tenant.getOcr() == 1) {
                        String idCard_before = request.getParameter("idCard_before");
                        byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(idCard_before.getBytes());
                        Blob photo = Hibernate.createBlob(b);
                        tenant.setCardPicBefore(photo);
                        tenant.setCommunityRoom(communityR);
                        tenant.setInitDate(new Date());
                        tenantDao.create(tenant);
                        msg = "新增租客信息成功!";
                        json = "{success:true,msg:'" + msg + "'}";
                    } else {
                        Tenant l = tenantDao.findByIdCard(communityDoorplate.getId(), tenant.getIdCard());
                        if (l != null) {
                            l.setLastDate(new Date());
                            tenantDao.update(l);
                            msg = "租客信息在此门牌下已存在！";
                            json = "{success:false,msg:'" + msg + "'}";
                        } else {
                            String bytes = request.getParameter("bytes");
                            byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(bytes.getBytes());
                            Blob photo = Hibernate.createBlob(b);
                            tenant.setBytes(photo);
                            tenant.setCommunityRoom(communityR);
                            tenant.setInitDate(new Date());
                            tenantDao.create(tenant);
                            msg = "新增租客信息成功!";
                            json = "{success:true,msg:'" + msg + "'}";
                        }
                    }
                }
            }
        }catch (Exception e){
            msg = "新增租客信息失败!";
            json = "{success:false,msg:'" + msg + "'}";
        }
        writer.write(json);
        writer.flush();
        writer.close();
        return null;
    }

    public String exitByClient() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String id = request.getParameter("id");
        String msg = null;
        String json = null;
        try {
            Tenant tenant = tenantDao.findById(Long.parseLong(id));
            if(tenant!=null){
                TenantLog tenantLog =  new TenantLog();
                tenantLog.setId(tenant.getId());
                tenantLog.setName(tenant.getName());
                tenantLog.setSex(tenant.getSex());
                tenantLog.setIdCard(tenant.getIdCard());
                tenantLog.setMz(tenant.getMz());
                tenantLog.setBirth(tenant.getBirth());
                tenantLog.setSign(tenant.getSign());
                tenantLog.setAddress(tenant.getAddress());
                tenantLog.setDN(tenant.getDN());
                tenantLog.setValidity(tenant.getValidity());
                tenantLog.setPhone(tenant.getPhone());
                tenantLog.setDescription(tenant.getDescription());
                tenantLog.setBytes(tenant.getBytes());
                tenantLog.setCardPicBefore(tenant.getCardPicBefore());
                tenantLog.setCardPicAfter(tenant.getCardPicAfter());
                tenantLog.setStatus(tenant.getStatus());
                tenantLog.setAttention(tenant.getAttention());
                tenantLog.setOcr(tenant.getOcr());
                tenantLog.setInitDate(tenant.getInitDate());
                tenantLog.setLastDate(tenant.getLastDate());
                tenantLog.setExitDate(new Date());
                tenantLog.setCommunityRoom(tenant.getCommunityRoom());
                tenantLogDao.create(tenantLog);
                tenantDao.remove(tenant);
                msg = "退租成功!";
                json = "{success:true,msg:'" + msg + "'}";
            }

        }catch (Exception e){
            msg = "退租失败!";
            json = "{success:false,msg:'" + msg + "'}";
        }
        writer.write(json);
        writer.flush();
        writer.close();
        return null;
    }

    public CommunityDoorplateDao getCommunityDoorplateDao() {
        return communityDoorplateDao;
    }

    public void setCommunityDoorplateDao(CommunityDoorplateDao communityDoorplateDao) {
        this.communityDoorplateDao = communityDoorplateDao;
    }
}
