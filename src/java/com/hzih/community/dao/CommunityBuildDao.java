package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.domain.CommunityBuild;

import java.util.List;

public interface CommunityBuildDao extends BaseDao {

    PageResult find(int start, int limit,String community_id,String value);

    PageResult find(int start, int limit,String community_id);

    List<CommunityBuild> find(String community_id,String value);

    CommunityBuild findById(long id);

    void remove(CommunityBuild b);
}
