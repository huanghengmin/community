package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import com.hzih.community.domain.Role;

public interface RoleDao extends BaseDao {

    public Role findByName(String name) throws Exception;
}
