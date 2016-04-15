package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.domain.TenantLog;

import java.util.List;

public interface TenantLogDao extends BaseDao {

    PageResult find(int start, int limit, long community_doorplate_id);

    PageResult find(int start, int limit, long community_id, String name, String idCard);

    TenantLog findById(long id);

    boolean remove(TenantLog lardLord);

    boolean add(TenantLog lardLord);

    boolean modify(TenantLog lardLord);

    TenantLog findByIdCard(String communityDoorplateId, String idCard, int status);

    List findByeIds(String ids);

    TenantLog find(int type_ocr);

    TenantLog findByIdCard(long id, String idCard);
}
