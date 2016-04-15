package com.hzih.community.web.action.ocr;

import com.hzih.community.dao.LardLordDao;
import com.hzih.community.domain.LardLord;
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
public class LardLordOCRAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(LardLordOCRAction.class);
    private LardLordDao lardLordDao;
    private LardLord lardLord;

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

    private LogService logService;

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
        LardLord tenant = lardLordDao.findById(Long.parseLong(id));
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
            LardLord l = lardLordDao.findById(lardLord.getId());
            l.setOcr(0);
            l.setName(lardLord.getName());
            l.setSex(lardLord.getSex());
            l.setMz(lardLord.getMz());
            l.setBirth(lardLord.getBirth());
            l.setAddress(lardLord.getAddress());
            l.setIdCard(lardLord.getIdCard());
            l.setValidity(lardLord.getValidity());
            l.setSign(lardLord.getSign());
            lardLordDao.update(l);
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
            LardLord l = lardLordDao.findById(Long.parseLong(id));
            l.setOcr(2);
            lardLordDao.update(l);
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
            LardLord expressLog = lardLordDao.find(type_ocr);
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
                LardLord log = lardLordDao.find(type_next);
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
