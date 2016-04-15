package com.hzih.community.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityDao;
import com.hzih.community.domain.Community;
import com.hzih.community.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class CommunityDaoImpl extends MyDaoSupport implements CommunityDao {

    @Override
    public void setEntityClass() {
        this.entityClass = Community.class;
    }

    @Override
    public PageResult find(int start, int limit,String name,String address) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from Community s where 1=1");
        List params = new ArrayList();// 手动指定容量，避免多次扩容

        if (StringUtils.isNotBlank(name)) {
            sb.append(" and name like ?");
            params.add("%" + name + "%");
        }
        if (StringUtils.isNotBlank(address)) {
            sb.append(" and address like ?");
            params.add("%" + address + "%");
        }

        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params .toArray(), pageIndex, limit);
        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                + ps.getResults().size());
        return ps;
    }

    @Override
    public Community findById(long id) {
        String hql = new String("from Community where id="+id);
        List<Community> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void remove(Community community) {
        getHibernateTemplate().delete(community);
    }
}
