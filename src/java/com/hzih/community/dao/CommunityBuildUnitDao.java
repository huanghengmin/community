package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.domain.CommunityBuildUnit;

import java.util.List;

public interface CommunityBuildUnitDao extends BaseDao {

    PageResult find(int start, int limit,String community_id,String company_build_id,String value);

    CommunityBuildUnit findById(long id);

    void remove(CommunityBuildUnit unit);

    List<CommunityBuildUnit> find(String community_id, String build, String unit);


}
