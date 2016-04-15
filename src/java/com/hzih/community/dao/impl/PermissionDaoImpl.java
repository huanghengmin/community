package com.hzih.community.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.community.dao.PermissionDao;
import com.hzih.community.domain.Permission;

public class PermissionDaoImpl extends MyDaoSupport implements PermissionDao {

	@Override
	public void setEntityClass() {
		this.entityClass = Permission.class;
	}

}
