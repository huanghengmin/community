package com.hzih.community.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.domain.Account;

public interface AccountDao extends BaseDao {

	PageResult listByPage(String userName, int userType,String status, int pageIndex, int limit);

	Account findByNameAndPwd(String name, String pwd);

    Account findByName(String userName);

	Account findById(long aId);
}
