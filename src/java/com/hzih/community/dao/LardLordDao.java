package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.domain.LardLord;

import java.util.List;

public interface LardLordDao extends BaseDao {

    PageResult find(int start, int limit, long community_doorplate_id);

    LardLord findById(long id);

    boolean remove(LardLord lardLord);

    boolean add(LardLord lardLord);

    boolean modify(LardLord lardLord);

    List findByeIds(String ids);

    PageResult find(int start, int limit, String communityId, String buildId, String unitId, String doorplate, String room, String name, String idCard, String phone, int type);

    PageResult find(int start, int limit, long id, String name, String idCard);

    LardLord find(int type_ocr);

    LardLord findByIdCard(long id, String idCard);

    boolean clearLardLord();
}
