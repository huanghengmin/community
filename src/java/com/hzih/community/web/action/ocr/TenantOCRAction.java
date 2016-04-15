package com.hzih.community.web.action.ocr;

import com.hzih.community.dao.TenantDao;
import com.hzih.community.domain.Tenant;
import com.hzih.community.service.LogService;
import com.hzih.community.utils.FileUtil;
import com.hzih.community.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Created by Administrator on 16-3-15.
 */
public class TenantOCRAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(TenantOCRAction.class);

    private TenantDao tenantDao;
    private Tenant tenant;
    private LogService logService;

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public TenantDao getTenantDao() {
        return tenantDao;
    }

    public void setTenantDao(TenantDao tenantDao) {
        this.tenantDao = tenantDao;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String loadPic() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        String id = request.getParameter("id");
        Tenant tenant = tenantDao.findById(Long.parseLong(id));
        InputStream inputStream = tenant.getCardPicBefore().getBinaryStream();
        FileUtil.copy(inputStream, response);
        return null;
    }

    public String saveOcr() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'保存人员信息失败'}";
        String msg = null;
        try {
            Tenant l = tenantDao.findById(tenant.getId());
            l.setOcr(0);
            l.setName(tenant.getName());
            l.setSex(tenant.getSex());
            l.setMz(tenant.getMz());
            l.setBirth(tenant.getBirth());
            l.setAddress(tenant.getAddress());
            l.setIdCard(tenant.getIdCard());
            l.setValidity(tenant.getValidity());
            l.setSign(tenant.getSign());
            tenantDao.update(l);
            json = "{success:true,msg:'保存人员信息成功'}";
        } catch (Exception e) {
            msg = "保存人员信息失败";
            json = "{success:true,msg:\"" + msg + "\"}";
        }
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }

    public String nextOcr() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        String json = "{success:false,msg:'跳到下一张失败'}";
        String msg = null;
        try {
            Tenant l = tenantDao.findById(Long.parseLong(id));
            l.setOcr(2);
            tenantDao.update(l);
            json = "{success:true,flag:true,msg:'跳到下一张成功'}";
        } catch (Exception e) {
            msg = "跳到下一张失败";
            json = "{success:true,msg:\"" + msg + "\"}";
        }
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }

    public String getPic() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'获取人员信息失败'}";
        String msg = null;
        String id = null;
        try {
            int type_ocr = 1;
            Tenant expressLog = tenantDao.find(type_ocr);
            if (expressLog != null) {
                id = String.valueOf(expressLog.getId());
                if (id == null) {
                    msg = "不存在需要处理的照片！";
                    json = "{success:false,msg:'" + msg + "'}";
                } else {
                    msg = "需要处理的数据Id:" + id;
                    json = "{success:true,id:" + id + ",msg:'" + msg + "'}";
                }
            } else {
                int type_next = 2;
                Tenant log = tenantDao.find(type_next);
                id = String.valueOf(log.getId());
                if (id == null) {
                    msg = "不存在需要处理的照片！";
                    json = "{success:false,msg:'" + msg + "'}";
                } else {
                    msg = "需要处理的数据Id:" + id;
                    json = "{success:true,id:" + id + ",msg:'" + msg + "'}";
                }
            }
        } catch (Exception e) {
            msg = "获取人员信息失败！";
            json = "{success:true,msg:\"" + msg + "\"}";
        } finally {
            id = null;
        }
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }
}
