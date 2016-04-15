package com.hzih.community.web.action.center;

import com.hzih.community.domain.*;
import com.hzih.community.service.LogService;
import com.hzih.community.web.SessionUtils;
import com.hzih.community.web.action.ActionBase;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 16-3-7.
 */
public class CenterAction {

    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    private Logger logger = Logger.getLogger(CenterAction.class);


    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Account account = SessionUtils.getAccount(request);
        String json = null;
        String msg = null;
        try {
            Community community = account.getCommunity();
            if(community!=null) {
                int idx = 0;
                StringBuilder stringBuilder = new StringBuilder("{root:[");
                stringBuilder.append("{name:'login_info',fieldName:'你好',value:'" +account.getName()  + "！'},");
                idx++;
                stringBuilder.append("{name:'community_address',fieldName:'详细地址',value:'" + Region.getName(community.getRegion()) + community.getAddress() + "'},");
                idx++;
                stringBuilder.append("{name:'community_name',fieldName:'社区名称',value:'" + community.getName() + "'},");
                idx++;
                stringBuilder.append("{name:'principal',fieldName:'负责人',value:'" + community.getPrincipal() + "'},");
                idx++;
                stringBuilder.append("{name:'principal_phone',fieldName:'负责人电话',value:'" + community.getPrincipal_phone() + "'},");
                idx++;
                int unitCount = 0;
                int doorplateCount = 0;
                int roomCount = 0;
                int zzCount = 0;
                int czCount = 0;
                int tenantCount = 0;

                Set<CommunityBuild> communityBuildSet = community.getCommunityBuilds();
                Iterator<CommunityBuild> ite = communityBuildSet.iterator();
                while (ite.hasNext()) {
                    CommunityBuild build = ite.next();
                    Set<CommunityBuildUnit> communityBuildUnitSet = build.getCommunityBuildUnits();
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
                }
                stringBuilder.append("{name:'buildCount',fieldName:'楼栋总数',value:'" + communityBuildSet.size() + "'},");
                idx++;
                stringBuilder.append("{name:'unitCount',fieldName:'单元总数',value:'" + unitCount + "'},");
                idx++;
                stringBuilder.append("{name:'doorplateCount',fieldName:'门牌总数',value:'" + doorplateCount + "'},");
                idx++;
                stringBuilder.append("{name:'roomCount',fieldName:'房间总数',value:'" + roomCount + "'},");
                idx++;
                stringBuilder.append("{name:'zzCount',fieldName:'自住总户数',value:'" + zzCount + "'},");
                idx++;
                stringBuilder.append("{name:'czCount',fieldName:'出租总户数',value:'" + czCount + "'},");
                idx++;
                stringBuilder.append("{name:'tenantCount',fieldName:'租住人数',value:'" + tenantCount + "'}");
                idx++;
                stringBuilder.append("],totalCount:" + idx + "}");
                json = stringBuilder.toString();
                msg = "查询登陆信息成功";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "登陆信息", msg);
            }else {
                int idx = 0;
                StringBuilder stringBuilder = new StringBuilder("{root:[");
                stringBuilder.append("{name:'login_info',fieldName:'你好',value:'" +account.getName()  + "！'},");
                idx++;
                stringBuilder.append("],totalCount:" + idx + "}");
                json = stringBuilder.toString();
                msg = "查询登陆信息成功";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "登陆信息", msg);
            }
        } catch (Exception e) {
            msg = "查询登陆信息出错";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "登陆信息", msg);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
