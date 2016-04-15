package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.domain.Tenant;

import java.util.List;

public interface TenantDao extends BaseDao {

    PageResult find(int start, int limit, long community_doorplate_id);

    PageResult find(int start, int limit,long community_id, String name,String idCard);

    Tenant findById(long id);

    boolean remove(Tenant lardLord);

    boolean add(Tenant lardLord);

    boolean modify(Tenant lardLord);

    Tenant findByIdCard(String communityDoorplateId, String idCard,int status);

    List findByeIds(String ids);

    Tenant find(int type_ocr);

    Tenant findByIdCard(long id, String idCard);
}
