package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.domain.Region;

public interface RegionDao extends BaseDao {

	PageResult findProvince(int start, int limit);

	Region findByCode(String code);

	Region findById(int id);

	PageResult findChild(int start, int limit, String parentCode);

}
