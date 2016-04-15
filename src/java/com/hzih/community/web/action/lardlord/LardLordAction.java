package com.hzih.community.web.action.lardlord;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityDoorplateDao;
import com.hzih.community.dao.LardLordDao;
import com.hzih.community.domain.*;
import com.hzih.community.service.LogService;
import com.hzih.community.utils.DateUtils;
import com.hzih.community.utils.FileUtil;
import com.hzih.community.web.SessionUtils;
import com.hzih.community.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Blob;
import java.util.*;

/**
 * Created by Administrator on 16-2-26.
 */
public class LardLordAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(LardLordAction.class);
    private LardLordDao lardLordDao;
    private LardLord lardLord;
    private int start;
    private int limit;
    private LogService logService;
    private CommunityDoorplateDao communityDoorplateDao;


    public LardLordDao getLardLordDao() {
        return lardLordDao;
    }

    public void setLardLordDao(LardLordDao lardLordDao) {
        this.lardLordDao = lardLordDao;
    }

    public LardLord getLardLord() {
        return lardLord;
    }

    public void setLardLord(LardLord lardLord) {
        this.lardLord = lardLord;
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

    public CommunityDoorplateDao getCommunityDoorplateDao() {
        return communityDoorplateDao;
    }

    public void setCommunityDoorplateDao(CommunityDoorplateDao communityDoorplateDao) {
        this.communityDoorplateDao = communityDoorplateDao;
    }

    public String findByIds() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String ids = request.getParameter("ids");
        StringBuilder json = null;
        try {
            List list = lardLordDao.findByeIds(ids);
            if (list != null) {
                int count = list.size();
                json = new StringBuilder("{total:" + count + ",rows:[");
                Iterator<LardLord> iterator = list.iterator();
                while (iterator.hasNext()) {
                    LardLord lardLord = iterator.next();
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
                        json.append("xzz:'" + lardLord.getXzz() + "',");
                        json.append("description:'" + lardLord.getDescription() + "',");
                        json.append("status:'" + lardLord.getStatus() + "',");
                        if (lardLord.getInitDate() == null) {
                            json.append("initDate:'',");
                        } else {
                            json.append("initDate:'" + DateUtils.formatDate(lardLord.getInitDate(), "yyyy-MM-dd HH:mm:ss") + "',");
                        }
                        if (lardLord.getLastDate() == null) {
                            json.append("lastDate:''");
                        } else {
                            json.append("lastDate:'" + DateUtils.formatDate(lardLord.getLastDate(), "yyyy-MM-dd HH:mm:ss") + "'");
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
                        json.append("xzz:'" + lardLord.getXzz() + "',");
                        json.append("description:'" + lardLord.getDescription() + "',");
                        json.append("status:'" + lardLord.getStatus() + "',");
                        if (lardLord.getInitDate() == null) {
                            json.append("initDate:'',");
                        } else {
                            json.append("initDate:'" + DateUtils.formatDate(lardLord.getInitDate(), "yyyy-MM-dd HH:mm:ss") + "',");
                        }
                        if (lardLord.getLastDate() == null) {
                            json.append("lastDate:''");
                        } else {
                            json.append("lastDate:'" + DateUtils.formatDate(lardLord.getLastDate(), "yyyy-MM-dd HH:mm:ss") + "'");
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
            PageResult pageResult = lardLordDao.find(start, limit, account.getCommunity().getId(), name, idCard);
            if (pageResult != null) {
                List<LardLord> companyList = pageResult.getResults();
                int count = pageResult.getAllResultsAmount();
                if (companyList != null) {
                    json = new StringBuilder("{total:" + count + ",rows:[");
                    Iterator<LardLord> iterator = companyList.iterator();
                    while (iterator.hasNext()) {
                        LardLord lardLord = iterator.next();
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
                            json.append("xzz:'" + lardLord.getXzz() + "',");
                            json.append("attention:'" + lardLord.getAttention() + "',");
                            json.append("ocr:'" + lardLord.getOcr() + "',");
                            if (lardLord.getInitDate() == null) {
                                json.append("initDate:'',");
                            } else {
                                json.append("initDate:'" + DateUtils.formatDate(lardLord.getInitDate(), "yyyy-MM-dd HH:mm:ss") + "',");
                            }
                            if (lardLord.getLastDate() == null) {
                                json.append("lastDate:'',");
                            } else {
                                json.append("lastDate:'" + DateUtils.formatDate(lardLord.getLastDate(), "yyyy-MM-dd HH:mm:ss") + "',");
                            }
                            json.append("community_name:'" + lardLord.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getName() + "',");
                            json.append("community_address:'" + lardLord.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getAddress() + "',");
                            json.append("community_build:'" + lardLord.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getValue() + "',");
                            json.append("community_build_unit:'" + lardLord.getCommunityDoorplate().getCommunityBuildUnit().getValue() + "',");
                            json.append("community_doorplate:'" + lardLord.getCommunityDoorplate().getDoorplate() + "'");
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
                            json.append("xzz:'" + lardLord.getXzz() + "',");
                            json.append("attention:'" + lardLord.getAttention() + "',");
                            json.append("ocr:'" + lardLord.getOcr() + "',");
                            if (lardLord.getInitDate() == null) {
                                json.append("initDate:'',");
                            } else {
                                json.append("initDate:'" + DateUtils.formatDate(lardLord.getInitDate(), "yyyy-MM-dd HH:mm:ss") + "',");
                            }
                            if (lardLord.getLastDate() == null) {
                                json.append("lastDate:'',");
                            } else {
                                json.append("lastDate:'" + DateUtils.formatDate(lardLord.getLastDate(), "yyyy-MM-dd HH:mm:ss") + "',");
                            }
                            json.append("community_name:'" + lardLord.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getName() + "',");
                            json.append("community_address:'" + lardLord.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getCommunity().getAddress() + "',");
                            json.append("community_build:'" + lardLord.getCommunityDoorplate().getCommunityBuildUnit().getCommunityBuild().getValue() + "',");
                            json.append("community_build_unit:'" + lardLord.getCommunityDoorplate().getCommunityBuildUnit().getValue() + "',");
                            json.append("community_doorplate:'" + lardLord.getCommunityDoorplate().getDoorplate() + "'");
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
        String json = "{success:false,msg:'删除失败'}";
        String msg = null;
        String id = request.getParameter("id");
        try {
            if (id != null) {
                lardLordDao.remove(new LardLord(Long.parseLong(id)));
                msg = "删除房东信息成功" + id;
                json = "{success:true,msg:\"" + msg + "\"}";
            }
        } catch (Exception e) {
            msg = "删除房东信息失败" + id;
            json = "{success:true,msg:\"" + msg + "\"}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String loadLardHead() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String msg = null;
        String id = request.getParameter("id");
        InputStream in = null;
        LardLord lardLord = lardLordDao.findById(Long.parseLong(id));
        if (lardLord.getBytes() != null) {
            in = lardLord.getBytes().getBinaryStream();
        } else {
            String str = request.getServletContext().getRealPath("js") + "/ext/resources/images/default/s.gif";
            in = new FileInputStream(str);
        }
        FileUtil.copy(in, response);

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
            if (id != null) {
                LardLord dataLardLord = lardLordDao.findById(Long.parseLong(id));
                dataLardLord.setName(lardLord.getName());
                dataLardLord.setLastDate(new Date());
                msg = "修改房东信息成功" + id;
                json = "{success:true,msg:'修改成功'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "房东管理", msg);
            }
        } catch (Exception e) {
            msg = "修改房东信息失败" + id;
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "房东管理", msg);
        }
        actionBase.actionEnd(response, json, result);
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
            CommunityDoorplate communityDoorplate = communityDoorplateDao.findById(Long.parseLong(sss[sss.length - 1]));
            if (communityDoorplate != null) {
                if (lardLord.getOcr() == 1) {
                    String idCard_before = request.getParameter("idCard_before");
                    byte[] b = Base64.decodeBase64(idCard_before.getBytes());
                    Blob photo = Hibernate.createBlob(b);
                    lardLord.setCardPicBefore(photo);
                    lardLord.setStatus(1);
                    lardLord.setCommunityDoorplate(communityDoorplate);
                    lardLord.setInitDate(new Date());
                    lardLordDao.clearLardLord();
                    lardLordDao.create(lardLord);
                    msg = "新增房东信息成功!";
                    json = "{success:true,msg:'" + msg + "'}";
                } else {
                    LardLord l = lardLordDao.findByIdCard(communityDoorplate.getId(), lardLord.getIdCard());
                    if (l != null) {
                        l.setLastDate(new Date());
                        l.setStatus(1);
                        lardLordDao.clearLardLord();
                        lardLordDao.update(l);
                        msg = "房东信息已存在此门牌，无需再次添加！";
                        json = "{success:false,msg:'" + msg + "'}";
                    } else {
                        String bytes = request.getParameter("bytes");
                        byte[] b = Base64.decodeBase64(bytes.getBytes());
                        Blob photo = Hibernate.createBlob(b);
                        lardLord.setBytes(photo);
                        lardLord.setStatus(1);
                        lardLord.setCommunityDoorplate(communityDoorplate);
                        lardLord.setInitDate(new Date());
                        lardLordDao.clearLardLord();
                        lardLordDao.create(lardLord);
                        msg = "新增房东信息成功!";
                        json = "{success:true,msg:'" + msg + "'}";
                    }
                }
            }
        }catch (Exception e){
            msg = "新增房东信息失败!";
            json = "{success:false,msg:'" + msg + "'}";
        }
        writer.write(json);
        writer.flush();
        writer.close();
        return null;
    }

}
