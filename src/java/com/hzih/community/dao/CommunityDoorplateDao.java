package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.domain.CommunityDoorplate;

import java.util.List;

public interface CommunityDoorplateDao extends BaseDao {

    PageResult find(int start, int limit,String community_id,String community_build_id,String community_build_unit_id,String doorplate);

    PageResult find(int start, int limit,String community_id);

    PageResult find(int start, int limit);

    CommunityDoorplate findById(long id);

    boolean add(CommunityDoorplate communityDoorplate);

    boolean remove(long id);

    List<CommunityDoorplate> find(String community_id, String build, String unit, String doorplate);

    boolean czDoorplate(String id);

    boolean zzDoorplate(String id);
}
