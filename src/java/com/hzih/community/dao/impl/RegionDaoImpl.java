package com.hzih.community.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.RegionDao;
import com.hzih.community.domain.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16-3-18.
 */
public class RegionDaoImpl extends MyDaoSupport implements RegionDao {

    @Override
    public PageResult findProvince(int start, int limit) {
        int pageIndex = start / limit + 1;
        String hql = " from Region r where r.code like '__0000'";
        String countHql = "select count(*) " + hql;
        PageResult ps = findByPage(hql, countHql, pageIndex, limit);
        return ps;
    }

    @Override
    public Region findByCode(String code) {
        String hql = new String(" from Region where code='" + code + "'");
        List<Region> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Region findById(int id) {
        String hql = new String(" from Region where id=" + id);
        List<Region> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }


    @Override
    public PageResult findChild(int start, int limit, String parentCode) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from Region s where 1=1");
        List params = new ArrayList();// 手动指定容量，避免多次扩容
            sb.append(" and s.parentRegion.code = ?");
            params.add(parentCode);

            String countString = "select count(*) " + sb.toString();
            String queryString = sb.toString();

            PageResult ps = this.findByPage(queryString, countString, params.toArray(), pageIndex, limit);
            logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                    + ps.getResults().size());
            return ps;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = Region.class;
    }
}
