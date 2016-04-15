package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.domain.Community;

public interface CommunityDao extends BaseDao {

    PageResult find(int start, int limit,String name,String address);

    Community findById(long id);

    void remove(Community community);
}
