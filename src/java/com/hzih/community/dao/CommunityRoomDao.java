package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.domain.CommunityRoom;

import java.util.List;

public interface CommunityRoomDao extends BaseDao {

    PageResult find(int start, int limit, String community_id, String community_build_id, String community_build_unit_id, String doorplate,String room);

    PageResult find(int start, int limit, String community_id);

    PageResult find(int start, int limit);

    CommunityRoom findById(long id);

    boolean add(CommunityRoom communityRoom);

    boolean remove(long id);

    CommunityRoom findByRoom(long id, String room);
}
