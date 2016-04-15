package com.hzih.community.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityBuildDao;
import com.hzih.community.domain.CommunityBuild;
import com.hzih.community.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class CommunityBuildDaoImpl extends MyDaoSupport implements CommunityBuildDao {

    @Override
    public void setEntityClass() {
        this.entityClass = CommunityBuild.class;
    }

    @Override
    public PageResult find(int start, int limit,String community_id,String value) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from CommunityBuild s where 1=1");
        List params = new ArrayList();// 手动指定容量，避免多次扩容
        if (StringUtils.isNotBlank(community_id)) {
            sb.append(" and s.community.id = ?");
            params.add(Long.parseLong(community_id));
        }
        if (StringUtils.isNotBlank(value)) {
            sb.append(" and s.value like ?");
            params.add("%"+value+"%");
        }
        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params.toArray(), pageIndex, limit);
        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                + ps.getResults().size());
        return ps;
    }

    @Override
    public List<CommunityBuild> find(String community_id,String value) {
        StringBuffer sb = new StringBuffer(" from CommunityBuild s where 1=1");
        if (StringUtils.isNotBlank(community_id)) {
            sb.append(" and s.community.id = " + community_id);
        }
        if (StringUtils.isNotBlank(value)) {
            sb.append(" and s.value = " + value);
        }
        List<CommunityBuild> list = getHibernateTemplate().find(sb.toString());
        return list;
    }

    @Override
    public PageResult find(int start, int limit,String community_id) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from CommunityBuild s where 1=1");
        List params = new ArrayList();// 手动指定容量，避免多次扩容
        if (StringUtils.isNotBlank(community_id)) {
            sb.append(" and s.community.id = ?");
            params.add(Long.parseLong(community_id));
        }
        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params.toArray(), pageIndex, limit);
        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                + ps.getResults().size());
        return ps;
    }

    @Override
    public CommunityBuild findById(long id) {
        String hql = new String("from CommunityBuild where id="+id);
        List<CommunityBuild> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void remove(CommunityBuild community) {
        getHibernateTemplate().delete(community);
    }
}
