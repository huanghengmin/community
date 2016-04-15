package com.hzih.community.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityBuildUnitDao;
import com.hzih.community.domain.CommunityBuild;
import com.hzih.community.domain.CommunityBuildUnit;
import com.hzih.community.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class CommunityBuildUnitDaoImpl extends MyDaoSupport implements CommunityBuildUnitDao {

    @Override
    public void setEntityClass() {
        this.entityClass = CommunityBuildUnit.class;
    }

    @Override
    public PageResult find(int start, int limit,String community_id,String company_build_id,String value) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from CommunityBuildUnit s where 1=1");
        List params = new ArrayList();// 手动指定容量，避免多次扩容

        if (StringUtils.isNotBlank(community_id)) {
            sb.append(" and s.communityBuild.community.id = ?");
            params.add(Long.parseLong(community_id));
        }

        if (StringUtils.isNotBlank(company_build_id)) {
            sb.append(" and s.communityBuild.id = ?");
            params.add(Long.parseLong(company_build_id));
        }
        if (StringUtils.isNotBlank(value)) {
            sb.append(" and s.value like ?");
            params.add("%"+value+"%");
        }
        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();
        PageResult ps = this.findByPage(queryString, countString,params.toArray(), pageIndex, limit);
        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                + ps.getResults().size());
        return ps;
    }

    @Override
    public CommunityBuildUnit findById(long id) {
        String hql = new String("from CommunityBuildUnit where id="+id);
        List<CommunityBuildUnit> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void remove(CommunityBuildUnit community) {
        getHibernateTemplate().delete(community);
    }

    @Override
    public List<CommunityBuildUnit> find(String community_id, String build, String unit) {
        StringBuffer sb = new StringBuffer(" from CommunityBuildUnit s where 1=1");
        if (StringUtils.isNotBlank(community_id)) {
            sb.append(" and s.communityBuild.community.id = " + community_id);
        }
        if (StringUtils.isNotBlank(build)) {
            sb.append(" and s.communityBuild.id = "+ build);
        }
        if (StringUtils.isNotBlank(unit)) {
            sb.append(" and s.value = "+ unit);
        }
        List<CommunityBuildUnit> list = getHibernateTemplate().find(sb.toString());
        return list;
    }


}
