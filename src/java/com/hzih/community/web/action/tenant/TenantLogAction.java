package com.hzih.community.web.action.tenant;

import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.TenantLogDao;
import com.hzih.community.domain.Account;
import com.hzih.community.domain.TenantLog;
import com.hzih.community.service.LogService;
import com.hzih.community.utils.DateUtils;
import com.hzih.community.utils.FileUtil;
import com.hzih.community.web.SessionUtils;
import com.hzih.community.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 16-2-26.
 */
public class TenantLogAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(TenantLogAction.class);
    private TenantLogDao tenantLogDao;
    private int start;
    private int limit;
    private LogService logService;

    public TenantLogDao getTenantLogDao() {
        return tenantLogDao;
    }

    public void setTenantLogDao(TenantLogDao tenantLogDao) {
        this.tenantLogDao = tenantLogDao;
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
            PageResult pageResult = tenantLogDao.find(start, limit,account.getCommunity().getId(),name,idCard);
            if (pageResult != null) {
                List<TenantLog> companyList = pageResult.getResults();
                int count = pageResult.getAllResultsAmount();
                if (companyList != null) {
                    json = new StringBuilder("{total:" + count + ",rows:[");
                    Iterator<TenantLog> iterator = companyList.iterator();
                    while (iterator.hasNext()) {
                        TenantLog lardLord = iterator.next();
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
        String json = "{success:false,msg:'删除租客退租信息失败'}";
        String msg = null;
        String id = request.getParameter("id");
        try {
            if(id!=null) {
                tenantLogDao.remove(new TenantLog(Long.parseLong(id)));
                msg = "删除租客退租信息成功"+id;
                json = "{success:true,msg:\""+msg+"\"}";
            }
        }catch (Exception e){
            msg = "删除租客退租信息失败"+id;
            json = "{success:false,msg:\""+msg+"\"}";
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
        TenantLog lardLord = tenantLogDao.findById(Long.parseLong(id));
        if (lardLord.getBytes() != null) {
            in = lardLord.getBytes().getBinaryStream();
        } else {
            String str = request.getServletContext().getRealPath("js") + "/ext/resources/images/default/s.gif";
            in = new FileInputStream(str);
        }
        FileUtil.copy(in, response);
        return null;
    }

}
